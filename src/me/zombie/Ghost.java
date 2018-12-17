package me.zombie;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Ghost {

	
	int speed = (int)(Math.random()*2 + 1);
	int health=(int)(Math.random()*10 + 1);
	int damage=(int)(Math.random()*2 + 1);

	int x,y,vx=speed,vy=speed,trigger=GUI.panSize+(GUI.panSize/2),radius=10;
	
	BufferedImage zombieImg = null;
	
	
		
	
	Ghost(int x, int y){
		this.x=x;
		this.y=y;
	}
	
	void checkClose(Player p) {	//Check if the player has entered the trigger radius
		int dX=x-p.x,dY=y-p.y;
		
		//Use pythagorean formula to find the distance between the ghost and player
		double length=Math.sqrt(dX*dX+dY*dY);
		
		if (length<=trigger) {	//Check if they're close enough
			move(p);
		} else {
			x+=p.vx;
			y+=p.vy;
		}
	}
	
	void move(Player p){	//Move toward the player (takes player x and y)
		if (x>p.x) {	//If the ghost is to the right of the player
			vx=-speed;
			zombieImg = ImageHandler.getImage("ZombieLeft");
		} else if (x==p.x){	//If they are on the same y axis
			vx=0;
		} else {	//If the ghost is to the left of the player
			vx=speed;
			zombieImg = ImageHandler.getImage("ZombieRight");
		}
		
		if (y>p.y) {	//If the ghost is lower than the player
			vy=-speed;
			zombieImg = ImageHandler.getImage("ZombieUp");
		} else if (y==p.y) {	//If it is at the same height
			vy=0;
		} else {	//If the ghost is above the player
			vy=speed;
			zombieImg = ImageHandler.getImage("ZombieDown");
		}
		
		x+=vx+p.vx;
		y+=vy+p.vy;
	}
	
	void paint(Graphics2D g) {
		//g.fillOval(this.x-radius, this.y-radius, 2*radius, 2*radius);
		g.drawImage(zombieImg,this.x-radius, this.y-radius, 2*radius, 2*radius, null);
	}
	
	void checkHit(Bullet b){
		int dX=this.x-(int)(b.x),dY=this.y-(int)(b.y);
		
		//Use pythagorean formula to find the distance between the ghost and bullet
		double length=Math.sqrt(dX*dX+dY*dY);
		
		/* Check if they are intersecting by adding the radii of the ghost and the bullet and seeing if
		 * the distance between them is equal or less than that
		 */
		if (length<=radius+b.radius) {
			
			//for testing:
			//System.out.println("health: " + health + " | damage: " + damage + " | speed: " +speed);	
			
			health-=b.damage;
			b.health--;
		}
	}
}