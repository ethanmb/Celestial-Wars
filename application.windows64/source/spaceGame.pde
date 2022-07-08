

PImage img, title, lava;
Player_Fighter playerF;
Boss_2 boss;
Enemy_Fighter enemy;
Boolean go, fight, fightBossDefeated;
Boolean raceBossDefeated, race, raceGo;
PImage fightBg;
int frameCounter;

Space_Ship player;
Button btnPlay;
Button btnHelp;
Button btnOptions;
Button btnOkay;
Button btnClose;
Button btnResume;
Button btnUpgrades;
Button speedUpgrade, shootSpeedUpgrade, bulletSpeedUpgrade, bulletDamageUpgrade, healthUpgrade, regenUpgrade, defenseUpgrade, spreadUpgrade, multishotUpgrade, specialUpgrade;
Button chicken, wolf, corn, nothing;
Button howToPlay;
Button easy, medium, hard;
int difficulty;
int speedCost = 100, shootSpeedCost = 100, bulletSpeedCost = 100, bulletDamageCost = 100, healthCost = 100, regenCost = 100, defenseCost = 100, spreadCost = 500, multishotCost = 1000, specialCost = 1000;
Boolean helpOpen = false, optionsOpen = false, paused = false, upgradesOpen = false, gameOver = false;
int numEnemiesLeft, nextSpawn;
int coins;
boolean playing = false;
boolean boss1 = false, bossOut = false, endOfRound = false, puzzleBossOut = false, gridInitialized = false, puzzleBossDefeated = false, puzzleBossStarted1 = false, howToPlayOpen = false;
int puzzleBossPhase = 1;
int time, lastTime, puzzleTime = 0;
boolean chickenCrossed = false, wolfCrossed = false, cornCrossed = false, crossed = false;
Boss_3 boss3;
ArrayList<Bullet> enemyBullets = new ArrayList<Bullet>(); // Needs to be here otherwise all bullets will be deleted when enemies die
BossGrid grid;
int wall1;
int wall2;
Wall[] walls = new Wall[4];
int level;

void setup() {
  difficulty = 1;
  level = 1;
  race = false;
  raceBossDefeated = false;
  time = 0;
  lastTime = 0;
  size(900, 600);
  cursor(CROSS);
  frameRate(60);
  smooth();
  img = loadImage("spaceBG.png");
  title = loadImage("title.png");
  background(img);
  lava = loadImage("lava.png");
  player = new Space_Ship();
  btnPlay = new Button(width/2, height/2 - 100, 100, 25, #00A100, #00FF00, "Play");
  btnHelp = new Button(75, height - 50, 100, 25, #00A100, #00FF00, "Help");
  btnOptions = new Button(75, height - 100, 100, 25, #00A100, #00FF00, "Options");
  btnClose = new Button(width - 75, height - 50, 100, 25, #A10000, #FF0000, "Exit");
  btnOkay = new Button(width/2, height/2 + 150, 100, 25, #00A100, #00FF00, "Okay");
  wolf = new Button(width/2 - 150, height/2, 100, 25, #008800, #00DD00, "Wolf");
  chicken = new Button(width/2, height/2, 100, 25, #008800, #00DD00, "Chicken");
  nothing = new Button(width/2, height/2 + 100, 100, 25, #008800, #00DD00, "Nothing");
  corn = new Button(width/2 + 150, height/2, 100, 25, #008800, #00DD00, "Corn");
  howToPlay = new Button(width/2, height/2 + 75, 100, 25, #00A100, #00FF00, "How to play");
  easy = new Button(width/2, height/2 - 105, 200, 50, #880000, #00DD00, "Easy");
  medium = new Button(width/2, height/2 - 35, 200, 50, #008800, #00DD00, "Medium");
  hard = new Button(width/2, height/2 + 35, 200, 50, #880000, #00DD00, "Hard");

  boss = new Boss_2(new PVector(0, 0));
  playerF = new Player_Fighter(new PVector(width/3, height/2));
  enemy = new Enemy_Fighter(new PVector(0, height/2));
  go = true;

  //coins = 1000000; //Setting to large number for testing purposes.
  coins = 100;
  numEnemiesLeft = 20;
  grid = new BossGrid();
  fight = false;
  fightBossDefeated = false;
  fightBg=loadImage("fightBack.png");
  frameCounter = 0;
  walls[0] = new Wall(0, (int) random(width) - 50);
  walls[1] = new Wall(1, (int) random(width) - 50);
  walls[2] = new Wall(2, (int) random(height) - 50);
  walls[3] = new Wall(3, (int) random(height) - 50);
  wall1 =(int) random(2);
  wall2 = (int) random(2, 4);
  boss3 = new Boss_3();
}

void draw() {
  lastTime = time;
  time = millis();
  background(img);
  fill(255, 10);
  if (!paused && !endOfRound && !gameOver && !puzzleBossOut && !fight) {
    player.shoot();
    player.render();
    player.drawPlayer();
    player.boost();
    if (playing == false) {
      image(title, width/2, 50);
      btnPlay.drawButton();
      btnHelp.drawButton();
      btnOptions.drawButton();
      btnClose.drawButton();
    }
    if (playing) {
      hud();
      nextSpawn--;
      boss1Func();
      if (numEnemiesLeft > 0 && nextSpawn <= 0 && bossOut == false && !race && level == 1 || player.numEnemies == 0) {
        addEnemy1();
      }
      if (numEnemiesLeft > 0 && nextSpawn <= 0 && bossOut == false && !race && level == 2 || player.numEnemies == 0) {
        addEnemy2();
      }
      if (numEnemiesLeft > 0 && nextSpawn <= 0 && bossOut == false && !race && level == 3 || player.numEnemies == 0) {
        addEnemy3();
      }
      if (numEnemiesLeft > 0 && nextSpawn <= 0 && bossOut == false && !race && level == 4 || player.numEnemies == 0) {
        addEnemy4();
      }
      if (numEnemiesLeft > 0 && nextSpawn <= 0 && bossOut == false && !race && level == 5 || player.numEnemies == 0) {
        addEnemy5();
      }
      enemyBullet();
    }
    playerBullet();
  }
  if (endOfRound) {//this is end of round
    fill(#FFFFFF);
    text("Thank you for playing our game prototype, we hope you enjoyed!", width/2, height/2);
  }
  if (gameOver) {
    fill(#FFFFFF);
    text("Game over! You finished with a score of: " + player.score, width/2, height/2);
  }
  if (paused) {
    pauseDraw();
  }
  if (helpOpen) {
    helpDraw();
  }
  if (optionsOpen) {
    optionDraw();
  }
  if (upgradesOpen) {
    upgradeDraw();
  }
  if (numEnemiesLeft <= 0 && !puzzleBossDefeated && level == 2) {
    puzzleBossOut = true;
  }
  if(numEnemiesLeft <= 0 && level == 5) {
    player.finalBossOut = true;
  }
  if (puzzleBossOut && !gameOver) {
    puzzleBoss();
  }
  if ((numEnemiesLeft <= 0 && level == 4)) {
    fight = true;
  }
  if (fight && !gameOver && !paused) {
    fightBoss();
  }
  if ((numEnemiesLeft <= 0 && level == 3)) {
    countDown();
  }
  if (race && !gameOver && !paused) {
    race();
  }
}

void race() {
  fill(#00FF00);
  rectMode(CENTER);
  rect(width/2, 50, boss3.health * 0.9, 50);
  fill(0);
  textSize(30);
  text(boss3.health, (width/2), 50);
  walls[wall1].draw();
  walls[wall2].draw();
}

void giveCoins(int amount) {
  if(difficulty == 0) {
    coins += (int) (amount * 2);
  }
  if(difficulty == 1) {
    coins += amount;
  }
  if(difficulty == 2) {
    coins += (int) (amount * 0.66);
  }
}

void countDown() {
  if (!race) {
    fill(#FFFFFF);
    textSize(50);
    textAlign(CENTER);
    text("FIRE BOSS BEGINS IN...", width/2, height/2-50);
    if (frameCounter < 61) { 
      fill(#DD0000);
      text('3', width/2, height/2);
    } else if (frameCounter < 121) {
      fill(#FF6600);
      text('2', width/2, height/2);
    } else if (frameCounter < 181) {
      fill(#DDDD00);
      text('1', width/2, height/2);
    } else if (frameCounter < 241) {
      fill(#00FF00);
      text("GO!", width/2, height/2);
    } else {
      race = true;
    }
    frameCounter++;
  }
}

void fightBoss() {
  background(fightBg);
  text(player.score, width/2, 20);

  enemy.draw();

  if (player.health <= 0) {
    gameOver = true;
  }
  if (playerF.score > 2 && boss.health > 0) {
    if (go) {
      boss.pos = new PVector(2*width/3, height/2);
      go = false;
    }
    boss.draw();
    fill(#00FF00);
    rect(0, 0, boss.health, 25);
    fill(#000000);
    text("Health: " + boss.health, width/2, 20);
    fill(#FF0000);
    imageMode(CORNER);
    image(lava, 0, height/2 + playerF.diameter);
  } else if (boss.health <= 0) {
    go = true;
    boss.health = 0;
    fightBossDefeated = true;
    fight = false;
    giveCoins(8000);
    numEnemiesLeft = 60;
    level = 5;
  } else {
    textSize(16);
    text("Space to jump, 'a' and 'd' to move back and forth, 'k' to attack (Must be in the air)", width/2, 100);
    fill(200);
    rect(0, height/2 + playerF.diameter, width, height);
  }
  hud();
  playerF.draw();
}

void puzzleBoss() {
  if (puzzleBossPhase == 1) {
    if (!puzzleBossStarted1) {
      fill(#660000);
      rectMode(CENTER);
      rect(width/2, height/2, 500, 250);
      fill(0);
      textSize(16);
      text("You must cross this area with a chicken, a wolf, and some corn.\nHowever, you may only bring one at once.\nIf the chicken is left with the corn, it will eat it.\nIf the wolf is left with the chicken, it will eat it.\nChoose the order to bring them wisely.", width/2, height/2 - 50);
      btnOkay.x = width/2;
      btnOkay.y = height/2 + 100;
      btnOkay.drawButton();
    } else {
      if (!crossed) {
        fill(255);
        textSize(18);
        text("What would you like to bring with you?", width/2, height/2 - 50);
        if (!wolfCrossed) {
          wolf.drawButton();
        }
        if (!chickenCrossed) {
          chicken.drawButton();
        }
        if (!cornCrossed) {
          corn.drawButton();
        }
        nothing.drawButton();
      }
      if (crossed) {
        fill(255);
        textSize(18);
        text("What would you like to bring back?", width/2, height/2 - 50);
        if (wolfCrossed) {
          wolf.drawButton();
        }
        if (chickenCrossed) {
          chicken.drawButton();
        }
        if (cornCrossed) {
          corn.drawButton();
        }
        nothing.drawButton();
      }
      if (chickenCrossed && cornCrossed && wolfCrossed) {
        giveCoins(750);
        player.score += 3;
        puzzleBossPhase = 2;
      }
    }
  }
  if (puzzleBossPhase == 2) {
    fill(255);
    textSize(48);
    text("Patience is the key", width/2, height/2);
    puzzleTime += time - lastTime;
    if (puzzleTime >= 5000) {
      giveCoins(750);
      player.score += 3;
      puzzleBossPhase = 3;
    }
  }
  if (puzzleBossPhase == 3) {
    if (!gridInitialized) {
      btnOkay.x = width/2;
      btnOkay.y = height/2 + 100;
      if (!howToPlayOpen) {
        fill(255);
        textSize(20);
        text("Oh no! The evil boss has hidden bombs all over the place\nYou must find a safe path around them.", width/2, height/2);
        btnOkay.drawButton();
        howToPlay.drawButton();
      }
      if (!paused && puzzleBossPhase == 3 && howToPlayOpen) {
        fill(#660000);
        rectMode(CENTER);
        rect(width/2, height/2, 500, 250);
        fill(0);
        textSize(16);
        text("The goal is to reveal every tile that is not a bomb.\nClicking on a tile will reveal it\nwhich shows how many adjacent tiles have bombs.\nIf a tile has no bombs, all adjacent ones are revealed.\nClicking on a bomb is game over.\nWhen all tiles not revealed are bombs, the game is won.", width/2, height/2 - 75);
        btnOkay.drawButton();
      }
    } else {
      for (int x = 0; x < grid.gridSize; x++) {
        for (int y = 0; y < grid.gridSize; y++) {
          grid.tiles[x][y].thisButton.drawButton();
        }
      }
      fill(255);
      text("Number of bombs left: " + grid.numBombs, width/2, height/2 - 200);
    }
  }
}

void pauseDraw() {
  if(!optionsOpen) {
    fill(#550000);
    rectMode(CORNER);
    rect(width/2 - 120, height/2 - 180, 240, 360);
    btnResume.drawButton();
    btnUpgrades.drawButton();
    btnHelp.drawButton();
    btnOptions.drawButton();
    btnClose.drawButton();
  }
}

void hud() {
  fill(155);
  textSize(12);
  text("Boost: " + player.boost, (width-50), 575);
  text("Score: " + player.score, (width - 50), 550);
  text("Coins: " + coins, (width - 50), 525);
  // Draw the players health bar
  rectMode(CORNER);
  fill(#00DD00);
  rect(25, height - 50, 100 * ((float)player.health/(float)player.maxHealth), 25);
  fill(#FFFFFF);
  textSize(12);
  text("Health: " + (int) player.health + "/" + player.maxHealth, 75, height - 35);
  if (player.specialLevel >= 1) {
    text("Special cooldown : " + player.specialCooldown, 75, height - 70);
  }
}

void boss1Func() {
  if (numEnemiesLeft <= 0 && level == 1) {//set up how many to kill
    bossOut = true;
  }
  if (bossOut) {
    boss1= true;
    fill(#00FF00);
    rectMode(CENTER);
    rect(width/2, 50, player.boss1Object.health * 0.9, 50);
    fill(0);
    textSize(30);
    text(player.boss1Object.health, (width/2), 50);
  }
}

void addEnemy1() {
  if(difficulty == 0) {
    player.enemies.add(new Enemy(new PVector(random(width), random(height)), 20, 20, 0, 10, 100, 4, 10, 100));
  }
  if(difficulty == 1) {
    player.enemies.add(new Enemy(new PVector(random(width), random(height)), 25, 20, (int) random(2), 12, 90, 4, 10, 100));
  }
  if(difficulty == 2) {
    player.enemies.add(new Enemy(new PVector(random(width), random(height)), 30, 20, 1, 10, 100, 4, 15, 100));
  }
  player.numEnemies++;
  nextSpawn = 200;
}
void addEnemy2() {
  if(difficulty == 0) {
    player.enemies.add(new Enemy(new PVector(random(width), random(height)), 40, 20, 0, 10, 100, 4, 10, 200));
  }
  if(difficulty == 1) {
    player.enemies.add(new Enemy(new PVector(random(width), random(height)), 60, 20, (int) random(2), 15, 85, 4, 10, 200));
  }
  if(difficulty == 2) {
    player.enemies.add(new Enemy(new PVector(random(width), random(height)), 85, 20, 1, 20, 70, 4, 20, 200));
  }
  player.numEnemies++;
  nextSpawn = 180;
}
void addEnemy3() {
  if(difficulty == 0) {
    player.enemies.add(new Enemy(new PVector(random(width), random(height)), 75, 20, 0, 15, 80, 4, 10, 350));
  }
  if(difficulty == 1) {
    player.enemies.add(new Enemy(new PVector(random(width), random(height)), 120, 20, 1, 20, 70, 4, 10, 350));
  }
  if(difficulty == 2) {
    player.enemies.add(new Enemy(new PVector(random(width), random(height)), 160, 20, 1, 30, 60, 4, 10, 350));
  }
  player.numEnemies++;
  nextSpawn = 160;
}
void addEnemy4() {
  if(difficulty == 0) {
    player.enemies.add(new Enemy(new PVector(random(width), random(height)), 100, 20, 0, 15, 80, 4, 10, 500));
  }
  if(difficulty == 1) {
    player.enemies.add(new Enemy(new PVector(random(width), random(height)), 150, 20, 1, 25, 65, 4, 10, 500));
  }
  if(difficulty == 2) {
    player.enemies.add(new Enemy(new PVector(random(width), random(height)), 200, 20, 1, 40, 55, 4, 10, 500));
  }
  player.numEnemies++;
  nextSpawn = 135;
}
void addEnemy5() {
  if(difficulty == 0) {
    player.enemies.add(new Enemy(new PVector(random(width), random(height)), 125, 20, 0, 20, 75, 4, 10, 750));
  }
  if(difficulty == 1) {
    player.enemies.add(new Enemy(new PVector(random(width), random(height)), 200, 20, 1, 35, 65, 4, 10, 750));
  }
  if(difficulty == 2) {
    player.enemies.add(new Enemy(new PVector(random(width), random(height)), 275, 20, 1, 50, 50, 4, 10, 750));
  }
  player.numEnemies++;
  nextSpawn = 100;
}

void enemyBullet() {
  for (int i  = enemyBullets.size() - 1; i > -1; i--) {
    boolean bulletExists = true;
    Bullet currentBullet = enemyBullets.get(i);
    currentBullet.update();
    if (pow(currentBullet.diameter / 2 + player.diameter / 2, 2) > pow(player.pos.x - currentBullet.X, 2) + pow(player.pos.y - currentBullet.Y, 2)) {
      if (currentBullet.damage * 0.66 - player.defense > 0 && difficulty == 0) {
        player.health -= (currentBullet.damage * 0.66 - player.defense);
      }
      if (currentBullet.damage - player.defense > 0 && difficulty == 1) {
        player.health -= (currentBullet.damage - player.defense);
      }
      if (currentBullet.damage * 1.5 - player.defense > 0 && difficulty == 2) {
        player.health -= (currentBullet.damage * 1.5 - player.defense);
      }
      if (player.health <= 0) {
        gameOver = true;
      }
      enemyBullets.remove(i);
      bulletExists = false;
    }
    if (bulletExists) {
      if (currentBullet.X < 0 || currentBullet.X > width || currentBullet.Y < 0 || currentBullet.Y > height) {
        enemyBullets.remove(i);
      }
    }
  }
}

void playerBullet() {
  for (int i = 0; i < player.numBullets; i++) {
    player.bullets.get(i).update();
    if (player.bullets.get(i).X > width || player.bullets.get(i).X < 0 || player.bullets.get(i).Y > height || player.bullets.get(i).Y < 0)
    {
      player.bullets.remove(i);
      i--;
      player.numBullets--;
    }
  }
}

void helpDraw() {
  fill(#880000);
  rect(width/2, height/2, 600, 400);
  fill(0);
  textAlign(LEFT);
  text("Press play to start the game!", width/2 - 280, height/2 - 180);
  text("Control your character with the mouse, and press space to move.", width/2 - 280, height/2 - 165);
  text("Press CTRL to boost, and move faster.", width/2 - 280, height/2 - 150);
  text("Defeat enemies to gain points.", width/2 - 280, height/2 - 135);
  text("At the end of each level, defeat the boss to progress.", width/2 - 280, height/2 - 120);
  text("Use coins gained from defeating enemies to power up your ship, or find temporary ones randomly.", width/2 - 280, height/2 - 105);
  text("You may use your special attack (given you have purchased it) with the z key", width/2 - 280, height/2 - 90);

  btnOkay.drawButton();
}

void optionDraw() {
  fill(#000088);
  rect(width/2, height/2, 250, 300);
  easy.drawButton();
  medium.drawButton();
  hard.drawButton();
  btnOkay.drawButton();
}

void upgradeDraw() {
  fill(#880000);
  rect(width/2, height/2, 460, 440);
  speedUpgrade.drawButton();
  shootSpeedUpgrade.drawButton();
  bulletSpeedUpgrade.drawButton();
  bulletDamageUpgrade.drawButton();
  healthUpgrade.drawButton();
  regenUpgrade.drawButton();
  defenseUpgrade.drawButton();
  spreadUpgrade.drawButton();
  multishotUpgrade.drawButton();
  specialUpgrade.drawButton();
  btnOkay.drawButton();
}



void keyPressed() {
  if (key == 'z') {
    player.special();
  }
  if (key == 27) {
    key = 0;
    paused = true;
    btnResume = new Button(width/2, height/2 - 140, 200, 50, #00A100, #00FF00, "Resume");
    btnUpgrades = new Button(width/2, height/2 - 70, 200, 50, #00A100, #00FF00, "Upgrades");
    btnHelp = new Button(width/2, height/2, 200, 50, #00A100, #00FF00, "Help");
    btnOptions = new Button(width/2, height/2 + 70, 200, 50, #00A100, #00FF00, "Options");
    btnClose = new Button(width/2, height/2 + 140, 200, 50, #A10000, #FF0000, "Exit");
  }
}

void mouseReleased() {
  if (puzzleBossPhase == 2) {
    player.health -= 10;
    if (player.health <= 0) {
      gameOver = true;
    }
  }
  if (puzzleBossOut) {
    if (gridInitialized) {
      for (int x = 0; x < grid.gridSize; x++) {
        for (int y = 0; y < grid.gridSize; y++) {
          if (grid.tiles[x][y].thisButton.mousePressed()) {
            grid.tiles[x][y].clicked();
            return;
          }
        }
      }
    }
    if(btnOkay.mousePressed() && !paused && puzzleBossPhase == 3 && howToPlayOpen) {
      howToPlayOpen = false;
      System.out.println("test");
      return;
    }
    if (btnOkay.mousePressed() && !paused && puzzleBossPhase == 1 && !puzzleBossStarted1) {
      puzzleBossStarted1 = true;
      return;
    }
    if (btnOkay.mousePressed() && !paused && puzzleBossPhase == 3 && !howToPlayOpen) {
      grid.createGrid();
      gridInitialized = true;
      return;
    }
    if (howToPlay.mousePressed() && !paused && puzzleBossPhase == 3) {
      howToPlayOpen = true;
      return;
    }
    if (puzzleBossStarted1 && puzzleBossPhase == 1 && !paused) {
      if (corn.mousePressed() && !cornCrossed && !crossed) {
        if (!chickenCrossed && !wolfCrossed || chickenCrossed && wolfCrossed) {
          player.health -= 10;
          if (player.health <= 0) {
            gameOver = true;
          }
          cornCrossed = false;
          chickenCrossed = false;
          wolfCrossed = false;
          crossed = false;
        } else {
          cornCrossed = true;
          crossed = true;
        }
        return;
      }
      if (corn.mousePressed() && cornCrossed && crossed) {
        if (!chickenCrossed && !wolfCrossed || chickenCrossed && wolfCrossed) {
          player.health -= 10;
          if (player.health <= 0) {
            gameOver = true;
          }
          cornCrossed = false;
          chickenCrossed = false;
          wolfCrossed = false;
          crossed = false;
        } else {
          cornCrossed = false;
          crossed = false;
        }
        return;
      }
      if (chicken.mousePressed() && !chickenCrossed && !crossed) {
        chickenCrossed = true;
        crossed = true;
        return;
      }
      if (chicken.mousePressed() && chickenCrossed && crossed) {
        chickenCrossed = false;
        crossed = false;
        return;
      }
      if (wolf.mousePressed() && !wolfCrossed && !crossed) {
        if (!chickenCrossed && !cornCrossed || chickenCrossed && cornCrossed) {
          player.health -= 10;
          if (player.health <= 0) {
            gameOver = true;
          }
          cornCrossed = false;
          chickenCrossed = false;
          wolfCrossed = false;
          crossed = false;
        } else {
          wolfCrossed = true;
          crossed = true;
        }
        return;
      }
      if (wolf.mousePressed() && wolfCrossed && crossed) {
        if (!chickenCrossed && !cornCrossed || chickenCrossed && cornCrossed) {
          player.health -= 10;
          if (player.health <= 0) {
            gameOver = true;
          }
          cornCrossed = false;
          chickenCrossed = false;
          wolfCrossed = false;
          crossed = false;
        } else {
          wolfCrossed = true;
          crossed = false;
        }
        return;
      }
      if (nothing.mousePressed() && crossed) {
        if (!chickenCrossed && !cornCrossed || chickenCrossed && cornCrossed) {
          player.health -= 10;
          if (player.health <= 0) {
            gameOver = true;
          }
          cornCrossed = false;
          chickenCrossed = false;
          wolfCrossed = false;
          crossed = false;
        } else if (!chickenCrossed && !wolfCrossed || chickenCrossed && wolfCrossed) {
          player.health -= 10;
          if (player.health <= 0) {
            gameOver = true;
          }
          cornCrossed = false;
          chickenCrossed = false;
          wolfCrossed = false;
          crossed = false;
        } else {
          crossed = false;
        }
        return;
      }
      if (nothing.mousePressed() && !crossed) {
        if (!chickenCrossed && !cornCrossed || chickenCrossed && cornCrossed) {
          player.health -= 10;
          if (player.health <= 0) {
            gameOver = true;
          }
          cornCrossed = false;
          chickenCrossed = false;
          wolfCrossed = false;
          crossed = false;
        } else if (!chickenCrossed && !wolfCrossed || chickenCrossed && wolfCrossed) {
          player.health -= 10;
          if (player.health <= 0) {
            gameOver = true;
          }
          cornCrossed = false;
          chickenCrossed = false;
          wolfCrossed = false;
          crossed = false;
        } else {
          crossed = true;
        }
        return;
      }
    }
  }
  if (!playing) {
    if (btnPlay.mousePressed() && !helpOpen && !optionsOpen) {
      playing = true;
      return;
    }
    if (btnHelp.mousePressed() && !helpOpen && !optionsOpen) {
      helpOpen = true;
      return;
    }
    if (btnOptions.mousePressed() && !helpOpen && !optionsOpen) {
      optionsOpen = true;
      btnOkay.x = width/2;
      btnOkay.y = height/2 + 105;
      btnOkay.widthB = 200;
      btnOkay.heightB = 50;
      btnOkay.text = "Okay";
      return;
    }
    if (btnClose.mousePressed()) {
      exit();
    }
  }
  if (upgradesOpen) {
    if (btnOkay.mousePressed()) {
      upgradesOpen = false;
      btnOkay = new Button(width/2, height/2 + 150, 100, 25, #00A100, #00FF00, "Okay");
      return;
    }
    if (speedUpgrade.mousePressed() && speedCost <= coins) {
      player.speedHolder += 1;
      coins -= speedCost;
      speedCost = (int) (speedCost * 3.5);
      speedUpgrade = new Button(width/2 - 115, height/2 - 175, 200, 50, #00A100, #00FF00, "Move Speed\n" + speedCost);
      return;
    }
    if (shootSpeedUpgrade.mousePressed() && shootSpeedCost <= coins) {
      player.attackDelay--;
      coins -= shootSpeedCost;
      shootSpeedCost = (int) (shootSpeedCost * 1.5);
      shootSpeedUpgrade = new Button(width/2 - 115, height/2 - 105, 200, 50, #00A100, #00FF00, "Shooting Speed\n" + shootSpeedCost);
      return;
    }
    if (bulletSpeedUpgrade.mousePressed() && bulletSpeedCost <= coins) {
      player.bulletSpeed++;
      coins -= bulletSpeedCost;
      bulletSpeedCost = (int) (bulletSpeedCost * 1.5);
      bulletSpeedUpgrade = new Button(width/2 - 115, height/2 - 35, 200, 50, #00A100, #00FF00, "Bullet Speed\n" + bulletSpeedCost);
      return;
    }
    if (bulletDamageUpgrade.mousePressed() && bulletDamageCost <= coins) {
      player.bulletDamage += 5;
      coins -= bulletDamageCost;
      bulletDamageCost = (int) (bulletDamageCost * 1.5);
      bulletDamageUpgrade = new Button(width/2 - 115, height/2 + 35, 200, 50, #00A100, #00FF00, "Bullet Damage\n" + bulletDamageCost);
      return;
    }
    if (healthUpgrade.mousePressed() && healthCost <= coins) {
      player.health += 50;
      player.maxHealth += 50;
      coins -= healthCost;
      healthCost = (int) (healthCost * 1.5);
      healthUpgrade = new Button(width/2 - 115, height/2 + 105, 200, 50, #00A100, #00FF00, "Health\n" + healthCost);
      return;
    }
    if (regenUpgrade.mousePressed() && regenCost <= coins) {
      player.regen += 1;
      coins -= regenCost;
      regenCost = (int) (regenCost * 1.5);
      regenUpgrade = new Button(width/2 + 115, height/2 - 175, 200, 50, #00A100, #00FF00, "Life Regen\n" + regenCost);
      return;
    }
    if (defenseUpgrade.mousePressed() && defenseCost <= coins) {
      player.defense += 1;
      coins -= defenseCost;
      defenseCost = (int) (defenseCost * 1.5);
      defenseUpgrade = new Button(width/2 + 115, height/2 - 105, 200, 50, #00A100, #00FF00, "Defense\n" + defenseCost);
      return;
    }
    if (spreadUpgrade.mousePressed() && spreadCost <= coins) {
      player.bulletSpread -= 2;
      coins -= spreadCost;
      spreadCost = (int) (spreadCost * 1.5);
      spreadUpgrade = new Button(width/2 + 115, height/2 - 35, 200, 50, #00A100, #00FF00, "Bullet Spread\n" + spreadCost);
      return;
    }
    if (multishotUpgrade.mousePressed() && multishotCost <= coins) {
      player.numToShoot += 1;
      coins -= multishotCost;
      multishotCost = (int) (multishotCost * 3.5); // Very strong, very expensive
      multishotUpgrade = new Button(width/2 + 115, height/2 + 35, 200, 50, #00A100, #00FF00, "Multishot\n" + multishotCost);
      return;
    }
    if (specialUpgrade.mousePressed() && specialCost <= coins && player.specialLevel < 3) {
      player.specialLevel += 1;
      coins -= specialCost;
      specialCost = (int) (specialCost * 6); // Very strong, very expensive
      specialUpgrade = new Button(width/2 + 115, height/2 + 105, 200, 50, #00A100, #00FF00, "Special Attack\n" + specialCost);
      if (player.specialLevel == 3) {
        specialCost = 1000000000;
        specialUpgrade = new Button(width/2 + 115, height/2 + 105, 200, 50, #004400, #004400, "Special Attack\nMAX");
      }
      return;
    }
  }
  if (!upgradesOpen && !helpOpen && !optionsOpen && paused) {
    if (btnResume.mousePressed()) {
      paused = false;
      btnOptions = new Button(75, height - 100, 100, 25, #00A100, #00FF00, "Options");
      btnHelp = new Button(75, height - 50, 100, 25, #00A100, #00FF00, "Help");
      btnClose = new Button(width - 75, height - 50, 100, 25, #A10000, #FF0000, "Exit");
      return;
    }
    if (btnUpgrades.mousePressed()) {
      upgradesOpen = true;
      speedUpgrade = new Button(width/2 - 115, height/2 - 175, 200, 50, #00A100, #00FF00, "Move Speed\n" + speedCost);
      shootSpeedUpgrade = new Button(width/2 - 115, height/2 - 105, 200, 50, #00A100, #00FF00, "Shooting Speed\n" + shootSpeedCost);
      bulletSpeedUpgrade = new Button(width/2 - 115, height/2 - 35, 200, 50, #00A100, #00FF00, "Bullet Speed\n" + bulletSpeedCost);
      bulletDamageUpgrade = new Button(width/2 - 115, height/2 + 35, 200, 50, #00A100, #00FF00, "Bullet Damage\n" + bulletDamageCost);
      healthUpgrade = new Button(width/2 - 115, height/2 + 105, 200, 50, #00A100, #00FF00, "Health\n" + healthCost);
      regenUpgrade = new Button(width/2 + 115, height/2 - 175, 200, 50, #00A100, #00FF00, "Life Regen\n" + regenCost);
      defenseUpgrade = new Button(width/2 + 115, height/2 - 105, 200, 50, #00A100, #00FF00, "Defense\n" + defenseCost);
      spreadUpgrade = new Button(width/2 + 115, height/2 - 35, 200, 50, #00A100, #00FF00, "Bullet Spread\n" + spreadCost);
      multishotUpgrade = new Button(width/2 + 115, height/2 + 35, 200, 50, #00A100, #00FF00, "Multishot\n" + multishotCost);
      specialUpgrade = new Button(width/2 + 115, height/2 + 105, 200, 50, #00A100, #00FF00, "Special Attack\n" + specialCost);
      if (player.specialLevel == 3) {
        specialCost = 1000000000;
        specialUpgrade = new Button(width/2 + 115, height/2 + 105, 200, 50, #004400, #004400, "Special Attack\nMAX");
      }
      btnOkay = new Button(width/2, height/2 + 175, 200, 50, #00A100, #00FF00, "Done");
      return;
    }
    if (btnHelp.mousePressed()) {
      helpOpen = true;
      return;
    }
    if (btnOptions.mousePressed()) {
      optionsOpen = true;
      btnOkay.x = width/2;
      btnOkay.y = height/2 + 105;
      btnOkay.widthB = 200;
      btnOkay.heightB = 50;
      btnOkay.text = "Okay";
      return;
    }
    if (btnClose.mousePressed()) {
      exit();
    }
  }
  if (helpOpen || optionsOpen) {
    if (btnOkay.mousePressed() && helpOpen) {
      helpOpen = false;
      return;
    }
    if (btnOkay.mousePressed() && optionsOpen) {
      btnOkay = new Button(width/2, height/2 + 150, 100, 25, #00A100, #00FF00, "Okay");
      optionsOpen = false;
      return;
    }
    if(easy.mousePressed() && optionsOpen) {
      difficulty = 0;
      easy.c = #008800;
      medium.c = #880000;
      hard.c = #880000;
      return;
    }
    if(medium.mousePressed() && optionsOpen) {
      difficulty = 1;
      easy.c = #880000;
      medium.c = #008800;
      hard.c = #880000;
      return;
    }
    if(hard.mousePressed() && optionsOpen) {
      difficulty = 2;
      easy.c = #880000;
      medium.c = #880000;
      hard.c = #008800;
      return;
    }
  }
}
