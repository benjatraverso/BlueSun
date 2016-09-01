package ar.com.kiel814.phonedrive;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Set;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback
{
	private GameThread thread;

	public static float scaleX = 1.0f;
	public static float scaleY = 1.0f;

	public static final int WIDTH = 1776;
	public static final int HEIGHT = 1080;

	public static PointF center;

	ArrayList<Finger> fingers;

	Wheel wheel;
	Throttle throttle;

	Paint txtPaint;

	Activity activity;
	BTManager bt;

	float delay;
	String message;

	public GamePanel(Context _context, Activity _activity)
	{
		super(_context);
		getHolder().addCallback(this);
		activity = _activity;
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder)
	{
		scaleX = (float)getWidth() / WIDTH;
		scaleY = (float)getHeight() / HEIGHT;
		center = new PointF(WIDTH * 0.5f, HEIGHT * 0.5f);

		fingers = new ArrayList<>();

		wheel = new Wheel();
		throttle = new Throttle();

		txtPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		txtPaint.setTextSize(40);
		txtPaint.setColor(Color.BLACK);

		bt = new BTManager();
		bt.initialize(activity);
		Set<BluetoothDevice> devices = bt.getPairedDevices();
		for(BluetoothDevice d : devices)
		{
			if(d.getName().contains("AUTITO"))
			{
				bt.connect(d.getAddress());
				//bt.send(testmsg);
				break;
			}
		}

		delay = 0;
		message = "";

		thread = new GameThread(getHolder(), this);
		thread.start();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
	{
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder)
	{
		boolean retry = true;
		int attempts = 0;
		while(retry && attempts < 1000)
		{
			attempts++;
			try
			{
				thread.stopRunning();
				thread.join();
				retry = false;
			}
			catch(Exception e)
			{
				System.out.println("Exception stopping the main thread!!!");
				e.printStackTrace();
			}
		}
	}

	public void update(float dt)
	{
		wheel.update(dt);
		throttle.update(dt);

		delay -= dt;
		if(delay < 0 &&  bt.isConnected())
		{
			delay = 0.5f;
			message = "<v=1.0"
					+ ";A=" + wheel.getServoAngle()
					+ ";G=" + throttle.getGear()
					+ ";T=" + throttle.getPower()
					+">\n\r";
			bt.send(message);
		}
	}

	@Override
	public void draw(Canvas canvas)
	{
		super.draw(canvas);

		final int savedState = canvas.save();
		canvas.scale(scaleX, scaleY);

		canvas.drawColor(Color.WHITE);
		wheel.draw(canvas);
		throttle.draw(canvas);

		canvas.drawText("Gear: " + throttle.getGear(), 50.0f, 50.0f, txtPaint);
		canvas.drawText("Power: " + throttle.getPower(), 50.0f, 100.0f, txtPaint);
		canvas.drawText("Angle: " + wheel.getServoAngle(), 50.0f, 150.0f, txtPaint);
		canvas.drawText("Message: " + message, 50.0f, 200.0f, txtPaint);

		Dbg.draw(canvas);

		canvas.restoreToCount(savedState);
	}

	public void processInput(MotionEvent event)
	{
		int index = event.getActionIndex();
		int id = event.getPointerId(index);
		int action = event.getActionMasked();

		switch(action)
		{
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_POINTER_DOWN:
				AddFinger(id, event.getX(index) / scaleX, event.getY(index) / scaleY);
				break;
			case MotionEvent.ACTION_MOVE:
				for(int i = 0; i < event.getPointerCount(); i++)
				{
					id = event.getPointerId(i);
					MoveFinger(id, event.getX(i) / scaleX, event.getY(i) / scaleY);
				}
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_POINTER_UP:
			case MotionEvent.ACTION_CANCEL:
				RemoveFinger(id);
				break;
		}
	}

	private void AddFinger(int id, float x, float y)
	{
		Finger f = new Finger(id);
		f.x = x;
		f.y = y;
		fingers.add(f);

		if(x < WIDTH / 2)
		{
			if(-1 == wheel.getFingerId())
			{
				wheel.setFinger(id, x);
			}
		}
		else
		{
			if(-1 == throttle.getFingerId())
			{
				throttle.setFinger(id, y);
			}
		}
	}

	private void MoveFinger(int id, float x, float y)
	{
		int idx = getFingerIndex(id);
		if(idx >= 0)
		{
			fingers.get(idx).x = x;
			fingers.get(idx).y = y;
		}

		if(wheel.getFingerId() == id)
		{
			wheel.updateFinger(x);
		}
		else if(throttle.getFingerId() == id)
		{
			throttle.updateFinger(y);
		}
	}

	private void RemoveFinger(int id)
	{
		int idx = getFingerIndex(id);
		if(idx >= 0)
		{
			fingers.remove(idx);
		}

		if(wheel.getFingerId() == id)
		{
			wheel.release();
		}
		else if(throttle.getFingerId() == id)
		{
			throttle.release();
		}
	}

	private int getFingerIndex(int pointerId)
	{
		int idx = -1;
		for(int i = 0; i < fingers.size(); i++)
		{
			if(pointerId == fingers.get(i).pid)
			{
				idx = i;
				break;
			}
		}
		return idx;
	}
}