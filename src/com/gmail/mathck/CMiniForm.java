package com.gmail.mathck;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

public class CMiniForm {

	private Bitmap m_bitmap;;
	private Matrix m_matrix;
	private float m_scale;
	private boolean m_touched;

	public CMiniForm(Bitmap bitmap) {
		m_bitmap = bitmap;
		m_matrix = new Matrix();
		m_scale = 1;
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
		m_matrix.postScale(s, s, getWidth(), 0);
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