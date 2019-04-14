package filemanager.utils;

import java.util.prefs.Preferences;

public class PreferencesManager {

    private Preferences preferences;

    public PreferencesManager() {
        this.preferences = Preferences.userNodeForPackage(this.getClass());
    }

    public Preferences setDefaultPreferences() {
        if (preferences.get(Constants.Prefs.LAST_SELECTED_DRIVE_FOR_RIGHT_DISPLAY, "").isEmpty()) {
            preferences.put(Constants.Prefs.LAST_SELECTED_DRIVE_FOR_RIGHT_DISPLAY, Constants.C_DRIVE);
        }
        if (preferences.get(Constants.Prefs.LAST_SELECTED_DRIVE_FOR_LEFT_DISPLAY, "").isEmpty()) {
            preferences.put(Constants.Prefs.LAST_SELECTED_DRIVE_FOR_LEFT_DISPLAY, Constants.C_DRIVE);
        }
        return preferences;
    }

    public String getPreferencesForKey(String key) {
        return preferences.get(key, "");
    }

    public void updateValueInPreferencesForKey(String value, String key) {
        preferences.put(key, value);
    }
}
