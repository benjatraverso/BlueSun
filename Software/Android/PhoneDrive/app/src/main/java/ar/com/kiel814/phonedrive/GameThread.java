package ar.com.kiel814.phonedrive;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class GameThread extends Thread
{
	private SurfaceHolder surfaceHolder;
	private GamePanel gamePanel;
	private boolean running;

	public GameThread(SurfaceHolder holder, GamePanel panel)
	{
		super();
		surfaceHolder = holder;
		gamePanel = panel;
		running = true;
	}

	public void stopRunning()
	{
		running = false;
	}

	@Override
	public void run()
	{
		long then = System.nanoTime();
		long now;
		float dt;

		while(running)
		{
			now = System.nanoTime();
			dt = (float)(now - then) / 1000000000;

			update(dt);
			draw();

			then = now;
		}
	}

	private void update(float dt)
	{
		try
		{
			gamePanel.update(dt);
		}
		catch(Exception e)
		{
			System.out.println("Exception updating the game panel!!!");
			e.printStackTrace();
		}
	}

	private void draw()
	{
		Canvas canvas = null;

		try
		{
			canvas = surfaceHolder.lockCanvas();
		}
		catch(Exception e)
		{
			System.out.println("Exception locking the canvas!!!");
			e.printStackTrace();
		}

		if(canvas != null)
		{
			try
			{
				gamePanel.draw(canvas);
			}
			catch(Exception e)
			{
				System.out.println("Exception drawing the game panel!!!");
				e.printStackTrace();
			}
		}

		if(canvas != null)
		{
			try
			{
				surfaceHolder.unlockCanvasAndPost(canvas);
			}
			catch(Exception e)
			{
				System.out.println("Exception unlocking the canvas!!!");
				e.printStackTrace();
			}
		}
	}
}