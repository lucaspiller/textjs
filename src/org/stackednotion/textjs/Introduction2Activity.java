package org.stackednotion.textjs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Introduction2Activity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// update settings with current context
		Settings.init(getApplicationContext());
		
		setContentView(R.layout.introduction2_view);

		// callback for button
		Button next = (Button) findViewById(R.id.next);
		next.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Intent intent = new Intent(view.getContext(),
						SettingsActivity.class);
				startActivity(intent);
			}
		});
		
		// callback for button
		Button back = (Button) findViewById(R.id.back);
		back.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		// update settings with current context
		Settings.init(getApplicationContext());
	}
}
