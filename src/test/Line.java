package test;

import java.awt.Graphics;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

//Line Class extends Geometry
class Line extends Geometry {
 public Line(int x, int y, int x2, int y2) {
     super(x, y, x2, y2);
 }

 @Override
 public void draw(Graphics g) {
     g.drawLine(x, y, x2, y2);
 }

 @Override
 public void saveToDatabase(Connection conn) throws SQLException {
     String geometrySql = "INSERT INTO geometries (type, created_at) VALUES (?, NOW())";
     try (PreparedStatement stmt = conn.prepareStatement(geometrySql, Statement.RETURN_GENERATED_KEYS)) {
         stmt.setString(1, "Line");
         stmt.executeUpdate();

         ResultSet generatedKeys = stmt.getGeneratedKeys();
         if (generatedKeys.next()) {
             id = generatedKeys.getInt(1);
         }

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

 @Override
 public String getType() {
     return "Line";
 }
}