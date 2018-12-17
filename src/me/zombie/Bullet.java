package me.zombie;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

public class Bullet {
	double x=GUI.panSize/2,y=GUI.panSize/2,vx=0,vy=0;
	int radius=5,damage=1,speed=0,health=0;
	int distance=0,maxD;
	
	Bullet(Player p, int a, int b){
		radius=p.held.radius;
		damage=p.held.damage;
		speed=p.held.speed;
		maxD=p.held.range;
		health=p.held.health;
		double a2=a, b2=b;
		getShotDirection(a2,b2);
		if (p.held.melee) {
			x+=vx;
			y+=vy;
			vx=vy=0;
		}
	}
	
	void paint(Graphics2D g) {
		//convert doubles to ints
		int a=(int) x,b=(int) y;
		g.fillOval(a-radius, b-radius, 2*radius, 2*radius);
	}
	
	void move(Player p) {
		if (!p.held.melee) {
			x+=vx+p.vx;
			y+=vy+p.vy;
		}

		
		distance+=speed;
	}
	
	void getShotDirection(double x,double y){
		double dX=x-this.x,dY=y-this.y;
		
		//I have no idea what tan2 is, but it does trig stuff that is better than my old trig stuff
		double angle = Math.atan2(dY, dX);
		vx = speed * Math.cos(angle);
		vy = speed * Math.sin(angle);
	}
	
	public boolean checkHit(Barrier b) {
		Rectangle bullet=new Rectangle((int)(x)-radius,(int)(y)-radius,radius*2,radius*2);
		
		//Check if the bullet is hitting a wall
		if (b.intersects(bullet)) {
			return true;
		}
		return false;
	}
	
	static Point getShotgun(double x, double y, boolean r) {
		
		//Generates a point that is 30 degrees left or right from the mouse. This will be used to make the shotgun bullets
		double dX=x-GUI.panSize/2,dY=y-GUI.panSize/2;
		
		double hyp=Math.sqrt(dX*dX+dY*dY);
		
		//I had no idea that tangent was popular enough to get a sequel
		double angle = Math.atan2(dY, dX);
		
		//Depending on the boolean, move the point 30 degrees left or right
		if (r) {
			angle+=Math.PI/6;
		} else {
			angle-=Math.PI/6;
		}
		x = hyp * Math.cos(angle)+GUI.panSize/2;
		y = hyp * Math.sin(angle)+GUI.panSize/2;
		
		return new Point((int)x,(int)y);
	}
}
