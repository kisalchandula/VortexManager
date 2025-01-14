package test;

import java.awt.Graphics;
import java.sql.Connection;
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
 public abstract String getType(); // To get the type of the geometry (Rectangle or Line)
}