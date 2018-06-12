package MainWindow;
/**
 * This class serves as the Server for the Peer vs Peer game board. It is created
 * by the user who accepts the game invite and the port number is randomly assigned
 * with a few restrictions.
 */
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * This class serves as the Server for the Peer vs Peer game board.
 */
public class HostServer implements Runnable {

  private Thread worker;
  private ServerSocket serversocket;
  private Socket s;
  private InputStream in;
  private OutputStream out;

    /**
     * Random port number for the game
     */
    public int port;

    /**
     * Port number of host computer
     */
    public HostConnection c;

    /**
     * Instance of host connection
     */
    public GameBoardController gbc;

    /**
     * Sets the HostServer's port number to port
     * @param port
     */
    public HostServer(int port) {
    this.port = port;
  }

  @Override
  public void run() {
    try {
      serversocket = new ServerSocket(port);
    } catch (IOException e) {
      System.out.println("Server Socket could not be created at the port");
    }
    while (true) {
      try {
        s = serversocket.accept();
      } catch (IOException e) {
        System.out.println("Server Socket could not accept");
      }
      try {
        in = s.getInputStream();
        out = s.getOutputStream();
        c = new HostConnection(s, this, in, out);
        //sc.setNum(cList.size());
        //sc.setConnectionList(cList);
      } catch (IOException e) {
        e.printStackTrace();
      } //creates a new Thread
      c.start();
    }
  }

  /**
   * Given method to listen for messages.
   */
  public void listen() {
    worker = new Thread(this);
    worker.start();
  }

}
