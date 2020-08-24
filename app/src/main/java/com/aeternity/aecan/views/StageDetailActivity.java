package com.aeternity.aecan.views;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.aeternity.aecan.R;
import com.aeternity.aecan.adapters.TabsViewPagerAdapter;
import com.aeternity.aecan.databinding.ActivityStageDetailBinding;
import com.aeternity.aecan.helpers.DialogHelper;
import com.aeternity.aecan.helpers.DialogInterface;
import com.aeternity.aecan.models.Beacon;
import com.aeternity.aecan.models.Item;
import com.aeternity.aecan.models.Lot;
import com.aeternity.aecan.models.Stage;
import com.aeternity.aecan.models.Tab;
import com.aeternity.aecan.network.responses.StageDetailResponse;
import com.aeternity.aecan.persistence.SessionPersistence;
import com.aeternity.aecan.util.Constants;
import com.aeternity.aecan.viewModels.StageDetailViewModel;
import com.aeternity.aecan.views.base.ToolbarActivity;
import com.aeternity.aecan.views.fragments.HeaderStageInformationFragment;
import com.aeternity.aecan.views.fragments.components.TabFragment;
import com.aeternity.aecan.views.modal.DoubleButtonModalFragment;
import com.aeternity.aecan.views.modal.MultipleSelectModalFragment;
import com.aeternity.aecan.views.modal.SingleButtonModalFragment;

import java.util.ArrayList;
import java.util.Arrays;


public class StageDetailActivity extends ToolbarActivity implements TabLayout.OnTabSelectedListener, TabLayoutMediator.TabConfigurationStrategy,
        DialogInterface {

    private ActivityStageDetailBinding binding;
    private StageDetailViewModel viewModel;
    private ArrayList<TabFragment> fragmentTabs;
    private HeaderStageInformationFragment headerStageInformationFragment;
    private int lastTabSelected = 0;

    public enum Actions {
        FINISH_STAGE,
        SELECT_BEACONS
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = putContentView(R.layout.activity_stage_detail);
        viewModel = ViewModelProviders.of(this).get(StageDetailViewModel.class);
        binding.setStageDetailViewModel(viewModel);
        binding.tabLayout.addOnTabSelectedListener(this);
        if (getIntent().hasExtra(Constants.STAGE_KEY) && getIntent().hasExtra(Constants.LOT_KEY)) {
            viewModel.setLot((Lot) getIntent().getExtras().getSerializable(Constants.LOT_KEY));
            viewModel.setStage((Stage) getIntent().getExtras().getSerializable(Constants.STAGE_KEY));
            getStageDetailFromServer();
            SessionPersistence.subscribe(this);
            observerStageDetail();
        }
    }

    public void getStageDetailFromServer() {
        viewModel.getStageDetailFromServer();

        try {//Try to save last tab
            lastTabSelected = binding.tabLayout.getSelectedTabPosition();
        } catch (NullPointerException ignored) {
        }

        showActivityOverlay();
    }

    private void observerStageDetail() {

        viewModel.getOnAssignBeaconsFail().observe(this, fail -> {
            hideActivityOverlay();
            getStageDetailFromServer();
        });

        viewModel.getOnAssignBeaconsSuccess().observe(this, success -> {
            hideActivityOverlay();
            getStageDetailFromServer();
        });

        viewModel.getOnGetStageDetailSuccessData().observe(this, onGetStageDetailSuccess -> {

            setToolbarTitle(viewModel.getStageDetailresponse().get().getStageName());
            headerStageInformationFragment = HeaderStageInformationFragment.newInstance(viewModel.getLot(),
                    viewModel.getStageDetailresponse().get().getEndDate(),
                    onGetStageDetailSuccess.getResponse().getAdmitsBeacons(),
                    viewModel.getStageDetailresponse().get().isFinished());
            putFragment(headerStageInformationFragment, R.id.headerLot);

            headerStageInformationFragment.getOnAction().observe(this, action -> {
                switch (action) {
                    case FINISH_STAGE:
                        showActivityOverlay();
                        viewModel.checkFinishStage();
                        break;
                    case SELECT_BEACONS:
                        showSelectBeacons();
                        break;
                }
            });

            setUpTabs(createTabsFragment(!onGetStageDetailSuccess.getResponse().isFinished(), onGetStageDetailSuccess.getResponse().getTabs()));
            hideActivityOverlay();
        });
        viewModel.getOnGetStageDetailFailData().observe(this, onGerStageDetailFail -> {
            getStageDetailFromServer();
            hideActivityOverlay();
        });


        viewModel.getOnAskedFinishedStageSuccessData().observe(this, onAskedFinishedStageSuccess -> {
            hideActivityOverlay();
            if (onAskedFinishedStageSuccess.getAskFinishedStageResponse().isCanFinished()) {
                DoubleButtonModalFragment doubleButtonModalFragment = new DoubleButtonModalFragment(onAskedFinishedStageSuccess.getAskFinishedStageResponse().getMessage(), new ArrayList<>());
                doubleButtonModalFragment.getRightButtonPressed().observe(this, aBoolean -> viewModel.finishStage());
                doubleButtonModalFragment.show(getSupportFragmentManager(), "doubleButtonModal");
            } else {
                SingleButtonModalFragment singleButtonModalFragment = new SingleButtonModalFragment(onAskedFinishedStageSuccess.getAskFinishedStageResponse().getMessage(), new ArrayList<>());
                singleButtonModalFragment.show(getSupportFragmentManager(), "singleButtonModal");
            }

        });

        viewModel.getOnAskedFinishedStageFailData().observe(this, onAskedFinishedStageFail -> {
            hideActivityOverlay();
            SingleButtonModalFragment dialogFragment = new SingleButtonModalFragment(onAskedFinishedStageFail.getErrorResponse().getError(), new ArrayList<>());
            dialogFragment.show(getSupportFragmentManager(), "singleButtonModal");

        });

        viewModel.getOnFinishedStageSuccessData().observe(this, onFinishedStageSuccess -> {
            hideActivityOverlay();
            onBackPressed();

        });
        viewModel.getOnFinishedStageFailData().observe(this, onFinishedStageFail -> {
            hideActivityOverlay();
            Toast.makeText(this, onFinishedStageFail.getErrorResponse().getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    public void generateItemTabBarwithColor(View view, int color) {
        TextView title = view.findViewById(R.id.textViewTabTitle);
        title.setTextColor(getResources().getColor(color));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);

    }

    public void createTabs(ArrayList<TabFragment> tabs) {
        TabsViewPagerAdapter adapter = new TabsViewPagerAdapter(tabs, getSupportFragmentManager(), getLifecycle());
        binding.viewPagerContainer.setOffscreenPageLimit(tabs.size() + 1);
        binding.viewPagerContainer.setAdapter(adapter);
        try {
            binding.viewPagerContainer.setCurrentItem(lastTabSelected);
        } catch (IndexOutOfBoundsException ignored) {
        }
    }

    public ArrayList<TabFragment> createTabsFragment(boolean editable, ArrayList<Tab> tabs) {
        fragmentTabs = new ArrayList<>();
        for (Tab tab : tabs) {
            fragmentTabs.add(TabFragment.newInstance(editable, tab.getComponents(), tab.getBadge(), tab.getTitle(), tab.getEmptyMessage()));
        }
        return fragmentTabs;
    }

    public void setUpTabs(ArrayList<TabFragment> tabs) {
        createTabs(tabs);
        new TabLayoutMediator(binding.tabLayout, binding.viewPagerContainer, this).attach();


    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        generateItemTabBarwithColor(tab.view, R.color.lightBlue);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        generateItemTabBarwithColor(tab.view, R.color.lighterGray);

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        generateItemTabBarwithColor(tab.view, R.color.lightBlue);

    }

    @Override
    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_tab_section, null);
        TextView badge = view.findViewById(R.id.textViewBadge);
        TextView title = view.findViewById(R.id.textViewTabTitle);
        TabFragment tabFragment = fragmentTabs.get(position);

        if (tabFragment.getBadge() != null && !tabFragment.getBadge().isEmpty()) {
            badge.setText(tabFragment.getBadge());
            badge.setVisibility(View.VISIBLE);
        } else {
            badge.setVisibility(View.GONE);
        }
        title.setText(tabFragment.getTitle());
        tab.parent.setPadding(30, 0, 30, 0);
        tab.setCustomView(view);

    }

    @Override
    public void onDialogSuccessRequest() {
        getStageDetailFromServer();
    }

    @Override
    public void onDialogFailRequest(String message) {
        runOnUiThread(() -> {

            DialogHelper.showStaticErrorDialog(this, message, "Aceptar", null).show();

           // final Toast toast = Toast.makeText(StageDetailActivity.this, message, Toast.LENGTH_SHORT);
            //toast.show();
        });
    }


    private void showSelectBeacons() {
        try {

            StageDetailResponse detail = viewModel.getOnGetStageDetailSuccessData().getValue().getResponse();
            ArrayList<Item> items = new ArrayList<>(detail.getBeaconsOptions());

            for (Item item : items) {
                item.setSelected(false);
            }

            for (Beacon beacon : detail.getBeacons()) {
                for (Item item : items) {
                    if (beacon.getId().equals(item.getId())) {
                        item.setSelected(true);
                    }
                }
            }

            MultipleSelectModalFragment multipleSelectModalFragment = MultipleSelectModalFragment.newInstance(getString(R.string.select_beacons),
                    getString(R.string.add), items, "");

            multipleSelectModalFragment.getBeaconsIds().observe(this, this::onAddMultipleSelection);

            multipleSelectModalFragment.show(getSupportFragmentManager(), "multipleSelectModalFragment");

        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }

    }

    private void onAddMultipleSelection(ArrayList<Integer> beacons) {
        showActivityOverlay();
        viewModel.updateBeacons(beacons);
        System.out.println(Arrays.toString(beacons.toArray()));
    }


}
