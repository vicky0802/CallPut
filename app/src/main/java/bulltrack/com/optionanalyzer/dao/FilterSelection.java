package bulltrack.com.optionanalyzer.dao;

import java.util.Date;

public class FilterSelection {
    Date expiry;
    double highRange;
    double lowRange;
    String risk;
    String symbol;
    String trend;

    public String getSymbol() {
        return this.symbol;
    }

    public void setSymbol(String str) {
        this.symbol = str;
    }

    public String getTrend() {
        return this.trend;
    }

    public void setTrend(String str) {
        this.trend = str;
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

    public String getRisk() {
        return this.risk;
    }

    public void setRisk(String str) {
        this.risk = str;
    }

    public void setHighRange(double d) {
        this.highRange = d;
    }

    public Date getExpiry() {
        return this.expiry;
    }

    public void setExpiry(Date date) {
        this.expiry = date;
    }
}
