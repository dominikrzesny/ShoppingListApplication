package com.example.Rzesny.shoppinglistapp.Utils;
import android.app.Activity;

import com.example.Rzesny.shoppinglistapp.R;

public class ThemeUtils
{
    public static int cTheme = 1;
    public final static int BLACK = 0;
    public final static int GREY = 1;

    public static void changeToTheme(int theme)
    {
        cTheme = theme;
    }

    public static void onActivityCreateSetTheme(Activity activity)
    {
        switch (cTheme)
        {
            default:
            case BLACK:
                activity.setTheme(R.style.BlackTheme);
                break;
            case GREY:
                activity.setTheme(R.style.GreyTheme);
                break;
        }
    }
}