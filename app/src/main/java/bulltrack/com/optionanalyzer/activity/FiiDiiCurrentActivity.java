package bulltrack.com.optionanalyzer.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.internal.view.SupportMenu;
import bulltrack.com.optionanalyzer.application.MyGreeksApplication;
import bulltrack.com.optionanalyzer.constants.Constants;
import bulltrack.com.optionanalyzer.dao.PartyRec;
import bulltrack.com.optiongreeks13.R;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.json.JSONObject;

public class FiiDiiCurrentActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Activity activity;

    /* renamed from: li */
    List<PartyRec> f83li;
    ProgressBar progressBar;
    Spinner spinnerInst;
    String strLatestDate = "";
    TextView tvEODDate;
    TextView tvParty1;
    TextView tvParty1LongOI;
    TextView tvParty1NetOI;
    TextView tvParty1ShortOI;
    TextView tvParty2;
    TextView tvParty2LongOI;
    TextView tvParty2NetOI;
    TextView tvParty2ShortOI;
    TextView tvParty3;
    TextView tvParty3LongOI;
    TextView tvParty3NetOI;
    TextView tvParty3ShortOI;
    TextView tvParty4;
    TextView tvParty4LongOI;
    TextView tvParty4NetOI;
    TextView tvParty4ShortOI;
    TextView tvPartyHeading;
    String webServiceUrl = Constants.URL_SERVICE;

    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
        if (((Spinner) adapterView).getId() == R.id.spnr_allparty_current_instrument) {
            AsyncListViewLoader asyncListViewLoader = new AsyncListViewLoader();
            asyncListViewLoader.execute(new String[]{"32", "" + i});
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.activity = this;
        setContentView((int) R.layout.allparty_current);
        this.progressBar = (ProgressBar) findViewById(R.id.progressbar_party_current);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Spinner spinner = (Spinner) findViewById(R.id.spnr_allparty_current_instrument);
        this.spinnerInst = spinner;
        spinner.setOnItemSelectedListener(this);
        this.tvPartyHeading = (TextView) findViewById(R.id.tv_allparty_current_heading);
        this.tvEODDate = (TextView) findViewById(R.id.tv_allparty_current_eod_date);
        this.tvParty1 = (TextView) findViewById(R.id.tv_allparty_current_long_short_data_1_party);
        this.tvParty1LongOI = (TextView) findViewById(R.id.tv_allparty_current_long_short_data_1_long);
        this.tvParty1ShortOI = (TextView) findViewById(R.id.tv_allparty_current_long_short_data_1_short);
        this.tvParty1NetOI = (TextView) findViewById(R.id.tv_allparty_current_long_short_data_1_net);
        this.tvParty2 = (TextView) findViewById(R.id.tv_allparty_current_long_short_data_2_party);
        this.tvParty2LongOI = (TextView) findViewById(R.id.tv_allparty_current_long_short_data_2_long);
        this.tvParty2ShortOI = (TextView) findViewById(R.id.tv_allparty_current_long_short_data_2_short);
        this.tvParty2NetOI = (TextView) findViewById(R.id.tv_allparty_current_long_short_data_2_net);
        this.tvParty3 = (TextView) findViewById(R.id.tv_allparty_current_long_short_data_3_party);
        this.tvParty3LongOI = (TextView) findViewById(R.id.tv_allparty_current_long_short_data_3_long);
        this.tvParty3ShortOI = (TextView) findViewById(R.id.tv_allparty_current_long_short_data_3_short);
        this.tvParty3NetOI = (TextView) findViewById(R.id.tv_allparty_current_long_short_data_3_net);
        this.tvParty4 = (TextView) findViewById(R.id.tv_allparty_current_long_short_data_4_party);
        this.tvParty4LongOI = (TextView) findViewById(R.id.tv_allparty_current_long_short_data_4_long);
        this.tvParty4ShortOI = (TextView) findViewById(R.id.tv_allparty_current_long_short_data_4_short);
        this.tvParty4NetOI = (TextView) findViewById(R.id.tv_allparty_current_long_short_data_4_net);
        ((ImageView) findViewById(R.id.img_allparty_current_help)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AlertDialog create = new AlertDialog.Builder(FiiDiiCurrentActivity.this).create();
                create.setTitle("Help");
                create.setMessage("There are 4 categories of traders (Party) : \n 1. FII (Foreign Institutional Investors )\n 2. DII (Domestic Institutional Investors / MFs) \n 3. Pro (Proprietary Traders / Brokers ) \n 4. Client (Clients of Brokers / Retail traders ) \n\n ------------- \n  ** FUTIDX - Index Future  \n     FUTSTK - Stock Future  \n     OPTIDXCALL -  Index Option Call \n     OPTIDXPUT  -  Index Option Put \n     OPTSTKCALL -  Stock Option Call \n     OPTSTKPUT -  Stock Option Put \n\n ------------- \n This screen shows the Open Interest generated by each of the above party for the last day(EOD). You can see the Open Interest build up  separately for each instrument type. This is helpful in  knowing individual party's outlook.");
                create.show();
            }
        });
        new AsyncListViewLoader().execute(new String[]{"32", "0"});
    }

    /* access modifiers changed from: private */
    public void loadSpinnerIntrument() {
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, 17367048, getGreekApplication().getIntrumentNamesFromCurrent());
        arrayAdapter.setDropDownViewResource(17367049);
        this.spinnerInst.setAdapter(arrayAdapter);
    }

    /* access modifiers changed from: protected */
    public MyGreeksApplication getGreekApplication() {
        return (MyGreeksApplication) getApplicationContext();
    }

    private class AsyncListViewLoader extends AsyncTask<String, Void, List<PartyRec>> {
        private AsyncListViewLoader() {
        }

        /* access modifiers changed from: protected */
        public List<PartyRec> doInBackground(String... strArr) {
            FiiDiiCurrentActivity.this.f83li = new ArrayList();
            String partyCurrentUpdateDate = FiiDiiCurrentActivity.this.getGreekApplication().getPartyCurrentUpdateDate();
            long j = 0;
            try {
                if (FiiDiiCurrentActivity.this.getGreekApplication().isInternetAvailable()) {
                    HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(FiiDiiCurrentActivity.this.webServiceUrl + "30").openConnection();
                    httpURLConnection.setConnectTimeout(5000);
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setRequestProperty("Accept-Charset", "UTF-8");
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8"));
                    String readLine = bufferedReader.readLine();
                    bufferedReader.close();
                    String obj = new JSONObject(readLine).get("PartyDateTime").toString();
                    long time = FiiDiiCurrentActivity.this.getGreekApplication().dateFormatter(obj, Constants.DT_FMT_yyyy_MM_dd_HH_m_ss).getTime();
                    if (partyCurrentUpdateDate != null) {
                        j = FiiDiiCurrentActivity.this.getGreekApplication().dateFormatter(partyCurrentUpdateDate, Constants.DT_FMT_yyyy_MM_dd_HH_m_ss).getTime();
                    }
                    if (obj != null && j < time) {
                        HttpURLConnection httpURLConnection2 = (HttpURLConnection) new URL(FiiDiiCurrentActivity.this.webServiceUrl + strArr[0]).openConnection();
                        httpURLConnection2.setConnectTimeout(Constants.VOLLEY_RESPONSE_TIMEOUT);
                        httpURLConnection2.setDoOutput(true);
                        httpURLConnection2.setRequestProperty("Accept-Charset", "UTF-8");
                        InputStream inputStream = httpURLConnection2.getInputStream();
                        BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                        String readLine2 = bufferedReader2.readLine();
                        Type type = new TypeToken<List<PartyRec>>() {
                        }.getType();
                        Gson create = new GsonBuilder().setDateFormat(Constants.DT_FMT_yyyy_MM_dd_HH_m_ss).create();
                        FiiDiiCurrentActivity.this.f83li = (List) create.fromJson(readLine2, type);
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        bufferedReader2.close();
                        if (httpURLConnection2 != null) {
                            httpURLConnection2.disconnect();
                        }
                        FiiDiiCurrentActivity.this.getGreekApplication().deletePartyCurrent();
                        FiiDiiCurrentActivity.this.getGreekApplication().insertPartyCurrent(FiiDiiCurrentActivity.this.f83li);
                    }
                }
            } catch (IOException | Exception unused) {
            }
            int parseInt = Integer.parseInt(strArr[1]);
            FiiDiiCurrentActivity fiiDiiCurrentActivity = FiiDiiCurrentActivity.this;
            fiiDiiCurrentActivity.f83li = fiiDiiCurrentActivity.getGreekApplication().getPartyCurrentOI(FiiDiiCurrentActivity.this.getGreekApplication().getIntrumentNamesFromCurrent().get(parseInt));
            String partyCurrentUpdateDate2 = FiiDiiCurrentActivity.this.getGreekApplication().getPartyCurrentUpdateDate();
            if (partyCurrentUpdateDate2 != null) {
                j = FiiDiiCurrentActivity.this.getGreekApplication().dateFormatter(partyCurrentUpdateDate2, Constants.DT_FMT_yyyy_MM_dd_HH_m_ss).getTime();
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.DT_FMT_dd_MMM_yyyy);
            FiiDiiCurrentActivity.this.strLatestDate = simpleDateFormat.format(new Date(j));
            return FiiDiiCurrentActivity.this.f83li;
        }

        /* access modifiers changed from: protected */
        public void onPreExecute() {
            super.onPreExecute();
            FiiDiiCurrentActivity.this.progressBar.setVisibility(0);
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(List<PartyRec> list) {
            super.onPostExecute(list);
            if (list.size() == 0) {
                FiiDiiCurrentActivity.this.setContentView((int) R.layout.option_details_empty);
                FiiDiiCurrentActivity.this.progressBar.setVisibility(4);
                return;
            }
            PartyRec partyRec = list.get(0);
            FiiDiiCurrentActivity.this.tvPartyHeading.setText(partyRec.getInstrument());
            TextView textView = FiiDiiCurrentActivity.this.tvEODDate;
            textView.setText("EOD update : " + FiiDiiCurrentActivity.this.strLatestDate);
            FiiDiiCurrentActivity.this.tvParty1.setText(partyRec.getParty());
            TextView textView2 = FiiDiiCurrentActivity.this.tvParty1LongOI;
            textView2.setText("" + String.format("%,d", new Object[]{Integer.valueOf(partyRec.getLongPositions())}));
            TextView textView3 = FiiDiiCurrentActivity.this.tvParty1ShortOI;
            textView3.setText("" + String.format("%,d", new Object[]{Integer.valueOf(partyRec.getShortPositions())}));
            int longPositions = partyRec.getLongPositions() - partyRec.getShortPositions();
            TextView textView4 = FiiDiiCurrentActivity.this.tvParty1NetOI;
            textView4.setText("" + String.format("%,d", new Object[]{Integer.valueOf(longPositions)}));
            if (longPositions >= 0) {
                FiiDiiCurrentActivity.this.tvParty1NetOI.setTextColor(Color.parseColor("#2E8B57"));
            } else {
                FiiDiiCurrentActivity.this.tvParty1NetOI.setTextColor(SupportMenu.CATEGORY_MASK);
            }
            if (list.size() == 4) {
                PartyRec partyRec2 = list.get(1);
                FiiDiiCurrentActivity.this.tvParty2.setText(partyRec2.getParty());
                TextView textView5 = FiiDiiCurrentActivity.this.tvParty2LongOI;
                textView5.setText("" + String.format("%,d", new Object[]{Integer.valueOf(partyRec2.getLongPositions())}));
                TextView textView6 = FiiDiiCurrentActivity.this.tvParty2ShortOI;
                textView6.setText("" + String.format("%,d", new Object[]{Integer.valueOf(partyRec2.getShortPositions())}));
                int longPositions2 = partyRec2.getLongPositions() - partyRec2.getShortPositions();
                TextView textView7 = FiiDiiCurrentActivity.this.tvParty2NetOI;
                textView7.setText("" + String.format("%,d", new Object[]{Integer.valueOf(longPositions2)}));
                if (longPositions2 >= 0) {
                    FiiDiiCurrentActivity.this.tvParty2NetOI.setTextColor(Color.parseColor("#2E8B57"));
                } else {
                    FiiDiiCurrentActivity.this.tvParty2NetOI.setTextColor(SupportMenu.CATEGORY_MASK);
                }
                PartyRec partyRec3 = list.get(2);
                FiiDiiCurrentActivity.this.tvParty3.setText(partyRec3.getParty());
                TextView textView8 = FiiDiiCurrentActivity.this.tvParty3LongOI;
                textView8.setText("" + String.format("%,d", new Object[]{Integer.valueOf(partyRec3.getLongPositions())}));
                TextView textView9 = FiiDiiCurrentActivity.this.tvParty3ShortOI;
                textView9.setText("" + String.format("%,d", new Object[]{Integer.valueOf(partyRec3.getShortPositions())}));
                int longPositions3 = partyRec3.getLongPositions() - partyRec3.getShortPositions();
                TextView textView10 = FiiDiiCurrentActivity.this.tvParty3NetOI;
                textView10.setText("" + String.format("%,d", new Object[]{Integer.valueOf(longPositions3)}));
                if (longPositions3 >= 0) {
                    FiiDiiCurrentActivity.this.tvParty3NetOI.setTextColor(Color.parseColor("#2E8B57"));
                } else {
                    FiiDiiCurrentActivity.this.tvParty3NetOI.setTextColor(SupportMenu.CATEGORY_MASK);
                }
                PartyRec partyRec4 = list.get(3);
                FiiDiiCurrentActivity.this.tvParty4.setText(partyRec4.getParty());
                TextView textView11 = FiiDiiCurrentActivity.this.tvParty4LongOI;
                textView11.setText("" + String.format("%,d", new Object[]{Integer.valueOf(partyRec4.getLongPositions())}));
                TextView textView12 = FiiDiiCurrentActivity.this.tvParty4ShortOI;
                textView12.setText("" + String.format("%,d", new Object[]{Integer.valueOf(partyRec4.getShortPositions())}));
                int longPositions4 = partyRec4.getLongPositions() - partyRec4.getShortPositions();
                TextView textView13 = FiiDiiCurrentActivity.this.tvParty4NetOI;
                textView13.setText("" + String.format("%,d", new Object[]{Integer.valueOf(longPositions4)}));
                if (longPositions4 >= 0) {
                    FiiDiiCurrentActivity.this.tvParty4NetOI.setTextColor(Color.parseColor("#2E8B57"));
                } else {
                    FiiDiiCurrentActivity.this.tvParty4NetOI.setTextColor(SupportMenu.CATEGORY_MASK);
                }
            }
            if (FiiDiiCurrentActivity.this.spinnerInst.getAdapter() == null) {
                FiiDiiCurrentActivity.this.loadSpinnerIntrument();
            }
            FiiDiiCurrentActivity.this.progressBar.setVisibility(4);
        }
    }
}
