package KemenyOptimiser;

import java.util.Random;

public class Ranking {

    private static Random random = new Random();

    private final int[] ranking;
    private final TournamentResults results;
    private final int kemenyScore;


    public Ranking(TournamentResults results, int[] ranking, int kemenyScore) {
        this.ranking = ranking;
        this.results = results;
        this.kemenyScore = kemenyScore;
    }


    public Ranking(TournamentResults results, int[] ranking) {
        this.ranking = ranking;
        this.results = results;
        kemenyScore = calculateKemenyScore();
    }


    public int getKemenyScore() {
        return kemenyScore;
    }


    // Takes a random participant and moves them to a random (different) position
    public Ranking getRandomNeighbouringRanking() {
        int oldRank, newRank;

        // Pick 2 different random ranks
        oldRank = (int) (random.nextDouble() * ranking.length);
        int difference;
        do {
            newRank = (int) (random.nextDouble() * ranking.length);
            difference = Math.abs(newRank - oldRank);
        }
        // If they are the same rank, regenerate the second rank
        // If the ranks are adjacent, there are 2 ways to reach that neighbour so regenerate 50% of the time
        while (difference == 0 || ((difference == 1) && (random.nextDouble() < 0.5)));

        int[] newRanking = new int[ranking.length];

        newRanking[newRank] = ranking[oldRank];

        int source = 0, target = 0;
        while (target < newRanking.length && source < newRanking.length) {
            if (source == oldRank) source++;
            if (target == newRank) target++;

            newRanking[target] = ranking[source];

            source++;
            target++;
        }

        return new Ranking(results, newRanking, calculateKemenyScoreForNeighbour(oldRank, newRank));
    }


    private int calculateKemenyScoreForNeighbour(int oldRank, int newRank) {
        int participant = ranking[oldRank];
        int kemenyScore = this.kemenyScore;

        // Determine which direction to iterate in to move from the new rank to the old rank
        int increment = (newRank < oldRank) ? 1 : -1;
        // Stop iterating when the old rank is reached
        for (int i = newRank; i != oldRank; i += increment) {
            Integer result = results.getMatchup(participant, ranking[i]);
            if (result == null)
                continue;
            if ((oldRank > i) ^ (result > 0))
                kemenyScore -= Math.abs(result);
            else
                kemenyScore += Math.abs(result);
        }

        return kemenyScore;
    }


    private int calculateKemenyScore() {
        // Generate array of [id] -> rank
        // This makes it easier to look up ranks
        int[] positions = new int[ranking.length];
        for (int i = 0; i < ranking.length; i++)
            positions[ranking[i]] = i;

        int kemenyScore = 0;

        // For each pair of participants
        for (int participant1 = 0; participant1 < ranking.length - 1; participant1++)
            for (int participant2 = participant1 + 1; participant2 < ranking.length; participant2++) {
                Integer result = results.getMatchup(participant1, participant2);
                if (result == null) continue;
                // If ranking disagrees with results, add result to Kemeny score;
                if ((positions[participant1] > positions[participant2]) ^ (result > 0))
                    kemenyScore += Math.abs(result);
            }
        return kemenyScore;
    }


    @Override
    public String toString() {
        if (ranking.length == 0)
            return "";

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("POS  NAME\n");

        for (int i = 0; i < ranking.length; i++) {
            String name = results.getParticipantName(ranking[i]);
            stringBuilder.append(String.format("%0$3d  %s\n", i + 1, name));
        }

        return stringBuilder.toString();
    }
}
