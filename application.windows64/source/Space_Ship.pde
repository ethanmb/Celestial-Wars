class Space_Ship {
  float x, y, topspeed, boost;
  PVector pos, velocity, acceleration;
  boolean shooting, powerUpPresent = false, powerUpActive = false;
  char powerUpType = 'x';
  ArrayList<Bullet> bullets;
  ArrayList enemies = new ArrayList();
  int numBullets;
  int delay;
  int targetPosX = 200;
  int targetPosY = 200;
  int numEnemies = 0;
  int bulletSpeed;
  int diameter = 16;
  int powerUpTimeLeft;
  float health = 100;
  int attackDelay = 20;
  int bulletDamage = 10;
  int maxHealth = 100;
  PImage img;
  float speedHolder = 4;
  int score = 0;
  int regen = 0, regenTime = 0;
  int defense;
  int bulletSpread;
  int numToShoot;
  Boss_1 boss1Object = new Boss_1();
  Final_Boss finalBoss = new Final_Boss();
  PowerUps powerUp;
  int specialLevel = 0;
  int specialCooldown = 12;
  boolean finalBossOut = false;

  Space_Ship() {
    pos = new PVector(0, 0);
    velocity = new PVector(0, 0);
    topspeed = 4;
    boost = 100;
    bullets = new ArrayList<Bullet>();
    numBullets = 0;
    img = loadImage("player.png");
    imageMode(CENTER);
    bulletSpeed = 5;
    bulletSpread = 20;
    numToShoot = 1;
  }

  void render() {
    if (playing) {
      PVector mouse = new PVector(mouseX, mouseY);
      PVector dir = PVector.sub(mouse, pos);
      dir.normalize();
      dir.mult(0.5); 
      acceleration = dir;
      if (keyPressed && key == ' ' || keyPressed && keyCode == CONTROL) {
        velocity.add(acceleration);
        velocity.limit(topspeed);
        pos.add(velocity);
      }
      regenTime += time - lastTime;
      if(regenTime >= 1000) {
        regenTime -= 1000;
        if(specialCooldown > 0) {
          specialCooldown--;
        }
        if(boost <= 100) {
          boost += 5;
          if(boost > 100) {
            boost = 100;
          }
        }
        if(health < maxHealth) {
          health += regen;
          if(health > maxHealth) {
            health = maxHealth;
          }
        }
      }
    } else {
      if (abs(pos.x - 200) < 10 && abs(pos.y - 200) < 10) {
        targetPosX = 400;
        targetPosY = 400;
        if (numEnemies == 0) {
          enemies.add(new Enemy(new PVector(400, 400), 40, 20, -1, -1, -1, -1, -1, 0));
          numEnemies++;
        }
        shooting = true;
      } else if (abs(pos.x - 400) < 10 && abs(pos.y - 400) < 10) {
        targetPosX = 800;
        targetPosY = 400;
        shooting = false;
        if (powerUpPresent == false) {
          powerUp = new PowerUps(800, 400, 'F');
          powerUpPresent = true;
        }
      } else if (abs(pos.x - 800) < 10 && abs(pos.y - 400) < 10) {
        targetPosX = 600;
        targetPosY = 100;
        if (numEnemies == 0) {
          enemies.add(new Enemy(new PVector(600, 100), 40, 20, -1, -1, -1, -1, -1, 0));
          numEnemies++;
        }
        shooting = true;
      } else if (abs(pos.x - 600) < 10 && abs(pos.y - 100) < 10) {
        targetPosX = 200;
        targetPosY = 200;
        shooting = false;
      }
      PVector mouse = new PVector(targetPosX, targetPosY);
      PVector dir = PVector.sub(mouse, pos);
      dir.normalize();
      dir.mult(0.5); 
      acceleration = dir;

      velocity.add(acceleration);
      velocity.limit(topspeed);
      pos.add(velocity);
    }
  }

  void drawPlayer() {
    if (playing && !powerUpPresent && random(2000) < 1 && !powerUpActive) {
      powerUp = new PowerUps((int)random(width), (int)random(height), 'F');
      powerUpPresent = true;
    }
    if (powerUpPresent) {//here
      if (pow(diameter / 2 + powerUp.diameter / 2, 2) > pow(pos.x - powerUp.x, 2) + pow(pos.y - powerUp.y, 2)) {

        if (powerUp.type == 'F') {
          powerUpTimeLeft = 120;
          bulletSpeed += 3;
          powerUpPresent = false;
          powerUpActive = true;
          powerUpType = 'F';
        }
      }
      powerUp.draw();
    }
    if (powerUpActive) {
      powerUpTimeLeft--;
      if (powerUpTimeLeft <= 0) {
        powerUpActive = false;
        if (powerUpType == 'F') {
          powerUpType = 'x';
          bulletSpeed -= 3;
        }
      }
    }
    for (int i = 0; i < numEnemies; i++) {
      Enemy e = (Enemy)enemies.get(i);
      for (int j = 0; j < numBullets; j++) {
        Bullet b = bullets.get(j);
        if (pow(b.diameter / 2 + e.diameter / 2, 2) > pow(e.pos.x - b.X, 2) + pow(e.pos.y - b.Y, 2)) {
          e.health -= b.damage;
          bullets.remove(b);
          j--;
          player.numBullets--;
        }
      }
      if (e.health <= 0) {
        Enemy temp = (Enemy) enemies.get(i);
        giveCoins(temp.coinValue);
        enemies.remove(temp);
        numEnemies--;
        if (playing)
          score ++;
          numEnemiesLeft--;
        }
      e.Update();
    }
    if (boss1) {//boss logic
      for (int j = 0; j < numBullets; j++) {
        Bullet b = bullets.get(j);
        if (pow(b.diameter / 2 + boss1Object.diameter / 2, 2) > pow(boss1Object.pos.x - b.X, 2) + pow(boss1Object.pos.y - b.Y, 2)) {
          boss1Object.health -= b.damage;
          bullets.remove(b);
          j--;
          player.numBullets--;
          if (boss1Object.health <= 0) {
            boss1Object.die();
          }
        }
      }
      boss1Object.update();
    }
    if(finalBossOut) {
      for (int j = 0; j < numBullets; j++) {
        Bullet b = bullets.get(j);
        if (pow(b.diameter / 2 + finalBoss.diameter / 2, 2) > pow(finalBoss.pos.x - b.X, 2) + pow(finalBoss.pos.y - b.Y, 2)) {
          finalBoss.health -= b.damage;
          bullets.remove(b);
          j--;
          player.numBullets--;
          if (finalBoss.health <= 0) {
            finalBoss.die();
          }
        }
      }
      finalBoss.Update();
    }
    if (race) {//boss3 logic
      for (int j = 0; j < numBullets; j++) {
        Bullet b = bullets.get(j);
        if (pow(b.diameter / 2 + boss3.diameter / 2, 2) > pow(boss3.pos.x - b.X, 2) + pow(boss3.pos.y - b.Y, 2)) {
          boss3.health -= b.damage;
          bullets.remove(b);
          j--;
          player.numBullets--;
          if (boss3.health <= 0) {
            boss3.die();
          }
        }
      }
      boss3.update();
    }
    PVector mouse = new PVector(mouseX, mouseY);
    PVector dir = PVector.sub(mouse, pos);
    if (!playing) {
      PVector tempTarget = new PVector(targetPosX, targetPosY);
      dir = PVector.sub(tempTarget, pos);
    }
    pushMatrix();
    translate(pos.x, pos.y);
    rotate(radians(180) - atan2(dir.x, dir.y));
    image(img, 0, 0, 16, 16);
    popMatrix();
    // optional; in case you want to see the vector TO MOUSE
    //stroke(255, 0, 0);
    //line(mouseX, mouseY, pos.x, pos.y);
    //stroke(255);
  }

  void boost() {
    if (keyPressed) {
      if (keyCode == CONTROL) {
        if (boost > 0) {
          topspeed = speedHolder * 2;
          boost--;
        } else {
          topspeed = speedHolder;
        }
      }
    } else {
      topspeed = speedHolder;
    }
  }

  void special() {
    if(specialCooldown <= 0) {
      if(specialLevel == 1) {
        specialCooldown = 8;
        numBullets += 4;
        bullets.add(new Bullet(pos, new PVector(pos.x + 1, pos.y).sub(pos), bulletSpeed, bulletDamage * 2));
        bullets.add(new Bullet(pos, new PVector(pos.x - 1, pos.y).sub(pos), bulletSpeed, bulletDamage * 2));
        bullets.add(new Bullet(pos, new PVector(pos.x, pos.y + 1).sub(pos), bulletSpeed, bulletDamage * 2));
        bullets.add(new Bullet(pos, new PVector(pos.x, pos.y - 1).sub(pos), bulletSpeed, bulletDamage * 2));
      }
      if(specialLevel == 2) {
        specialCooldown = 6;
        numBullets += 8;
        bullets.add(new Bullet(pos, new PVector(pos.x + 1, pos.y).sub(pos), bulletSpeed, bulletDamage * 5));
        bullets.add(new Bullet(pos, new PVector(pos.x - 1, pos.y).sub(pos), bulletSpeed, bulletDamage * 5));
        bullets.add(new Bullet(pos, new PVector(pos.x + 1, pos.y + 1).sub(pos), bulletSpeed, bulletDamage * 5));
        bullets.add(new Bullet(pos, new PVector(pos.x + 1, pos.y - 1).sub(pos), bulletSpeed, bulletDamage * 5));
        bullets.add(new Bullet(pos, new PVector(pos.x - 1, pos.y + 1).sub(pos), bulletSpeed, bulletDamage * 5));
        bullets.add(new Bullet(pos, new PVector(pos.x - 1, pos.y - 1).sub(pos), bulletSpeed, bulletDamage * 5));
        bullets.add(new Bullet(pos, new PVector(pos.x, pos.y + 1).sub(pos), bulletSpeed, bulletDamage * 5));
        bullets.add(new Bullet(pos, new PVector(pos.x, pos.y - 1).sub(pos), bulletSpeed, bulletDamage * 5));
      }
      if(specialLevel == 3) {
        specialCooldown = 4;
        numBullets +=16;
        bullets.add(new Bullet(pos, new PVector(pos.x + 1, pos.y).sub(pos), bulletSpeed, bulletDamage * 10));
        bullets.add(new Bullet(pos, new PVector(pos.x - 1, pos.y).sub(pos), bulletSpeed, bulletDamage * 10));
        bullets.add(new Bullet(pos, new PVector(pos.x + 1, pos.y + 1).sub(pos), bulletSpeed, bulletDamage * 10));
        bullets.add(new Bullet(pos, new PVector(pos.x + 1, pos.y - 1).sub(pos), bulletSpeed, bulletDamage * 10));
        bullets.add(new Bullet(pos, new PVector(pos.x - 1, pos.y + 1).sub(pos), bulletSpeed, bulletDamage * 10));
        bullets.add(new Bullet(pos, new PVector(pos.x - 1, pos.y - 1).sub(pos), bulletSpeed, bulletDamage * 10));
        bullets.add(new Bullet(pos, new PVector(pos.x, pos.y + 1).sub(pos), bulletSpeed, bulletDamage * 10));
        bullets.add(new Bullet(pos, new PVector(pos.x, pos.y - 1).sub(pos), bulletSpeed, bulletDamage * 10));
        bullets.add(new Bullet(pos, new PVector(pos.x + 2, pos.y + 1).sub(pos), bulletSpeed, bulletDamage * 10));
        bullets.add(new Bullet(pos, new PVector(pos.x + 2, pos.y - 1).sub(pos), bulletSpeed, bulletDamage * 10));
        bullets.add(new Bullet(pos, new PVector(pos.x - 2, pos.y + 1).sub(pos), bulletSpeed, bulletDamage * 10));
        bullets.add(new Bullet(pos, new PVector(pos.x - 2, pos.y - 1).sub(pos), bulletSpeed, bulletDamage * 10));
        bullets.add(new Bullet(pos, new PVector(pos.x + 1, pos.y + 2).sub(pos), bulletSpeed, bulletDamage * 10));
        bullets.add(new Bullet(pos, new PVector(pos.x + 1, pos.y - 2).sub(pos), bulletSpeed, bulletDamage * 10));
        bullets.add(new Bullet(pos, new PVector(pos.x - 1, pos.y + 1).sub(pos), bulletSpeed, bulletDamage * 10));
        bullets.add(new Bullet(pos, new PVector(pos.x - 1, pos.y - 1).sub(pos), bulletSpeed, bulletDamage * 10));
      }
    }
  }

  void shoot() {
    if (playing) {
      if (mousePressed && (mouseButton == LEFT)) {
        shooting = true;
      } else {
        shooting = false;
      }
    }
    if (shooting) {
      shooting = true;
      if (delay > 0)
        delay--;
      else {
        if (playing) {
          targetPosX = mouseX;
          targetPosY = mouseY;
        }
        for(int i = 0; i < numToShoot; i++) {
          // 150 pixels away for "normal" bullet spread
          int normX = (int) abs(((targetPosX - (int) pos.x) * bulletSpread) / 150);
          int normY = (int) abs(((targetPosY - (int) pos.y) * bulletSpread) / 150);
          targetPosX = targetPosX + normX - (int) random(normX * 2 + 1);
          targetPosY = targetPosY + normY - (int) random(normY * 2 + 1);
          PVector mouse = new PVector(targetPosX, targetPosY);
          numBullets++;
          bullets.add(new Bullet(pos, mouse.sub(pos), bulletSpeed, bulletDamage));
          delay = attackDelay;
        }
      }
    } else {
      shooting = false;
    }
  }
}
