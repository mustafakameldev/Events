package com.mostafakamel.events;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AuthActivity extends AppCompatActivity {
    FirebaseAuth mAuth ;
    EditText emailEt , passwordEt ;
    Button signinBtn ;
    FirebaseAuth.AuthStateListener mAthListener ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        declare();
        mAthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()!= null) {
                    startActivity(new Intent(AuthActivity.this , AddEventActivity.class));
                }
            }
        };

    }
    private void declare()
    {
        passwordEt =(EditText)findViewById(R.id.passwordEt_AtuhActivity);
        emailEt =(EditText)findViewById(R.id.emailEt_AuthActivity) ;
        mAuth = FirebaseAuth.getInstance();
        signinBtn =(Button)findViewById(R.id.signInBtn_AuthActivty) ;
        signinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }
    private void signIn (){
        String email , password ;
        email = emailEt.getText().toString().trim() ;
        password = passwordEt.getText().toString().trim() ;
        if( !TextUtils.isEmpty(email) || !TextUtils.isEmpty(password))
        {
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful())
                        startActivity(new Intent(AuthActivity.this ,AddEventActivity.class) );
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AuthActivity.this, "ادخل البيانات الصحيحه من فضلك ", Toast.LENGTH_SHORT).show();
                }
            }) ;
        } else
        {
            Toast.makeText(this, "املي الخانات من فضلك! ", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAthListener);
    }
}
