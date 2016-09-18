package com.example.cancerreport.mockdata;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MockData {
	
	private static final int NUM_DAYS = 30;
	
	private static final int MAX_NUM = 100;
	
	private static final int MIN_NUM = 50;
	
	public static List<Float> getXPoints() {
		List<Float> xPoints = new ArrayList<>();
		
		for (float i = 0; i < NUM_DAYS; i++) {
			xPoints.add(i);
		}
		
		return xPoints;
	}
	
	public static List<Float> getYPoints() {
		List<Float> yPoints = new ArrayList<>();
		Random rn = new Random();
		
		for (int i = 0; i < NUM_DAYS; i++) {
			int randomData = rn.nextInt(MAX_NUM - MIN_NUM + 1) + MIN_NUM;
			yPoints.add((float) randomData);
		}
		
		return yPoints;
	}

}
