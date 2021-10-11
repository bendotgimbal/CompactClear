package compact.mobile.config.firebase.service2;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Yosua Tony Batara on 21/05/2018.
 */

public class MySharedPreference {
    private SharedPreferences prefs;
    private Context context;
    public static final String FIREBASE_CLOUD_MESSAGING = "fcm";
    public static final String SET_NOTIFY = "set_notify";
    public MySharedPreference(Context context){
        this.context = context;
        prefs = context.getSharedPreferences(FIREBASE_CLOUD_MESSAGING, Context.MODE_PRIVATE);
    }
    public void saveNotificationSubscription(boolean value){
        SharedPreferences.Editor edits = prefs.edit();
        edits.putBoolean(SET_NOTIFY, value);
        edits.apply();
    }
    public boolean hasUserSubscribeToNotification(){
        return prefs.getBoolean(SET_NOTIFY, false);
    }
    public void clearAllSubscriptions(){
        prefs.edit().clear().apply();
    }
}
