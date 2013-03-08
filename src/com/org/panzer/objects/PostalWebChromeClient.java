/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.org.panzer.objects;

import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;

/**
 *
 * @author panzer
 */
public class PostalWebChromeClient extends WebChromeClient {

    @Override
    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        
        return true;
    }

    @Override
    public void onConsoleMessage(String message, int lineNumber, String sourceID) {
    }
    
}
