package ar.com.kiel814.phonedrive;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PointF;

public class Wheel
{
	Bitmap bmp;
	int fingerId;
	float initialX;
	float currentX;
	float angle;

	PointF center;

	public Wheel()
	{
		Bitmap b1 = ResLoader.getBmp(R.drawable.wheel);
		bmp = Bitmap.createScaledBitmap(b1, b1.getWidth() / 3, b1.getHeight() / 3, true);

		center = new PointF(GamePanel.WIDTH / 4, GamePanel.HEIGHT - (bmp.getHeight() * 0.25f));

		fingerId = -1;
		initialX = 0;
		currentX = 0;
		angle = 0;
	}

	public void update(float dt)
	{
		if(-1 == fingerId)
		{
			angle -= angle * dt * 10;
		}
	}

	public void draw(Canvas canvas)
	{
		canvas.save();
		canvas.rotate(angle, center.x, center.y);
		canvas.drawBitmap(bmp, center.x - bmp.getWidth() * 0.5f, center.y - bmp.getHeight() * 0.5f, null);
		canvas.restore();
	}

	public int getFingerId()
	{
		return fingerId;
	}

	public void setFinger(int id, float x)
	{
		fingerId = id;
		initialX = x;
		currentX = x;
	}

	public void updateFinger(float x)
	{
		currentX = x;
		angle = (currentX - initialX) * 135.0f / center.x;
		if(angle > 135.0f)
		{
			angle = 135.0f;
		}
		else if(angle < -135.0f)
		{
			angle = -135.0f;
		}
	}

	public void release()
	{
		fingerId = -1;
	}

	public int getServoAngle()
	{
		int rv = 90;
		if(fingerId >= 0)
		{
			rv = (int) (angle * (180.0f / 270.0f) + 90.0f);
		}
		return rv;
	}
}