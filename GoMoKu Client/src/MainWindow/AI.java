package MainWindow;

/**
 *
 * This is an interface for the AI's. All the AI classes implement this interface
 * so that they will have the next move method.
 * @author isaiahscheel
 */
public interface AI {
 /**
 * This method is implemented by every AI class. It take an int and will make a
 * call to the game board controller to update the board with the AI's next
 * move it will play.
 *
 * @param  num : An integer that corresponds to the 1-D array of buttons on
 * the board that was just pressed by the user.
 */
    public void nextMove(int num);
}
