package bulltrack.com.optionanalyzer.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.core.internal.view.SupportMenu;
import bulltrack.com.optionanalyzer.adapter.GreeksListAdapter;
import bulltrack.com.optionanalyzer.application.MyGreeksApplication;
import bulltrack.com.optionanalyzer.constants.Constants;
import bulltrack.com.optionanalyzer.dao.GreekValues;
import bulltrack.com.optiongreeks13.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddPortfolioActivity extends AppCompatActivity {
    private Activity activity;
    GreeksListAdapter adapter;
    Button btnAdd;
    Button btnMinus;
    Button btnPlus;
    SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DT_FMT_dd_MMM_yyyy);
    EditText edtEntryDate;
    EditText edtEntryPrice;
    EditText edtTradedQty;
    /* access modifiers changed from: private */
    public DatePickerDialog entryDatePickerDialog;

    /* renamed from: gv */
    GreekValues f78gv;
    RadioButton radioButtonBuy;
    RadioButton radioButtonSell;
    RadioGroup radioBuySellGroup;
    TextView tvCallPut;
    TextView tvExpiry;
    TextView tvLtp;
    TextView tvPriceChg;
    TextView tvStock;
    TextView tvStrike;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.add_portfolio_item);
        this.activity = this;
        ((AdView) findViewById(R.id.adView)).loadAd(new AdRequest.Builder().setRequestAgent("android_studio:ad_template").build());
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.f78gv = (GreekValues) ((List) new GsonBuilder().setDateFormat(Constants.DT_FMT_yyyy_MM_dd_HH_m_ss).create().fromJson(extras.getString("greekObect"), new TypeToken<List<GreekValues>>() {
            }.getType())).get(0);
            this.tvStock = (TextView) findViewById(R.id.tv_add_portfolio_underlyer);
            this.tvLtp = (TextView) findViewById(R.id.tv_add_portfolio_option_ltp);
            this.tvStrike = (TextView) findViewById(R.id.tv_add_portfolio_strike);
            this.tvCallPut = (TextView) findViewById(R.id.tv_add_portfolio_callput);
            this.tvExpiry = (TextView) findViewById(R.id.tv_add_portfolio_expiry);
            this.tvPriceChg = (TextView) findViewById(R.id.tv_add_portfolio_opotion_pricechange);
            this.edtTradedQty = (EditText) findViewById(R.id.edt_add_portfolio_quantity_vals);
            this.edtEntryPrice = (EditText) findViewById(R.id.edt_add_portfolio_entryprice_vals);
            EditText editText = (EditText) findViewById(R.id.edt_add_portfolio_entrydate_val);
            this.edtEntryDate = editText;
            editText.setInputType(0);
            this.edtEntryDate.requestFocus();
            this.radioBuySellGroup = (RadioGroup) findViewById(R.id.radio_add_portfolio_longshort);
            this.radioButtonBuy = (RadioButton) findViewById(R.id.radio_add_portfolio_long);
            this.radioButtonSell = (RadioButton) findViewById(R.id.radio_add_portfolio_short);
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            this.tvStock.setText(this.f78gv.getSymbol());
            this.tvLtp.setTextColor(-7829368);
            this.tvPriceChg.setTextColor(-7829368);
            if (this.f78gv.getChange() < 0.0f) {
                this.tvLtp.setText(decimalFormat.format((double) this.f78gv.getLastPrice()));
                this.tvLtp.setTextColor(SupportMenu.CATEGORY_MASK);
                TextView textView = this.tvPriceChg;
                textView.setText(decimalFormat.format((double) this.f78gv.getChange()) + " | " + decimalFormat.format((double) this.f78gv.getpChange()) + "%");
                this.tvPriceChg.setTextColor(SupportMenu.CATEGORY_MASK);
                EditText editText2 = this.edtEntryPrice;
                editText2.setText(this.f78gv.getLastPrice() + "");
            } else if (((double) this.f78gv.getUnderlyingValue()) == 0.0d && ((double) this.f78gv.getLastPrice()) == 0.0d) {
                this.tvLtp.setText(decimalFormat.format((double) this.f78gv.getClosePrice()));
                this.tvPriceChg.setText("- | -%");
                this.edtEntryPrice.setText(decimalFormat.format((double) this.f78gv.getClosePrice()));
            } else {
                this.tvLtp.setText(decimalFormat.format((double) this.f78gv.getLastPrice()));
                this.tvLtp.setTextColor(Color.parseColor("#2E8B57"));
                TextView textView2 = this.tvPriceChg;
                textView2.setText("+" + decimalFormat.format((double) this.f78gv.getChange()) + " | " + decimalFormat.format((double) this.f78gv.getpChange()) + "%");
                this.tvPriceChg.setTextColor(Color.parseColor("#2E8B57"));
                EditText editText3 = this.edtEntryPrice;
                StringBuilder sb = new StringBuilder();
                sb.append(this.f78gv.getLastPrice());
                sb.append("");
                editText3.setText(sb.toString());
            }
            TextView textView3 = this.tvStrike;
            textView3.setText(this.f78gv.getStrike() + "");
            if (this.f78gv.getCallPut().equalsIgnoreCase("C")) {
                this.tvCallPut.setText("Call");
            } else {
                this.tvCallPut.setText("Put");
            }
            this.tvExpiry.setText(this.dateFormat.format(this.f78gv.getExpiry_d()));
            this.edtEntryDate.setText(this.dateFormat.format(this.f78gv.getPrice_upd_d()));
            EditText editText4 = this.edtTradedQty;
            editText4.setText(this.f78gv.getMarketLot() + "");
        }
        this.btnAdd = (Button) findViewById(R.id.btn_add_portfolio_add);
        this.btnPlus = (Button) findViewById(R.id.btn_add_portfolio_plus);
        this.btnMinus = (Button) findViewById(R.id.btn_add_portfolio_minus);
        Calendar instance = Calendar.getInstance();
        this.entryDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                Calendar instance = Calendar.getInstance();
                instance.set(i, i2, i3);
                AddPortfolioActivity.this.edtEntryDate.setText(AddPortfolioActivity.this.dateFormat.format(instance.getTime()));
            }
        }, instance.get(1), instance.get(2), instance.get(5));
        this.edtEntryDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (view == AddPortfolioActivity.this.edtEntryDate) {
                    AddPortfolioActivity.this.entryDatePickerDialog.show();
                }
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        if (view.getId() == R.id.btn_add_portfolio_add) {
            Date dateFormatter = getGreekApplication().dateFormatter(this.edtEntryDate.getText().toString(), Constants.DT_FMT_dd_MMM_yyyy);
            Calendar instance = Calendar.getInstance();
            instance.set(11, 0);
            instance.set(12, 0);
            instance.set(13, 0);
            if (dateFormatter.getTime() > instance.getTime().getTime()) {
                Toast.makeText(getApplicationContext(), "Date cannot be in future ", 1).show();
                return;
            }
            try {
                if (Float.parseFloat(this.edtEntryPrice.getText().toString()) <= 0.0f) {
                    Toast.makeText(getApplicationContext(), "Invalid Price ", 1).show();
                    return;
                }
                try {
                    if (Integer.parseInt(this.edtTradedQty.getText().toString()) <= 0) {
                        Toast.makeText(getApplicationContext(), "Invalid Quantity ", 1).show();
                        return;
                    }
                    if (!getGreekApplication().isOptionAlreadyInPortfolio(this.tvStock.getText().toString(), this.f78gv.getExpiry_d(), this.f78gv.getStrike(), this.f78gv.getCallPut())) {
                        if (this.radioButtonBuy.isChecked()) {
                            this.f78gv.setLongShort("Long");
                        } else {
                            this.f78gv.setLongShort("Short");
                        }
                        this.f78gv.setEntryDate(getGreekApplication().dateFormatter(this.edtEntryDate.getText().toString(), Constants.DT_FMT_dd_MMM_yyyy));
                        this.f78gv.setQuantity(Integer.parseInt(this.edtTradedQty.getText().toString()));
                        this.f78gv.setEntryPrice(Float.parseFloat(this.edtEntryPrice.getText().toString()));
                        getGreekApplication().insertPortfolioItem(this.f78gv);
                        Toast.makeText(getApplicationContext(), "Added to Portfolio", 1).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Already Added to Portofio", 1).show();
                    }
                    finish();
                } catch (NumberFormatException unused) {
                    Toast.makeText(getApplicationContext(), "Invalid Quantity", 1).show();
                }
            } catch (NumberFormatException unused2) {
                Toast.makeText(getApplicationContext(), "Invalid Price ", 1).show();
            }
        } else if (view.getId() == R.id.btn_add_portfolio_plus) {
            int parseInt = Integer.parseInt(this.edtTradedQty.getText().toString());
            if (parseInt < Integer.MAX_VALUE - this.f78gv.getMarketLot()) {
                parseInt += this.f78gv.getMarketLot();
            }
            EditText editText = this.edtTradedQty;
            editText.setText(parseInt + "");
        } else if (view.getId() == R.id.btn_add_portfolio_minus) {
            int parseInt2 = Integer.parseInt(this.edtTradedQty.getText().toString());
            if (parseInt2 >= this.f78gv.getMarketLot() * 2) {
                parseInt2 -= this.f78gv.getMarketLot();
            }
            EditText editText2 = this.edtTradedQty;
            editText2.setText(parseInt2 + "");
        }
    }

    /* access modifiers changed from: protected */
    public MyGreeksApplication getGreekApplication() {
        return (MyGreeksApplication) getApplicationContext();
    }
}
