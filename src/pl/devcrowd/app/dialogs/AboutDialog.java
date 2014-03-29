package pl.devcrowd.app.dialogs;

import eu.inmite.android.lib.dialogs.BaseDialogFragment;
import pl.devcrowd.app.R;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;

public class AboutDialog extends BaseDialogFragment {

	public static void show(FragmentActivity activity, String tag) {
		AboutDialog dialog = new AboutDialog();
		dialog.show(activity.getSupportFragmentManager(), tag);
	}

	@Override
	protected Builder build(Builder initialBuilder) {
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		View aboutView = inflater.inflate(R.layout.about_dialog_layout, null);

		initialBuilder.setView(aboutView).setTitle(R.string.about_dialog_title)
				.setNegativeButton(R.string.cancel, new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						dismiss();

					}
				});
		return initialBuilder;
	}

}
