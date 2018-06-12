package server;

/**
* FILENAME :  
*       Constants.java
*
* DESCRIPTION :
*   This class is the constants used for the server part of the GoMoKu 
*   application. If there is a constant used in ServerController, Server, or
*   Connection, it should be in here.
* 
* USE :
*       Stores constants for the Server side.
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

public class Constants {
    static String portError = "Server Socket could not be created at the port";
    static String socketError = "Server Socket could not accept";
    static String localHostIP = "/127.0.0.1";
    static String userInfotxt = "UserInfo.txt";
    static String noUsername = "Username is not registered";
    static String correctInfo = "Correct info";
    static String alreadyLogged = "Already logged in";
    static String wrongPassword = "Wrong Password";
    static String usernameTaken = "Username taken";
    static String goodToLogin = "Good to login";
    static String nullString = "null";
    static String invite = "Invite ";
    static String stats = "Stats ";
    static String declined = "Declined ";
    static String acceptedString = "Accepted ";
    static String userInfoCopy = "userInfo-copy.txt";
    static String close = "Close ";
    static char login = 'l';
    static char closing = 'C';
    static char Invite = 'I';
    static char statsRequest = 'S';
    static char decline = 'D';
    static char accept = 'A';
    static char guest = 'G';
    static char play = 'P';
    static char returnString = 'R';
    static char back = 'B';
    static char computer = 'Z';
    static char exitString = 'E';
}
