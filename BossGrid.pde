class BossGrid {
  int numBombs, gridSize = 12, numGridTiles, numRemainingGridTiles;
  boolean firstClick;
  
  BossTile[][] tiles;
  
  void createGrid() {
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