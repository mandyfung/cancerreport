package com.example.cancerreport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class ReportGenerator {
	
	private List<Float> xPoints;
	
	private List<Float> yPoints;
	
	public ReportGenerator(List<Float> xPoints, List<Float> yPoints) {
		this.xPoints = xPoints;
		this.yPoints = yPoints;
	}
	
	public static List<Float> createXPoints(Date lastTherapyDate, Date therapyDate) {
		long diff = therapyDate.getTime() - lastTherapyDate.getTime();
	    long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
	    List<Float> xPoints = new ArrayList<>();
	    for (int i = 0; i < days; i++) {
	    	xPoints.add((float) i);
	    }
	    return xPoints;
	}
	
	public void createGraph(GraphView graph) {
		DataPoint[] dataPoints = new DataPoint[xPoints.size()];

		for (int i = 0; i < xPoints.size(); i++) {
			dataPoints[i] = new DataPoint(xPoints.get(i), yPoints.get(i));
		}
		
		LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(dataPoints);
		graph.addSeries(series);
	}

}
