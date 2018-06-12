package server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import static server.Constants.acceptedString;
import static server.Constants.alreadyLogged;
import static server.Constants.correctInfo;
import static server.Constants.declined;
import static server.Constants.goodToLogin;
import static server.Constants.invite;
import static server.Constants.localHostIP;
import static server.Constants.noUsername;
import static server.Constants.nullString;
import static server.Constants.portError;
import static server.Constants.socketError;
import static server.Constants.stats;
import static server.Constants.userInfoCopy;
import static server.Constants.userInfotxt;
import static server.Constants.usernameTaken;
import static server.Constants.wrongPassword;

/**
 * Server class for the Gomoku application. It creates the serversocket to allow
 * the client to connect to. It will also tell it's connection (connection.java) to 
 * send information back according to what it reads in. This server most be running
 * to be able to run the GoMoKu Application.
 */ 

class Server implements Runnable{
    private Thread worker;
    private ServerSocket serversocket;
    private Socket s;
    private Connection c;
    private InputStream in ;
    private OutputStream out ;
    private ServerController sc;
    ArrayList<Connection> cList = new ArrayList<Connection>();
    ArrayList<Connection> inGame = new ArrayList<Connection>();
    private File userInfo = new File(userInfotxt);

	//Thread One: sends messages to all Connection objects

	//Thread Two: accepts connections and creates Connection object
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		try {
			serversocket = new ServerSocket(4444);
		} catch (IOException e) {
			System.out.println(portError);
		}
		while(true) {
			try {
				s = serversocket.accept() ;
			} catch (IOException e) {
				System.out.println(socketError);
			}
			try {
				in = s.getInputStream() ;
				out = s.getOutputStream() ;
				c = new Connection(s, this, in, out);
                                sc.setNum(cList.size());
				sc.setConnectionList(cList);
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
		worker = new Thread( this) ;
		worker.start() ;
	}
        
        public void login(String username, String password, String ip){
           for(int i = 0; i < cList.size(); i++){
               String cListip = cList.get(i).s.getInetAddress().toString();
               
               //If you are the server, change the ip to your real ip instead of localhost
               if(cListip.equals(localHostIP)){
                   try {
                       cListip = InetAddress.getLocalHost().toString();
                   } catch (UnknownHostException ex) {
                       Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                   }
                   cListip = cListip.substring(cListip.indexOf("/"));
               }
               /////////////////////////////////////////////////
               
               if(cListip.equals(ip)){
                   c = cList.get(i);
               }
           }
           File userFile = new File(userInfotxt);
           Scanner scan = null;
        try {
            scan = new Scanner(userFile);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
           int flag = 0;
           int already_logged_in = 0;
           for(int i = 0; i < cList.size(); i++){
              if(cList.get(i).username == null){
                  
              }
              else if(cList.get(i).username.equals(username)){
                already_logged_in = 1;
                       }
                   }
           String dataPassword = null;

           
           scan.nextLine();
           String temp = null;
           String user = null;
           while(scan.hasNext()){
                temp = scan.next();
               if(temp.equals(username))
               {
                   dataPassword = scan.next();
                   flag = 1;
                   user = temp;
                   
               }
           }
           if(flag == 0){
              c.sendMessage(noUsername);
           }
           else{
               if(password.equals(dataPassword)){
                   if(already_logged_in == 0){
                    c.sendMessage(correctInfo);
                    c.setUsername(user);
                    sc.setConnectionList(cList);
                   for(int i = 0; i < cList.size(); i++){
                       cList.get(i).UpdateOnline(cList);
                   }
               }
                   else{
                       c.sendMessage(alreadyLogged);
                   }
               }
               else{
                   c.sendMessage(wrongPassword);
               }
           }
        }
        
        /**
         * Registers the user to the game server if the user is not already in the server.
         * 
         * @param info : The string of information following "Register" in the
         * message to the server. The message will have the desired username 
         * followed by the desired password.
         */
        public void register(String info) {
           String username = info.substring(0,info.indexOf(" "));
           String password = info.substring(info.indexOf(" ")+1);
           File userFile = new File(userInfotxt);
           Scanner scan = null;
           int flag = 0;
        try {
            scan = new Scanner(userFile);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
           scan.nextLine();
           while(scan.hasNext()){
               String temp = scan.next();
               if(temp.equals(username))
               {
                   flag = 1;
               }
           }
           if(flag == 1){
              c.sendMessage(usernameTaken); // sent back to the user on display
           }
           else{
              c.sendMessage(goodToLogin);
                   try (FileWriter writer = new FileWriter(userInfo, true)) {
                        writer.write(username + "\t" + password + "\t" + 0 + "\t" + 0  + "\n");
                        writer.flush();
                    } catch (IOException ex) {
                            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                    } 
           }
           scan.close();
        }  

        /**
	 * Used to add a new connection into the connection list
	 * @param conn : The connection you are adding to the online list
	 */
	public void addConnection(Connection conn)
    {
		cList.add(conn);
	}   
    
    /**
     * Sets the Server controller to the one being passed in.
     * 
     * @param sc : The servercontroller that is set to control the server's gui
     */
    public void setController(ServerController sc){
            this.sc = sc;
        }
    /**
     * Stops the connection to the user and takes them off the active user
     * list. Also, will send a message to all active users that the user
     * closed their connection.
     * 
     * @param username : The username of the connection you are closing
     */
    public void close(String username){

        if(username.substring(0,4).equals(nullString)){
            String ip = username.substring(5);
            System.out.println(ip);
            for(int i = 0; i < cList.size(); i++){
                if(cList.get(i).s.getInetAddress().toString().equals(ip)){
                    cList.remove(i);
                }
            }
        }
        else{
        for(int i = 0; i < cList.size(); i++){
            if(cList.get(i).username.equals(username)){
                cList.remove(i);
            }
        }
        }
        
        
        for(int i = 0; i < cList.size(); i++){
            cList.get(i).updateOffline(cList, username);
        }

        sc.setNum(cList.size());
        sc.setConnectionList(cList);
    }
    
    /**
     * Invites the user if they are in the list.
     * 
     * @param message : The message that comes after Invite. Will have the 
     * user's username you are sending at invite to and then the username of
     * the user sending the invite
     */
    void invite(String message) {
       Connection sendToConnection = null;
       Scanner scan = new Scanner(message);
       scan.nextLine();
       String sendTo = scan.nextLine();
       String from = scan.nextLine();
       
       for(int i = 0; i < cList.size(); i++){
           if(cList.get(i).username.equals(sendTo)){
               sendToConnection = cList.get(i);
           }
       }
       sendToConnection.sendMessage(invite + from);
    }
    
    /**
     * Gets the stats of the user and sends it back to the user requesting
     * the stats.
     * 
     * @param name: The usernames of the users involved in the request. The 
     * first username is the username of the person's whose stats are being 
     * requested. The second username is the person requesting the stats.
     */
    void updateStatsLabels(String name) {
        Connection sendBack = null;
        Scanner nameScan = new Scanner(name);
        String stat = nameScan.next();
        String statsFrom = nameScan.next();
        String requester = nameScan.next();
        nameScan.close(); 
        File userFile = new File(userInfotxt);
        Scanner scan = null;
        try {
            scan = new Scanner(userFile);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        while(scan.hasNext())
        {   
            String str = scan.next();
            if(str.equals(statsFrom))
            {
                String password = scan.next();
                int wins = scan.nextInt();
                int losses = scan.nextInt();
                for(int i = 0; i < cList.size(); i++)
                {
                    if(cList.get(i).username.equals(requester))
                    {
                        sendBack = cList.get(i);
                        sendBack.sendMessage(stats + wins +" "+ losses);
                    }
                }
            }
            else
            {
                scan.next();
            }
        }
        scan.close();
    }
    /**
    * Method that is called when the server receives a message starting with Decline.
    * This method will notify the declinee that the decliner has declined their
    * invite.
    * 
    * @param declinee : The username of the person who is being declined
    * @param decliner : The username of the person who is declining
    */
    void decline(String declinee, String decliner) {
        for(int i = 0; i < cList.size(); i++){
            if(cList.get(i).username.equals(declinee)){
                cList.get(i).sendMessage(declined + decliner);
            }
        }
    }
    
    /**
     * The method that is invoked when someone accepts a game. Sends all needed
     * information to connect to the other user's hostServer and the name of 
     * the person who accepted. So the server is sending the inviter back a 
     * message informing them that someone has accepted their invite.
     * 
     * @param accepted : The user whose invite is being accepted
     * @param accepter : The user who is accepting the invite
     * @param ip : The ip that the host server will be located at. So it will
     * be the ip of the accepter/invited.
     * @param port : The port at which the host server is going to be at. 
     */
    void accept(String accepted, String accepter, String ip, int port) {
        for(int i = 0; i < cList.size(); i++){
            if(cList.get(i).username.equals(accepted)){
                cList.get(i).sendMessage(acceptedString + "\n" + accepter + "\n" + ip + "\n" + port);
            }
        }
    }

   /**
     * The method that logs a guest in. A little different, but not drastically
     * different than the login method, however, doesn't check the text document
     * to check if the username or password are correct.
     * 
     * @param guest : The username of what the guest will be named. Randomly
     * generated on the client side.
     * @param ip : A string containing the ip of the user who is logging in as a 
     * guest.
     */
    void guest(String guest, String ip) {
          for(int i = 0; i < cList.size(); i++){
               String cListip = cList.get(i).s.getInetAddress().toString();
               
               //If you are the server, change the ip to your real ip instead of localhost
               if(cListip.equals(localHostIP)){
                   try {
                       cListip = InetAddress.getLocalHost().toString();
                   } catch (UnknownHostException ex) {
                       Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                   }
                   cListip = cListip.substring(cListip.indexOf("/"));
               }
               /////////////////////////////////////////////////
               
               if(cListip.equals(ip)){
                   c = cList.get(i);
               }
                   }
                   
                    c.sendMessage(correctInfo);
                    c.setUsername(guest);
                    sc.setConnectionList(cList);
                    
                    for(int i = 0; i < cList.size(); i++){
                       cList.get(i).UpdateOnline(cList);
                   }
    }

    void leaveLobby(String username) {
        for(int i = 0; i < cList.size(); i++){
            if(cList.get(i).username.equals(username)){
                inGame.add(cList.get(i));
                cList.remove(i);
            }
        }
        for(int i = 0; i < cList.size(); i++){
            cList.get(i).updateOffline(cList, username);
        }
        sc.setNum(cList.size());
        sc.setConnectionList(cList);
    }
   /**
     * The method that pushes a user from the in game arraylist back into
     * the online list to say they are in the lobby. This method is for the
     * LOSER of the game. If a user WINS use the returnLobby method.
     * 
     * @param username : The string containing the username of the user who
     * is backing out to the lobby from the game.
     */
    void back(String username){
        for(int i = 0; i < inGame.size(); i++){
        if(inGame.get(i).username.equals(username)){
           cList.add(inGame.get(i));
           inGame.remove(i);
            }
        }
        for(int i = 0; i < cList.size(); i++){
            cList.get(i).UpdateOnline(cList);
        }
        sc.setNum(cList.size());
        sc.setConnectionList(cList);
    }
   /**
     * Returns a user back to the lobby after a win. This method is for the
     * WINNER of the game. The two different methods are needed since the
     * winner reports who won and who lost in their game. This method will also
     * update the text file to insure that the stats are up to date.
     * 
     * @param user : The string containing the user's username
     * @param gameWin : Will be a 1 if the user won or 0 if they lost.
     * @param opponent : The string containing the username of user's opponent
     *
     */
    public void returnLobby(String user, int gameWin, String opponent) throws IOException {
        File file = new File(userInfotxt);
        for(int i = 0; i < inGame.size(); i++){
            if(inGame.get(i).username.equals(user)){
            cList.add(inGame.get(i));
            inGame.remove(i);
            }
        }
        for(int i = 0; i < cList.size(); i++){
            cList.get(i).UpdateOnline(cList);
         }
        sc.setNum(cList.size());
        sc.setConnectionList(cList);
        
        Scanner scan = null;
        try {
            scan = new Scanner(file);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        FileWriter writer;
        writer = new FileWriter(userInfoCopy);
        
        writer.write(scan.nextLine() + "\n");

        while(scan.hasNext()){
            String tempUser = scan.next();
            if(tempUser.equals(user)){
                writer.write(tempUser + "\t" + scan.next() + "\t");
                int wins = scan.nextInt();
                int losses = scan.nextInt();
                
                wins++;
                
                writer.write(wins + "\t" + losses + "\n");
            }
            else{
                if(tempUser.equals(opponent)){
                    writer.write(tempUser + "\t" + scan.next() + "\t");
                    int wins = scan.nextInt();
                    int losses =  scan.nextInt();
                    
                    losses++;
                    
                    writer.write(wins + "\t" + losses + "\n");
                }
                else
                    writer.write(tempUser + "\t" + scan.next() + "\t" + scan.nextInt() + "\t" + scan.nextInt() + "\n");
            }
        }
        writer.close();
        scan.close();
        
        FileWriter secondWriter = new FileWriter(userInfotxt);
        File secondFile = new File(userInfoCopy);
        Scanner secondScan = null;
        try {
            secondScan = new Scanner(secondFile);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        secondWriter.write(secondScan.nextLine() + "\n");
        while(secondScan.hasNext()){
            secondWriter.write(secondScan.nextLine() + "\n");
        }
        secondWriter.close();
        secondScan.close();
        
    }
   /**
     * This method will move a user from the inGame list to the connected list. 
     * This method is called when a user is connecting to an AI game so the 
     * user is not logged in and the server has to move the user based off of ip
     * rather than username like the accept method.
     * 
     * @param ip : A string with the ip of the user leaving into an AI game
     */
    public void computer(String ip) {
          for(int i = 0; i < cList.size(); i++){
            if(cList.get(i).s.getInetAddress().toString().equals(ip) && cList.get(i).username == null){
                inGame.add(cList.get(i));
                cList.remove(i);
            }
        }
        sc.setNum(cList.size());
        sc.setConnectionList(cList);
    }
   /**
     * This method will put the user back into the connections list after 
     * they are done with an AI game so they can log in and play an online game 
     * after their AI game.
     * 
     * @param ip : The ip of the user who is leaving the AI game and coming back
     * to the main window.
     */
    public void exit(String ip){
        for(int i = 0; i < inGame.size(); i++){
        if(inGame.get(i).s.getInetAddress().toString().equals(ip)){
           cList.add(inGame.get(i));
           inGame.remove(i);
            }
        }
        sc.setNum(cList.size());
        sc.setConnectionList(cList);
    }
    
   /**
     * A debugging method that allows cList to be printed to see who is 
     * "connected" to the server.
     */
    public void printcList(){
        for(int i = 0; i < cList.size(); i++){
            System.out.println(cList.get(i).username);
        }
    }
}