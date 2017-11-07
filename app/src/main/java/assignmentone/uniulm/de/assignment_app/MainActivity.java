package assignmentone.uniulm.de.assignment_app;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.net.Uri;
import android.content.Intent;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, SensorEventListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mAcceleration = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        brighnessValueTextView = findViewById(R.id.brightnessValueText);
        accelerationValueTextView = findViewById(R.id.accelerometerValueText);
        proximitysValueTextView = findViewById(R.id.proximityValueText);
        magnetometerValueTextView = findViewById(R.id.magnetometerValueText);
        gyroscopeValueTextView = findViewById(R.id.gyoscopeValueText);

    }

    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private SensorManager mSensorManager;
    private Sensor mLight;
    private Sensor mAcceleration;
    private Sensor mMagnetometer;
    private Sensor mProximity;
    private Sensor mGyroscope;
    private TextView brighnessValueTextView;
    private TextView accelerationValueTextView;
    private TextView magnetometerValueTextView;
    private TextView gyroscopeValueTextView;
    private TextView proximitysValueTextView;

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mLight, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mAcceleration, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mGyroscope, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mProximity, SensorManager.SENSOR_STATUS_ACCURACY_HIGH);
        mSensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    public void OpenCurrentLocationInGoogleMaps(android.view.View view) {

        getLastPosition();

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Uri gmmIntentUri = Uri.parse("geo:" + mLastLocation.getLatitude() + "," + mLastLocation.getLongitude());
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            if (mapIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(mapIntent);
            }
        }
    }

    public void OpenAirportLocationInGoogleMaps(android.view.View view) {
        Uri gmmIntentUri = Uri.parse("geo:0,0?q=münchen+Flughafen");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        }
    }

    public void OpenMuensterLocationInGoogleMaps(android.view.View view) {
        Uri gmmIntentUri = Uri.parse("geo:0,0?q=ulmer+münster");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle)
    {

    }

    public void getLocationPermission()
    {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    0);
    }

    @SuppressLint("MissingPermission")


    public void getLastPosition()
    {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
        }
        else
        {
            getLocationPermission();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent)
    {
        int stringMaxLength = 5;
        if(sensorEvent.sensor == mLight)
        {
            float value = sensorEvent.values[0];
            String convertedValue = Float.toString(value);
            brighnessValueTextView.setText(convertedValue);
        }
        if(sensorEvent.sensor == mMagnetometer)
        {
            float value = sensorEvent.values[0];
            String convertedValue = Float.toString(value);
            magnetometerValueTextView.setText(convertedValue);
        }
        if(sensorEvent.sensor == mProximity)
        {
            float value = sensorEvent.values[0];
            String convertedValue = Float.toString(value);
            proximitysValueTextView.setText(convertedValue);
        }
        if(sensorEvent.sensor == mGyroscope)
        {
            float valueX = sensorEvent.values[0] ;
            float valueY = sensorEvent.values[1] ;
            float valueZ = sensorEvent.values[2] ;
            String outputText = "X: " + Float.toString(valueX)
                    + " Y: " + Float.toString(valueY) + "\n"
                    + "Z: " + Float.toString(valueZ);
            gyroscopeValueTextView.setText(outputText);
        }
        if(sensorEvent.sensor == mAcceleration)
        {
            float valueX = sensorEvent.values[0] ;
            float valueY = sensorEvent.values[1] ;
            float valueZ = sensorEvent.values[2] ;
            String outputText = "X: " + Float.toString(valueX)
                    + " Y: " + Float.toString(valueY) + "\n"
                    + "Z: " + Float.toString(valueZ);
            accelerationValueTextView.setText(outputText);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
