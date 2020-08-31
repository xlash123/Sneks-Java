package game.xlash.start;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.lang.reflect.Constructor;
import java.util.ArrayList;

import game.xlash.display.Window;
import game.xlash.display.gui.GameGui;
import game.xlash.display.gui.Gui;
import game.xlash.display.gui.MainMenuGui;
import game.xlash.entity.Food;
import game.xlash.entity.Snake;
import game.xlash.entity.TeamEnum;
import game.xlash.start.input.InputType;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.Controller.Type;

public class Game {
	
	public static Window window;
	public Gui currentGui;
	
	public static Rectangle field;
	
	public static ArrayList<Controller> controllers;
	
	public static int size = 15;
	
	public ArrayList<Snake> snakes;
	public ArrayList<Food> foods;
	public int foodCount = 3;
	
	public boolean redWin, blueWin, singleWin;
	public int redPlayers, bluePlayers;
	
	public Game() {
		snakes = new ArrayList<>();
		this.controllers = new ArrayList<>();
		window = new Window() {
			@Override
			public void draw(Graphics2D g2d) {
				Game.this.draw(g2d);
			}
		};
		foods = new ArrayList<>();
		field = new Rectangle(0, 0, window.getSize().width, window.getSize().height);
		grabNewControllers();
		currentGui = new MainMenuGui();
		newGame();
		Thread gameLoop = new Thread("Game Loop") {
			@Override
			public void run() {
				Game.this.gameLoop();
			}
		};
		gameLoop.start();
	}
	
	public void grabNewControllers() {
		Controller[] controllers;
		try {
			controllers = createDefaultEnvironment().getControllers();
			this.controllers.clear();
			for (int i = 0; i < controllers.length; i++) {
				if((controllers[i].getType()==Type.GAMEPAD || controllers[i].getType()==Type.STICK) && controllers[i].poll()) {
					this.controllers.add(controllers[i]);
				}
			}
		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
		}
	}
	
	private static ControllerEnvironment createDefaultEnvironment() throws ReflectiveOperationException {

	    // Find constructor (class is package private, so we can't access it directly)
	    Constructor<ControllerEnvironment> constructor = (Constructor<ControllerEnvironment>)
	        Class.forName("net.java.games.input.DefaultControllerEnvironment").getDeclaredConstructors()[0];

	    // Constructor is package private, so we have to deactivate access control checks
	    constructor.setAccessible(true);

	    // Create object with default constructor
	    return constructor.newInstance();
	}
	
	public void addSnakes(ArrayList<Snake> ss) {
		snakes.addAll(ss);
	}
	
	public void newGame() {
		redWin = blueWin = singleWin = false;
		redPlayers = bluePlayers = 0;
//		grabNewControllers();
		foods.clear();
		for(int i=0; i<snakes.size(); i++) {
			Snake s = snakes.get(i);
			s.reset((Game.window.getSize().width/4)/Game.size, (Game.window.getSize().height/4)/Game.size + i*5);
			if (s.team == TeamEnum.RED)
				redPlayers++;
			else if (s.team == TeamEnum.BLUE)
				bluePlayers++;
		}
		for(int i = 0; i < foodCount; i++) {
			foods.add(new Food(this));
		}
	}
	
	public boolean gameOver() {
		if(redWin || blueWin || singleWin) return true;
		int alive=0;
		int red = 0;
		int blue = 0;
		int none = 0;
		for (Snake s : snakes) {
			if (!s.dead) {
				alive++;
				if (s.team == TeamEnum.RED) red++;
				else if (s.team == TeamEnum.BLUE) blue++;
				else if (s.team == TeamEnum.NONE) none++;
			}
		}
		if (none == 0 && (red == 0 || blue == 0)){
			if (red == 0 && bluePlayers > 0) blueWin = true;
			else if (blue == 0 && redPlayers > 0) redWin = true;
			return true;
		} else if (alive == 1) {
			singleWin = true;
			return true;
		}
		return false;
	}
	
	public void gameLoop() {
		while(true) {			
			currentGui.update();
			
			window.repaint();
			try {
				long prevTime = System.currentTimeMillis();
				while(System.currentTimeMillis()-prevTime<(1000/10)) {
					currentGui.updateBetweenFrame();
					Thread.sleep(10);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void draw(Graphics2D g2d) {
		if(currentGui != null) currentGui.draw(g2d);
	}

}
