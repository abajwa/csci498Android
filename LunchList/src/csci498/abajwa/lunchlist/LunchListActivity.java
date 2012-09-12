package csci498.abajwa.lunchlist;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.app.TabActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.Toast;

public class LunchListActivity extends TabActivity {
	List<Restaurant> model = new ArrayList<Restaurant>();
	RestaurantAdapter adapter = null; 
	
	EditText name = null;
	EditText address = null;
	EditText notes = null;
	RadioGroup types = null;
	Restaurant current = null;
	int progress;
	
	Handler handler;
	LinkedBlockingQueue<Runnable> linkedQueue = new LinkedBlockingQueue<Runnable>();
	ThreadPoolExecutor threads;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_PROGRESS);
		setContentView(R.layout.main);
		
		name = (EditText)findViewById(R.id.name);
		address = (EditText)findViewById(R.id.addr);
		types = (RadioGroup)findViewById(R.id.types);
		notes = (EditText)findViewById(R.id.notes);

		Button save = (Button)findViewById(R.id.save);

		save.setOnClickListener(onSave);

		ListView list = (ListView)findViewById(R.id.restaurants);

		adapter = new RestaurantAdapter();
		list.setAdapter(adapter);
		
		TabHost.TabSpec spec = getTabHost().newTabSpec("tag1");
	    
	    spec.setContent(R.id.restaurants);
	    spec.setIndicator("List", getResources().getDrawable(R.drawable.list));
	    getTabHost().addTab(spec);
	    
	    spec=getTabHost().newTabSpec("tag2");
	    spec.setContent(R.id.details);
	    spec.setIndicator("Details", getResources().getDrawable(R.drawable.restaurant));
	    getTabHost().addTab(spec);
	    
	    getTabHost().setCurrentTab(0);
	    
	    list.setOnItemClickListener(onListClick);
	    
	    handler = new Handler();
		threads = new ThreadPoolExecutor(1, Integer.MAX_VALUE, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
	    
	    
	    Runnable longTask = new Runnable() {
			public void run() {
				for (int i = 0; i < 20; i++) {
					fakeJob(500);
				}
				
				handler.post(new Runnable()  {
					public void run() {
					setProgressBarVisibility(false);
					String message = "Long Task Finished";
					Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();;
					}
				});
				killJob();
			}
		};
		linkedQueue.add(longTask);
		threads.execute(longTask);
	    
	}
	
	
	private void killJob() {
		threads.shutdown();
	}
	
	private void fakeJob(final int incr) {
		handler.post(new Runnable()  {
			public void run() {
				progress += incr;
				setProgress(progress);
			}
		});
		SystemClock.sleep(250);
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		new MenuInflater(this).inflate(R.menu.option, menu);
		
		return(super.onCreateOptionsMenu(menu));
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.toast) {
			String message = "No restaurant selected";
			
			if (current != null) {
				message = current.getNotes();
			}
			
			Toast.makeText(this, message, Toast.LENGTH_LONG).show();
			
			return(true);
		}
		
		return(super.onOptionsItemSelected(item));
	}

	private View.OnClickListener onSave = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			current = new Restaurant();
			current.setName(name.getText().toString());
			current.setAddress(name.getText().toString());
			current.setNotes(notes.getText().toString());

			RadioGroup types = (RadioGroup)findViewById(R.id.types);

			switch(types.getCheckedRadioButtonId()) {
			case R.id.sit_down:
				current.setType("sit_down");
				break;
			case R.id.take_out:
				current.setType("take_out");
				break;
			case R.id.delivery:
				current.setType("delivery");
				break;
			}

			adapter.add(current);
		}
	};

	private AdapterView.OnItemClickListener onListClick = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			current = model.get(position);
			
			name.setText(current.getName());
			address.setText(current.getAddress());
			notes.setText(current.getNotes());
			
			if (current.getType().equals("sit_down")) {
				types.check(R.id.sit_down);
			}
			else if (current.getType().equals("take_out")) {
				types.check(R.id.take_out);
			}
			else {
				types.check(R.id.delivery);
			}
			
			getTabHost().setCurrentTab(1);
		}
	};
	
	class RestaurantAdapter extends ArrayAdapter<Restaurant> {
		RestaurantAdapter() {
			super(LunchListActivity.this, R.layout.row, model);
		}


		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;
			RestaurantHolder holder = null;

			if (row == null) {                          
				LayoutInflater inflater = getLayoutInflater();

				row = inflater.inflate(R.layout.row, parent, false);
				holder = new RestaurantHolder(row);
				row.setTag(holder);
			}
			else {
				holder = (RestaurantHolder)row.getTag();
			}

			holder.populateFrom(model.get(position));

			return(row);
		}
	}

	static class RestaurantHolder {
		private TextView name = null;
		private TextView address = null;
		private ImageView icon = null;

		RestaurantHolder(View row) {
			name = (TextView)row.findViewById(R.id.title);
			address = (TextView)row.findViewById(R.id.address);
			icon = (ImageView)row.findViewById(R.id.icon);
		}

		void populateFrom(Restaurant r) {
			name.setText(r.getName());
			address.setText(r.getAddress());

			if (r.getType().equals("sit_down")) {
				icon.setImageResource(R.drawable.ball_red);
			}
			else if (r.getType().equals("take_out")) {
				icon.setImageResource(R.drawable.ball_yellow);
			}
			else {
				icon.setImageResource(R.drawable.ball_green);
			}
		}
	}
}
