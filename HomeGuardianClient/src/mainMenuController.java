import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class mainMenuController extends homeGuardianClientController {

    @FXML
    private ImageView activityLogButton;

    @FXML
    private ImageView devicesButton;

    @FXML
    private Button logoutButton;

    @FXML
    private ImageView settingsButton;

    @FXML
    void activityLogButtonClicked(MouseEvent event) {
        // Go to Activity Log screen
        switchScene(event, "ActivityLogMenu.fxml");
    }

    @FXML
    void devicesButtonClicked(MouseEvent event) {
        // Go to Devices screen
        switchScene(event, "DevicesMenu.fxml");
    }

    @FXML
    void logoutButtonPressed(ActionEvent event) {
        // Optional: tell server user is logging out
        sendToServer("logout");

        // Back to Login screen
        switchScene(event, "LoginMenu.fxml");
    }

    @FXML
    void settingsButtonClicked(MouseEvent event) {
        // Go to Settings screen
        switchScene(event, "SettingsMenu.fxml");
    }
}
