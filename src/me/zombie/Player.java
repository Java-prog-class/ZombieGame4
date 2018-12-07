package me.zombie;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;

public class Player {

	//***global variables***
	final int x=GUI.panSize/2, y=x, radius=10;
	
	//stats
	private static int maxHealth = 10;
	private static int maxSpeed = 3;
	private static int minSpeed = 1;
	private static int maxAttack = 10;	//TODO: depending on Weapons class attack variable here may be unnecessary
	private static int minAttack = 5; 
	int health = maxHealth;
	int speed = 3;	
	int attack = 10; 
	
	//start coords
	int vx=0;
	int vy=0;
	
	boolean iFrames=false;
	
	public Player() {
	}
	
	public void statCheck() {	//change stats depending on level of health
		if (health > maxHealth) health = maxHealth;	//health can't go higher than 100
		if (speed > maxSpeed) speed = maxSpeed;		//speed can't go higher than 15
		if (speed < minSpeed) speed = minSpeed; 	//speed can't go lower than 5
		if (attack > maxAttack) attack = maxAttack;	//attack can't go higher than 10
		if (attack < minAttack) attack = minAttack;	//attack can't go lower than 5
		
		if (health <= 50) speed = speed/2;	//at 50% health reduce speed
		if (health <= 0); //player death if health = 0... close game? end screen?
	}
	
	public void healthChange() {	//adds or subtracts HP if touched by zombie... maybe combine w statCheck
		//if different types of zombies change damage intake? 
		
		/* if (Player.x == Zombie.??? && Player.y == Zombie.???){
		 * 		health = health - 1; 	//maybe reduce by more?
		 * }
		 */
	}
	
	public void weaponCheck() {	//change weapon in use, adjust stats? or take care of this in Weapons class
		//if (WeaponClass.weaponName = true...){
		//all other Weapons.weaponName = false
		//strength + or - accordingly.
	}
	
	public void move(KeyEvent e) {	//moves player using WASD or arrow keys... or moves BG 
		//up
		if(e.getKeyCode() == e.VK_W || e.getKeyCode() == e.VK_UP) {
			vy = speed;
		}
		//down
		if(e.getKeyCode() == e.VK_S || e.getKeyCode() == e.VK_DOWN) {
			vy = -speed;
		}
		//left
		if(e.getKeyCode() == e.VK_A || e.getKeyCode() == e.VK_LEFT) {
			vx = speed;
		}
		//right
		if(e.getKeyCode() == e.VK_D || e.getKeyCode() == e.VK_RIGHT) {
			vx = -speed;
		}
	}
	
	public void stopMove(KeyEvent e) {
		if(e.getKeyCode() == e.VK_W || e.getKeyCode() == e.VK_UP) {
			vy = 0;
		}
		//down
		if(e.getKeyCode() == e.VK_S || e.getKeyCode() == e.VK_DOWN) {
			vy = 0;
		}
		//left
		if(e.getKeyCode() == e.VK_A || e.getKeyCode() == e.VK_LEFT) {
			vx = 0;
		}
		//right
		if(e.getKeyCode() == e.VK_D || e.getKeyCode() == e.VK_RIGHT) {
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
}