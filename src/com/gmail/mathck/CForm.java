package com.gmail.mathck;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

public class CForm {

	private Bitmap m_bitmap;
	private Matrix m_matrix;
	private boolean m_touched;
	private int m_ckp_count;

	//-------------------------
	//	CHECKPOINT POSITIONS
	//-------------------------
	//	0	1	2
	//
	//	3	4	5
	//
	//	6	7	8
	//-------------------------

	private boolean m_ckp[];

	public CForm(Bitmap bitmap) {
		m_bitmap = bitmap;
		m_matrix = new Matrix();
		m_ckp = new boolean[9];
		m_ckp_count = 0;
	}
	
	public Bitmap getBitmap() {
		return m_bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.m_bitmap = bitmap;
	}

	public boolean isTouched() {
		return m_touched;
	}

	public void setTouched(boolean touched) {
		this.m_touched = touched;
	}

	//----------------------------------------------------------------------
	//	full scale
	//----------------------------------------------------------------------
	public void scale(float s) {
		m_matrix.postScale(s, s, getWidth() / 2, getHeight() / 2);
	}

	public void setPosition(float x, float y) {
		m_matrix.postTranslate(x, y);
		getCKP();
	}

	//----------------------------------------------------------------------
	//	CHECKPOINT HANDLING
	//----------------------------------------------------------------------
	public void getCKP() {
		// 0 = transparent
		m_ckp_count = 0;
		int last_col = (int) (getWidth() * 0.832f);
		if(m_bitmap.getPixel(getWidth() / 6, getWidth() / 6) != 0)	{ m_ckp[0] = true;	m_ckp_count++;	} else { m_ckp[0] = false; }	// 0
		if(m_bitmap.getPixel(getWidth() / 2, getWidth() / 6) != 0)	{ m_ckp[1] = true;	m_ckp_count++;	} else { m_ckp[1] = false; }	// 1
		if(m_bitmap.getPixel(last_col, getWidth() / 6) != 0)		{ m_ckp[2] = true;	m_ckp_count++;	} else { m_ckp[2] = false; }	// 2
		if(m_bitmap.getPixel(getWidth() / 6, getWidth() / 2) != 0)	{ m_ckp[3] = true;	m_ckp_count++;	} else { m_ckp[3] = false; }	// 3
		if(m_bitmap.getPixel(getWidth() / 2, getWidth() / 2) != 0) 	{ m_ckp[4] = true;	m_ckp_count++;	} else { m_ckp[4] = false; }	// 4
		if(m_bitmap.getPixel(last_col, getWidth() / 2) != 0)		{ m_ckp[5] = true;	m_ckp_count++;	} else { m_ckp[5] = false; }	// 5
		if(m_bitmap.getPixel(getWidth() / 6, last_col) != 0)		{ m_ckp[6] = true;	m_ckp_count++;	} else { m_ckp[6] = false; }	// 6
		if(m_bitmap.getPixel(getWidth() / 2, last_col) != 0)		{ m_ckp[7] = true;	m_ckp_count++;	} else { m_ckp[7] = false; }	// 7
		if(m_bitmap.getPixel(last_col, last_col) != 0)				{ m_ckp[8] = true;	m_ckp_count++;	} else { m_ckp[8] = false; }	// 8
	}


	public boolean isCKP(int pos) {
		return m_ckp[pos];
	}

	public void delCKP(int pos) {
		m_ckp[pos] = false;
	}

	public void delCKP() {
		for(int i = 0; i <= 8; i++)
			m_ckp[i] = false;
	}

	public boolean noCKP() {
		for(int i = 0; i <= 8; i++)
			if(m_ckp[i] == true)
				return false;

		return true;
	}
	
	//----------------------------------------------------------------------

	public void draw(Canvas canvas) {
		canvas.drawBitmap(m_bitmap, m_matrix, null);
	}
	
	public int getCkpCount() {
		return m_ckp_count;
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

	public int getWidth() {
		return m_bitmap.getWidth();
	}

	public int getHeight() {
		return m_bitmap.getHeight();
	}
}