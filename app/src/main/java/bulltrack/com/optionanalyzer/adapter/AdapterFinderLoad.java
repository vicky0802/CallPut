package bulltrack.com.optionanalyzer.adapter;

import android.content.Context;
import android.widget.BaseAdapter;
import android.widget.ListView;
import bulltrack.com.optionanalyzer.application.MyGreeksApplication;
import bulltrack.com.optionanalyzer.dao.StrategyResultsFilter;
import com.google.android.gms.ads.AdRequest;
import java.util.List;

public class AdapterFinderLoad extends BaseAdapter {
    AdRequest adRequest;
    private MyGreeksApplication application;
    private Context context;
    boolean isFolio = false;

    /* renamed from: li */
    List<StrategyResultsFilter> f96li;
    ListView list;
    int prodRs;
    boolean showAds = true;

    public long getItemId(int i) {
        return (long) i;
    }

    public AdapterFinderLoad(Context context2, List<StrategyResultsFilter> list2, int i, boolean z) {
        this.context = context2;
        this.f96li = list2;
        this.application = (MyGreeksApplication) context2.getApplicationContext();
        this.adRequest = new AdRequest.Builder().setRequestAgent("android_studio:ad_template").build();
        this.prodRs = i;
        this.showAds = z;
    }

    public AdapterFinderLoad(Context context2, List<StrategyResultsFilter> list2, boolean z) {
        this.context = context2;
        this.f96li = list2;
        this.application = (MyGreeksApplication) context2.getApplicationContext();
        this.adRequest = new AdRequest.Builder().setRequestAgent("android_studio:ad_template").build();
        this.prodRs = this.prodRs;
        this.showAds = z;
        this.isFolio = true;
    }

    public int getViewTypeCount() {
        return this.showAds ? 3 : 1;
    }

    public int getItemViewType(int i) {
        if (this.showAds) {
            if (i > 0 && i % 12 == 0) {
                return 1;
            }
            if (i <= 0 || i % 6 != 0) {
                return 0;
            }
            return 2;
        }
        return 0;
    }

    public int getCount() {
        List<StrategyResultsFilter> list2 = this.f96li;
        if (list2 == null) {
            return 0;
        }
        return list2.size();
    }

    public Object getItem(int i) {
        return this.f96li.get(i);
    }

    public void setItemList(List<StrategyResultsFilter> list2) {
        this.f96li = list2;
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:0x004c  */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x0074  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public android.view.View getView(int r22, android.view.View r23, android.view.ViewGroup r24) {
        /*
            r21 = this;
            r0 = r21
            r1 = r24
            java.text.DecimalFormat r2 = new java.text.DecimalFormat
            java.lang.String r3 = "0.00"
            r2.<init>(r3)
            int r3 = r21.getItemViewType(r22)
            java.util.List<bulltrack.com.optionanalyzer.dao.StrategyResultsFilter> r4 = r0.f96li
            r5 = r22
            java.lang.Object r4 = r4.get(r5)
            bulltrack.com.optionanalyzer.dao.StrategyResultsFilter r4 = (bulltrack.com.optionanalyzer.dao.StrategyResultsFilter) r4
            r5 = 2
            r6 = 1
            r7 = 0
            if (r23 != 0) goto L_0x0046
            android.content.Context r8 = r0.context
            java.lang.String r9 = "layout_inflater"
            java.lang.Object r8 = r8.getSystemService(r9)
            android.view.LayoutInflater r8 = (android.view.LayoutInflater) r8
            if (r3 != 0) goto L_0x0032
            r9 = 2131427409(0x7f0b0051, float:1.8476433E38)
            android.view.View r1 = r8.inflate(r9, r1, r7)
            goto L_0x0048
        L_0x0032:
            if (r3 != r6) goto L_0x003c
            r9 = 2131427410(0x7f0b0052, float:1.8476435E38)
            android.view.View r1 = r8.inflate(r9, r1, r7)
            goto L_0x0048
        L_0x003c:
            if (r3 != r5) goto L_0x0046
            r9 = 2131427406(0x7f0b004e, float:1.8476427E38)
            android.view.View r1 = r8.inflate(r9, r1, r7)
            goto L_0x0048
        L_0x0046:
            r1 = r23
        L_0x0048:
            java.lang.String r8 = "₹ "
            if (r3 != r6) goto L_0x0074
            r2 = 2131231566(0x7f08034e, float:1.8079217E38)
            android.view.View r2 = r1.findViewById(r2)
            android.widget.TextView r2 = (android.widget.TextView) r2
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            r3.append(r8)
            bulltrack.com.optionanalyzer.application.MyGreeksApplication r4 = r0.application
            int r5 = r0.prodRs
            double r5 = (double) r5
            java.lang.String r4 = r4.round2Decimals(r5)
            r3.append(r4)
            java.lang.String r3 = r3.toString()
            r2.setText(r3)
        L_0x0070:
            r17 = r1
            goto L_0x0315
        L_0x0074:
            if (r3 != r5) goto L_0x0085
            r2 = 2131230802(0x7f080052, float:1.8077667E38)
            android.view.View r2 = r1.findViewById(r2)
            com.google.android.gms.ads.AdView r2 = (com.google.android.gms.ads.AdView) r2
            com.google.android.gms.ads.AdRequest r3 = r0.adRequest
            r2.loadAd(r3)
            goto L_0x0070
        L_0x0085:
            if (r3 != 0) goto L_0x0070
            r3 = 2131231584(0x7f080360, float:1.8079253E38)
            android.view.View r3 = r1.findViewById(r3)
            android.widget.TextView r3 = (android.widget.TextView) r3
            r5 = 2131231583(0x7f08035f, float:1.8079251E38)
            android.view.View r5 = r1.findViewById(r5)
            android.widget.TextView r5 = (android.widget.TextView) r5
            r6 = 2131231580(0x7f08035c, float:1.8079245E38)
            android.view.View r6 = r1.findViewById(r6)
            android.widget.TextView r6 = (android.widget.TextView) r6
            r9 = 2131231571(0x7f080353, float:1.8079227E38)
            android.view.View r9 = r1.findViewById(r9)
            android.widget.TextView r9 = (android.widget.TextView) r9
            r10 = 2131230986(0x7f08010a, float:1.807804E38)
            android.view.View r10 = r1.findViewById(r10)
            android.widget.ImageView r10 = (android.widget.ImageView) r10
            r11 = 2131230987(0x7f08010b, float:1.8078042E38)
            android.view.View r11 = r1.findViewById(r11)
            android.widget.ImageView r11 = (android.widget.ImageView) r11
            r12 = 2131231581(0x7f08035d, float:1.8079247E38)
            android.view.View r12 = r1.findViewById(r12)
            android.widget.TextView r12 = (android.widget.TextView) r12
            r13 = 2131231582(0x7f08035e, float:1.807925E38)
            android.view.View r13 = r1.findViewById(r13)
            android.widget.TextView r13 = (android.widget.TextView) r13
            r14 = 2131231577(0x7f080359, float:1.8079239E38)
            android.view.View r14 = r1.findViewById(r14)
            android.widget.TextView r14 = (android.widget.TextView) r14
            r15 = 2131231575(0x7f080357, float:1.8079235E38)
            android.view.View r15 = r1.findViewById(r15)
            android.widget.TextView r15 = (android.widget.TextView) r15
            r7 = 2131231574(0x7f080356, float:1.8079233E38)
            android.view.View r7 = r1.findViewById(r7)
            android.widget.TextView r7 = (android.widget.TextView) r7
            r23 = r7
            r7 = 2131231572(0x7f080354, float:1.8079229E38)
            android.view.View r7 = r1.findViewById(r7)
            android.widget.TextView r7 = (android.widget.TextView) r7
            r24 = r7
            r7 = 2131231567(0x7f08034f, float:1.8079219E38)
            android.view.View r7 = r1.findViewById(r7)
            android.widget.TextView r7 = (android.widget.TextView) r7
            r16 = r7
            r7 = 2131231568(0x7f080350, float:1.807922E38)
            android.view.View r7 = r1.findViewById(r7)
            android.widget.TextView r7 = (android.widget.TextView) r7
            r17 = r1
            boolean r1 = r0.showAds
            r18 = r15
            r15 = 4
            if (r1 == 0) goto L_0x0128
            int r1 = r4.getAudience()
            if (r1 != 0) goto L_0x0124
            r1 = 0
            r7.setVisibility(r1)
            java.lang.String r1 = "Free"
            r7.setText(r1)
            goto L_0x012b
        L_0x0124:
            r7.setVisibility(r15)
            goto L_0x012b
        L_0x0128:
            r7.setVisibility(r15)
        L_0x012b:
            java.lang.String r1 = r4.getSymbol()
            r3.setText(r1)
            java.lang.String r1 = r4.getStrategyName()
            r5.setText(r1)
            float r1 = r4.getMaxRisk()
            int r3 = r4.getLotSize()
            float r3 = (float) r3
            float r1 = r1 * r3
            r3 = r14
            double r14 = (double) r1
            float r1 = r4.getMaxGain()
            int r5 = r4.getLotSize()
            float r5 = (float) r5
            float r1 = r1 * r5
            r5 = r12
            r19 = r13
            double r12 = (double) r1
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            r1.append(r8)
            r20 = r7
            bulltrack.com.optionanalyzer.application.MyGreeksApplication r7 = r0.application
            java.lang.String r7 = r7.round2Decimals1000(r14)
            r1.append(r7)
            java.lang.String r1 = r1.toString()
            r6.setText(r1)
            int r1 = r4.getStrategyId()
            r6 = 104(0x68, float:1.46E-43)
            if (r1 == r6) goto L_0x01ca
            int r1 = r4.getStrategyId()
            r6 = 105(0x69, float:1.47E-43)
            if (r1 == r6) goto L_0x01ca
            int r1 = r4.getStrategyId()
            r6 = 111(0x6f, float:1.56E-43)
            if (r1 != r6) goto L_0x0188
            goto L_0x01ca
        L_0x0188:
            r6 = 4681608360884174848(0x40f86a0000000000, double:100000.0)
            int r1 = (r12 > r6 ? 1 : (r12 == r6 ? 0 : -1))
            if (r1 <= 0) goto L_0x01b1
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            r1.append(r8)
            java.lang.Double.isNaN(r12)
            double r12 = r12 / r6
            java.lang.String r6 = r2.format(r12)
            r1.append(r6)
            java.lang.String r6 = " Lac"
            r1.append(r6)
            java.lang.String r1 = r1.toString()
            r9.setText(r1)
            goto L_0x01cf
        L_0x01b1:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            r1.append(r8)
            bulltrack.com.optionanalyzer.application.MyGreeksApplication r6 = r0.application
            java.lang.String r6 = r6.round2Decimals1000(r12)
            r1.append(r6)
            java.lang.String r1 = r1.toString()
            r9.setText(r1)
            goto L_0x01cf
        L_0x01ca:
            java.lang.String r1 = "Infinite"
            r9.setText(r1)
        L_0x01cf:
            bulltrack.com.optionanalyzer.application.MyGreeksApplication r1 = r0.application
            int r6 = r4.getStrategyId()
            int r1 = r1.getPayoffDiagram(r6)
            r10.setImageResource(r1)
            float r1 = r4.getInvestment()
            float r6 = r4.getInterestCost()
            float r1 = r1 - r6
            double r6 = (double) r1
            float r1 = r4.getInterestCost()
            double r8 = (double) r1
            java.lang.Double.isNaN(r6)
            java.lang.Double.isNaN(r8)
            double r6 = r6 / r8
            r8 = 4636737291354636288(0x4059000000000000, double:100.0)
            double r6 = r6 * r8
            r12 = 0
            int r1 = (r6 > r12 ? 1 : (r6 == r12 ? 0 : -1))
            if (r1 < 0) goto L_0x0215
            r1 = 2131165400(0x7f0700d8, float:1.7945016E38)
            r11.setImageResource(r1)
            java.lang.String r1 = "#2E8B57"
            int r10 = android.graphics.Color.parseColor(r1)
            r5.setTextColor(r10)
            int r1 = android.graphics.Color.parseColor(r1)
            r10 = r19
            r10.setTextColor(r1)
            goto L_0x0225
        L_0x0215:
            r10 = r19
            r1 = 2131165331(0x7f070093, float:1.7944876E38)
            r11.setImageResource(r1)
            r1 = -65536(0xffffffffffff0000, float:NaN)
            r5.setTextColor(r1)
            r10.setTextColor(r1)
        L_0x0225:
            bulltrack.com.optionanalyzer.application.MyGreeksApplication r1 = r0.application
            float r11 = r4.getInvestment()
            double r14 = (double) r11
            java.lang.String r1 = r1.round2Decimals1000(r14)
            r5.setText(r1)
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r5 = r2.format(r6)
            r1.append(r5)
            java.lang.String r5 = "%"
            r1.append(r5)
            java.lang.String r1 = r1.toString()
            r10.setText(r1)
            float r1 = r4.getBreakevenDown()
            float r5 = r4.getInvestment()
            float r1 = r1 - r5
            float r5 = r4.getInvestment()
            float r1 = r1 / r5
            double r5 = (double) r1
            java.lang.Double.isNaN(r5)
            double r5 = r5 * r8
            float r1 = r4.getBreakevenDown()
            double r10 = (double) r1
            java.lang.String r1 = "-"
            java.lang.String r7 = "X"
            java.lang.String r14 = "% away"
            int r15 = (r10 > r12 ? 1 : (r10 == r12 ? 0 : -1))
            if (r15 > 0) goto L_0x0277
            r3.setText(r7)
            r15 = r18
            r15.setText(r1)
            goto L_0x029d
        L_0x0277:
            r15 = r18
            bulltrack.com.optionanalyzer.application.MyGreeksApplication r10 = r0.application
            float r11 = r4.getBreakevenDown()
            double r12 = (double) r11
            java.lang.String r10 = r10.round2Decimals1000(r12)
            r3.setText(r10)
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r5 = r2.format(r5)
            r3.append(r5)
            r3.append(r14)
            java.lang.String r3 = r3.toString()
            r15.setText(r3)
        L_0x029d:
            float r3 = r4.getBreakevenUp()
            float r5 = r4.getInvestment()
            float r3 = r3 - r5
            float r5 = r4.getInvestment()
            float r3 = r3 / r5
            double r5 = (double) r3
            java.lang.Double.isNaN(r5)
            double r5 = r5 * r8
            float r3 = r4.getBreakevenUp()
            double r8 = (double) r3
            r10 = 0
            int r3 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1))
            if (r3 > 0) goto L_0x02c7
            r3 = r23
            r3.setText(r7)
            r7 = r24
            r7.setText(r1)
            goto L_0x02ef
        L_0x02c7:
            r3 = r23
            r7 = r24
            bulltrack.com.optionanalyzer.application.MyGreeksApplication r1 = r0.application
            float r8 = r4.getBreakevenUp()
            double r8 = (double) r8
            java.lang.String r1 = r1.round2Decimals1000(r8)
            r3.setText(r1)
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = r2.format(r5)
            r1.append(r2)
            r1.append(r14)
            java.lang.String r1 = r1.toString()
            r7.setText(r1)
        L_0x02ef:
            boolean r1 = r0.isFolio
            if (r1 == 0) goto L_0x030f
            r7 = r16
            r1 = 0
            r7.setVisibility(r1)
            bulltrack.com.optionanalyzer.application.MyGreeksApplication r1 = r0.application
            java.util.Date r2 = r4.getUpdD()
            java.lang.String r3 = "ddMMM"
            java.lang.String r1 = r1.dateFormatter((java.util.Date) r2, (java.lang.String) r3)
            r7.setText(r1)
            r7 = r20
            r1 = 4
            r7.setVisibility(r1)
            goto L_0x0315
        L_0x030f:
            r7 = r16
            r1 = 4
            r7.setVisibility(r1)
        L_0x0315:
            return r17
        */
        throw new UnsupportedOperationException("Method not decompiled: bulltrack.com.optionanalyzer.adapter.AdapterFinderLoad.getView(int, android.view.View, android.view.ViewGroup):android.view.View");
    }
}
