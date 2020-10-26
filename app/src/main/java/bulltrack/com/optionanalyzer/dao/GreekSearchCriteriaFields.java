package bulltrack.com.optionanalyzer.dao;

import java.util.Date;

public class GreekSearchCriteriaFields {
    String callOrPut;
    Date expiryDate;
    float lastPrice;
    Date priceUpd;
    String stock;
    float strikeFrom;
    float strikeTo;

    public GreekSearchCriteriaFields(String str, Date date, float f, float f2, String str2) {
        this.stock = str;
        this.expiryDate = date;
        this.strikeFrom = f;
        this.strikeTo = f2;
        this.callOrPut = str2;
    }

    public String getStock() {
        return this.stock;
    }

    public void setStock(String str) {
        this.stock = str;
    }

    public Date getExpiryDate() {
        return this.expiryDate;
    }

    public void setExpiryDate(Date date) {
        this.expiryDate = date;
    }

    public float getStrikeFrom() {
        return this.strikeFrom;
    }

    public void setStrikeFrom(float f) {
        this.strikeFrom = f;
    }

    public float getStrikeTo() {
        return this.strikeTo;
    }

    public void setStrikeTo(float f) {
        this.strikeTo = f;
    }

    public String getCallOrPut() {
        return this.callOrPut;
    }

    public void setCallOrPut(String str) {
        this.callOrPut = str;
    }

    public float getLastPrice() {
        return this.lastPrice;
    }

    public void setLastPrice(float f) {
        this.lastPrice = f;
    }

    public Date getPriceUpd() {
        return this.priceUpd;
    }

    public void setPriceUpd(Date date) {
        this.priceUpd = date;
    }
}
