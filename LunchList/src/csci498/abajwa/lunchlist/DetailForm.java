package csci498.abajwa.lunchlist;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

public class DetailForm extends Activity {
	
	EditText name = null;
	EditText address = null;
	EditText notes = null;
	RadioGroup types = null;
	
	RestaurantHelper helper;
	String restaurantId = null;
	
	SharedPreferences prefs;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_form);
		
		helper = new RestaurantHelper(this);		
		name = (EditText)findViewById(R.id.name);
		address = (EditText)findViewById(R.id.addr);
		types = (RadioGroup)findViewById(R.id.types);
		notes = (EditText)findViewById(R.id.notes);

		Button save = (Button)findViewById(R.id.save);

		save.setOnClickListener(onSave);
		
		restaurantId = getIntent().getStringExtra(LunchListActivity.ID_EXTRA);
		
		if (restaurantId != null) {
			load();
		}
		
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		
		String type = prefs.getString("default_type", "take-out");
		if (type.equals("take-out")) {
			types.check(types.getChildAt(0).getId());
		}
		else if (type.equals("sit-down")) {
			types.check(types.getChildAt(1).getId());
		}
		else {
			types.check(types.getChildAt(2).getId());
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    new MenuInflater(this).inflate(R.menu.detailoptions, menu);

	    return(super.onCreateOptionsMenu(menu));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.add) {
			startActivity(new Intent(DetailForm.this, DetailForm.class));
			
			return (true);
		}
		else if (item.getItemId() == R.id.prefs) {
			startActivity(new Intent(this, EditPreferences.class));
			
			return (true);
		}
		
		return(super.onOptionsItemSelected(item));
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
				helper.insert(name.getText().toString(), address.getText().toString(), type, notes.getText().toString());
			}
			else {
				helper.update(restaurantId, name.getText().toString(), address.getText().toString(), type, notes.getText().toString());
			}
			
			finish();
		}
	};
	
	@Override
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
}
