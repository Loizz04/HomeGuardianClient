/*
 * Author: Lois Mathew
 */
import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Consumer;
import OCSF.ObservableSWRClient;
import OCSF.ClientUser;
import javafx.application.Platform;

//talks to server for the app
//keeps copy of current device states
//wraps 
public class HomeGuardianClient extends ObservableSWRClient {

    //Singleton-style instance
	//this is why there is only one client
    private static HomeGuardianClient instance;

    private ClientUser clientUser = new ClientUser();

    private Consumer<Object> messageHandler;

    public HomeGuardianClient(String host, int port) {
        super(host, port);
        instance = this;
    }
   //Global pointer to the client being used by the whole app
    public static HomeGuardianClient getInstance() {return instance;}

    public ClientUser getClientUser() {return clientUser;}

    public void setClientUser(ClientUser clientUser) {this.clientUser = clientUser;}


    public void setMessageHandler(Consumer<Object> handler) {
        this.messageHandler = handler;
    }

    @Override
    protected void handleMessageFromServer(Object msg) {
        if (msg instanceof ClientUser) {
            this.clientUser = (ClientUser) msg;
        }
        if (messageHandler != null) {
            Platform.runLater(()->messageHandler.accept(msg));
        }
    }

    public void sendCommand(ArrayList<Object> command) {
        try {
            sendToServer(command);
        } catch (IOException e) {
            System.err.println("Error sending command to server: " + e.getMessage());
        }
    }

    public void sendCommand(String command) {
        try {
            sendToServer(command);
        } catch (IOException e) {
            System.err.println("Error sending command to server: " + e.getMessage());
        }
    }

    public void connectToServer() {
        try {
            openConnection();
            System.out.println("Connected to server.");
        } catch (IOException e) {
            System.err.println("Could not connect to server: " + e.getMessage());
        }
    }

    public void disconnectFromServer() {
        try {
            closeConnection();
            System.out.println("Disconnected from server.");
        } catch (IOException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }
}
