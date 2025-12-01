package io.github.TiaMarieG.centroidFinder;

import java.io.File;
import java.io.IOException;

public class PathHandler {

   /**
    * Validates the input video path. Checks that the file exists and is a file
    * (not a directory).
    * 
    * @param inputPath the path to the video file
    * @return the File object if valid; null otherwise
    */
   public static File validateInputPath(String inputPath) {
      File file = new File(inputPath);
      if (!file.exists() || !file.isFile()) {
         System.err.println("Error: Input video file does not exist or is not a valid file.");
         return null;
      }
      return file;
   }

   /**
    * Ensures the output directory exists for the output CSV path. Creates parent
    * directories if needed.
    * 
    * @param outputCsv the path to the CSV output file
    */
   public static void ensureOutputPath(String outputCsv) {
      File outFile = new File(outputCsv);
      File parentDir = outFile.getParentFile();

      if (parentDir != null && !parentDir.exists()) {
         boolean created = parentDir.mkdirs();
         if (!created) {
            System.err.println("Warning: Could not create output directory: " + parentDir.getAbsolutePath());
         }
      }

      try {
         if (!outFile.exists()) {
            outFile.createNewFile(); // Optional — ensures the file exists ahead of time
         }
      } catch (IOException e) {
         System.err.println("Error: Could not create output CSV file.");
         e.printStackTrace();
      }
   }
}
