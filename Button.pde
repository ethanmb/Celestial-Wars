class Button {

  int x, y;
  int widthB, heightB;
  color c, highlight;
  boolean hover, hovering, playSound;
  String text;
  PImage img;

  Button(int inix, int iniy, int iniw, int inih, color inic, color inihover, String tin) {
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
  void update() {
    if (overButt(x, y, widthB, heightB) ) {
      hover = true;
    } else {
      hover = false;
    }
  }

  void drawButton() {
    update();
    if (hover) {
      fill(highlight);
      if(!hovering){
        if(playSound == true) {
          click.play();
        }
        hovering = true;
      }
      
    } else {
      fill(c);
      hovering = false;
    }
    rectMode(CENTER);
    imageMode(CENTER);
    
    rect(x,y,widthB,heightB, widthB / 12);
    image(img, x, y, widthB + (widthB*0.25), heightB+ (heightB*0.25));
    fill(0);
    textAlign(CENTER);
    textSize(12);
    text(text, x, y);
  }

  boolean overButt(int x, int y, int widthin, int heightin) {
    x = x - widthB/2;
    y = y - heightB/2;
    if ((mouseX >= x && mouseX <= x + widthin) && 
      mouseY >= y && mouseY <= y+heightin) {
      return true;
    } else {
      return false;
    }
  }

  boolean mousePressed() {
    if (hover) {
      return true;
    }
    else{
      return false;
    }
  }
}