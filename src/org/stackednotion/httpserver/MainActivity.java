package org.stackednotion.httpserver;

import org.stackednotion.httpserver.server.ServerApplication;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    
    @Override
    public void onResume()
    {
    	Settings.init(getApplicationContext());
    	ServerApplication.startServer(8080);
    	
    	super.onResume();
    }
    
    @Override
    public void onPause()
    {
    	ServerApplication.stopServer();
    	
    	super.onPause();
    }
}