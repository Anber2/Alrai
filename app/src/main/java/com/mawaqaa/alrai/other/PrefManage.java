package com.mawaqaa.alrai.other;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by HP on 5/23/2017.
 */

public class PrefManage {

    Context context;

    private final static String RES_PROFILEURI = "AlNasherUri";
    public static SharedPreferences sp;


    public PrefManage(Context context) {
        this.context = context;
    }

    public void saveUserDetails(String name, String userId) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", name);
        editor.putString("userId", userId);
        editor.commit();
    }

    public String getUserDetails() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        return sharedPreferences.getString("userId", "");
    }

    public void logOut() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }


    public void saveLoginDetails(String email, String password) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Email", email);
        editor.putString("Password", password);
        editor.commit();
    }

    public String getEmail() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        return sharedPreferences.getString("Email", "");
    }



    public boolean isUserLogedOut() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        boolean isEmailEmpty = sharedPreferences.getString("Email", "").isEmpty();
        boolean isPasswordEmpty = sharedPreferences.getString("Password", "").isEmpty();
        return isEmailEmpty || isPasswordEmpty;
    }

    public final static void setAlNasherUri(Context context, String lang) {
        sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putString(RES_PROFILEURI, lang).apply();
    }

    public final static String getAlNasherUri(Context context) {
        sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(RES_PROFILEURI, SessionClass.EatndRun_PROFILEURI);
    }
}