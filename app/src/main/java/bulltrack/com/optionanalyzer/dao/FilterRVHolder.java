package bulltrack.com.optionanalyzer.dao;

import bulltrack.com.optionanalyzer.application.MyGreeksApplication;
import bulltrack.com.optionanalyzer.constants.Constants;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FilterRVHolder {
    private static MyGreeksApplication application;
    private String curExpiry;
    private int curHigh;
    private int curLow;
    private String curRisk;
    private String curStock;
    private String curTrend;
    private List<FilterStockRange> lstRange;
    private List<Double> lstRisk;
    private List<String> lstRiskText;
    private List<String> lstStock;
    private List<String> lstTrend;

    public int getCurLow() {
        return this.curLow;
    }

    public void setCurLow(int i) {
        this.curLow = i;
    }

    public int getCurHigh() {
        return this.curHigh;
    }

    public void setCurHigh(int i) {
        this.curHigh = i;
    }

    private static class SingletonHolder {
        static final FilterRVHolder SINGLE_INSTANCE = new FilterRVHolder();

        private SingletonHolder() {
        }
    }

    private FilterRVHolder() {
    }

    public static FilterRVHolder getInstance(MyGreeksApplication myGreeksApplication) {
        application = myGreeksApplication;
        return SingletonHolder.SINGLE_INSTANCE;
    }

    public List<String> getLstStock() {
        return this.lstStock;
    }

    public void setLstStock(List<String> list) {
        this.lstStock = list;
    }

    public List<String> getLstTrend() {
        return this.lstTrend;
    }

    public void setLstTrend() {
        ArrayList arrayList = new ArrayList();
        this.lstTrend = arrayList;
        arrayList.add(Constants.FILTER_TREND_BULLISH);
        this.lstTrend.add(Constants.FILTER_TREND_BEARISH);
        this.lstTrend.add(Constants.FILTER_TREND_NEUTRAL);
    }

    public List<FilterStockRange> getLstRange() {
        return this.lstRange;
    }

    public void setLstRange(List<FilterStockRange> list) {
        this.lstRange = list;
    }

    public String getCurStock() {
        return this.curStock;
    }

    public String getCurTrend() {
        return this.curTrend;
    }

    public void setCurTrend(String str) {
        this.curTrend = str;
    }

    public void setCurStock(String str) {
        this.curStock = str;
    }

    public String getCurExpiry() {
        return this.curExpiry;
    }

    public void setCurExpiry(String str) {
        this.curExpiry = str;
    }

    public String getCurRisk() {
        return this.curRisk;
    }

    public void setCurRisk(String str) {
        this.curRisk = str;
    }

    public List<Double> getLstRisk() {
        return this.lstRisk;
    }

    public void setLstRisk(List<Double> list) {
        this.lstRisk = list;
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            arrayList.add(Integer.valueOf((int) list.get(i).doubleValue()).toString());
        }
        arrayList.add(Constants.FILTER_RISK_NOLIMIT);
        this.lstRiskText = arrayList;
    }

    /* access modifiers changed from: package-private */
    public int getPos(List<String> list, String str) {
        if (str == null) {
            return -1;
        }
        for (int i = 0; i < list.size(); i++) {
            if (str.trim().equalsIgnoreCase(list.get(i))) {
                return i;
            }
        }
        return -1;
    }

    public int getPosStock() {
        return getPos(this.lstStock, this.curStock);
    }

    public int getPosExpiry() {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < this.lstRange.size(); i++) {
            if (this.lstRange.get(i).getSymbol().equalsIgnoreCase(this.curStock)) {
                arrayList.add(application.dateFormatter(this.lstRange.get(i).getExpiry(), Constants.DT_FMT_dd_MMM_yyyy));
            }
        }
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            if (((String) arrayList.get(i2)).equalsIgnoreCase(this.curExpiry)) {
                return i2;
            }
        }
        return -1;
    }

    public int getPosTrend() {
        return getPos(this.lstTrend, this.curTrend);
    }

    public int getPosRisk() {
        return getPos(this.lstRiskText, this.curRisk);
    }

    public List<String> getLstRiskText() {
        return this.lstRiskText;
    }

    public double[] getRange(String str, Date date) {
        double d;
        double d2;
        int i = 0;
        while (true) {
            d = 0.0d;
            if (i < this.lstRange.size()) {
                if (this.lstRange.get(i).getSymbol().equalsIgnoreCase(str) && this.lstRange.get(i).getExpiry().getTime() == date.getTime()) {
                    d = this.lstRange.get(i).getLowRange();
                    d2 = this.lstRange.get(i).getHighRange();
                    break;
                }
                i++;
            } else {
                d2 = 0.0d;
                break;
            }
        }
        return new double[]{d, d2};
    }

    public double[] getRange() {
        double d;
        double d2;
        Date dateFormatter = application.dateFormatter(this.curExpiry, Constants.DT_FMT_dd_MMM_yyyy);
        int i = 0;
        while (true) {
            d = 0.0d;
            if (i < this.lstRange.size()) {
                if (this.lstRange.get(i).getSymbol().equalsIgnoreCase(this.curStock) && this.lstRange.get(i).getExpiry().getTime() == dateFormatter.getTime()) {
                    d = this.lstRange.get(i).getLowRange();
                    d2 = this.lstRange.get(i).getHighRange();
                    break;
                }
                i++;
            } else {
                d2 = 0.0d;
                break;
            }
        }
        return new double[]{d, d2};
    }
}
