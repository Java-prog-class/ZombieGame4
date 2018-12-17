package me.zombie;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class GUI extends JFrame {
	
	static int panSize=400;	//Size of Screen
	
	final int mapSize=800;	//Size of map
	
	int spawnRate=110;	//Ghost spawn rate
	int mX=0,mY=0;	//Mouse X and Y
	
	Timer t=new Timer(20,new Time());
	DrawingPanel panel=new DrawingPanel();
	Player p=new Player();
	
	ArrayList<Bullet> bullets=new ArrayList<Bullet>();
	ArrayList<Ghost> ghosts=new ArrayList<Ghost>();
	ArrayList<Barrier> barriers=new ArrayList<Barrier>();
	ArrayList<Weapon> weapons=new ArrayList<Weapon>();
	ArrayList<Pickup> pickups=new ArrayList<Pickup>();
	
	//Main program
	GUI(){
		setupData();
		setupGUI();				
		t.start();
	}

	void setupData() {
		Ghost z=new Ghost(100,100);
		ghosts.add(z);
		
		ImageHandler.loadAllImages();
		
	}
	
	void setupGUI() {		
		panel.addKeyListener(new KL());
		panel.addMouseListener(new ML());
		panel.addMouseMotionListener(new MML());
		addWeapons();
		addBarriers();
		pickups.add(new Pickup(150,150));
		
		this.add(panel);	
		this.setTitle("Main graphics ..."); 
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.pack();
		this.setVisible(true);
	}
	
	class DrawingPanel extends JPanel {
		
		DrawingPanel() {	
			this.setBackground(Color.WHITE);			
			this.setPreferredSize(new Dimension(panSize, panSize));	
			setResizable(false);
			
		}
		
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g); //clear screen and repaint using background colour
			this.requestFocus();
			panSize = this.getWidth();
			Graphics2D g2 = (Graphics2D) g;		
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			
			//draw zombies and everything else
			draw(g2);
			
			//Player
			//g2.drawOval(p.x-p.radius, p.y-p.radius, p.radius*2, p.radius*2);
			
			g2.drawImage(p.img, p.x-p.radius, p.y-p.radius, p.radius*2, p.radius*2, null);
			
			//Health
			g2.drawRect((panSize/3)*2, 0, panSize/3, panSize/6);
			g2.drawString("Weapon: "+p.held.name, (panSize/7)*4+panSize/8, panSize/22);
			if (p.held.weaponHeld==Weapon.PISTOL) {
				g2.drawString("Ammo: Infinite", (panSize/7)*4+panSize/8, panSize/22*2);
			} else {
				g2.drawString("Ammo: "+p.held.ammo, (panSize/7)*4+panSize/8, panSize/22*2);
			}
			
			g2.drawString("Health: "+p.health, (panSize/7)*4+panSize/8, panSize/22*3);
		}
	}
	
	class ML implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent e) {}
		@Override
		public void mousePressed(MouseEvent e) {
			p.firing=true;	//Activate shooting
		}
		@Override
		public void mouseReleased(MouseEvent e) {
			//If it is a full auto weapon deactivate shooting when the mouse is released, not when the firing sequence is done
			if (p.held.auto) {
				shoot();
				p.firing=false;
			}
		}
		@Override
		public void mouseEntered(MouseEvent e) {}
		@Override
		public void mouseExited(MouseEvent e) {}
	}
	
	class MML implements MouseMotionListener{
		@Override
		public void mouseDragged(MouseEvent e) {
			mX=e.getX();
			mY=e.getY();
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			mX=e.getX();
			mY=e.getY();
		}
		
	}
	
	class KL implements KeyListener{

		@Override
		public void keyTyped(KeyEvent e) {}

		@Override
		public void keyPressed(KeyEvent e) {
			p.move(e);
			switchWeapons(e);
		}

		@Override
		public void keyReleased(KeyEvent e) {
			p.stopMove(e);
		}
	}
	
	class Time implements ActionListener{
		int frameCount=0;	//Number of frames
		int iFrameCount=0;	//Used for tracking how long the player should be invincible
		int shotCount=0;
		
		public void actionPerformed(ActionEvent e) {
			if (frameCount%spawnRate==0) ghostSpawn();
			pickupSpawn();
			movement();
			checkHits();
			if (p.firing && !p.hasShot) {
				shoot();
				p.hasShot=true;
			}
			
			if (p.iFrames) {
				iFrameCount++;
			}
			if (iFrameCount==20) {
				p.iFrames=false;
				iFrameCount=0;
			}
			
			if (p.firing) {
				shotCount++;
			}
			if (shotCount>=p.held.rate) {
				if (!p.held.auto) p.firing=false;
				p.hasShot=false;
				shotCount=0;
			}
			
			checkDeaths();
			panel.repaint();
			frameCount++;
		}
	}
	
	void movement() {	//move everything
		for (Bullet b:bullets) {
			b.move(p);
		}
		for (Ghost z:ghosts) {
			z.checkClose(p);
		}
		for (Barrier b:barriers) {
			b.move(p);
		}
		for (Pickup pick:pickups) {
			pick.move(p);
		}
	}
	
	void draw(Graphics2D g) {	//Draw everything
		for (Bullet b:bullets) {
			b.paint(g);
		}
		for (Ghost z:ghosts) {
			z.paint(g);
		}
		for (Barrier b:barriers) {
			b.paint(g);
		}
		for (Pickup pick:pickups) {
			pick.paint(g);
		}
	}
	
	void checkHits(){	//Check all collision
		for (Bullet b:bullets) {
			for (Ghost z:ghosts) {
				z.checkHit(b);
			}
		}
		
		for (Ghost z:ghosts) {
			p.checkHit(z);
		}
		
		for (Barrier b:barriers) {
			if (p.checkHit(b)) {	//If the player has moved into a wall, move the player outta there
				if (b.wall) {	//If it's a vertical wall
					for (Bullet a:bullets) {
						a.x+=-p.vx;
					}
					for (Ghost z:ghosts) {
						z.x+=-p.vx;
					}
					for (Barrier c:barriers) {
						c.x+=-p.vx;
					}
					for (Pickup pick:pickups) {
						pick.x-=p.vx;
					}
				} else {	//If it's a horizontal wall
					for (Bullet a:bullets) {
						a.y+=-p.vy;
					}
					for (Ghost z:ghosts) {
						z.y+=-p.vy;
					}
					for (Barrier c:barriers) {
						c.y+=-p.vy;
					}
					for (Pickup pick:pickups) {
						pick.y-=p.vy;
					}
				}
			}
		}

		for (Pickup pick:pickups) {
			if (p.checkHit(pick)) {
				if (pick.knife) {	//If the pickup is the thrown knife
					if (p.held.weaponHeld==Weapon.KNIFE) {	//If the knife is currently being held
						p.held.ammo+=p.held.ammoPick;
						if (p.held.ammo>p.held.ammoMax) p.held.ammo=p.held.ammoMax;
						
					} else {	//If the knife is not being held
						weapons.set(Weapon.KNIFE, new Weapon(Weapon.KNIFE));
						
					}
				} else {	//If it's just a regular pickup
					p.held.ammo+=p.held.ammoPick;
					if (p.held.ammo>p.held.ammoMax) p.held.ammo=p.held.ammoMax;
				}
				pick.picked=true;
			}
		}
	}
	
	void checkDeaths() {	//Check if any objects need to be removed
		//Bullets
		for (int i = 0; i < bullets.size(); i++) {
			Bullet b = bullets.get(i);
			if (b.x<0 || b.x>panSize || b.y<0 || b.y>panSize) {	//Go off screen
				if (b.damage==Weapon.KNIFE_DAMAGE) knifeDrop(b);
				bullets.remove(i);
				i--;
				continue;
			}
			if (b.health<=0) {	//If it has hit a zombie
				if (b.damage==Weapon.KNIFE_DAMAGE) knifeDrop(b);
				bullets.remove(i);
				i--;
				continue;
			}
			if (b.distance>b.maxD){	//If it has exceeded it's range
				bullets.remove(i);
				i--;
				continue;
			}
			for (Barrier bar:barriers) {
				if (b.checkHit(bar)) {
					if (b.damage==Weapon.KNIFE_DAMAGE) knifeDrop(b);
					bullets.remove(i);
					i--;
					continue;
				}
			}
		}
		
		//Zombies
		for (int i=0;i<ghosts.size();i++) {
			Ghost z=ghosts.get(i);
			if (z.health<=0 ) {	//If zombie has 0 or less health
				ghosts.remove(i);
				i--;
				spawnRate--;
			}
		}
		
		//Player
		if (p.health<=0) {
			System.exit(500);
		}
		
		//Pickups
		for (int i=0;i<pickups.size();i++) {
			Pickup pick=pickups.get(i);
			if (pick.picked) {
				pickups.remove(i);
				i--;
			}
		}
	}
	
	void ghostSpawn() {
		boolean bad=true;
		int x=0,y=0;
		
		while (bad) {
			x=(int)(Math.random()*(mapSize)-mapSize/2);	//Create random x an y coords
			y=(int)(Math.random()*(mapSize)-mapSize/2);
			
			int dX=200-x,dY=200-y;
			
			//Use pythagorean formula to find the distance between the ghost and player
			double length=Math.sqrt(dX*dX+dY*dY);
			
			if (length<300) {	//If the ghost is spawning too close to the player, make new x and y
				continue;
			} else {
				break;
			}
		}
		ghosts.add(new Ghost(x,y));
	}
	
	void pickupSpawn() {
		boolean start=false;
		int x=0,y=0;
		int rando=(int)(Math.random()*500);
		if (rando==0) start=true;
		
		while (start) {
			x=(int)(Math.random()*(mapSize)-mapSize/2);	//Create random x an y coords
			y=(int)(Math.random()*(mapSize)-mapSize/2);
			
			int dX=200-x,dY=200-y;
			
			//Use pythagorean formula to find the distance between the pickup and player
			double length=Math.sqrt(dX*dX+dY*dY);
			
			if (length<300) {	//If the pickup is spawning too close to the player, make new x and y
				continue;
			} else {
				pickups.add(new Pickup(x,y));
				break;
			}
		}
	}
	
	void shoot() {
		if (p.held.ammo>0 || p.held.weaponHeld==Weapon.PISTOL) {
			//Shoot a bullet
			bullets.add(new Bullet(p,mX,mY));
			
			if (p.held.weaponHeld==Weapon.SHOTGUN) {
				Point puh=Bullet.getShotgun(mX, mY,true);
				bullets.add(new Bullet(p,puh.x,puh.y));
				
				puh=Bullet.getShotgun(mX, mY,false);
				bullets.add(new Bullet(p,puh.x,puh.y));
			}
			
			p.held.ammo--;
		}
	}
	
	void knifeDrop(Bullet b) {
		pickups.add(new KnifePick((int)b.x,(int)b.y));
	}
	
	void addBarriers() {
		//Edge of screen barriers
		barriers.add(new Barrier(panSize-mapSize,panSize-mapSize,mapSize,true));	//Left wall
		barriers.add(new Barrier(panSize-mapSize,panSize-mapSize,mapSize,false));	//Up wall
		barriers.add(new Barrier(mapSize-panSize,panSize-mapSize,mapSize,true));	//Right wall
		barriers.add(new Barrier(panSize-mapSize,mapSize-panSize,mapSize,false));	//Bottom wall
		
		//There must be little book-end walls on the end of long walls
		barriers.add(new Barrier(100,100,150,true));
		barriers.add(new Barrier(100,240,10,false));	//Book-end
		barriers.add(new Barrier(100,100,150,false));
		barriers.add(new Barrier(240,100,10,true));	//Book-End
	}
	
	void addWeapons() {
		weapons.add(new Weapon(Weapon.PISTOL));
		weapons.add(new Weapon(Weapon.EAGLE));
		weapons.add(new Weapon(Weapon.SHOTGUN));
		weapons.add(new Weapon(Weapon.UZI));
		weapons.add(new Weapon(Weapon.SWORD));
		weapons.add(new Weapon(Weapon.KNIFE));
	}
	
	void switchWeapons(KeyEvent e) {
		if (e.getKeyCode()==KeyEvent.VK_1) p.held=weapons.get(0);
		if (e.getKeyCode()==KeyEvent.VK_2) p.held=weapons.get(1);
		if (e.getKeyCode()==KeyEvent.VK_3) p.held=weapons.get(2);
		if (e.getKeyCode()==KeyEvent.VK_4) p.held=weapons.get(3);
		if (e.getKeyCode()==KeyEvent.VK_5) p.held=weapons.get(4);
		if (e.getKeyCode()==KeyEvent.VK_6) p.held=weapons.get(5);
	}
	
	void loadImages() {
		
	}
	
	
	
	public static void main(String[] args) {
		new GUI();
	} 
}