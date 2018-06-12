package MainWindow;

import static MainWindow.Constants.boardSIZE;
import java.util.Random;

/**
 * Evil AI that will win at all costs. This class may or may not cheat. It 
 * cheats by placing two pieces when it is about to lose rather than one.
 */
public class evilAI implements AI {
  
  public GameBoard gb;
  public GameBoardController gbc;
  public GameModel gm;
  public PlayerController pc;
  public int num;
  public boolean flag = true;
  public int x;
  public int y;
  public boolean notwin = true;

  /**
   * Initializes gb, gbc, gm, and pc for the evil AI
   * @param gb instance of GameBoard
   * @param gbc instance of GameBoardController
   * @param gm instance of GameModel
   * @param pc instance of PlayerController
   */
  public evilAI(GameBoard gb, GameBoardController gbc, GameModel gm, PlayerController pc) {
    this.gb = gb;
    this.gbc = gbc;
    this.gm = gm;
    this.pc = pc;
  }

  /**
   * Puts an temp AI button at every single square to check if the AI will win the game.
   */
  void checkEveryButtonAI() {
    for (int i = 0; i < boardSIZE; i++) {
      for (int j = 0; j < boardSIZE; j++) {
        if (gm.boardArray[i][j] == 0) {
          gm.boardArray[i][j] = 1;

          if (gbc.checkWin()) {
            int num = (i * boardSIZE) + j;
            if(notwin){
            gbc.updateBoard(num + "");
            notwin = false;
            }
            flag = false;
            break;
          } else {
            gm.boardArray[i][j] = 0;
          }
        }
      }
    }
  }

  /**
   * Puts a temp player button at every single square to check if the AI will win the game 
   * then the AI will put its piece there.
   */
  void checkEveryButtonPlayer() {
    for (int i = 0; i < boardSIZE; i++) {
      for (int j = 0; j < boardSIZE; j++) {
        if (gm.boardArray[i][j] == 0) {
          gm.boardArray[i][j] = 2;

          if (gbc.checkWin()) {
            int num = (i * boardSIZE) + j;
            gbc.updateBoard(num + "");
            flag = false;
            //break;
          } else {
            gm.boardArray[i][j] = 0;
          }
        }
      }
    }
  }

  /**
   * Sets the next move for the AI
   * @param num number of the button that was clicked 
   */
  public void nextMove(int num) {
      
      if(!gbc.checkWin()){
    checkEveryButtonPlayer();
    checkEveryButtonAI();
      }

    if (flag) {
      Random rand = new Random();
      int randomNum = rand.nextInt(8) + 1;

      switch (randomNum) {
        case 1:
          gbc.updateBoard((num - 1) + ""); // left 1
          break;
        case 2:
          gbc.updateBoard((num - 31) + ""); // left 1  up 1
          break;
        case 3:
          gbc.updateBoard((num - 30) + ""); // up 1
          break;
        case 4:
          gbc.updateBoard((num - 29) + ""); // right 1 up 1
          break;
        case 5:
          gbc.updateBoard((num + 1) + ""); // right 1
          break;
        case 6:
          gbc.updateBoard((num + 31) + ""); // right 1 down 1
          break;
        case 7:
          gbc.updateBoard((num + 30) + ""); // down 1 
          break;
        case 8:
          gbc.updateBoard((num + 29) + ""); // down 1 left 1
          break;
      }
    }
    flag = true;

  }

}
