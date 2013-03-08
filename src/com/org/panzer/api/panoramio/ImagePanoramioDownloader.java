/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.org.panzer.api.panoramio;

import android.os.Environment;
import com.org.panzer.api.IPictureDownloader;
import com.org.panzer.api.Utils;
import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.EntityUtils;
import com.org.panzer.api.geopairs;

/**
 *
 * @author panzer
 */
public class ImagePanoramioDownloader implements IPictureDownloader {
    
    public String getImagesNearFrom(Double lat, Double lng) {

        DefaultHttpClient dh = new DefaultHttpClient();
        geopairs calculateMeters = Utils.calculateMeters(lat, lng);
        String url = "http://panoramio.com/map/get_panoramas.php?set=public&from=0&to=50&minx="
                + calculateMeters.getLng_min() + "&miny=" + calculateMeters.getLat_min() + "&maxx="
                + calculateMeters.getLng_max() + "&maxy=" + calculateMeters.getLat_max()
                + "&size=thumbnail&mapfilter=true";
        HttpGet hg = new HttpGet(url);
        try {
            HttpResponse execute = dh.execute(hg);
            return EntityUtils.toString(execute.getEntity());

        } catch (IOException ex) {
            Logger.getLogger(ImagePanoramioDownloader.class.getName()).log(Level.SEVERE, null, ex);
        }

        return "";

    }

    public void downloadImage(String urli, String id) {

        /*
         * Open a connection to that URL.
         */
        try {
            URL url = new URL(urli);
            URLConnection ucon = url.openConnection();

            /*
             * Define InputStreams to read from the URLConnection.
             */
            InputStream is = ucon.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);

            /*
             * Read bytes to the Buffer until there is nothing more to read(-1).
             */
            ByteArrayBuffer baf = new ByteArrayBuffer(50);
            int current = 0;
            while ((current = bis.read()) != -1) {
                baf.append((byte) current);
            }

            /*
             * Convert the Bytes read to a String.
             */
            FileOutputStream fos = new FileOutputStream(Environment.getExternalStorageDirectory() + "/NicePostals/" + id);
            fos.write(baf.toByteArray());
            fos.close();
        } catch (Exception ex) {
        }
    }
}
