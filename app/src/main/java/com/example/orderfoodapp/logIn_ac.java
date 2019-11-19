package com.example.orderfoodapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;


import com.example.orderfoodapp.Common.Common;
import com.example.orderfoodapp.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class logIn_ac extends AppCompatActivity {



    AppCompatEditText editText,editText2;
    AppCompatButton button;
    CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_ac);

        editText=(AppCompatEditText) findViewById(R.id.username_TextField);
        editText2=(AppCompatEditText) findViewById(R.id.password_TextField);
        button=(AppCompatButton) findViewById(R.id.sign_inbtn);


        checkBox=(CheckBox) findViewById(R.id.checkbox);

        Paper.init(this);

        //init database
        final FirebaseDatabase database=FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference=database.getReference("user");

                button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Common.isConnectedToInternet(getBaseContext())) {


                    if (checkBox.isChecked()){

                        Paper.book().write(Common.User_key,editText.getText().toString());
                        Paper.book().write(Common.Password_key,editText2.getText().toString());

                    }

                    final ProgressDialog m = new ProgressDialog(logIn_ac.this);

                    m.setMessage("Please wait.....");

                    m.show();


                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {


                            if (dataSnapshot.child(editText.getText().toString()).exists()) {
                                m.dismiss();
                                User user = dataSnapshot.
                                        child(editText.getText().toString()).getValue(User.class);

                                user.setPhone(editText2.getText().toString());

                                if (user.getPassword().equals(editText2.getText().toString())) {

                                    Intent intent = new Intent(logIn_ac.this, Home.class);
                                    Common.currentUser = user;
                                    startActivity(intent);
                                    finish();


                                } else {
                                    Toast.makeText(getApplicationContext(), "Log in Failed", Toast.LENGTH_LONG).show();
                                }
                            } else {


                                Toast.makeText(getApplicationContext(), "User not found..", Toast.LENGTH_LONG).show();

                            }

                        }


                        @Override
                        public void onCancelled(DatabaseError databaseError) {


                        }
                    });

                }
                else {
                    Toast.makeText(logIn_ac.this, "Please check your connection....", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

    }

}
