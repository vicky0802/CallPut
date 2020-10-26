package bulltrack.com.optionanalyzer.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.core.internal.view.SupportMenu;
import bulltrack.com.optionanalyzer.application.MyGreeksApplication;
import bulltrack.com.optionanalyzer.constants.Constants;
import bulltrack.com.optionanalyzer.dao.PartyRec;
import bulltrack.com.optiongreeks13.R;
import java.text.SimpleDateFormat;
import java.util.List;

public class DayWiseAdapter extends BaseAdapter {
    private MyGreeksApplication application;
    private Context context;

    /* renamed from: li */
    List<PartyRec> f101li;
    ListView list;

    public long getItemId(int i) {
        return (long) i;
    }

    public DayWiseAdapter(Context context2, List<PartyRec> list2) {
        this.context = context2;
        this.f101li = list2;
        this.application = (MyGreeksApplication) context2.getApplicationContext();
    }

    public int getCount() {
        List<PartyRec> list2 = this.f101li;
        if (list2 == null) {
            return 0;
        }
        return list2.size();
    }

    public Object getItem(int i) {
        return this.f101li.get(i);
    }

    public void setItemList(List<PartyRec> list2) {
        this.f101li = list2;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        PartyRec partyRec = this.f101li.get(i);
        if (view == null) {
            view = ((LayoutInflater) this.context.getSystemService("layout_inflater")).inflate(R.layout.party_day_wise_item, viewGroup, false);
        }
        TextView textView = (TextView) view.findViewById(R.id.tv_party_day_wise_item_long_short_data_net);
        ((TextView) view.findViewById(R.id.tv_party_day_wise_item_long_short_data_date)).setText(new SimpleDateFormat(Constants.DT_FMT_dd_MMM).format(partyRec.getPriceUpd()));
        ((TextView) view.findViewById(R.id.tv_party_day_wise_item_long_short_data_long)).setText("" + String.format("%,d", new Object[]{Integer.valueOf(partyRec.getLongPositions())}));
        ((TextView) view.findViewById(R.id.tv_party_day_wise_item_long_short_data_short)).setText("" + String.format("%,d", new Object[]{Integer.valueOf(partyRec.getShortPositions())}));
        int longPositions = partyRec.getLongPositions() - partyRec.getShortPositions();
        textView.setText("" + String.format("%,d", new Object[]{Integer.valueOf(longPositions)}));
        if (longPositions >= 0) {
            textView.setTextColor(Color.parseColor("#2E8B57"));
        } else {
            textView.setTextColor(SupportMenu.CATEGORY_MASK);
        }
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
