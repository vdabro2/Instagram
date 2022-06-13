package com.example.instagram;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ParseObject.registerSubclass(Post.class);
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("dszd14eVQBIEIrMqRKyDwShjKaLMUhrP9x61k5Nn") // should correspond to Application Id env variable
                .clientKey("eraiFOFKswQtgL51wipYveurMqtR3sgA6qGeK68l")  // should correspond to Client key env variable
                .server("https://parseapi.back4app.com").build());
    }
}
