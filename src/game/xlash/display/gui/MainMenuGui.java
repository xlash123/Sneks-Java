package game.xlash.display.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.function.Predicate;

import game.xlash.entity.Snake;
import game.xlash.entity.TeamEnum;
import game.xlash.start.Game;
import game.xlash.start.Start;
import game.xlash.start.input.InputType;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.Controller.Type;

public class MainMenuGui extends Gui{
	
	public ArrayList<Snake> snakes;
	public ArrayList<Controller> controllers;
	public boolean[] inGame;
	public int screen;
	
	public ButtonGui button;
	public Rectangle teamRed, teamBlue;
	
	public MainMenuGui() {
		super(new Rectangle(0, 0, Game.window.getSize().width, Game.window.getSize().height));
		snakes = new ArrayList<>();
		this.controllers = new ArrayList<>();
		Controller[] cntls = ControllerEnvironment.getDefaultEnvironment().getControllers();
		this.controllers.clear();
		for(int i=0; i<cntls.length; i++) {
			if((cntls[i].getType()==Type.GAMEPAD || cntls[i].getType()==Type.STICK) && cntls[i].poll()) {
				this.controllers.add(cntls[i]);
			}
		}
		snakes.add(new Snake((Game.window.getSize().width/4)/Game.size, (Game.window.getSize().height/4)/Game.size, 10, InputType.NUMPAD));
		snakes.add(new Snake((Game.window.getSize().width/4)/Game.size, (Game.window.getSize().height/4)/Game.size+3, 11, InputType.WASD));
		for(int i=0; i<this.controllers.size(); i++) {
			snakes.add(new Snake((Game.window.getSize().width/4)/Game.size, (Game.window.getSize().height/4)/Game.size+3*(i+2), i+23, this.controllers.get(i)));
		}
		inGame = new boolean[snakes.size()];
		screen = 0;
		
		button = new ButtonGui("Next", new Rectangle(bounds.width-100, 50, 80, 50), Color.GRAY) {
			@Override
			public void action(Point p) {
				screen++;
			}
		};
		teamRed = new Rectangle(0, 0, bounds.width/2, 3*bounds.height/4);
		teamBlue = new Rectangle(bounds.width/2+1, 0, bounds.width/2, 3*bounds.height/4);
	}

	@Override
	public void update() {
		for(int i=0; i<snakes.size(); i++) {
			Snake s = snakes.get(i);
			if(s.input.hasActions()) {
				if((s.xVel<0 && !s.input.right) || (s.xVel>0 && !s.input.left) || (s.yVel<0 && !s.input.down) || (s.yVel>0 && !s.input.up)) {
					s.update();
				}else {
					s.input.clear();
				}
			}
			if(s.isTouchingEdge()) {
				s.undoMove();
			}
			if(screen==0) {
				inGame[i] = s.getHead().y >= 3*bounds.height/(4*Game.size)+1;
			}
			else if(screen==2) {
				if(s.getHead().y > 3*bounds.height/(4*Game.size)+1) {
					s.team = TeamEnum.NONE;
				}else if(s.getHead().x < bounds.width/(2*Game.size)+1) {
					s.team = TeamEnum.RED;
				}else s.team = TeamEnum.BLUE;
			}
			
		}
		if(screen==1) {
			for(int i=0; i<snakes.size(); i++) {
				if(!inGame[i]) snakes.get(i).dead = true;
			}
			snakes.removeIf(new Predicate<Snake>() {
				@Override
				public boolean test(Snake t) {
					return t.dead;
				}
			});
			screen++;
		}else if(screen == 3) {
			Start.game.addSnakes(snakes);
			Start.game.newGame();
			Start.game.currentGui = new GameGui(Start.game);
		}
	}
	
	private boolean isSomeonePlaying() {
		for(boolean b : inGame) {
			if(b) return true;
		}
		return false;
	}
	
	@Override
	public void onClick(Point p) {
		if(button.bounds.contains(p) && (screen == 0 || screen == 2) && isSomeonePlaying()) {
			button.action(p);
		}
	}
	
	@Override
	public void updateBetweenFrame() {
		for(Snake s : snakes) {
			if(!s.input.pollInput()) {
				s.dead = true;
			}
		}
	}
	
	@Override
	public void draw(Graphics2D g2d) {
		g2d.setColor(new Color(20, 180, 40));
		Font original = g2d.getFont();
		if(screen==0) {
			button.draw(g2d);
			g2d.setColor(new Color(20, 180, 40));
			g2d.setFont(new Font("Times New Roman", Font.BOLD, 70));
			int width = g2d.getFontMetrics().stringWidth("Sneks");
			g2d.drawString("Sneks", bounds.width/2-width/2, 80);
			g2d.setFont(new Font("Times New Roman", Font.PLAIN, 25));
			g2d.fillRect(0, 3*bounds.height/4+1, bounds.width, 15);
			g2d.setColor(Color.WHITE);
			g2d.setFont(original);
			width = g2d.getFontMetrics().stringWidth("Cross this line to enter the match.");
			g2d.drawString("Cross this line to enter the match.", bounds.width/2 - width/2, 3*bounds.height/4+14);
		}else if(screen==2) {
			button.draw(g2d);
			g2d.setFont(new Font("Times New Roman", Font.BOLD, 70));
			int width = g2d.getFontMetrics().stringWidth("Select a team");
			g2d.drawString("Select a team", bounds.width/2-width/2, 80);
			g2d.drawLine(bounds.width/2, 90, bounds.width/2, bounds.height);
		}
		g2d.setFont(original);
		for(int i=0; i<snakes.size(); i++) {
			Snake s = snakes.get(i);
			if(!s.dead) {
				g2d.setColor(s.team.color);
				g2d.drawString("P"+(i+1), s.getHead().x*Game.size, s.getHead().y*Game.size+12);
				s.draw(g2d, Game.size);
			}
		}
	}

}
