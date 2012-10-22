package csci498.abajwa.lunchlist;

import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;

public class RestaurantMap extends MapActivity {
	
	public static final String EXTRA_LATITUDE = "apt.tutorial.EXTRA_LATITUDE";
	public static final String EXTRA_LONGITUDE = "apt.tutorial.EXTRA_LONGITUDE";
	public static final String EXTRA_NAME = "apt.tutorial.EXTRA_NAME";
	
	private MapView map;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);
		
		double lat = getIntent().getDoubleExtra(EXTRA_LATITUDE, 0);
		double lon = getIntent().getDoubleExtra(EXTRA_LONGITUDE, 0);
		
		map = (MapView)findViewById(R.id.map);
		map.getController().setZoom(17);
		
		GeoPoint status = new GeoPoint((int)(lat * 1000000.0), (int)(lon * 1000000.0));
		
		map.getController().setCenter(status);
		map.setBuiltInZoomControls(true);
	}
	
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

}
