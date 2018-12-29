package com.serviceintentserviceandbrodcastreceiver;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.*;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.serviceintentserviceandbrodcastreceiver.BroadcastReceiver.NotificationService;
import com.serviceintentserviceandbrodcastreceiver.Services.BackGroundServices;
import com.serviceintentserviceandbrodcastreceiver.Services.BoundService;
import com.serviceintentserviceandbrodcastreceiver.Services.ForeGroundServices;
import com.serviceintentserviceandbrodcastreceiver.Services.SampleIntentService;

import java.io.File;

import static kotlin.text.Typography.amp;

public class MainActivity extends AppCompatActivity {

    Button btnForStart,btnForStop,btnBackStart,btnBackStop,btnBindStart,btnBindStop,btnIntentService,btnStartServiceWithBrodcast;
    BoundService mService;
    boolean mBound = false;
    private ServiceConnection mConnection;

    ProgressBar pd;
    ImageView imgView;
    SampleResultReceiver resultReceiever;
    //String defaultUrl = "http://developer.android.com/assets/images/dac_logo.png";
    String defaultUrl = "https://www.w3schools.com/images/myw3schoolsimage.jpg";

    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG,"onCreate");

        initview();
    }

    void initview(){
        btnForStart = findViewById(R.id.btn_for_startservice);
        btnForStop = findViewById(R.id.btn_for_stopservice);

        btnBackStart = findViewById(R.id.btn_back_startservice);
        btnBackStop = findViewById(R.id.btn_back_stopservice);


        btnBindStart = findViewById(R.id.btn_bind_startservice);
        btnBindStop = findViewById(R.id.btn_bind_stopservice);

        resultReceiever = new SampleResultReceiver(new Handler());
        pd = (ProgressBar) findViewById(R.id.downloadPD);
        imgView = (ImageView) findViewById(R.id.imgView);
        btnIntentService = findViewById(R.id.btn_intent_startservice);

        btnStartServiceWithBrodcast = findViewById(R.id.btn_brodcastwithService);


        btnForStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startIntent = new Intent(MainActivity.this, ForeGroundServices.class);
                startIntent.setAction(Constants.STARTFOREGROUND_ACTION);
                startService(startIntent);
            }
        });


        btnForStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent stopIntent =  new Intent(MainActivity.this,ForeGroundServices.class);
                stopIntent.setAction(Constants.STOPFOREGROUND_ACTION);
                startService(stopIntent);
            }
        });

        btnBackStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_back_start = new Intent(MainActivity.this,BackGroundServices.class);
                startService(intent_back_start);
            }
        });

        btnBackStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_back_Stop= new Intent(MainActivity.this,BackGroundServices.class);
                stopService(intent_back_Stop);
            }
        });

        btnBindStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                // Bind to LocalService
//                Intent intent = new Intent(MainActivity.this, BoundService.class);
//                bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

                if (mBound) {
                    // Call a method from the LocalService.
                    // However, if this call were something that might hang, then this request should
                    // occur in a separate thread to avoid slowing down the activity performance.
                    int num = mService.getRandomNumber();
                    Toast.makeText(MainActivity.this, "number: " + num, Toast.LENGTH_SHORT).show();
                }else{
                    // Bind to LocalService
                    Intent intent = new Intent(MainActivity.this, BoundService.class);
                    bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
                }
            }
        });

        btnBindStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unbindService(mConnection);
                mBound = false;
            }
        });


        btnIntentService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startIntent = new Intent(MainActivity.this,
                        SampleIntentService.class);
                startIntent.putExtra("receiver", resultReceiever);
                startIntent.putExtra("url", defaultUrl);
                startService(startIntent);

                pd.setVisibility(View.VISIBLE);
                pd.setIndeterminate(true);




//                String filePath =Environment.getExternalStorageDirectory().getAbsolutePath() +"/Test.jpg";
//                File imageFile = new File(filePath);
//                //String path = Environment.getExternalStorageDirectory().toString() + filePath;
//                Bitmap bmp = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
//                if ( imgView != null && bmp != null) {
//                    imgView.setImageBitmap(bmp);
//                    Log.d(TAG, "DOWNLOAD_SUCCESS");
//                }
            }
        });


        btnStartServiceWithBrodcast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("RestartService","Start Service From Main2Activity");
                startService(new Intent(MainActivity.this,NotificationService.class));
            }
        });


        /** Defines callbacks for service binding, passed to bindService() */
         mConnection = new ServiceConnection() {

            @Override
            public void onServiceConnected(ComponentName className,
                                           IBinder service) {
                // We've bound to LocalService, cast the IBinder and get LocalService instance
                BoundService.LocalBinder binder = (BoundService.LocalBinder) service;
                mService = binder.getService();
                mBound = true;
            }

            @Override
            public void onServiceDisconnected(ComponentName arg0) {
                mBound = false;
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG,"onStart");
//        // Bind to LocalService
//        Intent intent = new Intent(this, BoundService.class);
//        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG,"onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG,"onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy");
    }

    private class SampleResultReceiver extends ResultReceiver {

        public SampleResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            Log.d(TAG,"onReceiveResult");
            switch (resultCode) {
                case SampleIntentService.DOWNLOAD_ERROR:
                    Toast.makeText(getApplicationContext(), "error in download",
                            Toast.LENGTH_SHORT).show();
                    pd.setVisibility(View.INVISIBLE);
                    break;

                case SampleIntentService.DOWNLOAD_SUCCESS:
                    String DeviceFileLocation= Environment.getExternalStorageDirectory().getAbsolutePath() + "/SampleIntent/";
                    File dir = new File(DeviceFileLocation);
                    File image = new File(dir, "IntentService_Example.png");
                    String filePath = resultData.getString("filePath");
                    File imageFile = new File(filePath);
                    //String path = Environment.getExternalStorageDirectory().toString() + filePath;
                    Bitmap bmp = BitmapFactory.decodeFile(image.getAbsolutePath());
                    if ( imgView != null && bmp != null){
                    imgView.setImageBitmap(bmp);
                        Log.d(TAG,"DOWNLOAD_SUCCESS");
                    Toast.makeText(getApplicationContext(),
                            "image download via IntentService is done",
                            Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(getApplicationContext(),
                            "error in decoding downloaded file",
                            Toast.LENGTH_SHORT).show();
                }
                pd.setIndeterminate(false);
                pd.setVisibility(View.INVISIBLE);

                break;
            }
            super.onReceiveResult(resultCode, resultData);
        }
    }
}
