package com.sam_chordas.android.stockhawk.service;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.os.RemoteException;
import android.util.Log;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.TaskParams;
import com.sam_chordas.android.stockhawk.data.HistoryColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;
import com.sam_chordas.android.stockhawk.service.model.History;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by dmytrokaganovskyi on 8/15/16.
 */
public class StockHistoryDataService extends GcmTaskService {

    private Context mContext;

    private String created;
    private NetworkManager nm = new NetworkManager();

    public StockHistoryDataService() {
    }

    public StockHistoryDataService(boolean isUpdate, Context context) {
        mContext = context;
    }

    @Override
    public int onRunTask(TaskParams taskParams) {
        Log.i("Service History Task", taskParams.toString());

        mContext = getApplicationContext();
        List<History> historyList = Collections.emptyList();

        String ticker = taskParams.getExtras().getString("ticker");
        Cursor historyQueryCursor = mContext.getContentResolver().query(QuoteProvider.History.CONTENT_URI,
                new String[]{HistoryColumns.CREATED},
                HistoryColumns.SYMBOL + " = ?", new String[]{ticker},
                HistoryColumns.CREATED + " DESC");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -2);
        Date startDate = calendar.getTime();
        boolean shouldUpdate = true;
        created = YQL.SQL_FORMAT.format(new Date());
        if (historyQueryCursor != null) {
            if (historyQueryCursor.moveToFirst()) {
                int columnIndex = historyQueryCursor.getColumnIndex(HistoryColumns.CREATED);
                if (created.equals(historyQueryCursor.getString(columnIndex))) {
                    shouldUpdate = false;
                } else {
                    Cursor cursor = mContext.getContentResolver().query(QuoteProvider.History.CONTENT_URI, new String[] { HistoryColumns.DATE},
                            null, null, HistoryColumns.DATE + " DESC");
                    if (cursor != null && cursor.moveToFirst()) {
                        int dateColumnIndex = cursor.getColumnIndex(HistoryColumns.DATE);
                        String date = cursor.getString(dateColumnIndex);
                        try {
                            calendar.setTime(YQL.SQL_FORMAT.parse(date));
                            calendar.add(Calendar.DAY_OF_YEAR, 1);
                            startDate = calendar.getTime();
                        } catch (ParseException e) {
                            Log.e("tag", e.getMessage(), e);
                        }
                    }
                }
            }
        }

        Log.d("tag", "should update " + shouldUpdate);

        if (shouldUpdate) {
            Log.d("tag", "query data from " + startDate + " to " + new Date());
            Log.d("tag", "ticker = " + taskParams.getExtras().getString("ticker") + " startDate = " + startDate);
            historyList = nm.getHistoricalData(taskParams.getExtras().getString("ticker"), startDate, new Date());
        }

        if (historyList.isEmpty()) {
            return GcmNetworkManager.RESULT_RESCHEDULE;
        }

        try {
            ArrayList<ContentProviderOperation> batch = new ArrayList<>();
            for (History history : historyList) {
                batch.add(buildBatchOperation(history));
            }
            mContext.getContentResolver().applyBatch(QuoteProvider.AUTHORITY, batch);
        } catch (RemoteException | OperationApplicationException e) {
            Log.e("tag", "Error applying batch insert", e);
        }


        return GcmNetworkManager.RESULT_SUCCESS;
    }

    private ContentProviderOperation buildBatchOperation(History history) {
        ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert(
                QuoteProvider.History.CONTENT_URI);
        builder.withValue(HistoryColumns.SYMBOL, history.getSymbol());
        builder.withValue(HistoryColumns.DATE, history.getDate());
        builder.withValue(HistoryColumns.CLOSE, history.getClosed());
        builder.withValue(HistoryColumns.HIGH, history.getHigh());
        builder.withValue(HistoryColumns.LOW, history.getLow());
        builder.withValue(HistoryColumns.CREATED, created);
        return builder.build();
    }
}
