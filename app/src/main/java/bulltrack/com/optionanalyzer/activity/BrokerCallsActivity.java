package bulltrack.com.optionanalyzer.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.NavUtils;
import androidx.core.internal.view.SupportMenuItem;
import bulltrack.com.optionanalyzer.adapter.BCAdapter;
import bulltrack.com.optionanalyzer.application.MyGreeksApplication;
import bulltrack.com.optionanalyzer.constants.Constants;
import bulltrack.com.optionanalyzer.dao.Calls;
import bulltrack.com.optiongreeks13.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.json.JSONObject;

public class BrokerCallsActivity extends AppCompatActivity {
    /* access modifiers changed from: private */
    public Activity activity;
    boolean adClosedFlag = false;
    BCAdapter bcAdapter;

    /* renamed from: li */
    List<Calls> f79li;
    ListView lvBCData;
    InterstitialAd mIAd_BC;
    String strLatestDate = "";
    String webServiceUrl = Constants.URL_SERVICE;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.activity = this;
        setContentView((int) R.layout.activity_legs_progress);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        new AsyncListViewLoader().execute(new String[]{"51"});
    }

    /* access modifiers changed from: protected */
    public void LoadUI() {
        InterstitialAd interstitialAd = new InterstitialAd(this.activity);
        this.mIAd_BC = interstitialAd;
        interstitialAd.setAdUnitId(getString(R.string.ad_unit_id_broker_calls_inserstitial));
        this.mIAd_BC.loadAd(new AdRequest.Builder().build());
        setContentView((int) R.layout.broker_calls);
        ListView listView = (ListView) findViewById(R.id.lv_broker_calls);
        this.lvBCData = listView;
        listView.setEmptyView(findViewById(R.id.empty));
        ((AdView) findViewById(R.id.adView_banner_broker_call)).loadAd(new AdRequest.Builder().setRequestAgent("android_studio:ad_template").build());
        this.lvBCData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /* JADX WARNING: type inference failed for: r1v0, types: [android.widget.AdapterView<?>, android.widget.AdapterView] */
            /* JADX WARNING: Unknown variable types count: 1 */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void onItemClick(AdapterView<?> r1, View r2, int r3, long r4) {
                /*
                    r0 = this;
                    android.widget.Adapter r1 = r1.getAdapter()
                    java.lang.Object r1 = r1.getItem(r3)
                    bulltrack.com.optionanalyzer.dao.Calls r1 = (bulltrack.com.optionanalyzer.dao.Calls) r1
                    java.lang.String r2 = r1.getURL()
                    java.lang.String r2 = r2.trim()
                    java.lang.String r3 = ""
                    boolean r2 = r2.equals(r3)
                    if (r2 != 0) goto L_0x003f
                    java.lang.String r1 = r1.getURL()     // Catch:{ Exception -> 0x002f }
                    android.net.Uri r1 = android.net.Uri.parse(r1)     // Catch:{ Exception -> 0x002f }
                    android.content.Intent r2 = new android.content.Intent     // Catch:{ Exception -> 0x002f }
                    java.lang.String r3 = "android.intent.action.VIEW"
                    r2.<init>(r3, r1)     // Catch:{ Exception -> 0x002f }
                    bulltrack.com.optionanalyzer.activity.BrokerCallsActivity r1 = bulltrack.com.optionanalyzer.activity.BrokerCallsActivity.this     // Catch:{ Exception -> 0x002f }
                    r1.startActivity(r2)     // Catch:{ Exception -> 0x002f }
                    goto L_0x003f
                L_0x002f:
                    bulltrack.com.optionanalyzer.activity.BrokerCallsActivity r1 = bulltrack.com.optionanalyzer.activity.BrokerCallsActivity.this
                    android.content.Context r1 = r1.getApplicationContext()
                    r2 = 1
                    java.lang.String r3 = "Some problem with URL. Sorry!"
                    android.widget.Toast r1 = android.widget.Toast.makeText(r1, r3, r2)
                    r1.show()
                L_0x003f:
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: bulltrack.com.optionanalyzer.activity.BrokerCallsActivity.C05501.onItemClick(android.widget.AdapterView, android.view.View, int, long):void");
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        getMenuInflater().inflate(R.menu.action_search_bc, menu);
        SupportMenuItem supportMenuItem = (SupportMenuItem) menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) supportMenuItem.getActionView();
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View view, boolean z) {
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            public boolean onQueryTextSubmit(String str) {
                return false;
            }

            public boolean onQueryTextChange(String str) {
                if (BrokerCallsActivity.this.bcAdapter == null || BrokerCallsActivity.this.lvBCData == null) {
                    return true;
                }
                BrokerCallsActivity.this.bcAdapter.filter(str.toString().trim());
                BrokerCallsActivity.this.lvBCData.invalidate();
                return true;
            }
        });
        supportMenuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                return true;
            }

            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                return true;
            }
        });
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == R.id.action_refresh) {
            new AsyncListViewLoader().execute(new String[]{"51"});
        } else if (itemId == 16908332) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    if (BrokerCallsActivity.this.mIAd_BC != null && BrokerCallsActivity.this.mIAd_BC.isLoaded()) {
                        BrokerCallsActivity.this.mIAd_BC.show();
                        BrokerCallsActivity.this.mIAd_BC.setAdListener(new AdListener() {
                            public void onAdClosed() {
                                if (BrokerCallsActivity.this.getParent() == null) {
                                    BrokerCallsActivity.this.finish();
                                } else {
                                    NavUtils.navigateUpFromSameTask(BrokerCallsActivity.this.activity);
                                }
                            }
                        });
                    } else if (BrokerCallsActivity.this.getParent() == null) {
                        BrokerCallsActivity.this.finish();
                    } else {
                        NavUtils.navigateUpFromSameTask(BrokerCallsActivity.this.activity);
                    }
                }
            }, 1000);
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public MyGreeksApplication getGreekApplication() {
        return (MyGreeksApplication) getApplicationContext();
    }

    private class AsyncListViewLoader extends AsyncTask<String, Void, List<Calls>> {
        private AsyncListViewLoader() {
        }

        /* access modifiers changed from: protected */
        public List<Calls> doInBackground(String... strArr) {
            BrokerCallsActivity.this.f79li = new ArrayList();
            String brokerCallsUpdateDate = BrokerCallsActivity.this.getGreekApplication().getBrokerCallsUpdateDate();
            long j = 0;
            try {
                if (BrokerCallsActivity.this.getGreekApplication().isInternetAvailable()) {
                    HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(BrokerCallsActivity.this.webServiceUrl + "50").openConnection();
                    httpURLConnection.setConnectTimeout(5000);
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setRequestProperty("Accept-Charset", "UTF-8");
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8"));
                    String readLine = bufferedReader.readLine();
                    bufferedReader.close();
                    String obj = new JSONObject(readLine).get("BCDateTime").toString();
                    long time = BrokerCallsActivity.this.getGreekApplication().dateFormatter(obj, Constants.DT_FMT_yyyy_MM_dd_HH_m_ss).getTime();
                    if (brokerCallsUpdateDate != null) {
                        j = BrokerCallsActivity.this.getGreekApplication().dateFormatter(brokerCallsUpdateDate, Constants.DT_FMT_yyyy_MM_dd_HH_m_ss).getTime();
                    }
                    if (obj != null && j < time) {
                        HttpURLConnection httpURLConnection2 = (HttpURLConnection) new URL(BrokerCallsActivity.this.webServiceUrl + strArr[0]).openConnection();
                        httpURLConnection2.setConnectTimeout(Constants.VOLLEY_RESPONSE_TIMEOUT);
                        httpURLConnection2.setDoOutput(true);
                        httpURLConnection2.setRequestProperty("Accept-Charset", "UTF-8");
                        InputStream inputStream = httpURLConnection2.getInputStream();
                        BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                        String readLine2 = bufferedReader2.readLine();
                        Type type = new TypeToken<List<Calls>>() {
                        }.getType();
                        Gson create = new GsonBuilder().setDateFormat(Constants.DT_FMT_yyyy_MM_dd_HH_m_ss).create();
                        BrokerCallsActivity.this.f79li = (List) create.fromJson(readLine2, type);
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        bufferedReader2.close();
                        if (httpURLConnection2 != null) {
                            httpURLConnection2.disconnect();
                        }
                        BrokerCallsActivity.this.getGreekApplication().deleteBrokerCalls();
                        BrokerCallsActivity.this.getGreekApplication().insertBrokerCalls(BrokerCallsActivity.this.f79li);
                    }
                }
            } catch (IOException | Exception unused) {
            }
            BrokerCallsActivity brokerCallsActivity = BrokerCallsActivity.this;
            brokerCallsActivity.f79li = brokerCallsActivity.getGreekApplication().getBrokerCallsData();
            String brokerCallsUpdateDate2 = BrokerCallsActivity.this.getGreekApplication().getBrokerCallsUpdateDate();
            if (brokerCallsUpdateDate2 != null) {
                j = BrokerCallsActivity.this.getGreekApplication().dateFormatter(brokerCallsUpdateDate2, Constants.DT_FMT_yyyy_MM_dd_HH_m_ss).getTime();
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.DT_FMT_dd_MMM_yyyy);
            BrokerCallsActivity.this.strLatestDate = simpleDateFormat.format(new Date(j));
            return BrokerCallsActivity.this.f79li;
        }

        /* access modifiers changed from: protected */
        public void onPreExecute() {
            super.onPreExecute();
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(List<Calls> list) {
            super.onPostExecute(list);
            BrokerCallsActivity.this.LoadUI();
            if (list != null) {
                BrokerCallsActivity.this.bcAdapter = new BCAdapter(BrokerCallsActivity.this.activity, list);
                BrokerCallsActivity.this.lvBCData.setAdapter(BrokerCallsActivity.this.bcAdapter);
                BrokerCallsActivity.this.bcAdapter.setItemList(list);
                BrokerCallsActivity.this.bcAdapter.notifyDataSetChanged();
            }
        }
    }
}
