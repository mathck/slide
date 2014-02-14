package com.gmail.mathck;

import android.graphics.Bitmap;
import android.graphics.Canvas;

//----------------------------------------------------------------------
//	simple bitmap class
//----------------------------------------------------------------------
public class CBackground {

	private Bitmap m_bitmap;

	public CBackground(Bitmap bitmap) {
		this.m_bitmap = bitmap;
	}

	public Bitmap getBitmap() {
		return m_bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.m_bitmap = bitmap;
	}

	//----------------------------------------------------------------------
	//	DRAW	(draw pattern [i, j] times, considering display-size)
	//----------------------------------------------------------------------
	public void draw(Canvas canvas, float width, float height) {
		for(float i = 0; i <= width / getWidth(); i++)
			for(float j = 0; j <= height / getHeight(); j++)
				canvas.drawBitmap(m_bitmap, i * getHeight(), j * getWidth(), null);
	}

	//----------------------------------------------------------------------
	//	DRAW	(draw pattern [i, j] times, considering display-size)
	//----------------------------------------------------------------------
	public void draw(Canvas canvas, float width, float height, float x, float y) {
		for(float i = 0; i <= width / getWidth(); i++)
			for(float j = 0; j <= height / getHeight(); j++)
				canvas.drawBitmap(m_bitmap, x + i * getHeight(), y + j * getWidth(), null);
	}


	public void draw(Canvas canvas) {
		canvas.drawBitmap(m_bitmap, 0, 0, null);
	}

	// align right
	public void draw(Canvas canvas, int width) {
		canvas.drawBitmap(m_bitmap, width - getWidth(), 0, null);
	}

	public int getHeight() {
		return this.m_bitmap.getHeight();
	}

	public int getWidth() {
		return this.m_bitmap.getWidth();
	}
}