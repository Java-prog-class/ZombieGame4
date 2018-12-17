package me.zombie;

import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Barrier extends Rectangle{
	boolean wall;
	
	Barrier(int x, int y, int length, boolean vertical){
		width=10;
		height=10;
		
		this.x=x;
		this.y=y;
		if (vertical) {
			height = length;
			wall=true;
		} else {
			width=length;
			wall=false;
		}
	}
	
	public void paint(Graphics2D g) {
		g.fillRect(x, y, width, height);
	}
	
	public void move(Player p) {
		x+=p.vx;
		y+=p.vy;
	}
}