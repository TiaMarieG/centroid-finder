package io.github.TiaMarieG.centroidFinder;

import org.junit.jupiter.api.Test;

import java.awt.Color;
import java.io.File;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class VideoProcessingIntegrationTest {

   @Test
   void testVideoCentroidDetectionGeneratesCSV() throws Exception {
      // Arrange
      File inputVideo = new File("videos/cb_bg.mp4");
      assertTrue(inputVideo.exists(), "cb_bg.mp4 should exist in videos/");

      String outputCsv = "test_output/cb_bg_centroids.csv";
      Color targetColor = Color.decode("#FFFFFF"); // set to desired target
      int threshold = 60;

      // Act
      new VideoFrameRunner(inputVideo, outputCsv, targetColor, threshold).run();

      // Assert
      File csvFile = new File(outputCsv);
      assertTrue(csvFile.exists(), "Output CSV should be created");

      List<String> lines = Files.readAllLines(csvFile.toPath());
      assertTrue(lines.size() > 1, "CSV should have multiple lines");
      assertEquals("timestamp,x,y", lines.get(0), "First line should be CSV header");

      // Optional: check a known frame if you know expected coordinates
      // assertTrue(lines.get(1).matches("0\\.000,\\d+,-?\\d+"));
   }
}
