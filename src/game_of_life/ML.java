package game_of_life;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class ML extends MouseAdapter implements MouseMotionListener {
	private boolean isPressed = false;
	private boolean isPressedRight = false;
	private double x = 0.0, y = 0.0;
	private boolean isPressedF = false;
	private boolean isPressedBefore = false;
	
	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			isPressed = true;
		}
		if (e.getButton() == MouseEvent.BUTTON3) {
			isPressedRight = true;
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			isPressed = false;
		}
		if (e.getButton() == MouseEvent.BUTTON3) {
			isPressedRight = false;
		}
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		this.x = e.getX();
		this.y = e.getY();
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		this.x = e.getX();
	    this.y = e.getY();
	}
	
	public void update() {
		isPressedF = !isPressedBefore && isPressed;
		isPressedBefore = isPressed;
	}
	
	public double getX() { 
		return this.x; 
	}
	
	public double getY() {
		return this.y;
	}
	
	public boolean isPressed() {
		return this.isPressed;
	}
	
	public boolean isPressedF() { // flanke
		return this.isPressedF;
	}
	
	public boolean isPressedRight() {
		return this.isPressedRight;
	}
	
}
