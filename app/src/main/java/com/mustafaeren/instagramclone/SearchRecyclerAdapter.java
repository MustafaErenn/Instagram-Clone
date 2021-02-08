package com.mustafaeren.instagramclone;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SearchRecyclerAdapter extends RecyclerView.Adapter<SearchRecyclerAdapter.PostHolder> {

    private ArrayList<String> UserProfileEmailList;
    private ArrayList<String> UserProfileBioList;
    private ArrayList<String> UserProfilePhotoImageList;

    public SearchRecyclerAdapter(ArrayList<String> userProfileEmailList, ArrayList<String> userProfileBioList, ArrayList<String> userProfilePhotoImageList) {
        UserProfileEmailList = userProfileEmailList;
        UserProfileBioList = userProfileBioList;
        UserProfilePhotoImageList = userProfilePhotoImageList;
    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recyler_search,parent,false);

        return new SearchRecyclerAdapter.PostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder,int position) {
        holder.searchedUserEmailText.setText(UserProfileEmailList.get(position));
        holder.searchedUserBioText.setText(UserProfileBioList.get(position));
        Picasso.get().load(UserProfilePhotoImageList.get(position)).into(holder.profilePhoto);


    }

    @Override
    public int getItemCount() {
        return UserProfileEmailList.size();
    }

    public class PostHolder extends RecyclerView.ViewHolder {

        ImageView profilePhoto;
        TextView searchedUserEmailText;
        TextView searchedUserBioText;


        public PostHolder(@NonNull final View itemView) {
            super(itemView);

            profilePhoto = itemView.findViewById(R.id.searchedProfilePhoto);
            searchedUserEmailText = itemView.findViewById((R.id.searchProfileEmail));
            searchedUserBioText = itemView.findViewById(R.id.searchedProfileBio);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(),searchedProfileActivity.class);
                    intent.putExtra("searchedEmail",searchedUserEmailText.getText().toString());
                    view.getContext().startActivity(intent);

                }
            });

        }
    }
}
