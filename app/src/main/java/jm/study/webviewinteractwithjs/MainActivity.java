package jm.study.webviewinteractwithjs;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWebView = findViewById(R.id.webView);

        initWebView(mWebView);

        mWebView.addJavascriptInterface(new JsCallAndroid(), "test");
        mWebView.loadUrl("file:///android_asset/demo.html");
    }

    private void initWebView(WebView webView) {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);

        // 5.0以上开启混合模式加载
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        webView.setWebChromeClient(new WebChromeClient() {

            // 需要重写该方法才能弹出提示框
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }
        });

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
              // mWebView.evaluateJavascript("javascript:callJS()", null);
            }
        });

    }


    final class JsCallAndroid {

        @JavascriptInterface
        public void sayHello(String param){
            Toast.makeText(MainActivity.this, "收到来自js端传来的参数："+param, Toast.LENGTH_SHORT).show();
            mWebView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    String returnData = "1111111111";
                    mWebView.evaluateJavascript("javascript:returnResult(\""+returnData+"\")", null);
                }
            }, 1000);
        }
    }
}
