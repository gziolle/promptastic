package com.gziolle.promptastic.widget;

import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViewsService;

public class ScriptListWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Log.d("Ziollera", "service onGetViewFactory");
        return new ScriptListWidgetProvider(this, intent);
    }
}
