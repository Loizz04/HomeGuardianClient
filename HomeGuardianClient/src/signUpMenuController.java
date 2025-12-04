
/*
 * Author: Lois Mathew
 */
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
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

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

    private String emailFieldStyle;
    private String nameFieldStyle;
    private String usernameFieldStyle;
    private String passwordFieldStyle;

    private ActionEvent lastSignUpEvent;

    private boolean check;

    @FXML
    public void initialize() {
        if (emailField != null) {
            emailFieldStyle = emailField.getStyle();
        }
        if (nameField != null) {
            nameFieldStyle = nameField.getStyle();
        }
        if (usernameField != null) {
            usernameFieldStyle = usernameField.getStyle();
        }
        if (passwordField != null) {
            passwordFieldStyle = passwordField.getStyle();
        }

        //Remove red border when each field is focused again
        if (emailField != null) {
            emailField.focusedProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal) { 
                    emailField.setStyle(emailFieldStyle);
                }
            });
        }
        if (nameField != null) {
            nameField.focusedProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal) {
                    nameField.setStyle(nameFieldStyle);
                }
            });
        }
        if (usernameField != null) {
            usernameField.focusedProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal) {
                    usernameField.setStyle(usernameFieldStyle);
                }
            });
        }

        if (passwordField != null) {
            passwordField.focusedProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal) {
                    passwordField.setStyle(passwordFieldStyle);
                }
            });
        }

        HomeGuardianClient client = getClient();
        if (client != null) {
            client.setMessageHandler(this::handleServerMessage);
        }
    }

    //Format checking
    private boolean emailFormat(String s) {
        if (s == null) s = "";
        if (s.matches("^[a-zA-Z0-9_+&*-]+(?:\\." +
                      "[a-zA-Z0-9_+&*-]+)*@" +
                      "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) {
            return true;
        } else {
            if (emailField != null) {
                emailField.setStyle("-fx-border-color: red");
            }
            check = false;
            return false;
        }
    }
    private boolean generalFormat(String s, TextInputControl control) {
        if (s == null) s = "";
        if (s.length() > 22 || s.contains(" ") || "".equals(s)) {
            if (control != null) {
                control.setStyle("-fx-border-color: red");
            }
            check = false;
            return false;
        }
        return true;
    }
    private boolean checkFormat(String user, String pass, String name, String email) {
        check = true;  

        emailFormat(email);
        generalFormat(user, usernameField);
        generalFormat(pass, passwordField);
        generalFormat(name, nameField);

        if (!check) {
            Alert alert = new Alert(Alert.AlertType.ERROR,"Please fix the highlighted fields.\n" +"- Email must be a valid address.\n" +"- Name, username, and password must be 1–22 characters with no spaces.",ButtonType.OK);
            alert.setHeaderText("Invalid Input Format");
            alert.showAndWait();
            check = true;
            return false;
        }

        return true;
    }

    @FXML
    void loginLinkPressed(MouseEvent event) {
        switchScene(event, "login.fxml");
    }

    @FXML
    void signUpButtonPressed(ActionEvent event) {
        this.lastSignUpEvent = event;

        String name = nameField != null ? nameField.getText().trim() : "";
        String email = emailField != null ? emailField.getText().trim() : "";
        String username = usernameField != null ? usernameField.getText().trim() : "";
        String password = passwordField != null ? passwordField.getText() : "";
        if (!checkFormat(username, password, name, email)) {
            return;
        }

        ArrayList<Object> msg = new ArrayList<>();
        msg.add("signup");
        msg.add(name);
        msg.add(email);
        msg.add(username);
        msg.add(password);
        sendToServer(msg);
    }

    private void handleServerMessage(Object msg) {
        if (msg instanceof ArrayList<?>) {
            ArrayList<?> list = (ArrayList<?>) msg;

            if (list.size() >= 2 && "signupResult".equals(list.get(0))) {
                String status = String.valueOf(list.get(1));

                if ("success".equalsIgnoreCase(status)) {
                    onSignupSuccess(list);
                } else {
                    String error = (list.size() >= 3)
                            ? String.valueOf(list.get(2))
                            : "Signup failed.";
                    onSignupError(error);
                }
            }
        }
    }

    private void onSignupSuccess(ArrayList<?> list) {
        HomeGuardianClient client = getClient();
        if (client != null) {
            client.setMessageHandler(null);
        }

        String successMsg = (list.size() >= 3)
                ? String.valueOf(list.get(2))
                : "Signup successful.";

        Alert alert = new Alert(Alert.AlertType.INFORMATION, successMsg, ButtonType.OK);
        alert.setHeaderText("Signup Successful");
        alert.showAndWait();

        // Switch to main menu on the JavaFX thread
        javafx.application.Platform.runLater(() -> {
            try {
                if (lastSignUpEvent != null) {
                    switchScene(lastSignUpEvent, "mainMenu.fxml");
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
                Alert err = new Alert(Alert.AlertType.ERROR,
                        "Signup succeeded but failed to load main menu.",
                        ButtonType.OK);
                err.setHeaderText("Navigation Error");
                err.showAndWait();
            }
        });
    }

    private void onSignupError(String errorMessage) {
//show the server-specific error
        Alert alert = new Alert(Alert.AlertType.ERROR, errorMessage, ButtonType.OK);
        alert.setHeaderText("Signup Error");
        alert.showAndWait();
    }
}
