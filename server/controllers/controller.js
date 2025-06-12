// controller.js
import { spawn } from "child_process";
import { v4 as uuid } from "uuid";
import * as fs from "fs";
import path, { dirname } from "path";
import ffmpegPath from "ffmpeg-static";
import { fileURLToPath } from "url";

const __filename = fileURLToPath(import.meta.url);
const __dirname = dirname(__filename);

export const jobStatus = new Map();

export const processVid = (req, res) => {
   const { file } = req.params;
   const { targetColor, threshold } = req.query;

   if (!targetColor || !threshold) {
      return res.status(400).json({
         error: "Missing targetColor or threshold query parameter.",
      });
   }

   const inputPath = path.join(process.env.VIDEO_DIR || "videos", file);
   const jobId = uuid();
   const outputDir = process.env.OUTPUT_DIR || path.join(__dirname, "output");
   const outputFile = `${jobId}.csv`;
   const outputPath = path.join(outputDir, outputFile);

   if (!fs.existsSync(outputDir)) {
      fs.mkdirSync(outputDir, { recursive: true });
   }

   try {
      const jar = spawn(
         "java",
         [
            "-jar",
            process.env.JAR_PATH,
            inputPath,
            outputPath,
            targetColor,
            threshold.toString(),
         ],
         {
            detached: true,
            stdio: "inherit",
         }
      );

      if (jar && typeof jar.unref === "function") jar.unref();

      jobStatus.set(jobId, {
         status: "processing",
         output: outputFile,
      });

      monitorJob(jobId, outputPath);

      return res.status(202).json({ jobId });
   } catch (err) {
      console.error("Error starting job:", err);
      return res.status(500).json({ error: "Error starting job" });
   }
};

export const getJobStatus = (req, res) => {
   try {
      const job = jobStatus.get(req.params.jobId);

      if (!job) {
         return res.status(404).json({ error: "Job ID not found" });
      }

      const { status, output, error } = job;

      if (status === "processing") {
         return res.status(200).json({ status: "processing" });
      }

      if (status === "done") {
         const chartFile = output.replace(".csv", "_chart.png");
         return res.status(200).json({
            status: "done",
            result: `/results/${output}`,
            chart: `/downloads/${chartFile}`,
         });
      }

      if (status === "error") {
         return res.status(200).json({ status: "error", error });
      }

      return res.status(500).json({ error: "Unknown job status" });
   } catch (err) {
      console.error("Job status error:", err);
      return res.status(500).json({ error: "Error fetching job status" });
   }
};

function monitorJob(jobId, outputPath, timeout = 600000) {
   const start = Date.now();

   const interval = setInterval(() => {
      if (Date.now() - start > timeout) {
         jobStatus.set(jobId, {
            status: "error",
            error: "Job timed out or did not produce output",
         });
         clearInterval(interval);
         return;
      }

      if (fs.existsSync(outputPath)) {
         const stats = fs.statSync(outputPath);
         const now = Date.now();
         const ageMs = now - stats.mtimeMs;

         if (ageMs > 10000) {
            const chartProcess = spawn(
               "java",
               [
                  "-cp",
                  process.env.JAR_PATH,
                  "io.github.TiaMarieG.centroidFinder.CentroidPlotter",
                  path.resolve(outputPath),
               ],
               {
                  detached: true,
                  stdio: ["ignore", "pipe", "pipe"],
               }
            );

            chartProcess.stdout.on("data", (data) => {
               process.stdout.write(data);
            });

            chartProcess.stderr.on("data", (data) => {
               process.stderr.write(data);
            });

            if (chartProcess && typeof chartProcess.unref === "function") {
               chartProcess.unref();
            }

            jobStatus.set(jobId, {
               status: "done",
               output: path.basename(outputPath),
            });
            clearInterval(interval);
         }
      }
   }, 1000);
}

export const getJobs = (req, res) => {
   const jobs = [];
   for (const [jobId, data] of jobStatus.entries()) {
      jobs.push({ jobId, ...data });
   }
   res.status(200).json(jobs);
};

export const videos = (req, res) => {
   const videoDir = process.env.VIDEO_DIR || "videos";

   fs.readdir(videoDir, (err, files) => {
      if (err) {
         console.error("Cannot read from video directory: ", err);
         return res.status(500).json({ error: "Cannot read from video directory" });
      }

      const videoFiles = files.filter(
         (file) =>
            file.endsWith(".mp4") || file.endsWith(".mov") || file.endsWith(".mkv")
      );
      res.status(200).json(videoFiles);
   });
};

export const thumbnail = (req, res) => {
   const { filename } = req.params;
   const baseFilename = path.parse(filename).name;

   const videoPath = path.join(process.env.VIDEO_DIR || "videos", filename);
   const thumbnailDir = path.join("thumbnails");
   const thumbnailFile = `${baseFilename}.jpg`;
   const thumbnailPath = path.join(thumbnailDir, thumbnailFile);

   if (!fs.existsSync(videoPath)) {
      return res.status(404).json({ error: "Video not found" });
   }

   if (!fs.existsSync(thumbnailDir)) {
      fs.mkdirSync(thumbnailDir, { recursive: true });
   }

   if (fs.existsSync(thumbnailPath)) {
      res.set("Content-Type", "image/jpeg");
      return fs.createReadStream(thumbnailPath).pipe(res);
   }

   const ffmpeg = spawn(ffmpegPath, [
      "-y",
      "-i",
      videoPath,
      "-ss",
      "00:00:01",
      "-vframes",
      "1",
      thumbnailPath,
   ]);

   ffmpeg.on("close", (code) => {
      if (code === 0 && fs.existsSync(thumbnailPath)) {
         res.set("Content-Type", "image/jpeg");
         fs.createReadStream(thumbnailPath).pipe(res);
      } else {
         console.error("FFmpeg failed or thumbnail not created");
         res.status(500).json({ error: "Failed to generate thumbnail" });
      }
   });

   ffmpeg.on("error", (err) => {
      console.error("FFmpeg spawn error:", err);
      res.status(500).json({ error: "Failed to spawn ffmpeg" });
   });
};

export const generateCsv = (req, res) => {
   const { filename, color, threshold } = req.body;

   if (!filename || !color || isNaN(threshold)) {
      return res.status(400).json({ error: "Missing input data" });
   }

   const inputPath = path.join(process.env.VIDEO_DIR || "videos", filename);
   const jobId = uuid();
   const outputDir = process.env.OUTPUT_DIR || path.join(__dirname, "output");
   const outputFile = `${jobId}.csv`;
   const outputPath = path.join(outputDir, outputFile);

   if (!fs.existsSync(outputDir)) {
      fs.mkdirSync(outputDir, { recursive: true });
   }

   try {
      const jar = spawn(
         "java",
         [
            "-jar",
            process.env.JAR_PATH,
            inputPath,
            outputPath,
            `${color.r},${color.g},${color.b}`,
            threshold.toString(),
         ],
         {
            detached: true,
            stdio: ["ignore", "pipe", "pipe"],
         }
      );

      jar.stdout?.on("data", (data) => process.stdout.write(data));
      jar.stderr?.on("data", (data) => process.stderr.write(data));
      if (jar && typeof jar.unref === "function") jar.unref();

      jobStatus.set(jobId, {
         status: "processing",
         output: outputFile,
      });

      monitorJob(jobId, outputPath);
      return res.status(202).json({ jobId });
   } catch (err) {
      console.error("Error starting CSV generation:", err);
      return res.status(500).json({ error: "CSV generation failed" });
   }
};