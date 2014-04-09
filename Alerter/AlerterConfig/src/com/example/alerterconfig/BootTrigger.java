package com.example.alerterconfig;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootTrigger extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent i = new Intent(context, SetAlarms.class);
		i.putExtra("triggeredFromAlerter", false);
        context.startService(i);
	}
	

}
