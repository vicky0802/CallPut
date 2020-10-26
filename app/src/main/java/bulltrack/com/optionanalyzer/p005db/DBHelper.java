package bulltrack.com.optionanalyzer.p005db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Base64;
import android.util.Log;
import bulltrack.com.optionanalyzer.application.MyGreeksApplication;
import bulltrack.com.optionanalyzer.constants.Constants;
import bulltrack.com.optionanalyzer.dao.Calls;
import bulltrack.com.optionanalyzer.dao.GreekSearchCriteriaFields;
import bulltrack.com.optionanalyzer.dao.GreekValues;
import bulltrack.com.optionanalyzer.dao.MyCalls;
import bulltrack.com.optionanalyzer.dao.OIData;
import bulltrack.com.optionanalyzer.dao.PartyRec;
import bulltrack.com.optionanalyzer.dao.StrategyLegsFilter;
import bulltrack.com.optionanalyzer.dao.StrategyResultsFilter;
import bulltrack.com.optionanalyzer.dao.StrikeExpiry;
import com.google.firebase.messaging.FirebaseMessaging;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/* renamed from: bulltrack.com.optionanalyzer.db.DBHelper */
public class DBHelper extends SQLiteOpenHelper {
    public static final String APP_INSTALL_COLUMN_ID = "install_id";
    public static final String APP_INSTALL_TABLE_NAME = "app_install";
    public static final String BLIST_COLUMN_FLAG_E = "blist_flag_e";
    public static final String BLIST_COLUMN_FLAG_I = "blist_flag_i";
    public static final String BLIST_COLUMN_FLAG_S = "blist_flag_s";
    public static final String BLIST_TABLE_NAME = "blist";
    public static final String BROKER_CALLS_BROKER = "broker";
    public static final String BROKER_CALLS_CREATEDT = "create_dt";
    public static final String BROKER_CALLS_SEQ = "sequence";
    public static final String BROKER_CALLS_SPONSORED = "sponsored";
    public static final String BROKER_CALLS_SYMBOL = "symbol";
    public static final String BROKER_CALLS_TABLE_NAME = "stock_calls";
    public static final String BROKER_CALLS_TIP = "tip";
    public static final String BROKER_CALLS_UPDDT = "update_dt";
    public static final String BROKER_CALLS_URL = "url";
    public static final String DATABASE_NAME = "MyGreeksDB.db";
    private static final int DATABASE_VERSION = 9;
    public static final String FCM_TOPICS_COLUMN_TOPIC = "topic";
    public static final String FCM_TOPICS_TABLE_NAME = "fcm_topics";
    public static final String FINDER_ACC_COLUMN_ENCRP_ENDDT = "finder_stgacc_encrp_end";
    public static final String FINDER_ACC_COLUMN_I = "finder_stgacc_i";
    public static final String FINDER_ACC_COLUMN_S = "finder_stgacc_s";
    public static final String FINDER_ACC_TABLE_NAME = "finder_stgacc";
    public static final String FOLIO_COLUMN_ANNUAL_VOL = "annualisedVolatility";
    public static final String FOLIO_COLUMN_CALLPUT = "callPut";
    public static final String FOLIO_COLUMN_CLOSE = "closePrice";
    public static final String FOLIO_COLUMN_DELTA = "delta";
    public static final String FOLIO_COLUMN_ENTRY_DATE = "entryDate";
    public static final String FOLIO_COLUMN_ENTRY_PRICE = "entryPrice";
    public static final String FOLIO_COLUMN_EXIT_DATE = "exitDate";
    public static final String FOLIO_COLUMN_EXIT_PRICE = "exitPrice";
    public static final String FOLIO_COLUMN_EXPIRY = "expiry_d";
    public static final String FOLIO_COLUMN_GAMMA = "gamma";
    public static final String FOLIO_COLUMN_HIGH = "highPrice";
    public static final String FOLIO_COLUMN_IV = "impliedVolatility";
    public static final String FOLIO_COLUMN_LAST_PRICE = "lastPrice";
    public static final String FOLIO_COLUMN_LONGSHORT = "longShort";
    public static final String FOLIO_COLUMN_LOTSIZE = "marketLot";
    public static final String FOLIO_COLUMN_LOW = "lowPrice";
    public static final String FOLIO_COLUMN_OI = "OI";
    public static final String FOLIO_COLUMN_OICHANGE = "OIChange";
    public static final String FOLIO_COLUMN_OPEN = "openPrice";
    public static final String FOLIO_COLUMN_PC_CHANGE = "pChange";
    public static final String FOLIO_COLUMN_PREV_CLOSE = "prevClose";
    public static final String FOLIO_COLUMN_PRICE_CHANGE = "change";
    public static final String FOLIO_COLUMN_PRICE_UPD = "price_upd_d";
    public static final String FOLIO_COLUMN_QUANTITY = "quantity";
    public static final String FOLIO_COLUMN_SERVICEID = "service_id";
    public static final String FOLIO_COLUMN_STOCK_PRICE = "underlyingValue";
    public static final String FOLIO_COLUMN_STRIKE = "strike";
    public static final String FOLIO_COLUMN_SYMBOL = "symbol";
    public static final String FOLIO_COLUMN_THETA = "theta";
    public static final String FOLIO_COLUMN_UPD = "upd_d";
    public static final String FOLIO_COLUMN_VALUE = "theoValue";
    public static final String FOLIO_COLUMN_VEGA = "vega";
    public static final String FOLIO_COLUMN_VOL = "noOfCntr";
    public static final String FOLIO_TABLE_NAME = "portfolio";
    public static final String GREEKS_COLUMN_ANNUAL_VOL = "annualisedVolatility";
    public static final String GREEKS_COLUMN_CALLPUT = "callPut";
    public static final String GREEKS_COLUMN_CLOSE = "closePrice";
    public static final String GREEKS_COLUMN_DELTA = "delta";
    public static final String GREEKS_COLUMN_EXPIRY = "expiry_d";
    public static final String GREEKS_COLUMN_GAMMA = "gamma";
    public static final String GREEKS_COLUMN_HIGH = "highPrice";
    public static final String GREEKS_COLUMN_IV = "impliedVolatility";
    public static final String GREEKS_COLUMN_LAST_PRICE = "lastPrice";
    public static final String GREEKS_COLUMN_LOTSIZE = "marketLot";
    public static final String GREEKS_COLUMN_LOW = "lowPrice";
    public static final String GREEKS_COLUMN_OI = "OI";
    public static final String GREEKS_COLUMN_OICHANGE = "OIChange";
    public static final String GREEKS_COLUMN_OPEN = "openPrice";
    public static final String GREEKS_COLUMN_PC_CHANGE = "pChange";
    public static final String GREEKS_COLUMN_PREV_CLOSE = "prevClose";
    public static final String GREEKS_COLUMN_PRICE_CHANGE = "change";
    public static final String GREEKS_COLUMN_PRICE_UPD = "price_upd_d";
    public static final String GREEKS_COLUMN_SERVICEID = "service_id";
    public static final String GREEKS_COLUMN_STOCK_PRICE = "underlyingValue";
    public static final String GREEKS_COLUMN_STRIKE = "strike";
    public static final String GREEKS_COLUMN_SYMBOL = "symbol";
    public static final String GREEKS_COLUMN_THETA = "theta";
    public static final String GREEKS_COLUMN_UPD = "upd_d";
    public static final String GREEKS_COLUMN_VALUE = "theoValue";
    public static final String GREEKS_COLUMN_VEGA = "vega";
    public static final String GREEKS_COLUMN_VOL = "noOfCntr";
    public static final String GREEKS_TABLE_NAME = "daily_greeks";
    public static final String OI_STOCK_COLUMN_OI_CHG_CE = "oi_change_ce";
    public static final String OI_STOCK_COLUMN_OI_CHG_PE = "oi_change_pe";
    public static final String OI_STOCK_COLUMN_OI_CHG_PER_CE = "oi_change_per_ce";
    public static final String OI_STOCK_COLUMN_OI_CHG_PER_PE = "oi_change_per_pe";
    public static final String OI_STOCK_COLUMN_OI_CHG_PER_XX = "oi_change_per_xx";
    public static final String OI_STOCK_COLUMN_OI_CHG_XX = "oi_change_xx";
    public static final String OI_STOCK_COLUMN_OI_SUM_CE = "oi_sum_ce";
    public static final String OI_STOCK_COLUMN_OI_SUM_PE = "oi_sum_pe";
    public static final String OI_STOCK_COLUMN_OI_SUM_XX = "oi_sum_xx";
    public static final String OI_STOCK_COLUMN_SYMBOL = "symbol";
    public static final String OI_STOCK_COLUMN_UPD = "upd_d";
    public static final String OI_STOCK_TABLE_NAME = "oi_stock";
    public static final String PARTY_CURRENT_COLUMN_INSTR = "instrument";
    public static final String PARTY_CURRENT_COLUMN_LONG_POS = "long_positions";
    public static final String PARTY_CURRENT_COLUMN_PARTY = "party";
    public static final String PARTY_CURRENT_COLUMN_SHORT_POS = "short_positions";
    public static final String PARTY_CURRENT_COLUMN_UPD = "upd_d";
    public static final String PARTY_CURRENT_TABLE_NAME = "party_current";
    public static final String PARTY_DAYWISE_COLUMN_INSTR = "instrument";
    public static final String PARTY_DAYWISE_COLUMN_LONG_POS = "long_positions";
    public static final String PARTY_DAYWISE_COLUMN_PARTY = "party";
    public static final String PARTY_DAYWISE_COLUMN_SHORT_POS = "short_positions";
    public static final String PARTY_DAYWISE_COLUMN_UPD = "upd_d";
    public static final String PARTY_DAYWISE_TABLE_NAME = "party_daywise";
    public static final String REWARD_CHECK_COLUMN_FLAG = "reward_check_flag";
    public static final String REWARD_CHECK_TABLE_NAME = "reward_check";
    public static final String REWARD_CHECK_TC_COLUMN_FLAG = "reward_check_tc_flag";
    public static final String REWARD_CHECK_TC_TABLE_NAME = "reward_check_tc";
    public static final String STGACC_COLUMN_DT = "stgacc_dt";
    public static final String STGACC_COLUMN_I = "stgacc_i";
    public static final String STGACC_COLUMN_PASS = "stgacc_pass";
    public static final String STGACC_COLUMN_RUN_SEQ = "stgacc_runseqy";
    public static final String STGACC_COLUMN_S = "stgacc_s";
    public static final String STGACC_COLUMN_STG_ID = "stgacc_stg_id";
    public static final String STGACC_COLUMN_SUBKEY = "stgacc_subkey";
    public static final String STGACC_COLUMN_SYMBOL = "stgacc_symbol";
    public static final String STGACC_TABLE_NAME = "stgacc";
    public static final String STG_LEGS_COL_ACTION = "action_cd_c";
    public static final String STG_LEGS_COL_CP = "call_put_stk_cd_c";
    public static final String STG_LEGS_COL_EXP = "expiry_d";
    public static final String STG_LEGS_COL_LEGID = "strategy_leg_seq_i";
    public static final String STG_LEGS_COL_PREM = "premium_f";
    public static final String STG_LEGS_COL_STG_ID = "strategy_type_cd_i";
    public static final String STG_LEGS_COL_STRIKE = "strike_i";
    public static final String STG_LEGS_COL_SUBKEY = "strategy_subkey_i";
    public static final String STG_LEGS_COL_UND = "underlyer_c";
    public static final String STG_LEGS_COL_UPD = "upd_d";
    public static final String STG_LEGS_SAVE_TABLE_NAME = "stg_legs_save";
    public static final String STG_RSLT_COL_DEBIT = "net_debit_f";
    public static final String STG_RSLT_COL_DELTA = "net_delta";
    public static final String STG_RSLT_COL_GAIN = "max_gain_f";
    public static final String STG_RSLT_COL_GAMMA = "net_gamma";
    public static final String STG_RSLT_COL_HBEP = "breakeven_up_pt_f";
    public static final String STG_RSLT_COL_LBEP = "breakeven_dn_pt_f";
    public static final String STG_RSLT_COL_RISK = "max_risk_f";
    public static final String STG_RSLT_COL_STGNAME = "strategy_name_s";
    public static final String STG_RSLT_COL_STG_ID = "strategy_type_cd_i";
    public static final String STG_RSLT_COL_SUBKEY = "strategy_subkey_i";
    public static final String STG_RSLT_COL_THETA = "net_theta";
    public static final String STG_RSLT_COL_UND = "underlyer_c";
    public static final String STG_RSLT_COL_UPD = "upd_d";
    public static final String STG_RSLT_COL_VEGA = "net_vega";
    public static final String STG_RSLT_SAVE_TABLE_NAME = "stg_result_save";
    public static final String STRIKES_COLUMN_EXPIRY = "expiry_d";
    public static final String STRIKES_COLUMN_STRIKE_DIFF = "strike_diff";
    public static final String STRIKES_COLUMN_STRIKE_MAX = "strike_max";
    public static final String STRIKES_COLUMN_STRIKE_MIN = "strike_min";
    public static final String STRIKES_COLUMN_SYMBOL = "symbol";
    public static final String STRIKES_COLUMN_UPD = "upd_d";
    public static final String STRIKES_TABLE_NAME = "strikes";
    public static final String WATCHES_COLUMN_ANNUAL_VOL = "annualisedVolatility";
    public static final String WATCHES_COLUMN_CALLPUT = "callPut";
    public static final String WATCHES_COLUMN_CLOSE = "closePrice";
    public static final String WATCHES_COLUMN_DELTA = "delta";
    public static final String WATCHES_COLUMN_EXPIRY = "expiry_d";
    public static final String WATCHES_COLUMN_GAMMA = "gamma";
    public static final String WATCHES_COLUMN_HIGH = "highPrice";
    public static final String WATCHES_COLUMN_IV = "impliedVolatility";
    public static final String WATCHES_COLUMN_LAST_PRICE = "lastPrice";
    public static final String WATCHES_COLUMN_LOTSIZE = "marketLot";
    public static final String WATCHES_COLUMN_LOW = "lowPrice";
    public static final String WATCHES_COLUMN_OI = "OI";
    public static final String WATCHES_COLUMN_OICHANGE = "OIChange";
    public static final String WATCHES_COLUMN_OPEN = "openPrice";
    public static final String WATCHES_COLUMN_PC_CHANGE = "pChange";
    public static final String WATCHES_COLUMN_PREV_CLOSE = "prevClose";
    public static final String WATCHES_COLUMN_PRICE_CHANGE = "change";
    public static final String WATCHES_COLUMN_PRICE_UPD = "price_upd_d";
    public static final String WATCHES_COLUMN_SERVICEID = "service_id";
    public static final String WATCHES_COLUMN_STOCK_PRICE = "underlyingValue";
    public static final String WATCHES_COLUMN_STRIKE = "strike";
    public static final String WATCHES_COLUMN_SYMBOL = "symbol";
    public static final String WATCHES_COLUMN_THETA = "theta";
    public static final String WATCHES_COLUMN_UPD = "upd_d";
    public static final String WATCHES_COLUMN_VALUE = "theoValue";
    public static final String WATCHES_COLUMN_VEGA = "vega";
    public static final String WATCHES_COLUMN_VOL = "noOfCntr";
    public static final String WATCHES_TABLE_NAME = "my_watches";
    private String TAG = "DBHelper";
    MyGreeksApplication appContext = null;

    /* renamed from: db */
    SQLiteDatabase f125db;

    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("create table daily_greeks ( service_id text , symbol text , strike real , callPut text , expiry_d text , theoValue real , delta real , gamma real , vega real , theta real , upd_d text, openPrice real, highPrice real, lowPrice real, closePrice real, noOfCntr integer, OI integer, OIChange integer, price_upd_d text, annualisedVolatility real, marketLot integer, pChange real, prevClose real, change real, impliedVolatility real, underlyingValue real, lastPrice real ) ");
        sQLiteDatabase.execSQL("create table portfolio ( service_id text , symbol text , strike real , callPut text , expiry_d text , theoValue real , delta real , gamma real , vega real , theta real , upd_d text, openPrice real, highPrice real, lowPrice real, closePrice real, noOfCntr integer, OI integer, OIChange integer, price_upd_d text, annualisedVolatility real, marketLot integer, pChange real, prevClose real, change real, impliedVolatility real, underlyingValue real, lastPrice real, longShort text, entryPrice real, entryDate text, exitPrice  real, exitDate text, quantity integer  ) ");
        sQLiteDatabase.execSQL("create table my_watches ( service_id text , symbol text , strike real , callPut text , expiry_d text , theoValue real , delta real , gamma real , vega real , theta real , upd_d text, openPrice real, highPrice real, lowPrice real, closePrice real, noOfCntr integer, OI integer, OIChange integer, price_upd_d text, annualisedVolatility real, marketLot integer, pChange real, prevClose real, change real, impliedVolatility real, underlyingValue real, lastPrice real ) ");
        sQLiteDatabase.execSQL("create table strikes ( symbol text , expiry_d text , strike_min real , strike_max real , strike_diff real , upd_d text  ) ");
        createTablePartyCurrent(sQLiteDatabase);
        createTablePartyDayWise(sQLiteDatabase);
        createTableOIStock(sQLiteDatabase);
        createTableBrokerCalls(sQLiteDatabase);
        createTableFcmTopics(sQLiteDatabase);
        createTableInstallId(sQLiteDatabase);
        createTableBlist(sQLiteDatabase);
        createRewardCheck(sQLiteDatabase);
        createRewardCheckTC(sQLiteDatabase);
        createStgAccess(sQLiteDatabase);
        createStgResult(sQLiteDatabase);
        createStgLegs(sQLiteDatabase);
        createFinderStgAccess(sQLiteDatabase);
    }

    public void createTablePartyCurrent(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("create table party_current ( party text , instrument text , long_positions integer, short_positions integer, upd_d text  ) ");
    }

    public void createTablePartyDayWise(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("create table party_daywise ( party text , instrument text , long_positions integer, short_positions integer, upd_d text  ) ");
    }

    public void createTableOIStock(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("create table oi_stock ( symbol text , oi_sum_xx integer, oi_change_xx integer, oi_change_per_xx real, oi_sum_ce integer, oi_change_ce integer, oi_change_per_ce real, oi_sum_pe integer, oi_change_pe integer, oi_change_per_pe real, upd_d text  ) ");
    }

    public void createTableBrokerCalls(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("create table stock_calls ( broker text , symbol text , tip text , sequence integer, url text , sponsored text , create_dt text, update_dt text  ) ");
    }

    public void createTableFcmTopics(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("create table fcm_topics ( topic text   ) ");
    }

    public void createTableInstallId(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("create table app_install ( install_id text  ) ");
    }

    public void createTableBlist(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("create table blist ( blist_flag_e text ,blist_flag_s text ,blist_flag_i text  ) ");
        deleteNinsertBlist(sQLiteDatabase, Constants.BLIST_DEFAULT_CODE);
    }

    public void createRewardCheck(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("create table reward_check ( reward_check_flag text  ) ");
        insertRewardFlag(sQLiteDatabase, Constants.REWARD_CHK_DEFAULT_FLAG);
    }

    public void createRewardCheckTC(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("create table reward_check_tc ( reward_check_tc_flag text  ) ");
        insertRewardTCFlag(sQLiteDatabase, Constants.REWARD_CHK_DEFAULT_FLAG);
    }

    public void createStgAccess(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("create table stgacc ( stgacc_symbol text , stgacc_stg_id integer , stgacc_subkey integer , stgacc_runseqy integer, stgacc_dt inetger , stgacc_pass text , stgacc_s text, stgacc_i text  ) ");
    }

    public void createFinderStgAccess(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("create table finder_stgacc ( finder_stgacc_encrp_end text , finder_stgacc_s text, finder_stgacc_i text  ) ");
    }

    public void createStgResult(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("create table stg_result_save ( strategy_type_cd_i integer, strategy_subkey_i integer, strategy_name_s text , underlyer_c text , net_debit_f real , max_risk_f real , max_gain_f real , breakeven_dn_pt_f real , breakeven_up_pt_f real , net_delta text ,  net_gamma text , net_theta text , net_vega text , upd_d text  ) ");
    }

    public void createStgLegs(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("create table stg_legs_save ( strategy_type_cd_i integer, strategy_subkey_i integer, strategy_leg_seq_i integer, underlyer_c text , action_cd_c text , strike_i real , call_put_stk_cd_c text , expiry_d text, premium_f real , upd_d text  ) ");
    }

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS party_current ( party text , instrument text , long_positions integer, short_positions integer, upd_d text  ) ");
        sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS party_daywise ( party text , instrument text , long_positions integer, short_positions integer, upd_d text  ) ");
        sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS oi_stock ( symbol text , oi_sum_xx integer, oi_change_xx integer, oi_change_per_xx real, oi_sum_ce integer, oi_change_ce integer, oi_change_per_ce real, oi_sum_pe integer, oi_change_pe integer, oi_change_per_pe real, upd_d text  ) ");
        sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS stock_calls ( broker text , symbol text , tip text , sequence integer, url text , sponsored text , create_dt text, update_dt text  ) ");
        sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS fcm_topics ( topic text  ) ");
        sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS app_install ( install_id text  ) ");
        sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS blist ( blist_flag_e text ,blist_flag_s text ,blist_flag_i text  ) ");
        insertBListDefaultIfNA(sQLiteDatabase);
        sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS reward_check ( reward_check_flag text  ) ");
        insertRewardDefaultIfNA(sQLiteDatabase, Constants.REWARD_CHK_DEFAULT_FLAG);
        sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS reward_check_tc ( reward_check_tc_flag text  ) ");
        insertRewardTCDefaultIfNA(sQLiteDatabase, Constants.REWARD_CHK_DEFAULT_FLAG);
        sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS stgacc ( stgacc_symbol text , stgacc_stg_id integer , stgacc_subkey integer , stgacc_runseqy integer, stgacc_dt inetger , stgacc_pass text , stgacc_s text, stgacc_i text  ) ");
        sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS finder_stgacc ( finder_stgacc_encrp_end text , finder_stgacc_s text, finder_stgacc_i text  ) ");
        sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS stg_result_save ( strategy_type_cd_i integer, strategy_subkey_i integer, strategy_name_s text , underlyer_c text , net_debit_f real , max_risk_f real , max_gain_f real , breakeven_dn_pt_f real , breakeven_up_pt_f real , net_delta text ,  net_gamma text , net_theta text , net_vega text , upd_d text  ) ");
        sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS stg_legs_save ( strategy_type_cd_i integer, strategy_subkey_i integer, strategy_leg_seq_i integer, underlyer_c text , action_cd_c text , strike_i real , call_put_stk_cd_c text , expiry_d text, premium_f real , upd_d text  ) ");
    }

    public void onOpen(SQLiteDatabase sQLiteDatabase) {
        subscribeToTopic(sQLiteDatabase, Constants.FCM_TOPIC_APPONE);
        createAndInsertAppId(sQLiteDatabase);
    }

    private void getDatabaseHandler() {
        try {
            if (this.f125db == null) {
                this.f125db = getWritableDatabase();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, (SQLiteDatabase.CursorFactory) null, 9);
        this.appContext = (MyGreeksApplication) context;
    }

    public boolean insertPartyCurrent(List<PartyRec> list) {
        if (this.f125db == null) {
            getDatabaseHandler();
        }
        Iterator<PartyRec> it = list.iterator();
        PartyRec partyRec = null;
        while (it.hasNext()) {
            partyRec = it.next();
            ContentValues contentValues = new ContentValues();
            contentValues.put("party", partyRec.getParty());
            contentValues.put("instrument", partyRec.getInstrument());
            contentValues.put("long_positions", Integer.valueOf(partyRec.getLongPositions()));
            contentValues.put("short_positions", Integer.valueOf(partyRec.getShortPositions()));
            contentValues.put("upd_d", dateFormatter(partyRec.getPriceUpd(), Constants.DT_FMT_yyyy_MM_dd_HH_m_ss));
            this.f125db.insert(PARTY_CURRENT_TABLE_NAME, (String) null, contentValues);
        }
        return partyRec != null;
    }

    public boolean insertPartyDayWise(List<PartyRec> list) {
        if (this.f125db == null) {
            getDatabaseHandler();
        }
        Iterator<PartyRec> it = list.iterator();
        PartyRec partyRec = null;
        while (it.hasNext()) {
            partyRec = it.next();
            ContentValues contentValues = new ContentValues();
            contentValues.put("party", partyRec.getParty());
            contentValues.put("instrument", partyRec.getInstrument());
            contentValues.put("long_positions", Integer.valueOf(partyRec.getLongPositions()));
            contentValues.put("short_positions", Integer.valueOf(partyRec.getShortPositions()));
            contentValues.put("upd_d", dateFormatter(partyRec.getPriceUpd(), Constants.DT_FMT_yyyy_MM_dd_HH_m_ss));
            this.f125db.insert(PARTY_DAYWISE_TABLE_NAME, (String) null, contentValues);
        }
        return partyRec != null;
    }

    public boolean insertOIStocks(List<OIData> list) {
        if (this.f125db == null) {
            getDatabaseHandler();
        }
        Iterator<OIData> it = list.iterator();
        OIData oIData = null;
        while (it.hasNext()) {
            oIData = it.next();
            ContentValues contentValues = new ContentValues();
            contentValues.put("symbol", oIData.getSymbol());
            contentValues.put(OI_STOCK_COLUMN_OI_SUM_XX, Integer.valueOf(oIData.getOiSumXX()));
            contentValues.put(OI_STOCK_COLUMN_OI_CHG_XX, Integer.valueOf(oIData.getOiChangeXX()));
            contentValues.put(OI_STOCK_COLUMN_OI_CHG_PER_XX, Float.valueOf(oIData.getOiChgPerXX()));
            contentValues.put(OI_STOCK_COLUMN_OI_SUM_CE, Integer.valueOf(oIData.getOiSumCE()));
            contentValues.put(OI_STOCK_COLUMN_OI_CHG_CE, Integer.valueOf(oIData.getOiChangeCE()));
            contentValues.put(OI_STOCK_COLUMN_OI_CHG_PER_CE, Float.valueOf(oIData.getOiChgPerCE()));
            contentValues.put(OI_STOCK_COLUMN_OI_SUM_PE, Integer.valueOf(oIData.getOiSumPE()));
            contentValues.put(OI_STOCK_COLUMN_OI_CHG_PE, Integer.valueOf(oIData.getOiChangePE()));
            contentValues.put(OI_STOCK_COLUMN_OI_CHG_PER_PE, Float.valueOf(oIData.getOiChgPerPE()));
            contentValues.put("upd_d", dateFormatter(oIData.getUpdDate(), Constants.DT_FMT_yyyy_MM_dd_HH_m_ss));
            this.f125db.insert(OI_STOCK_TABLE_NAME, (String) null, contentValues);
        }
        return oIData != null;
    }

    public boolean insertBrokerCalls(List<Calls> list) {
        if (this.f125db == null) {
            getDatabaseHandler();
        }
        Iterator<Calls> it = list.iterator();
        Calls calls = null;
        while (it.hasNext()) {
            calls = it.next();
            ContentValues contentValues = new ContentValues();
            contentValues.put(BROKER_CALLS_BROKER, calls.getBrokerName());
            contentValues.put("symbol", calls.getSymbol());
            contentValues.put(BROKER_CALLS_TIP, calls.getCall());
            contentValues.put(BROKER_CALLS_SEQ, Integer.valueOf(calls.getSequence()));
            contentValues.put("url", calls.getURL());
            contentValues.put(BROKER_CALLS_SPONSORED, calls.getSponsored());
            contentValues.put(BROKER_CALLS_CREATEDT, dateFormatter(calls.getGenerateDate(), Constants.DT_FMT_yyyy_MM_dd_HH_m_ss));
            contentValues.put(BROKER_CALLS_UPDDT, dateFormatter(calls.getUpdDate(), Constants.DT_FMT_yyyy_MM_dd_HH_m_ss));
            this.f125db.insert(BROKER_CALLS_TABLE_NAME, (String) null, contentValues);
        }
        return calls != null;
    }

    public int deletePartyCurrent() {
        if (this.f125db == null) {
            getDatabaseHandler();
        }
        return this.f125db.delete(PARTY_CURRENT_TABLE_NAME, (String) null, (String[]) null);
    }

    public int deletePartyDayWise() {
        if (this.f125db == null) {
            getDatabaseHandler();
        }
        return this.f125db.delete(PARTY_DAYWISE_TABLE_NAME, (String) null, (String[]) null);
    }

    public int deleteOIStock() {
        if (this.f125db == null) {
            getDatabaseHandler();
        }
        return this.f125db.delete(OI_STOCK_TABLE_NAME, (String) null, (String[]) null);
    }

    public int deleteBrokerCalls() {
        if (this.f125db == null) {
            getDatabaseHandler();
        }
        return this.f125db.delete(BROKER_CALLS_TABLE_NAME, (String) null, (String[]) null);
    }

    public List<String> getPartyNamesFromCurrent() {
        ArrayList arrayList = new ArrayList();
        if (this.f125db == null) {
            getDatabaseHandler();
        }
        Cursor rawQuery = this.f125db.rawQuery("select distinct party from party_current", (String[]) null);
        rawQuery.moveToFirst();
        while (!rawQuery.isAfterLast()) {
            arrayList.add(rawQuery.getString(rawQuery.getColumnIndex("party")));
            rawQuery.moveToNext();
        }
        if (rawQuery != null) {
            rawQuery.close();
        }
        return arrayList;
    }

    public List<String> getPartyNamesFromDayWise() {
        ArrayList arrayList = new ArrayList();
        if (this.f125db == null) {
            getDatabaseHandler();
        }
        Cursor rawQuery = this.f125db.rawQuery("select distinct party from party_daywise", (String[]) null);
        rawQuery.moveToFirst();
        while (!rawQuery.isAfterLast()) {
            arrayList.add(rawQuery.getString(rawQuery.getColumnIndex("party")));
            rawQuery.moveToNext();
        }
        if (rawQuery != null) {
            rawQuery.close();
        }
        return arrayList;
    }

    public List<String> getIntrumentNamesFromCurrent() {
        ArrayList arrayList = new ArrayList();
        if (this.f125db == null) {
            getDatabaseHandler();
        }
        Cursor rawQuery = this.f125db.rawQuery("select distinct instrument from party_current", (String[]) null);
        rawQuery.moveToFirst();
        while (!rawQuery.isAfterLast()) {
            arrayList.add(rawQuery.getString(rawQuery.getColumnIndex("instrument")));
            rawQuery.moveToNext();
        }
        if (rawQuery != null) {
            rawQuery.close();
        }
        return arrayList;
    }

    public List<String> getIntrumentNamesFromDayWise() {
        ArrayList arrayList = new ArrayList();
        if (this.f125db == null) {
            getDatabaseHandler();
        }
        Cursor rawQuery = this.f125db.rawQuery("select distinct instrument from party_daywise", (String[]) null);
        rawQuery.moveToFirst();
        while (!rawQuery.isAfterLast()) {
            arrayList.add(rawQuery.getString(rawQuery.getColumnIndex("instrument")));
            rawQuery.moveToNext();
        }
        if (rawQuery != null) {
            rawQuery.close();
        }
        return arrayList;
    }

    public List<PartyRec> getPartyCurrentOI(String str) {
        ArrayList arrayList = new ArrayList();
        if (this.f125db == null) {
            getDatabaseHandler();
        }
        Cursor rawQuery = this.f125db.rawQuery("select * from party_current where upper(instrument) = upper('" + str + "')", (String[]) null);
        rawQuery.moveToFirst();
        while (!rawQuery.isAfterLast()) {
            PartyRec partyRec = new PartyRec();
            partyRec.setParty(rawQuery.getString(rawQuery.getColumnIndex("party")));
            partyRec.setInstrument(rawQuery.getString(rawQuery.getColumnIndex("instrument")));
            partyRec.setLongPositions(rawQuery.getInt(rawQuery.getColumnIndex("long_positions")));
            partyRec.setShortPositions(rawQuery.getInt(rawQuery.getColumnIndex("short_positions")));
            partyRec.setPriceUpd(dateFormatter(rawQuery.getString(rawQuery.getColumnIndex("upd_d")), Constants.DT_FMT_yyyy_MM_dd_HH_m_ss));
            arrayList.add(partyRec);
            rawQuery.moveToNext();
        }
        if (rawQuery != null) {
            rawQuery.close();
        }
        return arrayList;
    }

    public List<PartyRec> getPartyDayWiseOI(String str, String str2) {
        ArrayList arrayList = new ArrayList();
        if (this.f125db == null) {
            getDatabaseHandler();
        }
        Cursor rawQuery = this.f125db.rawQuery("select * from party_daywise where upper(instrument) = upper('" + str + "') and upper(" + "party" + ") = upper('" + str2 + "') order by " + "upd_d" + " DESC ", (String[]) null);
        rawQuery.moveToFirst();
        while (!rawQuery.isAfterLast()) {
            PartyRec partyRec = new PartyRec();
            partyRec.setParty(rawQuery.getString(rawQuery.getColumnIndex("party")));
            partyRec.setInstrument(rawQuery.getString(rawQuery.getColumnIndex("instrument")));
            partyRec.setLongPositions(rawQuery.getInt(rawQuery.getColumnIndex("long_positions")));
            partyRec.setShortPositions(rawQuery.getInt(rawQuery.getColumnIndex("short_positions")));
            partyRec.setPriceUpd(dateFormatter(rawQuery.getString(rawQuery.getColumnIndex("upd_d")), Constants.DT_FMT_yyyy_MM_dd_HH_m_ss));
            arrayList.add(partyRec);
            rawQuery.moveToNext();
        }
        if (rawQuery != null) {
            rawQuery.close();
        }
        return arrayList;
    }

    public List<OIData> getOIDataForAllStocks() {
        ArrayList arrayList = new ArrayList();
        if (this.f125db == null) {
            getDatabaseHandler();
        }
        Cursor rawQuery = this.f125db.rawQuery("select * from oi_stock order by symbol", (String[]) null);
        rawQuery.moveToFirst();
        while (!rawQuery.isAfterLast()) {
            OIData oIData = new OIData();
            oIData.setSymbol(rawQuery.getString(rawQuery.getColumnIndex("symbol")));
            oIData.setOiSumXX(rawQuery.getInt(rawQuery.getColumnIndex(OI_STOCK_COLUMN_OI_SUM_XX)));
            oIData.setOiChangeXX(rawQuery.getInt(rawQuery.getColumnIndex(OI_STOCK_COLUMN_OI_CHG_XX)));
            oIData.setOiChgPerXX(rawQuery.getFloat(rawQuery.getColumnIndex(OI_STOCK_COLUMN_OI_CHG_PER_XX)));
            oIData.setOiSumCE(rawQuery.getInt(rawQuery.getColumnIndex(OI_STOCK_COLUMN_OI_SUM_CE)));
            oIData.setOiChangeCE(rawQuery.getInt(rawQuery.getColumnIndex(OI_STOCK_COLUMN_OI_CHG_CE)));
            oIData.setOiChgPerCE(rawQuery.getFloat(rawQuery.getColumnIndex(OI_STOCK_COLUMN_OI_CHG_PER_CE)));
            oIData.setOiSumPE(rawQuery.getInt(rawQuery.getColumnIndex(OI_STOCK_COLUMN_OI_SUM_PE)));
            oIData.setOiChangePE(rawQuery.getInt(rawQuery.getColumnIndex(OI_STOCK_COLUMN_OI_CHG_PE)));
            oIData.setOiChgPerPE(rawQuery.getFloat(rawQuery.getColumnIndex(OI_STOCK_COLUMN_OI_CHG_PER_PE)));
            oIData.setUpdDate(dateFormatter(rawQuery.getString(rawQuery.getColumnIndex("upd_d")), Constants.DT_FMT_yyyy_MM_dd_HH_m_ss));
            arrayList.add(oIData);
            rawQuery.moveToNext();
        }
        if (rawQuery != null) {
            rawQuery.close();
        }
        return arrayList;
    }

    public List<Calls> getBrokerCallsData() {
        ArrayList arrayList = new ArrayList();
        if (this.f125db == null) {
            getDatabaseHandler();
        }
        Cursor rawQuery = this.f125db.rawQuery("select * from stock_calls order by date(create_dt) DESC , sequence DESC", (String[]) null);
        rawQuery.moveToFirst();
        while (!rawQuery.isAfterLast()) {
            String string = rawQuery.getString(rawQuery.getColumnIndex(BROKER_CALLS_BROKER));
            String string2 = rawQuery.getString(rawQuery.getColumnIndex("symbol"));
            String string3 = rawQuery.getString(rawQuery.getColumnIndex(BROKER_CALLS_TIP));
            int i = rawQuery.getInt(rawQuery.getColumnIndex(BROKER_CALLS_SEQ));
            arrayList.add(new Calls(dateFormatter(rawQuery.getString(rawQuery.getColumnIndex(BROKER_CALLS_CREATEDT)), Constants.DT_FMT_yyyy_MM_dd_HH_m_ss), string, rawQuery.getString(rawQuery.getColumnIndex("url")), i, rawQuery.getString(rawQuery.getColumnIndex(BROKER_CALLS_SPONSORED)), string2, string3, dateFormatter(rawQuery.getString(rawQuery.getColumnIndex(BROKER_CALLS_UPDDT)), Constants.DT_FMT_yyyy_MM_dd_HH_m_ss)));
            rawQuery.moveToNext();
        }
        if (rawQuery != null) {
            rawQuery.close();
        }
        return arrayList;
    }

    public boolean insertAllGreeks(List<GreekValues> list, String str) {
        if (this.f125db == null) {
            getDatabaseHandler();
        }
        Iterator<GreekValues> it = list.iterator();
        GreekValues greekValues = null;
        while (it.hasNext()) {
            greekValues = it.next();
            ContentValues contentValues = new ContentValues();
            contentValues.put("service_id", str);
            contentValues.put("symbol", greekValues.getSymbol());
            contentValues.put("strike", Float.valueOf(greekValues.getStrike()));
            contentValues.put("callPut", greekValues.getCallPut());
            contentValues.put("expiry_d", dateFormatter(greekValues.getExpiry_d(), Constants.DT_FMT_yyyy_MM_dd_HH_m_ss));
            contentValues.put("theoValue", Float.valueOf(greekValues.getTheoValue()));
            contentValues.put("delta", Float.valueOf(greekValues.getDelta()));
            contentValues.put("gamma", Float.valueOf(greekValues.getGamma()));
            contentValues.put("vega", Float.valueOf(greekValues.getVega()));
            contentValues.put("theta", Float.valueOf(greekValues.getTheta()));
            contentValues.put("upd_d", dateFormatter(greekValues.getUpd_d(), Constants.DT_FMT_yyyy_MM_dd_HH_m_ss));
            contentValues.put("openPrice", Float.valueOf(greekValues.getOpenPrice()));
            contentValues.put("highPrice", Float.valueOf(greekValues.getHighPrice()));
            contentValues.put("lowPrice", Float.valueOf(greekValues.getLowPrice()));
            contentValues.put("closePrice", Float.valueOf(greekValues.getClosePrice()));
            contentValues.put("OI", Integer.valueOf(greekValues.getOI()));
            contentValues.put("OIChange", Integer.valueOf(greekValues.getOIChange()));
            contentValues.put("price_upd_d", dateFormatter(greekValues.getPrice_upd_d(), Constants.DT_FMT_yyyy_MM_dd_HH_m_ss));
            contentValues.put("annualisedVolatility", Float.valueOf(greekValues.getAnnualisedVolatility()));
            contentValues.put("marketLot", Integer.valueOf(greekValues.getMarketLot()));
            contentValues.put("pChange", Float.valueOf(greekValues.getpChange()));
            contentValues.put("prevClose", Float.valueOf(greekValues.getPrevClose()));
            contentValues.put("change", Float.valueOf(greekValues.getChange()));
            contentValues.put("impliedVolatility", Float.valueOf(greekValues.getImpliedVolatility()));
            contentValues.put("underlyingValue", Float.valueOf(greekValues.getUnderlyingValue()));
            contentValues.put("lastPrice", Float.valueOf(greekValues.getLastPrice()));
            this.f125db.insert(GREEKS_TABLE_NAME, (String) null, contentValues);
        }
        return greekValues != null;
    }

    public boolean isOptionAlreadyInPortfolio(String str, Date date, float f, String str2) {
        if (this.f125db == null) {
            getDatabaseHandler();
        }
        String dateFormatter = dateFormatter(date, Constants.DT_FMT_yyyy_MM_dd_HH_m_ss);
        Cursor rawQuery = this.f125db.rawQuery(" select count(*)  from  portfolio  where symbol = '" + str + "' and " + "expiry_d" + " = '" + dateFormatter + "' and " + "strike" + " = " + f + " and " + FOLIO_COLUMN_EXIT_DATE + " is null and " + "callPut" + " = '" + str2 + "' ", (String[]) null);
        rawQuery.moveToFirst();
        int i = rawQuery.getInt(0);
        if (rawQuery != null) {
            rawQuery.close();
        }
        if (i == 1) {
            return true;
        }
        return false;
    }

    public boolean isOptionAlreadyInWatch(String str, Date date, float f, String str2) {
        if (this.f125db == null) {
            getDatabaseHandler();
        }
        String dateFormatter = dateFormatter(date, Constants.DT_FMT_yyyy_MM_dd_HH_m_ss);
        Cursor rawQuery = this.f125db.rawQuery(" select count(*)  from  my_watches  where symbol = '" + str + "' and " + "expiry_d" + " = '" + dateFormatter + "' and " + "strike" + " = " + f + " and " + "callPut" + " = '" + str2 + "' ", (String[]) null);
        rawQuery.moveToFirst();
        int i = rawQuery.getInt(0);
        if (rawQuery != null) {
            rawQuery.close();
        }
        if (i == 1) {
            return true;
        }
        return false;
    }

    public boolean insertPortfolioItem(GreekValues greekValues) {
        if (this.f125db == null) {
            getDatabaseHandler();
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("service_id", 5);
        contentValues.put("symbol", greekValues.getSymbol());
        contentValues.put("strike", Float.valueOf(greekValues.getStrike()));
        contentValues.put("callPut", greekValues.getCallPut());
        contentValues.put("expiry_d", dateFormatter(greekValues.getExpiry_d(), Constants.DT_FMT_yyyy_MM_dd_HH_m_ss));
        contentValues.put("theoValue", Float.valueOf(greekValues.getTheoValue()));
        contentValues.put("delta", Float.valueOf(greekValues.getDelta()));
        contentValues.put("gamma", Float.valueOf(greekValues.getGamma()));
        contentValues.put("vega", Float.valueOf(greekValues.getVega()));
        contentValues.put("theta", Float.valueOf(greekValues.getTheta()));
        contentValues.put("upd_d", dateFormatter(greekValues.getUpd_d(), Constants.DT_FMT_yyyy_MM_dd_HH_m_ss));
        contentValues.put("openPrice", Float.valueOf(greekValues.getOpenPrice()));
        contentValues.put("highPrice", Float.valueOf(greekValues.getHighPrice()));
        contentValues.put("lowPrice", Float.valueOf(greekValues.getLowPrice()));
        contentValues.put("closePrice", Float.valueOf(greekValues.getClosePrice()));
        contentValues.put("OI", Integer.valueOf(greekValues.getOI()));
        contentValues.put("OIChange", Integer.valueOf(greekValues.getOIChange()));
        contentValues.put("price_upd_d", dateFormatter(greekValues.getPrice_upd_d(), Constants.DT_FMT_yyyy_MM_dd_HH_m_ss));
        contentValues.put("annualisedVolatility", Float.valueOf(greekValues.getAnnualisedVolatility()));
        contentValues.put("marketLot", Integer.valueOf(greekValues.getMarketLot()));
        contentValues.put("pChange", Float.valueOf(greekValues.getpChange()));
        contentValues.put("prevClose", Float.valueOf(greekValues.getPrevClose()));
        contentValues.put("change", Float.valueOf(greekValues.getpChange()));
        contentValues.put("impliedVolatility", Float.valueOf(greekValues.getImpliedVolatility()));
        contentValues.put("underlyingValue", Float.valueOf(greekValues.getUnderlyingValue()));
        contentValues.put("lastPrice", Float.valueOf(greekValues.getLastPrice()));
        contentValues.put(FOLIO_COLUMN_LONGSHORT, greekValues.getLongShort());
        contentValues.put(FOLIO_COLUMN_ENTRY_PRICE, Float.valueOf(greekValues.getEntryPrice()));
        contentValues.put(FOLIO_COLUMN_ENTRY_DATE, dateFormatter(greekValues.getEntryDate(), Constants.DT_FMT_yyyy_MM_dd_HH_m_ss));
        if (greekValues.getExitPrice() != 0.0f) {
            contentValues.put(FOLIO_COLUMN_EXIT_PRICE, Float.valueOf(greekValues.getExitPrice()));
            contentValues.put(FOLIO_COLUMN_EXIT_DATE, dateFormatter(greekValues.getExitDate(), Constants.DT_FMT_yyyy_MM_dd_HH_m_ss));
        }
        contentValues.put(FOLIO_COLUMN_QUANTITY, Integer.valueOf(greekValues.getQuantity()));
        try {
            this.f125db.insertOrThrow(FOLIO_TABLE_NAME, (String) null, contentValues);
            return true;
        } catch (SQLException e) {
            String.valueOf(e.getMessage());
            return true;
        }
    }

    public boolean insertWatchItem(GreekValues greekValues) {
        if (this.f125db == null) {
            getDatabaseHandler();
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("service_id", 5);
        contentValues.put("symbol", greekValues.getSymbol());
        contentValues.put("strike", Float.valueOf(greekValues.getStrike()));
        contentValues.put("callPut", greekValues.getCallPut());
        contentValues.put("expiry_d", dateFormatter(greekValues.getExpiry_d(), Constants.DT_FMT_yyyy_MM_dd_HH_m_ss));
        contentValues.put("theoValue", Float.valueOf(greekValues.getTheoValue()));
        contentValues.put("delta", Float.valueOf(greekValues.getDelta()));
        contentValues.put("gamma", Float.valueOf(greekValues.getGamma()));
        contentValues.put("vega", Float.valueOf(greekValues.getVega()));
        contentValues.put("theta", Float.valueOf(greekValues.getTheta()));
        contentValues.put("upd_d", dateFormatter(greekValues.getUpd_d(), Constants.DT_FMT_yyyy_MM_dd_HH_m_ss));
        contentValues.put("openPrice", Float.valueOf(greekValues.getOpenPrice()));
        contentValues.put("highPrice", Float.valueOf(greekValues.getHighPrice()));
        contentValues.put("lowPrice", Float.valueOf(greekValues.getLowPrice()));
        contentValues.put("closePrice", Float.valueOf(greekValues.getClosePrice()));
        contentValues.put("OI", Integer.valueOf(greekValues.getOI()));
        contentValues.put("OIChange", Integer.valueOf(greekValues.getOIChange()));
        contentValues.put("price_upd_d", dateFormatter(greekValues.getPrice_upd_d(), Constants.DT_FMT_yyyy_MM_dd_HH_m_ss));
        contentValues.put("annualisedVolatility", Float.valueOf(greekValues.getAnnualisedVolatility()));
        contentValues.put("marketLot", Integer.valueOf(greekValues.getMarketLot()));
        contentValues.put("pChange", Float.valueOf(greekValues.getpChange()));
        contentValues.put("prevClose", Float.valueOf(greekValues.getPrevClose()));
        contentValues.put("change", Float.valueOf(greekValues.getChange()));
        contentValues.put("impliedVolatility", Float.valueOf(greekValues.getImpliedVolatility()));
        contentValues.put("underlyingValue", Float.valueOf(greekValues.getUnderlyingValue()));
        contentValues.put("lastPrice", Float.valueOf(greekValues.getLastPrice()));
        try {
            this.f125db.insertOrThrow(WATCHES_TABLE_NAME, (String) null, contentValues);
            return true;
        } catch (SQLException e) {
            String.valueOf(e.getMessage());
            return false;
        }
    }

    public int insertAllWatchItem(List<GreekValues> list) {
        if (this.f125db == null) {
            getDatabaseHandler();
        }
        int i = 0;
        for (GreekValues next : list) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("service_id", 5);
            contentValues.put("symbol", next.getSymbol());
            contentValues.put("strike", Float.valueOf(next.getStrike()));
            contentValues.put("callPut", next.getCallPut());
            contentValues.put("expiry_d", dateFormatter(next.getExpiry_d(), Constants.DT_FMT_yyyy_MM_dd_HH_m_ss));
            contentValues.put("theoValue", Float.valueOf(next.getTheoValue()));
            contentValues.put("delta", Float.valueOf(next.getDelta()));
            contentValues.put("gamma", Float.valueOf(next.getGamma()));
            contentValues.put("vega", Float.valueOf(next.getVega()));
            contentValues.put("theta", Float.valueOf(next.getTheta()));
            contentValues.put("upd_d", dateFormatter(next.getUpd_d(), Constants.DT_FMT_yyyy_MM_dd_HH_m_ss));
            contentValues.put("openPrice", Float.valueOf(next.getOpenPrice()));
            contentValues.put("highPrice", Float.valueOf(next.getHighPrice()));
            contentValues.put("lowPrice", Float.valueOf(next.getLowPrice()));
            contentValues.put("closePrice", Float.valueOf(next.getClosePrice()));
            contentValues.put("OI", Integer.valueOf(next.getOI()));
            contentValues.put("OIChange", Integer.valueOf(next.getOIChange()));
            contentValues.put("price_upd_d", dateFormatter(next.getPrice_upd_d(), Constants.DT_FMT_yyyy_MM_dd_HH_m_ss));
            contentValues.put("annualisedVolatility", Float.valueOf(next.getAnnualisedVolatility()));
            contentValues.put("marketLot", Integer.valueOf(next.getMarketLot()));
            contentValues.put("pChange", Float.valueOf(next.getpChange()));
            contentValues.put("prevClose", Float.valueOf(next.getPrevClose()));
            contentValues.put("change", Float.valueOf(next.getChange()));
            contentValues.put("impliedVolatility", Float.valueOf(next.getImpliedVolatility()));
            contentValues.put("underlyingValue", Float.valueOf(next.getUnderlyingValue()));
            contentValues.put("lastPrice", Float.valueOf(next.getLastPrice()));
            i++;
            try {
                this.f125db.insertOrThrow(WATCHES_TABLE_NAME, (String) null, contentValues);
            } catch (SQLException e) {
                String.valueOf(e.getMessage());
            }
        }
        return i;
    }

    public String getMonthExpiryDate(int i) {
        if (this.f125db == null) {
            getDatabaseHandler();
        }
        Cursor rawQuery = this.f125db.rawQuery("select min( expiry_d)  from  daily_greeks", (String[]) null);
        rawQuery.moveToFirst();
        String string = rawQuery.getString(rawQuery.getColumnIndex("expiry_d"));
        if (i != 2) {
            return string;
        }
        SQLiteDatabase sQLiteDatabase = this.f125db;
        Cursor rawQuery2 = sQLiteDatabase.rawQuery("select min( expiry_d)  from  daily_greeks where expiry_d > '" + string + "'", (String[]) null);
        return rawQuery2.getString(rawQuery2.getColumnIndex("expiry_d"));
    }

    public String getGreeksUpdateDate(String str) {
        if (this.f125db == null) {
            getDatabaseHandler();
        }
        SQLiteDatabase sQLiteDatabase = this.f125db;
        Cursor rawQuery = sQLiteDatabase.rawQuery("select max( upd_d )  from  daily_greeks where service_id = '" + str + "'", (String[]) null);
        rawQuery.moveToFirst();
        String string = rawQuery.getString(0);
        if (rawQuery != null) {
            rawQuery.close();
        }
        return string;
    }

    public String getPartyCurrentUpdateDate() {
        if (this.f125db == null) {
            getDatabaseHandler();
        }
        Cursor rawQuery = this.f125db.rawQuery("select max( upd_d )  from  party_current", (String[]) null);
        rawQuery.moveToFirst();
        String string = rawQuery.getString(0);
        if (rawQuery != null) {
            rawQuery.close();
        }
        return string;
    }

    public String getDayWiseUpdateDate() {
        if (this.f125db == null) {
            getDatabaseHandler();
        }
        Cursor rawQuery = this.f125db.rawQuery("select max( upd_d )  from  party_daywise", (String[]) null);
        rawQuery.moveToFirst();
        String string = rawQuery.getString(0);
        if (rawQuery != null) {
            rawQuery.close();
        }
        return string;
    }

    public String getOIStockUpdateDate() {
        if (this.f125db == null) {
            getDatabaseHandler();
        }
        Cursor rawQuery = this.f125db.rawQuery("select max( upd_d )  from  oi_stock", (String[]) null);
        rawQuery.moveToFirst();
        String string = rawQuery.getString(0);
        if (rawQuery != null) {
            rawQuery.close();
        }
        return string;
    }

    public String getBrokerCallsUpdateDate() {
        if (this.f125db == null) {
            getDatabaseHandler();
        }
        Cursor rawQuery = this.f125db.rawQuery("select max( update_dt )  from  stock_calls", (String[]) null);
        rawQuery.moveToFirst();
        String string = rawQuery.getString(0);
        if (rawQuery != null) {
            rawQuery.close();
        }
        return string;
    }

    public String getPriceUpdateDate(String str) {
        if (this.f125db == null) {
            getDatabaseHandler();
        }
        SQLiteDatabase sQLiteDatabase = this.f125db;
        Cursor rawQuery = sQLiteDatabase.rawQuery("select max( price_upd_d )  from  daily_greeks where service_id = '" + str + "'", (String[]) null);
        rawQuery.moveToFirst();
        String string = rawQuery.getString(0);
        if (rawQuery != null) {
            rawQuery.close();
        }
        return string;
    }

    public int deleteAllGreeks(String str) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        this.f125db = writableDatabase;
        return writableDatabase.delete(GREEKS_TABLE_NAME, "service_id = ? ", new String[]{str});
    }

    public int deleteFolioItemFromOpenPos(GreekValues greekValues) {
        this.f125db = getWritableDatabase();
        return this.f125db.delete(FOLIO_TABLE_NAME, "symbol = '" + greekValues.getSymbol() + "' and " + "strike" + " = " + greekValues.getStrike() + " and " + "callPut" + " = '" + greekValues.getCallPut() + "' and " + FOLIO_COLUMN_EXIT_DATE + " is " + " null " + " and  " + "expiry_d" + " = '" + dateFormatter(greekValues.getExpiry_d(), Constants.DT_FMT_yyyy_MM_dd_HH_m_ss) + "' ", (String[]) null);
    }

    public int deleteFolioItemFromClosedPos(GreekValues greekValues) {
        this.f125db = getWritableDatabase();
        return this.f125db.delete(FOLIO_TABLE_NAME, "symbol = '" + greekValues.getSymbol() + "' and " + "strike" + " = " + greekValues.getStrike() + " and " + "callPut" + " = '" + greekValues.getCallPut() + "' and " + FOLIO_COLUMN_EXIT_DATE + " = '" + dateFormatter(greekValues.getExitDate(), Constants.DT_FMT_yyyy_MM_dd_HH_m_ss) + "' and  " + "expiry_d" + " = '" + dateFormatter(greekValues.getExpiry_d(), Constants.DT_FMT_yyyy_MM_dd_HH_m_ss) + "' ", (String[]) null);
    }

    public int deleteWatchItem(GreekValues greekValues) {
        this.f125db = getWritableDatabase();
        return this.f125db.delete(WATCHES_TABLE_NAME, "symbol = '" + greekValues.getSymbol() + "' and " + "strike" + " = " + greekValues.getStrike() + " and " + "callPut" + " = '" + greekValues.getCallPut() + "' and " + "expiry_d" + " = '" + dateFormatter(greekValues.getExpiry_d(), Constants.DT_FMT_yyyy_MM_dd_HH_m_ss) + "' ", (String[]) null);
    }

    public boolean updateFolioPrices(List<GreekSearchCriteriaFields> list) {
        if (this.f125db == null) {
            getDatabaseHandler();
        }
        for (GreekSearchCriteriaFields next : list) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("price_upd_d", dateFormatter(next.getPriceUpd(), Constants.DT_FMT_yyyy_MM_dd_HH_m_ss));
            contentValues.put("lastPrice", Float.valueOf(next.getLastPrice()));
            SQLiteDatabase sQLiteDatabase = this.f125db;
            sQLiteDatabase.update(FOLIO_TABLE_NAME, contentValues, "symbol = '" + next.getStock() + "' and " + "strike" + " = " + next.getStrikeFrom() + " and " + "callPut" + " = '" + next.getCallOrPut() + "' and " + FOLIO_COLUMN_EXIT_DATE + " is null and " + "expiry_d" + " = '" + dateFormatter(next.getExpiryDate(), Constants.DT_FMT_yyyy_MM_dd_HH_m_ss) + "'", (String[]) null);
        }
        return true;
    }

    public boolean closeFolioPoistion(GreekValues greekValues) {
        if (this.f125db == null) {
            getDatabaseHandler();
        }
        if (greekValues == null) {
            return true;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(FOLIO_COLUMN_EXIT_DATE, dateFormatter(new Date(greekValues.getExitDate().getTime() + (new Date().getTime() % Constants.SERVER_CHECK_INTERVAL)), Constants.DT_FMT_yyyy_MM_dd_HH_m_ss));
        contentValues.put(FOLIO_COLUMN_EXIT_PRICE, Float.valueOf(greekValues.getExitPrice()));
        SQLiteDatabase sQLiteDatabase = this.f125db;
        sQLiteDatabase.update(FOLIO_TABLE_NAME, contentValues, "symbol = '" + greekValues.getSymbol() + "' and " + "strike" + " = " + greekValues.getStrike() + " and " + "callPut" + " = '" + greekValues.getCallPut() + "' and " + FOLIO_COLUMN_EXIT_DATE + " is null and " + "expiry_d" + " = '" + dateFormatter(greekValues.getExpiry_d(), Constants.DT_FMT_yyyy_MM_dd_HH_m_ss) + "'", (String[]) null);
        return true;
    }

    public List<GreekValues> getPortfolioItems(int i) {
        String str = i == 0 ? " not null " : "null";
        ArrayList arrayList = new ArrayList();
        if (this.f125db == null) {
            getDatabaseHandler();
        }
        SQLiteDatabase sQLiteDatabase = this.f125db;
        Cursor rawQuery = sQLiteDatabase.rawQuery("select * from portfolio where exitPrice is " + str, (String[]) null);
        rawQuery.moveToFirst();
        while (!rawQuery.isAfterLast()) {
            String string = rawQuery.getString(rawQuery.getColumnIndex("symbol"));
            float f = rawQuery.getFloat(rawQuery.getColumnIndex("strike"));
            String string2 = rawQuery.getString(rawQuery.getColumnIndex("callPut"));
            Date dateFormatter = dateFormatter(rawQuery.getString(rawQuery.getColumnIndex("expiry_d")), Constants.DT_FMT_yyyy_MM_dd_HH_m_ss);
            float f2 = rawQuery.getFloat(rawQuery.getColumnIndex("theoValue"));
            float f3 = rawQuery.getFloat(rawQuery.getColumnIndex("delta"));
            float f4 = rawQuery.getFloat(rawQuery.getColumnIndex("gamma"));
            float f5 = rawQuery.getFloat(rawQuery.getColumnIndex("vega"));
            float f6 = rawQuery.getFloat(rawQuery.getColumnIndex("theta"));
            Date dateFormatter2 = dateFormatter(rawQuery.getString(rawQuery.getColumnIndex("upd_d")), Constants.DT_FMT_yyyy_MM_dd_HH_m_ss);
            ArrayList arrayList2 = arrayList;
            String str2 = Constants.DT_FMT_yyyy_MM_dd_HH_m_ss;
            GreekValues greekValues = r4;
            GreekValues greekValues2 = new GreekValues(string, f, string2, dateFormatter, f2, f3, f4, f5, f6, dateFormatter2, rawQuery.getFloat(rawQuery.getColumnIndex("openPrice")), rawQuery.getFloat(rawQuery.getColumnIndex("highPrice")), rawQuery.getFloat(rawQuery.getColumnIndex("lowPrice")), rawQuery.getFloat(rawQuery.getColumnIndex("closePrice")), rawQuery.getInt(rawQuery.getColumnIndex("noOfCntr")), rawQuery.getInt(rawQuery.getColumnIndex("OI")), rawQuery.getInt(rawQuery.getColumnIndex("OIChange")), dateFormatter(rawQuery.getString(rawQuery.getColumnIndex("price_upd_d")), str2), rawQuery.getFloat(rawQuery.getColumnIndex("annualisedVolatility")), rawQuery.getInt(rawQuery.getColumnIndex("marketLot")), rawQuery.getFloat(rawQuery.getColumnIndex("pChange")), rawQuery.getFloat(rawQuery.getColumnIndex("prevClose")), rawQuery.getFloat(rawQuery.getColumnIndex("change")), rawQuery.getFloat(rawQuery.getColumnIndex("impliedVolatility")), rawQuery.getFloat(rawQuery.getColumnIndex("underlyingValue")), rawQuery.getFloat(rawQuery.getColumnIndex("lastPrice")));
            GreekValues greekValues3 = greekValues;
            greekValues3.setLongShort(rawQuery.getString(rawQuery.getColumnIndex(FOLIO_COLUMN_LONGSHORT)));
            greekValues3.setEntryPrice(rawQuery.getFloat(rawQuery.getColumnIndex(FOLIO_COLUMN_ENTRY_PRICE)));
            greekValues3.setEntryDate(dateFormatter(rawQuery.getString(rawQuery.getColumnIndex(FOLIO_COLUMN_ENTRY_DATE)), str2));
            greekValues3.setExitPrice(rawQuery.getFloat(rawQuery.getColumnIndex(FOLIO_COLUMN_EXIT_PRICE)));
            greekValues3.setExitDate(dateFormatter(rawQuery.getString(rawQuery.getColumnIndex(FOLIO_COLUMN_EXIT_DATE)), str2));
            greekValues3.setQuantity(rawQuery.getInt(rawQuery.getColumnIndex(FOLIO_COLUMN_QUANTITY)));
            arrayList = arrayList2;
            arrayList.add(greekValues3);
            rawQuery.moveToNext();
        }
        if (rawQuery != null) {
            rawQuery.close();
        }
        return arrayList;
    }

    public List getGreekForAnOption(String str, String str2, float f, String str3) {
        ArrayList arrayList = new ArrayList();
        if (this.f125db == null) {
            getDatabaseHandler();
        }
        SQLiteDatabase sQLiteDatabase = this.f125db;
        Cursor rawQuery = sQLiteDatabase.rawQuery("select * from daily_greeks where symbol = '" + str + "' and " + "expiry_d" + " = '" + str2 + "' and " + "strike" + " = " + f + " and " + "callPut" + " = '" + str3 + "' ", (String[]) null);
        rawQuery.moveToFirst();
        if (!rawQuery.isAfterLast()) {
            arrayList.add(new GreekValues(rawQuery.getString(rawQuery.getColumnIndex("symbol")), rawQuery.getFloat(rawQuery.getColumnIndex("strike")), rawQuery.getString(rawQuery.getColumnIndex("callPut")), dateFormatter(rawQuery.getString(rawQuery.getColumnIndex("expiry_d")), Constants.DT_FMT_yyyy_MM_dd_HH_m_ss), rawQuery.getFloat(rawQuery.getColumnIndex("theoValue")), rawQuery.getFloat(rawQuery.getColumnIndex("delta")), rawQuery.getFloat(rawQuery.getColumnIndex("gamma")), rawQuery.getFloat(rawQuery.getColumnIndex("vega")), rawQuery.getFloat(rawQuery.getColumnIndex("theta")), dateFormatter(rawQuery.getString(rawQuery.getColumnIndex("upd_d")), Constants.DT_FMT_yyyy_MM_dd_HH_m_ss), rawQuery.getFloat(rawQuery.getColumnIndex("openPrice")), rawQuery.getFloat(rawQuery.getColumnIndex("highPrice")), rawQuery.getFloat(rawQuery.getColumnIndex("lowPrice")), rawQuery.getFloat(rawQuery.getColumnIndex("closePrice")), rawQuery.getInt(rawQuery.getColumnIndex("noOfCntr")), rawQuery.getInt(rawQuery.getColumnIndex("OI")), rawQuery.getInt(rawQuery.getColumnIndex("OIChange")), dateFormatter(rawQuery.getString(rawQuery.getColumnIndex("price_upd_d")), Constants.DT_FMT_yyyy_MM_dd_HH_m_ss), rawQuery.getFloat(rawQuery.getColumnIndex("annualisedVolatility")), rawQuery.getInt(rawQuery.getColumnIndex("marketLot")), rawQuery.getFloat(rawQuery.getColumnIndex("pChange")), rawQuery.getFloat(rawQuery.getColumnIndex("prevClose")), rawQuery.getFloat(rawQuery.getColumnIndex("change")), rawQuery.getFloat(rawQuery.getColumnIndex("impliedVolatility")), rawQuery.getFloat(rawQuery.getColumnIndex("underlyingValue")), rawQuery.getFloat(rawQuery.getColumnIndex("lastPrice"))));
        }
        if (rawQuery != null) {
            rawQuery.close();
        }
        return arrayList;
    }

    public List<GreekValues> getAllGreeks(String str) {
        ArrayList arrayList = new ArrayList();
        if (this.f125db == null) {
            getDatabaseHandler();
        }
        SQLiteDatabase sQLiteDatabase = this.f125db;
        Cursor rawQuery = sQLiteDatabase.rawQuery("select * from daily_greeks where service_id = '" + str + "'", (String[]) null);
        rawQuery.moveToFirst();
        while (!rawQuery.isAfterLast()) {
            String string = rawQuery.getString(rawQuery.getColumnIndex("symbol"));
            float f = rawQuery.getFloat(rawQuery.getColumnIndex("strike"));
            String string2 = rawQuery.getString(rawQuery.getColumnIndex("callPut"));
            Date dateFormatter = dateFormatter(rawQuery.getString(rawQuery.getColumnIndex("expiry_d")), Constants.DT_FMT_yyyy_MM_dd_HH_m_ss);
            float f2 = rawQuery.getFloat(rawQuery.getColumnIndex("theoValue"));
            float f3 = rawQuery.getFloat(rawQuery.getColumnIndex("delta"));
            float f4 = rawQuery.getFloat(rawQuery.getColumnIndex("gamma"));
            float f5 = rawQuery.getFloat(rawQuery.getColumnIndex("vega"));
            float f6 = rawQuery.getFloat(rawQuery.getColumnIndex("theta"));
            Date dateFormatter2 = dateFormatter(rawQuery.getString(rawQuery.getColumnIndex("upd_d")), Constants.DT_FMT_yyyy_MM_dd_HH_m_ss);
            ArrayList arrayList2 = arrayList;
            String str2 = Constants.DT_FMT_yyyy_MM_dd_HH_m_ss;
            GreekValues greekValues = r4;
            GreekValues greekValues2 = new GreekValues(string, f, string2, dateFormatter, f2, f3, f4, f5, f6, dateFormatter2, rawQuery.getFloat(rawQuery.getColumnIndex("openPrice")), rawQuery.getFloat(rawQuery.getColumnIndex("highPrice")), rawQuery.getFloat(rawQuery.getColumnIndex("lowPrice")), rawQuery.getFloat(rawQuery.getColumnIndex("closePrice")), rawQuery.getInt(rawQuery.getColumnIndex("noOfCntr")), rawQuery.getInt(rawQuery.getColumnIndex("OI")), rawQuery.getInt(rawQuery.getColumnIndex("OIChange")), dateFormatter(rawQuery.getString(rawQuery.getColumnIndex("price_upd_d")), str2), rawQuery.getFloat(rawQuery.getColumnIndex("annualisedVolatility")), rawQuery.getInt(rawQuery.getColumnIndex("marketLot")), rawQuery.getFloat(rawQuery.getColumnIndex("pChange")), rawQuery.getFloat(rawQuery.getColumnIndex("prevClose")), rawQuery.getFloat(rawQuery.getColumnIndex("change")), rawQuery.getFloat(rawQuery.getColumnIndex("impliedVolatility")), rawQuery.getFloat(rawQuery.getColumnIndex("underlyingValue")), rawQuery.getFloat(rawQuery.getColumnIndex("lastPrice")));
            arrayList = arrayList2;
            arrayList.add(greekValues);
            rawQuery.moveToNext();
        }
        if (rawQuery != null) {
            rawQuery.close();
        }
        return arrayList;
    }

    public int deleteAllWatches() {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        this.f125db = writableDatabase;
        return writableDatabase.delete(WATCHES_TABLE_NAME, (String) null, (String[]) null);
    }

    public List<GreekValues> getAllWatches() {
        ArrayList arrayList = new ArrayList();
        if (this.f125db == null) {
            getDatabaseHandler();
        }
        Cursor rawQuery = this.f125db.rawQuery("select * from my_watches", (String[]) null);
        rawQuery.moveToFirst();
        while (!rawQuery.isAfterLast()) {
            ArrayList arrayList2 = arrayList;
            GreekValues greekValues = r4;
            GreekValues greekValues2 = new GreekValues(rawQuery.getString(rawQuery.getColumnIndex("symbol")), rawQuery.getFloat(rawQuery.getColumnIndex("strike")), rawQuery.getString(rawQuery.getColumnIndex("callPut")), dateFormatter(rawQuery.getString(rawQuery.getColumnIndex("expiry_d")), Constants.DT_FMT_yyyy_MM_dd_HH_m_ss), rawQuery.getFloat(rawQuery.getColumnIndex("theoValue")), rawQuery.getFloat(rawQuery.getColumnIndex("delta")), rawQuery.getFloat(rawQuery.getColumnIndex("gamma")), rawQuery.getFloat(rawQuery.getColumnIndex("vega")), rawQuery.getFloat(rawQuery.getColumnIndex("theta")), dateFormatter(rawQuery.getString(rawQuery.getColumnIndex("upd_d")), Constants.DT_FMT_yyyy_MM_dd_HH_m_ss), rawQuery.getFloat(rawQuery.getColumnIndex("openPrice")), rawQuery.getFloat(rawQuery.getColumnIndex("highPrice")), rawQuery.getFloat(rawQuery.getColumnIndex("lowPrice")), rawQuery.getFloat(rawQuery.getColumnIndex("closePrice")), rawQuery.getInt(rawQuery.getColumnIndex("noOfCntr")), rawQuery.getInt(rawQuery.getColumnIndex("OI")), rawQuery.getInt(rawQuery.getColumnIndex("OIChange")), dateFormatter(rawQuery.getString(rawQuery.getColumnIndex("price_upd_d")), Constants.DT_FMT_yyyy_MM_dd_HH_m_ss), rawQuery.getFloat(rawQuery.getColumnIndex("annualisedVolatility")), rawQuery.getInt(rawQuery.getColumnIndex("marketLot")), rawQuery.getFloat(rawQuery.getColumnIndex("pChange")), rawQuery.getFloat(rawQuery.getColumnIndex("prevClose")), rawQuery.getFloat(rawQuery.getColumnIndex("change")), rawQuery.getFloat(rawQuery.getColumnIndex("impliedVolatility")), rawQuery.getFloat(rawQuery.getColumnIndex("underlyingValue")), rawQuery.getFloat(rawQuery.getColumnIndex("lastPrice")));
            arrayList = arrayList2;
            arrayList.add(greekValues);
            rawQuery.moveToNext();
        }
        if (rawQuery != null) {
            rawQuery.close();
        }
        return arrayList;
    }

    public List<String> getAllExpiries(String str) {
        ArrayList arrayList = new ArrayList();
        if (this.f125db == null) {
            getDatabaseHandler();
        }
        SQLiteDatabase sQLiteDatabase = this.f125db;
        Cursor rawQuery = sQLiteDatabase.rawQuery("select expiry_d from strikes where symbol = '" + str + "'", (String[]) null);
        rawQuery.moveToFirst();
        while (!rawQuery.isAfterLast()) {
            arrayList.add(dateFormatter(dateFormatter(rawQuery.getString(rawQuery.getColumnIndex("expiry_d")), Constants.DT_FMT_yyyy_MM_dd_HH_m_ss), Constants.DT_FMT_dd_MMM_yyyy));
            rawQuery.moveToNext();
        }
        if (rawQuery != null) {
            rawQuery.close();
        }
        return arrayList;
    }

    public List<String> getAllStockNames() {
        ArrayList arrayList = new ArrayList();
        if (this.f125db == null) {
            getDatabaseHandler();
        }
        Cursor rawQuery = this.f125db.rawQuery("select distinct symbol from strikes", (String[]) null);
        rawQuery.moveToFirst();
        while (!rawQuery.isAfterLast()) {
            arrayList.add(rawQuery.getString(rawQuery.getColumnIndex("symbol")));
            rawQuery.moveToNext();
        }
        if (rawQuery != null) {
            rawQuery.close();
        }
        return arrayList;
    }

    public List<String> getAllStrikes(String str, String str2) {
        float f;
        float f2;
        ArrayList arrayList = new ArrayList();
        String dateFormatter = dateFormatter(dateFormatter(str2, Constants.DT_FMT_dd_MMM_yyyy), Constants.DT_FMT_yyyy_MM_dd_HH_m_ss);
        if (this.f125db == null) {
            getDatabaseHandler();
        }
        SQLiteDatabase sQLiteDatabase = this.f125db;
        Cursor rawQuery = sQLiteDatabase.rawQuery("select strike_min , strike_max , strike_diff from strikes where symbol = '" + str + "' AND " + "expiry_d" + " = '" + dateFormatter + "'", (String[]) null);
        rawQuery.moveToFirst();
        float f3 = 0.0f;
        if (!rawQuery.isAfterLast()) {
            f3 = rawQuery.getFloat(rawQuery.getColumnIndex(STRIKES_COLUMN_STRIKE_MIN));
            f = rawQuery.getFloat(rawQuery.getColumnIndex(STRIKES_COLUMN_STRIKE_MAX));
            f2 = (float) rawQuery.getInt(rawQuery.getColumnIndex(STRIKES_COLUMN_STRIKE_DIFF));
        } else {
            f = 0.0f;
            f2 = 0.0f;
        }
        while (f3 <= f) {
            arrayList.add(new String(f3 + ""));
            f3 += f2;
        }
        if (rawQuery != null) {
            rawQuery.close();
        }
        return arrayList;
    }

    public int deleteAllStrikesAndExpiries() {
        if (this.f125db == null) {
            getDatabaseHandler();
        }
        return this.f125db.delete(STRIKES_TABLE_NAME, (String) null, (String[]) null);
    }

    public String getStrikesAndExpiriesUpdDate() {
        if (this.f125db == null) {
            getDatabaseHandler();
        }
        Cursor rawQuery = this.f125db.rawQuery("select max( upd_d )  from  strikes", (String[]) null);
        rawQuery.moveToFirst();
        String string = rawQuery.getString(0);
        if (rawQuery != null) {
            rawQuery.close();
        }
        return string;
    }

    public boolean insertAllStrikesAndExpiries(List<StrikeExpiry> list) {
        if (this.f125db == null) {
            getDatabaseHandler();
        }
        Iterator<StrikeExpiry> it = list.iterator();
        StrikeExpiry strikeExpiry = null;
        while (it.hasNext()) {
            strikeExpiry = it.next();
            ContentValues contentValues = new ContentValues();
            contentValues.put("symbol", strikeExpiry.getSymbol());
            contentValues.put("expiry_d", dateFormatter(strikeExpiry.getExpiry_d(), Constants.DT_FMT_yyyy_MM_dd_HH_m_ss));
            contentValues.put(STRIKES_COLUMN_STRIKE_MIN, Float.valueOf(strikeExpiry.getStrikeMin()));
            contentValues.put(STRIKES_COLUMN_STRIKE_MAX, Float.valueOf(strikeExpiry.getStrikeMax()));
            contentValues.put(STRIKES_COLUMN_STRIKE_DIFF, Float.valueOf(strikeExpiry.getStrikeDiff()));
            contentValues.put("upd_d", dateFormatter(strikeExpiry.getUpd_d(), Constants.DT_FMT_yyyy_MM_dd_HH_m_ss));
            this.f125db.insert(STRIKES_TABLE_NAME, (String) null, contentValues);
        }
        return strikeExpiry != null;
    }

    private String dateFormatter(Date date, String str) {
        if (date == null || str == null) {
            return null;
        }
        return new SimpleDateFormat(str).format(date);
    }

    private Date dateFormatter(String str, String str2) {
        if (str == null || str.trim().equals("") || str2 == null) {
            return null;
        }
        try {
            return new SimpleDateFormat(str2).parse(str);
        } catch (ParseException unused) {
            return null;
        }
    }

    public String createAndInsertAppId(SQLiteDatabase sQLiteDatabase) {
        if (sQLiteDatabase == null) {
            getDatabaseHandler();
        }
        String str = null;
        Cursor rawQuery = sQLiteDatabase.rawQuery("select install_id  from  app_install", (String[]) null);
        rawQuery.moveToFirst();
        if (!rawQuery.isAfterLast()) {
            str = rawQuery.getString(rawQuery.getColumnIndex(APP_INSTALL_COLUMN_ID));
        } else {
            String generateUniquePsuedoID = this.appContext.generateUniquePsuedoID();
            ContentValues contentValues = new ContentValues();
            contentValues.put(APP_INSTALL_COLUMN_ID, generateUniquePsuedoID);
            if (sQLiteDatabase.insert(APP_INSTALL_TABLE_NAME, (String) null, contentValues) >= 0) {
                str = generateUniquePsuedoID;
            }
        }
        if (rawQuery != null) {
            rawQuery.close();
        }
        return str;
    }

    public String getAppId() {
        if (this.f125db == null) {
            getDatabaseHandler();
        }
        String str = null;
        Cursor rawQuery = this.f125db.rawQuery("select install_id  from  app_install", (String[]) null);
        rawQuery.moveToFirst();
        if (!rawQuery.isAfterLast()) {
            str = rawQuery.getString(rawQuery.getColumnIndex(APP_INSTALL_COLUMN_ID));
        } else {
            String generateUniquePsuedoID = this.appContext.generateUniquePsuedoID();
            ContentValues contentValues = new ContentValues();
            contentValues.put(APP_INSTALL_COLUMN_ID, generateUniquePsuedoID);
            if (this.f125db.insert(APP_INSTALL_TABLE_NAME, (String) null, contentValues) >= 0) {
                str = generateUniquePsuedoID;
            }
        }
        if (rawQuery != null) {
            rawQuery.close();
        }
        return str;
    }

    public void deleteNinsertBlist(SQLiteDatabase sQLiteDatabase, String str) {
        sQLiteDatabase.delete(BLIST_TABLE_NAME, (String) null, (String[]) null);
        insertBlist(sQLiteDatabase, str);
    }

    public void insertBlist(SQLiteDatabase sQLiteDatabase, String str) {
        String jNICallResult_getGold = this.appContext.getJNICallResult_getGold();
        String jNICallResult_getBList = this.appContext.getJNICallResult_getBList();
        if (!str.equals(Constants.BLIST_BANNED_CODE)) {
            str = jNICallResult_getBList;
        }
        long timeInMillis = Calendar.getInstance().getTimeInMillis();
        MyGreeksApplication myGreeksApplication = this.appContext;
        byte[][] doEncryption = myGreeksApplication.doEncryption(str + ":" + timeInMillis, jNICallResult_getGold);
        ContentValues contentValues = new ContentValues();
        contentValues.put(BLIST_COLUMN_FLAG_E, Base64.encodeToString(doEncryption[2], 0));
        contentValues.put(BLIST_COLUMN_FLAG_S, Base64.encodeToString(doEncryption[0], 0));
        contentValues.put(BLIST_COLUMN_FLAG_I, Base64.encodeToString(doEncryption[1], 0));
        sQLiteDatabase.insert(BLIST_TABLE_NAME, (String) null, contentValues);
    }

    public void insertBListDefaultIfNA(SQLiteDatabase sQLiteDatabase) {
        Cursor rawQuery = sQLiteDatabase.rawQuery("select blist_flag_s  from  blist", (String[]) null);
        rawQuery.moveToFirst();
        if (rawQuery.isAfterLast()) {
            insertBlist(sQLiteDatabase, Constants.BLIST_DEFAULT_CODE);
        }
        if (rawQuery != null) {
            rawQuery.close();
        }
    }

    public void insertRewardDefaultIfNA(SQLiteDatabase sQLiteDatabase, String str) {
        if (sQLiteDatabase == null) {
            getDatabaseHandler();
        }
        Cursor rawQuery = sQLiteDatabase.rawQuery("select reward_check_flag  from  reward_check", (String[]) null);
        rawQuery.moveToFirst();
        if (rawQuery.isAfterLast()) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(REWARD_CHECK_COLUMN_FLAG, str);
            sQLiteDatabase.insert(REWARD_CHECK_TABLE_NAME, (String) null, contentValues);
        }
        if (rawQuery != null) {
            rawQuery.close();
        }
    }

    public void insertRewardTCDefaultIfNA(SQLiteDatabase sQLiteDatabase, String str) {
        if (sQLiteDatabase == null) {
            getDatabaseHandler();
        }
        Cursor rawQuery = sQLiteDatabase.rawQuery("select reward_check_tc_flag  from  reward_check_tc", (String[]) null);
        rawQuery.moveToFirst();
        if (rawQuery.isAfterLast()) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(REWARD_CHECK_TC_COLUMN_FLAG, str);
            sQLiteDatabase.insert(REWARD_CHECK_TC_TABLE_NAME, (String) null, contentValues);
        }
        if (rawQuery != null) {
            rawQuery.close();
        }
    }

    public void insertRewardFlag(SQLiteDatabase sQLiteDatabase, String str) {
        sQLiteDatabase.delete(REWARD_CHECK_TABLE_NAME, (String) null, (String[]) null);
        ContentValues contentValues = new ContentValues();
        contentValues.put(REWARD_CHECK_COLUMN_FLAG, str);
        sQLiteDatabase.insert(REWARD_CHECK_TABLE_NAME, (String) null, contentValues);
    }

    public void insertRewardTCFlag(SQLiteDatabase sQLiteDatabase, String str) {
        sQLiteDatabase.delete(REWARD_CHECK_TC_TABLE_NAME, (String) null, (String[]) null);
        ContentValues contentValues = new ContentValues();
        contentValues.put(REWARD_CHECK_TC_COLUMN_FLAG, str);
        sQLiteDatabase.insert(REWARD_CHECK_TC_TABLE_NAME, (String) null, contentValues);
    }

    public void setBlistFlags(String str) {
        if (this.f125db == null) {
            getDatabaseHandler();
        }
        insertBlist(this.f125db, str);
    }

    public void setBlistFlagsForDebugging() {
        if (this.f125db == null) {
            getDatabaseHandler();
        }
        deleteBlistFlags();
        String jNICallResult_getGold = this.appContext.getJNICallResult_getGold();
        String jNICallResult_getBList = this.appContext.getJNICallResult_getBList();
        long timeInMillis = Calendar.getInstance().getTimeInMillis();
        MyGreeksApplication myGreeksApplication = this.appContext;
        byte[][] doEncryption = myGreeksApplication.doEncryption(jNICallResult_getBList + ":" + timeInMillis, jNICallResult_getGold);
        ContentValues contentValues = new ContentValues();
        contentValues.put(BLIST_COLUMN_FLAG_E, Base64.encodeToString("testvalues".getBytes(), 0));
        contentValues.put(BLIST_COLUMN_FLAG_S, Base64.encodeToString(doEncryption[0], 0));
        contentValues.put(BLIST_COLUMN_FLAG_I, Base64.encodeToString(doEncryption[1], 0));
        this.f125db.insert(BLIST_TABLE_NAME, (String) null, contentValues);
    }

    public void deleteBlistFlags() {
        if (this.f125db == null) {
            getDatabaseHandler();
        }
        this.f125db.delete(BLIST_TABLE_NAME, (String) null, (String[]) null);
    }

    public int countBlistRecs() {
        if (this.f125db == null) {
            getDatabaseHandler();
        }
        Cursor rawQuery = this.f125db.rawQuery("select count(*) from blist", (String[]) null);
        rawQuery.moveToFirst();
        if (!rawQuery.isAfterLast()) {
            return rawQuery.getInt(0);
        }
        return 0;
    }

    public int countRewardRecs() {
        if (this.f125db == null) {
            getDatabaseHandler();
        }
        Cursor rawQuery = this.f125db.rawQuery("select count(*) from reward_check", (String[]) null);
        rawQuery.moveToFirst();
        if (!rawQuery.isAfterLast()) {
            return rawQuery.getInt(0);
        }
        return 0;
    }

    public void deleteRewardFlags() {
        if (this.f125db == null) {
            getDatabaseHandler();
        }
        this.f125db.delete(REWARD_CHECK_TABLE_NAME, (String) null, (String[]) null);
    }

    public void setRewardCheckFlag(String str) {
        if (this.f125db == null) {
            getDatabaseHandler();
        }
        insertRewardFlag(this.f125db, str);
    }

    public void setRewardTCCheckFlag(String str) {
        if (this.f125db == null) {
            getDatabaseHandler();
        }
        insertRewardTCFlag(this.f125db, str);
    }

    public String getRewardCheckFlag() {
        if (this.f125db == null) {
            getDatabaseHandler();
        }
        Cursor rawQuery = this.f125db.rawQuery("select reward_check_flag  from  reward_check", (String[]) null);
        rawQuery.moveToFirst();
        if (!rawQuery.isAfterLast()) {
            return rawQuery.getString(rawQuery.getColumnIndex(REWARD_CHECK_COLUMN_FLAG));
        }
        return null;
    }

    public String getRewardTCCheckFlag() {
        if (this.f125db == null) {
            getDatabaseHandler();
        }
        Cursor rawQuery = this.f125db.rawQuery("select reward_check_tc_flag  from  reward_check_tc", (String[]) null);
        rawQuery.moveToFirst();
        if (!rawQuery.isAfterLast()) {
            return rawQuery.getString(rawQuery.getColumnIndex(REWARD_CHECK_TC_COLUMN_FLAG));
        }
        return null;
    }

    public String[] getBlistFlags() {
        String[] strArr = new String[3];
        if (this.f125db == null) {
            getDatabaseHandler();
        }
        Cursor rawQuery = this.f125db.rawQuery("select blist_flag_e , blist_flag_s , blist_flag_i  from  blist", (String[]) null);
        rawQuery.moveToFirst();
        if (!rawQuery.isAfterLast()) {
            strArr[0] = rawQuery.getString(rawQuery.getColumnIndex(BLIST_COLUMN_FLAG_E));
            strArr[1] = rawQuery.getString(rawQuery.getColumnIndex(BLIST_COLUMN_FLAG_S));
            strArr[2] = rawQuery.getString(rawQuery.getColumnIndex(BLIST_COLUMN_FLAG_I));
        } else {
            strArr[0] = null;
            strArr[1] = null;
            strArr[2] = null;
        }
        if (rawQuery != null) {
            rawQuery.close();
        }
        return strArr;
    }

    public boolean isFcmTopicExists(SQLiteDatabase sQLiteDatabase, String str) {
        boolean z;
        Cursor rawQuery = sQLiteDatabase.rawQuery("select topic  from  fcm_topics", (String[]) null);
        rawQuery.moveToFirst();
        while (true) {
            if (rawQuery.isAfterLast()) {
                z = false;
                break;
            } else if (str.equals(rawQuery.getString(rawQuery.getColumnIndex(FCM_TOPICS_COLUMN_TOPIC)))) {
                z = true;
                break;
            } else {
                rawQuery.moveToNext();
            }
        }
        if (rawQuery != null) {
            rawQuery.close();
        }
        return z;
    }

    public boolean subscribeToTopic(SQLiteDatabase sQLiteDatabase, String str) {
        if (isFcmTopicExists(sQLiteDatabase, str)) {
            return true;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(FCM_TOPICS_COLUMN_TOPIC, str);
        long insert = sQLiteDatabase.insert(FCM_TOPICS_TABLE_NAME, (String) null, contentValues);
        FirebaseMessaging.getInstance().subscribeToTopic(str);
        if (insert < 0) {
            return false;
        }
        return true;
    }

    public void insertStgAccessDB(StrategyResultsFilter strategyResultsFilter) {
        String jNICallResult_getGold = this.appContext.getJNICallResult_getGold();
        byte[][] doEncryption = this.appContext.doEncryption(this.appContext.getJNICallResult_getPass(), jNICallResult_getGold);
        ContentValues contentValues = new ContentValues();
        contentValues.put(STGACC_COLUMN_SYMBOL, strategyResultsFilter.getSymbol());
        contentValues.put(STGACC_COLUMN_STG_ID, Integer.valueOf(strategyResultsFilter.getStrategyId()));
        contentValues.put(STGACC_COLUMN_SUBKEY, Integer.valueOf(strategyResultsFilter.getStrategySubKey()));
        contentValues.put(STGACC_COLUMN_RUN_SEQ, Integer.valueOf(strategyResultsFilter.getRunSeq()));
        contentValues.put(STGACC_COLUMN_DT, Long.valueOf(strategyResultsFilter.getUpdD().getTime()));
        contentValues.put(STGACC_COLUMN_PASS, Base64.encodeToString(doEncryption[2], 0));
        contentValues.put(STGACC_COLUMN_S, Base64.encodeToString(doEncryption[0], 0));
        contentValues.put(STGACC_COLUMN_I, Base64.encodeToString(doEncryption[1], 0));
        this.f125db.insert(STGACC_TABLE_NAME, (String) null, contentValues);
    }

    private void insertFinderAccessDB(long j) {
        byte[][] doEncryption = this.appContext.doEncryption(Long.toString(j), this.appContext.getJNICallResult_getGold());
        ContentValues contentValues = new ContentValues();
        contentValues.put(FINDER_ACC_COLUMN_ENCRP_ENDDT, Base64.encodeToString(doEncryption[2], 0));
        contentValues.put(FINDER_ACC_COLUMN_S, Base64.encodeToString(doEncryption[0], 0));
        contentValues.put(FINDER_ACC_COLUMN_I, Base64.encodeToString(doEncryption[1], 0));
        this.f125db.insert(FINDER_ACC_TABLE_NAME, (String) null, contentValues);
    }

    private void deleteFinderAccessDB() {
        this.f125db.delete(FINDER_ACC_TABLE_NAME, (String) null, (String[]) null);
    }

    public void insertAfterDeleteFinderDB(long j) {
        if (this.f125db == null) {
            getDatabaseHandler();
        }
        this.f125db.beginTransaction();
        try {
            deleteFinderAccessDB();
            insertFinderAccessDB(j);
            this.f125db.setTransactionSuccessful();
        } catch (Exception e) {
            String str = this.TAG;
            Log.d(str, "Error in deleting" + e.getMessage());
        } catch (Throwable th) {
            this.f125db.endTransaction();
            throw th;
        }
        this.f125db.endTransaction();
    }

    public int deleteTradingCallStgAccessDB() {
        Calendar instance = Calendar.getInstance();
        instance.add(5, -10);
        Date time = instance.getTime();
        this.f125db = getWritableDatabase();
        return this.f125db.delete(STGACC_TABLE_NAME, "stgacc_symbol = 'TODAYSCALL' and stgacc_stg_id = 500 and stgacc_dt < " + time.getTime(), (String[]) null);
    }

    public int countStgAccess() {
        if (this.f125db == null) {
            getDatabaseHandler();
        }
        Cursor rawQuery = this.f125db.rawQuery("select count(*) from stgacc", (String[]) null);
        rawQuery.moveToFirst();
        if (!rawQuery.isAfterLast()) {
            return rawQuery.getInt(0);
        }
        return 0;
    }

    public int ifStrategyBoughtForLockDisplayDB(StrategyResultsFilter strategyResultsFilter) {
        if (this.f125db == null) {
            getDatabaseHandler();
        }
        SQLiteDatabase sQLiteDatabase = this.f125db;
        Cursor rawQuery = sQLiteDatabase.rawQuery("select count(*) from stgacc where stgacc_symbol = '" + strategyResultsFilter.getSymbol() + "' AND " + STGACC_COLUMN_STG_ID + " = " + strategyResultsFilter.getStrategyId() + " AND " + STGACC_COLUMN_SUBKEY + " = " + strategyResultsFilter.getStrategySubKey() + " AND " + STGACC_COLUMN_RUN_SEQ + " = " + strategyResultsFilter.getRunSeq() + " AND " + STGACC_COLUMN_DT + " = " + strategyResultsFilter.getUpdD().getTime() + " ", (String[]) null);
        rawQuery.moveToFirst();
        if (!rawQuery.isAfterLast()) {
            return rawQuery.getInt(0);
        }
        return 0;
    }

    public String[] getFinderPaidUserDB() {
        String[] strArr = new String[3];
        if (this.f125db == null) {
            getDatabaseHandler();
        }
        Cursor rawQuery = this.f125db.rawQuery("select finder_stgacc_encrp_end , finder_stgacc_s , finder_stgacc_i  from  finder_stgacc", (String[]) null);
        rawQuery.moveToFirst();
        if (!rawQuery.isAfterLast()) {
            strArr[0] = rawQuery.getString(rawQuery.getColumnIndex(FINDER_ACC_COLUMN_ENCRP_ENDDT));
            strArr[1] = rawQuery.getString(rawQuery.getColumnIndex(FINDER_ACC_COLUMN_S));
            strArr[2] = rawQuery.getString(rawQuery.getColumnIndex(FINDER_ACC_COLUMN_I));
        } else {
            strArr[0] = null;
            strArr[1] = null;
            strArr[2] = null;
        }
        if (rawQuery != null) {
            rawQuery.close();
        }
        return strArr;
    }

    public String[] ifExistsCountStgAccess(StrategyResultsFilter strategyResultsFilter) {
        String[] strArr = new String[3];
        if (this.f125db == null) {
            getDatabaseHandler();
        }
        SQLiteDatabase sQLiteDatabase = this.f125db;
        Cursor rawQuery = sQLiteDatabase.rawQuery("select stgacc_pass , stgacc_s , stgacc_i from stgacc where stgacc_symbol = '" + strategyResultsFilter.getSymbol() + "' AND " + STGACC_COLUMN_STG_ID + " = " + strategyResultsFilter.getStrategyId() + " AND " + STGACC_COLUMN_SUBKEY + " = " + strategyResultsFilter.getStrategySubKey() + " AND " + STGACC_COLUMN_RUN_SEQ + " = " + strategyResultsFilter.getRunSeq() + " AND " + STGACC_COLUMN_DT + " = " + strategyResultsFilter.getUpdD().getTime() + " ", (String[]) null);
        rawQuery.moveToFirst();
        if (!rawQuery.isAfterLast()) {
            strArr[0] = rawQuery.getString(rawQuery.getColumnIndex(STGACC_COLUMN_PASS));
            strArr[1] = rawQuery.getString(rawQuery.getColumnIndex(STGACC_COLUMN_S));
            strArr[2] = rawQuery.getString(rawQuery.getColumnIndex(STGACC_COLUMN_I));
        } else {
            strArr[0] = null;
            strArr[1] = null;
            strArr[2] = null;
        }
        if (rawQuery != null) {
            rawQuery.close();
        }
        return strArr;
    }

    public List<MyCalls> getMyCallsDB() {
        ArrayList arrayList = new ArrayList();
        if (this.f125db == null) {
            getDatabaseHandler();
        }
        Cursor rawQuery = this.f125db.rawQuery("select distinct stgacc_dt, stgacc_pass , stgacc_s , stgacc_i from stgacc where stgacc_symbol = 'TODAYSCALL' AND stgacc_stg_id = 500 AND stgacc_subkey = 0 AND stgacc_runseqy = 0 order by stgacc_dt DESC ", (String[]) null);
        rawQuery.moveToFirst();
        while (!rawQuery.isAfterLast()) {
            arrayList.add(new MyCalls(rawQuery.getString(rawQuery.getColumnIndex(STGACC_COLUMN_PASS)), rawQuery.getString(rawQuery.getColumnIndex(STGACC_COLUMN_S)), rawQuery.getString(rawQuery.getColumnIndex(STGACC_COLUMN_I)), rawQuery.getLong(rawQuery.getColumnIndex(STGACC_COLUMN_DT))));
            rawQuery.moveToNext();
        }
        if (rawQuery != arrayList) {
            rawQuery.close();
        }
        return arrayList;
    }

    public void insertStrategyResultAndLegs(StrategyResultsFilter strategyResultsFilter, List<StrategyLegsFilter> list, String str, String str2, String str3, String str4) {
        if (this.f125db == null) {
            getDatabaseHandler();
        }
        this.f125db.beginTransaction();
        try {
            insertStgResult(strategyResultsFilter, str, str2, str3, str4);
            insertStgLegs(list);
            this.f125db.setTransactionSuccessful();
        } catch (Exception e) {
            String str5 = this.TAG;
            Log.d(str5, "Error in saving legs" + e.getMessage());
        } catch (Throwable th) {
            this.f125db.endTransaction();
            throw th;
        }
        this.f125db.endTransaction();
    }

    public List<String> getPortfolioStockListDB() {
        ArrayList arrayList = new ArrayList();
        if (this.f125db == null) {
            getDatabaseHandler();
        }
        Cursor rawQuery = this.f125db.rawQuery("select distinct underlyer_c from stg_result_save", (String[]) null);
        rawQuery.moveToFirst();
        while (!rawQuery.isAfterLast()) {
            arrayList.add(rawQuery.getString(rawQuery.getColumnIndex("underlyer_c")));
            rawQuery.moveToNext();
        }
        if (rawQuery != null) {
            rawQuery.close();
        }
        return arrayList;
    }

    public List<StrategyResultsFilter> getStrategyResults() {
        ArrayList arrayList = new ArrayList();
        if (this.f125db == null) {
            getDatabaseHandler();
        }
        Cursor rawQuery = this.f125db.rawQuery("select distinct strategy_type_cd_i, strategy_subkey_i , strategy_name_s , underlyer_c , net_debit_f , max_risk_f , max_gain_f , breakeven_dn_pt_f , breakeven_up_pt_f , upd_d from stg_result_save order by upd_d DESC ", (String[]) null);
        rawQuery.moveToFirst();
        while (!rawQuery.isAfterLast()) {
            StrategyResultsFilter strategyResultsFilter = new StrategyResultsFilter();
            strategyResultsFilter.setStrategyId(rawQuery.getInt(rawQuery.getColumnIndex("strategy_type_cd_i")));
            strategyResultsFilter.setStrategySubKey(rawQuery.getInt(rawQuery.getColumnIndex("strategy_subkey_i")));
            strategyResultsFilter.setStrategyName(rawQuery.getString(rawQuery.getColumnIndex(STG_RSLT_COL_STGNAME)));
            strategyResultsFilter.setSymbol(rawQuery.getString(rawQuery.getColumnIndex("underlyer_c")));
            strategyResultsFilter.setNetDebit(rawQuery.getFloat(rawQuery.getColumnIndex(STG_RSLT_COL_DEBIT)));
            strategyResultsFilter.setMaxRisk(rawQuery.getFloat(rawQuery.getColumnIndex(STG_RSLT_COL_RISK)));
            strategyResultsFilter.setMaxGain(rawQuery.getFloat(rawQuery.getColumnIndex(STG_RSLT_COL_GAIN)));
            strategyResultsFilter.setBreakevenDown(rawQuery.getFloat(rawQuery.getColumnIndex(STG_RSLT_COL_LBEP)));
            strategyResultsFilter.setBreakevenUp(rawQuery.getFloat(rawQuery.getColumnIndex(STG_RSLT_COL_HBEP)));
            strategyResultsFilter.setUpdD(new Date(Long.valueOf(rawQuery.getString(rawQuery.getColumnIndex("upd_d"))).longValue()));
            arrayList.add(strategyResultsFilter);
            rawQuery.moveToNext();
        }
        if (rawQuery != null) {
            rawQuery.close();
        }
        return arrayList;
    }

    public String[] getStrategyGreeks(StrategyResultsFilter strategyResultsFilter) {
        String[] strArr = {Constants.NULL_GREEK_VALUE, Constants.NULL_GREEK_VALUE, Constants.NULL_GREEK_VALUE, Constants.NULL_GREEK_VALUE};
        if (this.f125db == null) {
            getDatabaseHandler();
        }
        Cursor rawQuery = this.f125db.rawQuery("select distinct net_delta, net_gamma, net_theta , net_vega from stg_result_save where strategy_type_cd_i = " + strategyResultsFilter.getStrategyId() + " AND " + "strategy_subkey_i" + " = " + strategyResultsFilter.getStrategySubKey() + " AND " + "underlyer_c" + " = '" + strategyResultsFilter.getSymbol() + "' AND " + "upd_d" + " = '" + strategyResultsFilter.getUpdD().getTime() + "' ", (String[]) null);
        rawQuery.moveToFirst();
        if (!rawQuery.isAfterLast()) {
            strArr[0] = rawQuery.getString(rawQuery.getColumnIndex(STG_RSLT_COL_DELTA));
            strArr[1] = rawQuery.getString(rawQuery.getColumnIndex(STG_RSLT_COL_GAMMA));
            strArr[2] = rawQuery.getString(rawQuery.getColumnIndex(STG_RSLT_COL_THETA));
            strArr[3] = rawQuery.getString(rawQuery.getColumnIndex(STG_RSLT_COL_VEGA));
        }
        if (rawQuery != null) {
            rawQuery.close();
        }
        return strArr;
    }

    public List<StrategyLegsFilter> getStrategyLegs(StrategyResultsFilter strategyResultsFilter) {
        ArrayList arrayList = new ArrayList();
        if (this.f125db == null) {
            getDatabaseHandler();
        }
        Cursor rawQuery = this.f125db.rawQuery("select distinct strategy_type_cd_i, strategy_subkey_i , strategy_leg_seq_i , underlyer_c , action_cd_c , strike_i , call_put_stk_cd_c , expiry_d , premium_f , upd_d from stg_legs_save where strategy_type_cd_i = " + strategyResultsFilter.getStrategyId() + " AND " + "strategy_subkey_i" + " = " + strategyResultsFilter.getStrategySubKey() + " AND " + "underlyer_c" + " = '" + strategyResultsFilter.getSymbol() + "' AND " + "upd_d" + " = '" + strategyResultsFilter.getUpdD().getTime() + "' order by " + STG_LEGS_COL_LEGID + " ASC ", (String[]) null);
        rawQuery.moveToFirst();
        while (!rawQuery.isAfterLast()) {
            StrategyLegsFilter strategyLegsFilter = new StrategyLegsFilter();
            strategyLegsFilter.setStrategyId(rawQuery.getInt(rawQuery.getColumnIndex("strategy_type_cd_i")));
            strategyLegsFilter.setStrategySubKey(rawQuery.getInt(rawQuery.getColumnIndex("strategy_subkey_i")));
            strategyLegsFilter.setSymbol(rawQuery.getString(rawQuery.getColumnIndex("underlyer_c")));
            strategyLegsFilter.setAction(rawQuery.getString(rawQuery.getColumnIndex(STG_LEGS_COL_ACTION)));
            strategyLegsFilter.setStrike(rawQuery.getFloat(rawQuery.getColumnIndex(STG_LEGS_COL_STRIKE)));
            strategyLegsFilter.setCallPut(rawQuery.getString(rawQuery.getColumnIndex(STG_LEGS_COL_CP)));
            strategyLegsFilter.setExpiry(this.appContext.dateFormatter(rawQuery.getString(rawQuery.getColumnIndex("expiry_d")), Constants.DT_FMT_dd_MMM_yyyy));
            strategyLegsFilter.setPremium(rawQuery.getFloat(rawQuery.getColumnIndex(STG_LEGS_COL_PREM)));
            strategyLegsFilter.setUpdD(new Date(Long.valueOf(rawQuery.getString(rawQuery.getColumnIndex("upd_d"))).longValue()));
            arrayList.add(strategyLegsFilter);
            rawQuery.moveToNext();
        }
        if (rawQuery != null) {
            rawQuery.close();
        }
        return arrayList;
    }

    private void insertStgResult(StrategyResultsFilter strategyResultsFilter, String str, String str2, String str3, String str4) {
        if (this.f125db == null) {
            getDatabaseHandler();
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("strategy_type_cd_i", Integer.valueOf(strategyResultsFilter.getStrategyId()));
        contentValues.put("strategy_subkey_i", Integer.valueOf(strategyResultsFilter.getStrategySubKey()));
        contentValues.put(STG_RSLT_COL_STGNAME, strategyResultsFilter.getStrategyName());
        contentValues.put("underlyer_c", strategyResultsFilter.getSymbol());
        contentValues.put(STG_RSLT_COL_DEBIT, Float.valueOf(strategyResultsFilter.getNetDebit()));
        contentValues.put(STG_RSLT_COL_RISK, Float.valueOf(strategyResultsFilter.getMaxRisk()));
        contentValues.put(STG_RSLT_COL_GAIN, Float.valueOf(strategyResultsFilter.getMaxGain()));
        contentValues.put(STG_RSLT_COL_LBEP, Float.valueOf(strategyResultsFilter.getBreakevenDown()));
        contentValues.put(STG_RSLT_COL_HBEP, Float.valueOf(strategyResultsFilter.getBreakevenUp()));
        contentValues.put(STG_RSLT_COL_DELTA, str);
        contentValues.put(STG_RSLT_COL_GAMMA, str2);
        contentValues.put(STG_RSLT_COL_THETA, str3);
        contentValues.put(STG_RSLT_COL_VEGA, str4);
        contentValues.put("upd_d", String.valueOf(strategyResultsFilter.getUpdD().getTime()));
        this.f125db.insert(STG_RSLT_SAVE_TABLE_NAME, (String) null, contentValues);
    }

    private void insertStgLegs(List<StrategyLegsFilter> list) {
        if (this.f125db == null) {
            getDatabaseHandler();
        }
        for (int i = 0; i < list.size(); i++) {
            StrategyLegsFilter strategyLegsFilter = list.get(i);
            ContentValues contentValues = new ContentValues();
            contentValues.put("strategy_type_cd_i", Integer.valueOf(strategyLegsFilter.getStrategyId()));
            contentValues.put("strategy_subkey_i", Integer.valueOf(strategyLegsFilter.getStrategySubKey()));
            contentValues.put(STG_LEGS_COL_LEGID, Integer.valueOf(i));
            contentValues.put("underlyer_c", strategyLegsFilter.getSymbol());
            contentValues.put(STG_LEGS_COL_ACTION, strategyLegsFilter.getAction());
            contentValues.put(STG_LEGS_COL_STRIKE, Float.valueOf(strategyLegsFilter.getStrike()));
            contentValues.put(STG_LEGS_COL_CP, strategyLegsFilter.getCallPut());
            contentValues.put("expiry_d", this.appContext.dateFormatter(strategyLegsFilter.getExpiry(), Constants.DT_FMT_dd_MMM_yyyy));
            contentValues.put(STG_LEGS_COL_PREM, Float.valueOf(strategyLegsFilter.getPremium()));
            contentValues.put("upd_d", String.valueOf(strategyLegsFilter.getUpdD().getTime()));
            this.f125db.insert(STG_LEGS_SAVE_TABLE_NAME, (String) null, contentValues);
        }
    }

    private int deleteStrategyResult(StrategyResultsFilter strategyResultsFilter) {
        if (strategyResultsFilter == null) {
            return 0;
        }
        return this.f125db.delete(STG_RSLT_SAVE_TABLE_NAME, "strategy_type_cd_i = " + strategyResultsFilter.getStrategyId() + " AND " + "strategy_subkey_i" + " = " + strategyResultsFilter.getStrategySubKey() + " AND " + "underlyer_c" + " = '" + strategyResultsFilter.getSymbol() + "' AND " + "upd_d" + " = '" + strategyResultsFilter.getUpdD().getTime() + "' ", (String[]) null);
    }

    private int deleteStrategyLegs(StrategyResultsFilter strategyResultsFilter) {
        if (strategyResultsFilter == null) {
            return 0;
        }
        return this.f125db.delete(STG_LEGS_SAVE_TABLE_NAME, "strategy_type_cd_i = " + strategyResultsFilter.getStrategyId() + " AND " + "strategy_subkey_i" + " = " + strategyResultsFilter.getStrategySubKey() + " AND " + "underlyer_c" + " = '" + strategyResultsFilter.getSymbol() + "' AND " + "upd_d" + " = '" + strategyResultsFilter.getUpdD().getTime() + "' ", (String[]) null);
    }

    public void deleteStrategyResultAndLegs(StrategyResultsFilter strategyResultsFilter) {
        if (this.f125db == null) {
            getDatabaseHandler();
        }
        this.f125db.beginTransaction();
        try {
            deleteStrategyResult(strategyResultsFilter);
            deleteStrategyLegs(strategyResultsFilter);
            this.f125db.setTransactionSuccessful();
        } catch (Exception e) {
            String str = this.TAG;
            Log.d(str, "Error in deleting" + e.getMessage());
        } catch (Throwable th) {
            this.f125db.endTransaction();
            throw th;
        }
        this.f125db.endTransaction();
    }
}
