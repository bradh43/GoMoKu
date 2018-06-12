package MainWindow;

/**
 * This class is the Controller for the Register GUI. It will change and alter
 * the GUI based off of the user's actions and choices.
 */
public class RegisterWindowController {

  private Client client;
  private MainWindow mw;
  private MasterController mc;
  private RegisterWindow rw;

  /**
   * Makes the register GUI
   * @param client user name and password
   */
  public RegisterWindowController(Client client) {
    this.rw = new RegisterWindow(this); // 
    this.client = client;
    client.rwc = this;

  }

  /**
   * Makes the register GUI when the user can text a username already.
   * @param userName name of the user 
   * @param client use to send messages to the server
   */
  RegisterWindowController(String userName, Client client) {
    this.rw = new RegisterWindow(userName, this);
    this.client = client;
    client.rwc = this;
  }

  /**
   *Sends register user name to server for validation.
   * @param info message to send to the server
   */
  void send(String info) {
    client.sendMessage(info);
  }

  /**
   * Closes the register window.
   */
  void close() {
    this.rw.setVisible(false);
  }

  /**
   * User to see what message was sent (testing)
   * @return String that your sending to the server
   */
  @Override
  public String toString() {
    return client.toString();
  }

  /**
   * Errors that the server has found from the login and register windows
   * @param error error that came back from the server
   */
  void error(String error) {
    rw.setError(error);
  }

  /**
   * Checks to see if the user name was taken
   */
  public void usernameTaken() {
    rw.setFlag();
  }

}
