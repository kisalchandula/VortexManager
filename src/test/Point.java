package test;

import java.awt.Graphics;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

//Point Class extends Geometry
class Point extends Geometry {
public Point(int x, int y) {
   super(x, y, x, y); // A point has no width or height, just x and y coordinates
}

@Override
public void draw(Graphics g) {
   g.fillOval(x - 5, y - 5, 10, 10); // Draw a small circle for the point
}

@Override
public void saveToDatabase(Connection conn) throws SQLException {
   String geometrySql = "INSERT INTO geometries (type, created_at) VALUES (?, NOW())";
   try (PreparedStatement stmt = conn.prepareStatement(geometrySql, Statement.RETURN_GENERATED_KEYS)) {
       stmt.setString(1, "Point");
       stmt.executeUpdate();

       ResultSet generatedKeys = stmt.getGeneratedKeys();
       if (generatedKeys.next()) {
           id = generatedKeys.getInt(1);
       }

       String pointSql = "INSERT INTO points (id, x, y) VALUES (?, ?, ?)";
       try (PreparedStatement pointStmt = conn.prepareStatement(pointSql)) {
           pointStmt.setInt(1, id);
           pointStmt.setInt(2, x);
           pointStmt.setInt(3, y);
           pointStmt.executeUpdate();
       }
   }
}

@Override
public String getType() {
   return "Point";
}
}