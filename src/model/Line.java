package model;

import java.awt.Graphics;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Line Class extends Geometry to represent a line geometry.
 * It includes methods to draw the line, save it to a database, load it from a database,
 * update its data in the database, and retrieve its type.
 * @author Kisal/Ravi
 * Date: 26 December 2024
 */
public class Line extends Geometry {

    /**
     * Constructor to initialize the Line with starting and ending coordinates.
     * 
     * @param x  Starting x-coordinate.
     * @param y  Starting y-coordinate.
     * @param x2 Ending x-coordinate.
     * @param y2 Ending y-coordinate.
     */
    public Line(int x, int y, int x2, int y2) {
        super(x, y, x2, y2);
    }

    /**
     * Draws the line on a graphical canvas.
     * 
     * @param g Graphics object used for rendering.
     */
    @Override
    public void draw(Graphics g) {
        g.drawLine(x, y, x2, y2); // Use the Graphics object to draw a line.
    }

    /**
     * Saves the line geometry to the database. 
     * This method inserts the geometry type into the `geometries` table and 
     * stores the line-specific data (coordinates) into the `line` table.
     * 
     * @param conn Database connection.
     * @throws SQLException if an SQL error occurs.
     */
    @Override
    public void saveToDatabase(Connection conn) throws SQLException {
        // Insert the geometry type and creation timestamp into the geometries table.
        String geometrySql = "INSERT INTO geometries (type, created_at) VALUES (?, NOW())";
        try (PreparedStatement stmt = conn.prepareStatement(geometrySql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, "Line"); // Specify the geometry type.
            stmt.executeUpdate();

            // Retrieve the generated ID for the geometry.
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                id = generatedKeys.getInt(1); // Store the generated ID.
            }

            // Insert the line-specific data into the line table.
            String lineSql = "INSERT INTO line (id, x1, y1, x2, y2) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement lineStmt = conn.prepareStatement(lineSql)) {
                lineStmt.setInt(1, id);
                lineStmt.setInt(2, x);
                lineStmt.setInt(3, y);
                lineStmt.setInt(4, x2);
                lineStmt.setInt(5, y2);
                lineStmt.executeUpdate();
            }
        }
    }

    /**
     * Returns the type of this geometry (i.e., "Line").
     * 
     * @return A string representing the geometry type.
     */
    @Override
    public String getType() {
        return "Line";
    }

    /**
     * Loads a line geometry from the provided ResultSet.
     * 
     * @param rs ResultSet containing the geometry data.
     * @return A Line object initialized with the data from the ResultSet.
     * @throws SQLException if an SQL error occurs.
     */
    @Override
    public Geometry loadFromDatabase(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");   // Retrieve the line's ID.
        int x1 = rs.getInt("x1");  // Retrieve the starting x-coordinate.
        int y1 = rs.getInt("y1");  // Retrieve the starting y-coordinate.
        int x2 = rs.getInt("x2");  // Retrieve the ending x-coordinate.
        int y2 = rs.getInt("y2");  // Retrieve the ending y-coordinate.
        
        Line line = new Line(x1, y1, x2, y2); // Create a Line object.
        line.id = id; // Assign the ID to the line object.
        return line;  // Return the Line object.
    }

    /**
     * Updates the line's data in the database.
     * 
     * @param conn Database connection.
     * @throws SQLException if an SQL error occurs.
     */
    @Override
    public void updateGeometryInDatabase(Connection conn) throws SQLException {
        // SQL query to update line data in the database.
        String sql = "UPDATE line SET x1 = ?, y1 = ?, x2 = ?, y2 = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, x);   // Update the starting x-coordinate.
            stmt.setInt(2, y);   // Update the starting y-coordinate.
            stmt.setInt(3, x2);  // Update the ending x-coordinate.
            stmt.setInt(4, y2);  // Update the ending y-coordinate.
            stmt.setInt(5, id);  // Specify the line ID for the update.
            stmt.executeUpdate(); // Execute the update statement.
        }
    }
}
