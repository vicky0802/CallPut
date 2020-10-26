package bulltrack.com.optionanalyzer.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import bulltrack.com.optionanalyzer.application.MyGreeksApplication;
import bulltrack.com.optionanalyzer.constants.Constants;
import bulltrack.com.optionanalyzer.dao.FilterStockRange;
import bulltrack.com.optiongreeks13.R;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.jiangjiesheng.slidingmenu.SlidingMenu;
import com.jiangjiesheng.slidingmenu.app.SlidingActivityBase;
import com.jiangjiesheng.slidingmenu.app.SlidingActivityHelper;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SlidingActivityBase {
    public static final String LAST_REFRESH_DATETIME = "last_refresh_dt";
    public static final String MY_PREF = "my_pref";
    /* access modifiers changed from: private */
    public String TAG = "MainActivity";
    private Activity activity;
    FilterFragment filterFragment;
    List<FilterStockRange> lstFilterRange = new ArrayList();
    List<Double> lstRisk = new ArrayList();
    List<String> lstStock = new ArrayList();
    /* access modifiers changed from: private */
    public InterstitialAd mIAd_BC;
    /* access modifiers changed from: private */
    public InterstitialAd mIAd_ChartEOD;
    /* access modifiers changed from: private */
    public InterstitialAd mIAd_MargnMain;
    /* access modifiers changed from: private */
    public InterstitialAd mIAd_OIDay;
    /* access modifiers changed from: private */
    public InterstitialAd mIAd_OIStock;
    /* access modifiers changed from: private */
    public InterstitialAd mIAd_OpenPos;
    /* access modifiers changed from: private */
    public InterstitialAd mIAd_PCRMain;
    /* access modifiers changed from: private */
    public InterstitialAd mIAd_StgMain;
    /* access modifiers changed from: private */
    public InterstitialAd mIAd_Xmove;
    /* access modifiers changed from: private */
    public SlidingMenu menu;
    String prodId;
    int prodRs;
    ProgressBar progressBar;
    SharedPreferences sharedpreferences;
    private SlidingActivityHelper slideHelper;
    StrategyFinderFragment strategyFragment;
    TextView tvStdUpdDT;
    String webServiceUrl = Constants.URL_SERVICE;

    /* access modifiers changed from: protected */
    public int getContentFrameId() {
        return R.id.rl_progress_main;
    }

    public boolean onCreateOptionsMenu(Menu menu2) {
        return true;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        SlidingActivityHelper slidingActivityHelper = new SlidingActivityHelper(this);
        this.slideHelper = slidingActivityHelper;
        slidingActivityHelper.onCreate(bundle);
        setContentView((int) R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.activity = this;
        LoadInterstitialAds();
        setSupportActionBar(toolbar);
        getGreekApplication().initDatabase();
        getSupportActionBar().setDisplayOptions(16);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView((int) R.layout.action_bar_finder);
        View customView = getSupportActionBar().getCustomView();
        this.tvStdUpdDT = (TextView) customView.findViewById(R.id.tv_actionbar_finder_dt);
        ((ImageView) customView.findViewById(R.id.img_actionbar_finder_filter)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (MainActivity.this.menu != null) {
                    MainActivity.this.menu.showMenu();
                }
            }
        });
        ((ImageView) customView.findViewById(R.id.img_actionbar_finder_demo)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                MainActivity.this.startActivity(new Intent(MainActivity.this, FinderDemoYTActivity.class));
            }
        });
        ProgressBar progressBar2 = (ProgressBar) findViewById(R.id.progressbar_progress_main);
        this.progressBar = progressBar2;
        progressBar2.setVisibility(0);
        new Thread(new Runnable() {
            public void run() {
                MainActivity mainActivity = MainActivity.this;
                mainActivity.sharedpreferences = mainActivity.getApplicationContext().getSharedPreferences("my_pref", 0);
                SharedPreferences.Editor edit = MainActivity.this.sharedpreferences.edit();
                edit.putLong("last_refresh_dt", Calendar.getInstance().getTimeInMillis());
                edit.commit();
            }
        }).start();
        ((FloatingActionButton) findViewById(R.id.fab)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent("android.intent.action.SEND");
                intent.setType("text/plain");
                intent.putExtra("android.intent.extra.SUBJECT", "Call Put Option Analyzer Android App");
                intent.putExtra("android.intent.extra.TEXT", "I am sharing this app with you. You will find it very useful for call / put option trading. Download link : https://play.google.com/store/apps/details?id=bulltrack.com.optiongreeks13&hl=en");
                MainActivity.this.startActivity(Intent.createChooser(intent, "Share via"));
            }
        });
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        C06695 r0 = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                if (MainActivity.this.menu != null) {
                    MainActivity.this.menu.setSlidingEnabled(true);
                }
            }

            public void onDrawerOpened(View view) {
                super.onDrawerOpened(view);
                if (MainActivity.this.menu != null) {
                    MainActivity.this.menu.setSlidingEnabled(false);
                }
            }
        };
        drawerLayout.addDrawerListener(r0);
        r0.syncState();
        ((NavigationView) findViewById(R.id.nav_view)).setNavigationItemSelectedListener(this);
        setBehindContentView((int) R.layout.filter_menu_frame);
        getFilterCriteria();
    }

    public void onPostCreate(Bundle bundle) {
        super.onPostCreate(bundle);
        this.slideHelper.onPostCreate(bundle);
        this.activity = this;
    }

    public View findViewById(int i) {
        View findViewById = super.findViewById(i);
        if (findViewById != null) {
            return findViewById;
        }
        return this.slideHelper.findViewById(i);
    }

    /* access modifiers changed from: protected */
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        this.slideHelper.onSaveInstanceState(bundle);
    }

    public void setContentView(int i) {
        setContentView(getLayoutInflater().inflate(i, (ViewGroup) null));
    }

    public void setContentView(View view) {
        setContentView(view, new ViewGroup.LayoutParams(-1, -1));
    }

    public void setContentView(View view, ViewGroup.LayoutParams layoutParams) {
        super.setContentView(view, layoutParams);
        this.slideHelper.registerAboveContentView(view, layoutParams);
    }

    public void setBehindContentView(int i) {
        setBehindContentView(getLayoutInflater().inflate(i, (ViewGroup) null));
    }

    public void setBehindContentView(View view, ViewGroup.LayoutParams layoutParams) {
        this.slideHelper.setBehindContentView(view, layoutParams);
    }

    public void setBehindContentView(View view) {
        setBehindContentView(view, new ViewGroup.LayoutParams(-1, -1));
    }

    public SlidingMenu getSlidingMenu() {
        return this.slideHelper.getSlidingMenu();
    }

    public void toggle() {
        this.slideHelper.toggle();
    }

    public void showContent() {
        this.slideHelper.showContent();
    }

    public void showMenu() {
        this.slideHelper.showMenu();
    }

    public void showSecondaryMenu() {
        this.slideHelper.showSecondaryMenu();
    }

    public void setSlidingActionBarEnabled(boolean z) {
        this.slideHelper.setSlidingActionBarEnabled(z);
    }

    public boolean onKeyUp(int i, KeyEvent keyEvent) {
        boolean onKeyUp = this.slideHelper.onKeyUp(i, keyEvent);
        if (onKeyUp) {
            return onKeyUp;
        }
        return super.onKeyUp(i, keyEvent);
    }

    /* access modifiers changed from: private */
    public void LoadUI() {
        SlidingMenu slidingMenu = this.slideHelper.getSlidingMenu();
        this.menu = slidingMenu;
        slidingMenu.setMode(2);
        this.menu.setTouchModeAbove(1);
        this.menu.setBehindOffsetRes(R.dimen.slider_menu_right_margin);
        this.menu.setMode(1);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.FINDER_GOOGLE_PROD_ID_KEY, this.prodId);
        bundle.putInt(Constants.FINDER_GOOGLE_PROD_PRICE_KEY, this.prodRs);
        StrategyFinderFragment strategyFinderFragment = new StrategyFinderFragment();
        this.strategyFragment = strategyFinderFragment;
        strategyFinderFragment.setArguments(bundle);
        setupStrategyFinderFragment();
        this.menu.setSecondaryMenu((int) R.layout.menu_frame_two);
        this.menu.setSecondaryShadowDrawable((int) R.drawable.shadowright);
        FilterFragment filterFragment2 = new FilterFragment();
        this.filterFragment = filterFragment2;
        filterFragment2.setMenu(this.menu);
        getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame_two, this.filterFragment).commitAllowingStateLoss();
        this.menu.setOnCloseListener(new SlidingMenu.OnCloseListener() {
            public void onClose() {
                if (MainActivity.this.filterFragment.hasFilterChanged()) {
                    MainActivity.this.filterFragment.saveToOldFilterSelection();
                    try {
                        if (!(MainActivity.this.getCurrentContentFragment(MainActivity.this.getSupportFragmentManager()) instanceof StrategyFinderFragment)) {
                            Bundle bundle = new Bundle();
                            bundle.putString(Constants.FINDER_GOOGLE_PROD_ID_KEY, MainActivity.this.prodId);
                            bundle.putInt(Constants.FINDER_GOOGLE_PROD_PRICE_KEY, MainActivity.this.prodRs);
                            MainActivity.this.strategyFragment = new StrategyFinderFragment();
                            MainActivity.this.strategyFragment.setArguments(bundle);
                            MainActivity.this.setupStrategyFinderFragment();
                            return;
                        }
                        MainActivity.this.strategyFragment.setLoadUI(false);
                        MainActivity.this.strategyFragment.webServiceCallFirstLoad();
                    } catch (Exception e) {
                        Log.d(MainActivity.this.TAG, e.getMessage());
                        MainActivity.this.setErrorFragment();
                    }
                }
            }
        });
    }

    public boolean setupStrategyFinderFragment() {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        if (getCurrentContentFragment(supportFragmentManager) instanceof StrategyFinderFragment) {
            return false;
        }
        setFragmentToActivity(supportFragmentManager, this.strategyFragment);
        return true;
    }

    public void getFilterCriteria() {
        String str;
        try {
            str = String.format(this.webServiceUrl + "%1$s", new Object[]{"102", "UTF-8"});
        } catch (Exception e) {
            String str2 = this.TAG;
            Log.d(str2, "Error | Volley Call | FinderLoadActivity getFilterCriteria : " + e.toString());
            str = "";
        }
        StringRequest stringRequest = new StringRequest(0, str, new Response.Listener<String>() {
            public void onResponse(String str) {
                if (str != null) {
                    try {
                        JSONObject jSONObject = new JSONObject(str.toString());
                        JSONArray jSONArray = jSONObject.getJSONArray("underlyer");
                        for (int i = 0; i < jSONArray.length(); i++) {
                            MainActivity.this.lstStock.add(jSONArray.getJSONObject(i).getString("symbol"));
                        }
                        JSONArray jSONArray2 = jSONObject.getJSONArray("range");
                        MainActivity.this.lstFilterRange = MainActivity.this.getRangeFromJSON(jSONArray2);
                        JSONArray jSONArray3 = jSONObject.getJSONArray("risk");
                        for (int i2 = 0; i2 < jSONArray3.length(); i2++) {
                            MainActivity.this.lstRisk.add(Double.valueOf(jSONArray3.getJSONObject(i2).getDouble("amt")));
                        }
                        MainActivity.this.prodId = jSONObject.getString("finder_prod_id");
                        MainActivity.this.prodRs = jSONObject.getInt("finder_prod_rs");
                        MainActivity.this.LoadUI();
                    } catch (Exception e) {
                        String access$200 = MainActivity.this.TAG;
                        Log.d(access$200, "Error | Volley Call " + e.toString());
                        MainActivity.this.setErrorFragment();
                        return;
                    }
                }
                if (MainActivity.this.isServerBlistChkRequired()) {
                    Executors.newSingleThreadExecutor().submit(new Runnable() {
                        public void run() {
                            try {
                                MainActivity.this.callServerBlist();
                                MainActivity.this.getGreekApplication().callServerRewards();
                                MainActivity.this.getGreekApplication().callServerRewardsTC();
                                MainActivity.this.getGreekApplication().callServerRewardsFinder();
                            } catch (Exception e) {
                                String access$200 = MainActivity.this.TAG;
                                Log.d(access$200, "Error : MAinActivity Blist/Reward Check : " + e.toString());
                            }
                        }
                    });
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError volleyError) {
                String access$200 = MainActivity.this.TAG;
                VolleyLog.d(access$200, "Error: " + volleyError.getMessage());
                MainActivity.this.setErrorFragment();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, 1, 1.0f));
        getGreekApplication();
        MyGreeksApplication.getInstance().addToRequestQueue(stringRequest);
    }

    /* access modifiers changed from: package-private */
    public List<FilterStockRange> getRangeFromJSON(JSONArray jSONArray) throws JSONException {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < jSONArray.length(); i++) {
            arrayList.add(new FilterStockRange(jSONArray.getJSONObject(i).getString("symbol"), getGreekApplication().dateFormatter(jSONArray.getJSONObject(i).getString("expiry"), Constants.DT_FMT_dd_MMM_yyyy), (double) jSONArray.getJSONObject(i).getInt("low"), (double) jSONArray.getJSONObject(i).getInt("high")));
        }
        return arrayList;
    }

    /* access modifiers changed from: package-private */
    public List<String> getStockList() {
        return this.lstStock;
    }

    /* access modifiers changed from: package-private */
    public List<Double> getRiskList() {
        return this.lstRisk;
    }

    /* access modifiers changed from: package-private */
    public List<FilterStockRange> getStockRangeList() {
        return this.lstFilterRange;
    }

    public void setLastUpdActionBar(String str) {
        TextView textView = this.tvStdUpdDT;
        textView.setText("Strategy Updated: " + str);
    }

    public void onResume() {
        super.onResume();
        if (this.sharedpreferences == null) {
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("my_pref", 0);
            this.sharedpreferences = sharedPreferences;
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.putLong("last_refresh_dt", Calendar.getInstance().getTimeInMillis());
            edit.commit();
        }
        long timeInMillis = Calendar.getInstance().getTimeInMillis();
        if (!this.sharedpreferences.contains("last_refresh_dt")) {
            SharedPreferences.Editor edit2 = this.sharedpreferences.edit();
            edit2.putLong("last_refresh_dt", timeInMillis);
            edit2.commit();
        } else if (timeInMillis - this.sharedpreferences.getLong("last_refresh_dt", 0) > 1200000) {
            finish();
            startActivity(getIntent());
            SharedPreferences.Editor edit3 = this.sharedpreferences.edit();
            edit3.putLong("last_refresh_dt", timeInMillis);
            edit3.commit();
        }
    }

    public void onBackPressed() {
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawerLayout == null || !drawerLayout.isDrawerOpen((int) GravityCompat.START)) {
            super.onBackPressed();
        } else {
            drawerLayout.closeDrawer((int) GravityCompat.START);
        }
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        menuItem.getItemId();
        return super.onOptionsItemSelected(menuItem);
    }

    public boolean onNavigationItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == R.id.near_mon_nifty_options) {
            startActivity(new Intent(this, GreekMainActivity.class));
        } else if (itemId == R.id.portfolio_open_positions) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    if ((MainActivity.this.mIAd_OpenPos != null) && MainActivity.this.mIAd_OpenPos.isLoaded()) {
                        MainActivity.this.mIAd_OpenPos.show();
                        MainActivity.this.mIAd_OpenPos.setAdListener(new AdListener() {
                            public void onAdClosed() {
                                MainActivity.this.startActivity(new Intent(MainActivity.this, ShowPortfolioActivity.class));
                            }
                        });
                        return;
                    }
                    MainActivity.this.startActivity(new Intent(MainActivity.this, ShowPortfolioActivity.class));
                }
            }, 1000);
        } else if (itemId == R.id.portfolio_close_positions) {
            startActivity(new Intent(this, ShowClosedFolioPositionActivity.class));
        } else if (itemId == R.id.my_watch) {
            startActivity(new Intent(this, MyWatchActivity.class));
        } else if (itemId == R.id.nav_adv_serach) {
            startActivity(new Intent(this, AdvGreekSearchActivity.class));
        } else if (itemId == R.id.nav_calculator) {
            startActivity(new Intent(this, CalculatorActivity.class));
        } else if (itemId == R.id.fii_dii_1) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    if (MainActivity.this.mIAd_OIDay == null || !MainActivity.this.mIAd_OIDay.isLoaded()) {
                        MainActivity.this.startActivity(new Intent(MainActivity.this, FiiDiiDayWiseActivity.class));
                        return;
                    }
                    MainActivity.this.mIAd_OIDay.show();
                    MainActivity.this.mIAd_OIDay.setAdListener(new AdListener() {
                        public void onAdClosed() {
                            MainActivity.this.startActivity(new Intent(MainActivity.this, FiiDiiDayWiseActivity.class));
                        }
                    });
                }
            }, 1000);
        } else if (itemId == R.id.fii_dii_2) {
            startActivity(new Intent(this, FiiDiiCurrentActivity.class));
        } else if (itemId == R.id.oi_stock) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    if (MainActivity.this.mIAd_OIStock == null || !MainActivity.this.mIAd_OIStock.isLoaded()) {
                        MainActivity.this.startActivity(new Intent(MainActivity.this, OIStockActivity.class));
                        return;
                    }
                    MainActivity.this.mIAd_OIStock.show();
                    MainActivity.this.mIAd_OIStock.setAdListener(new AdListener() {
                        public void onAdClosed() {
                            MainActivity.this.startActivity(new Intent(MainActivity.this, OIStockActivity.class));
                        }
                    });
                }
            }, 1000);
        } else if (itemId == R.id.mnu_brokerage_calls) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    if (MainActivity.this.mIAd_BC == null || !MainActivity.this.mIAd_BC.isLoaded()) {
                        MainActivity.this.startActivity(new Intent(MainActivity.this, BrokerCallsActivity.class));
                        return;
                    }
                    MainActivity.this.mIAd_BC.show();
                    MainActivity.this.mIAd_BC.setAdListener(new AdListener() {
                        public void onAdClosed() {
                            MainActivity.this.startActivity(new Intent(MainActivity.this, BrokerCallsActivity.class));
                        }
                    });
                }
            }, 1000);
        } else if (itemId == R.id.mnu_eod_option_charts) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    if (MainActivity.this.mIAd_ChartEOD == null || !MainActivity.this.mIAd_ChartEOD.isLoaded()) {
                        MainActivity.this.startActivity(new Intent(MainActivity.this, ChartEodActivity.class));
                        return;
                    }
                    MainActivity.this.mIAd_ChartEOD.show();
                    MainActivity.this.mIAd_ChartEOD.setAdListener(new AdListener() {
                        public void onAdClosed() {
                            MainActivity.this.startActivity(new Intent(MainActivity.this, ChartEodActivity.class));
                        }
                    });
                }
            }, 1000);
        } else if (itemId == R.id.mnu_margin) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    if (MainActivity.this.mIAd_MargnMain == null || !MainActivity.this.mIAd_MargnMain.isLoaded()) {
                        MainActivity.this.startActivity(new Intent(MainActivity.this, OptionMarginMainActivity.class));
                        return;
                    }
                    MainActivity.this.mIAd_MargnMain.show();
                    MainActivity.this.mIAd_MargnMain.setAdListener(new AdListener() {
                        public void onAdClosed() {
                            MainActivity.this.startActivity(new Intent(MainActivity.this, OptionMarginMainActivity.class));
                        }
                    });
                }
            }, 1000);
        } else if (itemId == R.id.mnu_pcr) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    if (MainActivity.this.mIAd_PCRMain == null || !MainActivity.this.mIAd_PCRMain.isLoaded()) {
                        MainActivity.this.startActivity(new Intent(MainActivity.this, PCRMainActivity.class));
                        return;
                    }
                    MainActivity.this.mIAd_PCRMain.show();
                    MainActivity.this.mIAd_PCRMain.setAdListener(new AdListener() {
                        public void onAdClosed() {
                            MainActivity.this.startActivity(new Intent(MainActivity.this, PCRMainActivity.class));
                        }
                    });
                }
            }, 1000);
        } else if (itemId == R.id.mnu_xday_mover_bullish) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    if (MainActivity.this.mIAd_Xmove == null || !MainActivity.this.mIAd_Xmove.isLoaded()) {
                        Intent intent = new Intent(MainActivity.this, XDayMoverActivity.class);
                        intent.putExtra("bullorbear", "bull");
                        MainActivity.this.startActivity(intent);
                        return;
                    }
                    MainActivity.this.mIAd_Xmove.show();
                    MainActivity.this.mIAd_Xmove.setAdListener(new AdListener() {
                        public void onAdClosed() {
                            Intent intent = new Intent(MainActivity.this, XDayMoverActivity.class);
                            intent.putExtra("bullorbear", "bull");
                            MainActivity.this.startActivity(intent);
                        }
                    });
                }
            }, 1000);
        } else if (itemId == R.id.mnu_xday_mover_bearish) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    if (MainActivity.this.mIAd_Xmove == null || !MainActivity.this.mIAd_Xmove.isLoaded()) {
                        Intent intent = new Intent(MainActivity.this, XDayMoverActivity.class);
                        intent.putExtra("bullorbear", "bear");
                        MainActivity.this.startActivity(intent);
                        return;
                    }
                    MainActivity.this.mIAd_Xmove.show();
                    MainActivity.this.mIAd_Xmove.setAdListener(new AdListener() {
                        public void onAdClosed() {
                            Intent intent = new Intent(MainActivity.this, XDayMoverActivity.class);
                            intent.putExtra("bullorbear", "bear");
                            MainActivity.this.startActivity(intent);
                        }
                    });
                }
            }, 1000);
        } else if (itemId == R.id.mnu_strategies_premium) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    if (MainActivity.this.mIAd_StgMain == null || !MainActivity.this.mIAd_StgMain.isLoaded()) {
                        Intent intent = new Intent(MainActivity.this, ActivityStrategyMain.class);
                        intent.putExtra("strategytype", Integer.toString(1));
                        MainActivity.this.startActivity(intent);
                        return;
                    }
                    MainActivity.this.mIAd_StgMain.show();
                    MainActivity.this.mIAd_StgMain.setAdListener(new AdListener() {
                        public void onAdClosed() {
                            Intent intent = new Intent(MainActivity.this, ActivityStrategyMain.class);
                            intent.putExtra("strategytype", Integer.toString(1));
                            MainActivity.this.startActivity(intent);
                        }
                    });
                }
            }, 1000);
        } else if (itemId == R.id.mnu_strategies_tool) {
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        } else if (itemId == R.id.mnu_strategies_folio) {
            startActivity(new Intent(this, FinderFolio.class));
        } else if (itemId == R.id.mnu_about) {
            startActivity(new Intent(this, AboutApp.class));
        } else if (itemId == R.id.mnu_todays_call) {
            startActivity(new Intent(this, TodaysCallActivity.class));
        } else if (itemId == R.id.mnu_call_perf) {
            startActivity(new Intent(this, CallPerformanceMain.class));
        } else if (itemId == R.id.mnu_my_calls) {
            startActivity(new Intent(this, MyCallsActivity.class));
        }
        ((DrawerLayout) findViewById(R.id.drawer_layout)).closeDrawer((int) GravityCompat.START);
        return true;
    }

    private void LoadInterstitialAds() {
        InterstitialAd interstitialAd = new InterstitialAd(this);
        this.mIAd_BC = interstitialAd;
        interstitialAd.setAdUnitId(getString(R.string.ad_unit_id_broker_calls_inserstitial));
        this.mIAd_BC.loadAd(new AdRequest.Builder().build());
        InterstitialAd interstitialAd2 = new InterstitialAd(this);
        this.mIAd_ChartEOD = interstitialAd2;
        interstitialAd2.setAdUnitId(getString(R.string.ad_unit_id_chart_eod_inserstitial));
        this.mIAd_ChartEOD.loadAd(new AdRequest.Builder().build());
        InterstitialAd interstitialAd3 = new InterstitialAd(this);
        this.mIAd_OIDay = interstitialAd3;
        interstitialAd3.setAdUnitId(getString(R.string.ad_unit_id_oi_daywise_inserstitial));
        this.mIAd_OIDay.loadAd(new AdRequest.Builder().build());
        InterstitialAd interstitialAd4 = new InterstitialAd(this);
        this.mIAd_OIStock = interstitialAd4;
        interstitialAd4.setAdUnitId(getString(R.string.ad_unit_id_oi_stocks_inserstitial));
        this.mIAd_OIStock.loadAd(new AdRequest.Builder().build());
        InterstitialAd interstitialAd5 = new InterstitialAd(this);
        this.mIAd_OpenPos = interstitialAd5;
        interstitialAd5.setAdUnitId(getString(R.string.ad_unit_id_open_position_inserstitial));
        this.mIAd_OpenPos.loadAd(new AdRequest.Builder().build());
        InterstitialAd interstitialAd6 = new InterstitialAd(this);
        this.mIAd_MargnMain = interstitialAd6;
        interstitialAd6.setAdUnitId(getString(R.string.ad_unit_id_fno_margin_main_interstitial));
        this.mIAd_MargnMain.loadAd(new AdRequest.Builder().build());
        InterstitialAd interstitialAd7 = new InterstitialAd(this);
        this.mIAd_PCRMain = interstitialAd7;
        interstitialAd7.setAdUnitId(getString(R.string.ad_unit_id_fno_pcr_main_interstitial));
        this.mIAd_PCRMain.loadAd(new AdRequest.Builder().build());
        InterstitialAd interstitialAd8 = new InterstitialAd(this);
        this.mIAd_Xmove = interstitialAd8;
        interstitialAd8.setAdUnitId(getString(R.string.ad_unit_id_fno_xmover_interstitial));
        this.mIAd_Xmove.loadAd(new AdRequest.Builder().build());
        InterstitialAd interstitialAd9 = new InterstitialAd(this);
        this.mIAd_StgMain = interstitialAd9;
        interstitialAd9.setAdUnitId(getString(R.string.ad_unit_id_strategy_main_interstitial));
        this.mIAd_StgMain.loadAd(new AdRequest.Builder().build());
    }

    /* access modifiers changed from: protected */
    public MyGreeksApplication getGreekApplication() {
        return (MyGreeksApplication) getApplicationContext();
    }

    public void hideProgressBar() {
        this.progressBar.setVisibility(4);
    }

    /* access modifiers changed from: private */
    public boolean isServerBlistChkRequired() {
        String[] blistFlags = getGreekApplication().getBlistFlags();
        if (blistFlags == null || blistFlags.length < 2) {
            getGreekApplication().setBlistFlag(Constants.BLIST_DEFAULT_CODE);
            return true;
        }
        String str = blistFlags[0];
        String str2 = blistFlags[1];
        if (str.equals(Constants.BLIST_BANNED_CODE)) {
            return false;
        }
        long parseLong = Long.parseLong(str2);
        long timeInMillis = Calendar.getInstance().getTimeInMillis();
        if (!str.trim().equals(getGreekApplication().getJNICallResult_getBList()) || timeInMillis - parseLong >= Constants.SERVER_CHECK_INTERVAL) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: private */
    public void callServerBlist() throws Exception {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("installid", URLEncoder.encode(getGreekApplication().getAppInstallId(), "UTF-8"));
        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(this.webServiceUrl + "85").openConnection();
        httpURLConnection.setConnectTimeout(Constants.VOLLEY_RESPONSE_TIMEOUT);
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setRequestProperty("Accept-Charset", "UTF-8");
        DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
        dataOutputStream.writeBytes("&condition=");
        dataOutputStream.writeBytes(jSONObject.toString());
        dataOutputStream.flush();
        dataOutputStream.close();
        InputStream inputStream = httpURLConnection.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        String readLine = bufferedReader.readLine();
        if (readLine != null && readLine.trim().equalsIgnoreCase(Constants.BLIST_BANNED_CODE.trim())) {
            getGreekApplication().setBlistFlag(Constants.BLIST_BANNED_CODE);
        }
        if (inputStream != null) {
            inputStream.close();
        }
        bufferedReader.close();
        if (httpURLConnection != null) {
            httpURLConnection.disconnect();
        }
    }

    /* access modifiers changed from: private */
    public Fragment getCurrentContentFragment(FragmentManager fragmentManager) {
        return fragmentManager.findFragmentById(getContentFrameId());
    }

    private void setFragmentToActivity(FragmentManager fragmentManager, Fragment fragment) {
        fragmentManager.beginTransaction().replace(getContentFrameId(), fragment).commitAllowingStateLoss();
    }

    public FilterFragment getFilterFragment() {
        return this.filterFragment;
    }

    public StrategyFinderFragment getStrategyFragment() {
        return this.strategyFragment;
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        getSupportFragmentManager().findFragmentById(getContentFrameId()).onActivityResult(i, i2, intent);
    }

    public void replaceFragment(Fragment fragment, Bundle bundle, boolean z) {
        String simpleName = fragment.getClass().getSimpleName();
        if (bundle != null) {
            fragment.setArguments(bundle);
        }
        FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
        if (getCurrentFragment() != null) {
            beginTransaction.hide(getCurrentFragment());
        }
        beginTransaction.add(getContentFrameId(), fragment, simpleName);
        fragment.setRetainInstance(true);
        if (z) {
            beginTransaction.addToBackStack(simpleName);
        }
        try {
            beginTransaction.commitAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: package-private */
    public Fragment getCurrentFragment() {
        return getSupportFragmentManager().findFragmentById(getContentFrameId());
    }

    public void setErrorFragment() {
        ErrorFragment errorFragment = new ErrorFragment();
        this.progressBar.setVisibility(4);
        try {
            FragmentManager supportFragmentManager = getSupportFragmentManager();
            if (!(getCurrentContentFragment(supportFragmentManager) instanceof StrategyFinderFragment)) {
                setFragmentToActivity(supportFragmentManager, errorFragment);
            }
        } catch (Exception e) {
            String str = this.TAG;
            Log.d(str, "Error | Fragment replace " + e.toString());
        }
    }
}
