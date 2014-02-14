package com.gmail.mathck;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

public class CProgressBar {

	private Bitmap m_bitmap;
	private Matrix m_matrix;
	private float m_pixel_progress;
	private boolean m_touched;

	public CProgressBar(Bitmap bitmap) {
		m_bitmap = bitmap;
		m_matrix = new Matrix();
		m_pixel_progress = 0;
	}

	//----------------------------------------------------------------------
	//	translate to x, y
	//----------------------------------------------------------------------
	public void translate(float x, float y) {
		m_matrix.postTranslate(x, y);
	}

	//----------------------------------------------------------------------
	//	scale	width
	//----------------------------------------------------------------------
	private void scaleWidth(float scale_pixel) {
		m_pixel_progress = scale_pixel;
	}

	public boolean isTouched() {
		return m_touched;
	}

	public void setTouched(boolean touched) {
		this.m_touched = touched;
	}

	public void draw(Canvas canvas, float scale_pixel) {
		scaleWidth(scale_pixel);
		for(float i = 0; i <= m_pixel_progress; i++)
			canvas.drawBitmap(m_bitmap, getX() + i, getY(), null);
	}

	//----------------------------------------------------------------------
	//	getter | setter
	//----------------------------------------------------------------------
	public float getX() {
		float[] values = new float[9];
		m_matrix.getValues(values);

		return values[Matrix.MTRANS_X];
	}

	public float getY() {
		float[] values = new float[9];
		m_matrix.getValues(values);

		return values[Matrix.MTRANS_Y];
	}

	public int getWidth() {
		return m_bitmap.getWidth();
	}

	public int getHeight() {
		return m_bitmap.getHeight();
	}
}