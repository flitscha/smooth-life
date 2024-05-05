package game_of_life;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import scenes.EditorScene;
import scenes.MenuScene;
import scenes.Scene;
import scenes.SimulationScene;
import scenes.functionEditorScene;
import utils.Scenes;
import utils.Time;

public class Window extends JFrame implements Runnable {
	
	public static Window window = null;
	private boolean isRunning = false;
	private Scene currentScene = null;
	private KL keyListener = new KL();
	private ML mouseListener = new ML();
	
	
	public Window(int width, int height, String title) {
		setSize(width, height);
		setTitle(title);
		setResizable(false);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		addKeyListener(keyListener);
		addMouseListener(mouseListener);
		addMouseMotionListener(mouseListener);
		
		isRunning = true;
		changeScene(Scenes.MENU_SCENE);
	}
	
	
	public static Window getWindow() {
		if (window == null) {
			window = new Window(Constants.WIN_WIDTH, Constants.WIN_HEIGHT, Constants.WIN_TITLE);
		}
		return window;
	}
	
	
	public void changeScene(Scenes scene) {
		currentScene = switch (scene) {
		case MENU_SCENE -> new MenuScene(keyListener, mouseListener);
		case EDITOR_SCENE -> new EditorScene(keyListener, mouseListener);
		case FUNCTION_EDITOR_SCENE -> new functionEditorScene(keyListener, mouseListener);
		case SIMULATION_SCENE -> new SimulationScene(keyListener, mouseListener);
		default -> null;
		};
	}
	
	
	public void update(double dt) {
		mouseListener.update();
		currentScene.update(dt);
	}
	
	
	public void draw(double dt) {
		//System.out.println("FPS: " + (int)(1/dt));
		Image dbImage = createImage(getWidth(), getHeight());
		Graphics dbg = dbImage.getGraphics();
		currentScene.draw(dbg);
		getGraphics().drawImage(dbImage, 0, 0, this);
	}
	
	
	@Override
	public void run() {
		double lastFrameTime = 0.0;	
		while(isRunning) {
			double time = Time.getTime();
			double deltaTime = time - lastFrameTime;
			lastFrameTime = time;
			
			update(deltaTime);
			draw(deltaTime);
		}
		SwingUtilities.invokeLater(this::closeFrame);
    }
	
	private void closeFrame() {
		this.dispose();
	}
	
	
	public void close() {
		isRunning = false;
	}

}
