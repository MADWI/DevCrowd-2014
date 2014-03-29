package pl.devcrowd.app.utils;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;

public final class ProgressUtils {

	private ProgressUtils() {
	}

	public static void show(Context context) {
		((ActionBarActivity)context).setSupportProgressBarIndeterminateVisibility(true);
	}

	public static void hide(Context context) {
		((ActionBarActivity)context).setSupportProgressBarIndeterminateVisibility(false);
	}
}
