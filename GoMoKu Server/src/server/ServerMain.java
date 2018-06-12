package server;

/**
* FILENAME :  
*       ServerMain.java
*
* DESCRIPTION :
*   This class is is used to call and run all the components of the server. It
*   only contains a main method that will make the server and Gui and connect
*   each other.
* 
* USE :
*       This class will call and run the server so users can connect to it.
*
* NOTES :
*       This Class is a piece of the GoMoKu Server side Application
* 
* @author :    
*       CashNCarry Group
* 
* START DATE :    
*       February 2018
**/


public class ServerMain {

  /**
   * Main method that will start the server by creating the threads necessary
   * to have the client connect.
   * @param args the command line arguments
   *
   */
  public static void main(String[] args) {
    Server app = new Server(); // creates Thread One
    app.listen(); // creates Thread Two
    ServerGui gui = new ServerGui();
    ServerController sc = new ServerController();
    sc.setGui(gui);
    app.setController(sc);
  }

}
