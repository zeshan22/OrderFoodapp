package com.example.orderfoodapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.Toast;

import com.example.orderfoodapp.Common.Common;
import com.example.orderfoodapp.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Signup_ac extends AppCompatActivity {


    AppCompatEditText editText,editText1,editText2;
    AppCompatButton button12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {




        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_ac);


        editText=(AppCompatEditText) findViewById(R.id.name_TextField);
        editText1=(AppCompatEditText) findViewById(R.id.phone_TextField);
        editText2=(AppCompatEditText) findViewById(R.id.passw_TextField);
        button12=(AppCompatButton) findViewById(R.id.sign_upbtnn);


        final FirebaseDatabase database=FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference=database.getReference("user");


        button12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (Common.isConnectedToInternet(getBaseContext())) {

                    final ProgressDialog progressDialog = new ProgressDialog(Signup_ac.this);
                    progressDialog.setMessage("Please wait....");
                    progressDialog.show();


                    databaseReference.addValueEventListener(new ValueEventListener() {


                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {


                            if (dataSnapshot.child(editText1.getText().toString()).exists()) {

                                progressDialog.dismiss();
                                Toast.makeText(Signup_ac.this, "Phone number already registered.", Toast.LENGTH_LONG).show();
                            } else {

                                progressDialog.dismiss();
                                User user = new User(editText.getText().toString(), editText2.getText().toString());
                                databaseReference.child(editText1.getText().toString()).setValue(user);

                                Toast.makeText(Signup_ac.this, "Sign up successful", Toast.LENGTH_LONG).show();
                                finish();


                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
                else {

                    Toast.makeText(Signup_ac.this, "Please check your connection....", Toast.LENGTH_SHORT).show();
                    return;
                }
                }

        });

    }
}
