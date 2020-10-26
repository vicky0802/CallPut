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
import java.util.List;

public class WatchListAdapter extends BaseAdapter {
    private MyGreeksApplication application;
    /* access modifiers changed from: private */
    public Context context;

    /* renamed from: li */
    List<GreekValues> f107li;
    ListView list;

    public long getItemId(int i) {
        return (long) i;
    }

    public WatchListAdapter(Context context2, List<GreekValues> list2) {
        this.context = context2;
        this.f107li = list2;
        this.application = (MyGreeksApplication) context2.getApplicationContext();
    }

    public int getCount() {
        List<GreekValues> list2 = this.f107li;
        if (list2 == null) {
            return 0;
        }
        return list2.size();
    }

    public Object getItem(int i) {
        return this.f107li.get(i);
    }

    public void setItemList(List<GreekValues> list2) {
        this.f107li = list2;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        final int i2 = i;
        final GreekValues greekValues = this.f107li.get(i2);
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
        TextView textView8 = (TextView) inflate.findViewById(R.id.tv_greeks_option_ltp);
        TextView textView9 = (TextView) inflate.findViewById(R.id.tv_greeks_opotion_pricechange);
        final ImageView imageView = (ImageView) inflate.findViewById(R.id.img_greek_overflow);
        View view2 = inflate;
        imageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(WatchListAdapter.this.context, imageView);
                popupMenu.getMenuInflater().inflate(R.menu.watch_popup_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    /* JADX WARNING: Can't fix incorrect switch cases order */
                    /* Code decompiled incorrectly, please refer to instructions dump. */
                    public boolean onMenuItemClick(android.view.MenuItem r5) {
                        /*
                            r4 = this;
                            java.lang.CharSequence r5 = r5.getTitle()
                            java.lang.String r5 = r5.toString()
                            int r0 = r5.hashCode()
                            r1 = 3
                            r2 = 2
                            r3 = 1
                            switch(r0) {
                                case -1175969022: goto L_0x0031;
                                case -1018828832: goto L_0x0027;
                                case 899757170: goto L_0x001d;
                                case 1371636270: goto L_0x0013;
                                default: goto L_0x0012;
                            }
                        L_0x0012:
                            goto L_0x003b
                        L_0x0013:
                            java.lang.String r0 = "Delete from Watch"
                            boolean r5 = r5.equals(r0)
                            if (r5 == 0) goto L_0x003b
                            r5 = 0
                            goto L_0x003c
                        L_0x001d:
                            java.lang.String r0 = "View Option details"
                            boolean r5 = r5.equals(r0)
                            if (r5 == 0) goto L_0x003b
                            r5 = 2
                            goto L_0x003c
                        L_0x0027:
                            java.lang.String r0 = "Recalculate Greeks"
                            boolean r5 = r5.equals(r0)
                            if (r5 == 0) goto L_0x003b
                            r5 = 3
                            goto L_0x003c
                        L_0x0031:
                            java.lang.String r0 = "Add to Portfolio"
                            boolean r5 = r5.equals(r0)
                            if (r5 == 0) goto L_0x003b
                            r5 = 1
                            goto L_0x003c
                        L_0x003b:
                            r5 = -1
                        L_0x003c:
                            if (r5 == 0) goto L_0x008e
                            if (r5 == r3) goto L_0x0076
                            if (r5 == r2) goto L_0x005e
                            if (r5 == r1) goto L_0x0046
                            goto L_0x00c2
                        L_0x0046:
                            bulltrack.com.optionanalyzer.adapter.WatchListAdapter$1 r5 = bulltrack.com.optionanalyzer.adapter.WatchListAdapter.C07681.this
                            bulltrack.com.optionanalyzer.adapter.WatchListAdapter r5 = bulltrack.com.optionanalyzer.adapter.WatchListAdapter.this
                            bulltrack.com.optionanalyzer.application.MyGreeksApplication r5 = r5.getGreekApplication()
                            bulltrack.com.optionanalyzer.adapter.WatchListAdapter$1 r0 = bulltrack.com.optionanalyzer.adapter.WatchListAdapter.C07681.this
                            bulltrack.com.optionanalyzer.adapter.WatchListAdapter r0 = bulltrack.com.optionanalyzer.adapter.WatchListAdapter.this
                            android.content.Context r0 = r0.context
                            bulltrack.com.optionanalyzer.adapter.WatchListAdapter$1 r1 = bulltrack.com.optionanalyzer.adapter.WatchListAdapter.C07681.this
                            bulltrack.com.optionanalyzer.dao.GreekValues r1 = r2
                            r5.reCalculateGreeks(r0, r1)
                            goto L_0x00c2
                        L_0x005e:
                            bulltrack.com.optionanalyzer.adapter.WatchListAdapter$1 r5 = bulltrack.com.optionanalyzer.adapter.WatchListAdapter.C07681.this
                            bulltrack.com.optionanalyzer.adapter.WatchListAdapter r5 = bulltrack.com.optionanalyzer.adapter.WatchListAdapter.this
                            bulltrack.com.optionanalyzer.application.MyGreeksApplication r5 = r5.getGreekApplication()
                            bulltrack.com.optionanalyzer.adapter.WatchListAdapter$1 r0 = bulltrack.com.optionanalyzer.adapter.WatchListAdapter.C07681.this
                            bulltrack.com.optionanalyzer.adapter.WatchListAdapter r0 = bulltrack.com.optionanalyzer.adapter.WatchListAdapter.this
                            android.content.Context r0 = r0.context
                            bulltrack.com.optionanalyzer.adapter.WatchListAdapter$1 r1 = bulltrack.com.optionanalyzer.adapter.WatchListAdapter.C07681.this
                            bulltrack.com.optionanalyzer.dao.GreekValues r1 = r2
                            r5.startViewOptionDetailsActivity(r0, r1, r3)
                            goto L_0x00c2
                        L_0x0076:
                            bulltrack.com.optionanalyzer.adapter.WatchListAdapter$1 r5 = bulltrack.com.optionanalyzer.adapter.WatchListAdapter.C07681.this
                            bulltrack.com.optionanalyzer.adapter.WatchListAdapter r5 = bulltrack.com.optionanalyzer.adapter.WatchListAdapter.this
                            bulltrack.com.optionanalyzer.application.MyGreeksApplication r5 = r5.getGreekApplication()
                            bulltrack.com.optionanalyzer.adapter.WatchListAdapter$1 r0 = bulltrack.com.optionanalyzer.adapter.WatchListAdapter.C07681.this
                            bulltrack.com.optionanalyzer.adapter.WatchListAdapter r0 = bulltrack.com.optionanalyzer.adapter.WatchListAdapter.this
                            android.content.Context r0 = r0.context
                            bulltrack.com.optionanalyzer.adapter.WatchListAdapter$1 r1 = bulltrack.com.optionanalyzer.adapter.WatchListAdapter.C07681.this
                            bulltrack.com.optionanalyzer.dao.GreekValues r1 = r2
                            r5.startAddItemToFolioActivity(r0, r1)
                            goto L_0x00c2
                        L_0x008e:
                            bulltrack.com.optionanalyzer.adapter.WatchListAdapter$1 r5 = bulltrack.com.optionanalyzer.adapter.WatchListAdapter.C07681.this
                            bulltrack.com.optionanalyzer.adapter.WatchListAdapter r5 = bulltrack.com.optionanalyzer.adapter.WatchListAdapter.this
                            bulltrack.com.optionanalyzer.application.MyGreeksApplication r5 = r5.getGreekApplication()
                            bulltrack.com.optionanalyzer.adapter.WatchListAdapter$1 r0 = bulltrack.com.optionanalyzer.adapter.WatchListAdapter.C07681.this
                            bulltrack.com.optionanalyzer.dao.GreekValues r0 = r2
                            r5.deleteWatchItem(r0)
                            bulltrack.com.optionanalyzer.adapter.WatchListAdapter$1 r5 = bulltrack.com.optionanalyzer.adapter.WatchListAdapter.C07681.this
                            bulltrack.com.optionanalyzer.adapter.WatchListAdapter r5 = bulltrack.com.optionanalyzer.adapter.WatchListAdapter.this
                            java.util.List<bulltrack.com.optionanalyzer.dao.GreekValues> r5 = r5.f107li
                            bulltrack.com.optionanalyzer.adapter.WatchListAdapter$1 r0 = bulltrack.com.optionanalyzer.adapter.WatchListAdapter.C07681.this
                            int r0 = r1
                            r5.remove(r0)
                            bulltrack.com.optionanalyzer.adapter.WatchListAdapter$1 r5 = bulltrack.com.optionanalyzer.adapter.WatchListAdapter.C07681.this
                            bulltrack.com.optionanalyzer.adapter.WatchListAdapter r5 = bulltrack.com.optionanalyzer.adapter.WatchListAdapter.this
                            r5.notifyDataSetChanged()
                            bulltrack.com.optionanalyzer.adapter.WatchListAdapter$1 r5 = bulltrack.com.optionanalyzer.adapter.WatchListAdapter.C07681.this
                            bulltrack.com.optionanalyzer.adapter.WatchListAdapter r5 = bulltrack.com.optionanalyzer.adapter.WatchListAdapter.this
                            android.content.Context r5 = r5.context
                            java.lang.String r0 = "Watch Deleted"
                            android.widget.Toast r5 = android.widget.Toast.makeText(r5, r0, r3)
                            r5.show()
                        L_0x00c2:
                            return r3
                        */
                        throw new UnsupportedOperationException("Method not decompiled: bulltrack.com.optionanalyzer.adapter.WatchListAdapter.C07681.C07691.onMenuItemClick(android.view.MenuItem):boolean");
                    }
                });
                popupMenu.show();
            }
        });
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.DT_FMT_dd_MMM_yyyy);
        ((TextView) inflate.findViewById(R.id.tv_greeks_underlyer)).setText(greekValues.getSymbol());
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
        if (((double) greekValues.getUnderlyingValue()) == 0.0d && ((double) greekValues.getLastPrice()) == 0.0d) {
            TextView textView10 = textView8;
            textView10.setText(decimalFormat.format((double) greekValues.getClosePrice()));
            textView9.setText("- | -%");
            textView10.setTextColor(-7829368);
        } else {
            TextView textView11 = textView8;
            TextView textView12 = textView9;
            if (((double) greekValues.getpChange()) >= 0.0d) {
                textView11.setTextColor(Color.parseColor("#2E8B57"));
                textView12.setText("+" + decimalFormat.format((double) greekValues.getChange()) + " | " + decimalFormat.format((double) greekValues.getpChange()) + "%");
                textView12.setTextColor(Color.parseColor("#2E8B57"));
            } else {
                textView11.setTextColor(SupportMenu.CATEGORY_MASK);
                textView12.setText(decimalFormat.format((double) greekValues.getChange()) + " | " + decimalFormat.format((double) greekValues.getpChange()) + "%");
                textView12.setTextColor(SupportMenu.CATEGORY_MASK);
            }
            textView11.setText(decimalFormat.format((double) greekValues.getLastPrice()));
        }
        return view2;
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
