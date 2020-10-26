package bulltrack.com.optionanalyzer.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.core.internal.view.SupportMenu;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.vectordrawable.graphics.drawable.PathInterpolatorCompat;
import bulltrack.com.optionanalyzer.application.MyGreeksApplication;
import bulltrack.com.optionanalyzer.constants.Constants;
import bulltrack.com.optionanalyzer.dao.LegId;
import bulltrack.com.optionanalyzer.dao.StrategyLegsFilter;
import bulltrack.com.optionanalyzer.dao.StrategyResultsFilter;
import bulltrack.com.optionanalyzer.utility.CustomMarkerView;
import bulltrack.com.optionanalyzer.utility.billing.IabBroadcastReceiver;
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
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.common.ConnectionResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ActivityStrategyLegs extends AppCompatActivity {
    static String ITEM_PRICE = "100";
    static final int RC_REQUEST = 10001;
    static final String SKU_GAS = "gas";
    static String SKU_PREMIUM = "01_strategy_100";
    /* access modifiers changed from: private */
    public String TAG = "ActivityStrategyLegs";
    String actionFrom = null;
    /* access modifiers changed from: private */
    public Activity activity;
    boolean adClosedFlag = false;
    BarChart barChart;
    Button btnBuy;
    ImageView imgLocation;
    ImageView imgPayOff;

    /* renamed from: li */
    List<StrategyLegsFilter> f76li;
    LinearLayout llLeg3;
    LinearLayout llLeg4;
    LinearLayout llPositionLegsFrame;
    int lotSize = 1;
    IabBroadcastReceiver mBroadcastReceiver;
    LineChart mChart;
    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
        public void onConsumeFinished(Purchase purchase, IabResult iabResult) {
            String access$200 = ActivityStrategyLegs.this.TAG;
            Log.d(access$200, "Consumption finished. Purchase: " + purchase + ", result: " + iabResult);
            if (ActivityStrategyLegs.this.mHelper != null) {
                if (iabResult.isSuccess()) {
                    Log.d(ActivityStrategyLegs.this.TAG, "Consumption successful. Provisioning.");
                } else {
                    ActivityStrategyLegs activityStrategyLegs = ActivityStrategyLegs.this;
                    activityStrategyLegs.complain("Error while consuming: " + iabResult);
                }
                Log.d(ActivityStrategyLegs.this.TAG, "End consumption flow.");
            }
        }
    };
    String mFirstChoiceSku = "";
    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult iabResult, Inventory inventory) {
            Log.d(ActivityStrategyLegs.this.TAG, "Query inventory finished.");
            if (iabResult.isFailure()) {
                String access$200 = ActivityStrategyLegs.this.TAG;
                Log.d(access$200, "Failed to query inventory: " + iabResult);
                return;
            }
            Log.d(ActivityStrategyLegs.this.TAG, "Query inventory was successful.");
            try {
                if (inventory.hasPurchase(ActivityStrategyLegs.SKU_PREMIUM)) {
                    String access$2002 = ActivityStrategyLegs.this.TAG;
                    Log.d(access$2002, "already purchased : " + ActivityStrategyLegs.SKU_PREMIUM);
                    ActivityStrategyLegs.this.mHelper.consumeAsync(inventory.getPurchase(ActivityStrategyLegs.SKU_PREMIUM), ActivityStrategyLegs.this.mConsumeFinishedListener);
                }
            } catch (IabHelper.IabAsyncInProgressException unused) {
                ActivityStrategyLegs.this.complain("Error launching purchase flow. Another async operation in progress.");
            }
        }
    };
    IabHelper mHelper;
    InterstitialAd mIAd_StgLeg;
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult iabResult, Purchase purchase) {
            String access$200 = ActivityStrategyLegs.this.TAG;
            Log.d(access$200, "Purchase finished: " + iabResult + ", purchase: " + purchase);
            if (ActivityStrategyLegs.this.mHelper != null) {
                if (iabResult.isFailure()) {
                    ActivityStrategyLegs.this.complain("Payment unsuccessful");
                } else if (!ActivityStrategyLegs.this.verifyDeveloperPayload(purchase)) {
                    ActivityStrategyLegs.this.complain("Error purchasing. Authenticity verification failed.");
                } else {
                    Log.d(ActivityStrategyLegs.this.TAG, "Purchase successful.");
                    if (purchase.getSku().equals(ActivityStrategyLegs.SKU_GAS)) {
                        Log.d(ActivityStrategyLegs.this.TAG, "Purchase is gas. Starting gas consumption.");
                        try {
                            ActivityStrategyLegs.this.mHelper.consumeAsync(purchase, ActivityStrategyLegs.this.mConsumeFinishedListener);
                        } catch (IabHelper.IabAsyncInProgressException unused) {
                            ActivityStrategyLegs.this.complain("Error consuming gas. Another async operation in progress.");
                        }
                    } else if (purchase.getSku().equals(ActivityStrategyLegs.SKU_PREMIUM)) {
                        Log.d(ActivityStrategyLegs.this.TAG, "Purchase is premium upgrade. Congratulating user.");
                        ActivityStrategyLegs.this.alert("Purchase Successful. Thank you!");
                        ActivityStrategyLegs.this.showStrategyLegs();
                        ActivityStrategyLegs.this.paidSuccess = true;
                        ActivityStrategyLegs.this.getGreekApplication().insertStgAccess(ActivityStrategyLegs.this.stgResult);
                        ActivityStrategyLegs.this.webServiceCallPayment(purchase.getOriginalJson());
                    }
                }
            }
        }
    };
    String mSecondChoiceSku = "";
    Map<Long, Double> mapPnl = new HashMap();
    Map<Long, Double> mapSpread;
    boolean paidSuccess = false;
    LineChart payoffChart;
    View payoffSeparator;
    RelativeLayout rlMain;
    RelativeLayout rlPayFrame;
    View separatorAboveL3;
    View separatorAboveL4;
    View separatorAbvPnl;
    View separatorBlwPnl;
    TextView shortDT;
    StrategyResultsFilter stgResult;
    float stockPrice = 0.0f;
    int stockPricePos = 0;
    float stockPricePrev = 0.0f;
    TextView tvBreakDown;
    TextView tvBreakUp;
    TextView tvBuySell1;
    TextView tvBuySell2;
    TextView tvBuySell3;
    TextView tvBuySell4;
    TextView tvInterest;
    TextView tvInvest;
    TextView tvInvestAmtText;
    TextView tvLegsCount;
    TextView tvLikelyGain;
    TextView tvLotSize;
    TextView tvMaxGain;
    TextView tvMaxRisk;
    TextView tvOptionId1;
    TextView tvOptionId2;
    TextView tvOptionId3;
    TextView tvOptionId4;
    TextView tvPayAmount;
    TextView tvPnlText;
    TextView tvPremium1;
    TextView tvPremium2;
    TextView tvPremium3;
    TextView tvPremium4;
    TextView tvSpreadHead;
    TextView tvSpreadVal;
    TextView tvStock;
    TextView tvStrategy;
    String webServiceUrl = Constants.URL_SERVICE;

    private double buycall(float f, float f2, float f3) {
        return (double) (f > f2 ? f - (f2 + f3) : -f3);
    }

    private double buyput(float f, float f2, float f3) {
        return f < f2 ? (double) (f2 - (f + f3)) : (double) (-f3);
    }

    private double sellcall(float f, float f2, float f3) {
        return f <= f2 ? (double) f3 : (double) ((f3 + f2) - f);
    }

    private double sellput(float f, float f2, float f3) {
        return f >= f2 ? (double) f3 : (double) ((f - f2) + f3);
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_legs_progress);
        InterstitialAd interstitialAd = new InterstitialAd(this);
        this.mIAd_StgLeg = interstitialAd;
        interstitialAd.setAdUnitId(getString(R.string.ad_unit_id_strategy_main_interstitial));
        this.mIAd_StgLeg.loadAd(new AdRequest.Builder().build());
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.stgResult = (StrategyResultsFilter) extras.get("result");
            this.actionFrom = (String) extras.get("actionfrom");
            new AsyncListViewLoader().execute(new String[]{"81"});
        }
    }

    public void LoadUI() {
        setContentView((int) R.layout.activity_legs);
        ((AdView) findViewById(R.id.adView_activity_legs_banner)).loadAd(new AdRequest.Builder().setRequestAgent("android_studio:ad_template").build());
        this.activity = this;
        this.tvInvestAmtText = (TextView) findViewById(R.id.tv_activity_legs_totinvest_text);
        this.imgPayOff = (ImageView) findViewById(R.id.img_activity_legs_payoff);
        this.imgLocation = (ImageView) findViewById(R.id.img_activity_leg_stocklocation);
        this.tvStock = (TextView) findViewById(R.id.tv_activity_legs_stock);
        this.tvStrategy = (TextView) findViewById(R.id.tv_activity_legs_strategy);
        this.tvInvest = (TextView) findViewById(R.id.tv_activity_legs_invest_val);
        this.tvInterest = (TextView) findViewById(R.id.tv_activity_legs_interest_val);
        this.tvMaxRisk = (TextView) findViewById(R.id.tv_activity_legs_risk_val);
        this.tvLikelyGain = (TextView) findViewById(R.id.tv_activity_legs_likelygain_val);
        this.tvMaxGain = (TextView) findViewById(R.id.tv_activity_legs_gain_val);
        this.tvBreakUp = (TextView) findViewById(R.id.tv_activity_legs_breakup_val);
        this.tvBreakDown = (TextView) findViewById(R.id.tv_activity_legs_breakdown_val);
        this.tvBuySell1 = (TextView) findViewById(R.id.tv_activity_legs_leg1_buysell);
        this.tvOptionId1 = (TextView) findViewById(R.id.tv_activity_legs_leg1_optionid);
        this.tvPremium1 = (TextView) findViewById(R.id.tv_activity_legs_leg1_premium);
        this.tvBuySell2 = (TextView) findViewById(R.id.tv_activity_legs_leg2_buysell);
        this.tvOptionId2 = (TextView) findViewById(R.id.tv_activity_legs_leg2_optionid);
        this.tvPremium2 = (TextView) findViewById(R.id.tv_activity_legs_leg2_premium);
        this.tvBuySell3 = (TextView) findViewById(R.id.tv_activity_legs_leg3_buysell);
        this.tvOptionId3 = (TextView) findViewById(R.id.tv_activity_legs_leg3_optionid);
        this.tvPremium3 = (TextView) findViewById(R.id.tv_activity_legs_leg3_premium);
        this.tvBuySell4 = (TextView) findViewById(R.id.tv_activity_legs_leg4_buysell);
        this.tvOptionId4 = (TextView) findViewById(R.id.tv_activity_legs_leg4_optionid);
        this.tvPremium4 = (TextView) findViewById(R.id.tv_activity_legs_leg4_premium);
        this.separatorAboveL3 = findViewById(R.id.separator_activity_legs_4);
        this.llLeg3 = (LinearLayout) findViewById(R.id.ll_activity_legs_legs3_val);
        this.separatorAboveL4 = findViewById(R.id.separator_activity_legs_5);
        this.llLeg4 = (LinearLayout) findViewById(R.id.ll_activity_legs_legs4_val);
        this.tvSpreadHead = (TextView) findViewById(R.id.tv_activity_legs_spread_head);
        this.tvSpreadVal = (TextView) findViewById(R.id.tv_activity_legs_spread_val);
        this.barChart = (BarChart) findViewById(R.id.bar_activity_legs_spread);
        this.mChart = (LineChart) findViewById(R.id.linechart_activity_legs_pnl);
        this.payoffSeparator = findViewById(R.id.separator_activity_legs_7);
        this.payoffChart = (LineChart) findViewById(R.id.linechart_activity_legs_payoff);
        this.shortDT = (TextView) findViewById(R.id.tv_activity_legs_short_date_time);
        this.tvLotSize = (TextView) findViewById(R.id.tv_activity_legs_lotsize);
        this.tvLegsCount = (TextView) findViewById(R.id.tv_activity_legs_legcount);
        this.separatorAbvPnl = findViewById(R.id.separator_activity_legs_9);
        this.tvPnlText = (TextView) findViewById(R.id.tv_activity_legs_pnl_head);
        this.separatorBlwPnl = findViewById(R.id.separator_activity_legs_10);
        this.tvPayAmount = (TextView) findViewById(R.id.tv_activity_legs_subs_amt);
        this.llPositionLegsFrame = (LinearLayout) findViewById(R.id.ll_activity_legs_legs_frame);
        this.rlPayFrame = (RelativeLayout) findViewById(R.id.rl_activity_legs_subscription_frame);
        this.rlMain = (RelativeLayout) findViewById(R.id.rl_activity_legs_main);
        this.btnBuy = (Button) findViewById(R.id.btn_activity_leg_buy);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView textView = (TextView) findViewById(R.id.tv_activity_legs_timer);
        this.stgResult.getUpdD().getTime();
        Calendar.getInstance(TimeZone.getTimeZone("Asia/Calcutta")).getTimeInMillis();
        this.imgLocation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                CustomMarkerView customMarkerView = (CustomMarkerView) ActivityStrategyLegs.this.payoffChart.getMarkerView();
                customMarkerView.setFirst();
                ActivityStrategyLegs.this.payoffChart.setMarkerView(customMarkerView);
                ActivityStrategyLegs.this.payoffChart.highlightValue(new Highlight(ActivityStrategyLegs.this.stockPricePos, 0));
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == 16908332) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    if (ActivityStrategyLegs.this.mIAd_StgLeg.isLoaded()) {
                        ActivityStrategyLegs.this.mIAd_StgLeg.show();
                        ActivityStrategyLegs.this.mIAd_StgLeg.setAdListener(new AdListener() {
                            public void onAdClosed() {
                                if (ActivityStrategyLegs.this.getParent() == null) {
                                    ActivityStrategyLegs.this.adClosedFlag = true;
                                    return;
                                }
                                NavUtils.navigateUpFromSameTask(ActivityStrategyLegs.this.activity);
                                ActivityStrategyLegs.this.finish();
                            }
                        });
                    } else if (ActivityStrategyLegs.this.getParent() == null) {
                        ActivityStrategyLegs.this.onBackPressed();
                    } else {
                        NavUtils.navigateUpFromSameTask(ActivityStrategyLegs.this.activity);
                        ActivityStrategyLegs.this.finish();
                    }
                }
            }, 1000);
            return true;
        }
        if (itemId == R.id.action_info) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(getLayoutInflater().inflate(R.layout.strategy_info, (ViewGroup) null));
            builder.setTitle("Info");
            AlertDialog create = builder.create();
            create.show();
            getGreekApplication();
            ((TextView) create.findViewById(R.id.tv_strategy_info_commentary)).setText(MyGreeksApplication.fromHtml(getGreekApplication().getStrategyCommentary(this.stgResult, this.stockPrice)));
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void onBackPressed() {
        Intent intent = new Intent();
        String str = this.actionFrom;
        if (str == null || !str.equalsIgnoreCase(Constants.STRATEGY_LEGS_SHOW_FLAG)) {
            Intent parentActivityIntent = NavUtils.getParentActivityIntent(this);
            if (this.stgResult.getAudience() == 1) {
                parentActivityIntent.putExtra("strategytype", Integer.toString(1));
            }
            NavUtils.navigateUpTo(this, parentActivityIntent);
        } else if (this.paidSuccess) {
            setResult(1020, intent);
        }
        finish();
    }

    public void onResume() {
        super.onResume();
        if (this.adClosedFlag) {
            onBackPressed();
            this.adClosedFlag = false;
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.info, menu);
        return true;
    }

    /* access modifiers changed from: private */
    public void googleBilling() {
        IabHelper iabHelper = new IabHelper(this, Constants.GOOGLE_BASE64_ENCODING_KEY);
        this.mHelper = iabHelper;
        iabHelper.enableDebugLogging(true);
        this.mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult iabResult) {
                Log.d(ActivityStrategyLegs.this.TAG, "Setup finished.");
                if (!iabResult.isSuccess()) {
                    ActivityStrategyLegs activityStrategyLegs = ActivityStrategyLegs.this;
                    activityStrategyLegs.complain("Problem setting up in-app billing: " + iabResult);
                } else if (ActivityStrategyLegs.this.mHelper != null) {
                    try {
                        ActivityStrategyLegs.this.mHelper.queryInventoryAsync(ActivityStrategyLegs.this.mGotInventoryListener);
                    } catch (IabHelper.IabAsyncInProgressException unused) {
                        ActivityStrategyLegs.this.complain("Error launching purchase flow. Another async operation in progress.");
                    }
                    Log.d(ActivityStrategyLegs.this.TAG, "Setup successful. Querying inventory.");
                }
            }
        });
    }

    public void onBuyButtonClicked(View view) {
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
    public boolean verifyDeveloperPayload(Purchase purchase) {
        purchase.getDeveloperPayload();
        return true;
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

    /* access modifiers changed from: private */
    public boolean isBlisted() {
        String str = getGreekApplication().getBlistFlags()[0];
        return !str.trim().equalsIgnoreCase(getGreekApplication().getJNICallResult_getBList());
    }

    /* access modifiers changed from: protected */
    public MyGreeksApplication getGreekApplication() {
        return (MyGreeksApplication) getApplicationContext();
    }

    private class AsyncListViewLoader extends AsyncTask<String, Void, List<StrategyLegsFilter>> {
        private AsyncListViewLoader() {
        }

        /* access modifiers changed from: protected */
        public List<StrategyLegsFilter> doInBackground(String... strArr) {
            ActivityStrategyLegs.this.f76li = new ArrayList();
            try {
                StringBuilder sb = new StringBuilder();
                String str = "strike";
                sb.append(ActivityStrategyLegs.this.webServiceUrl);
                String str2 = "expiry";
                sb.append(strArr[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(sb.toString()).openConnection();
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("Accept-Charset", "UTF-8");
                httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                httpURLConnection.setRequestProperty("Accept", "application/json");
                DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
                String symbol = ActivityStrategyLegs.this.stgResult.getSymbol();
                int strategyId = ActivityStrategyLegs.this.stgResult.getStrategyId();
                String str3 = "stock";
                int strategySubKey = ActivityStrategyLegs.this.stgResult.getStrategySubKey();
                String str4 = "application/json";
                int runSeq = ActivityStrategyLegs.this.stgResult.getRunSeq();
                String str5 = "Accept";
                String str6 = "application/x-www-form-urlencoded";
                ActivityStrategyLegs.this.lotSize = ActivityStrategyLegs.this.stgResult.getLotSize();
                Date updD = ActivityStrategyLegs.this.stgResult.getUpdD();
                LegId legId = new LegId();
                legId.setSymbol(symbol);
                legId.setStrategyId(strategyId);
                legId.setStrategySubKey(strategySubKey);
                legId.setRunSeq(runSeq);
                legId.setUpdD(updD);
                String json = new GsonBuilder().setDateFormat(Constants.DT_FMT_yyyy_MM_dd_HH_m_ss).create().toJson((Object) legId);
                dataOutputStream.writeBytes("&condition=");
                dataOutputStream.writeBytes(URLEncoder.encode(json, "UTF-8"));
                dataOutputStream.flush();
                dataOutputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                String readLine = bufferedReader.readLine();
                Type type = new TypeToken<List<StrategyLegsFilter>>() {
                }.getType();
                Gson create = new GsonBuilder().setDateFormat(Constants.DT_FMT_yyyy_MM_dd_HH_m_ss).create();
                String str7 = Constants.DT_FMT_yyyy_MM_dd_HH_m_ss;
                ActivityStrategyLegs.this.f76li = (List) create.fromJson(readLine, type);
                if (inputStream != null) {
                    inputStream.close();
                }
                bufferedReader.close();
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
                HttpURLConnection httpURLConnection2 = (HttpURLConnection) new URL(ActivityStrategyLegs.this.webServiceUrl + 88).openConnection();
                httpURLConnection2.setConnectTimeout(5000);
                httpURLConnection2.setDoInput(true);
                httpURLConnection2.setDoOutput(true);
                httpURLConnection2.setRequestMethod("POST");
                httpURLConnection2.setRequestProperty("Accept-Charset", "UTF-8");
                String str8 = str6;
                httpURLConnection2.setRequestProperty("Content-Type", str8);
                String str9 = str4;
                String str10 = str5;
                httpURLConnection2.setRequestProperty(str10, str9);
                DataOutputStream dataOutputStream2 = new DataOutputStream(httpURLConnection2.getOutputStream());
                dataOutputStream2.writeBytes("&condition=");
                dataOutputStream2.writeBytes(URLEncoder.encode(symbol, "UTF-8"));
                dataOutputStream2.flush();
                dataOutputStream2.close();
                JSONObject jSONObject = new JSONObject(new BufferedReader(new InputStreamReader(httpURLConnection2.getInputStream(), "UTF-8")).readLine());
                ActivityStrategyLegs.this.stockPrice = (float) jSONObject.getDouble("ltp");
                ActivityStrategyLegs.this.stockPricePrev = (float) jSONObject.getDouble("prevClose");
                ActivityStrategyLegs.SKU_PREMIUM = jSONObject.getString("gPid");
                ActivityStrategyLegs.ITEM_PRICE = jSONObject.getString("gAmt");
                HttpURLConnection httpURLConnection3 = (HttpURLConnection) new URL(ActivityStrategyLegs.this.webServiceUrl + 82).openConnection();
                httpURLConnection3.setConnectTimeout(5000);
                httpURLConnection3.setDoInput(true);
                httpURLConnection3.setDoOutput(true);
                httpURLConnection3.setRequestMethod("POST");
                httpURLConnection3.setRequestProperty("Accept-Charset", "UTF-8");
                httpURLConnection3.setRequestProperty("Content-Type", str8);
                httpURLConnection3.setRequestProperty(str10, str9);
                DataOutputStream dataOutputStream3 = new DataOutputStream(httpURLConnection3.getOutputStream());
                JSONObject jSONObject2 = new JSONObject();
                jSONObject2.put("strategy", strategyId);
                String str11 = str3;
                jSONObject2.put(str11, URLEncoder.encode(symbol, "UTF-8"));
                String str12 = str11;
                String str13 = str7;
                String str14 = str2;
                jSONObject2.put(str14, ActivityStrategyLegs.this.getGreekApplication().dateFormatter(ActivityStrategyLegs.this.f76li.get(0).getExpiry(), str13));
                JSONArray jSONArray = new JSONArray();
                String str15 = str10;
                String str16 = str14;
                int i = 0;
                while (i < ActivityStrategyLegs.this.f76li.size()) {
                    jSONArray.put((double) ActivityStrategyLegs.this.f76li.get(i).getStrike());
                    i++;
                    str8 = str8;
                }
                String str17 = str8;
                String str18 = str;
                jSONObject2.put(str18, jSONArray);
                dataOutputStream3.writeBytes("&condition=");
                dataOutputStream3.writeBytes(jSONObject2.toString());
                dataOutputStream3.flush();
                dataOutputStream3.close();
                InputStream inputStream2 = httpURLConnection3.getInputStream();
                BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(inputStream2, "UTF-8"));
                String readLine2 = bufferedReader2.readLine();
                Type type2 = new TypeToken<Map<Long, Double>>() {
                }.getType();
                Gson create2 = new GsonBuilder().setDateFormat(str13).create();
                String str19 = "&condition=";
                ActivityStrategyLegs.this.mapSpread = (Map) create2.fromJson(readLine2, type2);
                if (inputStream2 != null) {
                    inputStream2.close();
                }
                bufferedReader2.close();
                if (httpURLConnection3 != null) {
                    httpURLConnection3.disconnect();
                }
                HttpURLConnection httpURLConnection4 = (HttpURLConnection) new URL(ActivityStrategyLegs.this.webServiceUrl + 83).openConnection();
                httpURLConnection4.setConnectTimeout(5000);
                httpURLConnection4.setDoInput(true);
                httpURLConnection4.setDoOutput(true);
                httpURLConnection4.setRequestMethod("POST");
                httpURLConnection4.setRequestProperty("Accept-Charset", "UTF-8");
                httpURLConnection4.setRequestProperty("Content-Type", str17);
                httpURLConnection4.setRequestProperty(str15, str9);
                DataOutputStream dataOutputStream4 = new DataOutputStream(httpURLConnection4.getOutputStream());
                JSONArray jSONArray2 = new JSONArray();
                for (int i2 = 0; i2 < ActivityStrategyLegs.this.f76li.size(); i2++) {
                    JSONObject jSONObject3 = new JSONObject();
                    String str20 = str12;
                    jSONObject3.put(str20, ActivityStrategyLegs.this.f76li.get(i2).getSymbol());
                    jSONObject3.put("action", ActivityStrategyLegs.this.f76li.get(i2).getAction());
                    jSONObject3.put(str18, (double) ActivityStrategyLegs.this.f76li.get(i2).getStrike());
                    jSONObject3.put("callput", ActivityStrategyLegs.this.f76li.get(i2).getCallPut());
                    String str21 = str16;
                    jSONObject3.put(str21, ActivityStrategyLegs.this.getGreekApplication().dateFormatter(ActivityStrategyLegs.this.f76li.get(i2).getExpiry(), str13));
                    str12 = str20;
                    str16 = str21;
                    jSONObject3.put("premium", (double) ActivityStrategyLegs.this.f76li.get(i2).getPremium());
                    jSONObject3.put("upd", ActivityStrategyLegs.this.getGreekApplication().dateFormatter(ActivityStrategyLegs.this.f76li.get(i2).getUpdD(), str13));
                    jSONArray2.put(jSONObject3);
                }
                dataOutputStream4.writeBytes(str19);
                dataOutputStream4.writeBytes(jSONArray2.toString());
                dataOutputStream4.flush();
                dataOutputStream4.close();
                InputStream inputStream3 = httpURLConnection4.getInputStream();
                BufferedReader bufferedReader3 = new BufferedReader(new InputStreamReader(inputStream3, "UTF-8"));
                String readLine3 = bufferedReader3.readLine();
                Type type3 = new TypeToken<Map<Long, Double>>() {
                }.getType();
                Gson create3 = new GsonBuilder().setDateFormat(str13).create();
                ActivityStrategyLegs.this.mapPnl = (Map) create3.fromJson(readLine3, type3);
                if (inputStream3 != null) {
                    inputStream3.close();
                }
                bufferedReader3.close();
                if (httpURLConnection4 != null) {
                    httpURLConnection4.disconnect();
                }
            } catch (IOException e) {
                Log.i(ActivityStrategyLegs.this.TAG, e.toString());
            } catch (Exception e2) {
                Log.i(ActivityStrategyLegs.this.TAG, e2.toString());
            }
            return ActivityStrategyLegs.this.f76li;
        }

        /* access modifiers changed from: protected */
        public void onPreExecute() {
            super.onPreExecute();
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(List<StrategyLegsFilter> list) {
            String str;
            String str2;
            super.onPostExecute(list);
            if (list.size() == 0) {
                ActivityStrategyLegs.this.setContentView((int) R.layout.chart_error_message);
                return;
            }
            ActivityStrategyLegs.this.LoadUI();
            TextView textView = ActivityStrategyLegs.this.tvPayAmount;
            textView.setText("₹ " + ActivityStrategyLegs.this.getGreekApplication().round2Decimals((double) Float.parseFloat(ActivityStrategyLegs.ITEM_PRICE)) + " only");
            ActivityStrategyLegs.this.imgPayOff.setImageResource(ActivityStrategyLegs.this.getGreekApplication().getPayoffDiagram(ActivityStrategyLegs.this.stgResult.getStrategyId()));
            ActivityStrategyLegs.this.tvStock.setText(ActivityStrategyLegs.this.stgResult.getSymbol());
            ActivityStrategyLegs.this.tvStrategy.setText(ActivityStrategyLegs.this.stgResult.getStrategyName());
            TextView textView2 = ActivityStrategyLegs.this.tvInvestAmtText;
            ActivityStrategyLegs.this.getGreekApplication();
            textView2.setText(MyGreeksApplication.fromHtml("Total Investment<sup>*</sup>"));
            TextView textView3 = ActivityStrategyLegs.this.tvInvest;
            textView3.setText("₹ " + ActivityStrategyLegs.this.getGreekApplication().round2Decimals1000((double) ActivityStrategyLegs.this.stgResult.getInvestment()));
            TextView textView4 = ActivityStrategyLegs.this.tvInterest;
            textView4.setText(ActivityStrategyLegs.this.stgResult.getInterestCost() + "");
            TextView textView5 = ActivityStrategyLegs.this.tvMaxRisk;
            textView5.setText("₹ " + ActivityStrategyLegs.this.getGreekApplication().round2Decimals1000((double) ActivityStrategyLegs.this.stgResult.getMaxRisk()));
            TextView textView6 = ActivityStrategyLegs.this.tvLikelyGain;
            textView6.setText(ActivityStrategyLegs.this.stgResult.getExpectedGain() + "");
            TextView textView7 = ActivityStrategyLegs.this.tvMaxGain;
            textView7.setText("₹ " + ActivityStrategyLegs.this.getGreekApplication().round2Decimals1000((double) ActivityStrategyLegs.this.stgResult.getMaxGain()));
            if (((double) ActivityStrategyLegs.this.stgResult.getBreakevenUp()) == 0.0d) {
                ActivityStrategyLegs.this.tvBreakUp.setText("-");
            } else {
                ActivityStrategyLegs.this.tvBreakUp.setText(ActivityStrategyLegs.this.getGreekApplication().round2Decimals1000((double) ActivityStrategyLegs.this.stgResult.getBreakevenUp()));
            }
            if (((double) ActivityStrategyLegs.this.stgResult.getBreakevenDown()) == 0.0d) {
                ActivityStrategyLegs.this.tvBreakDown.setText("-");
            } else {
                ActivityStrategyLegs.this.tvBreakDown.setText(ActivityStrategyLegs.this.getGreekApplication().round2Decimals1000((double) ActivityStrategyLegs.this.stgResult.getBreakevenDown()));
            }
            TextView textView8 = ActivityStrategyLegs.this.tvLegsCount;
            textView8.setText(list.size() + " Legs");
            String str3 = "Buy";
            ActivityStrategyLegs.this.tvBuySell1.setText(list.get(0).getAction().equals(Constants.ACTION_BUY) ? str3 : "Sell");
            TextView textView9 = ActivityStrategyLegs.this.tvOptionId1;
            textView9.setText(list.get(0).getStrike() + "-" + list.get(0).getCallPut() + "-" + ActivityStrategyLegs.this.getGreekApplication().dateFormatter(list.get(0).getExpiry(), Constants.DT_FMT_dd_MMM_yyyy));
            ActivityStrategyLegs.this.tvPremium1.setText(ActivityStrategyLegs.this.getGreekApplication().round2Decimals((double) list.get(0).getPremium()));
            TextView textView10 = ActivityStrategyLegs.this.tvBuySell2;
            if (list.get(1).getAction().equals(Constants.ACTION_BUY)) {
                str = str3;
            } else {
                str = "Sell";
            }
            textView10.setText(str);
            TextView textView11 = ActivityStrategyLegs.this.tvOptionId2;
            textView11.setText(list.get(1).getStrike() + "-" + list.get(1).getCallPut() + "-" + ActivityStrategyLegs.this.getGreekApplication().dateFormatter(list.get(1).getExpiry(), Constants.DT_FMT_dd_MMM_yyyy));
            ActivityStrategyLegs.this.tvPremium2.setText(ActivityStrategyLegs.this.getGreekApplication().round2Decimals((double) list.get(1).getPremium()));
            ActivityStrategyLegs.this.llLeg3.setVisibility(8);
            ActivityStrategyLegs.this.separatorAboveL3.setVisibility(8);
            if (list.size() > 2) {
                ActivityStrategyLegs.this.llLeg3.setVisibility(0);
                TextView textView12 = ActivityStrategyLegs.this.tvBuySell3;
                if (list.get(2).getAction().equals(Constants.ACTION_BUY)) {
                    str2 = str3;
                } else {
                    str2 = "Sell";
                }
                textView12.setText(str2);
                TextView textView13 = ActivityStrategyLegs.this.tvOptionId3;
                textView13.setText(list.get(2).getStrike() + "-" + list.get(2).getCallPut() + "-" + ActivityStrategyLegs.this.getGreekApplication().dateFormatter(list.get(2).getExpiry(), Constants.DT_FMT_dd_MMM_yyyy));
                ActivityStrategyLegs.this.tvPremium3.setText(ActivityStrategyLegs.this.getGreekApplication().round2Decimals((double) list.get(2).getPremium()));
                ActivityStrategyLegs.this.separatorAboveL3.setVisibility(0);
            }
            ActivityStrategyLegs.this.llLeg4.setVisibility(8);
            ActivityStrategyLegs.this.separatorAboveL4.setVisibility(8);
            if (list.size() > 3) {
                ActivityStrategyLegs.this.llLeg4.setVisibility(0);
                TextView textView14 = ActivityStrategyLegs.this.tvBuySell4;
                if (!list.get(3).getAction().equals(Constants.ACTION_BUY)) {
                    str3 = "Sell";
                }
                textView14.setText(str3);
                TextView textView15 = ActivityStrategyLegs.this.tvOptionId4;
                textView15.setText(list.get(3).getStrike() + "-" + list.get(3).getCallPut() + "-" + ActivityStrategyLegs.this.getGreekApplication().dateFormatter(list.get(3).getExpiry(), Constants.DT_FMT_dd_MMM_yyyy));
                ActivityStrategyLegs.this.tvPremium4.setText(ActivityStrategyLegs.this.getGreekApplication().round2Decimals((double) list.get(3).getPremium()));
                ActivityStrategyLegs.this.separatorAboveL4.setVisibility(0);
            }
            float f = 0.0f;
            float f2 = 0.0f;
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getAction().trim().equalsIgnoreCase(Constants.ACTION_BUY)) {
                    f += list.get(i).getPremium();
                } else {
                    f2 += list.get(i).getPremium();
                }
            }
            TextView textView16 = ActivityStrategyLegs.this.tvSpreadVal;
            textView16.setText(Math.abs(f - f2) + "");
            ActivityStrategyLegs.this.shortDT.setText(ActivityStrategyLegs.this.getGreekApplication().shortDateOrTime(ActivityStrategyLegs.this.stgResult.getUpdD()));
            ActivityStrategyLegs.this.tvLotSize.setText(Integer.toString(ActivityStrategyLegs.this.stgResult.getLotSize()));
            ActivityStrategyLegs.this.LoadBarSpreadChart();
            if (ActivityStrategyLegs.this.mapPnl.size() > 0) {
                ActivityStrategyLegs.this.mChart.setVisibility(0);
                ActivityStrategyLegs.this.LoadLinePnLChart();
                ActivityStrategyLegs.this.separatorAbvPnl.setVisibility(0);
                ActivityStrategyLegs.this.tvPnlText.setVisibility(0);
                ActivityStrategyLegs.this.separatorBlwPnl.setVisibility(0);
            } else {
                ActivityStrategyLegs.this.mChart.setVisibility(8);
                ActivityStrategyLegs.this.separatorAbvPnl.setVisibility(8);
                ActivityStrategyLegs.this.tvPnlText.setVisibility(8);
                ActivityStrategyLegs.this.separatorBlwPnl.setVisibility(8);
            }
            try {
                ActivityStrategyLegs.this.LoadPayoffChart();
            } catch (Exception unused) {
                Log.e(ActivityStrategyLegs.this.TAG, "Error in load payoff chart");
            }
            if (ActivityStrategyLegs.this.stgResult.getAudience() != 1) {
                ActivityStrategyLegs.this.showStrategyLegs();
            } else if (ActivityStrategyLegs.this.isBlisted()) {
                ActivityStrategyLegs.this.complain("Possible app integrity violated! Please contact app support");
            } else if (ActivityStrategyLegs.this.actionFrom == null || !ActivityStrategyLegs.this.actionFrom.equalsIgnoreCase(Constants.STRATEGY_LEGS_SHOW_FLAG)) {
                if (ActivityStrategyLegs.this.getGreekApplication().isStgAccessToUser(ActivityStrategyLegs.this.stgResult)) {
                    ActivityStrategyLegs.this.showStrategyLegs();
                } else if (ActivityStrategyLegs.this.getGreekApplication().getRewardFlag().equals(Constants.REWARD_IS_AVAILABLE)) {
                    ActivityStrategyLegs.this.webServiceCallIsRewardAvailable();
                    ActivityStrategyLegs.this.googleBilling();
                } else {
                    ActivityStrategyLegs.this.googleBilling();
                }
            } else if (ActivityStrategyLegs.this.getGreekApplication().isStgAccessToUser(ActivityStrategyLegs.this.stgResult)) {
                ActivityStrategyLegs.this.showStrategyLegs();
            } else if (ActivityStrategyLegs.this.getGreekApplication().getRewardFlag().equals(Constants.REWARD_IS_AVAILABLE)) {
                ActivityStrategyLegs.this.webServiceCallIsRewardAvailable();
                ActivityStrategyLegs.this.googleBilling();
            } else {
                ActivityStrategyLegs.this.googleBilling();
            }
        }
    }

    public void LoadBarSpreadChart() {
        if (this.mapSpread != null) {
            ArrayList arrayList = new ArrayList();
            ArrayList arrayList2 = new ArrayList();
            int i = 0;
            for (Map.Entry next : this.mapSpread.entrySet()) {
                arrayList.add(new BarEntry((float) ((Double) next.getValue()).doubleValue(), i));
                arrayList2.add(getGreekApplication().dateFormatter(new Date(((Long) next.getKey()).longValue()), Constants.DT_FMT_dd_MMM));
                i++;
            }
            this.barChart.setData(new BarData((List<String>) arrayList2, new BarDataSet(arrayList, "Projects")));
            this.barChart.animateY(PathInterpolatorCompat.MAX_NUM_POINTS);
        }
    }

    public void LoadLinePnLChart() {
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        Map<Long, Double> map = this.mapPnl;
        if (map != null) {
            int i = 0;
            for (Map.Entry next : map.entrySet()) {
                arrayList2.add(new Entry(((float) ((Double) next.getValue()).doubleValue()) * ((float) this.lotSize), i));
                arrayList.add(getGreekApplication().dateFormatter(new Date(((Long) next.getKey()).longValue()), Constants.DT_FMT_dd_MMM));
                i++;
            }
            LineDataSet lineDataSet = new LineDataSet(arrayList2, "Profit/Loss Per Lot(₹) Day Wise");
            lineDataSet.setFillAlpha(110);
            lineDataSet.setColor(ViewCompat.MEASURED_STATE_MASK);
            lineDataSet.setCircleColor(ViewCompat.MEASURED_STATE_MASK);
            lineDataSet.setFillColor(Color.parseColor("#00DB6E"));
            lineDataSet.setLineWidth(1.0f);
            lineDataSet.setCircleSize(3.0f);
            lineDataSet.setDrawCircleHole(false);
            lineDataSet.setValueTextSize(9.0f);
            lineDataSet.setDrawFilled(true);
            ArrayList arrayList3 = new ArrayList();
            arrayList3.add(lineDataSet);
            this.mChart.setData(new LineData((List<String>) arrayList, (List<LineDataSet>) arrayList3));
            this.mChart.getLegend().setForm(Legend.LegendForm.LINE);
            this.mChart.setDescriptionTextSize(18.0f);
            this.mChart.setDescription("");
            this.mChart.setNoDataTextDescription("Waiting for data");
            YAxis axisLeft = this.mChart.getAxisLeft();
            axisLeft.removeAllLimitLines();
            axisLeft.enableGridDashedLine(10.0f, 10.0f, 0.0f);
            this.mChart.getAxisRight().setEnabled(false);
            this.mChart.invalidate();
        }
    }

    /* access modifiers changed from: private */
    public void LoadPayoffChart() {
        LinkedHashMap generate = generate(new Float(0.0f), new Float(0.0f));
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        float[] minMaxRange = getMinMaxRange();
        float f = minMaxRange[0];
        float f2 = minMaxRange[1];
        float f3 = minMaxRange[4];
        float floatValue = ((Float) generate.get(Float.toString(f))).floatValue();
        ((Float) generate.get(Float.toString(f2))).floatValue();
        this.stockPricePos = 0;
        int i = 0;
        while (f <= f2) {
            float f4 = this.stockPrice;
            if (f >= f4 && f - f3 < f4) {
                this.stockPricePos = i;
            }
            if (generate.containsKey(Float.toString(f))) {
                floatValue = ((Float) generate.get(Float.toString(f))).floatValue();
                arrayList2.add(new Entry(floatValue, i));
                arrayList.add(Float.toString(f));
            } else {
                arrayList2.add(new Entry(floatValue, i));
                arrayList.add("");
            }
            i++;
            f += f3;
        }
        LineDataSet lineDataSet = new LineDataSet(arrayList2, "Profit/Loss Per Lot(₹) at various expiry points");
        lineDataSet.setColor(ViewCompat.MEASURED_STATE_MASK);
        lineDataSet.setFillColor(Color.parseColor("#00DB6E"));
        lineDataSet.setLineWidth(1.0f);
        lineDataSet.setDrawValues(false);
        lineDataSet.setDrawFilled(true);
        lineDataSet.setDrawCircles(false);
        ArrayList arrayList3 = new ArrayList();
        arrayList3.add(lineDataSet);
        LineData lineData = new LineData((List<String>) arrayList, (List<LineDataSet>) arrayList3);
        lineData.setHighlightEnabled(true);
        this.payoffChart.setData(lineData);
        this.payoffChart.getLegend().setForm(Legend.LegendForm.LINE);
        this.payoffChart.setDescriptionTextSize(14.0f);
        float f5 = this.stockPrice;
        if (f5 != 0.0f) {
            float f6 = this.stockPricePrev;
            if (((double) f6) != 0.0d) {
                double d = (double) (((f5 - f6) / f6) * 100.0f);
                if (d >= 0.0d) {
                    this.payoffChart.setDescriptionColor(Color.parseColor("#2E8B57"));
                    getGreekApplication().round2Decimals(d);
                } else {
                    this.payoffChart.setDescriptionColor(SupportMenu.CATEGORY_MASK);
                    getGreekApplication().round2Decimals(d);
                }
            }
        }
        this.payoffChart.setDescription("");
        this.payoffChart.setDescriptionTextSize(14.0f);
        this.payoffChart.setDescriptionTypeface(Typeface.create(Typeface.SANS_SERIF, 1));
        ViewPortHandler viewPortHandler = this.payoffChart.getViewPortHandler();
        this.payoffChart.getValuesByTouchPoint(viewPortHandler.contentLeft(), viewPortHandler.contentTop(), YAxis.AxisDependency.LEFT);
        this.payoffChart.getValuesByTouchPoint(viewPortHandler.contentRight(), viewPortHandler.contentBottom(), YAxis.AxisDependency.LEFT);
        Paint paint = new Paint(65);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(14.0f);
        this.payoffChart.setNoDataTextDescription("Waiting for data");
        this.payoffChart.getAxisLeft().enableGridDashedLine(10.0f, 10.0f, 0.0f);
        this.payoffChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        this.payoffChart.getAxisRight().setEnabled(false);
        this.payoffChart.setDrawMarkerViews(true);
        this.payoffChart.setMarkerView(new CustomMarkerView(this.activity, R.layout.custom_marker, arrayList, this.stockPrice, this.stockPricePrev));
        this.payoffChart.highlightValue(new Highlight(this.stockPricePos, 0));
        this.payoffChart.invalidate();
    }

    private float[] getMinMaxRange() {
        int i;
        int ceil = (int) Math.ceil((double) this.stockPrice);
        int i2 = (this.stockPrice > 15.0f ? 1 : (this.stockPrice == 15.0f ? 0 : -1));
        float f = this.stockPrice;
        int i3 = 100;
        if (f <= 15.0f || f > 25.0f) {
            float f2 = this.stockPrice;
            if (f2 <= 25.0f || f2 > 45.0f) {
                float f3 = this.stockPrice;
                if (f3 <= 45.0f || f3 > 100.0f) {
                    float f4 = this.stockPrice;
                    if (f4 <= 100.0f || f4 > 200.0f) {
                        float f5 = this.stockPrice;
                        if (f5 <= 200.0f || f5 > 500.0f) {
                            float f6 = this.stockPrice;
                            if (f6 <= 500.0f || f6 > 750.0f) {
                                float f7 = this.stockPrice;
                                if (f7 <= 750.0f || f7 > 1000.0f) {
                                    float f8 = this.stockPrice;
                                    if (f8 <= 1000.0f || f8 > 2000.0f) {
                                        float f9 = this.stockPrice;
                                        if (f9 <= 2000.0f || f9 > 3500.0f) {
                                            float f10 = this.stockPrice;
                                            if (f10 <= 3500.0f || f10 > 5000.0f) {
                                                float f11 = this.stockPrice;
                                                if (f11 > 5000.0f && f11 <= 15000.0f) {
                                                    i3 = Constants.TODAYS_CALL_STG_ID;
                                                    i = 50;
                                                } else if (this.stockPrice > 15000.0f) {
                                                    i = 100;
                                                    i3 = ConnectionResult.DRIVE_EXTERNAL_STORAGE_REQUIRED;
                                                } else {
                                                    i = 1;
                                                    i3 = 1;
                                                }
                                            } else {
                                                i3 = 300;
                                                i = 30;
                                            }
                                        } else {
                                            i3 = ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION;
                                            i = 20;
                                        }
                                    } else {
                                        i = 10;
                                    }
                                } else {
                                    i3 = 75;
                                }
                            } else {
                                i = 10;
                                i3 = 50;
                            }
                        } else {
                            i3 = 25;
                        }
                        i = 5;
                    } else {
                        i = 1;
                        i3 = 10;
                    }
                } else {
                    i = 1;
                    i3 = 5;
                }
            } else {
                i = 1;
                i3 = 3;
            }
        } else {
            i = 1;
            i3 = 2;
        }
        if (((double) this.stockPrice) > 45.0d) {
            while (ceil % i3 != 0) {
                ceil++;
            }
        }
        return new float[]{(float) ((i3 * -3) + ceil), (float) ((i3 * 3) + ceil), (float) ceil, (float) i3, (float) i};
    }

    public LinkedHashMap generate(Float f, Float f2) {
        float[] minMaxRange = getMinMaxRange();
        float f3 = minMaxRange[1];
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        new HashMap();
        float f4 = 0.0f;
        Float valueOf = Float.valueOf(0.0f);
        float f5 = 0.0f;
        for (float f6 = minMaxRange[0]; f6 <= f3; f6 += 1.0f) {
            if (f6 == f3 - 1.0f) {
                System.out.println("stop");
            }
            float calcPayoffProfit = calcPayoffProfit(f6);
            if (calcPayoffProfit > f4) {
                f4 = calcPayoffProfit;
            }
            if (calcPayoffProfit < f5) {
                f5 = calcPayoffProfit;
            }
            linkedHashMap.put(Float.toString(f6), Float.valueOf(calcPayoffProfit));
        }
        if (((double) this.stgResult.getBreakevenDown()) > 0.0d) {
            linkedHashMap.put(Float.toString(this.stgResult.getBreakevenDown()), valueOf);
        }
        if (((double) this.stgResult.getBreakevenUp()) > 0.0d) {
            linkedHashMap.put(Float.toString(this.stgResult.getBreakevenUp()), valueOf);
        }
        linkedHashMap.put(Float.toString(this.stockPrice), Float.valueOf(calcPayoffProfit(this.stockPrice)));
        return linkedHashMap;
    }

    /* access modifiers changed from: package-private */
    public float calcPayoffProfit(float f) {
        double sellput;
        double d = 0.0d;
        for (int i = 0; i < this.f76li.size(); i++) {
            String callPut = this.f76li.get(i).getCallPut();
            String action = this.f76li.get(i).getAction();
            float strike = this.f76li.get(i).getStrike();
            float premium = this.f76li.get(i).getPremium();
            if (callPut == null || !callPut.equalsIgnoreCase("C")) {
                if (callPut != null && callPut.equalsIgnoreCase(Constants.FLAG_POSITIONAL)) {
                    if (action == null || !action.equalsIgnoreCase(Constants.ACTION_BUY)) {
                        sellput = sellput(f, strike, premium);
                    } else {
                        sellput = buyput(f, strike, premium);
                    }
                }
            } else if (action == null || !action.equalsIgnoreCase(Constants.ACTION_BUY)) {
                sellput = sellcall(f, strike, premium);
            } else {
                sellput = buycall(f, strike, premium);
            }
            d += sellput;
        }
        DecimalFormat decimalFormat = new DecimalFormat("#########0.00");
        double d2 = (double) this.lotSize;
        Double.isNaN(d2);
        return new Float(decimalFormat.format(d * d2)).floatValue();
    }

    /* access modifiers changed from: private */
    public void showStrategyLegs() {
        if (this.rlPayFrame.getVisibility() != 8) {
            this.rlPayFrame.setVisibility(8);
            this.llPositionLegsFrame.setVisibility(0);
            ((RelativeLayout.LayoutParams) this.payoffSeparator.getLayoutParams()).addRule(3, this.llPositionLegsFrame.getId());
        }
    }

    public void getUserYesNoChoice(String str) {
        C05327 r0 = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == -2) {
                    Toast.makeText(ActivityStrategyLegs.this.getApplicationContext(), "You can access free strategy later.", 1).show();
                } else if (i == -1) {
                    JSONObject jSONObject = new JSONObject();
                    try {
                        jSONObject.put("installid", ActivityStrategyLegs.this.getGreekApplication().getAppInstallId());
                        ActivityStrategyLegs.this.showStrategyLegs();
                        ActivityStrategyLegs.this.paidSuccess = true;
                        ActivityStrategyLegs.this.getGreekApplication().insertStgAccess(ActivityStrategyLegs.this.stgResult);
                        try {
                            StringRequest stringRequest = new StringRequest(0, String.format(ActivityStrategyLegs.this.webServiceUrl + "%1$s&condition=%2$s", new Object[]{"86", URLEncoder.encode(jSONObject.toString(), "UTF-8")}), new Response.Listener<String>() {
                                public void onResponse(String str) {
                                    Log.d(ActivityStrategyLegs.this.TAG, "getUserYesNoChoice Got response");
                                }
                            }, new Response.ErrorListener() {
                                public void onErrorResponse(VolleyError volleyError) {
                                    String access$200 = ActivityStrategyLegs.this.TAG;
                                    VolleyLog.d(access$200, "Error: " + volleyError.getMessage());
                                }
                            });
                            ActivityStrategyLegs.this.getGreekApplication();
                            MyGreeksApplication.getInstance().addToRequestQueue(stringRequest);
                        } catch (UnsupportedEncodingException e) {
                            String access$200 = ActivityStrategyLegs.this.TAG;
                            Log.d(access$200, "Error | Volley Call | StrategyLegs : " + e.toString());
                        }
                    } catch (JSONException e2) {
                        String access$2002 = ActivityStrategyLegs.this.TAG;
                        Log.d(access$2002, "Error | Volley Call | StrategyLegs : " + e2.toString());
                    }
                }
            }
        };
        new AlertDialog.Builder(this.activity).setMessage(str).setPositiveButton("Yes", r0).setNegativeButton("No", r0).show();
    }

    /* access modifiers changed from: private */
    public void webServiceCallIsRewardAvailable() {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("installid", getGreekApplication().getAppInstallId());
            StringRequest stringRequest = new StringRequest(0, String.format(this.webServiceUrl + "%1$s&condition=%2$s", new Object[]{"87", URLEncoder.encode(jSONObject.toString(), "UTF-8")}), new Response.Listener<String>() {
                public void onResponse(String str) {
                    Log.d(ActivityStrategyLegs.this.TAG, "webServiceCallIsRewardAvailable Got response");
                    if (str.equals(Constants.REWARD_IS_AVAILABLE)) {
                        ActivityStrategyLegs.this.getUserYesNoChoice("Congrats! You are eligible for free premium strategy.");
                    }
                }
            }, new Response.ErrorListener() {
                public void onErrorResponse(VolleyError volleyError) {
                    String access$200 = ActivityStrategyLegs.this.TAG;
                    VolleyLog.d(access$200, "Error: " + volleyError.getMessage());
                }
            });
            getGreekApplication();
            MyGreeksApplication.getInstance().addToRequestQueue(stringRequest);
        } catch (UnsupportedEncodingException | JSONException e) {
            String str = this.TAG;
            Log.d(str, "Error | Volley Call | StrategyLegs : " + e.toString());
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
                    byte[][] doEncryption = ActivityStrategyLegs.this.getGreekApplication().doEncryption(str, jNICallResult_getGold);
                    jSONObject3.put("installid", ActivityStrategyLegs.this.getGreekApplication().getAppInstallId());
                    jSONObject3.put("token", new JSONObject(jSONObject2).get("token"));
                    jSONObject3.put("salt", Base64.encodeToString(doEncryption[0], 0));
                    jSONObject3.put("iv", Base64.encodeToString(doEncryption[1], 0));
                    jSONObject3.put("tonka", Base64.encodeToString(doEncryption[2], 0));
                } catch (JSONException e) {
                    String access$200 = ActivityStrategyLegs.this.TAG;
                    Log.d(access$200, "Error | Volley Call | SrategyLegs : " + e.toString());
                }
                try {
                    StringRequest stringRequest = new StringRequest(0, String.format(ActivityStrategyLegs.this.webServiceUrl + "%1$s&condition=%2$s", new Object[]{"84", URLEncoder.encode(jSONObject3.toString(), "UTF-8")}), new Response.Listener<String>() {
                        public void onResponse(String str) {
                            Log.d(ActivityStrategyLegs.this.TAG, "webServiceCallPayment : Got response");
                        }
                    }, new Response.ErrorListener() {
                        public void onErrorResponse(VolleyError volleyError) {
                            String access$200 = ActivityStrategyLegs.this.TAG;
                            VolleyLog.d(access$200, "Error: " + volleyError.getMessage());
                        }
                    });
                    ActivityStrategyLegs.this.getGreekApplication();
                    MyGreeksApplication.getInstance().addToRequestQueue(stringRequest);
                } catch (Exception e2) {
                    String access$2002 = ActivityStrategyLegs.this.TAG;
                    Log.d(access$2002, "Error | Volley Call | SrategyLegs : " + e2.toString());
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError volleyError) {
                String access$200 = ActivityStrategyLegs.this.TAG;
                VolleyLog.d(access$200, "Error: " + volleyError.getMessage());
            }
        }), "json_obj_req");
    }
}
