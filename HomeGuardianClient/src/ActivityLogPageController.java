import java.util.List;

import OCSF.ClientUser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class ActivityLogPageController extends homeGuardianClientController {

    @FXML
    private TableColumn<ClientUser.ActivityEntry, String> activityColumn;

    @FXML
    private TableView<ClientUser.ActivityEntry> activityTable;

    @FXML
    private TableColumn<ClientUser.ActivityEntry, String> dateTimeColumn;

    @FXML
    private TableColumn<ClientUser.ActivityEntry, String> deviceColumn;

    @FXML
    private Button devicesButton;

    @FXML
    private Button logoutButton;

    @FXML
    private Button mainMenuButton;

    @FXML
    private Button refreshButton;

    @FXML
    private Button settingsButton;

    private final ObservableList<ClientUser.ActivityEntry> logData =
            FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Bind table columns to ClientUser.ActivityEntry getters
        deviceColumn.setCellValueFactory(
                new PropertyValueFactory<>("device")
        );
        activityColumn.setCellValueFactory(
                new PropertyValueFactory<>("activity")
        );
        dateTimeColumn.setCellValueFactory(
                new PropertyValueFactory<>("dateTime")
        );

        activityTable.setItems(logData);

        // Load initial logs from ClientUser
        loadLogs();
    }

    private void loadLogs() {
        ClientUser user = getClientUser();

        if (user == null) {
            System.out.println("ActivityLogPage: No ClientUser yet.");
            logData.clear();
            return;
        }

        List<ClientUser.ActivityEntry> logs = user.getActivityLog();

        if (logs == null || logs.isEmpty()) {
            System.out.println("ActivityLogPage: No logs available.");
            logData.clear();
            return;
        }

        logData.setAll(logs);
    }


    @FXML
    void refreshButtonPressed(ActionEvent event) {
        // If needed, send request to server:
        // sendToServer("getLogs");
        // For now, just reload local cache:
        loadLogs();
    }

    @FXML
    void devicesButtonPressed(ActionEvent event) {
        switchScene(event, "deviceMenu.fxml");
    }

    @FXML
    void mainMenuButtonPressed(ActionEvent event) {
        switchScene(event, "mainMenu.fxml");
    }

    @FXML
    void settingsButtonPressed(ActionEvent event) {
        switchScene(event, "settingsMenu.fxml");
    }

    @FXML
    void logoutButtonPressed(ActionEvent event) {
        sendToServer("logout");
        switchScene(event, "login.fxml");
    }
}
