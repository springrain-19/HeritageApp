package com.rgzn.heritage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AddPostActivity extends AppCompatActivity {
    private EditText editTitle, editDescription;
    private TextView editusername;
    private Button addButton;
    private ContentdbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_post_layout);
        editusername=findViewById(R.id.tv_name);
        Intent intent = getIntent();
        String editusername1 = intent.getStringExtra("USERNAME");
        editusername.setText(editusername1);
        editTitle = findViewById(R.id.edit_add_title);
        editDescription = findViewById(R.id.edit_add_desc);
        addButton = findViewById(R.id.add_diary_confirm);

        dbHelper = new ContentdbHelper(this);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = editTitle.getText().toString().trim();
                String description = editDescription.getText().toString().trim();

                if (title.isEmpty() || description.isEmpty()) {
                    Toast.makeText(AddPostActivity.this, "Please fill in both title and description", Toast.LENGTH_SHORT).show();
                    return;
                }

                dbHelper.addPost(title, description);
                Toast.makeText(AddPostActivity.this, "Post added successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setItemIconTintList(null);
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
    }}