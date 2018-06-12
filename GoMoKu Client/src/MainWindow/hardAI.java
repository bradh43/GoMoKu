package MainWindow;

import static MainWindow.Constants.boardSIZE;
import java.util.ArrayList;
import java.util.Random;

/**
 * This Class attempts to test a user's limits with how good they really are.
 * @author CashNCarry
 */
public class hardAI implements AI {

    public GameBoard gb;
    public GameBoardController gbc;
    public GameModel gm;
    public PlayerController pc;
    public int num;
    public boolean flag = true;
    public int randomNum = 0;
    public int numCount = 0;
    public int lastMove;

  /**
   * Initializes gb, gbc, gm, and pc for the hard AI
   * @param gb instance of GameBoard
   * @param gbc instance of GameBoardController
   * @param gm instance of GameModel
   * @param pc instance of PlayerController
   */
    public hardAI(GameBoard gb, GameBoardController gbc, GameModel gm, PlayerController pc) {
        this.gb = gb;
        this.gbc = gbc;
        this.gm = gm;
        this.pc = pc;

    }

    /**
     * Temporality places an AI move on the board to see if it would win the game
     */
    void checkEveryButtonAI() {
        for (int i = 0; i < boardSIZE; i++) {
            for (int j = 0; j < boardSIZE; j++) {
                if (gm.boardArray[i][j] == 0) {
                    gm.boardArray[i][j] = 1;

                    if (gbc.checkWin()) {
                        int num = (i * 30) + j;
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

    /**
     * Temporality places an player move on the board to see if it would win the game
     */
    void checkEveryButtonPlayer(int button) {
        for (int i = 0; i < boardSIZE; i++) {
            for (int j = 0; j < boardSIZE; j++) {
                if (gm.boardArray[i][j] == 0) {
                    gm.boardArray[i][j] = 2;
                    if (gbc.checkWin()) {
                        int num = (i * 30) + j;
                        gbc.updateBoard(num + "");
                        flag = false;
                        return;
                    } else {
                        gm.boardArray[i][j] = 0;
                    }
                }
            }
        }

        for (int i = 0; i < boardSIZE; i++) {
            for (int j = 0; j < boardSIZE; j++) {
                if (gm.boardArray[i][j] == 0) {
                    gm.boardArray[i][j] = 2;
                    if (fourInArow((i * 30) + j)) {
                        System.out.println("4!");
                        int num = (i * 30) + j;
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

    /**
     * Make the next move for the AI 
     */
    @Override
    public void nextMove(int num) {
        checkEveryButtonPlayer(num);

        if (flag) {
            checkEveryButtonAI();
        }

        if (flag) {
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
        gbc.printBoard();
    }

    /**
     * Checks to see if there are four in a row
     * @param button last botton clicked my the user
     */
    public boolean fourInArow(int button) {
        boolean four = false;
        int row = Math.floorDiv(button, boardSIZE);
        int col = button % boardSIZE;

        int Colcount = 0;
        System.out.println("-------------");
        for (int i = 1; i < 4; i++) {
            int x = gm.boardArray[row][col];
            boolean down = true;
            boolean up = true;
            try {
                if (gm.boardArray[row + i][col] == x && down) {
                    Colcount++;
                } else {
                    down = false;
                }
            } catch (ArrayIndexOutOfBoundsException e) {

            }
            try {
                if (gm.boardArray[row - i][col] == x && up) {
                    Colcount++;
                } else {
                    up = false;
                }
            } catch (ArrayIndexOutOfBoundsException e) {

            }
            if (Colcount == 3) {
                System.out.println("Columns");
                return true;
            }
        }
        int RowCount = 0;
        for (int i = 1; i < 4; i++) {
            int x = gm.boardArray[row][col];
            boolean left = true;
            boolean right = true;

            try {
                if (gm.boardArray[row][col + i] == x && right) {
                    RowCount++;
                } else {
                    right = false;
                }
            } catch (ArrayIndexOutOfBoundsException e) {

            }
            try {
                if (gm.boardArray[row][col - i] == x && left) {
                    RowCount++;
                } else {
                    left = false;
                }
            } catch (ArrayIndexOutOfBoundsException e) {

            }
            if (RowCount == 3) {
                return true;
            }
        }
        int DiagCount = 0;
        for (int i = 1; i < 4; i++) {
            int x = gm.boardArray[row][col];
            boolean upright = true;
            boolean downleft = true;

            try {
                if (gm.boardArray[row - i][col + i] == x && upright) {
                    DiagCount++;
                } else {
                    upright = false;
                }
            } catch (ArrayIndexOutOfBoundsException e) {

            }
            try {
                if (gm.boardArray[row + i][col - i] == x && downleft) {
                    DiagCount++;
                } else {
                    downleft = false;
                }
            } catch (ArrayIndexOutOfBoundsException e) {

            }
            if (DiagCount == 3) {
                return true;
            }
        }

        int upleftCount = 0;
        for (int i = 1; i < 4; i++) {
            int x = gm.boardArray[row][col];
            boolean upleft = true;
            boolean downright = true;

            try {
                if (gm.boardArray[row - i][col - i] == x && upleft) {
                    upleftCount++;
                } else {
                    upleft = false;
                }
            } catch (ArrayIndexOutOfBoundsException e) {

            }
            try {
                if (gm.boardArray[row + i][col + i] == x && downright) {
                    upleftCount++;
                } else {
                    downright = false;
                }
            } catch (ArrayIndexOutOfBoundsException e) {

            }
            if (upleftCount == 3) {
                return true;
            }
        }
        return four;
    }

}
