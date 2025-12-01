package io.github.TiaMarieG.centroidFinder;

import java.awt.image.BufferedImage;
import java.util.List;

public class CentroidFinderPerFrame {

    private final ImageBinarizer binarizer;
    private final BinaryGroupFinder groupFinder;

    public CentroidFinderPerFrame(ImageBinarizer binarizer, BinaryGroupFinder groupFinder) {
        this.binarizer = binarizer;
        this.groupFinder = groupFinder;
    }

    public List<Group> findGroups(BufferedImage image) {
    int[][] binary = binarizer.toBinaryArray(image);
    return groupFinder.findConnectedGroups(binary);
}

    // Existing record type (used for batch export, not frame-by-frame use)
    public record DataPoint(int frame, int x, int y) {}
}
