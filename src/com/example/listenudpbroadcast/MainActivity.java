package com.example.listenudpbroadcast;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.charset.Charset;

import android.net.wifi.WifiManager;

public class MainActivity extends Activity {
	Context context;
	WifiManager wifiManager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		context = this; 
		wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		new Thread(receiveBroadcast).start();
		
	}
/*	
	public InetAddress getBroadcastAddress() throws IOException {
	    DhcpInfo dhcp = wifiManager.getDhcpInfo();
	    int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
	    byte[] quads = new byte[4];
	    for (int k = 0; k < 4; k++) {
	      quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
	    }
	    return InetAddress.getByAddress(quads);
	}
*/	
	private Runnable receiveBroadcast = new Runnable() {
		@Override
        public void run() {
			DatagramSocket socket=null;
			try {
				socket = new DatagramSocket(7411); //printer plugin board sends out broadcast that will be received through port 7411
			} catch (SocketException e) {
		// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(socket!=null)
			{
				byte[] buf = new byte[1024];
				while(true) {
					DatagramPacket packet = new DatagramPacket(buf, buf.length);
					try {
						socket.receive(packet);
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} 
						String msg = packet.getAddress().getHostAddress();
						Log.d("Alex", "msg : "+ msg);
						byte[] data = packet.getData(); 						
						InputStreamReader input = new InputStreamReader(new ByteArrayInputStream(data), Charset.forName("UTF-8"));
						StringBuilder str = new StringBuilder();
						for (int value; (value = input.read()) != -1; )
							str.append((char) value);
						Log.d("Alex", "str is: "+str); 
						boolean check = str.toString().contains("KCodes-04ec24"); 
						Log.d("Alex", "check is: "+check); 
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	
				}
			}
		}
	};
}
