package com.android.demo.socket;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.android.demo.socket.lib.ServerConnection;
import com.android.demo.socket.lib.ServerConnection.OnServerEventListener;

public class ServerActivity extends Activity {

	protected static final String TAG = "ServerActivity";
	private ServerConnection connection;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.server);
		connection = new ServerConnection(ServerActivity.this, serverListener);
		TextView text = (TextView) findViewById(R.id.textView1);
		String allAddrs = connection.getIPFromWifiManager();
		text.setText(allAddrs);
		
		ImageButton btn = (ImageButton) findViewById(R.id.btn_send);
		btn.setOnClickListener(sendClick);
		
		connection.createServer(6000);
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
	
	private OnServerEventListener serverListener = new OnServerEventListener() {

		public void onClientConnected(String connectedIP) {
			String ipMessage = getString(R.string.connected_ip, connectedIP);
			LinearLayout root = (LinearLayout) findViewById(R.id.root);
			TextView text = new TextView(ServerActivity.this);
			text.setTextSize(20);
			text.setText(ipMessage);
			root.addView(text, LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		}

		public void onReceiveMessage(String message) {
			LinearLayout root = (LinearLayout) findViewById(R.id.root);
			View talkInfo = generateTalkItem(false, message, System.currentTimeMillis());
			LayoutParams params = new LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			root.addView(talkInfo, 0, params);
		}

		public void messageSent(String message) {
			LinearLayout root = (LinearLayout) findViewById(R.id.root);
			View talkInfo = generateTalkItem(true, message, System.currentTimeMillis());
			LayoutParams params = new LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			root.addView(talkInfo, 0, params);
		}
		
		public void connectionError() {
			Toast.makeText(ServerActivity.this, R.string.connection_error, Toast.LENGTH_LONG).show();
		}
		
	};
	
	private View generateTalkItem(boolean isSender, String message, long time) {
		LayoutInflater inflater = LayoutInflater.from(ServerActivity.this);
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
