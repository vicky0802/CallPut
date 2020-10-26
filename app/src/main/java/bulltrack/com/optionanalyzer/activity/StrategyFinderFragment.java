package bulltrack.com.optionanalyzer.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import bulltrack.com.optionanalyzer.adapter.AdapterFinderLoad;
import bulltrack.com.optionanalyzer.adapter.EndlessScrollListener;
import bulltrack.com.optionanalyzer.application.MyGreeksApplication;
import bulltrack.com.optionanalyzer.constants.Constants;
import bulltrack.com.optionanalyzer.dao.FilterSelection;
import bulltrack.com.optionanalyzer.dao.StrategyResultsFilter;
import bulltrack.com.optionanalyzer.utility.billing.IabHelper;
import bulltrack.com.optionanalyzer.utility.billing.IabResult;
import bulltrack.com.optionanalyzer.utility.billing.Inventory;
import bulltrack.com.optionanalyzer.utility.billing.Purchase;
import bulltrack.com.optiongreeks13.R;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

public class StrategyFinderFragment extends Fragment {
    public static final int LAST_UPDATE_INDEX = 0;
    public static final int LIST_STG_INDEX = 1;
    static final int RC_REQUEST = 10003;
    static final String SKU_GAS = "gas";
    static String SKU_PREMIUM = "01_finder_690";
    /* access modifiers changed from: private */
    public String TAG = "MainActivity";
    MainActivity activity;
    MyGreeksApplication application;
    CheckBox cbTerms;
    Button firstButton;
    View footerView;

    /* renamed from: li */
    List<StrategyResultsFilter> f93li;
    boolean loadUI = true;
    ListView lvStrategies;
    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
        public void onConsumeFinished(Purchase purchase, IabResult iabResult) {
            String access$000 = StrategyFinderFragment.this.TAG;
            Log.d(access$000, "Consumption finished. Purchase: " + purchase + ", result: " + iabResult);
            if (StrategyFinderFragment.this.mHelper != null) {
                if (iabResult.isSuccess()) {
                    Log.d(StrategyFinderFragment.this.TAG, "Consumption successful. Provisioning.");
                } else {
                    StrategyFinderFragment strategyFinderFragment = StrategyFinderFragment.this;
                    strategyFinderFragment.complain("Error while consuming: " + iabResult);
                }
                Log.d(StrategyFinderFragment.this.TAG, "End consumption flow.");
            }
        }
    };
    /* access modifiers changed from: private */
    public Context mContext;
    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult iabResult, Inventory inventory) {
            Log.d(StrategyFinderFragment.this.TAG, "Query inventory finished.");
            if (iabResult.isFailure()) {
                String access$000 = StrategyFinderFragment.this.TAG;
                Log.d(access$000, "Failed to query inventory: " + iabResult);
                return;
            }
            Log.d(StrategyFinderFragment.this.TAG, "Query inventory was successful.");
            try {
                if (inventory.hasPurchase(StrategyFinderFragment.SKU_PREMIUM)) {
                    String access$0002 = StrategyFinderFragment.this.TAG;
                    Log.d(access$0002, "already purchased : " + StrategyFinderFragment.SKU_PREMIUM);
                    StrategyFinderFragment.this.mHelper.consumeAsync(inventory.getPurchase(StrategyFinderFragment.SKU_PREMIUM), StrategyFinderFragment.this.mConsumeFinishedListener);
                }
            } catch (IabHelper.IabAsyncInProgressException unused) {
                StrategyFinderFragment.this.complain("Error launching purchase flow. Another async operation in progress.");
            }
        }
    };
    IabHelper mHelper;
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult iabResult, Purchase purchase) {
            String access$000 = StrategyFinderFragment.this.TAG;
            Log.d(access$000, "Purchase finished: " + iabResult + ", purchase: " + purchase);
            if (StrategyFinderFragment.this.mHelper != null) {
                if (iabResult.isFailure()) {
                    StrategyFinderFragment.this.complain("Payment unsuccessful");
                } else if (!StrategyFinderFragment.this.verifyDeveloperPayload(purchase)) {
                    StrategyFinderFragment.this.complain("Error purchasing. Authenticity verification failed.");
                } else {
                    Log.d(StrategyFinderFragment.this.TAG, "Purchase successful.");
                    if (purchase.getSku().equals(StrategyFinderFragment.SKU_GAS)) {
                        Log.d(StrategyFinderFragment.this.TAG, "Purchase is gas. Starting gas consumption.");
                        try {
                            StrategyFinderFragment.this.mHelper.consumeAsync(purchase, StrategyFinderFragment.this.mConsumeFinishedListener);
                        } catch (IabHelper.IabAsyncInProgressException unused) {
                            StrategyFinderFragment.this.complain("Error consuming gas. Another async operation in progress.");
                        }
                    } else if (purchase.getSku().equals(StrategyFinderFragment.SKU_PREMIUM)) {
                        Log.d(StrategyFinderFragment.this.TAG, "Purchase is premium upgrade. Congratulating user.");
                        StrategyFinderFragment.this.paidSuccess = true;
                        long purchaseTime = purchase.getPurchaseTime();
                        Calendar instance = Calendar.getInstance();
                        if (purchaseTime > 0) {
                            instance.setTimeInMillis(purchaseTime);
                        }
                        instance.add(5, 31);
                        StrategyFinderFragment.this.application.insertFinderAccess(instance.getTimeInMillis());
                        StrategyFinderFragment.this.webServiceCallPayment(purchase.getOriginalJson());
                        if (StrategyFinderFragment.this.lvStrategies != null) {
                            StrategyFinderFragment.this.lvStrategies.invalidate();
                        }
                        StrategyFinderFragment.this.showAds = false;
                        StrategyFinderFragment.this.mainAdapter = new AdapterFinderLoad(StrategyFinderFragment.this.mContext, StrategyFinderFragment.this.f93li, StrategyFinderFragment.this.prodRs, StrategyFinderFragment.this.showAds);
                        StrategyFinderFragment.this.lvStrategies.setAdapter(StrategyFinderFragment.this.mainAdapter);
                        StrategyFinderFragment.this.lvStrategies.setEmptyView(StrategyFinderFragment.this.view.findViewById(R.id.ll_finder_main_empty));
                        StrategyFinderFragment.this.lvStrategies.removeFooterView(StrategyFinderFragment.this.footerView);
                        StrategyFinderFragment.this.mainAdapter.setItemList(StrategyFinderFragment.this.f93li);
                        StrategyFinderFragment.this.mainAdapter.notifyDataSetChanged();
                        StrategyFinderFragment.this.activity.getFilterFragment().intitializeSubscHeader();
                        StrategyFinderFragment.this.alert("Purchase Successful. Thank you!");
                    }
                }
            }
        }
    };
    AdapterFinderLoad mainAdapter;
    int pageSize = 20;
    boolean paidSuccess = false;
    int prodRs;
    SwipeRefreshLayout pullToRefresh;
    boolean showAds = true;
    View view;
    String webServiceUrl = Constants.URL_SERVICE;

    /* access modifiers changed from: protected */
    public int getContentFrameId() {
        return R.id.rl_progress_main;
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        if (this.view == null) {
            this.view = layoutInflater.inflate(R.layout.fragment_strategy_finder, viewGroup, false);
        }
        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) this.view.findViewById(R.id.pull2Ref_finder_main);
        this.pullToRefresh = swipeRefreshLayout;
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            public void onRefresh() {
                try {
                    StrategyFinderFragment.this.webServiceCallFirstLoad();
                } catch (Exception unused) {
                    StrategyFinderFragment.this.setErrorFragment();
                }
                StrategyFinderFragment.this.pullToRefresh.setRefreshing(false);
            }
        });
        SKU_PREMIUM = getArguments().getString(Constants.FINDER_GOOGLE_PROD_ID_KEY);
        this.prodRs = getArguments().getInt(Constants.FINDER_GOOGLE_PROD_PRICE_KEY);
        this.activity = (MainActivity) getActivity();
        try {
            webServiceCallFirstLoad();
        } catch (Exception unused) {
            setErrorFragment();
        }
        googleBilling();
        return this.view;
    }

    public void onDestroyView() {
        if (this.view.getParent() != null) {
            ((ViewGroup) this.view.getParent()).removeView(this.view);
        }
        super.onDestroyView();
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
        this.application = (MyGreeksApplication) getActivity().getApplicationContext();
    }

    public void loadNextDataFromApi(int i) throws Exception {
        FilterSelection newFilterSelection = this.activity.getFilterFragment().getNewFilterSelection();
        this.lvStrategies.removeFooterView(this.footerView);
        this.lvStrategies.addFooterView(this.footerView);
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("min_i", this.pageSize * i);
        jSONObject.put("max_i", ((i + 1) * this.pageSize) - 1);
        List<StrategyResultsFilter> list = this.f93li;
        if (list == null || list.size() <= 0) {
            jSONObject.put("run_seq", -1);
        } else {
            jSONObject.put("run_seq", this.f93li.get(0).getRunSeq());
        }
        if (newFilterSelection != null) {
            jSONObject.put("symbol", newFilterSelection.getSymbol());
            jSONObject.put("trend", newFilterSelection.getTrend());
            jSONObject.put("expiry", newFilterSelection.getExpiry().getTime());
            if (newFilterSelection.getRisk().equalsIgnoreCase(Constants.FILTER_RISK_NOLIMIT)) {
                jSONObject.put("risk", Integer.MAX_VALUE);
            } else {
                jSONObject.put("risk", Integer.parseInt(newFilterSelection.getRisk()));
            }
            jSONObject.put("lowRange", newFilterSelection.getLowRange());
            jSONObject.put("highRange", newFilterSelection.getHighRange());
        }
        StringRequest stringRequest = new StringRequest(0, String.format(this.webServiceUrl + "%1$s&condition=%2$s", new Object[]{"101", URLEncoder.encode(jSONObject.toString(), "UTF-8")}), new Response.Listener<String>() {
            public void onResponse(String str) {
                Log.d(StrategyFinderFragment.this.TAG, "webServiceCallGetFinder got resposne");
                Type type = new TypeToken<List<StrategyResultsFilter>>() {
                }.getType();
                String[] access$100 = StrategyFinderFragment.this.parseStrategyJSON(str.toString());
                List list = access$100[1] != null ? (List) new GsonBuilder().setDateFormat(Constants.DT_FMT_yyyy_MM_dd_HH_m_ss).create().fromJson(access$100[1], type) : null;
                if (StrategyFinderFragment.this.f93li == null) {
                    StrategyFinderFragment.this.f93li = new ArrayList();
                }
                if (list != null) {
                    StrategyFinderFragment.this.f93li.addAll(list);
                }
                if (StrategyFinderFragment.this.f93li == null || StrategyFinderFragment.this.f93li.size() == 0) {
                    StrategyFinderFragment.this.setErrorFragment();
                    return;
                }
                StrategyFinderFragment.this.mainAdapter.setItemList(StrategyFinderFragment.this.f93li);
                StrategyFinderFragment.this.mainAdapter.notifyDataSetChanged();
                StrategyFinderFragment.this.lvStrategies.removeFooterView(StrategyFinderFragment.this.footerView);
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError volleyError) {
                String access$000 = StrategyFinderFragment.this.TAG;
                VolleyLog.d(access$000, "Error: " + volleyError.getMessage());
                StrategyFinderFragment.this.setErrorFragment();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, 1, 1.0f));
        MyGreeksApplication.getInstance().addToRequestQueue(stringRequest);
    }

    public void webServiceCallFirstLoad() throws Exception {
        this.application.isInternetAvailable();
        FilterSelection newFilterSelection = this.activity.getFilterFragment().getNewFilterSelection();
        final ProgressDialog progressDialog = new ProgressDialog(this.mContext);
        progressDialog.setProgressStyle(0);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Loading. Please wait...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCanceledOnTouchOutside(false);
        if (!this.loadUI) {
            progressDialog.show();
        }
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("min_i", 0);
        jSONObject.put("max_i", this.pageSize - 1);
        jSONObject.put("run_seq", -1);
        if (newFilterSelection != null) {
            jSONObject.put("symbol", newFilterSelection.getSymbol());
            jSONObject.put("trend", newFilterSelection.getTrend());
            jSONObject.put("expiry", newFilterSelection.getExpiry().getTime());
            if (newFilterSelection.getRisk().equalsIgnoreCase(Constants.FILTER_RISK_NOLIMIT)) {
                jSONObject.put("risk", Integer.MAX_VALUE);
            } else {
                jSONObject.put("risk", Integer.parseInt(newFilterSelection.getRisk()));
            }
            jSONObject.put("lowRange", newFilterSelection.getLowRange());
            jSONObject.put("highRange", newFilterSelection.getHighRange());
        }
        StringRequest stringRequest = new StringRequest(0, String.format(this.webServiceUrl + "%1$s&condition=%2$s", new Object[]{"101", URLEncoder.encode(jSONObject.toString(), "UTF-8")}), new Response.Listener<String>() {
            /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v23, resolved type: java.lang.Object} */
            /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v14, resolved type: java.util.List} */
            /* JADX WARNING: Multi-variable type inference failed */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void onResponse(String r8) {
                /*
                    r7 = this;
                    r0 = 0
                    r1 = 1
                    bulltrack.com.optionanalyzer.activity.StrategyFinderFragment r2 = bulltrack.com.optionanalyzer.activity.StrategyFinderFragment.this     // Catch:{ Exception -> 0x0112 }
                    java.lang.String r2 = r2.TAG     // Catch:{ Exception -> 0x0112 }
                    java.lang.String r3 = "webServiceCallGetFinder got resposne"
                    android.util.Log.d(r2, r3)     // Catch:{ Exception -> 0x0112 }
                    bulltrack.com.optionanalyzer.activity.StrategyFinderFragment$4$1 r2 = new bulltrack.com.optionanalyzer.activity.StrategyFinderFragment$4$1     // Catch:{ Exception -> 0x0112 }
                    r2.<init>()     // Catch:{ Exception -> 0x0112 }
                    java.lang.reflect.Type r2 = r2.getType()     // Catch:{ Exception -> 0x0112 }
                    com.google.gson.GsonBuilder r3 = new com.google.gson.GsonBuilder     // Catch:{ Exception -> 0x0112 }
                    r3.<init>()     // Catch:{ Exception -> 0x0112 }
                    java.lang.String r4 = "yyyy-MM-dd HH:mm:ss"
                    com.google.gson.GsonBuilder r3 = r3.setDateFormat((java.lang.String) r4)     // Catch:{ Exception -> 0x0112 }
                    com.google.gson.Gson r3 = r3.create()     // Catch:{ Exception -> 0x0112 }
                    bulltrack.com.optionanalyzer.activity.StrategyFinderFragment r4 = bulltrack.com.optionanalyzer.activity.StrategyFinderFragment.this     // Catch:{ Exception -> 0x0112 }
                    java.lang.String r8 = r8.toString()     // Catch:{ Exception -> 0x0112 }
                    java.lang.String[] r8 = r4.parseStrategyJSON(r8)     // Catch:{ Exception -> 0x0112 }
                    bulltrack.com.optionanalyzer.activity.StrategyFinderFragment r4 = bulltrack.com.optionanalyzer.activity.StrategyFinderFragment.this     // Catch:{ Exception -> 0x0112 }
                    java.util.List<bulltrack.com.optionanalyzer.dao.StrategyResultsFilter> r4 = r4.f93li     // Catch:{ Exception -> 0x0112 }
                    if (r4 == 0) goto L_0x0054
                    bulltrack.com.optionanalyzer.activity.StrategyFinderFragment r4 = bulltrack.com.optionanalyzer.activity.StrategyFinderFragment.this     // Catch:{ Exception -> 0x0112 }
                    java.util.List<bulltrack.com.optionanalyzer.dao.StrategyResultsFilter> r4 = r4.f93li     // Catch:{ Exception -> 0x0112 }
                    r4.clear()     // Catch:{ Exception -> 0x0112 }
                    r4 = 0
                    r5 = r8[r1]     // Catch:{ Exception -> 0x0112 }
                    if (r5 == 0) goto L_0x004a
                    r4 = r8[r1]     // Catch:{ Exception -> 0x0112 }
                    java.lang.Object r2 = r3.fromJson((java.lang.String) r4, (java.lang.reflect.Type) r2)     // Catch:{ Exception -> 0x0112 }
                    r4 = r2
                    java.util.List r4 = (java.util.List) r4     // Catch:{ Exception -> 0x0112 }
                L_0x004a:
                    if (r4 == 0) goto L_0x0064
                    bulltrack.com.optionanalyzer.activity.StrategyFinderFragment r2 = bulltrack.com.optionanalyzer.activity.StrategyFinderFragment.this     // Catch:{ Exception -> 0x0112 }
                    java.util.List<bulltrack.com.optionanalyzer.dao.StrategyResultsFilter> r2 = r2.f93li     // Catch:{ Exception -> 0x0112 }
                    r2.addAll(r4)     // Catch:{ Exception -> 0x0112 }
                    goto L_0x0064
                L_0x0054:
                    r4 = r8[r1]     // Catch:{ Exception -> 0x0112 }
                    if (r4 == 0) goto L_0x0064
                    bulltrack.com.optionanalyzer.activity.StrategyFinderFragment r4 = bulltrack.com.optionanalyzer.activity.StrategyFinderFragment.this     // Catch:{ Exception -> 0x0112 }
                    r5 = r8[r1]     // Catch:{ Exception -> 0x0112 }
                    java.lang.Object r2 = r3.fromJson((java.lang.String) r5, (java.lang.reflect.Type) r2)     // Catch:{ Exception -> 0x0112 }
                    java.util.List r2 = (java.util.List) r2     // Catch:{ Exception -> 0x0112 }
                    r4.f93li = r2     // Catch:{ Exception -> 0x0112 }
                L_0x0064:
                    r2 = r8[r0]     // Catch:{ Exception -> 0x0112 }
                    if (r2 == 0) goto L_0x0071
                    bulltrack.com.optionanalyzer.activity.StrategyFinderFragment r2 = bulltrack.com.optionanalyzer.activity.StrategyFinderFragment.this     // Catch:{ Exception -> 0x0112 }
                    bulltrack.com.optionanalyzer.activity.MainActivity r2 = r2.activity     // Catch:{ Exception -> 0x0112 }
                    r8 = r8[r0]     // Catch:{ Exception -> 0x0112 }
                    r2.setLastUpdActionBar(r8)     // Catch:{ Exception -> 0x0112 }
                L_0x0071:
                    android.app.ProgressDialog r8 = r1     // Catch:{ Exception -> 0x0112 }
                    boolean r8 = r8.isShowing()     // Catch:{ Exception -> 0x0112 }
                    if (r8 == 0) goto L_0x007e
                    android.app.ProgressDialog r8 = r1     // Catch:{ Exception -> 0x0112 }
                    r8.dismiss()     // Catch:{ Exception -> 0x0112 }
                L_0x007e:
                    bulltrack.com.optionanalyzer.activity.StrategyFinderFragment r8 = bulltrack.com.optionanalyzer.activity.StrategyFinderFragment.this     // Catch:{ Exception -> 0x0112 }
                    java.util.List<bulltrack.com.optionanalyzer.dao.StrategyResultsFilter> r8 = r8.f93li     // Catch:{ Exception -> 0x0112 }
                    if (r8 != 0) goto L_0x008b
                    bulltrack.com.optionanalyzer.activity.StrategyFinderFragment r8 = bulltrack.com.optionanalyzer.activity.StrategyFinderFragment.this     // Catch:{ Exception -> 0x0112 }
                    r8.setErrorFragment()     // Catch:{ Exception -> 0x0112 }
                    goto L_0x013a
                L_0x008b:
                    bulltrack.com.optionanalyzer.activity.StrategyFinderFragment r8 = bulltrack.com.optionanalyzer.activity.StrategyFinderFragment.this     // Catch:{ Exception -> 0x0112 }
                    boolean r8 = r8.loadUI     // Catch:{ Exception -> 0x0112 }
                    if (r8 == 0) goto L_0x009a
                    bulltrack.com.optionanalyzer.activity.StrategyFinderFragment r8 = bulltrack.com.optionanalyzer.activity.StrategyFinderFragment.this     // Catch:{ Exception -> 0x0112 }
                    r8.LoadUI()     // Catch:{ Exception -> 0x0112 }
                    bulltrack.com.optionanalyzer.activity.StrategyFinderFragment r8 = bulltrack.com.optionanalyzer.activity.StrategyFinderFragment.this     // Catch:{ Exception -> 0x0112 }
                    r8.loadUI = r0     // Catch:{ Exception -> 0x0112 }
                L_0x009a:
                    bulltrack.com.optionanalyzer.activity.StrategyFinderFragment r8 = bulltrack.com.optionanalyzer.activity.StrategyFinderFragment.this     // Catch:{ Exception -> 0x0112 }
                    android.widget.ListView r8 = r8.lvStrategies     // Catch:{ Exception -> 0x0112 }
                    if (r8 == 0) goto L_0x00a7
                    bulltrack.com.optionanalyzer.activity.StrategyFinderFragment r8 = bulltrack.com.optionanalyzer.activity.StrategyFinderFragment.this     // Catch:{ Exception -> 0x0112 }
                    android.widget.ListView r8 = r8.lvStrategies     // Catch:{ Exception -> 0x0112 }
                    r8.invalidate()     // Catch:{ Exception -> 0x0112 }
                L_0x00a7:
                    bulltrack.com.optionanalyzer.activity.StrategyFinderFragment r8 = bulltrack.com.optionanalyzer.activity.StrategyFinderFragment.this     // Catch:{ Exception -> 0x0112 }
                    bulltrack.com.optionanalyzer.application.MyGreeksApplication r8 = r8.application     // Catch:{ Exception -> 0x0112 }
                    boolean r8 = r8.isFinderPaidUser()     // Catch:{ Exception -> 0x0112 }
                    if (r8 == 0) goto L_0x00b5
                    bulltrack.com.optionanalyzer.activity.StrategyFinderFragment r8 = bulltrack.com.optionanalyzer.activity.StrategyFinderFragment.this     // Catch:{ Exception -> 0x0112 }
                    r8.showAds = r0     // Catch:{ Exception -> 0x0112 }
                L_0x00b5:
                    bulltrack.com.optionanalyzer.activity.StrategyFinderFragment r8 = bulltrack.com.optionanalyzer.activity.StrategyFinderFragment.this     // Catch:{ Exception -> 0x0112 }
                    bulltrack.com.optionanalyzer.adapter.AdapterFinderLoad r2 = new bulltrack.com.optionanalyzer.adapter.AdapterFinderLoad     // Catch:{ Exception -> 0x0112 }
                    bulltrack.com.optionanalyzer.activity.StrategyFinderFragment r3 = bulltrack.com.optionanalyzer.activity.StrategyFinderFragment.this     // Catch:{ Exception -> 0x0112 }
                    android.content.Context r3 = r3.mContext     // Catch:{ Exception -> 0x0112 }
                    bulltrack.com.optionanalyzer.activity.StrategyFinderFragment r4 = bulltrack.com.optionanalyzer.activity.StrategyFinderFragment.this     // Catch:{ Exception -> 0x0112 }
                    java.util.List<bulltrack.com.optionanalyzer.dao.StrategyResultsFilter> r4 = r4.f93li     // Catch:{ Exception -> 0x0112 }
                    bulltrack.com.optionanalyzer.activity.StrategyFinderFragment r5 = bulltrack.com.optionanalyzer.activity.StrategyFinderFragment.this     // Catch:{ Exception -> 0x0112 }
                    int r5 = r5.prodRs     // Catch:{ Exception -> 0x0112 }
                    bulltrack.com.optionanalyzer.activity.StrategyFinderFragment r6 = bulltrack.com.optionanalyzer.activity.StrategyFinderFragment.this     // Catch:{ Exception -> 0x0112 }
                    boolean r6 = r6.showAds     // Catch:{ Exception -> 0x0112 }
                    r2.<init>(r3, r4, r5, r6)     // Catch:{ Exception -> 0x0112 }
                    r8.mainAdapter = r2     // Catch:{ Exception -> 0x0112 }
                    bulltrack.com.optionanalyzer.activity.StrategyFinderFragment r8 = bulltrack.com.optionanalyzer.activity.StrategyFinderFragment.this     // Catch:{ Exception -> 0x0112 }
                    android.widget.ListView r8 = r8.lvStrategies     // Catch:{ Exception -> 0x0112 }
                    bulltrack.com.optionanalyzer.activity.StrategyFinderFragment r2 = bulltrack.com.optionanalyzer.activity.StrategyFinderFragment.this     // Catch:{ Exception -> 0x0112 }
                    bulltrack.com.optionanalyzer.adapter.AdapterFinderLoad r2 = r2.mainAdapter     // Catch:{ Exception -> 0x0112 }
                    r8.setAdapter(r2)     // Catch:{ Exception -> 0x0112 }
                    bulltrack.com.optionanalyzer.activity.StrategyFinderFragment r8 = bulltrack.com.optionanalyzer.activity.StrategyFinderFragment.this     // Catch:{ Exception -> 0x0112 }
                    android.widget.ListView r8 = r8.lvStrategies     // Catch:{ Exception -> 0x0112 }
                    bulltrack.com.optionanalyzer.activity.StrategyFinderFragment r2 = bulltrack.com.optionanalyzer.activity.StrategyFinderFragment.this     // Catch:{ Exception -> 0x0112 }
                    android.view.View r2 = r2.view     // Catch:{ Exception -> 0x0112 }
                    r3 = 2131231063(0x7f080157, float:1.8078196E38)
                    android.view.View r2 = r2.findViewById(r3)     // Catch:{ Exception -> 0x0112 }
                    r8.setEmptyView(r2)     // Catch:{ Exception -> 0x0112 }
                    bulltrack.com.optionanalyzer.activity.StrategyFinderFragment r8 = bulltrack.com.optionanalyzer.activity.StrategyFinderFragment.this     // Catch:{ Exception -> 0x0112 }
                    android.widget.ListView r8 = r8.lvStrategies     // Catch:{ Exception -> 0x0112 }
                    bulltrack.com.optionanalyzer.activity.StrategyFinderFragment r2 = bulltrack.com.optionanalyzer.activity.StrategyFinderFragment.this     // Catch:{ Exception -> 0x0112 }
                    android.view.View r2 = r2.footerView     // Catch:{ Exception -> 0x0112 }
                    r8.removeFooterView(r2)     // Catch:{ Exception -> 0x0112 }
                    bulltrack.com.optionanalyzer.activity.StrategyFinderFragment r8 = bulltrack.com.optionanalyzer.activity.StrategyFinderFragment.this     // Catch:{ Exception -> 0x0112 }
                    bulltrack.com.optionanalyzer.adapter.AdapterFinderLoad r8 = r8.mainAdapter     // Catch:{ Exception -> 0x0112 }
                    bulltrack.com.optionanalyzer.activity.StrategyFinderFragment r2 = bulltrack.com.optionanalyzer.activity.StrategyFinderFragment.this     // Catch:{ Exception -> 0x0112 }
                    java.util.List<bulltrack.com.optionanalyzer.dao.StrategyResultsFilter> r2 = r2.f93li     // Catch:{ Exception -> 0x0112 }
                    r8.setItemList(r2)     // Catch:{ Exception -> 0x0112 }
                    bulltrack.com.optionanalyzer.activity.StrategyFinderFragment r8 = bulltrack.com.optionanalyzer.activity.StrategyFinderFragment.this     // Catch:{ Exception -> 0x0112 }
                    bulltrack.com.optionanalyzer.adapter.AdapterFinderLoad r8 = r8.mainAdapter     // Catch:{ Exception -> 0x0112 }
                    r8.notifyDataSetChanged()     // Catch:{ Exception -> 0x0112 }
                    bulltrack.com.optionanalyzer.activity.StrategyFinderFragment r8 = bulltrack.com.optionanalyzer.activity.StrategyFinderFragment.this     // Catch:{ Exception -> 0x0112 }
                    bulltrack.com.optionanalyzer.activity.MainActivity r8 = r8.activity     // Catch:{ Exception -> 0x0112 }
                    r8.hideProgressBar()     // Catch:{ Exception -> 0x0112 }
                    goto L_0x013a
                L_0x0112:
                    r8 = move-exception
                    bulltrack.com.optionanalyzer.activity.StrategyFinderFragment r2 = bulltrack.com.optionanalyzer.activity.StrategyFinderFragment.this
                    java.lang.String r2 = r2.TAG
                    java.lang.Object[] r1 = new java.lang.Object[r1]
                    java.lang.StringBuilder r3 = new java.lang.StringBuilder
                    r3.<init>()
                    java.lang.String r4 = "Error | Volley Call "
                    r3.append(r4)
                    java.lang.String r8 = r8.toString()
                    r3.append(r8)
                    java.lang.String r8 = r3.toString()
                    r1[r0] = r8
                    com.android.volley.VolleyLog.d(r2, r1)
                    bulltrack.com.optionanalyzer.activity.StrategyFinderFragment r8 = bulltrack.com.optionanalyzer.activity.StrategyFinderFragment.this
                    r8.setErrorFragment()
                L_0x013a:
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: bulltrack.com.optionanalyzer.activity.StrategyFinderFragment.C07254.onResponse(java.lang.String):void");
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError volleyError) {
                String access$000 = StrategyFinderFragment.this.TAG;
                VolleyLog.d(access$000, "Error: " + volleyError.getMessage());
                new ErrorFragment();
                StrategyFinderFragment.this.setErrorFragment();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, 1, 1.0f));
        MyGreeksApplication.getInstance().addToRequestQueue(stringRequest);
    }

    /* access modifiers changed from: private */
    public String[] parseStrategyJSON(String str) {
        String[] strArr = new String[2];
        if (str == null) {
            return strArr;
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            if (jSONObject.has("upd")) {
                strArr[0] = jSONObject.getString("upd");
            }
            if (jSONObject.has("result")) {
                strArr[1] = jSONObject.getString("result");
            }
        } catch (JSONException e) {
            Log.d(this.TAG, e.getMessage());
        }
        return strArr;
    }

    /* access modifiers changed from: private */
    public void LoadUI() {
        ListView listView = (ListView) this.view.findViewById(R.id.fullscreen_content);
        this.lvStrategies = listView;
        listView.setEmptyView(this.view.findViewById(R.id.ll_finder_main_empty));
        View inflate = getLayoutInflater().inflate(R.layout.finder_list_footer, (ViewGroup) null);
        this.footerView = inflate;
        this.lvStrategies.addFooterView(inflate);
        ((ImageView) this.view.findViewById(R.id.img_finder_main_close)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                StrategyFinderFragment.this.activity.getFilterFragment().resetNewFilterSelection();
                StrategyFinderFragment.this.activity.getFilterFragment().resetOldFilterSelection();
                StrategyFinderFragment.this.activity.setupStrategyFinderFragment();
                try {
                    StrategyFinderFragment.this.webServiceCallFirstLoad();
                } catch (Exception unused) {
                    StrategyFinderFragment.this.setErrorFragment();
                }
            }
        });
        this.lvStrategies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /* JADX WARNING: type inference failed for: r1v0, types: [android.widget.AdapterView<?>, android.widget.AdapterView] */
            /* JADX WARNING: Unknown variable types count: 1 */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void onItemClick(AdapterView<?> r1, View r2, int r3, long r4) {
                /*
                    r0 = this;
                    android.widget.Adapter r2 = r1.getAdapter()
                    int r2 = r2.getItemViewType(r3)
                    r4 = 1
                    if (r2 != 0) goto L_0x002d
                    android.widget.Adapter r1 = r1.getAdapter()
                    java.lang.Object r1 = r1.getItem(r3)
                    bulltrack.com.optionanalyzer.dao.StrategyResultsFilter r1 = (bulltrack.com.optionanalyzer.dao.StrategyResultsFilter) r1
                    int r2 = r1.getAudience()
                    if (r2 != r4) goto L_0x0027
                    bulltrack.com.optionanalyzer.activity.StrategyFinderFragment r2 = bulltrack.com.optionanalyzer.activity.StrategyFinderFragment.this
                    boolean r2 = r2.showAds
                    if (r2 == 0) goto L_0x0027
                    bulltrack.com.optionanalyzer.activity.StrategyFinderFragment r1 = bulltrack.com.optionanalyzer.activity.StrategyFinderFragment.this
                    r1.showSubscribePopUp()
                    goto L_0x003c
                L_0x0027:
                    bulltrack.com.optionanalyzer.activity.StrategyFinderFragment r2 = bulltrack.com.optionanalyzer.activity.StrategyFinderFragment.this
                    r2.startViewLegsActivity(r1)
                    goto L_0x003c
                L_0x002d:
                    android.widget.Adapter r1 = r1.getAdapter()
                    int r1 = r1.getItemViewType(r3)
                    if (r1 != r4) goto L_0x003c
                    bulltrack.com.optionanalyzer.activity.StrategyFinderFragment r1 = bulltrack.com.optionanalyzer.activity.StrategyFinderFragment.this
                    r1.showSubscribePopUp()
                L_0x003c:
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: bulltrack.com.optionanalyzer.activity.StrategyFinderFragment.C07297.onItemClick(android.widget.AdapterView, android.view.View, int, long):void");
            }
        });
        this.lvStrategies.setOnScrollListener(new EndlessScrollListener() {
            public boolean onLoadMore(int i, int i2) {
                try {
                    StrategyFinderFragment.this.loadNextDataFromApi(i);
                } catch (Exception e) {
                    String access$000 = StrategyFinderFragment.this.TAG;
                    VolleyLog.d(access$000, "Error: " + e.getMessage());
                    StrategyFinderFragment.this.setErrorFragment();
                }
                return true;
            }
        });
    }

    public void setErrorFragment() {
        if (this.activity != null) {
            ErrorFragment errorFragment = new ErrorFragment();
            this.activity.hideProgressBar();
            try {
                this.activity.replaceFragment(errorFragment, (Bundle) null, false);
                getFragmentManager().beginTransaction().replace(R.id.rl_progress_main, errorFragment).commitAllowingStateLoss();
            } catch (Exception e) {
                String str = this.TAG;
                Log.d(str, "Error | Fragment replace " + e.toString());
            }
        }
    }

    public void showSubscribePopUp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(getLayoutInflater().inflate(R.layout.finder_subscription_popup, (ViewGroup) null));
        final AlertDialog create = builder.create();
        create.show();
        ((TextView) create.findViewById(R.id.tv_finder_subscription_price)).setText("₹ " + this.application.round2Decimals((double) this.prodRs));
        this.cbTerms = (CheckBox) create.findViewById(R.id.cb_finder_subscription_terms);
        ((Button) create.findViewById(R.id.btn_finder_subscription_buy)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                create.dismiss();
                StrategyFinderFragment.this.onBuyButtonClicked();
            }
        });
    }

    public void startViewLegsActivity(StrategyResultsFilter strategyResultsFilter) {
        Intent intent = new Intent(this.mContext, FinderLegsActivity.class);
        intent.putExtra("result", strategyResultsFilter);
        intent.putExtra("showAds", this.showAds);
        startActivityForResult(intent, 1010);
    }

    public void onBuyButtonClicked() {
        if (this.prodRs <= 0 || SKU_PREMIUM == null) {
            alert("Something went wrong. Contact support. Exiting");
        } else if (!this.cbTerms.isChecked()) {
            alert("Terms and conditions not agreed. Exiting");
        } else {
            Log.d(this.TAG, "Upgrade button clicked; launching purchase flow for upgrade.");
            try {
                if (this.mHelper != null) {
                    this.mHelper.launchPurchaseFlow(this.activity, SKU_PREMIUM, RC_REQUEST, this.mPurchaseFinishedListener, "");
                } else {
                    Toast.makeText(this.mContext, "Error: Please try later", 1).show();
                }
            } catch (IabHelper.IabAsyncInProgressException unused) {
                complain("Error launching purchase flow. Another async operation in progress.");
            }
        }
    }

    private void googleBilling() {
        IabHelper iabHelper = new IabHelper(this.mContext, Constants.GOOGLE_BASE64_ENCODING_KEY);
        this.mHelper = iabHelper;
        iabHelper.enableDebugLogging(true);
        this.mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult iabResult) {
                Log.d(StrategyFinderFragment.this.TAG, "Setup finished.");
                if (!iabResult.isSuccess()) {
                    StrategyFinderFragment strategyFinderFragment = StrategyFinderFragment.this;
                    strategyFinderFragment.complain("Problem setting up in-app billing: " + iabResult);
                } else if (StrategyFinderFragment.this.mHelper != null) {
                    try {
                        StrategyFinderFragment.this.mHelper.queryInventoryAsync(StrategyFinderFragment.this.mGotInventoryListener);
                    } catch (IabHelper.IabAsyncInProgressException unused) {
                        StrategyFinderFragment.this.complain("Error launching purchase flow. Another async operation in progress.");
                    }
                    Log.d(StrategyFinderFragment.this.TAG, "Setup successful. Querying inventory.");
                }
            }
        });
    }

    /* access modifiers changed from: package-private */
    public boolean verifyDeveloperPayload(Purchase purchase) {
        purchase.getDeveloperPayload();
        return true;
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        String str = this.TAG;
        Log.d(str, "onActivityResult(" + i + "," + i2 + "," + intent);
        IabHelper iabHelper = this.mHelper;
        if (iabHelper != null) {
            if (!iabHelper.handleActivityResult(i, i2, intent)) {
                super.onActivityResult(i, i2, intent);
            } else {
                Log.d(this.TAG, "onActivityResult handled by IABUtil.");
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void complain(String str) {
        String str2 = this.TAG;
        Log.e(str2, "**** TrivialDrive Error: " + str);
        alert("Error: " + str);
    }

    /* access modifiers changed from: package-private */
    public void alert(String str) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.mContext);
        builder.setMessage(str);
        builder.setNeutralButton("OK", (DialogInterface.OnClickListener) null);
        String str2 = this.TAG;
        Log.d(str2, "Showing alert dialog: " + str);
        builder.create().show();
    }

    /* access modifiers changed from: private */
    public void webServiceCallPayment(final String str) {
        final String jNICallResult_getGold = this.application.getJNICallResult_getGold();
        this.application.addToRequestQueue(new JsonObjectRequest(0, this.webServiceUrl + "7", (JSONObject) null, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject jSONObject) {
                String jSONObject2 = jSONObject.toString();
                JSONObject jSONObject3 = new JSONObject();
                try {
                    byte[][] doEncryption = StrategyFinderFragment.this.application.doEncryption(str, jNICallResult_getGold);
                    jSONObject3.put("installid", StrategyFinderFragment.this.application.getAppInstallId());
                    jSONObject3.put("token", new JSONObject(jSONObject2).get("token"));
                    jSONObject3.put("salt", Base64.encodeToString(doEncryption[0], 0));
                    jSONObject3.put("iv", Base64.encodeToString(doEncryption[1], 0));
                    jSONObject3.put("tonka", Base64.encodeToString(doEncryption[2], 0));
                } catch (JSONException e) {
                    String access$000 = StrategyFinderFragment.this.TAG;
                    Log.d(access$000, "Error | Volley Call | finder : " + e.toString());
                }
                try {
                    StringRequest stringRequest = new StringRequest(0, String.format(StrategyFinderFragment.this.webServiceUrl + "%1$s&condition=%2$s", new Object[]{"84", URLEncoder.encode(jSONObject3.toString(), "UTF-8")}), new Response.Listener<String>() {
                        public void onResponse(String str) {
                            Log.d(StrategyFinderFragment.this.TAG, "webServiceCallPayment got response");
                        }
                    }, new Response.ErrorListener() {
                        public void onErrorResponse(VolleyError volleyError) {
                            String access$000 = StrategyFinderFragment.this.TAG;
                            VolleyLog.d(access$000, "Error: " + volleyError.getMessage());
                        }
                    });
                    MyGreeksApplication myGreeksApplication = StrategyFinderFragment.this.application;
                    MyGreeksApplication.getInstance().addToRequestQueue(stringRequest);
                } catch (Exception e2) {
                    String access$0002 = StrategyFinderFragment.this.TAG;
                    Log.d(access$0002, "Error | Volley Call | finder : " + e2.toString());
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError volleyError) {
                String access$000 = StrategyFinderFragment.this.TAG;
                VolleyLog.d(access$000, "Error: " + volleyError.getMessage());
            }
        }), "json_obj_req_finder");
    }

    public boolean isLoadUI() {
        return this.loadUI;
    }

    public void setLoadUI(boolean z) {
        this.loadUI = z;
    }
}
