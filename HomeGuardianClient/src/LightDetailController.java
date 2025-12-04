import java.util.ArrayList;

import OCSF.ClientUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class LightDetailController extends homeGuardianClientController {

    // Set by lightController before loading this scene
    private static int currentLightId = 1;

    private int lightId = 1;

    public static void setCurrentLightId(int id) {
        currentLightId = id;
    }

    @FXML
    private Label lightTitleLabel;           // "LIGHT 1", "LIGHT 2", ...

    @FXML
    private Button activityLogButton;

    @FXML
    private Slider brightnessSlider;

    @FXML
    private TextField brightnessTextField;

    @FXML
    private ColorPicker colorPicker;

    @FXML
    private Button devicesButton;

    @FXML
    private Button logoutButton;

    @FXML
    private Button mainMenuButton;

    @FXML
    private TextField motionSensitivityField;

    @FXML
    private Slider motionSensitivitySlider;

    @FXML
    private ToggleButton motionToggleButton;

    @FXML
    private Button settingsButton;

    @FXML
    private Slider timeoutSlider;

    @FXML
    private TextField timeoutTextField;

    // ================== INITIALIZE ==================

    @FXML
    public void initialize() {
        // Use the ID passed in from the light menu
        lightId = currentLightId;

        if (lightTitleLabel != null) {
            lightTitleLabel.setText("LIGHT " + lightId);
        }
    }

    // ================== NAV BUTTONS ==================

    @FXML
    void activityLogButtonPressed(ActionEvent event) {
        switchScene(event, "activityLogPage.fxml");
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

    // ================== BRIGHTNESS ==================

    @FXML
    void brightnessSliderMoved(MouseEvent event) {
        int value = (int) brightnessSlider.getValue();
        brightnessTextField.setText(Integer.toString(value));
        sendBrightnessToServer(value);

        ClientUser user = getClientUser();
        if (user != null) {
            user.addActivity("Light " + lightId,
                    "Brightness set to " + value + "%");
        }
    }

    @FXML
    void brightnessTextFieldTyped(ActionEvent event) {
        try {
            int value = Integer.parseInt(brightnessTextField.getText().trim());
            if (value < 0) value = 0;
            if (value > 100) value = 100;
            brightnessSlider.setValue(value);
            sendBrightnessToServer(value);

            ClientUser user = getClientUser();
            if (user != null) {
                user.addActivity("Light " + lightId,
                        "Brightness set to " + value + "% (typed)");
            }
        } catch (NumberFormatException e) {
            brightnessTextField.setText(Integer.toString((int) brightnessSlider.getValue()));
        }
    }

    private void sendBrightnessToServer(int value) {
        ArrayList<Object> msg = new ArrayList<>();
        msg.add("SET_LIGHT_BRIGHTNESS");
        msg.add(lightId);
        msg.add(value);   // 0–100 %
        sendToServer(msg);
    }

    // ================== COLOUR ==================

    @FXML
    void colorPickerChanged(ActionEvent event) {
        Color color = colorPicker.getValue();
        int r = (int) (color.getRed() * 255);
        int g = (int) (color.getGreen() * 255);
        int b = (int) (color.getBlue() * 255);

        ArrayList<Object> msg = new ArrayList<>();
        msg.add("SET_LIGHT_COLOR");
        msg.add(lightId);
        msg.add(r);
        msg.add(g);
        msg.add(b);
        sendToServer(msg);

        ClientUser user = getClientUser();
        if (user != null) {
            user.addActivity("Light " + lightId,
                    "Colour changed to RGB(" + r + "," + g + "," + b + ")");
        }
    }

    // ================== TIMEOUT ==================

    @FXML
    void timeoutSliderMoved(MouseEvent event) {
        int minutes = (int) timeoutSlider.getValue();
        timeoutTextField.setText(Integer.toString(minutes));
        sendTimeoutToServer(minutes);

        ClientUser user = getClientUser();
        if (user != null) {
            user.addActivity("Light " + lightId,
                    "Timeout set to " + minutes + " minutes");
        }
    }

    @FXML
    void timeoutTextFieldTyped(ActionEvent event) {
        try {
            int minutes = Integer.parseInt(timeoutTextField.getText().trim());
            if (minutes < 0) minutes = 0;
            timeoutSlider.setValue(minutes);
            sendTimeoutToServer(minutes);

            ClientUser user = getClientUser();
            if (user != null) {
                user.addActivity("Light " + lightId,
                        "Timeout set to " + minutes + " minutes (typed)");
            }
        } catch (NumberFormatException e) {
            timeoutTextField.setText(Integer.toString((int) timeoutSlider.getValue()));
        }
    }

    private void sendTimeoutToServer(int minutes) {
        ArrayList<Object> msg = new ArrayList<>();
        msg.add("SET_LIGHT_TIMEOUT");
        msg.add(lightId);
        msg.add(minutes);
        sendToServer(msg);
    }

    // ================== MOTION ON/OFF ==================

    @FXML
    void motionToggleButtonPressed(ActionEvent event) {
        boolean on = motionToggleButton.isSelected();

        // Update button look
        if (on) {
            motionToggleButton.setText("Turn off");
            motionToggleButton.setStyle(
                    "-fx-background-color: #D9534F; -fx-text-fill: white; -fx-font-weight: bold;");
        } else {
            motionToggleButton.setText("Turn on");
            motionToggleButton.setStyle(
                    "-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        }

        // Build message to send to server
        ArrayList<Object> msg = new ArrayList<>();
        msg.add("SET_LIGHT_MOTION_LINK");
        msg.add(lightId);
        msg.add(on);
        sendToServer(msg);

        ClientUser user = getClientUser();
        if (user != null) {
            user.addActivity("Light " + lightId,
                    "Motion sensor turned " + (on ? "ON" : "OFF"));
        }
    }

    // ================== MOTION SENSITIVITY ==================

    @FXML
    void motionSensitivitySliderMoved(MouseEvent event) {
        int value = (int) motionSensitivitySlider.getValue();
        motionSensitivityField.setText(Integer.toString(value));
        sendMotionSensitivityToServer(value);

        ClientUser user = getClientUser();
        if (user != null) {
            user.addActivity("Light " + lightId,
                    "Motion sensitivity set to " + value + "%");
        }
    }

    @FXML
    void motionSensitivityFieldTyped(ActionEvent event) {
        try {
            int value = Integer.parseInt(motionSensitivityField.getText().trim());
            if (value < 0) value = 0;
            if (value > 100) value = 100;
            motionSensitivitySlider.setValue(value);
            sendMotionSensitivityToServer(value);

            ClientUser user = getClientUser();
            if (user != null) {
                user.addActivity("Light " + lightId,
                        "Motion sensitivity set to " + value + "% (typed)");
            }
        } catch (NumberFormatException e) {
            motionSensitivityField.setText(
                    Integer.toString((int) motionSensitivitySlider.getValue()));
        }
    }

    private void sendMotionSensitivityToServer(int value) {
        ArrayList<Object> msg = new ArrayList<>();
        msg.add("SET_MOTION_SENSITIVITY");
        // IMPORTANT: server expects only the value at index 1
        msg.add(value);
        sendToServer(msg);
    }
}
