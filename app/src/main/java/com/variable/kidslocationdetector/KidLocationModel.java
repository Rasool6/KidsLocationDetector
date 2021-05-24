package com.variable.kidslocationdetector;

public class KidLocationModel {

  public String id;
  public String lati;
  public String longi;
  public String pin;

    public KidLocationModel(String id, String lati, String longi,String pin) {
        this.id = id;
        this.lati = lati;
        this.longi = longi;
        this.pin = pin;
    }

    public KidLocationModel(String lati, String longi,String pin) {
        this.lati = lati;
        this.longi = longi;
        this.pin = pin;

    }
}
