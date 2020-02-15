package ru.nbdev.mediadownloader.app;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.squareup.leakcanary.LeakCanary;

import io.fabric.sdk.android.Fabric;
import ru.nbdev.mediadownloader.BuildConfig;
import ru.nbdev.mediadownloader.common.CrashlyticsTree;
import timber.log.Timber;

public class App extends Application {

    private static AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        loggingInit();
        leakCanaryInit();
        crashlyticsInit();
        appComponentInit();
    }

    private void loggingInit() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
        Timber.plant(new CrashlyticsTree());
    }


    private void leakCanaryInit() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
    }

    private void crashlyticsInit() {
        //crashlyticsInitAlways();
        crashlyticsInitNotForDebugBuild();
    }

    private void appComponentInit() {
        appComponent = DaggerAppComponent
                .builder()
                .appModule(new AppModule(this))
                .build();
    }

    private void crashlyticsInitAlways() {
        // Set up Crashlytics always
        Fabric.with(this, new Crashlytics());
    }

    private void crashlyticsInitNotForDebugBuild() {
        // Set up Crashlytics, disabled for debug builds
        Crashlytics crashlyticsKit = new Crashlytics.Builder()
                .core(new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())
                .build();

        // Initialize Fabric with the debug-disabled crashlytics.
        Fabric.with(this, crashlyticsKit);
    }

    public static AppComponent getAppComponent() {
        return appComponent;
    }
}
