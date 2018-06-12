package server;

import java.util.ArrayList;

/**
 * The serverController is a class that controls the server GUI.
 * It is connected to a gui and will update/change it according to the
 * current conditions of the server.
 * 
 * @author CashNCarry
 */
public class ServerController {

  private ServerGui gui;

  /**
   * Sets the numbers of users
   */
  public void setNum(int num) {
    this.gui.text.setText("" + num);
  }

  /**
   * Appends the online user to the server gui
   */
  public void setConnectionList(ArrayList<Connection> cList) {
    this.gui.text_area.setText("");
    for (int i = 0; i < cList.size(); i++) {
      String ip = cList.get(i).s.getInetAddress() + "";
      this.gui.text_area.append("" + ip + " - " + cList.get(i).username + "\n");
    }
  }

  /**
   * Sets the gui passed in to this one
   */
  public void setGui(ServerGui gui) {
    this.gui = gui;
  }
}
