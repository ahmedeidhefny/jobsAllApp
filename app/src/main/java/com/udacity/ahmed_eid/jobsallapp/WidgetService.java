package com.udacity.ahmed_eid.jobsallapp;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.udacity.ahmed_eid.jobsallapp.Model.Job;

import java.util.ArrayList;
import java.util.Collections;

public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetItemFactory(getApplicationContext(), intent);
    }

    class WidgetItemFactory implements RemoteViewsFactory {
        private Context mContext;
        private int appWidgetId;
        private ArrayList<Job> tenJobs;

        public WidgetItemFactory(Context mContext, Intent intent) {
            this.mContext = mContext;
            this.appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
            tenJobs = new ArrayList<>();
            Widget_MangeDataHelper mangeDataHelper = new Widget_MangeDataHelper(getApplicationContext());
            tenJobs = mangeDataHelper.getDataWidget();
        }

        @Override
        public void onCreate() {
            Widget_MangeDataHelper mangeDataHelper = new Widget_MangeDataHelper(getApplicationContext());
            //tenJobs = mangeDataHelper.getDataWidget();
            SystemClock.sleep(3000);
//            try {
//                Thread.sleep(3000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }

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
            views.setTextViewText(R.id.widget_item_Text, tenJobs.get(i).getTitle());
//            Bundle extras = new Bundle();
//            extras.putInt(JobWidgetProvider.EXTRA_ITEM, i);
//            Intent fillInIntent = new Intent();
//            fillInIntent.putExtras(extras);
//            views.setOnClickFillInIntent(R.id.widget_item_Text, fillInIntent);
//            try {
//                System.out.println("Loading view " + i);
//                Thread.sleep(500);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }

            SystemClock.sleep(500);
            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 0;
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
