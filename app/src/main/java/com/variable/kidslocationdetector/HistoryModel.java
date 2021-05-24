package com.variable.kidslocationdetector;

public class HistoryModel {

   public String histor_id;
   public String lat;
   public String lon;
   public String pinId;
   public String time_ofAddingHistory;

    public HistoryModel(String histor_id, String lat, String lon, String pinId, String time_ofAddingHistory) {
        this.histor_id = histor_id;
        this.lat = lat;
        this.lon = lon;
        this.pinId = pinId;
        this.time_ofAddingHistory = time_ofAddingHistory;
    }

    public HistoryModel(String lat, String lon, String pinId, String time_ofAddingHistory) {
        this.lat = lat;
        this.lon = lon;
        this.pinId = pinId;
        this.time_ofAddingHistory = time_ofAddingHistory;
    }
}
