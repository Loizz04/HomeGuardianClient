import java.util.ArrayList;
import java.util.regex.Pattern;

import OCSF.ClientUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

public class settingsController extends homeGuardianClientController {

    @FXML
    private Button devicesButton;

    @FXML
    private Button logoutButton;

    @FXML
    private Button mainMenuButton;

    @FXML
    private TextField notificationEmailField;

    @FXML
    private CheckBox notificationsCheckBox;

    @FXML
    private Button saveChangesButton;

    @FXML
    private Button activityLogButton;

    private String emailFieldBaseStyle = "";

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9_+&*-]+(?:\\." +
            "[a-zA-Z0-9_+&*-]+)*@" +
            "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );

    @FXML
    private void initialize() {
        // Capture base style
        if (notificationEmailField != null) {
            emailFieldBaseStyle = notificationEmailField.getStyle();
        }

        // Load current values from ClientUser
        ClientUser user = getClientUser();
        if (user != null) {
            String email = user.getEmail();
            boolean enabled = user.isNotificationsEnabled();

            notificationEmailField.setText(email != null ? email : "");
            notificationsCheckBox.setSelected(enabled);
            notificationEmailField.setDisable(!enabled);
        }

        // When checkbox changes, enable/disable email field
        notificationsCheckBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
            notificationEmailField.setDisable(!newVal);
            // clear any error style when user toggles
            notificationEmailField.setStyle(emailFieldBaseStyle);
        });

        // Clear red border on focus
        notificationEmailField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) { // gained focus
                notificationEmailField.setStyle(emailFieldBaseStyle);
            }
        });
    }
    @FXML
    void devicesButtonPressed(ActionEvent event) {
        switchScene(event, "deviceMenu.fxml");
    }

    @FXML
    void logoutButtonPressed(ActionEvent event) {
        sendToServer("logout");
        switchScene(event, "login.fxml");
    }

    @FXML
    void mainMenuButtonPressed(ActionEvent event) {
        switchScene(event, "mainMenu.fxml");
    }

    @FXML
    void activityLogButtonPressed(ActionEvent event) {
        switchScene(event, "activityLogPage.fxml");

    }

    @FXML
    void notificationCheckPressed(ActionEvent event) {
        // Logic is handled by the listener in initialize()
        // (we keep this method because FXML calls it)
    }

    @FXML
    void saveChangesButtonPressed(ActionEvent event) {
        boolean enabled = notificationsCheckBox.isSelected();
        String email = notificationEmailField.getText().trim();

        // If notifications are enabled, validate email
        if (enabled && !emailFormat(email)) {
            return; // email field already marked red
        }

        // Build command for server: ["updateNotifications", email, enabled]
        ArrayList<Object> msg = new ArrayList<>();
        msg.add("updateNotifications");
        msg.add(email);
        msg.add(enabled);

        // Send to server via base helper
        sendToServer(msg);

        // Update local ClientUser snapshot
        ClientUser user = getClientUser();
        if (user != null) {
            user.setEmail(email);
            user.setNotificationsEnabled(enabled);
        }

        // Confirmation alert
        Alert alert = new Alert(Alert.AlertType.INFORMATION,
                "Notification settings saved successfully.",
                ButtonType.OK);
        alert.setHeaderText("Settings Saved");
        alert.showAndWait();
    }

    private boolean emailFormat(String s) {
        if (s.isEmpty()) {
            // Allow empty email only if notifications are OFF
            if (notificationsCheckBox.isSelected()) {
                markEmailInvalid();
                return false;
            } else {
                return true;
            }
        }

        if (EMAIL_PATTERN.matcher(s).matches()) {
            return true;
        } else {
            markEmailInvalid();
            return false;
        }
    }

    private void markEmailInvalid() {
        if (notificationEmailField != null) {
            notificationEmailField.setStyle("-fx-border-color: red");
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Please enter a valid email address or disable notifications.",
                    ButtonType.OK);
            alert.setHeaderText("Invalid Email");
            alert.showAndWait();
        }
    }
}
