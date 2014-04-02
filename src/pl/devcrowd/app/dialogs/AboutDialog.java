package pl.devcrowd.app.dialogs;

import pl.devcrowd.app.R;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import eu.inmite.android.lib.dialogs.BaseDialogFragment;

public class AboutDialog extends BaseDialogFragment implements OnClickListener {

	private int website = 0;
	private final String ACTION = "android.intent.action.VIEW";
	private final String urlDevCrowd = "http://2014.devcrowd.pl";
	private final String urlAuthors = "http://www.mad.zut.edu.pl";

	public static void show(FragmentActivity activity, String tag) {
		AboutDialog dialog = new AboutDialog();
		dialog.show(activity.getSupportFragmentManager(), tag);
	}

	@Override
	protected Builder build(Builder initialBuilder) {
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		View aboutView = inflater.inflate(R.layout.about_dialog_layout, null);

		ImageView logoDevCrowd, logoMAD;
		logoDevCrowd = (ImageView) aboutView.findViewById(R.id.devCrowdLogo);
		logoDevCrowd.setOnClickListener(this);

		logoMAD = (ImageView) aboutView.findViewById(R.id.authorsLogo);
		logoMAD.setOnClickListener(this);

		initialBuilder.setView(aboutView).setTitle(R.string.about_dialog_title)
				.setNegativeButton(R.string.cancel, new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						dismiss();

					}
				});
		return initialBuilder;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.devCrowdLogo:
			website = 1;
			launchWebsite(website);
			break;

		case R.id.authorsLogo:
			website = 2;
			launchWebsite(website);
			break;
		}
	}

	private void launchWebsite(int website) {

		if (website == 1) {
			Intent intent = new Intent(ACTION, Uri.parse(urlDevCrowd));
			startActivity(intent);
		}

		if (website == 2) {
			Intent intent = new Intent(ACTION, Uri.parse(urlAuthors));
			startActivity(intent);
		}
	}
}
