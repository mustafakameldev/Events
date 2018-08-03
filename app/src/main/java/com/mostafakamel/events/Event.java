package com.mostafakamel.events;

/**
 * Created by mostafa kamel on 28/02/2018.
 */

public class Event {
    private String place , event , family , date  , type;

    public Event(){}
    public Event(String family, String event, String  place, String date , String type) {
        this.place = place;
        this.event = event;
        this.family = family;
        this.date = date;
        this.type = type ;
    }
    public Event(String family, String event, String  place, String date ) {
        this.place = place;
        this.event = event;
        this.family = family;
        this.date = date;
    }
    public String getPlace() {
        return place;
    }
    public void setPlace(String place) {
        this.place = place;
    }
    public String getEvent() {
        return event;
    }
    public void setEvent(String event) {
        this.event = event;
    }
    public String getFamily() {
        return family;
    }
    public void setFamily(String family) {
        this.family = family;
    }
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
