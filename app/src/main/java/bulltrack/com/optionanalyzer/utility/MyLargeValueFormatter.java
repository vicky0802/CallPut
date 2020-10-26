package bulltrack.com.optionanalyzer.utility;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;
import java.text.DecimalFormat;

public class MyLargeValueFormatter implements YAxisValueFormatter {
    private static final int MAX_LENGTH = 5;
    private static String[] SUFFIX = {"", "k", "m", "b", "t"};
    private DecimalFormat mFormat;
    private String mText;

    public int getDecimalDigits() {
        return 0;
    }

    public MyLargeValueFormatter() {
        this.mText = "";
        this.mFormat = new DecimalFormat("###E00");
    }

    public MyLargeValueFormatter(String str) {
        this();
        this.mText = str;
    }

    public String getFormattedValue(float f, YAxis yAxis) {
        return String.format("%6s", new Object[]{makePretty((double) f) + this.mText}) + " ";
    }

    public void setAppendix(String str) {
        this.mText = str;
    }

    public void setSuffix(String[] strArr) {
        SUFFIX = strArr;
    }

    private String makePretty(double d) {
        String format = this.mFormat.format(d);
        int numericValue = Character.getNumericValue(format.charAt(format.length() - 1));
        String replaceAll = format.replaceAll("E[0-9][0-9]", SUFFIX[Integer.valueOf(Character.getNumericValue(format.charAt(format.length() - 2)) + "" + numericValue).intValue() / 3]);
        while (true) {
            if (replaceAll.length() <= 5 && !replaceAll.matches("[0-9]+\\.[a-z]")) {
                return replaceAll;
            }
            replaceAll = replaceAll.substring(0, replaceAll.length() - 2) + replaceAll.substring(replaceAll.length() - 1);
        }
    }
}
