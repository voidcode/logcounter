package com.voidcodesystems.logcounter;

import java.math.BigDecimal;
import android.net.TrafficStats;
import android.os.Bundle;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.view.Menu;
import android.webkit.WebView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private long totalTNumberOfPackets;
	private NotificationManager mgr;
	private WebView wv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		if(!(totalTNumberOfPackets == TrafficStats.UNSUPPORTED))
		{
			mgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
			wv = (WebView) findViewById(R.id.wv);
			wv.loadUrl("file:///android_asset/info.html");
			intiNotification();
			
			new Thread(
				    new Runnable() {
				        public void run() {
				        	while(true)
				        	{
				        		intiNotification();
				        		try {
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
				        	}
				        }
				    }
				// Starts the thread by calling the run() method in its Runnable
				).start();
		}
		else
		{
			Toast.makeText(this, "Hov.. din Android understøtter ikke denne app!!", Toast.LENGTH_LONG).show();
		}
	}
	public static long round(Long unrounded, int precision, int roundingMode)
	{
	    BigDecimal bd = new BigDecimal(unrounded);
	    BigDecimal rounded = bd.setScale(precision, roundingMode);
	    return rounded.longValue();
	}
	public Long usersTotalNumberOfLogginger()
	{
		return round(new TrafficStats().getTotalTxPackets()/500, 0, BigDecimal.ROUND_HALF_DOWN);
	}
	@SuppressWarnings("deprecation")
	private void intiNotification() {
	        Notification note = new Notification(R.drawable.ic_launcher,
	                "Logningstæller ("+usersTotalNumberOfLogginger()+" gange)", System.currentTimeMillis());
	        PendingIntent i = PendingIntent.getActivity(this, 0, new Intent(
	                this, MainActivity.class), Notification.FLAG_ONGOING_EVENT);
	        note.setLatestEventInfo(this, "Logningstæller", "Du er blevet logget "+String.valueOf(usersTotalNumberOfLogginger())+" gange",
	                i);
	        // note.number = ++count;
	        note.flags |= Notification.FLAG_ONGOING_EVENT;
	        mgr.notify(0, note);
    }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}