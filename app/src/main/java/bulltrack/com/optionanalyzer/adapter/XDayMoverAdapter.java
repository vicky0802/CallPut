package bulltrack.com.optionanalyzer.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import androidx.core.internal.view.SupportMenu;
import androidx.core.view.ViewCompat;
import bulltrack.com.optionanalyzer.application.MyGreeksApplication;
import bulltrack.com.optionanalyzer.constants.Constants;
import bulltrack.com.optionanalyzer.dao.EodTick;
import bulltrack.com.optionanalyzer.dao.XDayMover;
import bulltrack.com.optiongreeks13.R;
import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class XDayMoverAdapter extends BaseAdapter {
    private MyGreeksApplication application;
    /* access modifiers changed from: private */
    public Context context;
    DecimalFormat df1 = new DecimalFormat("0.00");
    boolean isBullish = false;

    /* renamed from: li */
    List<XDayMover> f108li;
    List<XDayMover> liSearchResults;

    public long getItemId(int i) {
        return (long) i;
    }

    public XDayMoverAdapter(Context context2, List<XDayMover> list) {
        this.context = context2;
        this.f108li = list;
        this.application = (MyGreeksApplication) context2.getApplicationContext();
        ArrayList arrayList = new ArrayList();
        this.liSearchResults = arrayList;
        arrayList.addAll(list);
        if (list != null && list.size() > 0 && list.get(0).getProfitLoss() > 0.0f) {
            this.isBullish = true;
        }
    }

    public int getCount() {
        List<XDayMover> list = this.f108li;
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    public Object getItem(int i) {
        return this.f108li.get(i);
    }

    public void setItemList(List<XDayMover> list) {
        this.f108li = list;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        final XDayMover xDayMover = this.f108li.get(i);
        if (view == null) {
            view = ((LayoutInflater) this.context.getSystemService("layout_inflater")).inflate(R.layout.xdaymover_item, viewGroup, false);
        }
        TextView textView = (TextView) view.findViewById(R.id.tv_xdaymover_item_optionid);
        TextView textView2 = (TextView) view.findViewById(R.id.tv_xdaymover_item_gainloss);
        TextView textView3 = (TextView) view.findViewById(R.id.tv_xdaymover_item_days);
        LineChart lineChart = (LineChart) view.findViewById(R.id.cndl_xdaymover_item_thumbnail);
        lineChart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                XDayMoverAdapter.this.getGreekApplication().startChartActivityForOption(XDayMoverAdapter.this.context, xDayMover.getOptID());
            }
        });
        ((TextView) view.findViewById(R.id.tv_xdaymover_item_stock)).setText(xDayMover.getOptID().getSymbol());
        if (xDayMover.getProfitLoss() > 0.0f) {
            textView2.setText("+" + this.df1.format((double) xDayMover.getProfitLoss()) + " %");
        } else {
            textView2.setText(this.df1.format((double) xDayMover.getProfitLoss()) + " %");
        }
        String dateFormatter = getGreekApplication().dateFormatter(xDayMover.getOptID().getExpiry(), Constants.DT_FMT_dd_MMM_yyyy);
        textView.setText(xDayMover.getOptID().getStrike() + "-" + dateFormatter + "-" + xDayMover.getOptID().getPutOrCall());
        List<EodTick> ticks = xDayMover.getTicks();
        StringBuilder sb = new StringBuilder();
        sb.append("in ");
        sb.append(ticks.size());
        sb.append(" days");
        textView3.setText(sb.toString());
        loadLineChart(lineChart, ticks);
        return view;
    }

    public void loadLineChart(LineChart lineChart, List<EodTick> list) {
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            arrayList2.add(new Entry(list.get(i).getClosePrice(), i));
            arrayList.add("");
        }
        LineDataSet lineDataSet = new LineDataSet(arrayList2, "DataSet 1");
        lineDataSet.setFillAlpha(110);
        lineDataSet.setColor(ViewCompat.MEASURED_STATE_MASK);
        lineDataSet.setCircleColor(ViewCompat.MEASURED_STATE_MASK);
        lineDataSet.setLineWidth(1.0f);
        lineDataSet.setCircleSize(3.0f);
        lineDataSet.setDrawCircleHole(false);
        lineDataSet.setValueTextSize(9.0f);
        lineDataSet.setDrawFilled(true);
        if (this.isBullish) {
            lineDataSet.setFillColor(-16711936);
        } else {
            lineDataSet.setFillColor(SupportMenu.CATEGORY_MASK);
        }
        ArrayList arrayList3 = new ArrayList();
        arrayList3.add(lineDataSet);
        lineChart.setData(new LineData((List<String>) arrayList, (List<LineDataSet>) arrayList3));
        lineChart.setDescription("");
        lineChart.setNoDataTextDescription("Waiting for data");
        YAxis axisLeft = lineChart.getAxisLeft();
        axisLeft.removeAllLimitLines();
        axisLeft.setEnabled(false);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.getLegend().setEnabled(false);
        lineChart.invalidate();
    }

    public void loadThumbChart(CandleStickChart candleStickChart, List<EodTick> list) {
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            arrayList.add(new CandleEntry(i, list.get(i).getOpenPrice(), list.get(i).getHighPrice(), list.get(i).getLowPrice(), list.get(i).getClosePrice()));
            arrayList2.add("");
        }
        CandleDataSet candleDataSet = new CandleDataSet(arrayList, "Data Set");
        candleDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        candleDataSet.setShadowColorSameAsCandle(true);
        candleDataSet.setBodySpace(0.25f);
        candleDataSet.setShadowWidth(1.5f);
        candleDataSet.setColor(-16711936);
        candleDataSet.setDecreasingColor(SupportMenu.CATEGORY_MASK);
        candleDataSet.setDecreasingPaintStyle(Paint.Style.FILL);
        candleDataSet.setIncreasingColor(-16711936);
        candleDataSet.setIncreasingPaintStyle(Paint.Style.FILL);
        candleDataSet.setDrawValues(false);
        CandleData candleData = new CandleData((List<String>) arrayList2, candleDataSet);
        candleStickChart.getXAxis().setEnabled(false);
        candleStickChart.getAxisLeft().setEnabled(false);
        candleStickChart.getAxisRight().setEnabled(false);
        candleStickChart.getLegend().setEnabled(false);
        candleStickChart.setScaleYEnabled(true);
        candleStickChart.setDescription("");
        candleStickChart.setData(candleData);
        candleStickChart.invalidate();
    }

    /* access modifiers changed from: protected */
    public MyGreeksApplication getGreekApplication() {
        return this.application;
    }

    public void filter(String str) {
        String lowerCase = str.toLowerCase(Locale.getDefault());
        this.f108li.clear();
        if (lowerCase.length() == 0) {
            this.f108li.addAll(this.liSearchResults);
        } else {
            for (XDayMover next : this.liSearchResults) {
                if (lowerCase.length() != 0 && next.getOptID().getSymbol().toLowerCase(Locale.getDefault()).contains(lowerCase)) {
                    this.f108li.add(next);
                }
            }
        }
        notifyDataSetChanged();
    }
}
