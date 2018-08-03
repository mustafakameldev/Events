package com.mostafakamel.events;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AddEventActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private EditText familyET, eventET, placeET ,timeEt;
    private Spinner eventTypeSpinner;
    private DatabaseReference mainRef;
    private DatabaseReference addDateRef;
    private FirebaseAuth mAuth ;
    private String type = null ;
    private FirebaseAuth.AuthStateListener mAuthListener ;
    // ranking stuff
    private static Integer mainNum = 0 , spacificNum = 0  ;
    private DatabaseReference allRef , spacificRef ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        mAuth = FirebaseAuth.getInstance() ;
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null )
                {
                    startActivity(new Intent(AddEventActivity.this , AuthActivity.class));
                }
            }
        };
        declare();
    }
    private void declare() {
        mainRef = FirebaseDatabase.getInstance().getReference().child("allEvents");
        allRef =FirebaseDatabase.getInstance().getReference().child("allNum");
        allRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Integer main = dataSnapshot.getValue(Integer.class) ;
                mainNum = main ;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        familyET = (EditText) findViewById(R.id.familyET_addEventActivity);
        eventET = (EditText) findViewById(R.id.eventET_addActivity);
        placeET = (EditText) findViewById(R.id.placeET_addEventActivity);
        timeEt =(EditText)findViewById(R.id.eventTime_Et) ;
        eventTypeSpinner = (Spinner) findViewById(R.id.eventTypeSpinner_AddEventActivity);
        eventTypeSpinner.setOnItemSelectedListener(this);
        ArrayList<String> eventType = new ArrayList<>();
        eventType.add("فرح");
        eventType.add("واجب");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, eventType);
        eventTypeSpinner.setAdapter(dataAdapter);
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        if (item.equals("فرح")) {
            addDateRef = FirebaseDatabase.getInstance().getReference().child("celebrate");
            spacificRef = FirebaseDatabase.getInstance().getReference().child("celebrateNum");
            type = "celebrate";
            spacificRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Integer spacific = dataSnapshot.getValue(Integer.class);
                    spacificNum = spacific ;
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else if (item.equals("واجب")) {
            addDateRef = FirebaseDatabase.getInstance().getReference().child("bad");
            spacificRef = FirebaseDatabase.getInstance().getReference().child("badNum");
            type = "bad";
            spacificRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Integer spacific = dataSnapshot.getValue(Integer.class);
                    spacificNum = spacific ;
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    private void addEvent() {
        DatabaseReference addEventRef = mainRef.push();
        DatabaseReference addSpacificEvent = addDateRef.push();
        String familyString = familyET.getText().toString();
        String eventString = eventET.getText().toString();
        String eventTime = timeEt.getText().toString();
        String place = placeET.getText().toString();
        if (!TextUtils.isEmpty(familyString) || !TextUtils.isEmpty(eventString) || !TextUtils.isEmpty(place)
                || !TextUtils.isEmpty(eventTime)) {
            addEventRef.child("family").setValue(familyString);
            addEventRef.child("event").setValue(eventString);
            addEventRef.child("place").setValue(place);
            addEventRef.child("date").setValue(new SimpleDateFormat("yyyyMMdd_HHmmss")
                    .format(Calendar.getInstance().getTime()));
            addEventRef.child("time").setValue(eventTime);
            addEventRef.child("type").setValue(type);
            addEventRef.child("num").setValue(mainNum);
            allRef.setValue(mainNum -1);
            mainNum = mainNum -1 ;
            spacificRef.setValue(spacificNum-1);
            spacificNum=spacificNum -1 ;
            addSpacificEvent.child("family").setValue(familyString);
            addSpacificEvent.child("event").setValue(eventString);
            addSpacificEvent.child("place").setValue(place);
            addSpacificEvent.child("date").setValue(new SimpleDateFormat("yyyyMMdd_HHmmss")
                    .format(Calendar.getInstance().getTime()));
            addSpacificEvent.child("time").setValue(eventTime);
            addSpacificEvent.child("type").setValue(type);
            addSpacificEvent.child("num").setValue(spacificNum);

            Toast.makeText(this, "تم اضافه المناسبه ", Toast.LENGTH_SHORT).show();
            familyET.setText("");
            eventET.setText("");
            timeEt.setText("");
            placeET.setText("");
        }
        else
        {
            Toast.makeText(this, " قم بكتابه المناسبه اولا ", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.addeventmenu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.addEvent_MenuItem )
        {
            addEvent();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(AddEventActivity.this , Main2Activity.class));
    }

}
