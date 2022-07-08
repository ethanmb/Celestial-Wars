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
  
  void draw() {
    fill(#00FF00, 150);
    ellipse(x, y, diameter, diameter);
  }
}
