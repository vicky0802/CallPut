package bulltrack.com.optionanalyzer.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class LaunchActivity extends Activity {
    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                LaunchActivity.this.startActivity(new Intent(LaunchActivity.this, MainActivity.class));
                LaunchActivity.this.overridePendingTransition(17432576, 17432577);
                LaunchActivity.this.finish();
            }
        }, 500);
    }
}
