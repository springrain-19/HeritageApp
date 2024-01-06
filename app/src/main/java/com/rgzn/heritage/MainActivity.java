package com.rgzn.heritage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;
import com.google.android.material.snackbar.Snackbar;
import com.rgzn.heritage.databinding.ActivityMainBinding;
import com.rgzn.heritage.databinding.ContentMainBinding;

public class MainActivity extends AppCompatActivity {
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    private DatabaseHelper databaseHelper;

    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        databaseHelper = new DatabaseHelper(this);

        ContentMainBinding binding = ContentMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // 检查 SharedPreferences 是否有保存的用户名和密码
        String savedUsername = sharedPreferences.getString("username", "");
        String savedPassword = sharedPreferences.getString("password", "");
        boolean isRemembered = sharedPreferences.getBoolean("rememberMe", false);

        if (isRemembered) {
            binding.username.setText(savedUsername);
            binding.password.setText(savedPassword);
            binding.checkboxRememberMe.setChecked(true);
        }

        binding.loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = binding.username.getText().toString();
                String password = binding.password.getText().toString();
                if (databaseHelper.checkUser(username, password)) {
                    // 登录成功，跳转到 HomeActivity 并传递用户名和密码
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    intent.putExtra("USERNAME", username);
                    intent.putExtra("PASSWORD", password);
                    startActivity(intent);
                } else {
                    // 登录失败
                    Snackbar.make(view, "Login failed", Snackbar.LENGTH_LONG).show();
                }
                // 保存用户名和密码
                if (binding.checkboxRememberMe.isChecked()) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("username", username);
                    editor.putString("password", password);
                    editor.putBoolean("rememberMe", true);
                    editor.apply();
                } else {
                    sharedPreferences.edit().clear().apply();
                }
            }
        });  
        //注册
        binding.registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 启动注册界面
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        binding.btnabout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "作者:springrain", Snackbar.LENGTH_LONG).show();
            }
        });


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}