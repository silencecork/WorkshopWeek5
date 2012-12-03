package com.android.demo.socket;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import com.android.demo.socket.lib.ClientConnection;
import com.android.demo.socket.lib.ClientConnection.OnClientEventListener;

public class ClientActivity extends Activity {
	
	protected static final String TAG = "ClientActivity";
	private String ipToConnect;
	private ClientConnection connection;
	private ProgressDialog dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.client);
		Intent intent = getIntent();
		ipToConnect = intent.getStringExtra("ip_address");
		connection = new ClientConnection(ClientActivity.this, clientListener);
		
		TextView text = (TextView) findViewById(R.id.connecting);
		text.setText(getString(R.string.connecting, ipToConnect));
		
		ImageButton btn = (ImageButton) findViewById(R.id.btn_send);
		btn.setOnClickListener(sendClick);
		
		dialog = new ProgressDialog(ClientActivity.this);
		dialog.setMessage(getString(R.string.connecting, ipToConnect));
		dialog.show();
		connection.createClient(ipToConnect, 6000);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (connection != null) {
			connection.close();
		}
	}
	
	private void sendMessage(String message) {
		if (connection != null) {
			connection.sendMessage(message);
		}
	}
	
	private OnClickListener sendClick = new OnClickListener() {

		public void onClick(View v) {
			EditText input = (EditText) findViewById(R.id.input_msg);
			String message = input.getText().toString();
			sendMessage(message);
			input.setText("");
		}
		
	};
	
	private OnClientEventListener clientListener = new OnClientEventListener() {

		public void connectToServer(boolean result) {
			if (dialog != null) {
				dialog.dismiss();
			}
			LinearLayout root = (LinearLayout) findViewById(R.id.root);
			TextView text = new TextView(ClientActivity.this);
			text.setTextSize(20);
			text.setText(R.string.success);
			root.addView(text, LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		}

		public void messageSent(String message) {
			LinearLayout root = (LinearLayout) findViewById(R.id.root);
			View talkInfo = generateTalkItem(true, message, System.currentTimeMillis());
			LayoutParams params = new LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			root.addView(talkInfo, 0, params);
		}

		public void onReceiveMessage(String message) {
			LinearLayout root = (LinearLayout) findViewById(R.id.root);
			View talkInfo = generateTalkItem(false, message, System.currentTimeMillis());
			LayoutParams params = new LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			root.addView(talkInfo, 0, params);
		}

		public void connectionError() {
			Toast.makeText(ClientActivity.this, R.string.connection_error, Toast.LENGTH_LONG).show();
		}
		
	};
	
	private View generateTalkItem(boolean isSender, String message, long time) {
		LayoutInflater inflater = LayoutInflater.from(ClientActivity.this);
		View talkInfo = inflater.inflate(R.layout.talk_item, null);
		
		TextView text = (TextView) talkInfo.findViewById(R.id.content);
		text.setText(message);
		ImageView icon = (ImageView) talkInfo.findViewById(R.id.icon);
		TextView role = (TextView) talkInfo.findViewById(R.id.role);
		if (isSender) {
			icon.setImageResource(R.drawable.sender);
			role.setText(R.string.me);
		} else {
			icon.setImageResource(R.drawable.receiver);
			role.setText(R.string.receive);
		}
		TextView textTime = (TextView) talkInfo.findViewById(R.id.time);
		SimpleDateFormat format = new SimpleDateFormat();
		String strCurrentTime = format.format(new Date(time));
		textTime.setText(strCurrentTime);
		
		return talkInfo;
	}
}
