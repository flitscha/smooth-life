package scenes;

import static game_of_life.Constants.BACKGROUND_COLOR;
import static game_of_life.Constants.BUTTON_COLOR_1;
import static game_of_life.Constants.BUTTON_COLOR_2;
import static game_of_life.Constants.COLOR_PALETTE;
import static game_of_life.Constants.WIN_HEIGHT;
import static game_of_life.Constants.WIN_WIDTH;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import game_of_life.KL;
import game_of_life.ML;
import game_of_life.Settings;
import game_of_life.Window;
import objects.Button;
import objects.Rect;
import objects.Slider;
import objects.Text;
import utils.Scenes;
import utils.Time;

public class SimulationScene extends Scene {
	
	private KL keyListener;
	private ML mouseListener;
	
	private int innerRadius, outerRadius;
	private float maxInnerSum, maxOuterSum; // if the circles are fully filled
	
	private double time;
	private double timePerFrame;
	
	private Rect backgroundRect, titleRect;
	private Text titleText;
	private Button backButton, restartButton;
	
	private Rect gridBackgroundRect;
	private int gridSize;
	private double cellSize;
	private Rect[][] cellRects;
	private float[][] cellValues;	
	private float[][] newCellValues;
	
	// settings
	private Slider speedSlider;
	private Text speedText;
	private float speed; // between 0 and 1
	
	private float dt = 0.1f;
	
	public SimulationScene(KL keyListener, ML mouseListener) {
		this.time = Time.getTime();
		calculateTimePerFrame();
		
		this.keyListener = keyListener;
		this.mouseListener = mouseListener;
		
		loadSettings();
		this.cellValues = new float[gridSize][gridSize];
		this.newCellValues = new float[gridSize][gridSize];
		loadValues();
		
		double insetsTop = Window.getWindow().getInsets().top;
		double winHeight = WIN_HEIGHT - insetsTop;
		
		maxInnerSum = calculateMaxSum(innerRadius);
		maxOuterSum = calculateMaxSum(outerRadius) - maxInnerSum;
		
		// title
		backgroundRect = new Rect(0, 0, WIN_WIDTH, WIN_HEIGHT, BACKGROUND_COLOR);
		titleRect = new Rect(winHeight, 0, WIN_WIDTH, 120, COLOR_PALETTE[2]);
		titleText = new Text("simulation", winHeight+(WIN_WIDTH-winHeight)/2, 75, 60, true, true);
		
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
		// speed
		speedText = new Text("speed: "+(int)(speed*10), winHeight+20, 150, 30, false, true);
		speedSlider = new Slider((int)winHeight+20, 180, 100, speed, COLOR_PALETTE[4], COLOR_PALETTE[2]);
		
		// back- and continue button
		backButton = new Button((int)(winHeight+20), WIN_HEIGHT-80, 120, 50, BUTTON_COLOR_1, BUTTON_COLOR_2, "back", 30);
		restartButton = new Button(WIN_WIDTH-140, WIN_HEIGHT-80, 120, 50, BUTTON_COLOR_1, BUTTON_COLOR_2, "restart", 30);
	}
	
	
	private void calculateTimePerFrame() {
		double minTime = 0.01;
		double maxTime = 1.0;
		timePerFrame = (maxTime-minTime)*(1-speed);
	}
	
	
	private void loadValues() {
		for (int i = 0; i < gridSize; i++) {
			for (int c = 0; c < gridSize; c++) {
				cellValues[i][c] = Settings.grid[i][c];
			}
		}
	}
	
	private void loadSettings() {
		this.innerRadius = Settings.innerFunctionRadius;
		this.outerRadius = Settings.outerFunctionRadius;
		this.gridSize = Settings.gridSize;
	}
	
	
	private void saveSettings() {
		
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

	
	// inefficient variant: always count everything
	private float calculateSum(int y, int x, int radius) {
		float sum = 0;
		
		// iterate over each cell, in the square
		for (int i = y - radius; i <= y + radius; i++) {
			for (int c = x - radius; c <= x + radius; c++) {
				// check if we are in the circle
				if ((c-x)*(c-x) + (i-y)*(i-y) < (radius+0.5)*(radius+0.5)) {
					sum += cellValues[(i+gridSize)%gridSize][(c+gridSize)%gridSize];
				}
			}
		}
		return sum; //- cellValues[(y+gridSize)%gridSize][(x+gridSize)%gridSize];
	}
	
	
	private float calculateMaxSum(int radius) {
		float sum = 0;
		for (int i = -radius; i <= radius; i++) {
			for (int c = -radius; c <= radius; c++) {
				if (i*i + c*c < (radius+0.5)*(radius+0.5)) {
					sum += 1f;
				}
			}
		}
		return sum;
	}
	
	
	private void getNewValues() {
		for (int i = 0; i < gridSize; i++) {
			for (int c = 0; c < gridSize; c++) {
				float innerSum = calculateSum(i, c, innerRadius);
				float outerSum = calculateSum(i, c, outerRadius) - innerSum;
				
				// calculate the function indexes
				int index1 = (int)((1-(innerSum / maxInnerSum)) * (Settings.functionResolution-1));
				int index2 = (int)(outerSum / maxOuterSum * (Settings.functionResolution-1));
				newCellValues[i][c] = 2*Settings.simpleFunctionGrid[index1][index2]-1;
			}
		}
	}
	
	
	private void applyNewValues() {
		for (int i = 0; i < gridSize; i++) {
			for (int c = 0; c < gridSize; c++) {
				float newValue = clamp(cellValues[i][c] + dt*newCellValues[i][c], 0, 1);
				cellValues[i][c] = newValue;//newCellValues[i][c];
				cellRects[i][c].setColor(simulateAlpha(COLOR_PALETTE[4], COLOR_PALETTE[0], newValue));//newCellValues[i][c]));
			}
		}
	}
	
	private float clamp(float value, float min, float max) {
		return Math.max(min, Math.min(max, value));
	}
	

	@Override
	public void update(double dt) {
		backButton.update(mouseListener);
		restartButton.update(mouseListener);
		speedSlider.update(mouseListener);
		
		// settings
		if (speedSlider.isChanging()) {
			speed = (float)speedSlider.getValue();
			speedText.setString("speed: "+(int)(speed*10));
			calculateTimePerFrame();
		}
		
		// back button
		if (backButton.isPressed()) {
			saveSettings();
			Window.getWindow().changeScene(Scenes.FUNCTION_EDITOR_SCENE);
		}
		
		// restart button
		if (restartButton.isPressed()) {
			loadValues();
		}
		
		// simulation
		if (speed != 0) {
			if (Time.getTime() > time + timePerFrame) {
				time = Time.getTime();
				getNewValues();
				applyNewValues();
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
		speedSlider.draw(g2);
		speedText.draw(g2);
		
		// back , restart button
		backButton.draw(g2);
		restartButton.draw(g2);
	}
}
