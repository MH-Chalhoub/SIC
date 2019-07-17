package com.example.sic;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class ItemsContentActivity extends AppCompatActivity {

    ViewPager viewPager;
    ViewPagerAdapter adapter;
    TextView item_description, item_location, item_price, user_name, posted_time, itemIdAndViews;
    LinearLayout user_location;
    Item item;
    String itemId;
    FloatingActionButton fab;
    private FirebaseFirestore db;
    private Button report, sendMessage;
    private String[] reportReason = {"Inappropriate Photos","Inappropriate Text Description","Very High Price"};
    private View titleView;
    int views;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_content);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        db = FirebaseFirestore.getInstance();

        report = (Button)findViewById(R.id.report);
        sendMessage = (Button)findViewById(R.id.sendMessage);
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog();
            }
        });
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessageAlertDialog();
            }
        });

        item = getIntent().getExtras().getParcelable("item");
        itemId = getIntent().getExtras().getString("itemId", null);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //         .setAction("Action", null).show();
                fab = (FloatingActionButton) findViewById(R.id.fab);
                fab.setEnabled(false);
                DocumentReference docIdRef = db.collection("Users")
                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .collection("User_Favorite_Items")
                        .document(itemId);
                docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                db.collection("Users")
                                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .collection("User_Favorite_Items")
                                        .document(itemId)
                                        .delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d("delete", "DocumentSnapshot successfully deleted!");
                                                initializeFabButton();
                                                fab.setEnabled(true);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w("delete", "Error deleting document", e);
                                            }
                                        });
                                DocumentReference UserItemsRef = db.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                UserItemsRef.update("favoriteItemsCount", FieldValue.increment(-1));
                            } else {
                                db.collection("Users")
                                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .collection("User_Favorite_Items")
                                        .document(itemId)
                                        .set(item)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if(task.isSuccessful()){
                                                    initializeFabButton();
                                                    fab.setEnabled(true);
                                                }else {

                                                }
                                            }
                                        });
                                DocumentReference UserItemsRef = db.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                UserItemsRef.update("favoriteItemsCount", FieldValue.increment(1));
                            }
                        } else {
                            Log.d("Document", "Failed with: ", task.getException());
                        }
                    }
                });
            }
        });
        initializeFabButton();

        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.toolbar_layout);
        collapsingToolbar.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbar.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);

        item_description = findViewById(R.id.itemDescription);
        item_location = findViewById(R.id.location);
        item_price = findViewById(R.id.price);
        user_name = findViewById(R.id.userName);
        posted_time = findViewById(R.id.postedTime);
        user_location = findViewById(R.id.locationWrapper);
        itemIdAndViews = findViewById(R.id.itemIdAndViews);

        //Toast.makeText(ItemsContentActivity.this, item.getLocation(), Toast.LENGTH_SHORT).show();
        item_description.setText(item.getDescription());
        item_location.setText(item.getLocation());
        item_price.setText(item.getPrice() == 0 ? "Free" : item.getPrice()+"$");
        user_name.setText(item.getName());

        DocumentReference ItemsRef = db.collection("Items").document(itemId);
        ItemsRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        Item item = document.toObject(Item.class);
                        views = item.getViews();
                        itemIdAndViews.setText("ID : " + itemId + "      Views : " + views);
                        Log.d("LOGGER", "getViews : " + views);
                    } else {
                        Log.d("LOGGER", "No such document");
                    }
                } else {
                    Log.d("LOGGER", "get failed with ", task.getException());
                }
            }
        });


        String pattern = "yyyy MMMM dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        String date = simpleDateFormat.format(item.getPosted_time());
        posted_time.setText(date);

        user_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + item.getLocation());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });

        final TextView text = (TextView) findViewById(R.id.textView);
        viewPager = (ViewPager)findViewById(R.id.viewPager);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                text.setText((i+1) + "");
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        adapter = new ViewPagerAdapter(ItemsContentActivity.this,item.getImages());
        viewPager.setAdapter(adapter);

        getSupportActionBar().setTitle(item.getTitle());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void initializeFabButton(){
        fab = (FloatingActionButton) findViewById(R.id.fab);
        DocumentReference docIdRef = db.collection("Users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("User_Favorite_Items")
                .document(itemId);
        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        fab.setImageResource(R.drawable.btn_favorite_black);
                        Log.d("Document", "Document exists 1!");
                    } else {
                        fab.setImageResource(R.drawable.btn_favourite);
                        Log.d("Document", "Document does not exist 1!");
                    }
                } else {
                    Log.d("Document", "Failed with: ", task.getException());
                }
            }
        });
    }

    public void showAlertDialog(){
        LayoutInflater inflater = this.getLayoutInflater();

        titleView = inflater.inflate(R.layout.custom_title, null);
        LinearLayout linearLayout = (LinearLayout) titleView.findViewById(R.id.llsubhead);
        TextView txt = (TextView) titleView.findViewById(R.id.exemptionSubHeading4);
        txt.setText("What is the Problem");
        linearLayout.setBackgroundColor(Color.parseColor("#7EFF0000"));

        AlertDialog.Builder builder1 = new AlertDialog.Builder(ItemsContentActivity.this);
        builder1.setCancelable(true);
        builder1.setItems(reportReason, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, final int which) {

                final DocumentReference reportItemRef = db.collection("Items Reported")
                        .document(itemId);

                DocumentReference docIdRef = reportItemRef.collection("reporterDescription")
                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid());
                docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            Map<String, Object> reportMap = new HashMap<>();
                            reportMap.put("reportReason", reportReason[which]);
                            if (document.exists()) {
                                //reportItemRef.update("reportCount", FieldValue.increment(1));
                                reportItemRef.collection("reporterDescription")
                                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .set(reportMap)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                            }
                                        });
                                Toast.makeText(ItemsContentActivity.this, "The Old Report Will Be Overwritten", Toast.LENGTH_SHORT).show();
                                Log.d("Document", "Document exists 2!");
                            } else {
                                reportItemRef.collection("reporterDescription")
                                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .set(reportMap)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                            }
                                        });
                                Toast.makeText(ItemsContentActivity.this, "Item Reported", Toast.LENGTH_SHORT).show();
                                reportItemRef.update("reportCount", FieldValue.increment(1));
                                Log.d("Document", "Document does not exist 2!");
                            }
                        } else {
                            Log.d("Document", "Failed with: ", task.getException());
                        }
                    }
                });


            }
        });

        builder1.setCustomTitle(titleView);
        AlertDialog alert11 = builder1.create();
        alert11.show();


    }
    private void sendMessageAlertDialog(){

        LayoutInflater inflater = this.getLayoutInflater();

        titleView = inflater.inflate(R.layout.custom_title, null);

        TextView txt = (TextView) titleView.findViewById(R.id.exemptionSubHeading4);
        txt.setText("Send Message to " + item.getName());

        final String[] options = {"Send SMS", "Send Email", "Send WhatsApp"};

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ItemsContentActivity.this);
        alertDialogBuilder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, final int which) {
                switch (which) {
                    case 0:
                        Intent smsIntent = new Intent(android.content.Intent.ACTION_VIEW);
                        smsIntent.setType("vnd.android-dir/mms-sms");
                        smsIntent.putExtra("address",item.getPhone());
                        smsIntent.putExtra("sms_body","I like to get your " + item.getTitle() + " posted in SIC app");
                        startActivity(smsIntent);
                        break;
                    case 1:
                        Intent email = new Intent(Intent.ACTION_SEND);
                        email.putExtra(Intent.EXTRA_EMAIL, new String[]{ item.getEmail()});
                        email.putExtra(Intent.EXTRA_SUBJECT, "Buying request from SIC app");
                        email.putExtra(Intent.EXTRA_TEXT, "I like to get your " + item.getTitle() + " posted in SIC app");
                        email.setType("application/octet-stream");

                        startActivity(Intent.createChooser(email, "Choose an Email client :"));
                        break;
                    case 2:
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("http://api.whatsapp.com/send?phone=" + item.getPhone() + "&text=I like to get your " + item.getTitle() + " posted in SIC app"));
                        startActivity(intent);
                        break;
                }
            }
        });
        alertDialogBuilder.setCustomTitle(titleView)
                .create()
                .show();
    }
}
