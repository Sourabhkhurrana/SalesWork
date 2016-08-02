package com.gainstudy.demo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private Button btnSignup, btnLogin, btnReset;
    View view;
    int level = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();


        // set the view now
        setContentView(R.layout.activity_login);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        DataValues.mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        DataValues.pref = PreferenceManager.getDefaultSharedPreferences(this);
        DataValues.editor = DataValues.pref.edit();


        if (DataValues.pref.getString("level", null)!= null) {

            if (DataValues.pref.getString("level", null).equals("1"))
                startActivity(new Intent(this, AdminActivity.class));
            else if (DataValues.pref.getString("level", null).equals("2"))
                startActivity(new Intent(this, SalesmanActivity.class));
            else if (DataValues.pref.getString("level", null).equals("3"))
                startActivity(new Intent(this, MainActivity.class));
            else {
                Toast.makeText(getApplicationContext(), "value " + DataValues.pref.getString("level", null), Toast.LENGTH_LONG).show();
            }
        }

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();



        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        btnSignup = (Button) findViewById(R.id.btn_SignUp);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnReset = (Button) findViewById(R.id.btn_reset_password);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final String email = inputEmail.getText().toString();
                final String password = inputPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Snackbar.make(v, "Enter Email Address!", Snackbar.LENGTH_LONG).show();
                    //Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Snackbar.make(v, "Enter Password!", Snackbar.LENGTH_LONG).show();
                    // Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                //authenticate user
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                //progressBar.setVisibility(View.INVISIBLE);
                                if (!task.isSuccessful()) {
                                    // there was an error
                                    if (password.length() < 6) {
                                        Snackbar.make(v, "Password too short, enter minimum 6 characters!", Snackbar.LENGTH_LONG).show();
                                        //inputPassword.setError(getString(R.string.minimum_password));
                                    } else {
                                        Snackbar.make(v, getString(R.string.auth_failed), Snackbar.LENGTH_LONG).show();
                                        //Toast.makeText(LoginActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    auth = FirebaseAuth.getInstance();
                                    final DatabaseReference mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
                                    Query query = mFirebaseDatabaseReference.child("profile").orderByChild("uid").equalTo(auth.getCurrentUser().getUid());
                                    query.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                                Log.d("Check Login", child.toString());
                                                Object object = child.getValue();
                                                level = Integer.parseInt(((Map) object).get("level").toString());

                                                if (level == 1) {
                                                    //  Toast.makeText(LoginActivity.this,level,Toast.LENGTH_LONG).show();
                                                    //for admin login
                                                    DataValues.editor.putString("level", "1");
                                                    DataValues.editor.commit();
                                                    startActivity(new Intent(LoginActivity.this, AdminActivity.class));
                                                    finish();
                                                }
                                                if (level == 2) {
                                                    //Toast.makeText(LoginActivity.this,level,Toast.LENGTH_LONG).show();
                                                    //for salesman login
                                                    DataValues.editor.putString("level", "2");
                                                    DataValues.editor.commit();
                                                    startActivity(new Intent(LoginActivity.this, SalesmanActivity.class));
                                                    finish();
                                                } else if (level == 3) {
                                                    //Toast.makeText(LoginActivity.this,level,Toast.LENGTH_LONG).show();
                                                    //for customer login
                                                    DataValues.editor.putString("level", "3");
                                                    DataValues.editor.commit();
                                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                                    finish();
                                                } else {

                                                    Snackbar.make(v, "Error Occured in getting Acount Credentials, may be internet is not working", Snackbar.LENGTH_INDEFINITE)
                                                            .setAction("Ok", new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View view) {

                                                                }
                                                            });
                                                }
                                            }
                                            if (dataSnapshot.getChildrenCount()<1)
                                                progressBar.setVisibility(View.INVISIBLE);
                                                Snackbar.make(v, "     Error occurred!!! May be internet is not working please try again later.", Snackbar.LENGTH_LONG).show();

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            Snackbar.make(view, "Error occurred!!! May be internet is not working please try again later.", Snackbar.LENGTH_LONG).show();
                                        }
                                    });
                                   // Toast.makeText(LoginActivity.this,""+level,Toast.LENGTH_LONG).show();


                                }
                            }
                        });
            }
        });
    }
}