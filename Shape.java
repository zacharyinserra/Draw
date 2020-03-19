import java.awt.Graphics;
import java.awt.Color;

public abstract class Shape {
	
    protected Color myColor;    
	protected int startX;
	protected int startY;
	protected int endX;
	protected int endY;
	
	public Shape(int sx, int sy, int ex, int ey, Color c) {
		startX = sx;
		startY = sy;
		endX = ex;
		endY = ey;
		myColor = c;
	}	
		
	public abstract void draw(Graphics g);
    
    public abstract boolean contains(int x, int y);
    
    public abstract String toString();
    
	public String colorToString(Color myColor) {
		 String colorS = Integer.toString(myColor.getRGB());
		 return colorS;
	}
}