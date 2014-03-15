package pl.devcrowd.app.dialogs;

import pl.devcrowd.app.R;
import pl.devcrowd.app.interfaces.RatingCallback;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;

public class RateDialog extends DialogFragment {

	private RatingCallback mRatingCallback;

	public static RateDialog newInstance() {
		return new RateDialog();
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		LayoutInflater inflater = LayoutInflater.from(getActivity());
		View ratingView = inflater.inflate(R.layout.rating_dialog_layout, null);

		final EditText email = (EditText) ratingView
				.findViewById(R.id.emailAdress);
		final RatingBar topicRate = (RatingBar) ratingView
				.findViewById(R.id.topicRate);
		final RatingBar overallRate = (RatingBar) ratingView
				.findViewById(R.id.overallRate);

		return new AlertDialog.Builder(getActivity())
				.setView(ratingView)
				.setTitle(R.string.ratePresentation)
				.setPositiveButton(R.string.send,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								mRatingCallback.userGrades(
										topicRate.getRating(),
										overallRate.getRating());
								dismiss();
							}
						})
				.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								dismiss();
							}
						}).create();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// This makes sure that the container activity has implemented
		// the callback interface. If not, it throws an exception
		try {
			mRatingCallback = (RatingCallback) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement RatingCallback");
		}
	}
}
