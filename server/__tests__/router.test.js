import { describe, it, expect, beforeEach, vi } from "vitest";
import express from "express";
import request from "supertest";

// ✅ Mock all controller methods BEFORE importing the router
vi.mock("../controllers/controller.js", () => ({
  processVid: vi.fn((req, res) => res.status(200).json({ mocked: true })),
  getJobStatus: vi.fn((req, res) => res.status(200).json({ status: "mock" })),
  getJobs: vi.fn((req, res) => res.status(200).json([])),
  videos: vi.fn((req, res) => res.status(200).json(["video1.mp4"])),
  thumbnail: vi.fn((req, res) => res.status(200).send("thumbnail")),
  jobStatus: new Map(),
}));

import router from "../routers/router.js"; // Must come after the mock

const app = express();
app.use(express.json());
app.use(router);

describe("router.js", () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  it("POST /process should call processVid", async () => {
    const response = await request(app)
      .post("/process")
      .send({ file: "dummy.mp4" });

    expect(response.status).toBe(200);
    expect(response.body).toEqual({ mocked: true });
  });

  it("GET /jobs should return a response", async () => {
    const res = await request(app).get("/jobs");
    expect(res.status).toBe(200);
    expect(res.body).toEqual([]);
  });

  it("GET /api/videos should return videos", async () => {
    const res = await request(app).get("/api/videos");
    expect(res.status).toBe(200);
    expect(res.body).toEqual(["video1.mp4"]);
  });

  it("GET /thumbnail/:filename should return a response", async () => {
    const res = await request(app).get("/thumbnail/test.jpg");
    expect(res.status).toBe(200);
    expect(res.text).toBe("thumbnail");
  });
});
