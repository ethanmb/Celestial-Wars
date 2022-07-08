import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class spaceGame extends PApplet {



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

public void setup() {
  difficulty = 1;
  level = 1;
  race = false;
  raceBossDefeated = false;
  time = 0;
  lastTime = 0;
  
  cursor(CROSS);
  frameRate(60);
  
  img = loadImage("spaceBG.png");
  title = loadImage("title.png");
  background(img);
  lava = loadImage("lava.png");
  player = new Space_Ship();
  btnPlay = new Button(width/2, height/2 - 100, 100, 25, 0xff00A100, 0xff00FF00, "Play");
  btnHelp = new Button(75, height - 50, 100, 25, 0xff00A100, 0xff00FF00, "Help");
  btnOptions = new Button(75, height - 100, 100, 25, 0xff00A100, 0xff00FF00, "Options");
  btnClose = new Button(width - 75, height - 50, 100, 25, 0xffA10000, 0xffFF0000, "Exit");
  btnOkay = new Button(width/2, height/2 + 150, 100, 25, 0xff00A100, 0xff00FF00, "Okay");
  wolf = new Button(width/2 - 150, height/2, 100, 25, 0xff008800, 0xff00DD00, "Wolf");
  chicken = new Button(width/2, height/2, 100, 25, 0xff008800, 0xff00DD00, "Chicken");
  nothing = new Button(width/2, height/2 + 100, 100, 25, 0xff008800, 0xff00DD00, "Nothing");
  corn = new Button(width/2 + 150, height/2, 100, 25, 0xff008800, 0xff00DD00, "Corn");
  howToPlay = new Button(width/2, height/2 + 75, 100, 25, 0xff00A100, 0xff00FF00, "How to play");
  easy = new Button(width/2, height/2 - 105, 200, 50, 0xff880000, 0xff00DD00, "Easy");
  medium = new Button(width/2, height/2 - 35, 200, 50, 0xff008800, 0xff00DD00, "Medium");
  hard = new Button(width/2, height/2 + 35, 200, 50, 0xff880000, 0xff00DD00, "Hard");

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

public void draw() {
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
    fill(0xffFFFFFF);
    text("Thank you for playing our game prototype, we hope you enjoyed!", width/2, height/2);
  }
  if (gameOver) {
    fill(0xffFFFFFF);
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

public void race() {
  fill(0xff00FF00);
  rectMode(CENTER);
  rect(width/2, 50, boss3.health * 0.9f, 50);
  fill(0);
  textSize(30);
  text(boss3.health, (width/2), 50);
  walls[wall1].draw();
  walls[wall2].draw();
}

public void giveCoins(int amount) {
  if(difficulty == 0) {
    coins += (int) (amount * 2);
  }
  if(difficulty == 1) {
    coins += amount;
  }
  if(difficulty == 2) {
    coins += (int) (amount * 0.66f);
  }
}

public void countDown() {
  if (!race) {
    fill(0xffFFFFFF);
    textSize(50);
    textAlign(CENTER);
    text("FIRE BOSS BEGINS IN...", width/2, height/2-50);
    if (frameCounter < 61) { 
      fill(0xffDD0000);
      text('3', width/2, height/2);
    } else if (frameCounter < 121) {
      fill(0xffFF6600);
      text('2', width/2, height/2);
    } else if (frameCounter < 181) {
      fill(0xffDDDD00);
      text('1', width/2, height/2);
    } else if (frameCounter < 241) {
      fill(0xff00FF00);
      text("GO!", width/2, height/2);
    } else {
      race = true;
    }
    frameCounter++;
  }
}

public void fightBoss() {
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
    fill(0xff00FF00);
    rect(0, 0, boss.health, 25);
    fill(0xff000000);
    text("Health: " + boss.health, width/2, 20);
    fill(0xffFF0000);
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

public void puzzleBoss() {
  if (puzzleBossPhase == 1) {
    if (!puzzleBossStarted1) {
      fill(0xff660000);
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
        fill(0xff660000);
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

public void pauseDraw() {
  if(!optionsOpen) {
    fill(0xff550000);
    rectMode(CORNER);
    rect(width/2 - 120, height/2 - 180, 240, 360);
    btnResume.drawButton();
    btnUpgrades.drawButton();
    btnHelp.drawButton();
    btnOptions.drawButton();
    btnClose.drawButton();
  }
}

public void hud() {
  fill(155);
  textSize(12);
  text("Boost: " + player.boost, (width-50), 575);
  text("Score: " + player.score, (width - 50), 550);
  text("Coins: " + coins, (width - 50), 525);
  // Draw the players health bar
  rectMode(CORNER);
  fill(0xff00DD00);
  rect(25, height - 50, 100 * ((float)player.health/(float)player.maxHealth), 25);
  fill(0xffFFFFFF);
  textSize(12);
  text("Health: " + (int) player.health + "/" + player.maxHealth, 75, height - 35);
  if (player.specialLevel >= 1) {
    text("Special cooldown : " + player.specialCooldown, 75, height - 70);
  }
}

public void boss1Func() {
  if (numEnemiesLeft <= 0 && level == 1) {//set up how many to kill
    bossOut = true;
  }
  if (bossOut) {
    boss1= true;
    fill(0xff00FF00);
    rectMode(CENTER);
    rect(width/2, 50, player.boss1Object.health * 0.9f, 50);
    fill(0);
    textSize(30);
    text(player.boss1Object.health, (width/2), 50);
  }
}

public void addEnemy1() {
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
public void addEnemy2() {
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
public void addEnemy3() {
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
public void addEnemy4() {
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
public void addEnemy5() {
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

public void enemyBullet() {
  for (int i  = enemyBullets.size() - 1; i > -1; i--) {
    boolean bulletExists = true;
    Bullet currentBullet = enemyBullets.get(i);
    currentBullet.update();
    if (pow(currentBullet.diameter / 2 + player.diameter / 2, 2) > pow(player.pos.x - currentBullet.X, 2) + pow(player.pos.y - currentBullet.Y, 2)) {
      if (currentBullet.damage * 0.66f - player.defense > 0 && difficulty == 0) {
        player.health -= (currentBullet.damage * 0.66f - player.defense);
      }
      if (currentBullet.damage - player.defense > 0 && difficulty == 1) {
        player.health -= (currentBullet.damage - player.defense);
      }
      if (currentBullet.damage * 1.5f - player.defense > 0 && difficulty == 2) {
        player.health -= (currentBullet.damage * 1.5f - player.defense);
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

public void playerBullet() {
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

public void helpDraw() {
  fill(0xff880000);
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

public void optionDraw() {
  fill(0xff000088);
  rect(width/2, height/2, 250, 300);
  easy.drawButton();
  medium.drawButton();
  hard.drawButton();
  btnOkay.drawButton();
}

public void upgradeDraw() {
  fill(0xff880000);
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



public void keyPressed() {
  if (key == 'z') {
    player.special();
  }
  if (key == 27) {
    key = 0;
    paused = true;
    btnResume = new Button(width/2, height/2 - 140, 200, 50, 0xff00A100, 0xff00FF00, "Resume");
    btnUpgrades = new Button(width/2, height/2 - 70, 200, 50, 0xff00A100, 0xff00FF00, "Upgrades");
    btnHelp = new Button(width/2, height/2, 200, 50, 0xff00A100, 0xff00FF00, "Help");
    btnOptions = new Button(width/2, height/2 + 70, 200, 50, 0xff00A100, 0xff00FF00, "Options");
    btnClose = new Button(width/2, height/2 + 140, 200, 50, 0xffA10000, 0xffFF0000, "Exit");
  }
}

public void mouseReleased() {
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
      btnOkay = new Button(width/2, height/2 + 150, 100, 25, 0xff00A100, 0xff00FF00, "Okay");
      return;
    }
    if (speedUpgrade.mousePressed() && speedCost <= coins) {
      player.speedHolder += 1;
      coins -= speedCost;
      speedCost = (int) (speedCost * 3.5f);
      speedUpgrade = new Button(width/2 - 115, height/2 - 175, 200, 50, 0xff00A100, 0xff00FF00, "Move Speed\n" + speedCost);
      return;
    }
    if (shootSpeedUpgrade.mousePressed() && shootSpeedCost <= coins) {
      player.attackDelay--;
      coins -= shootSpeedCost;
      shootSpeedCost = (int) (shootSpeedCost * 1.5f);
      shootSpeedUpgrade = new Button(width/2 - 115, height/2 - 105, 200, 50, 0xff00A100, 0xff00FF00, "Shooting Speed\n" + shootSpeedCost);
      return;
    }
    if (bulletSpeedUpgrade.mousePressed() && bulletSpeedCost <= coins) {
      player.bulletSpeed++;
      coins -= bulletSpeedCost;
      bulletSpeedCost = (int) (bulletSpeedCost * 1.5f);
      bulletSpeedUpgrade = new Button(width/2 - 115, height/2 - 35, 200, 50, 0xff00A100, 0xff00FF00, "Bullet Speed\n" + bulletSpeedCost);
      return;
    }
    if (bulletDamageUpgrade.mousePressed() && bulletDamageCost <= coins) {
      player.bulletDamage += 5;
      coins -= bulletDamageCost;
      bulletDamageCost = (int) (bulletDamageCost * 1.5f);
      bulletDamageUpgrade = new Button(width/2 - 115, height/2 + 35, 200, 50, 0xff00A100, 0xff00FF00, "Bullet Damage\n" + bulletDamageCost);
      return;
    }
    if (healthUpgrade.mousePressed() && healthCost <= coins) {
      player.health += 50;
      player.maxHealth += 50;
      coins -= healthCost;
      healthCost = (int) (healthCost * 1.5f);
      healthUpgrade = new Button(width/2 - 115, height/2 + 105, 200, 50, 0xff00A100, 0xff00FF00, "Health\n" + healthCost);
      return;
    }
    if (regenUpgrade.mousePressed() && regenCost <= coins) {
      player.regen += 1;
      coins -= regenCost;
      regenCost = (int) (regenCost * 1.5f);
      regenUpgrade = new Button(width/2 + 115, height/2 - 175, 200, 50, 0xff00A100, 0xff00FF00, "Life Regen\n" + regenCost);
      return;
    }
    if (defenseUpgrade.mousePressed() && defenseCost <= coins) {
      player.defense += 1;
      coins -= defenseCost;
      defenseCost = (int) (defenseCost * 1.5f);
      defenseUpgrade = new Button(width/2 + 115, height/2 - 105, 200, 50, 0xff00A100, 0xff00FF00, "Defense\n" + defenseCost);
      return;
    }
    if (spreadUpgrade.mousePressed() && spreadCost <= coins) {
      player.bulletSpread -= 2;
      coins -= spreadCost;
      spreadCost = (int) (spreadCost * 1.5f);
      spreadUpgrade = new Button(width/2 + 115, height/2 - 35, 200, 50, 0xff00A100, 0xff00FF00, "Bullet Spread\n" + spreadCost);
      return;
    }
    if (multishotUpgrade.mousePressed() && multishotCost <= coins) {
      player.numToShoot += 1;
      coins -= multishotCost;
      multishotCost = (int) (multishotCost * 3.5f); // Very strong, very expensive
      multishotUpgrade = new Button(width/2 + 115, height/2 + 35, 200, 50, 0xff00A100, 0xff00FF00, "Multishot\n" + multishotCost);
      return;
    }
    if (specialUpgrade.mousePressed() && specialCost <= coins && player.specialLevel < 3) {
      player.specialLevel += 1;
      coins -= specialCost;
      specialCost = (int) (specialCost * 6); // Very strong, very expensive
      specialUpgrade = new Button(width/2 + 115, height/2 + 105, 200, 50, 0xff00A100, 0xff00FF00, "Special Attack\n" + specialCost);
      if (player.specialLevel == 3) {
        specialCost = 1000000000;
        specialUpgrade = new Button(width/2 + 115, height/2 + 105, 200, 50, 0xff004400, 0xff004400, "Special Attack\nMAX");
      }
      return;
    }
  }
  if (!upgradesOpen && !helpOpen && !optionsOpen && paused) {
    if (btnResume.mousePressed()) {
      paused = false;
      btnOptions = new Button(75, height - 100, 100, 25, 0xff00A100, 0xff00FF00, "Options");
      btnHelp = new Button(75, height - 50, 100, 25, 0xff00A100, 0xff00FF00, "Help");
      btnClose = new Button(width - 75, height - 50, 100, 25, 0xffA10000, 0xffFF0000, "Exit");
      return;
    }
    if (btnUpgrades.mousePressed()) {
      upgradesOpen = true;
      speedUpgrade = new Button(width/2 - 115, height/2 - 175, 200, 50, 0xff00A100, 0xff00FF00, "Move Speed\n" + speedCost);
      shootSpeedUpgrade = new Button(width/2 - 115, height/2 - 105, 200, 50, 0xff00A100, 0xff00FF00, "Shooting Speed\n" + shootSpeedCost);
      bulletSpeedUpgrade = new Button(width/2 - 115, height/2 - 35, 200, 50, 0xff00A100, 0xff00FF00, "Bullet Speed\n" + bulletSpeedCost);
      bulletDamageUpgrade = new Button(width/2 - 115, height/2 + 35, 200, 50, 0xff00A100, 0xff00FF00, "Bullet Damage\n" + bulletDamageCost);
      healthUpgrade = new Button(width/2 - 115, height/2 + 105, 200, 50, 0xff00A100, 0xff00FF00, "Health\n" + healthCost);
      regenUpgrade = new Button(width/2 + 115, height/2 - 175, 200, 50, 0xff00A100, 0xff00FF00, "Life Regen\n" + regenCost);
      defenseUpgrade = new Button(width/2 + 115, height/2 - 105, 200, 50, 0xff00A100, 0xff00FF00, "Defense\n" + defenseCost);
      spreadUpgrade = new Button(width/2 + 115, height/2 - 35, 200, 50, 0xff00A100, 0xff00FF00, "Bullet Spread\n" + spreadCost);
      multishotUpgrade = new Button(width/2 + 115, height/2 + 35, 200, 50, 0xff00A100, 0xff00FF00, "Multishot\n" + multishotCost);
      specialUpgrade = new Button(width/2 + 115, height/2 + 105, 200, 50, 0xff00A100, 0xff00FF00, "Special Attack\n" + specialCost);
      if (player.specialLevel == 3) {
        specialCost = 1000000000;
        specialUpgrade = new Button(width/2 + 115, height/2 + 105, 200, 50, 0xff004400, 0xff004400, "Special Attack\nMAX");
      }
      btnOkay = new Button(width/2, height/2 + 175, 200, 50, 0xff00A100, 0xff00FF00, "Done");
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
      btnOkay = new Button(width/2, height/2 + 150, 100, 25, 0xff00A100, 0xff00FF00, "Okay");
      optionsOpen = false;
      return;
    }
    if(easy.mousePressed() && optionsOpen) {
      difficulty = 0;
      easy.c = 0xff008800;
      medium.c = 0xff880000;
      hard.c = 0xff880000;
      return;
    }
    if(medium.mousePressed() && optionsOpen) {
      difficulty = 1;
      easy.c = 0xff880000;
      medium.c = 0xff008800;
      hard.c = 0xff880000;
      return;
    }
    if(hard.mousePressed() && optionsOpen) {
      difficulty = 2;
      easy.c = 0xff880000;
      medium.c = 0xff880000;
      hard.c = 0xff008800;
      return;
    }
  }
}
class BossGrid {
  int numBombs, gridSize = 12, numGridTiles, numRemainingGridTiles;
  boolean firstClick;
  
  BossTile[][] tiles;
  
  public void createGrid() {
    firstClick = true;
    if(!gridInitialized) {
      boolean tempBool;
      tiles = new BossTile[gridSize][gridSize];
      for(int x = 0; x < gridSize; x++) {
        for(int y = 0; y < gridSize; y++) {
          int temp;
          temp = (int) random(10);
          if(temp == 1) {
            tempBool = true;
            numBombs++;
          }
          else {
            tempBool = false;
          }
          tiles[x][y] = new BossTile(tempBool, x, y, width/2 - 150 + 25 * x, height/2 - 150 + 25 * y);
        }
      }
      numGridTiles = (int) pow(gridSize, 2);
      numRemainingGridTiles = numGridTiles;
      for(int x = 0; x < gridSize; x++) {
        for(int y = 0; y < gridSize; y++) {
          if(y > 0) {
            if(x > 0) {
              tiles[x][y].adjacentTiles.add(tiles[x - 1][y - 1]);
              tiles[x][y].numAdjacentTiles++;
              if(tiles[x - 1][y - 1].bomb) {
                tiles[x][y].numAdjacentBombs++;
              }
            }
            if(x < gridSize - 1) {
              tiles[x][y].adjacentTiles.add(tiles[x + 1][y - 1]);
              tiles[x][y].numAdjacentTiles++;
              if(tiles[x + 1][y - 1].bomb) {
                tiles[x][y].numAdjacentBombs++;
              }
            }
            tiles[x][y].adjacentTiles.add(tiles[x][y - 1]);
              tiles[x][y].numAdjacentTiles++;
              if(tiles[x][y - 1].bomb) {
                tiles[x][y].numAdjacentBombs++;
              }
          }
          if(y < gridSize - 1) {
            if(x > 0) {
              tiles[x][y].adjacentTiles.add(tiles[x - 1][y + 1]);
              tiles[x][y].numAdjacentTiles++;
              if(tiles[x - 1][y + 1].bomb) {
                tiles[x][y].numAdjacentBombs++;
              }
            }
            if(x < gridSize - 1) {
              tiles[x][y].adjacentTiles.add(tiles[x + 1][y + 1]);
              tiles[x][y].numAdjacentTiles++;
              if(tiles[x + 1][y + 1].bomb) {
                tiles[x][y].numAdjacentBombs++;
              }
            }
            tiles[x][y].adjacentTiles.add(tiles[x][y + 1]);
              tiles[x][y].numAdjacentTiles++;
              if(tiles[x][y + 1].bomb) {
                tiles[x][y].numAdjacentBombs++;
              }
          }
          if(x > 0) {
            tiles[x][y].adjacentTiles.add(tiles[x - 1][y]);
            tiles[x][y].numAdjacentTiles++;
            if(tiles[x - 1][y].bomb) {
              tiles[x][y].numAdjacentBombs++;
            }
          }
          if(x < gridSize - 1) {
            tiles[x][y].adjacentTiles.add(tiles[x + 1][y]);
            tiles[x][y].numAdjacentTiles++;
            if(tiles[x + 1][y].bomb) {
              tiles[x][y].numAdjacentBombs++;
            }
          }
        }
      }
    }
  }
}
class BossTile {
  int x, y, xPos, yPos, numAdjacentBombs, numAdjacentTiles;
  boolean bomb, beenClicked;
  
  ArrayList<BossTile> adjacentTiles;
  Button thisButton;
  
  BossTile() {}
  
  BossTile(boolean b, int x, int y, int xp, int yp) {
    bomb = b;
    this.x = x;
    this.y = y;
    xPos = xp;
    yPos = yp;
    thisButton = new Button(xPos, yPos, 25, 25, 125, 200, "");
    thisButton.playSound = false;
    adjacentTiles = new ArrayList<BossTile>();
    beenClicked = false;
  }
  
  public void clicked() {
    grid.numRemainingGridTiles--;
    beenClicked = true;
    if(bomb) {
      if(grid.firstClick) {
        grid.firstClick = false;
        bomb = false;
        for(int i = 0; i < numAdjacentTiles; i++) {
          adjacentTiles.get(i).numAdjacentBombs--;
        }
        grid.numBombs--;
      }
      else {
        player.health -= 10;
        if(player.health <= 0) {
          gameOver = true;
        }
        gridInitialized = false;
        puzzleBossOut = false;
        grid.firstClick = true;
        grid.numBombs = 0;
        return;
      }
    }
    if(numAdjacentBombs == 0) {
      for(int i = 0; i < numAdjacentTiles; i++) {
        if(!adjacentTiles.get(i).beenClicked) {
          adjacentTiles.get(i).clicked();
        }
      }
    }
    if(grid.firstClick) {
      grid.firstClick = false;
    }
    thisButton.text = "" + numAdjacentBombs;
    if(numAdjacentBombs == 0) {
      thisButton.text = "";
    }
    thisButton.c = 60;
    thisButton.highlight = 60;
    if(grid.numRemainingGridTiles == grid.numBombs && level == 2) {
      puzzleBossDefeated = true;
      puzzleBossOut = false;
      giveCoins(2500);
      numEnemiesLeft = 40;
      level = 3;
      player.score +=10;
    }
  }
}
class Boss_1 extends Enemy {
  int shootExtender = 0;
  PImage img;
  Boss_1() {
    super(new PVector(width/2, height/2), 1000, 250, 0, 100, 250, 4, 10, 1000);
    img = loadImage("boss1.png");
  }
  public void update() {
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
  public void shoot() {
    enemyBullets.add(new Bullet(pos, new PVector(width/2, 0).sub(pos), bulletSpeed, attackDamage));
    enemyBullets.add(new Bullet(pos, new PVector(0, 0).sub(pos), bulletSpeed, attackDamage));
    enemyBullets.add(new Bullet(pos, new PVector(width, 0).sub(pos), bulletSpeed, attackDamage));
    enemyBullets.add(new Bullet(pos, new PVector(width, height/2).sub(pos), bulletSpeed, attackDamage));
    enemyBullets.add(new Bullet(pos, new PVector(width, height).sub(pos), bulletSpeed, attackDamage));
    enemyBullets.add(new Bullet(pos, new PVector(width/2, height).sub(pos), bulletSpeed, attackDamage));
    enemyBullets.add(new Bullet(pos, new PVector(0, height).sub(pos), bulletSpeed, attackDamage));
    enemyBullets.add(new Bullet(pos, new PVector(0, height/2).sub(pos), bulletSpeed, attackDamage));
  }

  public void move() {
    PVector dir = PVector.sub(new PVector(targetX, targetY), pos);
    dir.normalize();  
  }

  public void die() {
    boss1 = false;
    bossOut = false;
    numEnemiesLeft = 40;
    level = 2;
  }
}
class Boss_2 extends Player_Fighter {
  boolean dead;
  int health;
  float targetX, targetY;
  int dirChangeCD;
  int c;
  PImage img;
  Boss_2(PVector p) {
    super(p);
    dead = false;
    health = width;
    targetX = random(200);
    targetY = random(200);
    dirChangeCD = 0;
    c=0xff000000;
    img = loadImage("ufo.png");
  }

  public void draw() {
    if (health> 0) {
      dirChangeCD--;
      if (random(300) > dirChangeCD) {
        targetX = (int) random(width);
        targetY = (int) random(height/2 - 200 ,height/2);
        dirChangeCD = 400;
      }
      moving();
      fill(c);
      image(img, pos.x, pos.y, diameter, diameter);
      c = 0xff000000;
    }
  }

  public void jump() {
    //jumping = true;
    //last = pos.y;
  }

  public void kick() {
  }

  public void die() {
    health-=random(60);
    c = 0xffFF0000;
  }

  public void moving() {
    PVector dir = PVector.sub(new PVector(targetX, targetY), pos);
    dir.normalize();
    pos.add(dir);
  }
}
class Boss_3 extends Boss_1{
  Boss_3(){
    super();
    img = loadImage("boss3.png");
  }
  public void shoot(){
    
  }
  public void die(){
    race = false;
    raceBossDefeated = true;
    giveCoins(4500);
    numEnemiesLeft = 40;
    level = 4;
  }
}
class Bullet {

  float X;
  float Y;
  float deltaX;
  float deltaY;
  int diameter = 4;
  int damage;
  int speedMult;
  int bulletSpread;

  Bullet(PVector p, PVector s, int sp, int d) {

    s.normalize();
    X = p.x;
    Y = p.y;
    deltaX = s.x * sp;
    deltaY = s.y * sp;
    damage = d;
  }

  public void update() {
    X += deltaX;
    Y += deltaY;
    ellipseMode(CENTER);
    fill(255);
    ellipse(X, Y, diameter, diameter);
  }
}
class Button {

  int x, y;
  int widthB, heightB;
  int c, highlight;
  boolean hover, hovering, playSound;
  String text;
  PImage img;

  Button(int inix, int iniy, int iniw, int inih, int inic, int inihover, String tin) {
    playSound = true;
    x = inix;
    y = iniy;
    widthB = iniw;
    heightB = inih;
    c = inic;
    highlight = inihover;
    text = tin;
    hover = false;
    img = loadImage("frame.png");
    noStroke();
  }
  public void update() {
    if (overButt(x, y, widthB, heightB) ) {
      hover = true;
    } else {
      hover = false;
    }
  }

  public void drawButton() {
    update();
    if (hover) {
      fill(highlight);
      if(!hovering){

        hovering = true;
      }
      
    } else {
      fill(c);
      hovering = false;
    }
    rectMode(CENTER);
    imageMode(CENTER);
    
    rect(x,y,widthB,heightB, widthB / 12);
    image(img, x, y, widthB + (widthB*0.25f), heightB+ (heightB*0.25f));
    fill(0);
    textAlign(CENTER);
    textSize(12);
    text(text, x, y);
  }

  public boolean overButt(int x, int y, int widthin, int heightin) {
    x = x - widthB/2;
    y = y - heightB/2;
    if ((mouseX >= x && mouseX <= x + widthin) && 
      mouseY >= y && mouseY <= y+heightin) {
      return true;
    } else {
      return false;
    }
  }

  public boolean mousePressed() {
    if (hover) {
      return true;
    }
    else{
      return false;
    }
  }
}
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
  
  public void Update() {
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
  
  public void move() {
    PVector dir = PVector.sub(new PVector(targetX, targetY), pos);
    dir.normalize();
    pos.add(dir);
  }
  
  public void shoot() {
    remainingCooldown = cooldown;
    enemyBullets.add(new Bullet(pos, new PVector(targetX - bulletSpread/2 + random(bulletSpread), targetY - bulletSpread/2 + random(bulletSpread)).sub(pos), bulletSpeed, attackDamage));
  }
}
class Enemy_Fighter extends Boss_2 {
  boolean movingL;
  PImage img;

  Enemy_Fighter(PVector p) {
    super(p);
    img = loadImage("spring.png");
  }

  public void draw() {
    if (!dead) {
      moving();
      fill(0xffFF0000);
      rect(pos.x, pos.y, diameter, diameter);
    }
    else if(playerF.score < 3){
     enemy = new Enemy_Fighter(new PVector(random(width), height/2)); 
    }
    else{
      
      image(img, pos.x, pos.y, diameter, diameter);
    }
  }

  public void jump() {
  }

  public void kick() {
  }

  public void die() {
    dead = true;
  }

  public void moving() {
    if (pos.x > width) {
      movingL = false;
    } else if (pos.x < 0) {
      movingL = true;
    }
    if (movingL) {
      pos.x+=random(5);
    } else {
      pos.x-=random(5);
    }
  }
}
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
  public void Update() {
    fill(0xff00FF00);
    rectMode(CENTER);
    rect(width/2, 50, player.finalBoss.health * 0.9f, 50);
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
  public void move() {
    PVector dir = PVector.sub(new PVector(targetX, targetY), pos);
    dir.normalize();
    dir.mult(2);
    pos.add(dir);
  }
  public void die() {
    playing = false;
    endOfRound = true;
  }
}
class Player_Fighter {
  PVector pos;
  boolean hit;
  float diameter;
  float jumpHeight;
  float gravity;
  boolean jumping, kicking;
  int counter;
  int cooldown;
  int kickDir;
  boolean keys[];
  int score;
  Boolean onFire;
  PImage img;

  Player_Fighter(PVector p) {
    pos = p;
    hit = false;
    diameter = 30;
    jumping = false;
    kicking = false;
    jumpHeight = 150;
    gravity =1;
    counter = (int) jumpHeight;
    cooldown = 0;
    kickDir = 7;
    keys = new boolean[2];
    onFire = false;
    img = loadImage("knight.png");
  }

  public void render() {
    jump();
    kick();
    moving();

    if (cooldown > 0) {
      cooldown--;
    }

    if (keys[0] || keys[1]) {
      if (keys[1]) {
        pos.x+=3;
        kickDir = 7;
      }
      if (keys[0]) {
        pos.x-=3;
        kickDir = -7;
      }
    }

    if (jumping) {

      if (kicking) {
        pos.x +=kickDir;
        gravity = 15;
        attacking(boss);
        attacking(enemy);
      }
      if (pos.y <= height/2) {
        pos.y -= 7 - gravity;
        gravity+=0.2f;
      } else {
        jumping = false; 
        gravity = 1;
        kicking = false;
      }
    } else if (!jumping) {
      if (!keys[0] || !keys[1]) {
        pos.y = height/2;
      }
      if (pos.x <= 0) {
        pos.x =0;
      }
      if (pos.x >= width - diameter) {
        pos.x = width - diameter;
      }
    }
    if (!go) {
      if (pos.y == height/2) {
        onFire = true;
      } else {
        onFire = false;
      }
    }
    if (onFire) {
      player.health -= 0.1f;
    }
  }

  public void draw() {
    keyReleased();
    render();

    if (!go) {
      fill(0xff00FF00);
      if (onFire){
       fill(0xffFF0000); 
      }
      rectMode(CENTER);
      rect(pos.x + diameter/2, pos.y - 10, player.health/3, 5);
    }


    rectMode(CORNER);
    fill(100);
    if (kicking) {
      ellipse(pos.x, pos.y, diameter, diameter);
    } else {
      if (kickDir < 0) {
        pushMatrix();
        scale(-1, 1);
        image(img, -pos.x - diameter, pos.y, diameter, diameter);
        popMatrix();
      } else {
        image(img, pos.x, pos.y, diameter, diameter);
      }
    }
  }

  public void keyReleased() {
    if (key=='a')
      keys[0]=false;
    if (key=='d')
      keys[1]=false;
  }


  public void jump() {
    if (keyPressed && key == ' ') {
      if (!jumping) {
        jumping = true;
      }
    }
  }

  public void attacking(Boss_2 o) {
    if ((pow(diameter / 2 + o.diameter / 2, 2) > pow(pos.x - o.pos.x, 2) + pow(pos.y - o.pos.y, 2))) {
      if (o.kicking) {
        if (pos.y > o.pos.y) {
          o.die();
          score++;
          pos.y -=20;
          kicking = false;
          gravity = 0;
        }
      } else {
        o.die();
        score++;
        pos.y -=20;
        kicking = false;
        gravity=0;
      }
    }
  }

  public void moving() {
    if (keyPressed && key == 'a') {
      if (!kicking) {
        keys[0]=true;
        keys[1]= false;
      }
    }
    if (keyPressed && key == 'd') {
      if (!kicking) {
        keys[1] = true;
        keys[0]= false;
      }
    }
  }

  public void kick() {
    if (keyPressed && key == 'k') {
      if (!kicking && jumping) {
        kicking = true;
      }
    }
  }
}
class PowerUps{
  char type;
  int x;
  int y;
  int diameter = 15;
  
  PowerUps(int X, int Y, char t) {
    type = t;
    x = X;
    y = Y;
  }
  
  public void draw() {
    fill(0xff00FF00, 150);
    ellipse(x, y, diameter, diameter);
  }
}
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

  public void render() {
    if (playing) {
      PVector mouse = new PVector(mouseX, mouseY);
      PVector dir = PVector.sub(mouse, pos);
      dir.normalize();
      dir.mult(0.5f); 
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
      dir.mult(0.5f); 
      acceleration = dir;

      velocity.add(acceleration);
      velocity.limit(topspeed);
      pos.add(velocity);
    }
  }

  public void drawPlayer() {
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

  public void boost() {
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

  public void special() {
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

  public void shoot() {
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
class Wall {
  int direction;
  int hole;
  float x, y;
  float speed;
  PImage img;
  Wall(int dir, int pos) {
    direction = dir;
    hole = pos;
    speed = 1;
    img = loadImage("lava.png");
    if(direction == 0){
      y = 0;
      x = 0;
    }
    else if(direction ==1){
      y = height;
      x = 0;
    }
    else if(direction ==2){
      y = 0;
      x = 0;
    }
    else{
      y = 0;
      x = width;
    }
  }
  public void render(){
    if(direction <= 1){
     //println(y + "    " + player.pos.y + "    " + (y+30));
      if(player.pos.x > hole && player.pos.x < (hole +50) && player.pos.y > y&& player.pos.y < y + 30){
        giveCoins(10);
      }
      else if((player.pos.x < hole || player.pos.x > (hole +50)) && player.pos.y > y && player.pos.y < y + 30){
       player.health-=1; 
      }
    }
    else{
      if(player.pos.y > hole && player.pos.y < (hole +50) && player.pos.x > x && player.pos.x < x + 30){
        giveCoins(10);
      }
      else if((player.pos.y < hole || player.pos.y > (hole +50)) && player.pos.x > x && player.pos.x < x + 30){
       player.health-=1; 
      }
    }
  }

  public void draw() {
    imageMode(CORNER);
    if (direction == 0) {
      image(img, x, y, width - (width - hole), 30);
      image(img, x+ width - (width - hole) + 50, y, width, 30);
      y+=speed;
      if (y> height){
       hole = (int) random(width); 
       y = 0;
       speed = speed *1.05f;
       wall1 =(int)random(2);
      }
    } else if (direction == 1) {
      
      image(img, x, y, width - (width - hole), 30);
      image(img ,x+ width - (width - hole) + 50, y, width, 30);
      y-=speed;
      if (y< 0){
       hole = (int) random(width); 
       y = height;
       speed = speed *1.05f;
       wall1 =(int)random(2);
      }
    }
    else if (direction ==2){
      image(img, x, y, 30, height - (height - hole));
      image(img ,x, y + height - (height - hole) + 50, 30, height);
      x+=speed*1.5f;
      if(x> width){
        hole = (int) random(height); 
        x = 0;
        speed = speed *1.05f;
        wall2 =(int) random(2, 4);
      }
      
    }
    else{
      image(img ,x, y, 30, height - (height - hole));
      image(img, x, y + height - (height - hole) + 50, 30, height);
      x-=speed*1.5f;
      if(x< 0){
        hole = (int) random(height); 
        x = width;
        speed = speed *1.05f;
        wall2 = (int) random(2, 4);
      }
      
    }
    render();
    imageMode(CENTER);
  }
}
  public void settings() {  size(900, 600);  smooth(); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "--present", "--window-color=#666666", "--stop-color=#cccccc", "spaceGame" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
