package com.example.stylishsloth.helper_classes;

public class StoriesItem {
    String text;
    int Image;

    public StoriesItem(String text, int image) {
        this.text = text;
        Image = image;
    }

    public StoriesItem(int image) {
        Image = image;
    }

    public int getImage() {
        return Image;
    }

    public void setImage(int image) {
        Image = image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public StoriesItem(String text) {
        this.text = text;
    }

    public StoriesItem() {
    }
}
