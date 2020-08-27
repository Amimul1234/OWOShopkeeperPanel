package com.owoshopkeeperpanel.Utilities;

import android.content.Context;

public class PxUtil {
    public static float dpToPx(Context context, int dp) {
        //Obtain the pixel density coefficient of the mask
        float density = context.getResources().getDisplayMetrics().density;
        return dp * density;
    }
    public static float pxTodp(Context context, int px) {
        //Obtain the pixel density coefficient of the mask
        float density = context.getResources().getDisplayMetrics().density;
        return px / density;
    }
}
