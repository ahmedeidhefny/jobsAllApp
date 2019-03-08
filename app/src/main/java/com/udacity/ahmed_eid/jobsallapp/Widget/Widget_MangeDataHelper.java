package com.udacity.ahmed_eid.jobsallapp.Widget;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.udacity.ahmed_eid.jobsallapp.Model.Job;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class Widget_MangeDataHelper {

    private static final String SHARED_PREFERENCES_NAME = "widget_name";
    private static final String SHARED_PREFERENCES_Key = "widget_key";
    private static final String TAG = "Widget_MangeDataHelper";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Gson gson;
    private Context mContext;

    public Widget_MangeDataHelper(Context context) {
        mContext = context;
        if (mContext != null) {
            sharedPreferences = mContext.getSharedPreferences(SHARED_PREFERENCES_NAME, 0);
            editor = sharedPreferences.edit();
        } else {
            Log.e(TAG, "context is null");
        }
        gson = new Gson();
    }

    public void setDataWidget(ArrayList<Job> jobs) {
        if (jobs != null) {
            String gsonJobsString = gson.toJson(jobs);
            //Log.e(TAG, gsonJobsString);
            editor.putString(SHARED_PREFERENCES_Key, gsonJobsString);
            editor.commit();
            Log.e(TAG, "Added Recipe Object In SharedPref Successfully..!");
        }
//        String gsonJobsString = sharedPreferences.getString(SHARED_PREFERENCES_Key,"");
//        if (TextUtils.isEmpty(gsonJobsString)) {
//            addInShared(jobs);
//        }else {
//            editor.remove(SHARED_PREFERENCES_Key).commit();
//            addInShared(jobs);
//        }
    }

    private void addInShared(ArrayList<Job> jobs) {
    }

    public ArrayList<Job> getDataWidget() {
        String gsonJobsString = sharedPreferences.getString(SHARED_PREFERENCES_Key, "");
        Log.e(TAG, gsonJobsString);
        Type type = new TypeToken<ArrayList<Job>>() {
        }.getType();
        ArrayList<Job> jobs = gson.fromJson(gsonJobsString, type);
        return jobs;
    }
}
