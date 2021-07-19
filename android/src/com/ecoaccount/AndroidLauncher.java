package com.ecoaccount;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.ecoaccount.barcode.BarcodeGraphicTracker;
import com.ecoaccount.barcode.BarcodeTrackerFactory;
import com.ecoaccount.barcode.ui.CameraSource;
import com.ecoaccount.barcode.ui.CameraSourcePreview;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class AndroidLauncher extends AndroidApplication implements Platform, BarcodeGraphicTracker.BarcodeUpdateListener {

    private Main main;
    private View mainView, scanView;
    private RelativeLayout mainLayout;
    private CameraSource mCameraSource;
    private CameraSourcePreview mPreview;

    private static final int RC_HANDLE_GMS = 9001;
    private static final int RC_HANDLE_CAMERA_PERM = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

        main = new Main(this);
        mainView = initializeForView(main, config);

        mainLayout = new RelativeLayout(this);
        mainLayout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        mainLayout.addView(mainView, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));

        scanView = getLayoutInflater().inflate(R.layout.barcode, mainLayout);
        mPreview = (CameraSourcePreview) scanView.findViewById(R.id.preview);
        mPreview.getLayoutParams().height = 540;
        mPreview.getLayoutParams().width = 540;
        mPreview.setVisibility(View.GONE);

        setContentView(mainLayout);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String cameraPermission = Manifest.permission.CAMERA;
            if (checkSelfPermission(cameraPermission) != PackageManager.PERMISSION_GRANTED) requestPermissions(new String[]{cameraPermission}, 1);
        }
    }

    @Override
    public void onBackPressed() {
        main.backPressed();
    }

    @Override
    public void showScanner() {
        runOnUiThread(() -> {
            createCameraSource(false, false);
            startCameraSource();
            mPreview.setVisibility(View.VISIBLE);
        });
    }

    @Override
    public void hideScanner() {
        runOnUiThread(() -> {
            if (mPreview != null) {
                mPreview.setVisibility(View.GONE);
                mPreview.stop();
            }
        });
    }

    @Override
    public void setAutofocus(boolean focus) {
        runOnUiThread(() -> mCameraSource.setFocusMode(focus ? Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE : null));
    }

    @Override
    public void setFlashLight(boolean flash) {
        runOnUiThread(() -> mCameraSource.setFlashMode(flash ? Camera.Parameters.FLASH_MODE_TORCH : null));
    }

    @Override
    public void setScannerBounds(int left, int top, int width, int height) {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mPreview.getLayoutParams();
        layoutParams.leftMargin = left;
        layoutParams.topMargin = top;
        layoutParams.width = width;
        layoutParams.height = height;
    }

    @Override
    public void onBarcodeDetected(Barcode barcode) {
        runOnUiThread(() -> {
            hideScanner();
            main.scanPage.scanned(barcode.displayValue);
        });


    }

    @SuppressLint("InlinedApi")
    private void createCameraSource(boolean autoFocus, boolean useFlash) {
        Context context = getApplicationContext();

        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(context).build();
        BarcodeTrackerFactory barcodeFactory = new BarcodeTrackerFactory(this);
        barcodeDetector.setProcessor(new MultiProcessor.Builder<>(barcodeFactory).build());

        if (!barcodeDetector.isOperational()) {
            IntentFilter lowstorageFilter = new IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW);
            boolean hasLowStorage = registerReceiver(null, lowstorageFilter) != null;
        }

        // Creates and starts the camera.  Note that this uses a higher resolution in comparison
        // to other detection examples to enable the barcode detector to detect small barcodes
        // at long distances.
        CameraSource.Builder builder = new CameraSource.Builder(getApplicationContext(), barcodeDetector)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedPreviewSize(1024, 1024)
                .setRequestedFps(60.0f);

        // make sure that auto focus is an available option
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            builder = builder.setFocusMode(autoFocus ? Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE : null);
        }
        mCameraSource = builder.setFlashMode(useFlash ? Camera.Parameters.FLASH_MODE_TORCH : null).build();
    }

    private void startCameraSource() throws SecurityException {
        if (mCameraSource != null) {
            try {
                mPreview.start(mCameraSource);
            } catch (IOException e) {
                mCameraSource.release();
                mCameraSource = null;
            }
        }
    }
}
