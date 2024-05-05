package objects;

import java.awt.Color;
import java.awt.Graphics2D;

import game_of_life.ML;

public class DoubleSlider {
	
	private double value1, value2;
	private int x, y, width;
	private Color color1, color2;
	private Button rect1, rect2, rect3;
	private Button rectLeft, rectRight; // doesnt get printed
	
	private boolean isChangingLeft, isChangingRight; // left / right slider
	private boolean justCreated = true;
	
	
	public DoubleSlider(int x, int y, int width, double value1, double value2, Color color1, Color color2) {
		this.value1 = value1;
		this.value2 = value2;
		this.x = x;
		this.y = y;
		this.width = width;
		this.color1 = color1;
		this.color2 = color2;
		
		// create rectangles
		rect1 = new Button(x, y, (int)(width * value1), 15, color2, color2);
		rect2 = new Button(x+(int)(width * value1), y, (int)(width * (value2-value1)), 15, color1, color1);
		rect3 = new Button(x+(int)(width * value2), y, (int)(width * (1-value2)), 15, color2, color2);
		
		int w = (int)(0.5 * width * (value2-value1));
		rectLeft = new Button(x+(int)(width * value1), y, w, 15, color1, color1);
		rectRight = new Button(x+(int)(width*value1)+w, y, w, 15, color1, color1);
	}
	
	public void update(ML mouseListener) {
		rect1.update(mouseListener);
		rect2.update(mouseListener);
		rect3.update(mouseListener);
		rectLeft.update(mouseListener);
		rectRight.update(mouseListener);
		
		// check if the value should be updated
		if (rect1.isPressed() || rectLeft.isPressed()) {
			isChangingLeft = true;
		}
		if (rect3.isPressed() || rectRight.isPressed()) {
			isChangingRight = true;
		}
		if (!mouseListener.isPressed() && !justCreated) {
			isChangingLeft = false;
			isChangingRight = false;
		}
				
		// if pressed, update the value and rectangles
			// left
		if (isChangingLeft &&  !justCreated) {
			double offset = mouseListener.getX() - x;
			value1 = offset / width;
			
			value1 = value1 < 0 ? 0 : value1 > value2 ? value2 : value1; // clamping
			rect1.setWidth((int)(value1*width));
			rect2.setX(x + (int)(value1 * width));
			rect2.setWidth((int)(width * (value2-value1)));
			
			int w = (int)(0.5 * width * (value2-value1));
			rectLeft = new Button(x+(int)(width * value1), y, w, 10, color1, color1);
			rectRight = new Button(x+(int)(width*value1)+w, y, w, 10, color1, color1);
		}
		
			// right
		if (isChangingRight &&  !justCreated) {
			double offset = mouseListener.getX() - x;
			value2 = offset / width;
			
			value2 = value2 > 1 ? 1 : value2 < value1 ? value1 : value2; // clamping
			rect3.setWidth((int)((1-value2)*width));
			rect3.setX(x + (int)(value2 * width));
			rect2.setWidth((int)(width * (value2-value1)));
			
			int w = (int)(0.5 * width * (value2-value1));
			rectLeft = new Button(x+(int)(width * value1), y, w, 10, color1, color1);
			rectRight = new Button(x+(int)(width*value1)+w, y, w, 10, color1, color1);
		}
		
		justCreated = false;
	}
	
	public void draw(Graphics2D g2) {
		rect1.draw(g2);
		rect2.draw(g2);
		rect3.draw(g2);
	}
	
	public boolean isChanging() {
		return (isChangingLeft || isChangingRight);
	}
	
	public double getValue1() {
		return value1;
	}
	
	public double getValue2() {
		return value2;
	}
}
