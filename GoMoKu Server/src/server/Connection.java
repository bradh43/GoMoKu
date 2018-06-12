package server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import static server.Constants.Invite;
import static server.Constants.accept;
import static server.Constants.back;
import static server.Constants.close;
import static server.Constants.closing;
import static server.Constants.computer;
import static server.Constants.decline;
import static server.Constants.exitString;
import static server.Constants.guest;
import static server.Constants.login;
import static server.Constants.play;
import static server.Constants.returnString;
import static server.Constants.statsRequest;

/**
 *The Connection class reads in information from the client and relays it to the
 * server. This class will read the first letter or word and call the correct 
 * method that is from server. There is a long list of potential messages that
 * it may receive from the client and it will call the server accordingly.
 * 
 * @author CashNCarry
 */
public class Connection extends Thread {

	// IO streams to the Client
	private InputStream in ;
	private OutputStream out ;
	private DataOutputStream dos;
	private Server server;
	private Reader is;

    /**
     * The socket that connects the client to connection
     */
    public Socket s;

    /**
     * Used to set a temp username
     */
    public String username;

	/**
	 * Creates a connection object.
	 * @param s - The socket the connection will use
	 * @param server - The server the connection will use
	 * @param in - The inputStream
	 * @param out - The output stream
	 */
	public Connection(Socket s, Server server, InputStream in, OutputStream out) {
		this.s = s;
		this.in = in;
		this.out = out;
		this.server = server;
		dos = new DataOutputStream(this.out);
		is = new InputStreamReader(this.in);
                this.server.addConnection(this);
	}
        
    /**
     * Sets the username variable within Connection
     * 
     * @param username - string of the username 
     */
    public void setUsername(String username){
            this.username = username;
        }

	// Thread One: sends a message to the Client
	// called by the Server
	/**
	 * Sends a message given a string.
	 * @param msg - String that contains the message that you will send to
         * Client.java
	 */
	public void sendMessage(String msg){
		byte [] buff ;
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

	// Thread Two: reads a message from the Client
	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		char[] buffer = new char[1024];
	    StringBuilder out = new StringBuilder();
		while(true) {
			// read a message (blocks waiting for a message)
			try {
				int ret = is.read(buffer, 0, buffer.length);
				out.append(buffer, 0, ret);
                                //Login request message
                                if(out.charAt(0) == login){
                                    String output = out.toString();
                                    Scanner scan = new Scanner(output);
                                    scan.nextLine();
                                    String ip = scan.nextLine();
                                    String userName = scan.nextLine();
                                    String password = scan.nextLine();
                                    server.login(userName, password, ip);
                                }
                                // Closing message
                                else if(out.charAt(0) == closing){
                                    server.close(out.toString().substring(out.indexOf(" ")+1));
                                }
                                // Invite message
                                else if(out.charAt(0) == Invite){
                                    server.invite(out.toString());
                                }
                                // Stats message
                                else if(out.charAt(0) == statsRequest)
                                {
                                    server.updateStatsLabels(out.toString());
                                }
                                // Decline message
                                else if(out.charAt(0) == decline){
                                    Scanner scan = new Scanner(out.toString());
                                    scan.nextLine();
                                    String declinee = scan.nextLine();
                                    String decliner = scan.nextLine();
                                    server.decline(declinee, decliner);
                                    
                                }
                                // Accept message
                                else if(out.charAt(0) == accept){
                                    Scanner scan = new Scanner(out.toString());
                                    scan.nextLine();
                                    String accepted = scan.nextLine();
                                    String accepter = scan.nextLine();
                                    String ip = scan.nextLine();
                                    int port = scan.nextInt();
                                    server.accept(accepted, accepter, ip, port);
                                }
                                //Guest Message
                                else if(out.charAt(0) == guest){
                                    Scanner scan = new Scanner(out.toString());
                                    scan.nextLine();
                                    String ip = scan.nextLine();
                                    String guest = scan.nextLine();
                                    server.guest(guest, ip);
                                }
                                //Play Game message to leave lobby and play a game
                                else if(out.charAt(0) == play){
                                    server.leaveLobby(out.toString().substring(out.indexOf(" ")+1));
                                }
                                //Return to Lobby from a game
                                else if(out.charAt(0) == returnString){
                                    Scanner scan = new Scanner(out.toString());
                                    scan.nextLine();
                                    String user = scan.nextLine();
                                    int gameWin = scan.nextInt();
                                    scan.nextLine();
                                    String opponent = scan.nextLine();
                                    server.returnLobby(user, gameWin, opponent);
                                   
                                }
                                //Losers method to go back to the lobby
                                else if(out.charAt(0) == back){
                                    server.back(out.toString().substring(out.indexOf(" ")+1));
                                }
                                //Leave Connection to go to Computer game
                                else if(out.charAt(0) == computer){
                                    server.computer(out.toString().substring(out.indexOf(" ")+1));
                                }
                                //Exit an AI Game and conenct again to the server
                                else if(out.charAt(0) == exitString){
                                   server.exit(out.toString().substring(out.indexOf(" ")+1));
                                }
                                // register message
                                else{
                                    server.register(out.toString().substring(out.toString().indexOf(" ")+1));
                                   
                                }
				out.setLength(0);

			} catch (Exception e) {
				//Catches the first nullpointer error due to no message being sent yet.
			}
	}
	}

    /**
     * Sends a message to a user that they are online
     * 
     * @param user : The user that is now online
     * @param num : The number of online users
     */
    void online(String user, int num) {
        this.sendMessage("online " + user + " ");
    }

    /**
     * Sends a list of the online users to each user who is in the online list
     * 
     * @param cList : The list with all the online users.
     */
    void UpdateOnline(ArrayList<Connection> cList) {
        int size = cList.size();
        String str = "online \n" + size + "\n";
        for(int i = 0; i < size; i++){
            str = str + cList.get(i).username + "\n";
        }
        this.sendMessage(str);
    }
    
    /**
     * Removes the offline users from the other users list.
     * 
     * @param cList : The list of users online
     * @param username : The username of the user going offline
     */
    void updateOffline(ArrayList<Connection> cList, String username) {
        String str = close + username;
        this.sendMessage(str);
    }
	

	}
