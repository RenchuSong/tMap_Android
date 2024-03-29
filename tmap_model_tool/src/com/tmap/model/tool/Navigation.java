package com.tmap.model.tool;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Navigation extends Activity {
	private Button sample, model, locating;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation);
        
        sample = (Button)this.findViewById(R.id.sampleBtn);
        model = (Button)this.findViewById(R.id.modelBtn);
        locating = (Button)this.findViewById(R.id.locatingBtn);
        
        sample.setOnClickListener(
        	new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(Navigation.this, WifiCollector.class);
					startActivity(intent);
				}
        	}
        );
        
        locating.setOnClickListener(
            new OnClickListener() {
    			@Override
    			public void onClick(View arg0) {
   					// TODO Auto-generated method stub
    				Intent intent = new Intent(Navigation.this, Locating.class);
    				startActivity(intent);
   				}
           	}
        );
            
	}
}
