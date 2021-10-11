package compact.mobile.SuratJalan;

import android.app.Activity;
import android.webkit.JavascriptInterface;

public class BackJavasciptInterface {
    private Activity activity;

    public BackJavasciptInterface(Activity activity){
        this.activity = activity;
    }

    @JavascriptInterface
    public void backNavigation(){
        activity.finish();
    }
}
