package compact.mobile;

import android.content.Context;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

/**
 * Project mobile_android.
 *
 * Created by wellsen on 2019-07-26.
 * for PT. Sumber Trijaya Lestari.
 */
public class CompactClearApplication extends MultiDexApplication {

  @Override public void onCreate() {
    super.onCreate();

    if (!BuildConfig.DEBUG) {
      Fabric.with(this, new Crashlytics());
    }
  }

  @Override protected void attachBaseContext(Context base) {
    super.attachBaseContext(base);

    MultiDex.install(this);
  }

}
