package com.gmail.mathck;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

public class CCheckpoint {

	private Bitmap m_bitmap;;
	private Matrix m_matrix;
	private float m_scale;
	private boolean m_touched;

	public CCheckpoint(Bitmap bitmap) {
		m_bitmap = bitmap;
		m_matrix = new Matrix();
		m_touched = false;
		m_scale = 0;
	}

	//----------------------------------------------------------------------
	//	translate to x, y
	//----------------------------------------------------------------------
	public void translate(float x, float y) {
		m_matrix.postTranslate(x, y);
	}

	//----------------------------------------------------------------------
	//	full scale
	//----------------------------------------------------------------------
	public void scale(float s) {
		m_matrix.postScale(s, s, getWidth() / 2, getHeight() / 2);
		m_scale = s;
	}

	public boolean isTouched() {
		return m_touched;
	}

	public void setTouched(boolean touched) {
		this.m_touched = touched;
	}

	public void draw(Canvas canvas) {
		canvas.drawBitmap(m_bitmap, m_matrix, null);
	}

	public void draw(Canvas canvas, float x, float y) {
		translate(x, y);
		canvas.drawBitmap(m_bitmap, m_matrix, null);
	}

	//----------------------------------------------------------------------
	//	TOUCH INPUT	(Checkpoint touched?)
	//----------------------------------------------------------------------
	public void handleActionDown(float eventX, float eventY) {
		if (eventX >= getX() && eventX <= getX() + getWidth()) {
			if (eventY >= getY() && eventY <= getY() + getHeight()) {
				setTouched(true);
			} else {
				setTouched(false);
			}
		} else {
			setTouched(false);
		}
	}

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

	public float getWidth() {
		return m_bitmap.getWidth() * m_scale;
	}

	public float getHeight() {
		return m_bitmap.getHeight() * m_scale;
	}
}