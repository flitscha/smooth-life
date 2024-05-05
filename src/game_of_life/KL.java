package game_of_life;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KL implements KeyListener {
	private boolean[] keys = new boolean[128];
	private boolean[] keysBefore = new boolean[128];
	
    @Override
    public void keyTyped(KeyEvent e) {
        // Implementiere Aktionen für das keyTyped-Event
    }

    @Override
    public void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
    	keys[e.getKeyCode()] = false;
    }

    
    public void update() {
        // Aktualisiere keysBefore, um den Status der vorherigen Frame-Tasten zu speichern
        System.arraycopy(keys, 0, keysBefore, 0, keys.length);
    }
    
    
    public boolean isPressed(int i) { // flanke
    	return keys[i] && !keysBefore[i];
    }
    
    public boolean keyPressed(int i) { // normal
    	return keys[i];
    }
    
    
    // this function checks the keys array. if there is a key pressed, it returns this key. If not it returns -1
    // (flanken)
    public int whatKeyIsPressed() {
    	for(int i = 0; i < keys.length; i++) {
    		if(isPressed(i)) {
    			return i;
    		}
    	}
    	return -1;
    }
}