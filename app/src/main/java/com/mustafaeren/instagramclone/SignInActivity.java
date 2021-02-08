package com.mustafaeren.instagramclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {

    EditText emailGiris;
    EditText sifreGiris;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        emailGiris = findViewById(R.id.emailAdresiGiris);
        sifreGiris = findViewById(R.id.sifreGiris);

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser!=null)
        {
            Intent intent = new Intent(SignInActivity.this, InstagramFeed.class);
            startActivity(intent);
            finish();
        }

    }
    public void girisYapClicked(View view)
    {
        String email = emailGiris.getText().toString();
        String password = sifreGiris.getText().toString();


        if(email.matches(""))
        {
            Toast.makeText(SignInActivity.this,"Email bos birakilamaz",Toast.LENGTH_LONG).show();
        }
        if(password.matches(""))
        {
            Toast.makeText(SignInActivity.this,"Sifre bos birakilamaz",Toast.LENGTH_LONG).show();
        }
        if(!email.matches("") && !password.matches(""))
        {
            firebaseAuth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Intent intent = new Intent(SignInActivity.this,InstagramFeed.class);
                    startActivity(intent);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SignInActivity.this,e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                }
            });
        }

    }
    public void kaydolButton(View view)
    {
        Intent intent = new Intent(SignInActivity.this, signUpActivity.class);
        startActivity(intent);
        finish();
    }
}