package com.example.sic;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UploadActivity extends AppCompatActivity {

    private static final int RESULT_LOAD_IMAGE = 1;
    private Button mSelectBtn;
    private RecyclerView mUploadList;

    private List<String> fileNameList;
    private List<String> fileDoneList;
    private List<Bitmap> fileImageList;

    private int i = 0;

    private UploadListAdapter uploadListAdapter;

    private StorageReference mStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        mStorage = FirebaseStorage.getInstance().getReference();

        mSelectBtn = (Button) findViewById(R.id.select_btn);
        mUploadList = (RecyclerView) findViewById(R.id.upload_list);

        fileNameList = new ArrayList<>();
        fileDoneList = new ArrayList<>();
        fileImageList = new ArrayList<>();

        uploadListAdapter = new UploadListAdapter(fileNameList, fileDoneList, fileImageList);

        //RecyclerView

        mUploadList.setLayoutManager(new LinearLayoutManager(this));
        mUploadList.setHasFixedSize(true);
        mUploadList.setAdapter(uploadListAdapter);


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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK){

            if(data.getClipData() != null){

                int totalItemsSelected = data.getClipData().getItemCount() + i;
                int j = 0;

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

                            fileDoneList.remove(finalI);
                            fileDoneList.add(finalI, "done");
                            System.out.println("Uploading" + fileDoneList);

                            uploadListAdapter.notifyDataSetChanged();

                        }
                    });

                }

                //Toast.makeText(MainActivity.this, "Selected Multiple Files", Toast.LENGTH_SHORT).show();

            } else if (data.getData() != null){

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

                        fileDoneList.remove(finalI);
                        fileDoneList.add(finalI, "done");
                        System.out.println("Uploading" + fileDoneList);

                        uploadListAdapter.notifyDataSetChanged();

                    }
                });
                i++;

                //Toast.makeText(MainActivity.this, "Selected Single File", Toast.LENGTH_SHORT).show();

            }

        }

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




}











