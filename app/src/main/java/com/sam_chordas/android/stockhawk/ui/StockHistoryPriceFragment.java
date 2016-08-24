package com.sam_chordas.android.stockhawk.ui;

import android.app.DatePickerDialog;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.OneoffTask;
import com.google.android.gms.gcm.PeriodicTask;
import com.google.android.gms.gcm.Task;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.HistoryColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;
import com.sam_chordas.android.stockhawk.service.StockHistoryDataService;
import com.sam_chordas.android.stockhawk.service.StockTaskService;
import com.sam_chordas.android.stockhawk.service.YQL;
import com.sam_chordas.android.stockhawk.service.model.History;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class StockHistoryPriceFragment extends Fragment implements View.OnClickListener{

    private Button startDateBtn;
    private Button endDateBtn;
    private Date startDate;
    private Date endDate;

    private LineChart mChart;

    private TextView startDateView;
    private TextView endDateView;
    private Calendar calendar;

    private HistoryObserver mHistoryObserver;

    private final static String DIALOG_DATE = "DialogDate";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mHistoryObserver = new HistoryObserver(new Handler(Looper.getMainLooper()));

        calendar = Calendar.getInstance();
        Bundle args = getArguments();
        args.putString("ticker", args.getString("ticker"));
        OneoffTask task = new OneoffTask.Builder()
                .setService(StockHistoryDataService.class)
                .setRequiredNetwork(Task.NETWORK_STATE_CONNECTED)
                .setExtras(args)
                .setExecutionWindow(0, 1)
                .setTag("whatever")
                .setRequiresCharging(false)
                .build();
        GcmNetworkManager.getInstance(getActivity()).schedule(task);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.stock_hictory_fragment, container, false);

        startDateView = (TextView) view.findViewById(R.id.history_start_date_field);
        endDateView = (TextView) view.findViewById(R.id.history_end_date_field);
        startDateBtn = (Button) view.findViewById(R.id.history_start_date_btn);
        endDateBtn = (Button) view.findViewById(R.id.history_end_date_btn);

        mChart = (LineChart) view.findViewById(R.id.stock_history_chart);

        startDateBtn.setOnClickListener(this);
        endDateBtn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getActivity().getContentResolver().registerContentObserver(QuoteProvider.History.CONTENT_URI, true, mHistoryObserver);
    }

    @Override
    public void onClick(View v) {
        DatePickerDialog d;

        switch (v.getId()){
            case R.id.history_start_date_btn:
                Log.i("BUTTON START DATE", "IS CLICKED");
                d = createDatePicker(startDate);
                startDate = null;
                d.show();
                break;

            case R.id.history_end_date_btn:
                Log.i("BUTTON END DATE", "IS CLICKED");
                d = createDatePicker(endDate);
                endDate = null;
                d.show();
                break;

        }

    }

    private DatePickerDialog createDatePicker(Date date) {
        if (date != null) {
            calendar.setTime(date);
        }
        return new DatePickerDialog(getActivity(), onDateSet,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
    }

    private Cursor queryData() {
        return getActivity().getContentResolver().query(
                QuoteProvider.History.CONTENT_URI, new String[] { HistoryColumns.CLOSE },
                HistoryColumns.DATE + " > ? AND " + HistoryColumns.DATE + " < ?",
                new String[] { YQL.SQL_FORMAT.format(startDate), YQL.SQL_FORMAT.format(endDate) },
                HistoryColumns.DATE + " ASC");
    }


    private void setData() {

        List<Entry> values = new ArrayList<Entry>();
        Cursor cursor = queryData();
        int x = 0;
        int closeIndex = cursor.getColumnIndex(HistoryColumns.CLOSE);
        while (cursor.moveToNext()) {
            values.add(new Entry(x++, cursor.getFloat(closeIndex)));
        }

        LineDataSet set1;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet)mChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
            mChart.invalidate();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(values, "DataSet 1");

            // set the line to be drawn like this "- - - - - -"
            set1.enableDashedLine(10f, 5f, 0f);
            set1.enableDashedHighlightLine(10f, 5f, 0f);
            set1.setColor(Color.BLACK);
            set1.setCircleColor(Color.BLACK);
            set1.setLineWidth(1f);
            set1.setCircleRadius(3f);
            set1.setDrawCircleHole(false);
            set1.setValueTextSize(9f);
            set1.setDrawFilled(true);


            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(set1); // add the datasets

            // create a data object with the datasets
            LineData data = new LineData(dataSets);

            // set data
            mChart.setData(data);
            mChart.invalidate();
        }
    }

    private DatePickerDialog.OnDateSetListener onDateSet = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            calendar.set((Calendar.YEAR), year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            if (startDate == null) {
                startDate = calendar.getTime();
                startDateView.setText(YQL.SQL_FORMAT.format(startDate));
            } else {
                endDate = calendar.getTime();
                endDateView.setText(YQL.SQL_FORMAT.format(endDate));
            }
            if (startDate != null && endDate != null) {
                setData();
            }
        }
    };



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().getContentResolver().unregisterContentObserver(mHistoryObserver);
    }

    public class HistoryObserver extends ContentObserver {

        public HistoryObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            this.onChange(selfChange,null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            setData();
        }
    }
}
