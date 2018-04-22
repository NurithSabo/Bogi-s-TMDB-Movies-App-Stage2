package com.example.android.bogipopmoviesstage2;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Bogi on 2018. 04. 11..
 */

class Trailer {

    private String key;
    private String name;

    private String getKey() {
        return key;
    }

    public String getUrl() {
        return "https://www.youtube.com/watch?v=" + this.getKey();
    }

    public String getName() {
        return name;
    }

    public String getDefaultImage(){
        return "http://img.youtube.com/vi/"+this.getKey()+"/default.jpg";
    }

    //Constructor
    public Trailer(JSONObject trailer) throws JSONException {
        this.key = trailer.getString("key");
        this.name = trailer.getString("name");
    }
}