package bulltrack.com.optionanalyzer.dao;

import java.util.Date;

public class EodTick {
    float closePrice;
    Date expiry;
    float highPrice;
    float lowPrice;
    int openInt;
    float openPrice;
    String optType;
    String putOrCall;
    float strike;
    String symbol;
    Date tickDate;
    int volume;

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

    public Date getTickDate() {
        return this.tickDate;
    }

    public void setTickDate(Date date) {
        this.tickDate = date;
    }

    public float getOpenPrice() {
        return this.openPrice;
    }

    public void setOpenPrice(float f) {
        this.openPrice = f;
    }

    public float getHighPrice() {
        return this.highPrice;
    }

    public void setHighPrice(float f) {
        this.highPrice = f;
    }

    public float getLowPrice() {
        return this.lowPrice;
    }

    public void setLowPrice(float f) {
        this.lowPrice = f;
    }

    public float getClosePrice() {
        return this.closePrice;
    }

    public void setClosePrice(float f) {
        this.closePrice = f;
    }

    public int getVolume() {
        return this.volume;
    }

    public void setVolume(int i) {
        this.volume = i;
    }

    public int getOpenInt() {
        return this.openInt;
    }

    public void setOpenInt(int i) {
        this.openInt = i;
    }
}
