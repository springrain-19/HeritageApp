package com.rgzn.heritage;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private List<String> posts;

    public PostAdapter(List<String> posts) {
        this.posts = posts;
    }


    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TextView textView = new TextView(parent.getContext());
        textView.setPadding(16, 16, 16, 16);
        return new PostViewHolder(textView);
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        holder.textView.setText(posts.get(position));
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        PostViewHolder(TextView itemView) {
            super(itemView);
            textView = itemView;
        }
    }
}



    public class ViewPostActivity extends AppCompatActivity {
        private ContentdbHelper dbHelper;
        private RecyclerView recyclerView;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.view_posts_layout);

            recyclerView = findViewById(R.id.recycler_posts);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            dbHelper = new ContentdbHelper(this);
            loadPosts();
            BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
            bottomNavigationView.setItemIconTintList(null);

            // 设置导航项选中事件监听器
            bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        navigateToScreen(HomeActivity.class);
                        return true;
                    case R.id.navigation_community:

                        navigateToScreen(ViewPostActivity.class);
                        return true;
                    case R.id.navigation_add:

                        navigateToScreen(AddPostActivity.class);
                        return true;
                    case R.id.navigation_messages:

                        return true;
                    case R.id.navigation_mine:
                        navigateToScreen(MineActivity.class);
                        return true;
                }
                return false;
            });
        }

        public void navigateToScreen(Class<?> screenClass) {
            Intent intent = getIntent();
            String username = intent.getStringExtra("USERNAME");
            String realPassword = intent.getStringExtra("PASSWORD");
            Intent intent2 = new Intent(this, screenClass);
            intent2.putExtra("USERNAME", username);
            intent2.putExtra("PASSWORD", realPassword);
            startActivity(intent2);
        }
        private void loadPosts() {
            Cursor cursor = dbHelper.getReadableDatabase().query(
                    ContentdbHelper.TABLE_NAME,
                    null, null, null, null, null,
                    ContentdbHelper.COLUMN_TIMESTAMP + " DESC"
            );

            List<String> posts = new ArrayList<>();
            while (cursor.moveToNext()) {
                String title = cursor.getString(cursor.getColumnIndexOrThrow(ContentdbHelper.COLUMN_TITLE));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(ContentdbHelper.COLUMN_DESCRIPTION));
                String timestamp = cursor.getString(cursor.getColumnIndexOrThrow(ContentdbHelper.COLUMN_TIMESTAMP));
                posts.add(title + "\n" + description + "\nPosted on: " + timestamp + "\n\n");
            }
            cursor.close();

            PostAdapter adapter = new PostAdapter(posts);
            recyclerView.setAdapter(adapter);
        }
    }

