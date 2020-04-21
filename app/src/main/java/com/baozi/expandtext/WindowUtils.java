package com.baozi.expandtext;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;


public class WindowUtils {

    public static int getScreenWidth(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        return  width;
    }

}
