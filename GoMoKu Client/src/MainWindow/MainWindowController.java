package MainWindow;

/**
 * This class is the controller for the MainWindow GUI. It controls and calls
 * the appropriate methods when buttons are clicked and change according
 * to user inputs.
 */

import static MainWindow.Constants.login;
import static MainWindow.Constants.onlineMatch;
import static MainWindow.Constants.serverIp;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The main controller of the MainWindow gui.
 */
public class MainWindowController {

  private MainWindow mw; // login GUI
  public Client client; // Client username/password
  public MasterController mc; // controller
  public User user;

  /**
   *
   * make the client connection with doit
   * @param client Client with username and password
   * @param mc Master Controller reference
   *
   */
  public MainWindowController(Client client, MasterController mc) {
    mw = new MainWindow(this);
    this.mc = mc;
    this.client = client;
    client.mwc = this;
    client.doIt();
    user = mc.user;
  }

  /**
   * Sends the users name and password to the server
   * @param info username and password from the users
   */
  public void send(String info) {
    client.sendMessage(info);
  }

  /**
   * closes the main window opens the lobby
   */
  void loginButton() {
    mw.setVisible(false);
    mc.openLobby();
  }

  /**
   * opens the register window
   * @param userName users enters name
   */
  void registerButton(String userName) {
    if (userName.equals("")) {
      mc.openRegister();
    } else {
      mc.openRegister(userName);
    }
  }

  /**
   * Errors that the server has found from the login and register windows
   * @param error error that came back from the server
   */
  void error(String error) {
    mw.setError(error);
  }

  /**
   * Depending on what the user selects from the drop down menu 
   * the login way with change
   */
  void gameType() {
    String selectedValue;
    selectedValue = mw.GameType.getSelectedItem().toString();
    if (selectedValue.equals(onlineMatch)) {
      mw.loginButton.setText(login);
      mw.passwordTF.setEditable(true);
      mw.userNameTF.setEditable(true);
    }
    if (selectedValue.equals("AI - Easy") || selectedValue.equals("AI - Hard") || selectedValue.equals("AI - Medium") || selectedValue.equals("AI - Evil")) {
      mw.loginButton.setText("Confirm");
      mw.dropDown = selectedValue;
    }
  }

  /**
   * Logs the user in by sending the ip, name, and password to the server of login  
   */
  void loginButtonListener() {
    if (mw.loginButton.getText().equals(login)) {
      try {
        String ip = InetAddress.getLocalHost().toString();
        ip = ip.substring(ip.indexOf('/'));
        send("login \n" + ip + "\n" + mw.userNameTF.getText() + "\n" + mw.passwordTF.getText());
        user.userName = mw.userNameTF.getText();
      } catch (UnknownHostException ex) {
        Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
      }
    } else {
      mw.setVisible(false);
      String ip = null;
      try {
        ip = InetAddress.getLocalHost().toString();
      } catch (UnknownHostException ex) {
        Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
      }
      if (serverIp.equals("localhost")) {
        ip = "/127.0.0.1";
      }
      ip = ip.substring(ip.indexOf('/'));
      send("Z " + ip);
      mc.AIGame(mw.dropDown);
    }
  }

}
