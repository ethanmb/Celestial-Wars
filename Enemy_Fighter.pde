class Enemy_Fighter extends Boss_2 {
  boolean movingL;
  PImage img;

  Enemy_Fighter(PVector p) {
    super(p);
    img = loadImage("spring.png");
  }

  void draw() {
    if (!dead) {
      moving();
      fill(#FF0000);
      rect(pos.x, pos.y, diameter, diameter);
    }
    else if(playerF.score < 3){
     enemy = new Enemy_Fighter(new PVector(random(width), height/2)); 
    }
    else{
      
      image(img, pos.x, pos.y, diameter, diameter);
    }
  }

  void jump() {
  }

  void kick() {
  }

  void die() {
    dead = true;
  }

  void moving() {
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