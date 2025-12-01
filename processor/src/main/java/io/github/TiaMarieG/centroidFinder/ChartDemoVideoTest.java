package io.github.TiaMarieG.centroidFinder;

import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.javacv.*;
import org.bytedeco.opencv.global.opencv_core;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.opencv_core.Mat;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ChartDemoVideoTest {
    public static void main(String[] args) {
        String videoPath = "videos/nyan-cat.mp4"; // **Adjust path as needed**
        String outputFolder = "frames_output";

        new File(outputFolder).mkdirs();

        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(videoPath);
        OpenCVFrameConverter.ToMat converter = new OpenCVFrameConverter.ToMat(); // **Efficient converter**

        try {
            grabber.start();
            int totalFrames = grabber.getLengthInFrames();
            double frameRate = grabber.getFrameRate();
            System.out.println("Total frames: " + totalFrames);
            System.out.println("Frame rate: " + frameRate);

            for (int i = 0; i < totalFrames; i++) {
                if (i % 100 == 0) {
                    Frame frame = grabber.grabFrame();
                    if (frame != null && frame.image != null) {
                        Mat mat = converter.convert(frame); // Convert Frame to Mat

                        String filename = outputFolder + File.separator + "frame_" + i + ".png";
                        boolean result = opencv_imgcodecs.imwrite(filename, mat);

                        if (result) {
                            System.out.println("Saved frame " + i + " as " + filename);
                        } else {
                            System.out.println("Failed to save frame " + i);
                        }
                    } else {
                        System.out.println("Frame " + i + " is empty or corrupted.");
                    }
                } else {
                    grabber.grabFrame();
                }
            }

            grabber.stop();
        } catch (FrameGrabber.Exception e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace(); // Handle potential IOExceptions
        }
    }
}