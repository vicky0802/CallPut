package bulltrack.com.optionanalyzer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import androidx.core.view.ViewCompat;
import bulltrack.com.optiongreeks13.R;
import com.exblr.dropdownmenu.DropdownListItem;
import java.util.List;

public class GridViewAdapter extends BaseAdapter {
    /* access modifiers changed from: private */
    public Context mContext;
    /* access modifiers changed from: private */
    public List<DropdownListItem> mList;

    public long getItemId(int i) {
        return 0;
    }

    public GridViewAdapter(Context context, List<DropdownListItem> list) {
        this.mContext = context;
        this.mList = list;
    }

    public int getCount() {
        return this.mList.size();
    }

    public DropdownListItem getItem(int i) {
        return this.mList.get(i);
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view != null) {
            viewHolder = (ViewHolder) view.getTag();
        } else {
            view = LayoutInflater.from(this.mContext).inflate(R.layout.ddm_custom_content_item, (ViewGroup) null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }
        viewHolder.bind(i);
        return view;
    }

    public DropdownListItem setSelectedItem(int i) {
        int i2 = 0;
        while (i2 < this.mList.size()) {
            this.mList.get(i2).setSelected(i == i2);
            i2++;
        }
        notifyDataSetChanged();
        return this.mList.get(i);
    }

    private class ViewHolder {
        private TextView mTextView;

        public ViewHolder(View view) {
            this.mTextView = (TextView) view.findViewById(R.id.ddm_custom_content_item_tv);
        }

        public void bind(int i) {
            DropdownListItem dropdownListItem = (DropdownListItem) GridViewAdapter.this.mList.get(i);
            this.mTextView.setText(dropdownListItem.getText());
            if (dropdownListItem.isSelected()) {
                this.mTextView.setTextColor(GridViewAdapter.this.mContext.getResources().getColor(R.color.colorPrimary));
                this.mTextView.setBackgroundResource(R.drawable.rounded_box_selected);
                return;
            }
            this.mTextView.setTextColor(ViewCompat.MEASURED_STATE_MASK);
            this.mTextView.setBackgroundResource(R.drawable.rounded_box_normal);
        }
    }
}
