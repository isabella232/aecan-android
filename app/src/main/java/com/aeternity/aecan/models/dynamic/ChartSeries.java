package com.aeternity.aecan.models.dynamic;

import com.github.mikephil.charting.data.Entry;
import com.aeternity.aecan.models.Item;

import java.util.ArrayList;

public class ChartSeries implements Item {

    private String label;
    private boolean selected;
    private ArrayList<ChartEntry> entries;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public ArrayList<ChartEntry> getEntries() {
        return entries;
    }

    public void setEntries(ArrayList<ChartEntry> entries) {
        this.entries = entries;
    }

    @Override
    public String getText() {
        return label;
    }

    @Override
    public Boolean isSelected() {
        return selected;
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public Integer getId() {
        return 0;
    }

    public ChartSeries(String label, boolean selected) {
        this.label = label;
        this.selected = selected;
    }

    public ChartSeries() {
    }

    public ArrayList<Entry> getEntriesData() {
        ArrayList<Entry> values = new ArrayList<>();
        for (ChartEntry entry : this.entries) {
            values.add(new Entry(entry.getIndex(), (float) entry.getValue(), entry.getLabel()));
        }

        return values;
    }
}
