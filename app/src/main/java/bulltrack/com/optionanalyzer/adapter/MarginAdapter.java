package bulltrack.com.optionanalyzer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.core.internal.view.SupportMenu;
import androidx.core.view.ViewCompat;
import bulltrack.com.optionanalyzer.application.MyGreeksApplication;
import bulltrack.com.optionanalyzer.dao.OptionMargin;
import bulltrack.com.optiongreeks13.R;
import java.text.DecimalFormat;
import java.util.List;

public class MarginAdapter extends BaseAdapter {
    private MyGreeksApplication application;
    float atmStrike;
    private Context context;
    DecimalFormat df1 = new DecimalFormat("###,##0.00");

    /* renamed from: li */
    List<OptionMargin> f103li;
    ListView list;

    public long getItemId(int i) {
        return (long) i;
    }

    public MarginAdapter(Context context2, List<OptionMargin> list2) {
        this.context = context2;
        this.f103li = list2;
        this.application = (MyGreeksApplication) context2.getApplicationContext();
        this.atmStrike = getAtmStrike();
    }

    public float getAtmStrike() {
        List<OptionMargin> list2 = this.f103li;
        if (list2 == null || list2.size() == 0) {
            return 0.0f;
        }
        float stockPrice = this.f103li.get(0).getStockPrice();
        for (int i = 1; i < this.f103li.size(); i++) {
            int i2 = i - 1;
            if (stockPrice > this.f103li.get(i2).getStrike() && stockPrice <= this.f103li.get(i).getStrike()) {
                return this.f103li.get(i2).getStrike();
            }
        }
        return 0.0f;
    }

    public int getCount() {
        List<OptionMargin> list2 = this.f103li;
        if (list2 == null) {
            return 0;
        }
        return list2.size();
    }

    public Object getItem(int i) {
        return this.f103li.get(i);
    }

    public void setItemList(List<OptionMargin> list2) {
        this.f103li = list2;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        OptionMargin optionMargin = this.f103li.get(i);
        if (view == null) {
            view = ((LayoutInflater) this.context.getSystemService("layout_inflater")).inflate(R.layout.option_margin_main_item, viewGroup, false);
        }
        TextView textView = (TextView) view.findViewById(R.id.tv_option_margin_main_item_strike_val);
        TextView textView2 = (TextView) view.findViewById(R.id.tv_option_margin_main_item_sellmargin_val);
        float optionPrice = optionMargin.getOptionPrice() * ((float) optionMargin.getLotSize());
        float strike = optionMargin.getStrike();
        getGreekApplication();
        float roundTo05 = MyGreeksApplication.roundTo05(optionMargin.getSpanMargin());
        getGreekApplication();
        getGreekApplication();
        float roundTo052 = roundTo05 + MyGreeksApplication.roundTo05(optionMargin.getExposureMargin()) + MyGreeksApplication.roundTo05(optionMargin.getAdditionalMargin());
        ((TextView) view.findViewById(R.id.tv_option_margin_main_item_buyprem_val)).setText(this.df1.format((double) optionPrice));
        textView.setText(strike + "");
        if (strike == this.atmStrike) {
            textView.setTextColor(SupportMenu.CATEGORY_MASK);
        } else {
            textView.setTextColor(ViewCompat.MEASURED_STATE_MASK);
        }
        textView2.setText(this.df1.format((double) roundTo052));
        return view;
    }

    private static class ViewHolder {
        protected TextView itemName;

        private ViewHolder() {
        }
    }

    /* access modifiers changed from: protected */
    public MyGreeksApplication getGreekApplication() {
        return this.application;
    }
}
