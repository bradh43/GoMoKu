package MainWindow;
import static MainWindow.Constants.boardSIZE;
import java.util.Random;


/**
 *
 * @author CashNCarry
 */
public class medAI implements AI{

  /**
   * Instance of GameBoard
   */
  public GameBoard gb;

  /**
   * Instance of GameBoardController
   */
  public GameBoardController gbc;

  /**
   * Instance of GameModel
   */
  public GameModel gm;

  /**
   * Instance of PlayerController
   */
  public PlayerController pc;

  /**
   * last botton number clicked
   */
  public int num;

  /**
   * if the flag of true it will use easy AI
   * if the flag is false it will use med AI
   */
  public boolean flag = true;

  /**
   * Row position
   */
  public int x;

  /**
   * Column position
   */
  public int y;

  /**
   * Initializes gb, gbc, gm, and pc for the medium AI
   * @param gb instance of GameBoard
   * @param gbc instance of GameBoardController
   * @param gm instance of GameModel
   * @param pc instance of PlayerController
   */
  public medAI(GameBoard gb, GameBoardController gbc, GameModel gm, PlayerController pc) {
        this.gb = gb;
        this.gbc = gbc;
        this.gm = gm;
        this.pc = pc;

    }
    
    void checkEveryButtonAI(){
        for(int i = 0; i < boardSIZE; i++){
            for(int j = 0; j < boardSIZE; j++){
                if (gm.boardArray[i][j] == 0){
                    //System.out.println(i+","+j);
                    gm.boardArray[i][j] = 2;
                    
                    if (gbc.checkWin()) {
                        int num = (i * boardSIZE) + j;
                        gbc.updateBoard(num + "");
                        flag = false;
                        return;
                    } else {
                        gm.boardArray[i][j] = 0;
                    }
                }
                }
        }
    }
    
        void checkEveryButtonPlayer(){
        for(int i = 0; i < boardSIZE; i++){
            for(int j = 0; j < boardSIZE; j++){
                if (gm.boardArray[i][j] == 0){
                    gm.boardArray[i][j] = 1;
                    
                    if (gbc.checkWin()) {
                        System.out.println("We in dis bitch Player");
                        int num = (i * boardSIZE) + j;
                        gbc.updateBoard(num + "");
                        flag = false;
                        return;
                    } else {
                        gm.boardArray[i][j] = 0;
                    }
                }
                }
        }
    }
    
    
    @Override
    public void nextMove(int num) {
       checkEveryButtonAI();
       checkEveryButtonPlayer();
       
       if(flag){
        Random rand = new Random();
        int randomNum = rand.nextInt(8) + 1;

    switch (randomNum) {
      case 1:
        gbc.updateBoard((num - 1) + "");
        break;
      case 2:
        gbc.updateBoard((num - 31) + "");
        break;
      case 3:
        gbc.updateBoard((num - 30) + "");
        break;
      case 4:
        gbc.updateBoard((num - 29) + "");
        break;
      case 5:
        gbc.updateBoard((num + 1) + "");
        break;
      case 6:
        gbc.updateBoard((num + 31) + "");
        break;
      case 7:
        gbc.updateBoard((num + 30) + "");
        break;
      case 8:
        gbc.updateBoard((num + 29) + "");
        break;
    }
       }
       flag = true;

    }
    
}
