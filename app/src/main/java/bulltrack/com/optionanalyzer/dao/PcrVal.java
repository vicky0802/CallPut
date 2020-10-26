package bulltrack.com.optionanalyzer.dao;

import java.util.Date;

public class PcrVal {
    int pcrType;
    float pcrVal;
    String symbol;
    Date updDate;

    public String getSymbol() {
        return this.symbol;
    }

    public void setSymbol(String str) {
        this.symbol = str;
    }

    public int getPcrType() {
        return this.pcrType;
    }

    public void setPcrType(int i) {
        this.pcrType = i;
    }

    public float getPcrVal() {
        return this.pcrVal;
    }

    public void setPcrVal(float f) {
        this.pcrVal = f;
    }

    public Date getUpdDate() {
        return this.updDate;
    }

    public void setUpdDate(Date date) {
        this.updDate = date;
    }
}
