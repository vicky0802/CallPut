package bulltrack.com.optionanalyzer.service;

import android.util.Log;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class FCMInitializationService extends FirebaseInstanceIdService {
    private static final String TAG = "FCMInitializationServ";

    public void onTokenRefresh() {
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "FCM Dev Token:" + token);
    }
}
