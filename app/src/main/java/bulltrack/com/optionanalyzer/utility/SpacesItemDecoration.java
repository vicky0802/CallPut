package bulltrack.com.optionanalyzer.utility;

import android.graphics.Rect;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int mHorizontalSpacing = 0;
    private boolean mIncludeEdge = false;
    private int mVerticalSpacing = 0;

    public SpacesItemDecoration(int i, int i2, boolean z) {
        this.mHorizontalSpacing = i;
        this.mVerticalSpacing = i2;
        this.mIncludeEdge = z;
    }

    public void getItemOffsets(Rect rect, View view, RecyclerView recyclerView, RecyclerView.State state) {
        super.getItemOffsets(rect, view, recyclerView, state);
        int childAdapterPosition = recyclerView.getChildAdapterPosition(view);
        rect.left = this.mHorizontalSpacing;
        rect.right = this.mHorizontalSpacing;
        if (this.mIncludeEdge) {
            if (childAdapterPosition == 0) {
                rect.top = this.mVerticalSpacing;
            }
            rect.bottom = this.mVerticalSpacing;
        } else if (childAdapterPosition > 0) {
            rect.top = this.mVerticalSpacing;
        }
    }
}
