package bulltrack.com.optionanalyzer.dao;

import java.io.Serializable;
import java.util.Date;

public class StrategyResultsFilter implements Serializable {
    private static final long serialVersionUID = 1;
    private int audience;
    private float breakevenDown;
    private float breakevenUp;
    private float expectedGain;
    private float interestCost;
    private float investment;
    private int lotSize;
    private float maxGain;
    private float maxRisk;
    private float netDebit;
    private int noOflegs;
    private int rank;
    private int runSeq;
    private int strategyId;
    private String strategyName;
    private int strategySubKey;
    private String symbol;
    private String trend_c;
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

    public float getExpectedGain() {
        return this.expectedGain;
    }

    public void setExpectedGain(float f) {
        this.expectedGain = f;
    }

    public float getInvestment() {
        return this.investment;
    }

    public void setInvestment(float f) {
        this.investment = f;
    }

    public float getInterestCost() {
        return this.interestCost;
    }

    public void setInterestCost(float f) {
        this.interestCost = f;
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

    public float getNetDebit() {
        return this.netDebit;
    }

    public void setNetDebit(float f) {
        this.netDebit = f;
    }

    public float getMaxRisk() {
        return this.maxRisk;
    }

    public void setMaxRisk(float f) {
        this.maxRisk = f;
    }

    public float getMaxGain() {
        return this.maxGain;
    }

    public void setMaxGain(float f) {
        this.maxGain = f;
    }

    public float getBreakevenDown() {
        return this.breakevenDown;
    }

    public void setBreakevenDown(float f) {
        this.breakevenDown = f;
    }

    public float getBreakevenUp() {
        return this.breakevenUp;
    }

    public void setBreakevenUp(float f) {
        this.breakevenUp = f;
    }

    public String getStrategyName() {
        return this.strategyName;
    }

    public void setStrategyName(String str) {
        this.strategyName = str;
    }

    public String getTrend_c() {
        return this.trend_c;
    }

    public void setTrend_c(String str) {
        this.trend_c = str;
    }

    public int getNoOflegs() {
        return this.noOflegs;
    }

    public void setNoOflegs(int i) {
        this.noOflegs = i;
    }

    public int getLotSize() {
        return this.lotSize;
    }

    public void setLotSize(int i) {
        this.lotSize = i;
    }
}
