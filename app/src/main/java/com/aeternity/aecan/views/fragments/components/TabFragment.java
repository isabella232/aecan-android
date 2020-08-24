package com.aeternity.aecan.views.fragments.components;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.aeternity.aecan.R;
import com.aeternity.aecan.databinding.FragmentTabBinding;
import com.aeternity.aecan.models.dynamic.Component;
import com.aeternity.aecan.views.fragments.base.BaseFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class TabFragment extends BaseFragment {

    private static final String COMPONENTS_KEY = "components";
    private FragmentTabBinding binding;
    private String badge;
    private String title;
    private String emptyMessage;
    private boolean isEditable;

    public TabFragment() {
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tab, container, false);

        if (getArguments() != null && getArguments().containsKey(COMPONENTS_KEY)) {
            ArrayList<Component> components = (ArrayList<Component>) getArguments().get(COMPONENTS_KEY);

            if ((components == null || components.isEmpty())) {

                binding.textMessage.setVisibility(View.VISIBLE);
                if (emptyMessage != null && !emptyMessage.isEmpty())
                    binding.textMessage.setText(emptyMessage);
                else
                    binding.textMessage.setText(R.string.without_elements);

            } else {
                for (Component component : components) {

                    Fragment fragmentComponent = component.makeFragment(isEditable);
                    if (fragmentComponent != null) {
                        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                        ft.add(binding.fragmentContainer.getId(), fragmentComponent, component.getTag());
                        ft.commit();
                    }
                }
            }
        }

        return binding.rootView;
    }

    public static TabFragment newInstance(boolean editable, ArrayList<Component> components, String badge, String title, String emptyMessage) {
        TabFragment frag = new TabFragment();
        Bundle args = new Bundle();
        args.putSerializable(COMPONENTS_KEY, components);
        frag.badge = badge;
        frag.title = title;
        frag.emptyMessage = emptyMessage;
        frag.isEditable = editable;
        frag.setArguments(args);
        return frag;
    }

    public String getBadge() {
        return badge;
    }

    public String getTitle() {
        return title;
    }

}
