package KemenyOptimiser;

public final class WMGParsingService {

    // Prevents instantiation
    private WMGParsingService() {}

    public static TournamentResults Parse(String[] fileContents) {
       int currentLine = 0;

       // Indexing Participants
       int numberOfParticipants = Integer.parseInt(fileContents[currentLine++]);
       String[] participants = new String[numberOfParticipants];
       for (int i = 0; i < numberOfParticipants; i++) {

           String[] pair = fileContents[currentLine++].split(",");

           int index = Integer.parseInt(pair[0]);
           String name = pair[1];

           participants[index - 1] = name;
       }

       TournamentResults results = new TournamentResults(participants);

        // Indexing Individual Matchups
       String[] metrics = fileContents[currentLine++].split(",");
       int numberOfMatchups = Integer.parseInt(metrics[2]);
        for (int i = 0; i < numberOfMatchups; i++) {

            String[] triple = fileContents[currentLine++].split(",");

            int score = Integer.parseInt(triple[0]);
            int participant1 = Integer.parseInt(triple[2]) - 1;
            int participant2 = Integer.parseInt(triple[1]) - 1;
            results.addMatchup(participant1, participant2, score);
        }

        return results;
    }
}
