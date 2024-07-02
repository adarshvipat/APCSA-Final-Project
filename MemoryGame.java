import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Constructs a memory game object and handles the calculations for a
 * functioning memory game
 * 
 * @author Adarsh Vipat
 * @version 5/24
 */
public class MemoryGame extends Game {

    private Point[][] points;
    private ArrayList<Point> orderedPoints;
    private ArrayList<Point> inputedPoints;
    private User myUser;
    private MemoryGameWindow window;
    private int numRounds;
    private final int MAXROUNDS = 15; // max # of rounds possible
    private final String GAMETYPE;

    /**
     * Constructs a memory game file
     * 
     * @param title Title of GUI window
     * @param hw    the homewindow that launched this game
     */
    public MemoryGame(String title, HomeWindow hw) {
        super(title, hw);
        GAMETYPE = "memory";
        points = new Point[3][3];
        orderedPoints = new ArrayList<Point>(MAXROUNDS);
        inputedPoints = new ArrayList<Point>(MAXROUNDS);

        numRounds = 0;
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                points[r][c] = new Point(r, c);
            }
        }
    }

    /**
     * Launches the window
     * 
     * @param user user launching the game
     */
    public void launch(User user) {

        window = new MemoryGameWindow("Memory Test", this);
        myUser = user;

    }

    /**
     * Plays the game once by:
     * 1. adding a random point to orderedPoints
     * 2. walking through ordered points, flashing each square
     */
    public void play() {
        addRandomPoint();

        Timer flashTimer = new Timer(500, null); // Delay of 1000 ms (1 second)
        final int[] index = { 0 };

        flashTimer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (index[0] < numRounds) {
                    int x = orderedPoints.get(index[0]).getX();
                    int y = orderedPoints.get(index[0]).getY();
                    SwingUtilities.invokeLater(() -> window.flashTile(x, y));
                    index[0]++;
                } else {
                    flashTimer.stop();
                    postPlayActions();

                }
            }
        });

        flashTimer.setInitialDelay(1000);
        flashTimer.start();
    }

    /**
     * Called after the game ends
     */
    private void postPlayActions() {
        Timer waitTimer = new Timer(numRounds * 200 + 400, e -> {
            ((Timer) e.getSource()).stop(); // Stop the timer after the action
            if (compareInOut()) {
                inputedPoints = new ArrayList<Point>(15); // empty input to start new round
                play(); // Recurse to play again
            } else {
                System.out.println("------------------------------------------");
                System.out.println("Failed");
                
                for (int r = 0; r < 3; r++) {
                    for (int c = 0; c < 3; c++) {
                        window.flashTile(r,c);
                    }
                }
                
                myUser.setMemScore(numRounds);
                setLeaderboard(myUser.requestLeaderboard(GAMETYPE));
                showLeaderboard(true);

                orderedPoints = new ArrayList<Point>(MAXROUNDS);
                inputedPoints = new ArrayList<Point>(MAXROUNDS);
                numRounds = 0;

            }
        });
        waitTimer.setRepeats(false); // Ensure it only runs once
        waitTimer.start();

    }

    /**
     * Comapres the input and output points
     * 
     * @return yes or no
     */
    private boolean compareInOut() {
        return orderedPoints.equals(inputedPoints);
    }

    /**
     * Adds a random point to the game
     */
    public void addRandomPoint() {
        Point p = getRandomPoint();
        orderedPoints.add(p);
        numRounds++;
    }

    /**
     * Adds a random point to the memory game
     * 
     * @return a point representing the point in the memory game
     */
    private Point getRandomPoint() {
        Point p;

        int r = (int) (Math.random() * 3);
        int c = (int) (Math.random() * 3);

        p = points[r][c];

        return p;
    }

    /**
     * Fetches the points in the game
     * 
     * @return the points
     */
    public ArrayList<Point> getOrderedPoints() {
        return orderedPoints;
    }

    /**
     * Fetches the input points of the game
     * 
     * @return the input points
     */
    public ArrayList<Point> getInputPoints() {
        return inputedPoints;
    }

    /**
     * Adds an input at (x, y)
     * 
     * @param x x
     * @param y y
     */
    public void addInput(int x, int y) {
        inputedPoints.add(new Point(x, y));
    }
}