package bulltrack.com.optionanalyzer.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import bulltrack.com.optionanalyzer.adapter.AdapterTodaysCall;
import bulltrack.com.optionanalyzer.application.MyGreeksApplication;
import bulltrack.com.optionanalyzer.constants.Constants;
import bulltrack.com.optionanalyzer.dao.StrategyResultsFilter;
import bulltrack.com.optionanalyzer.dao.TradeCalls;
import bulltrack.com.optionanalyzer.utility.billing.IabHelper;
import bulltrack.com.optionanalyzer.utility.billing.IabResult;
import bulltrack.com.optionanalyzer.utility.billing.Inventory;
import bulltrack.com.optionanalyzer.utility.billing.Purchase;
import bulltrack.com.optiongreeks13.R;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import org.json.JSONException;
import org.json.JSONObject;

public class TodaysCallActivity extends AppCompatActivity {
    static final int RC_REQUEST = 10002;
    static final String SKU_GAS = "gas";
    static String SKU_PREMIUM = "01_tcalls_35";
    /* access modifiers changed from: private */
    public String TAG = "TodaysCallActivity";
    /* access modifiers changed from: private */
    public Activity activity;
    Button btnBuy;
    Date buyEndTime;
    CheckBox cbTerms;

    /* renamed from: li */
    List<TradeCalls> f94li;
    ListView lvTodaysCall;
    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
        public void onConsumeFinished(Purchase purchase, IabResult iabResult) {
            String access$000 = TodaysCallActivity.this.TAG;
            Log.d(access$000, "Consumption finished. Purchase: " + purchase + ", result: " + iabResult);
            if (TodaysCallActivity.this.mHelper != null) {
                if (iabResult.isSuccess()) {
                    Log.d(TodaysCallActivity.this.TAG, "Consumption successful. Provisioning.");
                } else {
                    TodaysCallActivity todaysCallActivity = TodaysCallActivity.this;
                    todaysCallActivity.complain("Error while consuming: " + iabResult);
                }
                Log.d(TodaysCallActivity.this.TAG, "End consumption flow.");
            }
        }
    };
    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult iabResult, Inventory inventory) {
            Log.d(TodaysCallActivity.this.TAG, "Query inventory finished.");
            if (iabResult.isFailure()) {
                String access$000 = TodaysCallActivity.this.TAG;
                Log.d(access$000, "Failed to query inventory: " + iabResult);
                return;
            }
            Log.d(TodaysCallActivity.this.TAG, "Query inventory was successful.");
            try {
                if (inventory.hasPurchase(TodaysCallActivity.SKU_PREMIUM)) {
                    String access$0002 = TodaysCallActivity.this.TAG;
                    Log.d(access$0002, "already purchased : " + TodaysCallActivity.SKU_PREMIUM);
                    TodaysCallActivity.this.mHelper.consumeAsync(inventory.getPurchase(TodaysCallActivity.SKU_PREMIUM), TodaysCallActivity.this.mConsumeFinishedListener);
                }
            } catch (IabHelper.IabAsyncInProgressException unused) {
                TodaysCallActivity.this.complain("Error launching purchase flow. Another async operation in progress.");
            }
        }
    };
    IabHelper mHelper;
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult iabResult, Purchase purchase) {
            String access$000 = TodaysCallActivity.this.TAG;
            Log.d(access$000, "Purchase finished: " + iabResult + ", purchase: " + purchase);
            if (TodaysCallActivity.this.mHelper != null) {
                if (iabResult.isFailure()) {
                    TodaysCallActivity.this.complain("Payment unsuccessful");
                } else if (!TodaysCallActivity.this.verifyDeveloperPayload(purchase)) {
                    TodaysCallActivity.this.complain("Error purchasing. Authenticity verification failed.");
                } else {
                    Log.d(TodaysCallActivity.this.TAG, "Purchase successful.");
                    if (purchase.getSku().equals(TodaysCallActivity.SKU_GAS)) {
                        Log.d(TodaysCallActivity.this.TAG, "Purchase is gas. Starting gas consumption.");
                        try {
                            TodaysCallActivity.this.mHelper.consumeAsync(purchase, TodaysCallActivity.this.mConsumeFinishedListener);
                        } catch (IabHelper.IabAsyncInProgressException unused) {
                            TodaysCallActivity.this.complain("Error consuming gas. Another async operation in progress.");
                        }
                    } else if (purchase.getSku().equals(TodaysCallActivity.SKU_PREMIUM)) {
                        Log.d(TodaysCallActivity.this.TAG, "Purchase is premium upgrade. Congratulating user.");
                        TodaysCallActivity.this.alert("Purchase Successful. Thank you!");
                        TodaysCallActivity.this.showTodaysCall();
                        TodaysCallActivity.this.paidSuccess = true;
                        TodaysCallActivity.this.getGreekApplication().insertStgAccess(TodaysCallActivity.this.stgResult);
                        TodaysCallActivity.this.webServiceCallPayment(purchase.getOriginalJson());
                    }
                }
            }
        }
    };
    AdapterTodaysCall mainAdapter;
    boolean paidSuccess = false;
    ProgressBar progressBar;
    ScrollView rlBuyFrame;
    boolean runException = false;
    StrategyResultsFilter stgResult;
    TextView tvCntIntraday;
    TextView tvCntOption;
    TextView tvCntPositional;
    TextView tvCntStock;
    TextView tvPayAmount;
    TextView tvSummary;
    TextView tvTotalCalls;
    String webServiceUrl = Constants.URL_SERVICE;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_legs_progress);
        this.activity = this;
        setTitle(getResources().getString(R.string.todays_call));
        webServiceCallGetCalls(Constants.TODAYS_CALL_TYPE_LATEST);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId != 16908332) {
            if (itemId == R.id.action_demo) {
                startActivity(new Intent(this, DemoYTActivity.class));
            }
            return super.onOptionsItemSelected(menuItem);
        } else if (getParent() == null) {
            onBackPressed();
            return true;
        } else {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_demo, menu);
        return true;
    }

    /* access modifiers changed from: private */
    public void LoadUI() {
        setContentView((int) R.layout.activity_todays_call);
        this.lvTodaysCall = (ListView) findViewById(R.id.lv_activity_todays_call_main);
        this.tvSummary = (TextView) findViewById(R.id.tv_activity_todays_call_summary);
        this.tvTotalCalls = (TextView) findViewById(R.id.tv_activity_todays_call_totalcalls);
        this.tvCntOption = (TextView) findViewById(R.id.tv_activity_todays_call_cnt_option);
        this.tvCntStock = (TextView) findViewById(R.id.tv_activity_todays_call_cnt_stock);
        this.tvCntIntraday = (TextView) findViewById(R.id.tv_activity_todays_call_cnt_intraday);
        this.tvCntPositional = (TextView) findViewById(R.id.tv_activity_todays_call_cnt_positional);
        this.tvPayAmount = (TextView) findViewById(R.id.tv_activity_todays_call_amount);
        this.cbTerms = (CheckBox) findViewById(R.id.cb_activity_todays_call_terms);
        this.btnBuy = (Button) findViewById(R.id.btn_activity_todays_call_buy);
        this.rlBuyFrame = (ScrollView) findViewById(R.id.rl_activity_todays_call_buy_frame);
        this.lvTodaysCall.setVisibility(8);
        this.rlBuyFrame.setVisibility(0);
    }

    /* access modifiers changed from: private */
    public void googleBilling() {
        IabHelper iabHelper = new IabHelper(this, Constants.GOOGLE_BASE64_ENCODING_KEY);
        this.mHelper = iabHelper;
        iabHelper.enableDebugLogging(true);
        this.mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult iabResult) {
                Log.d(TodaysCallActivity.this.TAG, "Setup finished.");
                if (!iabResult.isSuccess()) {
                    TodaysCallActivity todaysCallActivity = TodaysCallActivity.this;
                    todaysCallActivity.complain("Problem setting up in-app billing: " + iabResult);
                } else if (TodaysCallActivity.this.mHelper != null) {
                    try {
                        TodaysCallActivity.this.mHelper.queryInventoryAsync(TodaysCallActivity.this.mGotInventoryListener);
                    } catch (IabHelper.IabAsyncInProgressException unused) {
                        TodaysCallActivity.this.complain("Error launching purchase flow. Another async operation in progress.");
                    }
                    Log.d(TodaysCallActivity.this.TAG, "Setup successful. Querying inventory.");
                }
            }
        });
    }

    /* access modifiers changed from: package-private */
    public boolean verifyDeveloperPayload(Purchase purchase) {
        purchase.getDeveloperPayload();
        return true;
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        String str = this.TAG;
        Log.d(str, "onActivityResult(" + i + "," + i2 + "," + intent);
        IabHelper iabHelper = this.mHelper;
        if (iabHelper != null) {
            if (!iabHelper.handleActivityResult(i, i2, intent)) {
                super.onActivityResult(i, i2, intent);
            } else {
                Log.d(this.TAG, "onActivityResult handled by IABUtil.");
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void complain(String str) {
        String str2 = this.TAG;
        Log.e(str2, "**** TrivialDrive Error: " + str);
        alert("Error: " + str);
    }

    /* access modifiers changed from: package-private */
    public void alert(String str) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(str);
        builder.setNeutralButton("OK", (DialogInterface.OnClickListener) null);
        String str2 = this.TAG;
        Log.d(str2, "Showing alert dialog: " + str);
        builder.create().show();
    }

    public void onBuyButtonClicked(View view) {
        if (Calendar.getInstance(TimeZone.getTimeZone("Asia/Calcutta")).getTimeInMillis() > this.buyEndTime.getTime()) {
            alert("Time limit [ " + getGreekApplication().dateFormatter(this.buyEndTime, Constants.DT_FMT_HH_mm) + " hours ] expired. Please try tomorrow  ");
        } else if (!this.cbTerms.isChecked()) {
            alert("Terms and conditions not agreed. Exiting");
        } else {
            Log.d(this.TAG, "Upgrade button clicked; launching purchase flow for upgrade.");
            try {
                if (this.mHelper != null) {
                    this.mHelper.launchPurchaseFlow(this, SKU_PREMIUM, RC_REQUEST, this.mPurchaseFinishedListener, "");
                    return;
                }
                Toast.makeText(getApplicationContext(), "Error: Please try later", 1).show();
            } catch (IabHelper.IabAsyncInProgressException unused) {
                complain("Error launching purchase flow. Another async operation in progress.");
            }
        }
    }

    /* access modifiers changed from: private */
    public void showTodaysCall() {
        if (this.rlBuyFrame.getVisibility() != 8) {
            this.rlBuyFrame.setVisibility(8);
            this.lvTodaysCall.setVisibility(0);
            if (this.f94li.size() > 0) {
                setTitle("Calls for " + getGreekApplication().dateFormatter(this.f94li.get(0).getNextTradeDate(), Constants.DT_FMT_dd_MMM_yyyy));
            }
        }
    }

    /* access modifiers changed from: private */
    public void webServiceCallPayment(final String str) {
        final String jNICallResult_getGold = getGreekApplication().getJNICallResult_getGold();
        getGreekApplication().addToRequestQueue(new JsonObjectRequest(0, this.webServiceUrl + "7", (JSONObject) null, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject jSONObject) {
                String jSONObject2 = jSONObject.toString();
                JSONObject jSONObject3 = new JSONObject();
                try {
                    byte[][] doEncryption = TodaysCallActivity.this.getGreekApplication().doEncryption(str, jNICallResult_getGold);
                    jSONObject3.put("installid", TodaysCallActivity.this.getGreekApplication().getAppInstallId());
                    jSONObject3.put("token", new JSONObject(jSONObject2).get("token"));
                    jSONObject3.put("salt", Base64.encodeToString(doEncryption[0], 0));
                    jSONObject3.put("iv", Base64.encodeToString(doEncryption[1], 0));
                    jSONObject3.put("tonka", Base64.encodeToString(doEncryption[2], 0));
                } catch (JSONException e) {
                    String access$000 = TodaysCallActivity.this.TAG;
                    Log.d(access$000, "Error | Volley Call | Todays Call : " + e.toString());
                }
                try {
                    StringRequest stringRequest = new StringRequest(0, String.format(TodaysCallActivity.this.webServiceUrl + "%1$s&condition=%2$s", new Object[]{"84", URLEncoder.encode(jSONObject3.toString(), "UTF-8")}), new Response.Listener<String>() {
                        public void onResponse(String str) {
                            Log.d(TodaysCallActivity.this.TAG, "webServiceCallPayment got response");
                        }
                    }, new Response.ErrorListener() {
                        public void onErrorResponse(VolleyError volleyError) {
                            String access$000 = TodaysCallActivity.this.TAG;
                            VolleyLog.d(access$000, "Error: " + volleyError.getMessage());
                        }
                    });
                    TodaysCallActivity.this.getGreekApplication();
                    MyGreeksApplication.getInstance().addToRequestQueue(stringRequest);
                } catch (Exception e2) {
                    String access$0002 = TodaysCallActivity.this.TAG;
                    Log.d(access$0002, "Error | Volley Call | Todays Call : " + e2.toString());
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError volleyError) {
                String access$000 = TodaysCallActivity.this.TAG;
                VolleyLog.d(access$000, "Error: " + volleyError.getMessage());
            }
        }), "json_obj_req_tc");
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
                    byte[][] doEncryption = TodaysCallActivity.this.getGreekApplication().doEncryption(jSONObject4.toString(), jNICallResult_getGold);
                    jSONObject3.put("salt", Base64.encodeToString(doEncryption[0], 0));
                    jSONObject3.put("iv", Base64.encodeToString(doEncryption[1], 0));
                    jSONObject3.put("tonka", Base64.encodeToString(doEncryption[2], 0));
                } catch (JSONException e) {
                    String access$000 = TodaysCallActivity.this.TAG;
                    Log.d(access$000, "Error | Volley Call | Trade Calls : " + e.toString());
                }
                try {
                    StringRequest stringRequest = new StringRequest(0, String.format(TodaysCallActivity.this.webServiceUrl + "%1$s&condition=%2$s", new Object[]{"90", URLEncoder.encode(jSONObject3.toString(), "UTF-8")}), new Response.Listener<String>() {
                        public void onResponse(String str) {
                            Log.d(TodaysCallActivity.this.TAG, "webServiceCallGetCalls got resposne");
                            Type type = new TypeToken<List<TradeCalls>>() {
                            }.getType();
                            Gson create = new GsonBuilder().setDateFormat(Constants.DT_FMT_yyyy_MM_dd_HH_m_ss).create();
                            try {
                                TodaysCallActivity.this.f94li = (List) create.fromJson(str.toString(), type);
                            } catch (Exception e) {
                                Log.d(TodaysCallActivity.this.TAG, e.getMessage());
                            }
                            if (TodaysCallActivity.this.f94li == null || TodaysCallActivity.this.f94li.size() == 0) {
                                TodaysCallActivity.this.setContentView((int) R.layout.no_data_layout);
                                return;
                            }
                            TodaysCallActivity.this.LoadUI();
                            TodaysCallActivity.this.getSummary(TodaysCallActivity.this.f94li);
                            TodaysCallActivity.this.mainAdapter = new AdapterTodaysCall(TodaysCallActivity.this.activity, TodaysCallActivity.this.f94li, 0);
                            TodaysCallActivity.this.lvTodaysCall.setAdapter(TodaysCallActivity.this.mainAdapter);
                            TodaysCallActivity.this.mainAdapter.setItemList(TodaysCallActivity.this.f94li);
                            TodaysCallActivity.this.mainAdapter.notifyDataSetChanged();
                            TodaysCallActivity.this.setStgObjectForSaving(TodaysCallActivity.this.f94li);
                            if (TodaysCallActivity.this.isBlisted()) {
                                TodaysCallActivity.this.complain("Possible app integrity violated! Please contact app support");
                            } else if (TodaysCallActivity.this.getGreekApplication().isStgAccessToUser(TodaysCallActivity.this.stgResult)) {
                                TodaysCallActivity.this.showTodaysCall();
                            } else if (TodaysCallActivity.this.getGreekApplication().getRewardTCFlag().equals(Constants.REWARD_IS_AVAILABLE)) {
                                TodaysCallActivity.this.webServiceCallIsRewardAvailable();
                                TodaysCallActivity.this.googleBilling();
                            } else {
                                TodaysCallActivity.this.googleBilling();
                            }
                        }
                    }, new Response.ErrorListener() {
                        public void onErrorResponse(VolleyError volleyError) {
                            String access$000 = TodaysCallActivity.this.TAG;
                            VolleyLog.d(access$000, "Error: " + volleyError.getMessage());
                            TodaysCallActivity.this.setContentView((int) R.layout.chart_error_message);
                        }
                    });
                    TodaysCallActivity.this.getGreekApplication();
                    MyGreeksApplication.getInstance().addToRequestQueue(stringRequest);
                } catch (Exception e2) {
                    String access$0002 = TodaysCallActivity.this.TAG;
                    Log.d(access$0002, "Error | Volley Call | Trade Calls : " + e2.toString());
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError volleyError) {
                String access$000 = TodaysCallActivity.this.TAG;
                VolleyLog.d(access$000, "Error: " + volleyError.getMessage());
                TodaysCallActivity.this.setContentView((int) R.layout.chart_error_message);
            }
        }), "json_obj_req_tc_getcalls");
    }

    public void setStgObjectForSaving(List<TradeCalls> list) {
        StrategyResultsFilter strategyResultsFilter = new StrategyResultsFilter();
        this.stgResult = strategyResultsFilter;
        strategyResultsFilter.setSymbol(Constants.TODAYS_CALL_SYMBOL);
        this.stgResult.setStrategyId(Constants.TODAYS_CALL_STG_ID);
        this.stgResult.setStrategySubKey(0);
        this.stgResult.setRunSeq(0);
        if (list.size() > 0) {
            this.stgResult.setUpdD(list.get(0).getNextTradeDate());
        }
    }

    public void getSummary(List<TradeCalls> list) {
        int i = 0;
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        for (int i5 = 0; i5 < list.size(); i5++) {
            if (list.get(i5).getInstrType().trim().equalsIgnoreCase(Constants.INSTR_CALL) || list.get(i5).getInstrType().trim().equalsIgnoreCase(Constants.INSTR_PUT)) {
                i3++;
            }
            if (list.get(i5).getInstrType().trim().equalsIgnoreCase(Constants.INSTR_STOCK)) {
                i4++;
            }
            if (list.get(i5).getFlag().trim().equalsIgnoreCase(Constants.FLAG_INTRADAY)) {
                i++;
            }
            if (list.get(i5).getFlag().trim().equalsIgnoreCase(Constants.FLAG_POSITIONAL)) {
                i2++;
            }
        }
        this.tvCntIntraday.setText(Integer.toString(i));
        this.tvCntPositional.setText(Integer.toString(i2));
        this.tvCntOption.setText(Integer.toString(i3));
        this.tvCntStock.setText(Integer.toString(i4));
        TextView textView = this.tvPayAmount;
        textView.setText("₹ " + getGreekApplication().round2Decimals(list.get(0).getTodaysPrice()));
        SKU_PREMIUM = list.get(0).getGoogleProduct();
        TextView textView2 = this.tvTotalCalls;
        textView2.setText("Total Calls :" + Integer.toString(list.size()));
        TextView textView3 = this.tvSummary;
        textView3.setText("Trading Calls for: " + getGreekApplication().dateFormatter(list.get(0).getNextTradeDate(), Constants.DT_FMT_dd_MMM_yyyy));
        this.buyEndTime = list.get(0).getSubscribeExipry();
    }

    /* access modifiers changed from: private */
    public boolean isBlisted() {
        String str = getGreekApplication().getBlistFlags()[0];
        return !str.trim().equalsIgnoreCase(getGreekApplication().getJNICallResult_getBList());
    }

    /* access modifiers changed from: private */
    public void webServiceCallIsRewardAvailable() {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("installid", getGreekApplication().getAppInstallId());
            StringRequest stringRequest = new StringRequest(0, String.format(this.webServiceUrl + "%1$s&condition=%2$s", new Object[]{"92", URLEncoder.encode(jSONObject.toString(), "UTF-8")}), new Response.Listener<String>() {
                public void onResponse(String str) {
                    Log.d(TodaysCallActivity.this.TAG, "webServiceCallIsRewardAvailable got tc response");
                    if (str.equals(Constants.REWARD_IS_AVAILABLE)) {
                        TodaysCallActivity.this.getUserYesNoChoice("Congrats! You are eligible for free trading calls.");
                    }
                }
            }, new Response.ErrorListener() {
                public void onErrorResponse(VolleyError volleyError) {
                    String access$000 = TodaysCallActivity.this.TAG;
                    VolleyLog.d(access$000, "Error: " + volleyError.getMessage());
                    TodaysCallActivity.this.setContentView((int) R.layout.chart_error_message);
                }
            });
            getGreekApplication();
            MyGreeksApplication.getInstance().addToRequestQueue(stringRequest);
        } catch (UnsupportedEncodingException | JSONException e) {
            String str = this.TAG;
            Log.d(str, "Error | Volley Call | TradeCalls : " + e.toString());
            setContentView((int) R.layout.chart_error_message);
        }
    }

    public void getUserYesNoChoice(String str) {
        C073411 r0 = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == -2) {
                    Toast.makeText(TodaysCallActivity.this.getApplicationContext(), "You can access free Calls later.", 1).show();
                } else if (i == -1) {
                    JSONObject jSONObject = new JSONObject();
                    try {
                        jSONObject.put("installid", TodaysCallActivity.this.getGreekApplication().getAppInstallId());
                        TodaysCallActivity.this.showTodaysCall();
                        TodaysCallActivity.this.paidSuccess = true;
                        TodaysCallActivity.this.getGreekApplication().insertStgAccess(TodaysCallActivity.this.stgResult);
                        try {
                            StringRequest stringRequest = new StringRequest(0, String.format(TodaysCallActivity.this.webServiceUrl + "%1$s&condition=%2$s", new Object[]{"91", URLEncoder.encode(jSONObject.toString(), "UTF-8")}), new Response.Listener<String>() {
                                public void onResponse(String str) {
                                    Log.d(TodaysCallActivity.this.TAG, "getUserYesNoChoice got response");
                                }
                            }, new Response.ErrorListener() {
                                public void onErrorResponse(VolleyError volleyError) {
                                    String access$000 = TodaysCallActivity.this.TAG;
                                    VolleyLog.d(access$000, "Error: " + volleyError.getMessage());
                                    TodaysCallActivity.this.setContentView((int) R.layout.chart_error_message);
                                }
                            });
                            TodaysCallActivity.this.getGreekApplication();
                            MyGreeksApplication.getInstance().addToRequestQueue(stringRequest);
                        } catch (UnsupportedEncodingException e) {
                            String access$000 = TodaysCallActivity.this.TAG;
                            Log.d(access$000, "Error | Volley Call | TradeCalls : " + e.toString());
                            TodaysCallActivity.this.setContentView((int) R.layout.chart_error_message);
                        }
                    } catch (JSONException e2) {
                        String access$0002 = TodaysCallActivity.this.TAG;
                        Log.d(access$0002, "Error | Volley Call | TradeCalls : " + e2.toString());
                    }
                }
            }
        };
        new AlertDialog.Builder(this.activity).setMessage(str).setPositiveButton("Yes", r0).setNegativeButton("No", r0).show();
    }

    /* access modifiers changed from: protected */
    public MyGreeksApplication getGreekApplication() {
        return (MyGreeksApplication) getApplicationContext();
    }
}
