package bulltrack.com.optionanalyzer.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import bulltrack.com.optionanalyzer.adapter.GreeksListAdapter;
import bulltrack.com.optionanalyzer.application.MyGreeksApplication;
import bulltrack.com.optionanalyzer.constants.Constants;
import bulltrack.com.optionanalyzer.dao.GreekValues;
import bulltrack.com.optiongreeks13.R;
import com.exblr.dropdownmenu.DropdownMenu;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

public class GreekSearchResultsActivity extends AppCompatActivity {
    /* access modifiers changed from: private */
    public Activity activity;
    GreeksListAdapter adapter;

    /* renamed from: li */
    List<GreekValues> f86li;
    ListView lvGreeks;
    ProgressBar progressBar;
    String webServiceUrl = Constants.URL_SERVICE;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.content_main);
        ((DropdownMenu) findViewById(R.id.dropdown_menu_mainactivity)).setVisibility(8);
        this.activity = this;
        this.progressBar = (ProgressBar) findViewById(R.id.progressbar_content_main);
        ListView listView = (ListView) findViewById(R.id.lv_fragment_tab1_optiongreeks);
        this.lvGreeks = listView;
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) listView.getLayoutParams();
        layoutParams.addRule(10);
        this.lvGreeks.setLayoutParams(layoutParams);
        this.lvGreeks.setEmptyView(findViewById(R.id.empty));
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
                    bulltrack.com.optionanalyzer.activity.GreekSearchResultsActivity r2 = bulltrack.com.optionanalyzer.activity.GreekSearchResultsActivity.this
                    bulltrack.com.optionanalyzer.application.MyGreeksApplication r2 = r2.getGreekApplication()
                    bulltrack.com.optionanalyzer.activity.GreekSearchResultsActivity r3 = bulltrack.com.optionanalyzer.activity.GreekSearchResultsActivity.this
                    android.content.Context r3 = r3.getApplicationContext()
                    r4 = 0
                    r2.startViewOptionDetailsActivity(r3, r1, r4)
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: bulltrack.com.optionanalyzer.activity.GreekSearchResultsActivity.C06441.onItemClick(android.widget.AdapterView, android.view.View, int, long):void");
            }
        });
        ((AdView) findViewById(R.id.adView)).loadAd(new AdRequest.Builder().setRequestAgent("android_studio:ad_template").build());
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String string = extras.getString("stock");
            String string2 = extras.getString("expiry");
            String string3 = extras.getString("strikefrom");
            String string4 = extras.getString("striketo");
            String string5 = extras.getString("callorput");
            new AsyncListViewLoader().execute(new String[]{"20", string, string2, string3, string4, string5});
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == R.id.action_refresh || itemId != 16908332) {
            return super.onOptionsItemSelected(menuItem);
        }
        if (getParent() == null) {
            onBackPressed();
            return true;
        }
        NavUtils.navigateUpFromSameTask(this);
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
        public List<GreekValues> doInBackground(String... strArr) {
            GreekSearchResultsActivity.this.f86li = new ArrayList();
            try {
                String str = strArr[1];
                String str2 = strArr[2];
                Float f = new Float(Float.parseFloat(strArr[3]));
                Float f2 = new Float(Float.parseFloat(strArr[4]));
                String str3 = strArr[5];
                HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(GreekSearchResultsActivity.this.webServiceUrl + strArr[0]).openConnection();
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("Accept-Charset", "UTF-8");
                httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                httpURLConnection.setRequestProperty("Accept", "application/json");
                DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
                dataOutputStream.writeBytes("&condition=");
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("callOrPut", str3);
                jSONObject.put("expiryDate", str2);
                jSONObject.put("stock", str);
                jSONObject.put("strikeFrom", f);
                jSONObject.put("strikeTo", f2);
                dataOutputStream.writeBytes(jSONObject.toString());
                dataOutputStream.flush();
                dataOutputStream.close();
                String readLine = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8")).readLine();
                Type type = new TypeToken<List<GreekValues>>() {
                }.getType();
                Gson create = new GsonBuilder().setDateFormat(Constants.DT_FMT_yyyy_MM_dd_HH_m_ss).create();
                GreekSearchResultsActivity.this.f86li = (List) create.fromJson(readLine, type);
                PrintStream printStream = System.out;
                printStream.println(GreekSearchResultsActivity.this.f86li.get(0).getSymbol() + " " + GreekSearchResultsActivity.this.f86li.get(0).getDelta() + " " + GreekSearchResultsActivity.this.f86li.get(0).getCallPut() + " " + GreekSearchResultsActivity.this.f86li.get(0).getExpiry_d() + " " + GreekSearchResultsActivity.this.f86li.get(0).getGamma() + " " + GreekSearchResultsActivity.this.f86li.get(0).getTheoValue() + " " + GreekSearchResultsActivity.this.f86li.get(0).getTheta() + " " + GreekSearchResultsActivity.this.f86li.get(0).getVega());
                System.out.println(GreekSearchResultsActivity.this.f86li.size());
            } catch (IOException e) {
                System.out.println(e.getStackTrace());
            } catch (Exception e2) {
                System.out.println(e2.getStackTrace());
            }
            return GreekSearchResultsActivity.this.f86li;
        }

        /* access modifiers changed from: protected */
        public void onPreExecute() {
            super.onPreExecute();
            GreekSearchResultsActivity.this.progressBar.setVisibility(0);
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(List<GreekValues> list) {
            super.onPostExecute(list);
            list.size();
            GreekSearchResultsActivity.this.adapter = new GreeksListAdapter(GreekSearchResultsActivity.this.activity, list);
            if (GreekSearchResultsActivity.this.lvGreeks != null) {
                GreekSearchResultsActivity.this.lvGreeks.setAdapter(GreekSearchResultsActivity.this.adapter);
                GreekSearchResultsActivity.this.adapter.setItemList(list);
                GreekSearchResultsActivity.this.adapter.notifyDataSetChanged();
            }
            GreekSearchResultsActivity.this.progressBar.setVisibility(4);
        }
    }
}
