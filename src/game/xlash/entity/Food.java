package game.xlash.entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Random;

import game.xlash.start.Game;

public class Food {
	
	public Point point;
	
	public boolean dead;
	
	public Food(Game game) {
		boolean flag;
		Random r = new Random();
		do {
			flag = false;
			point = new Point(r.nextInt(Game.window.getSize().width/Game.size), r.nextInt(Game.window.getSize().height/Game.size));
			for(Snake s : game.snakes) {
				if(s.isTouching(this.point)) {
					flag = true;
					break;
				}
			}
			for(Food food : game.foods) {
				if(this.isTouching(food.point)) {
					flag = true;
					break;
				}
			}
		}while(flag);
	}
	
	public boolean isTouching(Point point) {
		return this.point==point;
	}
	
	public void draw(Graphics2D g2d, int size) {
		g2d.setColor(Color.ORANGE);
		g2d.drawRect(size*point.x, size*point.y, size, size);
	}
	
	public void kill() {
		this.dead = true;
	}

}
