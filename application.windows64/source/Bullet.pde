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

  void update() {
    X += deltaX;
    Y += deltaY;
    ellipseMode(CENTER);
    fill(255);
    ellipse(X, Y, diameter, diameter);
  }
}
