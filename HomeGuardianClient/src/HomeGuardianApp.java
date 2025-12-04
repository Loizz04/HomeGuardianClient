/*
 * Author: Lois Mathew
 */
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HomeGuardianApp extends Application {

    private static HomeGuardianClient client;

    @Override
    public void start(Stage stage) throws Exception {
        try {

        	Class.forName("LoginMenuController");
            System.out.println("[CLIENT] Class.forName(LoginMenuController) succeeded.");

            Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));

            Scene scene = new Scene(root);
            stage.setTitle("Home Guardian");
            stage.setScene(scene);
            stage.show();

            System.out.println("[CLIENT] JavaFX UI started (login.fxml loaded).");

        } catch (Exception e) {
            System.err.println("[CLIENT] Failed to start JavaFX UI (problem loading login.fxml?)");
            e.printStackTrace();
        }
    }


    //Creates a HomeGuardianClient (host, port)
    public static void main(String[] args) {
        try {
            client = new HomeGuardianClient("localhost", 12345);
            client.connectAndWait();
            System.out.println("[CLIENT] Connected to Home Guardian server.");
            homeGuardianClientController.initClient(client);

        } catch (Exception e) {//catch exceptions if not connyibg to server
            System.err.println("[CLIENT] Failed to connect to server. Not launching UI.");
            e.printStackTrace();
            return;  
        }
        launch(args);

        System.out.println("[CLIENT] Window closed, application terminated.");
    }
}
