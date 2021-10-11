package compact.mobile.SuratJalan;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import compact.mobile.R;

public class AlfadigitalWebViewActivity extends Activity implements AlfadigitalWebChromeClient.OnProgressListener {

    private WebView wv;
    private ProgressBar pb;

    String STR_CodeStore, STR_WebviewURLResult;

    private CookieManager cookieManager;

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cookieManager = CookieManager.getInstance();

        setContentView(R.layout.alfadigital_webview_activity);
        wv = findViewById(R.id.wv);
        pb = findViewById(R.id.pb);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            Toast.makeText(this, "Kode Toko Kosong", Toast.LENGTH_LONG).show();
//            return;
        }

        STR_CodeStore = extras.getString("code_store");
        STR_WebviewURLResult = extras.getString("muse_webview");
//        STR_CodeStore = "K427";
//        STR_WebviewURLResult = "https://betapapa.alfadigital.id";
        Log.d("Debug", "Isi" + " || Code Store = " + STR_CodeStore + " || Webview URL = " + STR_WebviewURLResult);

        WebSettings webSetting = wv.getSettings();
        webSetting.setLoadsImagesAutomatically(true);
        webSetting.setJavaScriptEnabled(true);
        webSetting.setDomStorageEnabled(true);
        wv.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        wv.setWebViewClient(new AlfadigitalWebViewClient(this, STR_WebviewURLResult));
        wv.setWebChromeClient(new AlfadigitalWebChromeClient(this));
        wv.addJavascriptInterface(new BackJavasciptInterface(this), "Client");

        cookieManager.setCookie(STR_WebviewURLResult, "store_code=" + STR_CodeStore);

        pb.getProgressDrawable().setColorFilter(ContextCompat.getColor(this, R.color.light_blue), PorterDuff.Mode.SRC_IN);

        wv.loadUrl(STR_WebviewURLResult);
    }

    @Override
    public void onBackPressed() {
        if (wv.copyBackForwardList().getCurrentIndex() > 0){
            wv.goBack();
            return;
        }

        cookieManager.setCookie(STR_WebviewURLResult, "tab=");

        super.onBackPressed();
    }

    @Override
    public void onProgressChanged(int progress) {
        pb.setProgress(progress);

        if(progress != 100){
            pb.setVisibility(View.VISIBLE);
        }else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    pb.setVisibility(View.GONE);
                }
            },500);
        }
    }
}
