package bulltrack.com.optionanalyzer.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import bulltrack.com.optionanalyzer.adapter.AdapterFinderLoad;
import bulltrack.com.optionanalyzer.application.MyGreeksApplication;
import bulltrack.com.optionanalyzer.constants.Constants;
import bulltrack.com.optionanalyzer.dao.RTData;
import bulltrack.com.optionanalyzer.dao.StrategyResultsFilter;
import bulltrack.com.optiongreeks13.R;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

public class FinderFolio extends AppCompatActivity {
    /* access modifiers changed from: private */
    public String TAG = "FinderFolio";
    Activity activity;
    List<StrategyResultsFilter> lstStgResults;
    ListView lvStrategies;
    AdapterFinderLoad mainAdapter;
    String webServiceUrl = Constants.URL_SERVICE;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.activity = this;
        setContentView((int) R.layout.activity_legs_progress);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        webCallStockPrice();
    }

    public void webCallStockPrice() {
        Gson create = new GsonBuilder().setDateFormat(Constants.DT_FMT_yyyy_MM_dd_HH_m_ss).create();
        try {
            List<String> folioStockList = getGreekApplication().getFolioStockList();
            StringRequest stringRequest = new StringRequest(0, String.format(this.webServiceUrl + "%1$s&condition=%2$s", new Object[]{"104", URLEncoder.encode(create.toJson((Object) folioStockList), "UTF-8")}), new Response.Listener<String>() {
                public void onResponse(String str) {
                    HashMap hashMap = new HashMap();
                    try {
                        List list = (List) new GsonBuilder().setDateFormat(Constants.DT_FMT_yyyy_MM_dd_HH_m_ss).create().fromJson(str.toString(), new TypeToken<List<RTData>>() {
                        }.getType());
                        for (int i = 0; i < list.size(); i++) {
                            hashMap.put(((RTData) list.get(i)).getSymbol(), list.get(i));
                        }
                        FinderFolio.this.lstStgResults = FinderFolio.this.getGreekApplication().getStrategyResultsFromDB();
                        for (int i2 = 0; i2 < FinderFolio.this.lstStgResults.size(); i2++) {
                            RTData rTData = (RTData) hashMap.get(FinderFolio.this.lstStgResults.get(i2).getSymbol());
                            FinderFolio.this.lstStgResults.get(i2).setInvestment(rTData.getClosePrice());
                            FinderFolio.this.lstStgResults.get(i2).setInterestCost(rTData.getPrevClose());
                            FinderFolio.this.lstStgResults.get(i2).setLotSize(rTData.getMarketLot());
                        }
                        FinderFolio.this.setContentView((int) R.layout.activity_finder_folio);
                        FinderFolio.this.lvStrategies = (ListView) FinderFolio.this.findViewById(R.id.lv_finder_folio);
                        FinderFolio.this.lvStrategies.setEmptyView(FinderFolio.this.findViewById(R.id.tv_finder_folio_empty));
                        FinderFolio.this.lvStrategies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            /* JADX WARNING: type inference failed for: r1v0, types: [android.widget.AdapterView<?>, android.widget.AdapterView] */
                            /* JADX WARNING: Unknown variable types count: 1 */
                            /* Code decompiled incorrectly, please refer to instructions dump. */
                            public void onItemClick(AdapterView<?> r1, android.view.View r2, int r3, long r4) {
                                /*
                                    r0 = this;
                                    android.widget.Adapter r1 = r1.getAdapter()
                                    java.lang.Object r1 = r1.getItem(r3)
                                    bulltrack.com.optionanalyzer.dao.StrategyResultsFilter r1 = (bulltrack.com.optionanalyzer.dao.StrategyResultsFilter) r1
                                    bulltrack.com.optionanalyzer.activity.FinderFolio$1 r2 = bulltrack.com.optionanalyzer.activity.FinderFolio.C05961.this
                                    bulltrack.com.optionanalyzer.activity.FinderFolio r2 = bulltrack.com.optionanalyzer.activity.FinderFolio.this
                                    bulltrack.com.optionanalyzer.activity.FinderFolio$1 r3 = bulltrack.com.optionanalyzer.activity.FinderFolio.C05961.this
                                    bulltrack.com.optionanalyzer.activity.FinderFolio r3 = bulltrack.com.optionanalyzer.activity.FinderFolio.this
                                    android.content.Context r3 = r3.getApplicationContext()
                                    r2.startViewLegsActivity(r3, r1)
                                    return
                                */
                                throw new UnsupportedOperationException("Method not decompiled: bulltrack.com.optionanalyzer.activity.FinderFolio.C05961.C05982.onItemClick(android.widget.AdapterView, android.view.View, int, long):void");
                            }
                        });
                        FinderFolio.this.mainAdapter = new AdapterFinderLoad(FinderFolio.this.activity, FinderFolio.this.lstStgResults, false);
                        FinderFolio.this.lvStrategies.setAdapter(FinderFolio.this.mainAdapter);
                        FinderFolio.this.mainAdapter.setItemList(FinderFolio.this.lstStgResults);
                        FinderFolio.this.mainAdapter.notifyDataSetChanged();
                        FinderFolio.this.lvStrategies.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                            /* JADX WARNING: type inference failed for: r2v0, types: [android.widget.AdapterView<?>, android.widget.AdapterView] */
                            /* JADX WARNING: Unknown variable types count: 1 */
                            /* Code decompiled incorrectly, please refer to instructions dump. */
                            public boolean onItemLongClick(AdapterView<?> r2, android.view.View r3, final int r4, long r5) {
                                /*
                                    r1 = this;
                                    android.widget.Adapter r2 = r2.getAdapter()
                                    java.lang.Object r2 = r2.getItem(r4)
                                    bulltrack.com.optionanalyzer.dao.StrategyResultsFilter r2 = (bulltrack.com.optionanalyzer.dao.StrategyResultsFilter) r2
                                    java.lang.String r3 = r2.getStrategyName()
                                    android.app.AlertDialog$Builder r5 = new android.app.AlertDialog$Builder
                                    bulltrack.com.optionanalyzer.activity.FinderFolio$1 r6 = bulltrack.com.optionanalyzer.activity.FinderFolio.C05961.this
                                    bulltrack.com.optionanalyzer.activity.FinderFolio r6 = bulltrack.com.optionanalyzer.activity.FinderFolio.this
                                    android.app.Activity r6 = r6.activity
                                    r5.<init>(r6)
                                    java.lang.StringBuilder r6 = new java.lang.StringBuilder
                                    r6.<init>()
                                    java.lang.String r0 = "Do you want to remove "
                                    r6.append(r0)
                                    r6.append(r3)
                                    java.lang.String r0 = "?"
                                    r6.append(r0)
                                    java.lang.String r6 = r6.toString()
                                    r5.setMessage(r6)
                                    r6 = 0
                                    r5.setCancelable(r6)
                                    bulltrack.com.optionanalyzer.activity.FinderFolio$1$3$1 r6 = new bulltrack.com.optionanalyzer.activity.FinderFolio$1$3$1
                                    r6.<init>(r2, r4, r3)
                                    java.lang.String r2 = "Yes"
                                    r5.setPositiveButton(r2, r6)
                                    bulltrack.com.optionanalyzer.activity.FinderFolio$1$3$2 r2 = new bulltrack.com.optionanalyzer.activity.FinderFolio$1$3$2
                                    r2.<init>()
                                    java.lang.String r3 = "No"
                                    r5.setNegativeButton(r3, r2)
                                    r5.show()
                                    r2 = 1
                                    return r2
                                */
                                throw new UnsupportedOperationException("Method not decompiled: bulltrack.com.optionanalyzer.activity.FinderFolio.C05961.C05993.onItemLongClick(android.widget.AdapterView, android.view.View, int, long):boolean");
                            }
                        });
                    } catch (Exception e) {
                        String access$000 = FinderFolio.this.TAG;
                        Log.d(access$000, "Error | Volley Call " + e.toString());
                        FinderFolio.this.setContentView((int) R.layout.chart_error_message);
                    }
                }
            }, new Response.ErrorListener() {
                public void onErrorResponse(VolleyError volleyError) {
                    String access$000 = FinderFolio.this.TAG;
                    VolleyLog.d(access$000, "Error: " + volleyError.getMessage());
                    FinderFolio.this.setContentView((int) R.layout.chart_error_message);
                }
            });
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, 1, 1.0f));
            getGreekApplication();
            MyGreeksApplication.getInstance().addToRequestQueue(stringRequest);
        } catch (Exception e) {
            Log.d(this.TAG, e.getMessage());
        }
    }

    public void startViewLegsActivity(Context context, StrategyResultsFilter strategyResultsFilter) {
        Intent intent = new Intent(context, FinderLegsActivity.class);
        intent.putExtra("result", strategyResultsFilter);
        intent.putExtra("source", Constants.STG_LEGS_LAUNCHFROM_FOLIO);
        startActivityForResult(intent, 1010);
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 1010) {
            finish();
            startActivity(getIntent());
        }
    }

    /* access modifiers changed from: protected */
    public MyGreeksApplication getGreekApplication() {
        return (MyGreeksApplication) getApplicationContext();
    }
}
