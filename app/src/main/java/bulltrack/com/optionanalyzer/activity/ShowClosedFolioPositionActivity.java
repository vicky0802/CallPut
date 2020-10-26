package bulltrack.com.optionanalyzer.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;
import bulltrack.com.optionanalyzer.adapter.PortfolioListAdapter;
import bulltrack.com.optionanalyzer.application.MyGreeksApplication;
import bulltrack.com.optionanalyzer.dao.GreekValues;
import bulltrack.com.optiongreeks13.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import java.util.List;

public class ShowClosedFolioPositionActivity extends AppCompatActivity {
    private Activity activity;
    PortfolioListAdapter adapter;
    List<GreekValues> listGreek;
    ListView lvGreeks;
    ProgressBar progressBar;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.show_portfolio);
        ListView listView = (ListView) findViewById(R.id.lv_portfolio_items);
        this.lvGreeks = listView;
        listView.setEmptyView(findViewById(R.id.empty));
        ProgressBar progressBar2 = (ProgressBar) findViewById(R.id.progressbar_portfolio);
        this.progressBar = progressBar2;
        progressBar2.setVisibility(4);
        this.listGreek = getGreekApplication().getAllPortfolioItems(0);
        this.activity = this;
        ((AdView) findViewById(R.id.adView)).loadAd(new AdRequest.Builder().setRequestAgent("android_studio:ad_template").build());
        if (this.listGreek != null) {
            PortfolioListAdapter portfolioListAdapter = new PortfolioListAdapter(this.activity, this.listGreek);
            this.adapter = portfolioListAdapter;
            this.lvGreeks.setAdapter(portfolioListAdapter);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /* access modifiers changed from: protected */
    public MyGreeksApplication getGreekApplication() {
        return (MyGreeksApplication) getApplicationContext();
    }
}
