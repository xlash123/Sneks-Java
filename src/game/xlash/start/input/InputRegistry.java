package game.xlash.start.input;

import java.awt.event.KeyEvent;

import net.java.games.input.Component;
import net.java.games.input.Controller;

public class InputRegistry {

	public boolean up, down, left, right;
	public final InputType type;
	public Controller controller;
	public boolean valid;
	
	public InputRegistry(InputType type) {
		this.type = type;
		this.valid = true;
	}
	
	public InputRegistry(Controller controller) {
		this(InputType.CONTROLLER);
		this.controller = controller;
	}
	
	public boolean pollInput() {
		switch(type) {
		case ARROWS:
			setUp(InputListener.isKeyDown(KeyEvent.VK_UP));
			setDown(InputListener.isKeyDown(KeyEvent.VK_DOWN));
			setLeft(InputListener.isKeyDown(KeyEvent.VK_LEFT));
			setRight(InputListener.isKeyDown(KeyEvent.VK_RIGHT));
			break;
		case NUMPAD:
			setUp(InputListener.isKeyDown(KeyEvent.VK_NUMPAD8));
			setDown(InputListener.isKeyDown(KeyEvent.VK_NUMPAD5));
			setLeft(InputListener.isKeyDown(KeyEvent.VK_NUMPAD4));
			setRight(InputListener.isKeyDown(KeyEvent.VK_NUMPAD6));
			break;
		case WASD:
			setUp(InputListener.isKeyDown(KeyEvent.VK_W));
			setDown(InputListener.isKeyDown(KeyEvent.VK_S));
			setLeft(InputListener.isKeyDown(KeyEvent.VK_A));
			setRight(InputListener.isKeyDown(KeyEvent.VK_D));
			break;
		case CONTROLLER:
			if(valid && controller.poll()) {
				float x = 0, y = 0;
				boolean up = false, down = false, left = false, right = false;
				for(Component c : controller.getComponents()) {
					if(c.getName().equals("X Axis")) {
						x = c.getPollData();
					}else if(c.getName().equals("Y Axis")) {
						y = -c.getPollData();
					}else if(c.getName().equals("Hat Switch")) {
						float data = c.getPollData();
						up = data==0.25 || data==0.125 || data==.375;
						down = data==0.75 || data==.625|| data==.875;
						left = data==1 || data==.125 || data==.875;
						right = data==.5 || data==.375 || data==.625;
					}
				}
				setUp(up || y >= 0.7f);
				setDown(down || y <= -0.7f);
				setLeft(left || x <= -0.7f);
				setRight(right || x >= 0.7f);
			} else {
				this.valid = false;
				return false;
			}
			break;
		}
		return true;
	}
	
	public void clear() {
		up = false;
		down = false;
		left = false;
		right = false;
	}
	
	public boolean hasActions() {
		return up || down || left || right;
	}
	
	public void setUp(boolean b) {
		if(b && !up) up = true;
	}
	
	public void setDown(boolean b) {
		if(b && !down) down = true;
	}
	
	public void setLeft(boolean b) {
		if(b && !left) left = true;
	}
	
	public void setRight(boolean b) {
		if(b && !right) right = true;
	}
	
}
