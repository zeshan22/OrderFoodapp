package com.example.orderfoodapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.orderfoodapp.Common.Common;
import com.example.orderfoodapp.Interface.ItemClickListener;
import com.example.orderfoodapp.Model.Category;
import com.example.orderfoodapp.Model.Food;
import com.example.orderfoodapp.ViewHolder.FoodViewHolder;
import com.example.orderfoodapp.ViewHolder.MenuViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Foodlist extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    String categoryId="";


    FirebaseRecyclerOptions<Food> options;
    FirebaseRecyclerAdapter<Food, FoodViewHolder> adapter;

    //Search Functionality
    FirebaseRecyclerAdapter<Food, FoodViewHolder> searchAdapter;
    List<String> suggestList = new ArrayList<>();
    MaterialSearchBar materialSearchBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foodlist);

        //firebase

        firebaseDatabase= FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("Foods");

        recyclerView=(RecyclerView) findViewById(R.id.recycler_food);
        recyclerView.setHasFixedSize(true);

        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);





        if(getIntent()!=null) {

            categoryId = getIntent().getStringExtra("CategoryId");
            if (!categoryId.isEmpty() && categoryId != null) {

                if (Common.isConnectedToInternet(this)){
                    loadListFood(categoryId);

                }
                else {

                    Toast.makeText(this, "Check your Connection......", Toast.LENGTH_SHORT).show();
                    return;
                }

            }

        }

        // Search
        materialSearchBar = (MaterialSearchBar) findViewById(R.id.searchBar);
        materialSearchBar.setHint("Enter your Food");
        loadSuggest();
        materialSearchBar.setLastSuggestions(suggestList);
        materialSearchBar.setCardViewElevation(10);
        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                List<String> suggest =new ArrayList<>();

                for(String search:suggestList){

                    if(search.toLowerCase().contains(materialSearchBar.getText().toLowerCase())){

                        suggest.add(search);

                    }

                    materialSearchBar.setLastSuggestions(suggest);



                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                // When search bar is close
                // restore original adapter

                if(!enabled){

                    recyclerView.setAdapter(adapter);
                }



            }

            @Override
            public void onSearchConfirmed(CharSequence text) {

                startSearch(text);

            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });



    }

    private void startSearch(CharSequence text) {





        Query query=databaseReference.orderByChild("name").equalTo(text.toString());
       FirebaseRecyclerOptions<Food> options = new FirebaseRecyclerOptions.Builder<Food>()
                .setQuery(query,Food.class).build();
        searchAdapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(options) {


            @Override
            protected void onBindViewHolder(FoodViewHolder ViewHolder, int i,Food model) {
                ViewHolder.food_name.setText(model.getName());
                // Picasso.get().load(model.getImage()).into(menuViewHolder.imageView, (Callback) new Snackbar.Callback());
                Picasso.get().load(model.getImage()).into(ViewHolder.food_image);
                final Food local = model;
                ViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                        Intent intent= new Intent(getApplicationContext(),Fooddetails.class);
                        intent.putExtra("FoodId",searchAdapter.getRef(position).getKey());

                        startActivity(intent);

                    }
                });
            }
            @NonNull
            @Override
            public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item,parent,false);
                return new FoodViewHolder(view);
            } };
        //  GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),2);
        // recyclerView.setLayoutManager(gridLayoutManager);
        searchAdapter.startListening();
        recyclerView.setAdapter(searchAdapter);


    }

    private void loadSuggest() {

        databaseReference.orderByChild("menuId").equalTo(categoryId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                    Food item =postSnapshot.getValue(Food.class);
                    suggestList.add(item.getName());

                }




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    private void loadListFood(String categoryId) {

        Query query=databaseReference.orderByChild("menuId").equalTo(categoryId);

       options = new FirebaseRecyclerOptions.Builder<Food>().setQuery(query,Food.class).build();

       adapter= new FirebaseRecyclerAdapter<Food, FoodViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FoodViewHolder holder, int position, @NonNull Food model) {


                holder.food_name.setText(model.getName());

                Picasso.get().load(model.getImage()).into(holder.food_image);


                final Food local= model;

                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                        Intent intent =new Intent(getApplicationContext(),Fooddetails.class);
                        intent.putExtra("FoodId",adapter.getRef(position).getKey());

                        startActivity(intent);




                    }
                });





            }

            @NonNull
            @Override
            public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item,parent,false);
                return new FoodViewHolder(view);

            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);

    }

}
