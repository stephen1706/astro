package com.stephen.astro.util;

import android.os.Build;

/**
 * Created by stephenadipradhana on 12/14/16.
 */

public class AppUtils {
    public static boolean isAndroidM() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }
}
