package com.example.listenudpbroadcast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Enumeration;

import org.apache.http.conn.util.InetAddressUtils;

import android.app.IntentService;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

public class MyService extends Service {
	private Context context = this; 
	private DatagramSocket socket; 
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	protected void onCreate(Bundle savedInstanceState) {
		
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Log.d("Alex", "onStartCommand"); 
		MyAsyncTask asyncTask = new MyAsyncTask(); 
		asyncTask.execute(null, null, null); 
		return START_NOT_STICKY; 
	}
	
	public class MyAsyncTask extends AsyncTask<Void, Void, Void> {
		ProgressDialog progressDialog;
		
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			Log.d("Alex", "onPreExecute"); 
//			progressDialog = ProgressDialog.show(context, getString(R.string.net_scanning), getString(R.string.wait));
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			Log.d("Alex", "doInBackground"); 
			//get local machine's current network ip
			getLocalIpAddress(); 
//			listenForUDPPacket(); 
			
			//compare UDP and filter out those that aren't in the same network
			return null; 
		}
		

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			//send scanned result to UI
			Log.d("Alex", "onPostExecute"); 
//			progressDialog.dismiss();
		}		
	}
	
	public void listenForUDPPacket(){
		try {//get UDP sent out by others
			byte[] buf = new byte[10240];
			Log.d("Alex", "in listenForUDPPacket"); 
	    	while (true) {
			DatagramPacket packet = new DatagramPacket(buf, buf.length);
			socket = new DatagramSocket(2000, InetAddress.getByName("0.0.0.0"));
//			socket = new DatagramSocket(null);
			socket.setBroadcast(true);
//            socket.setReuseAddress(true);
//            socket.bind(new InetSocketAddress(2000)); //set a port 2000 to listen to the UDP packets broadcasted by others
			socket.receive(packet);
			Log.i("Alex", "Packet received from: " + packet.getAddress().getHostAddress());
			 String packetInfo = new String(packet.getData()).trim();
	        Log.d("Alex", "Received response " + packetInfo);
		} 
		}catch (SocketException e) {
		// TODO Auto-generated catch block
			Log.d("Alex", "exception1"); 
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.d("Alex", "exception2"); 
			e.printStackTrace();
		}
	}

	
	private String getLocalIpAddress() { //get local device's IP
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					String ipv4;
					if (!inetAddress.isLoopbackAddress() && InetAddressUtils.isIPv4Address(ipv4 = inetAddress.getHostAddress())) {
						return ipv4;
                    }
                }
            }
        } catch (Exception e) {
        	Log.d("Alex", "getLocalIpAddressException!");
        	e.printStackTrace();
        }
		return null;
	}

}

