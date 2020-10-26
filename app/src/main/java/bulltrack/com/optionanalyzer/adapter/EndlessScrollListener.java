package bulltrack.com.optionanalyzer.adapter;

import android.widget.AbsListView;

public abstract class EndlessScrollListener implements AbsListView.OnScrollListener {
    private int currentPage = 0;
    private boolean loading = true;
    private int previousTotalItemCount = 0;
    private int startingPageIndex = 0;
    private int visibleThreshold = 10;

    public abstract boolean onLoadMore(int i, int i2);

    public void onScrollStateChanged(AbsListView absListView, int i) {
    }

    public EndlessScrollListener() {
    }

    public EndlessScrollListener(int i) {
        this.visibleThreshold = i;
    }

    public EndlessScrollListener(int i, int i2) {
        this.visibleThreshold = i;
        this.startingPageIndex = i2;
        this.currentPage = i2;
    }

    public void onScroll(AbsListView absListView, int i, int i2, int i3) {
        if (i3 < this.previousTotalItemCount) {
            this.currentPage = this.startingPageIndex;
            this.previousTotalItemCount = i3;
            if (i3 == 0) {
                this.loading = true;
            }
        }
        if (this.loading && i3 > this.previousTotalItemCount) {
            this.loading = false;
            this.previousTotalItemCount = i3;
            this.currentPage++;
        }
        if (!this.loading && i + i2 + this.visibleThreshold >= i3) {
            this.loading = onLoadMore(this.currentPage + 1, i3);
        }
    }
}
