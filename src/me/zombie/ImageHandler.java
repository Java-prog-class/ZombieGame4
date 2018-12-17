package me.zombie;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

import javax.imageio.ImageIO;

class ImageHandler {

	//list of all images here	
	private static String[] imageNames = {
		"Player/PlayUp",
		"Player/PlayLeft",
		"Player/PlayDown",
		"Player/PlayRight",
		"Player/DeadLeft",
		"Player/DeadUp",
		"Player/DeadRight",
		"Player/DeadDown",
		"Player/PunchUp",
		"Player/PunchLeft",
		"Player/PunchDown",
		"Player/PunchRight",
		"Weapon/PistolUD",
		"Weapon/PistolLR",
		"Weapon/EagleUD",
		"Weapon/EagleLR",
		"Weapon/ShotgunUp",
		"Weapon/ShotgunDown",
		"Weapon/ShotgunRight",
		"Weapon/ShotgunLeft",
		"Weapon/SwordUp",
		"Weapon/SwordDown",
		"Weapon/SwordRight",
		"Weapon/SwordLeft",
		"Weapon/AxeUp",
		"Weapon/AxeDown",
		"Weapon/AxeRight",
		"Weapon/AxeLeft",
		"Weapon/KnifeUp",
		"Weapon/KnifeDown",
		"Weapon/KnifeRight",
		"Weapon/KnifeLeft",
		"Zombie/ZombieUp",
		"Zombie/ZombieDown",
		"Zombie/ZombieRight",
		"Zombie/ZombieLeft"
	};
	
	private static HashMap<String, BufferedImage>imagelist = new HashMap<String, BufferedImage>();
	
	static void loadAllImages() {
		BufferedImage img;
		
		for(String s : imageNames) {
			img = loadOneImage(s);
			int pos = s.lastIndexOf('/');
			String shortName = s.substring(pos+1);
			System.out.println(shortName);
			imagelist.put(shortName, img);			
		}
		
		System.out.println(Arrays.asList(imagelist));
		
		
	}
	
	
	private static BufferedImage loadOneImage(String imagename) {
		BufferedImage img = null;
		try {
//			img = ImageIO.read(new File ("res/Player/PlayUp.png"));
			img = ImageIO.read(new File ("res/" + imagename + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
		return img;
	}
	
	static BufferedImage getImage(String imagename) {
		//System.out.println(imagename);
		BufferedImage img = imagelist.get(imagename);
		
		//if (img == null) System.out.println("null");
		//else System.out.println("W=" +img.getWidth());
		
		return imagelist.get(imagename);
		
	}
}
