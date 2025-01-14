package test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

//DAO Class to handle database connection and saving geometry objects
class GeometryController {
 protected Connection conn;

 public GeometryController() throws SQLException {
 	conn = DatabaseConnection.getConnection();
 }

 public void saveGeometry(Geometry geometry) throws SQLException {
     geometry.saveToDatabase(conn);
 }

 public List<Geometry> loadGeometries() throws SQLException {
     List<Geometry> geometries = new ArrayList<>();

     String rectangleSql = "SELECT id, x, y, width, height FROM rectangles";
     try (Statement stmt = conn.createStatement();
          ResultSet rs = stmt.executeQuery(rectangleSql)) {
         while (rs.next()) {
             int id = rs.getInt("id");
             int x = rs.getInt("x");
             int y = rs.getInt("y");
             int width = rs.getInt("width");
             int height = rs.getInt("height");
             Rectangle rectangle = new Rectangle(x, y, x + width, y + height);
             rectangle.id = id; // Assign id
             geometries.add(rectangle);
         }
     }

     String lineSql = "SELECT id, x1, y1, x2, y2 FROM line";
     try (Statement stmt = conn.createStatement();
          ResultSet rs = stmt.executeQuery(lineSql)) {
         while (rs.next()) {
         	int id = rs.getInt("id");
             int x1 = rs.getInt("x1");
             int y1 = rs.getInt("y1");
             int x2 = rs.getInt("x2");
             int y2 = rs.getInt("y2");
             Line line = new Line(x1, y1, x2, y2);
             line.id = id; // Assign id
             geometries.add(line);
         }
     }
     
  // Load Points
     String pointSql = "SELECT id, x, y FROM points";
     try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(pointSql)) {
         while (rs.next()) {
             int id = rs.getInt("id");
             int x = rs.getInt("x");
             int y = rs.getInt("y");
             Point point = new Point(x, y);
             point.id = id;
             geometries.add(point);
         }
     }
     
  // Load Triangles
     String triangleSql = "SELECT id, x, y, x2, y2, x3, y3 FROM triangles";
     try (Statement stmt = conn.createStatement();
          ResultSet rs = stmt.executeQuery(triangleSql)) {
         while (rs.next()) {
             int id = rs.getInt("id");
             int x = rs.getInt("x");
             int y = rs.getInt("y");
             int x2 = rs.getInt("x2");
             int y2 = rs.getInt("y2");
             int x3 = rs.getInt("x3");
             int y3 = rs.getInt("y3");
             Triangle triangle = new Triangle(x, y, x2, y2, x3, y3);
             triangle.id = id;
             geometries.add(triangle);
         }
     }

     return geometries;
 }

 public void close() throws SQLException {
     conn.close();
 }
 
 
}