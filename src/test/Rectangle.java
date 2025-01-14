package test;

import java.awt.Graphics;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

//Rectangle Class extends Geometry
class Rectangle extends Geometry {
 public Rectangle(int x, int y, int x2, int y2) {
     super(x, y, x2, y2);
 }

 @Override
 public void draw(Graphics g) {
     g.drawRect(x, y, Math.abs(x2 - x), Math.abs(y2 - y));
 }

 @Override
 public void saveToDatabase(Connection conn) throws SQLException {
     String geometrySql = "INSERT INTO geometries (type, created_at) VALUES (?, NOW())";
     try (PreparedStatement stmt = conn.prepareStatement(geometrySql, Statement.RETURN_GENERATED_KEYS)) {
         stmt.setString(1, "Rectangle");
         stmt.executeUpdate();

         ResultSet generatedKeys = stmt.getGeneratedKeys();
         if (generatedKeys.next()) {
             id = generatedKeys.getInt(1);
         }

         String rectangleSql = "INSERT INTO rectangles (id, x, y, width, height) VALUES (?, ?, ?, ?, ?)";
         try (PreparedStatement rectStmt = conn.prepareStatement(rectangleSql)) {
             rectStmt.setInt(1, id);
             rectStmt.setInt(2, x);
             rectStmt.setInt(3, y);
             rectStmt.setInt(4, Math.abs(x2 - x)); // width
             rectStmt.setInt(5, Math.abs(y2 - y)); // height
             rectStmt.executeUpdate();
         }
     }
 }

 @Override
 public String getType() {
     return "Rectangle";
 }
}