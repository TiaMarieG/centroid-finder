package io.github.TiaMarieG.centroidFinder;

import java.nio.file.Paths;

import org.bytedeco.javacv.FFmpegFrameGrabber;

public class FPSFinder {

    /*
     * Exports the framerate per second of an mp4 file.
     * Prints the complete stack trace if there is an error.
     * 
     * @param videoPath String value of the path of the given video
     * 
     */
    public double FPSReader(String videoPath) {
        double fps = 0.0;
        // Using getFrameRate to return teh videos frames per second
        try (FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(videoPath)) {
            grabber.start();
            fps = grabber.getFrameRate();
            grabber.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fps;
    }

    /*
     * Exports the total frames a mp4 file has.
     * Prints the complete stack trace if there is an error.
     * 
     * @param videoPath String value of the path of the given video
     * 
     */
    public int totalFramesOfVideo(String videoPath) {
        int totalFrames = 0;

        // Using getLengthInFrames to access the amount of frames in the file
        try (FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(videoPath)) {
            grabber.start();
            totalFrames = grabber.getLengthInFrames();
            grabber.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return totalFrames;
    }

    /*
     * Exports the total frames a mp4 file has.
     * Prints the complete stack trace if there is an error.
     * 
     * @param videoPath String value of the path of the given video
     * 
     */
    public double lengthOfVideo(String videoPath) {
        double totalLength = 0;

        // Using getLengthInFrames to access the amount of frames in the file
        try (FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(videoPath)) {
            grabber.start();
            totalLength = grabber.getLengthInTime() / 1_000_000.0;
            grabber.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return totalLength;
    }

    public static int findFPS(FFmpegFrameGrabber grabber) {
        return (int) grabber.getFrameRate();
    }
}
