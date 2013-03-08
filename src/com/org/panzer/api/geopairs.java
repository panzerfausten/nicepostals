/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.org.panzer.api;

/**
 *
 * @author panzer
 */
    public class geopairs {

        private Double lat_min;
        private Double lat_max;
        private Double lng_min;
        private Double lng_max;

        public geopairs() {
        }

        public geopairs(Double lat_min, Double lat_max, Double lng_min, Double lng_max) {
            this.lat_min = lat_min;
            this.lat_max = lat_max;
            this.lng_min = lng_min;
            this.lng_max = lng_max;
        }

        /**
         * @return the lat_min
         */
        public Double getLat_min() {
            return lat_min;
        }

        /**
         * @return the lat_max
         */
        public Double getLat_max() {
            return lat_max;
        }

        /**
         * @return the lng_min
         */
        public Double getLng_min() {
            return lng_min;
        }

        /**
         * @return the lng_max
         */
        public Double getLng_max() {
            return lng_max;
        }
    }