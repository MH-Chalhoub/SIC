package com.example.sic;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ProfileActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private TextView textView_name, textView_email, textView_items, textView_favorite;
    LinearLayout itemsLayout, favLayout;
    EditText textView_phone;
    Button updateProfile;
    ImageView editPhone;
    int favCount = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        itemsLayout = (LinearLayout) findViewById(R.id.itemsLayout);
        favLayout = (LinearLayout) findViewById(R.id.favLayout);

        itemsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, ItemsActivity.class);
                intent.putExtra("FROM_WHERE", "ShopActivity/nav_ads");
                startActivity(intent);
                finish();
            }
        });
        favLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, ItemsActivity.class);
                intent.putExtra("FROM_WHERE", "ShopActivity/nav_fav");
                startActivity(intent);
                finish();
            }
        });

        textView_name = (TextView) findViewById(R.id.profile_name);
        textView_email = (TextView) findViewById(R.id.profile_email);
        textView_phone = (EditText) findViewById(R.id.profile_phone);
        textView_items = (TextView) findViewById(R.id.profile_items);
        textView_favorite = (TextView) findViewById(R.id.profile_favorite);
        editPhone = (ImageView) findViewById(R.id.editPhone);
        updateProfile = (Button) findViewById(R.id.updateProfile);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

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
                        textView_phone.setText(user.getContactno());
                        textView_items.setText(user.getItemsCount() + "");
                        //textView_favorite.setText(user.getFavoriteItemsCount() + "");


                    } else {
                        //Log.d(TAG, "No such document");
                    }
                } else {
                    //Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        db.collection("Users")
                .document(mAuth.getUid())
                .collection("User_Favorite_Items")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (final QueryDocumentSnapshot document : task.getResult()) {
                                favCount++;
                            }
                            //this is temporary until you fix the fav item count in user collection
                            textView_favorite.setText(favCount + "");
                        } else {
                        }
                    }
                });
        editPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(textView_phone.isEnabled()){
                    textView_phone.setEnabled(false);
                    updateProfile.setVisibility(View.INVISIBLE);
                }
                else{
                    textView_phone.setEnabled(true);
                    updateProfile.setVisibility(View.VISIBLE);
                }
            }
        });
        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String phone = textView_phone.getText().toString().trim();
                final DocumentReference reportItemRef = db.collection("Users")
                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid());
                Toast.makeText(ProfileActivity.this, "Phone Number Updated", Toast.LENGTH_SHORT).show();
                reportItemRef.update("contactno", phone);
                textView_phone.setEnabled(false);
                updateProfile.setVisibility(View.INVISIBLE);
            }
        });
    }
}
