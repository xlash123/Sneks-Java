package game.xlash.entity;

import java.awt.Color;

public enum TeamEnum {

	RED(Color.red), BLUE(Color.blue), NONE(Color.white);
	
	public final Color color;
	
	private TeamEnum(Color color) {
		this.color = color;
	}
	
	public boolean isOnSameTeam(TeamEnum other) {
		return other==this && this!=NONE;
	}
	
}
