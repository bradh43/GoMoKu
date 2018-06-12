package MainWindow;

import java.awt.Color;

/**
 * This class stores the color and number of the player. The PlayerController
 * assigns these values to the player model.
 * @author CashNCarry
 */
public class PlayerModel {

  /**
   * The color the tile will be when clicked by the player.
   */
  public Color tileColor;

  /**
   * The number that will be stored in the gamemodel for the player
   */
  public int playerNumber;

  /**
   * The constructor for the player model. It is empty so that the playercontroller 
   * can assign the values.
   */
  public PlayerModel() {

  }

}
