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
  void render(){
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

  void draw() {
    imageMode(CORNER);
    if (direction == 0) {
      image(img, x, y, width - (width - hole), 30);
      image(img, x+ width - (width - hole) + 50, y, width, 30);
      y+=speed;
      if (y> height){
       hole = (int) random(width); 
       y = 0;
       speed = speed *1.05;
       wall1 =(int)random(2);
      }
    } else if (direction == 1) {
      
      image(img, x, y, width - (width - hole), 30);
      image(img ,x+ width - (width - hole) + 50, y, width, 30);
      y-=speed;
      if (y< 0){
       hole = (int) random(width); 
       y = height;
       speed = speed *1.05;
       wall1 =(int)random(2);
      }
    }
    else if (direction ==2){
      image(img, x, y, 30, height - (height - hole));
      image(img ,x, y + height - (height - hole) + 50, 30, height);
      x+=speed*1.5;
      if(x> width){
        hole = (int) random(height); 
        x = 0;
        speed = speed *1.05;
        wall2 =(int) random(2, 4);
      }
      
    }
    else{
      image(img ,x, y, 30, height - (height - hole));
      image(img, x, y + height - (height - hole) + 50, 30, height);
      x-=speed*1.5;
      if(x< 0){
        hole = (int) random(height); 
        x = width;
        speed = speed *1.05;
        wall2 = (int) random(2, 4);
      }
      
    }
    render();
    imageMode(CENTER);
  }
}
