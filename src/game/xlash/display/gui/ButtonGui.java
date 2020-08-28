package game.xlash.display.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

public abstract class ButtonGui extends Gui{
	
	public Color color;
	public String text;

	public ButtonGui(String text, Rectangle bounds, Color background) {
		super(bounds);
		this.text = text;
		this.color = background;
	}

	@Override
	public void update() {
	}
	
	@Override
	public void onClick(Point p) {
		this.action(p);
	}
	
	public abstract void action(Point p);
	
	

	@Override
	public void draw(Graphics2D g2d) {
		Color prevColor = g2d.getColor();
		g2d.setColor(color);
		g2d.fill(bounds);
		g2d.setColor(Color.WHITE);
		this.localizeGraphics(g2d);
		int width = g2d.getFontMetrics().stringWidth(text);
		int height = (int) g2d.getFontMetrics().getStringBounds(text, g2d).getHeight();
		g2d.drawString(text, bounds.width/2-(int)(width/1.85), bounds.height/2+(int)(height/2.6));
		this.unlocalizeGraphics(g2d);
		g2d.setColor(prevColor);
	}

}
