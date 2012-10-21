package csci498.abajwa.lunchlist;

import android.os.Bundle;

import com.google.android.maps.MapActivity;

public class RestaurantMap extends MapActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);
	}
	
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

}
