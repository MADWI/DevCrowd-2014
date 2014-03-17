package pl.devcrowd.app.dialogs;

import pl.devcrowd.app.R;
import pl.devcrowd.app.utils.DebugLog;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;

public class RateDialog extends DialogFragment implements
		OnRatingBarChangeListener {

	public interface OnRatingListener {
		public void onSendRatingButtonClick(float topicGrade,
				float speakerGrade, String email);
	}

	private OnRatingListener mOnRatingListener;
	private AlertDialog alertDialog;
	private RatingBar topicRate;
	private boolean topicRateChanged = false;
	private float topicRateValue;
	private RatingBar speakerRate;
	private boolean speakerRateChanged = false;
	private float speakerRateValue;
	private EditText email;
	private boolean correctEmail = false;

	public static RateDialog newInstance() {
		return new RateDialog();
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		LayoutInflater inflater = LayoutInflater.from(getActivity());
		View ratingView = inflater.inflate(R.layout.rating_dialog_layout, null);

		email = (EditText) ratingView.findViewById(R.id.emailAddress);
		email.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// correctEmail = emailValidation();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (emailValidation()) {
					correctEmail = true;
					if (allFiledsFilled()) {
						alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
								.setEnabled(true);
					}
				}else{
					correctEmail = false;
					alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
					.setEnabled(false);
				}
			}
		});

		alertDialog = new AlertDialog.Builder(getActivity())
				.setView(ratingView)
				.setTitle(R.string.ratePresentation)
				.setPositiveButton(R.string.send,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								mOnRatingListener.onSendRatingButtonClick(
										topicRateValue, speakerRateValue, email
												.getText().toString());

							}
						})
				.setNeutralButton(R.string.cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {

							}
						}).create();

		topicRate = (RatingBar) ratingView.findViewById(R.id.topicRate);
		topicRate.setOnRatingBarChangeListener(this);
		speakerRate = (RatingBar) ratingView.findViewById(R.id.speakerRate);
		speakerRate.setOnRatingBarChangeListener(this);
		return alertDialog;
	}

	@Override
	public void onStart() {
		super.onStart();
		alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mOnRatingListener = (OnRatingListener) activity;
		} catch (ClassCastException e) {
			String message = activity.toString()
					+ " must implement mOnRatingListener";
			DebugLog.e(message);
			throw new IllegalArgumentException(message);
		}
	}

	private boolean emailValidation() {
		boolean validationCorrect = android.util.Patterns.EMAIL_ADDRESS
				.matcher(email.getText().toString()).matches();
		return validationCorrect;
	}

	@Override
	public void onRatingChanged(RatingBar ratingBar, float rating,
			boolean fromUser) {

		if (ratingBar == topicRate) {
			topicRateChanged = true;
			topicRateValue = rating;
		} else if (ratingBar == speakerRate) {
			speakerRateChanged = true;
			speakerRateValue = rating;
		}

		if (allFiledsFilled()) {
			alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);

		}
	}

	private boolean allFiledsFilled() {
		return topicRateChanged && speakerRateChanged && correctEmail;
	}
}
