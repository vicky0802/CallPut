package bulltrack.com.optionanalyzer.dao;

import java.util.Date;

public class RTData {
    float annualisedVolatility;
    float change;
    int changeinOpenInterest;
    float closePrice;
    Date expiry;
    float highPrice;
    float impliedVolatility;
    float lastPrice;
    float lowPrice;
    int marketLot;
    int numberOfContractsTraded;
    int openInterest;
    float openPrice;
    String optType;
    float pChange;
    float prevClose;
    String putCall;
    float strike;
    String symbol;
    float underlyingValue;

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

    public String getPutCall() {
        return this.putCall;
    }

    public void setPutCall(String str) {
        this.putCall = str;
    }

    public float getAnnualisedVolatility() {
        return this.annualisedVolatility;
    }

    public void setAnnualisedVolatility(float f) {
        this.annualisedVolatility = f;
    }

    public float getHighPrice() {
        return this.highPrice;
    }

    public void setHighPrice(float f) {
        this.highPrice = f;
    }

    public int getMarketLot() {
        return this.marketLot;
    }

    public void setMarketLot(int i) {
        this.marketLot = i;
    }

    public float getpChange() {
        return this.pChange;
    }

    public void setpChange(float f) {
        this.pChange = f;
    }

    public int getChangeinOpenInterest() {
        return this.changeinOpenInterest;
    }

    public void setChangeinOpenInterest(int i) {
        this.changeinOpenInterest = i;
    }

    public int getOpenInterest() {
        return this.openInterest;
    }

    public void setOpenInterest(int i) {
        this.openInterest = i;
    }

    public float getOpenPrice() {
        return this.openPrice;
    }

    public void setOpenPrice(float f) {
        this.openPrice = f;
    }

    public float getPrevClose() {
        return this.prevClose;
    }

    public void setPrevClose(float f) {
        this.prevClose = f;
    }

    public float getLowPrice() {
        return this.lowPrice;
    }

    public void setLowPrice(float f) {
        this.lowPrice = f;
    }

    public int getNumberOfContractsTraded() {
        return this.numberOfContractsTraded;
    }

    public void setNumberOfContractsTraded(int i) {
        this.numberOfContractsTraded = i;
    }

    public float getChange() {
        return this.change;
    }

    public void setChange(float f) {
        this.change = f;
    }

    public float getImpliedVolatility() {
        return this.impliedVolatility;
    }

    public void setImpliedVolatility(float f) {
        this.impliedVolatility = f;
    }

    public float getUnderlyingValue() {
        return this.underlyingValue;
    }

    public void setUnderlyingValue(float f) {
        this.underlyingValue = f;
    }

    public float getClosePrice() {
        return this.closePrice;
    }

    public void setClosePrice(float f) {
        this.closePrice = f;
    }

    public float getLastPrice() {
        return this.lastPrice;
    }

    public void setLastPrice(float f) {
        this.lastPrice = f;
    }
}
