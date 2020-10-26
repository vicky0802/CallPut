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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import bulltrack.com.optionanalyzer.adapter.MarginAdapter;
import bulltrack.com.optionanalyzer.application.MyGreeksApplication;
import bulltrack.com.optionanalyzer.constants.Constants;
import bulltrack.com.optionanalyzer.dao.OptionMargin;
import bulltrack.com.optionanalyzer.dao.StrikeExpiry;
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
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

public class OptionMarginMainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    /* access modifiers changed from: private */
    public String TAG = "OptionMarginMainActivity";
    /* access modifiers changed from: private */
    public Activity activity;
    Button btnMinus;
    Button btnPlus;
    int callPutPos = 0;
    DecimalFormat df1 = new DecimalFormat("###,##0.00");
    EditText edtQty;
    int expiryPos = 0;
    int futLotSize = 0;

    /* renamed from: li */
    List<OptionMargin> f90li;
    List<String> liStock;
    List<StrikeExpiry> liStrikeExpiry;
    LinearLayout llOptMarginHead;
    ListView lvOptionMargin;
    MarginAdapter marginAdapter;
    ProgressBar progressBar;
    ScrollView scrollViewFut;
    Spinner spinnerCallPut;
    Spinner spinnerExpiry;
    Spinner spinnerStock;
    int stockPos = -1;
    TextView tvFutExposureVal;
    TextView tvFutSpanVal;
    TextView tvFutTotalMargin;
    TextView tvOptionId;
    String webServiceUrl = Constants.URL_SERVICE;

    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
        Spinner spinner = (Spinner) adapterView;
        if (spinner.getId() == R.id.spnr_option_margin_main_stock) {
            this.stockPos = this.spinnerStock.getSelectedItemPosition();
            this.spinnerExpiry.setAdapter((SpinnerAdapter) null);
            this.expiryPos = 0;
        }
        if (spinner.getId() == R.id.spnr_option_margin_main_expriy) {
            this.expiryPos = this.spinnerExpiry.getSelectedItemPosition();
        }
        if (spinner.getId() == R.id.spnr_option_margin_main_callput) {
            this.callPutPos = this.spinnerCallPut.getSelectedItemPosition();
            if (this.spinnerCallPut.getSelectedItem().toString().equalsIgnoreCase("fut")) {
                this.lvOptionMargin.setVisibility(8);
                this.llOptMarginHead.setVisibility(8);
                this.scrollViewFut.setVisibility(0);
            } else {
                this.llOptMarginHead.setVisibility(0);
                this.lvOptionMargin.setVisibility(0);
                this.scrollViewFut.setVisibility(8);
            }
        }
        new AsyncListViewLoader().execute(new String[]{"70"});
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == R.id.action_help) {
            AlertDialog create = new AlertDialog.Builder(this).create();
            create.setTitle("Help");
            create.setMessage(" PREMIUM \n---------------------\n Premium is what you pay when you buy a Call or Put Option. Your premium is calculated as \n Premium = Option Buy Price * Lot Size \n  \n\n MARGIN  \n --------------------\n When you short sell a Call or Put option, you need to deposit margin amount with the broker. It is calculated based on various parameters such as volatility, stock/option price, span/var margin rates etc. The calculation is done using complex formula\n  \n\n DISCLAIMER - Note that the margin amount generally varies from broker to broker. The values displayed here are indicative.  \n\n Premium value is calulated based on the last close option price. You can re-claulate the the premium by entering the desired price");
            create.show();
        } else if (itemId == 16908332) {
            if (getParent() == null) {
                onBackPressed();
                return true;
            }
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.help, menu);
        return true;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.activity = this;
        setContentView((int) R.layout.option_margin_main);
        this.progressBar = (ProgressBar) findViewById(R.id.progressbar_margin_main);
        ListView listView = (ListView) findViewById(R.id.lv_option_margin_main_data);
        this.lvOptionMargin = listView;
        listView.setEmptyView(findViewById(R.id.empty));
        this.lvOptionMargin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /* JADX WARNING: type inference failed for: r1v0, types: [android.widget.AdapterView<?>, android.widget.AdapterView] */
            /* JADX WARNING: Unknown variable types count: 1 */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void onItemClick(AdapterView<?> r1, View r2, int r3, long r4) {
                /*
                    r0 = this;
                    android.widget.Adapter r1 = r1.getAdapter()
                    java.lang.Object r1 = r1.getItem(r3)
                    bulltrack.com.optionanalyzer.dao.OptionMargin r1 = (bulltrack.com.optionanalyzer.dao.OptionMargin) r1
                    bulltrack.com.optionanalyzer.activity.OptionMarginMainActivity r2 = bulltrack.com.optionanalyzer.activity.OptionMarginMainActivity.this
                    bulltrack.com.optionanalyzer.application.MyGreeksApplication r2 = r2.getGreekApplication()
                    bulltrack.com.optionanalyzer.activity.OptionMarginMainActivity r3 = bulltrack.com.optionanalyzer.activity.OptionMarginMainActivity.this
                    android.app.Activity r3 = r3.activity
                    r2.startViewMarginDetailsActivity(r3, r1)
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: bulltrack.com.optionanalyzer.activity.OptionMarginMainActivity.C07001.onItemClick(android.widget.AdapterView, android.view.View, int, long):void");
            }
        });
        Spinner spinner = (Spinner) findViewById(R.id.spnr_option_margin_main_stock);
        this.spinnerStock = spinner;
        spinner.setOnItemSelectedListener(this);
        Spinner spinner2 = (Spinner) findViewById(R.id.spnr_option_margin_main_expriy);
        this.spinnerExpiry = spinner2;
        spinner2.setOnItemSelectedListener(this);
        Spinner spinner3 = (Spinner) findViewById(R.id.spnr_option_margin_main_callput);
        this.spinnerCallPut = spinner3;
        spinner3.setOnItemSelectedListener(this);
        this.tvOptionId = (TextView) findViewById(R.id.tv_option_margin_main_optionid);
        this.llOptMarginHead = (LinearLayout) findViewById(R.id.ll_option_margin_main_prem_margin_head);
        this.scrollViewFut = (ScrollView) findViewById(R.id.scrollview_option_margin_main_fut_details);
        this.tvFutSpanVal = (TextView) findViewById(R.id.tv_option_margin_main_fut_span_val);
        this.tvFutExposureVal = (TextView) findViewById(R.id.tv_option_margin_main_fut_exposure_val);
        this.tvFutTotalMargin = (TextView) findViewById(R.id.tv_option_margin_main_fut_total_val);
        this.edtQty = (EditText) findViewById(R.id.edt_option_margin_main_fut_qty);
        this.btnPlus = (Button) findViewById(R.id.btn_option_margin_main_fut_plus);
        this.btnMinus = (Button) findViewById(R.id.btn_option_margin_main_fut_minus);
        getGreekApplication();
        ((TextView) findViewById(R.id.tv_option_margin_main_buyprem_txt1)).setText(MyGreeksApplication.fromHtml("Premium<sup>*</sup>"));
        getGreekApplication();
        ((TextView) findViewById(R.id.tv_option_margin_main_sellmargin_txt1)).setText(MyGreeksApplication.fromHtml("Margin<sup>*</sup>"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        new AsyncListViewLoader().execute(new String[]{"70"});
    }

    /* access modifiers changed from: private */
    public void loadSpinnerStock() {
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, 17367048, this.liStock);
        arrayAdapter.setDropDownViewResource(17367049);
        this.spinnerStock.setAdapter(arrayAdapter);
        this.spinnerStock.setSelection(this.stockPos);
    }

    /* access modifiers changed from: private */
    public void loadSpinnerExpiry() {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < this.liStrikeExpiry.size(); i++) {
            arrayList.add(getGreekApplication().dateFormatter(this.liStrikeExpiry.get(i).getExpiry_d(), Constants.DT_FMT_dd_MMM_yyyy));
        }
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, 17367048, arrayList);
        arrayAdapter.setDropDownViewResource(17367049);
        this.spinnerExpiry.setAdapter(arrayAdapter);
    }

    /* access modifiers changed from: private */
    public void loadSpinnerCallPut() {
        ArrayList arrayList = new ArrayList();
        arrayList.add("Call");
        arrayList.add("Put");
        arrayList.add("Fut");
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, 17367048, arrayList);
        arrayAdapter.setDropDownViewResource(17367049);
        this.spinnerCallPut.setAdapter(arrayAdapter);
    }

    public void buttonClicked(View view) {
        List<OptionMargin> list = this.f90li;
        if (list != null && list.size() != 0) {
            if (view.getId() == R.id.btn_option_margin_main_fut_plus) {
                int parseInt = Integer.parseInt(this.edtQty.getText().toString());
                if (parseInt < Integer.MAX_VALUE - this.futLotSize) {
                    float parseFloat = Float.parseFloat(this.tvFutSpanVal.getText().toString().replace(",", "")) + this.f90li.get(0).getSpanMargin();
                    float parseFloat2 = Float.parseFloat(this.tvFutExposureVal.getText().toString().replace(",", "")) + this.f90li.get(0).getExposureMargin();
                    getGreekApplication();
                    this.tvFutSpanVal.setText(this.df1.format((double) MyGreeksApplication.roundTo05(parseFloat)));
                    getGreekApplication();
                    this.tvFutExposureVal.setText(this.df1.format((double) MyGreeksApplication.roundTo05(parseFloat2)));
                    float parseFloat3 = Float.parseFloat(this.tvFutSpanVal.getText().toString().replace(",", "")) + Float.parseFloat(this.tvFutExposureVal.getText().toString().replace(",", ""));
                    TextView textView = this.tvFutTotalMargin;
                    textView.setText("₹ " + this.df1.format((double) parseFloat3));
                    parseInt += this.futLotSize;
                }
                EditText editText = this.edtQty;
                editText.setText(parseInt + "");
            } else if (view.getId() == R.id.btn_option_margin_main_fut_minus) {
                int parseInt2 = Integer.parseInt(this.edtQty.getText().toString());
                if (parseInt2 >= this.futLotSize * 2) {
                    float parseFloat4 = Float.parseFloat(this.tvFutSpanVal.getText().toString().replace(",", "")) - this.f90li.get(0).getSpanMargin();
                    float parseFloat5 = Float.parseFloat(this.tvFutExposureVal.getText().toString().replace(",", "")) - this.f90li.get(0).getExposureMargin();
                    getGreekApplication();
                    this.tvFutSpanVal.setText(this.df1.format((double) MyGreeksApplication.roundTo05(parseFloat4)));
                    getGreekApplication();
                    this.tvFutExposureVal.setText(this.df1.format((double) MyGreeksApplication.roundTo05(parseFloat5)));
                    float parseFloat6 = Float.parseFloat(this.tvFutSpanVal.getText().toString().replace(",", "")) + Float.parseFloat(this.tvFutExposureVal.getText().toString().replace(",", ""));
                    TextView textView2 = this.tvFutTotalMargin;
                    textView2.setText("₹ " + this.df1.format((double) parseFloat6));
                    parseInt2 -= this.futLotSize;
                }
                EditText editText2 = this.edtQty;
                editText2.setText(parseInt2 + "");
            }
        }
    }

    /* access modifiers changed from: protected */
    public MyGreeksApplication getGreekApplication() {
        return (MyGreeksApplication) getApplicationContext();
    }

    private class AsyncListViewLoader extends AsyncTask<String, Void, List<OptionMargin>> {
        private AsyncListViewLoader() {
        }

        /* access modifiers changed from: protected */
        public List<OptionMargin> doInBackground(String... strArr) {
            OptionMarginMainActivity.this.f90li = new ArrayList();
            try {
                HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(OptionMarginMainActivity.this.webServiceUrl + "62").openConnection();
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
                OptionMarginMainActivity.this.liStock = (List) create.fromJson(readLine, type);
                if (inputStream != null) {
                    inputStream.close();
                }
                bufferedReader.close();
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
                if (OptionMarginMainActivity.this.stockPos == -1) {
                    int indexOf = OptionMarginMainActivity.this.liStock.indexOf("NIFTY");
                    if (indexOf < 0) {
                        OptionMarginMainActivity.this.stockPos = 0;
                    } else {
                        OptionMarginMainActivity.this.stockPos = indexOf;
                    }
                }
                String str = OptionMarginMainActivity.this.liStock.get(OptionMarginMainActivity.this.stockPos);
                HttpURLConnection httpURLConnection2 = (HttpURLConnection) new URL(OptionMarginMainActivity.this.webServiceUrl + 63).openConnection();
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
                String str2 = str;
                Gson create2 = new GsonBuilder().setDateFormat(Constants.DT_FMT_yyyy_MM_dd_HH_m_ss).create();
                String str3 = Constants.DT_FMT_yyyy_MM_dd_HH_m_ss;
                OptionMarginMainActivity.this.liStrikeExpiry = (List) create2.fromJson(readLine2, type2);
                if (inputStream2 != null) {
                    inputStream2.close();
                }
                bufferedReader2.close();
                if (httpURLConnection2 != null) {
                    httpURLConnection2.disconnect();
                }
                HttpURLConnection httpURLConnection3 = (HttpURLConnection) new URL(OptionMarginMainActivity.this.webServiceUrl + strArr[0]).openConnection();
                httpURLConnection3.setConnectTimeout(5000);
                httpURLConnection3.setDoInput(true);
                httpURLConnection3.setDoOutput(true);
                httpURLConnection3.setRequestMethod("POST");
                httpURLConnection3.setRequestProperty("Accept-Charset", "UTF-8");
                httpURLConnection3.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                httpURLConnection3.setRequestProperty("Accept", "application/json");
                DataOutputStream dataOutputStream2 = new DataOutputStream(httpURLConnection3.getOutputStream());
                dataOutputStream2.writeBytes("&condition=");
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("optType", "OPTIDX");
                jSONObject.put("symbol", URLEncoder.encode(str2, "UTF-8"));
                String str4 = str3;
                jSONObject.put("expiry", OptionMarginMainActivity.this.getGreekApplication().dateFormatter(OptionMarginMainActivity.this.liStrikeExpiry.get(OptionMarginMainActivity.this.expiryPos).getExpiry_d(), str4));
                jSONObject.put("strike", "1");
                if (OptionMarginMainActivity.this.callPutPos == 0) {
                    jSONObject.put("putOrCall", "C");
                } else if (OptionMarginMainActivity.this.callPutPos == 1) {
                    jSONObject.put("putOrCall", Constants.FLAG_POSITIONAL);
                } else {
                    jSONObject.put("putOrCall", "X");
                }
                dataOutputStream2.writeBytes(jSONObject.toString());
                dataOutputStream2.flush();
                dataOutputStream2.close();
                InputStream inputStream3 = httpURLConnection3.getInputStream();
                BufferedReader bufferedReader3 = new BufferedReader(new InputStreamReader(inputStream3, "UTF-8"));
                String readLine3 = bufferedReader3.readLine();
                Type type3 = new TypeToken<List<OptionMargin>>() {
                }.getType();
                Gson create3 = new GsonBuilder().setDateFormat(str4).create();
                OptionMarginMainActivity.this.f90li = (List) create3.fromJson(readLine3, type3);
                if (inputStream3 != null) {
                    inputStream3.close();
                }
                bufferedReader3.close();
                if (httpURLConnection3 != null) {
                    httpURLConnection3.disconnect();
                }
            } catch (IOException e) {
                Log.i(OptionMarginMainActivity.this.TAG, e.toString());
            } catch (Exception e2) {
                Log.i(OptionMarginMainActivity.this.TAG, e2.toString());
            }
            return OptionMarginMainActivity.this.f90li;
        }

        /* access modifiers changed from: protected */
        public void onPreExecute() {
            super.onPreExecute();
            OptionMarginMainActivity.this.progressBar.setVisibility(0);
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(List<OptionMargin> list) {
            if (list.size() == 0) {
                OptionMarginMainActivity.this.setContentView((int) R.layout.chart_error_message);
                OptionMarginMainActivity.this.progressBar.setVisibility(4);
                return;
            }
            if (OptionMarginMainActivity.this.spinnerStock.getAdapter() == null) {
                OptionMarginMainActivity.this.loadSpinnerStock();
            }
            if (OptionMarginMainActivity.this.spinnerExpiry.getAdapter() == null) {
                OptionMarginMainActivity.this.loadSpinnerExpiry();
            }
            if (OptionMarginMainActivity.this.spinnerCallPut.getAdapter() == null) {
                OptionMarginMainActivity.this.loadSpinnerCallPut();
            }
            if (OptionMarginMainActivity.this.callPutPos < 2) {
                TextView textView = OptionMarginMainActivity.this.tvOptionId;
                textView.setText(list.get(0).getSymbol() + "-" + OptionMarginMainActivity.this.getGreekApplication().dateFormatter(list.get(0).getExpiry(), Constants.DT_FMT_dd_MMM_yyyy) + "-" + list.get(0).getPutOrCall());
                OptionMarginMainActivity.this.marginAdapter = new MarginAdapter(OptionMarginMainActivity.this.activity, list);
                OptionMarginMainActivity.this.lvOptionMargin.setAdapter(OptionMarginMainActivity.this.marginAdapter);
                OptionMarginMainActivity.this.marginAdapter.setItemList(list);
                OptionMarginMainActivity.this.marginAdapter.notifyDataSetChanged();
                OptionMarginMainActivity.this.lvOptionMargin.setSelectionFromTop(0, 0);
                OptionMarginMainActivity.this.lvOptionMargin.setSelectionFromTop(getAtmStrikePos(), OptionMarginMainActivity.this.lvOptionMargin.getHeight() / 2);
            } else {
                TextView textView2 = OptionMarginMainActivity.this.tvOptionId;
                textView2.setText(list.get(0).getSymbol() + "-" + OptionMarginMainActivity.this.getGreekApplication().dateFormatter(list.get(0).getExpiry(), Constants.DT_FMT_dd_MMM_yyyy) + "-FUT");
                OptionMarginMainActivity.this.futLotSize = list.get(0).getLotSize();
                OptionMarginMainActivity.this.edtQty.setText(Integer.toString(list.get(0).getLotSize()));
                OptionMarginMainActivity.this.getGreekApplication();
                OptionMarginMainActivity.this.tvFutSpanVal.setText(OptionMarginMainActivity.this.df1.format((double) MyGreeksApplication.roundTo05(list.get(0).getSpanMargin())));
                OptionMarginMainActivity.this.getGreekApplication();
                OptionMarginMainActivity.this.tvFutExposureVal.setText(OptionMarginMainActivity.this.df1.format((double) MyGreeksApplication.roundTo05(list.get(0).getExposureMargin())));
                float parseFloat = Float.parseFloat(OptionMarginMainActivity.this.tvFutSpanVal.getText().toString().replace(",", "")) + Float.parseFloat(OptionMarginMainActivity.this.tvFutExposureVal.getText().toString().replace(",", ""));
                TextView textView3 = OptionMarginMainActivity.this.tvFutTotalMargin;
                textView3.setText("₹ " + OptionMarginMainActivity.this.df1.format((double) parseFloat));
            }
            OptionMarginMainActivity.this.progressBar.setVisibility(4);
        }

        public int getAtmStrikePos() {
            if (OptionMarginMainActivity.this.f90li == null || OptionMarginMainActivity.this.f90li.size() == 0) {
                return 0;
            }
            float stockPrice = OptionMarginMainActivity.this.f90li.get(0).getStockPrice();
            for (int i = 1; i < OptionMarginMainActivity.this.f90li.size(); i++) {
                if (stockPrice > OptionMarginMainActivity.this.f90li.get(i - 1).getStrike() && stockPrice <= OptionMarginMainActivity.this.f90li.get(i).getStrike()) {
                    return i;
                }
            }
            return 0;
        }
    }
}
