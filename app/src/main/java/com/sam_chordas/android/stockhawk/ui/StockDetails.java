package com.sam_chordas.android.stockhawk.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.OneoffTask;
import com.google.android.gms.gcm.Task;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.service.StockHistoryDataService;
import com.sam_chordas.android.stockhawk.service.StockTaskService;

import java.util.List;

public class StockDetails extends AppCompatActivity {

    private ViewPager mViewPager;

    private StockPagerAdapter mStockPagerAdapter;

    private Bundle stockDetails;

    public static final int NUM_PAGES = 3;

    private Intent mHistoryServiceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stock_details);

        mViewPager = (ViewPager) findViewById(R.id.stock_details_view_pager);
        mStockPagerAdapter = new StockPagerAdapter(getSupportFragmentManager());

        mViewPager.setAdapter(mStockPagerAdapter);

        stockDetails = getIntent().getExtras();
        Log.i("StokDetails", stockDetails.toString());

//        mHistoryServiceIntent = new Intent(this, StockHistoryDataService.class);
//        mHistoryServiceIntent.putExtras(stockDetails);
//        startService(mHistoryServiceIntent);
        OneoffTask task = new OneoffTask.Builder()
                .setService(StockHistoryDataService.class)
                .setRequiredNetwork(Task.NETWORK_STATE_CONNECTED)
                .setExtras(stockDetails)
                .setExecutionWindow(0, 100)
                .setTag("whatever")
                .setRequiresCharging(false)
                .build();
        GcmNetworkManager.getInstance(this).schedule(task);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.my_stocks, menu);

        return true;
    }

    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance (int sectionNumber) {

            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.stockpager_fragment, container, false);
            return rootView;
        }
    }

    public class StockPagerAdapter extends FragmentStatePagerAdapter {


        private StockGeneralFragment mGeneralFragment;
        private StockHistoryPriceFragment mHistoryPriceFragment;
        private StockNewsFragment mNewsFragment;

        public StockPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position){
                case 0:
                    if(mGeneralFragment == null) {
                        mGeneralFragment = new StockGeneralFragment();
                        mGeneralFragment.setArguments(stockDetails);
                    }
                    return mGeneralFragment;

                case 1:
                    if(mHistoryPriceFragment == null) {
                        mHistoryPriceFragment = new StockHistoryPriceFragment();
                        mHistoryPriceFragment.setArguments(stockDetails);
                    }
                    return mHistoryPriceFragment;

                case 2:
                    if(mNewsFragment == null) {
                        mNewsFragment = new StockNewsFragment();
                        mNewsFragment.setArguments(stockDetails);
                    }
                    return mNewsFragment;
            }

            return PlaceholderFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "General Info";
                case 1:
                    return "Historical Price";
                case 2:
                    return "News";
            }
            return null;
        }
    }

    @Override
    public void onBackPressed() {
        if (mViewPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
        }
    }

}


