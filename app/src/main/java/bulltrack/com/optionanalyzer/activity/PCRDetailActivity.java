package bulltrack.com.optionanalyzer.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.core.view.ViewCompat;
import bulltrack.com.optionanalyzer.application.MyGreeksApplication;
import bulltrack.com.optionanalyzer.constants.Constants;
import bulltrack.com.optionanalyzer.dao.PcrVal;
import bulltrack.com.optiongreeks13.R;
import com.android.volley.DefaultRetryPolicy;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
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
import java.util.ArrayList;
import java.util.List;

public class PCRDetailActivity extends AppCompatActivity {
    /* access modifiers changed from: private */
    public String TAG = "PCRDetailActivity";
    private Activity activity;

    /* renamed from: li */
    List<PcrVal> f91li;
    LineChart mChart;
    String pcrStock;
    ProgressBar progressBar;
    String webServiceUrl = Constants.URL_SERVICE;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.pcr_detail);
        this.progressBar = (ProgressBar) findViewById(R.id.progressbar_pcr_detail);
        this.activity = this;
        ((AdView) findViewById(R.id.adView_banner_pcr_details)).loadAd(new AdRequest.Builder().setRequestAgent("android_studio:ad_template").build());
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.pcrStock = extras.getString("symbol");
            this.mChart = (LineChart) findViewById(R.id.linechart_pcr_detail_pcr);
            new AsyncListViewLoader().execute(new String[]{"74", this.pcrStock});
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() != 16908332) {
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

    private class AsyncListViewLoader extends AsyncTask<String, Void, List<PcrVal>> {
        private AsyncListViewLoader() {
        }

        /* access modifiers changed from: protected */
        public List<PcrVal> doInBackground(String... strArr) {
            PCRDetailActivity.this.f91li = new ArrayList();
            try {
                HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(PCRDetailActivity.this.webServiceUrl + strArr[0]).openConnection();
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("Accept-Charset", "UTF-8");
                httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                httpURLConnection.setRequestProperty("Accept", "application/json");
                DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
                String str = strArr[1];
                dataOutputStream.writeBytes("&condition=");
                dataOutputStream.writeBytes(URLEncoder.encode(str, "UTF-8"));
                dataOutputStream.flush();
                dataOutputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                String readLine = bufferedReader.readLine();
                Type type = new TypeToken<List<PcrVal>>() {
                }.getType();
                Gson create = new GsonBuilder().setDateFormat(Constants.DT_FMT_yyyy_MM_dd_HH_m_ss).create();
                PCRDetailActivity.this.f91li = (List) create.fromJson(readLine, type);
                if (inputStream != null) {
                    inputStream.close();
                }
                bufferedReader.close();
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
            } catch (IOException e) {
                Log.i(PCRDetailActivity.this.TAG, e.toString());
            } catch (Exception e2) {
                Log.i(PCRDetailActivity.this.TAG, e2.toString());
            }
            return PCRDetailActivity.this.f91li;
        }

        /* access modifiers changed from: protected */
        public void onPreExecute() {
            super.onPreExecute();
            PCRDetailActivity.this.progressBar.setVisibility(0);
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(List<PcrVal> list) {
            super.onPostExecute(list);
            if (list.size() == 0) {
                PCRDetailActivity.this.setContentView((int) R.layout.chart_error_message);
                PCRDetailActivity.this.progressBar.setVisibility(4);
                return;
            }
            loadChart(PCRDetailActivity.this.mChart, list);
            PCRDetailActivity.this.progressBar.setVisibility(4);
        }

        public void loadChart(LineChart lineChart, List<PcrVal> list) {
            ArrayList arrayList = new ArrayList();
            ArrayList arrayList2 = new ArrayList();
            for (int i = 0; i < list.size(); i++) {
                arrayList2.add(new Entry(list.get(i).getPcrVal(), i));
                arrayList.add(PCRDetailActivity.this.getGreekApplication().dateFormatter(list.get(i).getUpdDate(), Constants.DT_FMT_dd_MMM));
            }
            LineDataSet lineDataSet = new LineDataSet(arrayList2, " Put Call Ratio (PCR) day wise  ");
            lineDataSet.setFillAlpha(110);
            lineDataSet.setColor(ViewCompat.MEASURED_STATE_MASK);
            lineDataSet.setCircleColor(ViewCompat.MEASURED_STATE_MASK);
            lineDataSet.setLineWidth(1.0f);
            lineDataSet.setCircleSize(3.0f);
            lineDataSet.setDrawCircleHole(false);
            lineDataSet.setValueTextSize(9.0f);
            lineDataSet.setDrawFilled(true);
            ArrayList arrayList3 = new ArrayList();
            arrayList3.add(lineDataSet);
            PCRDetailActivity.this.mChart.setData(new LineData((List<String>) arrayList, (List<LineDataSet>) arrayList3));
            PCRDetailActivity.this.mChart.getLegend().setForm(Legend.LegendForm.LINE);
            PCRDetailActivity.this.mChart.setDescriptionTextSize(18.0f);
            LineChart lineChart2 = PCRDetailActivity.this.mChart;
            lineChart2.setDescription("Stock: " + list.get(0).getSymbol());
            PCRDetailActivity.this.mChart.setNoDataTextDescription("Waiting for data");
            LimitLine limitLine = new LimitLine(1.0f, "PCR = 1.0");
            limitLine.setLineWidth(4.0f);
            limitLine.enableDashedLine(10.0f, 10.0f, 0.0f);
            limitLine.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
            limitLine.setTextSize(10.0f);
            YAxis axisLeft = PCRDetailActivity.this.mChart.getAxisLeft();
            axisLeft.removeAllLimitLines();
            axisLeft.addLimitLine(limitLine);
            axisLeft.enableGridDashedLine(10.0f, 10.0f, 0.0f);
            PCRDetailActivity.this.mChart.getAxisRight().setEnabled(false);
            PCRDetailActivity.this.mChart.animateX((int) DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, Easing.EasingOption.EaseInOutQuart);
            PCRDetailActivity.this.mChart.invalidate();
        }
    }
}
