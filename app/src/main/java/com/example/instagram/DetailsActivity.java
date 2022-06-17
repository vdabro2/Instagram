package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends AppCompatActivity {
    public List<Post> finalpost;

    private TextView tvUsername;
    private TextView tvLikes;
    private ImageView ivImage;
    private TextView tvDescription;
    private TextView tvTime;
    private ImageView ivProfilePicture;
    private TextView tvUserInDes;
    private ImageView ivLikes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);


        tvUsername = findViewById(R.id.tvUsername);
        ivImage = findViewById(R.id.ivImage);
        tvDescription = findViewById(R.id.tvDescription);
        tvTime = findViewById(R.id.tvTime);
        tvUserInDes = findViewById(R.id.tvUserInDes);
        ivProfilePicture = findViewById(R.id.ivProfilePicture);
        tvLikes = findViewById(R.id.tvLikes);
        ivLikes = findViewById(R.id.ivLikes);
        finalpost = new ArrayList<>();
        Intent intent = getIntent();
        Post post = (Post) intent.getSerializableExtra("post");
        //getPost(postId);

        tvUsername.setText(post.getUser().getUsername());

        tvDescription.setText(post.getDescription());
        ParseFile image = post.getParseFile();
        if (image != null) {
            Glide.with(DetailsActivity.this).load(image.getUrl()).into(ivImage);
        }
        tvTime.setText(Post.calculateTimeAgo(post.getCreatedAt()));

        ParseFile profilepic = post.getUser().getParseFile("profilePicture");
        if (profilepic != null) {
            Glide.with(DetailsActivity.this).load(profilepic.getUrl()).circleCrop().into(ivProfilePicture);
        }
        tvUserInDes.setText(post.getUser().getUsername());

       liking(post);

    }


    private void liking(Post post) {
        List<String> list = ParseUser.getCurrentUser().getList("likedPosts");
        ArrayList<String> list1 = new ArrayList<String>(list);
        String add = "";
        if (post.getNumber("likes").intValue() != 1) {
            add = " likes";
        } else {
            add = " like";
        }
        tvLikes.setText(String.valueOf(post.getNumber("likes").intValue()) + add);
        boolean alreadyLiked = false;
        if (list1.contains(post.getObjectId())) {
            alreadyLiked = true;
        }
        if (alreadyLiked) ivLikes.setImageResource(R.drawable.img_3);
        ivLikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean alreadyLiked = false;
                if (list1.contains(post.getObjectId())) {
                    alreadyLiked = true;
                }
                if (alreadyLiked) {
                    ivLikes.setImageResource(R.drawable.ufi_heart);
                    list1.remove(post.getObjectId());
                    ParseUser.getCurrentUser().put("likedPosts", list1);
                    post.put("likes", post.getNumber("likes").intValue() - 1);
                } else {
                    ivLikes.setImageResource(R.drawable.img_2);
                    list1.add(post.getObjectId());
                    ParseUser.getCurrentUser().put("likedPosts", list1);
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