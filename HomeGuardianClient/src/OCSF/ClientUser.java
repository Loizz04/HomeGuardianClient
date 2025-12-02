package OCSF;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * ClientUser
 *
 * This class lives on the CLIENT side only.  It stores a snapshot of the
 * logged-in user's information and the current state of all devices that
 * the UI needs to display.
 *
 * All JavaFX controllers should read/write state via this class instead of
 * talking directly to the network layer.  The HomeGuardianClient is
 * responsible for updating this object when the server sends new data.
 *
 * NOTE:
 *  - Device IDs in the UI are 1-based (Light 1..3, Camera 1..3, etc.)
 *  - Internally we store them in 0-based lists; helper methods convert
 *    between the two.
 */
public class ClientUser {

    // ---------------------------------------------------------------------
    // Constants – how many of each device type the client UI supports
    // ---------------------------------------------------------------------
    public static final int NUM_LIGHTS  = 3;
    public static final int NUM_CAMERAS = 3;
    public static final int NUM_ALARMS  = 3;
    public static final int NUM_LOCKS   = 2;

    // ---------------------------------------------------------------------
    // Basic user info
    // ---------------------------------------------------------------------
    private int    userId;
    private String username;
    private String name;
    private String email;
    private boolean notificationsEnabled;

    // ---------------------------------------------------------------------
    // Nested value types representing the state of each device
    // ---------------------------------------------------------------------

    /**
     * Represents the local state of a SmartLight.
     */
    public static class LightState {
        private boolean present;
        private boolean on;
        private int     brightness;        // 0-100 %
        private String  colour;            // e.g. "#FFFFFF"
        private boolean motionEnabled;
        private int     motionSensitivity; // 0-100 %
        private int     timeoutMinutes;    // auto-off after N minutes

        public LightState() {
            this.present = false;
            this.on = false;
            this.brightness = 0;
            this.colour = "#FFFFFF";
            this.motionEnabled = false;
            this.motionSensitivity = 0;
            this.timeoutMinutes = 0;
        }

        // --- getters / setters ---

        public boolean isPresent() {
            return present;
        }

        public void setPresent(boolean present) {
            this.present = present;
        }

        public boolean isOn() {
            return on;
        }

        public void setOn(boolean on) {
            this.on = on;
        }

        public int getBrightness() {
            return brightness;
        }

        public void setBrightness(int brightness) {
            this.brightness = Math.max(0, Math.min(100, brightness));
        }

        public String getColour() {
            return colour;
        }

        public void setColour(String colour) {
            this.colour = colour;
        }

        public boolean isMotionEnabled() {
            return motionEnabled;
        }

        public void setMotionEnabled(boolean motionEnabled) {
            this.motionEnabled = motionEnabled;
        }

        public int getMotionSensitivity() {
            return motionSensitivity;
        }

        public void setMotionSensitivity(int motionSensitivity) {
            this.motionSensitivity = Math.max(0, Math.min(100, motionSensitivity));
        }

        public int getTimeoutMinutes() {
            return timeoutMinutes;
        }

        public void setTimeoutMinutes(int timeoutMinutes) {
            this.timeoutMinutes = Math.max(0, timeoutMinutes);
        }
    }

    /**
     * Represents the local state of a SecurityCamera.
     */
    public static class CameraState {
        private boolean present;
        private boolean on;               // powered / streaming
        private boolean recording;
        private boolean motionEnabled;
        private String  selectedFootage;  // label of selected clip, if any

        public CameraState() {
            this.present = false;
            this.on = false;
            this.recording = false;
            this.motionEnabled = false;
            this.selectedFootage = null;
        }

        public boolean isPresent() {
            return present;
        }

        public void setPresent(boolean present) {
            this.present = present;
        }

        public boolean isOn() {
            return on;
        }

        public void setOn(boolean on) {
            this.on = on;
        }

        public boolean isRecording() {
            return recording;
        }

        public void setRecording(boolean recording) {
            this.recording = recording;
        }

        public boolean isMotionEnabled() {
            return motionEnabled;
        }

        public void setMotionEnabled(boolean motionEnabled) {
            this.motionEnabled = motionEnabled;
        }

        public String getSelectedFootage() {
            return selectedFootage;
        }

        public void setSelectedFootage(String selectedFootage) {
            this.selectedFootage = selectedFootage;
        }
    }

    /**
     * Represents the local state of an Alarm.
     */
    public static class AlarmState {
        private boolean present;
        private boolean motionEnabled;
        private boolean recordOnCamera;

        public AlarmState() {
            this.present = false;
            this.motionEnabled = false;
            this.recordOnCamera = false;
        }

        public boolean isPresent() {
            return present;
        }

        public void setPresent(boolean present) {
            this.present = present;
        }

        public boolean isMotionEnabled() {
            return motionEnabled;
        }

        public void setMotionEnabled(boolean motionEnabled) {
            this.motionEnabled = motionEnabled;
        }

        public boolean isRecordOnCamera() {
            return recordOnCamera;
        }

        public void setRecordOnCamera(boolean recordOnCamera) {
            this.recordOnCamera = recordOnCamera;
        }
    }

    /**
     * Represents the local state of a SmartLock.
     */
    public static class LockState {
        private boolean present;
        private boolean engaged;          // true = locked
        private int     lockDuration;     // in minutes
        private boolean linkedToAlarm;

        public LockState() {
            this.present = false;
            this.engaged = false;
            this.lockDuration = 0;
            this.linkedToAlarm = false;
        }

        public boolean isPresent() {
            return present;
        }

        public void setPresent(boolean present) {
            this.present = present;
        }

        public boolean isEngaged() {
            return engaged;
        }

        public void setEngaged(boolean engaged) {
            this.engaged = engaged;
        }

        public int getLockDuration() {
            return lockDuration;
        }

        public void setLockDuration(int lockDuration) {
            this.lockDuration = Math.max(0, lockDuration);
        }

        public boolean isLinkedToAlarm() {
            return linkedToAlarm;
        }

        public void setLinkedToAlarm(boolean linkedToAlarm) {
            this.linkedToAlarm = linkedToAlarm;
        }
    }

    /**
     * Simple value object for a single activity log entry as displayed
     * on the Activity Log screen.
     */

    public static class ActivityEntry {
        private final String device;
        private final String activity;
        private final String dateTime;

        public ActivityEntry(String device, String activity, String dateTime) {
            this.device = device;
            this.activity = activity;
            this.dateTime = dateTime;
        }

        public String getDevice() {
            return device;
        }

        public String getActivity() {
            return activity;
        }

        public String getDateTime() {
            return dateTime;
        }
    }

    /**
     * Simple value object for notifications belonging to this user.
     */
    public static class NotificationEntry {
        private final String message;
        private final long   timestamp;
        private boolean      read;

        public NotificationEntry(String message, long timestamp) {
            this.message = message;
            this.timestamp = timestamp;
            this.read = false;
        }

        public String getMessage() {
            return message;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public boolean isRead() {
            return read;
        }

        public void markRead() {
            this.read = true;
        }
    }

    // ---------------------------------------------------------------------
    // Collections of device states, activity log, notifications
    // ---------------------------------------------------------------------
    private final List<LightState>        lights        = new ArrayList<>();
    private final List<CameraState>       cameras       = new ArrayList<>();
    private final List<AlarmState>        alarms        = new ArrayList<>();
    private final List<LockState>         locks         = new ArrayList<>();
    private final List<ActivityEntry>     activityLog   = new ArrayList<>();
    private final List<NotificationEntry> notifications = new ArrayList<>();
    


    // ---------------------------------------------------------------------
    // Constructors
    // ---------------------------------------------------------------------
    public ClientUser() {
        this(-1, null, null, null);
    }

    public ClientUser(int userId, String username, String name, String email) {
        this.userId = userId;
        this.username = username;
        this.name = name;
        this.email = email;
        this.notificationsEnabled = true;

        // initialise fixed-size device lists
        for (int i = 0; i < NUM_LIGHTS; i++) {
            lights.add(new LightState());
        }
        for (int i = 0; i < NUM_CAMERAS; i++) {
            cameras.add(new CameraState());
        }
        for (int i = 0; i < NUM_ALARMS; i++) {
            alarms.add(new AlarmState());
        }
        for (int i = 0; i < NUM_LOCKS; i++) {
            locks.add(new LockState());
        }
    }

    // ---------------------------------------------------------------------
    // Basic user info getters/setters
    // ---------------------------------------------------------------------

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isNotificationsEnabled() {
        return notificationsEnabled;
    }

    public void setNotificationsEnabled(boolean notificationsEnabled) {
        this.notificationsEnabled = notificationsEnabled;
    }

    // ---------------------------------------------------------------------
    // Device state access helpers
    // ---------------------------------------------------------------------

    // Lights
    public List<LightState> getLights() {
        return Collections.unmodifiableList(lights);
    }

    public LightState getLight(int lightId) {
        // lightId is 1-based in UI
        if (lightId < 1 || lightId > NUM_LIGHTS) {
            throw new IllegalArgumentException("Invalid lightId: " + lightId);
        }
        return lights.get(lightId - 1);
    }

    // Cameras
    public List<CameraState> getCameras() {
        return Collections.unmodifiableList(cameras);
    }

    public CameraState getCamera(int cameraId) {
        if (cameraId < 1 || cameraId > NUM_CAMERAS) {
            throw new IllegalArgumentException("Invalid cameraId: " + cameraId);
        }
        return cameras.get(cameraId - 1);
    }

    // Alarms
    public List<AlarmState> getAlarms() {
        return Collections.unmodifiableList(alarms);
    }

    public AlarmState getAlarm(int alarmId) {
        if (alarmId < 1 || alarmId > NUM_ALARMS) {
            throw new IllegalArgumentException("Invalid alarmId: " + alarmId);
        }
        return alarms.get(alarmId - 1);
    }

    // Locks
    public List<LockState> getLocks() {
        return Collections.unmodifiableList(locks);
    }

    public LockState getLock(int lockId) {
        if (lockId < 1 || lockId > NUM_LOCKS) {
            throw new IllegalArgumentException("Invalid lockId: " + lockId);
        }
        return locks.get(lockId - 1);
    }

    // ---------------------------------------------------------------------
    // Activity log & notifications
    // ---------------------------------------------------------------------

    public List<ActivityEntry> getActivityLog() {
        return Collections.unmodifiableList(activityLog);
    }

    public void clearActivityLog() {
        activityLog.clear();
    }

    public void addActivityEntry(String device, String activity, String dateTime) {
        activityLog.add(new ActivityEntry(device, activity, dateTime));
    }

    public void setActivityLogEntries(List<ActivityEntry> entries) {
        activityLog.clear();
        if (entries != null) {
            activityLog.addAll(entries);
        }
    }

    public List<NotificationEntry> getNotifications() {
        return Collections.unmodifiableList(notifications);
    }

    public void clearNotifications() {
        notifications.clear();
    }

    public void addNotification(String message, long timestamp) {
        notifications.add(new NotificationEntry(message, timestamp));
    }

    public void addNotification(NotificationEntry entry) {
        if (entry != null) {
            notifications.add(entry);
        }
    }

    public void setNotifications(List<NotificationEntry> entries) {
        notifications.clear();
        if (entries != null) {
            notifications.addAll(entries);
        }
    }
}
