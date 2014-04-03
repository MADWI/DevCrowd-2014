package pl.devcrowd.app.dialogs;

import pl.devcrowd.app.R;
import pl.devcrowd.app.utils.DebugLog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import eu.inmite.android.lib.dialogs.BaseDialogFragment;

public class AboutDialog extends BaseDialogFragment implements OnClickListener {

	private static final String ACTION_WWW = "android.intent.action.VIEW";
	private static final String URL_DEVCROWD = "http://2014.devcrowd.pl";
	private static final String URL_AUTHORS = "http://www.mad.zut.edu.pl";

	public static void show(FragmentActivity activity, String tag) {
		AboutDialog dialog = new AboutDialog();
		dialog.show(activity.getSupportFragmentManager(), tag);
	}

	@Override
	protected Builder build(Builder initialBuilder) {
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		View aboutView = inflater.inflate(R.layout.about_dialog_layout, null);

		ImageView logoDevCrowd = (ImageView) aboutView
				.findViewById(R.id.devCrowdLogo);
		logoDevCrowd.setOnClickListener(this);

		ImageView logoMAD = (ImageView) aboutView
				.findViewById(R.id.authorsLogo);
		logoMAD.setOnClickListener(this);

		initialBuilder.setView(aboutView).setTitle(R.string.about_dialog_title)
				.setNegativeButton(R.string.cancel, new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						dismiss();

					}
				});

		TextView versionNumber = (TextView) aboutView
				.findViewById(R.id.appVersionTV);
		try {
			PackageInfo pInfo = getActivity().getPackageManager()
					.getPackageInfo(getActivity().getPackageName(), 0);
			versionNumber.setText("v" + pInfo.versionName);
		} catch (NameNotFoundException e) {
			DebugLog.d("Cannot find app version number");
		}

		return initialBuilder;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.devCrowdLogo:
			startActivity(new Intent(ACTION_WWW, Uri.parse(URL_DEVCROWD)));
			break;

		case R.id.authorsLogo:
			startActivity(new Intent(ACTION_WWW, Uri.parse(URL_AUTHORS)));
			break;
		}
	}
}
