import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.stream.Collectors;

import javax.swing.*;
import java.awt.*;

/**
 * Abstract Class representing what a game needs to be able to do.
 * Holds the leaderboard object for itself, and can show the leaderboard through JFrame.
 * 
 * @Author Adarsh Vipat
 * @version 1.0
 */
public abstract class Game extends JFrame {

    private Leaderboard leaderboard;

    /**
     * Constructs a Game object
     * 
     * @param title title of GUI window
     * @param hw    a HomeWindow
     */
    public Game(String title, HomeWindow hw) {
        super(title);
        leaderboard = new Leaderboard();
    }

    /**
     * Launches the game, expecting different subclass games to do different things
     * 
     * @param user user that is launching the object
     */
    public abstract void launch(User user);

    /**
     * Gets a leaderboard
     * 
     * @return the leaderboard
     */
    public Leaderboard getLeaderboard() {
        return leaderboard;
    }

    /**
     * Sets the private field leaderboard
     * 
     * @param lb the leaderboard to be set
     */
    public void setLeaderboard(Leaderboard lb) {
        leaderboard = lb;
    }

    /**
     * Shows the leaderboard to the client through GUI
     */
    public void showLeaderboard() {
        SortedMap<User, Double> map = leaderboard.getMap();
        JFrame frame = new JFrame("Leaderboard");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(300, 300);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel headerLabel = new JLabel("Top 10 Leaderboard");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(headerLabel);

        int count = 0;

        Map<User, Double> result = map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
        for (Map.Entry<User, Double> entry : result.entrySet()) {
            if (count >= 10)
                break;
            double score = entry.getValue();
            User user = entry.getKey();
            JLabel entryLabel = new JLabel((count + 1) + ". " + user.getName() + ": " + (int) score);
            entryLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            panel.add(entryLabel);
            count++;
        }
        JScrollPane scrollPane = new JScrollPane(panel);
        frame.add(scrollPane);
        frame.setVisible(true);
    }

    public void showLeaderboard(boolean bool) {
        SortedMap<User, Double> map = leaderboard.getMap();
        JFrame frame = new JFrame("Leaderboard");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(300, 300);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel headerLabel = new JLabel("Top 10 Leaderboard");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(headerLabel);

        int count = 0;

        Map<User, Double> result = map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
        for (Map.Entry<User, Double> entry : result.entrySet()) {
            if (count >= 10)
                break;
            double score = entry.getValue();
            User user = entry.getKey();
            JLabel entryLabel = new JLabel((count + 1) + ". " + user.getName() + ": " + (int) score);
            entryLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            panel.add(entryLabel);
            count++;
        }
        JScrollPane scrollPane = new JScrollPane(panel);
        frame.add(scrollPane);
        frame.setVisible(true);
    }
}
