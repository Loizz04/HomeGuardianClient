
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HomeGuardianApp extends Application {

    private static HomeGuardianClient client;

    @Override
    public void start(Stage stage) throws Exception {
        // Load your login screen
        // Make sure the string matches your actual FXML file name:
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));

        Scene scene = new Scene(root);
        stage.setTitle("Home Guardian");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        System.out.println("JavaFX application is stopping...");

        try {
            if (homeGuardianClientController.getClient() != null) {
                homeGuardianClientController.getClient().sendCommand("logout");
                homeGuardianClientController.getClient().closeConnection();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.stop();
    }

    public static void main(String[] args) {
        try {
            client = new HomeGuardianClient("localhost", 12345);
            client.connectAndWait();
            System.out.println("Connected to Home Guardian server.");

            homeGuardianClientController.initClient(client);

        } catch (Exception e) {
            System.err.println("Failed to connect to server. Not launching UI.");
            e.printStackTrace();
            return;  // Don’t launch UI if no server connection
        }

        //Launch JavaFX
        launch(args);

        System.out.println("Window closed, application terminated.");
    }
}
