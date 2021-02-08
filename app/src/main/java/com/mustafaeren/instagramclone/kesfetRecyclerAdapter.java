package com.mustafaeren.instagramclone;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class kesfetRecyclerAdapter extends RecyclerView.Adapter<kesfetRecyclerAdapter.PostHolder> {

    private ArrayList<String> userEmailList;
    private ArrayList<String> userCommentList;
    private ArrayList<String> userImageList;

    public kesfetRecyclerAdapter(ArrayList<String> userEmailList, ArrayList<String> userCommentList, ArrayList<String> userImageList) {
        this.userEmailList = userEmailList;
        this.userCommentList = userCommentList;
        this.userImageList = userImageList;
    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycler_row,parent,false);

        return new kesfetRecyclerAdapter.PostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int position) {
        holder.userEmailText.setText(userEmailList.get(position));
        holder.userCommentText.setText(userCommentList.get(position));

        Picasso.get().load(userImageList.get(position))
                .into(holder.photoView);
    }

    @Override
    public int getItemCount() {
        return userEmailList.size();
    }

    public class PostHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView userEmailText;
        TextView userCommentText;
        PhotoView photoView;

        public PostHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.recyclerView_row_imageView);
            userEmailText = itemView.findViewById((R.id.recyclerView_row_userEmail_text));
            userCommentText = itemView.findViewById(R.id.recyclerView_row_comment_text);
            photoView = itemView.findViewById(R.id.recyclerView_row_photo_view);

        }
    }
}
