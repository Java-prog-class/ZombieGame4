package me.zombie;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class GUI extends JFrame {
	
	static int panSize=400;	//Size of Screen
	
	final int mapSize=800;	//Size of map
	
	int spawnRate=90;	//Ghost spawn rate
	int mX=0,mY=0;	//Mouse X and Y
	
	Timer t=new Timer(20,new Time());
	DrawingPanel panel=new DrawingPanel();
	Player p=new Player();
	
	ArrayList<Bullet> bullets=new ArrayList<Bullet>();
	ArrayList<Ghost> ghosts=new ArrayList<Ghost>();
	ArrayList<Barrier> barriers=new ArrayList<Barrier>();
	ArrayList<Weapon> weapons=new ArrayList<Weapon>();
	
	GUI(){
		panel.addKeyListener(new KL());
		panel.addMouseListener(new ML());
		panel.addMouseMotionListener(new MML());
		addWeapons();
		addBarriers();
		
		this.add(panel);	
		this.setTitle("Main graphics ..."); 
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.pack();
		this.setVisible(true);
		t.start();
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
			draw(g2);
			
			//Player
			g2.drawOval(p.x-p.radius, p.y-p.radius, p.radius*2, p.radius*2);
			
			//Health/stats
			g2.drawRect((panSize/3)*2, 0, panSize/3, panSize/6);
			g2.drawString("Weapon: "+p.held.name, (panSize/7)*4+panSize/8, panSize/16);
			g2.drawString("Health: "+p.health, (panSize/7)*4+panSize/8, panSize/8);
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
			if (frameCount%spawnRate==0) zombieSpawn();
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
				}
			}
		}
	}
	
	void checkDeaths() {	//Check if any objects need to be removed
		//Bullets
		for (int i = 0; i < bullets.size(); i++) {
			Bullet b = bullets.get(i);
			if (b.x<0 || b.x>panSize || b.y<0 || b.y>panSize) {	//Go off screen
				bullets.remove(i);
				i--;
				continue;
			}
			if (b.hasHit) {	//If it has hit a zombie
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
					bullets.remove(i);
					i--;
					break;
				}
			}
		}
		
		//Zombies
		for (int i=0;i<ghosts.size();i++) {
			Ghost z=ghosts.get(i);
			if (z.health<=0) {	//If zombie has 0 or less health
				ghosts.remove(i);
				i--;
				spawnRate--;
			}
		}
		
		//Player
		if (p.health<=0) {
			System.exit(500);
		}
	}
	
	void zombieSpawn() {
		boolean bad=true;
		int x=0,y=0;
		
		while (bad) {
			x=(int)(Math.random()*(mapSize)-mapSize/2);	//Create random x an y coords
			y=(int)(Math.random()*(mapSize)-mapSize/2);
			
			int dX=200-x,dY=200-y;
			
			//Use pythagorean formula to find the distance between the zombie and player
			double length=Math.sqrt(dX*dX+dY*dY);
			
			if (length<300) {	//If the zombie is spawning too close to the player, make new x and y
				continue;
			} else {
				break;
			}
		}
		ghosts.add(new Ghost(x,y));
	}
	
	void shoot() {
		//Shoot a bullet
		bullets.add(new Bullet(p,mX,mY));
		
		if (p.held.weaponHeld==Weapon.SHOTGUN) {
			Point puh=Bullet.getShotgun(mX, mY,true);
			bullets.add(new Bullet(p,puh.x,puh.y));
			
			puh=Bullet.getShotgun(mX, mY,false);
			bullets.add(new Bullet(p,puh.x,puh.y));
		}
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
		weapons.add(new Weapon(0));
		weapons.add(new Weapon(1));
		weapons.add(new Weapon(2));
		weapons.add(new Weapon(3));
		weapons.add(new Weapon(4));
		weapons.add(new Weapon(5));
		weapons.add(new Weapon(6));
		weapons.add(new Weapon(7));
	}
	
	void switchWeapons(KeyEvent e) {
		if (e.getKeyCode()==KeyEvent.VK_1) p.held=weapons.get(0);
		if (e.getKeyCode()==KeyEvent.VK_2) p.held=weapons.get(1);
		if (e.getKeyCode()==KeyEvent.VK_3) p.held=weapons.get(2);
		if (e.getKeyCode()==KeyEvent.VK_4) p.held=weapons.get(3);
		if (e.getKeyCode()==KeyEvent.VK_5) p.held=weapons.get(4);
		if (e.getKeyCode()==KeyEvent.VK_6) p.held=weapons.get(5);
		if (e.getKeyCode()==KeyEvent.VK_7) p.held=weapons.get(6);
		if (e.getKeyCode()==KeyEvent.VK_8) p.held=weapons.get(7);
	}
	
	public static void main(String[] args) {
		new GUI();
	} 
}