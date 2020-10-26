package bulltrack.com.optionanalyzer.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import bulltrack.com.optionanalyzer.application.MyGreeksApplication;
import bulltrack.com.optionanalyzer.constants.Constants;
import bulltrack.com.optionanalyzer.dao.OptionMargin;
import bulltrack.com.optiongreeks13.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.text.DecimalFormat;
import java.util.List;

public class MarginDetailActivity extends AppCompatActivity {
    private Activity activity;
    Button btnMinus;
    Button btnPlus;
    Button btnShowMargin;
    DecimalFormat df1 = new DecimalFormat("###,##0.00");
    DecimalFormat df2 = new DecimalFormat("0.00");
    EditText edtEntryPrice;
    EditText edtQty;
    OptionMargin optMarginRec;
    RadioButton radioButtonBuy;
    RadioButton radioButtonSell;
    RadioGroup radioBuySellGroup;
    TextView tvCallPut;
    TextView tvExpiry;
    TextView tvExposureTxt;
    TextView tvExposureVal;
    TextView tvSpanTxt;
    TextView tvSpanVal;
    TextView tvStock;
    TextView tvStrike;
    TextView tvTotalTxt;
    TextView tvTotalVal;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.margin_detail);
        this.activity = this;
        ((AdView) findViewById(R.id.adView_banner_margin_details)).loadAd(new AdRequest.Builder().setRequestAgent("android_studio:ad_template").build());
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.optMarginRec = (OptionMargin) ((List) new GsonBuilder().setDateFormat(Constants.DT_FMT_yyyy_MM_dd_HH_m_ss).create().fromJson(extras.getString("marginObect"), new TypeToken<List<OptionMargin>>() {
            }.getType())).get(0);
            this.tvStock = (TextView) findViewById(R.id.tv_margin_detail_underlyer);
            this.tvStrike = (TextView) findViewById(R.id.tv_margin_detail_strike);
            this.tvCallPut = (TextView) findViewById(R.id.tv_margin_detail_callput);
            this.tvExpiry = (TextView) findViewById(R.id.tv_margin_detail_expiry);
            this.edtQty = (EditText) findViewById(R.id.edt_margin_detail_quantity_vals);
            this.edtEntryPrice = (EditText) findViewById(R.id.edt_margin_detail_entryprice_vals);
            this.tvSpanTxt = (TextView) findViewById(R.id.tv_margin_details_span_txt);
            this.tvSpanVal = (TextView) findViewById(R.id.tv_margin_details_span_val);
            this.tvExposureTxt = (TextView) findViewById(R.id.tv_margin_details_exposure_txt);
            this.tvExposureVal = (TextView) findViewById(R.id.tv_margin_details_exposure_val);
            this.tvTotalTxt = (TextView) findViewById(R.id.tv_margin_detail_total_txt);
            this.tvTotalVal = (TextView) findViewById(R.id.tv_margin_detail_total_val);
            this.radioBuySellGroup = (RadioGroup) findViewById(R.id.radio_margin_detail_longshort);
            this.radioButtonBuy = (RadioButton) findViewById(R.id.radio_margin_detail_long);
            RadioButton radioButton = (RadioButton) findViewById(R.id.radio_margin_detail_long);
            this.radioButtonSell = radioButton;
            radioButton.setSelected(true);
            this.tvStock.setText(this.optMarginRec.getSymbol());
            this.edtEntryPrice.setText(this.df2.format((double) this.optMarginRec.getOptionPrice()));
            TextView textView = this.tvStrike;
            textView.setText(this.optMarginRec.getStrike() + "");
            if (this.optMarginRec.getPutOrCall().substring(0, 1).equalsIgnoreCase("C")) {
                this.tvCallPut.setText("Call");
            } else {
                this.tvCallPut.setText("Put");
            }
            this.tvExpiry.setText(getGreekApplication().dateFormatter(this.optMarginRec.getExpiry(), Constants.DT_FMT_dd_MMM_yyyy));
            EditText editText = this.edtQty;
            editText.setText(this.optMarginRec.getLotSize() + "");
            this.btnShowMargin = (Button) findViewById(R.id.btn_margin_detail_show);
            this.btnPlus = (Button) findViewById(R.id.btn_margin_detail_plus);
            this.btnMinus = (Button) findViewById(R.id.btn_margin_detail_minus);
            this.btnShowMargin.setText("Show Margin");
            this.radioBuySellGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    if (MarginDetailActivity.this.radioButtonSell.isChecked()) {
                        MarginDetailActivity.this.btnShowMargin.setText("Show Margin");
                    } else {
                        MarginDetailActivity.this.btnShowMargin.setText("Show Premium");
                    }
                    MarginDetailActivity.this.btnShowMargin.performClick();
                }
            });
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() != 16908332) {
            return super.onOptionsItemSelected(menuItem);
        }
        if (getParent() == null) {
            onBackPressed();
            return true;
        }
        NavUtils.navigateUpFromSameTask(this);
        return true;
    }

    public void buttonClicked(View view) {
        if (view.getId() == R.id.btn_margin_detail_show) {
            try {
                float parseFloat = Float.parseFloat(this.edtEntryPrice.getText().toString());
                if (parseFloat <= 0.0f) {
                    Toast.makeText(getApplicationContext(), "Invalid Price ", 1).show();
                    return;
                }
                try {
                    int parseInt = Integer.parseInt(this.edtQty.getText().toString());
                    if (parseInt <= 0) {
                        Toast.makeText(getApplicationContext(), "Invalid Quantity ", 1).show();
                    } else if (this.radioButtonBuy.isChecked()) {
                        this.tvSpanTxt.setVisibility(8);
                        this.tvSpanVal.setVisibility(8);
                        this.tvExposureTxt.setVisibility(8);
                        this.tvExposureVal.setVisibility(8);
                        this.tvTotalTxt.setText("Total Premium : ");
                        float f = ((float) parseInt) * parseFloat;
                        TextView textView = this.tvTotalVal;
                        textView.setText("₹ " + this.df1.format((double) f));
                        this.btnShowMargin.setText("Show Premium");
                    } else {
                        this.btnShowMargin.setText("Show Margin");
                        this.tvSpanTxt.setVisibility(0);
                        this.tvSpanVal.setVisibility(0);
                        this.tvExposureTxt.setVisibility(0);
                        this.tvExposureVal.setVisibility(0);
                        this.tvTotalTxt.setText("Total Margin : ");
                        float f2 = (float) parseInt;
                        float varMargin = (this.optMarginRec.getVarMargin() / 100.0f) * this.optMarginRec.getMPR() * f2;
                        if (this.optMarginRec.getOptType().equalsIgnoreCase("OPTIDX")) {
                            varMargin = (this.optMarginRec.getExtremeMargin() / 100.0f) * this.optMarginRec.getMPR() * f2;
                        }
                        getGreekApplication();
                        float roundTo05 = MyGreeksApplication.roundTo05(varMargin + (this.optMarginRec.getOptionPrice() * f2));
                        getGreekApplication();
                        float roundTo052 = MyGreeksApplication.roundTo05((this.optMarginRec.getExtremeMargin() / 100.0f) * this.optMarginRec.getStockPrice() * f2);
                        TextView textView2 = this.tvSpanVal;
                        textView2.setText("₹ " + this.df1.format((double) roundTo05));
                        TextView textView3 = this.tvExposureVal;
                        textView3.setText("₹ " + this.df1.format((double) roundTo052));
                        double d = (double) (roundTo05 + roundTo052);
                        TextView textView4 = this.tvTotalVal;
                        textView4.setText("₹ " + this.df1.format(d));
                    }
                } catch (NumberFormatException unused) {
                    Toast.makeText(getApplicationContext(), "Invalid Quantity", 1).show();
                }
            } catch (NumberFormatException unused2) {
                Toast.makeText(getApplicationContext(), "Invalid Price ", 1).show();
            }
        } else if (view.getId() == R.id.btn_margin_detail_plus) {
            int parseInt2 = Integer.parseInt(this.edtQty.getText().toString());
            if (parseInt2 < Integer.MAX_VALUE - this.optMarginRec.getLotSize()) {
                parseInt2 += this.optMarginRec.getLotSize();
            }
            EditText editText = this.edtQty;
            editText.setText(parseInt2 + "");
            this.btnShowMargin.performClick();
        } else if (view.getId() == R.id.btn_margin_detail_minus) {
            int parseInt3 = Integer.parseInt(this.edtQty.getText().toString());
            if (parseInt3 >= this.optMarginRec.getLotSize() * 2) {
                parseInt3 -= this.optMarginRec.getLotSize();
            }
            EditText editText2 = this.edtQty;
            editText2.setText(parseInt3 + "");
            this.btnShowMargin.performClick();
        }
    }

    /* access modifiers changed from: protected */
    public MyGreeksApplication getGreekApplication() {
        return (MyGreeksApplication) getApplicationContext();
    }
}
