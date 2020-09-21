package com.owoshopkeeperpanel.shopKeeperPanel;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.LocaleList;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.os.ConfigurationCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.owoshopkeeperpanel.R;
import com.owoshopkeeperpanel.Model.Offers;
import com.owoshopkeeperpanel.Model.Products;
import com.owoshopkeeperpanel.Prevalent.Prevalent;
import com.owoshopkeeperpanel.adapters.CategoryAdapter;
import com.owoshopkeeperpanel.adapters.ImageFlipperAdapter;
import com.owoshopkeeperpanel.adapters.ItemAdapter;
import com.owoshopkeeperpanel.adapters.Product_tag;
import com.owoshopkeeperpanel.pagination.ItemViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.owoshopkeeperpanel.pagination.ItemViewModelCategory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView recyclerView;
    private List<String> images = new ArrayList<String>();
    private ItemAdapter adapter;
    private ImageFlipperAdapter imageFlipperAdapter;
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
                Intent intent=new Intent(HomeActivity.this,CartActivity.class);
                startActivity(intent);
            }
        });

        searchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
                startActivity(intent);
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

        Glide.with(getApplicationContext()).load(Prevalent.currentOnlineUser.getImage()).into(profileImageView);

        contact_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);

                View view = LayoutInflater.from(HomeActivity.this).inflate(R.layout.care_toast, null);

                Button call_us_now = view.findViewById(R.id.call_us_now);
                Button issue_a_complain = view.findViewById(R.id.issue_a_complain);

                builder.setView(view);

                final AlertDialog alertDialog = builder.create();
                alertDialog.show();
                alertDialog.getWindow().setLayout(950, 800);

                call_us_now.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_DIAL); //calling activity
                        intent.setData(Uri.parse("tel:+8801612201602"));
                        startActivity(intent);
                        alertDialog.cancel();

                    }
                });

                issue_a_complain.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(HomeActivity.this, Contact_us.class);
                        intent.putExtra("mobileNumber", Prevalent.currentOnlineUser.getPhone());
                        startActivity(intent);
                        alertDialog.cancel();
                    }
                });

            }
        });

        DatabaseReference offersRef = FirebaseDatabase.getInstance().getReference().child("Offers");

        //Getting offers class from firebase

        offersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists())
                {
                    for(DataSnapshot dataSnapshot1 : snapshot.getChildren())
                    {
                        Offers offers = dataSnapshot1.getValue(Offers.class);
                        images.add(offers.getImage());
                    }
                    imageFlipperAdapter.updateItems(images);
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
        swipeRefreshLayout.setColorSchemeResources(R.color.blue);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getProducts();
            }
        });
    }




    public void getProducts() {

        adapter = new ItemAdapter(this);
        imageFlipperAdapter = new ImageFlipperAdapter(this, images);

        int size = Prevalent.category_to_display.size();

        String[] categories = new String[size];

        for(int i=0; i<size; i++)
        {
            categories[i] = Prevalent.category_to_display.get(i);
        }

        ItemViewModel itemViewModel = ViewModelProviders.of(this, new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T)new ItemViewModel (categories);//Provide here required arguments
            }
        }).get(ItemViewModel.class);

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

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 6);//Configuring recyclerview to receive two layout manager
        ((GridLayoutManager) layoutManager).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {

                if(position == 0)
                {
                    return 6;
                }
                else if(position >= 1 && position <= Prevalent.category_to_display.size())
                {
                    return 2;
                }
                else if(position == Prevalent.category_to_display.size()+1)
                {
                    return 6;
                }
                else
                    return 3;
            }
        });

        recyclerView.setLayoutManager(layoutManager);
        Product_tag product_tag = new Product_tag(getApplicationContext());
        CategoryAdapter categoryAdapter = new CategoryAdapter(HomeActivity.this);// For showing categories
        ConcatAdapter concatAdapter = new ConcatAdapter(imageFlipperAdapter, categoryAdapter, product_tag, adapter);
        recyclerView.setAdapter(concatAdapter);
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
            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);

            View view = LayoutInflater.from(HomeActivity.this).inflate(R.layout.custom_exit_alert_dialog, null);

            Button yes = view.findViewById(R.id.leave);
            Button no = view.findViewById(R.id.not_leave);

            builder.setView(view);

            final AlertDialog alertDialog = builder.create();
            alertDialog.show();

            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                    finish();
                }
            });

            no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.cancel();
                }
            });
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
            Intent intent=new Intent(HomeActivity.this,CartActivity.class);
            startActivity(intent);
        }
        else if(id==R.id.nav_wishlist)
        {
            Intent intent = new Intent(HomeActivity.this, WishList.class);
            startActivity(intent);
        }
        else if(id==R.id.nav_contact)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);

            View view = LayoutInflater.from(HomeActivity.this).inflate(R.layout.care_toast, null);

            Button call_us_now = view.findViewById(R.id.call_us_now);
            Button issue_a_complain = view.findViewById(R.id.issue_a_complain);

            builder.setView(view);

            final AlertDialog alertDialog = builder.create();
            alertDialog.show();
            alertDialog.getWindow().setLayout(950, 800);

            call_us_now.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_DIAL); //calling activity
                    intent.setData(Uri.parse("tel:+8801612201602"));
                    startActivity(intent);
                    alertDialog.cancel();

                }
            });

            issue_a_complain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(HomeActivity.this, Contact_us.class);
                    intent.putExtra("mobileNumber", Prevalent.currentOnlineUser.getPhone());
                    startActivity(intent);
                    alertDialog.cancel();
                }
            });
        }

        else if(id==R.id.nav_settings)
        {
            Intent intent=new Intent(HomeActivity.this, SettingsActivity.class);
            startActivity(intent);
            finish();
        }

        else if(id == R.id.nav_change_language)
        {
            if(Prevalent.locale%2 == 0)
            {
                Locale locale = new Locale("bn");
                Prevalent.locale++;
                setLocale(locale);
            }
            else
            {
                Locale locale = new Locale("en");
                Prevalent.locale++;
                setLocale(locale);
            }

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

    public void setLocale(Locale languageToLoad) {
        Locale locale = languageToLoad;
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
        finish();
        startActivity(getIntent());
    }
}
