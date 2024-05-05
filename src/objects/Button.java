package objects;

import java.awt.Color;
import java.awt.Graphics2D;

import game_of_life.ML;

public class Button extends Rect {
	
	private Rect frameRect;
	private Color color1, color2;
	private Text text;
	private boolean mouseCollision;
	private boolean isPressed, pressedBefore, pressedNow;
	private boolean isReleased;
	// isPressed and isReleased are only true in the frame this happens
	
	
	public Button(int x, int y, int width, int height, Color color1, Color color2) {
		super(x, y, width, height, color1);
		this.color1 = color1;
		this.color2 = color2;
		this.text = new Text("", 0, 0, 0, true, true);
		init();
	}
	
	public Button(int x, int y, int width, int height, Color color1, Color color2, String text) {
		super(x, y, width, height, color1);
		this.color1 = color1;
		this.color2 = color2;
		this.text = new Text(text, x+width/2.0, y+height/2.0, width/3, true, true);
		init();
	}
	
	public Button(int x, int y, int width, int height, Color color1, Color color2, String text, double fontSize) {
		super(x, y, width, height, color1);
		this.color1 = color1;
		this.color2 = color2;
		this.text = new Text(text, x+width/2.0, y+height/2.0, fontSize, true, true);
		init();
		
	}
	
	private void init() {
		this.isPressed = false;
		this.pressedBefore = false;
		this.pressedNow = false;
		this.isReleased = false;
		this.frameRect = new Rect(x-4, y-4, width+8, height+8, Color.BLACK);
	}
	
	public void update(ML mouseListener) {
		mouseCollision = pointCollision((int)mouseListener.getX(), (int)mouseListener.getY());
		// detect mouse collision and update values
		pressedBefore = pressedNow;
		
		if (!mouseListener.isPressed()) {
			pressedNow = false;
		}
		isReleased = false;
		
		if(mouseCollision) {
			if (getColor() == color1) {
				setColor(color2);
			}
			if (mouseListener.isPressedF()) {
				pressedNow = true;
			} 
			if (pressedBefore && !mouseListener.isPressed()) {
				isReleased = true;
			}
		} else if (!pressedNow){
			setColor(color1);
		}

		isPressed = !pressedBefore && pressedNow && mouseCollision;
	}
	
	public void draw(Graphics2D g2) {
		frameRect.draw(g2);
		super.draw(g2);
		g2.setColor(Color.BLACK);
		text.draw(g2);
	}
	
	public boolean getMouseCollision() {
		return this.mouseCollision;
	}
	
	public boolean isPressed() {
		return this.isPressed;
	}
	
	public boolean isReleased() {
		return this.isReleased;
	}
	
	public void setX(int x) {
		this.x = x;
		super.x = x;
		frameRect.setX(x-4);
	}
	
	public void setWidth(int width) {
		this.width = width;
		super.width = width;
		frameRect.setWidth(width+8);
	}
	
	public void changeText(String newText) {
		text.setString(newText);
	}

}
