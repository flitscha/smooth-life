package game_of_life;

import utils.Modes;

public class Settings {
	
	public static int gridSize = 200;
	public static int innerFunctionRadius = 5;
	public static int outerFunctionRadius = 15;
	public static Modes mode = Modes.SIMPLE_FUNCTION;
	public static float drawAlpha = 1f;
	public static int brushSize = 5;
	public static float randomFillPercentage = 0.7f;
	public static float alphaRangeLow = 0.3f;
	public static float alphaRangeHigh = 0.9f;
	public static float[][] grid = new float[gridSize][gridSize];;
	
	
	public static void newGrid(int size) {
		float[][] newGrid = new float[size][size];
		int minSize = size < gridSize ? size : gridSize;
		for (int i = 0; i < minSize; i++) {
			for (int c = 0; c < minSize; c++) {
				newGrid[i][c] = grid[i][c];
			}
		}
		Settings.grid = newGrid;
	}
	
	
	public static int functionResolution = 100;
	public static float[][] simpleFunctionGrid = new float[functionResolution][functionResolution];
	static {
		for (int i = 0; i < functionResolution; i++) {
			for (int c = 0; c < functionResolution; c++) {
				simpleFunctionGrid[i][c] = 1-(float)(i+1) / functionResolution;
			}
		}
	}
		
	public static int functionBrushSize = 3;
	public static float functionBrushValue = 0.5f;
}




