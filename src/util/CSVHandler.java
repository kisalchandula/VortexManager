package util;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;

import model.Geometry;
import model.Line;
import model.Point;
import model.Rectangle;
import model.Triangle;

import java.awt.Component;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * CSVHandler class is responsible for importing and exporting geometries from/to CSV files.
 * It provides methods to load geometries from a CSV file and save geometries to a CSV file.
 *
 * @author Sandys
 * Date: 02 January 2025
 */
public class CSVHandler {
    
    /**
     * Imports geometries from a CSV file using a file chooser dialog.
     * @param parent The component to be used as the parent for the file chooser dialog.
     * @return A list of geometries imported from the selected CSV file.
     */
    public List<Geometry> importGeometriesFromCSV(Component parent) {
        List<Geometry> geometries = new ArrayList<>();
        JFileChooser fileChooser = new JFileChooser();
        // Set the file filter to accept only CSV files
        fileChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.isDirectory() || file.getName().toLowerCase().endsWith(".csv");
            }

            @Override
            public String getDescription() {
                return "CSV Files (*.csv)";
            }
        });

        int result = fileChooser.showOpenDialog(parent);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                // Parse the selected CSV file to load geometries
                geometries = parseCSV(file);
                JOptionPane.showMessageDialog(parent, "Geometries imported successfully!");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(parent, "Error importing geometries: " + ex.getMessage());
            }
        }
        return geometries;
    }

    /**
     * Exports a list of geometries to a CSV file using a file chooser dialog.
     * @param parent The component to be used as the parent for the file chooser dialog.
     * @param geometries The list of geometries to export.
     */
    public void exportGeometriesToCSV(Component parent, List<Geometry> geometries) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Geometries to CSV");
        // Set the file filter to accept only CSV files
        fileChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.isDirectory() || file.getName().toLowerCase().endsWith(".csv");
            }

            @Override
            public String getDescription() {
                return "CSV Files (*.csv)";
            }
        });

        int result = fileChooser.showSaveDialog(parent);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            // Ensure the file has a .csv extension
            if (!file.getName().toLowerCase().endsWith(".csv")) {
                file = new File(file.getAbsolutePath() + ".csv");
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                // Write header for the CSV file
                writer.write("type,x,y,x2,y2,x3,y3,width,height");
                writer.newLine();
                // Write each geometry to the CSV file
                for (Geometry geometry : geometries) {
                    writer.write(geometryToCSV(geometry));
                    writer.newLine();
                }
                JOptionPane.showMessageDialog(parent, "Geometries exported successfully!");
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(parent, "Error exporting geometries: " + ex.getMessage());
            }
        }
    }

    /**
     * Parses a CSV file and creates a list of geometries based on the content.
     * @param file The CSV file to parse.
     * @return A list of geometries parsed from the CSV file.
     * @throws IOException If an error occurs while reading the file.
     */
    private List<Geometry> parseCSV(File file) throws IOException {
        List<Geometry> geometries = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isFirstRow = true;
            while ((line = br.readLine()) != null) {
                if (isFirstRow) {
                    isFirstRow = false; // Skip the first row (header)
                    continue;
                }
                String[] parts = line.split(",");
                String type = parts[0].trim();

                // Create the appropriate geometry object based on the type
                switch (type.toLowerCase()) {
                    case "rectangle":
                        geometries.add(new Rectangle(
                                Integer.parseInt(parts[1].trim()),
                                Integer.parseInt(parts[2].trim()),
                                Integer.parseInt(parts[3].trim()),
                                Integer.parseInt(parts[4].trim())));
                        break;

                    case "line":
                        geometries.add(new Line(
                                Integer.parseInt(parts[1].trim()),
                                Integer.parseInt(parts[2].trim()),
                                Integer.parseInt(parts[3].trim()),
                                Integer.parseInt(parts[4].trim())));
                        break;

                    case "point":
                        geometries.add(new Point(
                                Integer.parseInt(parts[1].trim()),
                                Integer.parseInt(parts[2].trim())));
                        break;

                    case "triangle":
                        geometries.add(new Triangle(
                                Integer.parseInt(parts[1].trim()),
                                Integer.parseInt(parts[2].trim()),
                                Integer.parseInt(parts[3].trim()),
                                Integer.parseInt(parts[4].trim()),
                                Integer.parseInt(parts[5].trim()),
                                Integer.parseInt(parts[6].trim())));
                        break;

                    default:
                        throw new IllegalArgumentException("Unknown geometry type: " + type);
                }
            }
        }
        return geometries;
    }

    /**
     * Converts a geometry object to a CSV string representation.
     * @param geometry The geometry to convert.
     * @return A CSV string representing the geometry.
     */
    private String geometryToCSV(Geometry geometry) {
        if (geometry instanceof Rectangle) {
            Rectangle rect = (Rectangle) geometry;
            return String.format("rectangle,%d,%d,%d,%d", rect.x, rect.y, rect.x2, rect.y2);
        } else if (geometry instanceof Line) {
            Line line = (Line) geometry;
            return String.format("line,%d,%d,%d,%d", line.x, line.y, line.x2, line.y2);
        } else if (geometry instanceof Point) {
            Point point = (Point) geometry;
            return String.format("point,%d,%d", point.x, point.y);
        } else if (geometry instanceof Triangle) {
            Triangle triangle = (Triangle) geometry;
            return String.format("triangle,%d,%d,%d,%d,%d,%d",
                    triangle.x, triangle.y, triangle.x2, triangle.y2, triangle.x3, triangle.y3);
        }
        throw new IllegalArgumentException("Unknown geometry type: " + geometry.getClass().getSimpleName());
    }
}
