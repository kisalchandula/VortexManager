package test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

//Geometry Controller Class control data with UI
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

        String[] geometryTypes = {"rectangles", "line", "points", "triangles"};
        for (String geometryType : geometryTypes) {
            String sql = "SELECT * FROM " + geometryType;
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    Geometry geometry = null;
                    if (geometryType.equals("rectangles")) {
                        geometry = new Rectangle(0, 0, 0, 0);
                    } else if (geometryType.equals("line")) {
                        geometry = new Line(0, 0, 0, 0);
                    } else if (geometryType.equals("points")) {
                        geometry = new Point(0, 0);
                    } else if (geometryType.equals("triangles")) {
                        geometry = new Triangle(0, 0, 0, 0, 0, 0);
                    }
                    geometry = geometry.loadFromDatabase(rs);
                    geometries.add(geometry);
                }
            }
        }
        return geometries;
    }
    
    public void updateGeometry(Geometry geometry) throws SQLException {
        geometry.updateGeometryInDatabase(conn); 
    }
    
    public void deleteGeometry(Geometry geometry) throws SQLException {
        geometry.deleteGeometryFromDatabase(conn); 
    }

    public void close() throws SQLException {
        conn.close();
    }
}
