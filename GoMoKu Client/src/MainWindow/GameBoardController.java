package MainWindow;

/**
 * This class is to handles the events that get executed including 
 * sending message between the two players as well as the AI, switching turns
 * between player to player as well as the AI, checking for the winner, if the 
 * move is on the board, and enabling and disabling the board. 
 * 
 */

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import javax.swing.Timer;
import static MainWindow.Constants.yourTurnLabel;
import static MainWindow.Constants.oponentsLabel;
import static MainWindow.Constants.oponentsTime;
import static MainWindow.Constants.yourTime;
import static MainWindow.Constants.minOutOfBoundsOfBoard;
import static MainWindow.Constants.maxOutOfBoundsOfBoard;
import static MainWindow.Constants.boardSIZE;


/**
 * This class controls the events that as to happen on the game board.
 */
public class GameBoardController {

    /**
     * Instance of the GameBoard
     */
    public GameBoard gb;

    /**
     * Instance of the MasterController
     */
    public MasterController mc;

    /**
     * Instance of the GameClient 
     */
    public GameClient gc = null;

    /**
     * Instance of the HostServer
     */
    public HostServer hs;

    /**
     * Instance of the GameModel
     */
    public GameModel gm = new GameModel();

    /**
     * Instance of the PlayerController
     */
    public PlayerController pc;

    /**
     * Last move made by the player
     */
    public int lastmove;

    /**
     * String depicting the AI difficulty
     */
    public String AItype;

  /**
   * GameBoardController Constructor associated with the accepter of an invite ( Server in Peer to Peer)
   * @param hs this is the server a player connects to when they are in the game
   * @param mc used to send the results of the game
   */
  public GameBoardController(HostServer hs, MasterController mc) {
    gb = new GameBoard(this);
    gb.turnLabel.setText(yourTurnLabel);
    this.hs = hs;
    this.hs.gbc = this;
    this.mc = mc;
  }

  /**
   * Second GameBoard Constructor associated with the sender of the Invite ( Client in Peer to Peer)
   * @param gc Works the accepting class of a peer to peer game
   * @param hs this is the server a player connects to when they are in the game
   * @param mc used to send the results of the game
   */
  public GameBoardController(GameClient gc, MasterController mc, HostServer hs) {
    gb = new GameBoard(this);
    this.mc = mc;
    this.gc = gc;
    this.hs = hs;
    this.gc.gbc = this;
  }
    /**
     * CameBoardController Constructor for the AI
     * @param mc used to send the results of the game
     * @param AItype (Easy, Medium, Hard) user picks one to play
     */

  public GameBoardController(MasterController mc, String AItype) {
    this.AItype = AItype;
    this.mc = mc;
    PlayerController thisP = new PlayerController(2, Color.BLUE);
    this.pc = thisP;
    gb = new GameBoard(this, 1, thisP, AItype);
  }

  

    /**
     * take message from other player and updateThe GameBoard at that location
     * @param info String containing the board location played by the other player
     * @return int 1 or 0 used as a boolean to depict if the board had been updated 
     */
  public int updateBoard(String info) {

    if (!(this.gc == null)) {
      restartClock();
    }

    int number = Integer.parseInt(info);
    if (!(number < minOutOfBoundsOfBoard || number > maxOutOfBoundsOfBoard)) {
      if (gb.button[number].getBackground().equals(Color.BLACK)) {
        if (this.pc.pm.tileColor == Color.RED) {
          gb.button[number].setBackground(Color.BLUE);
        } else {
          gb.button[number].setBackground(Color.RED);
        }
      } else {
        gb.ai.nextMove(lastmove);
        return 0;
      }
    } else {
      gb.ai.nextMove(lastmove);
      return 0;
    }

    int x;
    int y;
    int num = number + 1;
    if (num == 0) {
      x = 1;
      y = 1;
    } else {
      if (num < boardSIZE) {
        y = 1;
      } else {
        if (((double) num / boardSIZE) > Math.floorDiv(num, boardSIZE)) {
          y = Math.floorDiv(num, boardSIZE) + 1;
        } else {
          y = Math.floorDiv(num, boardSIZE);
        }
      }

      if ((num % boardSIZE) == 0) {
        x = boardSIZE;
      } else {
        x = (num % boardSIZE);
      }
    }
    if (pc.pm.playerNumber == 1) {
      gm.boardArray[y - 1][x - 1] = 2;
    } else {
      gm.boardArray[y - 1][x - 1] = 1;
    }

    gm.makeMove(x, y, pc);

    enableAll();
    disablePressed();

    if (pc.pm.playerNumber == 2) {
      gb.text_area.append("RED Player:   Row " + x + "  Col " + y + "\n");
    } else {
      gb.text_area.append("BLUE Player:   Row" + x + " Col " + y + "\n");
    }

    if (checkWin() == true) {
      disableAll();
      gb.lobby.setVisible(true);
      if (gb.aiFlag == 1) {
        gb.lobby.setText("Exit");
      }
      if (pc.pm.playerNumber == 2) {
        gb.text_area.append("RED Player Wins, GAME OVER");
      } else {
        gb.text_area.append("BLUE Player Wins, GAME OVER");
      }
    }
    return 1;
  }
    /**
     * Switches the turn label to either Your Turn or Opposite turn.
     */
  public void switchTurn() {
    if (gb.turnLabel.getText().equals(oponentsLabel)) {
      gb.turnLabel.setText(yourTurnLabel);
    } else {
      gb.turnLabel.setText(oponentsLabel);
    }
  }

  

    /**
     * Disables the buttons on the board that have already been pressed
     */
  public void disablePressed() {

    for (int i = 0; i < gm.boardSize; i++) {
      for (int j = 0; j < gm.boardSize; j++) {
        if (gm.boardArray[j][i] == 1 || gm.boardArray[j][i] == 2) {
          gb.button[((30 * j) + i)].setEnabled(false);
        }
      }
    }
  }

  

    /**
     * Disables all of the buttons on the board
     */
  public void disableAll() {
    for (int i = 0; i < (gm.boardSize * gm.boardSize); i++) {
      gb.button[i].setEnabled(false);
    }
  }
    /**
     * //Enables all of the buttons on the board
     */
  public void enableAll() {
    for (int i = 0; i < (gm.boardSize * gm.boardSize); i++) {
      gb.button[i].setEnabled(true);
    }
  }
    /**
     * check to see if GameBoard has a winner
     * @return True if winner, False if no winner.
     */
  public boolean checkWin() {
    if (checkDiagonals() == true || checkRows() == true || checkCol() == true) {
      return true;
    } else {
      return false;
    }

  }
  

    /**
     * check if their is 5 in a row in the diagonals
     * @return true if above statement is true
     */
    public boolean checkDiagonals() {
    int count = 0;
    int temp = 0;
    boolean match = false;
    for (int col = 0; col < this.gm.boardSize; col++) {
      for (int row = 0; row < this.gm.boardSize; row++) {
        match = true;
        if (count == 0) {
          if (this.gm.boardArray[row][col] != 0) {
            temp = this.gm.boardArray[row][col];
          } else {
            temp = -99;
          }
        }
        for (int i = 0; i < 5; i++) {

          if (row + i < 30 && col + i < 30) {
            if (temp != this.gm.boardArray[row + i][col + i]) {
              match = false;
            }
          } else {
            match = false;
          }

        }
        if (match) {
          return true;
        }
      }
    }
    for (int col = this.gm.boardSize - 1; col > 0; col--) {
      for (int row = 0; row < this.gm.boardSize; row++) {
        match = true;
        if (count == 0) {
          if (this.gm.boardArray[row][col] != 0) {
            temp = this.gm.boardArray[row][col];
          } else {
            temp = -99;
          }
        }
        for (int i = 0; i < 5; i++) {

          if (row + i < 30 && col - i > 0) {
            if (temp != this.gm.boardArray[row + i][col - i]) {
              match = false;
            }
          } else {
            match = false;
          }

        }
        if (match) {
          return true;
        }
      }
    }

    return match;
  }

    /**
     * Function to check if a location is on the GameBoard
     * @param i column location on the board
     * @param j row location on the board
     * @return true if the specified location exists on the board
     */
    public boolean onBoard(int i, int j) {
    if (i > this.gm.boardSize || j > this.gm.boardSize) {
      return false;
    }
    return true;
  }

    /**
     * @return true if their is 5 in a row on a row
     */
    public boolean checkRows() {
    int count = 0;
    int j = 0;
    int i = 0;
    int temp = 0;
    //boardSize is final and set to 30
    while (j < this.gm.boardSize) {
      while (i < this.gm.boardSize) {
        //temporary to keep track of the player that win is being checked on
        if (count == 0) {
          if (this.gm.boardArray[i][j] != 0) {
            temp = this.gm.boardArray[i][j];
          } else {
            temp = -99;
          }
        }
        if(this.gm.boardArray[i][j] - temp == 0){
                    count++;
                }
                else{
                    count = 0;
                    //System.out.println("COUNT IS NOW 0 MOTHER FUCKER");
                    if(this.gm.boardArray[i][j] != 0){
                        temp = this.gm.boardArray[i][j];
                        count++;
                    }
                    
                }

            if (count == 5) {
            return true;
            }
            i++;
        }
        j++;
        i = 0;
        }
        return false;
    }

    /**
     * Checks to see if the AI or the player has won with 5 in a row in the columns
     * @return true if their is 5 in a row in the columns
     */
    public boolean checkCol() {
    int count = 0;
    int j = 0;
    int i = 0;
    int temp = 0;
    //boardSize is final and set to 30
    while (i < this.gm.boardSize) {
      while (j < this.gm.boardSize) {
        //temporary to keep track of the player that win is being checked on
        if (count == 0) {
          if (this.gm.boardArray[i][j] != 0) {
            temp = this.gm.boardArray[i][j];
          } else {
            temp = -99;
          }
        }
        if(this.gm.boardArray[i][j] - temp == 0){
                    count++;
                }
                else{
                    count = 0;
                    if(this.gm.boardArray[i][j] != 0){
                        temp = this.gm.boardArray[i][j];
                        count++;
                    }
                    
                }

        if (count == 5) {
          return true;
        }
        j++;
      }
      i++;
      j = 0;
    }
    return false;

  }

    /**
     * Changing the turns in a game
     * @return the integer that represents who's turn it is.
     */
  public int checkTurn() {
    return gb.turn;
  }

  /**
   * send the location on the board that needs to be updated
   * @param info the number of the button that was pressed
   */
    void sendBoard(String info) {
        if (this.pc.pm.playerNumber == 1) {
        try {
            hs.c.sendMessage(info);
        } catch (NullPointerException e) {

        }
        } else {
        gc.c.sendMessage(info);
        }
     }

    /**
     * re-starts the clock timer
     */
    public void restartClock() {
    gb.timer.stop();
    ActionListener clockListener = new ActionListener() {
      long x = gb.ONE_MINUTE - 1000;

      public void actionPerformed(ActionEvent ae) {
        gb.clock.setText(gb.sdf.format(new Date(x)));
        x -= 1000;
        if (x < 0) {
          timesOut();
        }

      }
    };
    gb.timer = new Timer(1000, clockListener);
    gb.timer.start();
  }
//Return back to the lobby, calls to MasterController to do the work.
    
  /**
   * returns the player to the lobby when the game is over
   */ 
  void lobby() {
    mc.returnLobby(gb.winner);
  }

  /**
   * Make user lose when the run out of time.
   */  
   void timesOut() {
    gb.timer.stop();
    disableAll();
    gb.lobby.setVisible(true);
    if (gb.turnLabel.getText().charAt(0) == 'O') {
      gb.text_area.append(oponentsTime);
    } else {
      gb.text_area.append(yourTime);
    }
  }
  
    /**
     * Return o a string representation of the GameBoard for testing purposes.
     */
    public void printBoard(){
    for (int i = 0; i < gm.boardSize;i++){
      for (int j = 0; j < gm.boardSize; j++){
          System.out.print(gm.boardArray[i][j]+ " ");
          
      }
      System.out.println("\n");
  }
}

}
