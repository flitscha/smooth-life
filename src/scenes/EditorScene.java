package scenes;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import game_of_life.KL;
import game_of_life.ML;
import game_of_life.Settings;
import game_of_life.Window;
import objects.Button;
import objects.DoubleSlider;
import objects.Rect;
import objects.Slider;
import objects.Text;
import utils.Scenes;

import static game_of_life.Constants.*;


public class EditorScene extends Scene {
	
	private KL keyListener;
	private ML mouseListener;
	
	private Rect backgroundRect, titleRect;
	private Text titleText;
	private Button backButton, continueButton;
	
	private Rect gridBackgroundRect;
	private int gridSize;
	private double cellSize;
	private Rect[][] cellRects;
	private float[][] cellValues;
	
	private Text alphaText;
	private Slider alphaSlider;
	private float alpha;
	
	private Button clearButton;
	
	private Text brushSizeText;
	private Slider brushSizeSlider;
	private int brushSize;
	
	private Rect seperatingRect1;
	private Text fillPercentageText;
	private Slider fillPercentageSlider;
	private float fillPercentage;
	
	private Text alphaRangeText;
	private float alphaRangeLow, alphaRangeHigh;
	private DoubleSlider alphaRangeSlider;
	private Button fillRandomButton;
	
	private Rect seperatingRect2;
	
	
	
	public EditorScene(KL keyListener, ML mouseListener) {
		this.keyListener = keyListener;
		this.mouseListener = mouseListener;
		loadSettings();
		this.cellValues = new float[gridSize][gridSize];
		loadValues();
		
		double insetsTop = Window.getWindow().getInsets().top;
		double winHeight = WIN_HEIGHT - insetsTop;
		
		
		// title
		backgroundRect = new Rect(0, 0, WIN_WIDTH, WIN_HEIGHT, BACKGROUND_COLOR);
		titleRect = new Rect(winHeight, 0, WIN_WIDTH, 120, COLOR_PALETTE[2]);
		titleText = new Text("grid editor", winHeight+(WIN_WIDTH-winHeight)/2, 75, 60, true, true);
		
		// grid
		cellSize = (double)winHeight / gridSize;
		gridBackgroundRect = new Rect(0, 0, winHeight, WIN_HEIGHT, COLOR_PALETTE[0]);
		cellRects = new Rect[gridSize][gridSize];
		for (int i = 0; i < gridSize; i++) {
			for (int c = 0; c < gridSize; c++) {
				float alpha = cellValues[i][c];
				Color color = simulateAlpha(COLOR_PALETTE[4], COLOR_PALETTE[0], alpha);
				cellRects[i][c] = new Rect(c*cellSize, insetsTop+i*cellSize, cellSize, cellSize, color);
			}
		}
		
		
		// settings
			// alpha slider
		alphaText = new Text("alpha: "+(int)(alpha*100)+"%", winHeight+20, 150, 30, false, true);
		alphaSlider = new Slider((int)winHeight+20, 180, 100, alpha, COLOR_PALETTE[4], COLOR_PALETTE[2]);
		
			// clear button
		clearButton = new Button((int)(winHeight+245), 140, 120, 50, BUTTON_COLOR_1, BUTTON_COLOR_2, "clear", 30);
			// brush size slider
		brushSizeText = new Text("brush size: "+brushSize, (int)(winHeight+20), 250, 30, false, true);
		brushSizeSlider = new Slider((int)winHeight+20, 280, 100, 
				(float)(brushSize-1)/(MAX_BRUSH_SIZE-1), COLOR_PALETTE[4], COLOR_PALETTE[2]);
		
			// fill random
		seperatingRect1 = new Rect(winHeight, 310, WIN_WIDTH-winHeight, 10, COLOR_PALETTE[2]);
		fillPercentageText = new Text("fill percentage: "+(int)(fillPercentage*100)+"%", 
				(int)(winHeight+20), 350, 30, false, true);
		fillPercentageSlider = new Slider((int)(winHeight+20), 380, 100,
				fillPercentage, COLOR_PALETTE[4], COLOR_PALETTE[2]);
		alphaRangeText = new Text("alpha range: " + (int)(alphaRangeLow*100) + 
				"% - " + (int)(alphaRangeHigh*100) + "%", (int)(winHeight+20), 450, 30, false, true);
		fillRandomButton = new Button((int)(winHeight+205), 475, 160, 40, BUTTON_COLOR_1, BUTTON_COLOR_2, "fill random", 30);
		
		alphaRangeSlider = new DoubleSlider((int)(winHeight+20), 480, 100,
				alphaRangeLow, alphaRangeHigh, COLOR_PALETTE[4], COLOR_PALETTE[2]);
		
			// presets
		seperatingRect2 = new Rect(winHeight, 525, WIN_WIDTH-winHeight, 10, COLOR_PALETTE[2]);
		
		// back- and continue button
		backButton = new Button((int)(winHeight+20), WIN_HEIGHT-80, 120, 50, BUTTON_COLOR_1, BUTTON_COLOR_2, "back", 30);
		continueButton = new Button(WIN_WIDTH-140, WIN_HEIGHT-80, 120, 50, BUTTON_COLOR_1, BUTTON_COLOR_2, "continue", 30);
		
	}
	
	private void loadSettings() {
		gridSize = Settings.gridSize;
		alpha = Settings.drawAlpha;
		brushSize = Settings.brushSize;
		fillPercentage = Settings.randomFillPercentage;
		alphaRangeLow = Settings.alphaRangeLow;
		alphaRangeHigh = Settings.alphaRangeHigh;
	}
	
	private void loadValues() {
		for (int i = 0; i < gridSize; i++) {
			for (int c = 0; c < gridSize; c++) {
				cellValues[i][c] = Settings.grid[i][c];
			}
		}
	}
	
	private void saveSettings() {
		Settings.drawAlpha = alpha;
		Settings.brushSize = brushSize;
		Settings.randomFillPercentage = fillPercentage;
		Settings.alphaRangeLow = alphaRangeLow;
		Settings.alphaRangeHigh = alphaRangeHigh;
		
		// save grid
		for (int i = 0; i < gridSize; i++) {
			for (int c = 0; c < gridSize; c++) {
				Settings.grid[i][c] = cellValues[i][c];
			}
		}
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
		backButton.update(mouseListener);
		continueButton.update(mouseListener);
		alphaSlider.update(mouseListener);
		clearButton.update(mouseListener);
		brushSizeSlider.update(mouseListener);
		fillRandomButton.update(mouseListener);
		fillPercentageSlider.update(mouseListener);
		alphaRangeSlider.update(mouseListener);
		
		// check if the grid gets clicked
		updateGrid();
		
		// settings
			// alpha slider
		if (alphaSlider.isChanging()) {
			alpha = (float)alphaSlider.getValue();
			alphaText.setString("alpha: " + (int)(alpha*100) + "%");
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
		
				// RANDOM
			// fill percentage slider
		if (fillPercentageSlider.isChanging()) {
			fillPercentage = (float)fillPercentageSlider.getValue();
			fillPercentageText.setString("fill percentage: " + (int)(fillPercentage * 100) + "%");
		}
			// alpha range
		if (alphaRangeSlider.isChanging()) {
			alphaRangeLow = (float)alphaRangeSlider.getValue1();
			alphaRangeHigh = (float)alphaRangeSlider.getValue2();
			alphaRangeText.setString("alpha range: " + (int)(alphaRangeLow*100) + 
					"% - " + (int)(alphaRangeHigh*100) + "%");
		}
			// fill random button
		if (fillRandomButton.isPressed()) {
			fillRandom();
		}
		
		
		// back button -> menu
		if (backButton.isReleased()) {
			saveSettings();
			Window.getWindow().changeScene(Scenes.MENU_SCENE);
		}
		
		// continue button -> function editor
		if (continueButton.isReleased()) {
			saveSettings();
			Window.getWindow().changeScene(Scenes.FUNCTION_EDITOR_SCENE);
		}
	}
	
	
	private void updateGrid() {
		int mouseX = (int)mouseListener.getX();
		int mouseY = (int)mouseListener.getY();
		if (gridBackgroundRect.pointCollision(mouseX, mouseY)) {
			if (mouseListener.isPressed()) {
				// get the coordinates of the pressed cell
				int x = (int)(mouseX / cellSize);
				int y = (int)((mouseY - Window.getWindow().getInsets().top) / cellSize);
				drawPoint(y, x, alpha);
			}
			if (mouseListener.isPressedRight()) {
				int x = (int)(mouseX / cellSize);
				int y = (int)((mouseY - Window.getWindow().getInsets().top) / cellSize);
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
				int y2 = (i + gridSize) % gridSize;
				int x2 = (c + gridSize) % gridSize;
				
				// color everything that is in the circle
				if ((c-x)*(c-x) + (i-y)*(i-y) < (brushSize-0.5)*(brushSize-0.5)) {
					cellValues[y2][x2] = a;
					cellRects[y2][x2].setColor(simulateAlpha(COLOR_PALETTE[4], COLOR_PALETTE[0], a));
				}
			}
		}
	}
	
	
	private void clearGrid() {
		for (int i = 0; i < gridSize; i++) {
			for (int c = 0; c < gridSize; c++) {
				cellValues[i][c] = 0f;
				cellRects[i][c].setColor(COLOR_PALETTE[0]);
			}
		}
	}
	
	private void fillRandom() {
		clearGrid();
		for (int i = 0; i < gridSize; i++) {
			for (int c = 0; c < gridSize; c++) {
				if (Math.random() <= fillPercentage) {
					float a = (float)(alphaRangeLow + Math.random() * (alphaRangeHigh - alphaRangeLow));
					cellValues[i][c] = a;
					cellRects[i][c].setColor(simulateAlpha(COLOR_PALETTE[4], COLOR_PALETTE[0], a));
				}
			}
		}
	}
	
	@Override
	public void draw(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		
		// title
		backgroundRect.draw(g2);
		titleRect.draw(g2);
		g2.setColor(COLOR_PALETTE[4]);
		titleText.draw(g2);
		
		// grid
		gridBackgroundRect.draw(g2);
		for (int i = 0; i < gridSize; i++) {
			for (int c = 0; c < gridSize; c++) {
				cellRects[i][c].draw(g2);
			}
		}

		// settings
		g2.setColor(Color.BLACK);
		alphaText.draw(g2);
		alphaSlider.draw(g2);
		clearButton.draw(g2);
		brushSizeText.draw(g2);
		brushSizeSlider.draw(g2);
		fillRandomButton.draw(g2);
		alphaRangeText.draw(g2);
		fillPercentageText.draw(g2);
		seperatingRect1.draw(g2);
		seperatingRect2.draw(g2);
		fillPercentageSlider.draw(g2);
		alphaRangeSlider.draw(g2);
		
		// back- continue buttons
		backButton.draw(g2);
		continueButton.draw(g2);
	}

}
