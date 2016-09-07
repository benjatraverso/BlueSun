package ar.com.kiel814.phonedrive;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;

public class Throttle
{
	int fingerId;
	float fingerY;

	Bitmap bmpBar;
	Bitmap bmpFore;
	Bitmap bmpKnob;

	PointF center;
	PointF frame;
	PointF zero;

	float scale;
	final float limit = 0.4f;

	public Throttle()
	{
		fingerId = -1;

		bmpBar = ResLoader.getBmp(R.drawable.slider_bar);
		bmpFore = ResLoader.getBmp(R.drawable.slider_fore);
		Bitmap b1 = ResLoader.getBmp(R.drawable.slider_knob);
		bmpKnob = Bitmap.createScaledBitmap(b1, b1.getWidth() / 3, b1.getHeight() / 3, true);

		center = new PointF(GamePanel.WIDTH * 0.75f, GamePanel.HEIGHT * 0.5f);
		frame = new PointF(center.x - bmpFore.getWidth() * 0.5f, center.y - bmpFore.getHeight() * 0.5f);
		zero = new PointF(center.x - bmpKnob.getWidth() * 0.5f, center.y - bmpKnob.getHeight() * 0.5f);

		fingerY = center.y;

		scale = 255.0f / (bmpFore.getHeight() * limit);
	}

	public void update(float dt)
	{
		if(-1 == fingerId)
		{
			fingerY += (center.y - fingerY) * dt * 10;
		}
	}

	public void draw(Canvas canvas)
	{
		if(fingerY < center.y)
		{
			Rect src = new Rect();
			src.left = 0;
			src.top = (int)(fingerY - frame.y);
			src.right = bmpBar.getWidth();
			src.bottom = bmpBar.getHeight() / 2;
			RectF dst = new RectF();
			dst.left = frame.x;
			dst.top = fingerY;
			dst.right = frame.x + bmpBar.getWidth();
			dst.bottom = center.y;
			canvas.drawBitmap(bmpBar, src, dst, null);
		}
		else if(fingerY > center.y)
		{
			Rect src = new Rect();
			src.left = 0;
			src.top = bmpBar.getHeight() / 2;
			src.right = bmpBar.getWidth();
			src.bottom = (int)(fingerY - frame.y);
			RectF dst = new RectF();
			dst.left = frame.x;
			dst.top = center.y;
			dst.right = frame.x + bmpBar.getWidth();
			dst.bottom = fingerY;
			canvas.drawBitmap(bmpBar, src, dst, null);
		}
		canvas.drawBitmap(bmpFore, frame.x, frame.y, null);
		canvas.drawBitmap(bmpKnob, zero.x, fingerY - bmpKnob.getHeight() * 0.5f, null);
	}

	public int getFingerId()
	{
		return fingerId;
	}

	public void setFinger(int id, float y)
	{
		fingerId = id;
		updateFinger(y);
	}

	public void updateFinger(float y)
	{
		fingerY = y;
		if(fingerY > center.y + bmpFore.getHeight() * limit)
		{
			fingerY = center.y + bmpFore.getHeight() * limit;
		}
		else if(fingerY < center.y - bmpFore.getHeight() * limit)
		{
			fingerY = center.y - bmpFore.getHeight() * limit;
		}
	}

	public void release()
	{
		fingerId = -1;
	}

	public int getPower()
	{
		int power = 0;
		if(fingerId >= 0)
		{
			power = (int)((center.y - fingerY) * scale);
		}
		return power;
	}
}