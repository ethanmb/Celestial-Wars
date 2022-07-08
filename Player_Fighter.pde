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

  void render() {
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
        gravity+=0.2;
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
      player.health -= 0.1;
    }
  }

  void draw() {
    keyReleased();
    render();

    if (!go) {
      fill(#00FF00);
      if (onFire){
       fill(#FF0000); 
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

  void keyReleased() {
    if (key=='a')
      keys[0]=false;
    if (key=='d')
      keys[1]=false;
  }


  void jump() {
    if (keyPressed && key == ' ') {
      if (!jumping) {
        jumping = true;
      }
    }
  }

  void attacking(Boss_2 o) {
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

  void moving() {
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

  void kick() {
    if (keyPressed && key == 'k') {
      if (!kicking && jumping) {
        kicking = true;
      }
    }
  }
}