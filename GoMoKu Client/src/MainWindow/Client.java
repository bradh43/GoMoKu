package MainWindow;
/**
 * Client is a class that creates a connection with the connection class that
 * is used in the server. It does all the communicating with the server for the
 * client. It both receives messages and sends messages and will delegate to
 * other classes when it receives a message.
 */
import java.awt.Color;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.Socket;
import java.util.Scanner;
import static MainWindow.Constants.correctInfo;
import static MainWindow.Constants.usernameTaken;
import static MainWindow.Constants.wrongPassword;
import static MainWindow.Constants.usernameIsNotRegistered;
import static MainWindow.Constants.alreadyLoggedIn;
import static MainWindow.Constants.goodToLogin;
import static MainWindow.Constants.stats;
import static MainWindow.Constants.onlineInfo;
import static MainWindow.Constants.inviter;
import static MainWindow.Constants.closeMessage;
import static MainWindow.Constants.decline;
import static MainWindow.Constants.accept;
import java.util.concurrent.TimeUnit;







/**
 * Client is a class that creates the network framework for the messaging application.
 * It creates threads and sends/receives messages. 
 * The constructor needs an ip number as a string and a port number as an int. 
 * Also, to connect the gui from the GUI.java use the method addGUI().
 * 
 * 
 * @author Isaiah Scheel
 */
public class Client implements Runnable {
	Thread worker;
	private String server;
	private int servPort;
	private byte[] buf;
	public Socket s;
	private InputStream in;
	private OutputStream out;
	private DataOutputStream dos;
	private Reader is;
        public MainWindowController mwc;
        public RegisterWindowController rwc;
        public LobbyWindowController lwc;
        public MasterController mc;
        public User user;

	/**
	 * This is the constructor for the client class. 
         * It takes a String that is the ip address in the form of 
         * "xxx.xxx.xxx.xxx" and an int that is the port number. 
         * It creates a socket, s , with the two parameters and then 
         * creates a few streams that can be used to read and write messages.
	 * @param ip - The ip address of the server you are connecting to
	 * @param port - The port number of the server
	 * 
	 */
	public Client(String ip, int port, MasterController mc){
		//Socket Initializing 
                this.mc = mc;
                this.user = mc.user;
		server = ip;
		servPort = port;
		try {
			s = new Socket(server, servPort);
		} catch (IOException e) {
			e.printStackTrace();
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
		//InputStreamReader for the reading of messages in run()
		is = new InputStreamReader(in);
		dos = new DataOutputStream(out);
                

	}


	/**
	 * This method gets messages from the server in a manner that doesn't 
         * need a new line character.
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		char[] buffer = new char[1024];
		StringBuilder out = new StringBuilder();
		while(true) {
			// read a message (blocks waiting for a message)
			try {    
				int ret = is.read(buffer, 0, buffer.length);
				out.append(buffer, 0, ret);
                                System.out.println(mc.user.userName + " : " + out.toString());
                                /* list of messages that the server sends to the client.
                                Depending on the message, a different action is performed.
                                */
                                //Correct info message to allow the user to log in
                                if(out.toString().equals(correctInfo))
                                {              
                                    mwc.loginButton();
                                    
                                }
                                //Error Message stating that the user name is taken
                                else if(out.toString().equals(usernameTaken)){
                                    mwc.error("Username taken, Please register again.");
                                    rwc.usernameTaken();
                                }
                                //Error Message stating that they put in the wrong password
                                else if(out.toString().equals(wrongPassword))
                                {
                                    mwc.error("Wrong Password");
                                }
                                //Error Message stating that the username is not registered
                                else if(out.toString().equals(usernameIsNotRegistered))
                                {
                                    mwc.error("Username is not registered");
                                }
                                else if(out.toString().equals(alreadyLoggedIn)){
                                    mwc.error("User already logged in");
                                }
                                //Resets the error label after a good register
                                else if(out.toString().equals(goodToLogin)){
                                    mwc.error("");
                                }
                                //Information for the Stats
                                else if(out.toString().charAt(0) == stats)
                                {
                                    lwc.updateStatsLabels(out.toString());
                                }
                                //Online information when someone logs in
                                else if(out.toString().charAt(0) == onlineInfo){
                                    
                                    Scanner scan = new Scanner(out.toString());
                                    scan.nextLine();
                                    int num = scan.nextInt();
                                    scan.nextLine();
                                    for (int i = 0; i < num; i++) {
                                        String username = scan.nextLine();
                                        if (!username.equals("null")) {
                                            if(lwc!=null){
                                            lwc.online(username);
                                            }
                                            else {
                                                //Will sleep for 500 milliseconds 
                                                //to see if the lobby window controller is actually 
                                                //null or if it just hasn't been created yet
                                                try {
                                                    TimeUnit.MILLISECONDS.sleep(500);
                                                    if(lwc != null){
                                                        lwc.online(username);
                                                    }
                                                } catch (InterruptedException ex) {
                                                    
                                                }
                                            }
                                        }
                                    }
                                }
                                //Invite request message
                                else if(out.toString().charAt(0) == inviter){
                                    String inviter;
                                    inviter = out.toString().substring(out.indexOf(" ")+1);
                                    lwc.inviteRequest(inviter);
                                }
                                //Closing messsage. Will receive one for each person who signs out
                                else if(out.toString().substring(0, out.toString().indexOf(" ")).equals(closeMessage)){
                                    lwc.close(out.toString().substring(out.indexOf(" ")+1));
                                }
                                //Decline message saying someone has declined your invite
                                else if(out.toString().charAt(0) == decline){
                                    lwc.declined(out.toString().substring(out.indexOf(" ")+1));
                                }
                                //Accept message to tell the user that someone has accepted their invite
                                else if(out.toString().charAt(0) == accept){
                                    PlayerController thisP = new PlayerController(2,Color.BLUE);
                                    Scanner scan = new Scanner(out.toString());
                                    scan.nextLine();
                                    String accepter = scan.nextLine();
                                    String ip = scan.nextLine();
                                    int port = Integer.parseInt(scan.nextLine());
                                    mc.openGameBoard(accepter, ip, port, thisP);
                                    sendMessage("PlayGame " + user.userName);
                                    lwc.lw.setVisible(false);
                                    scan.close();
                                }
				out.setLength(0);
			} catch (IOException e) {
				System.exit(0);
			}
		}
	}

	/**
	 * This method is called in main and creates a new thread and calls run().
	 */
	public void doIt() {
		worker = new Thread( this ) ;
		// calls run() in the new Thread
		worker.start() ;
	}


	/**
	 * This method is used to send the user's message to the server by getting 
         * the bytes and using a DataOutputStream (dos) to write to it
	 * @param msg - The message from the user. Use this method in GUI.java 
         * in it's action listener for the textfield so when the user hits 'enter' 
         * the message will be sent
	 * @throws IOException
	 */
	public void sendMessage(String msg){
		// send the message to the Server
		buf = msg.getBytes();
		try {
			dos.write(buf, 0, msg.length());
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			dos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}


