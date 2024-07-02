import java.io.*;
import java.net.*;

/**
 * This class represents a user of any of the games.
 * This class also functions as the client for all networking operations
 * 
 * @version 1.0
 * @author Adarsh Vipat
 */
public class User implements Serializable, Comparable<User> {
    private String name;
    private double reactScore;
    private double memoryScore;
    private double aimScore;
    private HomeWindow homeWindow;
    private static final long serialVersionUID = 6529685098267757690L;

    private final String IP;
    private static final int PORT = 5000;

    /**
     * Constructs a user
     * 
     * @param nm name
     * @param hw Homewindow
     * @param ipAddress IPv4 of the Server
     */
    public User(String nm, HomeWindow hw, String ipAddress) {
        name = nm;
        homeWindow = hw;
        IP = ipAddress;
        reactScore = 200000000;
        aimScore = 200000000;
    }

    /**
     * Gets name
     * 
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets score for reaction game
     * 
     * @return reaction game score
     */
    public double getReactScore() {
        return reactScore;
    }

    /**
     * Gets score for memory game
     * 
     * @return memory game score
     */
    public double getMemScore() {
        return memoryScore;
    }

    /**
     * Gets score of aim game
     * 
     * @return aim game score
     */
    public double getAimScore() {
        return aimScore;
    }

    /**
     * Sets react game score
     * 
     * @param score react game score
     */
    public void setReactScore(double score) {
        if (score < reactScore) {
            reactScore = score;
            sendScoreUpdate("react", score);
        }
    }

    /**
     * Sets memory game score
     * 
     * @param score memory game score
     */
    public void setMemScore(double score) {
        if (score > memoryScore) {
            memoryScore = score;

            sendScoreUpdate("memory", score);
        }
    }

    /**
     * Sets aim game score
     * 
     * @param score aim game score
     */
    public void setAimScore(double score) {
        if (score < aimScore) {
            aimScore = score;
            sendScoreUpdate("aim", score);
        }
    }

    public String toString() {
        return name;
    }

    public int compareTo(User other) {
        return name.compareTo(other.getName());
    }

    /**
     * Sends score update to the server
     * 
     * @param gameType the type of game (react, memory, aim)
     * @param score    the score to be updated
     */
    private void sendScoreUpdate(String gameType, double score) {
        System.out.println("---------------------------------------------");
        System.out.println("HOST: " + IP + " PORT" + PORT);
        try (Socket socket = new Socket(IP, PORT);
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream())) {

            oos.writeObject(gameType); // Send the type of game
            //System.out.println("Sent: gameType");
            oos.writeObject(this); // Send the entire user object
            //System.out.print(", user");
            oos.writeDouble(score); // Send the score
            //System.out.print(", score");
            oos.flush();

            oos.close();
            socket.close();
            System.out.println("Score update sent to the server.\n User:" + this + " " + gameType + " " + score);

        } catch (IOException ex) {
            System.out.println("Client exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Requests the updated leaderboard from the server
     * 
     * @param gameType which of the three games' leaderboards the user is requesting
     * @return
     */
    public Leaderboard requestLeaderboard(String gameType) {
        try (Socket socket = new Socket(IP, PORT);
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())) {

            oos.writeObject("getLeaderboard"); // Request type
            oos.writeObject(gameType); // Game type

            Leaderboard leaderboard = (Leaderboard) ois.readObject(); // Read the leaderboard from the server
            System.out.println("Received leaderboard for " + gameType + " game from server.");
            oos.close();
            socket.close();
            return leaderboard;

        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("Client exception: " + ex.getMessage());
            ex.printStackTrace();
            return null;
        }
    }
}
