package com.mustafaeren.instagramclone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;

public class searchActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    EditText searchingEditText;

    ArrayList<String> searchedProfilesEmailFromFB;
    ArrayList<String> searchedProfilesBioFromFB;
    ArrayList<String> searchedProfilePhotosFromFB;
    SearchRecyclerAdapter searchRecyclerAdapter;


    ArrayList<String> userEmailFromFB;
    ArrayList<String> userCommentFromFB;
    ArrayList<String> userImageFromFB;
    kesfetRecyclerAdapter kesfetRecyclerAdapterXX;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchedProfilesEmailFromFB = new ArrayList<>();
        searchedProfilesBioFromFB = new ArrayList<>();
        searchedProfilePhotosFromFB = new ArrayList<>();

        searchingEditText=findViewById(R.id.searchEmailEditText);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        userEmailFromFB = new ArrayList<>();
        userCommentFromFB = new ArrayList<>();
        userImageFromFB = new ArrayList<>();

        //getDataForSearching();

        RecyclerView recyclerView = findViewById(R.id.recyclerViewForSearch);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchRecyclerAdapter = new SearchRecyclerAdapter(searchedProfilesEmailFromFB,searchedProfilesBioFromFB,searchedProfilePhotosFromFB);
        recyclerView.setAdapter(searchRecyclerAdapter);

//        getDataForKesfet();
//        RecyclerView recyclerView2 = findViewById(R.id.recyclerViewKesfet);
//        recyclerView2.setLayoutManager(new LinearLayoutManager(this));
//        kesfetRecyclerAdapterXX = new kesfetRecyclerAdapter(userEmailFromFB,userCommentFromFB,userImageFromFB);
//        recyclerView.setAdapter(kesfetRecyclerAdapterXX);




    }
    public void searchButton(View view)
    {
        String searchedEmail = searchingEditText.getText().toString();
        getDataForSearching(searchedEmail);
    }

    public void getDataForSearching(String arananEmail)
    {
        CollectionReference collectionReference = firebaseFirestore.collection("profiles");
        //collectionReference.whereArrayContains("userEmail", searchingEditText.getText().toString()).orderBy("date", Query.Direction.ASCENDING)
        //collectionReference.whereEqualTo("userEmail","mustafa@gmail.com").
        System.out.println("ARANAN EMAIL = "+ arananEmail);
        //collectionReference.whereEqualTo("userEmail",arananEmail).orderBy("date", Query.Direction.DESCENDING).limit(1).
        //collectionReference.limit(1).whereEqualTo("userEmail",arananEmail).orderBy("date", Query.Direction.DESCENDING).
        //collectionReference.whereEqualTo("userEmail",arananEmail).orderBy("date", Query.Direction.DESCENDING).limit(1).
//        collectionReference.whereEqualTo("userEmail",arananEmail).orderBy("date", Query.Direction.DESCENDING).limit(1).addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                if (value != null) {
//                    searchedProfilesEmailFromFB.clear();
//                    searchedProfilesBioFromFB.clear();
//                    searchedProfilePhotosFromFB.clear();
//                    System.out.println("FORUN HEMEN USTU");
//                    for (DocumentSnapshot snapShot : value.getDocuments()) {
//                        System.out.println("FORUN HEMEN ICI");
//                        Map<String, Object> data = snapShot.getData();
//
//                        String bio = (String) data.get("bio");
//                        String email = (String) data.get("userEmail");
//                        String downloadUrl = (String) data.get("downloadUrl");
//
//
//                        searchedProfilesEmailFromFB.add(email);
//                        searchedProfilesBioFromFB.add(bio);
//                        searchedProfilePhotosFromFB.add(downloadUrl);
//
//                        searchRecyclerAdapter.notifyDataSetChanged();
//
//                    }
//                }
//
//            }
//        });

//        collectionReference.whereEqualTo("userEmail",arananEmail).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()) {
//
//                    searchedProfilesEmailFromFB.clear();
//                    searchedProfilesBioFromFB.clear();
//                    searchedProfilePhotosFromFB.clear();
//                    System.out.println("FORUN HEMEN USTU");
//                    for (DocumentSnapshot snapShot : task.getResult()) {
//                        System.out.println("FORUN HEMEN ICI");
//                        Map<String, Object> data = snapShot.getData();
//
//                        String bio = (String) data.get("bio");
//                        String email = (String) data.get("userEmail");
//                        String downloadUrl = (String) data.get("downloadUrl");
//
//
//                        searchedProfilesEmailFromFB.add(email);
//                        searchedProfilesBioFromFB.add(bio);
//                        searchedProfilePhotosFromFB.add(downloadUrl);
//
//                        searchRecyclerAdapter.notifyDataSetChanged();
//                    }
//                }
//                else
//                {
//                    System.out.println("TASK FAILED");
//                }
//            }
//        });
        DocumentReference documentReference = firebaseFirestore.collection("UserProfile").document(arananEmail);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    DocumentSnapshot snapshot = task.getResult();
                    searchedProfilesEmailFromFB.clear();
                    searchedProfilesBioFromFB.clear();
                    searchedProfilePhotosFromFB.clear();
                    if(snapshot.exists())
                    {
                        Map<String, Object> data = snapshot.getData();

                        String bio = (String) data.get("bio");
                        String email = (String) data.get("userEmail");
                        String downloadUrl = (String) data.get("downloadUrl");


                        searchedProfilesEmailFromFB.add(email);
                        searchedProfilesBioFromFB.add(bio);
                        searchedProfilePhotosFromFB.add(downloadUrl);

                        searchRecyclerAdapter.notifyDataSetChanged();

                    }

                }
                else {
                    System.out.println("TASK FAILED");
                }

            }
        });


    }


    public void getDataForKesfet()
    {
                CollectionReference collectionReference = firebaseFirestore.collection("Posts");
        collectionReference.orderBy("date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error!=null)
                {
                    Toast.makeText(searchActivity.this, error.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                }
                if(value!=null)
                {
                    userCommentFromFB.clear();
                    userEmailFromFB.clear();
                    userImageFromFB.clear();
                    for (DocumentSnapshot snapShot : value.getDocuments())
                    {
                        Map<String,Object> data = snapShot.getData();

                        String comment = (String)data.get("commentText");
                        String userEmail = (String) data.get("userEmail");
                        String downloadUrl = (String) data.get("downloadUrl");



                        userEmailFromFB.add(userEmail);
                        userCommentFromFB.add(comment);
                        userImageFromFB.add(downloadUrl);

                        kesfetRecyclerAdapterXX.notifyDataSetChanged();



                    }
                }
            }
        });
    }
}