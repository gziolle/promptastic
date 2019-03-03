/*
 * Copyright (C) 2019 Guilherme Ziolle
 */

package com.gziolle.promptastic.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.RemoteViews;

import com.gziolle.promptastic.R;
import com.gziolle.promptastic.data.model.Script;
import com.gziolle.promptastic.ui.SplashActivity;
import com.gziolle.promptastic.util.Constants;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

/**
 * App Widget which displays the last script played by the user.
 */

public class ScriptWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, Script script) {
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.script_widget);
        Intent intent;
        if(script != null){
            views.setTextViewText(R.id.tv_widget_last_played_script, context.getString(R.string.widget_last_played_script));
            views.setTextViewText(R.id.tv_widget_script_title, script.getTitle());

        } else{
            views.setTextViewText(R.id.tv_widget_last_played_script, "");
            views.setTextViewText(R.id.tv_widget_script_title, context.getString(R.string.widget_no_script));
        }

        intent = new Intent(context, SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.widget_title, pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, null);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
        LocalBroadcastManager.getInstance(context).registerReceiver(this, new IntentFilter(Constants.ACTION_UPDATE_WIDGET));
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
        LocalBroadcastManager.getInstance(context).unregisterReceiver(this);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if(Constants.ACTION_UPDATE_WIDGET.equals(intent.getAction())){
            Script script = intent.getParcelableExtra(Constants.KEY_SCRIPT_EXTRA);
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context.getApplicationContext());
            ComponentName thisWidget = new ComponentName(context, ScriptWidget.class);

            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
            if(appWidgetIds != null && appWidgetIds.length > 0){
                for(int id : appWidgetIds){
                    updateAppWidget(context, appWidgetManager, id, script);
                }
            }
        }

    }
}

