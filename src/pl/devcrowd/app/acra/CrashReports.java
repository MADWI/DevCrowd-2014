package pl.devcrowd.app.acra;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;
import android.app.Application;

@ReportsCrashes(formKey = "",
				formUri="http://crashreports.bl.ee/android/DevCrowd_14_crash/DevCrowd_crash.php",
				mode = ReportingInteractionMode.TOAST,
				resToastText = pl.devcrowd.app.R.string.crash_toast_text)
public class CrashReports extends Application{

	@Override
	public void onCreate() {
		super.onCreate();
		ACRA.init(this);
	}

}
