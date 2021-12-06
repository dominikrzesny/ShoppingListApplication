package com.example.Rzesny.shoppinglistapp.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import com.example.Rzesny.shoppinglistapp.Utils.ThemeUtils;

import java.util.Locale;

public class SharedPreferencesUtils {

    public static String PreferencesTAG = "Settings";
    public static String appLanguage;

    public static void saveLocale(Activity activity, String language){
        SharedPreferences.Editor editor = activity.getSharedPreferences(PreferencesTAG,Context.MODE_PRIVATE).edit();
        editor.putString("Language",language);
        editor.apply();
    }

    public static void loadAndSetLocale(Activity activity){
        SharedPreferences preferences = activity.getSharedPreferences(PreferencesTAG,Context.MODE_PRIVATE);
        String language = preferences.getString("Language","");
        appLanguage = language;
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        activity.getBaseContext().getResources().updateConfiguration(configuration,activity.getBaseContext().getResources().getDisplayMetrics());
    }

    public static void saveTheme(Activity activity, int theme){
        SharedPreferences.Editor editor = activity.getSharedPreferences(PreferencesTAG,Context.MODE_PRIVATE).edit();
        editor.putInt("Theme",theme);
        editor.apply();
    }

    public static void loadTheme(Activity activity){
        SharedPreferences preferences = activity.getSharedPreferences(PreferencesTAG,Context.MODE_PRIVATE);
        int cTheme = preferences.getInt("Theme",0);
        ThemeUtils.changeToTheme(cTheme);
    }
}
