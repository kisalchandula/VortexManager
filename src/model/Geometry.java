package model;

import java.awt.Graphics;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Abstract class representing a geometric shape.
 * This class serves as a base for specific geometric shapes (e.g., Point, Line, Rectangle).
 * 
 * @author Kisal
 * Date: 24 December 2024
 */
public abstract class Geometry {
    // Coordinates for the geometry
    public int x;   // Starting x-coordinate
    public int y;   // Starting y-coordinate
    public int x2;  // Ending x-coordinate (used for shapes with an extent, like lines or rectangles)
    public int y2;  // Ending y-coordinate (used for shapes with an extent, like lines or rectangles)
    public int id;  // Unique identifier for the geometry (typically corresponds to database record)

    /**
     * Constructor to initialize the geometry with starting and ending coordinates.
     * 
     * @param x Starting x-coordinate
     * @param y Starting y-coordinate
     * @param x2 Ending x-coordinate
     * @param y2 Ending y-coordinate
     */
    public Geometry(int x, int y, int x2, int y2) {
        this.x = x;
        this.y = y;
        this.x2 = x2;
        this.y2 = y2;
    }

    /**
     * Abstract method to draw the geometry on a graphical canvas.
     * 
     * @param g Graphics object used for rendering.
     */
    public abstract void draw(Graphics g);
    
    /**
     * Abstract method to save the geometry to the database.
     * 
     * @param conn Database connection.
     * @throws SQLException if an SQL error occurs.
     */
    public abstract void saveToDatabase(Connection conn) throws SQLException;

    /**
     * Abstract method to load the geometry from a database ResultSet.
     * 
     * @param rs ResultSet containing the geometry data.
     * @return An instance of the specific geometry subclass.
     * @throws SQLException if an SQL error occurs.
     */
    public abstract Geometry loadFromDatabase(ResultSet rs) throws SQLException;

    /**
     * Abstract method to update the geometry in the database.
     * 
     * @param conn Database connection.
     * @throws SQLException if an SQL error occurs.
     */
    public abstract void updateGeometryInDatabase(Connection conn) throws SQLException;
    
    /**
     * Deletes the geometry from the database.
     * 
     * @param conn Database connection.
     * @throws SQLException if an SQL error occurs.
     */
    public void deleteGeometryFromDatabase(Connection conn) throws SQLException {
        String deleteGeometrySql = "DELETE FROM geometries WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(deleteGeometrySql)) {
            stmt.setInt(1, id); // Set the geometry ID for deletion
            stmt.executeUpdate(); // Execute the delete statement
        }
    }

    /**
     * Abstract method to return the type of the geometry (e.g., "Point", "Line", "Rectangle").
     * 
     * @return String representing the geometry type.
     */
    public abstract String getType();
}
