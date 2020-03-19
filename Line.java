import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Line2D;

public class Line extends Shape{
	
	public Line(int sx, int sy, int ex, int ey, Color c) {
		super(sx, sy, ex, ey, c);
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(myColor);
		g.drawLine(startX, startY, endX, endY);
	}

	@Override
	public boolean contains(int x, int y) {
		//calculates distance of click point to line
		//checks if its within a certain distance
		double distanceFromLine = Line2D.ptSegDist(startX, startY, endX, endY, x, y);
		if (distanceFromLine < 3) {
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public String toString() {
		return ("l" + "," + startX +","+ startY +","+ endX +","+ endY +","+ colorToString(myColor));
	}	
}
