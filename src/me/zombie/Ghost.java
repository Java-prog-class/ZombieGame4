package me.zombie;

import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Ghost {
	static int speed=1;
	int x,y,vx=speed,vy=speed,trigger=GUI.panSize+(GUI.panSize/2),radius=10,health=5,damage=1;
	
	Ghost(int x, int y){
		this.x=x;
		this.y=y;
	}
	
	void checkClose(Player p) {	//Check if the player has entered the trigger radius
		int dX=x-p.x,dY=y-p.y;
		
		//Use pythagorean formula to find the distance between the zombie and player
		double length=Math.sqrt(dX*dX+dY*dY);
		
		if (length<=trigger) {	//Check if they're close enough
			move(p);
		} else {
			x+=p.vx;
			y+=p.vy;
		}
	}
	
	void move(Player p){	//Move toward the player (takes player x and y
		if (x>p.x) {	//If the zombie if to the right of the player
			vx=-speed;
		} else if (x==p.x){	//If they are on the same y axis
			vx=0;
		} else {	//If the zombie is to the left of the player
			vx=speed;
		}
		
		if (y>p.y) {	//If the zombie is lower than the player
			vy=-speed;
		} else if (y==p.y) {	//If it is at the same height
			vy=0;
		} else {	//If the zombie is above the player
			vy=speed;
		}
		
		x+=vx+p.vx;
		y+=vy+p.vy;
	}
	
	void paint(Graphics2D g) {
		g.fillOval(this.x-radius, this.y-radius, 2*radius, 2*radius);
	}
	
	void checkHit(Bullet b){
		int dX=this.x-(int)(b.x),dY=this.y-(int)(b.y);
		
		//Use pythagorean formula to find the distance between the zombie and bullet
		double length=Math.sqrt(dX*dX+dY*dY);
		
		/*
		 * Check if they are intersecting by adding the radii of the zombie and the bullet and seeing if
		 * the distance between them is equal or less than that
		 */
		if (length<=radius+b.radius) {
			health-=b.damage;
			b.hasHit=true;
		}
	}
}