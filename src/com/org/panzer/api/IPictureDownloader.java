/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.org.panzer.api;

/**
 *
 * @author panzer
 */
public interface IPictureDownloader {
    public String getImagesNearFrom(Double lat, Double lng);
    
     public void downloadImage(String urli,String id);
   
}
