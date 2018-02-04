package xyz.pinaki.androidcamera.example;

import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import xyz.pinaki.android.camera.*;
import xyz.pinaki.android.camera.dimension.AspectRatio;
import xyz.pinaki.android.camera.dimension.Size;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private static final int REQUEST_WRITE = 1;
    private static final String TAG = MainActivity.class.getSimpleName();
    RelativeLayout container;
    DrawView drawView;
    ImageView previewImage;
    ImageSurface imageSurface;
    ImageView btnCamera;
    ImageView btnMeasure;
    SensorManager mSensorManager;
    Sensor mAccelerometer;
    Sensor mMagnetometer;
    CameraAPIClient apiClient;
    private float[] mGravity;
    private float[] mMagnetic;
    private double h = 10;
    private double D;
    private double AngleA = 0.0;
    DecimalFormat ThreeDForm = new DecimalFormat("#.###");
    float pressure;
    float[] value = new float[3];
    private Double objLength = 1.1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        container =(RelativeLayout) findViewById(R.id.container);
        btnCamera = (ImageView) findViewById(R.id.btnCamera);
        btnMeasure = (ImageView) findViewById(R.id.btnMeasure);
        drawView = new DrawView(this);

        container.addView(drawView);
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // write perm to write the image
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE);
        } else  if (null == savedInstanceState) {
            launchCamera();
        }
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                container.removeAllViews();
                launchCamera();
            }
        });
        btnMeasure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                objLength = drawView.calculate(D);
                drawView.clearCanvas();
                showResult();
            }
        });
    }

    private void launchCamera() {
        AspectRatio aspectRatio = AspectRatio.of(4, 3);
        btnCamera.setVisibility(View.INVISIBLE);
        btnMeasure.setVisibility(View.INVISIBLE);
        apiClient = new CameraAPIClient.Builder(this).
                previewType(CameraAPI.PreviewType.SURFACE_VIEW).
                maxSizeSmallerDimPixels(1000).
                desiredAspectRatio(aspectRatio).
                build();
        CameraAPIClient.Callback callback = new CameraAPIClient.Callback() {
            @Override
            public void onCameraOpened() {
                Log.i(TAG, "onCameraOpened");
            }

            @Override
            public void onAspectRatioAvailable(AspectRatio desired, AspectRatio chosen, List<Size> availableSizes) {
                Log.i(TAG, "onAspectRatio: desired "+ desired + ", chosen: " + chosen + ", sizes" + availableSizes);
            }

            @Override
            public void onCameraClosed() {
                Log.i(TAG, "onCameraClosed");

            }

            @Override
            public void onPhotoTaken(byte[] data) {
                Log.i(TAG, "onPhotoTaken data length: " + data.length);
                AngleA = getDirection();//taking x value
                AngleA = Math.toRadians(90)-AngleA;
                D = Double.valueOf(ThreeDForm.format(Math.abs(h*(Math.tan((AngleA))))));
                btnCamera.setVisibility(View.VISIBLE);
                btnMeasure.setVisibility(View.VISIBLE);
                Toast toast = Toast.makeText(getApplicationContext(), "Object distance calculated! " + D, Toast.LENGTH_SHORT);
                toast.show();
            }

            @Override
            public void onBitmapProcessed(Bitmap bitmap) {
                Log.i(TAG, "onBitmapProcessed dimensions: " + bitmap.getWidth() + ", " + bitmap.getHeight());
                // save the image for testing
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "JPEG_" + timeStamp + "_";
                File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                FileOutputStream fos = null;
                File image;
                ImageSurface imageSurface ;
                try {
                    image = File.createTempFile(
                            imageFileName,  /* prefix */
                            ".jpg",         /* suffix */
                            storageDir      /* directory */
                    );


                    Log.i("STORAGE", image.getAbsolutePath());
                    fos = new FileOutputStream(image);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    imageSurface = new ImageSurface(container.getContext(),bitmap);
                    container.removeAllViews();
                    container.addView(imageSurface);
                    container.addView(drawView);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        };

        apiClient.start(R.id.container, callback);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        launchCamera();
    }

    // https://stackoverflow.com/questions/7575921/illegalstateexception-can-not-perform-this-action-after-onsaveinstancestate-wit?rq=1
    // https://stackoverflow.com/questions/7469082/getting-exception-illegalstateexception-can-not-perform-this-action-after-onsa?rq=1
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //No call for super(). Bug on API Level > 11.
    }

    @Override
    protected void onResume() {
        super.onResume();

        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_UI);
    }
    @Override
    public void onDestroy() {
        apiClient.stop();
        mSensorManager.unregisterListener(this);
        super.onDestroy();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch(event.sensor.getType())
        {
            case Sensor.TYPE_ACCELEROMETER:
                mGravity = event.values.clone();
                //onAccelerometerChanged(values[0],values[1],values[2]);
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                mMagnetic = event.values.clone();
                break;
            case Sensor.TYPE_PRESSURE:
                pressure = event.values[0];
                pressure = pressure*100;
            default:
                return;
        }
        if(mGravity != null && mMagnetic != null)
        {
            getDirection();
        }
    }

    private float getDirection()
    {
        float[] temp = new float[9];
        float[] R = new float[9];

        //Load rotation matrix into R
        SensorManager.getRotationMatrix(temp, null, mGravity, mMagnetic);

        //Remap to camera's point-of-view
        SensorManager.remapCoordinateSystem(temp, SensorManager.AXIS_X, SensorManager.AXIS_Z, R);

        //Return the orientation values

        SensorManager.getOrientation(R, value);

        //value[0] - Z, value[1]-X, value[2]-Y in radians

        return value[1];       //return x
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void showResult(){
        if(objLength != -1) {
            DecimalFormat decimalFormat = new DecimalFormat("#.##");

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(getResources().getString(R.string.result_lbl) + decimalFormat.format(objLength));
            builder.create().show();
        }
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        finishAffinity();
        System.exit(0);
    }
}
