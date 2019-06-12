package com.example.sic;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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
    private View titleView;

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
                            iAdaptar.setOnItemClickListener(new OnItemClickListener() {
                                @Override
                                public void onItemClick(int position) {
                                    //Toast.makeText(ShopActivity.this, "Category " + position + " : " + cCategory.get(position).getCatname(), Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(ItemsActivity.this, ItemsContentActivity.class);
                                    intent.putExtra("item", (Parcelable) items.get(position));
                                    //intent.putExtra("category", items.get(position).getCatname());
                                    //intent.putExtra("FROM_WHERE", "ShopActivity/categoryChooser");
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }

                                @Override
                                public void onItemLongClick(int position) {
                                    showAlertDialog();
                                }
                            });
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
                            iAdaptar.setOnItemClickListener(new OnItemClickListener() {
                                @Override
                                public void onItemClick(int position) {
                                    //Toast.makeText(ShopActivity.this, "Category " + position + " : " + cCategory.get(position).getCatname(), Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(ItemsActivity.this, ItemsContentActivity.class);
                                    intent.putExtra("item", (Parcelable) items.get(position));
                                    //intent.putExtra("category", items.get(position).getCatname());
                                    //intent.putExtra("FROM_WHERE", "ShopActivity/categoryChooser");
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }

                                @Override
                                public void onItemLongClick(int position) {
                                    showAlertDialog();
                                }
                            });
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
                            iAdaptar.setOnItemClickListener(new OnItemClickListener() {
                                @Override
                                public void onItemClick(int position) {
                                    //Toast.makeText(ShopActivity.this, "Category " + position + " : " + cCategory.get(position).getCatname(), Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(ItemsActivity.this, ItemsContentActivity.class);
                                    intent.putExtra("item", (Parcelable) items.get(position));
                                    //intent.putExtra("FROM_WHERE", "ShopActivity/categoryChooser");
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }

                                @Override
                                public void onItemLongClick(int position) {
                                    showAlertDialog();
                                }
                            });
                            iRecycleView.setAdapter(iAdaptar);
                        } else {
                            Toast.makeText(getApplicationContext(), "Error getting documents." + task.getException(), Toast.LENGTH_LONG).show();
                            //Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

    }

    public void showAlertDialog(){
        LayoutInflater inflater = this.getLayoutInflater();

        titleView = inflater.inflate(R.layout.custom_title, null);
        LinearLayout linearLayout = (LinearLayout) titleView.findViewById(R.id.llsubhead);
        TextView txt = (TextView) titleView.findViewById(R.id.exemptionSubHeading4);
        txt.setText("Do You Want To Delete This item");
        linearLayout.setBackgroundColor(Color.parseColor("#7EFF0000"));

        AlertDialog.Builder builder1 = new AlertDialog.Builder(ItemsActivity.this);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        builder1.setCustomTitle(titleView);
        AlertDialog alert11 = builder1.create();
        alert11.show();


    }
}
