package ru.nbdev.mediadownloader.app;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.google.firebase.analytics.FirebaseAnalytics;

import io.fabric.sdk.android.Fabric;
import ru.nbdev.mediadownloader.BuildConfig;
import ru.nbdev.mediadownloader.common.CrashlyticsTree;
import timber.log.Timber;

public class App extends Application {

    private static AppComponent appComponent;
    private static FirebaseAnalytics firebaseAnalytics;


    @Override
    public void onCreate() {
        super.onCreate();

        firebaseInit();
        crashlyticsInit();
        loggingInit();
        appComponentInit();
    }

    public static AppComponent getAppComponent() {
        return appComponent;
    }

    public static FirebaseAnalytics getFirebaseAnalytics() {
        return firebaseAnalytics;
    }

    private void loggingInit() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new CrashlyticsTree());
        }
    }

    private void crashlyticsInit() {
        // Set up Crashlytics, disabled for debug builds
        Crashlytics crashlyticsKit = new Crashlytics.Builder()
                .core(new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())
                .build();

        // Initialize Fabric with the debug-disabled crashlytics.
        Fabric.with(this, crashlyticsKit);

        // Set up Crashlytics always
        //Fabric.with(this, new Crashlytics());
    }

    private void firebaseInit() {
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }

    private void appComponentInit() {
        appComponent = DaggerAppComponent
                .builder()
                .appModule(new AppModule(this))
                .build();
    }
}
