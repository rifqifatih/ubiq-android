package edu.ubi.selanjutnya.sharepic;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.ToggleButton;

import com.parse.Parse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "CameraFragment";
    private static final String POST_URL = "http://128.199.156.210:3000/";

    private static final int REQUEST_CODE = 1;
    private final AppCompatActivity thisActivity = this;

    private EditText nameText;
    private EditText passText;
    private LocationManager locationManager;
    private String mName;
    private String mSecret;
    private static String mDeviceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDeviceId = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        nameText = (EditText) findViewById(R.id.editText);
        passText  = (EditText) findViewById(R.id.editText2);

        Switch aSwitch = (Switch) findViewById(R.id.switch1);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mName = nameText.getText().toString();
                    mSecret = passText.getText().toString();
                    new RegisterTask().execute();
                }
                else {
                    // TODO on OFF switch
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // No Op
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private class RegisterTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                URL url = new URL(POST_URL);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("User-Agent", System.getProperty("http.agent"));
                httpURLConnection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
                httpURLConnection.setRequestProperty("Content-Type", "application/json");

                if (ActivityCompat.checkSelfPermission(thisActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(thisActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(thisActivity, Manifest.permission.ACCESS_FINE_LOCATION) ||
                            ActivityCompat.shouldShowRequestPermissionRationale(thisActivity, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                        // No Op?
                    }
                    else {
                        ActivityCompat.requestPermissions(thisActivity,
                                new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                                REQUEST_CODE);
                    }
                }

                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                Double latitide = location.getLatitude();
                Double longitude = location.getLongitude();

                JSONObject jsonParam = new JSONObject();
                jsonParam.put("nama", mName);
                jsonParam.put("secret", mSecret);
                jsonParam.put("lon", latitide);
                jsonParam.put("lat", longitude);
                jsonParam.put("deviceId", mDeviceId);

                httpURLConnection.setDoOutput(true);
                DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
                dataOutputStream.writeBytes(jsonParam.toString());
                dataOutputStream.flush();
                dataOutputStream.close();

                int responseCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "Sending 'POST' request to URL : " + url.toString());
                Log.d(TAG, "Post parameters : " + jsonParam.toString());
                Log.d(TAG, "Response Code : " + responseCode);

                // TODO any response?
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(httpURLConnection.getInputStream()));

                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = bufferedReader.readLine()) != null) {
                    response.append(inputLine);
                }
                bufferedReader.close();

                Log.d(TAG, "Response : " + response.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public void takePhoto(View view) {
        Intent intent = new Intent(this, CameraActivity.class);
        // TODO password & name
        startActivity(intent);

    }

    public void openGrid(View view) {
        Intent intent = new Intent(this, GridActivity.class);
        startActivity(intent);
    }
}
