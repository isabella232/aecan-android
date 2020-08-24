package com.aeternity.aecan.models.dynamic;

public class ChartEntry {
    /**
     * index : 0
     * value : 25.5
     * label : 15/07/2019 - 09:25
     */

    private int index;
    private double value;
    private String label;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public ChartEntry(int index, double value, String label) {
        this.index = index;
        this.value = value;
        this.label = label;
    }

    public ChartEntry() {
    }




}
