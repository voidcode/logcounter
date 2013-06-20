package com.voidcodesystems.logcounter;

import java.math.BigDecimal;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.TrafficStats;
import android.os.Binder;
import android.os.IBinder;

public class BoundService extends Service {

	private final IBinder myBinder = new MyLocalBinder();
	private NotificationManager mgr;
	private Notification note;
	private PendingIntent i;
	private Long numOfLogginger;
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return myBinder;
	}
	public void startLogCounter() {
		mgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		new Thread(
			    new Runnable() {
			        public void run() {
			        	while(true)
			        	{
			        		try {
								intiNotification();
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
			        	}
			        }
			    }
			).start();
	}
	public class MyLocalBinder extends Binder {
        BoundService getService() {
            return BoundService.this;
        }
    }	
	//--The part below is metode get an show Logginger to enduser via an Android-Notification 
	public static long round(Long unrounded, int precision, int roundingMode)
	{
	    BigDecimal bd = new BigDecimal(unrounded);
	    BigDecimal rounded = bd.setScale(precision, roundingMode);
	    return rounded.longValue();
	}
	@SuppressWarnings("static-access")
	public Long getUsersTotalNumberOfLogginger()
	{
		return round(new TrafficStats().getTotalTxPackets()/500, 0, BigDecimal.ROUND_HALF_DOWN);
	}
	@SuppressWarnings("deprecation")
	private void intiNotification() {
	        note = new Notification(R.drawable.ic_launcher, "Logningstæller ("+getUsersTotalNumberOfLogginger()+" gange)", System.currentTimeMillis());
	        i = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), Notification.FLAG_ONGOING_EVENT);
	        note.setLatestEventInfo(this, "Logningstæller", "Du er blevet logget "+String.valueOf(getUsersTotalNumberOfLogginger())+" gange", i);
	        note.flags |= Notification.FLAG_ONGOING_EVENT;
	        mgr.notify(0, note);
    }
}