package bulltrack.com.optionanalyzer.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.internal.view.SupportMenuItem;
import bulltrack.com.optionanalyzer.adapter.OIStockAdapter;
import bulltrack.com.optionanalyzer.application.MyGreeksApplication;
import bulltrack.com.optionanalyzer.constants.Constants;
import bulltrack.com.optionanalyzer.dao.OIData;
import bulltrack.com.optiongreeks13.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
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

public class OIStockActivity extends AppCompatActivity {
    /* access modifiers changed from: private */
    public Activity activity;

    /* renamed from: li */
    List<OIData> f88li;
    ListView lvOIData;
    OIStockAdapter oiAdapter;
    ProgressBar progressBar;
    String strLatestDate = "";
    TextView tvEODDate;
    String webServiceUrl = Constants.URL_SERVICE;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.activity = this;
        setContentView((int) R.layout.oi_stock);
        this.progressBar = (ProgressBar) findViewById(R.id.progressbar_oi_stock);
        ListView listView = (ListView) findViewById(R.id.lv_oi_stock);
        this.lvOIData = listView;
        listView.setEmptyView(findViewById(R.id.empty));
        this.tvEODDate = (TextView) findViewById(R.id.tv_oi_data_eod_date);
        ((AdView) findViewById(R.id.adView_oi_data)).loadAd(new AdRequest.Builder().setRequestAgent("android_studio:ad_template").build());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        new AsyncListViewLoader().execute(new String[]{"41"});
    }

    public boolean onCreateOptionsMenu(Menu menu) {
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
                if (OIStockActivity.this.oiAdapter == null || OIStockActivity.this.lvOIData == null) {
                    return true;
                }
                OIStockActivity.this.oiAdapter.filter(str.toString().trim());
                OIStockActivity.this.lvOIData.invalidate();
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

    /* access modifiers changed from: protected */
    public MyGreeksApplication getGreekApplication() {
        return (MyGreeksApplication) getApplicationContext();
    }

    private class AsyncListViewLoader extends AsyncTask<String, Void, List<OIData>> {
        private AsyncListViewLoader() {
        }

        /* access modifiers changed from: protected */
        public List<OIData> doInBackground(String... strArr) {
            OIStockActivity.this.f88li = new ArrayList();
            String oIStockUpdateDate = OIStockActivity.this.getGreekApplication().getOIStockUpdateDate();
            long j = 0;
            try {
                if (OIStockActivity.this.getGreekApplication().isInternetAvailable()) {
                    HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(OIStockActivity.this.webServiceUrl + "40").openConnection();
                    httpURLConnection.setConnectTimeout(5000);
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setRequestProperty("Accept-Charset", "UTF-8");
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8"));
                    String readLine = bufferedReader.readLine();
                    bufferedReader.close();
                    String obj = new JSONObject(readLine).get("OIDateTime").toString();
                    long time = OIStockActivity.this.getGreekApplication().dateFormatter(obj, Constants.DT_FMT_yyyy_MM_dd_HH_m_ss).getTime();
                    if (oIStockUpdateDate != null) {
                        j = OIStockActivity.this.getGreekApplication().dateFormatter(oIStockUpdateDate, Constants.DT_FMT_yyyy_MM_dd_HH_m_ss).getTime();
                    }
                    if (obj != null && j < time) {
                        HttpURLConnection httpURLConnection2 = (HttpURLConnection) new URL(OIStockActivity.this.webServiceUrl + strArr[0]).openConnection();
                        httpURLConnection2.setConnectTimeout(Constants.VOLLEY_RESPONSE_TIMEOUT);
                        httpURLConnection2.setDoOutput(true);
                        httpURLConnection2.setRequestProperty("Accept-Charset", "UTF-8");
                        InputStream inputStream = httpURLConnection2.getInputStream();
                        BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                        String readLine2 = bufferedReader2.readLine();
                        Type type = new TypeToken<List<OIData>>() {
                        }.getType();
                        Gson create = new GsonBuilder().setDateFormat(Constants.DT_FMT_yyyy_MM_dd_HH_m_ss).create();
                        OIStockActivity.this.f88li = (List) create.fromJson(readLine2, type);
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        bufferedReader2.close();
                        if (httpURLConnection2 != null) {
                            httpURLConnection2.disconnect();
                        }
                        OIStockActivity.this.getGreekApplication().deleteOIStock();
                        OIStockActivity.this.getGreekApplication().insertOIStock(OIStockActivity.this.f88li);
                    }
                }
            } catch (IOException | Exception unused) {
            }
            OIStockActivity oIStockActivity = OIStockActivity.this;
            oIStockActivity.f88li = oIStockActivity.getGreekApplication().getOIDataForAllStocks();
            String oIStockUpdateDate2 = OIStockActivity.this.getGreekApplication().getOIStockUpdateDate();
            if (oIStockUpdateDate2 != null) {
                j = OIStockActivity.this.getGreekApplication().dateFormatter(oIStockUpdateDate2, Constants.DT_FMT_yyyy_MM_dd_HH_m_ss).getTime();
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.DT_FMT_dd_MMM_yyyy);
            OIStockActivity.this.strLatestDate = simpleDateFormat.format(new Date(j));
            return OIStockActivity.this.f88li;
        }

        /* access modifiers changed from: protected */
        public void onPreExecute() {
            super.onPreExecute();
            OIStockActivity.this.progressBar.setVisibility(0);
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(List<OIData> list) {
            super.onPostExecute(list);
            TextView textView = OIStockActivity.this.tvEODDate;
            textView.setText("EOD update : " + OIStockActivity.this.strLatestDate);
            if (list != null) {
                OIStockActivity.this.oiAdapter = new OIStockAdapter(OIStockActivity.this.activity, list);
                OIStockActivity.this.lvOIData.setAdapter(OIStockActivity.this.oiAdapter);
                OIStockActivity.this.oiAdapter.setItemList(list);
                OIStockActivity.this.oiAdapter.notifyDataSetChanged();
            }
            OIStockActivity.this.progressBar.setVisibility(4);
        }
    }
}
