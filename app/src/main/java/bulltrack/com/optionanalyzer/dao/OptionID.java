package bulltrack.com.optionanalyzer.dao;

import java.util.Date;

public class OptionID {
    Date expiry;
    String optType;
    String putOrCall;
    float strike;
    String symbol;

    public OptionID() {
    }

    public OptionID(String str, String str2, Date date, float f, String str3) {
        this.optType = str;
        this.symbol = str2;
        this.expiry = date;
        this.strike = f;
        this.putOrCall = str3;
    }

    public String getOptType() {
        return this.optType;
    }

    public void setOptType(String str) {
        this.optType = str;
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

    public float getStrike() {
        return this.strike;
    }

    public void setStrike(float f) {
        this.strike = f;
    }

    public String getPutOrCall() {
        return this.putOrCall;
    }

    public void setPutOrCall(String str) {
        this.putOrCall = str;
    }
}
