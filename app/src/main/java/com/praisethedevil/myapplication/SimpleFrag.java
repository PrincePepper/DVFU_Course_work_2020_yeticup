package com.praisethedevil.myapplication;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;


public class SimpleFrag extends Fragment {

    private static final String KEY = "KEY";
    private SimpleClass simpleElem;

    public SimpleFrag() {
        // Required empty public constructor
    }

    public static SimpleFrag newInstance(SimpleClass elem) {
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
            simpleElem = (SimpleClass) getArguments().getSerializable(KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_simple, container, false);
        TextView textView = v.findViewById(R.id.textView8);
        TextView textView2 = v.findViewById(R.id.textView9);
        ImageView imageView = v.findViewById(R.id.imageView3);
        if (simpleElem.getDr() == null) {
            imageView.setImageURI(simpleElem.getUri());
        } else {
            imageView.setImageDrawable(simpleElem.getDr());
        }
        textView.setText(simpleElem.getName());
        textView2.setText(simpleElem.getDate());

        return v;
    }
}