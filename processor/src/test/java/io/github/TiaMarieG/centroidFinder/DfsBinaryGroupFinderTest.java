package io.github.TiaMarieG.centroidFinder;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.List;

// Used AI to generate and fix tests for DfsBinaryGroupFinder.java
public class DfsBinaryGroupFinderTest {

    private DfsBinaryGroupFinder finder;

    @BeforeEach
    public void setUp() {
        finder = new DfsBinaryGroupFinder();
    }

    @Test
    public void testSinglePixelGroup() {
        int[][] image = {
                { 0, 0, 0 },
                { 0, 1, 0 },
                { 0, 0, 0 }
        };

        List<Group> groups = finder.findConnectedGroups(image);
        assertEquals(1, groups.size());
        Group group = groups.get(0);
        assertEquals(1, group.size());
        assertEquals(1, group.centroid().x());
        assertEquals(1, group.centroid().y());
    }

    @Test
    public void testNoGroups() {
        int[][] image = {
                { 0, 0 },
                { 0, 0 }
        };

        List<Group> groups = finder.findConnectedGroups(image);
        assertTrue(groups.isEmpty());
    }

    @Test
    public void testTwoDisconnectedGroups() {
        int[][] image = {
            { 1, 0, 0 },
            { 0, 0, 1 }
        };
    
        List<Group> groups = finder.findConnectedGroups(image);
        assertEquals(2, groups.size());
    
        Group g1 = groups.get(0); // (2,1) comes first in descending order
        Group g2 = groups.get(1); // (0,0) comes second
    
        assertEquals(1, g1.size());
        assertEquals(2, g1.centroid().x());
        assertEquals(1, g1.centroid().y());
    
        assertEquals(1, g2.size());
        assertEquals(0, g2.centroid().x());
        assertEquals(0, g2.centroid().y());
    }
    

    @Test
    public void testOneLargeGroup() {
        int[][] image = {
                { 1, 1 },
                { 1, 1 }
        };

        List<Group> groups = finder.findConnectedGroups(image);
        assertEquals(1, groups.size());

        Group group = groups.get(0);
        assertEquals(4, group.size());
        assertEquals(0, group.centroid().x()); // x = (0+1+0+1)/4 = 0
        assertEquals(0, group.centroid().y()); // y = (0+0+1+1)/4 = 0
    }

    @Test
    public void testDiagonalDisconnected() {
        int[][] image = {
            { 1, 0 },
            { 0, 1 }
        };
    
        List<Group> groups = finder.findConnectedGroups(image);
        assertEquals(2, groups.size());
    
        Group g1 = groups.get(0); // (1,1) appears first in descending order
        Group g2 = groups.get(1); // (0,0) comes second
    
        assertEquals(1, g1.size());
        assertEquals(1, g1.centroid().x());
        assertEquals(1, g1.centroid().y());
    
        assertEquals(1, g2.size());
        assertEquals(0, g2.centroid().x());
        assertEquals(0, g2.centroid().y());
    }
    

    @Test
    public void testHorizontalLine() {
        int[][] image = {
            { 1, 1, 1, 1 }
        };
    
        List<Group> groups = finder.findConnectedGroups(image);
        Group group = groups.get(0);
    
        assertEquals(4, group.size());
        assertEquals(1, group.centroid().x()); // (0+1+2+3)/4 = 1
        assertEquals(0, group.centroid().y()); // All y's are 0 → centroid y = 0
    }
    

    @Test
    public void testVerticalLine() {
        int[][] image = {
                { 1 },
                { 1 },
                { 1 },
                { 1 }
        };

        Group group = finder.findConnectedGroups(image).get(0);
        assertEquals(4, group.size());
        assertEquals(0, group.centroid().x());
        assertEquals(1, group.centroid().y()); // (0+1+2+3)/4 = 1
    }

    @Test
    public void testMultipleEqualSizeGroupsSortedByCentroid() {
        int[][] image = {
            { 1, 0, 0, 1 },
            { 0, 0, 0, 0 },
            { 1, 0, 0, 1 }
        };
    
        List<Group> groups = finder.findConnectedGroups(image);
        assertEquals(4, groups.size());
    
        // Expecting descending order: (3,2), (3,0), (0,2), (0,0)
        Group g0 = groups.get(0);
        Group g3 = groups.get(3);
    
        assertEquals(1, g0.size());
        assertEquals(3, g0.centroid().x());
        assertEquals(2, g0.centroid().y());
    
        assertEquals(1, g3.size());
        assertEquals(0, g3.centroid().x());
        assertEquals(0, g3.centroid().y());
    }
    

    @Test
    public void testComplexShapeGroup() {
        int[][] image = {
                { 1, 1, 1 },
                { 1, 0, 1 },
                { 1, 1, 1 }
        };

        Group group = finder.findConnectedGroups(image).get(0);
        assertEquals(8, group.size());
        assertEquals(1, group.centroid().x()); // (sum of x) / 8 = 1
        assertEquals(1, group.centroid().y()); // (sum of y) / 8 = 1
    }

    @Test
    public void testZigZagGroup() {
        int[][] image = {
                { 1, 0, 1 },
                { 1, 1, 1 },
                { 1, 0, 1 }
        };

        Group group = finder.findConnectedGroups(image).get(0);
        assertEquals(7, group.size());
        assertEquals(1, group.centroid().x());
        assertEquals(1, group.centroid().y());
    }

    // 10 more variants...

    @Test
    public void testLShapedGroup() {
        int[][] image = {
                { 1, 0 },
                { 1, 0 },
                { 1, 1 }
        };
        Group group = finder.findConnectedGroups(image).get(0);
        assertEquals(4, group.size());
        assertEquals(0, group.centroid().x()); // (0+0+0+1)/4 = 0
        assertEquals(1, group.centroid().y()); // (0+1+2+2)/4 = 1
    }

    @Test
    public void testAllOnes() {
        int[][] image = {
                { 1, 1 },
                { 1, 1 }
        };
        Group group = finder.findConnectedGroups(image).get(0);
        assertEquals(4, group.size());
        assertEquals(0, group.centroid().x()); // (0+1+0+1)/4 = 0
        assertEquals(0, group.centroid().y()); // (0+0+1+1)/4 = 0
    }

    @Test
    public void testLongHorizontalWithBreak() {
        int[][] image = {
            { 1, 1, 0, 1, 1 }
        };
        List<Group> groups = finder.findConnectedGroups(image);
        assertEquals(2, groups.size());
    
        // First group has centroid x = 3
        assertEquals(2, groups.get(0).size());
        assertEquals(3, groups.get(0).centroid().x());
    
        // Second group has centroid x = 0
        assertEquals(2, groups.get(1).size());
        assertEquals(0, groups.get(1).centroid().x());
    }
    

    @Test
    public void testTwoVerticalColumns() {
        int[][] image = {
            { 1, 0, 1 },
            { 1, 0, 1 },
            { 1, 0, 1 }
        };
        List<Group> groups = finder.findConnectedGroups(image);
        assertEquals(2, groups.size());
    
        // First group should be the one on the right (x = 2)
        assertEquals(3, groups.get(0).size());
        assertEquals(2, groups.get(0).centroid().x());
    
        // Second group should be the one on the left (x = 0)
        assertEquals(3, groups.get(1).size());
        assertEquals(0, groups.get(1).centroid().x());
    }
    

    @Test
    public void testCrossShape() {
        int[][] image = {
                { 0, 1, 0 },
                { 1, 1, 1 },
                { 0, 1, 0 }
        };
        Group group = finder.findConnectedGroups(image).get(0);
        assertEquals(5, group.size());
        assertEquals(1, group.centroid().x());
        assertEquals(1, group.centroid().y());
    }

    @Test
    public void testRingShape() {
        int[][] image = {
                { 1, 1, 1 },
                { 1, 0, 1 },
                { 1, 1, 1 }
        };
        Group group = finder.findConnectedGroups(image).get(0);
        assertEquals(8, group.size());
        assertEquals(1, group.centroid().x());
        assertEquals(1, group.centroid().y());
    }

    @Test
    public void testCornerPixelsOnly() {
        int[][] image = {
                { 1, 0, 1 },
                { 0, 0, 0 },
                { 1, 0, 1 }
        };
        List<Group> groups = finder.findConnectedGroups(image);
        assertEquals(4, groups.size());
        for (Group g : groups) {
            assertEquals(1, g.size());
        }
    }

    @Test
    public void testSparseImage() {
        int[][] image = new int[10][10];
        image[1][1] = 1;
        image[8][8] = 1;
        image[3][5] = 1;

        List<Group> groups = finder.findConnectedGroups(image);
        assertEquals(3, groups.size());
        assertTrue(groups.stream().allMatch(g -> g.size() == 1));
    }

    @Test
    public void testClusterAtEdge() {
        int[][] image = {
                { 1, 1, 1 },
                { 0, 0, 1 }
        };
        Group group = finder.findConnectedGroups(image).get(0);
        assertEquals(4, group.size());
        assertEquals(1, group.centroid().x());
        assertEquals(0, group.centroid().y());
    }

    @Test
    public void testImageWithMultipleGroupsAndCentroidCalculation_Variant2() {
        int[][] image = {
                { 1, 0, 0, 1 },
                { 1, 0, 0, 1 },
                { 0, 0, 0, 1 },
                { 0, 1, 1, 1 }
        };

        List<Group> groups = finder.findConnectedGroups(image);

        assertEquals(2, groups.size());

        Group group1 = groups.get(0); // Largest group first
        Group group2 = groups.get(1);

        assertEquals(6, group1.size());
        assertEquals(2, group1.centroid().x());
        assertEquals(2, group1.centroid().y());

        assertEquals(2, group2.size());
        assertEquals(0, group2.centroid().x());
        assertEquals(0, group2.centroid().y());
    }

    @Test
    public void testJaggedArray() {
        int[][] image = {
                { 1, 1 },
                { 1 },
                { 1, 1, 1 }
        };

        // Depending on implementation, this might throw or be handled gracefully.
        assertThrows(Exception.class, () -> finder.findConnectedGroups(image));
    }

    @Test
    public void testGroupAtBottomRightCorner() {
        int[][] image = {
                { 0, 0, 0 },
                { 0, 0, 0 },
                { 0, 0, 1 }
        };

        List<Group> groups = finder.findConnectedGroups(image);
        assertEquals(1, groups.size());
        Group group = groups.get(0);
        assertEquals(1, group.size());
        assertEquals(2, group.centroid().x());
        assertEquals(2, group.centroid().y());
    }

    @Test
    public void testCentroidIntegerDivision() {
        int[][] image = {
                { 0, 1 },
                { 1, 1 }
        };

        List<Group> groups = finder.findConnectedGroups(image);
        Group group = groups.get(0);
        assertEquals(3, group.size());
        // Coordinates: (1,0), (0,1), (1,1) → x: (1+0+1)/3 = 0, y: (0+1+1)/3 = 0
        assertEquals(0, group.centroid().x());
        assertEquals(0, group.centroid().y());
    }

    @Test
    public void testSinglePixelInLargeGrid() {
        int[][] image = new int[11][11];
        image[5][5] = 1;

        List<Group> groups = finder.findConnectedGroups(image);
        assertEquals(1, groups.size());
        Group group = groups.get(0);
        assertEquals(1, group.size());
        assertEquals(5, group.centroid().x());
        assertEquals(5, group.centroid().y());
    }

    @Test
    public void testSortingWithCentroidTieBreaker() {
        int[][] image = {
            { 1, 0, 0, 1 },
            { 0, 0, 0, 0 },
            { 1, 0, 0, 1 }
        };
        List<Group> groups = finder.findConnectedGroups(image);
        // All 4 groups are size 1. Sorting should go by descending x, then descending y
        assertEquals(4, groups.size());
    
        assertEquals(3, groups.get(0).centroid().x());
        assertEquals(2, groups.get(0).centroid().y());
    
        assertEquals(3, groups.get(1).centroid().x());
        assertEquals(0, groups.get(1).centroid().y());
    
        assertEquals(0, groups.get(2).centroid().x());
        assertEquals(2, groups.get(2).centroid().y());
    
        assertEquals(0, groups.get(3).centroid().x());
        assertEquals(0, groups.get(3).centroid().y());
    }
}