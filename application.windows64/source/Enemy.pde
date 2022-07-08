class Enemy {
  static final int PASSIVE = 0;
  static final int AGRESSIVE = 1;
  float health;
  float diameter;
  PVector pos;
  int aiMode, attackDamage, targetX, targetY, cooldown, bulletSpeed, remainingCooldown, dirChangeCD, bulletSpread, coinValue;
  boolean moving;
  PImage img1, img2, img3, img4, img5;
  int enemyR;
  
  Enemy(PVector p, float h, float d, int m, int ad, int c, int bs, int bSpread, int cv) {
    pos = p;
    health = h;
    diameter = d;
    aiMode = m;
    attackDamage = ad;
    cooldown = c;
    bulletSpeed  = bs;
    bulletSpread = bSpread;
    coinValue = cv;
    targetX = (int) random(width);
    targetY = (int) random(height);
    remainingCooldown = 0;
    dirChangeCD = 400;
    moving = true;
    img1 = loadImage("enemy1.png");
    img2 = loadImage("enemy2.png");
    img3 = loadImage("enemy3.png");
    img4 = loadImage("enemy4.png");
    img5 = loadImage("enemy5.png");
    enemyR = (int) random(5);
  }
  
  void Update() {
    // Check for which ai mode the enemy is using, and update accordingly
    if(aiMode == Enemy.PASSIVE) {
      if(remainingCooldown > 0) {
        remainingCooldown--;
      }
      // Check if enemy is within 200 pixels, if so, stop moving and attack them
      if((pow(pos.x - player.pos.x, 2) + pow(pos.y - player.pos.y, 2)) < pow(200, 2)) {
        targetX = (int) player.pos.x;
        targetY = (int) player.pos.y;
        moving = false;
        if(remainingCooldown <= 0) {
          shoot();
        }
      }
      else {
        // Otherwise move randomly
        move();
        moving = true;
        dirChangeCD--;
        if(random(300) > dirChangeCD) {
          targetX = (int) random(width);
          targetY = (int) random(height);
          dirChangeCD = 400;
        }
      }
    }
    else if(aiMode == AGRESSIVE) {
      if(remainingCooldown > 0) {
        remainingCooldown--;
      }
      // Check if enemy is within 400 pixels, attack and chase them
      if((pow(pos.x - player.pos.x, 2) + pow(pos.y - player.pos.y, 2)) < pow(400, 2)) {
        targetX = (int) player.pos.x;
        targetY = (int) player.pos.y;
        move();
        moving = true;
        if(remainingCooldown <= 0) {
          shoot();
        }
      }
      else {
        // Otherwise move randomly
        move();
        moving = true;
        dirChangeCD--;
        if(random(300) > dirChangeCD) {
          targetX = (int) random(width);
          targetY = (int) random(height);
          dirChangeCD = 400;
        }
      }
    }
    //ellipseMode(CENTER);
    //fill(#FF0000);
    //ellipse(pos.x, pos.y, diameter, diameter);
    imageMode(CENTER);
    pushMatrix();
    PVector dir = PVector.sub(new PVector(targetX, targetY), pos);
    translate(pos.x, pos.y);
    rotate(radians(360) - atan2(dir.x, dir.y));
    
    if (enemyR == 0){
      image(img1, 0, 0, diameter, diameter);
    }
    else if (enemyR == 1){
      image(img2, 0, 0, diameter, diameter);
    }
    else if (enemyR == 2){
      image(img3, 0, 0, diameter, diameter);
    }
    else if (enemyR == 3){
      image(img4, 0, 0, diameter, diameter);
    }
    else if (enemyR == 4){
      image(img5, 0, 0, diameter, diameter);
    }
    popMatrix();
  }
  
  void move() {
    PVector dir = PVector.sub(new PVector(targetX, targetY), pos);
    dir.normalize();
    pos.add(dir);
  }
  
  void shoot() {
    remainingCooldown = cooldown;
    enemyBullets.add(new Bullet(pos, new PVector(targetX - bulletSpread/2 + random(bulletSpread), targetY - bulletSpread/2 + random(bulletSpread)).sub(pos), bulletSpeed, attackDamage));
  }
}
