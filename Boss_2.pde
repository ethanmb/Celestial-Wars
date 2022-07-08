class Boss_2 extends Player_Fighter {
  boolean dead;
  int health;
  float targetX, targetY;
  int dirChangeCD;
  color c;
  PImage img;
  Boss_2(PVector p) {
    super(p);
    dead = false;
    health = width;
    targetX = random(200);
    targetY = random(200);
    dirChangeCD = 0;
    c=#000000;
    img = loadImage("ufo.png");
  }

  void draw() {
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
      c = #000000;
    }
  }

  void jump() {
    //jumping = true;
    //last = pos.y;
  }

  void kick() {
  }

  void die() {
    health-=random(60);
    c = #FF0000;
  }

  void moving() {
    PVector dir = PVector.sub(new PVector(targetX, targetY), pos);
    dir.normalize();
    pos.add(dir);
  }
}