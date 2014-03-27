package pl.devcrowd.app.dialogs;

import pl.devcrowd.app.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;

public class AboutDialog extends DialogFragment {

	private AlertDialog alertDialog;
	
	public static AboutDialog newInstance() {
		return new AboutDialog();
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		View aboutView = inflater.inflate(R.layout.about_dialog_layout, null);

		alertDialog = new AlertDialog.Builder(getActivity())
		.setView(aboutView)
				.setTitle(R.string.about_dialog_title)
				.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								dismiss();
							}
						}).create();
		return alertDialog;
	}

}
