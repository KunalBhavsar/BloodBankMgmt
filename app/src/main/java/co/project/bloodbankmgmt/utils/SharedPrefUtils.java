package co.project.bloodbankmgmt.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import co.project.bloodbankmgmt.models.User;

/**
 * Created by Kunal on 26/03/17.
 */

public class SharedPrefUtils {
    private static final String TAG = SharedPrefUtils.class.getSimpleName();
    private static final String SHARED_PREFS_FILE_NAME = "paycillin_spref";
    public static final String CURRENT_USER = "current_user";
    public static final String IS_LOGGED_IN = "logged_in";
    private SharedPreferences sharedPrefs;
    private SharedPreferences.Editor editor;

    private List<OnSharedPrefsValueChangeListener> mSharedPrefValueChangeListeners;

    private static SharedPrefUtils mSharedPrefs;
    private static User currentUser;

    private SharedPrefUtils(Context mAppContext) {
        this.sharedPrefs = mAppContext.getSharedPreferences(SHARED_PREFS_FILE_NAME, Activity.MODE_PRIVATE);
        this.mSharedPrefValueChangeListeners = new ArrayList<>();

        this.editor = sharedPrefs.edit();
    }

    public static void init(Context context) {
        if (mSharedPrefs == null) {
            mSharedPrefs = new SharedPrefUtils(context);
        }
    }

    public static SharedPrefUtils getInstance() {
        return mSharedPrefs;
    }

    public static User getCurrentUser() {
        if (currentUser == null) {
            String currentUserJson = mSharedPrefs.get(CURRENT_USER, "");
            if (!TextUtils.isEmpty(currentUserJson)) {
                currentUser = new Gson().fromJson(currentUserJson, User.class);
                return currentUser;
            }
        }
        else {
            return currentUser;
        }
        return null;
    }

    public void add(String key, Object value) {
        //Getting the old value of the key
        Object oldValue = null;
        if (value instanceof String) {
            editor.putString(key, (String) value);
            oldValue = get(key, "");
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
            oldValue = get(key, false);
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
            oldValue = get(key, -1);
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value);
            oldValue = get(key, -1l);
        } else {
            if (value != null)
                Log.e(TAG, "Value not inserted, Type " + value.getClass() + " not supported");
            else
                Log.e(TAG, "Cannot insert null values in sharedprefs");
        }
        editor.apply();

        //notifying the observers
        notifyObservers(key, oldValue, value);
    }

    private synchronized void notifyObservers(String key, Object oldValue, Object newValue) {
        if (key.equals(CURRENT_USER)) {
            String currentUserJson = mSharedPrefs.get(CURRENT_USER, "");
            if (!TextUtils.isEmpty(currentUserJson)) {
                currentUser = new Gson().fromJson(currentUserJson, User.class);
            }
        }
        for (Iterator iterator = mSharedPrefValueChangeListeners.iterator(); iterator.hasNext(); ) {
            OnSharedPrefsValueChangeListener listener = (OnSharedPrefsValueChangeListener) iterator.next();
            listener.onValueChanged(key, oldValue, newValue);
        }
    }

    /**
     * The default value defines what type of value is to be retrieved.
     *
     * @param key      - The key to look for.
     * @param defValue - The default value of no result was found.
     * @return
     */
    public <T> T get(String key, T defValue) {
        Object returnValue = null;
        if (defValue instanceof String) {
            returnValue = sharedPrefs.getString(key, (String) defValue);

        } else if (defValue instanceof Boolean) {
            returnValue = sharedPrefs.getBoolean(key, (Boolean) defValue);
        } else if (defValue instanceof Integer) {
            returnValue = sharedPrefs.getInt(key, (Integer) defValue);
        } else if (defValue instanceof Long) {
            returnValue = sharedPrefs.getLong(key, (Long) defValue);
        }
        return (T) defValue.getClass().cast(returnValue);
    }

    public void resetSharedPrefsValue() {
        Log.d(TAG, "Resetting shared preference");
        editor.clear();
        editor.apply();
    }

    public interface OnSharedPrefsValueChangeListener {
        void onValueChanged(String key, Object oldValue, Object newValue);
    }

    public void attach(OnSharedPrefsValueChangeListener onSharedPrefsValueChangeListener) {
        if (mSharedPrefValueChangeListeners != null) {
            this.mSharedPrefValueChangeListeners.add(onSharedPrefsValueChangeListener);
        }
    }

    public void detach(OnSharedPrefsValueChangeListener onSharedPrefsValueChangeListener) {
        if (mSharedPrefValueChangeListeners != null) {
            this.mSharedPrefValueChangeListeners.remove(onSharedPrefsValueChangeListener);
        }
    }
}
