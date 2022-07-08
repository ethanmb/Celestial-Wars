class Boss_1 extends Enemy {
  int shootExtender = 0;
  PImage img;
  Boss_1() {
    super(new PVector(width/2, height/2), 1000, 250, 0, 100, 250, 4, 10, 1000);
    img = loadImage("boss1.png");
  }
  void update() {
    move();
    dirChangeCD--;
    if (random(300) > dirChangeCD) {
      targetX = (int) random(width);
      targetY = (int) random(height);
      dirChangeCD = 400;
    }
    if (remainingCooldown > 0) {
      remainingCooldown--;
    } else {
      remainingCooldown = cooldown;
    }
    if (remainingCooldown <= 125) {
      shoot();
    }
    imageMode(CENTER);
    image(img, pos.x, pos.y, diameter, diameter);
  }
  void shoot() {
    enemyBullets.add(new Bullet(pos, new PVector(width/2, 0).sub(pos), bulletSpeed, attackDamage));
    enemyBullets.add(new Bullet(pos, new PVector(0, 0).sub(pos), bulletSpeed, attackDamage));
    enemyBullets.add(new Bullet(pos, new PVector(width, 0).sub(pos), bulletSpeed, attackDamage));
    enemyBullets.add(new Bullet(pos, new PVector(width, height/2).sub(pos), bulletSpeed, attackDamage));
    enemyBullets.add(new Bullet(pos, new PVector(width, height).sub(pos), bulletSpeed, attackDamage));
    enemyBullets.add(new Bullet(pos, new PVector(width/2, height).sub(pos), bulletSpeed, attackDamage));
    enemyBullets.add(new Bullet(pos, new PVector(0, height).sub(pos), bulletSpeed, attackDamage));
    enemyBullets.add(new Bullet(pos, new PVector(0, height/2).sub(pos), bulletSpeed, attackDamage));
  }

  void move() {
    PVector dir = PVector.sub(new PVector(targetX, targetY), pos);
    dir.normalize();  
  }

  void die() {
    boss1 = false;
    bossOut = false;
    numEnemiesLeft = 40;
    level = 2;
  }
}