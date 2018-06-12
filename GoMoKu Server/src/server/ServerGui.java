package server;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JScrollPane;

/**
 * This class is to build the GUI associated with serverController.java. It is created in
 * main and then connected to the serverController with the addGui method. This gui
 * who is connected to the server by showing their ip address along with their username.
 *
 * @author CashNCarry
 */
public class ServerGui {

  private JFrame frame;
  private JLabel text_field_Label;
  public JTextField text;
  public JTextArea text_area;

  /**
   * This is the constructor for the GUI. It has no parameters and makes a
   * simple GUI that keeps track of who is online.
   */
  public ServerGui() {
    //Main frame for the gui
    frame = new JFrame("GoMoKu Server");
    frame.setResizable(false);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(600, 700);
    //Top panel with the text field and clear button
    JPanel top_bar = new JPanel();
    //Text Field label
    text_field_Label = new JLabel("Number of Connections");
    //Text Field for the message
    text = new JTextField(25);
    text.setEditable(false);
    //text.setFont(new java.awt.Font("Comic Sans MS", 0, 18));
    top_bar.add(text_field_Label);
    top_bar.add(text);
    //Setting the text area
    JPanel j_panel = new JPanel();

    JLabel messages = new JLabel("Connections");
    JPanel labelPanel = new JPanel();
    labelPanel.add(messages);
    //Text area changing
    text_area = new JTextArea();
    text_area.setColumns(40);
    text_area.setFont(new java.awt.Font("Comic Sans MS", 0, 18));
    text_area.setForeground(new java.awt.Color(0, 0, 0));
    text_area.setLineWrap(true);
    text_area.setRows(20);
    text_area.setEditable(false);

    //Make the text area scrollable
    JScrollPane scroll = new JScrollPane(text_area);
    j_panel.add(scroll);
    //Set the location for all the panels
    frame.getContentPane().add(BorderLayout.SOUTH, j_panel);
    frame.getContentPane().add(BorderLayout.CENTER, labelPanel);
    frame.getContentPane().add(BorderLayout.NORTH, top_bar);
    frame.setVisible(true);
    frame.pack();
  }
}
