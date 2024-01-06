package com.rgzn.heritage;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class HeritageDetailActivity extends AppCompatActivity {
    private VideoView videoView;
    private ImageButton buttonLike;
    private ImageButton buttonFavorite;
    private Button buttonBack;
    private TextView textViewTitle, textViewDescription;
    private ImageView imageViewHeritage;
    private boolean isLiked = false;
    private boolean isFavorited = false;
    private MediaController mMediaController;
    private TextView textViewLikes;
    private TextView textViewFavorites;
    private String heritageName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_heritage_detail);

        // UI 组件初始化
        videoView = findViewById(R.id.videoView);
        buttonLike = findViewById(R.id.button_like);
        buttonFavorite = findViewById(R.id.button_favorite);
        buttonBack = findViewById(R.id.buttonBack);
        textViewTitle = findViewById(R.id.textViewTitle);
        textViewDescription = findViewById(R.id.textViewHeritageDescription);
        imageViewHeritage = findViewById(R.id.imageViewHeritage);
        textViewLikes = findViewById(R.id.textViewLikes);
        textViewFavorites = findViewById(R.id.textViewFavorites);

        Intent intent = getIntent();
        heritageName = intent.getStringExtra("HeritageName");
        if (heritageName != null && !heritageName.isEmpty()) {
            loadHeritageData(heritageName);
        } else {
            Toast.makeText(this, "没有提供非遗信息", Toast.LENGTH_SHORT).show();
        }

        setupListeners();
    }
    private void loadHeritageData(String heritageName) {
        HeritagedbHelper dbHelper = new HeritagedbHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        this.heritageName = heritageName;
        Log.d("HeritageDetail", "Loading data for: " + heritageName);

        String[] projection = {
                HeritageContract.HeritageEntry.COLUMN_NAME,
                HeritageContract.HeritageEntry.COLUMN_DESCRIPTION,
                HeritageContract.HeritageEntry.COLUMN_MEDIA_PATH,
                HeritageContract.HeritageEntry.COLUMN_LIKES,
                HeritageContract.HeritageEntry.COLUMN_FAVORITES,
                HeritageContract.HeritageEntry.COLUMN_IMAGE_RES_NAME,
        };

        String selection = HeritageContract.HeritageEntry.COLUMN_NAME + " = ?";
        String[] selectionArgs = { heritageName };

        Cursor cursor = db.query(
                HeritageContract.HeritageEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null, null, null
        );

        Log.d("HeritageDetail", "Querying database for: " + heritageName);
        if (cursor.moveToFirst()) {
            Log.d("HeritageDetail", "Data found for: " + heritageName);
            String name = cursor.getString(cursor.getColumnIndexOrThrow(HeritageContract.HeritageEntry.COLUMN_NAME));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(HeritageContract.HeritageEntry.COLUMN_DESCRIPTION));
            String mediaPath = cursor.getString(cursor.getColumnIndexOrThrow(HeritageContract.HeritageEntry.COLUMN_MEDIA_PATH));
            int likes = cursor.getInt(cursor.getColumnIndexOrThrow(HeritageContract.HeritageEntry.COLUMN_LIKES));
            int favorites = cursor.getInt(cursor.getColumnIndexOrThrow(HeritageContract.HeritageEntry.COLUMN_FAVORITES));
            String imageResName = cursor.getString(cursor.getColumnIndexOrThrow(HeritageContract.HeritageEntry.COLUMN_IMAGE_RES_NAME));
            int imageResId = getResources().getIdentifier(imageResName, "drawable", getPackageName());
//            int imageResId = getResources().getIdentifier(name.toLowerCase().replaceAll("\\s+", ""), "drawable", getPackageName());
            if (imageResId != 0) {
                imageViewHeritage.setImageResource(imageResId);
            }

            // 更新 UI 组件
            textViewTitle.setText(name);
            textViewDescription.setText(description);
            textViewLikes.setText(String.valueOf(likes));
            textViewFavorites.setText(String.valueOf(favorites));



            String videoResourceName = Uri.parse(mediaPath).getLastPathSegment().split("\\.")[0];
            int videoResId = getResources().getIdentifier(videoResourceName, "raw", getPackageName());
            if (videoResId != 0) {
                Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + videoResId);
                videoView.setVideoURI(videoUri);
            }
        } else {
            Toast.makeText(this, "未找到相关信息", Toast.LENGTH_SHORT).show();
        }

        cursor.close();
    }
    protected void onPause() {
        super.onPause();
        updateHeritageInDatabase();
    }
    private void updateHeritageInDatabase() {
        HeritagedbHelper dbHelper = new HeritagedbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int likes = Integer.parseInt(textViewLikes.getText().toString());
        int favorites = Integer.parseInt(textViewFavorites.getText().toString());
        updateHeritageLikesAndFavorites(db, heritageName, likes, favorites);
        db.close();
    }

    public void updateHeritageLikesAndFavorites(SQLiteDatabase db, String name, int likes, int favorites) {
        ContentValues values = new ContentValues();
        values.put(HeritageContract.HeritageEntry.COLUMN_LIKES, likes);
        values.put(HeritageContract.HeritageEntry.COLUMN_FAVORITES, favorites);
        db.update(HeritageContract.HeritageEntry.TABLE_NAME, values, HeritageContract.HeritageEntry.COLUMN_NAME + "=?", new String[]{name});
    }
    private void setupListeners() {
        mMediaController = new MediaController(this);
        videoView.setMediaController(mMediaController);

        buttonLike.setOnClickListener(v -> {
            isLiked = !isLiked;
            int currentLikes = Integer.parseInt(textViewLikes.getText().toString());
            if (isLiked) {
                textViewLikes.setText(String.valueOf(currentLikes + 1));
                buttonLike.setImageResource(R.drawable.ic_liked);
            } else {
                textViewLikes.setText(String.valueOf(currentLikes - 1));
                buttonLike.setImageResource(R.drawable.ic_like);
            }
        });

        buttonFavorite.setOnClickListener(v -> {
            isFavorited = !isFavorited;
            int currentFavorites = Integer.parseInt(textViewFavorites.getText().toString());
            if (isFavorited) {
                textViewFavorites.setText(String.valueOf(currentFavorites + 1));
                buttonFavorite.setImageResource(R.drawable.ic_favorited);
            } else {
                textViewFavorites.setText(String.valueOf(currentFavorites - 1));
                buttonFavorite.setImageResource(R.drawable.ic_favorite);
            }
        });

        buttonBack.setOnClickListener(v -> finish());
    }}