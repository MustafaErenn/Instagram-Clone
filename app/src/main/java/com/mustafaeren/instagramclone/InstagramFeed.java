package com.mustafaeren.instagramclone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

public class InstagramFeed extends AppCompatActivity {


    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    private BottomNavigationView bottomNavigationView;

    ArrayList<String> userEmailFromFB;
    ArrayList<String> userCommentFromFB;
    ArrayList<String> userImageFromFB;
    FeedRecyclerAdapter feedRecyclerAdapter;
    ArrayList<String> followList;

    SwipeRefreshLayout refreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instagram_feed);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        userEmailFromFB = new ArrayList<>();
        userCommentFromFB = new ArrayList<>();
        userImageFromFB = new ArrayList<>();
        followList = new ArrayList<>();


        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_person:
                        Intent intent = new Intent(InstagramFeed.this, ProfileActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_add:
                        Intent intentToUpload = new Intent(InstagramFeed.this, UploadActivity.class);
                        startActivity(intentToUpload);
                        break;
                    case R.id.nav_search:
                        Intent intentToSearch = new Intent(InstagramFeed.this, searchActivity.class);
                        startActivity(intentToSearch);
                        break;
                }
                return true;
            }
        });


        getFollowingListForFeed();




        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        feedRecyclerAdapter = new FeedRecyclerAdapter(userEmailFromFB, userCommentFromFB, userImageFromFB);
        recyclerView.setAdapter(feedRecyclerAdapter);

        refreshLayout = findViewById(R.id.refreshLayout);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                feedRecyclerAdapter.notifyDataSetChanged();
                refreshLayout.setRefreshing(false);
            }
        });


    }

    public void getFollowingListForFeed()
    {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        String currentUserEmail = currentUser.getEmail();




        CollectionReference collectionReference = firebaseFirestore.collection("FollowingManager");
        collectionReference.whereEqualTo("currentUser",currentUserEmail).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error!= null)
                {
                    Toast.makeText(InstagramFeed.this, error.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                }
                if(value!=null)
                {
                    followList.clear();
                    System.out.println("followlist size 1 "+ followList.size());
                    for (DocumentSnapshot snapShot : value.getDocuments()) {
                        System.out.println("forun icindeyiz");
                        Map<String, Object> data = snapShot.getData();
                        String followedUser = (String)data.get("searchedUser");
                        followList.add(followedUser);

//                        getDataForIgFeed(followList);
                    }


                }
                if(followList.size()==0)
                {
                    Toast.makeText(InstagramFeed.this, "İnsanları takip etmeye başla",Toast.LENGTH_LONG).show();
                    System.out.println("boyut sifir oldugundan ekleme yaptik");
                    followList.add("yenikayitlaraozel@gmail.com");
                    getDataForIgFeed(followList);
                }
                else
                {
                    getDataForIgFeed(followList);
                }
            }
        });




    }

    public void getDataForIgFeed(final ArrayList followList) {

//        CollectionReference collectionReference = firebaseFirestore.collection("Posts");
//        collectionReference.orderBy("date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                if (error != null) {
//                    Toast.makeText(InstagramFeed.this, error.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
//                }
//                if (value != null) {
//                    userCommentFromFB.clear();
//                    userEmailFromFB.clear();
//                    userImageFromFB.clear();
//                    for (DocumentSnapshot snapShot : value.getDocuments()) {
//                        Map<String, Object> data = snapShot.getData();
//
//                        String comment = (String) data.get("commentText");
//                        String userEmail = (String) data.get("userEmail");
//                        String downloadUrl = (String) data.get("downloadUrl");
//
//
//                        userEmailFromFB.add(userEmail);
//                        userCommentFromFB.add(comment);
//                        userImageFromFB.add(downloadUrl);
//
//                        feedRecyclerAdapter.notifyDataSetChanged();
//
//
//                    }
//                }
//            }
//        });

//        System.out.println(followList);
//
//        CollectionReference collectionReference = firebaseFirestore.collection("Posts");
//        collectionReference.whereIn("userEmail", followList).orderBy("date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                if(error!=null)
//                {
//                    //Toast.makeText(InstagramFeed.this, error.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
//                }
//                if(value!=null)
//                {
//                    System.out.println("follow list size 2 ");
//
//                    userCommentFromFB.clear();
//                    userEmailFromFB.clear();
//                    userImageFromFB.clear();
//                    for (DocumentSnapshot snapShot : value.getDocuments())
//                    {
//                        Map<String,Object> data = snapShot.getData();
//                        System.out.println("folllow listesi forun ici");
//                        String comment = (String)data.get("commentText");
//                        String userEmail = (String) data.get("userEmail");
//                        String downloadUrl = (String) data.get("downloadUrl");
//
//
//
//                        userEmailFromFB.add(userEmail);
//                        userCommentFromFB.add(comment);
//                        userImageFromFB.add(downloadUrl);
//
//                        feedRecyclerAdapter.notifyDataSetChanged();
//
//                    }
//                }
//            }
//        });

        System.out.println(followList);

        CollectionReference collectionReference = firebaseFirestore.collection("Posts");
        collectionReference.orderBy("date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error!=null)
                {
                    //Toast.makeText(InstagramFeed.this, error.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                }
                if(value!=null)
                {
                    System.out.println("follow list size 2 ");

                    userCommentFromFB.clear();
                    userEmailFromFB.clear();
                    userImageFromFB.clear();
                    for (DocumentSnapshot snapShot : value.getDocuments())
                    {
                        Map<String,Object> data = snapShot.getData();
                        System.out.println("folllow listesi forun ici");
                        String comment = (String)data.get("commentText");
                        String userEmail = (String) data.get("userEmail");
                        String downloadUrl = (String) data.get("downloadUrl");


                        if(followList.contains(userEmail))
                        {
                            userEmailFromFB.add(userEmail);
                            userCommentFromFB.add(comment);
                            userImageFromFB.add(downloadUrl);

                            feedRecyclerAdapter.notifyDataSetChanged();
                        }



                    }
                }
            }
        });



    }


}
