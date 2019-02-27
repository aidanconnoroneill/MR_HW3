import java.util.*;
/** A Lemonade Stand game player that...
  *
  *
  * @author AM
  */
public class Pujara implements Bot {
    /** Template Bot class.
      *
      * @param player1LastMove the action that was selected by Player 1 on the
      *                        last round.
      * @param player2LastMove the action that was selected by Player 2 on the
      *                        last round.
      *
      * @return the next action to play.
      */
    List<Integer> pastMoves= new ArrayList<Integer>();
    int numRounds=0;
    private int ourMove = 5;
    double avgUtility=0.0;
    private Random generator = new Random();
    public int getNextMove(int player1LastMove, int player2LastMove) {

        if (numRounds<10){
            avgUtility= ((avgUtility*numRounds)+
            this.scoreRound(ourMove,player1LastMove,player2LastMove))/(numRounds+1);
            numRounds+=1;
            System.out.println("Our score: "+Double.toString(avgUtility));
            return ourMove;
          }
        else if(avgUtility<=8){
             ourMove = generator.nextInt(12) + 1;
             avgUtility= ((avgUtility*numRounds)+
             this.scoreRound(ourMove,player1LastMove,player2LastMove))/(numRounds+1);
             numRounds+=1;
             System.out.println("Our score: "+Double.toString(avgUtility));
             return ourMove;
        }
        else{
          avgUtility= ((avgUtility*numRounds)+
          this.scoreRound(ourMove,player1LastMove,player2LastMove))/(numRounds+1);
            numRounds+=1;
            System.out.println("Our score: "+Double.toString(avgUtility));
            return ourMove;
        }
    }

    public int scoreRound(int action1, int action2, int action3) {
        if ((action1 == action2) && (action1 == action3))
            return 8; // three-way tie
        else if ((action1 == action2) || (action1 == action3)) {
            return 6; // two-way tie
        }
        else {
            int score = 0;
            int i = action1;
            while ((i != action2) && (i != action3)) { // score clockwise
                i = (i % 12) + 1;
                score += 1;
            }
            i = action1;
            while ((i != action2) && (i != action3)) { // score anti-clockwise
                i = (i-1 > 0) ? i-1 : 12;
                score += 1;
            }
            return score;
        }
    }
}
