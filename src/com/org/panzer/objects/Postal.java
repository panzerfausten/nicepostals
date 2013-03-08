/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.org.panzer.objects;

/**
 *
 * @author panzer
 * This class is intended to save all the Postal properties.
 */
public class Postal {
    String name;
    String text;
    String IMGURL;
    Double LAT;
    Double LNG;
    String owner_name;
    String owner_url;
    public Postal() {
    }

    public Postal(String name, String text, String IMGURL, Double LAT, Double LNG, String owner_name, String owner_url) {
        this.name = name;
        this.text = text;
        this.IMGURL = IMGURL;
        this.LAT = LAT;
        this.LNG = LNG;
        this.owner_name = owner_name;
        this.owner_url = owner_url;
    }

    public Double getLAT() {
        return LAT;
    }

    public Double getLNG() {
        return LNG;
    }

    public String getOwner_name() {
        return owner_name;
    }

    public String getOwner_url() {
        return owner_url;
    }

    public void setLAT(Double LAT) {
        this.LAT = LAT;
    }

    public void setLNG(Double LNG) {
        this.LNG = LNG;
    }

    public void setOwner_name(String owner_name) {
        this.owner_name = owner_name;
    }

    public void setOwner_url(String owner_url) {
        this.owner_url = owner_url;
    }

  

    public String getText() {
        return text;
    }

    public String getName() {
        return name;
    }

    public String getIMGURL() {
        return IMGURL;
    }

    public void setIMGURL(String IMGURL) {
        this.IMGURL = IMGURL;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setText(String text) {
        this.text = text;
    }
    
}
