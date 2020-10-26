package bulltrack.com.optionanalyzer.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GreekValues {

    /* renamed from: OI */
    private int f118OI;
    private int OIChange;
    float annualisedVolatility;
    private String callPut;
    float change;
    private float closePrice;
    private float delta;
    private Date entryDate;
    private float entryPrice;
    private Date exitDate;
    private float exitPrice;
    private Date expiry_d;
    private float gamma;
    private float highPrice;
    float impliedVolatility;
    float lastPrice;
    private String longShort;
    private float lowPrice;
    int marketLot;
    private int noOfCntr;
    private float openPrice;
    float pChange;
    float prevClose;
    private Date price_upd_d;
    private int quantity;
    private float strike;
    private String symbol;
    private float theoValue;
    private float theta;
    float underlyingValue;
    private Date upd_d;
    private float vega;

    public GreekValues(String str, float f, String str2, Date date, float f2, float f3, float f4, float f5, float f6, Date date2, float f7, float f8, float f9, float f10, int i, int i2, int i3, Date date3, float f11, int i4, float f12, float f13, float f14, float f15, float f16, float f17) {
        this.symbol = str;
        this.strike = f;
        this.callPut = str2;
        this.expiry_d = date;
        this.theoValue = f2;
        this.delta = f3;
        this.gamma = f4;
        this.vega = f5;
        this.theta = f6;
        this.upd_d = date2;
        this.openPrice = f7;
        this.highPrice = f8;
        this.lowPrice = f9;
        this.closePrice = f10;
        this.noOfCntr = i;
        this.f118OI = i2;
        this.OIChange = i3;
        this.price_upd_d = date3;
        this.annualisedVolatility = f11;
        this.marketLot = i4;
        this.pChange = f12;
        this.prevClose = f13;
        this.change = f14;
        this.impliedVolatility = f15;
        this.underlyingValue = f16;
        this.lastPrice = f17;
    }

    public String getSymbol() {
        return this.symbol;
    }

    public void setSymbol(String str) {
        this.symbol = str;
    }

    public float getStrike() {
        return this.strike;
    }

    public void setStrike(float f) {
        this.strike = f;
    }

    public String getCallPut() {
        return this.callPut;
    }

    public void setCallPut(String str) {
        this.callPut = str;
    }

    public Date getExpiry_d() {
        return this.expiry_d;
    }

    public void setExpiry_d(Date date) {
        this.expiry_d = date;
    }

    public float getTheoValue() {
        return this.theoValue;
    }

    public void setTheoValue(float f) {
        this.theoValue = f;
    }

    public float getDelta() {
        return this.delta;
    }

    public void setDelta(float f) {
        this.delta = f;
    }

    public float getGamma() {
        return this.gamma;
    }

    public void setGamma(float f) {
        this.gamma = f;
    }

    public float getVega() {
        return this.vega;
    }

    public void setVega(float f) {
        this.vega = f;
    }

    public float getTheta() {
        return this.theta;
    }

    public void setTheta(float f) {
        this.theta = f;
    }

    public Date getUpd_d() {
        return this.upd_d;
    }

    public void setUpd_d(Date date) {
        this.upd_d = date;
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

    public int getNoOfCntr() {
        return this.noOfCntr;
    }

    public void setNoOfCntr(int i) {
        this.noOfCntr = i;
    }

    public int getOI() {
        return this.f118OI;
    }

    public void setOI(int i) {
        this.f118OI = i;
    }

    public int getOIChange() {
        return this.OIChange;
    }

    public void setOIChange(int i) {
        this.OIChange = i;
    }

    public Date getPrice_upd_d() {
        return this.price_upd_d;
    }

    public void setPrice_upd_d(Date date) {
        this.price_upd_d = date;
    }

    public float getAnnualisedVolatility() {
        return this.annualisedVolatility;
    }

    public void setAnnualisedVolatility(float f) {
        this.annualisedVolatility = f;
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

    public float getPrevClose() {
        return this.prevClose;
    }

    public void setPrevClose(float f) {
        this.prevClose = f;
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

    public float getLastPrice() {
        return this.lastPrice;
    }

    public void setLastPrice(float f) {
        this.lastPrice = f;
    }

    public String getLongShort() {
        return this.longShort;
    }

    public void setLongShort(String str) {
        this.longShort = str;
    }

    public float getEntryPrice() {
        return this.entryPrice;
    }

    public void setEntryPrice(float f) {
        this.entryPrice = f;
    }

    public Date getEntryDate() {
        return this.entryDate;
    }

    public void setEntryDate(Date date) {
        this.entryDate = date;
    }

    public float getExitPrice() {
        return this.exitPrice;
    }

    public void setExitPrice(float f) {
        this.exitPrice = f;
    }

    public Date getExitDate() {
        return this.exitDate;
    }

    public void setExitDate(Date date) {
        this.exitDate = date;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int i) {
        this.quantity = i;
    }

    public String dateFormatter(Date date, String str) {
        if (date == null || str == null) {
            return null;
        }
        return new SimpleDateFormat(str).format(date);
    }

    public Date dateFormatter(String str, String str2) {
        if (str == null || str.trim().equals("") || str2 == null) {
            return null;
        }
        try {
            return new SimpleDateFormat(str2).parse(str);
        } catch (ParseException unused) {
            return null;
        }
    }
}
