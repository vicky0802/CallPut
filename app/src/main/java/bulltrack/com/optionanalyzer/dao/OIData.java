package bulltrack.com.optionanalyzer.dao;

import java.util.Date;

public class OIData {
    int oiChangeCE;
    int oiChangePE;
    int oiChangeXX;
    float oiChgPerCE;
    float oiChgPerPE;
    float oiChgPerXX;
    int oiSumCE;
    int oiSumPE;
    int oiSumXX;
    String symbol;
    Date updDate;

    public Date getUpdDate() {
        return this.updDate;
    }

    public void setUpdDate(Date date) {
        this.updDate = date;
    }

    public String getSymbol() {
        return this.symbol;
    }

    public void setSymbol(String str) {
        this.symbol = str;
    }

    public int getOiSumXX() {
        return this.oiSumXX;
    }

    public void setOiSumXX(int i) {
        this.oiSumXX = i;
    }

    public int getOiChangeXX() {
        return this.oiChangeXX;
    }

    public void setOiChangeXX(int i) {
        this.oiChangeXX = i;
    }

    public float getOiChgPerXX() {
        return this.oiChgPerXX;
    }

    public void setOiChgPerXX(float f) {
        this.oiChgPerXX = f;
    }

    public int getOiSumCE() {
        return this.oiSumCE;
    }

    public void setOiSumCE(int i) {
        this.oiSumCE = i;
    }

    public int getOiChangeCE() {
        return this.oiChangeCE;
    }

    public void setOiChangeCE(int i) {
        this.oiChangeCE = i;
    }

    public float getOiChgPerCE() {
        return this.oiChgPerCE;
    }

    public void setOiChgPerCE(float f) {
        this.oiChgPerCE = f;
    }

    public int getOiSumPE() {
        return this.oiSumPE;
    }

    public void setOiSumPE(int i) {
        this.oiSumPE = i;
    }

    public int getOiChangePE() {
        return this.oiChangePE;
    }

    public void setOiChangePE(int i) {
        this.oiChangePE = i;
    }

    public float getOiChgPerPE() {
        return this.oiChgPerPE;
    }

    public void setOiChgPerPE(float f) {
        this.oiChgPerPE = f;
    }
}
