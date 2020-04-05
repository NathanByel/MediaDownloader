package ru.nbdev.mediadownloader.common;

import android.util.Log;

import androidx.annotation.Nullable;

import com.crashlytics.android.Crashlytics;

import timber.log.Timber;

public class CrashlyticsTree extends Timber.Tree {

    private static final String CRASHLYTICS_KEY_PRIORITY = "priority";
    private static final String CRASHLYTICS_KEY_TAG = "tag";
    private static final String CRASHLYTICS_KEY_MESSAGE = "message";


    @Override
    protected void log(int priority, @Nullable String tag, @Nullable String message, @Nullable Throwable throwable) {
        if (priority == Log.ERROR) {
            Crashlytics.setInt(CRASHLYTICS_KEY_PRIORITY, priority);
            Crashlytics.setString(CRASHLYTICS_KEY_TAG, tag);
            Crashlytics.setString(CRASHLYTICS_KEY_MESSAGE, message);
            if (throwable == null) {
                Crashlytics.logException(new RuntimeException("Log"));
            } else {
                Crashlytics.logException(throwable);
            }
        }
    }
}
