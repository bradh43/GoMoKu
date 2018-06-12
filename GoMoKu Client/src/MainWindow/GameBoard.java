package MainWindow;
/**
 * This class is to build the GUI associated with GameBoard.java. It creates
 * and array of buttons to be the game pieces and when clicked they will
 * change color depending if it is your turn. There is a text area off to the
 * right side to let you know where the last person placed their piece. Also,
 * there is a timer near the bottom right to indicate how much time you 
 * have left in your turn. If someone wins/time runs out then there will be
 * a button that appears that says "Lobby" or it will say "Exit" depending if
 * you are playing on line or against an AI. Also, there will be no timer if 
 * the user is playing against an AI.
 *
 * @author CashNCarry
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.Timer;
import javax.swing.UIManager;
import static MainWindow.Constants.gameLabel;
import static MainWindow.Constants.oponentsLabel;
import static MainWindow.Constants.boardSIZE;
import static MainWindow.Constants.serverIp;
import static MainWindow.Constants.time;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;




/**
 * This class is to build the GUI associated with GameBoard.java. It has a 
 * seperate controller, GameBoardController.java. 
 *
 */
public class GameBoard extends javax.swing.JFrame {

    /**
     * The Main Frame that the GUI will draw to. If this frame is not visible
     * the whole GUI will not be visible.
     */
    public JFrame frame;

    /**
     * Text area to display messages that are happening in the game.
     */
    public JTextArea text_area;

    /**
     * An array of JButtons for the gameboard clicks.
     */
    public JButton[] button;

    /**
     * The Label that indicates whose turn it is. This will be updated to show
     * the correct turn.
     */
  public JLabel turnLabel;
  private int boardSize;

    /**
     * The GameBoardController instance
     */
    public GameBoardController gbc;

    /**
     * The GameModel instance
     */
    public GameModel gm;

    /**
     * The AI instance. Uses the AI interface so it can be ambiguous at
     * this call.
     */
    public AI ai;

    /**
     * The integer that keeps track of whose turn it is. Originally set to 1,
     * but can be changes in the making of the class.
     */
    public int turn = 1;

    /**
     * The java Timer object that keeps track of the time in a turn.
     */
    public Timer timer;

    /**
     * A constant of one minute to keep track of the time.
     */
    public final long ONE_MINUTE = time;

    /**
     *  Formats the time to looks like the format of "mm : ss"
     */
    public final java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("mm : ss");

    /**
     * The JLabel for the clock and updates according to the timer ticking down
     */
    public final JLabel clock = new JLabel(sdf.format(new Date(ONE_MINUTE)), JLabel.CENTER);

    /**
     * The JButton to go back to the Lobby in an online game, will take you back
     * to the main menu during an AI game.
     */
    public JButton lobby;

    /**
     * Keeps track of the winner of the game so that the winner can send a 
     * message back to the server when the game is over.
     */
    public int winner = 0;

    /**
     * A simple boolean int (either a 1 or 0) flag to check if the game is an AI game or an online game
     */
    public int aiFlag;

  /**
   * A constructor for the game board. Only needs the gameboardcontroller
   * @param gbc - the gameboard controller that makes the gameboard
   */
  GameBoard(GameBoardController gbc) {
    this.gbc = gbc;
    this.boardSize = boardSIZE;
    this.gm = gbc.gm;
    initComponents();
    frame.setVisible(true);

  }

    /**
     * Constructor with no fields, used for testing
     */
    public GameBoard() {
    initComponents();
    frame.setVisible(true);
  }

    /**
     * Constructor for the AI game play.
     * @param gbc : The gameBoardController that creates and controls the gameboard.
     * @param AI : An int that is 1 or 0 a d tells sets the aiFlag above
     * @param pc : The playerController instance linked to the gameboard
     * @param AItype : A string determining what AI to use. Hard, medium, etc.
     */
  public GameBoard(GameBoardController gbc, int AI, PlayerController pc, String AItype) {
    this.gbc = gbc;
    this.boardSize = boardSIZE; //Literal fix later 
    this.gm = gbc.gm;
    this.aiFlag = AI;
    if (AItype.equals("AI - Easy")) {
      this.ai = new easyAI(this, gbc, gm, pc);
    } 
    else if (AItype.equals("AI - Medium")) {
      this.ai = new medAI(this, gbc, gm, pc);
    } 
    else if (AItype.equals("AI - Hard")) {
        this.ai = new hardAI(this,gbc,gm,pc);
    }
    else if(AItype.equals("AI - Evil")){
        this.ai = new evilAI(this,gbc,gm,pc);
    }
    initComponents();
    frame.setVisible(true);
  }

  private void initComponents() {
    try {
      UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
    } catch (Exception e) {
      e.printStackTrace();
    }
    //Main frame for the gui
    frame = new JFrame();
    frame.setResizable(true);
    frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    frame.setSize(600, 700);
    JPanel top_bar = new JPanel(new BorderLayout());
    JPanel side_bar = new JPanel(new BorderLayout());
    JPanel clock_panel = new JPanel(new BorderLayout());
    //Message label
    JLabel messageLabel = new JLabel(gameLabel);
    turnLabel = new JLabel(oponentsLabel);
    turnLabel.setBackground(Color.WHITE);
    top_bar.add(messageLabel, BorderLayout.EAST);
    clock_panel.add(turnLabel, BorderLayout.NORTH);
    //Setting the text area
    JPanel messagePanel = new JPanel();
    text_area = new JTextArea();
    text_area.setColumns(20);
    text_area.setFont(new java.awt.Font("Comic Sans MS", 0, 18));
    text_area.setForeground(new java.awt.Color(0, 0, 0));
    text_area.setLineWrap(true);
    text_area.setRows(15);
    text_area.setEditable(false);
    messagePanel.add(text_area);
    side_bar.add(messagePanel, BorderLayout.NORTH);

    //make GameBoard
    JPanel board = new JPanel();
    int size = boardSIZE;
    //int fontSize = 18;
    GridLayout gameBoard = new GridLayout(size, size);
    board.setLayout(gameBoard);
    button = new JButton[(size * size)];
    for (int i = 0; i < button.length; i++) {
      button[i] = new JButton();
      button[i].setBackground(new java.awt.Color(0, 0, 0));
      board.add(button[i]);
      button[i].addActionListener(new ButtonListener());
      button[i].setEnabled(true);
      button[i].setForeground(Color.black);
      button[i].setOpaque(true);
    }

    //Change this to mess with the squares
    board.setPreferredSize(new Dimension(700, 600));

    //Make the text area scrollable
    JScrollPane scroll = new JScrollPane(text_area);
    messagePanel.add(scroll);

    //Clock - No Clock for AI
    if (aiFlag != 1) {
      ActionListener clockListener = new ActionListener() {
        long x = ONE_MINUTE - 1000;

        public void actionPerformed(ActionEvent ae) {
          clock.setText(sdf.format(new Date(x)));
          x -= 1000;
          if (x < 0) {
            gbc.timesOut();
          }
        }
      };
      timer = new Timer(1000, clockListener);
      timer.start();
      clock_panel.add(clock, BorderLayout.CENTER);
    }

    //Lobby Button Stuff
    lobby = new JButton("Lobby");
    lobby.setVisible(false);
    lobby.addActionListener(new LobbyListener());
    clock_panel.add(lobby, BorderLayout.SOUTH);
    side_bar.add(clock_panel, BorderLayout.CENTER);

    //Set the location for all the panels
    frame.getContentPane().add(BorderLayout.EAST, side_bar);
    frame.getContentPane().add(BorderLayout.CENTER, board);
    frame.getContentPane().add(BorderLayout.NORTH, top_bar);
    frame.setVisible(true);
    frame.pack();
  }

  //Lobby Button Controller to go back to the lobby
  private class LobbyListener implements ActionListener {

    public void actionPerformed(ActionEvent e) {
     if(lobby.getText().equals("Exit")){
             String ip = null;
            try {
                ip = InetAddress.getLocalHost().toString();
            } catch (UnknownHostException ex) {
                Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (serverIp.equals("localhost")){
                ip = "/127.0.0.1";
            }
            ip = ip.substring(ip.indexOf('/'));
            gbc.mc.client.sendMessage("Exit " + ip);
            gbc.mc.openMainWindow();
        }
        else{
        gbc.lobby();
        }
        frame.setVisible(false);
     }
  }

  //Action Listener for all the buttons
  private class ButtonListener implements ActionListener {

    public void actionPerformed(ActionEvent e) {
      if (!(aiFlag == 1)) {
        gbc.restartClock();
      }

      int pos = 0;
      for (int i = 0; i < button.length; i++) {
        if (e.getSource() == button[i]) {
          pos = i;
          gbc.lastmove = i;
          button[i].setBackground(gbc.pc.pm.tileColor);
          button[i].disable();

          if (aiFlag != 1) {
            gbc.sendBoard("" + i);
            gbc.disableAll();
          }

          i++;
          int x;
          int y;
          if (i == 0) {
            x = 1;
            y = 1;
          } else {
            if (i < boardSIZE) {
              y = 1;
            } else {
              if (((double) i / boardSIZE) > Math.floorDiv(i, boardSIZE)) {
                y = Math.floorDiv(i, boardSIZE) + 1;
              } else {
                y = Math.floorDiv(i, boardSIZE);
              }
            }

            if ((i % boardSIZE) == 0) {
              x = boardSIZE;
            } else {
              x = (i % boardSIZE);
            }
          }
          gm.makeMove(x, y, gbc.pc);
          if (aiFlag != 1) {
            gbc.switchTurn();
          }

          if (gbc.pc.pm.playerNumber == 1) {
            text_area.append("RED Player:   Row " + x + "  Col " + y + "\n");
          } else {
            text_area.append("BLUE Player:   Row" + x + " Col " + y + "\n");
          }
        }
      }
      if (gbc.checkWin()) {
        text_area.append("You Win, GAME OVER");
        if (aiFlag == 1) {
          lobby.setText("Exit");
        }
        lobby.setVisible(true);
        gbc.disableAll();
        winner = 1;

      } else {
        if (aiFlag == 1) {
          ai.nextMove(pos);
        }
      }

    }
  }
}
