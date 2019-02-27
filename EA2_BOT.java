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

	double p = .5;

	double confidence = .1;
	double historyWeight = .75;
	int stickCounter = 3;
	int initStick = 3;
	int antiCollaborator = 3;
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
		if(prevPlayerOne.size()==1) {
			int move = generator.nextInt(12) + 1;
			myMoves.add(move);
			stickCounter --;
			return move;
		}
		if(myMoves.size()<initStick) {
			myMoves.add(myMoves.get(0));
			stickCounter--;
			return myMoves.get(0);
		}
		if(stickCounter>0) {
			myMoves.add(myMoves.get(myMoves.size()-1));
			stickCounter--;
			return myMoves.get(myMoves.size()-1);
		}
		
		double player1Stick = getStickIndex(true);
		double player2Stick = getStickIndex(false);
		double firstPlayerFollowingSecond = getFollowIndex(1, 2);
		double firstPlayerFollowingMe = getFollowIndex(1, 3);  	
		double secondPlayerFollowingFirst = getFollowIndex(2, 1);
		double secondPlayerFollowingMe = getFollowIndex(2, 3);  
		double secondPlayerFollow = secondPlayerFollowingFirst;
		boolean secondLikes = false;
		if(secondPlayerFollow<secondPlayerFollowingMe) {
			secondLikes = true;
			secondPlayerFollow = secondPlayerFollowingMe;
		}
		double firstPlayerFollow = firstPlayerFollowingSecond;
		boolean firstLikes = false;
		if(firstPlayerFollow<firstPlayerFollowingMe) {
			firstLikes = true;
			secondPlayerFollow = firstPlayerFollowingMe;
		}

		if(player1Stick>(secondPlayerFollow+confidence) && player1Stick > firstPlayerFollow+confidence && player1Stick > player2Stick+confidence) {
			int move = getOpposite(player1LastMove);
			myMoves.add(move);
			return move;
		}
		if(player2Stick>(secondPlayerFollow+confidence) && player2Stick > firstPlayerFollow+confidence && player2Stick > player1Stick+confidence) {
			int move = getOpposite(player2LastMove);
			myMoves.add(move);
			return move;
		}
		if(player1Stick>firstPlayerFollow+confidence && player2Stick>secondPlayerFollow+confidence) {
			stickCounter = initStick;
			if(getLastUtility(player1LastMove, player2LastMove, myMoves.get(myMoves.size()-1))>8) {
				 int move = myMoves.get(myMoves.size()-1);
				 myMoves.add(move);
				 return move;
			}
			else {
				int move = getOpposite(player1LastMove);
				if(player2Stick>player1Stick) {
					move = getOpposite(player2LastMove);
				}
				myMoves.add(move);
				return move;
			}
		}

		if(firstPlayerFollow > player1Stick + confidence && firstPlayerFollow > player2Stick + confidence && firstPlayerFollow > secondPlayerFollow + confidence) {
			if(firstLikes) {
				int move = myMoves.get(myMoves.size()-1);
				myMoves.add(move);
				return move;
			}
			else {
				int move = player2LastMove;
				myMoves.add(move);
				return move;
			}
		}
		if(secondPlayerFollow > player1Stick + confidence && secondPlayerFollow > player2Stick + confidence && secondPlayerFollow > firstPlayerFollow + confidence) {
			if(secondLikes) {
				int move = myMoves.get(myMoves.size()-1);
				myMoves.add(move);
				return move;
			}
			else {
				int move = player1LastMove;
				myMoves.add(move);
				return move;
			}
		}	
		if(firstPlayerFollow>player1Stick+confidence && secondPlayerFollow> player2Stick+confidence && !secondLikes && !firstLikes) {
			int move = player2LastMove;
			if(firstPlayerFollow>secondPlayerFollow) {
				move = player1LastMove;
			}
			myMoves.add(move);
			return move;
		}
		if(getDistance(player1LastMove, player2LastMove)==6) {
			if(antiCollaborator > 0) {
				int move = player1LastMove;
				if(player1Stick>player2Stick) {
					move = player2LastMove;
				}
				antiCollaborator--;
				myMoves.add(move);
				return move;
			}
			else {
				antiCollaborator = initStick;
				double lean;
				int move;
				if(player1Stick>player2Stick) {
					lean = getAvg(false);
					if(lean<player2LastMove) {
						move=  getOpposite(player2LastMove-1);
					}
					else {
						move=  getOpposite(player2LastMove+1);
					}
				}
				else {
					lean = getAvg(true);
					if(lean<player1LastMove) {
						move=  getOpposite(player1LastMove-1);
					}
					else {
						move=  getOpposite(player1LastMove+1);
					}
				}
				myMoves.add(move);
				return move;
			}
		}
		
		
		return myMoves.get(myMoves.size()-1);
	}
	
	private double getAvg(boolean isFirst) {
		List<Integer> curr = prevPlayerTwo;
		if(isFirst) {
			curr= prevPlayerOne;
		}
		double num = 0.0;
		double sum = 0.0;
		for(int i = curr.size()-6; i<curr.size(); i++) {
			if(i<0) {
				continue;
			}
			num++;
			sum+= curr.get(i);
		}
		return sum/num;
	}
	
	private double getLastUtility(int a, int b, int me) {
		return (getDistance(b, me) + getDistance(a, me))/2.0;
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
		for(int i = 1; i<sec.size(); i++) {
			ans -= (Math.pow(historyWeight, history-i) * getWeightedDistance(first.get(i-1), sec.get(i))) / gamma;
		}

		return ans;
	}
	private int getOpposite(int a) {
		int ans = a+6;
		if (ans>12) ans-=12;
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
