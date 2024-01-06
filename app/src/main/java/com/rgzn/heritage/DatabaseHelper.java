package com.rgzn.heritage;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "UserDatabase";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";

    private static final String COLUMN_SIGNATURE = "signature";
    private static final String COLUMN_AVATAR = "avatar";
    private static final String COLUMN_BIRTHDAY = "birthday";
    private static final String COLUMN_PREFERENCE = "preference";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
//创建数据库
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USERNAME + " TEXT,"
                + COLUMN_PASSWORD + " TEXT,"
                + COLUMN_SIGNATURE + " TEXT,"
                + COLUMN_AVATAR + " TEXT,"
                + COLUMN_BIRTHDAY + " TEXT,"
                + COLUMN_PREFERENCE + " TEXT" + ")";
        db.execSQL(CREATE_USERS_TABLE);
    }
    public static String getColumnAvatar() {
        return COLUMN_AVATAR;
    }
    public static String getCOLUMN_USERNAME() {
        return COLUMN_USERNAME;
    }
    public static String getTABLE_USERS() {
        return TABLE_USERS;
    }



//更新数据库版本
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }
//添加数据库
    public boolean addUser(String username, String password, String signature, String avatar, String birthday, String preference) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_SIGNATURE, signature);
        values.put(COLUMN_AVATAR, avatar);
        values.put(COLUMN_BIRTHDAY, birthday);
        values.put(COLUMN_PREFERENCE, preference);

        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result != -1;
    }
    //检查用户是否存在
    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = { COLUMN_ID };
        String selection = COLUMN_USERNAME + " = ? AND " + COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = { username, password };
        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        return cursorCount > 0;
    }
    public String getPassword(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = { COLUMN_PASSWORD };
        String selection = COLUMN_USERNAME + " = ?";
        String[] selectionArgs = { username };
        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            String password = cursor.getString(0);
            cursor.close();
            return password;
        }
        if (cursor != null) {
            cursor.close();
        }
        return null;
    }


}
