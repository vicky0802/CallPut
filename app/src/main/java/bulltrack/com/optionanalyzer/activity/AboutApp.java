package bulltrack.com.optionanalyzer.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import bulltrack.com.optionanalyzer.BuildConfig;
import bulltrack.com.optionanalyzer.application.MyGreeksApplication;
import bulltrack.com.optionanalyzer.constants.Constants;
import bulltrack.com.optiongreeks13.R;
import java.util.Calendar;

public class AboutApp extends AppCompatActivity {
    public static final String LAST_REFRESH_DATETIME = "last_refresh_dt";
    public static final String MY_PREF = "my_pref";
    /* access modifiers changed from: private */
    public String TAG = "AboutApp";
    private Activity activity;
    int counter = 0;
    ProgressBar progressBar;
    SharedPreferences sharedpreferences;
    TextView tvAppId = null;
    TextView tvBlist = null;
    TextView tvBlistCnt = null;
    TextView tvReward = null;
    TextView tvRewardCnt = null;
    TextView tvStgAccessCnt = null;
    TextView tvTerms = null;
    TextView tvVersion = null;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_about_app);
        this.activity = this;
        this.tvAppId = (TextView) findViewById(R.id.tv_activity_about_app_id);
        this.tvBlist = (TextView) findViewById(R.id.tv_activity_about_blist);
        this.tvReward = (TextView) findViewById(R.id.tv_activity_about_reward);
        this.tvBlistCnt = (TextView) findViewById(R.id.tv_activity_about_blist_count);
        this.tvRewardCnt = (TextView) findViewById(R.id.tv_activity_about_reward_count);
        this.tvVersion = (TextView) findViewById(R.id.tv_activity_about_app_version);
        this.tvStgAccessCnt = (TextView) findViewById(R.id.tv_activity_about_stgaccess_count_count);
        this.tvTerms = (TextView) findViewById(R.id.tv_activity_about_terms);
        this.progressBar = (ProgressBar) findViewById(R.id.progressbar);
        TextView textView = this.tvAppId;
        textView.setText("Install ID: " + getGreekApplication().getAppInstallId());
        this.tvReward.setText(getGreekApplication().getRewardFlag());
        this.tvBlist.setText(getGreekApplication().getBlistFlags()[0]);
        this.tvStgAccessCnt.setText(Integer.toString(getGreekApplication().countStgAccess()));
        TextView textView2 = this.tvTerms;
        textView2.setPaintFlags(textView2.getPaintFlags() | 8);
        this.tvTerms.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AboutApp.this.tvTerms.getText().toString();
                Intent intent = new Intent("android.intent.action.VIEW");
                intent.setData(Uri.parse("http://www.bulltrack.in/terms.txt"));
                AboutApp.this.startActivity(intent);
            }
        });
        TextView textView3 = this.tvVersion;
        textView3.setText("Version :" + BuildConfig.VERSION_NAME);
        try {
            TextView textView4 = this.tvBlistCnt;
            textView4.setText(getGreekApplication().getBlistRecordCount() + "");
            TextView textView5 = this.tvRewardCnt;
            textView5.setText(getGreekApplication().getRewardRecordCount() + "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((RelativeLayout) findViewById(R.id.rl_activity_about_app_top_layout)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AboutApp.this.counter++;
                if (AboutApp.this.counter == 5) {
                    new AsyncListViewLoader().execute(new String[0]);
                }
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        menuItem.getItemId();
        if (menuItem.getItemId() == 16908332) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    /* access modifiers changed from: protected */
    public MyGreeksApplication getGreekApplication() {
        return (MyGreeksApplication) getApplicationContext();
    }

    private class AsyncListViewLoader extends AsyncTask<String, Void, String> {
        private AsyncListViewLoader() {
        }

        /* access modifiers changed from: protected */
        public String doInBackground(String... strArr) {
            try {
                AboutApp.this.getGreekApplication().callServerRewards();
                AboutApp.this.getGreekApplication().callServerRewardsTC();
                AboutApp.this.getGreekApplication().callServerRewardsFinder();
                AboutApp.this.sharedpreferences = AboutApp.this.getApplicationContext().getSharedPreferences("my_pref", 0);
                SharedPreferences.Editor edit = AboutApp.this.sharedpreferences.edit();
                edit.putLong("last_refresh_dt", Calendar.getInstance().getTimeInMillis() - Constants.SERVER_CHECK_INTERVAL);
                edit.commit();
                return Constants.SUCCESS_FLAG;
            } catch (Exception e) {
                String access$100 = AboutApp.this.TAG;
                Log.d(access$100, "Exception in callServerRewards()" + e.getMessage());
                return "fail";
            }
        }

        /* access modifiers changed from: protected */
        public void onPreExecute() {
            super.onPreExecute();
            AboutApp.this.progressBar.setVisibility(View.VISIBLE);
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(String str) {
            super.onPostExecute(str);
            AboutApp.this.progressBar.setVisibility(View.INVISIBLE);
            if (str.equals(Constants.SUCCESS_FLAG)) {
                Toast.makeText(AboutApp.this.getApplicationContext(), "Data Refreshed... ", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(AboutApp.this.getApplicationContext(), "Refresh Error... ", Toast.LENGTH_LONG).show();
            }
        }
    }
}
