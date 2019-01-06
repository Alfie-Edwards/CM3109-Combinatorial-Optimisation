package KemenyOptimiser;

import java.util.Random;


public class SimulatedAnnealingModel {

    private static final Random random = new Random();
    private int numberOfUphillMoves;


    public int getNumberOfUphillMoves() {
        return numberOfUphillMoves;
    }

    // Simulated Annealing Code
    public Ranking runSimulatedAnnealing(Ranking initialRanking, double initialTemperature, int temperatureLength, double coolingRatio, int numNonImprove)
    {
        Ranking bestRanking = initialRanking;
        Ranking currentRanking = initialRanking;

        double temperature = initialTemperature;
        numberOfUphillMoves = 0;
        int iterationsSinceBestFound = 0;

        while (iterationsSinceBestFound < numNonImprove) // Termination Condition
        {
            for (int i = 0; i < temperatureLength; i++)
            {
                Ranking potentialRanking = currentRanking.getRandomNeighbouringRanking();
                double changeCost = potentialRanking.getKemenyScore() - currentRanking.getKemenyScore();

                if (changeCost <= 0) {
                    currentRanking = potentialRanking;

                    if (currentRanking.getKemenyScore() < bestRanking.getKemenyScore())
                        bestRanking = currentRanking;
                    else iterationsSinceBestFound++;
                }
                else {
                    double changeProbability = Math.pow(Math.E, -changeCost / temperature);
                    if (changeProbability > random.nextDouble()) {
                        currentRanking = potentialRanking;
                        numberOfUphillMoves++;
                    }

                    iterationsSinceBestFound++;
                }
            }
            temperature = temperature * coolingRatio;
        }

        return bestRanking;
    }
}
