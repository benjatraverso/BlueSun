package ar.com.kiel814.phonedrive;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

public class Game extends Activity
{
	private GamePanel panel;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		Dbg.get().init();
		ResLoader.get().init(getResources());

		panel = new GamePanel(this);
		setContentView(panel);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		int action = event.getActionMasked();

		boolean captured;
		switch(action)
		{
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_POINTER_DOWN:
			case MotionEvent.ACTION_MOVE:
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_POINTER_UP:
				panel.processInput(event);
				captured = true;
				break;
			default:
				captured = super.onTouchEvent(event);
				break;
		}
		return captured;
	}
}