package edu.ubi.selanjutnya.sharepic;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.ParseInstallation;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rifqi on 12/23/2015.
 */
public class ListViewLoader extends ListActivity {

    private List<String> urls;
    private ArrayAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        urls = new ArrayList<String>();

        listAdapter = new ArrayAdapter<String>(this, R.layout.simple_list_item, urls);
        setListAdapter(listAdapter);

        new RetrieveTask().execute(MainActivity.mDeviceId);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        String url = "http://" + urls.get(position);

        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    private class RetrieveTask extends AsyncTask<String, Void, String[]> {
        @Override
        protected void onPostExecute(String[] strings) {
            super.onPostExecute(strings);
            listAdapter.notifyDataSetChanged();
        }

        @Override
        protected String[] doInBackground(String... params) {
            String[] result = null;
            Log.d("ASD", params[0]);

            try {
                URL url = new URL(getResources().getString(R.string.service_base_url) + "/user/download/" + params[0]);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");

                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(httpURLConnection.getInputStream()));

                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = bufferedReader.readLine()) != null) {
                    response.append(inputLine);
                }
                bufferedReader.close();

                Log.d("ASD", "Response: " + response.toString());

                JSONArray jsonArray = new JSONArray(response.toString());
                result = new String[jsonArray.length()];
                for (int i = 0; i < jsonArray.length(); i++) {
                    result[i] = jsonArray.getJSONObject(i).getString("path");
                    urls.add(jsonArray.getJSONObject(i).getString("path"));
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return result;
        }
    }
}
