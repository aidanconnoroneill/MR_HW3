import java.util.*;
/** A Lemonade Stand game player that is extremely attached to the five o'clock
  * spot.
  * 
  * @author Andrew Miller
  * @author Aidan O'Neill
  */
public class EA2_BOT implements Bot {
	
	List<Integer> prevPlayerOne = new ArrayList<Integer>();
	List<Integer> prevPlayerTwo = new ArrayList<Integer>();
	List<Integer> myMoves = new ArrayList<Integer>();
    private Random generator = new Random();
    
    double p = 2.0;
    
    double historyWeight = .6;
    /** Finds a collaborator (hopefully)!  If it's getting wrecked, uses carrot and stick strategy to break up the collaborators.  
      * 
      * @param player1LastMove the action that was selected by Player 1 on the
      *                        last round.
      * @param player2LastMove the action that was selected by Player 2 on the
      *                        last round.
      * 
      * @return the next action to play.
      */
    public int getNextMove(int player1LastMove, int player2LastMove) {
    	prevPlayerOne.add(player1LastMove);
    	prevPlayerTwo.add(player2LastMove);
    	if(prevPlayerOne.size()<2) {
    		int move = generator.nextInt(12) + 1;
    		myMoves.add(move);
    		return move;
    	}
    	
    	
        return 5;
    }
    
    private double getFollowIndex(int firstPlayer, int secondPlayer) {
    	List<Integer> first = prevPlayerOne;
    	List<Integer> sec = prevPlayerTwo;
    	if(firstPlayer==2) {
    		first = prevPlayerTwo;
    	}
    	if(secondPlayer == 1) {
    		sec = prevPlayerOne;
    	}
    	if(secondPlayer == 3) {
    		sec = myMoves;
    	}
    	double ans = 0.0;
    	double gamma = getGamma();
    	int history = first.size();
    	for(int i = 1; i<prevPlayerOne.size(); i++) {
    		ans -= (Math.pow(historyWeight, history-i) * getWeightedDistance(first.get(i-1), sec.get(i))) / gamma;
    	}
    	
    	return ans;
    }
    private double getGamma() {
    	double ans = 0.0;
    	int numMoves = prevPlayerOne.size();
    	for(int i = 2; i<=numMoves; i++) {
    		ans += Math.pow(historyWeight, numMoves - i);
    	}
    	return ans;
    }
    private double getStickIndex(boolean isPlayerOne) {
    	List<Integer> curr = prevPlayerOne;
    	if(!isPlayerOne) {
    		curr = prevPlayerTwo;
    	}
    	int history = curr.size();
    	if(!isPlayerOne) {
    		curr = prevPlayerTwo;
    	}
    	double ans = 0.0;
    	double gamma = getGamma();
    	for(int i = 1; i<prevPlayerOne.size(); i++) {
    		ans -= (Math.pow(historyWeight, history-i) * getWeightedDistance(curr.get(i-1), curr.get(i))) / gamma;
    	}
    	return ans;

    }
    
    private double getWeightedDistance(int a, int b) {
    	return Math.pow(getDistance(a, b), p);
    }
    private int getDistance(int a, int b) {
    	if(b-a > 6) return a-b;
    	return b-a;
    }
}
