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

public class StockGeneralFragment extends Fragment {

    private TextView tickerName;
    private TextView companyName;
    private TextView changeAbs;
    private TextView changePercent;
    private TextView currentPrice;
    private int isUp;
    private int color;

    private TextView dayOpen;
    private TextView aveVolume;
    private TextView dayVolume;
    private TextView mrktCap;
    private TextView yearLow;
    private TextView yearHigh;
    private TextView ebitda;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {



        View view = inflater.inflate(R.layout.stock_general_fragment, container, false);

        Bundle bundle = getArguments();

        //TODO: implement ButterKnife

        tickerName = (TextView) view.findViewById(R.id.stock_ticker_name);
        changeAbs = (TextView) view.findViewById(R.id.stock_change_abs);
        changePercent = (TextView) view.findViewById(R.id.stock_change_percent);
        currentPrice = (TextView) view.findViewById(R.id.stock_current_price);
        companyName = (TextView) view.findViewById(R.id.stock_company_name);

        dayOpen = (TextView) view.findViewById(R.id.stock_day_open);
        aveVolume = (TextView) view.findViewById(R.id.ave_day_volume);
        dayVolume = (TextView) view.findViewById(R.id.current_volume);
        mrktCap = (TextView) view.findViewById(R.id.stock_mrkt_cap);
        yearLow = (TextView) view.findViewById(R.id.year_low);
        yearHigh = (TextView) view.findViewById(R.id.year_high);
        ebitda = (TextView) view.findViewById(R.id.stock_ebitda);

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
            companyName.setText(bundle.getString("Name"));
            changeAbs.setText(bundle.getString("abs"));
            changeAbs.setTextColor(color);
            changePercent.setText(bundle.getString("percent"));
            changePercent.setTextColor(color);
            currentPrice.setText(bundle.getString("bid"));
            currentPrice.setTextColor(color);

            dayOpen.setText(bundle.getString("Open"));
            aveVolume.setText(bundle.getString("AverageDailyVolume"));
            dayVolume.setText(bundle.getString("Volume"));
            mrktCap.setText(bundle.getString("MarketCapitalization"));
            yearLow.setText(bundle.getString("YearLow"));
            yearHigh.setText(bundle.getString("YearHigh"));
            ebitda.setText(bundle.getString("EBITDA"));

        }

        return view;
    }
}
