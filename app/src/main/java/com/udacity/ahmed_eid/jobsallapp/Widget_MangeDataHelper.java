package com.udacity.ahmed_eid.jobsallapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.udacity.ahmed_eid.jobsallapp.Model.Job;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class Widget_MangeDataHelper {

    public static final String SHARED_PREFERENCES_NAME = "widget_name";
    public static final String SHARED_PREFERENCES_Key= "widget_key";
    private static final String TAG = "Widget_MangeDataHelper";

    SharedPreferences sharedPreferences ;
    SharedPreferences.Editor editor ;
    Gson gson ;

    public Widget_MangeDataHelper(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME,0);
        editor = sharedPreferences.edit();
        gson = new Gson();
    }

    public void setDataWidget(ArrayList<Job> jobs){
        String gsonJobsString = sharedPreferences.getString(SHARED_PREFERENCES_Key,"");
        if (TextUtils.isEmpty(gsonJobsString)) {
            addInShared(jobs);
        }else {
            editor.remove(SHARED_PREFERENCES_Key).commit();
            addInShared(jobs);
        }
    }

    private void addInShared(ArrayList<Job> jobs) {
        String gsonJobsString = gson.toJson(jobs);
        Log.e(TAG, gsonJobsString);
        editor.putString(SHARED_PREFERENCES_Key, gsonJobsString);
        editor.commit();
        Log.e(TAG, "Added Recipe Object In SharedPref Successfully..!");
    }

    public ArrayList<Job> getDataWidget(){
        String gsonJobsString = sharedPreferences.getString(SHARED_PREFERENCES_Key,"");
        Log.e(TAG,gsonJobsString);
        Type type =  new TypeToken<ArrayList<Job>>(){}.getType();
        ArrayList<Job> jobs = gson.fromJson(gsonJobsString,type);
        return jobs ;
    }
}
