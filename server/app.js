import express from "express";
import dotenv from "dotenv";
import path from "path";
import route from "./routers/router.js";
import cors from "cors";

dotenv.config();

const app = express();

const VIDEO_DIR = process.env.VIDEO_DIR;
const OUTPUT_DIR = process.env.OUTPUT_DIR;

app.use(cors());
app.use(express.json());

// Mount main API routes
app.use("/", route);

// Serve static results (CSV outputs)
const outputDir = process.env.OUTPUT_DIR || path.resolve("output");
app.use("/results", express.static(outputDir));

// Serve static videos
const videoDir = process.env.VIDEO_DIR || path.resolve("videos");
app.use("/videos", express.static(videoDir));

// Serve generated thumbnails
const thumbnailDir = path.resolve("thumbnails");
app.use("/thumbnails", express.static(thumbnailDir));

export default app;