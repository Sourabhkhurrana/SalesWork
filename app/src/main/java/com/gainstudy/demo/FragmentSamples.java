package com.gainstudy.demo;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentSamples extends Fragment {


    private FirebaseAuth sample_mFirebaseAuth;
    private FirebaseUser sample_mFirebaseUser;
    public View sample_view;
    private ProgressBar sample_ProgressBar;
    private Spinner sample_spinner;
    private RadioGroup sample_radioGroup;
    private RadioButton sample_radioButton63, sample_radioButton64, sample_radioButton;
    private static final String FREE_SAMPLE = "FREE SAMPLE";
    private String sample_stringUID, sample_stringName, sample_stringphone, sample_stringEmailId, sample_stringUserID;
    private Button sample_submitButton, sample_CancelButton;
    private String sample_DATE;
    private String sample_index;
    private int sample_orderNo;
    private FirebaseAuth.AuthStateListener sample_authListener;

    public FragmentSamples() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        sample_view = inflater.inflate(R.layout.fragment_fragment_samples, container, false);

        sample_mFirebaseAuth = FirebaseAuth.getInstance();
        sample_mFirebaseUser = sample_mFirebaseAuth.getCurrentUser();
        if (sample_mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            Toast.makeText(getActivity(), "Please Log In to Continue", Toast.LENGTH_LONG).show();
            startActivity(new Intent(getActivity(), LoginActivity.class));
            //finish();
            //return;
        }

        sample_authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("TAG", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("TAG", "onAuthStateChanged:signed_out");
                    sample_mFirebaseAuth.signOut();

                }
                // ...
            }
        };

        //String currentDateString = DateFormat.getDateInstance().format(new Date());
        sample_spinner = (Spinner) sample_view.findViewById(R.id.sample_spinner);

        sample_radioGroup = (RadioGroup) sample_view.findViewById(R.id.sample_radioGroup);
        sample_CancelButton = (Button) sample_view.findViewById(R.id.sample_cancelButton);
        sample_submitButton = (Button) sample_view.findViewById(R.id.sample_submitButton);
        sample_radioButton63 = (RadioButton) sample_view.findViewById(R.id.sample_radioButton6_3);
        sample_radioButton64 = (RadioButton) sample_view.findViewById(R.id.sample_radioButton6_4);

        sample_ProgressBar = (ProgressBar) sample_view.findViewById(R.id.sample_progressBar);
        final TextView sample_CustomerId = (TextView) sample_view.findViewById(R.id.sample_customerId_TV);
        final TextView sample_custContactNo = (TextView) sample_view.findViewById(R.id.sample_custContactNo_TV);
        final TextView sample_emailfragment = (TextView) sample_view.findViewById(R.id.sample_email_TV);
        final TextView sample_nameTextView = (TextView) sample_view.findViewById(R.id.sample_name_TV);
        final TextView sample_Date = (TextView) sample_view.findViewById(R.id.sample_date_TV);

        sample_DATE = DateUtils.formatDateTime(getContext(), System.currentTimeMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR);
        sample_Date.setText(sample_DATE);

        sample_SetDisable();

        final DatabaseReference mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        Query query = mFirebaseDatabaseReference.child("profile").orderByChild("uid").equalTo(sample_mFirebaseUser.getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Object object = child.getValue();

                    sample_stringUID = ((Map) object).get("uid").toString();
                    sample_stringphone = ((Map) object).get("phone").toString();
                    sample_stringEmailId = ((Map) object).get("email").toString();
                    sample_stringName = ((Map) object).get("firstName").toString() + " " + (((Map) object).get("lastName")).toString();
                    sample_stringUserID = ((Map) object).get("userId").toString();
                    sample_index = ((Map) object).get("index").toString();


                    sample_CustomerId.setText(sample_stringUserID);
                    sample_custContactNo.setText(sample_stringphone);
                    sample_emailfragment.setText(sample_stringEmailId);
                    sample_nameTextView.setText(sample_stringName);
                }
                sample_ProgressBar.setVisibility(View.INVISIBLE);
                sample_SetEnable();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Snackbar.make(sample_view, "Error occurred!!! May be internet is not working please try again later.", Snackbar.LENGTH_LONG).show();
            }
        });

        sample_submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                sample_SetDisable();
                sample_ProgressBar.setVisibility(View.VISIBLE);
                int selectedId = sample_radioGroup.getCheckedRadioButtonId();
                sample_radioButton = (RadioButton) sample_view.findViewById(selectedId);

                //Toast.makeText(getActivity(), "Firebase Auth", Toast.LENGTH_LONG).show();
                final FreeSamples samples = new FreeSamples();
                samples.setName(sample_stringName);
                samples.setEmail(sample_stringEmailId);
                samples.setPhone(sample_stringphone);
                samples.setDate(sample_DATE);
                samples.setStatus("waiting");
                samples.setPackagedIce(sample_radioButton.getText().toString());
                samples.setQuantity(sample_spinner.getSelectedItem().toString());
                samples.setUid(sample_mFirebaseAuth.getCurrentUser().getUid());
                try {
                    sample_orderNo = Integer.parseInt(sample_index);
                    sample_orderNo++;
                    mFirebaseDatabaseReference.child(FREE_SAMPLE).push().setValue(samples)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    //orderNo++;
                                    Toast.makeText(getActivity(), "Within Completelistener", Toast.LENGTH_LONG).show();
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            //Do something after 100ms
                                            Toast.makeText(getActivity(), "Done", Toast.LENGTH_SHORT).show();
                                            new AlertDialog.Builder(getActivity())
                                                    .setTitle("Thank You for Place Order")
                                                    .setCancelable(false)
                                                    .setMessage("Click Ok for place more Order now..........")
                                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.dismiss();
                                                            sample_ProgressBar.setVisibility(View.INVISIBLE);
                                                            sample_SetEnable();
                                                        }
                                                    })
                                                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {

                                                            final Handler handler = new Handler();
                                                            handler.postDelayed(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    HomeFragment fragment2 = new HomeFragment();
                                                                    FragmentManager fragmentManager = getFragmentManager();
                                                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                                                    fragmentTransaction.replace(R.id.fragment_container, fragment2);
                                                                    fragmentTransaction.commit();
                                                                    //closefragment();
                                                                }
                                                            }, 3000);

                                                        }

                                                    })
                                                    .setIcon(android.R.drawable.sym_def_app_icon)
                                                    .show();
                                        }
                                    }, 2000);
                                }
                            });

                    mFirebaseDatabaseReference.child("WaitList").child(sample_stringUID).setValue(samples);
                } catch (Exception e) {
                    sample_SetEnable();
                    Log.e("Erorrrr ", e.getMessage());
                }
                //Toast.makeText(getActivity(),"Bahr aa gaye",Toast.LENGTH_LONG).show();


            }
        });
        sample_CancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HomeFragment fragment2 = new HomeFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment2);
                fragmentTransaction.commit();
                //closefragment();
                Toast.makeText(getActivity(), "Fragment Close", Toast.LENGTH_SHORT).show();
            }
        });

        return sample_view;

    }

    private void sample_SetEnable() {
        sample_radioGroup.setEnabled(true);
        sample_spinner.setEnabled(true);
        sample_submitButton.setEnabled(true);
        sample_CancelButton.setEnabled(true);
        sample_radioButton63.setEnabled(true);
        sample_radioButton64.setEnabled(true);

    }

    private void sample_SetDisable() {
        sample_radioGroup.setEnabled(false);
        sample_spinner.setEnabled(false);
        sample_CancelButton.setEnabled(false);
        sample_submitButton.setEnabled(false);
        sample_radioButton63.setEnabled(false);
        sample_radioButton64.setEnabled(false);
    }

}
