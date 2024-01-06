package com.rgzn.heritage;

public class HeritageItem {
    private final String title;
    private final String description;
    private final int imageResId;

    public HeritageItem(String title, String description, int imageResId) {
        this.title = title;
        this.description = description;
        this.imageResId = imageResId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getImageResId() {
        return imageResId;
    }
}

