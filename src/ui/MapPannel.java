package ui;

import org.geotools.map.MapContent;
import org.geotools.map.Layer;
import org.geotools.map.FeatureLayer;
import org.geotools.swing.JMapPane;
import org.geotools.swing.action.*;
import org.geotools.swing.tool.InfoTool;
import org.geotools.swing.tool.PanTool;
import org.geotools.swing.tool.ZoomInTool;
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
    /**
     * @author Sandys
     * Date: 05 January 2025
     * 
     * This class is responsible for creating and displaying a map panel using GeoTools for GIS data visualization.
     * It includes the setup of a map pane, toolbar actions (zoom, pan, info), and a status bar displaying 
     * coordinates and CRS (Coordinate Reference System) information.
     */
    private static final long serialVersionUID = 1L;
    private JMapPane mapPane;
    private MapContent mapContent;
    private JToolBar toolBar;
    private JLabel crsLabel;
    private JLabel coordLabel;

    public MapPannel() {
        setLayout(new BorderLayout());  // Set layout to BorderLayout for the panel
        createDefaultToolBar();         // Initialize the toolbar
        createStatusBar();              // Initialize the status bar to display CRS and coordinates
    }
    
    // Initializes the map content and pane.
    public void initialize() {
        initializeMap();
    }

    // Setup map content and map pane for rendering.
    private void initializeMap() {
        mapContent = new MapContent();  // Create a new MapContent object to store layers
        mapContent.setTitle("GIS Data Map View");  // Set a title for the map content

        mapPane = new JMapPane(mapContent);  // Create a map pane with the map content
        mapPane.setBackground(Color.WHITE);  // Set background color to white

        // Set default cursor tool as pan (to move the map around)
        mapPane.setCursorTool(new PanTool());

        // Add mouse motion listener to track the coordinates as the user moves the mouse over the map
        mapPane.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                updateCoordinates(e.getX(), e.getY());  // Update coordinates based on mouse position
            }
        });

        // Add the map pane to the panel's center region
        add(mapPane, BorderLayout.CENTER);
    }

    // Setup a default toolbar with pan, zoom, and info tools.
    private void createDefaultToolBar() {
        toolBar = new JToolBar();  // Create a new toolbar
        
        mapPane = new JMapPane(mapContent);  // Initialize map pane for toolbar buttons
        
        // Add GeoTools default actions to buttons
        JButton panButton = new JButton(new PanAction(mapPane));  // Pan button to move map
        JButton zoomInButton = new JButton(new ZoomInAction(mapPane));  // Zoom in button
        JButton zoomOutButton = new JButton(new ZoomOutAction(mapPane));  // Zoom out button
        JButton infoButton = new JButton(new InfoAction(mapPane));  // Info button to get information about features

        // Add buttons to the toolbar
        toolBar.add(panButton);
        toolBar.add(zoomInButton);
        toolBar.add(zoomOutButton);
        toolBar.add(infoButton);

        // Add listeners to switch between tools
        panButton.addActionListener(e -> mapPane.setCursorTool(new PanTool()));  // Switch to pan tool
        zoomInButton.addActionListener(e -> mapPane.setCursorTool(new ZoomInTool()));  // Switch to zoom-in tool
        zoomOutButton.addActionListener(e -> {
            if (mapPane != null && mapPane.getDisplayArea() != null) {
                // Zoom out by adjusting the display area
                ReferencedEnvelope displayArea = mapPane.getDisplayArea();
                double scaleFactor = 2.0; // Zoom out by a factor of 2

                double centerX = displayArea.getMedian(0);
                double centerY = displayArea.getMedian(1);
                double width = displayArea.getWidth() * scaleFactor;
                double height = displayArea.getHeight() * scaleFactor;

                // Create a new area with adjusted bounds
                ReferencedEnvelope newArea = new ReferencedEnvelope(
                    centerX - width / 2,
                    centerX + width / 2,
                    centerY - height / 2,
                    centerY + height / 2,
                    displayArea.getCoordinateReferenceSystem()
                );

                mapPane.setDisplayArea(newArea);  // Set new display area (zoom out)
            } else {
                System.err.println("Error: mapPane or displayArea is null");  // Error message if map or display area is null
            }
        });
        infoButton.addActionListener(e -> mapPane.setCursorTool(new InfoTool()));  // Switch to info tool
        
        // Add the toolbar to the top region of the panel
        add(toolBar, BorderLayout.NORTH);
    }

    // Create the status bar to display CRS and coordinates.
    private void createStatusBar() {
        JPanel statusBar = new JPanel(new BorderLayout());  // Create a panel with BorderLayout for status bar
        statusBar.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));  // Add padding around the status bar

        crsLabel = new JLabel("CRS: ");  // Label for Coordinate Reference System
        coordLabel = new JLabel("Coordinates: ");  // Label for displaying mouse coordinates

        // Add the labels to the status bar (left: CRS, right: coordinates)
        statusBar.add(crsLabel, BorderLayout.WEST);
        statusBar.add(coordLabel, BorderLayout.EAST);

        // Add the status bar to the bottom region of the panel
        add(statusBar, BorderLayout.SOUTH);
    }

    // Load a shapefile and add its features to the map.
    public void loadShapefile(File shapefile) {
        try {
            // Load shapefile data from the given file
            FileDataStore store = FileDataStoreFinder.getDataStore(shapefile);
            SimpleFeatureSource featureSource = store.getFeatureSource();
            
            // Apply a simple style to the shapefile features
            Style style = SLD.createSimpleStyle(featureSource.getSchema());
            Layer layer = new FeatureLayer(featureSource, style);  // Create a layer with the feature source and style
            mapContent.addLayer(layer);  // Add the layer to the map content

            // Update the CRS label with the current CRS of the map
            CoordinateReferenceSystem crs = mapContent.getCoordinateReferenceSystem();
            crsLabel.setText("CRS: " + (crs != null ? crs.getName().toString() : "Unknown"));
        } catch (Exception e) {
            // Display an error message if something goes wrong while loading the shapefile
            JOptionPane.showMessageDialog(this, "Error loading shapefile: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Update the coordinates displayed in the status bar when the mouse moves over the map.
    private void updateCoordinates(int x, int y) {
        try {
            if (mapPane.getDisplayArea() != null) {
                // Get the map's display area
                ReferencedEnvelope envelope = mapPane.getDisplayArea();
                
                // Convert mouse coordinates to map coordinates
                double mapX = envelope.getMinX() + x * envelope.getWidth() / mapPane.getWidth();
                double mapY = envelope.getMaxY() - y * envelope.getHeight() / mapPane.getHeight();

                // Format and display the coordinates
                DecimalFormat format = new DecimalFormat("#.####");
                coordLabel.setText("Coordinates: X = " + format.format(mapX) + ", Y = " + format.format(mapY));
            }
        } catch (Exception e) {
            coordLabel.setText("Coordinates: Error");  // Display error message if coordinate update fails
        }
    }

    // Dispose of the map content when done.
    public void disposeMap() {
        if (mapContent != null) {
            mapContent.dispose();  // Release resources related to map content
        }
    }
}
