package io.github.TiaMarieG.centroidFinder;

import java.awt.Color;
import java.io.File;

public class VideoProcessor {
   public static void main(String[] args) {
      if (args.length != 4) {
         System.err.println("Usage: java -jar videoprocessor.jar inputPath outputCsv targetColor threshold");
         System.exit(1);
      }

      String inputPath = args[0];
      String outputCsv = args[1];
      String colorInput = args[2];
      int threshold = parseThreshold(args[3]);

      // Validate paths
      File videoFile = PathHandler.validateInputPath(inputPath);
      PathHandler.ensureOutputPath(outputCsv);

      // Parse color
      Color targetColor = ColorParser.parse(colorInput);

      // Abort if any input is invalid
      if (videoFile == null || targetColor == null || threshold < 0) {
         System.exit(1);
      }

      // Run processing
      new VideoFrameRunner(videoFile, outputCsv, targetColor, threshold).run();
   }

   private static int parseThreshold(String value) {
      try {
         return Integer.parseInt(value);
      } catch (NumberFormatException e) {
         System.err.println("Threshold must be an integer.");
         return -1;
      }
   }
}
