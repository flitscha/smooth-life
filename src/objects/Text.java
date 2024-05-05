package objects;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

public class Text {
	
	private boolean firstDrawCall = true; // i want to use the Graphics object in the draw, but only once
	
	private Font font;
	private String str;
	private int inputX, inputY;
	private int x, y; //(x, y) is top left
	private float size;
	private int xCenter, yCenter;
	private boolean hCenter, vCenter;
	private int textWidth, textHeight;
	
	public Text(String str, double x, double y, double size, boolean hCenter, boolean vCenter) {
		this.font = new Font("Arial", Font.PLAIN, (int)size);
		this.str = str;
		this.size = (float)size;
		this.hCenter = hCenter;
		this.vCenter = vCenter;
		this.inputX = (int)x;
		this.inputY = (int)y;	
	}
	
	private void calculateTextDimensions(Graphics g) {
	    // This method calculates the width and height of the text
	    FontMetrics fontMetrics = g.getFontMetrics(this.font);
	    this.textWidth = fontMetrics.stringWidth(this.str);
	    this.textHeight = fontMetrics.getHeight();
	}
	
	private void calculateCoordinates(int x, int y) {
		if(hCenter) {
			this.x = x - this.textWidth / 2;
			this.xCenter = x;
		} else {
			this.x = x;
			this.xCenter = x + this.textWidth / 2;
		}
		
		if(vCenter) {
			this.y = y + this.textHeight / 4;
			this.yCenter = y;
		} else {
			this.y = y;
			this.yCenter = y - this.textHeight / 4;
		}
	}
	
	public void draw(Graphics g) {
		if(g == null) {
			System.out.println("Error while drawing text: g is null");
			return;
		}
		if(this.firstDrawCall) {
			calculateTextDimensions(g);
			calculateCoordinates(this.inputX, this.inputY);
			this.firstDrawCall = false;
		}
		g.setFont(font);        
		g.drawString(str, x, y);
	}
	
	
	public void setSize(float newSize) {
		this.size = newSize;
		this.font = new Font("Arial", Font.PLAIN, (int)newSize);
		this.firstDrawCall = true; // make calculations with Graphics g again
	}
	
	public float getSize() {
		return this.size;
	}
	
	public void setX(int newX) {
		this.inputX = newX;
		calculateCoordinates(inputX, inputY);
	}
	
	public int getX() {
		return this.inputX;
	}
	
	public void setY(int newY) {
		this.inputY = newY;
		calculateCoordinates(inputX, inputY);
	}
	
	public int getY() {
		return this.inputY;
	}
	
	public int getWidth() {
		return this.textWidth;
	}
	
	public int getHeight() {
		return this.textHeight;
	}
	
	public String getString() {
		return this.str;
	}
	
	public void setString(String newStr) {
		this.str = newStr;
		this.firstDrawCall = true; // make calculations again
	}
}