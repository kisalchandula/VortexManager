package model;

import java.awt.Graphics;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Point Class extends Geometry to represent a single point.
 * It provides methods for drawing the point, saving it to a database,
 * loading it from a database, updating its data, and retrieving its type.
 * 
 * @author Kisal/Ravi
 * Date: 26 December 2024
 */
public class Point extends Geometry {

    /**
     * Constructor to initialize the Point with x and y coordinates.
     * 
     * @param x x-coordinate of the point.
     * @param y y-coordinate of the point.
     */
    public Point(int x, int y) {
        super(x, y, x, y); // A point has no extent; x2 and y2 are the same as x and y.
    }

    /**
     * Draws the point on a graphical canvas as a small filled oval.
     * 
     * @param g Graphics object used for rendering.
     */
    @Override
    public void draw(Graphics g) {
        g.fillOval(x - 5, y - 5, 10, 10); // Draw a small circle with a 10-pixel diameter centered at (x, y).
    }

    /**
     * Saves the point geometry to the database.
     * This method inserts the geometry type into the `geometries` table and 
     * stores the point-specific data (coordinates) into the `points` table.
     * 
     * @param conn Database connection.
     * @throws SQLException if an SQL error occurs.
     */
    @Override
    public void saveToDatabase(Connection conn) throws SQLException {
        // Insert the geometry type and creation timestamp into the geometries table.
        String geometrySql = "INSERT INTO geometries (type, created_at) VALUES (?, NOW())";
        try (PreparedStatement stmt = conn.prepareStatement(geometrySql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, "Point"); // Specify the geometry type.
            stmt.executeUpdate();

            // Retrieve the generated ID for the geometry.
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                id = generatedKeys.getInt(1); // Store the generated ID.
            }

            // Insert the point-specific data into the points table.
            String pointSql = "INSERT INTO points (id, x, y) VALUES (?, ?, ?)";
            try (PreparedStatement pointStmt = conn.prepareStatement(pointSql)) {
                pointStmt.setInt(1, id); // Set the point ID.
                pointStmt.setInt(2, x);  // Set the x-coordinate.
                pointStmt.setInt(3, y);  // Set the y-coordinate.
                pointStmt.executeUpdate(); // Execute the insert statement.
            }
        }
    }

    /**
     * Returns the type of this geometry (i.e., "Point").
     * 
     * @return A string representing the geometry type.
     */
    @Override
    public String getType() {
        return "Point";
    }

    /**
     * Loads a point geometry from the provided ResultSet.
     * 
     * @param rs ResultSet containing the geometry data.
     * @return A Point object initialized with the data from the ResultSet.
     * @throws SQLException if an SQL error occurs.
     */
    @Override
    public Geometry loadFromDatabase(ResultSet rs) throws SQLException {
        int id = rs.getInt("id"); // Retrieve the point's ID.
        int x = rs.getInt("x");  // Retrieve the x-coordinate.
        int y = rs.getInt("y");  // Retrieve the y-coordinate.

        Point point = new Point(x, y); // Create a Point object with the retrieved coordinates.
        point.id = id; // Assign the ID to the point object.
        return point;  // Return the Point object.
    }

    /**
     * Updates the point's data in the database.
     * 
     * @param conn Database connection.
     * @throws SQLException if an SQL error occurs.
     */
    @Override
    public void updateGeometryInDatabase(Connection conn) throws SQLException {
        // SQL query to update point data in the database.
        String sql = "UPDATE points SET x = ?, y = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, x);  // Update the x-coordinate.
            stmt.setInt(2, y);  // Update the y-coordinate.
            stmt.setInt(3, id); // Specify the point ID for the update.
            stmt.executeUpdate(); // Execute the update statement.
        }
    }
}
