package com.mostafakamel.events;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Timer;
import java.util.TimerTask;

import static android.content.ContentValues.TAG;
public class EventService  extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private DatabaseReference mainRef  ;
    private  Integer allNum ;
    public EventService() {
        mainRef = FirebaseDatabase.getInstance().getReference().child("allNum") ;
        mainRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                allNum = dataSnapshot.getValue(Integer.class);
                Log.d(TAG, "all num 1st time : "+allNum);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                Timer timer = new Timer();
                timer.schedule(new SayHello() , 0, 4000);

            }
        }, 1 );
        return super.onStartCommand(intent, flags, startId);
    }

    public class SayHello extends TimerTask {
        public void run() {
            mainRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Integer newNum = dataSnapshot.getValue(Integer.class);
                    Log.d(TAG, "getting newNum : " +newNum);
                    if (!newNum.equals(allNum))
                    {
                        NewMessageNotification.notify(getApplicationContext() ,"h",0);
                        Log.d(TAG, "after condition newNum :"+ newNum +", allNum :"+allNum);
                        allNum= newNum;
                        Log.d(TAG, "after assenyment: allNum ="+allNum +"newNum ="+newNum );
                    }
                    allNum= newNum;
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
    }
}
