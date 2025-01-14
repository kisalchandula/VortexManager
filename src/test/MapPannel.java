package test;

import org.geotools.map.MapContent;
import org.geotools.map.Layer;
import org.geotools.map.FeatureLayer;
import org.geotools.swing.JMapPane;
import org.geotools.swing.action.*;
import org.geotools.swing.tool.InfoTool;
import org.geotools.swing.tool.PanTool;
import org.geotools.swing.tool.ZoomInTool;
import org.geotools.swing.tool.ZoomOutTool;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.styling.SLD;
import org.geotools.styling.Style;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.text.DecimalFormat;

public class MapPannel extends JPanel {
    private JMapPane mapPane;
    private MapContent mapContent;
    private JToolBar toolBar;
    private JLabel crsLabel;
    private JLabel coordLabel;

    public MapPannel() {
        setLayout(new BorderLayout());
        createDefaultToolBar();
        createStatusBar();
    }
    
    public void initialize() {
        initializeMap();
    }

    private void initializeMap() {
        mapContent = new MapContent();
        mapContent.setTitle("GIS Data Map View");

        mapPane = new JMapPane(mapContent);
        mapPane.setBackground(Color.WHITE);

        // Set default cursor tool
        mapPane.setCursorTool(new PanTool());

        // Add mouse motion listener for coordinates
        mapPane.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                updateCoordinates(e.getX(), e.getY());
            }
        });

        add(mapPane, BorderLayout.CENTER);
    }


    private void createDefaultToolBar() {
        toolBar = new JToolBar();

        // Add default GeoTools toolbar actions
        JButton panButton = new JButton(new PanAction(mapPane));
        JButton zoomInButton = new JButton(new ZoomInAction(mapPane));
        JButton zoomOutButton = new JButton(new ZoomOutAction(mapPane));
        JButton infoButton = new JButton(new InfoAction(mapPane));

        toolBar.add(panButton);
        toolBar.add(zoomInButton);
        toolBar.add(zoomOutButton);
        toolBar.add(infoButton);

        // Add listeners to toggle tools
        panButton.addActionListener(e -> mapPane.setCursorTool(new PanTool()));
        zoomInButton.addActionListener(e -> mapPane.setCursorTool(new ZoomInTool()));
        zoomOutButton.addActionListener(e -> mapPane.setCursorTool(new ZoomOutTool()));
        infoButton.addActionListener(e -> mapPane.setCursorTool(new InfoTool()));
        
        add(toolBar, BorderLayout.NORTH);
    }


    private void createStatusBar() {
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        crsLabel = new JLabel("CRS: ");
        coordLabel = new JLabel("Coordinates: ");

        statusBar.add(crsLabel, BorderLayout.WEST);
        statusBar.add(coordLabel, BorderLayout.EAST);

        add(statusBar, BorderLayout.SOUTH);
    }

    public void loadShapefile(File shapefile) {
        try {
            FileDataStore store = FileDataStoreFinder.getDataStore(shapefile);
            SimpleFeatureSource featureSource = store.getFeatureSource();
            Style style = SLD.createSimpleStyle(featureSource.getSchema());
            Layer layer = new FeatureLayer(featureSource, style);
            mapContent.addLayer(layer);

            // Update CRS label
            CoordinateReferenceSystem crs = mapContent.getCoordinateReferenceSystem();
            crsLabel.setText("CRS: " + (crs != null ? crs.getName().toString() : "Unknown"));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading shapefile: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateCoordinates(int x, int y) {
        try {
            if (mapPane.getDisplayArea() != null) {
                ReferencedEnvelope envelope = mapPane.getDisplayArea();
                double mapX = envelope.getMinX() + x * envelope.getWidth() / mapPane.getWidth();
                double mapY = envelope.getMaxY() - y * envelope.getHeight() / mapPane.getHeight();

                DecimalFormat format = new DecimalFormat("#.####");
                coordLabel.setText("Coordinates: X = " + format.format(mapX) + ", Y = " + format.format(mapY));
            }
        } catch (Exception e) {
            coordLabel.setText("Coordinates: Error");
        }
    }

    public void disposeMap() {
        if (mapContent != null) {
            mapContent.dispose();
        }
    }
}
