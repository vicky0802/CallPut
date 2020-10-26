package bulltrack.com.optionanalyzer.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import bulltrack.com.optiongreeks13.R;
import java.util.List;

public class RVAdapterStock extends RecyclerView.Adapter<MyViewHolder> {
    private int globalPos = -1;
    private List<String> horizontalList;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(TextView textView);
    }

    public void setGlobalPos(int i) {
        this.globalPos = i;
    }

    public RVAdapterStock(List<String> list, int i, OnItemClickListener onItemClickListener) {
        this.horizontalList = list;
        this.listener = onItemClickListener;
        this.globalPos = i;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycle_horizontal_item, viewGroup, false));
    }

    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
        myViewHolder.bind(this.horizontalList.get(i), this.listener);
        if (i == this.globalPos) {
            myViewHolder.txtView.setSelected(true);
        } else {
            myViewHolder.txtView.setSelected(false);
        }
    }

    public int getItemCount() {
        return this.horizontalList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtView;

        public MyViewHolder(View view) {
            super(view);
            this.txtView = (TextView) view.findViewById(R.id.tv_recycle_horizontal_item_display);
        }

        public void bind(String str, final OnItemClickListener onItemClickListener) {
            this.txtView.setText(str);
            this.itemView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    onItemClickListener.onItemClick(MyViewHolder.this.txtView);
                }
            });
        }
    }

    public void updateList(List<String> list) {
        this.horizontalList = list;
        notifyDataSetChanged();
    }
}
