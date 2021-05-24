package com.variable.kidslocationdetector;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.BuildConfig;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * The only activity in this sample.
 * <p>
 * Note: Users have three options in "Q" regarding location:
 * <ul>
 *     <li>Allow all the time</li>
 *     <li>Allow while app is in use, i.e., while app is in foreground</li>
 *     <li>Not allow location at all</li>
 * </ul>
 * Because this app creates a foreground service (tied to a Notification) when the user navigates
 * away from the app, it only needs location "while in use." That is, there is no need to ask for
 * location all the time (which requires additional permissions in the manifest).
 * <p>
 * "Q" also now requires developers to specify foreground service type in the manifest (in this
 * case, "location").
 * <p>
 * Note: For Foreground Services, "P" requires additional permission in manifest. Please check
 * project manifest for more information.
 * <p>
 * Note: for apps running in the background on "O" devices (regardless of the targetSdkVersion),
 * location may be computed less frequently than requested when the app is not in the foreground.
 * Apps that use a foreground service -  which involves displaying a non-dismissable
 * notification -  can bypass the background location limits and request location updates as before.
 * <p>
 * This sample uses a long-running bound and started service for location updates. The service is
 * aware of foreground status of this activity, which is the only bound client in
 * this sample. After requesting location updates, when the activity ceases to be in the foreground,
 * the service promotes itself to a foreground service and continues receiving location updates.
 * When the activity comes back to the foreground, the foreground service stops, and the
 * notification associated with that foreground service is removed.
 * <p>
 * While the foreground service notification is displayed, the user has the option to launch the
 * activity from the notification. The user can also remove location updates directly from the
 * notification. This dismisses the notification and stops the service.
 */
public class MainActivity extends AppCompatActivity implements
        SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    // Used in checking for runtime permissions.
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    SimpleDateFormat df1;
    Calendar c;
    private View parent_view;
    // The BroadcastReceiver used to listen from broadcasts from the service.
    private MyReceiver myReceiver;

    // A reference to the service used to get location updates.
    private LocationUpdatesService mService = null;

    // Tracks the bound state of the service.
    private boolean mBound = false;
    // Monitors the state of the connection to the service.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LocationUpdatesService.LocalBinder binder = (LocationUpdatesService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
            mBound = false;
        }
    };
    // UI elements.
    private Button mRequestLocationUpdatesButton;
    private Button mRemoveLocationUpdatesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myReceiver = new MyReceiver();
        setContentView(R.layout.activity_main);
        c = Calendar.getInstance();
        df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm ss");
        parent_view = findViewById(android.R.id.content);
//        PreferenceManager.getDefaultSharedPreferences(this)
//                .registerOnSharedPreferenceChangeListener(this);

        mRequestLocationUpdatesButton = (Button) findViewById(R.id.request_location_updates_button);
        mRemoveLocationUpdatesButton = (Button) findViewById(R.id.remove_location_updates_button);


        // Check that the user hasn't revoked permissions by going to Settings.
        if (!checkPermissions()) {
            requestPermissions();
        } else {
            if (mService != null) {
                mService.requestLocationUpdates();
            } else {
                bindService(new Intent(this, LocationUpdatesService.class), mServiceConnection,
                        Context.BIND_AUTO_CREATE);
            }
        }
//        //the Date and time at which you want to execute
//        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date date = null;
//        Date date1 = null;
//        Date date2 = null;
//        try {
//            date = dateFormatter .parse("2020-02-01 12:43 00");
//            date1 = dateFormatter .parse("2020-02-01 12:44 00");
//            date2 = dateFormatter .parse("2020-02-01 12:45 00");
//
//            Date currentDate=dateFormatter.parse(dateFormatter.format(c.getTime()));
//            if (timeIsBefore(currentDate,date)){
//                addHistory();
//            }if (timeIsBefore(currentDate,date1)){
//                addHistory();
//
//            }
//            if (timeIsBefore(currentDate,date2)){
//                addHistory();
//
//            }
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

        //Now create the time and schedule it
//        Timer timer = new Timer();
//
//        //Use this if you want to execute it once
//        timer.schedule(new MyTimeTask(), date);
//        timer.schedule(new MyTimeTask(), date1);
//        timer.schedule(new MyTimeTask(), date2);



        SharedPreferences preferences = getSharedPreferences("time", Context.MODE_PRIVATE);
        if(!preferences.contains("time")){
            String startTime1 ;
            startTime1 = df1.format(Calendar.getInstance().getTime());
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("time", startTime1);
            editor.apply();
            Log.d("dd","again call");
        }



//                       String str_startTime1 = preferences.getString("time", "");
//
//                            Date startDate = null;
//                            try {
//                                startDate = df1.parse(str_startTime1);
//                            } catch (ParseException e) {
//                                e.printStackTrace();
//                            }
//                            Date endDate = null;
//                            try {
//                                String startTime2 = df1.format(Calendar.getInstance().getTime());
//                                endDate = df1.parse(startTime2);
//                            } catch (ParseException e) {
//                                e.printStackTrace();
//                            }
//
//                            long difference = endDate.getTime() - startDate.getTime();
////                            if (difference < 0) {
////                                Date dateMax = null;
////                                try {
////                                    dateMax = df1.parse("24:00");
////                                } catch (ParseException e) {
////                                    e.printStackTrace();
////                                }
////                                Date dateMin = null;
////                                try {
////                                    dateMin = df1.parse("00:00");
////                                } catch (ParseException e) {
////                                    e.printStackTrace();
////                                }
////                                difference = (dateMax.getTime() - startDate.getTime()) + (endDate.getTime() - dateMin.getTime());
////                            }
//                            int days = (int) (difference / (1000 * 60 * 60 * 24));
//                            int hours = (int) ((difference - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60));
//                            int min = (int) (difference - (1000 * 60 * 60 * 24 * days) - (1000 * 60 * 60 * hours)) / (1000 * 60);
//                            Log.i("log_tag", "Hours: " + hours + ", Mins: " + min+"hassan"+difference);
//                            if(min%2==0){
//                                if(!preferences.contains("insert")){
//                                    if(preferences.getInt("insert",0)==0) {
//                                        SharedPreferences.Editor editor = preferences.edit();
//                                        editor.putInt("insert", 1);
//                                        editor.apply();
//                                        Log.d("dataInsertCall","dataInsertCall");
//                                    }else{
//                                        Log.d("not insert","not insert");
//                                    }
//                                }
//
//                                Toast.makeText(mService, "complete two min"+min, Toast.LENGTH_SHORT).show();
////                                addHistory(String.valueOf(endDate))
//        //   1000*60*60*24
//

    }

    public static boolean timeIsBefore(Date d1, Date d2) {
        DateFormat f = new SimpleDateFormat("HH:mm:ss");
        return f.format(d1).compareTo(f.format(d2)) == 0;
    }

    @Override
    protected void onStart() {
        super.onStart();
        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);

//        Toast.makeText(mService, Constants.pinID, Toast.LENGTH_SHORT).show();
//        Log.d("pinId",Constants.pinID);

        if (!checkPermissions()) {
            requestPermissions();
        } else {
            if (mService != null) {
                mService.requestLocationUpdates();
            } else {
                bindService(new Intent(this, LocationUpdatesService.class), mServiceConnection,
                        Context.BIND_AUTO_CREATE);
                bindService(new Intent(this, HistorySaveService.class), mServiceConnection,
                        Context.BIND_AUTO_CREATE);
            }

        }

//        TimerTaskClass tmClass=new TimerTaskClass();
//        tmClass.startTimer();

                //Do something after 10000ms
//        Timer timer = new Timer();
//        timer.scheduleAtFixedRate(new java.util.TimerTask() {
//            @SuppressLint("DefaultLocale")
//            @Override
//            public void run() {
//                Log.e("con","started");
//                SimpleDateFormat     df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm ss");
//                String currentDate= df1.format(Calendar.getInstance().getTime());
//                addHistory(currentDate);
//            }
//        }, 60000, 60000);




                //

        //  Restore the state of the buttons when the activity (re)launches.
        // setButtonsState(Utils.requestingLocationUpdates(this));

        // Bind to the service. If the service is in foreground mode, this signals to the service
        // that since this activity is in the foreground, the service can exit foreground mode.
        bindService(new Intent(this, LocationUpdatesService.class), mServiceConnection,
                Context.BIND_AUTO_CREATE);


    }


    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver,
                new IntentFilter(LocationUpdatesService.ACTION_BROADCAST));

    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(myReceiver);
        super.onPause();
    }

    @Override
    protected void onStop() {
        if (mBound) {
            // Unbind from the service. This signals to the service that this activity is no longer
            // in the foreground, and the service can respond by promoting itself to a foreground
            // service.
            unbindService(mServiceConnection);
            mBound = false;
        }
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
        super.onStop();
    }

    /**
     * Returns the current state of the permissions needed.
     */
    private boolean checkPermissions() {
        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.

        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");
            Snackbar.make(
                    parent_view,
                    R.string.permission_rationale,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    })
                    .show();
        } else {
            Log.i(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted.
                mService.requestLocationUpdates();
            } else {
                // Permission denied.
                setButtonsState(false);
                Snackbar.make(
                        parent_view,
                        R.string.permission_denied_explanation,
                        Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.settings, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Build intent that displays the App settings screen.
                                Intent intent = new Intent();
                                intent.setAction(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package",
                                        BuildConfig.APPLICATION_ID, null);
                                intent.setData(uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        })
                        .show();
            }
        }
    }

    private void Updatedata(String strKey, String lat, String lon) {


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Child").child(strKey);
        ref.child("latitutde").setValue(lat);
        ref.child("longitude").setValue(lon).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Location Updated Successful", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Location Updated failed", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        // Update the buttons state depending on whether location updates are being requested.
        if (s.equals(Utils.KEY_REQUESTING_LOCATION_UPDATES)) {
            setButtonsState(sharedPreferences.getBoolean(Utils.KEY_REQUESTING_LOCATION_UPDATES,
                    false));
        }
    }

    private void setButtonsState(boolean requestingLocationUpdates) {
        if (requestingLocationUpdates) {
            mRequestLocationUpdatesButton.setEnabled(false);
            mRemoveLocationUpdatesButton.setEnabled(true);
        } else {
            mRequestLocationUpdatesButton.setEnabled(true);
            mRemoveLocationUpdatesButton.setEnabled(false);
        }
    }

    /**
     * Receiver for broadcasts sent by {@link LocationUpdatesService}.
     */
    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Location location = intent.getParcelableExtra(LocationUpdatesService.EXTRA_LOCATION);
            if (location != null) {

//                Toast.makeText(MainActivity.this, Utils.getLocationText(location),
//                        Toast.LENGTH_SHORT).show();

                SharedPreferences preferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
//                String share_pin = preferences.getString("pinId", "");


                String[] part6 = Utils.getLocationText(location).split(",");
                String lat = part6[0];
                String lon = part6[1];

                String pin = getIntent().getStringExtra("pin");
//                Random random=new Random();
//                int random_number=random.nextInt(99999);

                //  Toast.makeText(context, "lat"+lat+"\n"+"lon"+lon, Toast.LENGTH_SHORT).show();
                Toast.makeText(context, "latForground" + Constants.latForground + "\n" + "lngForground" + Constants.lngForground, Toast.LENGTH_SHORT).show();


                //  KidLocationModel kidLocationModel=new KidLocationModel(lat,lon,pin);
                FirebaseAuth mAuth = FirebaseAuth.getInstance();

                FirebaseDatabase.getInstance().getReference().child("Child").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            if (snapshot1.child("pin_genrate").getValue().toString().equals(pin)) {
                                String str_key = snapshot1.getKey();
                                Updatedata(str_key, lat, lon);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        }
    }


    //The task which you want to execute

//    private static class MyTimeTask extends TimerTask
//    {
//
//        public void run()
//        {
//
//
//
//            ///////////////////////
//
//
//
//    }

    void addHistory(){


        if (Constants.latForground.equals("") && Constants.lngForground.equals("")) {
//            Toast.makeText(mService, "empty", Toast.LENGTH_SHORT).show();
            return;

        } else {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat  df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm ss");

            HistoryModel historyModel = new HistoryModel(Constants.latForground, Constants.lngForground, Constants.pinID, df1.format(c.getTime()));
            FirebaseDatabase.getInstance().getReference().child("History_Data").push().setValue(historyModel).addOnSuccessListener(
                    new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
//                                Toast.makeText(getApplicationContext(), "History added", Toast.LENGTH_SHORT).show();
                        }
                    }
            ).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
        //write your code here

    }

}
