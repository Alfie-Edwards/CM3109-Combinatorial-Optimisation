package KemenyOptimiser;

import java.util.HashMap;

public class TournamentResults {

    private final HashMap<Matchup, Integer> matchups;
    private final String[] participants;

    public TournamentResults(String[] participants) {
        this.participants = participants;
        this.matchups = new HashMap<>();
    }

    public void addMatchup(int participant1, int participant2, int score) {
        Matchup matchup = new Matchup(participant1, participant2);
        matchups.put(matchup, score);
    }

    public Integer getMatchup(int participant1, int participant2) {
        Matchup matchup = new Matchup(participant1, participant2);

        Integer result = matchups.get(matchup);
        if ( result != null ) return result;

        result = matchups.get(matchup.reverse());
        if ( result != null ) return -result;

        return null;
    }

    public String getParticipantName(int participant) {
        return participants[participant];
    }

    // A wrapper class for an ordered pair of participants
    // Used as keys in the hash map
    private class Matchup {
        private final int participant1;
        private final int participant2;

        public Matchup(int participant1, int participant2) {

            this.participant1 = participant1;
            this.participant2 = participant2;
        }

        public Matchup reverse() {
            return new Matchup(participant2, participant1);
        }

        @Override
        public boolean equals(Object o) {
            if (o.getClass() != this.getClass()) return false;

            Matchup matchup = (Matchup) o;
            return this.participant1 == matchup.participant1 && this.participant2 == matchup.participant2;
        }

        @Override
        public int hashCode() {
            // Use first 16 bits for participant 1 and last 16 bits for participant 2
            // System is limited to 2^16 participants
            return this.participant1 * 65536 + participant2;
        }
    }
}
