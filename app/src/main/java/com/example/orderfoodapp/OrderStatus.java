package com.example.orderfoodapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.orderfoodapp.Common.Common;
import com.example.orderfoodapp.Model.Category;
import com.example.orderfoodapp.Model.Request;
import com.example.orderfoodapp.ViewHolder.MenuViewHolder;
import com.example.orderfoodapp.ViewHolder.orderviewholder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class OrderStatus extends AppCompatActivity {


    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;


    FirebaseRecyclerOptions<Request> options;

    FirebaseRecyclerAdapter<Request, orderviewholder> adapter;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceStats) {
        super.onCreate(savedInstanceStats);
        setContentView(R.layout.activity_order_status);


        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Requests");

        recyclerView = (RecyclerView) findViewById(R.id.Listorders);

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        if (getIntent()==null){

            loadOrders(Common.currentUser.getPhone());
        }

        else {

            loadOrders(getIntent().getStringExtra("userPhone"));

        }

    }

    private void loadOrders(String phone) {


        Query query = databaseReference.orderByChild("phone").equalTo(phone);


        options = new FirebaseRecyclerOptions.Builder<Request>()
                .setQuery(query, Request.class).build();


        adapter = new FirebaseRecyclerAdapter<Request, orderviewholder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull orderviewholder holder, int position, @NonNull Request req) {

                holder.txtOrderId.setText(adapter.getRef(position).getKey());
                holder.txtOrderStatus.setText(Common.covertCodeToStatus(req.getStatus()));
                holder.txtOrderAddress.setText(req.getAddress());
                holder.txtOrderPhone.setText(req.getPhone());


            }

            @NonNull
            @Override
            public orderviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_layout, parent, false);
                return new orderviewholder(view);
            }
        };

        adapter.startListening();
        recyclerView.setAdapter(adapter);


    }




}
