package game.xlash.display;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

import game.xlash.start.input.InputListener;

public abstract class Window {
	
	public JFrame frame;
	public JPanel panel;
	
	public Window() {
		frame = new JFrame("Sneks");
		panel = new JPanel() {
			@Override
			public void paintComponent(java.awt.Graphics g) {
				Graphics2D g2d = (Graphics2D) g;
				super.paintComponent(g2d);
				draw(g2d);
			}
		};
		panel.setDoubleBuffered(true);
		panel.setBackground(new Color(45, 5, 45));
		frame.setResizable(false);
		panel.setPreferredSize(new Dimension(599, 599));
		InputListener input = new InputListener();
		panel.addKeyListener(input);
		panel.addMouseListener(input);
		frame.add(panel);
		frame.pack();
		panel.requestFocus();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public void repaint() {
		frame.repaint();
	}
	
	public Dimension getSize() {
		return panel.getSize();
	}
	
	public abstract void draw(Graphics2D g2d);

}
