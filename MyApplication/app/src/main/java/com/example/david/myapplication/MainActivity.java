package com.example.david.myapplication;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;

import android.app.Activity;
import android.content.ContentValues;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore.Images.Media;
import android.util.Base64;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity implements OnClickListener,
        SurfaceHolder.Callback {
    SurfaceView cameraView;
    SurfaceHolder surfaceHolder;
    Camera camera;

    Button startStopButton;
    TextView countdownTextView;
    Handler timerUpdateHandler;
    boolean timelapseRunning = false;
    int currentTime = 0;
    final int SECONDS_BETWEEN_PHOTOS = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cameraView = (SurfaceView) this.findViewById(R.id.CameraView);
        surfaceHolder = cameraView.getHolder();
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceHolder.addCallback(this);

        countdownTextView = (TextView) findViewById(R.id.CountDownTextView);
        startStopButton = (Button) findViewById(R.id.CountDownButton);
        startStopButton.setOnClickListener(this);
        timerUpdateHandler = new Handler();
    }

    public void onClick(View v) {
        if (!timelapseRunning) {
            startStopButton.setText("Stop");
            timelapseRunning = true;
            timerUpdateHandler.post(timerUpdateTask);
        } else {
            startStopButton.setText("Start");
            timelapseRunning = false;
            timerUpdateHandler.removeCallbacks(timerUpdateTask);
        }
    }

    private Runnable timerUpdateTask = new Runnable() {
        public void run() {
            if (currentTime < SECONDS_BETWEEN_PHOTOS) {
                currentTime++;
            } else {
                camera.takePicture(shutterCallback, rawCallback, jpegCallback);
                currentTime = 0;
            }

            timerUpdateHandler.postDelayed(timerUpdateTask, 1000);
            countdownTextView.setText("" + currentTime);
        }
    };

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        camera.startPreview();
    }

    public void surfaceCreated(SurfaceHolder holder) {
        camera = Camera.open();
        try {
            camera.setPreviewDisplay(holder);
            Camera.Parameters parameters = camera.getParameters();
            if (this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
                parameters.set("orientation", "portrait");
                camera.setDisplayOrientation(90);
            }
            camera.setParameters(parameters);
        } catch (IOException exception) {
            camera.release();
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        camera.stopPreview();
        camera.release();
    }

    Camera.ShutterCallback shutterCallback = new Camera.ShutterCallback() {
        public void onShutter() {
            //			 Log.d(TAG, "onShutter'd");
        }
    };

    Camera.PictureCallback rawCallback = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            //			 Log.d(TAG, "onPictureTaken - raw");
        }
    };

    Camera.PictureCallback jpegCallback = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            Uri imageFileUri = getContentResolver().insert(
                    Media.EXTERNAL_CONTENT_URI, new ContentValues());
            try {
                int newWidth = 300, newHeight=300;
                Bitmap yourBitmap = BitmapFactory.decodeByteArray(data,0,data.length);
                Bitmap resized = Bitmap.createScaledBitmap(yourBitmap, newWidth, newHeight, true);

                String imagen64 = getEncoded64ImageStringFromBitmap(resized);
                Log.i(imagen64,"<----------------"+imagen64);

                OutputStream imageFileOS = getContentResolver().openOutputStream(
                        imageFileUri);
                imageFileOS.write(data);
                imageFileOS.flush();
                imageFileOS.close();

                Toast t = Toast.makeText(MainActivity.this, "Saved JPEG!", Toast.LENGTH_SHORT);
                t.show();
            } catch (Exception e) {
                Toast t = Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT);
                t.show();
            }
            camera.startPreview();
        }
        public String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteFormat = stream.toByteArray();
            String imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);
            return imgString;
        }

    };


}