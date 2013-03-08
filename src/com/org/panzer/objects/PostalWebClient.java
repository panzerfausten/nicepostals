/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.org.panzer.objects;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import com.org.panzer.nicepostals.R;

/**
 *
 * @author panzer
 */
public class PostalWebClient extends WebViewClient {

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
         final String furl = failingUrl;
         final WebView vv = view;
        //super.onReceivedError(view, errorCode, description, failingUrl); //To change body of generated methods, choose Tools | Templates.
         LayoutInflater vi = (LayoutInflater) view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout v = (LinearLayout) vi.inflate(R.layout.error, null);
        Button errorbotton = (Button) v.findViewById(R.id.error_bn_retry);
        errorbotton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                vv.loadUrl(furl);
                vv.removeAllViews();
            }
        });
        
        view.addView(v);
        //view.loadData(furl, furl, furl);
        view.loadData("<html></html>", "text/html", "UTF-8");
        
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (!url.contains("http://201.116.202.231/")){
            view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    return true;

        }else{
                return false;
        }
        
    }
   
    
}
