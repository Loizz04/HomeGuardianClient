import java.util.ArrayList;

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

    // set by Light Menu before loading this scene
    private static int currentLightId = 1;

    private int lightId = 1;

    public static void setCurrentLightId(int id) {
        currentLightId = id;
    }

    @FXML
    private Label lightTitleLabel;           // label that says "LIGHT 1", "LIGHT 2", etc.

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

    // ---------------- INIT ----------------

    @FXML
    public void initialize() {
        // Decide which light this screen is for by reading the title text
        if (lightTitleLabel != null && lightTitleLabel.getText() != null) {
            String t = lightTitleLabel.getText().toLowerCase();
            if (t.contains("1")) {
                lightId = 1;
            } else if (t.contains("2")) {
                lightId = 2;
            } else if (t.contains("3")) {
                lightId = 3;
            }
        }

        // Optional: set initial button text
        motionToggleButton.setText(motionToggleButton.isSelected() ? "Turn off" : "Turn on");
    }

    // ------------- NAVIGATION BUTTONS -------------

    @FXML
    void activityLogButtonPressed(ActionEvent event) {
        switchScene(event, "activityLogPage.fxml");
    }

    @FXML
    void devicesButtonPressed(ActionEvent event) {
        // back to overall light menu (or DevicesPage – your choice)
        switchScene(event, "deviceMenu.fxml");
    }

    @FXML
    void mainMenuButtonPressed(ActionEvent event) {
        switchScene(event, "mainMenu.fxml");
    }

    //@FXML
    //void settingsButtonPressed(ActionEvent event) {
        //switchScene(event, "SettingsMenu.fxml");
    //}

    @FXML
    void logoutButtonPressed(ActionEvent event) {
        sendToServer("logout");
        switchScene(event, "login.fxml");
    }

    // ------------- BRIGHTNESS -------------

    @FXML
    void brightnessSliderMoved(MouseEvent event) {
        int value = (int) brightnessSlider.getValue();
        brightnessTextField.setText(Integer.toString(value));
        sendBrightnessToServer(value);
    }

    @FXML
    void brightnessTextFieldTyped(ActionEvent event) {
        try {
            int value = Integer.parseInt(brightnessTextField.getText().trim());
            if (value < 0) value = 0;
            if (value > 100) value = 100;
            brightnessSlider.setValue(value);
            sendBrightnessToServer(value);
        } catch (NumberFormatException e) {
            brightnessTextField.setText(Integer.toString((int) brightnessSlider.getValue()));
        }
    }

    private void sendBrightnessToServer(int value) {
        ArrayList<Object> msg = new ArrayList<>();
        msg.add("setLightBrightness");
        msg.add(lightId);
        msg.add(value);   // 0–100 %
        sendToServer(msg);
    }

    // ------------- COLOR -------------

    @FXML
    void colorPickerChanged(ActionEvent event) {
        Color color = colorPicker.getValue();
        int r = (int) (color.getRed() * 255);
        int g = (int) (color.getGreen() * 255);
        int b = (int) (color.getBlue() * 255);

        ArrayList<Object> msg = new ArrayList<>();
        msg.add("setLightColor");
        msg.add(lightId);
        msg.add(r);
        msg.add(g);
        msg.add(b);
        sendToServer(msg);
    }

    // ------------- TIMEOUT -------------

    @FXML
    void timeoutSliderMoved(MouseEvent event) {
        int minutes = (int) timeoutSlider.getValue();
        timeoutTextField.setText(Integer.toString(minutes));
        sendTimeoutToServer(minutes);
    }

    @FXML
    void timeoutTextFieldTyped(ActionEvent event) {
        try {
            int minutes = Integer.parseInt(timeoutTextField.getText().trim());
            if (minutes < 0) minutes = 0;
            timeoutSlider.setValue(minutes);
            sendTimeoutToServer(minutes);
        } catch (NumberFormatException e) {
            timeoutTextField.setText(Integer.toString((int) timeoutSlider.getValue()));
        }
    }

    private void sendTimeoutToServer(int minutes) {
        ArrayList<Object> msg = new ArrayList<>();
        msg.add("setLightTimeout");
        msg.add(lightId);
        msg.add(minutes);
        sendToServer(msg);
    }

    // ------------- MOTION SENSOR -------------

    @FXML
    void motionToggleButtonPressed(ActionEvent event) {
        boolean on = motionToggleButton.isSelected();
        motionToggleButton.setText(on ? "Turn off" : "Turn on");

        ArrayList<Object> msg = new ArrayList<>();
        msg.add("toggleMotionSensor");
        msg.add(lightId);
        msg.add(on);
        sendToServer(msg);
    }

    @FXML
    void motionSensitivitySliderMoved(MouseEvent event) {
        int value = (int) motionSensitivitySlider.getValue();
        motionSensitivityField.setText(Integer.toString(value));
        sendMotionSensitivityToServer(value);
    }

    @FXML
    void motionSensitivityFieldTyped(ActionEvent event) {
        try {
            int value = Integer.parseInt(motionSensitivityField.getText().trim());
            if (value < 0) value = 0;
            if (value > 100) value = 100;
            motionSensitivitySlider.setValue(value);
            sendMotionSensitivityToServer(value);
        } catch (NumberFormatException e) {
            motionSensitivityField.setText(Integer.toString((int) motionSensitivitySlider.getValue()));
        }
    }

    private void sendMotionSensitivityToServer(int value) {
        ArrayList<Object> msg = new ArrayList<>();
        msg.add("setMotionSensitivity");
        msg.add(lightId);
        msg.add(value);   // 0–100 %
        sendToServer(msg);
    }
}
