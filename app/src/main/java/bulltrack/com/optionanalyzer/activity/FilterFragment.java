package bulltrack.com.optionanalyzer.activity;

import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import bulltrack.com.optionanalyzer.adapter.RVAdapterStock;
import bulltrack.com.optionanalyzer.application.MyGreeksApplication;
import bulltrack.com.optionanalyzer.constants.Constants;
import bulltrack.com.optionanalyzer.dao.FilterRVHolder;
import bulltrack.com.optionanalyzer.dao.FilterSelection;
import bulltrack.com.optionanalyzer.dao.FilterStockRange;
import bulltrack.com.optiongreeks13.R;
import com.jiangjiesheng.slidingmenu.SlidingMenu;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class FilterFragment extends Fragment {
    private String TAG = "FilterFragment";
    MainActivity activity;
    /* access modifiers changed from: private */
    public RVAdapterStock adapterExpiry;
    /* access modifiers changed from: private */
    public RVAdapterStock adapterRisk;
    /* access modifiers changed from: private */
    public RVAdapterStock adapterStock;
    /* access modifiers changed from: private */
    public RVAdapterStock adapterTrend;
    MyGreeksApplication application;
    EditText edtHighRange;
    EditText edtLowRange;
    LinearLayoutManager horizontalLayoutManagaerExpiry;
    ImageView imgOk;
    ImageView imgPrem;
    boolean isPaidUser = false;
    private Context mContext;
    SlidingMenu menu;
    FilterSelection newFilterSelection;
    FilterSelection oldFilterSelection;
    RecyclerView rvExpiry;
    RecyclerView rvRisk;
    RecyclerView rvStock;
    RecyclerView rvTrend;
    TextView tvPrem;
    TextView tvValidityDate;
    View view;

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_filter, viewGroup, false);
        this.view = inflate;
        this.imgOk = (ImageView) this.view.findViewById(R.id.img_fragment_filter_ok);
        this.rvStock = (RecyclerView) this.view.findViewById(R.id.rv_fragment_filter_index);
        this.rvTrend = (RecyclerView) this.view.findViewById(R.id.rv_fragment_filter_trend);
        this.rvRisk = (RecyclerView) this.view.findViewById(R.id.rv_fragment_filter_risk);
        this.rvExpiry = (RecyclerView) this.view.findViewById(R.id.rv_fragment_filter_expiry);
        this.edtLowRange = (EditText) this.view.findViewById(R.id.edt_fragment_filter_low_range);
        this.edtHighRange = (EditText) this.view.findViewById(R.id.edt_fragment_filter_high_range);
        this.imgPrem = (ImageView) this.view.findViewById(R.id.img_fragment_filter_prem);
        this.tvPrem = (TextView) this.view.findViewById(R.id.tv_fragment_filter_prem);
        this.tvValidityDate = (TextView) this.view.findViewById(R.id.tv_fragment_filter_validtill);
        this.activity = (MainActivity) getActivity();
        final FilterRVHolder createRVHolder = createRVHolder();
        initialize(createRVHolder);
        ((RelativeLayout) inflate.findViewById(R.id.rl_fragment_filter_subscription)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (!FilterFragment.this.isPaidUser) {
                    FilterFragment.this.menu.post(new Runnable() {
                        public void run() {
                            FilterFragment.this.menu.toggle();
                            FilterFragment.this.activity.getStrategyFragment().showSubscribePopUp();
                        }
                    });
                }
            }
        });
        this.imgOk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                View currentFocus = FilterFragment.this.activity.getCurrentFocus();
                if (currentFocus != null) {
                    ((InputMethodManager) FilterFragment.this.getActivity().getSystemService("input_method")).hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
                }
                FilterFragment filterFragment = FilterFragment.this;
                filterFragment.newFilterSelection = filterFragment.assempleFilter(createRVHolder);
                FilterFragment.this.menu.post(new Runnable() {
                    public void run() {
                        FilterFragment.this.menu.toggle();
                    }
                });
            }
        });
        return this.view;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
        this.application = (MyGreeksApplication) getActivity().getApplicationContext();
    }

    /* access modifiers changed from: package-private */
    public FilterRVHolder createRVHolder() {
        FilterRVHolder instance = FilterRVHolder.getInstance(this.application);
        if (instance == null || this.activity.getStockList() == null || this.activity.getStockRangeList() == null || this.activity.getRiskList() == null || this.activity.getStockList().size() == 0 || this.activity.getStockRangeList().size() == 0 || this.activity.getRiskList().size() == 0) {
            return null;
        }
        instance.setLstStock(this.activity.getStockList());
        instance.setLstRange(this.activity.getStockRangeList());
        instance.setLstRisk(this.activity.getRiskList());
        instance.setLstTrend();
        return instance;
    }

    /* access modifiers changed from: package-private */
    public void initialize(FilterRVHolder filterRVHolder) {
        if (filterRVHolder != null) {
            intializeStockRV(filterRVHolder);
            intializeExpiryRV(filterRVHolder);
            intializeTrendRV(filterRVHolder);
            intializeRiskRV(filterRVHolder);
            initializeRangeTV(filterRVHolder);
            intitializeSubscHeader();
        }
    }

    public void intitializeSubscHeader() {
        long finderSubsrEndDate = this.application.getFinderSubsrEndDate();
        if (finderSubsrEndDate > 0) {
            this.tvPrem.setText("PRO");
            TextView textView = this.tvPrem;
            textView.setTypeface(textView.getTypeface(), 3);
            TextView textView2 = this.tvValidityDate;
            textView2.setText("Valid till : " + this.application.dateFormatter(new Date(finderSubsrEndDate), Constants.DT_FMT_dd_MMM_yyyy));
            this.isPaidUser = true;
            this.imgPrem.setImageResource(R.drawable.finder_premium);
            return;
        }
        this.tvPrem.setText("Free");
        this.tvValidityDate.setText("Subscribe to get full access ");
        setImageButtonEnabled(this.imgPrem, false);
        this.isPaidUser = false;
    }

    /* access modifiers changed from: package-private */
    public void setupFilter(FilterRVHolder filterRVHolder) {
        FilterSelection filterSelection = new FilterSelection();
        this.newFilterSelection = filterSelection;
        filterSelection.setSymbol(filterRVHolder.getCurStock());
        this.newFilterSelection.setTrend(filterRVHolder.getCurTrend());
        this.newFilterSelection.setRisk(filterRVHolder.getCurRisk());
        this.newFilterSelection.setExpiry(this.application.dateFormatter(filterRVHolder.getCurExpiry(), Constants.DT_FMT_dd_MMM_yyyy));
        int curLow = filterRVHolder.getCurLow();
        try {
            curLow = Integer.parseInt(this.edtLowRange.getText().toString());
        } catch (Exception unused) {
            Log.d(this.TAG, "Incorrect Low value entered ");
        }
        int curHigh = filterRVHolder.getCurHigh();
        try {
            curHigh = Integer.parseInt(this.edtHighRange.getText().toString());
        } catch (Exception unused2) {
            Log.d(this.TAG, "Incorrect High value entered ");
        }
        this.newFilterSelection.setLowRange((double) curLow);
        this.newFilterSelection.setHighRange((double) curHigh);
    }

    /* access modifiers changed from: package-private */
    public void intializeStockRV(final FilterRVHolder filterRVHolder) {
        final List<String> lstStock = filterRVHolder.getLstStock();
        this.adapterStock = new RVAdapterStock(lstStock, 0, new RVAdapterStock.OnItemClickListener() {
            public void onItemClick(TextView textView) {
                if (lstStock.indexOf(textView.getText()) != filterRVHolder.getPosStock()) {
                    filterRVHolder.setCurStock(textView.getText().toString());
                    List<String> expiriesForStock = FilterFragment.this.getExpiriesForStock(filterRVHolder.getLstRange(), filterRVHolder.getCurStock());
                    if (expiriesForStock.size() > 0) {
                        filterRVHolder.setCurExpiry(expiriesForStock.get(0));
                        FilterFragment.this.adapterExpiry.updateList(expiriesForStock);
                        FilterFragment.this.adapterExpiry.notifyDataSetChanged();
                        FilterFragment.this.adapterExpiry.setGlobalPos(0);
                        double[] range = filterRVHolder.getRange();
                        filterRVHolder.setCurLow((int) range[0]);
                        filterRVHolder.setCurHigh((int) range[1]);
                        FilterFragment.this.edtHighRange.setText(new Integer(filterRVHolder.getCurHigh()).toString());
                        FilterFragment.this.edtLowRange.setText(new Integer(filterRVHolder.getCurLow()).toString());
                    }
                    FilterFragment.this.setupFilter(filterRVHolder);
                    FilterFragment.this.adapterStock.notifyDataSetChanged();
                    FilterFragment.this.adapterStock.setGlobalPos(filterRVHolder.getPosStock());
                }
            }
        });
        filterRVHolder.setCurStock(lstStock.get(0));
        this.rvStock.setLayoutManager(new LinearLayoutManager(this.mContext, 0, false));
        this.rvStock.setAdapter(this.adapterStock);
    }

    /* access modifiers changed from: package-private */
    public void intializeExpiryRV(final FilterRVHolder filterRVHolder) {
        final List<String> expiriesForStock = getExpiriesForStock(filterRVHolder.getLstRange(), filterRVHolder.getCurStock());
        this.adapterExpiry = new RVAdapterStock(expiriesForStock, 0, new RVAdapterStock.OnItemClickListener() {
            public void onItemClick(TextView textView) {
                List<String> expiriesForStock = FilterFragment.this.getExpiriesForStock(filterRVHolder.getLstRange(), filterRVHolder.getCurStock());
                expiriesForStock.clear();
                expiriesForStock.addAll(expiriesForStock);
                if (expiriesForStock.size() >= 1 && expiriesForStock.indexOf(textView.getText()) != filterRVHolder.getPosExpiry()) {
                    filterRVHolder.setCurExpiry(textView.getText().toString());
                    FilterFragment.this.adapterExpiry.notifyDataSetChanged();
                    FilterFragment.this.adapterExpiry.setGlobalPos(filterRVHolder.getPosExpiry());
                    double[] range = filterRVHolder.getRange();
                    filterRVHolder.setCurLow((int) range[0]);
                    filterRVHolder.setCurHigh((int) range[1]);
                    FilterFragment.this.edtHighRange.setText(new Integer(filterRVHolder.getCurHigh()).toString());
                    FilterFragment.this.edtLowRange.setText(new Integer(filterRVHolder.getCurLow()).toString());
                    FilterFragment.this.setupFilter(filterRVHolder);
                    FilterFragment.this.horizontalLayoutManagaerExpiry.scrollToPosition(expiriesForStock.indexOf(Integer.valueOf(filterRVHolder.getPosExpiry())));
                }
            }
        });
        if (expiriesForStock.size() > 0) {
            filterRVHolder.setCurExpiry(expiriesForStock.get(0));
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.mContext, 0, false);
        this.horizontalLayoutManagaerExpiry = linearLayoutManager;
        this.rvExpiry.setLayoutManager(linearLayoutManager);
        this.rvExpiry.setAdapter(this.adapterExpiry);
    }

    /* access modifiers changed from: package-private */
    public void intializeTrendRV(final FilterRVHolder filterRVHolder) {
        final List<String> lstTrend = filterRVHolder.getLstTrend();
        this.adapterTrend = new RVAdapterStock(lstTrend, 0, new RVAdapterStock.OnItemClickListener() {
            public void onItemClick(TextView textView) {
                if (lstTrend.indexOf(textView.getText()) != filterRVHolder.getPosTrend()) {
                    if (textView.getText().equals(Constants.FILTER_TREND_BULLISH)) {
                        FilterFragment.this.edtLowRange.setEnabled(false);
                        FilterFragment.this.edtHighRange.setEnabled(true);
                    } else if (textView.getText().equals(Constants.FILTER_TREND_BEARISH)) {
                        FilterFragment.this.edtLowRange.setEnabled(true);
                        FilterFragment.this.edtHighRange.setEnabled(false);
                    } else {
                        FilterFragment.this.edtLowRange.setEnabled(true);
                        FilterFragment.this.edtHighRange.setEnabled(true);
                    }
                    filterRVHolder.setCurTrend(textView.getText().toString());
                    FilterFragment.this.adapterTrend.notifyDataSetChanged();
                    FilterFragment.this.adapterTrend.setGlobalPos(filterRVHolder.getPosTrend());
                    FilterFragment.this.setupFilter(filterRVHolder);
                }
            }
        });
        filterRVHolder.setCurTrend(lstTrend.get(0));
        if (filterRVHolder.getCurTrend().equals(Constants.FILTER_TREND_BULLISH)) {
            this.edtLowRange.setEnabled(false);
            this.edtHighRange.setEnabled(true);
        } else if (filterRVHolder.getCurTrend().equals(Constants.FILTER_TREND_BEARISH)) {
            this.edtLowRange.setEnabled(true);
            this.edtHighRange.setEnabled(false);
        } else {
            this.edtLowRange.setEnabled(true);
            this.edtHighRange.setEnabled(true);
        }
        this.rvTrend.setLayoutManager(new LinearLayoutManager(this.mContext, 0, false));
        this.rvTrend.setAdapter(this.adapterTrend);
    }

    /* access modifiers changed from: package-private */
    public void intializeRiskRV(final FilterRVHolder filterRVHolder) {
        final List<String> lstRiskText = filterRVHolder.getLstRiskText();
        this.adapterRisk = new RVAdapterStock(lstRiskText, 0, new RVAdapterStock.OnItemClickListener() {
            public void onItemClick(TextView textView) {
                if (lstRiskText.indexOf(textView.getText()) != filterRVHolder.getPosRisk()) {
                    filterRVHolder.setCurRisk(textView.getText().toString());
                    FilterFragment.this.adapterRisk.notifyDataSetChanged();
                    FilterFragment.this.adapterRisk.setGlobalPos(filterRVHolder.getPosRisk());
                    FilterFragment.this.setupFilter(filterRVHolder);
                }
            }
        });
        filterRVHolder.setCurRisk(lstRiskText.get(0));
        this.rvRisk.setLayoutManager(new LinearLayoutManager(this.mContext, 0, false));
        this.rvRisk.setAdapter(this.adapterRisk);
    }

    /* access modifiers changed from: package-private */
    public void initializeRangeTV(FilterRVHolder filterRVHolder) {
        double[] range = filterRVHolder.getRange(filterRVHolder.getCurStock(), this.application.dateFormatter(filterRVHolder.getCurExpiry(), Constants.DT_FMT_dd_MMM_yyyy));
        final int i = (int) range[0];
        final int i2 = (int) range[1];
        this.edtLowRange.setText(new Integer(i).toString());
        this.edtLowRange.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View view, boolean z) {
                if (z) {
                    FilterFragment.this.edtLowRange.setText("");
                } else if (FilterFragment.this.edtLowRange.getText().toString().trim().equals("")) {
                    FilterFragment.this.edtLowRange.setText(new Integer(i).toString());
                }
            }
        });
        this.edtHighRange.setText(new Integer(i2).toString());
        this.edtHighRange.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View view, boolean z) {
                if (z) {
                    FilterFragment.this.edtHighRange.setText("");
                } else if (FilterFragment.this.edtHighRange.getText().toString().trim().equals("")) {
                    FilterFragment.this.edtHighRange.setText(new Integer(i2).toString());
                }
            }
        });
        filterRVHolder.setCurLow(i);
        filterRVHolder.setCurHigh(i2);
    }

    /* access modifiers changed from: package-private */
    public List<String> getExpiriesForStock(List<FilterStockRange> list, String str) {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getSymbol().equalsIgnoreCase(str)) {
                arrayList.add(this.application.dateFormatter(list.get(i).getExpiry(), Constants.DT_FMT_dd_MMM_yyyy));
            }
        }
        return arrayList;
    }

    /* access modifiers changed from: package-private */
    public int getRiskValue(String str) {
        try {
            return Integer.parseInt(str);
        } catch (Exception unused) {
            String str2 = this.TAG;
            Log.d(str2, "Incorrect Risk Val " + str);
            return Integer.MAX_VALUE;
        }
    }

    public FilterSelection getNewFilterSelection() {
        return this.newFilterSelection;
    }

    public void resetNewFilterSelection() {
        this.newFilterSelection = null;
    }

    public void resetOldFilterSelection() {
        this.oldFilterSelection = null;
    }

    public FilterSelection getOldFilterSelection() {
        return this.oldFilterSelection;
    }

    public void saveToOldFilterSelection() {
        this.oldFilterSelection = this.newFilterSelection;
    }

    public boolean hasFilterChanged() {
        if (this.oldFilterSelection == null && this.newFilterSelection == null) {
            return false;
        }
        if ((this.oldFilterSelection != null || this.newFilterSelection == null) && this.oldFilterSelection.getSymbol().equalsIgnoreCase(this.newFilterSelection.getSymbol()) && this.oldFilterSelection.getTrend() == this.newFilterSelection.getTrend() && this.oldFilterSelection.getRisk() == this.newFilterSelection.getRisk() && this.oldFilterSelection.getLowRange() == this.newFilterSelection.getLowRange() && this.oldFilterSelection.getHighRange() == this.newFilterSelection.getHighRange()) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: package-private */
    public FilterSelection assempleFilter(FilterRVHolder filterRVHolder) {
        FilterSelection filterSelection = new FilterSelection();
        filterSelection.setSymbol(filterRVHolder.getCurStock());
        filterSelection.setTrend(filterRVHolder.getCurTrend());
        filterSelection.setRisk(filterRVHolder.getCurRisk());
        filterSelection.setExpiry(this.application.dateFormatter(filterRVHolder.getCurExpiry(), Constants.DT_FMT_dd_MMM_yyyy));
        int curLow = filterRVHolder.getCurLow();
        try {
            curLow = Integer.parseInt(this.edtLowRange.getText().toString());
        } catch (Exception unused) {
            Log.d(this.TAG, "Incorrect Low value entered ");
        }
        int curHigh = filterRVHolder.getCurHigh();
        try {
            curHigh = Integer.parseInt(this.edtHighRange.getText().toString());
        } catch (Exception unused2) {
            Log.d(this.TAG, "Incorrect High value entered ");
        }
        filterSelection.setLowRange((double) curLow);
        filterSelection.setHighRange((double) curHigh);
        return filterSelection;
    }

    public void setMenu(SlidingMenu slidingMenu) {
        this.menu = slidingMenu;
    }

    public static int getKeyByValue(Map<Integer, String> map, String str) {
        for (Map.Entry next : map.entrySet()) {
            if (str.equals(next.getValue())) {
                return ((Integer) next.getKey()).intValue();
            }
        }
        return Integer.MAX_VALUE;
    }

    public static void setImageButtonEnabled(ImageView imageView, boolean z) {
        imageView.setEnabled(z);
        imageView.setAlpha(z ? 1.0f : 0.3f);
        Drawable drawable = imageView.getDrawable();
        if (!z) {
            drawable = convertDrawableToGrayScale(drawable);
        }
        imageView.setImageDrawable(drawable);
    }

    private static Drawable convertDrawableToGrayScale(Drawable drawable) {
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0.0f);
        ColorMatrixColorFilter colorMatrixColorFilter = new ColorMatrixColorFilter(colorMatrix);
        Drawable mutate = drawable.mutate();
        mutate.setColorFilter(colorMatrixColorFilter);
        return mutate;
    }
}
