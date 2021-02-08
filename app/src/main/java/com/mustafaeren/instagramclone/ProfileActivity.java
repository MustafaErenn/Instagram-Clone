package com.mustafaeren.instagramclone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    TextView emailText;
    ImageView profilePhoto;
    TextView bioText;

    ArrayList<String> currentUserEmailFromFB;
    ArrayList<String> currentUserCommentFromFB;
    ArrayList<String> currentUserImageFromFB;
    ProfileRecyclerAdapter profileRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        currentUserEmailFromFB = new ArrayList<>();
        currentUserCommentFromFB = new ArrayList<>();
        currentUserImageFromFB = new ArrayList<>();

        emailText = findViewById(R.id.emailText);
        profilePhoto = findViewById(R.id.profilePhoto);
        bioText = findViewById(R.id.biyografiText);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        getProfileDatas();
        getDataFromFBForProfile();

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        profileRecyclerAdapter = new ProfileRecyclerAdapter(currentUserEmailFromFB,currentUserCommentFromFB,currentUserImageFromFB);
        recyclerView.setAdapter(profileRecyclerAdapter);


    }

    public void profiliDuzenle(View view) {

        Intent intent = new Intent(ProfileActivity.this, changePPActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }


    public void getProfileDatas() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String currentUserEmail = user.getEmail();
//        CollectionReference collectionReference = firebaseFirestore.collection("profiles");
//        //collectionReference.whereEqualTo("userEmail",currentUserEmail).orderBy("date", Query.Direction.ASCENDING).limit(1)
//        collectionReference.whereEqualTo("userEmail",currentUserEmail).orderBy("date", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                if (error != null) {
//                    //Toast.makeText(ProfileActivity.this, error.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
//                }
//                if (value != null) {
//                    for (DocumentSnapshot snapShot : value.getDocuments()) {
//                        Map<String, Object> data = snapShot.getData();
//
//                        String bio = (String) data.get("bio");
//                        String userEmail = (String) data.get("userEmail");
//                        String downloadUrl = (String) data.get("downloadUrl");
//
//                        emailText.setText(userEmail.toString());
//                        bioText.setText(bio.toString());
//                        Picasso.get().load(downloadUrl).into(profilePhoto);
//
//                    }
//                }
//            }
//        });

        DocumentReference documentReference = firebaseFirestore.collection("UserProfile").document(currentUserEmail);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    DocumentSnapshot snapshot = task.getResult();
                    if(snapshot.exists())
                    {
                        Map<String, Object> data = snapshot.getData();

                        String bio = (String) data.get("bio");
                        String email = (String) data.get("userEmail");
                        String downloadUrl = (String) data.get("downloadUrl");

                        emailText.setText(email.toString());
                        bioText.setText(bio.toString());
                        Picasso.get().load(downloadUrl).into(profilePhoto);

                    }

                }
            }
        });

    }


    public void getDataFromFBForProfile()
    {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String currentUserEmail = user.getEmail();
        CollectionReference collectionReference = firebaseFirestore.collection("Posts");
        collectionReference.whereEqualTo("userEmail",currentUserEmail).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(ProfileActivity.this, error.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
                }
                if (value != null) {
                    for (DocumentSnapshot snapShot : value.getDocuments()) {
                        Map<String, Object> data = snapShot.getData();

                        String comment = (String) data.get("commentText");
                        String userEmail = (String) data.get("userEmail");
                        String downloadUrl = (String) data.get("downloadUrl");


                        currentUserEmailFromFB.add(userEmail);
                        currentUserCommentFromFB.add(comment);
                        currentUserImageFromFB.add(downloadUrl);

                        profileRecyclerAdapter.notifyDataSetChanged();


                    }
                }
            }
        });
    }
}