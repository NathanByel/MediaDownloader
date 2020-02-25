package ru.nbdev.mediadownloader.common;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

public class Helpers {

    private Helpers() {
    }

    public static void hideKeyboard(Activity activity) {
        if (activity != null) {

            View view = activity.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            } else {
                activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            }
        }
    }

    public static void hideKeyboard(Context context, View view) {
        if (context != null && view != null) {
            try {
                InputMethodManager keyboard = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (keyboard != null) {
                    keyboard.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
