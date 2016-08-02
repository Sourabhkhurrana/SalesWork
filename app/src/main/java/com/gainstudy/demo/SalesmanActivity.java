package com.gainstudy.demo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SalesmanActivity extends AppCompatActivity {

    private Spinner order_spinner;
    private TextView order_textViewAmount_TV;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    public View view;
    private RadioGroup order_radioGroup;
    private RadioButton order_radioButton6_3, order_radioButton6_4, order_radioButton;
    private static final String Order = "Order";
    private String order_stringUID, order_stringName, order_stringphone, order_stringEmailId, order_stringUserID;
    private Button order_submitButton, order_cancelButton;
    private String order_DATE, order_index;
    private int order_orderNo;
    private FirebaseAuth.AuthStateListener authListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salesman);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            Toast.makeText(this,"Please Log In to Continue",Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, LoginActivity.class));
            //finish();
            //return;
        }
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("TAG", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("TAG", "onAuthStateChanged:signed_out");
                    mFirebaseAuth.signOut();

                }
                // ...
            }
        };



    }
}
