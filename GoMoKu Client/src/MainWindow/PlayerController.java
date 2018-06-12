package MainWindow;

import java.awt.Color;

/**
 * This class is used to keep track of which player is which to assign color and
 * the number the player is in the Game Model.
 */
public class PlayerController {

  /**
   * The player madel
   */
  public PlayerModel pm = new PlayerModel();

  /**
   * Instance of a player
   * @param pNum the number of the player 1 or 2
   * @param c the color assigned to the player red or blue
   */
  public PlayerController(int pNum, Color c) {
    pm.playerNumber = pNum;
    pm.tileColor = c;
  }
}