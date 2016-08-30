package ar.com.kiel814.phonedrive;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ResLoader
{
	private static ResLoader instance = null;
	private static Resources resources;

	protected ResLoader(){}

	public static ResLoader get()
	{
		if(null == instance)
		{
			instance = new ResLoader();
		}
		return instance;
	}

	public void init(Resources res)
	{
		resources = res;
	}

	public static Bitmap getBmp(int id)
	{
		return BitmapFactory.decodeResource(resources, id);
	}
}