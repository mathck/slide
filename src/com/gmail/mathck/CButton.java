package com.gmail.mathck;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

public class CButton {

	private Bitmap m_bitmap;;
	private Matrix m_matrix;
	private float m_scale;
	private boolean m_touched;

	public CButton(Bitmap bitmap) {
		m_bitmap = bitmap;
		m_matrix = new Matrix();
		m_scale = 1;
	}
	
	public Bitmap getBitmap() {
		return m_bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.m_bitmap = bitmap;
	}

	//----------------------------------------------------------------------
	//	translate to x, y
	//----------------------------------------------------------------------
	public void translate(float x, float y) {
		m_matrix.postTranslate(x, y);
	}

	//----------------------------------------------------------------------
	//	scale // dont use handleActionDown( ... )
	//----------------------------------------------------------------------
	public void scale(float width, float height) {
		m_matrix.postScale(width / getWidth(), height / getHeight());
	}

	//----------------------------------------------------------------------
	//	full scale
	//----------------------------------------------------------------------
	public void scale(float s) {
		m_matrix.postScale(s, s, getWidth() / 2, getHeight() / 2);
		m_scale = s;
	}
	
	public void scale2(float s) {
		m_matrix.postScale(s, s, getWidth() / 2, 0);
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

	//----------------------------------------------------------------------
	//	TOUCH INPUT	(CButton touched?)
	//----------------------------------------------------------------------
	public void handleActionDown(float eventX, float eventY) {
		if (eventX >= getX() && eventX <= getX() + getWidth() * m_scale) {
			if (eventY >= getY() && eventY <= getY() + getHeight() * m_scale) {
				setTouched(true);
			} else {
				setTouched(false);
			}
		} else {
			setTouched(false);
		}
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

	public float getScale() {
		return m_scale;
	}

	public float getWidth() {
		return m_bitmap.getWidth();
	}

	public float getHeight() {
		return m_bitmap.getHeight();
	}

	public float getWidthAS() {
		return m_bitmap.getWidth() * m_scale;
	}

	public float getHeightAS() {
		return m_bitmap.getHeight() * m_scale;
	}
}