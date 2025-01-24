package model;

import java.awt.Graphics;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Triangle Class extends Geometry to represent a triangular shape.
 * It provides methods for drawing the triangle, saving it to a database,
 * loading it from a database, updating its data, and retrieving its type.
 * 
 * @author Kisal/Ravi
 * Date: 26 January 2024
 */
public class Triangle extends Geometry {
    public int x3; // x-coordinate of the third point of the triangle
    public int y3; // y-coordinate of the third point of the triangle

    /**
     * Constructor to initialize the Triangle with its three points' coordinates.
     * 
     * @param x  x-coordinate of the first point.
     * @param y  y-coordinate of the first point.
     * @param x2 x-coordinate of the second point.
     * @param y2 y-coordinate of the second point.
     * @param x3 x-coordinate of the third point.
     * @param y3 y-coordinate of the third point.
     */
    public Triangle(int x, int y, int x2, int y2, int x3, int y3) {
        super(x, y, x2, y2); // The first two points are handled by the parent class.
        this.x3 = x3; // Assign the third point's x-coordinate.
        this.y3 = y3; // Assign the third point's y-coordinate.
    }

    /**
     * Draws the triangle on a graphical canvas.
     * 
     * @param g Graphics object used for rendering.
     */
    @Override
    public void draw(Graphics g) {
        // Define the three points of the triangle and draw it using a polygon.
        int[] xPoints = {x, x2, x3};
        int[] yPoints = {y, y2, y3};
        g.drawPolygon(xPoints, yPoints, 3); // Draw a polygon with three points.
    }

    /**
     * Saves the triangle geometry to the database.
     * This method inserts the geometry type into the `geometries` table and
     * stores the triangle-specific data (coordinates of the three points) into 
     * the `triangles` table.
     * 
     * @param conn Database connection.
     * @throws SQLException if an SQL error occurs.
     */
    @Override
    public void saveToDatabase(Connection conn) throws SQLException {
        // Insert the geometry type and creation timestamp into the geometries table.
        String geometrySql = "INSERT INTO geometries (type, created_at) VALUES (?, NOW())";
        try (PreparedStatement stmt = conn.prepareStatement(geometrySql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, "Triangle"); // Specify the geometry type.
            stmt.executeUpdate();

            // Retrieve the generated ID for the geometry.
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                id = generatedKeys.getInt(1); // Store the generated ID.
            }

            // Insert the triangle-specific data into the triangles table.
            String triangleSql = "INSERT INTO triangles (id, x, y, x2, y2, x3, y3) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement triangleStmt = conn.prepareStatement(triangleSql)) {
                triangleStmt.setInt(1, id);  // Set the triangle ID.
                triangleStmt.setInt(2, x);   // Set the x-coordinate of the first point.
                triangleStmt.setInt(3, y);   // Set the y-coordinate of the first point.
                triangleStmt.setInt(4, x2);  // Set the x-coordinate of the second point.
                triangleStmt.setInt(5, y2);  // Set the y-coordinate of the second point.
                triangleStmt.setInt(6, x3);  // Set the x-coordinate of the third point.
                triangleStmt.setInt(7, y3);  // Set the y-coordinate of the third point.
                triangleStmt.executeUpdate(); // Execute the insert statement.
            }
        }
    }

    /**
     * Returns the type of this geometry (i.e., "Triangle").
     * 
     * @return A string representing the geometry type.
     */
    @Override
    public String getType() {
        return "Triangle";
    }

    /**
     * Loads a triangle geometry from the provided ResultSet.
     * 
     * @param rs ResultSet containing the geometry data.
     * @return A Triangle object initialized with the data from the ResultSet.
     * @throws SQLException if an SQL error occurs.
     */
    @Override
    public Geometry loadFromDatabase(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");    // Retrieve the triangle's ID.
        int x = rs.getInt("x");      // Retrieve the x-coordinate of the first point.
        int y = rs.getInt("y");      // Retrieve the y-coordinate of the first point.
        int x2 = rs.getInt("x2");    // Retrieve the x-coordinate of the second point.
        int y2 = rs.getInt("y2");    // Retrieve the y-coordinate of the second point.
        int x3 = rs.getInt("x3");    // Retrieve the x-coordinate of the third point.
        int y3 = rs.getInt("y3");    // Retrieve the y-coordinate of the third point.

        // Create a Triangle object using the retrieved data.
        Triangle triangle = new Triangle(x, y, x2, y2, x3, y3);
        triangle.id = id;           // Assign the ID to the triangle object.
        return triangle;            // Return the Triangle object.
    }

    /**
     * Updates the triangle's data in the database.
     * 
     * @param conn Database connection.
     * @throws SQLException if an SQL error occurs.
     */
    @Override
    public void updateGeometryInDatabase(Connection conn) throws SQLException {
        // SQL query to update triangle data in the database.
        String sql = "UPDATE triangles SET x = ?, y = ?, x2 = ?, y2 = ?, x3 = ?, y3 = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, x);   // Update the x-coordinate of the first point.
            stmt.setInt(2, y);   // Update the y-coordinate of the first point.
            stmt.setInt(3, x2);  // Update the x-coordinate of the second point.
            stmt.setInt(4, y2);  // Update the y-coordinate of the second point.
            stmt.setInt(5, x3);  // Update the x-coordinate of the third point.
            stmt.setInt(6, y3);  // Update the y-coordinate of the third point.
            stmt.setInt(7, id);  // Specify the triangle ID for the update.
            stmt.executeUpdate(); // Execute the update statement.
        }
    }
}
