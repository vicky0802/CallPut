package bulltrack.com.optionanalyzer.application;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Handler;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Base64;
import android.widget.Toast;
import bulltrack.com.optionanalyzer.activity.AddPortfolioActivity;
import bulltrack.com.optionanalyzer.activity.CalculatorActivity;
import bulltrack.com.optionanalyzer.activity.ChartEodActivity;
import bulltrack.com.optionanalyzer.activity.MarginDetailActivity;
import bulltrack.com.optionanalyzer.activity.OptionDetailsActivity;
import bulltrack.com.optionanalyzer.activity.PCRDetailActivity;
import bulltrack.com.optionanalyzer.constants.Constants;
import bulltrack.com.optionanalyzer.dao.Calls;
import bulltrack.com.optionanalyzer.dao.GreekSearchCriteriaFields;
import bulltrack.com.optionanalyzer.dao.GreekValues;
import bulltrack.com.optionanalyzer.dao.MyCalls;
import bulltrack.com.optionanalyzer.dao.OIData;
import bulltrack.com.optionanalyzer.dao.OptionID;
import bulltrack.com.optionanalyzer.dao.OptionMargin;
import bulltrack.com.optionanalyzer.dao.PartyRec;
import bulltrack.com.optionanalyzer.dao.PcrVal;
import bulltrack.com.optionanalyzer.dao.StrategyLegsFilter;
import bulltrack.com.optionanalyzer.dao.StrategyResultsFilter;
import bulltrack.com.optionanalyzer.dao.StrikeExpiry;
import bulltrack.com.optionanalyzer.p005db.DBHelper;
import bulltrack.com.optiongreeks13.R;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import org.json.JSONObject;

public class MyGreeksApplication extends Application {
    private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";
    public static final String PBKDF2_DERIVATION_ALGORITHM = "PBKDF2WithHmacSHA1";
    private static final int PKCS5_SALT_LENGTH = 8;
    public static final String TAG = MyGreeksApplication.class.getSimpleName();
    private static MyGreeksApplication mInstance;
    private static SecureRandom random = new SecureRandom();
    Context appContext = null;

    /* renamed from: db */
    DBHelper f109db;
    InterstitialAd mIAd_AddMarginDetails;
    InterstitialAd mIAd_Addfolio;
    private RequestQueue mRequestQueue;

    public native String getBList();

    public native String getGold();

    public native String getPass();

    public int getPayoffDiagram(int i) {
        if (i == 117) {
            return R.drawable.long_put_condor;
        }
        if (i == 120) {
            return R.drawable.long_call_butterfly;
        }
        if (i == 201) {
            return R.drawable.bear_put_spread;
        }
        if (i == 122) {
            return R.drawable.long_put_butterfly;
        }
        if (i == 123) {
            return R.drawable.long_call_condor;
        }
        switch (i) {
            case 101:
            case 106:
                return R.drawable.bull_call_spread;
            case 102:
                return R.drawable.long_iron_butterfly;
            case 103:
                return R.drawable.long_iron_condor;
            case 104:
                return R.drawable.long_straddle;
            case 105:
                return R.drawable.long_strangle;
            case 107:
                return R.drawable.bull_put_spread;
            case 108:
                return R.drawable.bear_call_spread;
            case 109:
                return R.drawable.bear_put_spread;
            case 110:
                return R.drawable.bull_put_ladder;
            case 111:
                return R.drawable.bear_call_ladder;
            default:
                return R.drawable.default_small;
        }
    }

    static {
        System.loadLibrary("keys");
    }

    public void onCreate() {
        super.onCreate();
        mInstance = this;
        this.appContext = getApplicationContext();
        InterstitialAd interstitialAd = new InterstitialAd(this);
        this.mIAd_Addfolio = interstitialAd;
        interstitialAd.setAdUnitId(getString(R.string.ad_unit_id_add_portfolio_inserstitial));
        this.mIAd_Addfolio.loadAd(new AdRequest.Builder().build());
        InterstitialAd interstitialAd2 = new InterstitialAd(this);
        this.mIAd_AddMarginDetails = interstitialAd2;
        interstitialAd2.setAdUnitId(getString(R.string.ad_unit_id_fno_margin_main_interstitial));
        this.mIAd_AddMarginDetails.loadAd(new AdRequest.Builder().build());
    }

    public static synchronized MyGreeksApplication getInstance() {
        MyGreeksApplication myGreeksApplication;
        synchronized (MyGreeksApplication.class) {
            myGreeksApplication = mInstance;
        }
        return myGreeksApplication;
    }

    public RequestQueue getRequestQueue() {
        if (this.mRequestQueue == null) {
            this.mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return this.mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> request, String str) {
        if (TextUtils.isEmpty(str)) {
            str = TAG;
        }
        request.setTag(str);
        request.setShouldCache(false);
        request.setRetryPolicy(new DefaultRetryPolicy(Constants.VOLLEY_RESPONSE_TIMEOUT, 0, 1.0f));
        getRequestQueue().add(request);
    }

    public <T> void addToRequestQueue(Request<T> request) {
        request.setTag(TAG);
        getRequestQueue().add(request);
    }

    public void cancelPendingRequests(Object obj) {
        RequestQueue requestQueue = this.mRequestQueue;
        if (requestQueue != null) {
            requestQueue.cancelAll(obj);
        }
    }

    private DBHelper getDBHandler() {
        if (this.f109db == null) {
            DBHelper dBHelper = new DBHelper(this);
            this.f109db = dBHelper;
            dBHelper.getWritableDatabase();
        }
        return this.f109db;
    }

    public void initDatabase() {
        getDBHandler();
    }

    public int deleteAllGreeks(String str) {
        return getDBHandler().deleteAllGreeks(str);
    }

    public boolean insertAllGreeks(List<GreekValues> list, String str) {
        return getDBHandler().insertAllGreeks(list, str);
    }

    public List<GreekValues> getAllGreeks(String str) {
        return getDBHandler().getAllGreeks(str);
    }

    public List getGreekForAnOption(String str, String str2, float f, String str3) {
        return getDBHandler().getGreekForAnOption(str, str2, f, str3);
    }

    public String getGreeksUpdateDate(String str) {
        return getDBHandler().getGreeksUpdateDate(str);
    }

    public String getPartyCurrentUpdateDate() {
        return getDBHandler().getPartyCurrentUpdateDate();
    }

    public String getDayWiseUpdateDate() {
        return getDBHandler().getDayWiseUpdateDate();
    }

    public String getOIStockUpdateDate() {
        return getDBHandler().getOIStockUpdateDate();
    }

    public String getBrokerCallsUpdateDate() {
        return getDBHandler().getBrokerCallsUpdateDate();
    }

    public int deletePartyCurrent() {
        return getDBHandler().deletePartyCurrent();
    }

    public int deletePartyDayWise() {
        return getDBHandler().deletePartyDayWise();
    }

    public int deleteOIStock() {
        return getDBHandler().deleteOIStock();
    }

    public int deleteBrokerCalls() {
        return getDBHandler().deleteBrokerCalls();
    }

    public boolean insertPartyCurrent(List<PartyRec> list) {
        return getDBHandler().insertPartyCurrent(list);
    }

    public boolean insertPartyDayWsie(List<PartyRec> list) {
        return getDBHandler().insertPartyDayWise(list);
    }

    public boolean insertOIStock(List<OIData> list) {
        return getDBHandler().insertOIStocks(list);
    }

    public boolean insertBrokerCalls(List<Calls> list) {
        return getDBHandler().insertBrokerCalls(list);
    }

    public List<PartyRec> getPartyCurrentOI(String str) {
        return getDBHandler().getPartyCurrentOI(str);
    }

    public List<PartyRec> getPartyDayWiseOI(String str, String str2) {
        return getDBHandler().getPartyDayWiseOI(str, str2);
    }

    public List<OIData> getOIDataForAllStocks() {
        return getDBHandler().getOIDataForAllStocks();
    }

    public List<Calls> getBrokerCallsData() {
        return getDBHandler().getBrokerCallsData();
    }

    public List<String> getPartyNamesFromCurrent() {
        return getDBHandler().getPartyNamesFromCurrent();
    }

    public List<String> getIntrumentNamesFromCurrent() {
        return getDBHandler().getIntrumentNamesFromCurrent();
    }

    public List<String> getPartyNamesFromDayWise() {
        return getDBHandler().getPartyNamesFromDayWise();
    }

    public List<String> getIntrumentNamesFromDayWise() {
        return getDBHandler().getIntrumentNamesFromDayWise();
    }

    public String getPriceUpdateDate(String str) {
        return getDBHandler().getPriceUpdateDate(str);
    }

    public int deleteAllStrikesAndExpiries() {
        return getDBHandler().deleteAllStrikesAndExpiries();
    }

    public boolean insertAllStrikesAndExpiries(List<StrikeExpiry> list) {
        return getDBHandler().insertAllStrikesAndExpiries(list);
    }

    public List<String> getAllExpiries(String str) {
        return getDBHandler().getAllExpiries(str);
    }

    public List<String> getAllStrikes(String str, String str2) {
        return getDBHandler().getAllStrikes(str, str2);
    }

    public String getStrikesAndExpiriesUpdDate() {
        return getDBHandler().getStrikesAndExpiriesUpdDate();
    }

    public List<String> getAllStockNames() {
        return getDBHandler().getAllStockNames();
    }

    public boolean isOptionAlreadyInPortfolio(String str, Date date, float f, String str2) {
        return getDBHandler().isOptionAlreadyInPortfolio(str, date, f, str2);
    }

    public boolean insertPortfolioItem(GreekValues greekValues) {
        return getDBHandler().insertPortfolioItem(greekValues);
    }

    public List<GreekValues> getAllPortfolioItems(int i) {
        return getDBHandler().getPortfolioItems(i);
    }

    public boolean insertWatchItem(GreekValues greekValues) {
        return getDBHandler().insertWatchItem(greekValues);
    }

    public long DateDifferenceInDays(Date date, Date date2) {
        return TimeUnit.DAYS.convert(Math.abs(date.getTime() - date2.getTime()), TimeUnit.MILLISECONDS);
    }

    public long DateDifferenceInHours(Date date, Date date2) {
        return TimeUnit.HOURS.convert(Math.abs(date.getTime() - date2.getTime()), TimeUnit.MILLISECONDS);
    }

    public Date DateWithoutTime(Date date) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        instance.set(11, 0);
        instance.set(12, 0);
        instance.set(13, 0);
        instance.set(14, 0);
        return instance.getTime();
    }

    public long DateDifferenceInMins(Date date, Date date2) {
        return TimeUnit.MINUTES.convert(Math.abs(date.getTime() - date2.getTime()), TimeUnit.MILLISECONDS);
    }

    public List<GreekValues> getAllWatches() {
        return getDBHandler().getAllWatches();
    }

    public int insertAllWatches(List<GreekValues> list) {
        return getDBHandler().insertAllWatchItem(list);
    }

    public int deleteAllWatches() {
        return getDBHandler().deleteAllWatches();
    }

    public int deleteWatchItem(GreekValues greekValues) {
        return getDBHandler().deleteWatchItem(greekValues);
    }

    public int deleteFolioItemFromOpenPos(GreekValues greekValues) {
        return getDBHandler().deleteFolioItemFromOpenPos(greekValues);
    }

    public int deleteFolioItemFromClosedPos(GreekValues greekValues) {
        return getDBHandler().deleteFolioItemFromClosedPos(greekValues);
    }

    public boolean closeFolioPoistion(GreekValues greekValues) {
        return getDBHandler().closeFolioPoistion(greekValues);
    }

    public boolean isOptionAlreadyInWatch(String str, Date date, float f, String str2) {
        return getDBHandler().isOptionAlreadyInWatch(str, date, f, str2);
    }

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public boolean updateFolioPrices(List<GreekSearchCriteriaFields> list) {
        return getDBHandler().updateFolioPrices(list);
    }

    public String getAppInstallId() {
        return getDBHandler().getAppId();
    }

    public void deleteBlist() {
        getDBHandler().deleteBlistFlags();
    }

    public void setBlistFlag(String str) {
        getDBHandler().deleteBlistFlags();
        getDBHandler().setBlistFlags(str);
    }

    public void setRewardCheckFlag(String str) {
        getDBHandler().setRewardCheckFlag(str);
    }

    public void setRewardTCCheckFlag(String str) {
        getDBHandler().setRewardTCCheckFlag(str);
    }

    public String getRewardFlag() {
        return getDBHandler().getRewardCheckFlag();
    }

    public String getRewardTCFlag() {
        return getDBHandler().getRewardTCCheckFlag();
    }

    public void deleteRewardFlags() {
        getDBHandler().deleteRewardFlags();
    }

    public int getBlistRecordCount() {
        return getDBHandler().countBlistRecs();
    }

    public int getRewardRecordCount() {
        return getDBHandler().countRewardRecs();
    }

    public void setBlistFlagsForDebugging() {
        getDBHandler().setBlistFlagsForDebugging();
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v3, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v4, resolved type: java.lang.String} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public String[] getBlistFlags() {
        /*
            r9 = this;
            r0 = 2
            java.lang.String[] r1 = new java.lang.String[r0]
            bulltrack.com.optionanalyzer.db.DBHelper r2 = r9.getDBHandler()
            java.lang.String[] r2 = r2.getBlistFlags()
            r3 = 0
            r4 = r2[r3]
            r5 = 0
            if (r4 == 0) goto L_0x005e
            r4 = 1
            r6 = r2[r4]
            if (r6 == 0) goto L_0x005e
            r6 = r2[r0]
            if (r6 != 0) goto L_0x001b
            goto L_0x005e
        L_0x001b:
            r6 = r2[r3]
            byte[] r6 = android.util.Base64.decode(r6, r3)
            r7 = r2[r4]
            byte[] r7 = android.util.Base64.decode(r7, r3)
            r0 = r2[r0]
            byte[] r0 = android.util.Base64.decode(r0, r3)
            java.lang.String r0 = r9.doDecryption(r6, r7, r0)
            if (r0 != 0) goto L_0x0034
            return r5
        L_0x0034:
            java.util.StringTokenizer r2 = new java.util.StringTokenizer
            java.lang.String r6 = ":"
            r2.<init>(r0, r6)
            boolean r0 = r2.hasMoreElements()
            if (r0 == 0) goto L_0x0058
            java.lang.Object r0 = r2.nextElement()
            java.lang.String r0 = (java.lang.String) r0
            boolean r6 = r2.hasMoreElements()
            if (r6 == 0) goto L_0x0054
            java.lang.Object r2 = r2.nextElement()
            r5 = r2
            java.lang.String r5 = (java.lang.String) r5
        L_0x0054:
            r8 = r5
            r5 = r0
            r0 = r8
            goto L_0x0059
        L_0x0058:
            r0 = r5
        L_0x0059:
            r1[r3] = r5
            r1[r4] = r0
            return r1
        L_0x005e:
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: bulltrack.com.optionanalyzer.application.MyGreeksApplication.getBlistFlags():java.lang.String[]");
    }

    public void startViewOptionDetailsActivity(Context context, GreekValues greekValues, int i) {
        Intent intent = new Intent(context, OptionDetailsActivity.class);
        intent.addFlags(268435456);
        intent.putExtra("stock", greekValues.getSymbol());
        intent.putExtra("expiry", greekValues.dateFormatter(greekValues.getExpiry_d(), Constants.DT_FMT_yyyy_MM_dd_HH_m_ss));
        intent.putExtra("strike", greekValues.getStrike() + "");
        intent.putExtra("callput", greekValues.getCallPut());
        intent.putExtra("originator", i + "");
        startActivity(intent);
    }

    public void startViewMarginDetailsActivity(final Context context, final OptionMargin optionMargin) {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (MyGreeksApplication.this.mIAd_AddMarginDetails.isLoaded()) {
                    MyGreeksApplication.this.mIAd_AddMarginDetails.show();
                    MyGreeksApplication.this.mIAd_AddMarginDetails.setAdListener(new AdListener() {
                        public void onAdClosed() {
                            Intent intent = new Intent(context, MarginDetailActivity.class);
                            intent.addFlags(268435456);
                            Gson create = new GsonBuilder().setDateFormat(Constants.DT_FMT_yyyy_MM_dd_HH_m_ss).create();
                            ArrayList arrayList = new ArrayList();
                            arrayList.add(optionMargin);
                            intent.putExtra("marginObect", create.toJson((Object) arrayList));
                            MyGreeksApplication.this.startActivity(intent);
                        }
                    });
                    return;
                }
                Intent intent = new Intent(context, MarginDetailActivity.class);
                intent.addFlags(268435456);
                Gson create = new GsonBuilder().setDateFormat(Constants.DT_FMT_yyyy_MM_dd_HH_m_ss).create();
                ArrayList arrayList = new ArrayList();
                arrayList.add(optionMargin);
                intent.putExtra("marginObect", create.toJson((Object) arrayList));
                MyGreeksApplication.this.startActivity(intent);
            }
        }, 1000);
    }

    public void startViewPCRStockActivity(Context context, PcrVal pcrVal) {
        Intent intent = new Intent(context, PCRDetailActivity.class);
        intent.addFlags(268435456);
        intent.putExtra("symbol", pcrVal.getSymbol().trim());
        startActivity(intent);
    }

    public void startChartActivityForOption(Context context, GreekValues greekValues) {
        Intent intent = new Intent(context, ChartEodActivity.class);
        intent.addFlags(268435456);
        intent.putExtra("stock", greekValues.getSymbol());
        intent.putExtra("expiry", greekValues.dateFormatter(greekValues.getExpiry_d(), Constants.DT_FMT_yyyy_MM_dd_HH_m_ss));
        intent.putExtra("strike", greekValues.getStrike() + "");
        intent.putExtra("callput", greekValues.getCallPut());
        startActivity(intent);
    }

    public void startChartActivityForOption(Context context, OptionID optionID) {
        Intent intent = new Intent(context, ChartEodActivity.class);
        intent.addFlags(268435456);
        intent.putExtra("stock", optionID.getSymbol());
        intent.putExtra("expiry", dateFormatter(optionID.getExpiry(), Constants.DT_FMT_yyyy_MM_dd_HH_m_ss));
        intent.putExtra("strike", optionID.getStrike() + "");
        intent.putExtra("callput", optionID.getPutOrCall());
        startActivity(intent);
    }

    public void startAddItemToFolioActivity(final Context context, final GreekValues greekValues) {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (MyGreeksApplication.this.mIAd_Addfolio.isLoaded()) {
                    MyGreeksApplication.this.mIAd_Addfolio.show();
                    MyGreeksApplication.this.mIAd_Addfolio.setAdListener(new AdListener() {
                        public void onAdClosed() {
                            Intent intent = new Intent(context, AddPortfolioActivity.class);
                            intent.addFlags(268435456);
                            Gson create = new GsonBuilder().setDateFormat(Constants.DT_FMT_yyyy_MM_dd_HH_m_ss).create();
                            ArrayList arrayList = new ArrayList();
                            arrayList.add(greekValues);
                            intent.putExtra("greekObect", create.toJson((Object) arrayList));
                            MyGreeksApplication.this.startActivity(intent);
                        }
                    });
                    return;
                }
                Intent intent = new Intent(context, AddPortfolioActivity.class);
                intent.addFlags(268435456);
                Gson create = new GsonBuilder().setDateFormat(Constants.DT_FMT_yyyy_MM_dd_HH_m_ss).create();
                ArrayList arrayList = new ArrayList();
                arrayList.add(greekValues);
                intent.putExtra("greekObect", create.toJson((Object) arrayList));
                MyGreeksApplication.this.startActivity(intent);
            }
        }, 1000);
    }

    public void reCalculateGreeks(Context context, GreekValues greekValues) {
        long DateDifferenceInDays = DateDifferenceInDays(greekValues.getExpiry_d(), greekValues.getPrice_upd_d());
        Intent intent = new Intent(context, CalculatorActivity.class);
        intent.addFlags(268435456);
        intent.putExtra("stockPrice", greekValues.getUnderlyingValue());
        intent.putExtra("strikePrice", greekValues.getStrike());
        intent.putExtra("days2Expiry", DateDifferenceInDays);
        intent.putExtra("interestRate", 10.0f);
        intent.putExtra("anuualVolatility", greekValues.getAnnualisedVolatility());
        intent.putExtra("callPut", greekValues.getCallPut());
        startActivity(intent);
    }

    public void addItemToWatch(Context context, GreekValues greekValues) {
        if (isOptionAlreadyInWatch(greekValues.getSymbol(), greekValues.getExpiry_d(), greekValues.getStrike(), greekValues.getCallPut())) {
            Toast.makeText(context, "Watch already present", 1).show();
        } else if (insertWatchItem(greekValues)) {
            Toast.makeText(context, "Watch Added", 1).show();
        } else {
            Toast.makeText(context, "Watch could not be added", 1).show();
        }
    }

    public static float roundTo05(float f) {
        double d = (double) f;
        Double.isNaN(d);
        double round = (double) Math.round(d * 20.0d);
        Double.isNaN(round);
        return (float) (round / 20.0d);
    }

    public String dateFormatter(Date date, String str) {
        if (date == null || str == null) {
            return null;
        }
        return new SimpleDateFormat(str).format(date);
    }

    public Date dateFormatter(String str, String str2) {
        if (str == null || str.trim().equals("") || str2 == null) {
            return null;
        }
        try {
            return new SimpleDateFormat(str2).parse(str);
        } catch (ParseException unused) {
            return null;
        }
    }

    public boolean isInternetAvailable() {
        try {
            if (InetAddress.getByName("google.com").equals("")) {
                return false;
            }
            return true;
        } catch (Exception unused) {
            return false;
        }
    }

    public double theoValue(String str, double d, double d2, double d3, double d4, double d5) {
        double d6 = d4;
        double log = (Math.log(d / d2) + ((((d5 * d5) / 2.0d) + d6) * d3)) / (Math.sqrt(d3) * d5);
        double sqrt = log - (Math.sqrt(d3) * d5);
        if (str.trim().equalsIgnoreCase("C")) {
            return (CND(log) * d) - ((Math.exp((-d6) * d3) * d2) * CND(sqrt));
        }
        return ((Math.exp((-d6) * d3) * d2) * CND(-sqrt)) - (CND(-log) * d);
    }

    public double CND(double d) {
        double abs = Math.abs(d);
        double d2 = 1.0d / ((0.2316419d * abs) + 1.0d);
        double sqrt = 1.0d - (((1.0d / Math.sqrt(6.283185307179586d)) * Math.exp(((-abs) * abs) / 2.0d)) * (((((0.31938153d * d2) + ((-0.356563782d * d2) * d2)) + (Math.pow(d2, 3.0d) * 1.781477937d)) + (Math.pow(d2, 4.0d) * -1.821255978d)) + (Math.pow(d2, 5.0d) * 1.330274429d)));
        return d < 0.0d ? 1.0d - sqrt : sqrt;
    }

    public double callDelta(double d, double d2, double d3, double d4, double d5) {
        return CND((Math.log(d / d2) + ((d4 + ((0.5d * d5) * d5)) * d3)) / (d5 * Math.sqrt(d3)));
    }

    public double putDelta(double d, double d2, double d3, double d4, double d5) {
        return (1.0d - callDelta(d, d2, d3, d4, d5)) * -1.0d;
    }

    public double optionGamma(double d, double d2, double d3, double d4, double d5) {
        double log = (Math.log(d / d2) + ((d4 + ((0.5d * d5) * d5)) * d3)) / (Math.sqrt(d3) * d5);
        return (Math.exp(((log * log) * -1.0d) / 2.0d) * (1.0d / Math.sqrt(6.283185307179586d))) / ((d * d5) * Math.sqrt(d3));
    }

    public double optionVega(double d, double d2, double d3, double d4, double d5) {
        double log = (Math.log(d / d2) + ((d4 + ((0.5d * d5) * d5)) * d3)) / (d5 * Math.sqrt(d3));
        return d * Math.sqrt(d3) * (Math.exp(((log * log) * -1.0d) / 2.0d) / Math.sqrt(6.283185307179586d)) * 0.01d;
    }

    public double callTheta(double d, double d2, double d3, double d4, double d5) {
        double d6 = d4;
        double log = (Math.log(d / d2) + ((((0.5d * d5) * d5) + d6) * d3)) / (Math.sqrt(d3) * d5);
        double sqrt = log - (Math.sqrt(d3) * d5);
        double exp = ((((Math.exp(((log * log) * -1.0d) / 2.0d) * (1.0d / Math.sqrt(6.283185307179586d))) * d) * d5) * -1.0d) / (Math.sqrt(d3) * 2.0d);
        double exp2 = d6 * d2 * Math.exp((-d6) * d3);
        return (exp - (exp2 * CND(sqrt))) * 0.0027397260273972603d;
    }

    public double putTheta(double d, double d2, double d3, double d4, double d5) {
        double d6 = d4;
        double log = (Math.log(d / d2) + ((((0.5d * d5) * d5) + d6) * d3)) / (Math.sqrt(d3) * d5);
        double sqrt = log - (Math.sqrt(d3) * d5);
        double exp = ((((Math.exp(((log * log) * -1.0d) / 2.0d) * (1.0d / Math.sqrt(6.283185307179586d))) * d) * d5) * -1.0d) / (Math.sqrt(d3) * 2.0d);
        double exp2 = d6 * d2 * Math.exp((-d6) * d3);
        return (exp + (exp2 * (1.0d - CND(sqrt)))) * 0.0027397260273972603d;
    }

    public static SecretKey deriveKeyPbkdf2(byte[] bArr, String str) {
        try {
            System.currentTimeMillis();
            SecretKeySpec secretKeySpec = new SecretKeySpec(SecretKeyFactory.getInstance(PBKDF2_DERIVATION_ALGORITHM).generateSecret(new PBEKeySpec(str.toCharArray(), bArr, Constants.VOLLEY_RESPONSE_TIMEOUT, 128)).getEncoded(), "AES");
            System.currentTimeMillis();
            return secretKeySpec;
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] generateIv(int i) {
        byte[] bArr = new byte[i];
        random.nextBytes(bArr);
        return bArr;
    }

    public static byte[] generateSalt() {
        byte[] bArr = new byte[8];
        random.nextBytes(bArr);
        return bArr;
    }

    public static byte[][] encrypt(byte[] bArr, SecretKey secretKey, byte[] bArr2) {
        try {
            Cipher instance = Cipher.getInstance(CIPHER_ALGORITHM);
            byte[] generateIv = generateIv(instance.getBlockSize());
            instance.init(1, secretKey, new IvParameterSpec(generateIv));
            return new byte[][]{bArr2, generateIv, instance.doFinal(bArr)};
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    public static String decrypt(byte[] bArr, SecretKey secretKey, byte[] bArr2) {
        try {
            Cipher instance = Cipher.getInstance(CIPHER_ALGORITHM);
            instance.init(2, secretKey, new IvParameterSpec(bArr2));
            return new String(instance.doFinal(bArr));
        } catch (GeneralSecurityException unused) {
            return null;
        }
    }

    public String generateUniquePsuedoID() {
        String str = "35" + (Build.BOARD.length() % 10) + (Build.BRAND.length() % 10) + (Build.CPU_ABI.length() % 10) + (Build.DEVICE.length() % 10) + (Build.MANUFACTURER.length() % 10) + (Build.MODEL.length() % 10) + (Build.PRODUCT.length() % 10);
        try {
            return new UUID((long) str.hashCode(), (long) Build.class.getField("SERIAL").get((Object) null).toString().hashCode()).toString();
        } catch (Exception unused) {
            return new UUID((long) str.hashCode(), (long) -905839116).toString();
        }
    }

    public String doDecryption(byte[] bArr, byte[] bArr2, byte[] bArr3) {
        return decrypt(bArr, deriveKeyPbkdf2(bArr2, getJNICallResult_getGold()), bArr3);
    }

    public byte[][] doEncryption(String str, String str2) {
        byte[] generateSalt = generateSalt();
        return encrypt(str.getBytes(), deriveKeyPbkdf2(generateSalt, str2), generateSalt);
    }

    public String getJNICallResult_getGold() {
        return getGold();
    }

    public String getJNICallResult_getBList() {
        return getBList();
    }

    public String getJNICallResult_getPass() {
        return getPass();
    }

    public String round2Decimals(double d) {
        return new DecimalFormat("#########0.00").format(d);
    }

    public String round2Decimals1000(double d) {
        return new DecimalFormat("#,###,###,##0.00").format(d);
    }

    public boolean ifStrategyBoughtForLockDisplay(StrategyResultsFilter strategyResultsFilter) {
        return getDBHandler().ifStrategyBoughtForLockDisplayDB(strategyResultsFilter) > 0;
    }

    public boolean isStgAccessToUser(StrategyResultsFilter strategyResultsFilter) {
        String doDecryption;
        String[] ifExistsCountStgAccess = getDBHandler().ifExistsCountStgAccess(strategyResultsFilter);
        if (ifExistsCountStgAccess[0] == null || ifExistsCountStgAccess[1] == null || ifExistsCountStgAccess[2] == null || (doDecryption = doDecryption(Base64.decode(ifExistsCountStgAccess[0], 0), Base64.decode(ifExistsCountStgAccess[1], 0), Base64.decode(ifExistsCountStgAccess[2], 0))) == null) {
            return false;
        }
        return doDecryption.equals(getJNICallResult_getPass());
    }

    public boolean isFinderPaidUser() {
        try {
            String[] finderPaidUserDB = getDBHandler().getFinderPaidUserDB();
            if (!(finderPaidUserDB[0] == null || finderPaidUserDB[1] == null)) {
                if (finderPaidUserDB[2] != null) {
                    String doDecryption = doDecryption(Base64.decode(finderPaidUserDB[0], 0), Base64.decode(finderPaidUserDB[1], 0), Base64.decode(finderPaidUserDB[2], 0));
                    if (doDecryption != null && Long.parseLong(doDecryption) > Calendar.getInstance().getTimeInMillis()) {
                        return true;
                    }
                }
            }
        } catch (Exception unused) {
        }
        return false;
    }

    public long getFinderSubsrEndDate() {
        try {
            String[] finderPaidUserDB = getDBHandler().getFinderPaidUserDB();
            if (!(finderPaidUserDB[0] == null || finderPaidUserDB[1] == null)) {
                if (finderPaidUserDB[2] != null) {
                    String doDecryption = doDecryption(Base64.decode(finderPaidUserDB[0], 0), Base64.decode(finderPaidUserDB[1], 0), Base64.decode(finderPaidUserDB[2], 0));
                    if (doDecryption == null) {
                        return 0;
                    }
                    long parseLong = Long.parseLong(doDecryption);
                    if (parseLong > Calendar.getInstance().getTimeInMillis()) {
                        return parseLong;
                    }
                }
            }
        } catch (Exception unused) {
        }
        return 0;
    }

    public List<String> getMyCalls() {
        String doDecryption;
        List<MyCalls> myCallsDB = getDBHandler().getMyCallsDB();
        ArrayList arrayList = new ArrayList();
        int i = 0;
        while (i < myCallsDB.size() && (doDecryption = doDecryption(Base64.decode(myCallsDB.get(i).getE(), 0), Base64.decode(myCallsDB.get(i).getS(), 0), Base64.decode(myCallsDB.get(i).getI(), 0))) != null) {
            if (doDecryption.equals(getJNICallResult_getPass())) {
                arrayList.add(dateFormatter(new Date(myCallsDB.get(i).getLondNextDate()), Constants.DT_FMT_dd_MMM_yyyy));
            }
            i++;
        }
        return arrayList;
    }

    public void insertStgAccess(StrategyResultsFilter strategyResultsFilter) {
        getDBHandler().insertStgAccessDB(strategyResultsFilter);
    }

    public void insertFinderAccess(long j) {
        getDBHandler().insertAfterDeleteFinderDB(j);
    }

    public void deleteTradingCallStgAccess() {
        getDBHandler().deleteTradingCallStgAccessDB();
    }

    public int countStgAccess() {
        return getDBHandler().countStgAccess();
    }

    public String shortDateOrTime(Date date) {
        Calendar instance = Calendar.getInstance(TimeZone.getTimeZone("Asia/Calcutta"));
        instance.set(11, 0);
        instance.set(12, 0);
        instance.set(13, 0);
        instance.set(14, 0);
        long timeInMillis = instance.getTimeInMillis();
        Calendar instance2 = Calendar.getInstance();
        instance2.setTime(date);
        instance2.set(11, 0);
        instance2.set(12, 0);
        instance2.set(13, 0);
        instance2.set(14, 0);
        if (timeInMillis == instance2.getTimeInMillis()) {
            return dateFormatter(date, Constants.DT_FMT_HH_mm);
        }
        return dateFormatter(date, Constants.DT_FMT_dd_MMM);
    }

    public String getStrategyCommentary(StrategyResultsFilter strategyResultsFilter, float f) {
        float breakevenUp = strategyResultsFilter.getBreakevenUp() - f;
        float breakevenUp2 = ((strategyResultsFilter.getBreakevenUp() - f) / f) * 100.0f;
        float breakevenDown = f - strategyResultsFilter.getBreakevenDown();
        float breakevenDown2 = ((f - strategyResultsFilter.getBreakevenDown()) / f) * 100.0f;
        if (strategyResultsFilter.getStrategyId() == 106 || strategyResultsFilter.getStrategyId() == 107 || strategyResultsFilter.getStrategyId() == 101) {
            String str = "<B>Strategy </B> <br> " + strategyResultsFilter.getSymbol() + " - " + strategyResultsFilter.getStrategyName() + " <br><br>  <B>Market Outlook </B> <br>  This is a bullish strategy and you expect the spot price to go up. <br><br> <B>BreakEven Point Up </B> <br>  Breakeven Up Point is <b><u>" + strategyResultsFilter.getBreakevenUp() + "</b></u>. This means that this strategy will be profitable if " + strategyResultsFilter.getSymbol() + " expires above <b><u>" + strategyResultsFilter.getBreakevenUp() + "</b></u>. ";
            double d = (double) breakevenUp;
            if (d > 0.0d) {
                str = str + " To cross the Breakeven up point, the stock/spot price needs to rise <b><u>" + round2Decimals(d) + "</b></u> points or <b><u>" + round2Decimals((double) breakevenUp2) + "%</b></u> from current price.";
            }
            return (str + " In general, you are expected to make profit if the spot price goes up from the current levels <br><br> ") + " <B>Quantity </B> <br> Take 1 lot position in each option leg at the recommended price at the same time <br><br> <B>Total Investment </B> <br>  Total investment required is <b><u> ₹ " + strategyResultsFilter.getInvestment() + "</b></u> which includes option premium you pay and the margin amount for options selling if applicable. Note this is approx. amount as margin amount varies from broker to broker. <br><br> ";
        } else if (strategyResultsFilter.getStrategyId() == 108 || strategyResultsFilter.getStrategyId() == 109 || strategyResultsFilter.getStrategyId() == 201) {
            String str2 = "<B>Strategy </B> <br> " + strategyResultsFilter.getSymbol() + " - " + strategyResultsFilter.getStrategyName() + " <br><br>  <B>Market Outlook </B> <br>  This is a bearish strategy and you expect the spot price to go down. <br><br> <B>BreakEven Point Down </B> <br>  Breakeven Down Point is <b><u>" + strategyResultsFilter.getBreakevenDown() + "</b></u>. This means that this strategy  will be profitable if " + strategyResultsFilter.getSymbol() + " expires below <b><u>" + strategyResultsFilter.getBreakevenDown() + "</b></u>. ";
            double d2 = (double) breakevenDown;
            if (d2 > 0.0d) {
                str2 = str2 + " To cross the Breakeven down point, the stock/spot price needs to fall <b><u>" + round2Decimals(d2) + "</b></u> points or <b><u>" + round2Decimals((double) breakevenDown2) + "%</b></u> from current price.";
            }
            return (str2 + " In general, you are expected to make profit if the spot price goes down from the current levels <br><br> ") + " <B>Quantity </B> <br> Take 1 lot position in each option leg at the recommended price at the same time <br><br> <B>Total Investment </B> <br>  Total investment required is <b><u> ₹ " + strategyResultsFilter.getInvestment() + "</b></u> which includes option premium you pay and the margin amount for options selling if applicable. Note this is approx. amount as margin amount varies from broker to broker. <br><br> ";
        } else if (strategyResultsFilter.getStrategyId() != 102 && strategyResultsFilter.getStrategyId() != 103 && strategyResultsFilter.getStrategyId() != 117 && strategyResultsFilter.getStrategyId() != 123 && strategyResultsFilter.getStrategyId() != 120 && strategyResultsFilter.getStrategyId() != 122) {
            return "Sorry! Info not available for this strategy";
        } else {
            return ("<B>Strategy </B> <br> " + strategyResultsFilter.getSymbol() + " - " + strategyResultsFilter.getStrategyName() + " <br><br>  <B>Market Outlook </B> <br>  This is a neutral strategy and you expect the spot price to remain range-bound between <b><u>" + strategyResultsFilter.getBreakevenDown() + " & " + strategyResultsFilter.getBreakevenUp() + "</b></u>.  In general, you are expected to make profit if the spot price do not move aggressively on either side<br><br> <B>BreakEven Point Up </B> <br>  Breakeven Up Point is <b><u>" + strategyResultsFilter.getBreakevenUp() + "</b></u>. This means that this strategy  will be profitable if " + strategyResultsFilter.getSymbol() + " expires below <b><u>" + strategyResultsFilter.getBreakevenUp() + "</b></u>. <br><br> <B>BreakEven Point Down </B> <br>  Breakeven Down Point is <b><u>" + strategyResultsFilter.getBreakevenDown() + "</b></u>. This means that this strategy  will be profitable if " + strategyResultsFilter.getSymbol() + " expires above <b><u>" + strategyResultsFilter.getBreakevenDown() + "</b></u>. <br><br>") + " <B>Quantity </B> <br> Take 1 lot position in each option leg at the recommended price at the same time <br><br> <B>Total Investment </B> <br>  Total investment required is <b><u> ₹ " + strategyResultsFilter.getInvestment() + "</b></u> which includes option premium you pay and the margin amount for options selling if applicable. Note this is approx. amount as margin amount varies from broker to broker. <br><br> ";
        }
    }

    public static Spanned fromHtml(String str) {
        if (Build.VERSION.SDK_INT >= 24) {
            return Html.fromHtml(str, 0);
        }
        return Html.fromHtml(str);
    }

    public void callServerRewards() throws Exception {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("installid", URLEncoder.encode(getAppInstallId(), "UTF-8"));
        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(Constants.URL_SERVICE + "87").openConnection();
        httpURLConnection.setConnectTimeout(Constants.VOLLEY_RESPONSE_TIMEOUT);
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setRequestProperty("Accept-Charset", "UTF-8");
        DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
        dataOutputStream.writeBytes("&condition=");
        dataOutputStream.writeBytes(jSONObject.toString());
        dataOutputStream.flush();
        dataOutputStream.close();
        InputStream inputStream = httpURLConnection.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        if (bufferedReader.readLine().trim().equalsIgnoreCase(Constants.REWARD_IS_AVAILABLE.trim())) {
            setRewardCheckFlag(Constants.REWARD_IS_AVAILABLE.trim());
        } else {
            setRewardCheckFlag(Constants.REWARD_CHK_DEFAULT_FLAG.trim());
        }
        if (inputStream != null) {
            inputStream.close();
        }
        bufferedReader.close();
        if (httpURLConnection != null) {
            httpURLConnection.disconnect();
        }
    }

    public void callServerRewardsTC() throws Exception {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("installid", URLEncoder.encode(getAppInstallId(), "UTF-8"));
        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(Constants.URL_SERVICE + "92").openConnection();
        httpURLConnection.setConnectTimeout(Constants.VOLLEY_RESPONSE_TIMEOUT);
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setRequestProperty("Accept-Charset", "UTF-8");
        DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
        dataOutputStream.writeBytes("&condition=");
        dataOutputStream.writeBytes(jSONObject.toString());
        dataOutputStream.flush();
        dataOutputStream.close();
        InputStream inputStream = httpURLConnection.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        if (bufferedReader.readLine().trim().equalsIgnoreCase(Constants.REWARD_IS_AVAILABLE.trim())) {
            setRewardTCCheckFlag(Constants.REWARD_IS_AVAILABLE.trim());
        } else {
            setRewardTCCheckFlag(Constants.REWARD_CHK_DEFAULT_FLAG.trim());
        }
        if (inputStream != null) {
            inputStream.close();
        }
        bufferedReader.close();
        if (httpURLConnection != null) {
            httpURLConnection.disconnect();
        }
    }

    public void callServerRewardsFinder() throws Exception {
        long j;
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("installid", URLEncoder.encode(getAppInstallId(), "UTF-8"));
        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(Constants.URL_SERVICE + "105").openConnection();
        httpURLConnection.setConnectTimeout(Constants.VOLLEY_RESPONSE_TIMEOUT);
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setRequestProperty("Accept-Charset", "UTF-8");
        DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
        dataOutputStream.writeBytes("&condition=");
        dataOutputStream.writeBytes(jSONObject.toString());
        dataOutputStream.flush();
        dataOutputStream.close();
        InputStream inputStream = httpURLConnection.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        String readLine = bufferedReader.readLine();
        if (readLine != null && !readLine.trim().equalsIgnoreCase("")) {
            try {
                j = Long.parseLong(readLine.trim());
            } catch (Exception unused) {
                j = 0;
            }
            if (j > 0) {
                insertFinderAccess(j);
            }
        }
        if (inputStream != null) {
            inputStream.close();
        }
        bufferedReader.close();
        if (httpURLConnection != null) {
            httpURLConnection.disconnect();
        }
    }

    public void saveStrategy(StrategyResultsFilter strategyResultsFilter, List<StrategyLegsFilter> list, String str, String str2, String str3, String str4) {
        getDBHandler().insertStrategyResultAndLegs(strategyResultsFilter, list, str, str2, str3, str4);
    }

    public void deleteStrategy(StrategyResultsFilter strategyResultsFilter) {
        getDBHandler().deleteStrategyResultAndLegs(strategyResultsFilter);
    }

    public List<String> getFolioStockList() {
        return getDBHandler().getPortfolioStockListDB();
    }

    public List<StrategyResultsFilter> getStrategyResultsFromDB() {
        return getDBHandler().getStrategyResults();
    }

    public List<StrategyLegsFilter> getStrategyLegsFromDB(StrategyResultsFilter strategyResultsFilter) {
        return getDBHandler().getStrategyLegs(strategyResultsFilter);
    }

    public String[] getStgGreeksFromDB(StrategyResultsFilter strategyResultsFilter) {
        return getDBHandler().getStrategyGreeks(strategyResultsFilter);
    }
}
