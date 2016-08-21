package com.sam_chordas.android.stockhawk.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sam_chordas.android.stockhawk.R;

public class StockPagerFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private int pageNumber;

    static StockPagerFragment newInstance(int page) {
        StockPagerFragment pagerFragment = new StockPagerFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(ARG_SECTION_NUMBER, page);
        pagerFragment.setArguments(arguments);

        return pagerFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNumber = getArguments().getInt(ARG_SECTION_NUMBER);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.stockpager_fragment, container, false);
        TextView tvPager = (TextView) view.findViewById(R.id.section_label);
        tvPager.setText("Hello");

        return view;
    }
}
