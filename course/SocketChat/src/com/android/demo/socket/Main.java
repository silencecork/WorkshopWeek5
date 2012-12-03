package com.android.demo.socket;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Main extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button btnServer = (Button) findViewById(R.id.btn_establish);
        Button btnClient = (Button) findViewById(R.id.btn_join);
        btnServer.setOnClickListener(clickServer);
        btnClient.setOnClickListener(clickClient);
    }
    
    private OnClickListener clickServer = new OnClickListener() {

		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClassName("com.android.demo.socket", "com.android.demo.socket.ServerActivity");
			startActivity(intent);
		}
    	
    };
    
    private OnClickListener clickClient = new OnClickListener() {

		public void onClick(View v) {
			
			final View dialogView = LayoutInflater.from(Main.this).inflate(R.layout.client_input_ip, null);
			
			AlertDialog.Builder builder = new AlertDialog.Builder(Main.this);
			builder.setTitle(R.string.ip_title);
			builder.setView(dialogView);
			builder.setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					EditText ipInput = (EditText) dialogView.findViewById(R.id.ip_address);
					String ip = ipInput.getText().toString();
					Intent intent = new Intent();
					intent.setClassName("com.android.demo.socket", "com.android.demo.socket.ClientActivity");
					intent.putExtra("ip_address", ip);
					startActivity(intent);
				}
			});
			builder.setNegativeButton(R.string.btn_cancel, null);
			builder.show();
		}
    	
    };
}