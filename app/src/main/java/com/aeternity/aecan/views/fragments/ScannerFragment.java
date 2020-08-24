package com.aeternity.aecan.views.fragments;

import android.Manifest;
import android.animation.ValueAnimator;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.aeternity.aecan.R;
import com.aeternity.aecan.databinding.FragmentScanQrBinding;
import com.aeternity.aecan.scanner.BarcodeScanningProcessor;
import com.aeternity.aecan.scanner.CameraSource;
import com.aeternity.aecan.scanner.CameraSourcePreview;
import com.aeternity.aecan.scanner.GraphicOverlay;
import com.aeternity.aecan.util.gson.DisplayUtils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class ScannerFragment extends Fragment implements BarcodeScanningProcessor.BarcodeScanningProcessorListener {

    private FragmentScanQrBinding binding;
    private static final String TAG = "QRScanFragment";
    private CameraSource cameraSource = null;
    private CameraSourcePreview preview;
    private GraphicOverlay graphicOverlay;
    private String selectedModel = "Barcode Detection";
    private float initialPosY = 0;
    private ValueAnimator animationScanning = new ValueAnimator();
    private MutableLiveData<Bitmap> cameraSourceBitmap = new MutableLiveData<>();
    private MutableLiveData<FirebaseVisionBarcode> qrCodeScanned = new MutableLiveData<>();
    private MutableLiveData<String> permissionGrant = new MutableLiveData<>();


    public ScannerFragment() {
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(this.getLayoutInflater(), R.layout.fragment_scan_qr, container, false);
        binding.setVariable(com.aeternity.aecan.BR.scanFragment, this);

        preview = binding.firePreview;
        if (preview == null) {
            Log.d(TAG, "Preview is null");
        }
        graphicOverlay = binding.fireFaceOverlay;
        if (graphicOverlay == null) {
            Log.d(TAG, "graphicOverlay is null");
        }

        preview.stop();
        if (allPermissionsGranted()) {
            initCameraSource();
            startCameraSource();
        } else {
            getRuntimePermissions();
        }

        return binding.getRoot();
    }

    private boolean allPermissionsGranted() {
        return ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private void getRuntimePermissions() {
        permissionGrant.postValue(Manifest.permission.CAMERA);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        animateScan();
    }

    private void startCameraSource() {
        if (cameraSource != null) {
            try {
                if (preview == null) {
                    Log.d(TAG, "resume: Preview is null");
                }
                if (graphicOverlay == null) {
                    Log.d(TAG, "resume: graphOverlay is null");
                }
                preview.start(cameraSource, graphicOverlay);
            } catch (IOException e) {
                Log.e(TAG, "Unable to start camera source.", e);
                cameraSource.release();
                cameraSource = null;
            }
        }
    }


    @Override
    public void onPause() {
        stopCamera();
        super.onPause();
    }


    @Override
    public void onResume() {
        super.onResume();
        initCameraSource();
    }

    public void stopCamera() {
        preview.stop();
        try {
            cameraSource.release();
            cameraSource = null;
        } catch (NullPointerException ignore) {
        }
    }

    private void createCameraSource(String model) {
        // If there's no existing cameraSource, create one.
        if (cameraSource == null) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            this.getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            com.google.android.gms.common.images.Size viewSize = new com.google.android.gms.common.images.Size(640, 480);
            cameraSource = new CameraSource(this.getActivity(), graphicOverlay, viewSize);
        }
        try {
            Log.i(TAG, "Using Barcode Detector Processor");
            cameraSource.setMachineLearningFrameProcessor(new BarcodeScanningProcessor(this));

        } catch (Exception e) {
            Log.e(TAG, "Can not create image processor: " + model, e);
            Toast.makeText(
                    this.getActivity().getApplicationContext(),
                    "Can not create image processor: " + e.getMessage(),
                    Toast.LENGTH_LONG)
                    .show();
        }
    }

    private void animateScan() {
        animationScanning = ValueAnimator.ofFloat(DisplayUtils.convertDpToPixel(120, getContext()));
        animationScanning.setDuration(2000).setInterpolator(new AccelerateDecelerateInterpolator());
        animationScanning.setRepeatMode(ValueAnimator.REVERSE);
        animationScanning.setRepeatCount(ValueAnimator.INFINITE);
        binding.viewScanAnimated.postDelayed(() -> initialPosY = binding.viewScanAnimated.getY(), 100);

        animationScanning.setStartDelay(100);
        animationScanning.addUpdateListener(animation -> binding.viewScanAnimated.setY(initialPosY + (float) animation.getAnimatedValue()));

        animationScanning.start();

    }


    @Override
    public void onProductCodeDetected(FirebaseVisionBarcode barcode, Bitmap originalCameraImage) {
        preview.stop();
        try {
            cameraSource.release();
            cameraSource = null;
            cameraSourceBitmap.postValue(originalCameraImage);
            qrCodeScanned.postValue(barcode);
            binding.viewScanAnimated.setAlpha(0f);

        } catch (NullPointerException ignore) {
        }
    }

    public void initCameraSource() {
        if (cameraSource == null) {
            createCameraSource(selectedModel);
            startCameraSource();
            binding.viewScanAnimated.setAlpha(1f);
        }
    }



    public MutableLiveData<String> getPermissionGrant() {
        return permissionGrant;
    }

    public MutableLiveData<FirebaseVisionBarcode> getQrCodeScanned() {
        return qrCodeScanned;
    }

    public MutableLiveData<Bitmap> getCameraSourceBitmap() {
        return cameraSourceBitmap;
    }
}
