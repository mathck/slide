package com.gmail.mathck;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

//----------------------------------------------------------------------
//	draw Line (many pixels)
//----------------------------------------------------------------------
public class CLine {

	Paint m_paint;
	float m_pts_x[];
	float m_pts_y[];
	int m_counter = 0;
	float m_radius = 0;
	
	public CLine(float radius) {
		m_paint = new Paint();
		m_paint.setColor(Color.argb(100, 255, 255, 255));
		m_pts_x = new float[100];
		m_pts_y = new float[100];
		m_radius = radius;
	}
	
	public void addPoint(float x, float y) {
		if(m_counter < 100) {
			m_pts_x[m_counter] = x;
			m_pts_y[m_counter] = y;
			m_counter++;
		}
	}
	
	public void reset() {
		m_counter = 0;
	}

	public void draw(Canvas canvas) {
		for(int i = 0; i < m_counter; i++) {
			canvas.drawCircle(m_pts_x[i], m_pts_y[i], m_radius, m_paint);
		}
	}
}