import java.io.Serializable;
import java.util.*;

/**
 * Leaderboard class to manage scores of users for various games.
 * This class maintains a sorted map of users and their scores.
 *
 * @version 1.0
 * @author Adarsh Vipat
 */
public class Leaderboard implements Serializable {

    private SortedMap<User, Double> map;

    /**
     * Constructor to initialize an empty leaderboard.
     * The leaderboard is backed by a TreeMap to maintain order.
     */
    public Leaderboard() {
        map = new TreeMap<User, Double>();
    }

    /**
     * Adds a user to the leaderboard with the given score for a specific game.
     * If the user already exists, updates the score if the new score is higher.
     *
     * @param score the score to be added or updated
     * @param user  the user whose score is to be added or updated
     * @param game  the game for which the score is to be added or updated
     */
    public void addUser(double score, User user, String game) {
        if (map.containsKey(user)) {
            if (game.equals("memory")) {
                map.put(user, Math.max(score, map.get(user)));
            } else {
                map.put(user, Math.min(score, map.get(user)));
            }
        } else {
            map.put(user, score);
        }
    }

    /**
     * Gets the map of users and their scores.
     *
     * @return the sorted map of users and scores
     */
    public SortedMap<User, Double> getMap() {
        return map;
    }

    /**
     * Returns a string representation of the leaderboard.
     *
     * @return a string representation of the leaderboard
     */
    @Override
    public String toString() {
        return map.toString();
    }
}
