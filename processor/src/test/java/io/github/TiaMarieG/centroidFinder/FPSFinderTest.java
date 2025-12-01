package io.github.TiaMarieG.centroidFinder;

import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.junit.jupiter.api.Test;

public class FPSFinderTest {
    @Test
    public void testFPSReaderWithValidVideo() {
        // Build the relative path to the video
        String videoPath = Paths.get("videos", "nyan-cat.mp4").toString();

        FPSFinder finder = new FPSFinder();
        double fps = finder.FPSReader(videoPath);

        // Verify FPS is correct for a typical video
        assertTrue(fps == 25.0, "FPS is correct.");
        System.out.println(fps + " Frames Per Second");
    }

    @Test
    public void testFPSReaderWithInvalidPath() {
        String invalidPath = "videos/non_existent_video.mp4";
        FPSFinder finder = new FPSFinder();
        double fps = finder.FPSReader(invalidPath);

        // Should return 0.0 if file is not found or readable
        assertEquals(0.0, fps, "FPS should be 0.0 for an invalid file path.");
    }

    @Test
    public void testGetTotalFramesWithValidVideo() {
        // Path to the test video
        String videoPath = Paths.get("videos", "nyan-cat.mp4").toString();
        

        FPSFinder finder = new FPSFinder();
        int totalFrames = finder.totalFramesOfVideo(videoPath);

        // We expect some frames in a real video file
        System.out.println("Total Frames: " + totalFrames);
        assertTrue(totalFrames == 5421, "Total frames should be greater than 0 for a valid video.");
    }

    @Test
    public void testGetTotalFramesWithInvalidPath() {
        // Invalid or non-existent file path
        String invalidPath = "videos/invalid_file.mp4";

        FPSFinder finder = new FPSFinder();
        int totalFrames = finder.totalFramesOfVideo(invalidPath);

        // Should return 0 on failure
        assertEquals(0, totalFrames, "Total frames should be 0 for an invalid video path.");
    }

     @Test
    public void testGetTotalFramesWithSalamander() {
        // Path to the test video
        String videoPath = Paths.get("videos", "ensantina.mp4").toString();
        

        FPSFinder finder = new FPSFinder();
        int totalFrames = finder.totalFramesOfVideo(videoPath);

        double expected = finder.FPSReader(videoPath) * finder.lengthOfVideo(videoPath);
        // We expect some frames in a real video file
        System.out.println("Total Frames: " + totalFrames);
        System.out.println(expected);
        assertTrue(totalFrames > 0, "Total frames should be greater than 0 for a valid video.");
    }

    @Test
    public void testFPSReaderWithSalamandar() {
        // Build the relative path to the video
        String videoPath = Paths.get("videos", "ensantina.mp4").toString();

        FPSFinder finder = new FPSFinder();
        double fps = finder.FPSReader(videoPath);

        fps = Math.round(fps * Math.pow(10, 2)) / Math.pow(10, 2);
        

        // Verify FPS is correct for a typical video
        System.out.println(fps + " Frames Per Second");
        assertTrue(fps == 23.98, "FPS is correct.");
    }
    
}
