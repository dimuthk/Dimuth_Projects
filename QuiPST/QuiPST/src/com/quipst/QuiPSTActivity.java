package com.quipst;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class QuiPSTActivity extends Activity {
    
	Context context;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        context = this;
        final SharedPreferences settings = getSharedPreferences("settings", 0);
        String uid = settings.getString("uid",null);
        
        if(uid == null){
     		Intent i = new Intent(context, Login.class);
     		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
     		startActivity(i);
     		finish();
        }
        
        TextView output = (TextView) findViewById(R.id.output);
        output.setText(uid);
    }
}