package objects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;


public class Rect {
	public double x, y, width, height;
	public Color color;
	
	public Rect(int x, int y, int width, int height, Color color) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.color = color;
	}
	
	public Rect(double x, double y, double width, double height, Color color) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.color = color;
	}
	
	public void draw(Graphics2D g2) {
		g2.setColor(color);
		g2.fill(new Rectangle2D.Double(x, y, width, height));
	}
	
	public boolean pointCollision(int x, int y) {
		return (x >= this.x && x <= this.x + this.width && 
				y >= this.y && y <= this.y + this.height);
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public Color getColor() {
		return this.color;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public void setX(int x) {
		this.x = x;
	}
}
