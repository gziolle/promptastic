package com.gziolle.promptastic.widget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.gziolle.promptastic.R;
import com.gziolle.promptastic.data.model.Script;
import com.gziolle.promptastic.firebase.FirebaseAuthManager;
import com.gziolle.promptastic.util.Constants;
import com.gziolle.promptastic.util.Utils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public class ScriptListWidgetProvider implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private Intent mIntent;

    private List<Script> mScriptList = new ArrayList<>();

    public ScriptListWidgetProvider(Context context, Intent intent){
        this.mContext = context;
        this.mIntent = intent;
    }

    @Override
    public void onCreate() {
        provideData();
    }

    @Override
    public void onDataSetChanged() { Log.d("Ziollera", "onDataSetChanged"); }

    @Override
    public void onDestroy() {
    }

    @Override
    public int getCount() {
        return mScriptList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Log.d("Ziollera", "widgetprovider: getViewAt");
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.widget_script_list_item);
        remoteViews.setTextViewText(R.id.tv_widget_script_title, mScriptList.get(position).getTitle());
        return remoteViews;
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
        return true;
    }

    private void provideData(){
        Log.d("Ziollera", "provideData init");
        mScriptList.clear();

        String user = FirebaseAuthManager.getInstance().getFirebaseUserId();

        if(!TextUtils.isEmpty(user)){
            Log.d("Ziollera", "user: " + user);
            DatabaseReference reference = Utils.getFirebaseDatabase().getReference().child(Constants.PATH_USERS +
                    FirebaseAuthManager.getInstance().getFirebaseUserId() + Constants.PATH_SCRIPTS);

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(final DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Log.d("Ziollera", "onDataChanged: " + snapshot.child(Constants.KEY_TITLE).getValue().toString());
                        Script script = new Script(snapshot.child(Constants.KEY_TITLE).getValue().toString(),
                                snapshot.child(Constants.KEY_CONTENT).getValue().toString());
                        mScriptList.add(script);
                    }
                    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(mContext);
                    ComponentName thisWidget = new ComponentName(mContext, ScriptListWidget.class);
                    int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
                    AppWidgetManager.getInstance(mContext)
                            .notifyAppWidgetViewDataChanged(appWidgetIds, R.id.appwidget_list);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {}
            });
        }else {
            Log.d("Ziollera", "user == null");
        }
    }
}
