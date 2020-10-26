package bulltrack.com.optionanalyzer.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import bulltrack.com.optionanalyzer.application.MyGreeksApplication;
import bulltrack.com.optionanalyzer.dao.PcrVal;
import bulltrack.com.optiongreeks13.R;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PCRAdapter extends BaseAdapter {
    private MyGreeksApplication application;
    private Context context;
    DecimalFormat df1 = new DecimalFormat("0.00");

    /* renamed from: li */
    List<PcrVal> f105li;
    List<PcrVal> liSearchResults;

    public long getItemId(int i) {
        return (long) i;
    }

    public PCRAdapter(Context context2, List<PcrVal> list) {
        this.context = context2;
        this.f105li = list;
        this.application = (MyGreeksApplication) context2.getApplicationContext();
        ArrayList arrayList = new ArrayList();
        this.liSearchResults = arrayList;
        arrayList.addAll(list);
    }

    public int getCount() {
        List<PcrVal> list = this.f105li;
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    public Object getItem(int i) {
        return this.f105li.get(i);
    }

    public void setItemList(List<PcrVal> list) {
        this.f105li = list;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        PcrVal pcrVal = this.f105li.get(i);
        if (view == null) {
            view = ((LayoutInflater) this.context.getSystemService("layout_inflater")).inflate(R.layout.pcr_item, viewGroup, false);
        }
        TextView textView = (TextView) view.findViewById(R.id.tv_pcr_item_pcr_val);
        ((TextView) view.findViewById(R.id.tv_pcr_item_stock_val)).setText(pcrVal.getSymbol());
        if (((double) pcrVal.getPcrVal()) >= 1.0d) {
            textView.setTextColor(Color.parseColor("#2E8B57"));
        } else {
            textView.setTextColor(Color.parseColor("#C70039"));
        }
        textView.setText(this.df1.format((double) pcrVal.getPcrVal()));
        return view;
    }

    public void filter(String str) {
        String lowerCase = str.toLowerCase(Locale.getDefault());
        this.f105li.clear();
        if (lowerCase.length() == 0) {
            this.f105li.addAll(this.liSearchResults);
        } else {
            for (PcrVal next : this.liSearchResults) {
                if (lowerCase.length() != 0 && next.getSymbol().toLowerCase(Locale.getDefault()).contains(lowerCase)) {
                    this.f105li.add(next);
                }
            }
        }
        notifyDataSetChanged();
    }

    /* access modifiers changed from: protected */
    public MyGreeksApplication getGreekApplication() {
        return this.application;
    }
}
