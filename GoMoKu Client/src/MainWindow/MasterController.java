package MainWindow;

import static MainWindow.Constants.serverIp;

/**
 * A controller to control all the controllers for Gomoku game play
 */
public class MasterController {

  public Constants constants = new Constants();
  public User user = new User();
  public GameModel gm;
  public LobbyWindowController lwc;
  public String opponent;
  public Client client = new Client(serverIp, 4444, this); // the clients connecting information
  public GameBoard gb;

  /**
   * Method to open the lobby window of Gomoku
   */
  public void openLobby() {
    lwc = new LobbyWindowController(client, this);
    lwc.lw.setTitle(lwc.user.userName);
  }

  /**
   * Method to open the register window of Gomoku
   */
  void openRegister() {
    RegisterWindowController rwc = new RegisterWindowController(client); // passed in client for read/write to a file of users

  }

  /**
   * Method to open the register window if the user has types in to the login
   * user name of Gomoku
   */
  void openRegister(String userName) {
    RegisterWindowController rwc = new RegisterWindowController(userName, client);
  }

  /**
   * Method to open the main window of Gomoku
   */
  void openMainWindow() {
    MainWindowController mwc = new MainWindowController(client, this);
  }

  /**
   * Opens the game board with you and the inviters name
   */
  void openGameBoard(String accepter, String ip, int port, PlayerController thisP) {
    this.opponent = accepter;
    HostServer server = new HostServer(port);
    GameClient gc = new GameClient(ip, port, server);
    GameBoardController gbc = new GameBoardController(gc, this, server);
    gbc.pc = thisP;
    this.gm = gbc.gm;
    gm.addPlayer(thisP);

    gbc.gb.frame.setTitle("You vs " + accepter);

    gbc.disableAll();
    gc.listen();
  }

  /**
   * If an invite is send then you make the server
   */
  void openBoard(int port, String opponent, PlayerController thisP) {
    this.opponent = opponent;
    HostServer server = new HostServer(port);
    GameBoardController gbc = new GameBoardController(server, this);
    gbc.pc = thisP;
    this.gm = gbc.gm;
    gm.addPlayer(thisP);

    gbc.gb.frame.setTitle("You vs " + opponent);

    gbc.enableAll();
    server.listen();
  }

  /**
   * Method used to put the player back into the lobby. If gameWin == 1, then the
   * winner tells the server who won and who lost. Else, the player just goes back
   * into the lobby.
   */
  void returnLobby(int gameWin) {
    lwc.lw.OnlinelistModel.clear();
    lwc.lw.inviteList.clear();
    if (gameWin == 1) {
      lwc.send("Return " + "\n" + user.userName + "\n" + gameWin + "\n" + opponent);
    } else {
      lwc.send("Back " + user.userName);
    }
    lwc.lw.setVisible(true);
  }

  /**
   * A method to try and only make the client if needed. Always for offline gameplay.
   */
  void connect() {
    client = new Client(serverIp, 4444, this); // the clients connecting information
  }

  /**
   * Method to start an AI game. The String it takes determines the difficulty.
   * Follow the format of string in the mainwindow dropDown String.
   */
  void AIGame(String AItype) {
    GameBoardController gbc = new GameBoardController(this, AItype);
    gbc.gb.turnLabel.setText("Your turn");
  }

}
