import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Graph extends JPanel {
	private static final int PREF_W = 800;
	private static final int PREF_H = 650;
	private static final int BORDER_GAP = 30;
	private static final int GRAPH_POINT_WIDTH = 6;
	private static final int Y_HATCH_CNT = 25;
	private static HashMap<Character, ArrayList> dataHash;
	private static double max;
	private static double min;
	private static int timeBegin;
	private static int timeEnd;
	private static int size;

	public Graph(HashMap<Character, ArrayList> dataHash, double max, double min, int timeBegin, int timeEnd, int size) {
		this.dataHash = dataHash;
		this.max = max;
		this.min = min;
		this.timeBegin = timeBegin;
		this.timeEnd = timeEnd;
		this.size= size;
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		double xScale = (double)(getWidth() - (2 * BORDER_GAP)) / (size / 3);
		double yScale = (getHeight() / 2) * BORDER_GAP / (max - min);

		ArrayList<Point> pointsI = new ArrayList<Point>();
		ArrayList<Point> pointsA = new ArrayList<Point>();
		ArrayList<Point> pointsS = new ArrayList<Point>();
		Iterator itr = dataHash.entrySet().iterator();
		while(itr.hasNext()) {
			Map.Entry pair = (Map.Entry) itr.next();
			for(Data data : (ArrayList<Data>)pair.getValue()) {
				int x1 = (int) ((data.getTime() - timeBegin) / 360*xScale + BORDER_GAP);
				int y1 = (int) ((max - data.getValue()) * yScale + BORDER_GAP);
				if(pair.getKey().toString().equalsIgnoreCase("A")) {
					pointsA.add(new Point(x1,y1));
				} else if (pair.getKey().toString().equalsIgnoreCase("S")) {
					pointsS.add(new Point(x1, y1));
				} else if(pair.getKey().toString().equalsIgnoreCase("I")) {
					pointsI.add(new Point(x1, y1));
				}
			}
		}

		// Create x and y axis
		g2.drawLine(BORDER_GAP, getHeight() - BORDER_GAP, BORDER_GAP, BORDER_GAP);
		g2.drawLine(BORDER_GAP, getHeight() - BORDER_GAP, getWidth() - BORDER_GAP, getHeight()-BORDER_GAP);

		// Y axis hatches
		for (int i = (int) this.min; i < (int) this.max; i++) {
			int x0 = BORDER_GAP;
			int x1 = GRAPH_POINT_WIDTH + BORDER_GAP;
			int y0 = getHeight() - (((i + 1) * (getHeight() - BORDER_GAP * 2)) / Y_HATCH_CNT + BORDER_GAP);
			int y1 = y0;
			g2.drawLine(x0, y0, x1, y1);
		}

		// X axis 
		for (int i = this.timeBegin; i < this.timeEnd; i++) {
			int x0 = (i + 1) * (getWidth() - BORDER_GAP * 2) / this.timeEnd + BORDER_GAP;
			int x1 = x0;
			int y0 = getHeight() - BORDER_GAP;
			int y1 = y0 - GRAPH_POINT_WIDTH;
			g2.drawLine(x0, y0, x1, y1);
		}

		// Plot points and set to different colors per data center
		g2.setColor(Color.GREEN);
		for (int i = 0; i < pointsI.size(); i++) {
			int x = pointsI.get(i).x - GRAPH_POINT_WIDTH / 2;
			int y = pointsI.get(i).y - GRAPH_POINT_WIDTH / 2;;
			int ovalW = GRAPH_POINT_WIDTH;
			int ovalH = GRAPH_POINT_WIDTH;
			g2.fillOval(x, y, ovalW, ovalH);
		}
		g2.setColor(Color.RED);
		for (int i = 0; i < pointsA.size(); i++) {
			int x = pointsA.get(i).x - GRAPH_POINT_WIDTH / 2;
			int y = pointsA.get(i).y - GRAPH_POINT_WIDTH / 2;;
			int ovalW = GRAPH_POINT_WIDTH;
			int ovalH = GRAPH_POINT_WIDTH;
			g2.fillOval(x, y, ovalW, ovalH);
		}
		g2.setColor(Color.BLUE);
		for (int i = 0; i < pointsS.size(); i++) {
			int x = pointsS.get(i).x - GRAPH_POINT_WIDTH / 2;
			int y = pointsS.get(i).y - GRAPH_POINT_WIDTH / 2;;
			int ovalW = GRAPH_POINT_WIDTH;
			int ovalH = GRAPH_POINT_WIDTH;
			g2.fillOval(x, y, ovalW, ovalH);
		}
	}
	
	// Open GUI with the preferred with and height
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(PREF_W, PREF_H);
	}

	public static void createAndShowGUI() {

		Graph mainPanel = new Graph(dataHash, max, min, timeBegin, timeEnd, size);

		JFrame frame = new JFrame("Data Centers I, S, A Graph");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(mainPanel);
		frame.pack();
		frame.setLocationByPlatform(true);
		frame.setVisible(true);
	}
}
