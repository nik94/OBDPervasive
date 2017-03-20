package com.example.fede_xps.pervasivesystemsminiproject.OBD.reader.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ActionMenuView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fede_xps.pervasivesystemsminiproject.OBD.MapsActivity;
import com.example.fede_xps.pervasivesystemsminiproject.R;
import com.example.fede_xps.pervasivesystemsminiproject.OBD.commands.ObdCommand;
import com.example.fede_xps.pervasivesystemsminiproject.OBD.commands.SpeedCommand;
import com.example.fede_xps.pervasivesystemsminiproject.OBD.commands.engine.RPMCommand;
import com.example.fede_xps.pervasivesystemsminiproject.OBD.commands.engine.RuntimeCommand;
import com.example.fede_xps.pervasivesystemsminiproject.OBD.enums.AvailableCommandNames;
import com.example.fede_xps.pervasivesystemsminiproject.OBD.reader.config.ObdConfig;
import com.example.fede_xps.pervasivesystemsminiproject.OBD.reader.io.AbstractGatewayService;
import com.example.fede_xps.pervasivesystemsminiproject.OBD.reader.io.LogCSVWriter;
import com.example.fede_xps.pervasivesystemsminiproject.OBD.reader.io.MockObdGatewayService;
import com.example.fede_xps.pervasivesystemsminiproject.OBD.reader.io.ObdCommandJob;
import com.example.fede_xps.pervasivesystemsminiproject.OBD.reader.io.ObdGatewayService;
import com.example.fede_xps.pervasivesystemsminiproject.OBD.reader.io.ObdProgressListener;
import com.example.fede_xps.pervasivesystemsminiproject.OBD.reader.net.ObdReading;
import com.example.fede_xps.pervasivesystemsminiproject.OBD.reader.net.ObdService;
import com.example.fede_xps.pervasivesystemsminiproject.OBD.reader.trips.TripLog;
import com.example.fede_xps.pervasivesystemsminiproject.OBD.reader.trips.TripRecord;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.inject.Inject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import roboguice.RoboGuice;
import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

import static com.example.fede_xps.pervasivesystemsminiproject.OBD.reader.activity.ConfigActivity.getGpsDistanceUpdatePeriod;
import static com.example.fede_xps.pervasivesystemsminiproject.OBD.reader.activity.ConfigActivity.getGpsUpdatePeriod;



@ContentView(R.layout.activity_main)
public class MainActivity extends RoboActivity implements ObdProgressListener, LocationListener, GpsStatus.Listener {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    private static final String TAG = MainActivity.class.getName();
    private static final int NO_BLUETOOTH_ID = 0;
    private static final int BLUETOOTH_DISABLED = 1;
    private static final int SETTINGS = 4;
    private static final int TABLE_ROW_MARGIN = 7;
    private static final int NO_ORIENTATION_SENSOR = 8;
    private static final int NO_GPS_SUPPORT = 9;
    private static final int TRIPS_LIST = 10;
    private static final int SAVE_TRIP_NOT_AVAILABLE = 11;
    private static final int REQUEST_ENABLE_BT = 1234;
    private static boolean bluetoothDefaultIsEnable = false;
    private int aggiorna;






    public Map<String, String> commandResult = new HashMap<String, String>();
    boolean mGpsIsStarted = false;
    private LocationManager mLocService;
    private LocationProvider mLocProvider;
    private LogCSVWriter myCSVWriter;
    private Location mLastLocation;
    /// the trip log
    private TripLog triplog;
    private TripRecord currentTrip;

    private ArrayList<String> names = new ArrayList<String>();
    private ArrayList<String> result = new ArrayList<String>();
    private ArrayList<String> id = new ArrayList<String>();


    @InjectView(R.id.compass_text)
    private TextView compass;

//    int permissionCheck = ContextCompat.checkSelfPermission(this,
    //          Manifest.permission.ACCESS_FINE_LOCATION);

    private final SensorEventListener orientListener = new SensorEventListener() {
        //BUSSOLA
        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            String dir = "";
            if (x >= 337.5 || x < 22.5) {
                dir = "N";
            } else if (x >= 22.5 && x < 67.5) {
                dir = "NE";
            } else if (x >= 67.5 && x < 112.5) {
                dir = "E";
            } else if (x >= 112.5 && x < 157.5) {
                dir = "SE";
            } else if (x >= 157.5 && x < 202.5) {
                dir = "S";
            } else if (x >= 202.5 && x < 247.5) {
                dir = "SW";
            } else if (x >= 247.5 && x < 292.5) {
                dir = "W";
            } else if (x >= 292.5 && x < 337.5) {
                dir = "NW";
            }
            updateTextView(compass, dir);
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // do nothing
        }
    };
    @InjectView(R.id.BT_STATUS)
    private TextView btStatusTextView;
    @InjectView(R.id.OBD_STATUS)
    private TextView obdStatusTextView;
    @InjectView(R.id.vehicle_view)
    private LinearLayout vv;
    @InjectView(R.id.data_table)
    private TableLayout tl;
    @Inject
    private SensorManager sensorManager;
    @Inject
    private PowerManager powerManager;
    @Inject
    private SharedPreferences prefs;
    private boolean isServiceBound;
    private AbstractGatewayService service;
    private final Runnable mQueueCommands = new Runnable() {
        public void run() {
            if (service != null && service.isRunning() && service.queueEmpty()) {
                queueCommands();

                double lat = 0;
                double lon = 0;
                double alt = 0;
                final int posLen = 7;
                if (mGpsIsStarted && mLastLocation != null) {
                    lat = mLastLocation.getLatitude();
                    lon = mLastLocation.getLongitude();
                    alt = mLastLocation.getAltitude();

                    StringBuilder sb = new StringBuilder();
                    sb.append("Lat: ");
                    sb.append(String.valueOf(mLastLocation.getLatitude()).substring(0, posLen));
                    sb.append(" Lon: ");
                    sb.append(String.valueOf(mLastLocation.getLongitude()).substring(0, posLen));
                    sb.append(" Alt: ");
                    sb.append(String.valueOf(mLastLocation.getAltitude()));
                }
                if (prefs.getBoolean(ConfigActivity.UPLOAD_DATA_KEY, false)) {
                    // Upload the current reading by http
                    final String vin = prefs.getString(ConfigActivity.VEHICLE_ID_KEY, "UNDEFINED_VIN");
                    Map<String, String> temp = new HashMap<String, String>();
                    temp.putAll(commandResult);
                    ObdReading reading = new ObdReading(lat, lon, alt, System.currentTimeMillis(), vin, temp);
                    new UploadAsyncTask().execute(reading);

                } else if (prefs.getBoolean(ConfigActivity.ENABLE_FULL_LOGGING_KEY, false)) {
                    // Write the current reading to CSV
                    final String vin = prefs.getString(ConfigActivity.VEHICLE_ID_KEY, "UNDEFINED_VIN");
                    Map<String, String> temp = new HashMap<String, String>();
                    temp.putAll(commandResult);
                    ObdReading reading = new ObdReading(lat, lon, alt, System.currentTimeMillis(), vin, temp);
                    myCSVWriter.writeLineCSV(reading);
                }
                commandResult.clear();
            }
            // run again in period defined in preferences
            new Handler().postDelayed(mQueueCommands, ConfigActivity.getObdUpdatePeriod(prefs));
        }
    };
    private Sensor orientSensor = null;
    private PowerManager.WakeLock wakeLock = null;
    private boolean preRequisites = true;
    private ServiceConnection serviceConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder binder) {
            Log.d(TAG, className.toString() + " service is bound");
            isServiceBound = true;
            service = ((AbstractGatewayService.AbstractGatewayServiceBinder) binder).getService();
            service.setContext(MainActivity.this);
            Log.d(TAG, "Starting live data");
            try {
                service.startService();
                if (preRequisites)
                    btStatusTextView.setText(getString(R.string.status_bluetooth_connected));
            } catch (IOException ioe) {
                Log.e(TAG, "Failure Starting live data");
                btStatusTextView.setText(getString(R.string.status_bluetooth_error_connecting));
                doUnbindService();
            }
        }

        @Override
        protected Object clone() throws CloneNotSupportedException {
            return super.clone();
        }

        // This method is *only* called when the connection to the service is lost unexpectedly
        // and *not* when the client unbinds (http://developer.android.com/guide/components/bound-services.html)
        // So the isServiceBound attribute should also be set to false when we unbind from the service.
        @Override
        public void onServiceDisconnected(ComponentName className) {
            Log.d(TAG, className.toString() + " service is unbound");
            isServiceBound = false;
        }
    };

    public static String LookUpCommand(String txt) {
        for (AvailableCommandNames item : AvailableCommandNames.values()) {
            if (item.getValue().equals(txt)) return item.name();
        }
        return txt;
    }

    public void updateTextView(final TextView view, final String txt) {
        new Handler().post(new Runnable() {
            public void run() {
                view.setText(txt);
            }
        });
    }

    public void stateUpdate(final ObdCommandJob job) {
        String cmdName = job.getCommand().getName();
        String cmdResult = "";
        final String cmdID = LookUpCommand(cmdName);

        if (job.getState().equals(ObdCommandJob.ObdCommandJobState.EXECUTION_ERROR)) {
            cmdResult = job.getCommand().getResult();
            if (cmdResult != null) {
                obdStatusTextView.setText(cmdResult.toLowerCase());
            }
        } else if (job.getState().equals(ObdCommandJob.ObdCommandJobState.NOT_SUPPORTED)) {
            cmdResult = getString(R.string.status_obd_no_support);
        } else {
            cmdResult = job.getCommand().getFormattedResult();
            obdStatusTextView.setText(getString(R.string.status_obd_data));
        }

        if (vv.findViewWithTag(cmdID) != null) {
            TextView existingTV = (TextView) vv.findViewWithTag(cmdID);
            existingTV.setText(cmdResult);
        } else
        names.add(cmdName);
        result.add(cmdResult);
        id.add(cmdID);
        updateTripStatistic(job, cmdID);
    }

    private void updateTripStatistic(final ObdCommandJob job, final String cmdID) {
//VELOCITA'
        if (currentTrip != null) {
            if (cmdID.equals(AvailableCommandNames.SPEED.toString())) {
                SpeedCommand command = (SpeedCommand) job.getCommand();
                currentTrip.setSpeedMax(command.getMetricSpeed());
            } else if (cmdID.equals(AvailableCommandNames.ENGINE_RPM.toString())) {
                RPMCommand command = (RPMCommand) job.getCommand();
                currentTrip.setEngineRpmMax(command.getRPM());
            } else if (cmdID.endsWith(AvailableCommandNames.ENGINE_RUNTIME.toString())) {
                RuntimeCommand command = (RuntimeCommand) job.getCommand();
                currentTrip.setEngineRuntime(command.getFormattedResult());
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        Button buttonStop = (Button) findViewById(R.id.stop);
        buttonStop.setClickable(false);

        final BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        if (btAdapter != null)
            bluetoothDefaultIsEnable = btAdapter.isEnabled();

        // get Orientation sensor
        List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ORIENTATION);
        if (sensors.size() > 0)
            orientSensor = sensors.get(0);
        else
            showDialog(NO_ORIENTATION_SENSOR);

        // create a log instance for use by this application
        triplog = TripLog.getInstance(this.getApplicationContext());
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Log.d(TAG, "Entered onStart.");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mLocService != null) {
            mLocService.removeGpsStatusListener(this);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            mLocService.removeUpdates(this);
        }

        //releaseWakeLockIfHeld();
        if (isServiceBound) {
            doUnbindService();
        }

        endTrip();

        final BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        if (btAdapter != null && btAdapter.isEnabled() && !bluetoothDefaultIsEnable)
            btAdapter.disable();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "Pausing.");
        //releaseWakeLockIfHeld();
    }

    /**
     * If lock is held, release. Lock will be held when the service is running.
     */
    private void releaseWakeLockIfHeld() {
        if (wakeLock.isHeld())
            wakeLock.release();
    }

    protected void onResume() {
        super.onResume();
        Log.d(TAG, "Resuming.");
        sensorManager.registerListener(orientListener, orientSensor,
                SensorManager.SENSOR_DELAY_UI);
        //wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK,
                //"ObdReader");

        // get Bluetooth device
        final BluetoothAdapter btAdapter = BluetoothAdapter
                .getDefaultAdapter();

        preRequisites = btAdapter != null && btAdapter.isEnabled();
        if (!preRequisites && prefs.getBoolean(ConfigActivity.ENABLE_BT_KEY, false)) {
            preRequisites = btAdapter != null && btAdapter.enable();
        }


        if (!preRequisites) {
            showDialog(BLUETOOTH_DISABLED);
            btStatusTextView.setText(getString(R.string.status_bluetooth_disabled));
        } else {
            btStatusTextView.setText(getString(R.string.status_bluetooth_ok));
        }
    }

    private void updateConfig() {
        startActivity(new Intent(this, ConfigActivity.class));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
         menu.add(0, TRIPS_LIST, 0, getString(R.string.menu_trip_list));
        menu.add(0, SETTINGS, 0, getString(R.string.menu_settings));
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case SETTINGS:
                updateConfig();
                return true;
            case TRIPS_LIST:
                startActivity(new Intent(this, TripListActivity.class));
                return true;
        }
        return false;
    }


    private void startLiveData() {
        Log.d(TAG, "Starting live data.");

        tl.removeAllViews(); //start fresh
        doBindService();

        currentTrip = triplog.startTrip();
        if (currentTrip == null)
            showDialog(SAVE_TRIP_NOT_AVAILABLE);

        // start command execution
        new Handler().post(mQueueCommands);

        if (prefs.getBoolean(ConfigActivity.ENABLE_GPS_KEY, false))
            gpsStart();
        else

        // screen won't turn off until wakeLock.release()
        //wakeLock.acquire();

        if (prefs.getBoolean(ConfigActivity.ENABLE_FULL_LOGGING_KEY, false)) {

            // Create the CSV Logger
            long mils = System.currentTimeMillis();
            SimpleDateFormat sdf = new SimpleDateFormat("_dd_MM_yyyy_HH_mm_ss");

            try {
                myCSVWriter = new LogCSVWriter("Log" + sdf.format(new Date(mils)).toString() + ".csv",
                        prefs.getString(ConfigActivity.DIRECTORY_FULL_LOGGING_KEY,
                                getString(R.string.default_dirname_full_logging))
                );
            } catch (FileNotFoundException | RuntimeException e) {
                Log.e(TAG, "Can't enable logging to file.", e);
            }
        }
    }

    private void stopLiveData() {
        Log.d(TAG, "Stopping live data.");

        gpsStop();

        doUnbindService();
        endTrip();

        //releaseWakeLockIfHeld();

        final String devemail = prefs.getString(ConfigActivity.DEV_EMAIL_KEY, null);
        if (devemail != null) {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            ObdGatewayService.saveLogcatToFile(getApplicationContext(), devemail);
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            break;
                    }
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("\n" +
                    "The travel log is completed!\n" +
                    "Now you can find it between your trips list!").setPositiveButton("Ok", dialogClickListener).show();
        }

        if (myCSVWriter != null) {
            myCSVWriter.closeLogCSVWriter();
        }
    }

    protected void endTrip() {
        if (currentTrip != null) {
            currentTrip.setEndDate(new Date());
            triplog.updateRecord(currentTrip);
        }
    }

    protected Dialog onCreateDialog(int id) {
        AlertDialog.Builder build = new AlertDialog.Builder(this);
        switch (id) {
            case NO_BLUETOOTH_ID:
                build.setMessage(getString(R.string.text_no_bluetooth_id));
                return build.create();
            case BLUETOOTH_DISABLED:
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                return build.create();
            case NO_ORIENTATION_SENSOR:
                build.setMessage(getString(R.string.text_no_orientation_sensor));
                return build.create();
            case NO_GPS_SUPPORT:
                build.setMessage(getString(R.string.text_no_gps_support));
                return build.create();
            case SAVE_TRIP_NOT_AVAILABLE:
                build.setMessage(getString(R.string.text_save_trip_not_available));
                return build.create();
        }
        return null;
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem settingsItem = menu.findItem(SETTINGS);

        if (service != null && service.isRunning()) {
            settingsItem.setEnabled(false);
        } else {
            settingsItem.setEnabled(true);
        }

        return true;
    }

    private void addTableRow(String id, String key, String val) {

        TableRow tr = new TableRow(this);
        MarginLayoutParams params = new MarginLayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.setMargins(TABLE_ROW_MARGIN, TABLE_ROW_MARGIN, TABLE_ROW_MARGIN,
                TABLE_ROW_MARGIN);
        tr.setLayoutParams(params);

        TextView name = new TextView(this);
        name.setGravity(Gravity.RIGHT);
        name.setText(key + ": ");
        TextView value = new TextView(this);
        value.setGravity(Gravity.LEFT);
        value.setText(val);
        value.setTag(id);
        tr.addView(name);
        tr.addView(value);
        tl.addView(tr, params);
    }

    /**
     *INSERISCO I DATI CHE MI INTERESSANO
     */
    private void queueCommands() {
        if (isServiceBound) {
            for (ObdCommand Command : ObdConfig.getCommands()) {
                if (prefs.getBoolean(Command.getName(), true))
                    service.queueJob(new ObdCommandJob(Command));
            }
        }
    }

    private void doBindService() {
        if (!isServiceBound) {
            Log.d(TAG, "Binding OBD service.");
            if (preRequisites) {
                btStatusTextView.setText(getString(R.string.status_bluetooth_connecting));
                Intent serviceIntent = new Intent(this, ObdGatewayService.class);
                bindService(serviceIntent, serviceConn, Context.BIND_AUTO_CREATE);
            } else {
                btStatusTextView.setText(getString(R.string.status_bluetooth_disabled));
                Intent serviceIntent = new Intent(this, MockObdGatewayService.class);
                bindService(serviceIntent, serviceConn, Context.BIND_AUTO_CREATE);
            }
        }
    }

    private void doUnbindService() {
        if (isServiceBound) {
            if (service.isRunning()) {
                service.stopService();
                if (preRequisites)
                    btStatusTextView.setText(getString(R.string.status_bluetooth_ok));
            }
            Log.d(TAG, "Unbinding OBD service.");
            unbindService(serviceConn);
            isServiceBound = false;
            obdStatusTextView.setText(getString(R.string.status_obd_disconnected));
        }
    }

    public void onLocationChanged(Location location) {
        mLastLocation = location;
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    public void onProviderEnabled(String provider) {
    }

    public void onProviderDisabled(String provider) {
    }
//Not use
    public void onGpsStatusChanged(int event) {

        switch (event) {
            case GpsStatus.GPS_EVENT_STARTED:
                break;
            case GpsStatus.GPS_EVENT_STOPPED:
                break;
            case GpsStatus.GPS_EVENT_FIRST_FIX:
                break;
            case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                btStatusTextView.setText(getString(R.string.status_bluetooth_connected));
            } else {
                Toast.makeText(this, R.string.text_bluetooth_disabled, Toast.LENGTH_LONG).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private synchronized void gpsStart() {
        if (!mGpsIsStarted && mLocProvider != null && mLocService != null && mLocService.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mLocService.requestLocationUpdates(mLocProvider.getName(), getGpsUpdatePeriod(prefs), getGpsDistanceUpdatePeriod(prefs), this);
            mGpsIsStarted = true;
        } else {
        }
    }

    private synchronized void gpsStop() {
        if (mGpsIsStarted) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mLocService.removeUpdates(this);
            mGpsIsStarted = false;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        client.disconnect();
    }

    /**
     * Uploading asynchronous task
     */
    private class UploadAsyncTask extends AsyncTask<ObdReading, Void, Void> {

        @Override
        protected Void doInBackground(ObdReading... readings) {
            Log.d(TAG, "Uploading " + readings.length + " readings..");
            // instantiate reading service client
            final String endpoint = prefs.getString(ConfigActivity.UPLOAD_URL_KEY, "");
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(endpoint)
                    .build();
            ObdService service = restAdapter.create(ObdService.class);
            // upload readings
            for (ObdReading reading : readings) {
                try {
                    Response response = service.uploadReading(reading);
                    assert response.getStatus() == 200;
                } catch (RetrofitError re) {
                    Log.e(TAG, re.toString());
                }

            }
            Log.d(TAG, "Done");
            return null;
        }

    }

    public void start (View view) {
        startLiveData();
        aggiorna=0;
        Button buttonPlay = (Button) findViewById(R.id.play);
        buttonPlay.setClickable(false);

        Button buttonStop = (Button) findViewById(R.id.stop);
        buttonStop.setClickable(true);
    }


    public void stop (View view) {
        names.clear();
        names.trimToSize();
        result.clear();
        result.trimToSize();
        id.clear();
        id.trimToSize();

        stopLiveData();

        Button buttonPlay = (Button) findViewById(R.id.play);
        buttonPlay.setClickable(true);
        Button buttonStop = (Button) findViewById(R.id.stop);
             buttonStop.setClickable(false);

    }


    public void OpenMaps(View v) {
        Intent openPage1 = new Intent(MainActivity.this,MapsActivity.class);
        // passo all'attivazione dell'activity page1.java
        startActivity(openPage1);
    }





    @TargetApi(Build.VERSION_CODES.M)
    public void OBDCommand(View v){

        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.activity_main);
        dialog.setTitle("    LOG OBD COMMANDS");



        TableRow button = (TableRow) dialog.findViewById(R.id.button);
        button.setVisibility(View.INVISIBLE);

        TableRow fuel = (TableRow) dialog.findViewById(R.id.fuel);
        fuel.setVisibility(View.INVISIBLE);

        TableRow bobd = (TableRow) dialog.findViewById(R.id.bobd);
        bobd.setVisibility(View.INVISIBLE);

        TableRow giri = (TableRow) dialog.findViewById(R.id.giri);
        giri.setVisibility(View.INVISIBLE);

        TableRow status = (TableRow) dialog.findViewById(R.id.status);
        status.setVisibility(View.INVISIBLE);

        TableRow image = (TableRow) dialog.findViewById(R.id.image);
        image.setVisibility(View.INVISIBLE);

        TableRow km = (TableRow) dialog.findViewById(R.id.km);
        km.setVisibility(View.INVISIBLE);

        TableRow compass = (TableRow) dialog.findViewById(R.id.compass);
        compass.setVisibility(View.INVISIBLE);



        LayoutParams baparams = button.getLayoutParams();
        baparams.width = 0;
        baparams.height =0;
        button.requestLayout();

        LayoutParams gparams = giri.getLayoutParams();
        gparams.width = 0;
        gparams.height =0;
        giri.requestLayout();

        LayoutParams kmparams = km.getLayoutParams();
        kmparams.width = 0;
        kmparams.height =0;
        km.requestLayout();

        LayoutParams fparams = fuel.getLayoutParams();
        fparams.width = 0;
        fparams.height =0;
        fuel.requestLayout();

        LayoutParams oparams = bobd.getLayoutParams();
        oparams.width = 0;
        oparams.height =0;
        bobd.requestLayout();

        LayoutParams sparams = status.getLayoutParams();
        sparams.width = 0;
        sparams.height =0;
        status.requestLayout();


        LayoutParams imparams = image.getLayoutParams();
        imparams.width = 0;
        imparams.height =0;
        image.requestLayout();

        LayoutParams cparams = compass.getLayoutParams();
        cparams.width = 0;
        cparams.height =0;
        compass.requestLayout();



        ScrollView dati = (ScrollView) dialog.findViewById(R.id.data_scroll);
        dati.setVisibility(View.VISIBLE);
        LayoutParams dparams = dati.getLayoutParams();
        dparams.width = LayoutParams.MATCH_PARENT;
        dparams.height = 1450;
        dati.requestLayout();




        Button buttonSetting = (Button) dialog.findViewById(R.id.setting);
        buttonSetting.setVisibility(View.VISIBLE);
        buttonSetting.setText("SETTING");
        buttonSetting.setPadding(5,5,5,5);
        LayoutParams settingparams = buttonSetting.getLayoutParams();
        settingparams.width = LayoutParams.WRAP_CONTENT;
        settingparams.height = LayoutParams.WRAP_CONTENT;
        buttonSetting.requestLayout();
        buttonSetting.setBackground(getDrawable(R.drawable.background_button));
        buttonSetting.setTextColor(getColor(R.color.white));
        buttonSetting.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                updateConfig();


            }
        });



        TableLayout tl2 = (TableLayout) dialog.findViewById(R.id.data_table);

        TableRow tr2 = new TableRow(this);
        MarginLayoutParams params = new MarginLayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            params.setMargins(TABLE_ROW_MARGIN, TABLE_ROW_MARGIN, TABLE_ROW_MARGIN,
                    TABLE_ROW_MARGIN);

        LinearLayout parent = new LinearLayout(this);

        parent.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        parent.setOrientation(LinearLayout.VERTICAL);
        tr2.setLayoutParams(params);

        if (service != null && service.isRunning()) {
            buttonSetting.setBackground(getDrawable(R.drawable.background_button_off));
            buttonSetting.setClickable(false);}

        for (int i =0; i<names.size(); i++){
            if (aggiorna==i) {
                TextView aggiornamento = new TextView(this);
                aggiornamento.setText("+++++++++  UPDATING  +++++++++");
                aggiornamento.setGravity(View.TEXT_ALIGNMENT_CENTER);
                aggiornamento.setTextColor(getColor(R.color.verde));
                parent.addView(aggiornamento);
            }

            TextView value = new TextView(this);
            value.setGravity(Gravity.LEFT);
            String res = names.get(i).toString() + ": " + result.get(i).toString();
            value.setText(res);
            String idd = id.get(i).toString();
            value.setTag(idd + ":\n");
            value.setTextColor(getColor(R.color.blue));
            parent.addView(value);
            tl2.removeAllViews();
            tl2.addView(parent, params);
        }

        aggiorna = names.size();

        dialog.show();
    }
}
