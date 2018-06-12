package MainWindow;
/**
 * This class controls the Lobby GUI and if created, will create the frame
 * and set the GUI to visible. It has full control of the GUi and will change
 * the GUI according to the user's inputs.
 */
import java.awt.Color;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;

/**
 * Opens a new lobby view.
 */
public class LobbyWindowController {

  public LobbyWindow lw; // lobby GUI
  private Client client; // Client username/password
  public MasterController mc; // controller
  public User user; //name of the user class

  /**
   * Make the lobby window
   * @param client Sends the messages to the server
   * @param mc instance of the masterController
   */
  LobbyWindowController(Client client, MasterController mc) {
    lw = new LobbyWindow(this);
    this.mc = mc;
    this.client = client;
    client.lwc = this;
    lw.updateOnlineList();
    lw.updateInviteList();
    user = mc.user;
  }

  /**
   * Sends the users name and password to the server
   * @param info username and password from the users
   *
   */
  public void send(String info) {

    client.sendMessage(info);
  }

  /**
   * Clears the list of its default values
   */
  void clearOnlineList() {
    lw.OnlinelistModel = new DefaultListModel();;
  }

  /**
   * Checks to see if a user is online
   */
  void online(String username) {
    if (!lw.OnlinelistModel.contains(username)) {
      lw.OnlinelistModel.addElement(username);
    }

  }

  /**
   * Updates the stats with a string input form the server
   */
  void updateStatsLabels(String stats) {
    lw.updateStatsLabels(stats);
  }

  /**
   * Adds a user to the invite list
   */
  void inviteRequest(String inviter) {
    lw.inviteList.addElement(inviter);
  }

  /**
   * Removes the sender of the invite list if he closes the app after sending
   * the invite
   */
  void close(String username) {
    lw.OnlinelistModel.removeElement(username);
    if (lw.inviteList.contains(username)) {
      lw.inviteList.removeElement(username);
    }
  }

  /**
   * Displays a message of denial and removes the invite
   */
  void declined(String decliner) {
    lw.messageLabel.setText(decliner + " declined your invite");
    lw.invite.remove(decliner);
  }

  /**
   * Assigns a random port number and checks to see if the post number is
   * available Sends a message to the second user with ip and name so they can
   * set the sever up.
   */
  void acceptButton(String name) {
    PlayerController thisP = new PlayerController(1, Color.RED);

    String ip = null;
    try {
      ip = InetAddress.getLocalHost().toString();
    } catch (UnknownHostException ex) {
      Logger.getLogger(LobbyWindow.class.getName()).log(Level.SEVERE, null, ex);
    }
    Random rn = new Random();
    int port = 0;
    boolean flag = false;
    while (!flag) {
      port = rn.nextInt(9999) + 1024;
      flag = availablePort(port);
    }
    ip = ip.substring(ip.indexOf('/') + 1);
    send("Accept \n" + name + "\n" + user.userName + "\n" + ip + "\n" + port);
    mc.openBoard(port, name, thisP);
    lw.setVisible(false);
  }

  /**
   * Checks to see if the port is available
   */
  private static boolean availablePort(int port) {
    try (Socket ignored = new Socket("localhost", port)) {
      return false;
    } catch (IOException ignored) {
      return true;
    }
  }
}
