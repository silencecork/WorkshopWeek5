package com.android.demo.game;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Main extends Activity {
	
	protected static final String TAG = "Main";
	private Region position1;
	private Region position2;
	private Region position3;
	private Region position4;
	private Region position5;
	private Region position6;
	private Region position7;
	private Region position8;
	private Region position9;
	
	private int turn;
	
	private Rule rule;
	
	private int circleWin;
	private int circleLose;
	private int blockWin;
	private int blockLose;
	
	private MusicPlayer player;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
        setContentView(R.layout.main);
        
        position1 = (Region) findViewById(R.id.pos1);
        position2 = (Region) findViewById(R.id.pos2);
        position3 = (Region) findViewById(R.id.pos3);
        position4 = (Region) findViewById(R.id.pos4);
        position5 = (Region) findViewById(R.id.pos5);
        position6 = (Region) findViewById(R.id.pos6);
        position7 = (Region) findViewById(R.id.pos7);
        position8 = (Region) findViewById(R.id.pos8);
        position9 = (Region) findViewById(R.id.pos9);
        
        Button btnRestart = (Button) findViewById(R.id.btn_restart);
        btnRestart.setOnClickListener(restartClick);
        
        showRecord();
        
        position1.setOnClickListener(onClickListener);
        position2.setOnClickListener(onClickListener);
        position3.setOnClickListener(onClickListener);
        position4.setOnClickListener(onClickListener);
        position5.setOnClickListener(onClickListener);
        position6.setOnClickListener(onClickListener);
        position7.setOnClickListener(onClickListener);
        position8.setOnClickListener(onClickListener);
        position9.setOnClickListener(onClickListener);
        
        rule = new Rule(position1, position2, position3,
    			position4, position5, position6,
    			position7, position8, position9);
        
        player = new MusicPlayer(Main.this, R.raw.music);
        player.start();
    }

	@Override
	protected void onResume() {
		super.onResume();
		player.resume();
	}
    
    @Override
	protected void onPause() {
		super.onPause();
		player.pause();
	}
    
    @Override
	protected void onDestroy() {
		super.onDestroy();
		player.stop();
	}
    
    private OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			android.util.Log.v(TAG, "onclick");
			Region currentButton = (Region) v;
			boolean isCircle = (turn % 2 == 0);
			if (isCircle) {
				currentButton.setRegionToCircle(true);
			} else {
				currentButton.setRegionToCircle(false);
			}
			if (rule.checkWin(currentButton)) {
				showWinnerAndSetRecord(currentButton);
				showRecord();
				showRestart(true);
			} else if (rule.checkDeadLock()) {
				showDrawGame();
				showRestart(true);
			} else {
				turn++;
				View circleHint = findViewById(R.id.circle_turn);
				View blockHint = findViewById(R.id.block_turn);
				if (turn % 2 == 0) {
					circleHint.setVisibility(View.VISIBLE);
					blockHint.setVisibility(View.GONE);
				} else {
					blockHint.setVisibility(View.VISIBLE);
					circleHint.setVisibility(View.GONE);
				}
			}
		}
    	
    };
    
    private OnClickListener restartClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			reset();
			showRestart(false);
		}
    	
    };
    
    private void showWinnerAndSetRecord(Region button) {
    	View v = LayoutInflater.from(Main.this).inflate(R.layout.result, null);
		ImageView winner = (ImageView) v.findViewById(R.id.winner);
		AlertDialog.Builder builder = new AlertDialog.Builder(Main.this);
		if (button.isCircle()) {
			circleWin++;
			blockLose++;
			winner.setImageResource(R.drawable.circle);
		} else {
			blockWin++;
			circleLose++;
			winner.setImageResource(R.drawable.block);
		}
		builder.setView(v);
		builder.show();
    }
    
    private void showDrawGame() {
    	View v = LayoutInflater.from(Main.this).inflate(R.layout.draw, null);
		AlertDialog.Builder builder = new AlertDialog.Builder(Main.this);
		builder.setView(v);
		builder.show();
    }
    
    private void showRecord() {
    	TextView circleRecord = (TextView) findViewById(R.id.circle_record);
        circleRecord.setText(getString(R.string.record, circleWin, circleLose));
        TextView blockRecord = (TextView) findViewById(R.id.block_record);
        blockRecord.setText(getString(R.string.record, blockWin, blockLose));
    }
    
    private void showRestart(boolean show) {
    	if (show) {
    		Button btnRestart = (Button) findViewById(R.id.btn_restart);
			btnRestart.setVisibility(View.VISIBLE);
    	} else {
    		Button btnRestart = (Button) findViewById(R.id.btn_restart);
			btnRestart.setVisibility(View.GONE);
    	}
    	position1.setEnabled(!show);
    	position2.setEnabled(!show);
    	position3.setEnabled(!show);
    	position4.setEnabled(!show);
    	position5.setEnabled(!show);
    	position6.setEnabled(!show);
    	position7.setEnabled(!show);
    	position8.setEnabled(!show);
    	position9.setEnabled(!show);
    }
    
    private void reset() {
    	turn = 0;
    	View circleHint = findViewById(R.id.circle_turn);
		View blockHint = findViewById(R.id.block_turn);
		circleHint.setVisibility(View.VISIBLE);
		blockHint.setVisibility(View.GONE);      
        position1.reset();
        position2.reset();
        position3.reset();
        position4.reset();
        position5.reset();
        position6.reset();
        position7.reset();
        position8.reset();
        position9.reset();
    }
    
}