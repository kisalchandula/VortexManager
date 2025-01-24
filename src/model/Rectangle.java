package model;

import java.awt.Graphics;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Rectangle Class extends Geometry to represent a rectangular shape.
 * It provides methods for drawing the rectangle, saving it to a database,
 * loading it from a database, updating its data, and retrieving its type.
 * 
 * @author Kisal/Ravi
 * Date: 26 December 2024
 */
public class Rectangle extends Geometry {

    /**
     * Constructor to initialize the Rectangle with its two corners' coordinates.
     * 
     * @param x  x-coordinate of the top-left corner.
     * @param y  y-coordinate of the top-left corner.
     * @param x2 x-coordinate of the bottom-right corner.
     * @param y2 y-coordinate of the bottom-right corner.
     */
    public Rectangle(int x, int y, int x2, int y2) {
        super(x, y, x2, y2);
    }

    /**
     * Draws the rectangle on a graphical canvas.
     * 
     * @param g Graphics object used for rendering.
     */
    @Override
    public void draw(Graphics g) {
        g.drawRect(x, y, Math.abs(x2 - x), Math.abs(y2 - y)); // Calculate width and height based on corner coordinates.
    }

    /**
     * Saves the rectangle geometry to the database.
     * This method inserts the geometry type into the `geometries` table and 
     * stores the rectangle-specific data (coordinates, width, and height) into 
     * the `rectangles` table.
     * 
     * @param conn Database connection.
     * @throws SQLException if an SQL error occurs.
     */
    @Override
    public void saveToDatabase(Connection conn) throws SQLException {
        // Insert the geometry type and creation timestamp into the geometries table.
        String geometrySql = "INSERT INTO geometries (type, created_at) VALUES (?, NOW())";
        try (PreparedStatement stmt = conn.prepareStatement(geometrySql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, "Rectangle"); // Specify the geometry type.
            stmt.executeUpdate();

            // Retrieve the generated ID for the geometry.
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                id = generatedKeys.getInt(1); // Store the generated ID.
            }

            // Insert the rectangle-specific data into the rectangles table.
            String rectangleSql = "INSERT INTO rectangles (id, x, y, width, height) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement rectStmt = conn.prepareStatement(rectangleSql)) {
                rectStmt.setInt(1, id);                 // Set the rectangle ID.
                rectStmt.setInt(2, x);                  // Set the x-coordinate of the top-left corner.
                rectStmt.setInt(3, y);                  // Set the y-coordinate of the top-left corner.
                rectStmt.setInt(4, Math.abs(x2 - x));   // Calculate and set the width.
                rectStmt.setInt(5, Math.abs(y2 - y));   // Calculate and set the height.
                rectStmt.executeUpdate();               // Execute the insert statement.
            }
        }
    }

    /**
     * Returns the type of this geometry (i.e., "Rectangle").
     * 
     * @return A string representing the geometry type.
     */
    @Override
    public String getType() {
        return "Rectangle";
    }

    /**
     * Loads a rectangle geometry from the provided ResultSet.
     * 
     * @param rs ResultSet containing the geometry data.
     * @return A Rectangle object initialized with the data from the ResultSet.
     * @throws SQLException if an SQL error occurs.
     */
    @Override
    public Geometry loadFromDatabase(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");          // Retrieve the rectangle's ID.
        int x = rs.getInt("x");            // Retrieve the x-coordinate of the top-left corner.
        int y = rs.getInt("y");            // Retrieve the y-coordinate of the top-left corner.
        int width = rs.getInt("width");    // Retrieve the width.
        int height = rs.getInt("height");  // Retrieve the height.

        // Create a Rectangle object using the retrieved data.
        Rectangle rectangle = new Rectangle(x, y, x + width, y + height);
        rectangle.id = id;                 // Assign the ID to the rectangle object.
        return rectangle;                  // Return the Rectangle object.
    }

    /**
     * Updates the rectangle's data in the database.
     * 
     * @param conn Database connection.
     * @throws SQLException if an SQL error occurs.
     */
    @Override
    public void updateGeometryInDatabase(Connection conn) throws SQLException {
        // SQL query to update rectangle data in the database.
        String sql = "UPDATE rectangles SET x = ?, y = ?, width = ?, height = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, x);                  // Update the x-coordinate of the top-left corner.
            stmt.setInt(2, y);                  // Update the y-coordinate of the top-left corner.
            stmt.setInt(3, Math.abs(x2 - x));   // Calculate and update the width.
            stmt.setInt(4, Math.abs(y2 - y));   // Calculate and update the height.
            stmt.setInt(5, id);                 // Specify the rectangle ID for the update.
            stmt.executeUpdate();               // Execute the update statement.
        }
    }
}
