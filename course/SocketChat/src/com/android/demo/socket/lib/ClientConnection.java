package com.android.demo.socket.lib;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

public class ClientConnection {
	
	protected static final String TAG = "ClientConnection";
	private Socket socket;
	private AsyncTask<String, String, Void> connectTask;
	private AsyncTask<String, String, Void> sendMessageTask; 
	private OnClientEventListener listener;
	
	public ClientConnection(Context c, OnClientEventListener l) {
		listener = l;
	}
	
	public void createClient(String ipToConnect, int port) {
		socket = new Socket();
		final InetSocketAddress isa = new InetSocketAddress(ipToConnect, port);
		connectTask = new AsyncTask<String, String, Void>() {
			
			@Override
			protected void onCancelled() {
				super.onCancelled();
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
					if ("connect".equals(values[0])) {
						listener.connectToServer(true);
					} else if ("fail".equals(values[0])) { 
						listener.connectionError();
					} else {
						for (int i = 0; i < values.length; i++) {
							listener.onReceiveMessage(values[i]);
						}
					}
				}
			}

			@Override
			protected Void doInBackground(String... params) {
				if (socket == null || !socket.isConnected()) {
					try {
						socket.connect(isa, 60000);
						publishProgress("connect");
					} catch (IOException e) {
						e.printStackTrace();
						publishProgress("connect");
					}
					while (socket != null && !socket.isClosed()) {
						DataInputStream in = null;
						try {
							in = new DataInputStream(socket.getInputStream());
							String data = in.readUTF();
			                publishProgress(data);
						} catch (IOException e) {
							e.printStackTrace();
							publishProgress("fail");
							break;
						} 
					}
				}
				return null;
			}
			
		};
		connectTask.execute();
	}
	
	public void close() {
		if (connectTask != null) {
			connectTask.cancel(true);
		}
		if (sendMessageTask != null) {
			sendMessageTask.cancel(true);
		}
		if (socket != null) {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public void sendMessage(String message) {
		sendMessageTask = new AsyncTask<String, String, Void>() {

			@Override
			protected Void doInBackground(String... params) {
				if (socket != null && socket.isConnected()) {
					DataOutputStream out = null;
					try {
						out = new DataOutputStream(socket.getOutputStream());
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
					}
					for (int i = 0; i < values.length; i++) {
						listener.messageSent(values[i]);
					}
				}
			}
			
		};
		sendMessageTask.execute(message);
	}
	
	public interface OnClientEventListener {
		public void connectToServer(boolean result);
		public void onReceiveMessage(String message);
		public void messageSent(String msg);
		public void connectionError();
	}
}
