package pl.devcrowd.app.broadcasts;

import pl.devcrowd.app.utils.DebugLog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		DebugLog.d("ID of AlarmAction: " + intent.getIntExtra("lessonID", -1));
	}
}
