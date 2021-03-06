package org.stackednotion.textjs.rosebud;

import org.stackednotion.textjs.rosebud.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Introduction1Activity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// update settings with current context
		Settings.init(getApplicationContext());

		setContentView(R.layout.introduction1_view);

		if (Settings.isWifiConnected()) {
			TextView textView = (TextView) findViewById(R.id.instructions2);
			CharSequence text = getText(R.string.introduction1_instructions2_conn_part1)
					+ " "
					+ Settings.getAddress()
					+ " "
					+ getText(R.string.introduction1_instructions2_conn_part2);
			textView.setText(text);
		}

		// callback for button
		Button next = (Button) findViewById(R.id.next);
		next.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Intent intent = new Intent(view.getContext(),
						Introduction2Activity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		// update settings with current context
		Settings.init(getApplicationContext());
		Settings.analyticsEvent("Introduction1Activity Launch");
	}
}
