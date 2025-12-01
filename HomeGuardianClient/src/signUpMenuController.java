import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class signUpMenuController extends homeGuardianClientController {

    @FXML
    private TextField emailField;

    @FXML
    private Label loginLink;

    @FXML
    private TextField nameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button signUpButton;

    @FXML
    private TextField usernameField;

    /**
     * When the user clicks the "Login" link at the bottom.
     * Switches back to the Login screen.
     */
    @FXML
    void loginLinkPressed(MouseEvent event) {
        switchScene(event, "login.fxml");
    }

    /**
     * Sends a signup request to the server.
     */
    @FXML
    void signUpButtonPressed(ActionEvent event) {

        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        // Basic validation (optional)
        if (name.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty()) {
            System.out.println("All fields are required.");
            return;
        }

        // Build message to server: ["signup", name, email, username, password]
        ArrayList<Object> msg = new ArrayList<>();
        msg.add("signup");
        msg.add(name);
        msg.add(email);
        msg.add(username);
        msg.add(password);

        sendToServer(msg);

        // Server will respond separately. Once successful, client switches to login
        // inside handleMessageFromServer() in your client class.
    }
}
