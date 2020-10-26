package bulltrack.com.optionanalyzer.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import bulltrack.com.optionanalyzer.constants.Constants;
import bulltrack.com.optiongreeks13.R;
import com.jiangjiesheng.slidingmenu.SlidingMenu;
import com.jiangjiesheng.slidingmenu.app.SlidingFragmentActivity;

public class FinderMainActivity extends SlidingFragmentActivity {
    private static final boolean AUTO_HIDE = true;
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;
    private static final int UI_ANIMATION_DELAY = 300;
    Activity activity;
    /* access modifiers changed from: private */
    public View mContentView;
    /* access modifiers changed from: private */
    public View mControlMenuFrame;
    /* access modifiers changed from: private */
    public View mControlsView;
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        public boolean onTouch(View view, MotionEvent motionEvent) {
            Toast.makeText(FinderMainActivity.this.activity, "ontouch", 1).show();
            FinderMainActivity.this.delayedHide(3000);
            return false;
        }
    };
    private final Handler mHideHandler = new Handler();
    private final Runnable mHidePart2Runnable = new Runnable() {
        public void run() {
            FinderMainActivity.this.mContentView.setSystemUiVisibility(4871);
            FinderMainActivity.this.mControlMenuFrame.setSystemUiVisibility(4871);
        }
    };
    private final Runnable mHideRunnable = new Runnable() {
        public void run() {
            FinderMainActivity.this.hide();
        }
    };
    private final Runnable mShowPart2Runnable = new Runnable() {
        public void run() {
            FinderMainActivity.this.mControlsView.setVisibility(0);
        }
    };
    private boolean mVisible;
    private SlidingMenu menu;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.activity = this;
        this.menu = getSlidingMenu();
        getSlidingMenu().setMode(2);
        getSlidingMenu().setTouchModeAbove(1);
        getSlidingMenu().setBehindWidth(Constants.TODAYS_CALL_STG_ID);
        getSlidingMenu().setMode(1);
        setContentView((int) R.layout.activity_strategyfinder_main);
        setBehindContentView((int) R.layout.filter_menu_frame);
        getSupportFragmentManager().beginTransaction().replace(R.id.strategy_content_frame, new StrategyFinderFragment()).commit();
        getSlidingMenu().setSecondaryMenu((int) R.layout.menu_frame_two);
        getSlidingMenu().setSecondaryShadowDrawable((int) R.drawable.shadowright);
        getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame_two, new FilterFragment()).commit();
        this.mVisible = AUTO_HIDE;
        this.mContentView = findViewById(R.id.fullscreen_content);
        this.mControlMenuFrame = findViewById(R.id.menu_frame);
        this.mContentView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                FinderMainActivity.this.toggle();
            }
        });
        this.mControlsView.invalidate();
        this.mContentView.invalidate();
    }

    public void onPostCreate(Bundle bundle) {
        super.onPostCreate(bundle);
        delayedHide(100);
    }

    public void toggle() {
        if (this.mVisible) {
            hide();
        } else {
            show();
        }
    }

    /* access modifiers changed from: private */
    public void hide() {
        this.mControlsView.setVisibility(8);
        this.mVisible = false;
        this.mHideHandler.removeCallbacks(this.mShowPart2Runnable);
        this.mHideHandler.postDelayed(this.mHidePart2Runnable, 300);
    }

    public void toggleUiFlags() {
        if (this.mVisible) {
            hideControls();
        } else {
            showControls();
        }
    }

    public void hideControls() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() & -2 & -5 & -3 & -2049);
    }

    public void showControls() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() | 1 | 4 | 2 | 2048);
    }

    private void show() {
        this.mContentView.setSystemUiVisibility(1536);
        this.mVisible = AUTO_HIDE;
        this.mHideHandler.removeCallbacks(this.mHidePart2Runnable);
        this.mHideHandler.postDelayed(this.mShowPart2Runnable, 300);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(1 | decorView.getSystemUiVisibility() | 4 | 2 | 2048);
    }

    /* access modifiers changed from: private */
    public void delayedHide(int i) {
        this.mHideHandler.removeCallbacks(this.mHideRunnable);
        this.mHideHandler.postDelayed(this.mHideRunnable, (long) i);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() | 1 | 4 | 2 | 2048);
    }
}
