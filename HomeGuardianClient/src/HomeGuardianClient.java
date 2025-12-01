// File: homeguardian/client/HomeGuardianClient.java

import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Consumer;

import OCSF.ObservableSWRClient;
import OCSF.ClientUser;
import javafx.application.Platform;

/**
 * HomeGuardianClient
 *
 * Wraps the OCSF ObservableSWRClient and:
 *  - Maintains a shared ClientUser object (mirrors server-side state)
 *  - Provides helper methods for sending commands to the server
 *  - Dispatches messages from the server back to the JavaFX UI thread
 */
public class HomeGuardianClient extends ObservableSWRClient {

    // Singleton-style instance so all controllers can access one client
    private static HomeGuardianClient instance;

    // Local copy of the logged-in user & device states
    private ClientUser clientUser = new ClientUser();

    // Optional callback to push raw messages to whoever is interested (controller)
    private Consumer<Object> messageHandler;

    public HomeGuardianClient(String host, int port) {
        super(host, port);
        instance = this;
    }

    public static HomeGuardianClient getInstance() {
        return instance;
    }

    public ClientUser getClientUser() {
        return clientUser;
    }

    public void setClientUser(ClientUser clientUser) {
        this.clientUser = clientUser;
    }

    /**
     * Optional: set a handler that will receive every message from the server
     * on the JavaFX Application Thread.
     */
    public void setMessageHandler(Consumer<Object> handler) {
        this.messageHandler = handler;
    }

    /**
     * Core OCSF callback: every message from the server comes here.
     */
    @Override
    protected void handleMessageFromServer(Object msg) {
        // If server sends a fresh ClientUser object, update local model
        if (msg instanceof ClientUser) {
            this.clientUser = (ClientUser) msg;
        }

        // Forward to whoever is listening (e.g., current screen controller)
        if (messageHandler != null) {
            Platform.runLater(() -> messageHandler.accept(msg));
        }
    }

    /**
     * Convenience wrapper to send an ArrayList-based command.
     * First element should be a String command, e.g. "login", "signup", "toggleLight"
     */
    public void sendCommand(ArrayList<Object> command) {
        try {
            sendToServer(command);
        } catch (IOException e) {
            System.err.println("Error sending command to server: " + e.getMessage());
        }
    }

    /**
     * Example: send a simple String message (if you end up using plain strings)
     */
    public void sendCommand(String command) {
        try {
            sendToServer(command);
        } catch (IOException e) {
            System.err.println("Error sending command to server: " + e.getMessage());
        }
    }

    /**
     * Helper to connect to the server.
     */
    public void connectToServer() {
        try {
            openConnection();
            System.out.println("Connected to server.");
        } catch (IOException e) {
            System.err.println("Could not connect to server: " + e.getMessage());
        }
    }

    /**
     * Helper to disconnect cleanly.
     */
    public void disconnectFromServer() {
        try {
            closeConnection();
            System.out.println("Disconnected from server.");
        } catch (IOException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }
}
