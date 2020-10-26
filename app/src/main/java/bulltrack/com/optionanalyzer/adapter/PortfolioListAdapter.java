package bulltrack.com.optionanalyzer.adapter;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.internal.view.SupportMenu;
import bulltrack.com.optionanalyzer.application.MyGreeksApplication;
import bulltrack.com.optionanalyzer.constants.Constants;
import bulltrack.com.optionanalyzer.dao.GreekValues;
import bulltrack.com.optiongreeks13.R;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class PortfolioListAdapter extends BaseAdapter {
    /* access modifiers changed from: private */
    public MyGreeksApplication application;
    /* access modifiers changed from: private */
    public Context context;
    DatePickerDialog entryDatePickerDialog;

    /* renamed from: li */
    List<GreekValues> f106li;
    ListView list;

    public long getItemId(int i) {
        return (long) i;
    }

    public PortfolioListAdapter(Context context2, List<GreekValues> list2) {
        this.context = context2;
        this.f106li = list2;
        this.application = (MyGreeksApplication) context2.getApplicationContext();
    }

    public boolean isEnabled(int i) {
        return this.f106li.get(i).getExitDate() == null;
    }

    public int getCount() {
        List<GreekValues> list2 = this.f106li;
        if (list2 == null) {
            return 0;
        }
        return list2.size();
    }

    public Object getItem(int i) {
        return this.f106li.get(i);
    }

    public void setItemList(List<GreekValues> list2) {
        this.f106li = list2;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        float f;
        String str;
        float f2;
        final int i2 = i;
        final GreekValues greekValues = this.f106li.get(i2);
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        View inflate = view == null ? ((LayoutInflater) this.context.getSystemService("layout_inflater")).inflate(R.layout.portfolio_item, viewGroup, false) : view;
        TextView textView = (TextView) inflate.findViewById(R.id.tv_portfolio_item_callput);
        TextView textView2 = (TextView) inflate.findViewById(R.id.tv_portfolio_item_expiry);
        TextView textView3 = (TextView) inflate.findViewById(R.id.tv_portfolio_item_option_longshort);
        TextView textView4 = (TextView) inflate.findViewById(R.id.tv_portfolio_item_entry_val);
        TextView textView5 = (TextView) inflate.findViewById(R.id.tv_portfolio_item_entry_date_val);
        TextView textView6 = (TextView) inflate.findViewById(R.id.tv_portfolio_item_exitprice_txt);
        TextView textView7 = (TextView) inflate.findViewById(R.id.tv_portfolio_item_exitprice_txt_val);
        TextView textView8 = (TextView) inflate.findViewById(R.id.tv_portfolio_item_exit_date_val);
        TextView textView9 = (TextView) inflate.findViewById(R.id.tv_portfolio_item_qty_val);
        TextView textView10 = (TextView) inflate.findViewById(R.id.tv_portfolio_item_netprofit);
        final ImageView imageView = (ImageView) inflate.findViewById(R.id.img_folio_item_overflow);
        View view2 = inflate;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.DT_FMT_dd_MMM_yyyy);
        ((TextView) inflate.findViewById(R.id.tv_portfolio_item_underlyer)).setText(greekValues.getSymbol());
        ((TextView) inflate.findViewById(R.id.tv_portfolio_item_strike)).setText(greekValues.getStrike() + "");
        if (greekValues.getCallPut().equalsIgnoreCase("C")) {
            textView.setText("Call");
        } else {
            textView.setText("Put");
        }
        textView2.setText(simpleDateFormat.format(greekValues.getExpiry_d()));
        textView3.setText(greekValues.getLongShort() + "");
        textView4.setText(decimalFormat.format((double) greekValues.getEntryPrice()));
        textView5.setText("[ " + simpleDateFormat.format(greekValues.getEntryDate()) + " ]");
        if (greekValues.getExitDate() == null) {
            textView8.setVisibility(4);
            textView6.setText("LTP");
            textView7.setText(decimalFormat.format((double) greekValues.getLastPrice()) + "");
            if (greekValues.getLongShort().equalsIgnoreCase("Long")) {
                f2 = greekValues.getLastPrice() - greekValues.getEntryPrice();
                textView3.setTextColor(Color.parseColor("#2E8B57"));
            } else {
                f2 = greekValues.getEntryPrice() - greekValues.getLastPrice();
                textView3.setTextColor(SupportMenu.CATEGORY_MASK);
            }
            imageView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(PortfolioListAdapter.this.context, imageView);
                    final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.DT_FMT_dd_MMM_yyyy);
                    popupMenu.getMenuInflater().inflate(R.menu.folio_open_menu, popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        /* JADX WARNING: Can't fix incorrect switch cases order */
                        /* Code decompiled incorrectly, please refer to instructions dump. */
                        public boolean onMenuItemClick(MenuItem r17) {
                            /*
                                r16 = this;
                                r7 = r16
                                java.lang.CharSequence r0 = r17.getTitle()
                                java.lang.String r0 = r0.toString()
                                int r1 = r0.hashCode()
                                r2 = 3
                                r3 = 2
                                r8 = 1
                                switch(r1) {
                                    case -1878664377: goto L_0x0033;
                                    case -1328258338: goto L_0x0029;
                                    case -1018828832: goto L_0x001f;
                                    case 899757170: goto L_0x0015;
                                    default: goto L_0x0014;
                                }
                            L_0x0014:
                                goto L_0x003d
                            L_0x0015:
                                java.lang.String r1 = "View Option details"
                                boolean r0 = r0.equals(r1)
                                if (r0 == 0) goto L_0x003d
                                r0 = 1
                                goto L_0x003e
                            L_0x001f:
                                java.lang.String r1 = "Recalculate Greeks"
                                boolean r0 = r0.equals(r1)
                                if (r0 == 0) goto L_0x003d
                                r0 = 2
                                goto L_0x003e
                            L_0x0029:
                                java.lang.String r1 = "Close (Exit) position"
                                boolean r0 = r0.equals(r1)
                                if (r0 == 0) goto L_0x003d
                                r0 = 3
                                goto L_0x003e
                            L_0x0033:
                                java.lang.String r1 = "Delete from portfolio"
                                boolean r0 = r0.equals(r1)
                                if (r0 == 0) goto L_0x003d
                                r0 = 0
                                goto L_0x003e
                            L_0x003d:
                                r0 = -1
                            L_0x003e:
                                if (r0 == 0) goto L_0x010c
                                if (r0 == r8) goto L_0x00f4
                                if (r0 == r3) goto L_0x00dc
                                if (r0 == r2) goto L_0x0048
                                goto L_0x0140
                            L_0x0048:
                                bulltrack.com.optionanalyzer.adapter.PortfolioListAdapter$1 r0 = bulltrack.com.optionanalyzer.adapter.PortfolioListAdapter.C07591.this
                                int r6 = r1
                                android.app.Dialog r5 = new android.app.Dialog
                                bulltrack.com.optionanalyzer.adapter.PortfolioListAdapter$1 r0 = bulltrack.com.optionanalyzer.adapter.PortfolioListAdapter.C07591.this
                                bulltrack.com.optionanalyzer.adapter.PortfolioListAdapter r0 = bulltrack.com.optionanalyzer.adapter.PortfolioListAdapter.this
                                android.content.Context r0 = r0.context
                                r5.<init>(r0)
                                r0 = 2131427384(0x7f0b0038, float:1.8476383E38)
                                r5.setContentView(r0)
                                java.lang.String r0 = "Close Position"
                                r5.setTitle(r0)
                                r0 = 2131230921(0x7f0800c9, float:1.8077908E38)
                                android.view.View r0 = r5.findViewById(r0)
                                r4 = r0
                                android.widget.EditText r4 = (android.widget.EditText) r4
                                r0 = 2131230920(0x7f0800c8, float:1.8077906E38)
                                android.view.View r0 = r5.findViewById(r0)
                                r9 = r0
                                android.widget.EditText r9 = (android.widget.EditText) r9
                                r0 = 2131231554(0x7f080342, float:1.8079192E38)
                                android.view.View r0 = r5.findViewById(r0)
                                r2 = r0
                                android.widget.TextView r2 = (android.widget.TextView) r2
                                java.util.Calendar r0 = java.util.Calendar.getInstance()
                                bulltrack.com.optionanalyzer.adapter.PortfolioListAdapter$1 r1 = bulltrack.com.optionanalyzer.adapter.PortfolioListAdapter.C07591.this
                                bulltrack.com.optionanalyzer.adapter.PortfolioListAdapter r1 = bulltrack.com.optionanalyzer.adapter.PortfolioListAdapter.this
                                android.app.DatePickerDialog r15 = new android.app.DatePickerDialog
                                bulltrack.com.optionanalyzer.adapter.PortfolioListAdapter$1 r10 = bulltrack.com.optionanalyzer.adapter.PortfolioListAdapter.C07591.this
                                bulltrack.com.optionanalyzer.adapter.PortfolioListAdapter r10 = bulltrack.com.optionanalyzer.adapter.PortfolioListAdapter.this
                                android.content.Context r11 = r10.context
                                bulltrack.com.optionanalyzer.adapter.PortfolioListAdapter$1$1$1 r12 = new bulltrack.com.optionanalyzer.adapter.PortfolioListAdapter$1$1$1
                                r12.<init>(r9)
                                int r13 = r0.get(r8)
                                int r14 = r0.get(r3)
                                r3 = 5
                                int r0 = r0.get(r3)
                                r10 = r15
                                r3 = r15
                                r15 = r0
                                r10.<init>(r11, r12, r13, r14, r15)
                                r1.entryDatePickerDialog = r3
                                bulltrack.com.optionanalyzer.adapter.PortfolioListAdapter$1$1$2 r0 = new bulltrack.com.optionanalyzer.adapter.PortfolioListAdapter$1$1$2
                                r0.<init>(r4, r2)
                                r4.setOnClickListener(r0)
                                bulltrack.com.optionanalyzer.adapter.PortfolioListAdapter$1$1$3 r0 = new bulltrack.com.optionanalyzer.adapter.PortfolioListAdapter$1$1$3
                                r0.<init>(r9, r2)
                                r9.setOnClickListener(r0)
                                r5.show()
                                r9.setFocusable(r8)
                                r0 = 2131230839(0x7f080077, float:1.8077742E38)
                                android.view.View r0 = r5.findViewById(r0)
                                r10 = r0
                                android.widget.Button r10 = (android.widget.Button) r10
                                bulltrack.com.optionanalyzer.adapter.PortfolioListAdapter$1$1$4 r11 = new bulltrack.com.optionanalyzer.adapter.PortfolioListAdapter$1$1$4
                                r0 = r11
                                r1 = r16
                                r3 = r4
                                r4 = r9
                                r0.<init>(r2, r3, r4, r5, r6)
                                r10.setOnClickListener(r11)
                                goto L_0x0140
                            L_0x00dc:
                                bulltrack.com.optionanalyzer.adapter.PortfolioListAdapter$1 r0 = bulltrack.com.optionanalyzer.adapter.PortfolioListAdapter.C07591.this
                                bulltrack.com.optionanalyzer.adapter.PortfolioListAdapter r0 = bulltrack.com.optionanalyzer.adapter.PortfolioListAdapter.this
                                bulltrack.com.optionanalyzer.application.MyGreeksApplication r0 = r0.getGreekApplication()
                                bulltrack.com.optionanalyzer.adapter.PortfolioListAdapter$1 r1 = bulltrack.com.optionanalyzer.adapter.PortfolioListAdapter.C07591.this
                                bulltrack.com.optionanalyzer.adapter.PortfolioListAdapter r1 = bulltrack.com.optionanalyzer.adapter.PortfolioListAdapter.this
                                bulltrack.com.optionanalyzer.application.MyGreeksApplication r1 = r1.application
                                bulltrack.com.optionanalyzer.adapter.PortfolioListAdapter$1 r2 = bulltrack.com.optionanalyzer.adapter.PortfolioListAdapter.C07591.this
                                bulltrack.com.optionanalyzer.dao.GreekValues r2 = r2
                                r0.reCalculateGreeks(r1, r2)
                                goto L_0x0140
                            L_0x00f4:
                                bulltrack.com.optionanalyzer.adapter.PortfolioListAdapter$1 r0 = bulltrack.com.optionanalyzer.adapter.PortfolioListAdapter.C07591.this
                                bulltrack.com.optionanalyzer.adapter.PortfolioListAdapter r0 = bulltrack.com.optionanalyzer.adapter.PortfolioListAdapter.this
                                bulltrack.com.optionanalyzer.application.MyGreeksApplication r0 = r0.getGreekApplication()
                                bulltrack.com.optionanalyzer.adapter.PortfolioListAdapter$1 r1 = bulltrack.com.optionanalyzer.adapter.PortfolioListAdapter.C07591.this
                                bulltrack.com.optionanalyzer.adapter.PortfolioListAdapter r1 = bulltrack.com.optionanalyzer.adapter.PortfolioListAdapter.this
                                android.content.Context r1 = r1.context
                                bulltrack.com.optionanalyzer.adapter.PortfolioListAdapter$1 r2 = bulltrack.com.optionanalyzer.adapter.PortfolioListAdapter.C07591.this
                                bulltrack.com.optionanalyzer.dao.GreekValues r2 = r2
                                r0.startViewOptionDetailsActivity(r1, r2, r3)
                                goto L_0x0140
                            L_0x010c:
                                bulltrack.com.optionanalyzer.adapter.PortfolioListAdapter$1 r0 = bulltrack.com.optionanalyzer.adapter.PortfolioListAdapter.C07591.this
                                bulltrack.com.optionanalyzer.adapter.PortfolioListAdapter r0 = bulltrack.com.optionanalyzer.adapter.PortfolioListAdapter.this
                                bulltrack.com.optionanalyzer.application.MyGreeksApplication r0 = r0.getGreekApplication()
                                bulltrack.com.optionanalyzer.adapter.PortfolioListAdapter$1 r1 = bulltrack.com.optionanalyzer.adapter.PortfolioListAdapter.C07591.this
                                bulltrack.com.optionanalyzer.dao.GreekValues r1 = r2
                                r0.deleteFolioItemFromOpenPos(r1)
                                bulltrack.com.optionanalyzer.adapter.PortfolioListAdapter$1 r0 = bulltrack.com.optionanalyzer.adapter.PortfolioListAdapter.C07591.this
                                bulltrack.com.optionanalyzer.adapter.PortfolioListAdapter r0 = bulltrack.com.optionanalyzer.adapter.PortfolioListAdapter.this
                                java.util.List<bulltrack.com.optionanalyzer.dao.GreekValues> r0 = r0.f106li
                                bulltrack.com.optionanalyzer.adapter.PortfolioListAdapter$1 r1 = bulltrack.com.optionanalyzer.adapter.PortfolioListAdapter.C07591.this
                                int r1 = r1
                                r0.remove(r1)
                                bulltrack.com.optionanalyzer.adapter.PortfolioListAdapter$1 r0 = bulltrack.com.optionanalyzer.adapter.PortfolioListAdapter.C07591.this
                                bulltrack.com.optionanalyzer.adapter.PortfolioListAdapter r0 = bulltrack.com.optionanalyzer.adapter.PortfolioListAdapter.this
                                r0.notifyDataSetChanged()
                                bulltrack.com.optionanalyzer.adapter.PortfolioListAdapter$1 r0 = bulltrack.com.optionanalyzer.adapter.PortfolioListAdapter.C07591.this
                                bulltrack.com.optionanalyzer.adapter.PortfolioListAdapter r0 = bulltrack.com.optionanalyzer.adapter.PortfolioListAdapter.this
                                bulltrack.com.optionanalyzer.application.MyGreeksApplication r0 = r0.application
                                java.lang.String r1 = "Portfolio Item Deleted"
                                android.widget.Toast r0 = android.widget.Toast.makeText(r0, r1, r8)
                                r0.show()
                            L_0x0140:
                                return r8
                            */
                            throw new UnsupportedOperationException("Method not decompiled: bulltrack.com.optionanalyzer.adapter.PortfolioListAdapter.C07591.C07601.onMenuItemClick(android.view.MenuItem):boolean");
                        }
                    });
                    popupMenu.show();
                }
            });
            f = f2;
        } else {
            textView8.setVisibility(0);
            textView8.setText("[ " + simpleDateFormat.format(greekValues.getExitDate()) + " ]");
            textView6.setText("Exit@");
            textView7.setText(decimalFormat.format((double) greekValues.getExitPrice()) + "");
            if (greekValues.getLongShort().equalsIgnoreCase("Long")) {
                f = greekValues.getExitPrice() - greekValues.getEntryPrice();
                textView3.setTextColor(Color.parseColor("#2E8B57"));
            } else {
                f = greekValues.getEntryPrice() - greekValues.getExitPrice();
                textView3.setTextColor(SupportMenu.CATEGORY_MASK);
            }
            imageView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(PortfolioListAdapter.this.context, imageView);
                    popupMenu.getMenuInflater().inflate(R.menu.folio_close_menu, popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            String charSequence = menuItem.getTitle().toString();
                            if (((charSequence.hashCode() == 2043376075 && charSequence.equals(Constants.FOLIOCLOSE_POPUP_DELETE)) ? (char) 0 : 65535) == 0) {
                                PortfolioListAdapter.this.getGreekApplication().deleteFolioItemFromClosedPos(greekValues);
                                PortfolioListAdapter.this.f106li.remove(i2);
                                PortfolioListAdapter.this.notifyDataSetChanged();
                                Toast.makeText(PortfolioListAdapter.this.context, "Portfolio Item Deleted", 1).show();
                            }
                            return true;
                        }
                    });
                    popupMenu.show();
                }
            });
        }
        textView9.setText(greekValues.getQuantity() + "");
        float entryPrice = (f / greekValues.getEntryPrice()) * 100.0f;
        float quantity = f * ((float) greekValues.getQuantity());
        if (Math.abs(quantity) >= 100000.0f) {
            str = decimalFormat.format((double) (quantity / 100000.0f)) + " lac";
        } else {
            str = decimalFormat.format((double) quantity);
        }
        textView10.setText(str + " [" + decimalFormat.format((double) entryPrice) + "%]");
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
