package pl.devcrowd.app.dialogs;

import pl.devcrowd.app.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Html;

public class AboutDialog extends DialogFragment {

	public static AboutDialog newInstance() {
		return new AboutDialog();
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		return new AlertDialog.Builder(getActivity())
				.setTitle(R.string.about)
				.setMessage(
						Html.fromHtml(getText(R.string.about_msg).toString()))
				.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								dismiss();
							}
						}).create();
	}

}
