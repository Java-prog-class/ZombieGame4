package me.zombie;

public class Weapon {
	
	int range=99999;
	int speed;
	int damage;
	int rate;
	int ammo=0;
	int ammoMax;
	int radius=0;
	int weaponHeld = 0;
	boolean melee=false;
	boolean auto=false;
	String name="";
	
	static final int FIST    = 0;
	static final int PISTOL  = 1;
	static final int EAGLE   = 2;
	static final int SHOTGUN = 3;
	static final int UZI     = 4;
	static final int SWORD   = 5;
	static final int KNIFE   = 6;
	static final int AXE     = 7;
	
	Weapon(int weapon) {
		weaponHeld=weapon;
		
		if(weaponHeld == FIST) {
			range = 10;
			damage = 1;
			rate = 20;
			melee=true;
			name="Fist";
		}
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
			name="D. Eagle";
		}
		if(weaponHeld == SHOTGUN) {
			range = 100;
			speed = 4;
			damage = 5;
			rate = 32;
			radius=10;
			name="Shotgun";
		}
		if(weaponHeld == UZI) {
			speed = 16;
			damage = 1;
			rate = 5;
			radius=3;
			auto=true;
			name="Uzi";
		}
		if(weaponHeld == SWORD) {
			range = 30;
			ammo = 100;
			damage = 8;
			rate = 40;
			melee=true;
		}
		if(weaponHeld == KNIFE) {
			range = 15;
			ammo = 100;
			damage = 3;
			rate = 15;
			melee=true;
		}
		if(weaponHeld == AXE) {
			range = 25;
			ammo = 100;
			damage = 5;
			rate = 45;
			melee=true;
		}
	}
}