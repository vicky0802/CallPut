package bulltrack.com.optionanalyzer.dao;

import java.util.Date;

public class LegId {
    private int runSeq;
    private int strategyId;
    private int strategySubKey;
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

    public int getRunSeq() {
        return this.runSeq;
    }

    public void setRunSeq(int i) {
        this.runSeq = i;
    }

    public Date getUpdD() {
        return this.updD;
    }

    public void setUpdD(Date date) {
        this.updD = date;
    }
}
