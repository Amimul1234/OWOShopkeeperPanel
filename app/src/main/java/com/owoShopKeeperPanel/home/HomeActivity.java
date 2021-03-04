package com.owoShopKeeperPanel.home;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.owoShopKeeperPanel.ApiAndClient.RetrofitClient;
import com.owoShopKeeperPanel.R;
import com.owoShopKeeperPanel.configurations.HostAddress;
import com.owoShopKeeperPanel.prevalent.Prevalent;
import com.owoShopKeeperPanel.homeComponents.bannerComponents.ImageFlipperAdapter;
import com.owoShopKeeperPanel.home.productPagination.ItemAdapter;
import com.owoShopKeeperPanel.adapters.Product_tag;
import com.owoShopKeeperPanel.configurations.ServiceMobile;
import com.owoShopKeeperPanel.login.LogInActivity;
import com.owoShopKeeperPanel.home.productPagination.ItemViewModel;
import com.google.android.material.navigation.NavigationView;
import com.owoShopKeeperPanel.shopKeeperPanel.Calculator;
import com.owoShopKeeperPanel.shopCart.CartActivity;
import com.owoShopKeeperPanel.homeComponents.categoryComponents.Categories;
import com.owoShopKeeperPanel.shopKeeperPanel.Contact_us;
import com.owoShopKeeperPanel.shopKeeperPanel.MyShopActivity;
import com.owoShopKeeperPanel.shopKeeperPanel.Order_list;
import com.owoShopKeeperPanel.shopKeeperPanel.SearchActivity;
import com.owoShopKeeperPanel.shopKeeperPanel.SettingsActivity;
import com.owoShopKeeperPanel.shopKeeperPanel.WishList;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView recyclerView;
    private final List<String> images = new ArrayList<>();
    private ItemAdapter adapter;
    private ImageFlipperAdapter imageFlipperAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ItemViewModel itemViewModel;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerView = findViewById(R.id.recycler_view_for_products);
        AppCompatButton searchBar = findViewById(R.id.search_product);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        ProgressBar home_progress = findViewById(R.id.home_progressbar);

        adapter = new ItemAdapter(this, home_progress);

        getProducts();

        Paper.init(this);

        Toolbar toolbar = findViewById(R.id.toolbar);

        ImageView contact_us = findViewById(R.id.contact_us);

        searchBar.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
            startActivity(intent);
            finish();
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
        CircleImageView profileImageView = headerView.findViewById(R.id.user_profile_image);

        userNameTextView.setText(Prevalent.currentOnlineUser.getName());

        Glide.with(getApplicationContext()).load(HostAddress.HOST_ADDRESS.getHostAddress()+Prevalent.currentOnlineUser.getImageUri()).
                diskCacheStrategy(DiskCacheStrategy.ALL).timeout(6000).into(profileImageView);

        contact_us.setOnClickListener(v -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);

            View view = LayoutInflater.from(HomeActivity.this).inflate(R.layout.care_toast, null);

            Button call_us_now = view.findViewById(R.id.call_us_now);
            Button issue_a_complain = view.findViewById(R.id.issue_a_complain);

            builder.setView(view);

            final AlertDialog alertDialog = builder.create();
            alertDialog.show();
            alertDialog.getWindow().setLayout(950, 800);

            call_us_now.setOnClickListener(v1 -> {
                Intent intent = new Intent(Intent.ACTION_DIAL); //calling activity
                intent.setData(Uri.parse("tel:"+ ServiceMobile.SERVICE_MOBILE.getServiceMobile())); //Configurable Service Mobile
                startActivity(intent);
                alertDialog.cancel();
            });

            issue_a_complain.setOnClickListener(v12 -> {
                Intent intent = new Intent(HomeActivity.this, Contact_us.class);
                intent.putExtra("mobileNumber", Prevalent.currentOnlineUser.getMobileNumber());
                startActivity(intent);
                alertDialog.cancel();
            });

        });

        //offerFetching

        getOfferBanners();

        bottomNavigationView.setOnNavigationItemSelectedListener(item ->
        {
            if(item.getItemId() == R.id.action_categories)
            {
                Intent intent = new Intent(HomeActivity.this, Categories.class);
                startActivity(intent);
            }
            else if(item.getItemId() == R.id.action_calculator)
            {

                Intent intent = new Intent(HomeActivity.this, Calculator.class);
                startActivity(intent);
            }
            else if(item.getItemId() == R.id.action_cart)
            {
                Intent intent = new Intent(HomeActivity.this, CartActivity.class);
                startActivity(intent);
            }
            else if(item.getItemId() == R.id.action_account)
            {
                Intent intent=new Intent(HomeActivity.this, SettingsActivity.class);
                startActivity(intent);
            }

            return true;
        });

        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.blue);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            getOfferBanners();
            itemViewModel.clear();
            getProducts();
        });
    }

    private void getOfferBanners() {

        RetrofitClient.getInstance().getApi()
                .bannerImages(Prevalent.category_to_display)
                .enqueue(new Callback<List<String>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<String>> call, @NotNull Response<List<String>> response) {
                        if(response.isSuccessful())
                        {
                            images.clear();
                            assert response.body() != null;
                            images.addAll(response.body());
                            imageFlipperAdapter.updateItems(images);
                        }
                        else
                        {
                            Toast.makeText(HomeActivity.this, "Failed to get offer banners, please try again", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<List<String>> call, @NotNull Throwable t) {
                        Log.e("Home", "Error is: "+t.getMessage());
                        Toast.makeText(HomeActivity.this, "Failed to get offer banners", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    public void getProducts()
    {
        int size = Prevalent.category_to_display.size();

        FragmentManager fragmentManager = getSupportFragmentManager();
        Lifecycle lifecycle = getLifecycle();
        imageFlipperAdapter = new ImageFlipperAdapter(this, images, fragmentManager, lifecycle, recyclerView);

        Long[] categories = new Long[size];

        for(int i=0; i<size; i++)
        {
            categories[i] = Prevalent.category_to_display.get(i);
        }

        itemViewModel = new ItemViewModel(categories);

        itemViewModel.itemPagedList.observe(this, items -> {
            adapter.submitList(items);
            showOnRecyclerView();
        });
    }

    private void showOnRecyclerView()
    {

        recyclerView.setHasFixedSize(true);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 6);//Configuring recyclerview to receive two layout manager
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if(position>=0 && position<2)
                {
                    return 6;
                }
                else
                    return 3;
            }
        });

        recyclerView.setLayoutManager(layoutManager);
        Product_tag product_tag = new Product_tag(getApplicationContext());

        ConcatAdapter concatAdapter = new ConcatAdapter(imageFlipperAdapter, product_tag, adapter);

        recyclerView.setAdapter(concatAdapter);
        adapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }


    @Override
    public void onBackPressed()
    {
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

            yes.setOnClickListener(v -> {
                alertDialog.dismiss();
                finish();
            });

            no.setOnClickListener(v -> alertDialog.cancel());
        }
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
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
            Intent intent = new Intent(HomeActivity.this, Order_list.class);
            startActivity(intent);
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

        else if(id == R.id.nav_owoLoan)
        {
            Toast.makeText(this, "Coming soon...", Toast.LENGTH_SHORT).show();
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

            call_us_now.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_DIAL); //calling activity
                intent.setData(Uri.parse("tel:+8801612201602"));
                startActivity(intent);
                alertDialog.cancel();

            });

            issue_a_complain.setOnClickListener(v -> {
                Intent intent = new Intent(HomeActivity.this, Contact_us.class);
                intent.putExtra("mobileNumber", Prevalent.currentOnlineUser.getMobileNumber());
                startActivity(intent);
                alertDialog.cancel();
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
            Locale locale;

            if(Prevalent.locale%2 == 0)
            {
                locale = new Locale("bn");
            }
            else
            {
                locale = new Locale("en");
            }
            Prevalent.locale++;
            setLocale(locale);

        }



        else if(id==R.id.nav_logout)
        {
            Paper.book().destroy();
            Intent intent=new Intent(HomeActivity.this, LogInActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setLocale(Locale languageToLoad)
    {
        Locale.setDefault(languageToLoad);
        Configuration config = new Configuration();
        config.locale = languageToLoad;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
        finish();
        startActivity(getIntent());
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        bottomNavigationView.setSelectedItemId(R.id.action_home);
    }
}
