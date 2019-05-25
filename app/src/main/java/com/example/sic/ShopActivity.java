package com.example.sic;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShopActivity extends AppCompatActivity {
    ViewFlipper imgBanner;
    private RecyclerView mRecycleView, cRecycleView;
    private PopularAdapter mAdaptar;
    private CategoryAdapter cAdaptar;

    private DatabaseReference mDatabaseRef;
    private List<Popular> mPopulars;
    private List<Category> cCategory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_main);
        imgBanner = findViewById(R.id.imgBanner);
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
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("popular");
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren())
                {
                    Popular popular = postSnapshot.getValue(Popular.class);
                    mPopulars.add(popular);
                }
                mAdaptar = new PopularAdapter(ShopActivity.this, mPopulars);
                mRecycleView.setAdapter(mAdaptar);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    public void showCategories()
    {
        cRecycleView = findViewById(R.id.category_view);
        cRecycleView.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        cRecycleView.setLayoutManager(mLayoutManager);

        cCategory = new ArrayList<>();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Category");
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren())
                {
                    Category category = postSnapshot.getValue(Category.class);
                    cCategory.add(category);
                }
                cAdaptar = new CategoryAdapter(ShopActivity.this, cCategory);
                cRecycleView.setAdapter(cAdaptar);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }
}
