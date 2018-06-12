package MainWindow;

/**
 * Works as the accepting class of a peer to peer game to connect the other
 * player to this user
 */ 

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.Socket;

/**
 * Creates a socket for a Peer vs Peer game
 */
public class GameClient implements Runnable {

  Thread worker;
  private String server;
  private int servPort;
  private byte[] buf;
  private HostServer serv;
  private InputStream in;
  private OutputStream out;
  private DataOutputStream dos;
  private Reader is;

    /**
     * The GameBoardController instance
     */
    public GameBoardController gbc;

    /**
     * The HostConnection instance
     */
    public HostConnection c;

    /**
     * The Sockets instance
     */
    public Socket s;

    /**
     * Method to connect the user to this client server
     * @param ip Server IP
     * @param port Server Port Number
     * @param serv instance of the host server
     */
    public GameClient(String ip, int port, HostServer serv) {
    this.serv = serv;
    server = ip;
    servPort = port;
    try {
      s = new Socket(server, servPort);
    } catch (IOException e) {
      while (s == null) {
        try {
          s = new Socket(server, servPort);
        } catch (IOException ex) {

        }
      }

    }
    //Input and Output Stream initialization
    try {
      in = s.getInputStream();
    } catch (IOException e) {
      e.printStackTrace();
    }
    try {
      out = s.getOutputStream();
    } catch (IOException e) {
      e.printStackTrace();
    }
    c = new HostConnection(s, serv, in, out);
    //InputStreamReader for the reading of messages in run()
    is = new InputStreamReader(in);
    dos = new DataOutputStream(out);
  }
    
  @Override
  public void run() {
    char[] buffer = new char[1024];
    StringBuilder out = new StringBuilder();
    while (true) {
      // read a message (blocks waiting for a message)
      try {
        int ret = is.read(buffer, 0, buffer.length);
        out.append(buffer, 0, ret);

        //put if statement based on returned info
        if (!out.toString().equals(null)) {
          gbc.updateBoard(out.toString());
          gbc.switchTurn();
        }
        out.setLength(0);
      } catch (IOException e) {

      }

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
