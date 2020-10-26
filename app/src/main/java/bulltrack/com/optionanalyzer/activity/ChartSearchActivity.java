package bulltrack.com.optionanalyzer.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.PointerIconCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import bulltrack.com.optionanalyzer.adapter.RVAdapterStock;
import bulltrack.com.optionanalyzer.application.MyGreeksApplication;
import bulltrack.com.optionanalyzer.constants.Constants;
import bulltrack.com.optionanalyzer.dao.StrikeExpiry;
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
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChartSearchActivity extends AppCompatActivity {
    /* access modifiers changed from: private */
    public String TAG = "ChartSerachActivity";
    /* access modifiers changed from: private */
    public Activity activity;
    private Button btnOk;
    String curCallPut;
    String curExpiry;
    String curStock;
    String curStrike;
    SimpleDateFormat dateFormatLong = new SimpleDateFormat(Constants.DT_FMT_yyyy_MM_dd_HH_m_ss);
    SimpleDateFormat dateFormatShort = new SimpleDateFormat(Constants.DT_FMT_dd_MMM_yyyy);
    /* access modifiers changed from: private */
    public RVAdapterStock horizontalAdapterCallPut;
    /* access modifiers changed from: private */
    public RVAdapterStock horizontalAdapterExpiry;
    /* access modifiers changed from: private */
    public RVAdapterStock horizontalAdapterStock;
    /* access modifiers changed from: private */
    public RVAdapterStock horizontalAdapterStrike;

    /* renamed from: li */
    List<StrikeExpiry> f82li;
    List<String> liCallPut;
    List<String> liExpiry;
    List<String> liStock;
    List<String> liStrike;
    int posCallPut = -1;
    int posExpiry = -1;
    int posStock = -1;
    int posStrike = -1;
    ProgressBar progressBar;
    /* access modifiers changed from: private */
    public RecyclerView rvCallPut;
    /* access modifiers changed from: private */
    public RecyclerView rvExpiry;
    /* access modifiers changed from: private */
    public RecyclerView rvStock;
    /* access modifiers changed from: private */
    public RecyclerView rvStrike;
    TextView tvSelectedOption;
    String webServiceUrl = Constants.URL_SERVICE;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.activity = this;
        setContentView((int) R.layout.activity_chart_search);
        this.rvStock = (RecyclerView) findViewById(R.id.rv_activity_chart_search_stock);
        this.rvStrike = (RecyclerView) findViewById(R.id.rv_activity_chart_search_strike);
        this.rvExpiry = (RecyclerView) findViewById(R.id.rv_activity_chart_search_expiry);
        this.rvCallPut = (RecyclerView) findViewById(R.id.rv_activity_chart_search_callput);
        this.progressBar = (ProgressBar) findViewById(R.id.progressbar_chart_search);
        this.rvCallPut.setHasFixedSize(true);
        this.btnOk = (Button) ((TextView) findViewById(R.id.btn_activity_chart_search_ok));
        ((AdView) findViewById(R.id.adView_chart_search)).loadAd(new AdRequest.Builder().setRequestAgent("android_studio:ad_template").build());
        this.btnOk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (ChartSearchActivity.this.posStock == -1) {
                    Toast.makeText(ChartSearchActivity.this, "Please select STOCK by clicking..", 0).show();
                } else if (ChartSearchActivity.this.posStrike == -1) {
                    Toast.makeText(ChartSearchActivity.this, "Please select STRIKE by clicking", 0).show();
                } else if (ChartSearchActivity.this.posExpiry == -1) {
                    Toast.makeText(ChartSearchActivity.this, "Please select EXPIRY by clicking", 0).show();
                } else if (ChartSearchActivity.this.posCallPut == -1) {
                    Toast.makeText(ChartSearchActivity.this, "Please select CALL or PUT by clicking", 0).show();
                } else {
                    Intent intent = new Intent();
                    if (ChartSearchActivity.this.posStock >= ChartSearchActivity.this.liStock.size()) {
                        intent.putExtra("retStock", ChartSearchActivity.this.liStock.get(0));
                    } else {
                        intent.putExtra("retStock", ChartSearchActivity.this.curStock);
                    }
                    if (ChartSearchActivity.this.posStrike >= ChartSearchActivity.this.liStrike.size()) {
                        intent.putExtra("retStrike", ChartSearchActivity.this.liStrike.get(0));
                    } else {
                        intent.putExtra("retStrike", ChartSearchActivity.this.curStrike);
                    }
                    if (ChartSearchActivity.this.posExpiry >= ChartSearchActivity.this.liExpiry.size()) {
                        intent.putExtra("retExpiry", ChartSearchActivity.this.liExpiry.get(0));
                    } else {
                        intent.putExtra("retExpiry", ChartSearchActivity.this.curExpiry);
                    }
                    intent.putExtra("retCallPut", ChartSearchActivity.this.curCallPut);
                    ChartSearchActivity.this.setResult(PointerIconCompat.TYPE_HAND, intent);
                    ChartSearchActivity.this.finish();
                }
            }
        });
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.curStock = extras.getString("stock");
            this.curExpiry = extras.getString("expiry");
            this.curStrike = extras.getString("strike");
            this.curCallPut = extras.getString("callput");
            new AsyncListViewLoader().execute(new String[]{"63", this.curStock});
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        menuItem.getItemId();
        if (menuItem.getItemId() == 16908332) {
            setResult(PointerIconCompat.TYPE_WAIT);
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void onBackPressed() {
        super.onBackPressed();
        new AdRequest.Builder().build();
        setResult(PointerIconCompat.TYPE_WAIT);
    }

    /* access modifiers changed from: protected */
    public MyGreeksApplication getGreekApplication() {
        return (MyGreeksApplication) getApplicationContext();
    }

    private class AsyncListViewLoader extends AsyncTask<String, Void, List<StrikeExpiry>> {
        private AsyncListViewLoader() {
        }

        /* access modifiers changed from: protected */
        public List<StrikeExpiry> doInBackground(String... strArr) {
            ChartSearchActivity.this.f82li = new ArrayList();
            try {
                HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(ChartSearchActivity.this.webServiceUrl + "62").openConnection();
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestProperty("Accept-Charset", "UTF-8");
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                String readLine = bufferedReader.readLine();
                bufferedReader.close();
                Type type = new TypeToken<List<String>>() {
                }.getType();
                Gson create = new GsonBuilder().create();
                ChartSearchActivity.this.liStock = (List) create.fromJson(readLine, type);
                if (inputStream != null) {
                    inputStream.close();
                }
                bufferedReader.close();
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
                String str = strArr[1];
                HttpURLConnection httpURLConnection2 = (HttpURLConnection) new URL(ChartSearchActivity.this.webServiceUrl + strArr[0]).openConnection();
                httpURLConnection2.setConnectTimeout(5000);
                httpURLConnection2.setDoInput(true);
                httpURLConnection2.setDoOutput(true);
                httpURLConnection2.setRequestMethod("POST");
                httpURLConnection2.setRequestProperty("Accept-Charset", "UTF-8");
                httpURLConnection2.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                httpURLConnection2.setRequestProperty("Accept", "application/json");
                DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection2.getOutputStream());
                dataOutputStream.writeBytes("&condition=" + URLEncoder.encode(str, "UTF-8"));
                dataOutputStream.flush();
                dataOutputStream.close();
                InputStream inputStream2 = httpURLConnection2.getInputStream();
                BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(inputStream2, "UTF-8"));
                String readLine2 = bufferedReader2.readLine();
                Type type2 = new TypeToken<List<StrikeExpiry>>() {
                }.getType();
                Gson create2 = new GsonBuilder().setDateFormat(Constants.DT_FMT_yyyy_MM_dd_HH_m_ss).create();
                ChartSearchActivity.this.f82li = (List) create2.fromJson(readLine2, type2);
                if (inputStream2 != null) {
                    inputStream2.close();
                }
                bufferedReader2.close();
                if (httpURLConnection2 != null) {
                    httpURLConnection2.disconnect();
                }
            } catch (IOException e) {
                Log.i(ChartSearchActivity.this.TAG, e.toString());
            } catch (Exception e2) {
                Log.i(ChartSearchActivity.this.TAG, e2.toString());
            }
            return ChartSearchActivity.this.f82li;
        }

        /* access modifiers changed from: protected */
        public void onPreExecute() {
            super.onPreExecute();
            ChartSearchActivity.this.progressBar.setVisibility(0);
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(final List<StrikeExpiry> list) {
            super.onPostExecute(list);
            if (list.size() == 0) {
                ChartSearchActivity.this.setContentView((int) R.layout.chart_error_message);
                ChartSearchActivity.this.progressBar.setVisibility(4);
                return;
            }
            RVAdapterStock unused = ChartSearchActivity.this.horizontalAdapterStock = new RVAdapterStock(ChartSearchActivity.this.liStock, ChartSearchActivity.this.liStock.indexOf(ChartSearchActivity.this.curStock), new RVAdapterStock.OnItemClickListener() {
                public void onItemClick(TextView textView) {
                    if (ChartSearchActivity.this.liStock.indexOf(textView.getText()) != ChartSearchActivity.this.posStock) {
                        ChartSearchActivity.this.curStock = textView.getText().toString();
                        if (ChartSearchActivity.this.liExpiry != null) {
                            ChartSearchActivity.this.curExpiry = ChartSearchActivity.this.liExpiry.get(0);
                        }
                        if (ChartSearchActivity.this.liStrike != null) {
                            ChartSearchActivity.this.curStrike = ChartSearchActivity.this.liStrike.get(0);
                        }
                        ChartSearchActivity.this.curCallPut = "";
                        ChartSearchActivity.this.curStrike = "";
                        ChartSearchActivity.this.curExpiry = "";
                        new AsyncListViewLoader().execute(new String[]{"63", textView.getText().toString()});
                        ChartSearchActivity.this.posStock = ChartSearchActivity.this.liStock.indexOf(textView.getText());
                        ChartSearchActivity.this.horizontalAdapterStock.notifyDataSetChanged();
                        ChartSearchActivity.this.horizontalAdapterStock.setGlobalPos(ChartSearchActivity.this.posStock);
                        ChartSearchActivity.this.posStrike = -1;
                        ChartSearchActivity.this.posExpiry = -1;
                        ChartSearchActivity.this.posCallPut = -1;
                    }
                }
            });
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ChartSearchActivity.this.activity, 0, false);
            ChartSearchActivity.this.rvStock.setLayoutManager(linearLayoutManager);
            ChartSearchActivity.this.rvStock.setAdapter(ChartSearchActivity.this.horizontalAdapterStock);
            linearLayoutManager.scrollToPosition(ChartSearchActivity.this.liStock.indexOf(ChartSearchActivity.this.curStock));
            ChartSearchActivity chartSearchActivity = ChartSearchActivity.this;
            chartSearchActivity.posStock = chartSearchActivity.liStock.indexOf(ChartSearchActivity.this.curStock);
            StrikeExpiry strikeExpiry = list.get(0);
            ChartSearchActivity.this.liStrike = new ArrayList();
            float strikeDiff = strikeExpiry.getStrikeDiff();
            float strikeMax = strikeExpiry.getStrikeMax();
            for (float strikeMin = strikeExpiry.getStrikeMin(); strikeMin <= strikeMax; strikeMin += strikeDiff) {
                ChartSearchActivity.this.liStrike.add(Float.toString(strikeMin));
            }
            RVAdapterStock unused2 = ChartSearchActivity.this.horizontalAdapterStrike = new RVAdapterStock(ChartSearchActivity.this.liStrike, ChartSearchActivity.this.liStrike.indexOf(ChartSearchActivity.this.curStrike), new RVAdapterStock.OnItemClickListener() {
                public void onItemClick(TextView textView) {
                    ChartSearchActivity.this.curStrike = textView.getText().toString();
                    ChartSearchActivity.this.posStrike = ChartSearchActivity.this.liStrike.indexOf(textView.getText());
                    ChartSearchActivity.this.horizontalAdapterStrike.notifyDataSetChanged();
                    ChartSearchActivity.this.horizontalAdapterStrike.setGlobalPos(ChartSearchActivity.this.posStrike);
                }
            });
            LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(ChartSearchActivity.this.activity, 0, false);
            ChartSearchActivity.this.rvStrike.setLayoutManager(linearLayoutManager2);
            ChartSearchActivity.this.rvStrike.setAdapter(ChartSearchActivity.this.horizontalAdapterStrike);
            linearLayoutManager2.scrollToPosition(ChartSearchActivity.this.liStrike.indexOf(ChartSearchActivity.this.curStrike));
            ChartSearchActivity chartSearchActivity2 = ChartSearchActivity.this;
            chartSearchActivity2.posStrike = chartSearchActivity2.liStrike.indexOf(ChartSearchActivity.this.curStrike);
            ChartSearchActivity.this.liExpiry = new ArrayList();
            for (int i = 0; i < list.size(); i++) {
                ChartSearchActivity.this.liExpiry.add(ChartSearchActivity.this.dateFormatShort.format(list.get(i).getExpiry_d()));
            }
            try {
                Date parse = ChartSearchActivity.this.dateFormatLong.parse(ChartSearchActivity.this.curExpiry);
                ChartSearchActivity.this.curExpiry = ChartSearchActivity.this.dateFormatShort.format(parse);
            } catch (ParseException unused3) {
            }
            RVAdapterStock unused4 = ChartSearchActivity.this.horizontalAdapterExpiry = new RVAdapterStock(ChartSearchActivity.this.liExpiry, ChartSearchActivity.this.liExpiry.indexOf(ChartSearchActivity.this.curExpiry), new RVAdapterStock.OnItemClickListener() {
                public void onItemClick(TextView textView) {
                    if (ChartSearchActivity.this.liExpiry.indexOf(textView.getText()) != ChartSearchActivity.this.posExpiry) {
                        ChartSearchActivity.this.curExpiry = textView.getText().toString();
                        ChartSearchActivity.this.posExpiry = ChartSearchActivity.this.liExpiry.indexOf(textView.getText());
                        StrikeExpiry strikeExpiry = (StrikeExpiry) list.get(ChartSearchActivity.this.liExpiry.indexOf(textView.getText()));
                        ChartSearchActivity.this.liStrike.clear();
                        float strikeDiff = strikeExpiry.getStrikeDiff();
                        float strikeMax = strikeExpiry.getStrikeMax();
                        for (float strikeMin = strikeExpiry.getStrikeMin(); strikeMin <= strikeMax; strikeMin += strikeDiff) {
                            ChartSearchActivity.this.liStrike.add(Float.toString(strikeMin));
                        }
                        ChartSearchActivity.this.posStrike = ChartSearchActivity.this.liStrike.indexOf(ChartSearchActivity.this.curStrike);
                        if (ChartSearchActivity.this.posStrike == -1) {
                            ChartSearchActivity.this.curStrike = "";
                        }
                        ChartSearchActivity.this.rvStrike.scrollToPosition(ChartSearchActivity.this.posStrike);
                        ChartSearchActivity.this.horizontalAdapterStrike.setGlobalPos(ChartSearchActivity.this.posStrike);
                        ChartSearchActivity.this.horizontalAdapterStrike.notifyDataSetChanged();
                        ChartSearchActivity.this.horizontalAdapterExpiry.notifyDataSetChanged();
                        ChartSearchActivity.this.horizontalAdapterExpiry.setGlobalPos(ChartSearchActivity.this.posExpiry);
                    }
                }
            });
            LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(ChartSearchActivity.this.activity, 0, false);
            ChartSearchActivity.this.rvExpiry.setLayoutManager(linearLayoutManager3);
            ChartSearchActivity.this.rvExpiry.setAdapter(ChartSearchActivity.this.horizontalAdapterExpiry);
            linearLayoutManager3.scrollToPosition(ChartSearchActivity.this.liExpiry.indexOf(ChartSearchActivity.this.curExpiry));
            ChartSearchActivity chartSearchActivity3 = ChartSearchActivity.this;
            chartSearchActivity3.posExpiry = chartSearchActivity3.liExpiry.indexOf(ChartSearchActivity.this.curExpiry);
            ChartSearchActivity.this.liCallPut = new ArrayList();
            ChartSearchActivity.this.liCallPut.add("Call");
            ChartSearchActivity.this.liCallPut.add("Put");
            if (ChartSearchActivity.this.curCallPut.trim().equalsIgnoreCase("C")) {
                ChartSearchActivity.this.curCallPut = "Call";
                ChartSearchActivity.this.posCallPut = 0;
            } else if (ChartSearchActivity.this.curCallPut.trim().equalsIgnoreCase(Constants.FLAG_POSITIONAL)) {
                ChartSearchActivity.this.curCallPut = "Put";
                ChartSearchActivity.this.posCallPut = 1;
            } else {
                ChartSearchActivity.this.curCallPut = "";
            }
            RVAdapterStock unused5 = ChartSearchActivity.this.horizontalAdapterCallPut = new RVAdapterStock(ChartSearchActivity.this.liCallPut, ChartSearchActivity.this.liCallPut.indexOf(ChartSearchActivity.this.curCallPut), new RVAdapterStock.OnItemClickListener() {
                public void onItemClick(TextView textView) {
                    ChartSearchActivity.this.curCallPut = textView.getText().toString();
                    ChartSearchActivity.this.posCallPut = ChartSearchActivity.this.liCallPut.indexOf(textView.getText());
                    ChartSearchActivity.this.horizontalAdapterCallPut.notifyDataSetChanged();
                    ChartSearchActivity.this.horizontalAdapterCallPut.setGlobalPos(ChartSearchActivity.this.posCallPut);
                }
            });
            ChartSearchActivity.this.rvCallPut.setLayoutManager(new LinearLayoutManager(ChartSearchActivity.this.activity, 0, false));
            ChartSearchActivity.this.rvCallPut.setAdapter(ChartSearchActivity.this.horizontalAdapterCallPut);
            ChartSearchActivity.this.progressBar.setVisibility(4);
        }
    }
}
