package com.gainstudy.demo;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

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

public class SignupActivity extends AppCompatActivity {

    private EditText inputname, inputcontact, inputaddress, inputcity, inputpincode,
            inputstate, inputemail, inputpassword, inputconfirmpassword;
    private Button btn_registerNow;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    public static final String MESSAGES_CHILD = "profile";
    private FirebaseAuth.AuthStateListener authListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);



        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        inputname = (EditText) findViewById(R.id.input_name);
        inputcontact = (EditText) findViewById(R.id.input_contactNo);
        inputaddress = (EditText) findViewById(R.id.input_address);
        inputcity = (EditText) findViewById(R.id.input_city);
        inputpincode = (EditText) findViewById(R.id.input_pincode);
        inputstate = (EditText) findViewById(R.id.input_state);
        inputemail = (EditText) findViewById(R.id.input_email);
        inputpassword = (EditText) findViewById(R.id.input_password);
        inputconfirmpassword = (EditText) findViewById(R.id.input_confirmPassword);
        btn_registerNow = (Button) findViewById(R.id.registerNow);

        progressBar = (ProgressBar) findViewById(R.id.progress);

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    // Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    // Log.d(TAG, "onAuthStateChanged:signed_out");

                }
                // ...
            }
        };
    }

    public void onclick(final View v) {

        final DatabaseReference mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();

        final String name = inputname.getText().toString().trim();
        final String phone = inputcontact.getText().toString().trim();
        final String address = inputaddress.getText().toString().trim();
        final String city = inputcity.getText().toString().trim();
        final String pincode = inputpincode.getText().toString().trim();
        final String state = inputstate.getText().toString().trim();
        final String email = inputemail.getText().toString().trim();
        final String password = inputpassword.getText().toString().trim();
        final String confirm_password = inputconfirmpassword.getText().toString().trim();



            int status = 0;
            if (name.length() < 2) {

                status = 1;
                Snackbar.make(v, "Must Enter Name !", Snackbar.LENGTH_LONG).show();
                inputname.setError("Input name");
            }
            else if (phone.length() < 10) {
                status = 1;
                Snackbar.make(v, "Enter Valid 10 Digit Contact No ! ", Snackbar.LENGTH_LONG).show();
                inputcontact.setError("enter valid Contact No.");

            }
            else if (address.length()<1) {
                status = 1;
                Snackbar.make(v, "plese enter your address", Snackbar.LENGTH_LONG).show();
                inputaddress.setError("plese enter your address");
            }
            else if (city.length()<3) {
                status = 1;
                Snackbar.make(v, "plese enter your valid city name", Snackbar.LENGTH_LONG).show();
                inputcity.setError("plese enter your city");
            }
            else if (pincode.length()!=6) {
                status = 1;
                Snackbar.make(v, "plese enter valid pincode", Snackbar.LENGTH_LONG).show();
                inputpincode.setError("Please enter your valid pincode");
            }
            else if (state.length()<1) {
                status = 1;
                Snackbar.make(v, "plese enter your state", Snackbar.LENGTH_LONG).show();
                inputstate.setError("please enter details");
            }
            else if ((email.length() < 4) || (!email.contains("@"))) {
                status = 1;
                Snackbar.make(v, "Enter Valid Email !", Snackbar.LENGTH_LONG).show();

            }

            else if(password.length() <6)
            {
                status = 1;
                Snackbar.make(v, "Password Length Must Be Greater Than 5 ", Snackbar.LENGTH_LONG).show();
            }
            else if (!password.equals(confirm_password)) {
                status = 1;
                Snackbar.make(v, "Confirm Password Not Match !", Snackbar.LENGTH_LONG).show();
            }
            if (status == 0) {
                progressBar.setVisibility(ProgressBar.VISIBLE);
                Control_Disable();

                Query query = mFirebaseDatabaseReference.child("profile").orderByChild("phone").equalTo(phone.trim());
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {

                        if (dataSnapshot.getChildrenCount() > 0) {

                            for(DataSnapshot child: dataSnapshot.getChildren()) {
                                Object object = child.getValue();
                                Log.e("Register ", ((Map)object).get("phone").toString());
                                Log.e("Register ", ((Map)object).get("email").toString());
                                Log.e("Register ", ((Map)object).get("uid").toString());

                            }
                            Snackbar.make(v, "Already Registered", Snackbar.LENGTH_LONG).show();
                            progressBar.setVisibility(ProgressBar.INVISIBLE);
                            Control_Enable();
                        } else {


                            auth.createUserWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            //Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                                            // If sign in fails, display a message to the user. If sign in succeeds
                                            // the auth state listener will be notified and logic to handle the
                                            // signed in user can be handled in the listener.
                                            if (!task.isSuccessful()) {
                                                String Result = String.valueOf(task.getException());
                                                if (Result.contains("email")) {
                                                    Snackbar.make(v, "Email Already Exist !", Snackbar.LENGTH_LONG).show();
                                                    progressBar.setVisibility(ProgressBar.INVISIBLE);
                                                    Control_Enable();

                                                } else {
                                                    Snackbar.make(v, "No Internet !"+Result, Snackbar.LENGTH_LONG).show();
                                                    progressBar.setVisibility(ProgressBar.INVISIBLE);
                                                    Control_Enable();

                                                }
                                            } else {
                                                final RegistrationValues Values = new RegistrationValues();
                                                Values.setName(name);
                                                Values.setEmail(email);
                                                Values.setPhone(phone);
                                                Values.setAddress(address);
                                                Values.setCity(city);
                                                Values.setPincode(pincode);
                                                Values.setState(state);
                                                Values.setUid(auth.getCurrentUser().getUid());
                                                String Uid=auth.getCurrentUser().getUid();

                                                try {
                                                    mFirebaseDatabaseReference.child(MESSAGES_CHILD).child(Uid).setValue(Values);
                                                }
                                                catch (Exception e)
                                                {
                                                    Log.e("Erorrrr ",e.getMessage());
                                                }
                                                Snackbar.make(v, "Registration Done !", Snackbar.LENGTH_LONG).show();
                                                progressBar.setVisibility(ProgressBar.INVISIBLE);

                                                progressBar.setVisibility(ProgressBar.INVISIBLE);
                                                Control_Enable();

                                                startActivity(new Intent(SignupActivity.this,MainActivity.class));
                                                finish();
                                            }

                                            // ...
                                        }
                                    });
                            Control_Enable();

                        }
                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("DEBUGTAG", "NOT DONE " + databaseError.getMessage());
                        Snackbar.make(v, "Process Failed", Snackbar.LENGTH_LONG).show();
                        progressBar.setVisibility(ProgressBar.INVISIBLE);
                        Control_Enable();

                    }
                });

            }
        }

    // Enable==false all tools lable Editboxes buttons..
    public void Control_Disable() {
        inputemail.setEnabled(false);
        inputaddress.setEnabled(false);
        inputname.setEnabled(false);
        inputpassword.setEnabled(false);
        inputcontact.setEnabled(false);
        inputstate.setEnabled(false);
        inputcity.setEnabled(false);
        inputpincode.setEnabled(false);
        inputconfirmpassword.setEnabled(false);
        btn_registerNow.setEnabled(true);
        btn_registerNow.setVisibility(Button.INVISIBLE);
    }

    // Enable==true all tools lable Editboxes buttons..
    public void Control_Enable() {
        inputemail.setEnabled(true);
        inputaddress.setEnabled(true);
        inputname.setEnabled(true);
        inputpassword.setEnabled(true);
        inputcontact.setEnabled(true);
        inputstate.setEnabled(true);
        inputcity.setEnabled(true);
        inputpincode.setEnabled(true);
        inputconfirmpassword.setEnabled(true);
        btn_registerNow.setEnabled(true);
        btn_registerNow.setVisibility(Button.VISIBLE);
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }
}









//        String email = inputemail.getText().toString().trim();
//        String password = inputpassword.getText().toString().trim();
//
//        if (TextUtils.isEmpty(email)) {
//            Snackbar.make(findViewById(R.id.root), "Enter email Address!!", Snackbar.LENGTH_LONG).show();
//            //Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        if (TextUtils.isEmpty(password)) {
//            Snackbar.make(findViewById(R.id.root), "Enter password!", Snackbar.LENGTH_LONG).show();
//            //Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        if (password.length() < 6) {
//            Snackbar.make(findViewById(R.id.root), "Password too short, enter minimum 6 characters!", Snackbar.LENGTH_LONG).show();
//            //Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        progressBar.setVisibility(View.VISIBLE);
//        //create user
//        auth.createUserWithEmailAndPassword(email, password)
//                .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        Snackbar.make(findViewById(R.id.root), "Check your Email Id" + task.isSuccessful(), Snackbar.LENGTH_LONG).show();
//                        //Toast.makeText(SignUpActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
//                        progressBar.setVisibility(View.GONE);
//                        // If sign in fails, display a message to the user. If sign in succeeds
//                        // the auth state listener will be notified and logic to handle the
//                        // signed in user can be handled in the listener.
//                        if (!task.isSuccessful()) {
//                            Snackbar.make(findViewById(R.id.root), "Authentication failed." + task.getException(), Snackbar.LENGTH_LONG).show();
//                            //Toast.makeText(SignUpActivity.this, "Authentication failed." + task.getException(),
//                            //Toast.LENGTH_LONG).show();
//                        } else {
//                            startActivity(new Intent(SignupActivity.this, MainActivity.class));
//                            Snackbar.make(findViewById(R.id.root), "Welcome", Snackbar.LENGTH_LONG).show();
//                            finish();
//                        }
//                    }
//                });
//
//    }
