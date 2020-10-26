package bulltrack.com.optionanalyzer.utility;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;
import bulltrack.com.optionanalyzer.application.MyGreeksApplication;
import bulltrack.com.optiongreeks13.R;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class CustomMarkerView extends MarkerView {
    private String TAG = "CustomViewMarker";
    boolean isFirst = true;
    float stockPrice;
    float stockPricePrev;
    private TextView tvContent = ((TextView) findViewById(R.id.tv_custom_marker_holder));
    private ArrayList<String> xValues;

    public CustomMarkerView(Context context, int i, ArrayList<String> arrayList, float f, float f2) {
        super(context, i);
        this.xValues = arrayList;
        this.stockPrice = f;
        this.stockPricePrev = f2;
    }

    public void setFirst() {
        this.isFirst = true;
    }

    public void refreshContent(Entry entry, Highlight highlight) {
        try {
            if (this.isFirst) {
                double d = (double) (((this.stockPrice - this.stockPricePrev) / this.stockPricePrev) * 100.0f);
                if (d >= 0.0d) {
                    TextView textView = this.tvContent;
                    textView.setText(MyGreeksApplication.fromHtml("<font color=\"#000000\">Stock is here: <b></font> <font color=\"#008000\">" + (this.stockPrice + " ᐃ +" + round2Decimals(d) + "%") + "</font><b>"));
                } else {
                    TextView textView2 = this.tvContent;
                    textView2.setText(MyGreeksApplication.fromHtml("<font color=\"#000000\">Stock is here: </font> <font color=\"#FF0000\">" + (this.stockPrice + " ᐁ " + round2Decimals(d) + "%") + "</font>"));
                }
                this.isFirst = false;
                return;
            }
            this.tvContent.setText("₹ " + entry.getVal() + " @ " + this.xValues.get(entry.getXIndex()));
        } catch (Exception e) {
            Log.d(this.TAG, e.toString());
        }
    }

    public int getXOffset(float f) {
        return -(getWidth() / 2);
    }

    public int getYOffset(float f) {
        return -getHeight();
    }

    public String round2Decimals(double d) {
        return new DecimalFormat("#########0.00").format(d);
    }
}
