package bulltrack.com.optionanalyzer.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import bulltrack.com.optionanalyzer.application.MyGreeksApplication;
import bulltrack.com.optionanalyzer.constants.Constants;
import bulltrack.com.optionanalyzer.dao.StrikeExpiry;
import bulltrack.com.optiongreeks13.R;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.json.JSONObject;

public class AdvGreekSearchActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Button btnSearch;
    List<String> liStock;
    ProgressBar progressBar;
    Spinner spinnerExch;
    Spinner spinnerExpiry;
    Spinner spinnerStock;
    Spinner spinnerStrikeFrom;
    Spinner spinnerStrikeTo;
    String webServiceUrl = Constants.URL_SERVICE;

    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
        Spinner spinner = (Spinner) adapterView;
        if (spinner.getId() == R.id.spnr_adv_search_stock) {
            loadSpinnerExpiry(adapterView.getItemAtPosition(i).toString());
        } else if (spinner.getId() == R.id.spnr_adv_search_expiry) {
            Spinner spinner2 = this.spinnerExpiry;
            String obj = spinner2.getItemAtPosition(spinner2.getSelectedItemPosition()).toString();
            Spinner spinner3 = this.spinnerStock;
            String obj2 = spinner3.getItemAtPosition(spinner3.getSelectedItemPosition()).toString();
            loadSpinnerStrikeFrom(obj2, obj);
            loadSpinnerStrikeTo(obj2, obj);
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.adv_greek_search_screen);
        this.spinnerExch = (Spinner) findViewById(R.id.spnr_adv_search_exch);
        this.spinnerStock = (Spinner) findViewById(R.id.spnr_adv_search_stock);
        this.spinnerExpiry = (Spinner) findViewById(R.id.spnr_adv_search_expiry);
        this.spinnerStrikeFrom = (Spinner) findViewById(R.id.spnr_adv_search_strike_from);
        this.spinnerStrikeTo = (Spinner) findViewById(R.id.spnr_adv_search_strike_to);
        this.progressBar = (ProgressBar) findViewById(R.id.progressbar_adv_search);
        this.spinnerStock.setOnItemSelectedListener(this);
        this.spinnerExpiry.setOnItemSelectedListener(this);
        loadSpinnerExch();
        new AsyncListViewLoader().execute(new String[]{"8"});
        Button button = (Button) findViewById(R.id.btn_adv_search_ok);
        this.btnSearch = button;
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String dateFormatter = AdvGreekSearchActivity.this.getGreekApplication().dateFormatter(AdvGreekSearchActivity.this.getGreekApplication().dateFormatter(AdvGreekSearchActivity.this.spinnerExpiry.getItemAtPosition(AdvGreekSearchActivity.this.spinnerExpiry.getSelectedItemPosition()).toString(), Constants.DT_FMT_dd_MMM_yyyy), Constants.DT_FMT_yyyy_MM_dd_HH_m_ss);
                Intent intent = new Intent(AdvGreekSearchActivity.this, GreekSearchResultsActivity.class);
                String obj = AdvGreekSearchActivity.this.spinnerStock.getItemAtPosition(AdvGreekSearchActivity.this.spinnerStock.getSelectedItemPosition()).toString();
                AdvGreekSearchActivity.this.spinnerExpiry.getItemAtPosition(AdvGreekSearchActivity.this.spinnerExpiry.getSelectedItemPosition()).toString();
                String obj2 = AdvGreekSearchActivity.this.spinnerStrikeFrom.getItemAtPosition(AdvGreekSearchActivity.this.spinnerStrikeFrom.getSelectedItemPosition()).toString();
                String obj3 = AdvGreekSearchActivity.this.spinnerStrikeTo.getItemAtPosition(AdvGreekSearchActivity.this.spinnerStrikeTo.getSelectedItemPosition()).toString();
                intent.putExtra("stock", obj);
                intent.putExtra("expiry", dateFormatter);
                intent.putExtra("strikefrom", obj2);
                intent.putExtra("striketo", obj3);
                intent.putExtra("callorput", "PC");
                AdvGreekSearchActivity.this.startActivity(intent);
            }
        });
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

    private void loadSpinnerExch() {
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, 17367048, new String[]{"National Stock Exch- NSE"});
        arrayAdapter.setDropDownViewResource(17367049);
        this.spinnerExch.setAdapter(arrayAdapter);
    }

    /* access modifiers changed from: private */
    public void loadSpinnerStock() {
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, 17367048, getGreekApplication().getAllStockNames());
        arrayAdapter.setDropDownViewResource(17367049);
        this.spinnerStock.setAdapter(arrayAdapter);
    }

    private void loadSpinnerStrikeFrom(String str, String str2) {
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, 17367048, getGreekApplication().getAllStrikes(str, str2));
        arrayAdapter.setDropDownViewResource(17367049);
        this.spinnerStrikeFrom.setAdapter(arrayAdapter);
    }

    private void loadSpinnerStrikeTo(String str, String str2) {
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, 17367048, getGreekApplication().getAllStrikes(str, str2));
        arrayAdapter.setDropDownViewResource(17367049);
        this.spinnerStrikeTo.setAdapter(arrayAdapter);
    }

    private void loadSpinnerExpiry(String str) {
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, 17367048, getGreekApplication().getAllExpiries(str));
        arrayAdapter.setDropDownViewResource(17367049);
        this.spinnerExpiry.setAdapter(arrayAdapter);
    }

    /* access modifiers changed from: protected */
    public MyGreeksApplication getGreekApplication() {
        return (MyGreeksApplication) getApplicationContext();
    }

    private class AsyncListViewLoader extends AsyncTask<String, Void, List<String>> {
        private AsyncListViewLoader() {
        }

        /* access modifiers changed from: protected */
        public List<String> doInBackground(String... strArr) {
            AdvGreekSearchActivity.this.liStock = new ArrayList();
            String strikesAndExpiriesUpdDate = AdvGreekSearchActivity.this.getGreekApplication().getStrikesAndExpiriesUpdDate();
            try {
                HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(AdvGreekSearchActivity.this.webServiceUrl + "9").openConnection();
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestProperty("Accept-Charset", "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8"));
                String readLine = bufferedReader.readLine();
                bufferedReader.close();
                JSONObject jSONObject = new JSONObject(readLine);
                String obj = jSONObject.get("priceDateTime").toString();
                jSONObject.get("greekDateTime").toString();
                Calendar instance = Calendar.getInstance();
                instance.setTime(AdvGreekSearchActivity.this.getGreekApplication().dateFormatter(obj, Constants.DT_FMT_yyyy_MM_dd_HH_m_ss));
                instance.set(11, 0);
                instance.set(12, 0);
                instance.set(13, 0);
                instance.set(14, 0);
                long timeInMillis = instance.getTimeInMillis();
                long time = strikesAndExpiriesUpdDate != null ? AdvGreekSearchActivity.this.getGreekApplication().dateFormatter(strikesAndExpiriesUpdDate, Constants.DT_FMT_yyyy_MM_dd_HH_m_ss).getTime() : 0;
                if (strikesAndExpiriesUpdDate == null || time < timeInMillis) {
                    HttpURLConnection httpURLConnection2 = (HttpURLConnection) new URL(AdvGreekSearchActivity.this.webServiceUrl + strArr[0]).openConnection();
                    httpURLConnection2.setConnectTimeout(5000);
                    httpURLConnection2.setDoOutput(true);
                    httpURLConnection2.setRequestProperty("Accept-Charset", "UTF-8");
                    List list = (List) new GsonBuilder().setDateFormat(Constants.DT_FMT_yyyy_MM_dd_HH_m_ss).create().fromJson(new BufferedReader(new InputStreamReader(httpURLConnection2.getInputStream(), "UTF-8")).readLine(), new TypeToken<List<StrikeExpiry>>() {
                    }.getType());
                    System.out.println(((StrikeExpiry) list.get(0)).getSymbol());
                    System.out.println(list.size());
                    AdvGreekSearchActivity.this.getGreekApplication().deleteAllStrikesAndExpiries();
                    AdvGreekSearchActivity.this.getGreekApplication().insertAllStrikesAndExpiries(list);
                }
            } catch (IOException e) {
                System.out.println(e.getStackTrace());
            } catch (Exception e2) {
                System.out.println(e2.getStackTrace());
            }
            AdvGreekSearchActivity advGreekSearchActivity = AdvGreekSearchActivity.this;
            advGreekSearchActivity.liStock = advGreekSearchActivity.getGreekApplication().getAllStockNames();
            return AdvGreekSearchActivity.this.liStock;
        }

        /* access modifiers changed from: protected */
        public void onPreExecute() {
            super.onPreExecute();
            AdvGreekSearchActivity.this.progressBar.setVisibility(0);
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(List<String> list) {
            super.onPostExecute(list);
            AdvGreekSearchActivity.this.loadSpinnerStock();
            AdvGreekSearchActivity.this.progressBar.setVisibility(4);
        }
    }
}
