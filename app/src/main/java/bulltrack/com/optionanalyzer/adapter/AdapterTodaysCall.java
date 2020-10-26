package bulltrack.com.optionanalyzer.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.core.internal.view.SupportMenu;
import bulltrack.com.optionanalyzer.application.MyGreeksApplication;
import bulltrack.com.optionanalyzer.constants.Constants;
import bulltrack.com.optionanalyzer.dao.TradeCalls;
import bulltrack.com.optiongreeks13.R;
import java.util.ArrayList;
import java.util.List;

public class AdapterTodaysCall extends BaseAdapter {
    private MyGreeksApplication application;
    private int callFrom = 0;
    private Context context;

    /* renamed from: li */
    List<TradeCalls> f99li;
    List<TradeCalls> liSearchResults;
    ListView list;

    public long getItemId(int i) {
        return (long) i;
    }

    public AdapterTodaysCall(Context context2, List<TradeCalls> list2, int i) {
        this.context = context2;
        this.f99li = list2;
        this.application = (MyGreeksApplication) context2.getApplicationContext();
        ArrayList arrayList = new ArrayList();
        this.liSearchResults = arrayList;
        arrayList.addAll(list2);
        this.callFrom = i;
    }

    public int getCount() {
        List<TradeCalls> list2 = this.f99li;
        if (list2 == null) {
            return 0;
        }
        return list2.size();
    }

    public Object getItem(int i) {
        return this.f99li.get(i);
    }

    public void setItemList(List<TradeCalls> list2) {
        this.f99li = list2;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        TradeCalls tradeCalls = this.f99li.get(i);
        View inflate = view == null ? ((LayoutInflater) this.context.getSystemService("layout_inflater")).inflate(R.layout.todays_call_item, viewGroup, false) : view;
        TextView textView = (TextView) inflate.findViewById(R.id.tv_item_todays_call_option_ltp);
        TextView textView2 = (TextView) inflate.findViewById(R.id.tv_item_todays_call_option_id);
        ImageView imageView = (ImageView) inflate.findViewById(R.id.img_item_todays_call_opotion_arrow);
        TextView textView3 = (TextView) inflate.findViewById(R.id.tv_item_todays_call_option_pricechange);
        TextView textView4 = (TextView) inflate.findViewById(R.id.tv_item_todays_call_option_calltext);
        TextView textView5 = (TextView) inflate.findViewById(R.id.tv_item_todays_call_period);
        TextView textView6 = (TextView) inflate.findViewById(R.id.tv_item_todays_call_option_t1);
        TextView textView7 = (TextView) inflate.findViewById(R.id.tv_item_todays_call_option_t2);
        TextView textView8 = (TextView) inflate.findViewById(R.id.tv_item_todays_call_option_sl);
        TextView textView9 = (TextView) inflate.findViewById(R.id.tv_item_todays_call_profit);
        View view2 = inflate;
        ((TextView) inflate.findViewById(R.id.tv_item_todays_call_underlyer)).setText(tradeCalls.getSymbol());
        TextView textView10 = (TextView) inflate.findViewById(R.id.tv_item_todays_call_loss);
        TextView textView11 = (TextView) inflate.findViewById(R.id.tv_item_todays_call_lotsize);
        textView.setText(this.application.round2Decimals1000(tradeCalls.getClosePrice()));
        if (tradeCalls.getInstrType().trim().equalsIgnoreCase(Constants.INSTR_CALL)) {
            textView2.setText(tradeCalls.getStrike() + "-Call-" + this.application.dateFormatter(tradeCalls.getExpiry(), Constants.DT_FMT_dd_MMM_yyyy));
        } else if (tradeCalls.getInstrType().trim().equalsIgnoreCase(Constants.INSTR_PUT)) {
            textView2.setText(tradeCalls.getStrike() + "-Put-" + this.application.dateFormatter(tradeCalls.getExpiry(), Constants.DT_FMT_dd_MMM_yyyy));
        } else {
            textView2.setText("NSE Stock");
        }
        if (tradeCalls.getPriceGainPC() >= 0.0d) {
            imageView.setImageResource(R.drawable.up_green);
            textView.setTextColor(Color.parseColor("#2E8B57"));
            textView3.setTextColor(Color.parseColor("#2E8B57"));
        } else {
            imageView.setImageResource(R.drawable.down_red);
            textView.setTextColor(SupportMenu.CATEGORY_MASK);
            textView3.setTextColor(SupportMenu.CATEGORY_MASK);
        }
        textView3.setText(this.application.round2Decimals1000(tradeCalls.getPriceGainPC()) + "%");
        if (tradeCalls.getAction().trim().equalsIgnoreCase(Constants.ACTION_BUY)) {
            textView4.setText("Buy Above " + this.application.round2Decimals1000(tradeCalls.getEntryPrice().doubleValue()));
            textView4.setTextColor(Color.parseColor("#2E8B57"));
        } else {
            textView4.setText("Sell Below " + this.application.round2Decimals1000(tradeCalls.getEntryPrice().doubleValue()));
            textView4.setTextColor(Color.parseColor("#b22222"));
        }
        if (tradeCalls.getFlag().trim().equalsIgnoreCase(Constants.FLAG_INTRADAY)) {
            textView5.setText("INTRADAY");
        } else if (tradeCalls.getFlag().trim().equalsIgnoreCase(Constants.FLAG_POSITIONAL)) {
            textView5.setText("POSITIONAL");
        }
        textView6.setText("T1: " + this.application.round2Decimals1000(tradeCalls.getT1().doubleValue()));
        textView7.setText("T2: " + this.application.round2Decimals1000(tradeCalls.getT2().doubleValue()));
        textView8.setText("SL: " + this.application.round2Decimals1000(tradeCalls.getSL().doubleValue()));
        textView9.setText("Profit = " + this.application.round2Decimals1000(Math.abs(((tradeCalls.getT2().doubleValue() - tradeCalls.getEntryPrice().doubleValue()) / tradeCalls.getEntryPrice().doubleValue()) * 100.0d)) + "%");
        textView10.setText("Loss = " + this.application.round2Decimals1000(Math.abs(((tradeCalls.getSL().doubleValue() - tradeCalls.getEntryPrice().doubleValue()) / tradeCalls.getEntryPrice().doubleValue()) * 100.0d)) + "%");
        int i2 = this.callFrom;
        if (i2 != 0) {
            TextView textView12 = textView11;
            if (i2 == 1) {
                textView12.setText(this.application.dateFormatter(tradeCalls.getNextTradeDate(), Constants.DT_FMT_dd_MMM_yyyy));
            } else {
                textView12.setText("");
            }
        } else if (tradeCalls.getLotSize() > 0) {
            textView11.setText("1 Lot = " + Integer.toString(tradeCalls.getLotSize()));
        } else {
            textView11.setText("");
        }
        return view2;
    }
}
