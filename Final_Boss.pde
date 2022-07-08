class Final_Boss extends Enemy {
  int swarmCooldown = 20;
  int healCooldown = 15;
  int burstCooldown = 10;
  int shootCooldown = 5;
  int dirChangeCooldown = 400;
  int aimX = 0, aimY = 0;
  int pastTime = 0;
  PImage img;
  Final_Boss() {
    super(new PVector(width/2, height/2), 2500, 75, -1, 50, 10, 6, 0, 2500);
    img = loadImage("enemy.png");
  }
  void Update() {
    fill(#00FF00);
    rectMode(CENTER);
    rect(width/2, 50, player.finalBoss.health * 0.9, 50);
    fill(0);
    textSize(30);
    text(player.finalBoss.health, (width/2), 50);
    pastTime += time - lastTime;
    if(pastTime >= 1000) {
      pastTime -= 1000;
      if(swarmCooldown > 0) {
        swarmCooldown--;
      }
      if(healCooldown > 0) {
        healCooldown--;
      }
      if(burstCooldown > 0) {
        burstCooldown--;
      }
    }
    move();
    dirChangeCooldown--;
    if(random(300) > dirChangeCooldown) {
      dirChangeCooldown = 400;
      targetX = (int) random(width);
      targetY = (int) random(height);
    }
    shootCooldown--;
    if(shootCooldown <= 0) {
      enemyBullets.add(new Bullet(pos, new PVector(aimX, aimY).sub(pos), bulletSpeed, attackDamage));
      shootCooldown = 5;
    }
    if(random(200) > random(800) && swarmCooldown <= 0) {
      for(int i  = 0; i < 25; i++) {
        player.enemies.add(new Enemy(new PVector(random(width), random(height)), 35, 20, 1, 10, 100, 4, 10, 50));
        player.numEnemies++;
      }
      swarmCooldown = 20;
    }
    if(healCooldown <= 0 && health <= 2500) {
      health += 250;
      healCooldown = 15;
    }
    if(burstCooldown <= 0) {
      for(int i = 0; i < 50; i++) {
        enemyBullets.add(new Bullet(pos, new PVector((int) random(width), (int) random(height)).sub(pos), bulletSpeed, attackDamage));
      }
      burstCooldown = 10;
    }
    if(aimX == width && aimY != height) {
      aimY+=5;
    }
    if(aimX == 0 && aimY != 0) {
      aimY-=5;
    }
    if(aimY == height && aimX != 0) {
      aimX-=5;
    }
    if(aimY == 0 && aimX != width){
      aimX+=5;
    }
    imageMode(CENTER);
    image(img, pos.x, pos.y, diameter, diameter);
  }
  void move() {
    PVector dir = PVector.sub(new PVector(targetX, targetY), pos);
    dir.normalize();
    dir.mult(2);
    pos.add(dir);
  }
  void die() {
    playing = false;
    endOfRound = true;
  }
}