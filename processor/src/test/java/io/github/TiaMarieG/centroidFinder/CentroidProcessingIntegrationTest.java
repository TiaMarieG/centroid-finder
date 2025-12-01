package io.github.TiaMarieG.centroidFinder;

import org.junit.jupiter.api.Test;

import java.awt.Color;
import java.io.File;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CentroidProcessingIntegrationTest {

    @Test
    void testFullPipelineWithCbBgVideo() throws Exception {
        // Arrange
        File inputVideo = new File("videos/cb_bg.mp4");
        assertTrue(inputVideo.exists(), "cb_bg.mp4 should exist in videos/");

        String outputCsv = "test_output/cb_bg_centroids_full_pipeline.csv";
        Color targetColor = Color.WHITE;
        int threshold = 100;

        // Act
        new VideoFrameRunner(inputVideo, outputCsv, targetColor, threshold).run();

        // Assert
        File csvFile = new File(outputCsv);
        assertTrue(csvFile.exists(), "CSV file should be created");

        List<String> lines = Files.readAllLines(csvFile.toPath());
        assertTrue(lines.size() > 1, "CSV should contain centroid data");
        assertEquals("timestamp,x,y", lines.get(0), "CSV should contain header");
    }
}
