package bulltrack.com.optionanalyzer.dao;

import java.util.Date;

public class OptionMargin {
    float MPR;
    float additionalMargin;
    float annualVolatility;
    Date expiry;
    float exposureMargin;
    float extremeMargin;
    int lotSize;
    String optType;
    float optionPrice;
    String putOrCall;
    float securityMargin;
    float spanMargin;
    float stockPrice;
    float strike;
    String symbol;
    float varMargin;

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

    public float getStockPrice() {
        return this.stockPrice;
    }

    public void setStockPrice(float f) {
        this.stockPrice = f;
    }

    public float getOptionPrice() {
        return this.optionPrice;
    }

    public void setOptionPrice(float f) {
        this.optionPrice = f;
    }

    public float getAnnualVolatility() {
        return this.annualVolatility;
    }

    public void setAnnualVolatility(float f) {
        this.annualVolatility = f;
    }

    public float getMPR() {
        return this.MPR;
    }

    public void setMPR(float f) {
        this.MPR = f;
    }

    public int getLotSize() {
        return this.lotSize;
    }

    public void setLotSize(int i) {
        this.lotSize = i;
    }

    public float getSecurityMargin() {
        return this.securityMargin;
    }

    public void setSecurityMargin(float f) {
        this.securityMargin = f;
    }

    public float getVarMargin() {
        return this.varMargin;
    }

    public void setVarMargin(float f) {
        this.varMargin = f;
    }

    public float getExtremeMargin() {
        return this.extremeMargin;
    }

    public void setExtremeMargin(float f) {
        this.extremeMargin = f;
    }

    public float getSpanMargin() {
        return this.spanMargin;
    }

    public void setSpanMargin(float f) {
        this.spanMargin = f;
    }

    public float getExposureMargin() {
        return this.exposureMargin;
    }

    public void setExposureMargin(float f) {
        this.exposureMargin = f;
    }

    public float getAdditionalMargin() {
        return this.additionalMargin;
    }

    public void setAdditionalMargin(float f) {
        this.additionalMargin = f;
    }
}
