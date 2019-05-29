package com.example.sic;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ShopActivity extends AppCompatActivity {
    ViewFlipper imgBanner;
    private RecyclerView mRecycleView, cRecycleView;
    private PopularAdapter mAdaptar;
    private CategoryAdapter cAdaptar;

    private FirebaseFirestore db;
    private List<Popular> mPopulars;
    private List<Category> cCategory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_main);
        imgBanner = findViewById(R.id.imgBanner);
        db = FirebaseFirestore.getInstance();
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

        db.collection("popular")
                .orderBy("product_price", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Popular popular = document.toObject(Popular.class);
                                mPopulars.add(popular);
                                Log.d("att", document.getId() + " => " + document.getData());
                            }
                            mAdaptar = new PopularAdapter(ShopActivity.this, mPopulars);
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

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
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
}
