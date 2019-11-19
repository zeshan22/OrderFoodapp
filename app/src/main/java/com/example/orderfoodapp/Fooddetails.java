package com.example.orderfoodapp;

import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.orderfoodapp.Database.Database;
import com.example.orderfoodapp.Model.Food;
import com.example.orderfoodapp.Model.Order;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Fooddetails extends AppCompatActivity {


    TextView foodName,foodPrice,foodDescription;
    ImageView image;
    FloatingActionButton btn_Cart;
    ElegantNumberButton numberButton;
    CollapsingToolbarLayout collapsingToolbarLayout;

    String foodid="";


    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    Food currentFood;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fooddetails);


        foodName=(TextView)findViewById(R.id.food_name);
        foodPrice=(TextView)findViewById(R.id.food_price);
        foodDescription=(TextView)findViewById(R.id.food_description);

        image=(ImageView)findViewById(R.id.img_food);
        btn_Cart=(FloatingActionButton) findViewById(R.id.btnCart);



        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("Foods");


        collapsingToolbarLayout=(CollapsingToolbarLayout)findViewById(R.id.collapse);
        numberButton=(ElegantNumberButton) findViewById(R.id.number_button);

        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);

        btn_Cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Database(getBaseContext()).addToCart(new Order(
                        foodid,
                        currentFood.getName(),
                        numberButton.getNumber(),
                        currentFood.getPrice(),
                        currentFood.getDiscount()

                ));




                Toast.makeText(Fooddetails.this, "Added to Cart", Toast.LENGTH_SHORT).show();
                

            }
        });


        if(getIntent() != null){

            foodid = getIntent().getStringExtra("FoodId");

        }
        if(!foodid.isEmpty()){

            getDetailFood(foodid);


        }

    }

    private void getDetailFood(final String foodid) {

        databaseReference.child(foodid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                currentFood = dataSnapshot.getValue(Food.class);

                Picasso.get().load(currentFood.getImage()).into(image);

                collapsingToolbarLayout.setTitle(currentFood.getName());
                foodPrice.setText(currentFood.getPrice());
                foodName.setText(currentFood.getName());
                foodDescription.setText(currentFood.getDescription());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }
}
