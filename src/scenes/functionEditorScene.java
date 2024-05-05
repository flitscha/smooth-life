package scenes;

import static game_of_life.Constants.BACKGROUND_COLOR;
import static game_of_life.Constants.BUTTON_COLOR_1;
import static game_of_life.Constants.BUTTON_COLOR_2;
import static game_of_life.Constants.COLOR_PALETTE;
import static game_of_life.Constants.MAX_BRUSH_SIZE;
import static game_of_life.Constants.WIN_HEIGHT;
import static game_of_life.Constants.WIN_WIDTH;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import game_of_life.KL;
import game_of_life.ML;
import game_of_life.Settings;
import game_of_life.Window;
import objects.Arrow;
import objects.Button;
import objects.Rect;
import objects.Slider;
import objects.Text;
import utils.Directions;
import utils.Modes;
import utils.Scenes;

public class functionEditorScene extends Scene {
	
	private KL keyListener;
	private ML mouseListener;
	private Rect backgroundRect = new Rect(0, 0, WIN_WIDTH, WIN_HEIGHT, BACKGROUND_COLOR);
	private Rect titleRect;
	private Text titleText;
	private Button startButton, backButton;
	
	// axis
	private int originX = 90;
	private int originY = WIN_HEIGHT-60;
	private Arrow valueAxis, sumAxis;
	private Text axisValueText, axisSumText;
	private Rect axisSumMark, axisValueMark;
	private Text axisMaxValueText, axisMaxSumText;
	
	private float[][] functionValues;
	
	// grid
	private Rect gridBackgroundRect;
	private int gridSize = Settings.functionResolution;
	private int gridWidth = 420; // in pixel
	private double cellSize;
	private Rect[][] cellRects;
	
	// brush settings
	private Text valueText, brushSizeText;
	private Slider valueSlider, brushSizeSlider;
	private Button clearButton;
	private int brushSize;
	private float brushValue;
	
	// standard functions
	private Text standardFunctionsText;
	private Button sigmoidButton, convayButton;
		// sigmoid constants:
	private double alpha_n = 0.028;
	private double alpha_m = 0.147;
	private double b1 = 0.278;
	private double b2 = 0.365;
	private double d1 = 0.267;
	private double d2 = 0.445;
	
	//private double alpha_n = 0.04;
	//private double alpha_m = 0.2;
	//private double b1 = 0.29;
	//private double b2 = 0.45;
	//private double d1 = 0.25;
	//private double d2 = 0.46;
	
	
	
	
	public functionEditorScene(KL keyListener, ML mouseListener) {
		this.keyListener = keyListener;
		this.mouseListener = mouseListener;
		functionValues = new float[gridSize][gridSize];
		loadSettings();
		
		// title
		titleRect = new Rect(0, 0, WIN_WIDTH, 160, COLOR_PALETTE[2]);
		titleText = new Text("function editor", WIN_WIDTH/2, 90, 90, true, true);
		
			// function grid
		// axis
		gridBackgroundRect = new Rect(originX, originY-gridWidth, gridWidth, gridWidth, Color.BLACK);
		valueAxis = new Arrow(originX-3, originY+33, gridWidth+53, 6, Directions.UP, Color.BLACK);
		sumAxis = new Arrow(originX-33, originY+3, gridWidth+53, 6, Directions.RIGHT, Color.BLACK);
		axisValueText = new Text("inner radius area", 120, WIN_HEIGHT-540, 25, true, true);
		axisSumText = new Text("outer radius area", 250, WIN_HEIGHT-40, 25, true, true);
		axisSumMark = new Rect(originX + gridWidth, originY, 5, 15, Color.BLACK);
		axisValueMark = new Rect(originX-15, originY - gridWidth, 15, 5, Color.BLACK);
		axisMaxValueText = new Text("100%", originX-50, originY - gridWidth, 22, true, true);
		axisMaxSumText = new Text("100%", originX + gridWidth, originY + 35, 22, true, true);
		
		// grid
		cellSize = (double)gridWidth / gridSize;
		cellRects = new Rect[gridSize][gridSize];
		for (int i = 0; i < gridSize; i++) {
			for (int c = 0; c < gridSize; c++) {
				float alpha = functionValues[i][c];
				Color color = simulateAlpha(COLOR_PALETTE[4], COLOR_PALETTE[0], alpha);
				cellRects[i][c] = new Rect(originX + c*cellSize, originY-gridWidth + i*cellSize, cellSize, cellSize, color);
			}
		}
		
			// brush settings
		// alpha slider
		int x = 600;
		valueText = new Text("value: "+(int)(brushValue*100)+"%", x+20, 250, 30, false, true);
		valueSlider = new Slider((int)x+20, 280, 100, brushValue, COLOR_PALETTE[4], COLOR_PALETTE[2]);
				
		// clear button
		clearButton = new Button((int)(x+245), 240, 120, 50, BUTTON_COLOR_1, BUTTON_COLOR_2, "clear", 30);
		// brush size slider
		brushSizeText = new Text("brush size: "+brushSize, (int)(x+20), 350, 30, false, true);
		brushSizeSlider = new Slider((int)x+20, 380, 100, 
				(float)(brushSize-1)/(MAX_BRUSH_SIZE-1), COLOR_PALETTE[4], COLOR_PALETTE[2]);
		
			// standard functions
		standardFunctionsText = new Text("or apply a standard function:", x+20, 450, 30, false, true);
		sigmoidButton = new Button((int)(x+20), 480, 120, 50, BUTTON_COLOR_1, BUTTON_COLOR_2, "sigmoid", 30);
		convayButton = new Button((int)(x+245), 480, 120, 50, BUTTON_COLOR_1, BUTTON_COLOR_2, "convay", 30);
		
		// start/exit button
		startButton = new Button(WIN_WIDTH-200, WIN_HEIGHT-80, 150, 50, BUTTON_COLOR_1, BUTTON_COLOR_2, "start", 30);
		backButton = new Button(600, WIN_HEIGHT-80, 150, 50, BUTTON_COLOR_1, BUTTON_COLOR_2, "edit grid", 30);
	}
	
	
	private void loadSettings() {
		// function values
		for (int i = 0; i < gridSize; i++) {
			for (int c = 0; c < gridSize; c++) {
				functionValues[i][c] = Settings.simpleFunctionGrid[i][c];
			}
		}
		
		// settings
		brushSize = Settings.functionBrushSize;
		brushValue = Settings.functionBrushValue;
	}
	
	
	private void saveSettings() {
		// function values
		for (int i = 0; i < gridSize; i++) {
			for (int c = 0; c < gridSize; c++) {
				Settings.simpleFunctionGrid[i][c] = functionValues[i][c];
			}
		}
		
		// settings
		Settings.functionBrushSize = brushSize;
		Settings.functionBrushValue = brushValue;
	}
	
	// if color is over backgroundColor, this function simulates how it looks like, if color has an alpha value.
	// this is usefull to avoid lag in the draw function
	private Color simulateAlpha(Color color, Color backgroundColor, float alpha) {
		int r, g, b;
		r = (int)(backgroundColor.getRed() + (color.getRed() - backgroundColor.getRed()) * alpha);
		g = (int)(backgroundColor.getGreen() + (color.getGreen() - backgroundColor.getGreen()) * alpha);
		b = (int)(backgroundColor.getBlue() + (color.getBlue() - backgroundColor.getBlue()) * alpha);
		return new Color(r, g, b);
	}
	
	
	@Override
	public void update(double dt) {
		startButton.update(mouseListener);
		backButton.update(mouseListener);
		clearButton.update(mouseListener);
		valueSlider.update(mouseListener);
		brushSizeSlider.update(mouseListener);
		sigmoidButton.update(mouseListener);
		//convayButton.update(mouseListener);

		// check if the grid gets clicked
		updateGrid();
				
		// settings
			// alpha slider
		if (valueSlider.isChanging()) {
			brushValue = (float)valueSlider.getValue();
			valueText.setString("alpha: " + (int)(brushValue*100) + "%");
		}
				
			// clear button
		if (clearButton.isReleased()) {
			clearGrid();
		}
			// brush size slider
		if (brushSizeSlider.isChanging()) {
			brushSize = (int)(brushSizeSlider.getValue() * (MAX_BRUSH_SIZE-1))+1;
			brushSizeText.setString("brush size: " + brushSize);
		}
				
		// sigmoid button
		if (sigmoidButton.isReleased()) {
			applySigmoid();
		}
		
		// convay button
		if (convayButton.isReleased()) {
			applyConvay();
		}
		
		// back button
		if (backButton.isReleased()) {
			saveSettings();
			Window.getWindow().changeScene(Scenes.EDITOR_SCENE);
		}
				
		// start button: save settings and change to simulation scene
		if (startButton.isReleased()) {
			saveSettings();
			Window.getWindow().changeScene(Scenes.SIMULATION_SCENE);
		}
	}
	
	
	private void updateGrid() {
		int mouseX = (int)mouseListener.getX();
		int mouseY = (int)mouseListener.getY();
		if (gridBackgroundRect.pointCollision(mouseX, mouseY)) {
			if (mouseListener.isPressed()) {
				// get the coordinates of the pressed cell
				int x = (int)((mouseX - originX) / cellSize);
				int y = (int)((mouseY - originY + gridWidth) / cellSize);
				drawPoint(y, x, brushValue);
			}
			if (mouseListener.isPressedRight()) {
				int x = (int)((mouseX - originX) / cellSize);
				int y = (int)((mouseY - originY + gridWidth) / cellSize);
				drawPoint(y, x, 0f);
			}
		}
	}
	
	
	private void drawPoint(int y, int x, float a) {
		// with brushSize 
		// size 1: 1x1
		// size 2: 3x3
		// size 3: 5x5
		// => size = brushSize * 2 - 1

		// loop through this square
		for (int i = y-brushSize+1; i <= y+brushSize-1; i++) {
			for (int c = x-brushSize+1; c <= x+brushSize-1; c++) {
				// fix out of border
				int y2 = i;
				int x2 = c;
				
				if (y2 >= gridSize || y2 < 0 || x2 >= gridSize || x2 < 0) {
					continue;
				}

				// color everything that is in the circle
				if ((c-x)*(c-x) + (i-y)*(i-y) < (brushSize-0.5)*(brushSize-0.5)) {
					functionValues[y2][x2] = a;
					cellRects[y2][x2].setColor(simulateAlpha(COLOR_PALETTE[4], COLOR_PALETTE[0], a));
				}
			}
		}
	}
	
	
	private void clearGrid() {
		for (int i = 0; i < gridSize; i++) {
			for (int c = 0; c < gridSize; c++) {
				float a = 1-(float)(i+1) / gridSize;
				functionValues[i][c] = a;
				Color color = simulateAlpha(COLOR_PALETTE[4], COLOR_PALETTE[0], a);
				cellRects[i][c].setColor(color);
			}
		}
	}
	
	
	
	private void applySigmoid() {
		for (int i = 0; i < gridSize; i++) {
			for (int c = 0; c < gridSize; c++) {
				float innerArea = 1 - (float)i / gridSize;
				float outerArea = (float)c / gridSize;
				float value = (float)sigmoid(outerArea, innerArea);
				functionValues[i][c] = value;
				cellRects[i][c].setColor(simulateAlpha(COLOR_PALETTE[4], COLOR_PALETTE[0], value));
			}
		}
	}
	
	// from the paper 'Generalization of Conway's "Game of Life" to a continuous domain - SmoothLife'
	// https://arxiv.org/abs/1111.1567
	private double sigmoid(double n, double m) {
		return s2(n, s_m(b1, d1, m), s_m(b2, d2, m), alpha_n);
	}
	
	private double s_m(double x, double y, double m) {
		return x * (1 - s1(m, 0.5, alpha_m)) + y * s1(m, 0.5, alpha_m);
	}
	
	private double s2(double x, double a, double b, double alpha) {
		return s1(x, a, alpha) * (1 - s1(x, b, alpha));
	}
	
	private double s1(double x, double a, double alpha) {
		return 1 / (1 + Math.exp(-(x-a)*4/alpha));
	}
	
	
	private void applyConvay() {
		for (int i = 0; i < gridSize; i++) {
			for (int c = 0; c < gridSize; c++) {
				float innerArea = 1 - (float)i / gridSize;
				float outerArea = (float)c / gridSize;
				
				if ((innerArea > 0.95 && outerArea > 1.5 / 8 && outerArea < 3.5 / 8) ||
						(innerArea < 0.05 && outerArea > 2.5 / 8 && outerArea < 3.5 / 8)) {
					functionValues[i][c] = 1f;
					cellRects[i][c].setColor(simulateAlpha(COLOR_PALETTE[4], COLOR_PALETTE[0], 1));
				} else {
					functionValues[i][c] = 0f;
					cellRects[i][c].setColor(simulateAlpha(COLOR_PALETTE[4], COLOR_PALETTE[0], 0));
				}
			}
		}
	}
	
	@Override
	public void draw(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		backgroundRect.draw(g2);
		
		// title
		titleRect.draw(g2);
		g2.setColor(COLOR_PALETTE[4]);
		titleText.draw(g2);
		
		// function grid
		valueAxis.draw(g2);
		sumAxis.draw(g2);
		axisValueText.draw(g2);
		axisSumText.draw(g2);
		axisSumMark.draw(g2);
		axisValueMark.draw(g2);
		axisMaxValueText.draw(g2);
		axisMaxSumText.draw(g2);
		
		// grid
		for (int i = 0; i < gridSize; i++) {
			for (int c = 0; c < gridSize; c++) {
				cellRects[i][c].draw(g2);
			}
		}
				
				
		// settings
		g2.setColor(Color.BLACK);
		brushSizeText.draw(g2);
		brushSizeSlider.draw(g2);
		valueText.draw(g2);
		valueSlider.draw(g2);
		clearButton.draw(g2);
		
		// standard functions
		standardFunctionsText.draw(g2);
		sigmoidButton.draw(g2);
		//convayButton.draw(g2);
		
		// start/exit button
		startButton.draw(g2);
		backButton.draw(g2);
	}

}
