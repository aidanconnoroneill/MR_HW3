import java.util.ArrayList;
import java.util.List;
/** A Lemonade Stand game player that...
  *
  *
  * @author AM
  */
public class SplitBot implements Bot {
    /** Template Bot class.
      *
      * @param player1LastMove the action that was selected by Player 1 on the
      *                        last round.
      * @param player2LastMove the action that was selected by Player 2 on the
      *                        last round.
      *
      * @return the next action to play.
      */

    List<Integer> player1moves = new ArrayList<Integer>();
    List<Integer> player2moves = new ArrayList<Integer>();
    public int getNextMove(int player1LastMove, int player2LastMove) {
        player1moves.add(player1LastMove);
        player2moves.add(player2LastMove);
        System.out.println(player1moves);
        return (player1LastMove+player2LastMove)/2;
    }
}
