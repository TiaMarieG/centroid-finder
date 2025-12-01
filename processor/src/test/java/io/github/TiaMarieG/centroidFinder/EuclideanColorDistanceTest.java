package io.github.TiaMarieG.centroidFinder;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;


public class EuclideanColorDistanceTest {
    EuclideanColorDistance ecd = new EuclideanColorDistance();

    @Test
    public void testDistance_sameColor() {
        double result = ecd.distance(0x112233, 0x112233);
        assertEquals(0.0, result, 0.0001);
    }

    @Test
    public void testDistance_blackAndWhite() {
        double result = ecd.distance(0x000000, 0xFFFFFF);
        // Expected: sqrt(255^2 + 255^2 + 255^2) = sqrt(3 * 65025) = sqrt(195075) ≈ 441.67
        assertEquals(441.67, result, 0.01);
    }

    @Test
    public void testDistance_redToGreen() {
        double result = ecd.distance(0xFF0000, 0x00FF00);
        // Red = (255,0,0), Green = (0,255,0)
        // Distance = sqrt(255^2 + 255^2) = sqrt(130050) ≈ 360.62
        assertEquals(360.62, result, 0.01);
    }

    @Test
    public void testDistance_randomColors() {
        double result = ecd.distance(0x123456, 0x654321);
        int[] c1 = ecd.convertColor(0x123456); // [18, 52, 86]
        int[] c2 = ecd.convertColor(0x654321); // [101, 67, 33]
        double expected = Math.sqrt(
            Math.pow(c1[0] - c2[0], 2) +
            Math.pow(c1[1] - c2[1], 2) +
            Math.pow(c1[2] - c2[2], 2)
        );
        assertEquals(expected, result, 0.001);
    }

    @Test
    public void testconverColor_black() {
        int[] rgb = EuclideanColorDistance.convertColor(0x000000);
        assertArrayEquals(new int[]{0, 0, 0}, rgb);
    }

    @Test
    public void testconvertColor_white() {
        int[] rgb = EuclideanColorDistance.convertColor(0xFFFFFF);
        assertArrayEquals(new int[]{255, 255, 255}, rgb);
    }

    @Test
    public void testConvertColor_red() {
        int[] rgb = EuclideanColorDistance.convertColor(0xFF0000);
        assertArrayEquals(new int[]{255, 0, 0}, rgb);
    }

    @Test
    public void testConvertColor_green() {
        int[] rgb = EuclideanColorDistance.convertColor(0x00FF00);
        assertArrayEquals(new int[]{0, 255, 0}, rgb);
    }

    @Test
    public void testConvertColor_blue() {
        int[] rgb = EuclideanColorDistance.convertColor(0x0000FF);
        assertArrayEquals(new int[]{0, 0, 255}, rgb);
    }

    @Test
    public void testConvertColor_randomColor() {
        int[] rgb = EuclideanColorDistance.convertColor(0x1A2B3C);
        assertArrayEquals(new int[]{26, 43, 60}, rgb);
    }
}