package com.example.android.bogipopmoviesstage2;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Bogi on 2018. 04. 06..
 */

class Review {
    private String author;
    private String content;

    public Review (JSONObject review) throws JSONException {
        author = review.getString("author");
        content = review.getString("content");
    }

    public String getAuthor()  { return author; }
    public String getContent() { return content; }
}
