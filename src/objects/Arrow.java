package objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;

import utils.Directions;

public class Arrow {

	private double x, y;
	private double length, width;
	private Directions dir;
	private Color color;

	private Rect rect;

	public Arrow(double x, double y, double length, double width, Directions dir, Color color) {
		this.x = x;
		this.y = y;
		this.length = length;
		this.width = width;
		this.dir = dir;
		this.color = color;

		switch (dir) {
		case LEFT:
			rect = new Rect(x - length, y - width / 2, length, width, color);
			break;
		case RIGHT:
			rect = new Rect(x, y - width / 2, length, width, color);
			break;
		case UP:
			rect = new Rect(x - width / 2, y - length, width, length, color);
			break;
		case DOWN:
			rect = new Rect(x - width / 2, y, width, length, color);
			break;

		}
	}

	public void draw(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		rect.draw(g2);
		drawArrowhead(g2);
	}

	private void drawArrowhead(Graphics2D g2) {
		int[] xPoints = new int[3];
		int[] yPoints = new int[3];

		switch (dir) {
		case LEFT:
			xPoints[0] = (int) (x - length);
			xPoints[1] = (int) (x - length);
			xPoints[2] = (int) (x - length - 5*width);
			yPoints[0] = (int) (y - 2*width);
			yPoints[1] = (int) (y + 2*width);
			yPoints[2] = (int) y;
			break;
		case RIGHT:
			xPoints[0] = (int) (x + length);
			xPoints[1] = (int) (x + length);
			xPoints[2] = (int) (x + length + 5*width);
			yPoints[0] = (int) (y - 2*width);
			yPoints[1] = (int) (y + 2*width);
			yPoints[2] = (int) y;
			break;
		case UP:
			xPoints[0] = (int) (x - 2*width);
			xPoints[1] = (int) (x + 2*width);
			xPoints[2] = (int) (x);
			yPoints[0] = (int) (y - length);
			yPoints[1] = (int) (y - length);
			yPoints[2] = (int) (y - length - 5*width);
			break;
		case DOWN:
			xPoints[0] = (int) (x - 2*width);
			xPoints[1] = (int) (x + 2*width);
			xPoints[2] = (int) x;
			yPoints[0] = (int) (y + length);
			yPoints[1] = (int) (y + length);
			yPoints[2] = (int) (y + length + 5*width);
			break;
		}

		Polygon arrowhead = new Polygon(xPoints, yPoints, 3);
		g2.setColor(color);
		g2.fill(arrowhead);
	}
}
