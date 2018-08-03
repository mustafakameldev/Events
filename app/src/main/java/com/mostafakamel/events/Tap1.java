package com.mostafakamel.events;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mostafa kamel on 28/02/2018.
 */

public class Tap1 extends android.support.v4.app.Fragment {
 private DatabaseReference mainRef ;
 private ListView eventsLV ;
 private AdView mAdView;
 private ArrayList<Event> events ;
 EventListAdapter eventsAdapter;
 public Tap1() {
 }
 @Nullable
 @Override
 public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
  View rootView = inflater.inflate(R.layout.tap1, container, false);
  eventsLV=(ListView)rootView.findViewById(R.id.eventsLV_tap1);
  mainRef= FirebaseDatabase.getInstance().getReference().child("allEvents");
  mainRef.keepSynced(true);
  // ads stuff
  MobileAds.initialize(getContext(),"ca-app-pub-9502802921397120~4573557064");
  mAdView = rootView.findViewById(R.id.adView);
  AdRequest adRequest = new AdRequest.Builder().build();
  mAdView.loadAd(adRequest);
  // finishing ads stuff
  events = new ArrayList<>() ;
  retrieve();
  eventsAdapter = new EventListAdapter(getContext() ,android.R.layout.simple_list_item_1 , events);
  eventsLV.setAdapter(eventsAdapter);
  return rootView;

 }
 void retrieve()
 {
  Query query = mainRef.orderByChild("num");
  query.addChildEventListener(new ChildEventListener() {
   @Override
   public void onChildAdded(DataSnapshot dataSnapshot, String s) {
    String family = dataSnapshot.child("family").getValue(String.class);
    String event = dataSnapshot.child("event").getValue(String.class);
    String time  = dataSnapshot.child("time").getValue(String.class) ;
    String place = dataSnapshot.child("place").getValue(String.class) ;
    String type = dataSnapshot.child("type").getValue(String.class );
    events.add(new Event(family ,event ,place ,time , type));
    eventsAdapter.notifyDataSetChanged();

   }

   @Override
   public void onChildChanged(DataSnapshot dataSnapshot, String s) {

   }

   @Override
   public void onChildRemoved(DataSnapshot dataSnapshot) {

   }

   @Override
   public void onChildMoved(DataSnapshot dataSnapshot, String s) {

   }

   @Override
   public void onCancelled(DatabaseError databaseError) {

   }
  }) ;
 }
 public class EventListAdapter extends ArrayAdapter {
  private LayoutInflater layoutInflater;
  public EventListAdapter(Context context, int resource , List eventsA) {
   super(context, resource , eventsA);
  }
  @Override
  public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

    layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    convertView = layoutInflater.inflate(R.layout.eventlayout, null);

   final TextView family = (TextView) convertView.findViewById(R.id.familyTv_recyclerViewLayout);
   final TextView place = (TextView) convertView.findViewById(R.id.placeTv_recyclerViewLayout);
   final TextView event = (TextView) convertView.findViewById(R.id.eventTv_recyclerViewLayout);
   final TextView time = (TextView) convertView.findViewById(R.id.dateTv_recyclerViewLayout);
   family.setText(events.get(position).getFamily());
   family.setTextColor(Color.BLACK);
   place.setText(events.get(position).getPlace());
   String type1 = events.get(position).getType();
   if (type1.equals("celebrate")) {
    event.setBackgroundColor(Color.YELLOW);
    event.setTextColor(Color.BLUE);
   } else if (type1.equals("bad"))
   {
    event.setBackgroundColor(Color.BLACK);
    event.setTextColor(Color.WHITE);
   }
   event.setText(events.get(position).getEvent());
   time.setText(events.get(position).getDate());
   return convertView;
  }
 }
}

