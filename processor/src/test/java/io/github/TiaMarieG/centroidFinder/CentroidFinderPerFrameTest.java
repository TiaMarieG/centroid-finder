package io.github.TiaMarieG.centroidFinder;

import org.junit.jupiter.api.Test;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CentroidFinderPerFrameTest {

    @Test
    void testFindGroupsAndLargestCentroid() {
        // Arrange: Create 100x100 image with two white squares
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = image.createGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 100, 100); // black background

        g.setColor(Color.WHITE);
        g.fillRect(10, 10, 10, 10);  // group 1 (10x10 = 100 pixels)
        g.fillRect(50, 50, 20, 20);  // group 2 (20x20 = 400 pixels)
        g.dispose();

        // Binarizer for white, threshold 10
        ColorDistanceFinder colorDistance = new EuclideanColorDistance();
        DistanceImageBinarizer binarizer = new DistanceImageBinarizer(colorDistance, Color.WHITE.getRGB(), 10);
        BinaryGroupFinder groupFinder = new DfsBinaryGroupFinder();
        CentroidFinderPerFrame processor = new CentroidFinderPerFrame(binarizer, groupFinder);

        // Act
        List<Group> groups = processor.findGroups(image);

        // Assert: Expecting 2 groups
        assertEquals(2, groups.size(), "Expected 2 distinct white pixel groups");

        Group largest = groups.stream().max((g1, g2) -> Integer.compare(g1.size(), g2.size())).orElse(null);
        assertNotNull(largest, "Largest group should not be null");

        // Validate largest group has correct size and approximate centroid
        assertTrue(largest.size() > 200, "Largest group should be the larger square");
        Coordinate centroid = largest.centroid();
        assertTrue(centroid.x() >= 55 && centroid.x() <= 65, "Centroid X should be near center of larger square");
        assertTrue(centroid.y() >= 55 && centroid.y() <= 65, "Centroid Y should be near center of larger square");
    }
}
