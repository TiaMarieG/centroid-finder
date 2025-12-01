package io.github.TiaMarieG.centroidFinder;

import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CentroidCoordsPerFrameTest {

    /**
     * DummyCentroidFinder returns no groups (simulates a frame with no matches)
     */
    static class DummyCentroidFinder extends CentroidFinderPerFrame {
        public DummyCentroidFinder() {
            super(null, null);
        }

        @Override
        public List<Group> findGroups(BufferedImage image) {
            return Collections.emptyList(); // No groups detected
        }
    }

    @Test
    void testReturnsNegativeOneIfNoGroupsFound() {
        // Arrange
        BufferedImage blankImage = new BufferedImage(10, 10, BufferedImage.TYPE_3BYTE_BGR);
        CentroidCoordsPerFrame processor = new CentroidCoordsPerFrame(new DummyCentroidFinder());

        // Act
        Coordinate result = processor.findCentroid(blankImage);

        // Assert
        assertEquals(-1, result.x());
        assertEquals(-1, result.y());
    }
}
