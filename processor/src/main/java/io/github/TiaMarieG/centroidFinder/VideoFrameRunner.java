package io.github.TiaMarieG.centroidFinder;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.Frame;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

public class VideoFrameRunner {
   private final File videoFile;
   private final String outputCsv;
   private final Color targetColor;
   private final int threshold;

   public VideoFrameRunner(File videoFile, String outputCsv, Color targetColor, int threshold) {
      this.videoFile = videoFile;
      this.outputCsv = outputCsv;
      this.targetColor = targetColor;
      this.threshold = threshold;
   }

   public void run() {
      try (FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(videoFile)) {
         grabber.start();
         int fps = FPSFinder.findFPS(grabber);
         double secondsPerFrame = 1.0 / fps;
         int frameNumber = 0;

         try (Java2DFrameConverter converter = new Java2DFrameConverter()) {
            // Convert targetColor to int for DistanceImageBinarizer
            int colorAsInt = targetColor.getRGB();
            ColorDistanceFinder distanceFinder = new EuclideanColorDistance();
            DistanceImageBinarizer binarizer = new DistanceImageBinarizer(distanceFinder, colorAsInt, threshold);
            BinaryGroupFinder groupFinder = new DfsBinaryGroupFinder();

            CentroidFinderPerFrame frameProcessor = new CentroidFinderPerFrame(binarizer, groupFinder);

            CentroidCoordsPerFrame centroidProcessor = new CentroidCoordsPerFrame(frameProcessor);

            CsvLogger logger = new CsvLogger(outputCsv);
            logger.writeHeader();

            Frame frame;
            double lastLoggedSecond = -1.0;

            while ((frame = grabber.grabImage()) != null) {
               BufferedImage image = converter.convert(frame);
               Coordinate centroid = centroidProcessor.findCentroid(image);
               double timestamp = frameNumber * secondsPerFrame;

               // Only log when full second has passed
               if (timestamp - lastLoggedSecond >= 1.0) {
                  logger.write(timestamp, centroid.x(), centroid.y());
                  lastLoggedSecond = timestamp;
               }

               frameNumber++;
            }

            logger.close();
         }

         grabber.stop();
         System.out.println("Video processed successfully. Output saved to " + outputCsv);

      } catch (Exception e) {
         System.err.println("Error during video processing: " + e.getMessage());
         e.printStackTrace();
      }
   }
}
