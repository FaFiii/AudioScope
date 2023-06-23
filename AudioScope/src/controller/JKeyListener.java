package controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class JKeyListener implements KeyListener {
	private volatile boolean keyNotPressed = true;
	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}


	/**
	 * overrides keyReleased() so that when any button is released keyNotPressed is set false 
	 * @param e KeyEvent
	 */
	@Override
	public void keyReleased(KeyEvent arg0) {
    	if (arg0.getKeyCode() == KeyEvent.VK_SPACE) {
	        keyNotPressed = false;
	        System.out.println("key released");//debug
	    }		
	}

    @Override
	public void keyTyped(KeyEvent arg0) {

	}

	public boolean isKeyNotPressed() {
		return keyNotPressed;
	}

	public void setKeyNotPressed(boolean keyNotPressed) {
		this.keyNotPressed = keyNotPressed;
	}

}
