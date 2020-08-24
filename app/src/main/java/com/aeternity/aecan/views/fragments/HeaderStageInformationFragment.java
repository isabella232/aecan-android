package com.aeternity.aecan.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.aeternity.aecan.R;
import com.aeternity.aecan.databinding.FragmentStageHeaderInformationBinding;
import com.aeternity.aecan.models.Lot;
import com.aeternity.aecan.util.Constants;
import com.aeternity.aecan.util.SingleLiveEvent;
import com.aeternity.aecan.viewModels.HeaderStageInformationViewModel;
import com.aeternity.aecan.views.StageDetailActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class HeaderStageInformationFragment extends Fragment {
    private HeaderStageInformationViewModel viewModel;
    private FragmentStageHeaderInformationBinding binding;
    private SingleLiveEvent<StageDetailActivity.Actions> onAction = new SingleLiveEvent<>();

    public HeaderStageInformationFragment() {
        // Required empty public constructor
    }

    public enum Actions {
        FINISH_STAGE,
        SELECT_BEACONS
    }

    public static HeaderStageInformationFragment newInstance(Lot lot, String endDate, boolean admitsBeacons, boolean isFinished) {
        HeaderStageInformationFragment frag = new HeaderStageInformationFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constants.LOT_KEY, lot);
        args.putString(Constants.END_DATE_KEY, endDate);
        args.putBoolean(Constants.ADMITS_BEACONS_KEY, admitsBeacons);
        args.putBoolean(Constants.IS_FINISHED_KEY, isFinished);

        frag.setArguments(args);
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_stage_header_information, container, false);
        viewModel = ViewModelProviders.of(this).get(HeaderStageInformationViewModel.class);
        if (getArguments() != null) viewModel.setArguments(getArguments());
        binding.setHeaderStageInformationViewModel(viewModel);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpObserver();
    }

    private void setUpObserver() {
        viewModel.getFragmentAction().observe(this, action -> {

            switch (action) {
                case FINISH_STAGE:
                    getOnAction().setValue(StageDetailActivity.Actions.FINISH_STAGE);
                    break;
                case SELECT_BEACONS:
                    getOnAction().setValue(StageDetailActivity.Actions.SELECT_BEACONS);
                    break;
            }
        });
    }

    public SingleLiveEvent<StageDetailActivity.Actions> getOnAction() {
        return onAction;
    }

}
