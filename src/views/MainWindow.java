package views;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Locale;

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
	JButton newButton, openButton, saveButton, exitButton, pointButton, lineButton, triangleButton, rectButton, rectSelectButton, moveButton, eraseButton, editButton;
//	JTextArea ta;
	JLabel imageLabel;
	
	
	// Add the main method here.
		public static void main(String[] args) {
			// Ensure the GUI runs on the Event Dispatch Thread (EDT)
			EventQueue.invokeLater(() -> {
				MainWindow frame = new MainWindow();
				frame.setVisible(true);
			});
		}

	public MainWindow() {
		setTitle("Vortex Manager - Draw Pannel");
		menuBar = new JMenuBar();

//		ta = new JTextArea();
//		ta.setBounds(10, 300, 500, 600);

		imageLabel = new JLabel("");
		imageLabel.setBounds(10, 10, 300, 200);
		imageLabel.setBackground(getForeground().DARK_GRAY);

		fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);

		newMenuItem = new JMenuItem("New", new ImageIcon(getClass().getResource("new.png")));
		newMenuItem.setMnemonic(KeyEvent.VK_N);
		newMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		newMenuItem.addActionListener(this);

		openMenuItem = new JMenuItem("Open", new ImageIcon(getClass().getResource("open.png")));
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
						"This is a complete example of Java \n" + "Swing Menu, MenuItem, Dialogs, \n"+
				"Db Functionality " + "GeoTool Lib Use \n"+"Drawing Tool");
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
		openButton = new JButton(new ImageIcon(getClass().getResource("open.png")));
		openButton.addActionListener(this);
		saveButton = new JButton(new ImageIcon(getClass().getResource("save.png")));
		saveButton.addActionListener(this);
		exitButton = new JButton(new ImageIcon(getClass().getResource("exit.png")));
		exitButton.addActionListener((e) -> System.exit(0));

		toolbar.add(newButton);
		toolbar.add(openButton);
		toolbar.add(saveButton);
		toolbar.add(exitButton);

		add(toolbar, BorderLayout.NORTH);
		
		
		toolbar = new JToolBar();
		toolbar.setOrientation(JToolBar.VERTICAL);
		pointButton = new JButton(new ImageIcon(getClass().getResource("point.png")));
		pointButton.addActionListener(this);
		lineButton = new JButton(new ImageIcon(getClass().getResource("line.png")));
		lineButton.addActionListener(this);
		triangleButton = new JButton(new ImageIcon(getClass().getResource("triangle.png")));
		triangleButton.addActionListener(this);
		rectButton = new JButton(new ImageIcon(getClass().getResource("rectangle.png")));
		rectButton.addActionListener((e) -> System.exit(0));
		
		moveButton = new JButton(new ImageIcon(getClass().getResource("move.png")));
		moveButton.addActionListener((e) -> System.exit(0));
		editButton = new JButton(new ImageIcon(getClass().getResource("edit.png")));
		editButton.addActionListener((e) -> System.exit(0));
		eraseButton = new JButton(new ImageIcon(getClass().getResource("erase.png")));
		eraseButton.addActionListener((e) -> System.exit(0));
		rectSelectButton = new JButton(new ImageIcon(getClass().getResource("rectangle_selection.png")));
		rectSelectButton.addActionListener((e) -> System.exit(0));

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
		
		DrawPannel drawPannel = new DrawPannel();
		
		add(drawPannel, BorderLayout.CENTER);
		
		
//		add(ta, BorderLayout.SOUTH);
		setBounds(450, 170, 610, 600);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
//		if (e.getSource() == newMenuItem || e.getSource() == newButton) {
//			ta.setText("Add the new content here!");
//		}
		if (e.getSource() == openMenuItem || e.getSource() == csvSubMenuItem || e.getSource() == openButton) {
			JFileChooser fc = new JFileChooser();
			fc.setAcceptAllFileFilterUsed(false);
			FileNameExtensionFilter extFilter = new FileNameExtensionFilter("text file", "txt", "fin");
			fc.addChoosableFileFilter(extFilter);
			int i = fc.showOpenDialog(this);
			if (i == JFileChooser.APPROVE_OPTION) {
				File f = fc.getSelectedFile();
				String filepath = f.getPath();
				try {
					BufferedReader reader = new BufferedReader(new FileReader(filepath));
					String s1 = "", s2 = "";
					while ((s1 = reader.readLine()) != null) {
						s2 += s1 + "\n";
					}
//					ta.setText(s2);
					reader.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}

		if (e.getSource() == imageSubMenuItem) {
			JFileChooser fc = new JFileChooser();
			fc.setAcceptAllFileFilterUsed(false);
			FileNameExtensionFilter extFilter = new FileNameExtensionFilter("*.Images", "jpg", "jpeg", "gif", "png");
			fc.addChoosableFileFilter(extFilter);
			int i = fc.showOpenDialog(this);
			if (i == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				String path = file.getAbsolutePath();
				ImageIcon iconPath = new ImageIcon(path);
				Image image = iconPath.getImage();
				Image resizedImg = image.getScaledInstance(imageLabel.getWidth(), imageLabel.getHeight(),
						image.SCALE_SMOOTH);
				ImageIcon icon = new ImageIcon(resizedImg);
				imageLabel.setIcon(icon);
				add(imageLabel, BorderLayout.CENTER);
			}
		}

		if (e.getSource() == saveMenuItem || e.getSource() == saveButton) {
			JFileChooser fc = new JFileChooser();
			fc.setAcceptAllFileFilterUsed(false);
			FileNameExtensionFilter extFilter = new FileNameExtensionFilter("text file", "txt", "fin");
			fc.addChoosableFileFilter(extFilter);
			int i = fc.showSaveDialog(this);
//			if (i == JFileChooser.APPROVE_OPTION) {
//				File f = fc.getSelectedFile();
//				try {
//					BufferedWriter writer = new BufferedWriter(new FileWriter(f));
//					writer.write(ta.getText());
//					writer.close();
//				} catch (Exception ex) {
//					ex.printStackTrace();
//				}
//			}
		}

//		if (e.getSource() == cutMenuItem) {
//			ta.cut();
//		}
//		if (e.getSource() == copyMenuItem) {
//			ta.copy();
//		}
//		if (e.getSource() == pasteMenuItem) {
//			ta.paste();
//		}
//		if (e.getSource() == selectAllMenuItem) {
//			ta.selectAll();
//		}
//		if (e.getSource() == colorChooserMenuItem) {
//			Color c = JColorChooser.showDialog(null, "Choose a Color", ta.getForeground());
//			if (ta.getText() != null && c != null)
//				ta.setForeground(c);
//		}
		
//		if (e.getSource() == gisToolMenuItem) {
//			SwingGISTool gisTool = new SwingGISTool();
//			try {
//				gisTool.displayShapefile();
//			} catch (Exception e1) {
//				e1.printStackTrace();
//			}
//		}
		
//		if (e.getSource() == userRegistrationMenuItem) {
//			EventQueue.invokeLater(new Runnable() {
//				public void run() {
//					try {
//						UserRegistration userRegistration = new UserRegistration();
//						userRegistration.setVisible(true);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//			});
//		}
//		
//		if (e.getSource() == userLoginMenuItem) {
//			EventQueue.invokeLater(new Runnable() {
//				public void run() {
//					try {
//						UserLogin userLogin = new UserLogin();
//						userLogin.setVisible(true);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//			});
//		}
		
	}
}