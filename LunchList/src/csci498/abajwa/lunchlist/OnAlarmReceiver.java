package csci498.abajwa.lunchlist;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.net.Uri;
import android.preference.PreferenceManager;


public class OnAlarmReceiver extends BroadcastReceiver {
	
	private static final int NOTIFY_ME_ID = 1337;
	
	@Override
	public void onReceive(Context ctxt, Intent intent) {
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctxt);
		boolean useNotification = prefs.getBoolean(ctxt.getResources().getString(R.string.use_notification), true);
		
		if (useNotification) {
			NotificationManager mgr = (NotificationManager)ctxt.getSystemService(Context.NOTIFICATION_SERVICE);
			Notification note = new Notification(R.drawable.stat_notify_chat, ctxt.getResources().getString(R.string.lunch_time), System.currentTimeMillis());
			PendingIntent i = PendingIntent.getActivity(ctxt, 0, new Intent(ctxt, AlarmActivity.class), 0);
			
			note.setLatestEventInfo(ctxt, ctxt.getResources().getString(R.string.title_activity_lunch_list), ctxt.getResources().getString(R.string.lunch_notification_message), i);
			note.flags |= Notification.FLAG_AUTO_CANCEL;
			
			String sound = prefs.getString("alarm_ringtone", null);
			
			if (sound != null) {
				note.sound = Uri.parse(sound);
				note.audioStreamType = AudioManager.STREAM_ALARM;
			}
			
			mgr.notify(NOTIFY_ME_ID, note);
		}
		else {
		
			Intent i = new Intent(ctxt, AlarmActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			ctxt.startActivity(i);
		}
	}

}
