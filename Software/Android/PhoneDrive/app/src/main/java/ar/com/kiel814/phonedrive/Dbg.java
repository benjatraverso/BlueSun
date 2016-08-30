package ar.com.kiel814.phonedrive;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;

public class Dbg
{
	private static Dbg instance = null;
	private static ArrayList<String> lines;
	private static Paint paint;

	private Dbg()
	{
	}

	public static Dbg get()
	{
		if(null == instance)
		{
			instance = new Dbg();
		}
		return instance;
	}

	public void init()
	{
		lines = new ArrayList<>();
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setTextSize(25);
		paint.setColor(Color.BLACK);
	}

	public static void log(String line)
	{
		lines.add(line);
		if(lines.size() > 20)
		{
			lines.remove(0);
		}
	}

	public static void draw(Canvas canvas)
	{
		for(int i = 0; i < lines.size(); i++)
		{
			canvas.drawText(lines.get(i), 1000.0f, 1000.0f - (lines.size() - i) * 55.0f, paint);
		}
	}
}