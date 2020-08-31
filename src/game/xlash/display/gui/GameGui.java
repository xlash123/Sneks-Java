package game.xlash.display.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.function.Predicate;

import game.xlash.entity.Food;
import game.xlash.entity.Snake;
import game.xlash.start.Game;
import game.xlash.start.input.InputListener;

public class GameGui extends Gui{

	public Game game;
	
	public GameGui(Game game) {
		super(new Rectangle(0, 0, 599, 599));
		this.game = game;
	}
	
	@Override
	public void update() {
		if(game.gameOver()) {
			if(InputListener.isKeyDown(KeyEvent.VK_SPACE)) {
				game.newGame();
			}
		}else {
			
			for(int i=0; i<game.snakes.size(); i++) {
				Snake s = game.snakes.get(i);
				
				int removed = 0;
				for(Food food : game.foods) {
					if(!s.dead && s.isTouching(food) && !food.dead) {
						s.shouldGrow = true;
						food.kill();
						removed++;
					}
				}
				game.foods.removeIf(new Predicate<Food>() {
					@Override
					public boolean test(Food t) {
						return t.dead;
					}
				});
				for(int j=0; j<removed; j++) {
					game.foods.add(new Food(game));
				}
				
				s.update();
				
				if(!s.dead && (s.isTouchingSelf() || s.isTouchingEdge())) {
					s.kill();
				}
				if(!s.dead) {
					for(int j=0; j<game.snakes.size(); j++) {
						Snake other = game.snakes.get(j);
						if(other.dead) continue;
						if(s!=other) {
							if(s.isTouchingSnake(game.snakes.get(j))) {
								s.kill();
							}
						}
					}

				}
			}
			for(Snake s : game.snakes) {
				if(s.toDie) {
					s.dead = true;
					s.toDie = false;
				}
			}
		}
	}
	
	@Override
	public void updateBetweenFrame() {
		for(Snake s : game.snakes) {
			// Poll input, and kill Snake if controller was unplugged
			if(!s.input.pollInput()) {
				s.dead = true;
			}
		}
	}

	@Override
	public void draw(Graphics2D g2d) {
		for(int i=0; i<game.snakes.size(); i++) {
			Snake s = game.snakes.get(i);
			if(!s.dead) {
				g2d.setColor(s.team.color);
				g2d.drawString("P"+(i+1), s.getHead().x*Game.size, s.getHead().y*Game.size+12);
				s.draw(g2d, Game.size);
			}
		}
		for(Food food : game.foods) {
			food.draw(g2d, Game.size);
		}
		 if(game.gameOver()) {
			g2d.setColor(Color.green);
			g2d.setFont(new Font("Times New Roman", Font.BOLD, 60));
			String deathMessage = "";
			if (game.singleWin) {
				for (int i = 0; i < game.snakes.size(); i++) {
					if (!game.snakes.get(i).dead) {
						deathMessage += "P" + (i + 1) + " WINS!";
						break;
					}
				}
			} else if (game.redWin) {
				deathMessage += "RED TEAM WINS!";
			} else if (game.blueWin) {
				deathMessage += "BLUE TEAM WINS!";
			}
			if (deathMessage.isEmpty())
				deathMessage = "Y'ALL DIED!";
			int width = g2d.getFontMetrics().stringWidth(deathMessage);
			g2d.drawString(deathMessage, bounds.width/2-width/2, bounds.getSize().height/2);
		}
	}

}
