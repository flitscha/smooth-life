package objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import game_of_life.ML;

public class Slider {
	
	private int x, y, width;
	private Color color1, color2;
	private double value; // between 0 and 1
	
	private Button rect1, rect2;
	
	private boolean isChanging = true;
	private boolean justCreated = true;
	
	
	public Slider(int x, int y, int width, double startingValue, Color color1, Color color2) {
		this.value = startingValue;
		this.x = x;
		this.y = y;
		this.width = width;
		this.color1 = color1;
		this.color2 = color2;
		
		// create rectangles
		rect1 = new Button(x, y, (int)(width * startingValue), 15, color1, color1);
		rect2 = new Button(x+(int)(width * startingValue), y, (int)(width * (1-startingValue)), 15, color2, color2);
	}
	
	
	public void update(ML mouseListener) {
		rect1.update(mouseListener);
		rect2.update(mouseListener);
		
		// check if the value should be updated
		if (rect1.isPressed() || rect2.isPressed()) {
			isChanging = true;
		}
		if (!mouseListener.isPressed()) {
			isChanging = false;
		}
		
		// if pressed, update the value and rectangles
		if (isChanging && !justCreated) {
			double offset = mouseListener.getX() - x;
			value = offset / width;
			value = value < 0 ? 0 : value > 1 ? 1 : value; // clamping
			rect1.setWidth((int)(width * value));
			rect2.setWidth((int)(width * (1-value)));
			rect2.setX(x + (int)(width * value));
		}
		
		if (justCreated) {
			isChanging = false;
			justCreated = false;
		}
	}

	
	public void draw(Graphics2D g2) {
		rect1.draw(g2);
		rect2.draw(g2);
	}
	
	
	public double getValue() {
		return value;
	}
	
	public boolean isChanging() {
		return isChanging;
	}

}
