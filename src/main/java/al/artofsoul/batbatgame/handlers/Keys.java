package al.artofsoul.batbatgame.handlers;

import java.awt.event.KeyEvent;

/**
 * @author ArtOfSoul
 *
 */

// this class contains a boolean array of current and previous key states
// for the 10 keys that are used for this game.
// a key k is down when keyState[k] is true.

public class Keys {
	
	public static final int NUM_KEYS = 16;
	
	public static final boolean KEY_STATE[] = new boolean[NUM_KEYS];
	private static boolean prevKeyState[] = new boolean[NUM_KEYS];
	
	public static final int UP = 0;
	public static final int LEFT = 1;
	public static final int DOWN = 2;
	public static final int RIGHT = 3;
	public static final int BUTTON1 = 4;
	public static final int BUTTON2 = 5;
	public static final int BUTTON3 = 6;
	public static final int BUTTON4 = 7;
	public static final int ENTER = 8;
	public static final int ESCAPE = 9;
	
	public static void keySet(int i, boolean b) {
		if(i == KeyEvent.VK_UP) KEY_STATE[UP] = b;
		else if(i == KeyEvent.VK_LEFT) KEY_STATE[LEFT] = b;
		else if(i == KeyEvent.VK_DOWN) KEY_STATE[DOWN] = b;
		else if(i == KeyEvent.VK_RIGHT) KEY_STATE[RIGHT] = b;
		else if(i == KeyEvent.VK_W) KEY_STATE[BUTTON1] = b;
		else if(i == KeyEvent.VK_E) KEY_STATE[BUTTON2] = b;
		else if(i == KeyEvent.VK_R) KEY_STATE[BUTTON3] = b;
		else if(i == KeyEvent.VK_F) KEY_STATE[BUTTON4] = b;
		else if(i == KeyEvent.VK_ENTER) KEY_STATE[ENTER] = b;
		else if(i == KeyEvent.VK_ESCAPE) KEY_STATE[ESCAPE] = b;
	}
	
	public static void update() {
		for(int i = 0; i < NUM_KEYS; i++) {
			prevKeyState[i] = KEY_STATE[i];
		}
	}
	
	public static boolean isPressed(int i) {
		return KEY_STATE[i] && !prevKeyState[i];
	}
	
	public static boolean anyKeyPress() {
		for(int i = 0; i < NUM_KEYS; i++) {
			if(KEY_STATE[i]) return true;
		}
		return false;
	}
	
}
