package game.xlash.entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import game.xlash.start.Game;
import game.xlash.start.input.InputRegistry;
import game.xlash.start.input.InputType;
import net.java.games.input.Controller;

public class Snake {
	
	public ArrayList<Point> points = new ArrayList<>();
	public ArrayList<Point> prevPoints = new ArrayList<>();
	
	public int xVel, yVel;
	public boolean shouldGrow;
	public boolean dead;
	public boolean toDie;
	
	public Color colorHead, colorBody;
	public InputRegistry input;
	public TeamEnum team;
	
	public Snake(int x, int y, int seed, InputType type) {
		Random r = new Random(seed);
		colorHead = new Color(r.nextInt(256), r.nextInt(256), r.nextInt(256));
		colorBody = new Color(r.nextInt(256), r.nextInt(256), r.nextInt(256));
		points.add(new Point(x, y));
		points.add(new Point(x-1, y));
		xVel = 1;
		yVel = 0;
		shouldGrow = false;
		team = TeamEnum.NONE;
		if(type != null) this.input = new InputRegistry(type);
	}
	
	public Snake(int x, int y, int seed, Controller c) {
		this(x, y, seed, (InputType) null);
		this.input = new InputRegistry(c);
	}
	
	public void reset(int x, int y) {
		points.clear();
		points.add(new Point(x, y));
		points.add(new Point(x-1, y));
		input.pollInput();
		input.clear();
		xVel = 1;
		yVel = 0;
		shouldGrow = false;
		dead = false;
		toDie = false;
	}
	
	public void setTeam(TeamEnum team) {
		this.team = team;
	}
	
	public boolean isOnSameTeam(Snake snake) {
		return this.team.isOnSameTeam(snake.team);
	}
	
	public void kill() {
		this.toDie = true;
	}
	
	public void update() {
		control(input.up, input.down, input.left, input.right);
		Point head = getHead();
		if(shouldGrow) {
			points.add(0, new Point(head.x+xVel, head.y+yVel));
			shouldGrow = false;
		}else {
			prevPoints.clear();
			for(Point p : points) {
				prevPoints.add(new Point(p.x, p.y));
			}
			for(int i=points.size()-1; i>0; i--) {
				Point back = points.get(i);
				Point front = points.get(i-1);
				back.setLocation(front.x, front.y);
			}
			head.x += xVel;
			head.y += yVel;
		}
		input.clear();
	}
	
	public void undoMove() {
		this.points.clear();
		this.points.addAll(prevPoints);
	}
	
	private void control(boolean up, boolean down, boolean left, boolean right) {
		if(up && yVel==0) {
			xVel = 0;
			yVel = -1;
		} else if(down && yVel==0) {
			xVel = 0;
			yVel = 1;
		} else if(left && xVel==0) {
			xVel = -1;
			yVel = 0;
		} else if(right && xVel==0) {
			xVel = 1;
			yVel = 0;
		}
	}
	
	public boolean isTouching(Food food) {
		return points.get(0).x==food.point.x && points.get(0).y==food.point.y;
	}
	
	public boolean isTouchingSelf() {
		for(int i=0; i<points.size(); i++) {
			Point p0 = points.get(i);
			for(int ii=i+1; ii<points.size(); ii++) {
				Point p1 = points.get(ii);
				if(p0.equals(p1)) return true;
			}
		}
		return false;
	}
	
	public boolean isTouchingSnake(Snake snake) {
		if(!this.isOnSameTeam(snake)) {
			Point head = this.getHead();
			for(Point p : snake.points) {
				if(head.equals(p)) return true;
			}
		}
		return false;
	}
	
	public boolean isTouching(Point point) {
		for(Point p : this.points) {
			if(point.equals(p)) return true;
		}
		return false;
	}
	
	public boolean isTouchingEdge() {
		return this.getHead().x < 0 || this.getHead().x > Game.field.width/Game.size || this.getHead().y < 0 || this.getHead().y > Game.field.height/Game.size;
	}
	
	public Point getHead() {
		return points.get(0);
	}
	
	public void draw(Graphics2D g2d, int size) {
		g2d.setColor(colorBody);
		for(int i=1; i<points.size(); i++) {
			Point p = points.get(i);
			g2d.drawRect(size*p.x, size*p.y, size, size);
		}
		g2d.setColor(colorHead);
		Point head = getHead();
		g2d.drawRect(size*head.x, size*head.y, size, size);
	}

}
