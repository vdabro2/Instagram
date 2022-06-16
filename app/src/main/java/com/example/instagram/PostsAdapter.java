package com.example.instagram;

import android.content.Context;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {
    private Context context;
    private List<Post> posts;

    public PostsAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        try {
            holder.bind(post);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    // Clean all elements of the recycler
    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Post> list) {
        posts.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvUsername;
        private TextView tvLikes;
        private ImageView ivImage;
        private TextView tvDescription;
        private TextView tvTime;
        private ImageView ivProfilePicture;
        private TextView tvUserInDes;
        private ImageView ivLikes;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvUserInDes = itemView.findViewById(R.id.tvUserInDes);
            ivProfilePicture = itemView.findViewById(R.id.ivProfilePicture);
            tvLikes = itemView.findViewById(R.id.tvLikes);
            ivLikes = itemView.findViewById(R.id.ivLikes);
        }

        public void bind(Post post) throws JSONException {
            // Bind the post data to the view elements
            tvDescription.setText(post.getDescription());
            tvUsername.setText(post.getUser().getUsername());
            ParseFile image = post.getParseFile();
            if (image != null) {
                Glide.with(context).load(image.getUrl()).into(ivImage);
            }
            tvTime.setText(Post.calculateTimeAgo(post.getCreatedAt()));

            ParseFile profilepic = post.getUser().getParseFile("profilePicture");
            if (profilepic != null) {
                Glide.with(context).load(profilepic.getUrl()).circleCrop().into(ivProfilePicture);
            }
            tvUserInDes.setText(post.getUser().getUsername());

            liking(post);


        }

        private void liking(Post post) throws JSONException {

            List<String> list = ParseUser.getCurrentUser().getList("likedPosts");
            String add = "";
            if (post.getNumber("likes").intValue() != 1) {
                add = " likes";
            } else {
                add = " like";
            }
            tvLikes.setText(String.valueOf(post.getNumber("likes").intValue()) + add);
            boolean alreadyLiked = false;
            if (list.contains(post.getObjectId())) {
                alreadyLiked = true;
            }
            if (alreadyLiked) ivLikes.setImageResource(R.drawable.img_3);
            ivLikes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean alreadyLiked = false;
                    if (list.contains(post.getObjectId())) {
                        alreadyLiked = true;
                    }
                    if (alreadyLiked) {
                        ivLikes.setImageResource(R.drawable.ufi_heart);
                        list.remove(post.getObjectId());
                        ParseUser.getCurrentUser().put("likedPosts", list);
                        post.put("likes", post.getNumber("likes").intValue() - 1);
                    } else {
                        ivLikes.setImageResource(R.drawable.img_2);
                        list.add(post.getObjectId());
                        ParseUser.getCurrentUser().put("likedPosts", list);
                        post.put("likes", post.getNumber("likes").intValue() + 1);
                    }

                    post.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {

                        }
                    });
                    ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {

                        }
                    });
                    tvLikes.setText(String.valueOf(post.getNumber("likes").intValue()) + " likes");
                }
            });
        }


    }
}