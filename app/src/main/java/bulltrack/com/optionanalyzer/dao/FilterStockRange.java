package bulltrack.com.optionanalyzer.dao;

import java.util.Date;

public class FilterStockRange {
    Date expiry;
    double highRange;
    double lowRange;
    String symbol;

    public FilterStockRange(String str, Date date, double d, double d2) {
        this.symbol = str;
        this.expiry = date;
        this.lowRange = d;
        this.highRange = d2;
    }

    public String getSymbol() {
        return this.symbol;
    }

    public void setSymbol(String str) {
        this.symbol = str;
    }

    public Date getExpiry() {
        return this.expiry;
    }

    public void setExpiry(Date date) {
        this.expiry = date;
    }

    public double getLowRange() {
        return this.lowRange;
    }

    public void setLowRange(double d) {
        this.lowRange = d;
    }

    public double getHighRange() {
        return this.highRange;
    }

    public void setHighRange(double d) {
        this.highRange = d;
    }
}
