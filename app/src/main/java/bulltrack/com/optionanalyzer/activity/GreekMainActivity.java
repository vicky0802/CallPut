package bulltrack.com.optionanalyzer.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.NavUtils;
import androidx.core.internal.view.SupportMenuItem;
import bulltrack.com.optionanalyzer.adapter.GreeksListAdapter;
import bulltrack.com.optionanalyzer.adapter.GridViewAdapter;
import bulltrack.com.optionanalyzer.application.MyGreeksApplication;
import bulltrack.com.optionanalyzer.constants.Constants;
import bulltrack.com.optionanalyzer.constants.ConstantsDropDown;
import bulltrack.com.optionanalyzer.dao.GreekValues;
import bulltrack.com.optiongreeks13.R;
import com.exblr.dropdownmenu.DropdownListItem;
import com.exblr.dropdownmenu.DropdownMenu;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

public class GreekMainActivity extends AppCompatActivity {
    /* access modifiers changed from: private */
    public String TAG = "GreekMainActivity";
    /* access modifiers changed from: private */
    public Activity activity;
    GreeksListAdapter adapter;
    AsyncListViewLoader asyncTask;
    String expirySelection = ConstantsDropDown.MENU_LIST_EXPIRY_NEAR;

    /* renamed from: li */
    List<GreekValues> f85li;
    private ArrayList listExpiry = createMonthList();
    private ArrayList listStock = createStockList();
    ListView lvGreeks;
    DropdownMenu mDropdownMenu;
    /* access modifiers changed from: private */
    public GridViewAdapter mGridViewAdapterExpiry;
    /* access modifiers changed from: private */
    public GridViewAdapter mGridViewAdapterStock;
    ProgressBar progressBar;
    String refreshId = "11";
    String stockSelection = ConstantsDropDown.MENU_LIST_STOCK_NIFTY;
    String webServiceUrl = Constants.URL_SERVICE;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.content_main);
        this.activity = this;
        ((AdView) findViewById(R.id.adView)).loadAd(new AdRequest.Builder().setRequestAgent("android_studio:ad_template").build());
        this.progressBar = (ProgressBar) findViewById(R.id.progressbar_content_main);
        ListView listView = (ListView) findViewById(R.id.lv_fragment_tab1_optiongreeks);
        this.lvGreeks = listView;
        listView.setEmptyView(findViewById(R.id.empty));
        ConfigureDropDownMenu();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
                    bulltrack.com.optionanalyzer.activity.GreekMainActivity r2 = bulltrack.com.optionanalyzer.activity.GreekMainActivity.this
                    bulltrack.com.optionanalyzer.application.MyGreeksApplication r2 = r2.getGreekApplication()
                    bulltrack.com.optionanalyzer.activity.GreekMainActivity r3 = bulltrack.com.optionanalyzer.activity.GreekMainActivity.this
                    android.content.Context r3 = r3.getApplicationContext()
                    r4 = 0
                    r2.startViewOptionDetailsActivity(r3, r1, r4)
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: bulltrack.com.optionanalyzer.activity.GreekMainActivity.C06371.onItemClick(android.widget.AdapterView, android.view.View, int, long):void");
            }
        });
        new AsyncListViewLoader().execute(new String[]{"11"});
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        getMenuInflater().inflate(R.menu.action_search_strike, menu);
        SupportMenuItem supportMenuItem = (SupportMenuItem) menu.findItem(R.id.action_search_strike_1);
        SearchView searchView = (SearchView) supportMenuItem.getActionView();
        searchView.setQueryHint("Strike or Stock");
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View view, boolean z) {
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            public boolean onQueryTextSubmit(String str) {
                return false;
            }

            public boolean onQueryTextChange(String str) {
                if (GreekMainActivity.this.adapter == null || GreekMainActivity.this.lvGreeks == null) {
                    return true;
                }
                GreekMainActivity.this.adapter.filter(str.toString().trim());
                GreekMainActivity.this.lvGreeks.invalidate();
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
        new AsyncListViewLoader().execute(new String[]{"11"});
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId != 16908332) {
            if (itemId == R.id.action_refresh) {
                DisplayOptions();
            }
            return super.onOptionsItemSelected(menuItem);
        } else if (getParent() == null) {
            onBackPressed();
            return true;
        } else {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
    }

    private ArrayList createMonthList() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new DropdownListItem(0, ConstantsDropDown.MENU_LIST_EXPIRY_NEAR));
        arrayList.add(new DropdownListItem(1, ConstantsDropDown.MENU_LIST_EXPIRY_FAR));
        return arrayList;
    }

    private ArrayList createStockList() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new DropdownListItem(0, ConstantsDropDown.MENU_LIST_STOCK_NIFTY));
        arrayList.add(new DropdownListItem(1, ConstantsDropDown.MENU_LIST_STOCK_BANKNIFTY));
        arrayList.add(new DropdownListItem(2, ConstantsDropDown.MENU_LIST_STOCK_MIXED));
        return arrayList;
    }

    private void ConfigureDropDownMenu() {
        this.mGridViewAdapterStock = new GridViewAdapter(this, this.listStock);
        View inflate = getLayoutInflater().inflate(R.layout.ddm_custom_content, (ViewGroup) null, false);
        GridView gridView = (GridView) inflate.findViewById(R.id.ddm_custom_content_gv);
        gridView.setNumColumns(3);
        gridView.setAdapter(this.mGridViewAdapterStock);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                DropdownListItem selectedItem = GreekMainActivity.this.mGridViewAdapterStock.setSelectedItem(i);
                GreekMainActivity.this.mDropdownMenu.setCurrentTitle(selectedItem.getText());
                GreekMainActivity.this.mDropdownMenu.dismissCurrentPopupWindow();
                GreekMainActivity.this.stockSelection = selectedItem.getText();
                GreekMainActivity.this.DisplayOptions();
            }
        });
        this.mGridViewAdapterExpiry = new GridViewAdapter(this, this.listExpiry);
        View inflate2 = getLayoutInflater().inflate(R.layout.ddm_custom_content, (ViewGroup) null, false);
        GridView gridView2 = (GridView) inflate2.findViewById(R.id.ddm_custom_content_gv);
        gridView2.setNumColumns(2);
        gridView2.setAdapter(this.mGridViewAdapterExpiry);
        gridView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                DropdownListItem selectedItem = GreekMainActivity.this.mGridViewAdapterExpiry.setSelectedItem(i);
                GreekMainActivity.this.mDropdownMenu.setCurrentTitle(selectedItem.getText());
                GreekMainActivity.this.mDropdownMenu.dismissCurrentPopupWindow();
                GreekMainActivity.this.expirySelection = selectedItem.getText();
                GreekMainActivity.this.DisplayOptions();
            }
        });
        DropdownMenu dropdownMenu = (DropdownMenu) findViewById(R.id.dropdown_menu_mainactivity);
        this.mDropdownMenu = dropdownMenu;
        dropdownMenu.add(ConstantsDropDown.MENU_STOCK, inflate);
        this.mDropdownMenu.add(ConstantsDropDown.MENU_EXPIRY, inflate2);
        this.mGridViewAdapterStock.setSelectedItem(0);
        this.mDropdownMenu.dismissCurrentPopupWindow();
        this.mGridViewAdapterExpiry.setSelectedItem(0);
        this.mDropdownMenu.dismissCurrentPopupWindow();
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x003d  */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x008f  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void DisplayOptions() {
        /*
            r6 = this;
            java.lang.String r0 = r6.stockSelection
            int r1 = r0.hashCode()
            r2 = 74357723(0x46e9bdb, float:2.804833E-36)
            r3 = 2
            r4 = 0
            r5 = 1
            if (r1 == r2) goto L_0x002d
            r2 = 75264432(0x47c71b0, float:2.967466E-36)
            if (r1 == r2) goto L_0x0023
            r2 = 200741076(0xbf710d4, float:9.516626E-32)
            if (r1 == r2) goto L_0x0019
            goto L_0x0037
        L_0x0019:
            java.lang.String r1 = "BankNifty"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x0037
            r0 = 1
            goto L_0x0038
        L_0x0023:
            java.lang.String r1 = "Nifty"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x0037
            r0 = 0
            goto L_0x0038
        L_0x002d:
            java.lang.String r1 = "Mixed"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x0037
            r0 = 2
            goto L_0x0038
        L_0x0037:
            r0 = -1
        L_0x0038:
            java.lang.String r1 = "This Series"
            r2 = 0
            if (r0 == 0) goto L_0x008f
            if (r0 == r5) goto L_0x0069
            if (r0 == r3) goto L_0x0043
            goto L_0x00b4
        L_0x0043:
            java.lang.String r0 = r6.expirySelection
            boolean r0 = r0.equalsIgnoreCase(r1)
            if (r0 == 0) goto L_0x005a
            bulltrack.com.optionanalyzer.activity.GreekMainActivity$AsyncListViewLoader r0 = new bulltrack.com.optionanalyzer.activity.GreekMainActivity$AsyncListViewLoader
            r0.<init>()
            java.lang.String[] r1 = new java.lang.String[r5]
            java.lang.String r2 = "10"
            r1[r4] = r2
            r0.execute(r1)
            goto L_0x00b4
        L_0x005a:
            bulltrack.com.optionanalyzer.activity.GreekMainActivity$AsyncListViewLoader r0 = new bulltrack.com.optionanalyzer.activity.GreekMainActivity$AsyncListViewLoader
            r0.<init>()
            java.lang.String[] r1 = new java.lang.String[r5]
            java.lang.String r2 = "13"
            r1[r4] = r2
            r0.execute(r1)
            goto L_0x00b4
        L_0x0069:
            java.lang.String r0 = r6.expirySelection
            boolean r0 = r0.equalsIgnoreCase(r1)
            if (r0 == 0) goto L_0x0080
            bulltrack.com.optionanalyzer.activity.GreekMainActivity$AsyncListViewLoader r0 = new bulltrack.com.optionanalyzer.activity.GreekMainActivity$AsyncListViewLoader
            r0.<init>()
            java.lang.String[] r1 = new java.lang.String[r5]
            java.lang.String r2 = "12"
            r1[r4] = r2
            r0.execute(r1)
            goto L_0x00b4
        L_0x0080:
            bulltrack.com.optionanalyzer.activity.GreekMainActivity$AsyncListViewLoader r0 = new bulltrack.com.optionanalyzer.activity.GreekMainActivity$AsyncListViewLoader
            r0.<init>()
            java.lang.String[] r1 = new java.lang.String[r5]
            java.lang.String r2 = "15"
            r1[r4] = r2
            r0.execute(r1)
            goto L_0x00b4
        L_0x008f:
            java.lang.String r0 = r6.expirySelection
            boolean r0 = r0.equalsIgnoreCase(r1)
            if (r0 == 0) goto L_0x00a6
            bulltrack.com.optionanalyzer.activity.GreekMainActivity$AsyncListViewLoader r0 = new bulltrack.com.optionanalyzer.activity.GreekMainActivity$AsyncListViewLoader
            r0.<init>()
            java.lang.String[] r1 = new java.lang.String[r5]
            java.lang.String r2 = "11"
            r1[r4] = r2
            r0.execute(r1)
            goto L_0x00b4
        L_0x00a6:
            bulltrack.com.optionanalyzer.activity.GreekMainActivity$AsyncListViewLoader r0 = new bulltrack.com.optionanalyzer.activity.GreekMainActivity$AsyncListViewLoader
            r0.<init>()
            java.lang.String[] r1 = new java.lang.String[r5]
            java.lang.String r2 = "14"
            r1[r4] = r2
            r0.execute(r1)
        L_0x00b4:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: bulltrack.com.optionanalyzer.activity.GreekMainActivity.DisplayOptions():void");
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
            GreekMainActivity.this.f85li = new ArrayList();
            String greeksUpdateDate = GreekMainActivity.this.getGreekApplication().getGreeksUpdateDate(strArr[0]);
            String priceUpdateDate = GreekMainActivity.this.getGreekApplication().getPriceUpdateDate(strArr[0]);
            try {
                if (GreekMainActivity.this.getGreekApplication().isInternetAvailable()) {
                    HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(GreekMainActivity.this.webServiceUrl + "9").openConnection();
                    httpURLConnection.setConnectTimeout(5000);
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setRequestProperty("Accept-Charset", "UTF-8");
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8"));
                    String readLine = bufferedReader.readLine();
                    bufferedReader.close();
                    JSONObject jSONObject = new JSONObject(readLine);
                    String obj = jSONObject.get("priceDateTime").toString();
                    String obj2 = jSONObject.get("greekDateTime").toString();
                    long time = GreekMainActivity.this.getGreekApplication().dateFormatter(obj, Constants.DT_FMT_yyyy_MM_dd_HH_m_ss).getTime();
                    long time2 = GreekMainActivity.this.getGreekApplication().dateFormatter(obj2, Constants.DT_FMT_yyyy_MM_dd_HH_m_ss).getTime();
                    long time3 = priceUpdateDate != null ? GreekMainActivity.this.getGreekApplication().dateFormatter(priceUpdateDate, Constants.DT_FMT_yyyy_MM_dd_HH_m_ss).getTime() : 0;
                    long time4 = greeksUpdateDate != null ? GreekMainActivity.this.getGreekApplication().dateFormatter(greeksUpdateDate, Constants.DT_FMT_yyyy_MM_dd_HH_m_ss).getTime() : 0;
                    if (!(obj == null || obj2 == null || (time4 >= time2 && time3 >= time))) {
                        HttpURLConnection httpURLConnection2 = (HttpURLConnection) new URL(GreekMainActivity.this.webServiceUrl + strArr[0]).openConnection();
                        httpURLConnection2.setConnectTimeout(Constants.VOLLEY_RESPONSE_TIMEOUT);
                        httpURLConnection2.setDoOutput(true);
                        httpURLConnection2.setRequestProperty("Accept-Charset", "UTF-8");
                        InputStream inputStream = httpURLConnection2.getInputStream();
                        BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                        GreekMainActivity.this.f85li = (List) new GsonBuilder().setDateFormat(Constants.DT_FMT_yyyy_MM_dd_HH_m_ss).create().fromJson(bufferedReader2.readLine(), new TypeToken<List<GreekValues>>() {
                        }.getType());
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        bufferedReader2.close();
                        if (httpURLConnection2 != null) {
                            httpURLConnection2.disconnect();
                        }
                        GreekMainActivity.this.getGreekApplication().deleteAllGreeks(strArr[0]);
                        GreekMainActivity.this.getGreekApplication().insertAllGreeks(GreekMainActivity.this.f85li, strArr[0]);
                    }
                }
            } catch (IOException e) {
                String access$400 = GreekMainActivity.this.TAG;
                Log.d(access$400, "" + e.toString());
            } catch (Exception e2) {
                String access$4002 = GreekMainActivity.this.TAG;
                Log.d(access$4002, "" + e2.toString());
            }
            GreekMainActivity greekMainActivity = GreekMainActivity.this;
            greekMainActivity.f85li = greekMainActivity.getGreekApplication().getAllGreeks(strArr[0]);
            return GreekMainActivity.this.f85li;
        }

        /* access modifiers changed from: protected */
        public void onPreExecute() {
            super.onPreExecute();
            GreekMainActivity.this.progressBar.setVisibility(0);
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(List<GreekValues> list) {
            super.onPostExecute(list);
            GreekMainActivity.this.adapter = new GreeksListAdapter(GreekMainActivity.this.activity, list);
            if (GreekMainActivity.this.lvGreeks != null) {
                GreekMainActivity.this.lvGreeks.setAdapter(GreekMainActivity.this.adapter);
                GreekMainActivity.this.adapter.setItemList(list);
                GreekMainActivity.this.adapter.notifyDataSetChanged();
            }
            GreekMainActivity.this.progressBar.setVisibility(4);
        }
    }
}
