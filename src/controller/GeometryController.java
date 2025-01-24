package controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Geometry;
import model.Line;
import model.Point;
import model.Rectangle;
import model.Triangle;
import util.DatabaseConnection;

/**
 * GeometryController Class handles interactions between the UI and the data layer.
 * It provides methods to save, load, update, and delete geometric objects in the database.
 * 
 * @author Kisal
 * Date: 10 January 2025
 */
public class GeometryController {
    protected Connection conn; // Database connection

    /**
     * Constructor that establishes a database connection using DatabaseConnection utility.
     * 
     * @throws SQLException if a database connection error occurs.
     */
    public GeometryController() throws SQLException {
        conn = DatabaseConnection.getConnection();
    }

    /**
     * Saves a geometry object to the database.
     * 
     * @param geometry The Geometry object to be saved.
     * @throws SQLException if an error occurs while saving to the database.
     */
    public void saveGeometry(Geometry geometry) throws SQLException {
        geometry.saveToDatabase(conn); // Delegates the saving logic to the Geometry class.
    }

    /**
     * Loads all geometries from the database, iterating through each geometry type table.
     * 
     * @return A list of Geometry objects loaded from the database.
     * @throws SQLException if an error occurs while querying the database.
     */
    public List<Geometry> loadGeometries() throws SQLException {
        List<Geometry> geometries = new ArrayList<>(); // List to store all loaded geometries.

        // Array of table names corresponding to different geometry types.
        String[] geometryTypes = {"rectangles", "line", "points", "triangles"};
        for (String geometryType : geometryTypes) {
            String sql = "SELECT * FROM " + geometryType; // SQL query to select all records.
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    Geometry geometry = null;
                    // Create the appropriate geometry object based on the table being queried.
                    if (geometryType.equals("rectangles")) {
                        geometry = new Rectangle(0, 0, 0, 0);
                    } else if (geometryType.equals("line")) {
                        geometry = new Line(0, 0, 0, 0);
                    } else if (geometryType.equals("points")) {
                        geometry = new Point(0, 0);
                    } else if (geometryType.equals("triangles")) {
                        geometry = new Triangle(0, 0, 0, 0, 0, 0);
                    }
                    // Load data into the geometry object using its specific implementation.
                    geometry = geometry.loadFromDatabase(rs);
                    geometries.add(geometry); // Add the loaded geometry to the list.
                }
            }
        }
        return geometries;
    }

    /**
     * Updates an existing geometry in the database.
     * 
     * @param geometry The Geometry object to be updated.
     * @throws SQLException if an error occurs while updating the database.
     */
    public void updateGeometry(Geometry geometry) throws SQLException {
        geometry.updateGeometryInDatabase(conn); // Delegates the update logic to the Geometry class.
    }

    /**
     * Deletes a geometry object from the database.
     * 
     * @param geometry The Geometry object to be deleted.
     * @throws SQLException if an error occurs while deleting from the database.
     */
    public void deleteGeometry(Geometry geometry) throws SQLException {
        geometry.deleteGeometryFromDatabase(conn); // Delegates the deletion logic to the Geometry class.
    }

    /**
     * Closes the database connection.
     * 
     * @throws SQLException if an error occurs while closing the connection.
     */
    public void close() throws SQLException {
        conn.close(); // Ensures that the database connection is properly closed.
    }
}
