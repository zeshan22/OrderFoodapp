package com.example.orderfoodapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.Toast;

import com.example.orderfoodapp.Common.Common;
import com.example.orderfoodapp.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {


    AppCompatButton button1,button2;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Paper.init(this);


    button1=(AppCompatButton) findViewById(R.id.login_);
    button2=(AppCompatButton) findViewById(R.id.signup_);

    button1.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent send = new Intent(MainActivity.this, logIn_ac.class);
            startActivity(send);

        }
    });


        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendd = new Intent(MainActivity.this, Signup_ac.class);
                startActivity(sendd);

            }
        });


        //check remember

        String user= Paper.book().read(Common.User_key);
        String pwd=Paper.book().read(Common.Password_key);

        if (user!=null && pwd!=null){

            if (!user.isEmpty() && !pwd.isEmpty()){
                login(user,pwd);
                
            }
        }





    }

    private void login(final String phone, String pwd) {

        if (Common.isConnectedToInternet(getBaseContext())) {


            //init database
            final FirebaseDatabase database=FirebaseDatabase.getInstance();
            final DatabaseReference databaseReference=database.getReference("user");

            final ProgressDialog m = new ProgressDialog(MainActivity.this);

            m.setMessage("Please wait.....");

            m.show();


            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {


                    if (dataSnapshot.child(phone).exists()) {
                        m.dismiss();
                        User user = dataSnapshot.
                                child(phone).getValue(User.class);

                        user.setPhone(phone);

                        if (user.getPassword().equals(phone)) {

                            Intent intent = new Intent(MainActivity.this, Home.class);
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
            Toast.makeText(MainActivity.this, "Please check your connection....", Toast.LENGTH_SHORT).show();
            return;
        }
    }
}
