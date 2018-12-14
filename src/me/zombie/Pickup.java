package me.zombie;

import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Pickup extends Rectangle{
	boolean picked=false;
	boolean knife=false;
	
	Pickup(int x, int y){
		this.x=x;
		this.y=y;
		width=40;
		height=20;
	}
	
	void paint(Graphics2D g) {
		g.drawRect(x, y, width, height);
	}
	
	void move(Player p) {
		x+=p.vx;
		y+=p.vy;
	}
}
