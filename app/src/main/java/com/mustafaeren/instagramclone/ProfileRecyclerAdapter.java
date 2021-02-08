package com.mustafaeren.instagramclone;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProfileRecyclerAdapter extends RecyclerView.Adapter<ProfileRecyclerAdapter.PostHolder> {

    private ArrayList<String> currentUserEmailList;
    private ArrayList<String> currentUserCommentList;
    private ArrayList<String> currentUserImageList;

    public ProfileRecyclerAdapter(ArrayList<String> currentUserEmailList, ArrayList<String> currentUserCommentList, ArrayList<String> currentUserImageList) {
        this.currentUserEmailList = currentUserEmailList;
        this.currentUserCommentList = currentUserCommentList;
        this.currentUserImageList = currentUserImageList;
    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycler_row2,parent,false);

        return new ProfileRecyclerAdapter.PostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int position) {
        holder.currentUserEmailText.setText(currentUserEmailList.get(position));
        holder.currentUserCommentText.setText(currentUserCommentList.get(position));
        Picasso.get().load(currentUserImageList.get(position)).into(holder.imageView);


    }

    @Override
    public int getItemCount() {
        return currentUserImageList.size();
    }

    class PostHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView currentUserEmailText;
        TextView currentUserCommentText;


        public PostHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.recyclerView_row_imageView);
            currentUserEmailText = itemView.findViewById((R.id.recyclerView_row_userEmail_text));
            currentUserCommentText = itemView.findViewById(R.id.recyclerView_row_comment_text);



        }
    }
}
