package com.example.instagram;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Post")
public class Post extends ParseObject {
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_USER = "user";

    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }
    public void setDescription(String de) {
         put(KEY_DESCRIPTION, de);
    }

    public ParseFile getParseFile() {
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile de) {
        put(KEY_IMAGE, de);
    }

    public ParseUser getUser() {
        return getParseUser(KEY_IMAGE);
    }

    public void setUser(ParseUser de) {
        put(KEY_USER, de);
    }
}
