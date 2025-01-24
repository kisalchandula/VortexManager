package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import controller.GeometryController;
import model.Geometry;
import model.Line;
import model.Point;
import model.Rectangle;
import model.Triangle;
import util.CSVHandler;


// DrawPanel Class
public class DrawPanel extends JPanel {
    /**
     * Draw Panel class responsible for drawing and changing geometries.
     * This class handles the drawing of geometries such as rectangles, lines, points, and triangles.
     * It also manages user interactions such as deleting, moving, and querying geometries.
     * 
     * @author Bhavin/ Ravi/ Kisal
     * Date: 27 December 2024
     */
	private static final long serialVersionUID = 1L;
	private int x, y, x2, y2;
    private GeometryController geometryController;
    private Geometry currentGeometry;
    private boolean isDrawingRectangle;
    private boolean isDrawingLine;
    private boolean isDrawingPoint;
    private boolean isDrawingTriangle;
    private boolean isDeleteMode = false;
    List<Geometry> geometries; // To store loaded geometries
    private List<Geometry> newGeometries; // To store newly created geometries
    
    private boolean isMoveMode = false;
    private Geometry selectedGeometry = null;
    private int offsetX, offsetY; // To track the mouse offset for dragging
    
    private boolean isRangeQueryMode = false;

    public DrawPanel() {
        x = y = x2 = y2 = 0;
        isDrawingRectangle = false;
        isDrawingLine = false;
        geometries = new ArrayList<>();
        newGeometries = new ArrayList<>();

        try {
            geometryController = new GeometryController();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        setLayout(new BorderLayout());


        MyMouseListener listener = new MyMouseListener();
        addMouseListener(listener);
        addMouseMotionListener(listener);
    }

    // main method
//    public static void main(String[] args) {
//        JFrame f = new JFrame("Draw Geometry");
//        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        f.setContentPane(new DrawPanel());
//        f.setSize(500, 600);
//        f.setVisible(true);
//    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g); // Clears the canvas

        // Draw all geometries in blue
        g.setColor(Color.BLUE);
        for (Geometry geometry : geometries) {
            if (geometry != null) { 
                geometry.draw(g);
            }
        }

        if (currentGeometry != null) {
            if (isRangeQueryMode && currentGeometry instanceof Rectangle) {
                g.setColor(Color.BLACK); // Use black for the query rectangle
            } else {
                g.setColor(Color.BLUE); // Default color
            }
            currentGeometry.draw(g);
        }
    }
    
    
 // Clears all geometries from the canvas and resets modes
    public void clearPanel() {
        // Clear all geometries
        geometries.clear();
        newGeometries.clear();
        currentGeometry = null;

        // Reset drawing states
        isDrawingRectangle = false;
        isDrawingLine = false;
        isDrawingPoint = false;
        isDrawingTriangle = false;
        isDeleteMode = false;
        isMoveMode = false;
        isRangeQueryMode = false;
        selectedGeometry = null;

        repaint();
    }

    
 // Imports geometries from a CSV file
    public void importGeometriesFromCSV(CSVHandler csvHandler) {
        List<Geometry> importedGeometries = csvHandler.importGeometriesFromCSV(this);
        geometries.addAll(importedGeometries);
        repaint();
    }
    
 // Exports geometries to a CSV file
    public void exportGeometriesToCSV(CSVHandler csvHandler) {
        csvHandler.exportGeometriesToCSV(this, geometries);
    }
    
 // Sets the mode for drawing or interacting with geometries
    public void setDrawingMode(String mode) {
        isDeleteMode = false;
        isMoveMode = false;
        isRangeQueryMode = false;
        isDrawingRectangle = false;
        isDrawingLine = false;
        isDrawingPoint = false;
        isDrawingTriangle = false;

        switch (mode) {
            case "RECTANGLE":
                isDrawingRectangle = true;
                break;
            case "LINE":
                isDrawingLine = true;
                break;
            case "POINT":
                isDrawingPoint = true;
                break;
            case "TRIANGLE":
                isDrawingTriangle = true;
                break;
            case "DELETE":
                isDeleteMode = true;
                JOptionPane.showMessageDialog(this, "Click on a geometry to delete!");
                break;
            case "MOVE":
                isMoveMode = true;
                JOptionPane.showMessageDialog(this, "Drag geometries to move!");
                break;
            case "RANGE_QUERY":
                isRangeQueryMode = true;
                JOptionPane.showMessageDialog(this, "Drag to select a rectangular area!");
                break;
            default:
                // No mode
                break;
        }

        repaint();
    }


 // Saves newly created or modified geometries
    public void saveGeometry() {
    	try {
            if (newGeometries.isEmpty() && selectedGeometry == null) {
                JOptionPane.showMessageDialog(this, "No changes to save!");
                return;
            }
            else if (selectedGeometry != null) {
            	geometryController.updateGeometry(selectedGeometry);
                selectedGeometry = null; // Clear selection
                isMoveMode = false; 
            } else {
            	
            	for (Geometry geometry : newGeometries) {
            		System.out.print(geometry);
                    geometryController.saveGeometry(geometry);
                }
            }
            
            newGeometries.clear();
            JOptionPane.showMessageDialog(this, "Changes saved successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Loads geometries from the database
    public void loadGeometries() {
        try {
            geometries = geometryController.loadGeometries();
            newGeometries.clear();
            repaint();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // Finds a geometry at a given x, y position
    private Geometry findGeometryAt(int x, int y) {
        for (Geometry geometry : geometries) {
            if (geometry instanceof Rectangle) {
                Rectangle rect = (Rectangle) geometry;
                if (x >= rect.x && x <= rect.x + Math.abs(rect.x2 - rect.x) &&
                    y >= rect.y && y <= rect.y + Math.abs(rect.y2 - rect.y)) {
                    return rect;
                }
            } else if (geometry instanceof Line) {
                Line line = (Line) geometry;
                int tolerance = 5; // Tolerance for selecting a line
                if (distanceFromLine(x, y, line.x, line.y, line.x2, line.y2) <= tolerance) {
                    return line;
                }
            } else if (geometry instanceof Point) {
                Point point = (Point) geometry;
                if (Math.abs(x - point.x) <= 5 && Math.abs(y - point.y) <= 5) {
                    return point;
                }
            } else if (geometry instanceof Triangle) {
                Triangle triangle = (Triangle) geometry;
                if (isPointInsideTriangle(x, y, 
                                          triangle.x, triangle.y, 
                                          triangle.x2, triangle.y2, 
                                          triangle.x3, triangle.y3)) {
                    return triangle;
                }
            }
        }
        return null;
    }
    
    // Checks if a point is inside a triangle
    private boolean isPointInsideTriangle(int px, int py, int x, int y, int x2, int y2, int x3, int y3) {
        // Calculate the areas
        double areaOrig = Math.abs((x * (y2 - y3) + x2 * (y3 - y) + x3 * (y - y2)) / 2.0);
        double area1 = Math.abs((px * (y2 - y3) + x2 * (y3 - py) + x3 * (py - y2)) / 2.0);
        double area2 = Math.abs((x * (py - y3) + px * (y3 - y) + x3 * (y - py)) / 2.0);
        double area3 = Math.abs((x * (y2 - py) + x2 * (py - y) + px * (y - y2)) / 2.0);

        // Check if the sum of sub-areas equals the original area
        return Math.abs(areaOrig - (area1 + area2 + area3)) < 1e-5;
    }


 // Computes the distance between a point and a line
    private double distanceFromLine(int x, int y, int x1, int y1, int x2, int y2) {
        double a = y2 - y1;
        double b = x1 - x2;
        double c = x2 * y1 - x1 * y2;
        return Math.abs(a * x + b * y + c) / Math.sqrt(a * a + b * b);
    }
    
 // Performs a range query by checking which geometries are inside the given rectangle
    private void performRangeQuery(Rectangle queryRectangle) {
        List<Geometry> selectedGeometries = new ArrayList<>();

        for (Geometry geometry : geometries) {
            if (geometry instanceof Rectangle) {
                Rectangle rect = (Rectangle) geometry;
                if (isRectangleWithin(queryRectangle, rect)) {
                    selectedGeometries.add(rect);
                }
            } else if (geometry instanceof Line) {
                Line line = (Line) geometry;
                if (isLineWithin(queryRectangle, line)) {
                    selectedGeometries.add(line);
                }
            } else if (geometry instanceof Point) {
                Point point = (Point) geometry;
                if (isPointWithin(queryRectangle, point)) {
                    selectedGeometries.add(point);
                }
            } else if (geometry instanceof Triangle) {
            	Triangle triangle = (Triangle) geometry;
            	if (isTriangleWithin(queryRectangle, triangle)) {
                    selectedGeometries.add(triangle);
                }
            }
        }

        highlightGeometries(selectedGeometries);
        JOptionPane.showMessageDialog(this, selectedGeometries.size() + " geometries selected.");
    }

    private boolean isRectangleWithin(Rectangle query, Rectangle rect) {
        return rect.x >= query.x && rect.x + Math.abs(rect.x2 - rect.x) <= query.x + Math.abs(query.x2 - query.x) &&
               rect.y >= query.y && rect.y + Math.abs(rect.y2 - rect.y) <= query.y + Math.abs(query.y2 - query.y);
    }

    private boolean isLineWithin(Rectangle query, Line line) {
        return line.x >= query.x && line.x <= query.x + Math.abs(query.x2 - query.x) &&
               line.y >= query.y && line.y <= query.y + Math.abs(query.y2 - query.y) &&
               line.x2 >= query.x && line.x2 <= query.x + Math.abs(query.x2 - query.x) &&
               line.y2 >= query.y && line.y2 <= query.y + Math.abs(query.y2 - query.y);
    }
    
    private boolean isPointWithin(Rectangle query, Point point) {
        return point.x >= query.x && point.x <= query.x + Math.abs(query.x2 - query.x) &&
               point.y >= query.y && point.y <= query.y + Math.abs(query.y2 - query.y);
    }
    
    private boolean isTriangleWithin(Rectangle query, Triangle triangle) {
        return isPointWithin(query, new Point(triangle.x, triangle.y)) &&
               isPointWithin(query, new Point(triangle.x2, triangle.y2)) &&
               isPointWithin(query, new Point(triangle.x3, triangle.y3));
    }

    private void highlightGeometries(List<Geometry> geometries) {
        Graphics g = getGraphics();
        g.setColor(Color.RED); // Highlight color
        for (Geometry geometry : geometries) {
            geometry.draw(g);
        }
    }

    class MyMouseListener extends MouseAdapter {

        private int[] trianglePointsX = new int[3]; // To store the x-coordinates of the triangle points
        private int[] trianglePointsY = new int[3]; // To store the y-coordinates of the triangle points
        private int trianglePointCount = 0; // Tracks the number of points defined for the triangle

        public void mousePressed(MouseEvent e) {
            x = e.getX();
            y = e.getY();

            if (isDrawingTriangle) {
                if (trianglePointCount < 2) {
                    // Store the first two points of the triangle
                    trianglePointsX[trianglePointCount] = x;
                    trianglePointsY[trianglePointCount] = y;
                    trianglePointCount++;
                } else if (trianglePointCount == 2) {
                    // Complete the triangle on the third click
                    trianglePointsX[2] = x;
                    trianglePointsY[2] = y;

                    currentGeometry = new Triangle(
                        trianglePointsX[0], trianglePointsY[0],
                        trianglePointsX[1], trianglePointsY[1],
                        trianglePointsX[2], trianglePointsY[2]
                    );

                    geometries.add(currentGeometry);
                    newGeometries.add(currentGeometry);
                    System.out.print(geometries);
                    trianglePointCount = 0; // Reset for the next triangle
                    isDrawingTriangle = false; // Exit triangle drawing mode
                    repaint();
                }
            }
            
            if (isRangeQueryMode) {
                currentGeometry = new Rectangle(x, y, x, y); // Temporary rectangle for visual feedback
            }

            if (isMoveMode) {
                selectedGeometry = findGeometryAt(x, y);
                if (selectedGeometry != null) {
                    offsetX = x - selectedGeometry.x;
                    offsetY = y - selectedGeometry.y;
                }
            } else if (isDeleteMode) {
                Geometry selectedGeometry = findGeometryAt(e.getX(), e.getY());
                if (selectedGeometry != null) {
                    int response = JOptionPane.showConfirmDialog(
                        DrawPanel.this,
                        "Are you sure you want to delete this geometry?",
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION
                    );

                    if (response == JOptionPane.YES_OPTION) {
                        try {
                            geometryController.deleteGeometry(selectedGeometry);
                            currentGeometry = null; // Clear the current geometry
                            geometries.remove(selectedGeometry);
                            repaint();
                            JOptionPane.showMessageDialog(DrawPanel.this, "Geometry deleted successfully!");
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(DrawPanel.this, "Error deleting geometry from database.");
                        }
                    }
                }
                isDeleteMode = false; // Exit delete mode after one deletion attempt
            }
        }

        public void mouseDragged(MouseEvent e) {
            x2 = e.getX();
            y2 = e.getY();

            if (isRangeQueryMode && currentGeometry instanceof Rectangle) {
                ((Rectangle) currentGeometry).x2 = x2;
                ((Rectangle) currentGeometry).y2 = y2;
                repaint();
            }

            if (isMoveMode && selectedGeometry != null) {
                if (selectedGeometry instanceof Rectangle) {
                    Rectangle rect = (Rectangle) selectedGeometry;
                    int width = Math.abs(rect.x2 - rect.x);
                    int height = Math.abs(rect.y2 - rect.y);
                    rect.x = x2 - offsetX;
                    rect.y = y2 - offsetY;
                    rect.x2 = rect.x + width;
                    rect.y2 = rect.y + height;
                } else if (selectedGeometry instanceof Line) {
                    Line line = (Line) selectedGeometry;
                    int dx = x2 - x;
                    int dy = y2 - y;
                    line.x += dx;
                    line.y += dy;
                    line.x2 += dx;
                    line.y2 += dy;
                    x = x2;
                    y = y2;
                } else if (selectedGeometry instanceof Point) {
                    Point point = (Point) selectedGeometry;
                    point.x = x2 - offsetX;
                    point.y = y2 - offsetY;
                }  else if (selectedGeometry instanceof Triangle) {
                    Triangle triangle = (Triangle) selectedGeometry;
                    int dx = x2 - x;
                    int dy = y2 - y;

                    // Move all three points of the triangle
                    triangle.x += dx;
                    triangle.y += dy;
                    triangle.x2 += dx;
                    triangle.y2 += dy;
                    triangle.x3 += dx;
                    triangle.y3 += dy;

                    x = x2; // Update for next drag calculation
                    y = y2;
                }
                repaint();
                
                
                
            }
            

            if (isDrawingTriangle && trianglePointCount == 2) {
                // Update temporary third point for visual feedback
                trianglePointsX[2] = x2;
                trianglePointsY[2] = y2;

                repaint();
            }

            if (isDrawingRectangle) {
                currentGeometry = new Rectangle(x, y, x2, y2);
            } else if (isDrawingLine) {
                currentGeometry = new Line(x, y, x2, y2);
            }

            repaint();
        }

        public void mouseReleased(MouseEvent e) {
            x2 = e.getX();
            y2 = e.getY();

            if (isRangeQueryMode && currentGeometry instanceof Rectangle) {
                Rectangle queryRectangle = (Rectangle) currentGeometry;
                performRangeQuery(queryRectangle);
                currentGeometry = null;
                isRangeQueryMode = false;
            }

            if (isDrawingRectangle) {
                currentGeometry = new Rectangle(x, y, x2, y2);
                geometries.add(currentGeometry);
                newGeometries.add(currentGeometry);
            } else if (isDrawingLine) {
                currentGeometry = new Line(x, y, x2, y2);
                geometries.add(currentGeometry);
                newGeometries.add(currentGeometry);
            } else if (isDrawingPoint) {
                currentGeometry = new Point(x, y);
                geometries.add(currentGeometry);
                newGeometries.add(currentGeometry);
            }
            repaint();
        }
    }

}
