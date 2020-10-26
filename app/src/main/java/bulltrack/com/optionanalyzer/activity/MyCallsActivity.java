package bulltrack.com.optionanalyzer.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

public class MyCallsActivity extends AppCompatActivity {
    /* access modifiers changed from: private */
    public String TAG = "MyCallsActivity";
    /* access modifiers changed from: private */
    public Activity activity;
    ArrayList<String> arrMyCalls = new ArrayList<>();

    /* renamed from: li */
    List<TradeCalls> f87li;
    ListView lvMyCalls;
    AdapterTodaysCall mainAdapter;
    ProgressBar progressBar;
    TextView tvAccesText;
    String webServiceUrl = Constants.URL_SERVICE;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_my_calls);
        this.activity = this;
        this.lvMyCalls = (ListView) findViewById(R.id.lv_my_calls_main);
        this.tvAccesText = (TextView) findViewById(R.id.tv_my_calls_accesstext);
        ProgressBar progressBar2 = (ProgressBar) findViewById(R.id.progressbar_activity_my_calls);
        this.progressBar = progressBar2;
        progressBar2.setVisibility(0);
        this.tvAccesText.setVisibility(4);
        new Thread(new Runnable() {
            public void run() {
                MyCallsActivity myCallsActivity = MyCallsActivity.this;
                myCallsActivity.arrMyCalls = (ArrayList) myCallsActivity.getMyCalls();
                try {
                    MyCallsActivity.this.getGreekApplication().deleteTradingCallStgAccess();
                } catch (Exception unused) {
                    Log.e(MyCallsActivity.this.TAG, "Error deleteing old trading call strategies");
                }
                synchronized (this) {
                    MyCallsActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            if (MyCallsActivity.this.arrMyCalls.size() > 0) {
                                MyCallsActivity.this.tvAccesText.setVisibility(0);
                                MyCallsActivity.this.lvMyCalls.setAdapter(new ArrayAdapter(MyCallsActivity.this.activity, R.layout.item_my_calls, R.id.tv_item_my_calls_tradedate, MyCallsActivity.this.arrMyCalls));
                                MyCallsActivity.this.lvMyCalls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                                        try {
                                            MyCallsActivity.this.lvMyCalls.setVisibility(4);
                                            MyCallsActivity.this.tvAccesText.setVisibility(8);
                                            MyCallsActivity.this.progressBar.setVisibility(0);
                                            MyCallsActivity.this.webServiceCallGetCalls(Constants.TODAYS_CALL_TYPE_FORDATE, MyCallsActivity.this.getGreekApplication().dateFormatter(MyCallsActivity.this.getGreekApplication().dateFormatter(MyCallsActivity.this.arrMyCalls.get(i), Constants.DT_FMT_dd_MMM_yyyy), Constants.DT_FMT_yyyy_MM_dd_HH_m_ss));
                                        } catch (Exception e) {
                                            Log.d(MyCallsActivity.this.TAG, e.getMessage());
                                        }
                                    }
                                });
                            } else {
                                MyCallsActivity.this.lvMyCalls.setEmptyView(MyCallsActivity.this.findViewById(R.id.tv_my_calls_empty_no_error));
                                MyCallsActivity.this.tvAccesText.setVisibility(4);
                            }
                            MyCallsActivity.this.progressBar.setVisibility(4);
                        }
                    });
                }
            }
        }).start();
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

    /* access modifiers changed from: private */
    public List<String> getMyCalls() {
        return getGreekApplication().getMyCalls();
    }

    public void webServiceCallGetCalls(final String str, final String str2) {
        final String jNICallResult_getGold = getGreekApplication().getJNICallResult_getGold();
        getGreekApplication().addToRequestQueue(new JsonObjectRequest(0, this.webServiceUrl + "7", (JSONObject) null, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject jSONObject) {
                String jSONObject2 = jSONObject.toString();
                JSONObject jSONObject3 = new JSONObject();
                try {
                    JSONObject jSONObject4 = new JSONObject();
                    jSONObject4.put("token", new JSONObject(jSONObject2).get("token"));
                    jSONObject4.put("tc_data_key", str);
                    jSONObject4.put("str_price_date", str2);
                    byte[][] doEncryption = MyCallsActivity.this.getGreekApplication().doEncryption(jSONObject4.toString(), jNICallResult_getGold);
                    jSONObject3.put("salt", Base64.encodeToString(doEncryption[0], 0));
                    jSONObject3.put("iv", Base64.encodeToString(doEncryption[1], 0));
                    jSONObject3.put("tonka", Base64.encodeToString(doEncryption[2], 0));
                } catch (JSONException e) {
                    String access$100 = MyCallsActivity.this.TAG;
                    Log.d(access$100, "Error | Volley Call | Trade Calls : " + e.toString());
                }
                try {
                    StringRequest stringRequest = new StringRequest(0, String.format(MyCallsActivity.this.webServiceUrl + "%1$s&condition=%2$s", new Object[]{"90", URLEncoder.encode(jSONObject3.toString(), "UTF-8")}), new Response.Listener<String>() {
                        public void onResponse(String str) {
                            Log.d(MyCallsActivity.this.TAG, "webServiceCallGetCalls got resposne");
                            Type type = new TypeToken<List<TradeCalls>>() {
                            }.getType();
                            Gson create = new GsonBuilder().setDateFormat(Constants.DT_FMT_yyyy_MM_dd_HH_m_ss).create();
                            MyCallsActivity.this.f87li = (List) create.fromJson(str.toString(), type);
                            if (MyCallsActivity.this.f87li == null || MyCallsActivity.this.f87li.size() == 0) {
                                MyCallsActivity.this.setContentView((int) R.layout.no_data_layout);
                                return;
                            }
                            MyCallsActivity.this.mainAdapter = new AdapterTodaysCall(MyCallsActivity.this.activity, MyCallsActivity.this.f87li, 1);
                            MyCallsActivity.this.lvMyCalls.setAdapter(MyCallsActivity.this.mainAdapter);
                            MyCallsActivity.this.mainAdapter.setItemList(MyCallsActivity.this.f87li);
                            MyCallsActivity.this.mainAdapter.notifyDataSetChanged();
                            MyCallsActivity.this.lvMyCalls.getHandler().post(new Runnable() {
                                public void run() {
                                    MyCallsActivity.this.lvMyCalls.setVisibility(0);
                                }
                            });
                            MyCallsActivity.this.progressBar.getHandler().post(new Runnable() {
                                public void run() {
                                    MyCallsActivity.this.progressBar.setVisibility(4);
                                }
                            });
                        }
                    }, new Response.ErrorListener() {
                        public void onErrorResponse(VolleyError volleyError) {
                            String access$100 = MyCallsActivity.this.TAG;
                            VolleyLog.d(access$100, "Error: " + volleyError.getMessage());
                            MyCallsActivity.this.setContentView((int) R.layout.chart_error_message);
                        }
                    });
                    MyCallsActivity.this.getGreekApplication();
                    MyGreeksApplication.getInstance().addToRequestQueue(stringRequest);
                } catch (Exception e2) {
                    String access$1002 = MyCallsActivity.this.TAG;
                    Log.d(access$1002, "Error | Volley Call | Trade Calls : " + e2.toString());
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError volleyError) {
                String access$100 = MyCallsActivity.this.TAG;
                VolleyLog.d(access$100, "Error: " + volleyError.getMessage());
                MyCallsActivity.this.setContentView((int) R.layout.chart_error_message);
            }
        }), "json_obj_req_tc_getcalls");
    }
}
