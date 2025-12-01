package io.github.TiaMarieG.centroidFinder;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class CsvLogger {
   private final PrintWriter writer;

   public CsvLogger(String outputCsvPath) throws IOException {
      this.writer = new PrintWriter(new FileWriter(outputCsvPath));
   }

   /**
    * Writes the CSV header: timestamp,x,y
    */
   public void writeHeader() {
      writer.println("timestamp,x,y");
   }

   /**
    * Writes one line of output: timestamp in seconds, x coordinate, y coordinate
    */
   public void write(double timestamp, int x, int y) {
      writer.printf("%.3f,%d,%d%n", timestamp, x, y);
   }

   /**
    * Closes the writer stream
    */
   public void close() {
      writer.close();
   }
}
