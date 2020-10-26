package bulltrack.com.optionanalyzer.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;
import bulltrack.com.optionanalyzer.adapter.PortfolioListAdapter;
import bulltrack.com.optionanalyzer.application.MyGreeksApplication;
import bulltrack.com.optionanalyzer.constants.Constants;
import bulltrack.com.optionanalyzer.dao.GreekSearchCriteriaFields;
import bulltrack.com.optionanalyzer.dao.GreekValues;
import bulltrack.com.optionanalyzer.utility.DateSerializer;
import bulltrack.com.optiongreeks13.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.json.JSONObject;

public class ShowPortfolioActivity extends AppCompatActivity {
    /* access modifiers changed from: private */
    public Activity activity;
    PortfolioListAdapter adapter;
    List<GreekValues> listGreek;
    ListView lvGreeks;
    ProgressBar progressBar;
    String webServiceUrl = Constants.URL_SERVICE;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.show_portfolio);
        this.progressBar = (ProgressBar) findViewById(R.id.progressbar_portfolio);
        ListView listView = (ListView) findViewById(R.id.lv_portfolio_items);
        this.lvGreeks = listView;
        listView.setEmptyView(findViewById(R.id.empty));
        this.listGreek = getGreekApplication().getAllPortfolioItems(1);
        this.activity = this;
        this.lvGreeks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /* JADX WARNING: type inference failed for: r1v0, types: [android.widget.AdapterView<?>, android.widget.AdapterView] */
            /* JADX WARNING: Unknown variable types count: 1 */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void onItemClick(AdapterView<?> r1, android.view.View r2, int r3, long r4) {
                /*
                    r0 = this;
                    android.widget.Adapter r1 = r1.getAdapter()
                    java.lang.Object r1 = r1.getItem(r3)
                    bulltrack.com.optionanalyzer.dao.GreekValues r1 = (bulltrack.com.optionanalyzer.dao.GreekValues) r1
                    bulltrack.com.optionanalyzer.activity.ShowPortfolioActivity r2 = bulltrack.com.optionanalyzer.activity.ShowPortfolioActivity.this
                    bulltrack.com.optionanalyzer.application.MyGreeksApplication r2 = r2.getGreekApplication()
                    bulltrack.com.optionanalyzer.activity.ShowPortfolioActivity r3 = bulltrack.com.optionanalyzer.activity.ShowPortfolioActivity.this
                    android.content.Context r3 = r3.getApplicationContext()
                    r4 = 2
                    r2.startViewOptionDetailsActivity(r3, r1, r4)
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: bulltrack.com.optionanalyzer.activity.ShowPortfolioActivity.C07111.onItemClick(android.widget.AdapterView, android.view.View, int, long):void");
            }
        });
        ((AdView) findViewById(R.id.adView)).loadAd(new AdRequest.Builder().setRequestAgent("android_studio:ad_template").build());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        new AsyncListViewLoader().execute(new String[]{"21"});
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        new AsyncListViewLoader().execute(new String[]{"21"});
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.action_refresh) {
            new AsyncListViewLoader().execute(new String[]{"21"});
        }
        return super.onOptionsItemSelected(menuItem);
    }

    /* access modifiers changed from: protected */
    public MyGreeksApplication getGreekApplication() {
        return (MyGreeksApplication) getApplicationContext();
    }

    private class AsyncListViewLoader extends AsyncTask<String, Void, List<GreekSearchCriteriaFields>> {
        boolean readServerSuccessful;

        private AsyncListViewLoader() {
            this.readServerSuccessful = false;
        }

        /* access modifiers changed from: protected */
        public List<GreekSearchCriteriaFields> doInBackground(String... strArr) {
            ArrayList arrayList = new ArrayList();
            try {
                if (!ShowPortfolioActivity.this.getGreekApplication().isInternetAvailable() || ShowPortfolioActivity.this.listGreek == null) {
                    return arrayList;
                }
                HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(ShowPortfolioActivity.this.webServiceUrl + "9").openConnection();
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestProperty("Accept-Charset", "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8"));
                String readLine = bufferedReader.readLine();
                bufferedReader.close();
                JSONObject jSONObject = new JSONObject(readLine);
                String obj = jSONObject.get("priceDateTime").toString();
                String obj2 = jSONObject.get("greekDateTime").toString();
                long time = ShowPortfolioActivity.this.getGreekApplication().dateFormatter(obj, Constants.DT_FMT_yyyy_MM_dd_HH_m_ss).getTime();
                long time2 = ShowPortfolioActivity.this.getGreekApplication().dateFormatter(obj2, Constants.DT_FMT_yyyy_MM_dd_HH_m_ss).getTime();
                if (obj == null || obj2 == null || time == time2) {
                    return arrayList;
                }
                for (GreekValues next : ShowPortfolioActivity.this.listGreek) {
                    arrayList.add(new GreekSearchCriteriaFields(next.getSymbol(), next.getExpiry_d(), next.getStrike(), next.getStrike(), next.getCallPut()));
                }
                GsonBuilder gsonBuilder = new GsonBuilder();
                gsonBuilder.registerTypeAdapter(Date.class, new DateSerializer());
                Gson create = gsonBuilder.create();
                HttpURLConnection httpURLConnection2 = (HttpURLConnection) new URL(ShowPortfolioActivity.this.webServiceUrl + strArr[0]).openConnection();
                httpURLConnection2.setConnectTimeout(5000);
                httpURLConnection2.setDoInput(true);
                httpURLConnection2.setDoOutput(true);
                httpURLConnection2.setRequestMethod("POST");
                httpURLConnection2.setRequestProperty("Accept-Charset", "UTF-8");
                httpURLConnection2.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                httpURLConnection2.setRequestProperty("Accept", "application/json");
                DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection2.getOutputStream());
                dataOutputStream.writeBytes("&condition=");
                dataOutputStream.writeBytes(create.toJson((Object) arrayList).toString());
                dataOutputStream.flush();
                dataOutputStream.close();
                InputStream inputStream = httpURLConnection2.getInputStream();
                BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                List<GreekSearchCriteriaFields> list = (List) new GsonBuilder().setDateFormat(Constants.DT_FMT_yyyy_MM_dd_HH_m_ss).create().fromJson(bufferedReader2.readLine(), new TypeToken<List<GreekSearchCriteriaFields>>() {
                }.getType());
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e = e;
                        arrayList = list;
                        System.out.println(e.getStackTrace());
                        return arrayList;
                    } catch (Exception e2) {
                        e = e2;
                        arrayList = list;
                        System.out.println(e.getStackTrace());
                        return arrayList;
                    }
                }
                bufferedReader2.close();
                if (httpURLConnection2 != null) {
                    httpURLConnection2.disconnect();
                }
                this.readServerSuccessful = true;
                return list;
            } catch (IOException e3) {
                e = e3;
                System.out.println(e.getStackTrace());
                return arrayList;
            } catch (Exception e4) {
                e = e4;
                System.out.println(e.getStackTrace());
                return arrayList;
            }
        }

        /* access modifiers changed from: protected */
        public void onPreExecute() {
            super.onPreExecute();
            ShowPortfolioActivity.this.progressBar.setVisibility(0);
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(List<GreekSearchCriteriaFields> list) {
            super.onPostExecute(list);
            if (this.readServerSuccessful) {
                ShowPortfolioActivity.this.getGreekApplication().updateFolioPrices(list);
            }
            ShowPortfolioActivity showPortfolioActivity = ShowPortfolioActivity.this;
            showPortfolioActivity.listGreek = showPortfolioActivity.getGreekApplication().getAllPortfolioItems(1);
            if (ShowPortfolioActivity.this.listGreek != null) {
                ShowPortfolioActivity.this.adapter = new PortfolioListAdapter(ShowPortfolioActivity.this.activity, ShowPortfolioActivity.this.listGreek);
                ShowPortfolioActivity.this.lvGreeks.setAdapter(ShowPortfolioActivity.this.adapter);
            }
            ShowPortfolioActivity.this.progressBar.setVisibility(4);
        }
    }
}
