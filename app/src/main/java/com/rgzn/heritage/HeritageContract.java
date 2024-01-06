package com.rgzn.heritage;

import android.provider.BaseColumns;

public final class HeritageContract {
    private HeritageContract() {
    }

    public static class HeritageEntry implements BaseColumns {
        public static final String TABLE_NAME = "heritage";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_MEDIA_PATH = "media_path";
        public static final String COLUMN_LIKES = "likes";
        public static final String COLUMN_FAVORITES = "favorites";
        public static final String COLUMN_IMAGE_RES_NAME = "image_res_name";
    }



}
