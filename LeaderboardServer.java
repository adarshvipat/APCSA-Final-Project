import java.io.*;
import java.net.*;
import java.util.concurrent.*;

/**
 * LeaderboardServer class to manage a server that handles leaderboard data for
 * different games.
 * This server listens for client connections and processes requests to get or
 * update leaderboard data.
 *
 * @version 1.0
 * @author Adarsh Vipat
 */
public class LeaderboardServer {

    private static final int PORT = 5000;
    private static Leaderboard memLeaderboard;
    private static Leaderboard aimLeaderboard;
    private static Leaderboard reactLeaderboard;

    /**
     * The main method to start the server and listen for client connections.
     * Initializes the leaderboards and handles incoming client requests.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(1); // A thread pool to handle multiple clients
        memLeaderboard = new Leaderboard();
        aimLeaderboard = new Leaderboard();
        reactLeaderboard = new Leaderboard();

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is listening on port " + PORT);

            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    executor.execute(new ClientHandler(socket)); // Handle each client in a separate thread
                } catch (IOException ex) {
                    System.out.println("Server exception: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }

        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            executor.shutdown();
        }
    }

    /**
     * Gets the leaderboard for the aim game.
     *
     * @return the aim game leaderboard
     */
    public static Leaderboard getAimLeaderboard() {
        return aimLeaderboard;
    }

    /**
     * Gets the leaderboard for the memory game.
     *
     * @return the memory game leaderboard
     */
    public static Leaderboard getMemLeaderboard() {
        return memLeaderboard;
    }

    /**
     * Gets the leaderboard for the reaction game.
     *
     * @return the reaction game leaderboard
     */
    public static Leaderboard getReactLeaderboard() {
        return reactLeaderboard;
    }

}

/**
 * ClientHandler class to handle the communication with a single client.
 * Processes client requests to get or update leaderboard data.
 */
class ClientHandler implements Runnable {
    private final Socket socket;

    /**
     * Constructor to initialize the ClientHandler with a client socket.
     *
     * @param socket the client socket
     */
    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    /**
     * Runs the client handler thread, processing client requests.
     * Reads the request type and processes accordingly to get or update leaderboard
     * data.
     */
    @Override
    public void run() {
        try (ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream())) {

            String requestType = (String) ois.readObject();
            System.out.println("---------------------------------------------");
            System.out.println(" Recieved request of type: " + requestType);

            if ("getLeaderboard".equals(requestType)) {
                String gameType = (String) ois.readObject();
                Leaderboard leaderboard = getLeaderboardByGameType(gameType);
                System.out.println("ClientHandler sent leaderboard: " + leaderboard);
                oos.writeObject(leaderboard);
                oos.flush();
            } else {
                // Handle score update
                User user = (User) ois.readObject();
                double score = ois.readDouble();
                updateLeaderboard(user, requestType, score);
                System.out.println("Received score update from client: " + user.getName() + ", Game: " + requestType
                        + ", Score: " + score);
            }

        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("Client handler exception: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException ex) {
                System.out.println("Error closing socket: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    /**
     * Updates the specified leaderboard with the given user's score.
     *
     * @param user  the user whose score needs to be updated
     * @param game  the game type for which the score needs to be updated
     * @param score the score to be updated
     */
    private void updateLeaderboard(User user, String game, double score) {
        synchronized (LeaderboardServer.class) {
            if (game.equals("memory")) {
                Leaderboard leaderboard = LeaderboardServer.getMemLeaderboard();
                leaderboard.addUser(score, user, game);
            } else if (game.equals("aim")) {
                Leaderboard leaderboard = LeaderboardServer.getAimLeaderboard();
                leaderboard.addUser(score, user, game);
            } else if (game.equals("react")) {
                Leaderboard leaderboard = LeaderboardServer.getReactLeaderboard();
                leaderboard.addUser(score, user, game);
            }
        }
    }

    /**
     * Gets the leaderboard corresponding to the specified game type.
     *
     * @param gameType the type of game for which the leaderboard is requested
     * @return the leaderboard for the specified game type, or null if the game type
     *         is invalid
     */
    private Leaderboard getLeaderboardByGameType(String gameType) {
        synchronized (LeaderboardServer.class) {
            switch (gameType) {
                case "memory":
                    return LeaderboardServer.getMemLeaderboard();
                case "aim":
                    return LeaderboardServer.getAimLeaderboard();
                case "react":
                    return LeaderboardServer.getReactLeaderboard();
                default:
                    return null;
            }
        }
    }
}
