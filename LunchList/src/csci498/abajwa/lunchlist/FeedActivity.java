package csci498.abajwa.lunchlist;

import org.mcsoxford.rss.RSSFeed;
import org.mcsoxford.rss.RSSReader;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.os.AsyncTask;
import android.util.Log;

public class FeedActivity extends Activity {
	
	private static class FeedTask extends AsyncTask<String, Void, Void> {
		private RSSReader reader = new RSSReader();
		private Exception e;
		private FeedActivity activity;
		
		FeedTask(FeedActivity activity) {
			attach(activity);
		}
		
		void attach(FeedActivity activity) {
			this.activity = activity;
		}
		
		void detach() {
			this.activity = null;
		}
		
		@Override
		public RSSFeed doInBackground(String... urls) {
			RSSFeed result = null;
			
			try {
				result = reader.load(urls[0]);
			}
			catch (Exception e) {
				this.e = e;
			}
			
			return result;
		}
		
		public void onPostExecute(RSSFeed feed) {
			if (e == null) {
				activity.setFeed(feed);
			}
			else {
				Log.e("LunchListActivity", "Exception parsing feed", e);
				activity.goBlooey(e);
			}
		}
		
		private void goBlooey(Throwable t) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			
			builder.setTile("Exception!").setMessage(t.toString()).setPositiveButton("OK", null).show();
		}
	}

}
