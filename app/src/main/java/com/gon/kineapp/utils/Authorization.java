package com.gon.kineapp.utils;

import android.content.Context;

public class Authorization {

    private String PREFIX = "auth";
    private String AUTH_KEY = "auth_key";
    private Context context;
    private static Authorization instance;

    public void init(Context context) {
        this.context = context;
    }

    public static synchronized Authorization getInstance() {
        if (instance == null) {
            instance = new Authorization();
        }
        return instance;
    }

    public String get() {
        return new SharedPreferencesEditor(context, PREFIX).valueForKey(AUTH_KEY, "");
    }

    public void set(String token) {
        new SharedPreferencesEditor(context, PREFIX).setValueForKey(AUTH_KEY, token);
    }

}
