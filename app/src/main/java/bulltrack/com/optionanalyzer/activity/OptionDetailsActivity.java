package bulltrack.com.optionanalyzer.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.core.internal.view.SupportMenu;
import bulltrack.com.optionanalyzer.adapter.GreeksListAdapter;
import bulltrack.com.optionanalyzer.application.MyGreeksApplication;
import bulltrack.com.optionanalyzer.constants.Constants;
import bulltrack.com.optionanalyzer.dao.GreekValues;
import bulltrack.com.optiongreeks13.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class OptionDetailsActivity extends AppCompatActivity {
    /* access modifiers changed from: private */
    public Activity activity;
    boolean adClosedFlag = false;
    GreeksListAdapter adapter;
    ImageView imgChart;

    /* renamed from: li */
    List<GreekValues> f89li;
    InterstitialAd mIAd_OptDetail;
    int originator;
    ProgressBar progressBar;
    TextView tvCallPut;
    TextView tvClosePrice;
    TextView tvDaysToExpiry;
    TextView tvDelta;
    TextView tvExpiry;
    TextView tvGamma;
    TextView tvHighPrice;
    TextView tvIV;
    TextView tvLotSize;
    TextView tvLowPrice;
    TextView tvLtp;
    TextView tvOI;
    TextView tvOpenPrice;
    TextView tvPriceChg;
    TextView tvStock;
    TextView tvStockLTP;
    TextView tvStockVolat;
    TextView tvStrike;
    TextView tvTheoVal;
    TextView tvTheta;
    TextView tvVega;
    TextView tvVolume;
    String webServiceUrl = Constants.URL_SERVICE;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.activity = this;
        setContentView((int) R.layout.option_details);
        this.progressBar = (ProgressBar) findViewById(R.id.progressbar_opt_detail);
        ((AdView) findViewById(R.id.adView_opotioin_details)).loadAd(new AdRequest.Builder().setRequestAgent("android_studio:ad_template").build());
        InterstitialAd interstitialAd = new InterstitialAd(this);
        this.mIAd_OptDetail = interstitialAd;
        interstitialAd.setAdUnitId(getString(R.string.ad_unit_id_option_prices_interstitial));
        this.mIAd_OptDetail.loadAd(new AdRequest.Builder().build());
        this.tvStock = (TextView) findViewById(R.id.tv_option_details_underlyer);
        this.tvLtp = (TextView) findViewById(R.id.tv_option_details_option_ltp);
        this.tvStrike = (TextView) findViewById(R.id.tv_option_details_strike);
        this.tvCallPut = (TextView) findViewById(R.id.tv_option_details_callput);
        this.tvExpiry = (TextView) findViewById(R.id.tv_option_details_expiry);
        this.tvPriceChg = (TextView) findViewById(R.id.tv_option_details_opotion_pricechange);
        this.tvDaysToExpiry = (TextView) findViewById(R.id.tv_option_details_daystoexpiry_val);
        this.tvVolume = (TextView) findViewById(R.id.tv_option_details_vol_val);
        this.tvOI = (TextView) findViewById(R.id.tv_option_details_OI_val);
        this.tvLotSize = (TextView) findViewById(R.id.tv_option_details_lotsize_val);
        this.tvOpenPrice = (TextView) findViewById(R.id.tv_option_details_openprice_val);
        this.tvHighPrice = (TextView) findViewById(R.id.tv_option_details_highprice_val);
        this.tvLowPrice = (TextView) findViewById(R.id.tv_option_details_lowprice_val);
        this.tvClosePrice = (TextView) findViewById(R.id.tv_option_details_closeprice_val);
        this.tvIV = (TextView) findViewById(R.id.tv_option_details_option_iv_val);
        this.tvTheoVal = (TextView) findViewById(R.id.tv_option_details_optionvalue_val);
        this.tvDelta = (TextView) findViewById(R.id.tv_option_details_delta_val);
        this.tvGamma = (TextView) findViewById(R.id.tv_option_details_gamma_val);
        this.tvTheta = (TextView) findViewById(R.id.tv_option_details_theta_val);
        this.tvVega = (TextView) findViewById(R.id.tv_option_details_vega_val);
        this.tvStockLTP = (TextView) findViewById(R.id.tv_option_details_stockprice_val);
        this.tvStockVolat = (TextView) findViewById(R.id.tv_option_details_stockvolat_val);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(400);
        alphaAnimation.setStartOffset(20);
        alphaAnimation.setRepeatMode(2);
        alphaAnimation.setRepeatCount(-1);
        ((TextView) findViewById(R.id.tv_option_details_chart_txt)).startAnimation(alphaAnimation);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String string = extras.getString("stock");
            String string2 = extras.getString("expiry");
            String string3 = extras.getString("strike");
            String string4 = extras.getString("callput");
            this.originator = Integer.parseInt(extras.getString("originator"));
            new AsyncListViewLoader().execute(new String[]{"20", string, string2, string3, string3, string4});
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Button button = (Button) findViewById(R.id.btn_option_details_addtoportfolio);
        Button button2 = (Button) findViewById(R.id.btn_option_details_addtowatch);
        int i = this.originator;
        if (i == 1) {
            button2.setVisibility(8);
        } else if (i == 2) {
            button.setVisibility(8);
            button2.setVisibility(8);
        }
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                OptionDetailsActivity.this.getGreekApplication().addItemToWatch(OptionDetailsActivity.this.getApplicationContext(), OptionDetailsActivity.this.f89li.get(0));
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                OptionDetailsActivity.this.getGreekApplication().startAddItemToFolioActivity(OptionDetailsActivity.this.activity, OptionDetailsActivity.this.f89li.get(0));
            }
        });
        this.imgChart = (ImageView) findViewById(R.id.img_option_details_chart_btn);
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == R.id.action_refresh || itemId != 16908332) {
            return super.onOptionsItemSelected(menuItem);
        }
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (OptionDetailsActivity.this.mIAd_OptDetail != null && OptionDetailsActivity.this.mIAd_OptDetail.isLoaded()) {
                    OptionDetailsActivity.this.mIAd_OptDetail.show();
                    OptionDetailsActivity.this.mIAd_OptDetail.setAdListener(new AdListener() {
                        public void onAdClosed() {
                            if (OptionDetailsActivity.this.getParent() == null) {
                                OptionDetailsActivity.this.adClosedFlag = true;
                                OptionDetailsActivity.this.onBackPressed();
                                return;
                            }
                            NavUtils.navigateUpFromSameTask(OptionDetailsActivity.this.activity);
                            OptionDetailsActivity.this.finish();
                        }
                    });
                } else if (OptionDetailsActivity.this.getParent() == null) {
                    OptionDetailsActivity.this.onBackPressed();
                } else {
                    NavUtils.navigateUpFromSameTask(OptionDetailsActivity.this.activity);
                    OptionDetailsActivity.this.finish();
                }
            }
        }, 1000);
        return true;
    }

    /* access modifiers changed from: protected */
    public MyGreeksApplication getGreekApplication() {
        return (MyGreeksApplication) getApplicationContext();
    }

    private class AsyncListViewLoader extends AsyncTask<String, Void, List<GreekValues>> {
        private AsyncListViewLoader() {
        }

        /* access modifiers changed from: protected */
        /* JADX WARNING: Removed duplicated region for block: B:19:0x0179  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public List<bulltrack.com.optionanalyzer.dao.GreekValues> doInBackground(String... r17) {
            /*
                r16 = this;
                r0 = r16
                java.lang.String r1 = "Accept-Charset"
                java.lang.String r2 = "UTF-8"
                java.lang.String r3 = "yyyy-MM-dd HH:mm:ss"
                bulltrack.com.optionanalyzer.activity.OptionDetailsActivity r4 = bulltrack.com.optionanalyzer.activity.OptionDetailsActivity.this
                java.util.ArrayList r5 = new java.util.ArrayList
                r5.<init>()
                r4.f89li = r5
                r4 = 5
                r5 = 3
                r6 = 2
                r7 = 1
                bulltrack.com.optionanalyzer.activity.OptionDetailsActivity r8 = bulltrack.com.optionanalyzer.activity.OptionDetailsActivity.this     // Catch:{ IOException | Exception -> 0x0176 }
                bulltrack.com.optionanalyzer.application.MyGreeksApplication r8 = r8.getGreekApplication()     // Catch:{ IOException | Exception -> 0x0176 }
                boolean r8 = r8.isInternetAvailable()     // Catch:{ IOException | Exception -> 0x0176 }
                if (r8 == 0) goto L_0x0176
                java.net.URL r8 = new java.net.URL     // Catch:{ IOException | Exception -> 0x0176 }
                java.lang.StringBuilder r9 = new java.lang.StringBuilder     // Catch:{ IOException | Exception -> 0x0176 }
                r9.<init>()     // Catch:{ IOException | Exception -> 0x0176 }
                bulltrack.com.optionanalyzer.activity.OptionDetailsActivity r10 = bulltrack.com.optionanalyzer.activity.OptionDetailsActivity.this     // Catch:{ IOException | Exception -> 0x0176 }
                java.lang.String r10 = r10.webServiceUrl     // Catch:{ IOException | Exception -> 0x0176 }
                r9.append(r10)     // Catch:{ IOException | Exception -> 0x0176 }
                java.lang.String r10 = "9"
                r9.append(r10)     // Catch:{ IOException | Exception -> 0x0176 }
                java.lang.String r9 = r9.toString()     // Catch:{ IOException | Exception -> 0x0176 }
                r8.<init>(r9)     // Catch:{ IOException | Exception -> 0x0176 }
                java.net.URLConnection r8 = r8.openConnection()     // Catch:{ IOException | Exception -> 0x0176 }
                java.net.HttpURLConnection r8 = (java.net.HttpURLConnection) r8     // Catch:{ IOException | Exception -> 0x0176 }
                r9 = 5000(0x1388, float:7.006E-42)
                r8.setConnectTimeout(r9)     // Catch:{ IOException | Exception -> 0x0176 }
                r8.setDoOutput(r7)     // Catch:{ IOException | Exception -> 0x0176 }
                r8.setRequestProperty(r1, r2)     // Catch:{ IOException | Exception -> 0x0176 }
                java.io.InputStream r8 = r8.getInputStream()     // Catch:{ IOException | Exception -> 0x0176 }
                java.io.BufferedReader r10 = new java.io.BufferedReader     // Catch:{ IOException | Exception -> 0x0176 }
                java.io.InputStreamReader r11 = new java.io.InputStreamReader     // Catch:{ IOException | Exception -> 0x0176 }
                r11.<init>(r8, r2)     // Catch:{ IOException | Exception -> 0x0176 }
                r10.<init>(r11)     // Catch:{ IOException | Exception -> 0x0176 }
                java.lang.String r8 = r10.readLine()     // Catch:{ IOException | Exception -> 0x0176 }
                r10.close()     // Catch:{ IOException | Exception -> 0x0176 }
                org.json.JSONObject r10 = new org.json.JSONObject     // Catch:{ IOException | Exception -> 0x0176 }
                r10.<init>(r8)     // Catch:{ IOException | Exception -> 0x0176 }
                java.lang.String r8 = "priceDateTime"
                java.lang.Object r8 = r10.get(r8)     // Catch:{ IOException | Exception -> 0x0176 }
                java.lang.String r8 = r8.toString()     // Catch:{ IOException | Exception -> 0x0176 }
                java.lang.String r11 = "greekDateTime"
                java.lang.Object r10 = r10.get(r11)     // Catch:{ IOException | Exception -> 0x0176 }
                java.lang.String r10 = r10.toString()     // Catch:{ IOException | Exception -> 0x0176 }
                bulltrack.com.optionanalyzer.activity.OptionDetailsActivity r11 = bulltrack.com.optionanalyzer.activity.OptionDetailsActivity.this     // Catch:{ IOException | Exception -> 0x0176 }
                bulltrack.com.optionanalyzer.application.MyGreeksApplication r11 = r11.getGreekApplication()     // Catch:{ IOException | Exception -> 0x0176 }
                java.util.Date r11 = r11.dateFormatter((java.lang.String) r8, (java.lang.String) r3)     // Catch:{ IOException | Exception -> 0x0176 }
                long r11 = r11.getTime()     // Catch:{ IOException | Exception -> 0x0176 }
                bulltrack.com.optionanalyzer.activity.OptionDetailsActivity r13 = bulltrack.com.optionanalyzer.activity.OptionDetailsActivity.this     // Catch:{ IOException | Exception -> 0x0176 }
                bulltrack.com.optionanalyzer.application.MyGreeksApplication r13 = r13.getGreekApplication()     // Catch:{ IOException | Exception -> 0x0176 }
                java.util.Date r13 = r13.dateFormatter((java.lang.String) r10, (java.lang.String) r3)     // Catch:{ IOException | Exception -> 0x0176 }
                long r13 = r13.getTime()     // Catch:{ IOException | Exception -> 0x0176 }
                r15 = 0
                if (r8 == 0) goto L_0x00a1
                if (r10 == 0) goto L_0x00a1
                int r8 = (r11 > r13 ? 1 : (r11 == r13 ? 0 : -1))
                if (r8 != 0) goto L_0x00a1
                goto L_0x0176
            L_0x00a1:
                java.lang.String r8 = "&condition="
                r10 = r17[r7]     // Catch:{ IOException | Exception -> 0x0176 }
                r11 = r17[r6]     // Catch:{ IOException | Exception -> 0x0176 }
                java.lang.Float r12 = new java.lang.Float     // Catch:{ IOException | Exception -> 0x0176 }
                r13 = r17[r5]     // Catch:{ IOException | Exception -> 0x0176 }
                float r13 = java.lang.Float.parseFloat(r13)     // Catch:{ IOException | Exception -> 0x0176 }
                r12.<init>(r13)     // Catch:{ IOException | Exception -> 0x0176 }
                java.lang.Float r13 = new java.lang.Float     // Catch:{ IOException | Exception -> 0x0176 }
                r14 = 4
                r14 = r17[r14]     // Catch:{ IOException | Exception -> 0x0176 }
                float r14 = java.lang.Float.parseFloat(r14)     // Catch:{ IOException | Exception -> 0x0176 }
                r13.<init>(r14)     // Catch:{ IOException | Exception -> 0x0176 }
                r14 = r17[r4]     // Catch:{ IOException | Exception -> 0x0176 }
                java.net.URL r4 = new java.net.URL     // Catch:{ IOException | Exception -> 0x0176 }
                java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ IOException | Exception -> 0x0176 }
                r5.<init>()     // Catch:{ IOException | Exception -> 0x0176 }
                bulltrack.com.optionanalyzer.activity.OptionDetailsActivity r6 = bulltrack.com.optionanalyzer.activity.OptionDetailsActivity.this     // Catch:{ IOException | Exception -> 0x0176 }
                java.lang.String r6 = r6.webServiceUrl     // Catch:{ IOException | Exception -> 0x0176 }
                r5.append(r6)     // Catch:{ IOException | Exception -> 0x0176 }
                r6 = r17[r15]     // Catch:{ IOException | Exception -> 0x0176 }
                r5.append(r6)     // Catch:{ IOException | Exception -> 0x0176 }
                java.lang.String r5 = r5.toString()     // Catch:{ IOException | Exception -> 0x0176 }
                r4.<init>(r5)     // Catch:{ IOException | Exception -> 0x0176 }
                java.net.URLConnection r4 = r4.openConnection()     // Catch:{ IOException | Exception -> 0x0176 }
                java.net.HttpURLConnection r4 = (java.net.HttpURLConnection) r4     // Catch:{ IOException | Exception -> 0x0176 }
                r4.setConnectTimeout(r9)     // Catch:{ IOException | Exception -> 0x0176 }
                r4.setDoInput(r7)     // Catch:{ IOException | Exception -> 0x0176 }
                r4.setDoOutput(r7)     // Catch:{ IOException | Exception -> 0x0176 }
                java.lang.String r5 = "POST"
                r4.setRequestMethod(r5)     // Catch:{ IOException | Exception -> 0x0176 }
                r4.setRequestProperty(r1, r2)     // Catch:{ IOException | Exception -> 0x0176 }
                java.lang.String r1 = "Content-Type"
                java.lang.String r5 = "application/x-www-form-urlencoded"
                r4.setRequestProperty(r1, r5)     // Catch:{ IOException | Exception -> 0x0176 }
                java.lang.String r1 = "Accept"
                java.lang.String r5 = "application/json"
                r4.setRequestProperty(r1, r5)     // Catch:{ IOException | Exception -> 0x0176 }
                java.io.DataOutputStream r1 = new java.io.DataOutputStream     // Catch:{ IOException | Exception -> 0x0176 }
                java.io.OutputStream r5 = r4.getOutputStream()     // Catch:{ IOException | Exception -> 0x0176 }
                r1.<init>(r5)     // Catch:{ IOException | Exception -> 0x0176 }
                r1.writeBytes(r8)     // Catch:{ IOException | Exception -> 0x0176 }
                org.json.JSONObject r5 = new org.json.JSONObject     // Catch:{ IOException | Exception -> 0x0176 }
                r5.<init>()     // Catch:{ IOException | Exception -> 0x0176 }
                java.lang.String r6 = "callOrPut"
                r5.put(r6, r14)     // Catch:{ IOException | Exception -> 0x0176 }
                java.lang.String r6 = "expiryDate"
                r5.put(r6, r11)     // Catch:{ IOException | Exception -> 0x0176 }
                java.lang.String r6 = "stock"
                r5.put(r6, r10)     // Catch:{ IOException | Exception -> 0x0176 }
                java.lang.String r6 = "strikeFrom"
                r5.put(r6, r12)     // Catch:{ IOException | Exception -> 0x0176 }
                java.lang.String r6 = "strikeTo"
                r5.put(r6, r13)     // Catch:{ IOException | Exception -> 0x0176 }
                java.lang.String r5 = r5.toString()     // Catch:{ IOException | Exception -> 0x0176 }
                r1.writeBytes(r5)     // Catch:{ IOException | Exception -> 0x0176 }
                r1.flush()     // Catch:{ IOException | Exception -> 0x0176 }
                r1.close()     // Catch:{ IOException | Exception -> 0x0176 }
                java.io.InputStream r1 = r4.getInputStream()     // Catch:{ IOException | Exception -> 0x0176 }
                java.io.BufferedReader r5 = new java.io.BufferedReader     // Catch:{ IOException | Exception -> 0x0176 }
                java.io.InputStreamReader r6 = new java.io.InputStreamReader     // Catch:{ IOException | Exception -> 0x0176 }
                r6.<init>(r1, r2)     // Catch:{ IOException | Exception -> 0x0176 }
                r5.<init>(r6)     // Catch:{ IOException | Exception -> 0x0176 }
                java.lang.String r2 = r5.readLine()     // Catch:{ IOException | Exception -> 0x0176 }
                bulltrack.com.optionanalyzer.activity.OptionDetailsActivity$AsyncListViewLoader$1 r6 = new bulltrack.com.optionanalyzer.activity.OptionDetailsActivity$AsyncListViewLoader$1     // Catch:{ IOException | Exception -> 0x0176 }
                r6.<init>()     // Catch:{ IOException | Exception -> 0x0176 }
                java.lang.reflect.Type r6 = r6.getType()     // Catch:{ IOException | Exception -> 0x0176 }
                com.google.gson.GsonBuilder r8 = new com.google.gson.GsonBuilder     // Catch:{ IOException | Exception -> 0x0176 }
                r8.<init>()     // Catch:{ IOException | Exception -> 0x0176 }
                com.google.gson.GsonBuilder r3 = r8.setDateFormat((java.lang.String) r3)     // Catch:{ IOException | Exception -> 0x0176 }
                com.google.gson.Gson r3 = r3.create()     // Catch:{ IOException | Exception -> 0x0176 }
                bulltrack.com.optionanalyzer.activity.OptionDetailsActivity r8 = bulltrack.com.optionanalyzer.activity.OptionDetailsActivity.this     // Catch:{ IOException | Exception -> 0x0176 }
                java.lang.Object r2 = r3.fromJson((java.lang.String) r2, (java.lang.reflect.Type) r6)     // Catch:{ IOException | Exception -> 0x0176 }
                java.util.List r2 = (java.util.List) r2     // Catch:{ IOException | Exception -> 0x0176 }
                r8.f89li = r2     // Catch:{ IOException | Exception -> 0x0176 }
                if (r1 == 0) goto L_0x016d
                r1.close()     // Catch:{ IOException | Exception -> 0x0176 }
            L_0x016d:
                r5.close()     // Catch:{ IOException | Exception -> 0x0176 }
                if (r4 == 0) goto L_0x0177
                r4.disconnect()     // Catch:{ IOException | Exception -> 0x0176 }
                goto L_0x0177
            L_0x0176:
                r15 = 1
            L_0x0177:
                if (r15 == 0) goto L_0x0194
                bulltrack.com.optionanalyzer.activity.OptionDetailsActivity r1 = bulltrack.com.optionanalyzer.activity.OptionDetailsActivity.this
                bulltrack.com.optionanalyzer.application.MyGreeksApplication r2 = r1.getGreekApplication()
                r3 = r17[r7]
                r4 = 2
                r4 = r17[r4]
                r5 = 3
                r5 = r17[r5]
                float r5 = java.lang.Float.parseFloat(r5)
                r6 = 5
                r6 = r17[r6]
                java.util.List r2 = r2.getGreekForAnOption(r3, r4, r5, r6)
                r1.f89li = r2
            L_0x0194:
                bulltrack.com.optionanalyzer.activity.OptionDetailsActivity r1 = bulltrack.com.optionanalyzer.activity.OptionDetailsActivity.this
                java.util.List<bulltrack.com.optionanalyzer.dao.GreekValues> r1 = r1.f89li
                return r1
            */
            throw new UnsupportedOperationException("Method not decompiled: bulltrack.com.optionanalyzer.activity.OptionDetailsActivity.AsyncListViewLoader.doInBackground(java.lang.String[]):java.util.List");
        }

        /* access modifiers changed from: protected */
        public void onPreExecute() {
            super.onPreExecute();
            OptionDetailsActivity.this.progressBar.setVisibility(0);
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(List<GreekValues> list) {
            super.onPostExecute(list);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.DT_FMT_dd_MMM_yyyy);
            DecimalFormat decimalFormat = new DecimalFormat("###,###");
            DecimalFormat decimalFormat2 = new DecimalFormat("0.00");
            new DecimalFormat("0.0000");
            if (list.size() == 0) {
                OptionDetailsActivity.this.setContentView((int) R.layout.option_details_empty);
                OptionDetailsActivity.this.progressBar.setVisibility(4);
                return;
            }
            final GreekValues greekValues = list.get(0);
            OptionDetailsActivity.this.imgChart.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    OptionDetailsActivity.this.getGreekApplication().startChartActivityForOption((Context) OptionDetailsActivity.this.activity, greekValues);
                }
            });
            OptionDetailsActivity.this.tvStock.setText(greekValues.getSymbol());
            OptionDetailsActivity.this.tvLtp.setText(decimalFormat2.format((double) greekValues.getLastPrice()));
            TextView textView = OptionDetailsActivity.this.tvStrike;
            textView.setText(greekValues.getStrike() + "");
            if (greekValues.getCallPut().equalsIgnoreCase("C")) {
                OptionDetailsActivity.this.tvCallPut.setText("Call");
            } else {
                OptionDetailsActivity.this.tvCallPut.setText("Put");
            }
            OptionDetailsActivity.this.tvExpiry.setText(simpleDateFormat.format(greekValues.getExpiry_d()));
            if (((double) greekValues.getChange()) > 0.0d) {
                TextView textView2 = OptionDetailsActivity.this.tvPriceChg;
                textView2.setText("+" + decimalFormat2.format((double) greekValues.getChange()) + " | " + decimalFormat2.format((double) greekValues.getpChange()) + "%");
                OptionDetailsActivity.this.tvPriceChg.setTextColor(Color.parseColor("#2E8B57"));
                OptionDetailsActivity.this.tvLtp.setTextColor(Color.parseColor("#2E8B57"));
            } else {
                TextView textView3 = OptionDetailsActivity.this.tvPriceChg;
                textView3.setText(greekValues.getChange() + " | " + greekValues.getpChange() + "%");
                OptionDetailsActivity.this.tvPriceChg.setTextColor(SupportMenu.CATEGORY_MASK);
                OptionDetailsActivity.this.tvLtp.setTextColor(SupportMenu.CATEGORY_MASK);
            }
            long DateDifferenceInDays = OptionDetailsActivity.this.getGreekApplication().DateDifferenceInDays(greekValues.getExpiry_d(), greekValues.getPrice_upd_d());
            TextView textView4 = OptionDetailsActivity.this.tvDaysToExpiry;
            textView4.setText((DateDifferenceInDays + 1) + " days");
            TextView textView5 = OptionDetailsActivity.this.tvLotSize;
            textView5.setText(greekValues.getMarketLot() + "");
            OptionDetailsActivity.this.tvVolume.setText(decimalFormat.format((long) greekValues.getNoOfCntr()));
            if (greekValues.getOIChange() >= 0) {
                TextView textView6 = OptionDetailsActivity.this.tvOI;
                textView6.setText(decimalFormat.format((long) greekValues.getOI()) + " [+" + decimalFormat.format((long) greekValues.getOIChange()) + "]");
            } else {
                TextView textView7 = OptionDetailsActivity.this.tvOI;
                textView7.setText(decimalFormat.format((long) greekValues.getOI()) + " [" + decimalFormat.format((long) greekValues.getOIChange()) + "]");
            }
            OptionDetailsActivity.this.tvOpenPrice.setText(decimalFormat2.format((double) greekValues.getOpenPrice()));
            OptionDetailsActivity.this.tvHighPrice.setText(decimalFormat2.format((double) greekValues.getHighPrice()));
            OptionDetailsActivity.this.tvLowPrice.setText(decimalFormat2.format((double) greekValues.getLowPrice()));
            if (greekValues.getClosePrice() == 0.0f) {
                OptionDetailsActivity.this.tvClosePrice.setText("-");
            } else {
                OptionDetailsActivity.this.tvClosePrice.setText(decimalFormat2.format((double) greekValues.getClosePrice()));
            }
            OptionDetailsActivity.this.tvIV.setText(decimalFormat2.format((double) greekValues.getImpliedVolatility()));
            OptionDetailsActivity.this.tvTheoVal.setText(decimalFormat2.format((double) greekValues.getTheoValue()));
            OptionDetailsActivity.this.tvDelta.setText(decimalFormat2.format((double) greekValues.getDelta()));
            OptionDetailsActivity.this.tvGamma.setText(decimalFormat2.format((double) greekValues.getGamma()));
            OptionDetailsActivity.this.tvTheta.setText(decimalFormat2.format((double) greekValues.getTheta()));
            OptionDetailsActivity.this.tvVega.setText(decimalFormat2.format((double) greekValues.getVega()));
            OptionDetailsActivity.this.tvStockLTP.setText(decimalFormat2.format((double) greekValues.getUnderlyingValue()));
            OptionDetailsActivity.this.tvStockVolat.setText(decimalFormat2.format((double) greekValues.getAnnualisedVolatility()));
            OptionDetailsActivity.this.progressBar.setVisibility(4);
        }
    }
}
