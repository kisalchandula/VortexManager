package test;

import java.awt.Graphics;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

class Triangle extends Geometry {
    int x3; // Third point of the triangle
    int y3;

    public Triangle(int x, int y, int x2, int y2, int x3, int y3) {
        super(x, y, x2, y2); // x, y represent the first point; x2, y2 represent the second point
        this.x3 = x3;
        this.y3 = y3;
    }

    @Override
    public void draw(Graphics g) {
        // Draw the triangle by connecting the three points
        int[] xPoints = {x, x2, x3};
        int[] yPoints = {y, y2, y3};
        g.drawPolygon(xPoints, yPoints, 3);
    }

    @Override
    public void saveToDatabase(Connection conn) throws SQLException {
        // Insert into geometries table
        String geometrySql = "INSERT INTO geometries (type, created_at) VALUES (?, NOW())";
        try (PreparedStatement stmt = conn.prepareStatement(geometrySql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, "Triangle");
            stmt.executeUpdate();

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                id = generatedKeys.getInt(1);
            }

            // Insert into triangles table
            String triangleSql = "INSERT INTO triangles (id, x, y, x2, y2, x3, y3) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement triangleStmt = conn.prepareStatement(triangleSql)) {
                triangleStmt.setInt(1, id);
                triangleStmt.setInt(2, x);   // First point x
                triangleStmt.setInt(3, y);   // First point y
                triangleStmt.setInt(4, x2);  // Second point x
                triangleStmt.setInt(5, y2);  // Second point y
                triangleStmt.setInt(6, x3);  // Third point x
                triangleStmt.setInt(7, y3);  // Third point y
                triangleStmt.executeUpdate();
            }
        }
    }

    @Override
    public String getType() {
        return "Triangle";
    }

    // Load from database
    @Override
    public Geometry loadFromDatabase(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int x = rs.getInt("x");
        int y = rs.getInt("y");
        int x2 = rs.getInt("x2");
        int y2 = rs.getInt("y2");
        int x3 = rs.getInt("x3");
        int y3 = rs.getInt("y3");
        Triangle triangle = new Triangle(x, y, x2, y2, x3, y3);
        triangle.id = id;
        return triangle;
    }
    
    @Override
    public void updateGeometryInDatabase(Connection conn) throws SQLException {
        String sql = "UPDATE triangles SET x = ?, y = ?, x2 = ?, y2 = ?, x3 = ?, y3 = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, x);
            stmt.setInt(2, y);
            stmt.setInt(3, x2);
            stmt.setInt(4, y2);
            stmt.setInt(5, x3);
            stmt.setInt(6, y3);
            stmt.setInt(7, id);
            stmt.executeUpdate();
        }
    }
}
