package game.xlash.start.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import game.xlash.start.Start;

public class InputListener implements KeyListener, MouseListener{
	
	private static ArrayList<Integer> keys = new ArrayList<>();
	private static ArrayList<Integer> mouse = new ArrayList<>();
	
	public static boolean isKeyDown(int key) {
		return keys.contains(key);
	}
	
	public static boolean isMouseButtonDown(int button) {
		return mouse.contains(button);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		Object code = e.getKeyCode();
		if(!keys.contains(code)) {
			keys.add((Integer) code);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		Object code = e.getKeyCode();
		if(keys.contains(code)) {
			keys.remove(code);
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int code = e.getButton();
		if(code==MouseEvent.BUTTON1) {
			Start.game.currentGui.onClick(e.getPoint());
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		Object code = e.getButton();
		if(!mouse.contains(code)) {
			mouse.add((Integer) code);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		Object code = e.getButton();
		if(mouse.contains(code)) {
			mouse.remove(code);
		}
	}

}
