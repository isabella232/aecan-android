package com.aeternity.aecan.views.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.aeternity.aecan.BR;
import com.aeternity.aecan.R;
import com.aeternity.aecan.adapters.ListAdapter;
import com.aeternity.aecan.adapters.StagesAdapter;
import com.aeternity.aecan.databinding.FragmentStageListBinding;
import com.aeternity.aecan.models.Lot;
import com.aeternity.aecan.models.Stage;
import com.aeternity.aecan.views.fragments.base.BaseFragment;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class StageListFragment extends BaseFragment {

    private FragmentStageListBinding binding;
    private StagesAdapter adapter;
    private Lot lot;
    private static final String LOT = "lot";
    private MutableLiveData<Stage> stageData = new MutableLiveData<>();

    public StageListFragment() {
        // Required empty public constructor
    }



    public static StageListFragment newInstance(Lot lot){
        StageListFragment stageListFragment = new StageListFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(LOT,lot);
        stageListFragment.setArguments(bundle);
        return stageListFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_stage_list, container, false);
        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!=null){
            lot = (Lot) getArguments().getSerializable(LOT);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.recyclerViewStage.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new StagesAdapter(lot.getStages());
        binding.recyclerViewStage.setAdapter(adapter);
        adapter.getItemSelected().observe(this, stage -> {
            if (!stage.isPending()) getStageData().postValue(stage);
        });

    }

    public MutableLiveData<Stage> getStageData() {
        return stageData;
    }


}
