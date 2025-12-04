/*
 * Author: Lois Mathew
 */
import java.io.IOException;

import java.util.ArrayList;

import OCSF.ClientUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
    

    public static void initClient(HomeGuardianClient c) {
        client = c;
    }
//allow any controller to hget the client
    public static HomeGuardianClient getClient() {
        return client;
    }


    //any controller can ask for the latest ClientUser
    public static ClientUser getClientUser() {
        if (client == null) {
            return null;
        }
        return client.getClientUser();
    }

    protected void switchScene(Event event, String fxmlPath) {
        if (event == null) {
            System.err.println("switchScene called with null event for " + fxmlPath);
            return;
        }

        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Node sourceNode = (Node) event.getSource();
            Stage stage = (Stage) sourceNode.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error switching scene to " + fxmlPath + ": " + e.getMessage());
        } catch (ClassCastException e) {
            System.err.println("Event source is not a Node; cannot switch scene to " + fxmlPath);
        }
    }


//Methods to talk to server 
    protected void sendToServer(ArrayList<Object> msg) {
        if (client != null) {
            client.sendCommand(msg);
        } else {
            System.err.println("Client is null");
        }
    }

//for string msgs
    protected void sendToServer(String msg) {
        if (client != null) {
            client.sendCommand(msg);
        } else {
            System.err.println("Client is null");
        }
    }
}
