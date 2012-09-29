package csci498.abajwa.lunchlist;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

public class DetailForm extends Activity {
	
	EditText address;
	EditText feed;
	EditText name;
	EditText notes;
	RadioGroup types;
	
	RestaurantHelper helper;
	String restaurantId;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_form);
		
		helper = new RestaurantHelper(this);		
		name = (EditText)findViewById(R.id.name);
		address = (EditText)findViewById(R.id.addr);
		types = (RadioGroup)findViewById(R.id.types);
		notes = (EditText)findViewById(R.id.notes);
		feed = (EditText)findViewById(R.id.feed);

		Button save = (Button)findViewById(R.id.save);

		save.setOnClickListener(onSave);
		
		restaurantId = getIntent().getStringExtra(LunchListActivity.ID_EXTRA);
		
		if (restaurantId != null) {
			load();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		new MenuInflater(this).inflate(R.menu.details_options, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.feed) {
			if (isNetworkAvailable()) {
				Intent i = new Intent(this, FeedActivity.class);
				
				i.putExtra(FeedActivity.FEED_URL, feed.getText().toString());
				startActivity(i);
			}
			else {
				Toast.makeText(this, getString(R.string.no_internet_error), Toast.LENGTH_LONG).show();
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private boolean isNetworkAvailable() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		
		return (info != null);
	}
	
	@Override
	public void onSaveInstanceState(Bundle state) {
		super.onSaveInstanceState(state);
		
		state.putString(getString(R.string.name), name.getText().toString());
		state.putString(getString(R.string.address), address.getText().toString());
		state.putString(getString(R.string.notes), notes.getText().toString());
		state.putInt(getString(R.string.type), types.getCheckedRadioButtonId());
	}
	
	@Override
	public void onRestoreInstanceState(Bundle state) {
		super.onRestoreInstanceState(state);
		
		name.setText(state.getString(getString(R.string.name)));
		address.setText(state.getString(getString(R.string.address)));
		notes.setText(state.getString(getString(R.string.notes)));
		types.check(state.getInt(getString(R.string.type)));
		
	}

	public void onDestroy() {
		super.onDestroy();
		
		helper.close();
	}
	
	public void load() {

		Cursor c = helper.getById(restaurantId);
		
		c.moveToFirst();
		name.setText(helper.getName(c));
		address.setText(helper.getAddress(c));
		notes.setText(helper.getNotes(c));
		feed.setText(helper.getFeed(c));
		
		if (helper.getType(c).equals("sit_down")) {
			types.check(R.id.sit_down);
		}
		else if (helper.getType(c).equals("take_out")) {
			types.check(R.id.take_out);
		}
		else {
			types.check(R.id.delivery);
		}
		
		c.close();
	}
	
	private View.OnClickListener onSave = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			String type = null;

			switch(types.getCheckedRadioButtonId()) {
			case R.id.sit_down:
				type = "sit_down";
				break;
			case R.id.take_out:
				type = "take_out";
				break;
			case R.id.delivery:
				type = "delivery";
				break;
			}
			
			if (restaurantId == null) {
				helper.insert(name.getText().toString(), address.getText().toString(), type, notes.getText().toString(), feed.getText().toString());
			}
			else {
				helper.update(restaurantId, name.getText().toString(), address.getText().toString(), type, notes.getText().toString(), feed.getText().toString());
			}
			
			finish();
		}
	};
	
}
