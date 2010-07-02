package org.stackednotion.httpserver;

import android.content.Context;

public class Settings {
	public static final String LOG_TAG = "HttpServer";
	
	private static Context context;
	
	public static void init(Context applicationContext)
	{
		context = applicationContext;
	}
	
	public static Context getContext()
	{
		return context;
	} 
}