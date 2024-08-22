package com.qatapultt.tryscannerndk;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.OpenCVLoader;

import com.qatapultt.tryscannerndk.databinding.FragmentScannerBinding;
import com.qatapultt.tryscannerndk.models.QCode;
import com.qatapultt.tryscannerndk.scanner.CameraViewListenerNative;

public class ScannerFragment extends Fragment {

    private static final String TAG = "ScannerFragment >>>> ";

    private FragmentScannerBinding binding;
    private MainViewModel mainViewModel;
    private boolean isOpenCVLoaded;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentScannerBinding.inflate(inflater, container, false);
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        // load openCV library
        isOpenCVLoaded = OpenCVLoader.initDebug();
        Log.d(TAG, "OpenCV loaded: "+ isOpenCVLoaded);

        if (isOpenCVLoaded) {
            binding.mainCamera.setCameraIndex(CameraBridgeViewBase.CAMERA_ID_BACK);
            binding.mainCamera.setCameraPermissionGranted();
            binding.mainCamera.setCvCameraViewListener(
                new CameraViewListenerNative(
                    (ids, orientations) -> {
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < ids.length; i++) {
                            QCode qCode = new QCode(
                                Integer.toString(ids[i]),
                                orientations[i]
                                /*
                                 * Note: Orientations give the raw angle of the marker (from 0 to 359)
                                 * This angle will change depending on the orientation of the device.
                                 */
                            );
                            // The below code is just to update the display with the
                            // detected markers. Feel free to add your business logic here
                            mainViewModel.detectedQueue.add(qCode);
                            sb.append("(").append(ids[i]).append(", ").append(orientations[i]).append(") \n");
                        }
                        requireActivity().runOnUiThread(() -> {
                            binding.tvMarkerDetails.setText(sb.toString());
                        });
                    }
                )
            );
        }

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.submitButton.setOnClickListener(v -> {
                    mainViewModel.processResponses();
                    NavHostFragment.findNavController(ScannerFragment.this)
                            .navigate(R.id.action_SecondFragment_to_FirstFragment);
                }
        );
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isOpenCVLoaded) {
            binding.mainCamera.enableView();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (isOpenCVLoaded) {
            binding.mainCamera.disableView();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}