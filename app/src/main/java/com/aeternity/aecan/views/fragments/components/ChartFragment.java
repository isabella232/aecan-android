package com.aeternity.aecan.views.fragments.components;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.MarkerImage;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.aeternity.aecan.BR;
import com.aeternity.aecan.R;
import com.aeternity.aecan.adapters.ListAdapter;
import com.aeternity.aecan.customComponents.CustomLinearRenderer;
import com.aeternity.aecan.databinding.ComponentChartBinding;
import com.aeternity.aecan.models.dynamic.Chart;
import com.aeternity.aecan.models.dynamic.ChartEntry;
import com.aeternity.aecan.models.dynamic.ChartSeries;
import com.aeternity.aecan.models.dynamic.ItemAction;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Locale;

public class ChartFragment extends Fragment {

    private static final String CHART_TITLE = "title";
    private static final String CHART_SERIES = "series";
    private static final String LIST_ENTRIES = "entries";
    private static final String UNIT = "unit";
    private static final String ACTION = "action";
    private static final String HAS_ACTION = "editable";

    private ComponentChartBinding binding;
    private ListAdapter adapter;
    private MutableLiveData<Boolean> editable = new MutableLiveData<>(true);
    private Chart chart;
    private ItemAction action;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.component_chart, container, false);
        binding.setChartFragment(this);

        if (getArguments() != null &&
                getArguments().containsKey(CHART_TITLE) &&
                getArguments().containsKey(CHART_SERIES) &&
                getArguments().containsKey(ACTION) &&
                getArguments().containsKey(UNIT) &&
                getArguments().containsKey(HAS_ACTION) &&
                getArguments().containsKey(LIST_ENTRIES)) {

            action = (ItemAction) getArguments().getSerializable(ACTION);
            editable.setValue(getArguments().getBoolean(HAS_ACTION));

            chart = new Chart();
            chart.setUnit(getArguments().getString(UNIT));
            chart.setTitle(getArguments().getString(CHART_TITLE));
            chart.setChartSeries((ArrayList<ChartSeries>) getArguments().getSerializable(CHART_SERIES));
            chart.setEntriesList((ArrayList<ChartEntry>) getArguments().getSerializable(LIST_ENTRIES));

            binding.textTitle.setText(chart.getTitle());
            prepareRecyclerForSeries();
            prepareChart(chart);
        }

        return binding.getRoot();
    }


    private void prepareRecyclerForSeries() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);

        binding.recyclerChartOptions.setLayoutManager(layoutManager);
        adapter = new ListAdapter(chart.getChartSeries(), BR.optionName, R.layout.chart_options_item);
        binding.recyclerChartOptions.setAdapter(adapter);

        adapter.getItemSelected().observe(getActivity(), serie -> {
            for (Object chartSeries : adapter.getItems()) {
                if (chartSeries.equals(serie))
                    ((ChartSeries) chartSeries).setSelected(true);
                else
                    ((ChartSeries) chartSeries).setSelected(false);
            }

            adapter.notifyDataSetChanged();
            setData((ChartSeries) serie);
        });
    }

    private void prepareRecyclerForList() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.recyclerChartOptions.setLayoutManager(layoutManager);

        adapter = new ListAdapter(chart.getEntriesAsNameValue(), BR.keyValueItem, R.layout.component_key_value_array);

        binding.recyclerChartOptions.setAdapter(adapter);
    }

    private void prepareChart(Chart chartData) {

        LineChart chart = binding.chart;

        MarkerImage customMarker = new MarkerImage(getContext(), R.drawable.circle_hightlight);
        float offset = -getContext().getResources().getDimension(R.dimen.margin_5);

        CustomLinearRenderer customLinearRenderer = new CustomLinearRenderer(chart,
                chart.getAnimator(),
                chart.getViewPortHandler(),
                getResources().getDimension(R.dimen.text_12),
                chartData.getUnit(),
                ResourcesCompat.getFont(getContext(), R.font.montserrat_bold),
                ResourcesCompat.getFont(getContext(), R.font.montserrat_regular));

        chart.setRenderer(customLinearRenderer);
        customMarker.setOffset(offset, offset);

        chart.getXAxis().setEnabled(false);
        chart.getAxisRight().setEnabled(false);
        chart.getAxisRight().setDrawAxisLine(false);
        chart.getLegend().setEnabled(false);
        chart.setMarker(customMarker);
        chart.getDescription().setEnabled(false);

        chart.setNoDataText(getString(R.string.selectChartData));
        chart.setNoDataTextTypeface(ResourcesCompat.getFont(getContext(), R.font.montserrat_semi_bold));
        chart.setNoDataTextColor(getResources().getColor(R.color.chartColor));

        chart.setScaleEnabled(false);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setDrawGridLines(false);
        leftAxis.setDrawAxisLine(false);

        //TODO Ask if the backend will provide these or the graph will auto adjust
        // leftAxis.setAxisMaximum(25f);
        // leftAxis.setAxisMinimum(20f);

        leftAxis.setTextColor(getContext().getResources().getColor(R.color.lightGrayA6A6A6));
        //leftAxis.setGranularity(0.);
        leftAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.format(Locale.US, "%.2f %s", value, chartData.getUnit());
            }
        });

        try {

            ((ChartSeries) adapter.getItems().get(0)).setSelected(true);
            setData(chartData.getChartSeries().get(0));
            adapter.notifyDataSetChanged();

        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }


    }

    public static ChartFragment newInstance(String title, String unit, ArrayList<ChartSeries> chartSeries, ArrayList<ChartEntry> listEntries, ItemAction componentAction, boolean hasAction) {
        ChartFragment frag = new ChartFragment();
        Bundle args = new Bundle();

        args.putSerializable(LIST_ENTRIES, listEntries);
        args.putSerializable(CHART_SERIES, chartSeries);

        args.putString(CHART_TITLE, title);
        args.putString(UNIT, unit);

        args.putSerializable(ACTION, componentAction);
        args.putBoolean(HAS_ACTION, hasAction);
        frag.setArguments(args);
        return frag;
    }

    private void setData(ChartSeries series) {
        // clear the data displayed
        binding.chart.clear();


        if (series.getEntries() != null && !series.getEntries().isEmpty()) {
            // create a data set and give it a type
            LineDataSet lineDataSet = new LineDataSet(series.getEntriesData(), "");
            lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
            lineDataSet.setColor(getContext().getResources().getColor(R.color.chartColor));
            lineDataSet.setLineWidth(2f);
            lineDataSet.setDrawCircles(false);
            lineDataSet.setDrawValues(false);
            lineDataSet.setHighLightColor(getContext().getResources().getColor(R.color.lightGrayA6A6A6));
            lineDataSet.setHighlightEnabled(true);
            lineDataSet.setHighlightLineWidth(1f);

            if (series.getEntries().size() == 1) {
                lineDataSet.setDrawCircles(true);
                lineDataSet.setCircleHoleRadius(5f);
                lineDataSet.setCircleColor(getContext().getResources().getColor(R.color.chartColor));
            }

            // create a data object with the data sets
            LineData data = new LineData(lineDataSet);

            // set data
            binding.chart.setData(data);
        } else {
            binding.chart.setNoDataText(getString(R.string.no_data_avaiable));
        }

    }

    public void onChartSelected() {
        binding.chart.setVisibility(View.VISIBLE);
        prepareRecyclerForSeries();
    }

    public void onListSelected() {
        binding.chart.setVisibility(View.GONE);
        prepareRecyclerForList();
    }

    public MutableLiveData<Boolean> getEditable() {
        return editable;
    }

    public void onAddPressed() {
        if (editable.getValue() != null && editable.getValue()) {
            DialogFragment dialogFragment = action.getFragment(this);
            if (dialogFragment != null)
                dialogFragment.show(this.getChildFragmentManager(), action.getActionType());
        }

    }
}
