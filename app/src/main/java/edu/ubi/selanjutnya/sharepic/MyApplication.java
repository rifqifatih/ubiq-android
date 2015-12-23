package edu.ubi.selanjutnya.sharepic;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseInstallation;

/**
 * Created by Rifqi on 12/21/2015.
 */
public class MyApplication extends Application {

    public static String DEVICE_TOKEN;

    @Override
    public void onCreate() {
        super.onCreate();

        DEVICE_TOKEN = (String) ParseInstallation.getCurrentInstallation().get("deviceToken");

        Parse.initialize(this,
                getResources().getString(R.string.parse_application_id),
                getResources().getString(R.string.parse_client_key));
    }
}
