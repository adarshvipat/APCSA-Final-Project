import javax.swing.*;
import java.awt.event.*;
import java.util.Random;

/**
 * Constructs a Reaction Time Game with GUI
 * 
 * @author Ashton Wu
 * @Version 5/24
 */
public class ReactionTimeGame extends Game {
    private ImageIcon initialImage, waitImage, clickImage, tooSoonImage;
    private JButton reactionButton;
    private long startTime;
    private Timer timer;
    private User myUser;
    private final String GAMETYPE;

    /**
     * Initializes a reaction time game
     * 
     * @param title
     * @param hw
     */
    public ReactionTimeGame(String title, HomeWindow hw) {
        super(title, hw);
        initializeGame();
        GAMETYPE = "react";
    }

    /**
     * Initializes a game
     */
    private void initializeGame() {
        // Load images
        initialImage = new ImageIcon("reactionbackground.png");
        waitImage = new ImageIcon("RedFulk.png");
        clickImage = new ImageIcon("GreenFulk.png");
        tooSoonImage = new ImageIcon("toosoon.png");

        // Initialize JButton
        reactionButton = new JButton(initialImage);
        reactionButton.setBounds(0, 0, 1280, 720);
        reactionButton.setFocusPainted(false);
        reactionButton.setBorderPainted(false);
        reactionButton.setContentAreaFilled(false);
        reactionButton.setActionCommand("start");
        reactionButton.addActionListener(new ButtonClickListener());

        // Set up frame
        this.setLayout(null);
        this.add(reactionButton);
    }

    /**
     * Launches the game
     * 
     * @param user the user who launched the game
     */
    public void launch(User user) {
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(1280, 720);
        this.setResizable(false);
        this.setVisible(true);
        myUser = user;
    }

    /**
     * a Private class used by reaction time game
     */
    private class ButtonClickListener implements ActionListener {
        @Override
        /**
         * Checks if an action was performed
         * 
         * @param e an action
         */
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();

            if (command.equals("start")) {
                reactionButton.setIcon(waitImage);
                reactionButton.setActionCommand("wait");

                Random rand = new Random();
                int delay = 1000 + rand.nextInt(4000); // Random delay between 1 and 5 seconds

                timer = new Timer(delay, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        reactionButton.setIcon(clickImage);
                        reactionButton.setActionCommand("react");
                        startTime = System.currentTimeMillis();
                    }
                });
                timer.setRepeats(false);
                timer.start();
            } else if (command.equals("wait")) {
                reactionButton.setIcon(tooSoonImage);
                reactionButton.setActionCommand("start");
                if (timer != null) {
                    timer.stop();
                }
            } else if (command.equals("react")) {
                long reactionTime = System.currentTimeMillis() - startTime;
                JOptionPane.showMessageDialog(null, "Your reaction time is: " + reactionTime + " milliseconds");

                // adarsh addded these 3 lines
                myUser.setReactScore(reactionTime);
                setLeaderboard(myUser.requestLeaderboard(GAMETYPE));
                System.out.println("showing reactiontime board");
                showLeaderboard();
                // those 3

                reactionButton.setIcon(initialImage);
                reactionButton.setActionCommand("start");
            }
        }
    }
}
