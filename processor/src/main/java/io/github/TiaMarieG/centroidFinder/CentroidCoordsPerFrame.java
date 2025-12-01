package io.github.TiaMarieG.centroidFinder;

import java.awt.image.BufferedImage;
import java.util.List;

public class CentroidCoordsPerFrame {

    private final CentroidFinderPerFrame frameProcessor;

    public CentroidCoordsPerFrame(CentroidFinderPerFrame frameProcessor) {
        this.frameProcessor = frameProcessor;
    }

    /**
     * Finds the centroid of the largest group in a binary image.
     * If no groups are found, returns (-1, -1).
     */
    public Coordinate findCentroid(BufferedImage image) {
        List<Group> groups = frameProcessor.findGroups(image);
        Group largest = selectLargest(groups);

        if (largest == null) {
            return new Coordinate(-1, -1);
        }
        return largest.centroid();
    }

    /**
     * Selects the group with the largest number of pixels from a list.
     */
    private Group selectLargest(List<Group> groups) {
        if (groups == null || groups.isEmpty()) return null;

        Group largest = groups.get(0);
        for (Group g : groups) {
            if (g.size() > largest.size()) {
                largest = g;
            }
        }
        return largest;
    }
}
