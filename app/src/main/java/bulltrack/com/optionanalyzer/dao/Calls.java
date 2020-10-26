package bulltrack.com.optionanalyzer.dao;

import java.util.Date;

public class Calls {
    String URL;
    String brokerName;
    String call;
    Date generateDate;
    int sequence;
    String sponsored;
    String symbol;
    Date updDate;

    public Calls(Date date, String str, String str2, int i, String str3, String str4, String str5, Date date2) {
        this.generateDate = date;
        this.brokerName = str;
        this.URL = str2;
        this.sequence = i;
        this.sponsored = str3;
        this.symbol = str4;
        this.call = str5;
        this.updDate = date2;
    }

    public Date getGenerateDate() {
        return this.generateDate;
    }

    public void setGenerateDate(Date date) {
        this.generateDate = date;
    }

    public String getBrokerName() {
        return this.brokerName;
    }

    public void setBrokerName(String str) {
        this.brokerName = str;
    }

    public String getURL() {
        return this.URL;
    }

    public void setURL(String str) {
        this.URL = str;
    }

    public int getSequence() {
        return this.sequence;
    }

    public void setSequence(int i) {
        this.sequence = i;
    }

    public String getSponsored() {
        return this.sponsored;
    }

    public void setSponsored(String str) {
        this.sponsored = str;
    }

    public String getSymbol() {
        return this.symbol;
    }

    public void setSymbol(String str) {
        this.symbol = str;
    }

    public String getCall() {
        return this.call;
    }

    public void setCall(String str) {
        this.call = str;
    }

    public Date getUpdDate() {
        return this.updDate;
    }

    public void setUpdDate(Date date) {
        this.updDate = date;
    }
}
