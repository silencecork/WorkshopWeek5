package com.android.demo.socket.lib;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.text.TextUtils;

public class ServerConnection {
	
	protected static final String TAG = "ServerConnection";
	private Socket connectedSocket;
	private ServerSocket socket;
	private AsyncTask<Void, String, Void> task;
	private AsyncTask<String, String, Void> sendMessageTask; 
	private Context context;
	private OnServerEventListener listener;
	
	public ServerConnection(Context c, OnServerEventListener l) {
		context = c;
		listener = l;
	}
	
	public void createServer(int port) {
		try {
			socket = new ServerSocket(port);
			task = new AsyncTask<Void, String, Void>() {
				boolean firstTime = true;
				@Override
				protected Void doInBackground(Void... params) {
						try {
							connectedSocket = socket.accept();
						} catch (IOException e) {
							e.printStackTrace();
						}
						String connectedAddress = connectedSocket.getInetAddress().getHostAddress();
						publishProgress(connectedAddress);
						while (connectedSocket != null && connectedSocket.isConnected() && !socket.isClosed()) {
							DataInputStream in = null;
							try {
								in = new DataInputStream(connectedSocket.getInputStream());
								String data = in.readUTF();
				                publishProgress(data);
							} catch (IOException e) {
								e.printStackTrace();
								publishProgress("fail");
								break;
							} 
						}
						return null;
				}
				@Override
				protected void onPostExecute(Void result) {
					super.onPostExecute(result);
				}
				@Override
				protected void onPreExecute() {
					super.onPreExecute();
				}
				@Override
				protected void onProgressUpdate(String... values) {
					super.onProgressUpdate(values);
					if (listener != null) {
						if ("fail".equals(values[0])) {
							listener.connectionError();
						} else if (firstTime) {
							firstTime = false;
							listener.onClientConnected(values[0]);
						} else {
							for (int i = 0; i < values.length; i++) {
								listener.onReceiveMessage(values[i]);
							}
						}
					}
				}
				@Override
				protected void onCancelled() {
					super.onCancelled();
					if (connectedSocket != null) {
						try {
							connectedSocket.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			};
			task.execute();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendMessage(String message) {
		sendMessageTask = new AsyncTask<String, String, Void>() {

			@Override
			protected Void doInBackground(String... params) {
				if (connectedSocket != null && connectedSocket.isConnected()) {
					DataOutputStream out = null;
					try {
						out = new DataOutputStream(connectedSocket.getOutputStream());
						for (int i = 0; i < params.length; i++) {
							if (!TextUtils.isEmpty(params[i])) {
								out.writeUTF(params[i]);
								out.flush();
							}
						}
						publishProgress(params);
					} catch (IOException e) {
						e.printStackTrace();
						publishProgress("fail");
					}
				}
				return null;
			}

			@Override
			protected void onProgressUpdate(String... values) {
				super.onProgressUpdate(values);
				if (listener != null) {
					if ("fail".equals(values[0])) {
						listener.connectionError();
					} else {
						for (int i = 0; i < values.length; i++) {
							listener.messageSent(values[i]);
						}
					}
				}
			}
			
		};
		sendMessageTask.execute(message);
	}
	
	public void close() {
		if (task != null) {
			task.cancel(true);
		}
		if (socket != null) {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (connectedSocket != null) {
			try {
				connectedSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public String getIPFromWifiManager() {
    	WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    	WifiInfo wifiInfo = wifiManager.getConnectionInfo();
    	int ipAddress = wifiInfo.getIpAddress();
    	return print_ip(ipAddress);
    }
    
    private String print_ip(int ip) {
        int bytes[] = new int[4];
        bytes[0] = ip & 0xFF;
        bytes[1] = (ip >> 8) & 0xFF;
        bytes[2] = (ip >> 16) & 0xFF;
        bytes[3] = (ip >> 24) & 0xFF;       
        return bytes[0] + "." + bytes[1] + "." + bytes[2] + "." + bytes[3];
    }
    
    public interface OnServerEventListener {
		public void onClientConnected(String connectedIP);
		public void onReceiveMessage(String message);
		public void messageSent(String message);
		public void connectionError();
	};
	
}
