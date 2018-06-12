package MainWindow;

import java.util.Random;

/**
 * The easy AI was built to be trivially easy. It places a piece in one of the 8
 * pieces that surrounds the piece the user just clicked. It implements the AI
 * interface and it's code is used in all the other AI's as well.
 * @author CashNCarry
 */
public class easyAI implements AI {

  public GameBoard gb;
  public GameBoardController gbc;
  public GameModel gm;
  public PlayerController pc;
  public int num;
  public int x;
  public int y;

  /**
   * Initializes gb, gbc, gm, and pc for the easy AI
   * @param gb instance of GameBoard
   * @param gbc instance of GameBoardController
   * @param gm instance of GameModel
   * @param pc instance of PlayerController
   */
  public easyAI(GameBoard gb, GameBoardController gbc, GameModel gm, PlayerController pc) {
    this.gb = gb;
    this.gbc = gbc;
    this.gm = gm;
    this.pc = pc;

  }

  /**
   * Sets the next move for the AI
   * @param num number of the button that was clicked 
   */
  public void nextMove(int num) {

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
}
