package game.xlash.display.gui;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

public abstract class Gui {
	
	public Rectangle bounds;
	private boolean localized;
	
	public Gui(Rectangle bounds) {
		this.bounds = bounds;
	}
	
	public void onClick(Point p) {
		
	}
	
	public void onType(char c) {
		
	}
	
	public abstract void update();
	
	public void updateBetweenFrame() {
		
	}
	
	protected void localizeGraphics(Graphics2D g2d) {
		if(!localized) g2d.translate(bounds.x, bounds.y);
		localized = true;
	}
	
	protected void unlocalizeGraphics(Graphics2D g2d) {
		if(localized) g2d.translate(-bounds.x, -bounds.y);
		localized = false;
	}
	
	public abstract void draw(Graphics2D g2d);

}
