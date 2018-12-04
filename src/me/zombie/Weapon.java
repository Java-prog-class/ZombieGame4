package me.zombie;

public class Weapon {
	
	int range;
	int speed;
	int damage;
	int rate;
	int travelled;
	int ammo;
	int dura;
	int rerate;
	int reloaded = 0;
	int weaponHeld = 0;
	
	static final int FIST    = 0;
	static final int PISTOL  = 1;
	static final int EAGLE   = 2;
	static final int SHOTGUN = 3;
	static final int UZI     = 4;
	static final int SWORD   = 5;
	static final int KNIFE   = 6;
	static final int AXE     = 7;
	
	Weapon() {
		if(weaponHeld == FIST) {
			range = 10;
			ammo = 900;
			speed = 5;
			damage = 15;
			rate = 20;
		}
		if(weaponHeld == PISTOL) {
			range = 350;
			ammo = 20;
			speed = 40;		
			damage = 35;
			rate = 20;
		}
		if(weaponHeld == EAGLE) {
			range = 400;
			ammo = 8;
			speed = 50;
			damage = 80;
			rate = 30;
		}
		if(weaponHeld == SHOTGUN) {
			range = 50;
			ammo = 8;
			speed = 30;
			damage = 8;
			rate = 45;
		}
		if(weaponHeld == UZI) {
			range = 300;
			ammo = 50;
			speed = 40;
			damage = 15;
			rate = 5;
		}
		if(weaponHeld == SWORD) {
			range = 30;
			dura = 100;
			speed = 5;
			damage = 45;
			rate = 40;
		}
		if(weaponHeld == KNIFE) {
			range = 15;
			dura = 100;
			speed = 10;
			damage = 35;
			rate = 15;
		}
		if(weaponHeld == AXE) {
			range = 25;
			dura = 100;
			speed = 3;
			damage = 85;
			rate = 45;
		}
	}
	
	void fire() {
		//special case for fist
		if(weaponHeld == FIST) ammo = 900;
		
		travelled += speed;
		if(travelled >= range) {
			/**despawn bullet**/
		}
		if(ammo != 0) {
			if(weaponHeld == PISTOL) {
				travelled += speed;
				if(travelled >= range) {
					/**despawn bullet**/
				}
				ammo --;
			}

			if(weaponHeld == EAGLE) {
				travelled += speed;
				if(travelled >= range) {
					/**despawn bullet**/
				}
				ammo --;
			}

			if(weaponHeld == SHOTGUN) {
				travelled += speed;
				if(travelled >= range) {
					/**despawn bullet**/
				}
			}

			if(weaponHeld == UZI) {
				travelled += speed;
				if(travelled >= range) {
					/**despawn bullet**/
				}
				ammo --;
			}

			if(weaponHeld == SWORD) {
				travelled += speed;
				if(travelled >= range) {
					/**despawn bullet**/
				}
				ammo --;
			}

			if(weaponHeld == KNIFE) {
				travelled += speed;
				if(travelled >= range) {
					/**despawn bullet**/
				}
				ammo --;
			}

			if(weaponHeld == AXE) {
				travelled += speed;
				if(travelled >= range) {
					/**despawn bullet**/
				}
				ammo --;
			}
		}
		
	}
	
	void Reload() {
		if(ammo == 0) {
			if(weaponHeld == PISTOL) {
				rerate = 90;
				reloaded ++;
				if(reloaded == rerate) {
					ammo = 20;
					reloaded = 0;
				}
			}

			if(weaponHeld == EAGLE) {
				rerate = 110;
				reloaded ++;
				if(reloaded == rerate) {
					ammo = 8;
					reloaded = 0;
				}
			}

			if(weaponHeld == SHOTGUN) {
				rerate = 180;
				reloaded ++;
				if(reloaded == rerate) {
					ammo = 8;
					reloaded = 0;
				}
			}

			if(weaponHeld == UZI) {
				rerate = 120;
				reloaded ++;
				if(reloaded == rerate) {
					ammo = 50;
					reloaded = 0;
				}
			}
		}
	}
	
	void Drop() {
		
	}
}



