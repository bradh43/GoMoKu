package MainWindow;

import java.lang.reflect.Array;

/**
 *
 * This class used to keep a model of the game board using a 2D array to 
 * keep track of all the game pieces on the board. This makes checking for a 
 * win more convenient and separates the model, view, and controller.
 * @author CashNCarry
 */
public class GameModel {
    public int boardSize = 30;
    public int[][] boardArray = new int[boardSize][boardSize];
  public PlayerController player1,player2;
  private int turn;
  private int totalMoves;
  private long startTime;
  private long player1Time, player2Time;

    /**
     * Blank GameModel
     */
    public GameModel() {

    }

    /**
     * Set the number of the player in the game
     * @param player and Instance of a player controller.
     */
    public void addPlayer(PlayerController player) {
    if (player.pm.playerNumber == 1) {
      this.player1 = player;
    } else {
      this.player2 = player;
    }
  }

    /**
     * This method puts the move that was made into the array lsit with the appropriate 
     * number, so 1 or 3.
     * @param col Column within the game board
     * @param row Row within the game board
     * @param player instance of the playerController to depict what character is to make a move
     */
    public void makeMove(int col, int row, PlayerController player) {
    if ((boardArray[row - 1][col - 1] == 0)) {
      if (player.pm.playerNumber == 1) {
        boardArray[row - 1][col - 1] = 1;
      } else {
        boardArray[row - 1][col - 1] = 2;
      }
    } else {
      return;
    }

  }

}

