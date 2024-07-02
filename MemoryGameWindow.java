import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;

/**
 * A memory game window representation.
 * 
 * @author Adarsh Vipat
 * @version 5/24
 */
public class MemoryGameWindow extends JFrame implements ActionListener {

    private JButton start;
    private JLabel backgroundLabel;
    private MemoryGame memGame;
    private JButton[][] buttons;

    /**
     * Constructs a memory game window
     * 
     * @param str window header
     * @param mg  memory game the window belongs to
     */
    public MemoryGameWindow(String str, MemoryGame mg) {
        super(str);

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(1280, 720);
        this.setResizable(false);

        buttons = new JButton[3][3];
        memGame = mg;
        start = new JButton("Start Game");
        start.setActionCommand("start");
        start.setBounds(1280 / 2 - 100, 720 / 2 - 30, 200, 60);
        start.addActionListener(this);

        backgroundLabel = new JLabel();
        backgroundLabel.setBounds(0, 0, 1280, 720);
        backgroundLabel.setLayout(null);
        backgroundLabel.add(start); // Add button to background label

        this.getContentPane().add(backgroundLabel);

        JLabel instruct = new JLabel("Repeat the pattern displayed");
        instruct.setBounds(1280 / 2 - 100, 720 / 2 - 100, 200, 30);
        backgroundLabel.add(instruct); // Add instruction to background label

        this.setVisible(true);
    }

    /**
     * Starts the game
     * 
     * @param parentFrame parent frame of the game
     */
    public void startGame(JFrame parentFrame) {
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(300, 300);
        this.getContentPane().remove(backgroundLabel);
        this.getContentPane().remove(start);
        this.setResizable(false);
        this.setLayout(new GridLayout(3, 3));

        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new JButton("");
                buttons[i][j].setActionCommand(i + "," + j);
                buttons[i][j].addActionListener(this);
                this.add(buttons[i][j]);
            }
        }

        this.setVisible(true);

    }

    /**
     * flashes a tile at x, y coordinate
     * 
     * @param x x
     * @param y y
     */
    public void flashTile(int x, int y) {
        JButton button = buttons[x][y];
        Color originalColor = button.getBackground();

        button.setBackground(Color.BLUE);
        button.repaint(); // Force repaint to immediately show the color change

        Timer timer = new Timer(200, e -> {
            button.setBackground(originalColor);
            button.repaint(); // Force repaint to immediately revert the color change
        });
        timer.setRepeats(false);
        timer.start();
    }

    /**
     * Checks if an action was performed
     * 
     * @param e an action
     */
    public void actionPerformed(ActionEvent e) {
        String s = e.getActionCommand();
        if (s.equals("start")) {
            startGame(this);
            memGame.play();
        } else {
            String[] parts = s.split(",");
            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);
            memGame.addInput(x, y);
        }
    }
}