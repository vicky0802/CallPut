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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import bulltrack.com.optionanalyzer.application.MyGreeksApplication;
import bulltrack.com.optiongreeks13.R;

public class CalculatorActivity extends AppCompatActivity {
    /* access modifiers changed from: private */
    public Activity activity;
    double days2Expiry = 0.0d;
    double delta;
    double gamma;
    double interest = 0.0d;
    double stockPrice = 0.0d;
    double strike = 0.0d;
    double theoVal;
    double theta;
    double vega;
    double volatility = 0.0d;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        EditText editText;
        TextView textView;
        TextView textView2;
        TextView textView3;
        TextView textView4;
        EditText editText2;
        super.onCreate(bundle);
        this.activity = this;
        setContentView((int) R.layout.calculator_entry);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        EditText editText3 = (EditText) findViewById(R.id.edt_calc_stockprice);
        EditText editText4 = (EditText) findViewById(R.id.edt_calc_strike);
        final EditText editText5 = (EditText) findViewById(R.id.edt_calc_daystoexpiry);
        final EditText editText6 = (EditText) findViewById(R.id.edt_calc_interest);
        final EditText editText7 = (EditText) findViewById(R.id.edt_calc_volatility);
        final RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radiogrp_calc_callorput);
        RadioButton radioButton = (RadioButton) findViewById(R.id.radiobtn_calc_call);
        RadioButton radioButton2 = (RadioButton) findViewById(R.id.radiobtn_calc_put);
        TextView textView5 = (TextView) findViewById(R.id.txt_calc_entry_theoval_val);
        TextView textView6 = (TextView) findViewById(R.id.txt_calc_entry_delta_val);
        TextView textView7 = (TextView) findViewById(R.id.txt_calc_entry_gamma_val);
        TextView textView8 = (TextView) findViewById(R.id.txt_calc_entry_theta_val);
        TextView textView9 = (TextView) findViewById(R.id.txt_calc_entry_vega_val);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            float f = extras.getFloat("stockPrice");
            textView4 = textView9;
            float f2 = extras.getFloat("strikePrice");
            textView3 = textView8;
            long j = extras.getLong("days2Expiry");
            float f3 = extras.getFloat("interestRate");
            textView2 = textView7;
            float f4 = extras.getFloat("anuualVolatility");
            textView = textView6;
            String string = extras.getString("callPut");
            editText3.setText(f + "");
            editText4.setText(f2 + "");
            StringBuilder sb = new StringBuilder();
            editText2 = editText3;
            editText = editText4;
            sb.append(j + 1);
            sb.append("");
            editText5.setText(sb.toString());
            editText6.setText(f3 + "");
            editText7.setText(f4 + "");
            if (string.trim().equalsIgnoreCase("C")) {
                radioButton.setChecked(true);
            } else {
                radioButton2.setChecked(true);
            }
        } else {
            editText2 = editText3;
            editText = editText4;
            textView = textView6;
            textView2 = textView7;
            textView3 = textView8;
            textView4 = textView9;
        }
        final EditText editText8 = editText2;
        final EditText editText9 = editText;
        final TextView textView10 = textView5;
        final TextView textView11 = textView;
        final TextView textView12 = textView2;
        final TextView textView13 = textView3;
        C05571 r15 = r0;
        final TextView textView14 = textView4;
        C05571 r0 = new View.OnClickListener() {
            /* JADX WARNING: Removed duplicated region for block: B:46:0x00e9  */
            /* JADX WARNING: Removed duplicated region for block: B:48:0x00f9  */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void onClick(View r14) {
                /*
                    r13 = this;
                    bulltrack.com.optionanalyzer.activity.CalculatorActivity r14 = bulltrack.com.optionanalyzer.activity.CalculatorActivity.this
                    android.app.Activity r14 = r14.activity
                    android.view.View r14 = r14.getCurrentFocus()
                    r0 = 0
                    if (r14 == 0) goto L_0x001e
                    bulltrack.com.optionanalyzer.activity.CalculatorActivity r1 = bulltrack.com.optionanalyzer.activity.CalculatorActivity.this
                    java.lang.String r2 = "input_method"
                    java.lang.Object r1 = r1.getSystemService(r2)
                    android.view.inputmethod.InputMethodManager r1 = (android.view.inputmethod.InputMethodManager) r1
                    android.os.IBinder r14 = r14.getWindowToken()
                    r1.hideSoftInputFromWindow(r14, r0)
                L_0x001e:
                    android.widget.RadioGroup r14 = r2
                    int r14 = r14.getCheckedRadioButtonId()
                    r1 = 2131231219(0x7f0801f3, float:1.8078513E38)
                    r2 = 1
                    if (r14 != r1) goto L_0x002c
                    r14 = 1
                    goto L_0x002d
                L_0x002c:
                    r14 = 0
                L_0x002d:
                    bulltrack.com.optionanalyzer.activity.CalculatorActivity r1 = bulltrack.com.optionanalyzer.activity.CalculatorActivity.this     // Catch:{ Exception -> 0x00e3 }
                    android.widget.EditText r3 = r3     // Catch:{ Exception -> 0x00e3 }
                    android.text.Editable r3 = r3.getText()     // Catch:{ Exception -> 0x00e3 }
                    java.lang.String r3 = r3.toString()     // Catch:{ Exception -> 0x00e3 }
                    double r3 = java.lang.Double.parseDouble(r3)     // Catch:{ Exception -> 0x00e3 }
                    r1.stockPrice = r3     // Catch:{ Exception -> 0x00e3 }
                    bulltrack.com.optionanalyzer.activity.CalculatorActivity r1 = bulltrack.com.optionanalyzer.activity.CalculatorActivity.this     // Catch:{ Exception -> 0x00e3 }
                    double r3 = r1.stockPrice     // Catch:{ Exception -> 0x00e3 }
                    r5 = 0
                    int r1 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1))
                    if (r1 < 0) goto L_0x004b
                    r1 = 1
                    goto L_0x004c
                L_0x004b:
                    r1 = 0
                L_0x004c:
                    bulltrack.com.optionanalyzer.activity.CalculatorActivity r3 = bulltrack.com.optionanalyzer.activity.CalculatorActivity.this     // Catch:{ Exception -> 0x00e4 }
                    android.widget.EditText r4 = r4     // Catch:{ Exception -> 0x00e4 }
                    android.text.Editable r4 = r4.getText()     // Catch:{ Exception -> 0x00e4 }
                    java.lang.String r4 = r4.toString()     // Catch:{ Exception -> 0x00e4 }
                    double r7 = java.lang.Double.parseDouble(r4)     // Catch:{ Exception -> 0x00e4 }
                    r3.strike = r7     // Catch:{ Exception -> 0x00e4 }
                    bulltrack.com.optionanalyzer.activity.CalculatorActivity r3 = bulltrack.com.optionanalyzer.activity.CalculatorActivity.this     // Catch:{ Exception -> 0x00e4 }
                    double r3 = r3.strike     // Catch:{ Exception -> 0x00e4 }
                    int r7 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1))
                    if (r7 < 0) goto L_0x0068
                    r3 = 1
                    goto L_0x0069
                L_0x0068:
                    r3 = 0
                L_0x0069:
                    bulltrack.com.optionanalyzer.activity.CalculatorActivity r4 = bulltrack.com.optionanalyzer.activity.CalculatorActivity.this     // Catch:{ Exception -> 0x00e5 }
                    android.widget.EditText r7 = r5     // Catch:{ Exception -> 0x00e5 }
                    android.text.Editable r7 = r7.getText()     // Catch:{ Exception -> 0x00e5 }
                    java.lang.String r7 = r7.toString()     // Catch:{ Exception -> 0x00e5 }
                    double r7 = java.lang.Double.parseDouble(r7)     // Catch:{ Exception -> 0x00e5 }
                    r4.days2Expiry = r7     // Catch:{ Exception -> 0x00e5 }
                    bulltrack.com.optionanalyzer.activity.CalculatorActivity r4 = bulltrack.com.optionanalyzer.activity.CalculatorActivity.this     // Catch:{ Exception -> 0x00e5 }
                    double r7 = r4.days2Expiry     // Catch:{ Exception -> 0x00e5 }
                    int r4 = (r7 > r5 ? 1 : (r7 == r5 ? 0 : -1))
                    if (r4 < 0) goto L_0x0093
                    bulltrack.com.optionanalyzer.activity.CalculatorActivity r4 = bulltrack.com.optionanalyzer.activity.CalculatorActivity.this     // Catch:{ Exception -> 0x00e5 }
                    bulltrack.com.optionanalyzer.activity.CalculatorActivity r7 = bulltrack.com.optionanalyzer.activity.CalculatorActivity.this     // Catch:{ Exception -> 0x00e5 }
                    double r7 = r7.days2Expiry     // Catch:{ Exception -> 0x00e5 }
                    r9 = 4645128764097822720(0x4076d00000000000, double:365.0)
                    double r7 = r7 / r9
                    r4.days2Expiry = r7     // Catch:{ Exception -> 0x00e5 }
                    r4 = 1
                    goto L_0x0094
                L_0x0093:
                    r4 = 0
                L_0x0094:
                    bulltrack.com.optionanalyzer.activity.CalculatorActivity r7 = bulltrack.com.optionanalyzer.activity.CalculatorActivity.this     // Catch:{ Exception -> 0x00e6 }
                    android.widget.EditText r8 = r6     // Catch:{ Exception -> 0x00e6 }
                    android.text.Editable r8 = r8.getText()     // Catch:{ Exception -> 0x00e6 }
                    java.lang.String r8 = r8.toString()     // Catch:{ Exception -> 0x00e6 }
                    double r8 = java.lang.Double.parseDouble(r8)     // Catch:{ Exception -> 0x00e6 }
                    r7.interest = r8     // Catch:{ Exception -> 0x00e6 }
                    bulltrack.com.optionanalyzer.activity.CalculatorActivity r7 = bulltrack.com.optionanalyzer.activity.CalculatorActivity.this     // Catch:{ Exception -> 0x00e6 }
                    double r7 = r7.interest     // Catch:{ Exception -> 0x00e6 }
                    r9 = 4636737291354636288(0x4059000000000000, double:100.0)
                    int r11 = (r7 > r5 ? 1 : (r7 == r5 ? 0 : -1))
                    if (r11 < 0) goto L_0x00bb
                    bulltrack.com.optionanalyzer.activity.CalculatorActivity r7 = bulltrack.com.optionanalyzer.activity.CalculatorActivity.this     // Catch:{ Exception -> 0x00e6 }
                    bulltrack.com.optionanalyzer.activity.CalculatorActivity r8 = bulltrack.com.optionanalyzer.activity.CalculatorActivity.this     // Catch:{ Exception -> 0x00e6 }
                    double r11 = r8.interest     // Catch:{ Exception -> 0x00e6 }
                    double r11 = r11 / r9
                    r7.interest = r11     // Catch:{ Exception -> 0x00e6 }
                    r7 = 1
                    goto L_0x00bc
                L_0x00bb:
                    r7 = 0
                L_0x00bc:
                    bulltrack.com.optionanalyzer.activity.CalculatorActivity r8 = bulltrack.com.optionanalyzer.activity.CalculatorActivity.this     // Catch:{ Exception -> 0x00e1 }
                    android.widget.EditText r11 = r7     // Catch:{ Exception -> 0x00e1 }
                    android.text.Editable r11 = r11.getText()     // Catch:{ Exception -> 0x00e1 }
                    java.lang.String r11 = r11.toString()     // Catch:{ Exception -> 0x00e1 }
                    double r11 = java.lang.Double.parseDouble(r11)     // Catch:{ Exception -> 0x00e1 }
                    r8.volatility = r11     // Catch:{ Exception -> 0x00e1 }
                    bulltrack.com.optionanalyzer.activity.CalculatorActivity r8 = bulltrack.com.optionanalyzer.activity.CalculatorActivity.this     // Catch:{ Exception -> 0x00e1 }
                    double r11 = r8.volatility     // Catch:{ Exception -> 0x00e1 }
                    int r8 = (r11 > r5 ? 1 : (r11 == r5 ? 0 : -1))
                    if (r8 < 0) goto L_0x00e7
                    bulltrack.com.optionanalyzer.activity.CalculatorActivity r5 = bulltrack.com.optionanalyzer.activity.CalculatorActivity.this     // Catch:{ Exception -> 0x00e1 }
                    bulltrack.com.optionanalyzer.activity.CalculatorActivity r6 = bulltrack.com.optionanalyzer.activity.CalculatorActivity.this     // Catch:{ Exception -> 0x00e1 }
                    double r11 = r6.volatility     // Catch:{ Exception -> 0x00e1 }
                    double r11 = r11 / r9
                    r5.volatility = r11     // Catch:{ Exception -> 0x00e1 }
                    r0 = 1
                    goto L_0x00e7
                L_0x00e1:
                    goto L_0x00e7
                L_0x00e3:
                    r1 = 0
                L_0x00e4:
                    r3 = 0
                L_0x00e5:
                    r4 = 0
                L_0x00e6:
                    r7 = 0
                L_0x00e7:
                    if (r1 != 0) goto L_0x00f9
                    bulltrack.com.optionanalyzer.activity.CalculatorActivity r14 = bulltrack.com.optionanalyzer.activity.CalculatorActivity.this
                    android.content.Context r14 = r14.getApplicationContext()
                    java.lang.String r0 = "Invalid Stock Price"
                    android.widget.Toast r14 = android.widget.Toast.makeText(r14, r0, r2)
                    r14.show()
                    return
                L_0x00f9:
                    if (r3 != 0) goto L_0x010b
                    bulltrack.com.optionanalyzer.activity.CalculatorActivity r14 = bulltrack.com.optionanalyzer.activity.CalculatorActivity.this
                    android.content.Context r14 = r14.getApplicationContext()
                    java.lang.String r0 = "Invalid Strike Price"
                    android.widget.Toast r14 = android.widget.Toast.makeText(r14, r0, r2)
                    r14.show()
                    return
                L_0x010b:
                    if (r4 != 0) goto L_0x011d
                    bulltrack.com.optionanalyzer.activity.CalculatorActivity r14 = bulltrack.com.optionanalyzer.activity.CalculatorActivity.this
                    android.content.Context r14 = r14.getApplicationContext()
                    java.lang.String r0 = "Invalid Days To Expiry"
                    android.widget.Toast r14 = android.widget.Toast.makeText(r14, r0, r2)
                    r14.show()
                    return
                L_0x011d:
                    if (r7 != 0) goto L_0x012f
                    bulltrack.com.optionanalyzer.activity.CalculatorActivity r14 = bulltrack.com.optionanalyzer.activity.CalculatorActivity.this
                    android.content.Context r14 = r14.getApplicationContext()
                    java.lang.String r0 = "Invalid Interest"
                    android.widget.Toast r14 = android.widget.Toast.makeText(r14, r0, r2)
                    r14.show()
                    return
                L_0x012f:
                    if (r0 != 0) goto L_0x0141
                    bulltrack.com.optionanalyzer.activity.CalculatorActivity r14 = bulltrack.com.optionanalyzer.activity.CalculatorActivity.this
                    android.content.Context r14 = r14.getApplicationContext()
                    java.lang.String r0 = "Invalid Volatility"
                    android.widget.Toast r14 = android.widget.Toast.makeText(r14, r0, r2)
                    r14.show()
                    return
                L_0x0141:
                    if (r14 == 0) goto L_0x01a6
                    bulltrack.com.optionanalyzer.activity.CalculatorActivity r14 = bulltrack.com.optionanalyzer.activity.CalculatorActivity.this
                    bulltrack.com.optionanalyzer.application.MyGreeksApplication r0 = r14.getGreekApplication()
                    bulltrack.com.optionanalyzer.activity.CalculatorActivity r1 = bulltrack.com.optionanalyzer.activity.CalculatorActivity.this
                    double r2 = r1.stockPrice
                    bulltrack.com.optionanalyzer.activity.CalculatorActivity r1 = bulltrack.com.optionanalyzer.activity.CalculatorActivity.this
                    double r4 = r1.strike
                    bulltrack.com.optionanalyzer.activity.CalculatorActivity r1 = bulltrack.com.optionanalyzer.activity.CalculatorActivity.this
                    double r6 = r1.days2Expiry
                    bulltrack.com.optionanalyzer.activity.CalculatorActivity r1 = bulltrack.com.optionanalyzer.activity.CalculatorActivity.this
                    double r8 = r1.interest
                    bulltrack.com.optionanalyzer.activity.CalculatorActivity r1 = bulltrack.com.optionanalyzer.activity.CalculatorActivity.this
                    double r10 = r1.volatility
                    java.lang.String r1 = "C"
                    double r0 = r0.theoValue(r1, r2, r4, r6, r8, r10)
                    r14.theoVal = r0
                    bulltrack.com.optionanalyzer.activity.CalculatorActivity r14 = bulltrack.com.optionanalyzer.activity.CalculatorActivity.this
                    bulltrack.com.optionanalyzer.application.MyGreeksApplication r0 = r14.getGreekApplication()
                    bulltrack.com.optionanalyzer.activity.CalculatorActivity r1 = bulltrack.com.optionanalyzer.activity.CalculatorActivity.this
                    double r1 = r1.stockPrice
                    bulltrack.com.optionanalyzer.activity.CalculatorActivity r3 = bulltrack.com.optionanalyzer.activity.CalculatorActivity.this
                    double r3 = r3.strike
                    bulltrack.com.optionanalyzer.activity.CalculatorActivity r5 = bulltrack.com.optionanalyzer.activity.CalculatorActivity.this
                    double r5 = r5.days2Expiry
                    bulltrack.com.optionanalyzer.activity.CalculatorActivity r7 = bulltrack.com.optionanalyzer.activity.CalculatorActivity.this
                    double r7 = r7.interest
                    bulltrack.com.optionanalyzer.activity.CalculatorActivity r9 = bulltrack.com.optionanalyzer.activity.CalculatorActivity.this
                    double r9 = r9.volatility
                    double r0 = r0.callDelta(r1, r3, r5, r7, r9)
                    r14.delta = r0
                    bulltrack.com.optionanalyzer.activity.CalculatorActivity r14 = bulltrack.com.optionanalyzer.activity.CalculatorActivity.this
                    bulltrack.com.optionanalyzer.application.MyGreeksApplication r0 = r14.getGreekApplication()
                    bulltrack.com.optionanalyzer.activity.CalculatorActivity r1 = bulltrack.com.optionanalyzer.activity.CalculatorActivity.this
                    double r1 = r1.stockPrice
                    bulltrack.com.optionanalyzer.activity.CalculatorActivity r3 = bulltrack.com.optionanalyzer.activity.CalculatorActivity.this
                    double r3 = r3.strike
                    bulltrack.com.optionanalyzer.activity.CalculatorActivity r5 = bulltrack.com.optionanalyzer.activity.CalculatorActivity.this
                    double r5 = r5.days2Expiry
                    bulltrack.com.optionanalyzer.activity.CalculatorActivity r7 = bulltrack.com.optionanalyzer.activity.CalculatorActivity.this
                    double r7 = r7.interest
                    bulltrack.com.optionanalyzer.activity.CalculatorActivity r9 = bulltrack.com.optionanalyzer.activity.CalculatorActivity.this
                    double r9 = r9.volatility
                    double r0 = r0.callTheta(r1, r3, r5, r7, r9)
                    r14.theta = r0
                    goto L_0x0208
                L_0x01a6:
                    bulltrack.com.optionanalyzer.activity.CalculatorActivity r14 = bulltrack.com.optionanalyzer.activity.CalculatorActivity.this
                    bulltrack.com.optionanalyzer.application.MyGreeksApplication r0 = r14.getGreekApplication()
                    bulltrack.com.optionanalyzer.activity.CalculatorActivity r1 = bulltrack.com.optionanalyzer.activity.CalculatorActivity.this
                    double r2 = r1.stockPrice
                    bulltrack.com.optionanalyzer.activity.CalculatorActivity r1 = bulltrack.com.optionanalyzer.activity.CalculatorActivity.this
                    double r4 = r1.strike
                    bulltrack.com.optionanalyzer.activity.CalculatorActivity r1 = bulltrack.com.optionanalyzer.activity.CalculatorActivity.this
                    double r6 = r1.days2Expiry
                    bulltrack.com.optionanalyzer.activity.CalculatorActivity r1 = bulltrack.com.optionanalyzer.activity.CalculatorActivity.this
                    double r8 = r1.interest
                    bulltrack.com.optionanalyzer.activity.CalculatorActivity r1 = bulltrack.com.optionanalyzer.activity.CalculatorActivity.this
                    double r10 = r1.volatility
                    java.lang.String r1 = "P"
                    double r0 = r0.theoValue(r1, r2, r4, r6, r8, r10)
                    r14.theoVal = r0
                    bulltrack.com.optionanalyzer.activity.CalculatorActivity r14 = bulltrack.com.optionanalyzer.activity.CalculatorActivity.this
                    bulltrack.com.optionanalyzer.application.MyGreeksApplication r0 = r14.getGreekApplication()
                    bulltrack.com.optionanalyzer.activity.CalculatorActivity r1 = bulltrack.com.optionanalyzer.activity.CalculatorActivity.this
                    double r1 = r1.stockPrice
                    bulltrack.com.optionanalyzer.activity.CalculatorActivity r3 = bulltrack.com.optionanalyzer.activity.CalculatorActivity.this
                    double r3 = r3.strike
                    bulltrack.com.optionanalyzer.activity.CalculatorActivity r5 = bulltrack.com.optionanalyzer.activity.CalculatorActivity.this
                    double r5 = r5.days2Expiry
                    bulltrack.com.optionanalyzer.activity.CalculatorActivity r7 = bulltrack.com.optionanalyzer.activity.CalculatorActivity.this
                    double r7 = r7.interest
                    bulltrack.com.optionanalyzer.activity.CalculatorActivity r9 = bulltrack.com.optionanalyzer.activity.CalculatorActivity.this
                    double r9 = r9.volatility
                    double r0 = r0.putDelta(r1, r3, r5, r7, r9)
                    r14.delta = r0
                    bulltrack.com.optionanalyzer.activity.CalculatorActivity r14 = bulltrack.com.optionanalyzer.activity.CalculatorActivity.this
                    bulltrack.com.optionanalyzer.application.MyGreeksApplication r0 = r14.getGreekApplication()
                    bulltrack.com.optionanalyzer.activity.CalculatorActivity r1 = bulltrack.com.optionanalyzer.activity.CalculatorActivity.this
                    double r1 = r1.stockPrice
                    bulltrack.com.optionanalyzer.activity.CalculatorActivity r3 = bulltrack.com.optionanalyzer.activity.CalculatorActivity.this
                    double r3 = r3.strike
                    bulltrack.com.optionanalyzer.activity.CalculatorActivity r5 = bulltrack.com.optionanalyzer.activity.CalculatorActivity.this
                    double r5 = r5.days2Expiry
                    bulltrack.com.optionanalyzer.activity.CalculatorActivity r7 = bulltrack.com.optionanalyzer.activity.CalculatorActivity.this
                    double r7 = r7.interest
                    bulltrack.com.optionanalyzer.activity.CalculatorActivity r9 = bulltrack.com.optionanalyzer.activity.CalculatorActivity.this
                    double r9 = r9.volatility
                    double r0 = r0.putTheta(r1, r3, r5, r7, r9)
                    r14.theta = r0
                L_0x0208:
                    bulltrack.com.optionanalyzer.activity.CalculatorActivity r14 = bulltrack.com.optionanalyzer.activity.CalculatorActivity.this
                    bulltrack.com.optionanalyzer.application.MyGreeksApplication r0 = r14.getGreekApplication()
                    bulltrack.com.optionanalyzer.activity.CalculatorActivity r1 = bulltrack.com.optionanalyzer.activity.CalculatorActivity.this
                    double r1 = r1.stockPrice
                    bulltrack.com.optionanalyzer.activity.CalculatorActivity r3 = bulltrack.com.optionanalyzer.activity.CalculatorActivity.this
                    double r3 = r3.strike
                    bulltrack.com.optionanalyzer.activity.CalculatorActivity r5 = bulltrack.com.optionanalyzer.activity.CalculatorActivity.this
                    double r5 = r5.days2Expiry
                    bulltrack.com.optionanalyzer.activity.CalculatorActivity r7 = bulltrack.com.optionanalyzer.activity.CalculatorActivity.this
                    double r7 = r7.interest
                    bulltrack.com.optionanalyzer.activity.CalculatorActivity r9 = bulltrack.com.optionanalyzer.activity.CalculatorActivity.this
                    double r9 = r9.volatility
                    double r0 = r0.optionGamma(r1, r3, r5, r7, r9)
                    r14.gamma = r0
                    bulltrack.com.optionanalyzer.activity.CalculatorActivity r14 = bulltrack.com.optionanalyzer.activity.CalculatorActivity.this
                    bulltrack.com.optionanalyzer.application.MyGreeksApplication r0 = r14.getGreekApplication()
                    bulltrack.com.optionanalyzer.activity.CalculatorActivity r1 = bulltrack.com.optionanalyzer.activity.CalculatorActivity.this
                    double r1 = r1.stockPrice
                    bulltrack.com.optionanalyzer.activity.CalculatorActivity r3 = bulltrack.com.optionanalyzer.activity.CalculatorActivity.this
                    double r3 = r3.strike
                    bulltrack.com.optionanalyzer.activity.CalculatorActivity r5 = bulltrack.com.optionanalyzer.activity.CalculatorActivity.this
                    double r5 = r5.days2Expiry
                    bulltrack.com.optionanalyzer.activity.CalculatorActivity r7 = bulltrack.com.optionanalyzer.activity.CalculatorActivity.this
                    double r7 = r7.interest
                    bulltrack.com.optionanalyzer.activity.CalculatorActivity r9 = bulltrack.com.optionanalyzer.activity.CalculatorActivity.this
                    double r9 = r9.volatility
                    double r0 = r0.optionVega(r1, r3, r5, r7, r9)
                    r14.vega = r0
                    java.text.DecimalFormat r14 = new java.text.DecimalFormat
                    java.lang.String r0 = "0.00"
                    r14.<init>(r0)
                    java.text.DecimalFormat r0 = new java.text.DecimalFormat
                    java.lang.String r1 = "0.0000"
                    r0.<init>(r1)
                    android.widget.TextView r1 = r8
                    bulltrack.com.optionanalyzer.activity.CalculatorActivity r2 = bulltrack.com.optionanalyzer.activity.CalculatorActivity.this
                    double r2 = r2.theoVal
                    java.lang.String r2 = r14.format(r2)
                    r1.setText(r2)
                    android.widget.TextView r1 = r9
                    bulltrack.com.optionanalyzer.activity.CalculatorActivity r2 = bulltrack.com.optionanalyzer.activity.CalculatorActivity.this
                    double r2 = r2.delta
                    java.lang.String r14 = r14.format(r2)
                    r1.setText(r14)
                    android.widget.TextView r14 = r10
                    bulltrack.com.optionanalyzer.activity.CalculatorActivity r1 = bulltrack.com.optionanalyzer.activity.CalculatorActivity.this
                    double r1 = r1.gamma
                    java.lang.String r1 = r0.format(r1)
                    r14.setText(r1)
                    android.widget.TextView r14 = r11
                    bulltrack.com.optionanalyzer.activity.CalculatorActivity r1 = bulltrack.com.optionanalyzer.activity.CalculatorActivity.this
                    double r1 = r1.theta
                    java.lang.String r1 = r0.format(r1)
                    r14.setText(r1)
                    android.widget.TextView r14 = r12
                    bulltrack.com.optionanalyzer.activity.CalculatorActivity r1 = bulltrack.com.optionanalyzer.activity.CalculatorActivity.this
                    double r1 = r1.vega
                    java.lang.String r0 = r0.format(r1)
                    r14.setText(r0)
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: bulltrack.com.optionanalyzer.activity.CalculatorActivity.C05571.onClick(android.view.View):void");
            }
        };
        ((Button) findViewById(R.id.btn_calculator_ok)).setOnClickListener(r15);
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

    /* access modifiers changed from: protected */
    public MyGreeksApplication getGreekApplication() {
        return (MyGreeksApplication) getApplicationContext();
    }
}
