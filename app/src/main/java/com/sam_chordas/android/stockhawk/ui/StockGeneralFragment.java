package com.sam_chordas.android.stockhawk.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sam_chordas.android.stockhawk.R;

/**
 * Created by dmytrokaganovskyi on 8/5/16.
 */
public class StockGeneralFragment extends Fragment {

    private TextView tickerName;
    private TextView changeAbs;
    private TextView changePercent;
    private TextView currentPrice;
    private int isUp;
    private int color;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {



        View view = inflater.inflate(R.layout.stock_general_fragment, container, false);

        Bundle bundle = getArguments();


        tickerName = (TextView) view.findViewById(R.id.stock_ticker_name);
        changeAbs = (TextView) view.findViewById(R.id.stock_change_abs);
        changePercent = (TextView) view.findViewById(R.id.stock_change_percent);
        currentPrice = (TextView) view.findViewById(R.id.stock_current_price);


        if (bundle != null) {
            isUp = bundle.getInt("is_up");
            if (isUp == 1) {
                color = Color.GREEN;
            }
            else {
                color = Color.RED;
            }
            tickerName.setText(bundle.getString("ticker"));
            tickerName.setTextColor(color);
            changeAbs.setText(bundle.getString("abs"));
            changeAbs.setTextColor(color);
            changePercent.setText(bundle.getString("percent"));
            changePercent.setTextColor(color);
            currentPrice.setText(bundle.getString("bid"));
            currentPrice.setTextColor(color);
        }

        return view;
    }
}
