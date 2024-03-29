package com.example.orderfoodapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.orderfoodapp.Common.Common;
import com.example.orderfoodapp.Interface.ItemClickListener;
import com.example.orderfoodapp.Model.Category;
import com.example.orderfoodapp.Service.ListenOrder;
import com.example.orderfoodapp.ViewHolder.MenuViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import io.paperdb.Paper;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseDatabase database;
    DatabaseReference category;
    TextView textView;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;


    FirebaseRecyclerOptions<Category> options;
    FirebaseRecyclerAdapter<Category,MenuViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Menu");
        setSupportActionBar(toolbar);

        //init Firebase
        database=FirebaseDatabase.getInstance();
        category=database.getReference("Category");

        Paper.init(this);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Cart.class);
                startActivity(intent);

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Set Name for users
        View headerView=navigationView.getHeaderView(0);
        textView=(TextView) headerView.findViewById(R.id.txt_fullName);
        textView.setText(Common.currentUser.getName());

        //load Menu

        recyclerView=(RecyclerView) findViewById(R.id.recycler_mune);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);



        if (Common.isConnectedToInternet(this)){

            loadMenu();



        }

        else {
            Toast.makeText(Home.this, "Please check your connection....", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent service =new Intent(Home.this, ListenOrder.class);
        startService(service);



    }

    private void loadMenu() {
            options = new FirebaseRecyclerOptions.Builder<Category>()
                    .setQuery(category,Category.class).build();
            adapter = new FirebaseRecyclerAdapter<Category, MenuViewHolder>(options) {


                @Override
                protected void onBindViewHolder(MenuViewHolder menuViewHolder, int i,Category model) {
                    menuViewHolder.txtMenuName.setText(model.getName());
                   // Picasso.get().load(model.getImage()).into(menuViewHolder.imageView, (Callback) new Snackbar.Callback());
                    Picasso.get().load(model.getImage()).into(menuViewHolder.imageView);
                    final Category clickItem = model;
                    menuViewHolder.setItemClickListener(new ItemClickListener() {
                        @Override
                        public void onClick(View view, int position, boolean isLongClick) {

                            Intent intent= new Intent(getApplicationContext(),Foodlist.class);
                            intent.putExtra("CategoryId",adapter.getRef(position).getKey());

                            startActivity(intent);

                        }
                    });
                }
                @NonNull
                @Override
                public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item,parent,false);
                    return new MenuViewHolder(view);
                } };
          //  GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),2);
           // recyclerView.setLayoutManager(gridLayoutManager);
            adapter.startListening();
            recyclerView.setAdapter(adapter);

     //   adapter.notifyDataSetChanged();
        }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        if (item.getItemId()==R.id.action_settings){

            loadMenu();
        }

        return super.onOptionsItemSelected(item);


    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_menu) {
            // Handle the camera action
        } else if (id == R.id.nav_cart) {

            Intent intent = new Intent(getApplicationContext(),Cart.class);
            startActivity(intent);

        } else if (id == R.id.nav_orders) {


            Intent intent = new Intent(getApplicationContext(),OrderStatus.class);
            startActivity(intent);

        } else if (id == R.id.nav_log_out) {

            Paper.book().destroy();

            Intent intent = new Intent(getApplicationContext(),logIn_ac.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
