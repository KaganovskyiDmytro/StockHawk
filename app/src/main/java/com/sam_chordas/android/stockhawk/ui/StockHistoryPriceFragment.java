package com.sam_chordas.android.stockhawk.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.OneoffTask;
import com.google.android.gms.gcm.PeriodicTask;
import com.google.android.gms.gcm.Task;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.service.StockHistoryDataService;
import com.sam_chordas.android.stockhawk.service.StockTaskService;
import com.sam_chordas.android.stockhawk.service.YQL;

import java.util.Calendar;
import java.util.Date;

public class StockHistoryPriceFragment extends Fragment implements View.OnClickListener{

    private Button startDateBtn;
    private Button endDateBtn;
    private Date startDate;
    private Date endDate;

    private TextView startDateView;
    private TextView endDateView;
    private Calendar calendar;

    private final static String DIALOG_DATE = "DialogDate";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        calendar = Calendar.getInstance();
        Bundle args = getArguments();
        args.putString("ticker", args.getString("ticker"));
        OneoffTask task = new OneoffTask.Builder()
                .setService(StockHistoryDataService.class)
                .setRequiredNetwork(Task.NETWORK_STATE_CONNECTED)
                .setExtras(args)
                .setExecutionWindow(1, 4)
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

        startDateBtn.setOnClickListener(this);
        endDateBtn.setOnClickListener(this);

        return view;
    }
    @Override
    public void onClick(View v) {
        DatePickerDialog d;

        switch (v.getId()){
            case R.id.history_start_date_btn:
                Log.i("BUTTON START DATE", "IS CLICKED");
                d = new DatePickerDialog(getActivity(), onDateSet, 2000, 12, 1);
                startDate = null;
                d.show();
                break;

            case R.id.history_end_date_btn:
                Log.i("BUTTON END DATE", "IS CLICKED");

                calendar.setTime(endDate);
                d = new DatePickerDialog(getActivity(), onDateSet, 2000, 12, 1);
                endDate = null;
                d.show();
                break;

        }

    }

    private DatePickerDialog.OnDateSetListener onDateSet = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            calendar.set((Calendar.YEAR), year);
            if (startDate == null) {
                startDate = calendar.getTime();
                startDateView.setText(YQL.SQL_FORMAT.format(startDate));
            } else {
                endDate = calendar.getTime();
                endDateView.setText(YQL.SQL_FORMAT.format(endDate));
            }
            updateChart();
        }
    };

    //SELECT Price FROM History WHERE Date >+ start AND DAte <= end ORDER BY DAte ASC
    private void updateChart() {
        //int x = 0;
        //Point point = new  Point(x++, cursor.getInt("Price"))
    }

}
