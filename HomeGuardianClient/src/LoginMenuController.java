import java.io.IOException;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controller for login.fxml
 *
 * - Validates username/password format (length <= 22, no spaces, not empty)
 * - Shows red borders on invalid input
 * - Sends ["login", username, password] to the server
 * - Expects server reply: ["loginResult", "success/error", "message"]
 * - On success: switches to mainMenu.fxml
 */
public class LoginMenuController extends homeGuardianClientController {

    @FXML
    private Button loginButton;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button signUpButton;

    @FXML
    private TextField usernameField;

    // To restore original styles after red border
    private String usernameFieldStyle;
    private String passwordFieldStyle;

    // Store the last login event so we can use it for scene switching on success
    private ActionEvent lastLoginEvent;

    @FXML
    public void initialize() {
        // Save original styles
        if (usernameField != null) {
            usernameFieldStyle = usernameField.getStyle();
        }
        if (passwordField != null) {
            passwordFieldStyle = passwordField.getStyle();
        }

        // Remove red border when username field is focused again
        if (usernameField != null) {
            usernameField.focusedProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal) { // got focus
                    usernameField.setStyle(usernameFieldStyle);
                }
            });
        }

        // Remove red border when password field is focused again
        if (passwordField != null) {
            passwordField.focusedProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal) {
                    passwordField.setStyle(passwordFieldStyle);
                }
            });
        }

        // Register a message handler for this screen (for loginResult messages)
        HomeGuardianClient client = getClient();
        if (client != null) {
            client.setMessageHandler(this::handleServerMessage);
        }
    }

    // ---------- Format checking helpers ----------

    // Same logic as your example: <= 22 chars, not empty, no spaces
    private boolean generalFormat(String s) {
        if (s == null) return false;
        if (s.length() > 22) return false;
        if ("".equals(s)) return false;
        if (s.contains(" ")) return false;
        return true;
    }

    private boolean checkFormat(String user, String pass) {
        boolean ok = generalFormat(user) && generalFormat(pass);

        if (!ok) {
            // Add red borders to indicate error
            if (usernameField != null) {
                usernameField.setStyle("-fx-border-color: red");
            }
            if (passwordField != null) {
                passwordField.setStyle("-fx-border-color: red");
            }

            // Optional: show a small alert to say what's wrong
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Username and password must be 1–22 characters with no spaces.",
                    ButtonType.OK);
            alert.setHeaderText("Invalid Input Format");
            alert.showAndWait();
        }

        return ok;
    }

    // ---------- Button handlers ----------

    @FXML
    private void loginButtonPressed(ActionEvent event) {
        this.lastLoginEvent = event;

        String username = usernameField != null ? usernameField.getText() : "";
        String password = passwordField != null ? passwordField.getText() : "";

        // Client-side format validation
        if (!checkFormat(username, password)) {
            return; // Borders already red + alert shown
        }

        // Build the login command: ["login", username, password]
        ArrayList<Object> command = new ArrayList<>();
        command.add("login");
        command.add(username);
        command.add(password);

        // Send to server via base controller helper
        sendToServer(command);
    }

    @FXML
    private void signUpButtonPressed(ActionEvent event) {
        // Navigate to the signup screen
        // Make sure the FXML name matches your actual file: "signUp.fxml"
        switchScene(event, "signUp.fxml");
    }

    // ---------- Server message handling ----------

    /**
     * Handles messages from HomeGuardianClient.setMessageHandler(...)
     *
     * Expected login response format from server:
     *   ["loginResult", "success", "Welcome back, <user>"]
     *   ["loginResult", "error", "Invalid username or password"]
     *
     * On success: switch to mainMenu.fxml
     * On error: show alert + red borders
     */
    private void handleServerMessage(Object msg) {
        if (msg instanceof ArrayList<?>) {
            ArrayList<?> list = (ArrayList<?>) msg;

            if (list.size() >= 2 && "loginResult".equals(list.get(0))) {
                String status = String.valueOf(list.get(1));

                if ("success".equalsIgnoreCase(status)) {
                    onLoginSuccess();
                } else {
                    String error = (list.size() >= 3)
                            ? String.valueOf(list.get(2))
                            : "Login failed.";
                    onLoginError(error);
                }
            }
        }
        // Ignore other messages (like ClientUser) here;
        // HomeGuardianClient already updates its ClientUser internally.
    }

    private void onLoginSuccess() {
        // Stop handling login-specific messages so the next screen can use its own handler
        HomeGuardianClient client = getClient();
        if (client != null) {
            client.setMessageHandler(null);
        }

        // Switch to main menu on the JavaFX thread
        javafx.application.Platform.runLater(() -> {
            try {
                if (lastLoginEvent != null) {
                    // Use shared helper in base controller
                    switchScene(lastLoginEvent, "mainMenu.fxml");
                } else if (usernameField != null) {
                    // Fallback: manually load if event somehow null
                    Parent root = FXMLLoader.load(getClass().getResource("mainMenu.fxml"));
                    Stage stage = (Stage) ((Node) usernameField).getScene().getWindow();
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                }
            } catch (IOException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR,
                        "Login succeeded but failed to load main menu.",
                        ButtonType.OK);
                alert.setHeaderText("Navigation Error");
                alert.showAndWait();
            }
        });
    }

    private void onLoginError(String errorMessage) {
        // Red borders again to indicate error from server
        if (usernameField != null) {
            usernameField.setStyle("-fx-border-color: red");
        }
        if (passwordField != null) {
            passwordField.setStyle("-fx-border-color: red");
        }

        Alert alert = new Alert(Alert.AlertType.ERROR, errorMessage, ButtonType.OK);
        alert.setHeaderText("Login Error");
        alert.showAndWait();
    }
}
