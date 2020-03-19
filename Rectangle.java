import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Line2D;

public class Rectangle extends Shape{
	
	public int px, py, w, h;

	public Rectangle(int sx, int sy, int ex, int ey, Color c) {
		super(sx, sy, ex, ey, c);
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(myColor);
		px = Math.min(startX, endX);
		py = Math.min(startY, endY);
		w = Math.abs(startX - endX);
		h = Math.abs(startY - endY);
		g.drawRect(px, py, w, h);
	}

	@Override
	public boolean contains(int x, int y) {
		
		//calculates distance of click point to line
		double rectTop = Line2D.ptSegDist(px, py, px+w, py, x, y); //top line of rectangle
		double rectBot = Line2D.ptSegDist(px, py+h, px+w, py+h, x, y); // bottom line of rectangle
		double rectLeft = Line2D.ptSegDist(px, py, px, py+h, x, y); // left line of rectangle
		double rectRight = Line2D.ptSegDist(px+w, py, px+w, py+h, x, y); //right line of rectangle

		//checks if its within a certain distance
		if (rectTop < 3 || rectBot < 3 || rectLeft < 3 || rectRight < 3) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public String toString() {
		return ("r" + "," + startX +","+ startY +","+ endX +","+ endY +","+ colorToString(myColor));
	}
}
