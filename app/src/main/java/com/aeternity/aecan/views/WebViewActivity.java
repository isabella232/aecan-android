package com.aeternity.aecan.views;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.aeternity.aecan.R;
import com.aeternity.aecan.databinding.ActivityWebViewBinding;
import com.aeternity.aecan.views.base.ToolbarActivity;

public class WebViewActivity extends ToolbarActivity {
    private ActivityWebViewBinding binding;
    private String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = putContentView(R.layout.activity_web_view);
        if (getIntent().hasExtra("url")){
            url = getIntent().getExtras().getString("url");
            setupWebView();
        }
        setToolbarTitle(getIntent().getExtras().getString("titleToolbar"));
    }

    private void setupWebView() {
        binding.webView.getSettings().setDomStorageEnabled(true);
        binding.webView.getSettings().setBuiltInZoomControls(true);
        binding.webView.getSettings().setAllowFileAccessFromFileURLs(true);
        binding.webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        binding.webView.getSettings().setLoadWithOverviewMode(true);
        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                showActivityOverlay();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                showActivityOverlay();
                view.loadUrl("https://docs.google.com/gview?embedded=true&url="+url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
               hideActivityOverlay();
            }
        });

        binding.webView.loadUrl("https://docs.google.com/gview?embedded=true&url="+url);

    }

}
