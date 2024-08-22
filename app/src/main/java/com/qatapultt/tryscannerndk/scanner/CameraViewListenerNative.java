package com.qatapultt.tryscannerndk.scanner;

import android.util.Log;

import com.qatapultt.scanner.QCodeScanner;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class CameraViewListenerNative implements CameraBridgeViewBase.CvCameraViewListener2 {
    private static final String TAG = "CameraViewListener >>>> ";
    public int[] ids;
    public int[] orientations;
    public QCodeScanner qCodeScanner;
    public Mat rgb;
    public Mat localrgb;
    private OnQCodeDetectedListener onQCodeDetectedListener;

    public CameraViewListenerNative(OnQCodeDetectedListener onQCodeDetectedListener) {
        this.onQCodeDetectedListener = onQCodeDetectedListener;
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        rgb = new Mat();
        localrgb = new Mat();
        qCodeScanner = new QCodeScanner();
        qCodeScanner.activate("DKrnCKrnCaDoDKzoDuyR");
    }

    @Override
    public void onCameraViewStopped() {
        localrgb.release();
        rgb.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        // convert the RGBA format to RGB storing the result in `rgb` param
        Imgproc.cvtColor(inputFrame.rgba(), rgb, Imgproc.COLOR_RGBA2RGB);
        // rotate the localRGB so that the pic is visible in Portrait mode also
        // and store the result in rgb:
        int[][] result = qCodeScanner.detectMarkers(rgb.nativeObj);
        if (result != null && onQCodeDetectedListener != null) {
            updateDetections(result);
        }
        Core.rotate(rgb, localrgb, Core.ROTATE_90_CLOCKWISE);
        Imgproc.resize(localrgb, rgb, rgb.size());
        return rgb;
    }

    private void updateDetections(int[][] result) {
        ids = new int[result.length];
        orientations = new int[result.length];
        for (int i = 0; i < result.length; i++) {
            ids[i] = result[i][0];
            orientations[i] = result[i][1];
            Log.d(TAG, "updateDetections: " + ids[i] + "; " + orientations[i]);
        }

        if (onQCodeDetectedListener != null) {
            onQCodeDetectedListener.onQCodeDetected(ids, orientations);
        }
    }

    public void setOnQCodeDetectedListener(OnQCodeDetectedListener listener) {
        this.onQCodeDetectedListener = listener;
    }

    public interface OnQCodeDetectedListener {
        void onQCodeDetected(int[] ids, int[] orientations);
    }
}
