package edu.ubi.selanjutnya.sharepic;

import android.content.Context;
import android.content.Intent;

import com.parse.ParsePushBroadcastReceiver;

/**
 * Created by Rifqi on 12/23/2015.
 */
public class ParsePushCustomReceiver extends ParsePushBroadcastReceiver{

    @Override
    protected void onPushOpen(Context context, Intent intent) {
        super.onPushOpen(context, intent);

        Intent newIntent = new Intent(context, ListViewLoader.class);
        newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(newIntent);
    }
}
