package com.gainstudy.demo;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentAddSalesMan extends Fragment {


    private View view;
    private EditText AddSalesman_FirstName, AddSalesman_Lastname, AddSalesman_contactNo, AddSalesman_address, AddSalesman_city, AddSalesman_pincode,
            AddSalesman_state, AddSalesman_email, AddSalesman_password, AddSalesman_confirmPassword;
    private Button AddSalesman_registerNow;
    private ProgressBar AddSalesman_progressBar;
    private FirebaseAuth auth;
    private FirebaseUser mFirebaseUser;
    private static final String MESSAGES_CHILD = "profile";
    private FirebaseAuth.AuthStateListener authListener;
    private Utility utility;
    private String mobileNo;

    public FragmentAddSalesMan() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_fragment_add_sales_man, container, false);
        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
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

        AddSalesman_FirstName = (EditText) view.findViewById(R.id.AddSalesman_FirstName);
        AddSalesman_Lastname = (EditText) view.findViewById(R.id.AddSalesman_Lastname);
        AddSalesman_contactNo = (EditText) view.findViewById(R.id.AddSalesman_contactNo);
        AddSalesman_address = (EditText) view.findViewById(R.id.AddSalesman_address);
        AddSalesman_city = (EditText) view.findViewById(R.id.AddSalesman_city);
        AddSalesman_pincode = (EditText) view.findViewById(R.id.AddSalesman_pincode);
        AddSalesman_state = (EditText) view.findViewById(R.id.AddSalesman_state);
        AddSalesman_email = (EditText) view.findViewById(R.id.AddSalesman_email);
        AddSalesman_password = (EditText) view.findViewById(R.id.AddSalesman_password);
        AddSalesman_confirmPassword = (EditText) view.findViewById(R.id.AddSalesman_confirmPassword);
        AddSalesman_registerNow = (Button) view.findViewById(R.id.AddSalesman_registerNow);

        AddSalesman_progressBar = (ProgressBar) view.findViewById(R.id.AddSalesman_progress);


        mFirebaseUser = auth.getCurrentUser();
        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            Toast.makeText(getActivity(), "Please Log In to Continue", Toast.LENGTH_LONG).show();
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }

        AddSalesman_registerNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View V) {

                try {
                    final DatabaseReference mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();

                    final String FirstName = AddSalesman_FirstName.getText().toString().trim();
                    final String LastName = AddSalesman_Lastname.getText().toString().trim();
                    final String phone = AddSalesman_contactNo.getText().toString().trim();
                    final String address = AddSalesman_address.getText().toString().trim();
                    final String city = AddSalesman_city.getText().toString().trim();
                    final String pincode = AddSalesman_pincode.getText().toString().trim();
                    final String state = AddSalesman_state.getText().toString().trim();
                    final String email = AddSalesman_email.getText().toString().trim();
                    final String password = AddSalesman_password.getText().toString().trim();
                    final String confirm_password = AddSalesman_confirmPassword.getText().toString().trim();


                    int status = 0;
                    if (FirstName.length() < 2) {

                        status = 1;
                        Snackbar.make(V, "Must Enter Name !", Snackbar.LENGTH_LONG).show();
                        AddSalesman_FirstName.setError("Input name");
                    } else if (phone.length() < 10) {
                        status = 1;
                        Snackbar.make(V, "Enter Valid 10 Digit Contact No ! ", Snackbar.LENGTH_LONG).show();
                        AddSalesman_contactNo.setError("enter valid Contact No.");

                    } else if (address.length() < 1) {
                        status = 1;
                        Snackbar.make(V, "plese enter your address", Snackbar.LENGTH_LONG).show();
                        AddSalesman_address.setError("plese enter your address");
                    } else if (city.length() < 3) {
                        status = 1;
                        Snackbar.make(V, "plese enter your valid city name", Snackbar.LENGTH_LONG).show();
                        AddSalesman_city.setError("plese enter your city");
                    } else if (pincode.length() != 6) {
                        status = 1;
                        Snackbar.make(V, "plese enter valid pincode", Snackbar.LENGTH_LONG).show();
                        AddSalesman_pincode.setError("Please enter your valid pincode");
                    } else if (state.length() < 1) {
                        status = 1;
                        Snackbar.make(V, "plese enter your state", Snackbar.LENGTH_LONG).show();
                        AddSalesman_state.setError("please enter details");
                    } else if ((email.length() < 4) || (!email.contains("@"))) {
                        status = 1;
                        Snackbar.make(V, "Enter Valid Email !", Snackbar.LENGTH_LONG).show();
                        AddSalesman_email.setError("Invalid Email Address");

                    } else if (password.length() < 6) {
                        status = 1;
                        Snackbar.make(V, "Password Length Must Be Greater Than 5 ", Snackbar.LENGTH_LONG).show();
                    } else if (!password.equals(confirm_password)) {
                        status = 1;
                        Snackbar.make(V, "Confirm Password Not Match !", Snackbar.LENGTH_LONG).show();
                    }
                    if (status == 0) {
                        AddSalesman_progressBar.setVisibility(ProgressBar.VISIBLE);
                        Control_Disable();
                        final char[] mobile = phone.toCharArray();
                        mobileNo = mobile[mobile.length - 4] + "" + mobile[mobile.length - 3] + "" + mobile[mobile.length - 2] + "" + mobile[mobile.length - 1];

                        try {

                            Query query = mFirebaseDatabaseReference.child("Sales Man").orderByChild("phone").equalTo(phone);
                            query.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {

                                    if (dataSnapshot.getChildrenCount() > 0) {

                                                    /*for (DataSnapshot child : dataSnapshot.getChildren()) {
                                                        Object object = child.getValue();
                                                        Log.e("Register ", ((Map) object).get("phone").toString());
                                                        Log.e("Register ", ((Map) object).get("email").toString());
                                                        Log.e("Register ", ((Map) object).get("uid").toString());
                                                    }*/
                                        //Snackbar.make(v, "Already Registered", Snackbar.LENGTH_LONG).show();
                                        Toast.makeText(getActivity(), " Mobile Number Already Registered", Toast.LENGTH_SHORT).show();
                                        AddSalesman_progressBar.setVisibility(ProgressBar.INVISIBLE);
                                        Control_Enable();
                                    } else {

                                        auth.createUserWithEmailAndPassword(email, password)
                                                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                                //Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                                                                // If sign in fails, display a message to the user. If sign in succeeds
                                                                // the auth state listener will be notified and logic to handle the
                                                                // signed in user can be handled in the listener.

                                                                if (!task.isSuccessful()) {
                                                                    String Result = String.valueOf(task.getException());
                                                                    Snackbar.make(V, "The email address is already in use by another account.", Snackbar.LENGTH_INDEFINITE)
                                                                            .setAction("ok", new View.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(View view) {
                                                                                    AddSalesman_email.setError("Please use another email account");
                                                                                }
                                                                            }).show();
                                                                    Log.d("Authentication Failed", Result);
                                                                    Control_Enable();
                                                                    AddSalesman_progressBar.setVisibility(ProgressBar.INVISIBLE);
                                                                } else {
                                                                    final RegistrationValues Values = new RegistrationValues();
                                                                    Values.setFirstName(FirstName);
                                                                    Values.setLastName(LastName);
                                                                    Values.setEmail(email);
                                                                    Values.setPhone(phone);
                                                                    Values.setAddress(address);
                                                                    Values.setCity(city);
                                                                    Values.setPincode(pincode);
                                                                    Values.setState(state);
                                                                    Values.setLevel(2);
                                                                    Values.setUserId(FirstName.toUpperCase().trim() + "" + mobileNo);
                                                                    Values.setUid(auth.getCurrentUser().getUid());
                                                                    String Uid = auth.getCurrentUser().getUid();

                                                                    try {
                                                                        mFirebaseDatabaseReference.child("profile").child(Uid).setValue(Values);
                                                                    } catch (Exception e) {
                                                                        Log.e("Erorrrr ", e.getMessage());
                                                                    }
                                                                    Snackbar.make(V, "Registration Done !", Snackbar.LENGTH_LONG).show();
                                                                    AddSalesman_progressBar.setVisibility(ProgressBar.INVISIBLE);
                                                                    // Control_Enable();

                                                                    new AlertDialog.Builder(getActivity())
                                                                            .setTitle("Salesman is Registered")
                                                                            .setCancelable(false)
                                                                            .setMessage("Salesman Id is " + FirstName.toUpperCase()
                                                                                    + "" + mobileNo + "\nClick Yes to proceed for further...")
                                                                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                                                public void onClick(DialogInterface dialog, int which) {

                                                                                }
                                                                            })
                                                                            .setIcon(android.R.drawable.sym_def_app_icon)
                                                                            .show();
                                                                    Control_Disable();

                                                                }

                                                                auth.signOut();
                                                            }
                                                        }

                                                );

                                        //Control_Enable();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Log.e("DEBUGTAG", "NOT DONE " + databaseError.getMessage());
                                    Snackbar.make(V, "Process Failed", Snackbar.LENGTH_LONG).show();
                                    AddSalesman_progressBar.setVisibility(ProgressBar.INVISIBLE);
                                    Control_Enable();

                                }
                            });
                        } catch (Exception e) {
                            Log.e("Final Error", "" + e);
                        }

                    }
                } catch (Exception e) {
                    Log.e("Exception", "" + e);
                }

            }

        });
        return view;
    }
//    @Override
//    public void onStart() {
//        super.onStart();
//        auth.addAuthStateListener(authListener);
//    }

    public void Control_Disable() {
        AddSalesman_email.setEnabled(false);
        AddSalesman_address.setEnabled(false);
        AddSalesman_Lastname.setEnabled(false);
        AddSalesman_FirstName.setEnabled(false);
        AddSalesman_password.setEnabled(false);
        AddSalesman_contactNo.setEnabled(false);
        AddSalesman_state.setEnabled(false);
        AddSalesman_city.setEnabled(false);
        AddSalesman_pincode.setEnabled(false);
        AddSalesman_confirmPassword.setEnabled(false);
        AddSalesman_registerNow.setEnabled(true);
        AddSalesman_registerNow.setVisibility(Button.INVISIBLE);


    }

    // Enable==true all tools lable Editboxes buttons..
    public void Control_Enable() {
        AddSalesman_email.setEnabled(true);
        AddSalesman_address.setEnabled(true);
        AddSalesman_Lastname.setEnabled(true);
        AddSalesman_FirstName.setEnabled(true);
        AddSalesman_password.setEnabled(true);
        AddSalesman_contactNo.setEnabled(true);
        AddSalesman_state.setEnabled(true);
        AddSalesman_city.setEnabled(true);
        AddSalesman_pincode.setEnabled(true);
        AddSalesman_confirmPassword.setEnabled(true);
        AddSalesman_registerNow.setEnabled(true);
        AddSalesman_registerNow.setVisibility(Button.VISIBLE);

    }
}
