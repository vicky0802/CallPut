package bulltrack.com.optionanalyzer.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import bulltrack.com.optionanalyzer.adapter.AdapterTodaysCall;
import bulltrack.com.optionanalyzer.application.MyGreeksApplication;
import bulltrack.com.optionanalyzer.constants.Constants;
import bulltrack.com.optionanalyzer.dao.TradeCalls;
import bulltrack.com.optiongreeks13.R;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

public class CallPerformanceMain extends AppCompatActivity {
    /* access modifiers changed from: private */
    public String TAG = "CallPerformanceMain";
    /* access modifiers changed from: private */
    public Activity activity;

    /* renamed from: li */
    List<TradeCalls> f80li;
    ListView lvCalls;
    AdapterTodaysCall mainAdapter;
    String webServiceUrl = Constants.URL_SERVICE;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_legs_progress);
        this.activity = this;
        setTitle(getResources().getString(R.string.call_performance));
        webServiceCallGetCalls(Constants.TODAYS_CALL_TYPE_HISTORY);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() != 16908332) {
            return super.onOptionsItemSelected(menuItem);
        }
        if (getParent() == null) {
            onBackPressed();
            return true;
        }
        NavUtils.navigateUpFromSameTask(this);
        return true;
    }

    /* access modifiers changed from: protected */
    public MyGreeksApplication getGreekApplication() {
        return (MyGreeksApplication) getApplicationContext();
    }

    public void webServiceCallGetCalls(final String str) {
        final String jNICallResult_getGold = getGreekApplication().getJNICallResult_getGold();
        getGreekApplication().addToRequestQueue(new JsonObjectRequest(0, this.webServiceUrl + "7", (JSONObject) null, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject jSONObject) {
                String jSONObject2 = jSONObject.toString();
                JSONObject jSONObject3 = new JSONObject();
                try {
                    JSONObject jSONObject4 = new JSONObject();
                    jSONObject4.put("token", new JSONObject(jSONObject2).get("token"));
                    jSONObject4.put("tc_data_key", str);
                    jSONObject4.put("str_price_date", (Object) null);
                    byte[][] doEncryption = CallPerformanceMain.this.getGreekApplication().doEncryption(jSONObject4.toString(), jNICallResult_getGold);
                    jSONObject3.put("salt", Base64.encodeToString(doEncryption[0], 0));
                    jSONObject3.put("iv", Base64.encodeToString(doEncryption[1], 0));
                    jSONObject3.put("tonka", Base64.encodeToString(doEncryption[2], 0));
                } catch (JSONException e) {
                    String access$000 = CallPerformanceMain.this.TAG;
                    Log.d(access$000, "Error | Volley Call | Trade Calls : " + e.toString());
                }
                try {
                    StringRequest stringRequest = new StringRequest(0, String.format(CallPerformanceMain.this.webServiceUrl + "%1$s&condition=%2$s", new Object[]{"90", URLEncoder.encode(jSONObject3.toString(), "UTF-8")}), new Response.Listener<String>() {
                        public void onResponse(String str) {
                            Log.d(CallPerformanceMain.this.TAG, "webServiceCallGetCalls got resposne");
                            Type type = new TypeToken<List<TradeCalls>>() {
                            }.getType();
                            Gson create = new GsonBuilder().setDateFormat(Constants.DT_FMT_yyyy_MM_dd_HH_m_ss).create();
                            try {
                                CallPerformanceMain.this.f80li = (List) create.fromJson(str.toString(), type);
                            } catch (Exception e) {
                                Log.d(CallPerformanceMain.this.TAG, e.getMessage());
                            }
                            if (CallPerformanceMain.this.f80li == null || CallPerformanceMain.this.f80li.size() == 0) {
                                CallPerformanceMain.this.setContentView((int) R.layout.chart_error_message);
                                return;
                            }
                            CallPerformanceMain.this.setContentView((int) R.layout.call_performance_main);
                            CallPerformanceMain.this.lvCalls = (ListView) CallPerformanceMain.this.findViewById(R.id.lv_call_perf_main);
                            CallPerformanceMain.this.mainAdapter = new AdapterTodaysCall(CallPerformanceMain.this.activity, CallPerformanceMain.this.f80li, 1);
                            CallPerformanceMain.this.lvCalls.setAdapter(CallPerformanceMain.this.mainAdapter);
                            CallPerformanceMain.this.mainAdapter.setItemList(CallPerformanceMain.this.f80li);
                            CallPerformanceMain.this.mainAdapter.notifyDataSetChanged();
                        }
                    }, new Response.ErrorListener() {
                        public void onErrorResponse(VolleyError volleyError) {
                            String access$000 = CallPerformanceMain.this.TAG;
                            VolleyLog.d(access$000, "Error: " + volleyError.getMessage());
                            CallPerformanceMain.this.setContentView((int) R.layout.chart_error_message);
                        }
                    });
                    CallPerformanceMain.this.getGreekApplication();
                    MyGreeksApplication.getInstance().addToRequestQueue(stringRequest);
                } catch (Exception e2) {
                    String access$0002 = CallPerformanceMain.this.TAG;
                    Log.d(access$0002, "Error | Volley Call | Trade Calls : " + e2.toString());
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError volleyError) {
                String access$000 = CallPerformanceMain.this.TAG;
                VolleyLog.d(access$000, "Error: " + volleyError.getMessage());
                CallPerformanceMain.this.setContentView((int) R.layout.chart_error_message);
            }
        }), "json_obj_req_getcalls_perf");
    }
}
