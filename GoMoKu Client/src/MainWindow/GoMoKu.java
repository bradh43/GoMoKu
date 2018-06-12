package MainWindow;

/**
 *
 * Start of Gomoku game. Creates the MasterController to control the other
 * contollers which will make instances of all the other frames needed for
 * the user to interact with the game.
 */
public class GoMoKu {

  public static void main(String[] args) {
    MasterController mc = new MasterController(); // new master controller
    mc.openMainWindow(); // open the main window contoller 
  }

}
