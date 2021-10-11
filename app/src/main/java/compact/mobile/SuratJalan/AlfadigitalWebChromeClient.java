package compact.mobile.SuratJalan;

import android.webkit.WebChromeClient;
import android.webkit.WebView;

public class AlfadigitalWebChromeClient extends WebChromeClient {

    private OnProgressListener listener;

    public AlfadigitalWebChromeClient (OnProgressListener listener){
        this.listener = listener;
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        listener.onProgressChanged(newProgress);
    }

    interface OnProgressListener{
        void onProgressChanged(int progress);
    }
}
