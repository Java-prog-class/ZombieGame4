package me.zombie;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class GUI extends JFrame {
	
	static int panSize=400;
	
	int spawnRate=90;
	
	Timer t=new Timer(20,new Time());
	DrawingPanel panel=new DrawingPanel();
	Player p=new Player();
	
	ArrayList<Bullet> bullets=new ArrayList<Bullet>();
	ArrayList<Zambo> zambies=new ArrayList<Zambo>();
	ArrayList<Barrier> barriers=new ArrayList<Barrier>();
	Zambo z=new Zambo(100,100);
	
	GUI(){
		panel.addKeyListener(new KL());
		panel.addMouseListener(new ML());
		
		//There must be little book-end walls on the end of long walls
		barriers.add(new Barrier(100,100,150,true));
		barriers.add(new Barrier(100,240,10,false));	//Book-end
		barriers.add(new Barrier(100,100,150,false));
		barriers.add(new Barrier(240,100,10,true));	//Book-End
		
		zambies.add(z);
		
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
			
			//Health
			g2.drawRect((panSize/3)*2, 0, panSize/3, panSize/6);
			g2.drawString("Health: "+p.health, (panSize/3)*2+panSize/8, panSize/8);
		}
	}
	
	class ML implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent e) {}
		@Override
		public void mousePressed(MouseEvent e) {
			//Shoot a bullet
			bullets.add(new Bullet(200,200,e.getX(),e.getY()));
		}
		@Override
		public void mouseReleased(MouseEvent e) {}
		@Override
		public void mouseEntered(MouseEvent e) {}
		@Override
		public void mouseExited(MouseEvent e) {}
	}
	
	class KL implements KeyListener{

		@Override
		public void keyTyped(KeyEvent e) {}

		@Override
		public void keyPressed(KeyEvent e) {
			p.move(e);
		}

		@Override
		public void keyReleased(KeyEvent e) {
			p.stopMove(e);
		}
	}
	
	class Time implements ActionListener{
		int frameCount=0;	//Number of frames
		int iFrameCount=0;	//Used for tracking how long the player should be invincible
		
		public void actionPerformed(ActionEvent e) {
			if (frameCount%spawnRate==0) zombieSpawn();
			movement();
			checkHits();
			
			if (p.iFrames) {
				iFrameCount++;
			}
			if (iFrameCount==20) {
				p.iFrames=false;
				iFrameCount=0;
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
		for (Zambo z:zambies) {
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
		for (Zambo z:zambies) {
			z.paint(g);
		}
		for (Barrier b:barriers) {
			b.paint(g);
		}
	}
	
	void checkHits(){	//Check all collision
		for (Bullet b:bullets) {
			for (Zambo z:zambies) {
				z.checkHit(b);
			}
		}
		
		for (Zambo z:zambies) {
			p.checkHit(z);
		}
		
		for (Barrier b:barriers) {
			if (p.checkHit(b)) {	//If the player has moved into a wall, move the player outta there
				if (b.wall) {	//If it's a vertical wall
					for (Bullet a:bullets) {
						a.x+=-p.vx;
					}
					for (Zambo z:zambies) {
						z.x+=-p.vx;
					}
					for (Barrier c:barriers) {
						c.x+=-p.vx;
					}
				} else {	//If it's a horizontal wall
					for (Bullet a:bullets) {
						a.y+=-p.vy;
					}
					for (Zambo z:zambies) {
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
			}
		}
		
		//Zombies
		for (int i=0;i<zambies.size();i++) {
			Zambo z=zambies.get(i);
			if (z.health<=0) {	//If zombie has 0 or less health
				zambies.remove(i);
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
			x=(int)(Math.random()*(panSize*2)-panSize);	//Create random x an y coords
			y=(int)(Math.random()*(panSize*2)-panSize);
			
			int dX=200-x,dY=200-y;
			
			//Use pythagorean formula to find the distance between the zombie and player
			double length=Math.sqrt(dX*dX+dY*dY);
			
			if (length<300) {	//If the zombie is spawning too close to the player, make new x and y
				continue;
			} else {
				break;
			}
		}
		zambies.add(new Zambo(x,y));
	}
	
	public static void main(String[] args) {
		new GUI();
	} 
}