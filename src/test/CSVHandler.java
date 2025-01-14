package test;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.Component;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CSVHandler {
    public List<Geometry> importGeometriesFromCSV(Component parent) {
        List<Geometry> geometries = new ArrayList<>();
        JFileChooser fileChooser = new JFileChooser();
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
                geometries = parseCSV(file);
                JOptionPane.showMessageDialog(parent, "Geometries imported successfully!");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(parent, "Error importing geometries: " + ex.getMessage());
            }
        }
        return geometries;
    }

    public void exportGeometriesToCSV(Component parent, List<Geometry> geometries) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Geometries to CSV");
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
            if (!file.getName().toLowerCase().endsWith(".csv")) {
                file = new File(file.getAbsolutePath() + ".csv");
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write("type,x,y,x2,y2,x3,y3,width,height");
                writer.newLine();
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

    private List<Geometry> parseCSV(File file) throws IOException {
        List<Geometry> geometries = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isFirstRow = true;
            while ((line = br.readLine()) != null) {
                if (isFirstRow) {
                    isFirstRow = false;
                    continue;
                }
                String[] parts = line.split(",");
                String type = parts[0].trim();

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

