package com.android.demo.game;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SplashScreen extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		setContentView(R.layout.splash);
		
		Button btnNewGame = (Button) findViewById(R.id.btn_new);
		Button btnInstruction = (Button) findViewById(R.id.btn_ins);
		
		btnNewGame.setOnClickListener(newGameClick);
		btnInstruction.setOnClickListener(insClick);
	}
	
	private OnClickListener newGameClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClassName("com.android.demo.game", "com.android.demo.game.Main");
			startActivity(intent);
		}
		
	};
	
	private OnClickListener insClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClassName("com.android.demo.game", "com.android.demo.game.Instruction");
			startActivity(intent);
		}
		
	};

}
