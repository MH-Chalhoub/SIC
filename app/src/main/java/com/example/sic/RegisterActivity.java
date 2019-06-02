package com.example.sic;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {
    EditText ufirstname, ulastname, uemail, upassword, uconfpassword, ucontactno;
    Button btnRegister;
    TextInputLayout userFirstNameWrapper, userLastNameWrapper, userEmailWrapper, userPasswordWrapperm,
            userConfPasswordWrapper, userContactNoWrapper;
    private FirebaseFirestore db;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        ufirstname = findViewById(R.id.userFirstName);
        ulastname = findViewById(R.id.userLastName);
        uemail = findViewById(R.id.userEmailAddress);
        upassword = findViewById(R.id.userPassword);
        uconfpassword = findViewById(R.id.userConfirmPassword);
        ucontactno = findViewById(R.id.userContactNumber);

        userFirstNameWrapper = findViewById(R.id.userFirstNameWrapper);
        userLastNameWrapper = findViewById(R.id.userLastNameWrapper);
        userEmailWrapper = findViewById(R.id.userEmailWrapper);
        userPasswordWrapperm = findViewById(R.id.userPasswordWrapper);
        userConfPasswordWrapper = findViewById(R.id.userConfPasswordWrapper);
        userContactNoWrapper = findViewById(R.id.userContactNoWrapper);

        db = FirebaseFirestore.getInstance();

        btnRegister = findViewById(R.id.btnRegister);
        //Toast.makeText(getApplicationContext(),FirebaseAuth.getInstance().getCurrentUser().getEmail(), Toast.LENGTH_LONG).show();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAuth.getCurrentUser()!= null){
                    Intent intent = new Intent(RegisterActivity.this, ShopActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }else{

                    final String firstname = ufirstname.getText().toString().trim();
                    final String lastname = ulastname.getText().toString().trim();
                    final String email = uemail.getText().toString().trim();
                    String password = upassword.getText().toString().trim();
                    String confpassword = uconfpassword.getText().toString().trim();
                    final String contactno = ucontactno.getText().toString().trim();
                    if(firstname.isEmpty()){
                        userFirstNameWrapper.setError("Enter First Name");
                        userFirstNameWrapper.requestFocus();
                        return;
                    }
                    if(lastname.isEmpty()){
                        userLastNameWrapper.setError("Enter Last Name");
                        userLastNameWrapper.requestFocus();
                        return;
                    }
                    if(email.isEmpty()){
                        userEmailWrapper.setError("Enter Email");
                        userEmailWrapper.requestFocus();
                        return;
                    }
                    if(password.isEmpty()){
                        userPasswordWrapperm.setError("Enter Password");
                        userPasswordWrapperm.requestFocus();
                        return;
                    }
                    if(confpassword.isEmpty()){
                        userConfPasswordWrapper.setError("Enter Confirm Password");
                        userConfPasswordWrapper.requestFocus();
                        return;
                    }
                    if(!password.equals(confpassword)){
                        userConfPasswordWrapper.setError("Password didn't match");
                        userConfPasswordWrapper.requestFocus();
                        return;
                    }
                    if(contactno.isEmpty()){
                        userContactNoWrapper.setError("Enter Contact Number");
                        userContactNoWrapper.requestFocus();
                        return;
                    }
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                User user = new User(firstname, lastname , email, contactno);
                                db.collection("Users")
                                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(RegisterActivity.this, "User created successfuly.", Toast.LENGTH_LONG).show();

                                            Intent intent = new Intent(RegisterActivity.this, ShopActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                            finish();
                                        }else {
                                            Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                                /*User user = new User(firstname, lastname , email, contactno);
                                FirebaseDatabase .getInstance().getReference("Users")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(RegisterActivity.this, "User created successfuly.", Toast.LENGTH_LONG).show();
                                        }else {
                                            Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });*/
                            }
                            else {
                                Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
               }
            }
        });

    }
}
