package test;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class MainWindow extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	JMenuBar menuBar;
	JMenu fileMenu, editMenu, extrasMenu, graphicMenu, helpMenu, importSubMenu, exportSubMenu;
	JMenuItem newMenuItem, openMenuItem, csvSubMenuItem, imageSubMenuItem,csvSubMenuItem2, imageSubMenuItem2, saveMenuItem, exitMenuItem, 
	          cutMenuItem, copyMenuItem, pasteMenuItem, selectAllMenuItem, checkboxMenuItemShow, 
	          checkboxMenuItemHide, radioButtonEngMenuItem, radioButtonDeMenuItem, colorChooserMenuItem, 
	          gisToolMenuItem, userRegistrationMenuItem, userLoginMenuItem;
	JPopupMenu popupMenu;
	JToolBar toolbar;
	JButton newButton, openButton, exportCsvButton, saveButton, viewButton, exitButton, shapeButton, pointButton, lineButton, triangleButton, rectButton, rectSelectButton, moveButton, eraseButton, editButton;
//	JTextArea ta;
	JLabel imageLabel;
	// Define a JLabel for username display
    private JLabel usernameLabel;

private DrawPanel drawPannel;

private MapPannel mapPannel;
	
	
	// Add the main method here.
		public static void main(String[] args) {
			// Ensure the GUI runs on the Event Dispatch Thread (EDT)
			EventQueue.invokeLater(() -> {
				MainWindow frame = new MainWindow();
				frame.setVisible(true);
			});
		}

	public MainWindow() {
		setTitle("Vortex Manager");
		menuBar = new JMenuBar();
		
		// Initialize the shared DrawPanel instance
		drawPannel = new DrawPanel();

//		ta = new JTextArea();
//		ta.setBounds(10, 300, 500, 600);

		imageLabel = new JLabel("");
		imageLabel.setBounds(10, 10, 300, 200);
		imageLabel.setBackground(getForeground().WHITE);

		fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);

		newMenuItem = new JMenuItem("New", new ImageIcon(getClass().getResource("new.png")));
		newMenuItem.setMnemonic(KeyEvent.VK_N);
		newMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		newMenuItem.addActionListener(this);

		openMenuItem = new JMenuItem("Import Data", new ImageIcon(getClass().getResource("import_csv.png")));
		openMenuItem.setMnemonic(KeyEvent.VK_O);
		openMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		openMenuItem.addActionListener(this);

		saveMenuItem = new JMenuItem("Save", new ImageIcon(getClass().getResource("save.png")));
		saveMenuItem.setMnemonic(KeyEvent.VK_S);
		saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		saveMenuItem.addActionListener(this);

		exitMenuItem = new JMenuItem("Exit", new ImageIcon(getClass().getResource("exit.png")));
		exitMenuItem.setMnemonic(KeyEvent.VK_E);
		exitMenuItem.setToolTipText("Exit application");
		exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_DOWN_MASK));
		exitMenuItem.addActionListener((event) -> System.exit(0));

		importSubMenu = new JMenu("Import");
		csvSubMenuItem = new JMenuItem("CSV");
		csvSubMenuItem.addActionListener(this);
		imageSubMenuItem = new JMenuItem("Shapefile");
		imageSubMenuItem.addActionListener(this);
		importSubMenu.add(csvSubMenuItem);
		importSubMenu.add(imageSubMenuItem);
		
		exportSubMenu = new JMenu("Export");
		csvSubMenuItem2 = new JMenuItem("CSV");
		csvSubMenuItem2.addActionListener(this);
		imageSubMenuItem2 = new JMenuItem("Shapefile");
		imageSubMenuItem2.addActionListener(this);
		exportSubMenu.add(csvSubMenuItem2);
		exportSubMenu.add(imageSubMenuItem2);

		fileMenu.add(newMenuItem);
		fileMenu.add(openMenuItem);
		fileMenu.add(importSubMenu);
		fileMenu.add(exportSubMenu);
		fileMenu.add(saveMenuItem);
		fileMenu.addSeparator();
		fileMenu.add(exitMenuItem);

		editMenu = new JMenu("Edit");
		editMenu.setMnemonic(KeyEvent.VK_E);

		cutMenuItem = new JMenuItem("Cut", KeyEvent.VK_T);
		cutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
		cutMenuItem.addActionListener(this);
		copyMenuItem = new JMenuItem("Copy", KeyEvent.VK_C);
		copyMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
		copyMenuItem.addActionListener(this);
		pasteMenuItem = new JMenuItem("Paste", KeyEvent.VK_P);
		pasteMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
		pasteMenuItem.addActionListener(this);
		selectAllMenuItem = new JMenuItem("Select All", KeyEvent.VK_A);
		selectAllMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
		selectAllMenuItem.addActionListener(this);

		editMenu.add(cutMenuItem);
		editMenu.add(copyMenuItem);
		editMenu.add(pasteMenuItem);
		editMenu.addSeparator();
		editMenu.add(selectAllMenuItem);

		extrasMenu = new JMenu("Extras");
		extrasMenu.setMnemonic(KeyEvent.VK_T);
		ButtonGroup bgCB = new ButtonGroup();
		checkboxMenuItemShow = new JCheckBoxMenuItem("Show Toolbar", true);
		checkboxMenuItemShow.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getSource() == checkboxMenuItemShow) {
					toolbar.setVisible(true);
				}
			}
		});

		extrasMenu.add(checkboxMenuItemShow);
		bgCB.add(checkboxMenuItemShow);

		checkboxMenuItemHide = new JCheckBoxMenuItem("Hide Toolbar");
		checkboxMenuItemHide.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getSource() == checkboxMenuItemHide) {
					toolbar.setVisible(false);
				}
			}
		});
		extrasMenu.add(checkboxMenuItemHide);
		bgCB.add(checkboxMenuItemHide);
		extrasMenu.addSeparator();

		ButtonGroup bgRB = new ButtonGroup();
		radioButtonEngMenuItem = new JRadioButtonMenuItem("English",true);
		extrasMenu.add(radioButtonEngMenuItem);
		bgRB.add(radioButtonEngMenuItem);
		radioButtonDeMenuItem = new JRadioButtonMenuItem("Deutsch");
		extrasMenu.add(radioButtonDeMenuItem);
		bgRB.add(radioButtonDeMenuItem);
		extrasMenu.addSeparator();
		colorChooserMenuItem = new JMenuItem("Change Text Color");
		colorChooserMenuItem.addActionListener(this);
		extrasMenu.add(colorChooserMenuItem);
		extrasMenu.addSeparator();
		
		gisToolMenuItem = new JMenuItem("GIS Tool");
		gisToolMenuItem.addActionListener(this);
		extrasMenu.add(gisToolMenuItem);
		
		extrasMenu.addSeparator();
		userRegistrationMenuItem = new JMenuItem("User Registration");
		userRegistrationMenuItem.addActionListener(this);
		extrasMenu.add(userRegistrationMenuItem);
		extrasMenu.addSeparator();
		userLoginMenuItem = new JMenuItem("User Login");
		userLoginMenuItem.addActionListener(this);
		extrasMenu.add(userLoginMenuItem);
		
		//Java Graphic
		graphicMenu = new JMenu("Graphic");
		graphicMenu.setMnemonic(KeyEvent.VK_H);
		//###add MenuListener

		helpMenu = new JMenu("Help");
		helpMenu.setMnemonic(KeyEvent.VK_H);
		helpMenu.addMenuListener(new MenuListener() {
			@Override
			public void menuCanceled(MenuEvent arg0) {
			}

			@Override
			public void menuDeselected(MenuEvent arg0) {
			}

			@Override
			public void menuSelected(MenuEvent arg0) {
				JOptionPane.showMessageDialog(getContentPane(),
						"This is a application for\n" + "managing your spatial data \n"+
				"with ease and reliable way");
			}
		});

		menuBar.add(fileMenu);
		menuBar.add(editMenu);
		menuBar.add(extrasMenu);
		menuBar.add(Box.createHorizontalGlue());
		menuBar.add(helpMenu);
		setJMenuBar(menuBar);

		popupMenu = new JPopupMenu();
		JMenuItem maximizeMenuItem = new JMenuItem("Maximize");
		maximizeMenuItem.addActionListener((e) -> {
			if (getExtendedState() != JFrame.MAXIMIZED_BOTH) {
				setExtendedState(JFrame.MAXIMIZED_BOTH);
				maximizeMenuItem.setEnabled(false);
			}
		});

		JMenuItem minimizeMenuItem = new JMenuItem("Minimize");
		minimizeMenuItem.addActionListener((e) -> {
			if (getExtendedState() != JFrame.NORMAL) {
				setExtendedState(JFrame.NORMAL);
				minimizeMenuItem.setEnabled(false);
			}
		});

		JMenuItem quitMenuItem = new JMenuItem("Quit");
		quitMenuItem.addActionListener((e) -> System.exit(0));

		popupMenu.add(maximizeMenuItem);
		popupMenu.add(minimizeMenuItem);
		popupMenu.add(quitMenuItem);

		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {

				if (getExtendedState() != JFrame.MAXIMIZED_BOTH) {
					maximizeMenuItem.setEnabled(true);
					minimizeMenuItem.setEnabled(false);
				} else if (getExtendedState() == JFrame.MAXIMIZED_BOTH) {
					minimizeMenuItem.setEnabled(true);
					maximizeMenuItem.setEnabled(false);
				}
				if (e.getButton() == MouseEvent.BUTTON3) {
					popupMenu.show(e.getComponent(), e.getX(), e.getY());
				}
			}
		});

		toolbar = new JToolBar();
		newButton = new JButton(new ImageIcon(getClass().getResource("new.png")));
		newButton.addActionListener(this);
		openButton = new JButton(new ImageIcon(getClass().getResource("import_csv.png")));
		openButton.addActionListener(this);
		exportCsvButton = new JButton(new ImageIcon(getClass().getResource("export_csv.png")));
		exportCsvButton.addActionListener(this);
		saveButton = new JButton(new ImageIcon(getClass().getResource("save.png")));
		saveButton.addActionListener(this);
		viewButton = new JButton(new ImageIcon(getClass().getResource("view.png")));
		viewButton.addActionListener(this);
		exitButton = new JButton(new ImageIcon(getClass().getResource("exit.png")));
		exitButton.addActionListener((e) -> System.exit(0));
		
		shapeButton = new JButton(new ImageIcon(getClass().getResource("shape2.png")));
		shapeButton.addActionListener(this);
		
		// Add JLabel to show username
        usernameLabel = new JLabel("User: " + UserLogin.loggedInUser); // Assuming loggedInUser is a static variable holding the username
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        usernameLabel.setForeground(new Color(70, 130, 180));
        usernameLabel.setHorizontalAlignment(SwingConstants.RIGHT);  // Align right
        
     // Load the user icon image
        ImageIcon userIcon = new ImageIcon(getClass().getResource("user.png")); // Replace with your icon file path

        // Update the usernameLabel to display the icon instead of text
        usernameLabel.setIcon(userIcon);
        usernameLabel.setText(UserLogin.loggedInUser); // Clear any existing text

		toolbar.add(newButton);
		toolbar.add(openButton);
		toolbar.add(exportCsvButton);
		toolbar.add(saveButton);
		toolbar.add(viewButton);
		toolbar.add(exitButton);
		toolbar.add(shapeButton);
		
		
		// Add the usernameLabel to the toolbar on the right side
	    toolbar.add(Box.createHorizontalGlue());  // Pushes the label to the far right
	    toolbar.add(usernameLabel);  // Add username label to the right of the toolbar

	    // Set the toolbar layout to make it flexible
	    toolbar.setLayout(new BoxLayout(toolbar, BoxLayout.X_AXIS));
        
        
        

		add(toolbar, BorderLayout.NORTH);
		
		 
		
		
		toolbar = new JToolBar();
		toolbar.setOrientation(JToolBar.VERTICAL);
		pointButton = new JButton(new ImageIcon(getClass().getResource("point.png")));
		pointButton.addActionListener(e -> drawPannel.setDrawingMode("POINT"));
		lineButton = new JButton(new ImageIcon(getClass().getResource("line.png")));
		lineButton.addActionListener(e -> drawPannel.setDrawingMode("LINE"));
		triangleButton = new JButton(new ImageIcon(getClass().getResource("triangle.png")));
		triangleButton.addActionListener(e -> drawPannel.setDrawingMode("TRIANGLE"));
		rectButton = new JButton(new ImageIcon(getClass().getResource("rectangle.png")));
		rectButton.addActionListener(e -> drawPannel.setDrawingMode("RECTANGLE"));
		
		moveButton = new JButton(new ImageIcon(getClass().getResource("move.png")));
		moveButton.addActionListener(e -> drawPannel.setDrawingMode("MOVE"));
		editButton = new JButton(new ImageIcon(getClass().getResource("edit.png")));
		editButton.addActionListener((e) -> System.exit(0));
		eraseButton = new JButton(new ImageIcon(getClass().getResource("erase.png")));
		eraseButton.addActionListener(e -> drawPannel.setDrawingMode("DELETE"));
		rectSelectButton = new JButton(new ImageIcon(getClass().getResource("rectangle_selection.png")));
		rectSelectButton.addActionListener(e -> drawPannel.setDrawingMode("RANGE_QUERY"));
		
		toolbar.add(pointButton);
		toolbar.add(lineButton);
		toolbar.add(triangleButton);
		toolbar.add(rectButton);
		toolbar.add(moveButton);
		toolbar.add(editButton);
		toolbar.add(eraseButton);
		toolbar.add(rectSelectButton);
		add(toolbar, BorderLayout.WEST);
		
		add(imageLabel, BorderLayout.CENTER);
		
		add(drawPannel, BorderLayout.CENTER);
		
		
//		add(ta, BorderLayout.SOUTH);
		setBounds(450, 170, 610, 600);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == newMenuItem || e.getSource() == newButton) {
			mapPannel.setVisible(false);
			drawPannel.setVisible(true);
		}
		if (e.getSource() == openMenuItem || e.getSource() == csvSubMenuItem || e.getSource() == openButton) {
			CSVHandler csvHandler = new CSVHandler();
			drawPannel.importGeometriesFromCSV(csvHandler);
	        
		}
		
		if (e.getSource() == csvSubMenuItem2 || e.getSource() == exportCsvButton) {
			CSVHandler csvHandler = new CSVHandler();
			drawPannel.exportGeometriesToCSV(csvHandler);
		}
		
		
		if (e.getSource() == imageSubMenuItem || e.getSource() == shapeButton) {
			
			mapPannel = new MapPannel();
			add(mapPannel, BorderLayout.CENTER);
			// Initialize the map separately
			mapPannel.initialize();
			
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileFilter(new FileNameExtensionFilter("Shapefiles", "shp"));
	        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
	            mapPannel.loadShapefile(fileChooser.getSelectedFile());
	        }
	        
	        
	        drawPannel.setVisible(false);
		}
		
	
		if (e.getSource() == saveMenuItem || e.getSource() == saveButton) {
			drawPannel.saveGeometry();
		}
		
		if (e.getSource() == viewButton) {
			drawPannel.loadGeometries();
		}
		
	}
}