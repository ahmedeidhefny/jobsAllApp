package com.udacity.ahmed_eid.jobsallapp.Widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.firebase.auth.FirebaseAuth;
import com.udacity.ahmed_eid.jobsallapp.Activities.MainScreenWithNavigation;
import com.udacity.ahmed_eid.jobsallapp.Model.Job;
import com.udacity.ahmed_eid.jobsallapp.R;

import java.util.ArrayList;

public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetItemFactory(getApplicationContext(), intent);
    }

    class WidgetItemFactory implements RemoteViewsFactory {
        private final Context mContext;
        int appWidgetId;
        ArrayList<Job> tenJobs;
        FirebaseAuth mAuth ;

        public WidgetItemFactory(Context mContext, Intent intent) {
            this.mContext = mContext;
            this.appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
            tenJobs = new ArrayList<>();
            mAuth = FirebaseAuth.getInstance();
        }

        @Override
        public void onCreate() {
            //Widget_MangeDataHelper mangeDataHelper = new Widget_MangeDataHelper(getApplicationContext());
            if(mAuth.getCurrentUser().getUid() != null) {
               // tenJobs = mangeDataHelper.getDataWidget();
            }
            SystemClock.sleep(3000);

        }


        @Override
        public void onDataSetChanged() {
        }

        @Override
        public void onDestroy() {
            tenJobs.clear();
        }

        @Override
        public int getCount() {
            return tenJobs.size();
        }

        @Override
        public RemoteViews getViewAt(int i) {
            RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.item_widget);
            views.setTextViewText(R.id.widget_job_name, tenJobs.get(i).getTitle());
            views.setTextViewText(R.id.widget_job_location, tenJobs.get(i).getCountry()+" - "+tenJobs.get(i).getCity());
            Intent intent = new Intent(mContext, MainScreenWithNavigation.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, 0);
            views.setOnClickPendingIntent(R.id.item_layout, pendingIntent);
            //-----------------------------------------------------------------------------
            Bundle extras = new Bundle();
            extras.putInt(JobWidgetProvider.EXTRA_ITEM, i);
            Intent fillInIntent = new Intent();
            fillInIntent.putExtras(extras);
            views.setOnClickFillInIntent(R.id.widget_job_name, fillInIntent);
            try {
                //Log.e("adapterWidget", "Loading view " + i);
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //SystemClock.sleep(500);
            return views;
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
        public long getItemId(int i) {
            return i;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
