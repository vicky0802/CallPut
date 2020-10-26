package bulltrack.com.optionanalyzer.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import bulltrack.com.optionanalyzer.application.MyGreeksApplication;
import bulltrack.com.optionanalyzer.dao.Calls;
import bulltrack.com.optiongreeks13.R;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BCAdapter extends BaseAdapter {
    private MyGreeksApplication application;
    private int backgroundColor;
    private Context context;

    /* renamed from: li */
    List<Calls> f100li;
    List<Calls> liSearchResults;
    ListView list;

    public long getItemId(int i) {
        return (long) i;
    }

    public BCAdapter(Context context2, List<Calls> list2) {
        this.context = context2;
        this.f100li = list2;
        this.application = (MyGreeksApplication) context2.getApplicationContext();
        ArrayList arrayList = new ArrayList();
        this.liSearchResults = arrayList;
        arrayList.addAll(list2);
    }

    public int getCount() {
        List<Calls> list2 = this.f100li;
        if (list2 == null) {
            return 0;
        }
        return list2.size();
    }

    public Object getItem(int i) {
        return this.f100li.get(i);
    }

    public void setItemList(List<Calls> list2) {
        this.f100li = list2;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        Calls calls = this.f100li.get(i);
        if (view == null) {
            view = ((LayoutInflater) this.context.getSystemService("layout_inflater")).inflate(R.layout.broker_calls_item, viewGroup, false);
        }
        RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.rl_broker_calls_full_layout);
        TextView textView = (TextView) view.findViewById(R.id.tv_broker_calls_featured);
        TextView textView2 = (TextView) view.findViewById(R.id.tv_broker_calls_daycount);
        TextView textView3 = (TextView) view.findViewById(R.id.tv_broker_calls_readmore);
        ((TextView) view.findViewById(R.id.tv_broker_calls_symbol)).setText(calls.getSymbol());
        ((TextView) view.findViewById(R.id.tv_broker_calls_broker)).setText(calls.getBrokerName());
        ((TextView) view.findViewById(R.id.tv_broker_calls_call)).setText(calls.getCall());
        if (calls.getURL().toLowerCase().contains(".pdf")) {
            textView3.setText("Click to read pdf");
        } else if (calls.getURL().trim().equals("")) {
            textView3.setText("");
        } else {
            textView3.setText("Click to visit web-link");
        }
        if (calls.getSponsored().equalsIgnoreCase("N")) {
            textView.setText("");
            relativeLayout.setBackgroundColor(Color.parseColor("#ffffff"));
        } else {
            relativeLayout.setBackgroundColor(Color.parseColor("#fff8dc"));
            textView.setText("Featured");
        }
        if (calls.getSymbol().trim().toLowerCase().equals("advt")) {
            textView2.setText("");
        } else {
            MyGreeksApplication myGreeksApplication = this.application;
            long DateDifferenceInDays = myGreeksApplication.DateDifferenceInDays(myGreeksApplication.DateWithoutTime(new Date()), this.application.DateWithoutTime(calls.getGenerateDate()));
            if (DateDifferenceInDays == 0) {
                textView2.setText("Today");
            } else if (DateDifferenceInDays <= 0 || DateDifferenceInDays >= 2) {
                textView2.setText(DateDifferenceInDays + "d ago");
            } else {
                textView2.setText("Yesterday");
            }
        }
        return view;
    }

    public void filter(String str) {
        String lowerCase = str.toLowerCase(Locale.getDefault());
        this.f100li.clear();
        if (lowerCase.length() == 0) {
            this.f100li.addAll(this.liSearchResults);
        } else {
            for (Calls next : this.liSearchResults) {
                if (lowerCase.length() != 0 && next.getSymbol().toLowerCase(Locale.getDefault()).contains(lowerCase)) {
                    this.f100li.add(next);
                } else if (lowerCase.length() != 0 && next.getCall().toLowerCase(Locale.getDefault()).contains(lowerCase)) {
                    this.f100li.add(next);
                } else if (lowerCase.length() != 0 && next.getBrokerName().toLowerCase(Locale.getDefault()).contains(lowerCase)) {
                    this.f100li.add(next);
                }
            }
        }
        notifyDataSetChanged();
    }
}
