package org.stackednotion.textjs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class IntroductionActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// update settings with current context
		Settings.init(getApplicationContext());
		
		if (Settings.skipIntroduction()) {
			Intent intent = new Intent(this, SettingsActivity.class);
        	startActivityForResult(intent, 0);
		} else {
			setContentView(R.layout.introduction1_view);
			
			// callback for button
			Button next = (Button) findViewById(R.id.next);
	        next.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View view) {
	                Intent intent = new Intent(view.getContext(), SettingsActivity.class);
	                startActivityForResult(intent, 0);
	            }
	        });
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();

		// update settings with current context
		Settings.init(getApplicationContext());
	}
}
