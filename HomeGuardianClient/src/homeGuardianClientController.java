
import java.io.IOException;
import java.util.ArrayList;

import OCSF.ClientUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import OCSF.ObservableSWRClient;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

/**
 * Shared base controller for all JavaFX screens.
 * Holds static references to:
 *  - HomeGuardianClient (network connection)
 *  - ClientUser (current user + devices state)
 *
 * Also provides:
 *  - Helper method to send messages to server
 *  - Helper method to switch scenes
 */
public abstract class homeGuardianClientController {

    protected static HomeGuardianClient client;
    protected static ClientUser clientUser;

    public static void initClient(HomeGuardianClient c) {
        client = c;
    }

    public static HomeGuardianClient getClient() {
        return client;
    }

    public static void setClientUser(ClientUser u) {
        clientUser = u;
    }

    public static ClientUser getClientUser() {
        return clientUser;
    }

    // ---------- Scene switching helper ----------

    protected void switchScene(ActionEvent event, String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error switching scene to " + fxmlPath + ": " + e.getMessage());
        }
    }

    // ---------- Helper methods to talk to the server ----------

    /**
     * Send an ArrayList-based command to the server.
     * First element = String command name.
     */
    protected void sendToServer(ArrayList<Object> msg) {
        if (client != null) {
            client.sendCommand(msg);
        } else {
            System.err.println("Client is null - did you call initClient()?");
        }
    }

    /**
     * Send a plain String command (if your protocol uses Strings).
     */
    protected void sendToServer(String msg) {
        if (client != null) {
            client.sendCommand(msg);
        } else {
            System.err.println("Client is null - did you call initClient()?");
        }
    }
}
