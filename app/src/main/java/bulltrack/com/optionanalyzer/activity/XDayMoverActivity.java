package bulltrack.com.optionanalyzer.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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
import bulltrack.com.optionanalyzer.adapter.XDayMoverAdapter;
import bulltrack.com.optionanalyzer.application.MyGreeksApplication;
import bulltrack.com.optionanalyzer.constants.Constants;
import bulltrack.com.optionanalyzer.dao.XDayMover;
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
import java.util.ArrayList;
import java.util.List;

public class XDayMoverActivity extends AppCompatActivity {
    /* access modifiers changed from: private */
    public String TAG = "XDayMoverActivity";
    /* access modifiers changed from: private */
    public Activity activity;

    /* renamed from: li */
    List<XDayMover> f95li;
    ListView lvXdayMover;
    InterstitialAd mIAd_xmove;
    ProgressBar progressBar;
    String webServiceUrl = Constants.URL_SERVICE;
    XDayMoverAdapter xDayAdapter;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        String str;
        super.onCreate(bundle);
        this.activity = this;
        InterstitialAd interstitialAd = new InterstitialAd(this.activity);
        this.mIAd_xmove = interstitialAd;
        interstitialAd.setAdUnitId(getString(R.string.ad_unit_id_fno_xmover_interstitial));
        this.mIAd_xmove.loadAd(new AdRequest.Builder().build());
        setContentView((int) R.layout.activity_xdaymover);
        this.progressBar = (ProgressBar) findViewById(R.id.progressbar_xmover);
        ((AdView) findViewById(R.id.adView_banner_xmover)).loadAd(new AdRequest.Builder().setRequestAgent("android_studio:ad_template").build());
        ListView listView = (ListView) findViewById(R.id.lv_activity_xdaymover_data);
        this.lvXdayMover = listView;
        listView.setEmptyView(findViewById(R.id.empty));
        this.lvXdayMover.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /* JADX WARNING: type inference failed for: r1v0, types: [android.widget.AdapterView<?>, android.widget.AdapterView] */
            /* JADX WARNING: Unknown variable types count: 1 */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void onItemClick(AdapterView<?> r1, View r2, int r3, long r4) {
                /*
                    r0 = this;
                    android.widget.Adapter r1 = r1.getAdapter()
                    java.lang.Object r1 = r1.getItem(r3)
                    bulltrack.com.optionanalyzer.dao.XDayMover r1 = (bulltrack.com.optionanalyzer.dao.XDayMover) r1
                    bulltrack.com.optionanalyzer.activity.XDayMoverActivity r2 = bulltrack.com.optionanalyzer.activity.XDayMoverActivity.this
                    bulltrack.com.optionanalyzer.application.MyGreeksApplication r2 = r2.getGreekApplication()
                    bulltrack.com.optionanalyzer.activity.XDayMoverActivity r3 = bulltrack.com.optionanalyzer.activity.XDayMoverActivity.this
                    android.app.Activity r3 = r3.activity
                    bulltrack.com.optionanalyzer.dao.OptionID r1 = r1.getOptID()
                    r2.startChartActivityForOption((android.content.Context) r3, (bulltrack.com.optionanalyzer.dao.OptionID) r1)
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: bulltrack.com.optionanalyzer.activity.XDayMoverActivity.C07501.onItemClick(android.widget.AdapterView, android.view.View, int, long):void");
            }
        });
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            str = extras.getString("bullorbear");
        } else {
            str = "bull";
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (str.equals("bull")) {
            setTitle("Options - Gainers");
            new AsyncListViewLoader().execute(new String[]{"71"});
            return;
        }
        setTitle("Options - Losers");
        new AsyncListViewLoader().execute(new String[]{"72"});
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
                if (XDayMoverActivity.this.xDayAdapter == null || XDayMoverActivity.this.lvXdayMover == null) {
                    return true;
                }
                XDayMoverActivity.this.xDayAdapter.filter(str.toString().trim());
                XDayMoverActivity.this.lvXdayMover.invalidate();
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
        final int itemId = menuItem.getItemId();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (XDayMoverActivity.this.mIAd_xmove.isLoaded()) {
                    XDayMoverActivity.this.mIAd_xmove.show();
                    XDayMoverActivity.this.mIAd_xmove.setAdListener(new AdListener() {
                        public void onAdClosed() {
                            if (itemId != 16908332) {
                                return;
                            }
                            if (XDayMoverActivity.this.getParent() == null) {
                                XDayMoverActivity.this.finish();
                            } else {
                                NavUtils.navigateUpFromSameTask(XDayMoverActivity.this.activity);
                            }
                        }
                    });
                } else if (itemId != 16908332) {
                } else {
                    if (XDayMoverActivity.this.getParent() == null) {
                        XDayMoverActivity.this.finish();
                    } else {
                        NavUtils.navigateUpFromSameTask(XDayMoverActivity.this.activity);
                    }
                }
            }
        }, 1000);
        return true;
    }

    /* access modifiers changed from: protected */
    public MyGreeksApplication getGreekApplication() {
        return (MyGreeksApplication) getApplicationContext();
    }

    private class AsyncListViewLoader extends AsyncTask<String, Void, List<XDayMover>> {
        private AsyncListViewLoader() {
        }

        /* access modifiers changed from: protected */
        public List<XDayMover> doInBackground(String... strArr) {
            XDayMoverActivity.this.f95li = new ArrayList();
            try {
                HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(XDayMoverActivity.this.webServiceUrl + strArr[0]).openConnection();
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
                Type type = new TypeToken<List<XDayMover>>() {
                }.getType();
                Gson create = new GsonBuilder().setDateFormat(Constants.DT_FMT_yyyy_MM_dd_HH_m_ss).create();
                XDayMoverActivity.this.f95li = (List) create.fromJson(readLine, type);
                if (inputStream != null) {
                    inputStream.close();
                }
                bufferedReader.close();
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
            } catch (IOException e) {
                Log.i(XDayMoverActivity.this.TAG, e.toString());
            } catch (Exception e2) {
                Log.i(XDayMoverActivity.this.TAG, e2.toString());
            }
            return XDayMoverActivity.this.f95li;
        }

        /* access modifiers changed from: protected */
        public void onPreExecute() {
            super.onPreExecute();
            XDayMoverActivity.this.progressBar.setVisibility(0);
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(List<XDayMover> list) {
            super.onPostExecute(list);
            if (list.size() == 0) {
                XDayMoverActivity.this.setContentView((int) R.layout.chart_error_message);
                XDayMoverActivity.this.progressBar.setVisibility(4);
                return;
            }
            XDayMoverActivity.this.xDayAdapter = new XDayMoverAdapter(XDayMoverActivity.this.activity, list);
            XDayMoverActivity.this.lvXdayMover.setAdapter(XDayMoverActivity.this.xDayAdapter);
            XDayMoverActivity.this.xDayAdapter.setItemList(list);
            XDayMoverActivity.this.xDayAdapter.notifyDataSetChanged();
            XDayMoverActivity.this.progressBar.setVisibility(4);
        }
    }
}
