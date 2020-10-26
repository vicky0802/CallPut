package bulltrack.com.optionanalyzer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import bulltrack.com.optionanalyzer.application.MyGreeksApplication;
import bulltrack.com.optionanalyzer.constants.Constants;
import bulltrack.com.optiongreeks13.R;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import org.json.JSONException;
import org.json.JSONObject;

public class DemoYTActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {
    static String DEVELOPER_KEY = "AIzaSyBZdiRiUpAsg6GZxubh6dWv4zQYw1IUGYw";
    private static final int RECOVERY_DIALOG_REQUEST = 1;
    static String YOUTUBE_VIDEO_CODE;
    static String YOUTUBE_VIDEO_TEXT;
    /* access modifiers changed from: private */
    public String TAG = "DemoYTActivity";
    /* access modifiers changed from: private */
    public TextView tvVideoText;
    String webServiceUrl = Constants.URL_SERVICE;
    private YouTubePlayerView youTubeView;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_demo);
        webServiceCallVideo();
        this.youTubeView = (YouTubePlayerView) findViewById(R.id.ytv_activity_demo);
        this.tvVideoText = (TextView) findViewById(R.id.tv_activity_demo_text);
        ((ImageView) findViewById(R.id.img_activity_demo_close)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                DemoYTActivity.this.finish();
            }
        });
        this.youTubeView.initialize(DEVELOPER_KEY, this);
    }

    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        if (youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(this, 1).show();
        } else {
            Toast.makeText(this, "Error", 1).show();
        }
    }

    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean z) {
        if (!z && youTubePlayer != null) {
            youTubePlayer.cueVideo(YOUTUBE_VIDEO_CODE);
            youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);
        }
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        if (i == 1) {
            getYouTubePlayerProvider().initialize(DEVELOPER_KEY, this);
        }
    }

    private YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerView) findViewById(R.id.ytv_activity_demo);
    }

    public void webServiceCallVideo() {
        getGreekApplication().addToRequestQueue(new JsonObjectRequest(0, this.webServiceUrl + "93", (JSONObject) null, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject jSONObject) {
                try {
                    JSONObject jSONObject2 = new JSONObject(jSONObject.toString());
                    DemoYTActivity.YOUTUBE_VIDEO_CODE = jSONObject2.get("video_id").toString();
                    DemoYTActivity.YOUTUBE_VIDEO_TEXT = jSONObject2.get("video_text").toString();
                    if (DemoYTActivity.YOUTUBE_VIDEO_TEXT != null) {
                        DemoYTActivity.this.tvVideoText.setText(DemoYTActivity.YOUTUBE_VIDEO_TEXT);
                    }
                } catch (JSONException e) {
                    String access$100 = DemoYTActivity.this.TAG;
                    Log.d(access$100, "Error | Volley Call | Trade Calls : " + e.toString());
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError volleyError) {
                String access$100 = DemoYTActivity.this.TAG;
                VolleyLog.d(access$100, "Error: " + volleyError.getMessage());
            }
        }));
    }

    /* access modifiers changed from: protected */
    public MyGreeksApplication getGreekApplication() {
        return (MyGreeksApplication) getApplicationContext();
    }
}
