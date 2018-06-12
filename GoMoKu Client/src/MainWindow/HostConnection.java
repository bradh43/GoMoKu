package MainWindow;
/**
 * This class creates the connections portion of the Host server implementation.
 * The person who accepts the invite will create their own server for the
 * inviter to connect to and the server is broken into two parts, the server and
 * the connection. The connection is what makes the connection between the 
 * client and communicates with it and relays the information back to the 
 * server. The server in this case will be HostServer and the client will be
 * GameClient.
 */
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.Socket;

/**
 * This class is for the Peer to Peer read/send messages from the game
 */
public class HostConnection extends Thread {

  // IO streams to the Client
  private InputStream in;
  private OutputStream out;
  private DataOutputStream dos;
  private HostServer server;
  private Reader is;

    /**
     * The Socket instance
     */
    public Socket s;

    /**
     * Name of the user
     */
    public String username;

    /**
     * The GameBoardController instance
     */
    public GameBoardController gbc;

  /**
   * Creates a connection object.
   *
   * @param s - The socket the connection will use
   * @param server - The server the connection will use
   * @param in - The inputStream
   * @param out - The output stream
   */
  public HostConnection(Socket s, HostServer server, InputStream in, OutputStream out) {
    this.s = s;
    this.in = in;
    this.out = out;
    this.server = server;
    dos = new DataOutputStream(this.out);
    is = new InputStreamReader(this.in);
    this.gbc = server.gbc;
  }

  // Thread One: sends a message to the Client
  // called by the Server
  /**
   * Sends a message given a string.
   *
   * @param msg to be sent across the network
   */
  public void sendMessage(String msg) {
    byte[] buff;
    // send the message to the Client
    buff = msg.getBytes();
    try {
      dos.write(buff, 0, msg.length());
    } catch (IOException e) {
      e.printStackTrace();
    }
    try {
      dos.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  public void run() {
    char[] buffer = new char[1024];
    StringBuilder out = new StringBuilder();
    while (true) {
      // read a message (blocks waiting for a message)
      try {
        int ret = is.read(buffer, 0, buffer.length);
        out.append(buffer, 0, ret);

        if (!out.toString().equals(null)) {
          gbc.updateBoard(out.toString());
          gbc.switchTurn();
        }
        out.setLength(0);
      } catch (Exception e) {

      }

    }
  }
}
