package edu.ubi.selanjutnya.sharepic;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by Rifqi on 12/21/2015.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(this,
                getResources().getString(R.string.parse_application_id),
                getResources().getString(R.string.parse_client_key));
    }
}
