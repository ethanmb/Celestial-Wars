class Boss_3 extends Boss_1{
  Boss_3(){
    super();
    img = loadImage("boss3.png");
  }
  void shoot(){
    
  }
  void die(){
    race = false;
    raceBossDefeated = true;
    giveCoins(4500);
    numEnemiesLeft = 40;
    level = 4;
  }
}
