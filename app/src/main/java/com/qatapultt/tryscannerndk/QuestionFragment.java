package com.qatapultt.tryscannerndk;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.qatapultt.tryscannerndk.databinding.FragmentQuestionBinding;
import com.qatapultt.tryscannerndk.models.QCode;

public class QuestionFragment extends Fragment {

    private FragmentQuestionBinding binding;
    private MainViewModel mainViewModel;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentQuestionBinding.inflate(inflater, container, false);
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonGotoScanner.setOnClickListener(v -> {
                mainViewModel.uniqueResponses.clear();
                NavHostFragment.findNavController(QuestionFragment.this)
                            .navigate(R.id.action_FirstFragment_to_SecondFragment);

            }
        );
    }

    private static final char[] CHOICES = {'N', 'A', 'B', 'C', 'D', 'E', 'F'};
    private static final float TOLERANCE = 0.35f;

    @Override
    public void onResume() {
        super.onResume();
        StringBuilder sb = new StringBuilder();
        for (QCode code : mainViewModel.uniqueResponses.values()) {
            float radian = (code.orientation + 45)/90f;
            int orientation = Math.round(radian);
            if (Math.abs(radian - orientation) > TOLERANCE) {
                orientation = -1;
            }
            sb.append("(")
                .append(code.id)
                .append(", ")
                .append(CHOICES[(orientation > 3 ? 0: orientation) + 1])
                .append(")\n ");
        }
        binding.textviewResponse.setText(sb.toString());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}