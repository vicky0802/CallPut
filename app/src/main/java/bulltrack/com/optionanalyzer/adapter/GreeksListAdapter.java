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
import androidx.appcompat.widget.PopupMenu;
import androidx.core.internal.view.SupportMenu;
import bulltrack.com.optionanalyzer.application.MyGreeksApplication;
import bulltrack.com.optionanalyzer.constants.Constants;
import bulltrack.com.optionanalyzer.dao.GreekValues;
import bulltrack.com.optiongreeks13.R;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GreeksListAdapter extends BaseAdapter {
    private MyGreeksApplication application;
    /* access modifiers changed from: private */
    public Context context;

    /* renamed from: li */
    List<GreekValues> f102li;
    List<GreekValues> liSearchResults;
    ListView list;

    public long getItemId(int i) {
        return (long) i;
    }

    public GreeksListAdapter(Context context2, List<GreekValues> list2) {
        this.context = context2;
        this.f102li = list2;
        this.application = (MyGreeksApplication) context2.getApplicationContext();
        ArrayList arrayList = new ArrayList();
        this.liSearchResults = arrayList;
        arrayList.addAll(list2);
    }

    public int getCount() {
        List<GreekValues> list2 = this.f102li;
        if (list2 == null) {
            return 0;
        }
        return list2.size();
    }

    public Object getItem(int i) {
        return this.f102li.get(i);
    }

    public void setItemList(List<GreekValues> list2) {
        this.f102li = list2;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        final GreekValues greekValues = this.f102li.get(i);
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        DecimalFormat decimalFormat2 = new DecimalFormat("0.0000");
        View inflate = view == null ? ((LayoutInflater) this.context.getSystemService("layout_inflater")).inflate(R.layout.greeks, viewGroup, false) : view;
        TextView textView = (TextView) inflate.findViewById(R.id.tv_greeks_callput);
        TextView textView2 = (TextView) inflate.findViewById(R.id.tv_greeks_expiry);
        TextView textView3 = (TextView) inflate.findViewById(R.id.tv_greeks_optionvalue);
        TextView textView4 = (TextView) inflate.findViewById(R.id.tv_greeks_delta);
        TextView textView5 = (TextView) inflate.findViewById(R.id.tv_greeks_gamma);
        TextView textView6 = (TextView) inflate.findViewById(R.id.tv_greeks_theta);
        TextView textView7 = (TextView) inflate.findViewById(R.id.tv_greeks_vega);
        TextView textView8 = (TextView) inflate.findViewById(R.id.tv_greeks_opotion_pricechange);
        final ImageView imageView = (ImageView) inflate.findViewById(R.id.img_greek_overflow);
        View view2 = inflate;
        imageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(GreeksListAdapter.this.context, imageView);
                popupMenu.getMenuInflater().inflate(R.menu.greek_popup_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    /* JADX WARNING: Can't fix incorrect switch cases order */
                    /* Code decompiled incorrectly, please refer to instructions dump. */
                    public boolean onMenuItemClick(android.view.MenuItem r6) {
                        /*
                            r5 = this;
                            java.lang.CharSequence r6 = r6.getTitle()
                            java.lang.String r6 = r6.toString()
                            int r0 = r6.hashCode()
                            r1 = 0
                            r2 = 3
                            r3 = 2
                            r4 = 1
                            switch(r0) {
                                case -1175969022: goto L_0x0032;
                                case -1018828832: goto L_0x0028;
                                case 899757170: goto L_0x001e;
                                case 1344130953: goto L_0x0014;
                                default: goto L_0x0013;
                            }
                        L_0x0013:
                            goto L_0x003c
                        L_0x0014:
                            java.lang.String r0 = "Add to Watch"
                            boolean r6 = r6.equals(r0)
                            if (r6 == 0) goto L_0x003c
                            r6 = 0
                            goto L_0x003d
                        L_0x001e:
                            java.lang.String r0 = "View Option details"
                            boolean r6 = r6.equals(r0)
                            if (r6 == 0) goto L_0x003c
                            r6 = 2
                            goto L_0x003d
                        L_0x0028:
                            java.lang.String r0 = "Recalculate Greeks"
                            boolean r6 = r6.equals(r0)
                            if (r6 == 0) goto L_0x003c
                            r6 = 3
                            goto L_0x003d
                        L_0x0032:
                            java.lang.String r0 = "Add to Portfolio"
                            boolean r6 = r6.equals(r0)
                            if (r6 == 0) goto L_0x003c
                            r6 = 1
                            goto L_0x003d
                        L_0x003c:
                            r6 = -1
                        L_0x003d:
                            if (r6 == 0) goto L_0x008e
                            if (r6 == r4) goto L_0x0076
                            if (r6 == r3) goto L_0x005e
                            if (r6 == r2) goto L_0x0046
                            goto L_0x00a5
                        L_0x0046:
                            bulltrack.com.optionanalyzer.adapter.GreeksListAdapter$1 r6 = bulltrack.com.optionanalyzer.adapter.GreeksListAdapter.C07571.this
                            bulltrack.com.optionanalyzer.adapter.GreeksListAdapter r6 = bulltrack.com.optionanalyzer.adapter.GreeksListAdapter.this
                            bulltrack.com.optionanalyzer.application.MyGreeksApplication r6 = r6.getGreekApplication()
                            bulltrack.com.optionanalyzer.adapter.GreeksListAdapter$1 r0 = bulltrack.com.optionanalyzer.adapter.GreeksListAdapter.C07571.this
                            bulltrack.com.optionanalyzer.adapter.GreeksListAdapter r0 = bulltrack.com.optionanalyzer.adapter.GreeksListAdapter.this
                            android.content.Context r0 = r0.context
                            bulltrack.com.optionanalyzer.adapter.GreeksListAdapter$1 r1 = bulltrack.com.optionanalyzer.adapter.GreeksListAdapter.C07571.this
                            bulltrack.com.optionanalyzer.dao.GreekValues r1 = r1
                            r6.reCalculateGreeks(r0, r1)
                            goto L_0x00a5
                        L_0x005e:
                            bulltrack.com.optionanalyzer.adapter.GreeksListAdapter$1 r6 = bulltrack.com.optionanalyzer.adapter.GreeksListAdapter.C07571.this
                            bulltrack.com.optionanalyzer.adapter.GreeksListAdapter r6 = bulltrack.com.optionanalyzer.adapter.GreeksListAdapter.this
                            bulltrack.com.optionanalyzer.application.MyGreeksApplication r6 = r6.getGreekApplication()
                            bulltrack.com.optionanalyzer.adapter.GreeksListAdapter$1 r0 = bulltrack.com.optionanalyzer.adapter.GreeksListAdapter.C07571.this
                            bulltrack.com.optionanalyzer.adapter.GreeksListAdapter r0 = bulltrack.com.optionanalyzer.adapter.GreeksListAdapter.this
                            android.content.Context r0 = r0.context
                            bulltrack.com.optionanalyzer.adapter.GreeksListAdapter$1 r2 = bulltrack.com.optionanalyzer.adapter.GreeksListAdapter.C07571.this
                            bulltrack.com.optionanalyzer.dao.GreekValues r2 = r1
                            r6.startViewOptionDetailsActivity(r0, r2, r1)
                            goto L_0x00a5
                        L_0x0076:
                            bulltrack.com.optionanalyzer.adapter.GreeksListAdapter$1 r6 = bulltrack.com.optionanalyzer.adapter.GreeksListAdapter.C07571.this
                            bulltrack.com.optionanalyzer.adapter.GreeksListAdapter r6 = bulltrack.com.optionanalyzer.adapter.GreeksListAdapter.this
                            bulltrack.com.optionanalyzer.application.MyGreeksApplication r6 = r6.getGreekApplication()
                            bulltrack.com.optionanalyzer.adapter.GreeksListAdapter$1 r0 = bulltrack.com.optionanalyzer.adapter.GreeksListAdapter.C07571.this
                            bulltrack.com.optionanalyzer.adapter.GreeksListAdapter r0 = bulltrack.com.optionanalyzer.adapter.GreeksListAdapter.this
                            android.content.Context r0 = r0.context
                            bulltrack.com.optionanalyzer.adapter.GreeksListAdapter$1 r1 = bulltrack.com.optionanalyzer.adapter.GreeksListAdapter.C07571.this
                            bulltrack.com.optionanalyzer.dao.GreekValues r1 = r1
                            r6.startAddItemToFolioActivity(r0, r1)
                            goto L_0x00a5
                        L_0x008e:
                            bulltrack.com.optionanalyzer.adapter.GreeksListAdapter$1 r6 = bulltrack.com.optionanalyzer.adapter.GreeksListAdapter.C07571.this
                            bulltrack.com.optionanalyzer.adapter.GreeksListAdapter r6 = bulltrack.com.optionanalyzer.adapter.GreeksListAdapter.this
                            bulltrack.com.optionanalyzer.application.MyGreeksApplication r6 = r6.getGreekApplication()
                            bulltrack.com.optionanalyzer.adapter.GreeksListAdapter$1 r0 = bulltrack.com.optionanalyzer.adapter.GreeksListAdapter.C07571.this
                            bulltrack.com.optionanalyzer.adapter.GreeksListAdapter r0 = bulltrack.com.optionanalyzer.adapter.GreeksListAdapter.this
                            android.content.Context r0 = r0.context
                            bulltrack.com.optionanalyzer.adapter.GreeksListAdapter$1 r1 = bulltrack.com.optionanalyzer.adapter.GreeksListAdapter.C07571.this
                            bulltrack.com.optionanalyzer.dao.GreekValues r1 = r1
                            r6.addItemToWatch(r0, r1)
                        L_0x00a5:
                            return r4
                        */
                        throw new UnsupportedOperationException("Method not decompiled: bulltrack.com.optionanalyzer.adapter.GreeksListAdapter.C07571.C07581.onMenuItemClick(android.view.MenuItem):boolean");
                    }
                });
                popupMenu.show();
            }
        });
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.DT_FMT_dd_MMM_yyyy);
        ((TextView) inflate.findViewById(R.id.tv_greeks_underlyer)).setText(greekValues.getSymbol());
        TextView textView9 = (TextView) inflate.findViewById(R.id.tv_greeks_option_ltp);
        ((TextView) inflate.findViewById(R.id.tv_greeks_strike)).setText(decimalFormat.format((double) greekValues.getStrike()));
        if (greekValues.getCallPut().equalsIgnoreCase("C")) {
            textView.setText("Call");
        } else {
            textView.setText("Put");
        }
        textView2.setText(simpleDateFormat.format(greekValues.getExpiry_d()));
        textView3.setText(decimalFormat.format((double) greekValues.getTheoValue()));
        textView4.setText(decimalFormat.format((double) greekValues.getDelta()));
        textView5.setText(decimalFormat2.format((double) greekValues.getGamma()));
        textView6.setText(decimalFormat.format((double) greekValues.getTheta()));
        textView7.setText(decimalFormat.format((double) greekValues.getVega()) + "");
        if (((double) greekValues.getMarketLot()) == 0.0d && ((double) greekValues.getUnderlyingValue()) == 0.0d && ((double) greekValues.getLastPrice()) == 0.0d) {
            TextView textView10 = textView9;
            textView10.setText(decimalFormat.format((double) greekValues.getClosePrice()));
            textView8.setText("- | -%");
            textView10.setTextColor(-7829368);
        } else {
            TextView textView11 = textView8;
            TextView textView12 = textView9;
            if (((double) greekValues.getpChange()) >= 0.0d) {
                textView12.setTextColor(Color.parseColor("#2E8B57"));
                textView11.setTextColor(Color.parseColor("#2E8B57"));
                textView12.setText(decimalFormat.format((double) greekValues.getLastPrice()));
                textView11.setText("+" + decimalFormat.format((double) greekValues.getChange()) + " | " + decimalFormat.format((double) greekValues.getpChange()) + "%");
            } else {
                textView11.setTextColor(SupportMenu.CATEGORY_MASK);
                textView12.setTextColor(SupportMenu.CATEGORY_MASK);
                textView12.setText(decimalFormat.format((double) greekValues.getLastPrice()));
                textView11.setText(decimalFormat.format((double) greekValues.getChange()) + " | " + decimalFormat.format((double) greekValues.getpChange()) + "%");
            }
        }
        return view2;
    }

    private static class ViewHolder {
        protected TextView itemName;

        private ViewHolder() {
        }
    }

    public void filter(String str) {
        String lowerCase = str.toLowerCase(Locale.getDefault());
        this.f102li.clear();
        if (lowerCase.length() == 0) {
            this.f102li.addAll(this.liSearchResults);
        } else {
            for (GreekValues next : this.liSearchResults) {
                if (lowerCase.length() != 0 && next.getSymbol().toLowerCase(Locale.getDefault()).contains(lowerCase)) {
                    this.f102li.add(next);
                } else if (lowerCase.length() != 0 && Float.toString(next.getStrike()).contains(lowerCase)) {
                    this.f102li.add(next);
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
