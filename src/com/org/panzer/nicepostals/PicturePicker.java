/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.org.panzer.nicepostals;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.view.MenuCompat;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockListActivity;
import com.org.panzer.api.panoramio.ImagePanoramioDownloader;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author panzer
 */
public class PicturePicker extends SherlockActivity {

    public static int PICTURE_PICKED = 1;
    private double LATITUDE;
    private double LONGITUDE;
    private GridView GRIDVIEW;

    @Override
    public boolean onOptionsItemSelected(com.actionbarsherlock.view.MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                Intent intent = new Intent(this, nicepostals.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * public boolean onCreateOptionsMenu(Menu menu) { MenuItem camera =
     * menu.add("Cámara"); camera.setOnMenuItemClickListener(new
     * MenuItem.OnMenuItemClickListener() {
     *
     * public boolean onMenuItemClick(MenuItem arg0) { String IMAGEPATH =
     * Environment.getExternalStorageDirectory() + "/NicePostals/postcard.jpeg";
     *
     * Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
     *
     * File f = new File(IMAGEPATH);
     * camera.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
     * Uri.fromFile(f));
     *
     * startActivityForResult(camera, 0); return true; } });
     * MenuCompat.setShowAsAction(camera, 1); return true; }
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.picturepicker_layout);
        GRIDVIEW = (GridView) findViewById(R.id.gridview);
        getSupportActionBar().show();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.picturepicker_title));
        customAdapter ca = new customAdapter(getApplicationContext());
        
               
        GRIDVIEW.setAdapter(ca);
        GRIDVIEW.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Item it = (Item) arg0.getItemAtPosition(arg2);
                Intent i = new Intent();
                Bundle b = new Bundle();
                b.putString("uri", it.getRuta().replace("thumbnail", "medium"));
                b.putDouble("lat", LATITUDE);
                b.putDouble("lng", LONGITUDE);
                b.putString("owner_name", it.getAuthor());
                b.putString("owner_url", it.getOwner_URL());
                b.putString("title", it.getTitle());
                i.putExtras(b);
                setResult(PICTURE_PICKED, i);
                finish();
            }
        });
        //Handler to populate list
        //Call t.start() later
        final Handler h = new Handler(new Handler.Callback() {
            public boolean handleMessage(Message arg0) {
                if (arg0.obj != null) {
                    //rellenar lista
                    Item it = (Item) arg0.obj;
                    // ca = new customAdapter(getApplicationContext(), R.layout.picturepicker_layout, R.layout.picturepicker_list_item, its);
                    //setListAdapter(ca);
                    
                    //customAdapter.ITEMS = ;
                    ((customAdapter )GRIDVIEW.getAdapter()).addItem(it);
                    //GRIDVIEW.setAdapter(customAdapter);

                }
                return true;
            }
        });

        final Thread t = new Thread(new Runnable() {
            public void run() {
                ImagePanoramioDownloader gim = new ImagePanoramioDownloader();
                String imagesNearFrom = gim.getImagesNearFrom(LATITUDE, LONGITUDE);
                Log.i("PicturePicker", imagesNearFrom);
                List<Item> its = new ArrayList<Item>();
                if (!(imagesNearFrom.equals(""))) {
                    try {
                        JSONObject jobj = new JSONObject(String.valueOf(imagesNearFrom));
                        JSONArray pArray = jobj.getJSONArray("photos");
                        // its = new Item[pArray.length()];

                        for (int i = 0; i < pArray.length(); i++) {

                            JSONObject photo = pArray.getJSONObject(i);
                            //System.out.println(photo.getString("photo_title"));
                            //Item it = new Item(1, "", "", "", "", "");
                            //it = new Item(photo.getInt("photo_id"),photo.getString("photo_title"),)
                            Item it = new Item(photo.getInt("photo_id"), photo.getString("photo_title"), photo.getString("photo_file_url"), photo.getString("owner_name"), photo.getString("owner_url"));
                            //its.add(it);
                            
                               Message m = new Message();
                m.obj = it;

                h.sendMessage(m);
                    
                            gim.downloadImage(photo.getString("photo_file_url"), photo.getString("photo_id"));
                            Log.i("PICTUREPICKER", it.getRuta());

                        }
                    } catch (JSONException ex) {
                        //No se pudo cargar imagenes
                    }
                }
             





            }
        });

        //Get position
        String LocationProvider = LocationManager.NETWORK_PROVIDER;
        final LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationProvider)) {
            //switch to gps
            LocationProvider = LocationManager.GPS_PROVIDER;
            if (!lm.isProviderEnabled(LocationProvider)) {
                Toast.makeText(this, getResources().getString(R.string.picturepicker_gps_off), Toast.LENGTH_LONG).show();
            }
        }
        lm.requestLocationUpdates(LocationProvider, 0, 0, new LocationListener() {
            public void onLocationChanged(Location lctn) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                lm.removeUpdates(this);
                LATITUDE = lctn.getLatitude();
                LONGITUDE = lctn.getLongitude();

                t.start();

            }

            public void onStatusChanged(String string, int i, Bundle bundle) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            public void onProviderEnabled(String string) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            public void onProviderDisabled(String string) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == -1) {
                finishActivity(0);

            }
        }
    }

    private class customAdapter extends BaseAdapter {

        private Context mContext;
        List<Item> ITEMS = new ArrayList<Item>();

        public customAdapter(Context c) {
            mContext = c;
        }
        public void addItem(Item it){
            ITEMS.add(it);
            notifyDataSetChanged();
        }

        /*@Override
         * public View getView(int position, View convertView, ViewGroup parent) {
         * View v = convertView;
         * final Item it = getItem(position);
         * if (it != null) {
         * 
         * LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         * v = vi.inflate(R.layout.picturepicker_list_item, null);
         * View horizontal = v.findViewById(R.id.picturepicker_ll);
         * horizontal.setOnClickListener(new View.OnClickListener() {
         * public void onClick(View arg0) {
         * 
         * Intent i = new Intent();
         * Bundle b = new Bundle();
         * b.putString("uri", it.getRuta().replace("thumbnail", "medium"));
         * b.putDouble("lat", LATITUDE);
         * b.putDouble("lng", LONGITUDE);
         * b.putString("owner_name", it.getAuthor());
         * b.putString("owner_url", it.getOwner_URL());
         * b.putString("title",it.getTitle());
         * i.putExtras(b);
         * setResult(PICTURE_PICKED, i);
         * finish();
         * 
         * }
         * });
         * 
         * TextView description = (TextView) v.findViewById(R.id.picturepicker_tt_description);
         * if (it.getTitle().trim().equals("")) {
         * description.setText(getResources().getString(R.string.picturepicker_item_notitle));
         * } else {
         * description.setText(it.getAuthor());
         * 
         * }
         * TextView title = (TextView) v.findViewById(R.id.picturepicker_tt_title);
         * title.setText(it.getTitle());
         * ImageView picture = (ImageView) v.findViewById(R.id.picturepicker_iv);
         * BitmapDrawable bd = new BitmapDrawable(Environment.getExternalStorageDirectory() + "/NicePostals/" + it.getId());
         * picture.setBackgroundDrawable(bd);
         * 
         * 
         * }
         * return v;
         * }*/
        public int getCount() {
            return ITEMS.size();
        }

        public Object getItem(int arg0) {
            return ITEMS.get(arg0);
        }

        public long getItemId(int arg0) {
            return 0;
        }

        public View getView(final int position, View convertView, ViewGroup arg2) {
            final ImageView imageView;

            if (convertView == null) {  // if it's not recycled, initialize some attributes

                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(8, 8, 8, 8);

     
            } else {
                imageView =(ImageView) convertView;
            }
            
            
            imageView.setImageURI(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/nicepostals/" + ITEMS.get(position).getId())));

            return imageView;
        }
    }

    private class Item {

        private int id;
        private String title;
        private String path;
        private String author;
        private String owner_URL;

        public Item(int id, String title, String path, String author, String owner_URL) {
            this.id = id;
            this.title = title;

            this.path = path;
            this.author = author;
            this.owner_URL = owner_URL;
        }

        public String getOwner_URL() {
            return owner_URL;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public Item() {
        }

        /**
         * @return the id
         */
        public int getId() {
            return id;
        }

        /**
         * @param id the id to set
         */
        public void setId(int id) {
            this.id = id;
        }

        /**
         * @return the ruta
         */
        public String getRuta() {
            return path;
        }

        /**
         * @param ruta the ruta to set
         */
        public void setRuta(String ruta) {
            this.path = ruta;
        }

        /**
         * @return the titulo
         */
        public String getTitle() {
            return title;
        }
    }
}
