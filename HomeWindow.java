import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * HomeWindow is the main window for the Human Benchmark application. It
 * displays
 * a background and buttons for different game tests. It also handles user
 * login.
 * 
 * @author Adarsh Vipat, Ashton Wu, Jan Fong Lim
 * @version 1.0
 */
public class HomeWindow extends JFrame implements ActionListener {
    private ArrayList<Game> games;
    private ImageIcon background;
    private JLabel backgroundLabel;
    private JFrame loginFrame;
    private JTextField userField;
    private JTextField ipField;
    private ArrayList<User> users;

    /**
     * Constructs a HomeWindow instance, initializing the main window, games,
     * background,
     * and login window.
     */
    public HomeWindow() {
        // Main window setup
        super("Human Benchmark");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1280, 720);
        this.setResizable(false);

        // Objects
        users = new ArrayList<User>();
        games = new ArrayList<Game>(3);

        // Adds all the game instances to the games arraylist
        games.add(new MemoryGame("Memory Test", this));
        games.add(new AimGame("Aim Test", this));
        games.add(new ReactionTimeGame("Reaction Time Test", this));

        // Background
        background = new ImageIcon("background.png");
        backgroundLabel = new JLabel(background);
        this.setLayout(null);
        backgroundLabel.setBounds(0, 0, 1280, 720);
        this.getContentPane().add(backgroundLabel);

        // Button
        addButtons();

        // Make sure the button is visible over the background
        backgroundLabel.setLayout(null);

        setVisible(false);

        // Open login window
        showLogin();
    }

    /**
     * Adds buttons for the different games to the main window.
     */
    private void addButtons() {
        JButton jb1 = new JButton("Play");
        jb1.setBounds(340 - 75 - 30 - 100, 500 + 110, 150, 50);
        jb1.addActionListener(this);
        this.getContentPane().add(jb1);
        jb1.setActionCommand("aim");
        backgroundLabel.add(jb1);

        JButton jb2 = new JButton("Play");
        jb2.setBounds(640 - 75, 500 + 100 + 10, 150, 50);
        jb2.addActionListener(this);
        this.getContentPane().add(jb2);
        jb2.setActionCommand("reactiontime");
        backgroundLabel.add(jb2);

        JButton jb3 = new JButton("Play");
        jb3.setBounds(940 - 75 + 30 + 100, 500 + 110, 150, 50);
        jb3.addActionListener(this);
        this.getContentPane().add(jb3);
        jb3.setActionCommand("memory");
        backgroundLabel.add(jb3);
    }

    /**
     * Displays the login window where users can enter their username.
     */
    private void showLogin() {
        loginFrame = new JFrame("Login");
        loginFrame.setSize(300, 200); // Adjust size to accommodate the additional textbox
        loginFrame.setLayout(new GridLayout(3, 2));

        JLabel userLabel = new JLabel("Username: ");
        userField = new JTextField();

        JLabel ipLabel = new JLabel("IP Address: ");
        ipField = new JTextField();

        JButton loginButton = new JButton("Login");
        loginButton.setActionCommand("login");
        loginButton.addActionListener(this);

        loginFrame.add(userLabel);
        loginFrame.add(userField);
        loginFrame.add(ipLabel);
        loginFrame.add(ipField);
        loginFrame.add(new JLabel()); // Placeholder to align the button
        loginFrame.add(loginButton);

        loginFrame.setLocationRelativeTo(this);
        loginFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        loginFrame.setVisible(true);
    }

    /**
     * Gets the list of games.
     * 
     * @return the list of games
     */
    public ArrayList<Game> getGames() {
        return games;
    }

    /**
     * Gets the latest user to launch the program
     * 
     * @return the user that was just added to this HomeWindow object
     */
    public User getLatestUser() {
        return users.get(users.size() - 1);
    }

    /**
     * Handles action events for the buttons in the main window and login window.
     * 
     * @param e the action event
     */
    public void actionPerformed(ActionEvent e) {
        String s = e.getActionCommand();
        if (s.equals("reactiontime")) {
            games.get(0).launch(getLatestUser());
        } else if (s.equals("memory")) {
            games.get(1).launch(getLatestUser());
        } else if (s.equals("aim")) {
            games.get(2).launch(getLatestUser());
        } else if (s.equals("login")) {
            String ipAddress = ipField.getText();
            users.add(new User(userField.getText(), this, ipAddress));
            System.out.println("Added user: " + userField.getText() + " with host add: " + ipAddress);
            loginFrame.dispose();
            setVisible(true);
        }
    }
}