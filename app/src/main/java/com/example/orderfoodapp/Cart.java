package com.example.orderfoodapp;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.orderfoodapp.Common.Common;
import com.example.orderfoodapp.Database.Database;
import com.example.orderfoodapp.Model.Order;
import com.example.orderfoodapp.Model.Request;
import com.example.orderfoodapp.ViewHolder.CartAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import info.hoang8f.widget.FButton;

public class Cart extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    TextView totalPrice;

    AppCompatButton buttn;

    List<Order> cart = new ArrayList<>();

    CartAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("Requests");

        //init

        recyclerView =(RecyclerView) findViewById(R.id.listCard);
        recyclerView.setHasFixedSize(true);
        layoutManager= new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);

        totalPrice = (TextView) findViewById(R.id.total);
        buttn =(AppCompatButton) findViewById(R.id.placeOrder);

        buttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (cart.size()>0){

                    showAlertDailog();

                }
                else {
                    Toast.makeText(Cart.this, "Your cart is empty.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        loadListFood();

    }

    private void showAlertDailog() {

        final AlertDialog.Builder alertDailog = new AlertDialog.Builder(Cart.this);

        alertDailog.setTitle("One more step!");

        alertDailog.setMessage("Enter your address: ");

        final EditText editAddress = new EditText(Cart.this);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );

        editAddress.setLayoutParams(lp);

        alertDailog.setView(editAddress);
        alertDailog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        alertDailog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // create new request
                Request request = new Request(
                        Common.currentUser.getPhone(),
                        Common.currentUser.getName(),
                        editAddress.getText().toString(),
                        totalPrice.getText().toString(),
                        cart

                );


                //submit to firebase
                databaseReference.child(String.valueOf(System.currentTimeMillis())).setValue(request);
                // delete cart

                new  Database(getBaseContext()).cleanCart();
                Toast.makeText(Cart.this, "Thank you... Order Placed", Toast.LENGTH_SHORT).show();

                finish();


            }
        });

        alertDailog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });



        alertDailog.show();

    }


    private void loadListFood() {

        cart = new Database(this).getCart();

        adapter = new CartAdapter(cart,this);
        adapter.notifyDataSetChanged();

        recyclerView.setAdapter(adapter);

        int total=0;

        for (Order order:cart){

            total += (Integer.parseInt(order.getPrice()))*(Integer.parseInt(order.getQuantity()));

            Locale locale = new Locale("en","US");

            NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);


            totalPrice.setText(fmt.format(total));



        }




    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle().equals(Common.DELETE)){
            deleteCart(item.getOrder());
        }
        return true;
    }

    private void deleteCart(int order) {

        cart.remove(order);

        new Database(this).cleanCart();

        for (Order item:cart){

            new Database(this).addToCart(item);
        }


    }
}
