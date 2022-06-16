package com.example.instagram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.instagram.fragments.ComposeFragment;
import com.example.instagram.fragments.PostsFragment;
import com.example.instagram.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import androidx.appcompat.widget.Toolbar;


public class FeedActivity extends AppCompatActivity {
    //RecyclerView rvPosts;
    private SwipeRefreshLayout swipeContainer;
    protected PostsAdapter adapter;
    protected List<Post> allPosts;
    private Toolbar toolBar;  // here
    private BottomNavigationView bottomNavigationView;
    private String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //rvPosts = findViewById(R.id.rvPosts);
        //allPosts = new ArrayList<>();
        //adapter = new PostsAdapter(this, allPosts);
        ///toolBar = findViewById(R.id.toolbar);

        //setSupportActionBar(toolBar);




        //rvPosts.setAdapter(adapter);
        // set the layout manager on the recycler view
        //rvPosts.setLayoutManager(new LinearLayoutManager(this));
        //swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        //swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            ///@Override
            //public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                //queryPosts();
              //  swipeContainer.setRefreshing(false);
          //  }
       // });
        // Configure the refreshing colors
       // swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
               // android.R.color.holo_green_light,
               // android.R.color.holo_orange_light,
               // android.R.color.holo_red_light);
        // query posts from Parstagra
        //queryPosts();

        //bottomNavigationView = findViewById(R.id.bottom_navigation);


/*
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_post:
                        Intent i = new Intent(FeedActivity.this, MainActivity.class);
                        startActivity(i);
                        return true;
                    case R.id.action_home:
                        Intent in = new Intent(FeedActivity.this, FeedActivity.class);
                        startActivity(in);
                        // do something here
                        return true;
                    case R.id.action_prof:
                        // do something here
                        Intent inte = new Intent(FeedActivity.this, ProfileActivity.class);
                        startActivity(inte);
                        return true;
                    default: return true;
                }
            }
        });*/
        final FragmentManager fragmentManager = getSupportFragmentManager();

        bottomNavigationView = findViewById(R.id.bottom_navigation);


        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = new Fragment();
                switch (item.getItemId()) {
                    case R.id.action_post:
                        //Intent i = new Intent(MainActivity.this, MainActivity.class);
                        //startActivity(i);
                        fragment = new ComposeFragment();
                        //return true;
                        break;
                    case R.id.action_home:
                        //Intent in = new Intent(MainActivity.this, FeedActivity.class);
                        //startActivity(in);
                        fragment = new PostsFragment();
                        // do something here
                        //return true;
                        break;
                    case R.id.action_prof:
                        // do something here
                        //Intent inte = new Intent(MainActivity.this, ProfileActivity.class);
                        fragment = new ProfileFragment();
                        //startActivity(inte);
                        break;
                    default: break;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.action_home);

    }

    private void queryPosts() {
        // specify what type of data we want to query - Post.class
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        // include data referred by user key
        query.include(Post.KEY_USER);
        // limit query to latest 20 items
        query.setLimit(20);
        // order posts by creation date (newest first)
        query.addDescendingOrder("createdAt");
        // start an asynchronous call for posts
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                // check for errors
                if (e != null) {
                    Log.e("FEED", "Issue with getting posts", e);
                    return;
                }

                // for debugging purposes let's print every post description to logcat
                for (Post post : posts) {
                    Log.i("FEED", "Post: " + post.getDescription() + ", username: " + post.getUser().getUsername());
                }

                // save received posts to list and notify adapter of new data
                allPosts.addAll(posts);
                adapter.notifyDataSetChanged();
            }
        });
    }



}