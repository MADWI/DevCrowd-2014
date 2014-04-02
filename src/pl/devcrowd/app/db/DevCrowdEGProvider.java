package pl.devcrowd.app.db;

import pl.devcrowd.app.R;
import pl.devcrowd.app.utils.DebugLog;
import android.app.Activity;
import android.os.CountDownTimer;
import android.widget.TextView;

public class DevCrowdEGProvider extends CountDownTimer {
	
	public interface OnDevCrowdProviderFinish {
		public void onDevCrowdProviderFinish();
	}
	
	private OnDevCrowdProviderFinish onDevCrowdProviderFinish;
	private TextView tvInfo, tvSubinfo;
	
	public DevCrowdEGProvider(TextView viewTop, TextView viewBottom, Activity activity) {
		super(10000, 1000);
		this.tvInfo = viewTop;
		this.tvSubinfo = viewBottom;
		try {
			onDevCrowdProviderFinish = (OnDevCrowdProviderFinish) activity;
		} catch (ClassCastException e) {
			String message = activity.toString()
					+ " must implement OnDevCrowdProviderFinish";
			DebugLog.e(message);
			throw new IllegalArgumentException(message);
		}
	}

	public DevCrowdEGProvider(long millisInFuture, long countDownInterval, 
			TextView view) {
		super(millisInFuture, countDownInterval);
	}

	@Override
	public void onFinish() {
		tvInfo.setText(R.string.devcrowd_info_title);
		tvSubinfo.setText(R.string.devcrowd_info_body);
		onDevCrowdProviderFinish.onDevCrowdProviderFinish();
	}

	@Override
	public void onTick(long millisUntilFinished) {
		tvInfo.setText("Are you M.A.D.?\nTime left:" + (millisUntilFinished/1000));
	}
	
	public boolean setCurrentYearValue() {
		tvInfo.setText("Are you M.A.D.?");
		this.start();
		return true;
	}
	
	public void cancelProviderAction() {
		tvInfo.setText(R.string.devcrowd_info_title);
		tvSubinfo.setText(R.string.devcrowd_info_body);
		this.cancel();
	}
}
