package MainWindow;

/**
 * Hold the users information
 */
public class User {
  
  public String userName; // name of the user
  public String password; // password of the user

  /**
   * Make the user and sets the user name and password to null
   */
  public User() {

  }

  /**
   * Sets the name and password
   * @param userName The name of the user
   * @param password the password of the user
   */
  public User(String userName, String password) {
    this.userName = userName;
    this.password = password;
  }

}
