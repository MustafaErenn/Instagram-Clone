package com.mustafaeren.instagramclone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

import java.io.ObjectStreamException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class searchedProfileActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    TextView searchedProfileEmail;
    TextView searchedProfileBio;
    ImageView searchedProfilePP;

    ArrayList<String> searchedProfileEmailList;
    ArrayList<String> searchedProfilePostList;
    ArrayList<String> searchedProfileCommentList;

    String searchedIntentEmail;
    ProfileRecyclerAdapter profileRecyclerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searched_profile);

        searchedProfileEmail = findViewById(R.id.searchedProfileEmailText);
        searchedProfilePP = findViewById(R.id.searchedProfilePhoto);
        searchedProfileBio = findViewById(R.id.searchedBiyografiText);

        searchedProfileEmailList = new ArrayList<>();
        searchedProfilePostList = new ArrayList<>();
        searchedProfileCommentList = new ArrayList<>();

        searchedIntentEmail = getIntent().getStringExtra("searchedEmail");
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        getDataSearchedProfile(searchedIntentEmail);
        getSearchedProfilePosts(searchedIntentEmail);
        RecyclerView recyclerView = findViewById(R.id.searchedRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        profileRecyclerAdapter = new ProfileRecyclerAdapter(searchedProfileEmailList,searchedProfileCommentList,searchedProfilePostList);
        recyclerView.setAdapter(profileRecyclerAdapter);
    }

    public void getDataSearchedProfile(String paramEmail)
    {
//        CollectionReference collectionReference  = firebaseFirestore.collection("profiles");
//        collectionReference.whereEqualTo("userEmail",paramEmail).orderBy("date", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                if(value!=null)
//                {
//                    for (DocumentSnapshot snapShot : value.getDocuments()) {
//                        Map<String, Object> data = snapShot.getData();
//
//                        String bio = (String) data.get("bio");
//                        String userEmail = (String) data.get("userEmail");
//                        String downloadUrl = (String) data.get("downloadUrl");
//
//                        searchedProfileEmail.setText(userEmail.toString());
//                        searchedProfileBio.setText(bio.toString());
//                        Picasso.get().load(downloadUrl).into(searchedProfilePP);
//
//                    }
//                }
//            }
//        });

        DocumentReference documentReference = firebaseFirestore.collection("UserProfile").document(paramEmail);
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

                        searchedProfileEmail.setText(email.toString());
                        searchedProfileBio.setText(bio.toString());
                        Picasso.get().load(downloadUrl).into(searchedProfilePP);

                    }

                }
            }
        });
    }
    public void followButton(View view)
    {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String current_user = user.getEmail();
        HashMap<String, Object> data = new HashMap<>();
        data.put("currentUser",current_user);
        data.put("searchedUser",searchedIntentEmail);

        firebaseFirestore.collection("FollowingManager").add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Intent intentToFeed = new Intent(searchedProfileActivity.this,InstagramFeed.class);
                intentToFeed.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentToFeed);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(searchedProfileActivity.this,e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
            }
        });


    }
    public void getSearchedProfilePosts(String paramEmail2)
    {
        CollectionReference collectionReference = firebaseFirestore.collection("Posts");
        collectionReference.whereEqualTo("userEmail",paramEmail2).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value!=null)
                {
                    for(DocumentSnapshot snapshot : value.getDocuments())
                    {
                        Map<String,Object> data = snapshot.getData();

                        String comment = (String) data.get("commentText");
                        String userEmail = (String) data.get("userEmail");
                        String downloadUrl = (String) data.get("downloadUrl");


                        searchedProfileEmailList.add(userEmail);
                        searchedProfileCommentList.add(comment);
                        searchedProfilePostList.add(downloadUrl);

                        profileRecyclerAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

}