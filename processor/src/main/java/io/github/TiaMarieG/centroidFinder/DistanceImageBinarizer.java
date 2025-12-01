package io.github.TiaMarieG.centroidFinder;
import java.awt.image.BufferedImage;

/**
 * An implementation of the ImageBinarizer interface that uses color distance
 * to determine whether each pixel should be black or white in the binary image.
 * 
 * The binarization is based on the Euclidean distance between a pixel's color and a reference target color.
 * If the distance is less than the threshold, the pixel is considered white (1);
 * otherwise, it is considered black (0).
 * 
 * The color distance is computed using a provided ColorDistanceFinder, which defines how to compare two colors numerically.
 * The targetColor is represented as a 24-bit RGB integer in the form 0xRRGGBB.
 */
public class DistanceImageBinarizer implements ImageBinarizer {
    private final ColorDistanceFinder distanceFinder;
    private final int threshold;
    private final int targetColor;

    /**
     * Constructs a DistanceImageBinarizer using the given ColorDistanceFinder,
     * target color, and threshold.
     * 
     * The distanceFinder is used to compute the Euclidean distance between a pixel's color and the target color.
     * The targetColor is represented as a 24-bit hex RGB integer (0xRRGGBB).
     * The threshold determines the cutoff for binarization: pixels with distances less than
     * the threshold are marked white, and others are marked black.
     *
     * @param distanceFinder an object that computes the distance between two colors
     * @param targetColor the reference color as a 24-bit hex RGB integer (0xRRGGBB)
     * @param threshold the distance threshold used to decide whether a pixel is white or black
     */
    public DistanceImageBinarizer(ColorDistanceFinder distanceFinder, int targetColor, int threshold) {
        this.distanceFinder = distanceFinder;
        this.targetColor = targetColor;
        this.threshold = threshold;
    }

    /**
     * Converts the given BufferedImage into a binary 2D array using color distance and a threshold.
     * Each entry in the returned array is either 0 or 1, representing a black or white pixel.
     * A pixel is white (1) if its Euclidean distance to the target color is less than the threshold.
     *
     * @param image the input RGB BufferedImage
     * @return a 2D binary array where 1 represents white and 0 represents black
     */
    @Override
    public int[][] toBinaryArray(BufferedImage image) {
        if (image == null) throw new NullPointerException("Image is null");
        
        // Using getWidth() and getHeight() to get the respected size of image to populate a 2d array
        int[][] bArray = new int[image.getHeight()][image.getWidth()];
        
        for (int r = 0; r < bArray.length; r++) {
            for (int c = 0; c < bArray[r].length; c++) {
                // As we are looping over the array, we save the pixel at the point at row and column
                int pixel = image.getRGB(c, r);

                // Using EuclideanColorDistance, we compare the target and the pixel
                double distance = distanceFinder.distance(pixel, targetColor);

                // We find whether we set the point to 0/black or 1/white
                if (distance < threshold) {
                    bArray[r][c] = 1;
                } else {
                    bArray[r][c] = 0;
                }
            }
        }
        return bArray;
    }

    /**
     * Converts a binary 2D array into a BufferedImage.
     * Each value should be 0 (black) or 1 (white).
     * Black pixels are encoded as 0x000000 and white pixels as 0xFFFFFF.
     *
     * @param image a 2D array of 0s and 1s representing the binary image
     * @return a BufferedImage where black and white pixels are represented with standard RGB hex values
     */
    @Override
    public BufferedImage toBufferedImage(int[][] image) {
        if (image == null) throw new NullPointerException("Image array is null.");
        if (image.length == 0) throw new IllegalArgumentException("Image array is null.");
        
        // Creating a BufferedImage with proportional height and width to image array
        // Using BufferedImage.TYPE_INT_RGB represents an image with 8 bit rgb color components, that we will reassign with setRGB
        BufferedImage monochrome = new BufferedImage(image[0].length, image.length, BufferedImage.TYPE_INT_RGB);

        for (int r = 0; r < image.length; r++) {
            for (int c = 0; c < image[r].length; c++) {
                // As we go through the image array we reassign the rgb using set at the point of the array
                if (image[r][c] == 1){
                    // White
                    monochrome.setRGB(c, r, 0xffffff);
                } else {
                    // Black
                    monochrome.setRGB(c, r, 0x000000);
                }
            }
        }
        return monochrome;
    }
}
