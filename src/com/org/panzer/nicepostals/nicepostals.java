package com.org.panzer.nicepostals;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v4.view.MenuCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.ConsoleMessage;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Window;
import com.org.panzer.objects.Postal;
import com.org.panzer.objects.PostalWebClient;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class nicepostals extends SherlockActivity {

    private String BASE_URL = "http://201.116.202.231/~dmiranda/nicepostals/index.php?";
    //private Double LATITUDE;
    //private Double LONGITUDE;
    private Postal POSTAL;
    com.actionbarsherlock.view.MenuItem ShareMenuItem;
    /**
     * Called when the activity is first created.
     */
    WebView piv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.main);
        piv = (WebView) findViewById(R.id.postal);
        piv.getSettings().setJavaScriptEnabled(true);

        piv.setWebViewClient(new PostalWebClient());
        piv.setWebChromeClient(new WebChromeClient());
        getSupportActionBar().show();
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout v = (LinearLayout) vi.inflate(R.layout.start, null);
        ImageButton hint = (ImageButton) v.findViewById(R.id.main_button_hint);
        hint.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                startPiturePickerActivity();

            }
        });

        piv.addView(v);

        // piv.loadUrl("http://201.116.202.231/~dmiranda/nicepostals/index.php?bgurl=http://mw2.google.com/mw-panoramio/photos/medium/7357230.jpg");

//folder
        File folder = new File(Environment.getExternalStorageDirectory() + "/NicePostals");
        if (!folder.exists()) {
            folder.mkdir();
            folder.mkdirs();
//            System.out.println("carpeta creada");
        } else {
//            System.out.println("carpeta ya existe");
        }

    }

    private String takeSnapShot() {
        Picture picture = piv.capturePicture();
        Bitmap b = Bitmap.createBitmap(picture.getWidth(),
                picture.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);

        picture.draw(c);
        FileOutputStream fos;
        try {
            String filename = Environment.getExternalStorageDirectory() + "/NicePostals/postal.jpeg";
            fos = new FileOutputStream(filename);
            if (fos != null) {
                b.compress(Bitmap.CompressFormat.JPEG, 100, fos);

                fos.close();
                ExifInterface exif = new ExifInterface(filename);
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
                boolean key_tag = sharedPref.getBoolean(SettingsActivity.KEY_PREF_GEOTAGS, false);
                if (key_tag) {
                    if (POSTAL.getLAT() != null) {
                        Log.i("nicepostals", "saving exif");

                        exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE, decimalToDMS(POSTAL.getLAT()));
                        exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, decimalToDMS(POSTAL.getLNG()));
                        exif.saveAttributes();
                    }
                }
                return Environment.getExternalStorageDirectory() + "/NicePostals/postal.jpeg";
            }
        } catch (Exception e) {
            return "";
        }
        return "";

    }

    @Override
    public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
        //        super.onCreateContextMenu(menu, v, menuInfo);
        com.actionbarsherlock.view.MenuItem photo = menu.add(getResources().getString(R.string.main_menuitem_change_bg));
        photo.setIcon(R.drawable.picture);
        photo.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        photo.setOnMenuItemClickListener(new com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener() {
            public boolean onMenuItemClick(com.actionbarsherlock.view.MenuItem mi) {
                startPiturePickerActivity();
                return true;
            }
        });

        ShareMenuItem = menu.add(getResources().getString(R.string.main_menuitem_share));
        ShareMenuItem.setIcon(R.drawable.share);

        ShareMenuItem.setOnMenuItemClickListener(new com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener() {
            public boolean onMenuItemClick(com.actionbarsherlock.view.MenuItem mi) {
                String snapshot = takeSnapShot();
                Log.i("send", snapshot);
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("image/jpeg");
                share.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.main_intent_share_title));
                share.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.main_intent_share_text));
                share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(snapshot)));
                startActivity(share);
                return true;
            }
        });
        ShareMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        ShareMenuItem.setVisible(false);

        com.actionbarsherlock.view.MenuItem settings = menu.add(0, 2, 0, getResources().getString(R.string.main_menuitem_settings));
        settings.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
        settings.setOnMenuItemClickListener(new com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener() {
            public boolean onMenuItemClick(com.actionbarsherlock.view.MenuItem mi) {
                        startActivity(new Intent(nicepostals.this, SettingsActivity.class));

                return true;
            }
        });
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == PicturePicker.PICTURE_PICKED) {
            String uri = data.getExtras().getString("uri");
            //WebViewNavigationController.changeImage(piv, uri);
            //LATITUDE = data.getExtras().getDouble("lat");
            //LONGITUDE = data.getExtras().getDouble("lng");
            POSTAL = new Postal(data.getExtras().getString("title"), getString(R.string.main_webview_hint_note), data.getExtras().getString("uri"), data.getExtras().getDouble("lat"), data.getExtras().getDouble("lng"), data.getExtras().getString("owner_name"), data.getExtras().getString("owner_url"));
            piv.removeAllViews();
            piv.loadUrl(constructURL(BASE_URL, POSTAL));
            //ShareMenuItem.setEnabled(true);
            ShareMenuItem.setVisible(true);
        }
    }

    private String constructURL(String base_url, Postal p) {
        String curl = String.format("%sbgurl=%s&name=%s&text=%s&owner_name=%s&owner_url=%s", base_url, p.getIMGURL(), p.getName(), p.getText(), p.getOwner_name(), p.getOwner_url());

        Log.i("URLCONSTRUCTER", curl);
        return curl;
    }

    private void startPiturePickerActivity() {
        Intent i = new Intent(nicepostals.this, PicturePicker.class);
                startActivityForResult(i, PicturePicker.PICTURE_PICKED);

    }

    String decimalToDMS(double coord) {
        String output, degrees, minutes, seconds;

        // gets the modulus the coordinate divided by one (MOD1).
        // in other words gets all the numbers after the decimal point.
        // e.g. mod = 87.728056 % 1 == 0.728056
        //
        // next get the integer part of the coord. On other words the whole number part.
        // e.g. intPart = 87

        double mod = coord % 1;
        int intPart = (int) coord;

        //set degrees to the value of intPart
        //e.g. degrees = "87"

        degrees = String.valueOf(intPart);

        // next times the MOD1 of degrees by 60 so we can find the integer part for minutes.
        // get the MOD1 of the new coord to find the numbers after the decimal point.
        // e.g. coord = 0.728056 * 60 == 43.68336
        //      mod = 43.68336 % 1 == 0.68336
        //
        // next get the value of the integer part of the coord.
        // e.g. intPart = 43

        coord = mod * 60;
        mod = coord % 1;
        intPart = (int) coord;

        // set minutes to the value of intPart.
        // e.g. minutes = "43"
        minutes = String.valueOf(intPart);

        //do the same again for minutes
        //e.g. coord = 0.68336 * 60 == 41.0016
        //e.g. intPart = 41
        coord = mod * 60;
        intPart = (int) coord;

        // set seconds to the value of intPart.
        // e.g. seconds = "41"
        seconds = String.valueOf(intPart);

        // I used this format for android but you can change it 
        // to return in whatever format you like
        // e.g. output = "87/1,43/1,41/1"
        output = degrees + "/1," + minutes + "/1," + seconds + "/1";

        //Standard output of D°M′S″
        //output = degrees + "°" + minutes + "'" + seconds + "\"";

        return output;
    }
}
