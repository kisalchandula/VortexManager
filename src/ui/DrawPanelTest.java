package ui;

import static org.junit.Assert.*;

import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;


import model.Rectangle;
import model.Triangle;

public class DrawPanelTest {

    private DrawPanel drawPanel;

    @Before
    public void setUp() {
        drawPanel = new DrawPanel();
    }

    // Test for isTriangleWithin() method
    @Test
    public void testIsTriangleWithin() throws Exception {
        // Create a sample Rectangle and Triangle
        Rectangle queryRect = new Rectangle(0, 0, 10, 10);
        Triangle insideTriangle = new Triangle(1, 1, 2, 2, 3, 3);
        Triangle outsideTriangle = new Triangle(11, 11, 12, 12, 13, 13);

        // Use reflection to access the private method
        Method method = DrawPanel.class.getDeclaredMethod("isTriangleWithin", Rectangle.class, Triangle.class);
        method.setAccessible(true);  // Make the private method accessible

        // Test when the triangle is inside the rectangle
        boolean resultInside = (boolean) method.invoke(drawPanel, queryRect, insideTriangle);
        assertTrue("Triangle should be within the rectangle", resultInside);

        // Test when the triangle is outside the rectangle
        boolean resultOutside = (boolean) method.invoke(drawPanel, queryRect, outsideTriangle);
        assertFalse("Triangle should not be within the rectangle", resultOutside);
    }
}
