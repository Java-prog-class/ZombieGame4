package me.zombie;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;

public class Player {

	//***global variables***
	final int x=GUI.panSize/2, y=x, radius=10;
	Weapon held=new Weapon(0);
	
	//stats
	int health = 100;
	int speed = 3;	
	int attack = 10; 
	
	//start coords
	int vx=0;
	int vy=0;
	
	boolean iFrames=false,firing=false,hasShot=false;
	
	public Player() {
	}
	
	public void move(KeyEvent e) {	//moves player using WASD or arrow keys... or moves BG 
		//up
		if(e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_UP) {
			vy = speed;
		}
		//down
		if(e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_DOWN) {
			vy = -speed;
		}
		//left
		if(e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_LEFT) {
			vx = speed;
		}
		//right
		if(e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_RIGHT) {
			vx = -speed;
		}
	}
	
	public void stopMove(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_UP) {
			vy = 0;
		}
		//down
		if(e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_DOWN) {
			vy = 0;
		}
		//left
		if(e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_LEFT) {
			vx = 0;
		}
		//right
		if(e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_RIGHT) {
			vx = 0;
		}
	}
	
	public void checkHit(Ghost z) {
		int dX=this.x-z.x,dY=this.y-z.y;
		
		//Use pythagorean formula to find the distance between the zombie and bullet
		double length=Math.sqrt(dX*dX+dY*dY);
		
		/*
		 * Check if they are intersecting by adding the radii of the zombie and the bullet and seeing if
		 * the distance between them is equal or less than that
		 */
		if (length<=radius+z.radius && !iFrames) {
			health-=z.damage;
			iFrames=true;
		}
	}
	
	public boolean checkHit(Barrier b) {
		Rectangle p=new Rectangle(x-radius,y-radius,radius*2,radius*2);
		
		if (p.intersects(b)) {
			return true;
		}
		return false;
	}
	
	boolean checkHit(Pickup p) {
		Rectangle play=new Rectangle(x-radius,y-radius,radius*2,radius*2);
		
		if (p.intersects(play) && held.ammo<held.ammoMax && held.weaponHeld!=Weapon.PISTOL) {
			return true;
		}
		return false;
	}
}