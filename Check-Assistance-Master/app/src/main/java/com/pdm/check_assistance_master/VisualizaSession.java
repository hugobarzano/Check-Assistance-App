package com.pdm.check_assistance_master;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class VisualizaSession extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualiza_session);

        WebView webView = (WebView) this.findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.loadUrl("http://dispositivosmoviles.herokuapp.com");
        //webView.loadUrl("https://www.google.es/?gfe_rd=cr&ei=2U00V6HBIK2p8wfdsoW4BQ&gws_rd=ssl");

        webView.setWebViewClient(new WebViewClient());

    }

}
