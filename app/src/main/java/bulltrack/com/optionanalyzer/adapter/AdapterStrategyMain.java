package bulltrack.com.optionanalyzer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import bulltrack.com.optionanalyzer.application.MyGreeksApplication;
import bulltrack.com.optionanalyzer.dao.StrategyResultsFilter;
import bulltrack.com.optiongreeks13.R;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AdapterStrategyMain extends BaseAdapter {
    private MyGreeksApplication application;
    private Context context;

    /* renamed from: li */
    List<StrategyResultsFilter> f98li;
    List<StrategyResultsFilter> liSearchResults;
    ListView list;

    public long getItemId(int i) {
        return (long) i;
    }

    public AdapterStrategyMain(Context context2, List<StrategyResultsFilter> list2) {
        this.context = context2;
        this.f98li = list2;
        this.application = (MyGreeksApplication) context2.getApplicationContext();
        ArrayList arrayList = new ArrayList();
        this.liSearchResults = arrayList;
        arrayList.addAll(list2);
    }

    public int getCount() {
        List<StrategyResultsFilter> list2 = this.f98li;
        if (list2 == null) {
            return 0;
        }
        return list2.size();
    }

    public Object getItem(int i) {
        return this.f98li.get(i);
    }

    public void setItemList(List<StrategyResultsFilter> list2) {
        this.f98li = list2;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        StrategyResultsFilter strategyResultsFilter = this.f98li.get(i);
        if (view == null) {
            view = ((LayoutInflater) this.context.getSystemService("layout_inflater")).inflate(R.layout.item_strategy_results, viewGroup, false);
        }
        TextView textView = (TextView) view.findViewById(R.id.tv_item_strategy_results_legs_count);
        ImageView imageView = (ImageView) view.findViewById(R.id.img_item_strategy_results_lock);
        ((TextView) view.findViewById(R.id.tv_item_strategy_results_stock)).setText(strategyResultsFilter.getSymbol());
        ((TextView) view.findViewById(R.id.tv_item_strategy_results_strategy)).setText(strategyResultsFilter.getStrategyName());
        ((TextView) view.findViewById(R.id.tv_item_strategy_results_risk_val)).setText("₹ " + this.application.round2Decimals1000((double) strategyResultsFilter.getMaxRisk()));
        ((TextView) view.findViewById(R.id.tv_item_strategy_results_gain_val)).setText("₹ " + this.application.round2Decimals1000((double) strategyResultsFilter.getMaxGain()));
        ((TextView) view.findViewById(R.id.tv_item_strategy_results_upd)).setText(this.application.shortDateOrTime(strategyResultsFilter.getUpdD()));
        ((ImageView) view.findViewById(R.id.img_item_strategy_results_payoff)).setImageResource(this.application.getPayoffDiagram(strategyResultsFilter.getStrategyId()));
        if (strategyResultsFilter.getAudience() != 1) {
            imageView.setVisibility(8);
        } else if (!this.application.ifStrategyBoughtForLockDisplay(strategyResultsFilter)) {
            imageView.setVisibility(0);
            imageView.setImageResource(R.drawable.lockclosed);
        } else {
            imageView.setVisibility(8);
        }
        textView.setText(strategyResultsFilter.getNoOflegs() + " Legs");
        return view;
    }

    public void filter(String str) {
        String lowerCase = str.toLowerCase(Locale.getDefault());
        this.f98li.clear();
        if (lowerCase.length() == 0) {
            this.f98li.addAll(this.liSearchResults);
        } else {
            for (StrategyResultsFilter next : this.liSearchResults) {
                if (lowerCase.length() != 0 && next.getSymbol().toLowerCase(Locale.getDefault()).contains(lowerCase)) {
                    this.f98li.add(next);
                } else if (lowerCase.length() != 0 && next.getStrategyName().toLowerCase(Locale.getDefault()).contains(lowerCase)) {
                    this.f98li.add(next);
                }
            }
        }
        notifyDataSetChanged();
    }
}
