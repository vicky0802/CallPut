package bulltrack.com.optionanalyzer.dao;

import java.util.Date;

public class StrategyLegsFilter {
    private String action;
    private int audience;
    private String callPut;
    private Date expiry;
    private float premium;
    private int rank;
    private int runSeq;
    private int strategyId;
    private int strategySubKey;
    private float strike;
    private String symbol;
    private Date updD;

    public String getSymbol() {
        return this.symbol;
    }

    public void setSymbol(String str) {
        this.symbol = str;
    }

    public int getStrategyId() {
        return this.strategyId;
    }

    public void setStrategyId(int i) {
        this.strategyId = i;
    }

    public int getStrategySubKey() {
        return this.strategySubKey;
    }

    public void setStrategySubKey(int i) {
        this.strategySubKey = i;
    }

    public int getRank() {
        return this.rank;
    }

    public void setRank(int i) {
        this.rank = i;
    }

    public int getRunSeq() {
        return this.runSeq;
    }

    public void setRunSeq(int i) {
        this.runSeq = i;
    }

    public int getAudience() {
        return this.audience;
    }

    public void setAudience(int i) {
        this.audience = i;
    }

    public Date getUpdD() {
        return this.updD;
    }

    public void setUpdD(Date date) {
        this.updD = date;
    }

    public String getAction() {
        return this.action;
    }

    public void setAction(String str) {
        this.action = str;
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

    public Date getExpiry() {
        return this.expiry;
    }

    public void setExpiry(Date date) {
        this.expiry = date;
    }

    public float getPremium() {
        return this.premium;
    }

    public void setPremium(float f) {
        this.premium = f;
    }
}
