package com.example.sic;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ShopActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{
    ViewFlipper imgBanner;
    private RecyclerView mRecycleView, cRecycleView;
    private PopularAdapter mAdaptar;
    private CategoryAdapter cAdaptar;

    private FirebaseFirestore db;
    private List<Popular> mPopulars;
    private List<Category> cCategory;
    private TextView textView_name, textView_email;

    private FirebaseAuth mAuth;

    private SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_drawer);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        imgBanner = findViewById(R.id.imgBanner);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        int sliders[] = {
                R.drawable.general_banner, R.drawable.electronic_banner, R.drawable.selfcare_banner
        };
        for(int slide : sliders)
        {
            bannerFlipper(slide);
        }
        showCategories();
        showPopularProducts();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "ADD Item Images", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent intent = new Intent(ShopActivity.this, UploadActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        handleIntent(getIntent());

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        textView_name = (TextView) headerView.findViewById(R.id.header_name);
        textView_email = (TextView) headerView.findViewById(R.id.header_email);

        DocumentReference docRef = db.collection("Users").document(mAuth.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        //Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        User user = document.toObject(User.class);
                        String firstname = user.getFirstname().toUpperCase();
                        String lastname = user.getLastname().toUpperCase();
                        char s1 = firstname.charAt(0);

                        textView_name.setText(s1 + user.getFirstname().substring(1) + " " + lastname);
                        textView_email.setText(user.getEmail());

                    } else {
                        //Log.d(TAG, "No such document");
                    }
                } else {
                    //Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }
    public void bannerFlipper (int image){
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(image);
        imgBanner.addView(imageView);
        imgBanner.setFlipInterval(5000);
        imgBanner.setAutoStart(true);
        //imgBanner.setInAnimation(this, android.R.anim.fade_in);
        //imgBanner.setOutAnimation(this, android.R.anim.fade_out);
    }

    public void showPopularProducts()
    {
        mRecycleView = findViewById(R.id.recycler_view);
        mRecycleView.setHasFixedSize(true);
        mRecycleView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        mPopulars = new ArrayList<>();

        db.collection("popular")
                .orderBy("product_price", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        mAdaptar = new PopularAdapter(ShopActivity.this, mPopulars);
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Popular popular = document.toObject(Popular.class);
                                popular.setProduct_id(document.getId());
                                mPopulars.add(popular);
                                Log.d("att", document.getId() + " => " + document.getData());
                            }
                            mAdaptar.setOnItemClickListener(new PopularAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(int position) {
                                    //mPopulars.get(position).changeTitle("Clicked");
                                    if(mPopulars.get(position).getProduct_favorite() == 0){
                                        mPopulars.get(position).changeFavorite(1);
                                        db.collection("popular")
                                                .document(mPopulars.get(position).getProduct_id())
                                                .update("product_favorite", 1);
                                    }
                                    else {
                                        mPopulars.get(position).changeFavorite(0);
                                        db.collection("popular")
                                                .document(mPopulars.get(position).getProduct_id())
                                                .update("product_favorite", 0);
                                    }
                                    mAdaptar.notifyItemChanged(position);
                                }
                            });
                            mRecycleView.setAdapter(mAdaptar);
                        } else {
                            Toast.makeText(getApplicationContext(), "Error getting documents." + task.getException(), Toast.LENGTH_LONG).show();
                            //Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }
    public void showCategories()
    {
        cRecycleView = findViewById(R.id.category_view);
        cRecycleView.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        cRecycleView.setLayoutManager(mLayoutManager);

        cCategory = new ArrayList<>();

        db.collection("Category")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Category category = document.toObject(Category.class);
                                cCategory.add(category);
                                Log.d("att", document.getId() + " => " + document.getData());
                            }
                            cAdaptar = new CategoryAdapter(ShopActivity.this, cCategory);
                            cRecycleView.setAdapter(cAdaptar);
                        } else {
                            Toast.makeText(getApplicationContext(), "Error getting documents." + task.getException(), Toast.LENGTH_LONG).show();
                            //Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_account) {
            Intent intent = new Intent(ShopActivity.this, ProfileActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_ads) {
            Intent intent = new Intent(ShopActivity.this, ItemsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_fav) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        } else if (id == R.id.nav_logout){
            mAuth.getInstance().signOut();
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                //Toast.makeText(MainActivity.this, "onMenuItemActionExpand", Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                //Toast.makeText(MainActivity.this, "onMenuItemActionCollapse", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Toast.makeText(MainActivity.this, query, Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Toast.makeText(MainActivity.this, newText, Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo( searchManager.getSearchableInfo(getComponentName()) );


        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Toast.makeText(ShopActivity.this, query, Toast.LENGTH_SHORT).show();

            searchView.setQuery(query, false);
            searchView.clearFocus();
        }
    }
}
