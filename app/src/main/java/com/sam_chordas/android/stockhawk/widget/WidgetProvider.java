package com.sam_chordas.android.stockhawk.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.sam_chordas.android.stockhawk.R;

/**
 * Created by dmytrokaganovskyi on 8/24/16.
 */
public class WidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int id : appWidgetIds) {
            Intent intent = new Intent(context, RemoteAdapter.class);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
            views.setRemoteAdapter(R.id.widget_listview, intent);

            views.setEmptyView(R.id.widget_listview, R.id.widget_textview);


            appWidgetManager.updateAppWidget(id, views);
        }
    }
}
