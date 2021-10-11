package compact.mobile.SuratJalan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.HttpAuthHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

/**
 * Project mobile_android.
 * <p>
 * Created by wellsen on 2019-10-28.
 * for PT. Sumber Trijaya Lestari.
 */
public class AlfadigitalWebViewClient extends WebViewClient {

    private String url;
    private Context context;

    AlfadigitalWebViewClient(Context context, String url) {
        this.url = url;
        this.context = context;
    }

    @Override
    public void onLoadResource(WebView view, String url) {
        Toast.makeText(context, "List Order page is loading... ", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        CookieSyncManager.getInstance().sync();
        String cookies = CookieManager.getInstance().getCookie(url);
        System.out.println("All COOKIES " + cookies);
        Log.d("Debug", "All COOKIES = " + cookies);
    }

    @Override
    public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
        handler.proceed("mama", "mama2019");
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (url.contains("maps.google.com")){
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setPackage("com.google.android.apps.maps");

            Activity activity = (Activity) context;
            if(intent.resolveActivity(activity.getPackageManager()) != null){
                activity.startActivity(intent);
                return true;
            }

            return super.shouldOverrideUrlLoading(view, url);
        }
        if (url.startsWith("tel:")) {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
            view.getContext().startActivity(intent);
            return true;
        }

        return super.shouldOverrideUrlLoading(view, url);
    }
}
