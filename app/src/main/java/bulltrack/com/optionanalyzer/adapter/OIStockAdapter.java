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
import bulltrack.com.optionanalyzer.dao.OIData;
import bulltrack.com.optiongreeks13.R;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OIStockAdapter extends BaseAdapter {
    private MyGreeksApplication application;
    private Context context;

    /* renamed from: li */
    List<OIData> f104li;
    List<OIData> liSearchResults;
    ListView list;

    public long getItemId(int i) {
        return (long) i;
    }

    public OIStockAdapter(Context context2, List<OIData> list2) {
        this.context = context2;
        this.f104li = list2;
        this.application = (MyGreeksApplication) context2.getApplicationContext();
        ArrayList arrayList = new ArrayList();
        this.liSearchResults = arrayList;
        arrayList.addAll(list2);
    }

    public int getCount() {
        List<OIData> list2 = this.f104li;
        if (list2 == null) {
            return 0;
        }
        return list2.size();
    }

    public Object getItem(int i) {
        return this.f104li.get(i);
    }

    public void setItemList(List<OIData> list2) {
        this.f104li = list2;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        OIData oIData = this.f104li.get(i);
        View inflate = view == null ? ((LayoutInflater) this.context.getSystemService("layout_inflater")).inflate(R.layout.oi_stock_item, viewGroup, false) : view;
        TextView textView = (TextView) inflate.findViewById(R.id.tv_oi_stock_oi_change_xx);
        TextView textView2 = (TextView) inflate.findViewById(R.id.tv_oi_stock_oi_change_per_xx);
        TextView textView3 = (TextView) inflate.findViewById(R.id.tv_oi_stock_instr_ce);
        TextView textView4 = (TextView) inflate.findViewById(R.id.tv_oi_stock_oi_ce);
        TextView textView5 = (TextView) inflate.findViewById(R.id.tv_oi_stock_oi_change_ce);
        TextView textView6 = (TextView) inflate.findViewById(R.id.tv_oi_stock_oi_change_per_ce);
        TextView textView7 = (TextView) inflate.findViewById(R.id.tv_oi_stock_instr_pe);
        View view2 = inflate;
        DecimalFormat decimalFormat = new DecimalFormat(Constants.NUM_FMT_X_00);
        ((TextView) inflate.findViewById(R.id.tv_oi_stock_underlyer)).setText(oIData.getSymbol());
        ((TextView) inflate.findViewById(R.id.tv_oi_stock_instr_xx)).setText("Fut OI");
        StringBuilder sb = new StringBuilder();
        sb.append("");
        TextView textView8 = (TextView) inflate.findViewById(R.id.tv_oi_stock_oi_change_per_pe);
        sb.append(String.format("%,d", new Object[]{Integer.valueOf(oIData.getOiSumXX())}));
        ((TextView) inflate.findViewById(R.id.tv_oi_stock_oi_xx)).setText(sb.toString());
        StringBuilder sb2 = new StringBuilder();
        sb2.append(" [ ");
        TextView textView9 = (TextView) inflate.findViewById(R.id.tv_oi_stock_oi_change_pe);
        sb2.append(String.format("%,d", new Object[]{Integer.valueOf(oIData.getOiChangeXX())}));
        textView.setText(sb2.toString());
        StringBuilder sb3 = new StringBuilder();
        sb3.append("");
        TextView textView10 = (TextView) inflate.findViewById(R.id.tv_oi_stock_oi_pe);
        sb3.append(decimalFormat.format((double) oIData.getOiChgPerXX()));
        sb3.append("% ]");
        textView2.setText(sb3.toString());
        if (oIData == null || oIData.getOiChangeXX() < 0) {
            textView.setTextColor(SupportMenu.CATEGORY_MASK);
            textView2.setTextColor(SupportMenu.CATEGORY_MASK);
        } else {
            textView.setTextColor(Color.parseColor("#2E8B57"));
            textView2.setTextColor(Color.parseColor("#2E8B57"));
        }
        textView3.setText("Call OI");
        textView4.setText("" + String.format("%,d", new Object[]{Integer.valueOf(oIData.getOiSumCE())}));
        textView5.setText(" [ " + String.format("%,d", new Object[]{Integer.valueOf(oIData.getOiChangeCE())}));
        textView6.setText("" + decimalFormat.format((double) oIData.getOiChgPerCE()) + "% ]");
        if (oIData == null || oIData.getOiChangeCE() < 0) {
            textView5.setTextColor(SupportMenu.CATEGORY_MASK);
            textView6.setTextColor(SupportMenu.CATEGORY_MASK);
        } else {
            textView5.setTextColor(Color.parseColor("#2E8B57"));
            textView6.setTextColor(Color.parseColor("#2E8B57"));
        }
        textView7.setText("Put OI");
        textView10.setText("" + String.format("%,d", new Object[]{Integer.valueOf(oIData.getOiSumPE())}));
        TextView textView11 = textView9;
        textView11.setText(" [ " + String.format("%,d", new Object[]{Integer.valueOf(oIData.getOiChangePE())}));
        TextView textView12 = textView8;
        textView12.setText("" + decimalFormat.format((double) oIData.getOiChgPerPE()) + "% ]");
        if (oIData == null || oIData.getOiChangePE() < 0) {
            textView11.setTextColor(SupportMenu.CATEGORY_MASK);
            textView12.setTextColor(SupportMenu.CATEGORY_MASK);
        } else {
            textView11.setTextColor(Color.parseColor("#2E8B57"));
            textView12.setTextColor(Color.parseColor("#2E8B57"));
        }
        return view2;
    }

    public void filter(String str) {
        String lowerCase = str.toLowerCase(Locale.getDefault());
        this.f104li.clear();
        if (lowerCase.length() == 0) {
            this.f104li.addAll(this.liSearchResults);
        } else {
            for (OIData next : this.liSearchResults) {
                if (lowerCase.length() != 0 && next.getSymbol().toLowerCase(Locale.getDefault()).contains(lowerCase)) {
                    this.f104li.add(next);
                }
            }
        }
        notifyDataSetChanged();
    }
}
