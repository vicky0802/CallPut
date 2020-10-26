package bulltrack.com.optionanalyzer.dao;

import java.util.Date;

public class StrikeExpiry {
    Date expiry_d;
    float strikeDiff;
    float strikeMax;
    float strikeMin;
    String symbol;
    Date upd_d;

    public String getSymbol() {
        return this.symbol;
    }

    public void setSymbol(String str) {
        this.symbol = str;
    }

    public Date getExpiry_d() {
        return this.expiry_d;
    }

    public void setExpiry_d(Date date) {
        this.expiry_d = date;
    }

    public float getStrikeMin() {
        return this.strikeMin;
    }

    public void setStrikeMin(float f) {
        this.strikeMin = f;
    }

    public float getStrikeMax() {
        return this.strikeMax;
    }

    public void setStrikeMax(float f) {
        this.strikeMax = f;
    }

    public float getStrikeDiff() {
        return this.strikeDiff;
    }

    public void setStrikeDiff(float f) {
        this.strikeDiff = f;
    }

    public Date getUpd_d() {
        return this.upd_d;
    }

    public void setUpd_d(Date date) {
        this.upd_d = date;
    }
}
