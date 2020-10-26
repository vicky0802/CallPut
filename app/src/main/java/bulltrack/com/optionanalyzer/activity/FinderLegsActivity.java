package bulltrack.com.optionanalyzer.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.core.internal.view.SupportMenu;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import bulltrack.com.optionanalyzer.application.MyGreeksApplication;
import bulltrack.com.optionanalyzer.constants.Constants;
import bulltrack.com.optionanalyzer.dao.StrategyLegsFilter;
import bulltrack.com.optionanalyzer.dao.StrategyResultsFilter;
import bulltrack.com.optionanalyzer.utility.CustomMarkerView;
import bulltrack.com.optionanalyzer.utility.OptionCalculator;
import bulltrack.com.optiongreeks13.R;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
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
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
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

public class FinderLegsActivity extends AppCompatActivity {
    /* access modifiers changed from: private */
    public String TAG = "FinderLegsActivity";
    /* access modifiers changed from: private */
    public Activity activity;
    boolean adClosedFlag = false;
    String calledFrom = "";
    AlertDialog dialogEdit;
    private Handler handler = new Handler();
    ImageView imgLocation;
    ImageView imgPayOff;
    LinearLayout llLeg3;
    LinearLayout llLeg4;
    LinearLayout llPositionLegsFrame;
    int lotSize = 1;
    List<StrategyLegsFilter> lstLegs;
    LineChart mChart;
    InterstitialAd mIAd_StgLeg;
    Map<Long, Double> mapPnl = new HashMap();
    LineChart payoffChart;
    View payoffSeparator;
    private ProgressBar progressBar;
    RelativeLayout rlMain;
    RelativeLayout rlPayFrame;
    View separatorAboveL3;
    View separatorAboveL4;
    View separatorAbvPnl;
    View separatorBlwPnl;
    boolean showAds = true;
    StrategyResultsFilter stgResult;
    float stockPrice = 0.0f;
    int stockPricePos = 0;
    float stockPricePrev = 0.0f;
    String strDelta = Constants.NULL_GREEK_VALUE;
    String strGamma = Constants.NULL_GREEK_VALUE;
    String strTheta = Constants.NULL_GREEK_VALUE;
    String strVega = Constants.NULL_GREEK_VALUE;
    TextView tvBreakDown;
    TextView tvBreakUp;
    TextView tvBuySell1;
    TextView tvBuySell2;
    TextView tvBuySell3;
    TextView tvBuySell4;
    TextView tvDelta;
    TextView tvEditorGain;
    TextView tvEditorHBEP;
    TextView tvEditorLBEP;
    TextView tvEditorRisk;
    TextView tvGamma;
    TextView tvInterest;
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
    TextView tvTheta;
    TextView tvVega;
    String webServiceUrl = Constants.URL_SERVICE;

    private double buyFut(float f, float f2) {
        return (double) (f - f2);
    }

    private double buycall(float f, float f2, float f3) {
        return (double) (f > f2 ? f - (f2 + f3) : -f3);
    }

    private double buyput(float f, float f2, float f3) {
        return f < f2 ? (double) (f2 - (f + f3)) : (double) (-f3);
    }

    private double sellFut(float f, float f2) {
        return (double) (f2 - f);
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
        interstitialAd.setAdUnitId(getString(R.string.ad_unit_id_finder_legs_interstitial));
        this.mIAd_StgLeg.loadAd(new AdRequest.Builder().build());
        this.progressBar = (ProgressBar) findViewById(R.id.progressbar_strategy_legs);
        this.activity = this;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.stgResult = (StrategyResultsFilter) extras.get("result");
            this.calledFrom = (String) extras.get("source");
            this.showAds = Boolean.valueOf(extras.getBoolean("showAds")).booleanValue();
            this.stockPrice = this.stgResult.getInvestment();
            this.stockPricePrev = this.stgResult.getInterestCost();
            this.lotSize = this.stgResult.getLotSize();
            String str = this.calledFrom;
            if (str == null || !str.equalsIgnoreCase(Constants.STG_LEGS_LAUNCHFROM_FOLIO)) {
                this.progressBar.post(new Runnable() {
                    public void run() {
                        if (FinderLegsActivity.this.isBlisted()) {
                            FinderLegsActivity.this.complain("Possible app integrity violated! Please contact app support");
                        } else {
                            FinderLegsActivity.this.WebServiceCallGetLegs();
                        }
                    }
                });
            } else {
                LoadFolioStgLegs(this.activity);
            }
        }
    }

    /* access modifiers changed from: private */
    public boolean isBlisted() {
        String str = getGreekApplication().getBlistFlags()[0];
        return !str.trim().equalsIgnoreCase(getGreekApplication().getJNICallResult_getBList());
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

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == 16908332) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    if (FinderLegsActivity.this.showAds && FinderLegsActivity.this.mIAd_StgLeg.isLoaded()) {
                        FinderLegsActivity.this.mIAd_StgLeg.show();
                        FinderLegsActivity.this.mIAd_StgLeg.setAdListener(new AdListener() {
                            public void onAdClosed() {
                                if (FinderLegsActivity.this.getParent() == null) {
                                    FinderLegsActivity.this.adClosedFlag = true;
                                    return;
                                }
                                NavUtils.navigateUpFromSameTask(FinderLegsActivity.this.activity);
                                FinderLegsActivity.this.finish();
                            }
                        });
                    } else if (FinderLegsActivity.this.getParent() == null) {
                        FinderLegsActivity.this.onBackPressed();
                    } else {
                        NavUtils.navigateUpFromSameTask(FinderLegsActivity.this.activity);
                        FinderLegsActivity.this.finish();
                    }
                }
            }, 1000);
            return true;
        }
        if (itemId == R.id.action_save) {
            try {
                new Date();
                Date time = Calendar.getInstance(TimeZone.getTimeZone("Asia/Calcutta")).getTime();
                if (this.calledFrom != null && this.calledFrom.equalsIgnoreCase(Constants.STG_LEGS_LAUNCHFROM_FOLIO)) {
                    getGreekApplication().deleteStrategy(this.stgResult);
                }
                this.stgResult.setUpdD(time);
                for (int i = 0; i < this.lstLegs.size(); i++) {
                    this.lstLegs.get(i).setUpdD(time);
                }
                getGreekApplication().saveStrategy(this.stgResult, this.lstLegs, this.tvDelta.getText().toString(), this.tvGamma.getText().toString(), this.tvTheta.getText().toString(), this.tvVega.getText().toString());
                Toast.makeText(getApplicationContext(), "Strategy Saved! ", 1).show();
            } catch (Exception e) {
                String str = this.TAG;
                Log.d(str, "Error | Saving strategy " + e.toString());
                Toast.makeText(getApplicationContext(), "ERROR: Strategy not saved ", 1).show();
            }
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void onResume() {
        super.onResume();
        if (this.adClosedFlag) {
            onBackPressed();
            this.adClosedFlag = false;
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_save, menu);
        return true;
    }

    /* access modifiers changed from: package-private */
    public void PrepareScreen() {
        try {
            if (this.lstLegs != null) {
                if (this.lstLegs.size() != 0) {
                    launchScreen();
                    return;
                }
            }
            setContentView((int) R.layout.no_data_layout);
        } catch (Exception e) {
            String str = this.TAG;
            Log.d(str, "Error | Volley Call " + e.toString());
            setContentView((int) R.layout.chart_error_message);
        }
    }

    /* access modifiers changed from: package-private */
    public void WebServiceCallGetPnL() {
        JSONArray jSONArray = new JSONArray();
        int i = 0;
        while (i < this.lstLegs.size()) {
            try {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("stock", this.lstLegs.get(i).getSymbol());
                jSONObject.put("action", this.lstLegs.get(i).getAction());
                jSONObject.put("strike", (double) this.lstLegs.get(i).getStrike());
                jSONObject.put("callput", this.lstLegs.get(i).getCallPut());
                jSONObject.put("expiry", getGreekApplication().dateFormatter(this.lstLegs.get(i).getExpiry(), Constants.DT_FMT_yyyy_MM_dd_HH_m_ss));
                jSONObject.put("premium", (double) this.lstLegs.get(i).getPremium());
                Calendar instance = Calendar.getInstance();
                instance.setTime(this.lstLegs.get(i).getUpdD());
                instance.set(11, 0);
                instance.set(12, 0);
                instance.set(13, 0);
                instance.set(14, 0);
                jSONObject.put("upd", getGreekApplication().dateFormatter(new Date(instance.getTimeInMillis()), Constants.DT_FMT_yyyy_MM_dd_HH_m_ss));
                jSONArray.put(jSONObject);
                i++;
            } catch (UnsupportedEncodingException | JSONException e) {
                String str = this.TAG;
                Log.d(str, "Error | Volley Call | FinderLegsActivity WebServiceCallGetLegs : " + e.toString());
                return;
            }
        }
        StringRequest stringRequest = new StringRequest(0, String.format(this.webServiceUrl + "%1$s&condition=%2$s", new Object[]{"83", URLEncoder.encode(jSONArray.toString(), "UTF-8")}), new Response.Listener<String>() {
            public void onResponse(String str) {
                try {
                    Log.d(FinderLegsActivity.this.TAG, "webServiceCallFinderLegs got resposne");
                    Type type = new TypeToken<Map<Long, Double>>() {
                    }.getType();
                    Gson create = new GsonBuilder().setDateFormat(Constants.DT_FMT_yyyy_MM_dd_HH_m_ss).create();
                    FinderLegsActivity.this.mapPnl = (Map) create.fromJson(str, type);
                    FinderLegsActivity.this.ShowPnLIfDataAvailable();
                } catch (Exception e) {
                    String access$200 = FinderLegsActivity.this.TAG;
                    Log.d(access$200, "Error | Volley Call " + e.toString());
                    FinderLegsActivity.this.setContentView((int) R.layout.chart_error_message);
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError volleyError) {
                String access$200 = FinderLegsActivity.this.TAG;
                VolleyLog.d(access$200, "Error: " + volleyError.getMessage());
                FinderLegsActivity.this.setContentView((int) R.layout.chart_error_message);
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, 1, 1.0f));
        getGreekApplication();
        MyGreeksApplication.getInstance().addToRequestQueue(stringRequest);
    }

    /* access modifiers changed from: package-private */
    public void ShowPnLIfDataAvailable() {
        if (this.mapPnl.size() > 0) {
            this.mChart.setVisibility(0);
            LoadLinePnLChart();
            this.separatorAbvPnl.setVisibility(0);
            this.tvPnlText.setVisibility(0);
            this.separatorBlwPnl.setVisibility(0);
            return;
        }
        this.mChart.setVisibility(8);
        this.separatorAbvPnl.setVisibility(8);
        this.tvPnlText.setVisibility(8);
        this.separatorBlwPnl.setVisibility(8);
    }

    /* access modifiers changed from: package-private */
    public void WebServiceCallGetLegs() {
        final AlertDialog progressDialog = setProgressDialog();
        progressDialog.show();
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("symbol", this.stgResult.getSymbol());
            jSONObject.put("strategyid", this.stgResult.getStrategyId());
            jSONObject.put("subkey", this.stgResult.getStrategySubKey());
            jSONObject.put("runseq", this.stgResult.getRunSeq());
            StringRequest stringRequest = new StringRequest(0, String.format(this.webServiceUrl + "%1$s&condition=%2$s", new Object[]{"103", URLEncoder.encode(jSONObject.toString(), "UTF-8")}), new Response.Listener<String>() {
                public void onResponse(String str) {
                    try {
                        Log.d(FinderLegsActivity.this.TAG, "webServiceCallGetFinder got resposne");
                        Type type = new TypeToken<List<StrategyLegsFilter>>() {
                        }.getType();
                        Gson create = new GsonBuilder().setDateFormat(Constants.DT_FMT_yyyy_MM_dd_HH_m_ss).create();
                        JSONObject jSONObject = new JSONObject(str.toString());
                        FinderLegsActivity.this.strDelta = jSONObject.getString("delta");
                        FinderLegsActivity.this.strGamma = jSONObject.getString("gamma");
                        FinderLegsActivity.this.strTheta = jSONObject.getString("theta");
                        FinderLegsActivity.this.strVega = jSONObject.getString("vega");
                        FinderLegsActivity.this.lstLegs = (List) create.fromJson(jSONObject.getString("legs"), type);
                        if (FinderLegsActivity.this.lstLegs != null) {
                            if (FinderLegsActivity.this.lstLegs.size() != 0) {
                                FinderLegsActivity.this.launchScreen();
                                FinderLegsActivity.this.ShowPnLIfDataAvailable();
                                progressDialog.dismiss();
                                return;
                            }
                        }
                        FinderLegsActivity.this.setContentView((int) R.layout.no_data_layout);
                        progressDialog.dismiss();
                    } catch (Exception e) {
                        String access$200 = FinderLegsActivity.this.TAG;
                        Log.d(access$200, "Error | Volley Call " + e.toString());
                        FinderLegsActivity.this.setContentView((int) R.layout.chart_error_message);
                    } catch (Throwable th) {
                        progressDialog.dismiss();
                        throw th;
                    }
                }
            }, new Response.ErrorListener() {
                public void onErrorResponse(VolleyError volleyError) {
                    String access$200 = FinderLegsActivity.this.TAG;
                    VolleyLog.d(access$200, "Error: " + volleyError.getMessage());
                    FinderLegsActivity.this.setContentView((int) R.layout.chart_error_message);
                    progressDialog.dismiss();
                }
            });
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, 1, 1.0f));
            getGreekApplication();
            MyGreeksApplication.getInstance().addToRequestQueue(stringRequest);
        } catch (UnsupportedEncodingException | JSONException e) {
            String str = this.TAG;
            Log.d(str, "Error | Volley Call | FinderLegsActivity WebServiceCallGetLegs : " + e.toString());
        }
    }

    public void launchScreen() {
        LoadUI();
        setupUIElements();
        LoadPayoffChart();
        showStrategyLegs();
    }

    public void setupUIElements() {
        String str;
        String str2;
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        this.imgPayOff.setImageResource(getGreekApplication().getPayoffDiagram(this.stgResult.getStrategyId()));
        this.tvStock.setText(this.stgResult.getSymbol());
        this.tvStrategy.setText(this.stgResult.getStrategyName());
        TextView textView = this.tvInterest;
        textView.setText(this.stgResult.getInterestCost() + "");
        TextView textView2 = this.tvMaxRisk;
        textView2.setText("₹ " + getGreekApplication().round2Decimals1000((double) MyGreeksApplication.roundTo05(this.stgResult.getMaxRisk() * ((float) this.lotSize))));
        TextView textView3 = this.tvLikelyGain;
        textView3.setText(this.stgResult.getExpectedGain() + "");
        if (this.stgResult.getStrategyId() == 104 || this.stgResult.getStrategyId() == 105 || this.stgResult.getStrategyId() == 111) {
            this.tvMaxGain.setText(Constants.FINDER_UNLIMITED_GAIN);
        } else if (this.stgResult.getMaxGain() * ((float) this.lotSize) >= 100000.0f) {
            TextView textView4 = this.tvMaxGain;
            StringBuilder sb = new StringBuilder();
            sb.append("₹ ");
            double maxGain = (double) (this.stgResult.getMaxGain() * ((float) this.lotSize));
            Double.isNaN(maxGain);
            sb.append(decimalFormat.format(maxGain / 100000.0d));
            sb.append(" Lac");
            textView4.setText(sb.toString());
        } else {
            TextView textView5 = this.tvMaxGain;
            textView5.setText("₹ " + getGreekApplication().round2Decimals1000((double) MyGreeksApplication.roundTo05(this.stgResult.getMaxGain() * ((float) this.lotSize))));
        }
        this.tvDelta.setText(this.strDelta);
        this.tvGamma.setText(this.strGamma);
        this.tvTheta.setText(this.strTheta);
        this.tvVega.setText(this.strVega);
        if (((double) this.stgResult.getBreakevenUp()) == 0.0d) {
            this.tvBreakUp.setText("-");
        } else {
            this.tvBreakUp.setText(getGreekApplication().round2Decimals1000((double) this.stgResult.getBreakevenUp()));
        }
        if (((double) this.stgResult.getBreakevenDown()) == 0.0d) {
            this.tvBreakDown.setText("-");
        } else {
            this.tvBreakDown.setText(getGreekApplication().round2Decimals1000((double) this.stgResult.getBreakevenDown()));
        }
        TextView textView6 = this.tvLegsCount;
        textView6.setText(this.lstLegs.size() + " Legs");
        String str3 = "Buy";
        this.tvBuySell1.setText(this.lstLegs.get(0).getAction().equals(Constants.ACTION_BUY) ? str3 : "Sell");
        if (this.lstLegs.get(0).getCallPut().equalsIgnoreCase("F")) {
            TextView textView7 = this.tvOptionId1;
            textView7.setText("FUTURE-" + getGreekApplication().dateFormatter(this.lstLegs.get(0).getExpiry(), Constants.DT_FMT_dd_MMM_yyyy));
        } else {
            TextView textView8 = this.tvOptionId1;
            textView8.setText(this.lstLegs.get(0).getStrike() + "-" + this.lstLegs.get(0).getCallPut() + "-" + getGreekApplication().dateFormatter(this.lstLegs.get(0).getExpiry(), Constants.DT_FMT_dd_MMM_yyyy));
        }
        this.tvPremium1.setText(getGreekApplication().round2Decimals((double) this.lstLegs.get(0).getPremium()));
        TextView textView9 = this.tvBuySell2;
        if (this.lstLegs.get(1).getAction().equals(Constants.ACTION_BUY)) {
            str = str3;
        } else {
            str = "Sell";
        }
        textView9.setText(str);
        if (this.lstLegs.get(1).getCallPut().equalsIgnoreCase("F")) {
            TextView textView10 = this.tvOptionId2;
            textView10.setText("FUTURE-" + getGreekApplication().dateFormatter(this.lstLegs.get(1).getExpiry(), Constants.DT_FMT_dd_MMM_yyyy));
        } else {
            TextView textView11 = this.tvOptionId2;
            textView11.setText(this.lstLegs.get(1).getStrike() + "-" + this.lstLegs.get(1).getCallPut() + "-" + getGreekApplication().dateFormatter(this.lstLegs.get(1).getExpiry(), Constants.DT_FMT_dd_MMM_yyyy));
        }
        this.tvPremium2.setText(getGreekApplication().round2Decimals((double) this.lstLegs.get(1).getPremium()));
        this.llLeg3.setVisibility(8);
        this.separatorAboveL3.setVisibility(8);
        if (this.lstLegs.size() > 2) {
            this.llLeg3.setVisibility(0);
            TextView textView12 = this.tvBuySell3;
            if (this.lstLegs.get(2).getAction().equals(Constants.ACTION_BUY)) {
                str2 = str3;
            } else {
                str2 = "Sell";
            }
            textView12.setText(str2);
            if (this.lstLegs.get(2).getCallPut().equalsIgnoreCase("F")) {
                TextView textView13 = this.tvOptionId3;
                textView13.setText("FUTURE-" + getGreekApplication().dateFormatter(this.lstLegs.get(2).getExpiry(), Constants.DT_FMT_dd_MMM_yyyy));
            } else {
                TextView textView14 = this.tvOptionId3;
                textView14.setText(this.lstLegs.get(2).getStrike() + "-" + this.lstLegs.get(2).getCallPut() + "-" + getGreekApplication().dateFormatter(this.lstLegs.get(2).getExpiry(), Constants.DT_FMT_dd_MMM_yyyy));
            }
            this.tvPremium3.setText(getGreekApplication().round2Decimals((double) this.lstLegs.get(2).getPremium()));
            this.separatorAboveL3.setVisibility(0);
        }
        this.llLeg4.setVisibility(8);
        this.separatorAboveL4.setVisibility(8);
        if (this.lstLegs.size() > 3) {
            this.llLeg4.setVisibility(0);
            TextView textView15 = this.tvBuySell4;
            if (!this.lstLegs.get(3).getAction().equals(Constants.ACTION_BUY)) {
                str3 = "Sell";
            }
            textView15.setText(str3);
            if (this.lstLegs.get(3).getCallPut().equalsIgnoreCase("F")) {
                TextView textView16 = this.tvOptionId4;
                textView16.setText("FUTURE-" + getGreekApplication().dateFormatter(this.lstLegs.get(3).getExpiry(), Constants.DT_FMT_dd_MMM_yyyy));
            } else {
                TextView textView17 = this.tvOptionId4;
                textView17.setText(this.lstLegs.get(3).getStrike() + "-" + this.lstLegs.get(3).getCallPut() + "-" + getGreekApplication().dateFormatter(this.lstLegs.get(3).getExpiry(), Constants.DT_FMT_dd_MMM_yyyy));
            }
            this.tvPremium4.setText(getGreekApplication().round2Decimals((double) this.lstLegs.get(3).getPremium()));
            this.separatorAboveL4.setVisibility(0);
        }
        float f = 0.0f;
        float f2 = 0.0f;
        for (int i = 0; i < this.lstLegs.size(); i++) {
            if (this.lstLegs.get(i).getAction().trim().equalsIgnoreCase(Constants.ACTION_BUY)) {
                f += this.lstLegs.get(i).getPremium();
            } else {
                f2 += this.lstLegs.get(i).getPremium();
            }
        }
        TextView textView18 = this.tvSpreadVal;
        textView18.setText(Math.abs(f - f2) + "");
        TextView textView19 = this.tvLotSize;
        textView19.setText("Lot Size : " + Integer.toString(this.stgResult.getLotSize()));
    }

    public void LoadUI() {
        setContentView((int) R.layout.activity_strategyfinder_legs);
        AdView adView = (AdView) findViewById(R.id.adView_strategyfinder_legs_banner);
        if (this.showAds) {
            adView.setVisibility(0);
            adView.loadAd(new AdRequest.Builder().setRequestAgent("android_studio:ad_template").build());
        } else {
            adView.setVisibility(8);
        }
        this.activity = this;
        this.imgPayOff = (ImageView) findViewById(R.id.img_strategyfinder_legs_payoff);
        this.imgLocation = (ImageView) findViewById(R.id.img_activity_leg_stocklocation);
        this.tvStock = (TextView) findViewById(R.id.tv_strategyfinder_legs_stock);
        this.tvStrategy = (TextView) findViewById(R.id.tv_strategyfinder_legs_strategy);
        this.tvInterest = (TextView) findViewById(R.id.tv_strategyfinder_legs_interest_val);
        this.tvMaxRisk = (TextView) findViewById(R.id.tv_strategyfinder_legs_risk_val);
        this.tvLikelyGain = (TextView) findViewById(R.id.tv_strategyfinder_legs_likelygain_val);
        this.tvMaxGain = (TextView) findViewById(R.id.tv_strategyfinder_legs_gain_val);
        this.tvBreakUp = (TextView) findViewById(R.id.tv_strategyfinder_legs_hbep_val);
        this.tvBreakDown = (TextView) findViewById(R.id.tv_strategyfinder_legs_lbep_val);
        this.tvBuySell1 = (TextView) findViewById(R.id.tv_strategyfinder_legs_leg1_buysell);
        this.tvOptionId1 = (TextView) findViewById(R.id.tv_strategyfinder_legs_leg1_optionid);
        this.tvPremium1 = (TextView) findViewById(R.id.tv_strategyfinder_legs_leg1_premium);
        this.tvBuySell2 = (TextView) findViewById(R.id.tv_strategyfinder_legs_leg2_buysell);
        this.tvOptionId2 = (TextView) findViewById(R.id.tv_strategyfinder_legs_leg2_optionid);
        this.tvPremium2 = (TextView) findViewById(R.id.tv_strategyfinder_legs_leg2_premium);
        this.tvBuySell3 = (TextView) findViewById(R.id.tv_strategyfinder_legs_leg3_buysell);
        this.tvOptionId3 = (TextView) findViewById(R.id.tv_strategyfinder_legs_leg3_optionid);
        this.tvPremium3 = (TextView) findViewById(R.id.tv_strategyfinder_legs_leg3_premium);
        this.tvBuySell4 = (TextView) findViewById(R.id.tv_strategyfinder_legs_leg4_buysell);
        this.tvOptionId4 = (TextView) findViewById(R.id.tv_strategyfinder_legs_leg4_optionid);
        this.tvPremium4 = (TextView) findViewById(R.id.tv_strategyfinder_legs_leg4_premium);
        this.separatorAboveL3 = findViewById(R.id.separator_strategyfinder_legs_4);
        this.llLeg3 = (LinearLayout) findViewById(R.id.ll_strategyfinder_legs_legs3_val);
        this.separatorAboveL4 = findViewById(R.id.separator_strategyfinder_legs_5);
        this.llLeg4 = (LinearLayout) findViewById(R.id.ll_strategyfinder_legs_legs4_val);
        this.tvSpreadHead = (TextView) findViewById(R.id.tv_strategyfinder_legs_spread_head);
        this.tvSpreadVal = (TextView) findViewById(R.id.tv_strategyfinder_legs_spread_val);
        this.mChart = (LineChart) findViewById(R.id.linechart_strategyfinder_pnl);
        this.payoffSeparator = findViewById(R.id.separator_strategyfinder_legs_7);
        this.payoffChart = (LineChart) findViewById(R.id.linechart_strategyfinder_legs_payoff);
        this.tvLotSize = (TextView) findViewById(R.id.tv_strategyfinder_legs_lotsize);
        this.tvLegsCount = (TextView) findViewById(R.id.tv_strategyfinder_legs_legcount);
        this.separatorAbvPnl = findViewById(R.id.separator_strategyfinder_legs_9);
        this.tvPnlText = (TextView) findViewById(R.id.tv_strategyfinder_pnl_head);
        this.separatorBlwPnl = findViewById(R.id.separator_strategyfinder_legs_10);
        this.tvPayAmount = (TextView) findViewById(R.id.tv_strategyfinder_legs_subs_amt);
        this.llPositionLegsFrame = (LinearLayout) findViewById(R.id.ll_strategyfinder_legs_legs_frame);
        this.rlPayFrame = (RelativeLayout) findViewById(R.id.rl_strategyfinder_legs_subscription_frame);
        this.rlMain = (RelativeLayout) findViewById(R.id.rl_strategyfinder_legs_main);
        this.tvDelta = (TextView) findViewById(R.id.tv_strategyfinder_legs_delta_val);
        this.tvGamma = (TextView) findViewById(R.id.tv_strategyfinder_legs_gamma_val);
        this.tvTheta = (TextView) findViewById(R.id.tv_strategyfinder_legs_theta_val);
        this.tvVega = (TextView) findViewById(R.id.tv_strategyfinder_legs_vega_val);
        ((ImageView) findViewById(R.id.img_strategyfinder_legs_edit)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(FinderLegsActivity.this);
                builder.setView(FinderLegsActivity.this.getLayoutInflater().inflate(R.layout.strategy_editor, (ViewGroup) null));
                builder.setTitle("Modify Entry Prices");
                FinderLegsActivity.this.dialogEdit = builder.create();
                FinderLegsActivity.this.dialogEdit.show();
                FinderLegsActivity.this.FillEditStrategyForm();
                FinderLegsActivity.this.dialogEdit.setCanceledOnTouchOutside(true);
                FinderLegsActivity.this.dialogEdit.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    public void onCancel(DialogInterface dialogInterface) {
                        FinderLegsActivity.this.EditStrategyRefresh(FinderLegsActivity.this.activity);
                    }
                });
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.imgLocation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                CustomMarkerView customMarkerView = (CustomMarkerView) FinderLegsActivity.this.payoffChart.getMarkerView();
                customMarkerView.setFirst();
                FinderLegsActivity.this.payoffChart.setMarkerView(customMarkerView);
                FinderLegsActivity.this.payoffChart.highlightValue(new Highlight(FinderLegsActivity.this.stockPricePos, 0));
            }
        });
    }

    public AlertDialog setProgressDialog() {
        LinearLayout linearLayout = new LinearLayout(this.activity);
        linearLayout.setOrientation(0);
        linearLayout.setPadding(30, 30, 30, 30);
        linearLayout.setGravity(17);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
        layoutParams.gravity = 17;
        linearLayout.setLayoutParams(layoutParams);
        ProgressBar progressBar2 = new ProgressBar(this.activity);
        progressBar2.setIndeterminate(true);
        progressBar2.setPadding(0, 0, 30, 0);
        progressBar2.setLayoutParams(layoutParams);
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-2, -2);
        layoutParams2.gravity = 17;
        TextView textView = new TextView(this.activity);
        textView.setText("Loading ...");
        textView.setTextColor(Color.parseColor("#000000"));
        textView.setTextSize(20.0f);
        textView.setLayoutParams(layoutParams2);
        linearLayout.addView(progressBar2);
        linearLayout.addView(textView);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setView(linearLayout);
        AlertDialog create = builder.create();
        if (create.getWindow() != null) {
            WindowManager.LayoutParams layoutParams3 = new WindowManager.LayoutParams();
            layoutParams3.copyFrom(create.getWindow().getAttributes());
            layoutParams3.width = -2;
            layoutParams3.height = -2;
            create.getWindow().setAttributes(layoutParams3);
        }
        return create;
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
        double d;
        double d2 = 0.0d;
        for (int i = 0; i < this.lstLegs.size(); i++) {
            String callPut = this.lstLegs.get(i).getCallPut();
            String action = this.lstLegs.get(i).getAction();
            float strike = this.lstLegs.get(i).getStrike();
            float premium = this.lstLegs.get(i).getPremium();
            if (callPut == null || !callPut.equalsIgnoreCase("C")) {
                if (callPut == null || !callPut.equalsIgnoreCase(Constants.FLAG_POSITIONAL)) {
                    if (action == null || !action.equalsIgnoreCase(Constants.ACTION_BUY)) {
                        d = sellFut(f, premium);
                    } else {
                        d = buyFut(f, premium);
                    }
                } else if (action == null || !action.equalsIgnoreCase(Constants.ACTION_BUY)) {
                    d = sellput(f, strike, premium);
                } else {
                    d = buyput(f, strike, premium);
                }
            } else if (action == null || !action.equalsIgnoreCase(Constants.ACTION_BUY)) {
                d = sellcall(f, strike, premium);
            } else {
                d = buycall(f, strike, premium);
            }
            d2 += d;
        }
        DecimalFormat decimalFormat = new DecimalFormat("#########0.00");
        double d3 = (double) this.lotSize;
        Double.isNaN(d3);
        return new Float(decimalFormat.format(d2 * d3)).floatValue();
    }

    /* access modifiers changed from: private */
    public void showStrategyLegs() {
        if (this.rlPayFrame.getVisibility() != 8) {
            this.rlPayFrame.setVisibility(8);
            this.llPositionLegsFrame.setVisibility(0);
            ((RelativeLayout.LayoutParams) this.payoffSeparator.getLayoutParams()).addRule(3, this.llPositionLegsFrame.getId());
        }
    }

    public void FillEditStrategyForm() {
        this.tvEditorRisk = (TextView) this.dialogEdit.findViewById(R.id.tv_strategy_editor_risk_val);
        this.tvEditorGain = (TextView) this.dialogEdit.findViewById(R.id.tv_strategy_editor_gain_val);
        this.tvEditorLBEP = (TextView) this.dialogEdit.findViewById(R.id.tv_strategy_editor_lbep_val);
        this.tvEditorHBEP = (TextView) this.dialogEdit.findViewById(R.id.tv_strategy_editor_hbep_val);
        Button button = (Button) this.dialogEdit.findViewById(R.id.btn_strategy_editor_leg1_inc);
        Button button2 = (Button) this.dialogEdit.findViewById(R.id.btn_strategy_editor_leg1_dec);
        Button button3 = (Button) this.dialogEdit.findViewById(R.id.btn_strategy_editor_leg2_inc);
        Button button4 = (Button) this.dialogEdit.findViewById(R.id.btn_strategy_editor_leg2_dec);
        Button button5 = (Button) this.dialogEdit.findViewById(R.id.btn_strategy_editor_leg3_inc);
        Button button6 = (Button) this.dialogEdit.findViewById(R.id.btn_strategy_editor_leg3_dec);
        Button button7 = (Button) this.dialogEdit.findViewById(R.id.btn_strategy_editor_leg4_inc);
        Button button8 = (Button) this.dialogEdit.findViewById(R.id.btn_strategy_editor_leg4_dec);
        final OptionCalculator optionCalculatorInstance = OptionCalculator.getOptionCalculatorInstance();
        if (this.lstLegs.size() > 0) {
            final EditText editText = (EditText) this.dialogEdit.findViewById(R.id.edt_strategy_editor_leg1_price);
            TextView textView = (TextView) this.dialogEdit.findViewById(R.id.tv_strategy_editor_leg_1);
            editText.setVisibility(0);
            textView.setVisibility(0);
            button2.setVisibility(0);
            button.setVisibility(0);
            textView.setText(String.valueOf(this.lstLegs.get(0).getStrike()) + " " + this.lstLegs.get(0).getCallPut());
            editText.setText(getGreekApplication().round2Decimals1000((double) this.lstLegs.get(0).getPremium()));
            editText.addTextChangedListener(new TextWatcher() {
                public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                }

                public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                }

                public void afterTextChanged(Editable editable) {
                    if (!editText.getText().toString().trim().equals("")) {
                        FinderLegsActivity.this.lstLegs.get(0).setPremium(Float.parseFloat(editText.getText().toString().replace(",", "")));
                        FinderLegsActivity.this.updateStraegyEditorDisplay(optionCalculatorInstance);
                    }
                }
            });
            editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                public void onFocusChange(View view, boolean z) {
                    if (z) {
                        editText.setText("");
                    } else if (editText.getText().toString().trim().equals("")) {
                        editText.setText(FinderLegsActivity.this.getGreekApplication().round2Decimals1000((double) FinderLegsActivity.this.lstLegs.get(0).getPremium()));
                    }
                }
            });
        }
        if (this.lstLegs.size() > 1) {
            final EditText editText2 = (EditText) this.dialogEdit.findViewById(R.id.edt_strategy_editor_leg2_price);
            TextView textView2 = (TextView) this.dialogEdit.findViewById(R.id.tv_strategy_editor_leg_2);
            editText2.setVisibility(0);
            textView2.setVisibility(0);
            button4.setVisibility(0);
            button3.setVisibility(0);
            textView2.setText(String.valueOf(this.lstLegs.get(1).getStrike()) + " " + this.lstLegs.get(1).getCallPut());
            editText2.setText(getGreekApplication().round2Decimals1000((double) this.lstLegs.get(1).getPremium()));
            editText2.addTextChangedListener(new TextWatcher() {
                public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                }

                public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                }

                public void afterTextChanged(Editable editable) {
                    if (!editText2.getText().toString().trim().equals("")) {
                        FinderLegsActivity.this.lstLegs.get(1).setPremium(Float.parseFloat(editText2.getText().toString().replace(",", "")));
                        FinderLegsActivity.this.updateStraegyEditorDisplay(optionCalculatorInstance);
                    }
                }
            });
            editText2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                public void onFocusChange(View view, boolean z) {
                    if (z) {
                        editText2.setText("");
                    } else if (editText2.getText().toString().trim().equals("")) {
                        editText2.setText(FinderLegsActivity.this.getGreekApplication().round2Decimals1000((double) FinderLegsActivity.this.lstLegs.get(1).getPremium()));
                    }
                }
            });
        }
        if (this.lstLegs.size() > 2) {
            final EditText editText3 = (EditText) this.dialogEdit.findViewById(R.id.edt_strategy_editor_leg3_price);
            TextView textView3 = (TextView) this.dialogEdit.findViewById(R.id.tv_strategy_editor_leg_3);
            editText3.setVisibility(0);
            textView3.setVisibility(0);
            textView3.setText(String.valueOf(this.lstLegs.get(2).getStrike()) + " " + this.lstLegs.get(2).getCallPut());
            button6.setVisibility(0);
            button5.setVisibility(0);
            editText3.setText(getGreekApplication().round2Decimals1000((double) this.lstLegs.get(2).getPremium()));
            editText3.addTextChangedListener(new TextWatcher() {
                public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                }

                public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                }

                public void afterTextChanged(Editable editable) {
                    if (!editText3.getText().toString().trim().equals("")) {
                        FinderLegsActivity.this.lstLegs.get(2).setPremium(Float.parseFloat(editText3.getText().toString().replace(",", "")));
                        FinderLegsActivity.this.updateStraegyEditorDisplay(optionCalculatorInstance);
                    }
                }
            });
            editText3.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                public void onFocusChange(View view, boolean z) {
                    if (z) {
                        editText3.setText("");
                    } else if (editText3.getText().toString().trim().equals("")) {
                        editText3.setText(FinderLegsActivity.this.getGreekApplication().round2Decimals1000((double) FinderLegsActivity.this.lstLegs.get(2).getPremium()));
                    }
                }
            });
        }
        if (this.lstLegs.size() > 3) {
            final EditText editText4 = (EditText) this.dialogEdit.findViewById(R.id.edt_strategy_editor_leg4_price);
            TextView textView4 = (TextView) this.dialogEdit.findViewById(R.id.tv_strategy_editor_leg_4);
            editText4.setVisibility(0);
            textView4.setVisibility(0);
            button8.setVisibility(0);
            button7.setVisibility(0);
            textView4.setText(String.valueOf(this.lstLegs.get(3).getStrike()) + " " + this.lstLegs.get(3).getCallPut());
            editText4.setText(getGreekApplication().round2Decimals1000((double) this.lstLegs.get(3).getPremium()));
            editText4.addTextChangedListener(new TextWatcher() {
                public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                }

                public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                }

                public void afterTextChanged(Editable editable) {
                    if (!editText4.getText().toString().trim().equals("")) {
                        FinderLegsActivity.this.lstLegs.get(3).setPremium(Float.parseFloat(editText4.getText().toString().replace(",", "")));
                        FinderLegsActivity.this.updateStraegyEditorDisplay(optionCalculatorInstance);
                    }
                }
            });
            editText4.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                public void onFocusChange(View view, boolean z) {
                    if (z) {
                        editText4.setText("");
                    } else if (editText4.getText().toString().trim().equals("")) {
                        editText4.setText(FinderLegsActivity.this.getGreekApplication().round2Decimals1000((double) FinderLegsActivity.this.lstLegs.get(3).getPremium()));
                    }
                }
            });
        }
        this.tvEditorRisk.setText(getGreekApplication().round2Decimals1000((double) (this.stgResult.getMaxRisk() * ((float) this.lotSize))));
        this.tvEditorGain.setText(getGreekApplication().round2Decimals1000((double) (this.stgResult.getMaxGain() * ((float) this.lotSize))));
        this.tvEditorLBEP.setText(getGreekApplication().round2Decimals1000((double) this.stgResult.getBreakevenDown()));
        this.tvEditorHBEP.setText(getGreekApplication().round2Decimals1000((double) this.stgResult.getBreakevenUp()));
    }

    public void updateStraegyEditorDisplay(OptionCalculator optionCalculator) {
        double recalculateNetDebit = (double) ((float) optionCalculator.recalculateNetDebit(this.lstLegs));
        float reCalculateMaxRisk = (float) optionCalculator.reCalculateMaxRisk(this.lstLegs, recalculateNetDebit);
        float reCalculateMaxGain = (float) optionCalculator.reCalculateMaxGain(this.lstLegs, recalculateNetDebit);
        double[] reCalculateBreakevenpoints = optionCalculator.reCalculateBreakevenpoints(this.lstLegs, recalculateNetDebit);
        this.tvEditorRisk.setText(getGreekApplication().round2Decimals1000((double) (((float) this.lotSize) * reCalculateMaxRisk)));
        this.tvEditorGain.setText(getGreekApplication().round2Decimals1000((double) (((float) this.lotSize) * reCalculateMaxGain)));
        if (reCalculateBreakevenpoints[0] == Double.NaN) {
            this.tvEditorLBEP.setText("-");
        } else {
            this.tvEditorLBEP.setText(getGreekApplication().round2Decimals1000(reCalculateBreakevenpoints[0]));
        }
        if (reCalculateBreakevenpoints[1] == Double.NaN) {
            this.tvEditorHBEP.setText("-");
        } else {
            this.tvEditorHBEP.setText(getGreekApplication().round2Decimals1000(reCalculateBreakevenpoints[1]));
        }
        this.stgResult.setMaxGain(reCalculateMaxGain);
        this.stgResult.setMaxRisk(reCalculateMaxRisk);
        this.stgResult.setBreakevenUp((float) reCalculateBreakevenpoints[1]);
        this.stgResult.setBreakevenDown((float) reCalculateBreakevenpoints[0]);
    }

    public void buttonClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_strategy_editor_leg1_dec /*2131230848*/:
                try {
                    EditText editText = (EditText) this.dialogEdit.findViewById(R.id.edt_strategy_editor_leg1_price);
                    float parseFloat = Float.parseFloat(editText.getText().toString().replace(",", ""));
                    if (parseFloat > 0.05f) {
                        editText.setText(getGreekApplication().round2Decimals1000((double) (parseFloat - 0.05f)));
                        return;
                    }
                    return;
                } catch (NumberFormatException unused) {
                    Toast.makeText(getApplicationContext(), "Invalid Price ", 1).show();
                    return;
                }
            case R.id.btn_strategy_editor_leg1_inc /*2131230849*/:
                try {
                    EditText editText2 = (EditText) this.dialogEdit.findViewById(R.id.edt_strategy_editor_leg1_price);
                    editText2.setText(getGreekApplication().round2Decimals1000((double) (Float.parseFloat(editText2.getText().toString().replace(",", "")) + 0.05f)));
                    return;
                } catch (NumberFormatException unused2) {
                    Toast.makeText(getApplicationContext(), "Invalid Price ", 1).show();
                    return;
                }
            case R.id.btn_strategy_editor_leg2_dec /*2131230850*/:
                try {
                    EditText editText3 = (EditText) this.dialogEdit.findViewById(R.id.edt_strategy_editor_leg2_price);
                    float parseFloat2 = Float.parseFloat(editText3.getText().toString().replace(",", ""));
                    if (parseFloat2 > 0.05f) {
                        editText3.setText(getGreekApplication().round2Decimals1000((double) (parseFloat2 - 0.05f)));
                        return;
                    }
                    return;
                } catch (NumberFormatException unused3) {
                    Toast.makeText(getApplicationContext(), "Invalid Price ", 1).show();
                    return;
                }
            case R.id.btn_strategy_editor_leg2_inc /*2131230851*/:
                try {
                    EditText editText4 = (EditText) this.dialogEdit.findViewById(R.id.edt_strategy_editor_leg2_price);
                    editText4.setText(getGreekApplication().round2Decimals1000((double) (Float.parseFloat(editText4.getText().toString().replace(",", "")) + 0.05f)));
                    return;
                } catch (NumberFormatException unused4) {
                    Toast.makeText(getApplicationContext(), "Invalid Price ", 1).show();
                    return;
                }
            case R.id.btn_strategy_editor_leg3_dec /*2131230852*/:
                try {
                    EditText editText5 = (EditText) this.dialogEdit.findViewById(R.id.edt_strategy_editor_leg3_price);
                    float parseFloat3 = Float.parseFloat(editText5.getText().toString().replace(",", ""));
                    if (parseFloat3 > 0.05f) {
                        editText5.setText(getGreekApplication().round2Decimals1000((double) (parseFloat3 - 0.05f)));
                        return;
                    }
                    return;
                } catch (NumberFormatException unused5) {
                    Toast.makeText(getApplicationContext(), "Invalid Price ", 1).show();
                    return;
                }
            case R.id.btn_strategy_editor_leg3_inc /*2131230853*/:
                try {
                    EditText editText6 = (EditText) this.dialogEdit.findViewById(R.id.edt_strategy_editor_leg3_price);
                    editText6.setText(getGreekApplication().round2Decimals1000((double) (Float.parseFloat(editText6.getText().toString().replace(",", "")) + 0.05f)));
                    return;
                } catch (NumberFormatException unused6) {
                    Toast.makeText(getApplicationContext(), "Invalid Price ", 1).show();
                    return;
                }
            case R.id.btn_strategy_editor_leg4_dec /*2131230854*/:
                try {
                    EditText editText7 = (EditText) this.dialogEdit.findViewById(R.id.edt_strategy_editor_leg4_price);
                    float parseFloat4 = Float.parseFloat(editText7.getText().toString().replace(",", ""));
                    if (parseFloat4 > 0.05f) {
                        editText7.setText(getGreekApplication().round2Decimals1000((double) (parseFloat4 - 0.05f)));
                        return;
                    }
                    return;
                } catch (NumberFormatException unused7) {
                    Toast.makeText(getApplicationContext(), "Invalid Price ", 1).show();
                    return;
                }
            case R.id.btn_strategy_editor_leg4_inc /*2131230855*/:
                try {
                    EditText editText8 = (EditText) this.dialogEdit.findViewById(R.id.edt_strategy_editor_leg4_price);
                    editText8.setText(getGreekApplication().round2Decimals1000((double) (Float.parseFloat(editText8.getText().toString().replace(",", "")) + 0.05f)));
                    return;
                } catch (NumberFormatException unused8) {
                    Toast.makeText(getApplicationContext(), "Invalid Price ", 1).show();
                    return;
                }
            default:
                return;
        }
    }

    private int[] ReCalcStrategy(int i, int i2, int i3, int i4, int i5, int i6) {
        int i7;
        int i8;
        int i9;
        if (i4 == 101) {
            i8 = ((i + i2) - i5) - i3;
            i7 = (i6 - i5) - i8;
            i9 = (i - i3) + i2;
        } else if (i4 == 201) {
            i9 = i + (i2 - i3);
            i8 = i6 - i9;
            i7 = i9 - i5;
        } else {
            i9 = 0;
            i8 = 0;
            i7 = 0;
        }
        int i10 = this.lotSize;
        return new int[]{i8 * i10, i7 * i10, i9};
    }

    /* access modifiers changed from: protected */
    public MyGreeksApplication getGreekApplication() {
        return (MyGreeksApplication) getApplicationContext();
    }

    private class MyAsyncClass extends AsyncTask<String, String, String> {
        MyAsyncClass() {
        }

        /* access modifiers changed from: protected */
        public String doInBackground(String... strArr) {
            FinderLegsActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    FinderLegsActivity.this.setupUIElements();
                    FinderLegsActivity.this.LoadPayoffChart();
                    FinderLegsActivity.this.showStrategyLegs();
                }
            });
            return "";
        }
    }

    private class AsyncLoadFolioStgLegs extends AsyncTask<String, String, String> {
        AsyncLoadFolioStgLegs() {
        }

        /* access modifiers changed from: protected */
        public String doInBackground(String... strArr) {
            FinderLegsActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    FinderLegsActivity.this.lstLegs = FinderLegsActivity.this.getGreekApplication().getStrategyLegsFromDB(FinderLegsActivity.this.stgResult);
                    String[] stgGreeksFromDB = FinderLegsActivity.this.getGreekApplication().getStgGreeksFromDB(FinderLegsActivity.this.stgResult);
                    FinderLegsActivity.this.strDelta = stgGreeksFromDB[0];
                    FinderLegsActivity.this.strGamma = stgGreeksFromDB[1];
                    FinderLegsActivity.this.strTheta = stgGreeksFromDB[2];
                    FinderLegsActivity.this.strVega = stgGreeksFromDB[3];
                    FinderLegsActivity.this.PrepareScreen();
                    FinderLegsActivity.this.WebServiceCallGetPnL();
                }
            });
            return "";
        }
    }

    private class AsyncLoadWebStgLegs extends AsyncTask<String, String, String> {
        AsyncLoadWebStgLegs() {
        }

        /* access modifiers changed from: protected */
        public String doInBackground(String... strArr) {
            FinderLegsActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    FinderLegsActivity.this.WebServiceCallGetLegs();
                }
            });
            return "";
        }
    }

    public void LoadFolioStgLegs(Context context) {
        final AlertDialog progressDialog = setProgressDialog();
        final C061117 r0 = new AsyncLoadFolioStgLegs() {
            /* access modifiers changed from: protected */
            public void onPreExecute() {
                super.onPreExecute();
            }

            /* access modifiers changed from: protected */
            public void onPostExecute(String str) {
                super.onPostExecute(str);
                progressDialog.cancel();
            }
        };
        progressDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            public void onShow(DialogInterface dialogInterface) {
                r0.execute(new String[]{""});
            }
        });
        progressDialog.show();
    }

    public void LoadWebStgLegs(Context context) {
        final AlertDialog progressDialog = setProgressDialog();
        final C061319 r0 = new AsyncLoadWebStgLegs() {
            /* access modifiers changed from: protected */
            public void onPreExecute() {
                super.onPreExecute();
            }

            /* access modifiers changed from: protected */
            public void onPostExecute(String str) {
                super.onPostExecute(str);
                progressDialog.cancel();
            }
        };
        progressDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            public void onShow(DialogInterface dialogInterface) {
                r0.execute(new String[]{""});
            }
        });
        progressDialog.show();
    }

    public void EditStrategyRefresh(Context context) {
        final AlertDialog progressDialog = setProgressDialog();
        final C061721 r0 = new MyAsyncClass() {
            /* access modifiers changed from: protected */
            public void onPreExecute() {
                super.onPreExecute();
            }

            /* access modifiers changed from: protected */
            public void onPostExecute(String str) {
                super.onPostExecute(str);
                progressDialog.cancel();
            }
        };
        progressDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            public void onShow(DialogInterface dialogInterface) {
                r0.execute(new String[]{""});
            }
        });
        progressDialog.show();
    }
}
