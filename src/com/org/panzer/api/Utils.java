/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.org.panzer.api;

/**
 *
 * @author panzer
 */
public class Utils {
      public static geopairs calculateMeters(Double lat0, Double lon0) {

        Double lat_min = lat0 - 0.045;
        Double lat_max = lat0 + 0.045;
        Double long_min = lon0 - (0.045 / Math.cos(lat0 * Math.PI / 180));
        Double long_max = lon0 + (0.045 / Math.cos(lat0 * Math.PI / 180));
        //0.009
        return new geopairs(lat_min, lat_max, long_min, long_max);

    }
}
