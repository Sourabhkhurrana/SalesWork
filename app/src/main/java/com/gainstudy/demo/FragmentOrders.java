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
import android.widget.AdapterView;
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
public class FragmentOrders extends Fragment {


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

    public FragmentOrders() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_fragment_orders, container, false);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            Toast.makeText(getActivity(),"Please Log In to Continue",Toast.LENGTH_LONG).show();
            startActivity(new Intent(getActivity(), LoginActivity.class));
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


        order_spinner = (Spinner) view.findViewById(R.id.order_spinner);
        order_radioGroup = (RadioGroup) view.findViewById(R.id.order_radioGroup);
        order_submitButton = (Button) view.findViewById(R.id.order_submitButton);
        order_cancelButton = (Button) view.findViewById(R.id.order_cancelButton);
        order_radioButton6_3 = (RadioButton) view.findViewById(R.id.order_radioButton6_3);
        order_radioButton6_4 = (RadioButton) view.findViewById(R.id.order_radioButton6_4);
        order_textViewAmount_TV = (TextView) view.findViewById(R.id.order_textViewAmount_TV);

        order_spinner.setPrompt("Choose an option...");
        order_radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                order_radioButton = (RadioButton) view.findViewById(selectedId);
                String abc = order_radioButton.getText().toString();
                if (abc.equals("Packaged 6*3 pack")) {
                    int value = Integer.parseInt(order_spinner.getSelectedItem().toString());
                    order_textViewAmount_TV.setText("" + (value * 5));
                }
                if (abc.equals("Packaged 6*4 pack")){
                    int value = Integer.parseInt(order_spinner.getSelectedItem().toString());
                    order_textViewAmount_TV.setText("" + (value * 10));
                }
            }
        });

        order_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View V, int position, long l) {

                int selectedId = order_radioGroup.getCheckedRadioButtonId();
                order_radioButton = (RadioButton) view.findViewById(selectedId);
                //textViewAmount.setText(rb.getId() + "  " + spin.getSelectedItem());

                String abc = order_radioButton.getText().toString();
                if (abc.equals("Packaged 6*3 pack")) {
                    int value = Integer.parseInt(order_spinner.getSelectedItem().toString());
                    order_textViewAmount_TV.setText("" + (value * 5));
                }
                if (abc.equals("Packaged 6*4 pack")){
                    int value = Integer.parseInt(order_spinner.getSelectedItem().toString());
                    order_textViewAmount_TV.setText("" + (value * 10));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });



        final ProgressBar order_progressBar = (ProgressBar) view.findViewById(R.id.order_progressBar);
        final TextView order_customerId_TV = (TextView) view.findViewById(R.id.order_customerId_TV);
        final TextView order_custContactNo_TV = (TextView) view.findViewById(R.id.order_custContactNo_TV);
        final TextView order_email_TV = (TextView) view.findViewById(R.id.order_email_TV);
        final TextView order_name_TV = (TextView) view.findViewById(R.id.order_name_TV);
        final TextView order_date_TV = (TextView) view.findViewById(R.id.order_date_TV);
        SetDisable();


        order_DATE = DateUtils.formatDateTime(getContext(), System.currentTimeMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR);
        order_date_TV.setText(order_DATE);

        final DatabaseReference mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        Query query = mFirebaseDatabaseReference.child("profile").orderByChild("uid").equalTo(mFirebaseUser.getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Object object = child.getValue();

                    order_stringUID = ((Map) object).get("uid").toString();
                    order_stringphone = ((Map) object).get("phone").toString();
                    order_stringEmailId = ((Map) object).get("email").toString();
                    order_stringName = ((Map) object).get("firstName").toString()+" "+(((Map) object).get("lastName")).toString();
                    order_stringUserID = ((Map) object).get("userId").toString();
                    order_index = ((Map) object).get("index").toString();


                    order_customerId_TV.setText(order_stringUserID);
                    order_custContactNo_TV.setText(order_stringphone);
                    order_email_TV.setText(order_stringEmailId);
                    order_name_TV.setText(order_stringName);
                }
                order_progressBar.setVisibility(View.INVISIBLE);
                SetEnable();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Snackbar.make(view, "Error occurred!!! May be internet is not working please try again later.", Snackbar.LENGTH_LONG).show();
            }
        });

        order_submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                SetDisable();
                order_progressBar.setVisibility(View.VISIBLE);

                try {
                    int selectedId = order_radioGroup.getCheckedRadioButtonId();
                    order_radioButton = (RadioButton) view.findViewById(selectedId);

                    //Toast.makeText(getActivity(), "Firebase Auth", Toast.LENGTH_LONG).show();
                    final FreeSamples samples = new FreeSamples();
                    samples.setName(order_stringName);
                    samples.setEmail(order_stringEmailId);
                    samples.setPhone(order_stringphone);
                    samples.setDate(order_DATE);
                    samples.setStatus("waiting");
                    samples.setPackagedIce(order_radioButton.getText().toString());
                    samples.setQuantity(order_spinner.getSelectedItem().toString());
                    samples.setUid(mFirebaseAuth.getCurrentUser().getUid());
                    try {
                        order_orderNo = Integer.parseInt(order_index);
                        order_orderNo++;
                        mFirebaseDatabaseReference.child(Order).push().setValue(samples)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        //orderNo++;
                                        //Toast.makeText(getActivity(),"Within Completelistener",Toast.LENGTH_LONG).show();
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
                                                                order_progressBar.setVisibility(View.INVISIBLE);
                                                                SetEnable();
                                                            }
                                                        })
                                                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int which) {

                                                                HomeFragment fragment2 = new HomeFragment();
                                                                FragmentManager fragmentManager = getFragmentManager();
                                                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                                                fragmentTransaction.replace(R.id.fragment_container, fragment2);
                                                                fragmentTransaction.commit();
                                                            }
                                                        })
                                                        .setIcon(android.R.drawable.sym_def_app_icon)
                                                        .show();

                                                String Uid = mFirebaseAuth.getCurrentUser().getUid();
                                                mFirebaseDatabaseReference.child("profile").child(Uid).child("index").setValue(order_orderNo);
                                            }
                                        }, 2000);
                                    }
                                });
                    } catch (Exception e) {
                        SetEnable();
                        Log.e("Erorrrr ", e.getMessage());
                    }

                } catch (Exception e) {
                    Log.e("Error", "" + e);
                }
            }
        });

        order_cancelButton.setOnClickListener(new View.OnClickListener() {
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

        return view;
    }

    private void SetEnable() {
        order_radioGroup.setEnabled(true);
        order_spinner.setEnabled(true);
        order_submitButton.setEnabled(true);
        order_cancelButton.setEnabled(true);
        order_radioButton6_3.setEnabled(true);
        order_radioButton6_4.setEnabled(true);

    }

    private void SetDisable() {
        order_radioGroup.setEnabled(false);
        order_spinner.setEnabled(false);
        order_submitButton.setEnabled(false);
        order_cancelButton.setEnabled(false);
        order_radioButton6_3.setEnabled(false);
        order_radioButton6_4.setEnabled(false);
    }

}
