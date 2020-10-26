package bulltrack.com.optionanalyzer.dao;

import java.util.Date;

public class TradeCalls {
    String AlgoType;

    /* renamed from: SL */
    Double f122SL;

    /* renamed from: T1 */
    Double f123T1;

    /* renamed from: T2 */
    Double f124T2;
    String action;
    double closePrice;
    Double entryPrice;
    Date expiry;
    String flag;
    String googleProduct;
    String instrType;
    int lotSize;
    Date nextTradeDate;
    Date priceDate;
    double priceGainPC;
    double strike;
    int subkey;
    Date subscribeExipry;
    String symbol;
    double todaysPrice;
    Date updateDT;

    public Date getPriceDate() {
        return this.priceDate;
    }

    public void setPriceDate(Date date) {
        this.priceDate = date;
    }

    public int getSubkey() {
        return this.subkey;
    }

    public void setSubkey(int i) {
        this.subkey = i;
    }

    public String getSymbol() {
        return this.symbol;
    }

    public void setSymbol(String str) {
        this.symbol = str;
    }

    public String getInstrType() {
        return this.instrType;
    }

    public void setInstrType(String str) {
        this.instrType = str;
    }

    public double getStrike() {
        return this.strike;
    }

    public void setStrike(double d) {
        this.strike = d;
    }

    public Date getExpiry() {
        return this.expiry;
    }

    public void setExpiry(Date date) {
        this.expiry = date;
    }

    public double getClosePrice() {
        return this.closePrice;
    }

    public void setClosePrice(double d) {
        this.closePrice = d;
    }

    public double getPriceGainPC() {
        return this.priceGainPC;
    }

    public void setPriceGainPC(double d) {
        this.priceGainPC = d;
    }

    public String getAction() {
        return this.action;
    }

    public void setAction(String str) {
        this.action = str;
    }

    public Double getEntryPrice() {
        return this.entryPrice;
    }

    public void setEntryPrice(Double d) {
        this.entryPrice = d;
    }

    public Double getT1() {
        return this.f123T1;
    }

    public void setT1(Double d) {
        this.f123T1 = d;
    }

    public Double getT2() {
        return this.f124T2;
    }

    public void setT2(Double d) {
        this.f124T2 = d;
    }

    public Double getSL() {
        return this.f122SL;
    }

    public void setSL(Double d) {
        this.f122SL = d;
    }

    public String getAlgoType() {
        return this.AlgoType;
    }

    public void setAlgoType(String str) {
        this.AlgoType = str;
    }

    public String getFlag() {
        return this.flag;
    }

    public void setFlag(String str) {
        this.flag = str;
    }

    public Date getUpdateDT() {
        return this.updateDT;
    }

    public void setUpdateDT(Date date) {
        this.updateDT = date;
    }

    public int getLotSize() {
        return this.lotSize;
    }

    public void setLotSize(int i) {
        this.lotSize = i;
    }

    public Date getNextTradeDate() {
        return this.nextTradeDate;
    }

    public void setNextTradeDate(Date date) {
        this.nextTradeDate = date;
    }

    public String getGoogleProduct() {
        return this.googleProduct;
    }

    public void setGoogleProduct(String str) {
        this.googleProduct = str;
    }

    public double getTodaysPrice() {
        return this.todaysPrice;
    }

    public void setTodaysPrice(double d) {
        this.todaysPrice = d;
    }

    public Date getSubscribeExipry() {
        return this.subscribeExipry;
    }

    public void setSubscribeExipry(Date date) {
        this.subscribeExipry = date;
    }
}
