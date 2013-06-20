package com.voidcodesystems.logcounter;

import com.voidcodesystems.logcounter.BoundService.MyLocalBinder;

import android.net.TrafficStats;
import android.os.Bundle;
import android.os.IBinder;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.webkit.WebView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private long totalTNumberOfPackets;
	private WebView wv;
	protected BoundService myService;
	protected boolean isBound = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		if(!(totalTNumberOfPackets == TrafficStats.UNSUPPORTED))
		{
			Intent intent = new Intent(this, BoundService.class);
	        bindService(intent, myConnection, Context.BIND_AUTO_CREATE);
			wv = (WebView) findViewById(R.id.wv);
			wv.loadUrl("file:///android_asset/info.html");
		}
		else
		{
			finish();
			Toast.makeText(this, "Hov.. din Android understøtter ikke denne app!!", Toast.LENGTH_LONG).show();
		}
	}
	private ServiceConnection myConnection = new ServiceConnection() {
	    public void onServiceConnected(ComponentName className, IBinder service) {
	        MyLocalBinder binder = (MyLocalBinder) service;
	        myService = binder.getService();
	        myService.startLogCounter();//start log counter thred in service
	        isBound  = true;
	    }
	    public void onServiceDisconnected(ComponentName arg0) {
	        isBound = false;
	    }
	};
	/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}*/
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		unbindService(myConnection);
	}
}