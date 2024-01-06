package com.rgzn.heritage;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MineActivity  extends AppCompatActivity {

    private ImageView ivAvatar;
    private TextView tvName;
    private TextView tvPassword;
    private View btnLogout;
    private View btnSystemIntro;
    private boolean isPasswordVisible = false;
    private String username;
    private String realPassword;
    private RecyclerView recyclerViewHeritageItems;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragement_me);

        ivAvatar = findViewById(R.id.iv_avatar);
        btnLogout = findViewById(R.id.btn_logout);
        btnSystemIntro = findViewById(R.id.btn_system_intro);
        tvName = findViewById(R.id.tv_name);
        tvPassword = findViewById(R.id.tv_password);
        // 设置密码可见性切换监听器
        databaseHelper = new DatabaseHelper(this);
        Intent intent = getIntent();
        username = intent.getStringExtra("USERNAME");
        realPassword = intent.getStringExtra("PASSWORD");
        if (username != null) {
            SQLiteDatabase db = databaseHelper.getReadableDatabase();
            try {
                String[] columns = { DatabaseHelper.getColumnAvatar() };
                String selection = DatabaseHelper.getCOLUMN_USERNAME() + " = ?";
                String[] selectionArgs = { username };
                Cursor cursor = db.query(DatabaseHelper.getTABLE_USERS(), columns, selection, selectionArgs, null, null, null);

                if (cursor != null && cursor.moveToFirst()) {
                    String avatarPath = cursor.getString(0);
                    ivAvatar.setImageURI(android.net.Uri.parse(avatarPath));
                }
                if (cursor != null) {
                    cursor.close();
                }
            } finally {
                db.close();
            }
        }

        // 显示用户名和隐藏的密码
        tvName.setText(username);

        tvPassword.setText(getString(R.string.password_hide_text));
            // 设置点击监听器以切换密码的可见性
        tvPassword.setOnClickListener(v -> togglePasswordVisibility());

        // 注销按钮点击事件
        btnLogout.setOnClickListener(v -> {
            // 返回到登录界面
            Intent intenth = new Intent(MineActivity.this, MainActivity.class);
            startActivity(intenth);
            finish();
        });


        recyclerViewHeritageItems = findViewById(R.id.recyclerViewHeritageItems);
        recyclerViewHeritageItems.setLayoutManager(new LinearLayoutManager(this));
        List<HeritageItem> heritageItemList = new ArrayList<>();
        heritageItemList.add(new HeritageItem("京剧", "又称平剧、京戏等，中国国粹之一，是中国影响力最大的戏曲剧种，分布地以北京为中心，遍及全国各地。", R.drawable.art));
        heritageItemList.add(new HeritageItem("点茶", "点茶是唐宋时期的一种沏茶方法。点茶是分茶的基础，所以点茶法的起始不会晚于五代。点茶是古代沏茶方法之一。", R.drawable.skill));
        HeritageItemAdapter adapter = new HeritageItemAdapter(heritageItemList, item -> {
            Intent intent2 = new Intent(MineActivity.this, HeritageDetailActivity.class);
            intent2.putExtra("HeritageName", item.getTitle());
            startActivity(intent2);
        });
        recyclerViewHeritageItems.setAdapter(adapter);
        recyclerViewHeritageItems.setAdapter(adapter);
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



    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            // 显示隐藏的密码
            tvPassword.setText(R.string.password_hide_text);
            tvPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.eye_off, 0);
            isPasswordVisible = false;
        } else {
            // 显示真实密码
            tvPassword.setText(realPassword);
            tvPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.eye_on, 0);
            isPasswordVisible = true;
        }
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


}


