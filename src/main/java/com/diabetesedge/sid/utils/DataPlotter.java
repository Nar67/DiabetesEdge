package com.diabetesedge.sid.utils;

import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;

public class DataPlotter extends ApplicationFrame
{

    public DataPlotter(final String title, final List<Integer> array)
    {

        super(title);
        final XYSeries series = new XYSeries("Blood Glucose");
        int x = 0;
        for (Integer i : array)
        {
            series.add(++x, i);
        }
        final XYSeriesCollection data = new XYSeriesCollection(series);
        final JFreeChart chart = ChartFactory.createXYLineChart("Blood glucose levels", "Time",
            "Glucose Level", data,
            PlotOrientation.VERTICAL, true, true, false);

        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(chartPanel);

    }
}
