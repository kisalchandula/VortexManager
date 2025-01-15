package test;

import java.awt.Graphics;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//Abstract Geometry Class
abstract class Geometry {
    int x, y, x2, y2;
    int id;

    public Geometry(int x, int y, int x2, int y2) {
        this.x = x;
        this.y = y;
        this.x2 = x2;
        this.y2 = y2;
    }

    public abstract void draw(Graphics g);
    
    public abstract void saveToDatabase(Connection conn) throws SQLException;
    public abstract Geometry loadFromDatabase(ResultSet rs) throws SQLException; 
    public abstract void updateGeometryInDatabase(Connection conn) throws SQLException;
    
    public void deleteGeometryFromDatabase(Connection conn) throws SQLException {
        String deleteGeometrySql = "DELETE FROM geometries WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(deleteGeometrySql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
    public abstract String getType();
}
