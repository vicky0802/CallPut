package bulltrack.com.optionanalyzer.dao;

import java.util.List;

public class XDayMover {
    OptionID optID;
    float profitLoss;
    List<EodTick> ticks;

    public OptionID getOptID() {
        return this.optID;
    }

    public void setOptID(OptionID optionID) {
        this.optID = optionID;
    }

    public float getProfitLoss() {
        return this.profitLoss;
    }

    public void setProfitLoss(float f) {
        this.profitLoss = f;
    }

    public List<EodTick> getTicks() {
        return this.ticks;
    }

    public void setTicks(List<EodTick> list) {
        this.ticks = list;
    }
}
