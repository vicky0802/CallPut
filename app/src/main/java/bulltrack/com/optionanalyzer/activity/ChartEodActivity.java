package bulltrack.com.optionanalyzer.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.internal.view.SupportMenu;
import androidx.core.view.PointerIconCompat;
import androidx.core.view.ViewCompat;
import bulltrack.com.optionanalyzer.application.MyGreeksApplication;
import bulltrack.com.optionanalyzer.constants.Constants;
import bulltrack.com.optionanalyzer.dao.EodTick;
import bulltrack.com.optionanalyzer.utility.MyLargeValueFormatter;
import bulltrack.com.optiongreeks13.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
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
import java.util.List;
import org.json.JSONObject;

public class ChartEodActivity extends AppCompatActivity {
    /* access modifiers changed from: private */
    public String TAG = "ChartEodActivity";
    private Activity activity;
    BarChart barChartOI;
    BarChart barChartVol;
    CandleStickChart candleChart;
    SimpleDateFormat dateFormatLong = new SimpleDateFormat(Constants.DT_FMT_yyyy_MM_dd_HH_m_ss);
    SimpleDateFormat dateFormatShort = new SimpleDateFormat(Constants.DT_FMT_dd_MMM_yyyy);
    String description = "";
    String extraCallPut;
    String extraExpiry;
    String extraStock;
    String extraStrike;

    /* renamed from: li */
    List<EodTick> f81li;
    boolean lowVolumeFlag = false;
    ProgressBar progressBar;
    TextView tvLowVolMessage;
    String webServiceUrl = Constants.URL_SERVICE;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.activity = this;
        setContentView((int) R.layout.activity_chart_eod);
        this.candleChart = (CandleStickChart) findViewById(R.id.candle_activity_chart_eod);
        this.barChartOI = (BarChart) findViewById(R.id.bar_activity_chart_eod_oi);
        this.barChartVol = (BarChart) findViewById(R.id.bar_activity_chart_eod_vol);
        this.tvLowVolMessage = (TextView) findViewById(R.id.tv_candle_activity_chart_lowvolume_txt);
        this.progressBar = (ProgressBar) findViewById(R.id.progressbar_eodchart);
        this.tvLowVolMessage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ChartEodActivity.this.tvLowVolMessage.setVisibility(8);
            }
        });
        setChartGestureListener();
        setChartValSelectedListener();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.extraStock = extras.getString("stock");
            this.extraExpiry = extras.getString("expiry");
            this.extraStrike = extras.getString("strike");
            this.extraCallPut = extras.getString("callput");
            try {
                String format = this.dateFormatShort.format(this.dateFormatLong.parse(this.extraExpiry));
                StringBuilder sb = new StringBuilder();
                sb.append(this.extraStock);
                sb.append("-");
                sb.append(this.extraStrike);
                sb.append("-");
                sb.append(format);
                sb.append("-");
                sb.append(this.extraCallPut.substring(0, 1).equalsIgnoreCase("C") ? "Call" : "Put");
                this.description = sb.toString();
            } catch (ParseException unused) {
            }
            new AsyncListViewLoader().execute(new String[]{"61", this.extraStock, this.extraExpiry, this.extraStrike, this.extraCallPut});
        } else {
            new AsyncListViewLoader().execute(new String[]{"61"});
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_search_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.action_search_option_mnu) {
            if (getIntent().getExtras() != null) {
                Intent intent = new Intent(this, ChartSearchActivity.class);
                intent.putExtra("stock", this.extraStock);
                intent.putExtra("expiry", this.extraExpiry);
                intent.putExtra("strike", this.extraStrike + "");
                intent.putExtra("callput", this.extraCallPut);
                startActivityForResult(intent, PointerIconCompat.TYPE_HAND);
            } else if (this.f81li == null) {
                Toast.makeText(this, "An error has occured", 0).show();
                return super.onOptionsItemSelected(menuItem);
            } else {
                Intent intent2 = new Intent(this, ChartSearchActivity.class);
                intent2.putExtra("stock", this.f81li.get(0).getSymbol());
                intent2.putExtra("expiry", this.dateFormatLong.format(this.f81li.get(0).getExpiry()));
                intent2.putExtra("strike", this.f81li.get(0).getStrike() + "");
                intent2.putExtra("callput", this.f81li.get(0).getPutOrCall().substring(0, 1));
                startActivityForResult(intent2, PointerIconCompat.TYPE_HAND);
            }
        }
        if (menuItem.getItemId() == 16908332) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i2 == 1002 && intent != null) {
            Bundle extras = intent.getExtras();
            this.extraStock = extras.getString("retStock");
            this.extraExpiry = extras.getString("retExpiry");
            this.extraStrike = extras.getString("retStrike");
            this.extraCallPut = extras.getString("retCallPut");
            this.description = this.extraStock + "-" + this.extraStrike + "-" + this.extraExpiry + "-" + this.extraCallPut;
            try {
                this.extraExpiry = this.dateFormatLong.format(this.dateFormatShort.parse(this.extraExpiry));
            } catch (ParseException unused) {
            }
            this.extraCallPut = this.extraCallPut.substring(0, 1);
            new AsyncListViewLoader().execute(new String[]{"61", this.extraStock, this.extraExpiry, this.extraStrike, this.extraCallPut});
        }
    }

    /* access modifiers changed from: protected */
    public void setChartValSelectedListener() {
        this.candleChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            public void onNothingSelected() {
            }

            public void onValueSelected(Entry entry, int i, Highlight highlight) {
                CandleEntry candleEntry = (CandleEntry) entry;
                ChartEodActivity chartEodActivity = ChartEodActivity.this;
                Toast.makeText(chartEodActivity, "Open = " + candleEntry.getOpen() + "\nHigh = " + candleEntry.getHigh() + "\nLow = " + candleEntry.getLow() + "\nClose = " + candleEntry.getClose() + "\nDate = " + ChartEodActivity.this.candleChart.getXValue(entry.getXIndex()), 0).show();
            }
        });
        this.barChartOI.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            public void onNothingSelected() {
            }

            public void onValueSelected(Entry entry, int i, Highlight highlight) {
                BarDataSet barDataSet = (BarDataSet) ((BarData) ChartEodActivity.this.barChartOI.getData()).getDataSetByIndex(i);
                ChartEodActivity chartEodActivity = ChartEodActivity.this;
                Toast.makeText(chartEodActivity, "Open Interest = " + ((int) ((BarEntry) entry).getVal()) + "\nDate = " + ChartEodActivity.this.barChartOI.getXValue(entry.getXIndex()), 1).show();
            }
        });
        this.barChartVol.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            public void onNothingSelected() {
            }

            public void onValueSelected(Entry entry, int i, Highlight highlight) {
                BarDataSet barDataSet = (BarDataSet) ((BarData) ChartEodActivity.this.barChartVol.getData()).getDataSetByIndex(i);
                ChartEodActivity chartEodActivity = ChartEodActivity.this;
                Toast.makeText(chartEodActivity, "Volume = " + ((int) ((BarEntry) entry).getVal()) + "\nDate = " + ChartEodActivity.this.barChartVol.getXValue(entry.getXIndex()), 1).show();
            }
        });
    }

    /* access modifiers changed from: protected */
    public void setChartGestureListener() {
        this.candleChart.setOnChartGestureListener(new OnChartGestureListener() {
            public void onChartDoubleTapped(MotionEvent motionEvent) {
            }

            public void onChartFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            }

            public void onChartGestureEnd(MotionEvent motionEvent, ChartTouchListener.ChartGesture chartGesture) {
            }

            public void onChartGestureStart(MotionEvent motionEvent, ChartTouchListener.ChartGesture chartGesture) {
            }

            public void onChartLongPressed(MotionEvent motionEvent) {
            }

            public void onChartSingleTapped(MotionEvent motionEvent) {
            }

            public void onChartTranslate(MotionEvent motionEvent, float f, float f2) {
                float[] fArr = new float[9];
                float[] fArr2 = new float[9];
                ChartEodActivity.this.candleChart.getViewPortHandler().getMatrixTouch().getValues(fArr);
                Matrix matrixTouch = ChartEodActivity.this.barChartOI.getViewPortHandler().getMatrixTouch();
                matrixTouch.getValues(fArr2);
                fArr2[0] = fArr[0];
                fArr2[2] = fArr[2];
                matrixTouch.setValues(fArr2);
                ChartEodActivity.this.barChartOI.getViewPortHandler().refresh(matrixTouch, ChartEodActivity.this.barChartOI, true);
                ChartEodActivity.this.barChartVol.getViewPortHandler().refresh(matrixTouch, ChartEodActivity.this.barChartVol, true);
            }

            public void onChartScale(MotionEvent motionEvent, float f, float f2) {
                float[] fArr = new float[9];
                float[] fArr2 = new float[9];
                ChartEodActivity.this.candleChart.getViewPortHandler().getMatrixTouch().getValues(fArr);
                Matrix matrixTouch = ChartEodActivity.this.barChartOI.getViewPortHandler().getMatrixTouch();
                matrixTouch.getValues(fArr2);
                fArr2[0] = fArr[0];
                fArr2[2] = fArr[2];
                matrixTouch.setValues(fArr2);
                ChartEodActivity.this.barChartOI.getViewPortHandler().refresh(matrixTouch, ChartEodActivity.this.barChartOI, true);
                ChartEodActivity.this.barChartVol.getViewPortHandler().refresh(matrixTouch, ChartEodActivity.this.barChartVol, true);
            }
        });
    }

    /* access modifiers changed from: protected */
    public MyGreeksApplication getGreekApplication() {
        return (MyGreeksApplication) getApplicationContext();
    }

    private class AsyncListViewLoader extends AsyncTask<String, Void, List<EodTick>> {
        private AsyncListViewLoader() {
        }

        /* access modifiers changed from: protected */
        public List<EodTick> doInBackground(String... strArr) {
            ChartEodActivity.this.f81li = new ArrayList();
            try {
                HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(ChartEodActivity.this.webServiceUrl + strArr[0]).openConnection();
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("Accept-Charset", "UTF-8");
                httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                httpURLConnection.setRequestProperty("Accept", "application/json");
                DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
                if (strArr.length == 5) {
                    String str = strArr[1];
                    String str2 = strArr[2];
                    Float f = new Float(Float.parseFloat(strArr[3]));
                    String str3 = strArr[4];
                    dataOutputStream.writeBytes("&condition=");
                    JSONObject jSONObject = new JSONObject();
                    jSONObject.put("optType", "OPTIDX");
                    jSONObject.put("symbol", URLEncoder.encode(str, "UTF-8"));
                    jSONObject.put("expiry", str2);
                    jSONObject.put("strike", f);
                    jSONObject.put("putOrCall", str3);
                    dataOutputStream.writeBytes(jSONObject.toString());
                }
                dataOutputStream.flush();
                dataOutputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                String readLine = bufferedReader.readLine();
                Type type = new TypeToken<List<EodTick>>() {
                }.getType();
                Gson create = new GsonBuilder().setDateFormat(Constants.DT_FMT_yyyy_MM_dd_HH_m_ss).create();
                ChartEodActivity.this.f81li = (List) create.fromJson(readLine, type);
                if (inputStream != null) {
                    inputStream.close();
                }
                bufferedReader.close();
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
            } catch (IOException e) {
                Log.i(ChartEodActivity.this.TAG, e.toString());
            } catch (Exception e2) {
                Log.i(ChartEodActivity.this.TAG, e2.toString());
            }
            return ChartEodActivity.this.f81li;
        }

        /* access modifiers changed from: protected */
        public void onPreExecute() {
            super.onPreExecute();
            ChartEodActivity.this.progressBar.setVisibility(0);
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(List<EodTick> list) {
            List<EodTick> list2 = list;
            super.onPostExecute(list);
            if (list.size() == 0) {
                ChartEodActivity.this.setContentView((int) R.layout.chart_error_message);
                ChartEodActivity.this.progressBar.setVisibility(4);
                return;
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.DT_FMT_dd_MMM);
            ArrayList arrayList = new ArrayList();
            ArrayList arrayList2 = new ArrayList();
            ArrayList arrayList3 = new ArrayList();
            ArrayList arrayList4 = new ArrayList();
            float f = -100.0f;
            ChartEodActivity.this.lowVolumeFlag = false;
            int i = 0;
            for (int size = list.size() - 1; size >= 0; size--) {
                CandleEntry candleEntry = r12;
                int i2 = i;
                CandleEntry candleEntry2 = new CandleEntry(i, list2.get(size).getHighPrice(), list2.get(size).getLowPrice(), list2.get(size).getOpenPrice(), list2.get(size).getClosePrice());
                arrayList.add(candleEntry);
                arrayList2.add(simpleDateFormat.format(list2.get(size).getTickDate()));
                arrayList3.add(new BarEntry((float) list2.get(size).getOpenInt(), i2));
                arrayList4.add(new BarEntry((float) list2.get(size).getVolume(), i2));
                if (list2.get(size).getVolume() == 0) {
                    ChartEodActivity.this.lowVolumeFlag = true;
                }
                if (list2.get(size).getHighPrice() > f) {
                    f = list2.get(size).getHighPrice();
                }
                i = i2 + 1;
            }
            if (ChartEodActivity.this.lowVolumeFlag) {
                ChartEodActivity.this.tvLowVolMessage.setVisibility(0);
            } else {
                ChartEodActivity.this.tvLowVolMessage.setVisibility(8);
            }
            CandleDataSet candleDataSet = new CandleDataSet(arrayList, "OHLC - Click bars for prices");
            candleDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
            candleDataSet.setShadowColorSameAsCandle(true);
            candleDataSet.setShadowWidth(1.5f);
            candleDataSet.setColor(-16711936);
            candleDataSet.setDecreasingColor(SupportMenu.CATEGORY_MASK);
            candleDataSet.setDecreasingPaintStyle(Paint.Style.FILL_AND_STROKE);
            candleDataSet.setIncreasingColor(-16711936);
            candleDataSet.setIncreasingPaintStyle(Paint.Style.FILL_AND_STROKE);
            candleDataSet.setDrawValues(false);
            CandleData candleData = new CandleData((List<String>) arrayList2, candleDataSet);
            XAxis xAxis = ChartEodActivity.this.candleChart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setSpaceBetweenLabels(3);
            ChartEodActivity.this.candleChart.getAxisRight().setValueFormatter(new MyLargeValueFormatter());
            ChartEodActivity.this.candleChart.getAxisLeft().setEnabled(false);
            ChartEodActivity.this.candleChart.setScaleYEnabled(false);
            if (ChartEodActivity.this.description.trim().equals("") || ChartEodActivity.this.description == null) {
                String format = ChartEodActivity.this.dateFormatShort.format(list2.get(0).getExpiry());
                String substring = list2.get(0).getPutOrCall().substring(0, 1);
                ChartEodActivity chartEodActivity = ChartEodActivity.this;
                StringBuilder sb = new StringBuilder();
                sb.append(list2.get(0).getSymbol());
                sb.append("-");
                sb.append(list2.get(0).getStrike());
                sb.append("-");
                sb.append(format);
                sb.append("-");
                sb.append(substring.equalsIgnoreCase("C") ? "Call" : "Put");
                chartEodActivity.description = sb.toString();
            }
            ChartEodActivity.this.candleChart.setDescription(ChartEodActivity.this.description);
            ChartEodActivity.this.candleChart.setDescriptionColor(Color.parseColor("#ff6a00"));
            ChartEodActivity.this.candleChart.setScaleYEnabled(true);
            ChartEodActivity.this.candleChart.setDescriptionTextSize(16.0f);
            ChartEodActivity.this.candleChart.setData(candleData);
            ChartEodActivity.this.candleChart.notifyDataSetChanged();
            ChartEodActivity.this.candleChart.fitScreen();
            ChartEodActivity.this.candleChart.invalidate();
            BarDataSet barDataSet = new BarDataSet(arrayList3, "Open Interest");
            barDataSet.setColor(ViewCompat.MEASURED_STATE_MASK);
            barDataSet.setDrawValues(false);
            ChartEodActivity.this.barChartOI.setData(new BarData((List<String>) arrayList2, barDataSet));
            ChartEodActivity.this.barChartOI.setScaleEnabled(false);
            ChartEodActivity.this.barChartOI.setDragEnabled(false);
            ChartEodActivity.this.barChartOI.setDescription("");
            XAxis xAxis2 = ChartEodActivity.this.barChartOI.getXAxis();
            xAxis2.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis2.setSpaceBetweenLabels(3);
            xAxis2.setEnabled(false);
            ChartEodActivity.this.barChartOI.getAxisLeft().setEnabled(false);
            ChartEodActivity.this.barChartOI.getAxisRight().setValueFormatter(new MyLargeValueFormatter());
            ChartEodActivity.this.barChartOI.fitScreen();
            ChartEodActivity.this.barChartOI.invalidate();
            BarDataSet barDataSet2 = new BarDataSet(arrayList4, "Volume");
            barDataSet2.setColor(-12303292);
            barDataSet2.setDrawValues(false);
            ChartEodActivity.this.barChartVol.setData(new BarData((List<String>) arrayList2, barDataSet2));
            ChartEodActivity.this.barChartVol.setScaleEnabled(false);
            ChartEodActivity.this.barChartVol.setDragEnabled(false);
            ChartEodActivity.this.barChartVol.setDescription("");
            XAxis xAxis3 = ChartEodActivity.this.barChartVol.getXAxis();
            xAxis3.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis3.setSpaceBetweenLabels(3);
            xAxis3.setEnabled(false);
            ChartEodActivity.this.barChartVol.getAxisLeft().setEnabled(false);
            ChartEodActivity.this.barChartVol.fitScreen();
            ChartEodActivity.this.barChartVol.invalidate();
            ChartEodActivity.this.barChartVol.getAxisRight().setValueFormatter(new MyLargeValueFormatter());
            ChartEodActivity.this.progressBar.setVisibility(4);
        }
    }
}
