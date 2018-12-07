package me.zombie;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

public class Bullet {
	double x,y,vx=0,vy=0;
	int radius=5,damage=1;
	boolean hasHit=false;
	
	Bullet(int x, int y, int a, int b){
		this.x=x;
		this.y=y;
		double a2=a, b2=b;
		getShotDirection(a2,b2);
	}
	
	void paint(Graphics2D g) {
		//convert doubles to ints
		int a=(int) x,b=(int) y;
		g.fillOval(a-radius, b-radius, 2*radius, 2*radius);
	}
	
	void move(Player p) {
		x+=vx+p.vx;
		y+=vy+p.vy;
	}
	
	public void getShotDirection(double x,double y){
		double dX,dY;
		
		//Get the X and Y difference between the player and the mouse
		if (x>this.x) {
			dX=x-this.x;
		} else {
			dX=this.x-x;
		}
		if (y>this.y) {
			dY=y-this.y;
		} else {
			dY=this.y-y;
		}
		
		//use the pythagorean formula to find the length of the line between the player and the mouse		
		double hyp=Math.sqrt(dX*dX+dY*dY);
		
		double right=Math.PI/2;
		
		//Use sine law formula to find the rest of the angles in the triangle
		double angle1= Math.asin(((Math.sin(right)/hyp)*dY));
		double angle2= Math.asin(((Math.sin(right)/hyp)*dX));
		
		hyp=5;	//We want the bullet to move at 5 pixels per tick, so we'll set the hypotenuse to that speed
		
		//Use sine law again to get the X speed and Y speed
		vx=((hyp/Math.sin(right))*Math.sin(angle2));
		vy=((hyp/Math.sin(right))*Math.sin(angle1));
		
		//Since this all take place on Q1, we need to set the correct direction
		
		//Make the values positive
		if (vx<0) vx=-vx;
		if (vy<0) vy=-vy;
		
		if (this.x>x) vx=-vx;	//If the player is further right than the mouse
		if (this.y>y) vy=-vy;	//If the player is further down than the mouse
	}
	
	public boolean checkHit(Barrier b) {
		Rectangle bullet=new Rectangle((int)(x)-radius,(int)(y)-radius,radius*2,radius*2);
		
		if (b.intersects(bullet)) {
			return true;
		}
		return false;
	}
}
