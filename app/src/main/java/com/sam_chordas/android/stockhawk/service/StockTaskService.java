package com.sam_chordas.android.stockhawk.service;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.os.RemoteException;
import android.util.Log;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.TaskParams;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;
import com.sam_chordas.android.stockhawk.rest.Utils;
import com.sam_chordas.android.stockhawk.service.model.Stock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by sam_chordas on 9/30/15.
 * The GCMTask service is primarily for periodic tasks. However, OnRunTask can be called directly
 * and is used for the initialization and adding task as well.
 */
public class StockTaskService extends GcmTaskService {
    private String LOG_TAG = StockTaskService.class.getSimpleName();

    private Context mContext;

    private boolean isUpdate;

    private NetworkManager nm = new NetworkManager();

    public StockTaskService() {
    }

    public StockTaskService(Context context) {
        mContext = context;
    }

    @Override
    public int onRunTask(TaskParams params) {
        Cursor initQueryCursor;
        if (mContext == null) {
            mContext = this;
        }

        List<Stock> stocksToUpdate = Collections.emptyList();

        if (params.getTag().equals("init") || params.getTag().equals("periodic")) {
            isUpdate = true;
            initQueryCursor = mContext.getContentResolver().query(QuoteProvider.Quotes.CONTENT_URI,
                    new String[]{"Distinct " + QuoteColumns.SYMBOL}, null,
                    null, null);
            if (initQueryCursor != null) {
                if (initQueryCursor.getCount() == 0) {
                    stocksToUpdate = nm.getStocks("YHOO", "AAPL", "GOOG", "MSFT");
                }
                initQueryCursor.close();
            }
        } else if (params.getTag().equals("add")) {
            isUpdate = false;
            String stockInput = params.getExtras().getString("symbol");
            stocksToUpdate = nm.getStocks(stockInput);
        }
        if (stocksToUpdate.isEmpty()) {
            return GcmNetworkManager.RESULT_FAILURE;
        }
        try {
            ContentValues contentValues = new ContentValues();
            // update ISCURRENT to 0 (false) so new data is current
            if (isUpdate) {
                contentValues.put(QuoteColumns.ISCURRENT, 0);
                mContext.getContentResolver()
                        .update(QuoteProvider.Quotes.CONTENT_URI,
                                contentValues,
                                null, null);
            }
            ArrayList<ContentProviderOperation> batch = new ArrayList<>();
            for (Stock stock : stocksToUpdate) {
                if (stock.isValid()) {
                    batch.add(buildBatchOperation(stock));
                }
            }
            mContext.getContentResolver().applyBatch(QuoteProvider.AUTHORITY, batch);
        } catch (RemoteException | OperationApplicationException e) {
            Log.e(LOG_TAG, "Error applying batch insert", e);
        }

        return GcmNetworkManager.RESULT_SUCCESS;
    }

    private static ContentProviderOperation buildBatchOperation(Stock stock) {
        ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert(
                QuoteProvider.Quotes.CONTENT_URI);
        String change = stock.getChange();
        builder.withValue(QuoteColumns.SYMBOL, stock.getSymbol());
        builder.withValue(QuoteColumns.NAME, stock.getName());
        builder.withValue(QuoteColumns.OPEN, stock.getOpen());
        builder.withValue(QuoteColumns.AVEVOLUME, stock.getAveVolume());
        builder.withValue(QuoteColumns.VOLUME, stock.getVolume());
        builder.withValue(QuoteColumns.MRKTCAP, stock.getMrktCap());
        builder.withValue(QuoteColumns.EBITDA, stock.getEbitda());
        builder.withValue(QuoteColumns.YEARLOW, stock.getYearLow());
        builder.withValue(QuoteColumns.YEARHIGH, stock.getYearHigh());
        builder.withValue(QuoteColumns.BIDPRICE, Utils.truncateBidPrice(stock.getBid()));
        builder.withValue(QuoteColumns.PERCENT_CHANGE, Utils.truncateChange(
                stock.getChangeInPercent(), true));
        builder.withValue(QuoteColumns.CHANGE, Utils.truncateChange(change, false));
        builder.withValue(QuoteColumns.ISCURRENT, 1);

        if (change.charAt(0) == '-') {
            builder.withValue(QuoteColumns.ISUP, 0);
        } else {
            builder.withValue(QuoteColumns.ISUP, 1);
        }
        return builder.build();
    }

}


