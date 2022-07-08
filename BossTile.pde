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
  
  void clicked() {
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