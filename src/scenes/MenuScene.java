package scenes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import static game_of_life.Constants.*;
import game_of_life.KL;
import game_of_life.ML;
import game_of_life.Settings;
import game_of_life.Window;
import objects.Button;
import objects.Rect;
import objects.Slider;
import objects.Text;
import utils.Modes;
import utils.Scenes;

public class MenuScene extends Scene {
	private KL keyListener;
	private ML mouseListener;
	private Rect backgroundRect = new Rect(0, 0, WIN_WIDTH, WIN_HEIGHT, BACKGROUND_COLOR);
	private Rect titleRect;
	private Text titleText, sizeText, functionModeText;
	private Slider sizeSlider, innerRadiusSlider, outerRadiusSlider;
	private Button startButton, exitButton;
	private int gridSize, innerRadius, outerRadius;
	private Text gridSizeText, innerFunctionRadiusText, outerFunctionRadiusText;
	private Button functionModeButton;
	private Modes functionMode = Modes.SIMPLE_FUNCTION;
	
	
	public MenuScene(KL keyListener, ML mouseListener) {
		this.keyListener = keyListener;
		this.mouseListener = mouseListener;
		loadSettings();
		
		// title
		titleRect = new Rect(0, 0, WIN_WIDTH, 200, COLOR_PALETTE[2]);
		titleText = new Text("game of life", WIN_WIDTH/2, 110, 90, true, true);
		
		// settings
			// size Slider
		sizeText = new Text("grid size: "+gridSize+" x "+gridSize, WIN_WIDTH/2-200, 250, 30, false, true);
		double sizeSliderPosition = (double)(gridSize - MIN_GRID_SIZE) / (MAX_GRID_SIZE - MIN_GRID_SIZE);
		sizeSlider = new Slider(WIN_WIDTH/2-160, 280, 250, sizeSliderPosition, COLOR_PALETTE[4], COLOR_PALETTE[2]);
		gridSizeText = new Text("", WIN_WIDTH/2-150 + 170, 250, 30, false, true);
		
			// inner radius slider
		innerFunctionRadiusText = new Text("inner function radius: "+Settings.innerFunctionRadius + " cells", 
				WIN_WIDTH/2-200, 350, 30, false, true);
		double sliderPosition = (double)(innerRadius - MIN_INNER_RADIUS) / 
				(MAX_INNER_RADIUS - MIN_INNER_RADIUS);
		innerRadiusSlider = new Slider(WIN_WIDTH/2-160, 380, 250, sliderPosition, COLOR_PALETTE[4], COLOR_PALETTE[2]);
		
			// outer function radius slider
		outerFunctionRadiusText = new Text("outer function radius: "+Settings.outerFunctionRadius + " cells",
				WIN_WIDTH/2-200, 450, 30, false, true);
		sliderPosition = (double)(outerRadius - MIN_OUTER_RADIUS) /
				(MAX_OUTER_RADIUS - MIN_OUTER_RADIUS);
		outerRadiusSlider = new Slider(WIN_WIDTH/2-160, 480, 250, sliderPosition, COLOR_PALETTE[4], COLOR_PALETTE[2]);
		
			// function Mode
		functionModeText = new Text("function mode:", WIN_WIDTH/2-200, 560, 30, false, true);
		functionModeButton = new Button(WIN_WIDTH/2+10, 545, 300, 30, 
				BUTTON_COLOR_1, BUTTON_COLOR_2, FUNCTION_MODE_STRINGS[functionMode.ordinal()], 25);
		
		// start/exit button
		startButton = new Button(WIN_WIDTH-200, WIN_HEIGHT-100, 150, 50, BUTTON_COLOR_1, BUTTON_COLOR_2, "continue", 30);
		exitButton = new Button(50, WIN_HEIGHT-100, 150, 50, BUTTON_COLOR_1, BUTTON_COLOR_2, "exit", 30);
	}
	
	private void loadSettings() {		functionMode = Settings.mode;
		innerRadius = Settings.innerFunctionRadius;
		outerRadius = Settings.outerFunctionRadius;
		gridSize = Settings.gridSize;
	}
	
	
	@Override
	public void update(double dt) {
		startButton.update(mouseListener);
		exitButton.update(mouseListener);
		sizeSlider.update(mouseListener);
		innerRadiusSlider.update(mouseListener);
		outerRadiusSlider.update(mouseListener);
		functionModeButton.update(mouseListener);
		
		// update setting changes
		if (sizeSlider.isChanging()) {
			gridSize = (int)(MIN_GRID_SIZE + sizeSlider.getValue() * (MAX_GRID_SIZE - MIN_GRID_SIZE));
			sizeText.setString("grid size: " + gridSize + " x " + gridSize);
		}
		if (innerRadiusSlider.isChanging()) {
			innerRadius = (int)(MIN_INNER_RADIUS + innerRadiusSlider.getValue() * (MAX_INNER_RADIUS - MIN_INNER_RADIUS));
			innerFunctionRadiusText.setString("inner function radius: " + innerRadius + " cells");
		}
		if (outerRadiusSlider.isChanging()) {
			outerRadius = (int)(MIN_OUTER_RADIUS + outerRadiusSlider.getValue() * (MAX_OUTER_RADIUS - MIN_OUTER_RADIUS));
			outerFunctionRadiusText.setString("outer function radius: " + outerRadius + " cells");
		}
		if (functionModeButton.isReleased()) {
			Modes[] modes = Modes.values(); // Enum to array
			int functionModeIndex = (functionMode.ordinal() + 1) % modes.length; 
			functionMode = modes[functionModeIndex]; 
			functionModeButton.changeText(FUNCTION_MODE_STRINGS[functionModeIndex]);
		}
		
		// exit button
		if (exitButton.isReleased()) {
			Window.getWindow().close();
		}
		
		// start button: save settings and change to map editor
		if (startButton.isReleased()) {
			// update the saved grid in the Settings, if the gridSize changed
			if (this.gridSize != Settings.gridSize) {
				Settings.newGrid(this.gridSize);
			}
			// update the function grid, if the function radius changed
			//if (this.radius != Settings.functionRadius) {
			//	Settings.newSimpleFunctionGrid(radius);
			//}
			Settings.gridSize = this.gridSize;
			Settings.innerFunctionRadius = this.innerRadius;
			Settings.outerFunctionRadius = this.outerRadius;
			Settings.mode = this.functionMode;
			Window.getWindow().changeScene(Scenes.EDITOR_SCENE);
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
		
		// settings
		g2.setColor(Color.BLACK);
		sizeText.draw(g2);
		sizeSlider.draw(g2);
		gridSizeText.draw(g2);
		
		innerFunctionRadiusText.draw(g2);
		outerFunctionRadiusText.draw(g2);
		innerRadiusSlider.draw(g2);
		outerRadiusSlider.draw(g2);
		
		//functionModeText.draw(g2);
		//functionModeButton.draw(g2);
		
		// start/exit button
		startButton.draw(g2);
		exitButton.draw(g2);
		
	}

}
