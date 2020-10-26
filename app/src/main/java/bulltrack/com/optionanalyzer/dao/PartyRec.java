package bulltrack.com.optionanalyzer.dao;

import java.util.Date;

public class PartyRec {
    String instrument;
    int longPositions;
    String party;
    Date priceUpd;
    int shortPositions;

    public Date getPriceUpd() {
        return this.priceUpd;
    }

    public void setPriceUpd(Date date) {
        this.priceUpd = date;
    }

    public String getParty() {
        return this.party;
    }

    public void setParty(String str) {
        this.party = str;
    }

    public int getLongPositions() {
        return this.longPositions;
    }

    public void setLongPositions(int i) {
        this.longPositions = i;
    }

    public int getShortPositions() {
        return this.shortPositions;
    }

    public void setShortPositions(int i) {
        this.shortPositions = i;
    }

    public String getInstrument() {
        return this.instrument;
    }

    public void setInstrument(String str) {
        this.instrument = str;
    }
}
