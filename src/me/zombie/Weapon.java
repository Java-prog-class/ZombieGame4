package me.zombie;

public class Weapon {
	
	int range=99999;
	int speed;
	int damage;
	int rate;
	int ammo=0;
	int ammoMax;
	int ammoPick=0;
	int radius=0;
	int weaponHeld = 0;
	int health=1;
	boolean melee=false;
	boolean auto=false;
	String name="";
	
	static final int PISTOL  = 0;
	static final int EAGLE   = 1;
	static final int SHOTGUN = 2;
	static final int UZI     = 3;
	static final int SWORD   = 4;
	static final int KNIFE   = 5;
	static final int KNIFE_DAMAGE=666;
	
	Weapon(int weapon) {
		weaponHeld=weapon;
		if(weaponHeld == PISTOL) {
			speed = 5;		
			damage = 1;
			rate = 5;
			radius=5;
			name="Pistol";
		}
		if(weaponHeld == EAGLE) {
			speed = 12;
			damage = 3;
			rate = 20;
			radius=7;
			ammoMax=60;
			ammoPick=20;
			name="D. Eagle";
		}
		if(weaponHeld == SHOTGUN) {
			range = 100;
			speed = 4;
			damage = 5;
			rate = 32;
			radius=10;
			ammoMax=24;
			ammoPick=6;
			name="Shotgun";
			health=3;
		}
		if(weaponHeld == UZI) {
			speed = 16;
			damage = 1;
			rate = 5;
			radius=3;
			auto=true;
			ammoMax=120;
			ammoPick=40;
			name="Uzi";
		}
		if(weaponHeld == SWORD) {
			range = 80;	//AKA how long the sword lasts for
			speed = 16; //AKA displacement from the player
			radius = 15;
			damage = 8;
			rate = 18;
			ammoMax=100;
			ammoPick=20;
			melee=true;
			auto=true;
			health=5;
			name="Sword";
		}
		if (weaponHeld==KNIFE) {
			ammo=1;
			speed=6;
			radius=8;
			damage=KNIFE_DAMAGE;
			rate=1;
			ammoMax=1;
			ammoPick=1;
			health=1;
			name="Knife";
		}
	}
}