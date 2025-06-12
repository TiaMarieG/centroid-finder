package io.github.TiaMarieG.centroidFinder;

import org.knowm.xchart.*;
import org.knowm.xchart.style.markers.SeriesMarkers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class CentroidPlotter {

   public static void main(String[] args) throws Exception {
      String csvPath = args.length > 0 ? args[0] : "server/results/default.csv";
      File csvFile = new File(csvPath);

      System.out.println("CentroidPlotter started with: " + csvPath);
      System.out.println("CSV exists? " + csvFile.exists());
      System.out.println("Absolute path: " + csvFile.getAbsolutePath());

      List<Double> xData = new ArrayList<>();
      List<Double> yData = new ArrayList<>();

      try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
         String line;
         boolean firstLine = true;

         while ((line = reader.readLine()) != null) {
            if (firstLine) {
               firstLine = false;
               continue;
            }

            String[] parts = line.split(",");
            if (parts.length != 3)
               continue;

            try {
               double x = Double.parseDouble(parts[1]);
               double y = Double.parseDouble(parts[2]);

               if (x >= 0 && y >= 0) {
                  xData.add(x);
                  yData.add(y);
               }
            } catch (NumberFormatException e) {
               System.err.println("Skipping invalid line: " + line);
            }
         }
      }

      System.out.println("Loaded " + xData.size() + " centroid points");

      if (xData.isEmpty()) {
         System.out.println("No data to plot. Exiting.");
         return;
      }

      // Create chart
      XYChart chart = new XYChartBuilder()
            .width(800)
            .height(600)
            .title("Centroid Tracking - Salamander")
            .xAxisTitle("X")
            .yAxisTitle("Y")
            .build();

      chart.getStyler().setLegendVisible(false);
      chart.getStyler().setMarkerSize(6);
      chart.getStyler().setChartTitlePadding(15);
      chart.getStyler().setYAxisTitleVisible(true);
      chart.getStyler().setXAxisTitleVisible(true);
      chart.getStyler().setYAxisMin(0.0);
      chart.getStyler().setXAxisMin(0.0);

      XYSeries series = chart.addSeries("Centroid Path", xData, yData);
      series.setMarker(SeriesMarkers.CIRCLE);

      // Compute output path using same folder as CSV
      String csvFileName = csvFile.getName().replace(".csv", "");
      File parentDir = csvFile.getParentFile();
      File chartOutputFile = new File(parentDir, csvFileName + "_chart");

      System.out.println("Saving chart to: " + chartOutputFile.getAbsolutePath() + ".png");
      BitmapEncoder.saveBitmap(chart, chartOutputFile.getAbsolutePath(), BitmapEncoder.BitmapFormat.PNG);
      System.out.println("✅ Chart saved successfully.");
   }
}
