import java.util.*;
/** Program that runs multiple Lemonade Stand tournaments between the three named
  * players. Calculates average utility and Standard Deviation for each bot
  *
  * @author AM
  */
  public class BotTesting {

    public static void main(String[] args) {
      ArrayList<Double> player1scores = new ArrayList<Double>();
      ArrayList<Double> player2scores = new ArrayList<Double>();
      ArrayList<Double> player3scores = new ArrayList<Double>();
      String player1 = args[0];
      String player2 = args[1];
      String player3 = args[2];
      int numTournaments = Integer.parseInt(args[4]);
      System.out.println("Running "+Integer.toString(numTournaments)+" tournaments");

      if (args.length != 5) {
          System.out.print("Usage: java Tournament <player1> <player2> ");
          System.out.println("<player3> <n>");
          System.out.println("where <player1> = class name of first bot.");
          System.out.println("      <player2> = class name of second bot.");
          System.out.println("      <player3> = class name of third bot.");
          System.out.println("      <n>       = number of rounds to play.");
          System.out.println("Example:");
          System.out.print("java Tournament RandomBot FiveOClockBot ");
          System.out.println("RandomBot 10000 5");
          System.exit(-1);
      }

      // Determine number of rounds to run
      int numRounds = 0;
      try {
          numRounds = Integer.parseInt(args[3]);
      }
      catch (Exception e) {
          System.out.println("Error: invalid value for num rounds.");
          System.exit(-1);
      }

      // Instantiate players
      Bot[] players = {null, null, null};
      try {
          for (int i = 0; i < 3; i++)
              players[i] = (Bot)Class.forName(args[i]).newInstance();
      }
      catch (Exception e) {
          System.out.println("Error: one or more named bots could not be " +
                             "loaded.");
          System.out.println("Double check that your bots have been " +
                             "compiled and that the .class files are in " +
                             "the current directory.");
          System.exit(-1);
      }
      for(int j=0;j<numTournaments;j++){
        System.out.println("Running Tournament: "+Integer.toString(j+1)+"... "+Integer.toString(numTournaments-j-1)+" to go.");
      // Run tournament
      Arbiter judge = new Arbiter(players[0], players[1], players[2]);
      for (int i = 0; i < numRounds; i++)
          judge.runRound();

      // Print mean scores
      int[] scores = judge.getCurrentScore();
      for (int i = 0; i < 3; i++)
          // System.out.println(args[i] + ": " + (scores[i]/(double)numRounds));
          player1scores.add((scores[0]/(double)numRounds));
          player2scores.add((scores[1]/(double)numRounds));
          player3scores.add((scores[2]/(double)numRounds));
          // System.out.println("Player 1 Scores: "+Arrays.toString(player1scores.toArray()));
        }
      calcStatistics(player1scores,player2scores,player3scores,args[0],args[1],args[2]);

  }
  public static void calcStatistics(ArrayList<Double> player1scores,ArrayList<Double> player2scores,ArrayList<Double> player3scores,
  String ply1, String ply2, String ply3){
    Double pl1avg = calculateAverage(player1scores);
    Double pl2avg = calculateAverage(player2scores);
    Double pl3avg = calculateAverage(player3scores);

    Double pl1err = meanSqrErr(player1scores,pl1avg);
    Double pl1stdDev = Math.sqrt(pl1err);

    Double pl2err = meanSqrErr(player2scores,pl2avg);
    Double pl2stdDev = Math.sqrt(pl2err);

    Double pl3err = meanSqrErr(player3scores,pl3avg);
    Double pl3stdDev = Math.sqrt(pl3err);
    //Print Player 1 Results
    System.out.println(ply1);
    System.out.print("Avg Score: "+Double.toString(pl1avg));
    System.out.println(" Std Dev: " + Double.toString(pl1stdDev));
    //Print Player 2 Results
    System.out.println(ply2);
    System.out.print("Avg Score: "+Double.toString(pl2avg));
    System.out.println(" Std Dev: " + Double.toString(pl2stdDev));
    //Print Player 3 Results
    System.out.println(ply3);
    System.out.print("Avg Score: "+Double.toString(pl3avg));
    System.out.println(" Std Dev: " + Double.toString(pl3stdDev));
  }


private static double calculateAverage(List <Double> scores) {
  Double sum = 0.0;
  if(!scores.isEmpty()) {
    for (Double score : scores) {
        sum += score;
    }
    return sum / (double)scores.size();
  }
  return sum;
}
private static double meanSqrErr(List<Double> scores, Double mean){
  Double err = 0.0;
  for(int i=0; i<scores.size();i++){
    err+=Math.pow(scores.get(i)-mean,2);
  }
  Double meanErr = err/(double)scores.size();
  return meanErr;
}
    }
