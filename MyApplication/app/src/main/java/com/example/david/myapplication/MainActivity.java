package com.example.david.myapplication;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

import org.tensorflow.contrib.android.TensorFlowInferenceInterface;


public class MainActivity extends Activity implements OnClickListener,
        SurfaceHolder.Callback {

    private static final int PIXEL_WIDTH = 64;
    private Bitmap croppedBitmap = null;

    SurfaceView cameraView;
    SurfaceHolder surfaceHolder;
    Camera camera;

    Button rejectButton, acceptButton;
    Button takePhotoButton;
    TextView output;
    private List<Classifier> mClassifiers = new ArrayList<>();
    private String prediction = "";
    private TextView predictionText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cameraView = (SurfaceView) this.findViewById(R.id.CameraView);
        surfaceHolder = cameraView.getHolder();
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceHolder.addCallback(this);

        output = (TextView) findViewById(R.id.output);
        predictionText = (TextView) findViewById(R.id.predictionText);
        takePhotoButton = (Button) findViewById(R.id.TakePhotoButton);
        takePhotoButton.setOnClickListener(this);

        rejectButton = (Button) findViewById(R.id.rejectButton);
        rejectButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                prediction = "";
                predictionText.setText("");
                acceptButton.setEnabled(false);
                rejectButton.setEnabled(false);
            }
        });

        acceptButton = (Button) findViewById(R.id.acceptButton);
        acceptButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                output.setText(output.getText() + prediction);
                prediction = "";
                predictionText.setText("");
                acceptButton.setEnabled(false);
                rejectButton.setEnabled(false);
            }
        });

        loadModel();
    }

    public void onClick(View v) {
        camera.takePicture(shutterCallback, rawCallback, jpegCallback);
    }

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
                int newWidth = 64, newHeight=64;
                Bitmap yourBitmap = BitmapFactory.decodeByteArray(data,0,data.length);
                Bitmap resized = Bitmap.createScaledBitmap(yourBitmap, newWidth, newHeight, true);

                int w = resized.getWidth();
                int h = resized.getHeight();
                int pixels[] = new int[w*h];
                resized.getPixels(pixels, 0, w, 0, 0, w, h);

                float floatPixels[] = new float[pixels.length];
                for (int i = 0; i < pixels.length; i++) {
                    floatPixels[i] = (float) pixels[i] / 255.0f;
                }

                croppedBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);

                prediction = "";
                for (Classifier classifier : mClassifiers) {
                    final Classification res = classifier.recognize(floatPixels);
                    prediction = res.getLabel();
                    if (prediction != null && !prediction.isEmpty()) {
                        predictionText.setText(prediction);
                        acceptButton.setEnabled(true);
                        rejectButton.setEnabled(true);
                    } else {
                        Toast t = Toast.makeText(MainActivity.this, "No se pudo interpretar la señal. Intenta de nuevo", Toast.LENGTH_SHORT);
                        t.show();
                    }
                }
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage());
                Toast t = Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT);
                t.show();
            }
            camera.startPreview();
        }
    };

    private void loadModel() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mClassifiers.add(TensorFlowClassifier.create(
                            getAssets(),
                            "Keras",
                            "opt_mnist_convnet-keras.pb", PIXEL_WIDTH,
                            "conv2d_1_input", "dense_2/Softmax", false)
                    );
                } catch (final Exception e) {
                    throw new RuntimeException("Error initializing classifiers!", e);
                }
            }
        }).start();
    }


}