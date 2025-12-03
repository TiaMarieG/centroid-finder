import { describe, it, expect, vi, beforeEach } from "vitest";
import * as fs from "fs";
import * as child_process from "child_process";
import { processVid, jobStatus } from "../controllers/controller.js";

process.env.VIDEO_DIR = "/mock/videos";
process.env.OUTPUT_DIR = "/mock/output";
process.env.JAR_PATH = "/mock/analyzer.jar";

vi.mock("fs", () => ({
  existsSync: vi.fn(() => false),
  mkdirSync: vi.fn(),
  statSync: vi.fn(),
}));
vi.mock("child_process", () => ({
  spawn: vi.fn(() => ({ unref: vi.fn() })),
}));

function mockRes() {
  return {
    status: vi.fn().mockReturnThis(),
    json: vi.fn(),
    sendFile: vi.fn(),
    set: vi.fn(),
  };
}

describe("controller.js", () => {
  beforeEach(() => {
    vi.clearAllMocks();
    jobStatus.clear();
  });

  it("spawns java and creates directories", () => {
    const req = {
      params: {
        file: "test.mp4",
      },
      query: {
        targetColor: "255,0,0",
        threshold: 95,
      },
    };
    const res = mockRes();

    processVid(req, res);

    expect(child_process.spawn).toHaveBeenCalled();
    expect(fs.mkdirSync).toHaveBeenCalledWith("/mock/output", { recursive: true });
    expect(res.json).toHaveBeenCalledWith(
      expect.objectContaining({ jobId: expect.any(String) })
    );
  });
});