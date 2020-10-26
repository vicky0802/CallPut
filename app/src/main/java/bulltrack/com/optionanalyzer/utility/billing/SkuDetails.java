package bulltrack.com.optionanalyzer.utility.billing;

import org.json.JSONException;
import org.json.JSONObject;

public class SkuDetails {
    private final String mDescription;
    private final String mItemType;
    private final String mJson;
    private final String mPrice;
    private final long mPriceAmountMicros;
    private final String mPriceCurrencyCode;
    private final String mSku;
    private final String mTitle;
    private final String mType;

    public SkuDetails(String str) throws JSONException {
        this(IabHelper.ITEM_TYPE_INAPP, str);
    }

    public SkuDetails(String str, String str2) throws JSONException {
        this.mItemType = str;
        this.mJson = str2;
        JSONObject jSONObject = new JSONObject(this.mJson);
        this.mSku = jSONObject.optString("productId");
        this.mType = jSONObject.optString("type");
        this.mPrice = jSONObject.optString("price");
        this.mPriceAmountMicros = jSONObject.optLong("price_amount_micros");
        this.mPriceCurrencyCode = jSONObject.optString("price_currency_code");
        this.mTitle = jSONObject.optString("title");
        this.mDescription = jSONObject.optString("description");
    }

    public String getSku() {
        return this.mSku;
    }

    public String getType() {
        return this.mType;
    }

    public String getPrice() {
        return this.mPrice;
    }

    public long getPriceAmountMicros() {
        return this.mPriceAmountMicros;
    }

    public String getPriceCurrencyCode() {
        return this.mPriceCurrencyCode;
    }

    public String getTitle() {
        return this.mTitle;
    }

    public String getDescription() {
        return this.mDescription;
    }

    public String toString() {
        return "SkuDetails:" + this.mJson;
    }
}
