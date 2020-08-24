package com.aeternity.aecan.customComponents;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;

import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.renderer.LineChartRenderer;
import com.github.mikephil.charting.utils.MPPointD;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.Locale;

public class CustomLinearRenderer extends LineChartRenderer {
    public CustomLinearRenderer(LineDataProvider chart, ChartAnimator animator,
                                ViewPortHandler viewPortHandler, float textSize,
                                String suffix, Typeface fontDataY, Typeface fontDataX) {
        super(chart, animator, viewPortHandler);
        this.textSize = textSize;
        this.textSuffix = suffix;
        this.fontDataY = fontDataY;
        this.fontDataX = fontDataX;

    }

    private float textSize;
    private String textSuffix;
    private Typeface fontDataY;
    private Typeface fontDataX;

    @Override
    public void drawHighlighted(Canvas c, Highlight[] indices) {

        LineData lineData = mChart.getLineData();

        for (Highlight high : indices) {

            ILineDataSet set = lineData.getDataSetByIndex(high.getDataSetIndex());

            if (set == null || !set.isHighlightEnabled())
                continue;

            Entry e = set.getEntryForXValue(high.getX(), high.getY());

            String textX = e.getData().toString();

            if (!isInBoundsX(e, set))
                continue;

            MPPointD pix = mChart.getTransformer(set.getAxisDependency()).getPixelForValues(e.getX(), e.getY() * mAnimator
                    .getPhaseY());

            high.setDraw((float) pix.x, (float) pix.y);

            // set color and stroke-width
            mHighlightPaint.setColor(set.getHighLightColor());
            mHighlightPaint.setStrokeWidth(set.getHighlightLineWidth());

            // create vertical path
            Path mHighlightLinePath = new Path();

            mHighlightLinePath.reset();
            mHighlightLinePath.moveTo((float) pix.x, mViewPortHandler.contentTop());
            mHighlightLinePath.lineTo((float) pix.x, mViewPortHandler.contentBottom());

            c.drawPath(mHighlightLinePath, mHighlightPaint);

            Paint paintLabelX = new Paint();
            paintLabelX.setTypeface(fontDataX);
            paintLabelX.setColor(Color.WHITE);
            paintLabelX.setTextSize(textSize);
            paintLabelX.setTextAlign(Paint.Align.RIGHT);

            Paint paintLabelY = new Paint();
            paintLabelY.setColor(Color.parseColor("#4F4F4F"));
            paintLabelY.setTextSize(textSize);
            paintLabelY.setTextAlign(Paint.Align.RIGHT);
            paintLabelY.setTypeface(fontDataY);

            float baseDistanceToLine = (float) pix.x - textSize * 2;
            float baseDistanceLeft = baseDistanceToLine - paintLabelY.measureText(textX) - textSize / 2;
            float baseDistanceRight = baseDistanceToLine + textSize;


            if (mViewPortHandler.getContentRect().left > (baseDistanceToLine - paintLabelX.measureText(textX) - textSize)) {
                System.out.println("no entra en el rect");
                paintLabelX.setTextAlign(Paint.Align.LEFT);
                paintLabelY.setTextAlign(Paint.Align.LEFT);

                baseDistanceToLine = (float) pix.x + textSize * 2;

                baseDistanceLeft = baseDistanceToLine + paintLabelY.measureText(textX) + textSize / 2;
                baseDistanceRight = baseDistanceToLine - textSize;
            }

            c.drawText(
                    String.format(Locale.US, "%.2f %s", high.getY(), textSuffix)
                    , baseDistanceToLine, mViewPortHandler.getContentRect().top + (textSize), paintLabelY);

            Paint paintRect = new Paint();
            paintRect.setColor(Color.parseColor("#F7296E"));

            c.drawRoundRect(new RectF(
                    baseDistanceLeft,
                    (float) (mViewPortHandler.getContentRect().top + (textSize * 1.8)),
                    baseDistanceRight,
                    (float) (mViewPortHandler.getContentRect().top + (textSize * 3.5))), 60, 60, paintRect);

            c.drawText(textX
                    , baseDistanceToLine, mViewPortHandler.getContentRect().top + (textSize * 3), paintLabelX);


        }
    }
}
