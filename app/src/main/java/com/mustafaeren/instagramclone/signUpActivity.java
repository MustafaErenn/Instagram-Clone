package com.mustafaeren.instagramclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.UUID;

public class signUpActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    EditText emailAdresKayit;
    EditText sifreKayit;
    private FirebaseFirestore firebaseFirestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();

        emailAdresKayit = findViewById(R.id.emailAdresiKayit);
        sifreKayit = findViewById(R.id.sifreKayit);

    }

    public void girisYapButton(View view)
    {
        Intent intent = new Intent(signUpActivity.this, SignInActivity.class);
        startActivity(intent);
        finish();
    }
    public void kaydolButton(View view)
    {
        String email = emailAdresKayit.getText().toString();
        String password = sifreKayit.getText().toString();

        if(email.matches(""))
        {
            Toast.makeText(signUpActivity.this,"Email bos birakilamaz",Toast.LENGTH_LONG).show();
        }
        if(password.matches(""))
        {
            Toast.makeText(signUpActivity.this,"Sifre bos birakilamaz",Toast.LENGTH_LONG).show();
        }


        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                Toast.makeText(signUpActivity.this,"User Created",Toast.LENGTH_LONG).show();
                defaultUserProfile();



            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(signUpActivity.this,e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
            }
        });



    }

    public void defaultUserProfile()
    {

        String defaultImageName = "profileImages/default_avatar.jpg";
        StorageReference newReference = FirebaseStorage.getInstance().getReference(defaultImageName);
        newReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                String userEmail = user.getEmail();
                String downloadURL = uri.toString();
                HashMap<String,Object> userDefaultData = new HashMap<>();
                userDefaultData.put("userEmail",userEmail);
                userDefaultData.put("bio","");
                userDefaultData.put("downloadUrl",downloadURL);
                userDefaultData.put("date", FieldValue.serverTimestamp());


                //firebaseFirestore.collection("UserProfile").add(userDefaultData) OLD
                firebaseFirestore.collection("UserProfile").document(userEmail).set(userDefaultData).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Intent intent = new Intent(signUpActivity.this,InstagramFeed.class);
                        startActivity(intent);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(signUpActivity.this,e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                    }
                });
            }
        });




    }

}