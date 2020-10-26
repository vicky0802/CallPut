package bulltrack.com.optionanalyzer.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.NavUtils;
import androidx.core.internal.view.SupportMenuItem;
import bulltrack.com.optionanalyzer.adapter.PCRAdapter;
import bulltrack.com.optionanalyzer.application.MyGreeksApplication;
import bulltrack.com.optionanalyzer.constants.Constants;
import bulltrack.com.optionanalyzer.dao.PcrVal;
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
import java.util.ArrayList;
import java.util.List;

public class PCRMainActivity extends AppCompatActivity {
    /* access modifiers changed from: private */
    public String TAG = "PCRMainActivity";
    /* access modifiers changed from: private */
    public Activity activity;

    /* renamed from: li */
    List<PcrVal> f92li;
    ListView lvPCRData;
    PCRAdapter pcrAdapter;
    ProgressBar progressBar;
    String webServiceUrl = Constants.URL_SERVICE;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.activity = this;
        setContentView((int) R.layout.pcr_activity_main);
        this.progressBar = (ProgressBar) findViewById(R.id.progressbar_pcr_main);
        ((AdView) findViewById(R.id.adView_banner_pcr_main)).loadAd(new AdRequest.Builder().setRequestAgent("android_studio:ad_template").build());
        ListView listView = (ListView) findViewById(R.id.lv_pcr_activity_main_data);
        this.lvPCRData = listView;
        listView.setEmptyView(findViewById(R.id.empty));
        this.lvPCRData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /* JADX WARNING: type inference failed for: r1v0, types: [android.widget.AdapterView<?>, android.widget.AdapterView] */
            /* JADX WARNING: Unknown variable types count: 1 */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void onItemClick(AdapterView<?> r1, View r2, int r3, long r4) {
                /*
                    r0 = this;
                    android.widget.Adapter r1 = r1.getAdapter()
                    java.lang.Object r1 = r1.getItem(r3)
                    bulltrack.com.optionanalyzer.dao.PcrVal r1 = (bulltrack.com.optionanalyzer.dao.PcrVal) r1
                    bulltrack.com.optionanalyzer.activity.PCRMainActivity r2 = bulltrack.com.optionanalyzer.activity.PCRMainActivity.this
                    bulltrack.com.optionanalyzer.application.MyGreeksApplication r2 = r2.getGreekApplication()
                    bulltrack.com.optionanalyzer.activity.PCRMainActivity r3 = bulltrack.com.optionanalyzer.activity.PCRMainActivity.this
                    android.content.Context r3 = r3.getApplicationContext()
                    r2.startViewPCRStockActivity(r3, r1)
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: bulltrack.com.optionanalyzer.activity.PCRMainActivity.C07061.onItemClick(android.widget.AdapterView, android.view.View, int, long):void");
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        new AsyncListViewLoader().execute(new String[]{"73"});
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId != 16908332) {
            if (itemId == R.id.action_help) {
                AlertDialog create = new AlertDialog.Builder(this).create();
                create.setTitle("Help");
                create.setMessage(" PCR - Put Call Ratio \n---------------------\n Put Call Ratio is a very important indicator commonly used by traders. It is calculated as: \n PCR = Total Put Options / Total Call Option \n  \n\n It is a contrarian indicator. High value of PCR denotes bullishness. When PCR > 1, it is considered bullish. Note that stock PCR are generally low in value\n To see historical PCR trend for a stock click on the stock row.  \n\n DISCLAIMER - Note that PCR is used in conjunction with other indicators to gauge the market mood and it varies with time. PCR values show here are EOD   ");
                create.show();
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
        getMenuInflater().inflate(R.menu.action_search_pcr, menu);
        getMenuInflater().inflate(R.menu.help, menu);
        SupportMenuItem supportMenuItem = (SupportMenuItem) menu.findItem(R.id.id_action_search_pcr);
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
                if (PCRMainActivity.this.pcrAdapter == null || PCRMainActivity.this.lvPCRData == null) {
                    return true;
                }
                PCRMainActivity.this.pcrAdapter.filter(str.toString().trim());
                PCRMainActivity.this.lvPCRData.invalidate();
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

    private class AsyncListViewLoader extends AsyncTask<String, Void, List<PcrVal>> {
        private AsyncListViewLoader() {
        }

        /* access modifiers changed from: protected */
        public List<PcrVal> doInBackground(String... strArr) {
            PCRMainActivity.this.f92li = new ArrayList();
            try {
                HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(PCRMainActivity.this.webServiceUrl + strArr[0]).openConnection();
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("Accept-Charset", "UTF-8");
                httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                httpURLConnection.setRequestProperty("Accept", "application/json");
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                String readLine = bufferedReader.readLine();
                Type type = new TypeToken<List<PcrVal>>() {
                }.getType();
                Gson create = new GsonBuilder().setDateFormat(Constants.DT_FMT_yyyy_MM_dd_HH_m_ss).create();
                PCRMainActivity.this.f92li = (List) create.fromJson(readLine, type);
                if (inputStream != null) {
                    inputStream.close();
                }
                bufferedReader.close();
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
            } catch (IOException e) {
                Log.i(PCRMainActivity.this.TAG, e.toString());
            } catch (Exception e2) {
                Log.i(PCRMainActivity.this.TAG, e2.toString());
            }
            return PCRMainActivity.this.f92li;
        }

        /* access modifiers changed from: protected */
        public void onPreExecute() {
            super.onPreExecute();
            PCRMainActivity.this.progressBar.setVisibility(0);
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(List<PcrVal> list) {
            super.onPostExecute(list);
            if (list.size() == 0) {
                PCRMainActivity.this.setContentView((int) R.layout.chart_error_message);
                PCRMainActivity.this.progressBar.setVisibility(4);
                return;
            }
            PCRMainActivity.this.pcrAdapter = new PCRAdapter(PCRMainActivity.this.activity, list);
            PCRMainActivity.this.lvPCRData.setAdapter(PCRMainActivity.this.pcrAdapter);
            PCRMainActivity.this.pcrAdapter.setItemList(list);
            PCRMainActivity.this.pcrAdapter.notifyDataSetChanged();
            PCRMainActivity.this.progressBar.setVisibility(4);
        }
    }
}
