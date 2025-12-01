import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginMenuController extends homeGuardianClientController {

    @FXML
    private Button loginButton;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button signUpButton;

    @FXML
    private TextField usernameField;

    /**
     * Called when the Login button is pressed.
     * Sends a "login" request to the server with username + password.
     */
    @FXML
    void loginButtonPressed(ActionEvent event) {

        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        // basic input check (you can replace with GUI error label later)
        if (username.isEmpty() || password.isEmpty()) {
            System.out.println("Username and password are required.");
            return;
        }

        // Build message for server: ["login", username, password]
        ArrayList<Object> msg = new ArrayList<>();
        msg.add("login");
        msg.add(username);
        msg.add(password);

        // send to server using base controller helper
        sendToServer(msg);

        // NOTE:
        // You should handle the server's response in your client class
        // (e.g., HomeGuardianClient.handleMessageFromServer),
        // and once login is confirmed, call switchScene(...)
        // to go to the main menu.
    }

    /**
     * Called when the Sign Up button is pressed.
     * Switches to the Sign Up screen.
     */
    @FXML
    void signUpButtonPressed(ActionEvent event) {
        // Change the FXML path below to match your actual Sign Up FXML file name
        switchScene(event, "signUp.fxml");
    }
}
