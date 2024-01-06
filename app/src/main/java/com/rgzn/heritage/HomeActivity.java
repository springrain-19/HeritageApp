package com.rgzn.heritage;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private Carousel carousel;
    private RecyclerView recyclerViewHeritageItems;
    private HeritagedbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ViewPager2 viewPager2 = findViewById(R.id.viewPager2);
        LinearLayout dotLinearLayout = findViewById(R.id.index_dot);

        // 创建 Carousel 的实例
        carousel = new Carousel(this, dotLinearLayout, viewPager2);

        // 初始化轮播图
        carousel.initViews(new int[]{R.drawable.mappicture, R.drawable.newspicture});
        // 自动滚动
        carousel.startAutoScroll();
        recyclerViewHeritageItems = findViewById(R.id.recyclerViewHeritageItems);
        recyclerViewHeritageItems.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new HeritagedbHelper(this);

        recyclerViewHeritageItems = findViewById(R.id.recyclerViewHeritageItems);
        recyclerViewHeritageItems.setLayoutManager(new LinearLayoutManager(this));
        loadHeritageItemsFromDB();

        // 非遗列表
        List<HeritageItem> heritageItemList = new ArrayList<>();
        heritageItemList.add(new HeritageItem("京剧", "又称平剧、京戏等，中国国粹之一，是中国影响力最大的戏曲剧种，分布地以北京为中心，遍及全国各地。", R.drawable.art));
        heritageItemList.add(new HeritageItem("点茶", "点茶是唐宋时期的一种沏茶方法。点茶是分茶的基础，所以点茶法的起始不会晚于五代。点茶是古代沏茶方法之一。", R.drawable.skill));


        HeritageItemAdapter adapter = new HeritageItemAdapter(heritageItemList, item -> {
            Intent intent = new Intent(HomeActivity.this, HeritageDetailActivity.class);
            intent.putExtra("HeritageName", item.getTitle()); // 添加额外数据
            startActivity(intent);
        });
        recyclerViewHeritageItems.setAdapter(adapter);

        recyclerViewHeritageItems.setAdapter(adapter);

        recyclerViewHeritageItems.setAdapter(adapter);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setItemIconTintList(null); // 这会使图标显示其原始颜色

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
    private void loadHeritageItemsFromDB() {
        List<HeritageItem> heritageItemList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                HeritageContract.HeritageEntry.TABLE_NAME,
                null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String title = cursor.getString(cursor.getColumnIndexOrThrow(HeritageContract.HeritageEntry.COLUMN_NAME));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(HeritageContract.HeritageEntry.COLUMN_DESCRIPTION));
            int imageResId = getResources().getIdentifier(title.toLowerCase().replaceAll("\\s+",""), "drawable", getPackageName());

            heritageItemList.add(new HeritageItem(title, description, imageResId));
        }
        cursor.close();

        HeritageItemAdapter adapter = new HeritageItemAdapter(heritageItemList, item -> {
            Intent intent = new Intent(HomeActivity.this, HeritageDetailActivity.class);
            intent.putExtra("HeritageName", item.getTitle());
            startActivity(intent);
        });
        recyclerViewHeritageItems.setAdapter(adapter);
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


    @Override
    protected void onPause() {
        super.onPause();
        // 当活动暂停时停止轮播图滚动
        carousel.stopAutoScroll();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 当活动恢复时启动轮播图滚动
        carousel.startAutoScroll();
    }

}








