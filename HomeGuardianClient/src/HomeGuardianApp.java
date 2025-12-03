import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HomeGuardianApp extends Application {

    // Keep a static reference so main() and stop() can see the client
    private static HomeGuardianClient client;

    @Override
    public void start(Stage stage) throws Exception {
        try {
            // Test: can the classloader see LoginMenuController?

        	Class.forName("LoginMenuController");
            System.out.println("[CLIENT] Class.forName(LoginMenuController) succeeded.");

            // Load the login screen FXML
            // -> Make sure this file exists: login.fxml
            Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));

            Scene scene = new Scene(root);
            stage.setTitle("Home Guardian");
            stage.setScene(scene);
            stage.show();

            System.out.println("[CLIENT] JavaFX UI started (login.fxml loaded).");

        } catch (Exception e) {
            System.err.println("[CLIENT] Failed to start JavaFX UI (problem loading login.fxml?)");
            e.printStackTrace();
            // If start() throws, the app will exit
        }
    }

    @Override
    public void stop() throws Exception {
        System.out.println("[CLIENT] Stopping HomeGuardianApp...");
        // Optional: clean shutdown logic
        if (client != null) {
            try {
                client.closeConnection();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.stop();
    }

    public static void main(String[] args) {
        try {
            // 1) Create the client
            // MUST match your server port: 12345
            client = new HomeGuardianClient("localhost", 12345);

            // 2) Connect to the server (blocking until connected or exception)
            client.connectAndWait();
            System.out.println("[CLIENT] Connected to Home Guardian server.");

            // 3) Inject the client into the shared base controller
            homeGuardianClientController.initClient(client);

        } catch (Exception e) {
            System.err.println("[CLIENT] Failed to connect to server. Not launching UI.");
            e.printStackTrace();
            return;  // Don’t launch UI if no server connection
        }

        // 4) Launch JavaFX
        launch(args);

        System.out.println("[CLIENT] Window closed, application terminated.");
    }
}
