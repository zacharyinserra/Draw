import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.filechooser.FileSystemView;

@SuppressWarnings("serial")
public class BasicLinePix extends JFrame {

	/**
	 * @param args
	 */
	private JPanel drawingPanel; // user draws here

	private Container cp;
	private JPanel statusBar;
	private JLabel statusLabel;// used to show informational messages


	private JMenuBar menuBar;
	private JMenu fileMenu;
	private JMenu drawMenu;
	private EventHandler eh;
	
	//arraylist of shapes
	private ArrayList<Shape> allShapes = new ArrayList<>();
	
	//list of colors for random color
	private Color colors[] = {Color.RED, Color.GREEN, Color.BLUE, Color.MAGENTA};
	
	private JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
	
	private enum Mode {
		LINE, RECTANGLE
	}
	
	private Mode drawMode = Mode.LINE;

	public static void main(String[] args) {
	
		BasicLinePix lp = new BasicLinePix();	
		
		lp.setVisible(true);
	}

	public BasicLinePix() {
		setTitle("Basic Line Pix 1.0");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		cp = getContentPane();
		cp.setLayout(new BorderLayout());
		
		eh = new EventHandler();

		drawingPanel = makeDrawingPanel();
		drawingPanel.addMouseListener(eh);
		drawingPanel.addMouseMotionListener(eh);

		statusBar = createStatusBar();

		cp.add(drawingPanel, BorderLayout.CENTER);
		cp.add(statusBar, BorderLayout.SOUTH);
		
		buildMenu();

		pack();
	}

	public void buildMenu() {	
		menuBar = new JMenuBar();
		fileMenu = new JMenu("File");
		drawMenu = new JMenu("Draw");
		
		JMenuItem menuItem = new JMenuItem("New");
		menuItem.addActionListener(eh);
		fileMenu.add(menuItem);

		menuItem = new JMenuItem("Open");
		menuItem.addActionListener(eh);
		fileMenu.add(menuItem);

		menuItem = new JMenuItem("Save");
		menuItem.addActionListener(eh);
		fileMenu.add(menuItem);

		menuItem = new JMenuItem("Exit");
		menuItem.addActionListener(eh);
		fileMenu.add(menuItem);
		
		menuItem = new JMenuItem("Line");
		menuItem.addActionListener(eh);
		drawMenu.add(menuItem);
		
		menuItem = new JMenuItem("Rectangle");
		menuItem.addActionListener(eh);
		drawMenu.add(menuItem);

		menuBar.add(fileMenu);
		menuBar.add(drawMenu);
		
		setJMenuBar(menuBar);
	}

	private JPanel makeDrawingPanel() {
		JPanel p = new JPanel();
		p.setPreferredSize(new Dimension(500, 400));
		p.setBackground(Color.WHITE);

		return p;
	}

	private JPanel createStatusBar() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		statusLabel = new JLabel("No message");
		panel.add(statusLabel, BorderLayout.CENTER);

		panel.setBorder(new BevelBorder(BevelBorder.LOWERED));

		return panel;
	}

	//this method overrides the paint method defined in JFrame
	//add code here for drawing the lines on the drawingPanel
	@Override
	public void paint(Graphics g) {
		super.paint(g);
	
		Graphics g1 = drawingPanel.getGraphics();

		// Send a message to each shape in the drawing to
		// draw itself on g1
		
		for (Shape s: allShapes) {
			s.draw(g1);
		}		
	}

	// Inner class - instances of this class handle action events
	private class EventHandler implements ActionListener, MouseListener, MouseMotionListener {

		private int startX, startY;   // line's start coordinates
		private int lastX, lastY;     // line's most recent end point
		private int X, Y;             // for rectangles
		private int width, height;    // width and height for rectangles
		public Color c;               // initializes color variable
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (arg0.getActionCommand().equals("Exit")) {
				statusLabel.setText("Exiting program...");
				System.exit(0);
			} 
			if (arg0.getActionCommand().equals("New")) { // clears list and drawing
				allShapes.clear();
				repaint();				
			}
			if (arg0.getActionCommand().equals("Open")) { // opens file chooser to allow user to pick txt file
				Graphics g = drawingPanel.getGraphics();
				int returnVal = jfc.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = jfc.getSelectedFile();
					System.out.println(file.getAbsolutePath());
					readLines(file);                      // reads file 
					repaint();                            // repaints drawing with saved lines/rectangles
					paint(g);
				}
			}
			if (arg0.getActionCommand().equals("Save")) { // opens file chooser allowing user to save shape list as
														  // as txt file
				int returnVal = jfc.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = jfc.getSelectedFile();
					System.out.println(file.getAbsolutePath());
					try {
						FileWriter fw = new FileWriter(file);
						BufferedWriter bw = new BufferedWriter(fw);
						// separates lines for file reader
						bw.write(allShapes.toString().replaceAll(", ", System.getProperty("line.separator")));
						bw.close();
						
					} catch (IOException e) {
						e.printStackTrace();
					}
				}	
			}
			// allows user to change mode from draw line to draw rectangle
			if (arg0.getActionCommand().equals("Line")) {
				drawMode = Mode.LINE;
			}
			if (arg0.getActionCommand().equals("Rectangle")) {
				drawMode = Mode.RECTANGLE;
			}
		}
		
		private void readLines(File file) {  //reads chosen txt file 
			try (BufferedReader br = new BufferedReader(new FileReader(file))) {
				String line;
				while ((line = br.readLine()) != null) {
					line = line.replace("[", "").replace("]", "");
					String[] stuff = line.trim().split(",");
					String id = stuff[0].trim();
					// checks for l or r to determine if saved shape is a line or rectangle
					// adds shape to shape list
					if (id.equals("l")) {
						int sx = Integer.parseInt(stuff[1]);
						int sy = Integer.parseInt(stuff[2]);
						int ex = Integer.parseInt(stuff[3]);
						int ey = Integer.parseInt(stuff[4]);
						Color c = new Color(Integer.parseInt(stuff[5]));
						Shape s = new Line(sx, sy, ex, ey,  c);
						allShapes.add(s);
					}
					if (id.equals("r")) {
						int px = Integer.parseInt(stuff[1]);
						int py = Integer.parseInt(stuff[2]);
						int w = Integer.parseInt(stuff[3]);
						int h = Integer.parseInt(stuff[4]);
						Color c = new Color(Integer.parseInt(stuff[5]));
						Shape s = new Rectangle(px, py, w, h, c);
						allShapes.add(s);
					}
					System.out.println(allShapes);
				}
			} catch (IOException e) {
				e.printStackTrace();		
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
				
			if ((e.getModifiers() & ActionEvent.CTRL_MASK) != ActionEvent.CTRL_MASK) { //checks control not held
				c = randomColor(colors);  //sets random color
				
				if (drawMode.equals(Mode.LINE)) { // if mode is line
					startX = e.getX();
					startY = e.getY();
					// initialize lastX, lastY
					lastX = startX;
					lastY = startY;
				}
				if (drawMode.equals(Mode.RECTANGLE)) { // if mode is rectangle
					startX = e.getX();
					startY = e.getY();
					lastX = startX;
					lastY = startY;
					X = Math.min(startX, lastX);
					Y = Math.min(startY, lastY);
					width = Math.abs(startX - lastX);
					height = Math.abs(startY - lastY);
				}
			}
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			
			if ((e.getModifiers() & ActionEvent.CTRL_MASK) != ActionEvent.CTRL_MASK) { // checks control is not held
				// Implement rubber-band cursor
				Graphics g = drawingPanel.getGraphics();
				g.setColor(c);
			
				g.setXORMode(drawingPanel.getBackground());
			
				// REDRAW the line that was drawn 
				// most recently during this drag
				// XOR mode means that yellow pixels turn black
				// essentially erasing the existing line
				if (drawMode.equals(Mode.LINE))	{             // draws line
					g.drawLine(startX, startY, lastX, lastY);
				}
				if (drawMode.equals(Mode.RECTANGLE)) {        // draws rectangle
					X = Math.min(startX, lastX);
					Y = Math.min(startY, lastY);
					width = Math.abs(startX - lastX);
					height = Math.abs(startY - lastY);
					g.drawRect(X, Y, width, height);
				}
				// draw line to current mouse position
				// XOR mode: yellow pixels become black
				// black pixels, like those from existing lines, temporarily become
				// yellow
				if (drawMode.equals(Mode.LINE)) {             // draws line
					g.drawLine(startX, startY, e.getX(), e.getY());
				}
				if (drawMode.equals(Mode.RECTANGLE)) {        // draw rectangle
					X = Math.min(startX, e.getX());
					Y = Math.min(startY, e.getY());
					width = Math.abs(startX - e.getX());
					height = Math.abs(startY - e.getY());
					g.drawRect(X, Y, width, height);
				}
				lastX = e.getX();
				lastY = e.getY();	
			}
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {

			if ((arg0.getModifiers() & ActionEvent.CTRL_MASK) != ActionEvent.CTRL_MASK) { // checks control not held
				//add lines and rectangles to allShapes
				if (drawMode == Mode.LINE)
					allShapes.add(new Line(startX, startY, arg0.getX(), arg0.getY(), c));
				else if (drawMode == Mode.RECTANGLE)
					allShapes.add(new Rectangle(startX, startY, arg0.getX(), arg0.getY(), c));
				System.out.println(allShapes);	
			}
		}

		@Override
		public void mouseClicked(MouseEvent arg0) {
			
			// checks control IS held
			// delivers x coord and y coord of click to contains function
			// if contains returns true the shape is removed from shape list
			// painting is repainted, lines/rectangles redrawn without removed line/rectangle
			if ((arg0.getModifiers() & ActionEvent.CTRL_MASK) == ActionEvent.CTRL_MASK) {
				Graphics g = drawingPanel.getGraphics();
				int x = arg0.getX();
				int y = arg0.getY();
				System.out.println(x +","+ y);
				for (Shape s: allShapes) {
					if (s.contains(x, y)) {
						allShapes.remove(s);
						repaint();
						paint(g);
					}
				}
			}
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
		}

		@Override
		public void mouseMoved(MouseEvent arg0) {
		}
		
		public Color randomColor(Color[] c) { // returns random color from list of colors
			Random rand = new Random();
			int x = rand.nextInt(c.length);
			return c[x];
		}
	}
}
