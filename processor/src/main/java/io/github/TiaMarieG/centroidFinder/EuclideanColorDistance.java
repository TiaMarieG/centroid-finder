package io.github.TiaMarieG.centroidFinder;
public class EuclideanColorDistance implements ColorDistanceFinder {
    /**
     * Returns the euclidean color distance between two hex RGB colors.
     * 
     * Each color is represented as a 24-bit integer in the form 0xRRGGBB, where
     * RR is the red component, GG is the green component, and BB is the blue component,
     * each ranging from 0 to 255.
     * 
     * The Euclidean color distance is calculated by treating each color as a point
     * in 3D space (red, green, blue) and applying the Euclidean distance formula:
     * 
     * sqrt((r1 - r2)^2 + (g1 - g2)^2 + (b1 - b2)^2)
     * 
     * This gives a measure of how visually different the two colors are.
     * 
     * @param colorA the first color as a 24-bit hex RGB integer
     * @param colorB the second color as a 24-bit hex RGB integer
     * @return the Euclidean distance between the two colors
     */
    @Override
    public double distance(int colorA, int colorB) {
        int[] colorOne = convertColor(colorA);
        int[] colorTwo = convertColor(colorB);

        int redOne = colorOne[0];
        int greenOne = colorOne[1];
        int blueOne = colorOne[2];

        int redTwo = colorTwo[0];
        int greenTwo = colorTwo[1];
        int blueTwo = colorTwo[2];
        
        return Math.sqrt(Math.pow((redOne - redTwo), 2) + Math.pow((greenOne - greenTwo),2) + Math.pow((blueOne - blueTwo),2));
    }

    public static int[] convertColor(int color) {
        // Shifting the bits to the appropriate byte
        // Then masking it to get the last 8 bits/byte of data to get the number for the R, G, B respectively
        int red = (color >> 16) & 0xFF;
        int green = (color >> 8) & 0xFF;
        int blue = color & 0xFF;

        return new int[]{red, green, blue};
    }
}
