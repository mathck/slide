package com.gmail.mathck;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        //	READ HIGHSCORE
    	SharedPreferences prefs = getSharedPreferences("gamedata", Context.MODE_PRIVATE);
    	Highscore.Highscore = prefs.getInt("hisc", 0);
        
        try {
			setContentView(new MainGamePanel(this));
		} catch (IOException e) {
				e.printStackTrace();
		}
    }
    
    @Override
    protected void onPause() {
    	super.onPause();
    	
    	SharedPreferences prefs = getSharedPreferences("gamedata", Context.MODE_PRIVATE);
		Editor editor = prefs.edit();
		editor.putInt("hisc", Highscore.Highscore);
		editor.commit();
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    }

	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		SharedPreferences prefs = getSharedPreferences("gamedata", Context.MODE_PRIVATE);
		Editor editor = prefs.edit();
		editor.putInt("hisc", Highscore.Highscore);
		editor.commit();
	}

	@Override
	protected void onStop() {
		super.onStop();
		
		SharedPreferences prefs = getSharedPreferences("gamedata", Context.MODE_PRIVATE);
		Editor editor = prefs.edit();
		editor.putInt("hisc", Highscore.Highscore);
		editor.commit();
	}
    
    
}