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
	Zambo z=new Zambo(100,100);
	
	GUI(){
		panel.addKeyListener(new KL());
		panel.addMouseListener(new ML());
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
			g2.drawOval(p.x-p.radius, p.y-p.radius, p.radius*2, p.radius*2);
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
		int frameCount=0;
		
		public void actionPerformed(ActionEvent e) {
			if (frameCount%spawnRate==0) zombieSpawn();
			movement();
			checkHits();
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
	}
	
	void draw(Graphics2D g) {	//Draw everything
		for (Bullet b:bullets) {
			b.paint(g);
		}
		for (Zambo z:zambies) {
			z.paint(g);
		}
	}
	
	void checkHits(){	//Check all collision
		for (Bullet b:bullets) {
			for (Zambo z:zambies) {
				z.checkHit(b);
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