import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * An Object that Represents an AimGame. Runs an Aimgame when launch() is
 * called.
 * 
 * @Author Jan Fong Lim
 * @version 5/24
 */
public class AimGame extends Game implements ActionListener {

    private ImageIcon background;
    private long start;
    private int score;
    private int displayed;
    private User myUser;
    private boolean actionPerformed;
    private JButton backgroundLabel;
    private final String GAMETYPE;
    private JButton quit;

    /**
     * Constructor for AimGame
     * 
     * @param title GUI title
     * @param hw    Home Window
     */
    public AimGame(String title, HomeWindow hw) {
        super(title, hw);
        GAMETYPE = "aim";
    }

    /**
     * Launches the AimGame
     * 
     * @param user the user who wishes to launch the program, stored for use with
     *             leaderboard system
     */
    public void launch(User user) {
        background = new ImageIcon("RedFulk.png");
        Image image = background.getImage();
        Image neew = image.getScaledInstance(100, 100, java.awt.Image.SCALE_SMOOTH);
        background = new ImageIcon(neew);
        backgroundLabel = new JButton(background);
        this.setLayout(null);
        this.getContentPane().removeAll();
        start = 0;
        score = 0;
        displayed = 0;
        actionPerformed = false;
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1280, 720);
        this.setResizable(false);
        this.setVisible(true);
        myUser = user;
        game();
    }

    /**
     * Executes the main game. Which shows targets.
     */
    public void game() {
        displayed = 0;
        backgroundLabel.setActionCommand("hit");
        backgroundLabel.addActionListener(this);
        backgroundLabel.setBounds((int) (Math.random() * 800 + 100), (int) (Math.random() * 600 + 50), 100, 100);
        this.getContentPane().add(backgroundLabel);
        start = System.currentTimeMillis();
    }

    /**
     * Closes the window & Shows leaderboard
     */
    public void quit() {
        this.getContentPane().removeAll();
        quit = new JButton();
        JLabel aLabel = new JLabel(score / 30 + " " + "ms");
        aLabel.setFont(new Font("Times New Roman", Font.PLAIN, 400));
        quit.add(aLabel);
        quit.setBounds(0, 0, 1280, 720);
        this.getContentPane().add(quit);
        quit.addActionListener(this);
        quit.setActionCommand("quit");

        // adarsh added these two lines
        myUser.setAimScore(score / 30);
        setLeaderboard(myUser.requestLeaderboard(GAMETYPE));
        // those two

        showLeaderboard();
    }

    /**
     * Called when a button is clicked
     * 
     * @param e an action
     */
    public void actionPerformed(ActionEvent e) {
        String s = e.getActionCommand();
        if (s.equals("hit")) {
            backgroundLabel.setLocation((int) (Math.random() * 800 + 100), (int) (Math.random() * 600 + 50));
            this.getContentPane().add(backgroundLabel);
            score += (new Date()).getTime() - start;
            displayed++;
            if (displayed >= 10) {
                quit();
            }
        }
        if (s.equals("quit")) {
            this.getContentPane().remove(quit);
            this.setVisible(false);
        }
    }
}
