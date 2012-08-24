package csci498.hello;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import java.util.Date;

public class MainActivity extends Activity implements View.OnClickListener {
	Button btn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        btn = new Button(this);
        btn.setOnClickListener(this);
        updateTime();
        setContentView(btn);
    }

    public void onClick(View view) {
    	updateTime();
    }
    
    private void updateTime() {
    	btn.setText(new Date().toString());
    }
}
