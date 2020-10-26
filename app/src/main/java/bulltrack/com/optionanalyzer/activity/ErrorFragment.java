package bulltrack.com.optionanalyzer.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import bulltrack.com.optiongreeks13.R;

public class ErrorFragment extends Fragment {
    View view;

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_error, viewGroup, false);
        this.view = inflate;
        return inflate;
    }
}
