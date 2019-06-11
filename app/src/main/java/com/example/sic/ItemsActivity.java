package com.example.sic;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ItemsActivity extends AppCompatActivity {
    private RecyclerView iRecycleView;
    private ItemsAdapter iAdaptar;

    private FirebaseFirestore db;
    private List<Item> items;
    private String category,fromWhere,searchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);

        db = FirebaseFirestore.getInstance();
        category = getIntent().getExtras().getString("category",null);
        searchText = getIntent().getExtras().getString("searchText",null);
        fromWhere = getIntent().getExtras().getString("FROM_WHERE",null);
        //Toast.makeText(ItemsActivity.this,category , Toast.LENGTH_SHORT).show();
        if(fromWhere.equals("ShopActivity/categoryChooser")) {
            //Toast.makeText(ItemsActivity.this,"1" , Toast.LENGTH_SHORT).show();
            showItemsFromCategory();
        }
        else if(fromWhere.equals("ShopActivity/searchBar")){
            //Toast.makeText(ItemsActivity.this,"2" , Toast.LENGTH_SHORT).show();
            showItemsFromSearchBar();
        }
        else if(fromWhere.equals("ShopActivity/nav_ads")){
            //Toast.makeText(ItemsActivity.this,"2" , Toast.LENGTH_SHORT).show();
            showItemsFromMyads();
        }
    }
    public void showItemsFromCategory()
    {
        iRecycleView = findViewById(R.id.items_view);
        iRecycleView.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        iRecycleView.setLayoutManager(mLayoutManager);

        items = new ArrayList<>();

        db.collection("Items")
                .whereEqualTo("category", category)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Item item = document.toObject(Item.class);
                                items.add(item);
                                Log.d("att", document.getId() + " => " + document.getData());
                            }
                            iAdaptar = new ItemsAdapter(ItemsActivity.this, items);
                            iRecycleView.setAdapter(iAdaptar);
                        } else {
                            Toast.makeText(getApplicationContext(), "Error getting documents." + task.getException(), Toast.LENGTH_LONG).show();
                            //Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

    }
    public void showItemsFromSearchBar()
    {
        iRecycleView = findViewById(R.id.items_view);
        iRecycleView.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        iRecycleView.setLayoutManager(mLayoutManager);

        items = new ArrayList<>();

        db.collection("Items")
                .whereEqualTo("title", searchText)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Item item = document.toObject(Item.class);
                                items.add(item);
                                Log.d("att", document.getId() + " => " + document.getData());
                            }
                            iAdaptar = new ItemsAdapter(ItemsActivity.this, items);
                            iRecycleView.setAdapter(iAdaptar);
                        } else {
                            Toast.makeText(getApplicationContext(), "Error getting documents." + task.getException(), Toast.LENGTH_LONG).show();
                            //Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

    }
    public void showItemsFromMyads()
    {
        iRecycleView = findViewById(R.id.items_view);
        iRecycleView.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        iRecycleView.setLayoutManager(mLayoutManager);

        items = new ArrayList<>();

        db.collection("Users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("User_Items")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Item item = document.toObject(Item.class);
                                items.add(item);
                                Log.d("att", document.getId() + " => " + document.getData());
                            }
                            iAdaptar = new ItemsAdapter(ItemsActivity.this, items);
                            iRecycleView.setAdapter(iAdaptar);
                        } else {
                            Toast.makeText(getApplicationContext(), "Error getting documents." + task.getException(), Toast.LENGTH_LONG).show();
                            //Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

    }
}
