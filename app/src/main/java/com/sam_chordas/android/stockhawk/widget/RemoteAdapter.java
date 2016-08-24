package com.sam_chordas.android.stockhawk.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;
import com.sam_chordas.android.stockhawk.rest.Utils;

/**
 * Created by dmytrokaganovskyi on 8/24/16.
 */
public class RemoteAdapter extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ViewsFactory(this);
    }

    private static class ViewsFactory implements RemoteViewsFactory {

        private Context context;
        private Cursor c;

        private ViewsFactory(Context context) {
            this.context = context;
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
            releaseCursor();
            c = context.getContentResolver().query(QuoteProvider.Quotes.CONTENT_URI,
                    new String[]{ QuoteColumns._ID, QuoteColumns.SYMBOL, QuoteColumns.NAME, QuoteColumns.BIDPRICE,
                            QuoteColumns.PERCENT_CHANGE, QuoteColumns.CHANGE, QuoteColumns.ISUP, QuoteColumns.EBITDA,
                            QuoteColumns.OPEN, QuoteColumns.AVEVOLUME, QuoteColumns.VOLUME, QuoteColumns.MRKTCAP,
                            QuoteColumns.YEARLOW, QuoteColumns.YEARHIGH},
                    QuoteColumns.ISCURRENT + " = ?",
                    new String[]{"1"},
                    null);
        }

        @Override
        public void onDestroy() {
            releaseCursor();
        }

        private void releaseCursor() {
            if (c != null) {
                c.close();
            }
        }

        @Override
        public int getCount() {
            return c != null ? c.getCount() : 0;
        }

        @Override
        public RemoteViews getViewAt(int position) {
            c.moveToPosition(position);

            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.list_item_quote);
            rv.setTextViewText(R.id.bid_price, c.getString(c.getColumnIndex("bid_price")));
            rv.setTextViewText(R.id.stock_symbol, c.getString(c.getColumnIndex("symbol")));

            if (c.getInt(c.getColumnIndex("is_up")) == 1) {
                rv.setTextColor(R.id.change, Color.GREEN);
            } else {
                rv.setTextColor(R.id.change, Color.RED);
            }
            if (Utils.showPercent) {
                rv.setTextViewText(R.id.change, c.getString(c.getColumnIndex("percent_change")));
            } else {
                rv.setTextViewText(R.id.change, c.getString(c.getColumnIndex("change")));
            }
            return rv;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }
    }
}
