package io.github.TiaMarieG.centroidFinder;

import org.knowm.xchart.*;
import org.knowm.xchart.style.markers.SeriesMarkers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class CentroidPlotter {

   public static void main(String[] args) throws Exception {
      String csvPath = "output/ensantina_centroids.csv";

      List<Double> xData = new ArrayList<>();
      List<Double> yData = new ArrayList<>();

      System.out.println("Reading CSV: " + csvPath);

      try (BufferedReader reader = new BufferedReader(new FileReader(csvPath))) {
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

            double x = Double.parseDouble(parts[1]);
            double y = Double.parseDouble(parts[2]);

            if (x >= 0 && y >= 0) {
               xData.add(x);
               yData.add(y);
            }
         }
      }

      System.out.println("Loaded " + xData.size() + " centroid points");

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

      System.out.println("Displaying chart...");
      new SwingWrapper<>(chart).displayChart();
      System.out.println("Chart should now be visible.");
   }
}
