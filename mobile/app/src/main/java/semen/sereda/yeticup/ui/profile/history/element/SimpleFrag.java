package semen.sereda.yeticup.ui.profile.history.element;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import semen.sereda.yeticup.R;


public class SimpleFrag extends Fragment {

    private static final String KEY = "KEY";
    private HistoryClass simpleElem;

    public SimpleFrag() {
        // Required empty public constructor
    }

    public static SimpleFrag newInstance(HistoryClass elem) {
        SimpleFrag fragment = new SimpleFrag();
        Bundle args = new Bundle();
        args.putSerializable(KEY, elem);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            simpleElem = (HistoryClass) getArguments().getSerializable(KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.model_history_profile, container, false);
        TextView textView = v.findViewById(R.id.name_participants);
        TextView textView2 = v.findViewById(R.id.role);
        TextView textView3 = v.findViewById(R.id.place);
        textView.setText(simpleElem.name);
        textView2.setText(simpleElem.role);
        textView3.setText(simpleElem.date);
        return v;
    }
}