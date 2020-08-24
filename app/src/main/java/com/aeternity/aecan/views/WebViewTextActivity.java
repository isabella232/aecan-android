package com.aeternity.aecan.views;

import android.os.Bundle;
import android.util.Log;

import androidx.databinding.DataBindingUtil;

import com.aeternity.aecan.R;
import com.aeternity.aecan.databinding.ActivityWebviewTextBinding;
import com.aeternity.aecan.views.base.ToolbarActivity;

public class WebViewTextActivity extends ToolbarActivity {

    ActivityWebviewTextBinding binding;
    public static final String TAG = WebViewTextActivity.class.getSimpleName();
    public static final String INTENT_URL = "intent_url_key";
    public static final String INTENT_STRING = "intent_string_key";

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = putContentView(R.layout.activity_webview_text);
        setToolbarTitle(getIntent().getExtras().getString("titleToolbar"));


        try {
            String intentUrl = getIntent().getExtras().getString(INTENT_URL);

            if (intentUrl != null) {
                binding.webView.loadUrl(intentUrl);
            }

        } catch (NullPointerException ex) {
            Log.println(Log.WARN, TAG, "INTENT_URL not found");
        }

    }

}
