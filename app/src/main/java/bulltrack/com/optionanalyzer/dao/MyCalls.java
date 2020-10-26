package bulltrack.com.optionanalyzer.dao;

public class MyCalls {

    /* renamed from: E */
    String f119E;

    /* renamed from: I */
    String f120I;

    /* renamed from: S */
    String f121S;
    long londNextDate;

    public MyCalls(String str, String str2, String str3, long j) {
        this.f119E = str;
        this.f121S = str2;
        this.f120I = str3;
        this.londNextDate = j;
    }

    public String getE() {
        return this.f119E;
    }

    public void setE(String str) {
        this.f119E = str;
    }

    public String getS() {
        return this.f121S;
    }

    public void setS(String str) {
        this.f121S = str;
    }

    public String getI() {
        return this.f120I;
    }

    public void setI(String str) {
        this.f120I = str;
    }

    public long getLondNextDate() {
        return this.londNextDate;
    }

    public void setLondNextDate(long j) {
        this.londNextDate = j;
    }
}
