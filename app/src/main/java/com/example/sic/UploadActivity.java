package com.example.sic;

import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Date;

public class UploadActivity extends AppCompatActivity {

    private static final int RESULT_LOAD_IMAGE = 1;
    private Button mSelectBtn, submitBtn;
    private RecyclerView mUploadList;

    private List<String> fileNameList;
    private List<String> fileDoneList;
    private List<Bitmap> fileImageList;

    private int i = 0;
    int totalItemsSelected_1, itemInlist = 0;

    Place place;
    AlertDialog.Builder alertDialogBuilder;
    View titleView;

    private UploadListAdapter uploadListAdapter;
    TextInputLayout enterTitleWrapper, enterCategoryWrapper, enterDescriptionWrapper, enterLocationWrapper, enterNameWrapper
            , enterEmailWrapper, enterPhoneWrapper, enterPriceWrapper;
    EditText enterTitle, enterCategory, enterDescription, enterLocation, enterName
            , enterEmail, enterPhone, enterPrice;

    private StorageReference mStorage;
    private FirebaseFirestore db;

    private List<String> cCategory;
    private String[] category_names;
    private ArrayList<String> images;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        cCategory = new ArrayList<>();

        mStorage = FirebaseStorage.getInstance().getReference();

        fillAddItem();

        enterTitle = findViewById(R.id.enterTitle);
        enterCategory = findViewById(R.id.enterCategory);
        enterDescription = findViewById(R.id.enterDescription);
        enterLocation = findViewById(R.id.enterLocation);
        enterName = findViewById(R.id.enterName);
        enterEmail = findViewById(R.id.enterEmail);
        enterPhone = findViewById(R.id.enterPhone);
        enterPrice = findViewById(R.id.enterPrice);

        enterTitleWrapper = findViewById(R.id.enterTitleWrapper);
        enterCategoryWrapper = findViewById(R.id.enterCategoryWrapper);
        enterDescriptionWrapper = findViewById(R.id.enterDescriptionWrapper);
        enterLocationWrapper = findViewById(R.id.enterLocationWrapper);
        enterNameWrapper = findViewById(R.id.enterNameWrapper);
        enterEmailWrapper = findViewById(R.id.enterEmailWrapper);
        enterPhoneWrapper = findViewById(R.id.enterPhoneWrapper);
        enterPriceWrapper = findViewById(R.id.enterPriceWrapper);

        mSelectBtn = (Button) findViewById(R.id.select_btn);
        submitBtn = (Button) findViewById(R.id.btnSubmit);

        mUploadList = (RecyclerView) findViewById(R.id.upload_list);

        enterEmail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        fileNameList = new ArrayList<>();
        fileDoneList = new ArrayList<>();
        fileImageList = new ArrayList<>();
        images = new ArrayList<>();

        totalItemsSelected_1 = 0;

        uploadListAdapter = new UploadListAdapter(fileNameList, fileDoneList, fileImageList);

        //RecyclerView

        mUploadList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mUploadList.setHasFixedSize(true);
        mUploadList.setAdapter(uploadListAdapter);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String title = enterTitle.getText().toString().trim();
                final String category = enterCategory.getText().toString().trim();
                final String description = enterDescription.getText().toString().trim();
                final String location = enterLocation.getText().toString().trim();
                final String name = enterName.getText().toString().trim();
                final String email = enterEmail.getText().toString().trim();
                final String phone = enterPhone.getText().toString().trim();
                final Float price = Float.parseFloat(enterPrice.getText().toString().trim());

                if(title.isEmpty() || title.length()<5){
                    enterTitleWrapper.setError("Enter Title And Title length must be grater than 5");
                    enterTitleWrapper.requestFocus();
                    return;
                }
                else{
                    enterTitleWrapper.setErrorEnabled(false);
                }
                if(category.isEmpty()){
                    enterCategoryWrapper.setError("Enter Category");
                    enterCategoryWrapper.requestFocus();
                    return;
                }
                else{
                    enterCategoryWrapper.setErrorEnabled(false);
                }
                if(description.isEmpty() && title.length()>5){
                    enterDescriptionWrapper.setError("Enter Description");
                    enterDescriptionWrapper.requestFocus();
                    return;
                }
                else{
                    enterDescriptionWrapper.setErrorEnabled(false);
                }
                if(location.isEmpty()){
                    enterLocationWrapper.setError("Enter Location");
                    enterLocationWrapper.requestFocus();
                    return;
                }
                else{
                    enterLocationWrapper.setErrorEnabled(false);
                }
                if(name.isEmpty() || title.length()<5){
                    enterNameWrapper.setError("Enter Name And Name length must be grater than 5");
                    enterNameWrapper.requestFocus();
                    return;
                }
                else{
                    enterNameWrapper.setErrorEnabled(false);
                }
                if(email.isEmpty()){
                    enterEmailWrapper.setError("Enter Email");
                    enterEmailWrapper.requestFocus();
                    return;
                }
                else{
                    enterEmailWrapper.setErrorEnabled(false);
                }
                if(phone.isEmpty()){
                    enterPhoneWrapper.setError("Enter Phone Number");
                    enterPhoneWrapper.requestFocus();
                    return;
                }
                else{
                    enterPhoneWrapper.setErrorEnabled(false);
                }
                if(phone.isEmpty()){
                    enterPriceWrapper.setError("Enter Item Price");
                    enterPriceWrapper.requestFocus();
                    return;
                }
                else{
                    enterPriceWrapper.setErrorEnabled(false);
                }
                Date date = new Date();
                Item item = new Item(images,title, category, description, location, name, email, phone, price,date);
                db.collection("Items")
                        .add(item)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(UploadActivity.this, "Item added successfuly.", Toast.LENGTH_LONG).show();
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(UploadActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                db.collection("Users")
                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .collection("User_Items")
                        .add(item)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(UploadActivity.this, "Item added successfuly to User_Items.", Toast.LENGTH_LONG).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(UploadActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                DocumentReference UserItemsRef = db.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
                UserItemsRef.update("itemsCount", FieldValue.increment(1));

                finish();
            }
        });

        mSelectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), RESULT_LOAD_IMAGE);

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //Toast.makeText(UploadActivity.this, "onBackPressed", Toast.LENGTH_LONG).show();
        for(int i=0; i<images.size(); i++){
            StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(images.get(i));
            storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    // File deleted successfully
                    Log.e("firebasestorage", "onSuccess: deleted file");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Uh-oh, an error occurred!
                    Log.e("firebasestorage", "onFailure: did not delete file");
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            totalItemsSelected_1 = data.getClipData().getItemCount();
            //data.removeExtra("Select Picture");
        }catch (Exception e){

        }

        if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK){
            if(data.getClipData() != null && totalItemsSelected_1 + itemInlist  <= 3){
                int totalItemsSelected = data.getClipData().getItemCount() + i;
                int j = 0;
                totalItemsSelected_1 += data.getClipData().getItemCount();
                for(; i < totalItemsSelected; i++,j++){
                    Uri fileUri = data.getClipData().getItemAt(j).getUri();
                    Random random = new Random();
                    String fileName = Math.abs(random.nextInt()) + " - " + getFileName(fileUri);
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), fileUri);
                        fileImageList.add(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    fileNameList.add(fileName);
                    fileDoneList.add("uploading");
                    uploadListAdapter.notifyDataSetChanged();
                    System.out.println("Uploading" + fileDoneList);
                    StorageReference fileToUpload = mStorage.child("Images").child(fileName);
                    final int finalI = i;
                    fileToUpload.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    images.add(uri.toString());
                                    //Toast.makeText(UploadActivity.this, image, Toast.LENGTH_LONG).show();
                                    //System.out.println("file link : " + image);
                                }
                            });
                            fileDoneList.remove(finalI);
                            fileDoneList.add(finalI, "done");
                            System.out.println("Uploading" + fileDoneList);
                            uploadListAdapter.notifyDataSetChanged();
                        }
                    });
                }
                //Toast.makeText(MainActivity.this, "Selected Multiple Files", Toast.LENGTH_SHORT).show();
            } else if (data.getData() != null && itemInlist + 1  <= 3){
                int totalItemsSelected = i + 1;
                Uri fileUri = data.getData();
                Random random = new Random();
                String fileName = Math.abs(random.nextInt()) + " - " + getFileName(fileUri);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), fileUri);
                    fileImageList.add(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                fileNameList.add(fileName);
                fileDoneList.add("uploading");
                uploadListAdapter.notifyDataSetChanged();
                System.out.println("Uploading" + fileDoneList);
                StorageReference fileToUpload = mStorage.child("Images").child(fileName);
                final int finalI = i;
                fileToUpload.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                images.add(uri.toString());
                                //Toast.makeText(UploadActivity.this, image, Toast.LENGTH_LONG).show();
                                //System.out.println("file link : " + image);
                            }
                        });
                        fileDoneList.remove(finalI);
                        fileDoneList.add(finalI, "done");
                        System.out.println("Uploading" + fileDoneList);
                        uploadListAdapter.notifyDataSetChanged();
                    }
                });
                i++;
                //Toast.makeText(MainActivity.this, "Selected Single File", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(UploadActivity.this, "You must SELECT less then 3 picture", Toast.LENGTH_SHORT).show();
                //data.setClipData(ClipData.newIntent("", null));
            }
        }
        itemInlist = fileNameList.size();
        System.out.println("Total Item Selected : " + totalItemsSelected_1 + "/ itemInlist : " + itemInlist);

    }



    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    public void fillAddItem()
    {
        db = FirebaseFirestore.getInstance();

        enterCategory = findViewById(R.id.enterCategory);
        enterLocation = findViewById(R.id.enterLocation);
        db.collection("Category")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Category category = document.toObject(Category.class);
                                cCategory.add(category.getCatname());
                                category_names = new String[cCategory.size()];
                                category_names = cCategory.toArray(category_names);
                                enterCategory.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        LayoutInflater inflater = UploadActivity.this.getLayoutInflater();
                                        titleView = inflater.inflate(R.layout.custom_title, null);
                                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(UploadActivity.this);
                                        TextView textView = (TextView) titleView.findViewById(R.id.exemptionSubHeading4);
                                        textView.setText("Choose Your Category");
                                        alertDialogBuilder.setItems(category_names, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                enterCategory.setText(category_names[which]);
                                                switch (which) {
                                                    case 0: // horse
                                                    case 1: // cow
                                                    case 2: // camel
                                                    case 3: // sheep
                                                    case 4: // goat
                                                }
                                            }
                                        });
                                        alertDialogBuilder.setCustomTitle(titleView);
                                        //alertDialogBuilder.setCancelable(false);
                                        AlertDialog alertDialog = alertDialogBuilder.create();
                                        alertDialog.show();
                                    }
                                });
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Error getting documents." + task.getException(), Toast.LENGTH_LONG).show();
                            //Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
        enterLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog();
            }
        });
    }
    private void showAlertDialog(){

        LayoutInflater inflater = this.getLayoutInflater();

        titleView = inflater.inflate(R.layout.custom_title, null);

        place = new Place();

        alertDialogBuilder = new AlertDialog.Builder(UploadActivity.this);
        alertDialogBuilder.setItems(place.getKaza_names(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, final int which) {
                AlertDialog.Builder alertDialogBuilder1 = new AlertDialog.Builder(UploadActivity.this);
                alertDialogBuilder1.setItems(place.getPlace(which), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which1) {
                        Toast.makeText(UploadActivity.this, place.getKaza_names()[which] + "/" + place.getPlace(which)[which1]
                                , Toast.LENGTH_LONG).show();

                        enterLocation.setText(place.getKaza_names()[which] + " / " + place.getPlace(which)[which1]);
                    }
                });

                LayoutInflater inflater1 = UploadActivity.this.getLayoutInflater();

                View titleView1 = inflater1.inflate(R.layout.custom_title, null);
                TextView txt2 = titleView1.findViewById(R.id.exemptionSubHeading4);
                txt2.setTypeface(null, Typeface.NORMAL);
                addTitle(place.getKaza_names()[which], titleView1);
                alertDialogBuilder1.setCustomTitle(titleView1)
                        .create()
                        .show();
            }
        });
        alertDialogBuilder.setCustomTitle(titleView)
                .create()
                .show();
    }

    public void addTitle(String Title, View titleView){

        LinearLayout linearLayout = (LinearLayout) titleView.findViewById(R.id.llsubhead);
        TextView txt1 = new TextView(UploadActivity.this);
        txt1.setText(Title);
        txt1.setTypeface(null, Typeface.BOLD);
        txt1.setTextColor(Color.rgb(0,0,0));
        txt1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(convertDpToPx(UploadActivity.this, 20),
                convertDpToPx(UploadActivity.this, 10),
                convertDpToPx(UploadActivity.this, 15),
                convertDpToPx(UploadActivity.this, 10));
        txt1.setLayoutParams(params);
        linearLayout.addView(txt1);

        View view = new View(UploadActivity.this);
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                convertDpToPx(UploadActivity.this, 1));
        view.setLayoutParams(params1);
        view.setBackgroundColor(Color.parseColor("#5B332F2F"));
        linearLayout.addView(view);

    }

    public int convertDpToPx(Context context, float dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }


}











