package com.aeternity.aecan.models.dynamic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Locale;

public class Chart implements Serializable {

    private String title;
    private String unit;
    private ArrayList<ChartSeries> chartSeries = new ArrayList<>();
    private ArrayList<ChartEntry> entriesList = new ArrayList<>();

    public String getTitle() {
        return title;
    }

    public String getUnit() {
        return unit;
    }

    public ArrayList<ChartSeries> getChartSeries() {
        if (chartSeries == null)
            return new ArrayList<>();
        return chartSeries;
    }

    public ArrayList<ChartEntry> getEntriesList() {
        return entriesList;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setChartSeries(ArrayList<ChartSeries> chartSeries) {
        this.chartSeries = chartSeries;
    }

    public void setEntriesList(ArrayList<ChartEntry> entriesList) {
        this.entriesList = entriesList;
    }

    public ArrayList<DynamicItem> getEntriesAsNameValue() {

        ArrayList<DynamicItem> valueItems = new ArrayList<>();

        for (ChartEntry entry : entriesList) {
            valueItems.add(new DynamicItem(entry.getLabel() + ":",
                    String.format(Locale.US, "%.2f %s", entry.getValue(), unit)));
        }

        return valueItems;

    }
}
