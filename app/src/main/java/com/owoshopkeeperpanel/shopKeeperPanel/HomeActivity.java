package com.owoshopkeeperpanel.shopKeeperPanel;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.owoshopkeeperpanel.R;
import com.owoshopkeeperpanel.Model.Offers;
import com.owoshopkeeperpanel.Model.Products;
import com.owoshopkeeperpanel.Prevalent.Prevalent;
import com.owoshopkeeperpanel.adapters.ItemAdapter;
import com.owoshopkeeperpanel.pagination.ItemViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView recyclerView;
    private List<String> images = new ArrayList<String>();
    private ItemAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private AppCompatButton searchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerView = findViewById(R.id.recycler_view_for_products);
        searchBar = findViewById(R.id.search_product);

        getProducts();

        Paper.init(this);

        Toolbar toolbar = findViewById(R.id.toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        ImageView contact_us = findViewById(R.id.contact_us);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent=new Intent(HomeActivity.this,CartActivity.class);
                startActivity(intent);*/
            }
        });

        searchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        toggle.getDrawerArrowDrawable().setColor(Color.BLACK);//Changing navigation drawer color

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView=navigationView.getHeaderView(0);
        TextView userNameTextView=headerView.findViewById(R.id.user_profile_name);
        CircleImageView profileImageView=headerView.findViewById(R.id.user_profile_image);

        userNameTextView.setText(Prevalent.currentOnlineUser.getName());
        Picasso.get().load(Prevalent.currentOnlineUser.getImage()).placeholder(R.drawable.profile).into(profileImageView);

        contact_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(HomeActivity.this)
                        .setTitle("Contact us")
                        .setMessage("You want to call us or issue a complain? ")
                        .setPositiveButton(R.string.complain, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(HomeActivity.this, Contact_us.class);
                                intent.putExtra("mobileNumber", Prevalent.currentOnlineUser.getPhone());
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton(R.string.call, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Intent.ACTION_DIAL); //calling activity
                                intent.setData(Uri.parse("tel:+8801612201602"));
                                startActivity(intent);
                            }
                        })
                        .setIcon(R.drawable.care)
                        .show();
            }
        });

        DatabaseReference offersRef = FirebaseDatabase.getInstance().getReference().child("Offers");

        //Getting offers class from firebase

        offersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists())
                {
                    for(DataSnapshot dataSnapshot1 : snapshot.getChildren())
                    {
                        Offers offers = dataSnapshot1.getValue(Offers.class);
                        images.add(offers.getImage());
                    }
                    adapter.updateItems(images);
                }
                else
                {
                    Toast.makeText(HomeActivity.this, "No offer currently available", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HomeActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getProducts();
            }
        });
    }




    public void getProducts() {

        adapter = new ItemAdapter(this, images);
        ItemViewModel itemViewModel = ViewModelProviders.of(this).get(ItemViewModel.class);

        itemViewModel.itemPagedList.observe(this, new Observer<PagedList<Products>>() {
            @Override
            public void onChanged(@Nullable PagedList<Products> items) {
                adapter.submitList(items);
                showOnRecyclerView();
            }
        });
    }

    private void showOnRecyclerView() {

        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);//Configuring recyclerview to receive two layout manager
        ((GridLayoutManager) layoutManager).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (adapter.getItemViewType(position)) {
                    case ItemAdapter.View_Flipper:
                        return 2;
                    case ItemAdapter.Products_shower:
                        return 1;
                    default:
                        return 1;
                }
            }
        });

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if(drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        else
        {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.nav_shop)
        {
            Intent intent=new Intent(HomeActivity.this, MyShopActivity.class);
            startActivity(intent);
        }
        else if(id==R.id.nav_order)
        {

        }
        else if(id==R.id.nav_cart)
        {
            /*Intent intent=new Intent(HomeActivity.this,CartActivity.class);
            startActivity(intent);*/
        }
        else if(id==R.id.nav_wishlist)
        {

        }
        else if(id==R.id.nav_contact)
        {

        }
        else if(id==R.id.nav_settings)
        {
            Intent intent=new Intent(HomeActivity.this, SettingsActivity.class);
            startActivity(intent);
        }
        else if(id==R.id.nav_logout)
        {
            Paper.book().destroy();
            Intent intent=new Intent(HomeActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



}
