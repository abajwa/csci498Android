package csci498.abajwa.lunchlist;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class LunchListActivity extends Activity {
	Restaurant r = new Restaurant();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button save = (Button)findViewById(R.id.save);
        
        save.setOnClickListener(onSave);
        
        // extra credit - adding radio buttons with java
        RadioGroup rgroup = (RadioGroup)findViewById(R.id.types);
        RadioButton extraButton = new RadioButton(this);
        extraButton.setText("Other");
        rgroup.addView(extraButton);
        
        RadioButton extraButton2 = new RadioButton(this);
        extraButton2.setText("extra 1");
        rgroup.addView(extraButton2);
        
        RadioButton extraButton3 = new RadioButton(this);
        extraButton3.setText("extra 2");
        rgroup.addView(extraButton3);
        
        RadioButton extraButton4 = new RadioButton(this);
        extraButton4.setText("extra 3");
        rgroup.addView(extraButton4);
        
        RadioButton extraButton5 = new RadioButton(this);
        extraButton5.setText("extra 4");
        rgroup.addView(extraButton5);
        
        RadioButton extraButton6 = new RadioButton(this);
        extraButton6.setText("extra 5");
        rgroup.addView(extraButton6);
        
        RadioButton extraButton7 = new RadioButton(this);
        extraButton7.setText("extra 6");
        rgroup.addView(extraButton7);
        
        RadioButton extraButton8 = new RadioButton(this);
        extraButton8.setText("extra 7");
        rgroup.addView(extraButton8);
    }

    private View.OnClickListener onSave = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			EditText name = (EditText)findViewById(R.id.name);
			EditText address = (EditText)findViewById(R.id.addr);
			
			r.setName(name.getText().toString());
			r.setAddress(address.getText().toString());
			
			RadioGroup types = (RadioGroup)findViewById(R.id.types);
			
			switch(types.getCheckedRadioButtonId()) {
				case R.id.sit_down:
					r.setType("sit_down");
					break;
				case R.id.take_out:
					r.setType("take_out");
					break;
				case R.id.delivery:
					r.setType("delivery");
					break;
			}
		}
	};
}
