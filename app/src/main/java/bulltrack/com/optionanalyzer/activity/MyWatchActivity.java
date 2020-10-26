package bulltrack.com.optionanalyzer.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import androidx.appcompat.app.AppCompatActivity;
import bulltrack.com.optionanalyzer.adapter.WatchListAdapter;
import bulltrack.com.optionanalyzer.application.MyGreeksApplication;
import bulltrack.com.optionanalyzer.constants.Constants;
import bulltrack.com.optionanalyzer.dao.GreekSearchCriteriaFields;
import bulltrack.com.optionanalyzer.dao.GreekValues;
import bulltrack.com.optionanalyzer.utility.DateSerializer;
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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.json.JSONObject;

public class MyWatchActivity extends AppCompatActivity {
    /* access modifiers changed from: private */
    public Activity activity;
    WatchListAdapter adapter;
    View emptyView;
    List<GreekValues> listGreek;
    ListView lvGreeks;
    ProgressBar progressBar;
    String webServiceUrl = Constants.URL_SERVICE;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.content_main);
        this.lvGreeks = (ListView) findViewById(R.id.lv_fragment_tab1_optiongreeks);
        View findViewById = findViewById(R.id.empty);
        this.emptyView = findViewById;
        this.lvGreeks.setEmptyView(findViewById);
        this.progressBar = (ProgressBar) findViewById(R.id.progressbar_content_main);
        ((DropdownMenu) findViewById(R.id.dropdown_menu_mainactivity)).setVisibility(8);
        this.activity = this;
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.lvGreeks.getLayoutParams();
        layoutParams.addRule(10);
        this.lvGreeks.setLayoutParams(layoutParams);
        this.lvGreeks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /* JADX WARNING: type inference failed for: r1v0, types: [android.widget.AdapterView<?>, android.widget.AdapterView] */
            /* JADX WARNING: Unknown variable types count: 1 */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void onItemClick(AdapterView<?> r1, View r2, int r3, long r4) {
                /*
                    r0 = this;
                    android.widget.Adapter r1 = r1.getAdapter()
                    java.lang.Object r1 = r1.getItem(r3)
                    bulltrack.com.optionanalyzer.dao.GreekValues r1 = (bulltrack.com.optionanalyzer.dao.GreekValues) r1
                    bulltrack.com.optionanalyzer.activity.MyWatchActivity r2 = bulltrack.com.optionanalyzer.activity.MyWatchActivity.this
                    bulltrack.com.optionanalyzer.application.MyGreeksApplication r2 = r2.getGreekApplication()
                    bulltrack.com.optionanalyzer.activity.MyWatchActivity r3 = bulltrack.com.optionanalyzer.activity.MyWatchActivity.this
                    android.content.Context r3 = r3.getApplicationContext()
                    r4 = 1
                    r2.startViewOptionDetailsActivity(r3, r1, r4)
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: bulltrack.com.optionanalyzer.activity.MyWatchActivity.C06881.onItemClick(android.widget.AdapterView, android.view.View, int, long):void");
            }
        });
        ((AdView) findViewById(R.id.adView)).loadAd(new AdRequest.Builder().setRequestAgent("android_studio:ad_template").build());
        this.listGreek = getGreekApplication().getAllWatches();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        new AsyncListViewLoader().execute(new String[]{"22"});
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        new AsyncListViewLoader().execute(new String[]{"22"});
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.action_refresh) {
            new AsyncListViewLoader().execute(new String[]{"22"});
        }
        return super.onOptionsItemSelected(menuItem);
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
            ArrayList arrayList = new ArrayList();
            try {
                if (MyWatchActivity.this.getGreekApplication().isInternetAvailable() && MyWatchActivity.this.listGreek != null) {
                    HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(MyWatchActivity.this.webServiceUrl + "9").openConnection();
                    httpURLConnection.setConnectTimeout(5000);
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setRequestProperty("Accept-Charset", "UTF-8");
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8"));
                    String readLine = bufferedReader.readLine();
                    bufferedReader.close();
                    JSONObject jSONObject = new JSONObject(readLine);
                    String obj = jSONObject.get("priceDateTime").toString();
                    String obj2 = jSONObject.get("greekDateTime").toString();
                    long time = MyWatchActivity.this.getGreekApplication().dateFormatter(obj, Constants.DT_FMT_yyyy_MM_dd_HH_m_ss).getTime();
                    long time2 = MyWatchActivity.this.getGreekApplication().dateFormatter(obj2, Constants.DT_FMT_yyyy_MM_dd_HH_m_ss).getTime();
                    if (!(obj == null || obj2 == null || time == time2)) {
                        HashMap hashMap = new HashMap();
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.DT_FMT_dd_MMM_yyyy);
                        for (GreekValues next : MyWatchActivity.this.listGreek) {
                            GreekSearchCriteriaFields greekSearchCriteriaFields = r11;
                            GreekSearchCriteriaFields greekSearchCriteriaFields2 = new GreekSearchCriteriaFields(next.getSymbol(), next.getExpiry_d(), next.getStrike(), next.getStrike(), next.getCallPut());
                            arrayList.add(greekSearchCriteriaFields);
                            next.getSymbol();
                            simpleDateFormat.format(next.getExpiry_d());
                            next.getStrike();
                            next.getCallPut();
                            hashMap.put(next.getSymbol() + simpleDateFormat.format(next.getExpiry_d()) + next.getStrike() + next.getCallPut(), new Integer(next.getMarketLot()));
                        }
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        gsonBuilder.registerTypeAdapter(Date.class, new DateSerializer());
                        Gson create = gsonBuilder.create();
                        StringBuilder sb = new StringBuilder();
                        sb.append(MyWatchActivity.this.webServiceUrl);
                        int i = 0;
                        sb.append(strArr[0]);
                        HttpURLConnection httpURLConnection2 = (HttpURLConnection) new URL(sb.toString()).openConnection();
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
                        String readLine2 = bufferedReader2.readLine();
                        Type type = new TypeToken<List<GreekValues>>() {
                        }.getType();
                        Gson create2 = new GsonBuilder().setDateFormat(Constants.DT_FMT_yyyy_MM_dd_HH_m_ss).create();
                        MyWatchActivity.this.listGreek = (List) create2.fromJson(readLine2, type);
                        Iterator<GreekValues> it = MyWatchActivity.this.listGreek.iterator();
                        while (it.hasNext()) {
                            it.next();
                            if (MyWatchActivity.this.listGreek.get(i).getMarketLot() == 0) {
                                GreekValues greekValues = MyWatchActivity.this.listGreek.get(i);
                                MyWatchActivity.this.listGreek.get(i).setMarketLot(((Integer) hashMap.get(greekValues.getSymbol() + simpleDateFormat.format(greekValues.getExpiry_d()) + greekValues.getStrike() + greekValues.getCallPut())).intValue());
                            }
                            i++;
                        }
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        bufferedReader2.close();
                        if (httpURLConnection2 != null) {
                            httpURLConnection2.disconnect();
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println(e.getStackTrace());
            } catch (Exception e2) {
                System.out.println(e2.getStackTrace());
            }
            return MyWatchActivity.this.listGreek;
        }

        /* access modifiers changed from: protected */
        public void onPreExecute() {
            super.onPreExecute();
            MyWatchActivity.this.progressBar.setVisibility(0);
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(List<GreekValues> list) {
            super.onPostExecute(list);
            if (list.size() > 0) {
                MyWatchActivity.this.getGreekApplication().deleteAllWatches();
                MyWatchActivity.this.getGreekApplication().insertAllWatches(list);
            }
            if (list != null && list.size() == 0) {
                MyWatchActivity.this.emptyView.setVisibility(8);
                MyWatchActivity.this.lvGreeks.setEmptyView(MyWatchActivity.this.findViewById(R.id.tv_contentmain_empty_no_error));
            }
            if (list != null) {
                MyWatchActivity.this.adapter = new WatchListAdapter(MyWatchActivity.this.activity, list);
                MyWatchActivity.this.lvGreeks.setAdapter(MyWatchActivity.this.adapter);
                MyWatchActivity.this.adapter.setItemList(list);
                MyWatchActivity.this.adapter.notifyDataSetChanged();
            }
            MyWatchActivity.this.progressBar.setVisibility(4);
        }
    }
}
