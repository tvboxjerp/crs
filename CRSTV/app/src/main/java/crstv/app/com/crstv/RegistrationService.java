package crstv.app.com.crstv;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;


/**
 * Created by eutiquio on 1/19/18.
 */

public class RegistrationService extends IntentService {
    public RegistrationService() {
        super("RegistrationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try{
            // Non-blocking methods. No need to use AsyncTask or background thread.
            FirebaseMessaging.getInstance().subscribeToTopic("/topics/notifications");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}