package game_of_life;

import java.awt.Color;

public final class Constants {
	public static final Color[] COLOR_PALETTE = {
		new Color(234,239,189),
		new Color(201,227,172),
		new Color(173,209,141),
		new Color(144,190,109),
		//new Color(234,144,16)
		new Color(210,128,15)
	};
	
	// Window
	public static final int WIN_WIDTH = 1080;
	public static final int WIN_HEIGHT = 720;
	public static final String WIN_TITLE = "game of life";
	
	// colors
	public static final Color BACKGROUND_COLOR = COLOR_PALETTE[3];//Color.DARK_GRAY;
	public static final Color BUTTON_COLOR_1 = COLOR_PALETTE[0];
	public static final Color BUTTON_COLOR_2 = COLOR_PALETTE[1];
	
	// setting limits
	public static final int MIN_GRID_SIZE = 20;
	public static final int MAX_GRID_SIZE = 500;
	public static final int MIN_INNER_RADIUS = 0;
	public static final int MAX_INNER_RADIUS = 7;
	public static final int MIN_OUTER_RADIUS = 1;
	public static final int MAX_OUTER_RADIUS = 21;
	public static final String[] FUNCTION_MODE_STRINGS = {
			"simple", "simple (neural network)", "complex", "complex (neural network)"
	};
	public static final int MAX_BRUSH_SIZE = 10;
	
	public static final int MAX_FUNCTION_BRUSH_SIZE = 10;
	
	
}
