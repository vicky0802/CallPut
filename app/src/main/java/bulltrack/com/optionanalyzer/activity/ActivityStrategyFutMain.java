package bulltrack.com.optionanalyzer.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import androidx.core.internal.view.SupportMenuItem;
import bulltrack.com.optionanalyzer.adapter.AdapterFutStrategyMain;
import bulltrack.com.optionanalyzer.application.MyGreeksApplication;
import bulltrack.com.optionanalyzer.constants.Constants;
import bulltrack.com.optionanalyzer.dao.StrategyResultsFilter;
import bulltrack.com.optiongreeks13.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ActivityStrategyFutMain extends AppCompatActivity {
    /* access modifiers changed from: private */
    public String TAG = "ActivityStrategyFutMain";
    /* access modifiers changed from: private */
    public Activity activity;

    /* renamed from: li */
    List<StrategyResultsFilter> f75li;
    ListView lvStrategyResults;
    AdapterFutStrategyMain mainAdapter;
    ProgressBar progressBar;
    boolean runException = false;
    String strategyType = "0";
    String webServiceUrl = Constants.URL_SERVICE;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_strategy_futures_main);
        this.activity = this;
        Bundle extras = getIntent().getExtras();
        String string = getResources().getString(R.string.free_strategies);
        if (extras != null) {
            String str = (String) extras.get("strategytype");
            this.strategyType = str;
            if (str.equalsIgnoreCase(Integer.toString(1))) {
                string = getResources().getString(R.string.paid_strategies);
            } else {
                string = getResources().getString(R.string.free_strategies);
            }
        }
        setTitle(string);
        this.lvStrategyResults = (ListView) findViewById(R.id.lv_activity_strategy_results_main);
        this.progressBar = (ProgressBar) findViewById(R.id.progressbar_strategy_fut_main);
        this.lvStrategyResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /* JADX WARNING: type inference failed for: r1v0, types: [android.widget.AdapterView<?>, android.widget.AdapterView] */
            /* JADX WARNING: Unknown variable types count: 1 */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void onItemClick(AdapterView<?> r1, View r2, int r3, long r4) {
                /*
                    r0 = this;
                    android.widget.Adapter r1 = r1.getAdapter()
                    java.lang.Object r1 = r1.getItem(r3)
                    bulltrack.com.optionanalyzer.dao.StrategyResultsFilter r1 = (bulltrack.com.optionanalyzer.dao.StrategyResultsFilter) r1
                    bulltrack.com.optionanalyzer.activity.ActivityStrategyFutMain r2 = bulltrack.com.optionanalyzer.activity.ActivityStrategyFutMain.this
                    android.content.Context r3 = r2.getApplicationContext()
                    r2.startViewLegsActivity(r3, r1)
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: bulltrack.com.optionanalyzer.activity.ActivityStrategyFutMain.C05161.onItemClick(android.widget.AdapterView, android.view.View, int, long):void");
            }
        });
        new AsyncListViewLoader().execute(new String[]{"89", this.strategyType});
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 1010 && i2 == 1020) {
            startActivity(getIntent());
            finish();
        }
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        menuItem.getItemId();
        if (menuItem.getItemId() != 16908332) {
            return true;
        }
        finish();
        return true;
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
                if (ActivityStrategyFutMain.this.mainAdapter == null || ActivityStrategyFutMain.this.lvStrategyResults == null) {
                    return true;
                }
                ActivityStrategyFutMain.this.mainAdapter.filter(str.toString().trim());
                ActivityStrategyFutMain.this.lvStrategyResults.invalidate();
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

    private class AsyncListViewLoader extends AsyncTask<String, Void, List<StrategyResultsFilter>> {
        private AsyncListViewLoader() {
        }

        /* access modifiers changed from: protected */
        public List<StrategyResultsFilter> doInBackground(String... strArr) {
            ActivityStrategyFutMain.this.runException = false;
            ActivityStrategyFutMain.this.f75li = new ArrayList();
            try {
                HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(ActivityStrategyFutMain.this.webServiceUrl + strArr[0]).openConnection();
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("Accept-Charset", "UTF-8");
                httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
                dataOutputStream.writeBytes("&condition=");
                dataOutputStream.writeBytes(strArr[1]);
                dataOutputStream.flush();
                dataOutputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                String readLine = bufferedReader.readLine();
                bufferedReader.close();
                Type type = new TypeToken<List<StrategyResultsFilter>>() {
                }.getType();
                Gson create = new GsonBuilder().setDateFormat(Constants.DT_FMT_yyyy_MM_dd_HH_m_ss).create();
                ActivityStrategyFutMain.this.f75li = (List) create.fromJson(readLine, type);
                if (inputStream != null) {
                    inputStream.close();
                }
                bufferedReader.close();
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
            } catch (IOException e) {
                ActivityStrategyFutMain.this.runException = true;
                Log.i(ActivityStrategyFutMain.this.TAG, e.toString());
            } catch (Exception e2) {
                ActivityStrategyFutMain.this.runException = true;
                Log.i(ActivityStrategyFutMain.this.TAG, e2.toString());
            }
            return ActivityStrategyFutMain.this.f75li;
        }

        /* access modifiers changed from: protected */
        public void onPreExecute() {
            super.onPreExecute();
            ActivityStrategyFutMain.this.progressBar.setVisibility(View.VISIBLE);
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(List<StrategyResultsFilter> list) {
            super.onPostExecute(list);
            if (list != null) {
                if (ActivityStrategyFutMain.this.runException) {
                    ActivityStrategyFutMain.this.lvStrategyResults.setEmptyView(ActivityStrategyFutMain.this.findViewById(R.id.tv_strategy_empty_error));
                } else {
                    ActivityStrategyFutMain.this.lvStrategyResults.setEmptyView(ActivityStrategyFutMain.this.findViewById(R.id.tv_strategy_empty_no_error));
                }
                ActivityStrategyFutMain.this.mainAdapter = new AdapterFutStrategyMain(ActivityStrategyFutMain.this.activity, list);
                ActivityStrategyFutMain.this.lvStrategyResults.setAdapter(ActivityStrategyFutMain.this.mainAdapter);
                ActivityStrategyFutMain.this.mainAdapter.setItemList(list);
                ActivityStrategyFutMain.this.mainAdapter.notifyDataSetChanged();
            } else {
                ActivityStrategyFutMain.this.lvStrategyResults.setEmptyView(ActivityStrategyFutMain.this.findViewById(R.id.tv_strategy_empty_error));
            }
            ActivityStrategyFutMain.this.progressBar.setVisibility(View.INVISIBLE);
        }
    }

    public void startViewLegsActivity(Context context, StrategyResultsFilter strategyResultsFilter) {
        Intent intent = new Intent(context, ActivityStrategyFutLegs.class);
        intent.putExtra("result", strategyResultsFilter);
        intent.putExtra("actionfrom", Constants.STRATEGY_LEGS_SHOW_FLAG);
        startActivityForResult(intent, 1010);
    }
}
