package pl.devcrowd.app.dialogs;

import pl.devcrowd.app.R;
import pl.devcrowd.app.activities.ScheduleDetailsActivity;
import pl.devcrowd.app.utils.DebugLog;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;

public class RateDialog extends DialogFragment implements
		OnRatingBarChangeListener {

	public interface OnRatingListener {
		public void onSendRatingButtonClick(float topicGrade,
				float speakerGrade, String email);
	}

	private OnRatingListener mOnRatingListener;
	private RatingBar topicRate;
	private boolean topicRateChanged = false;
	private float topicRateValue;
	private RatingBar speakerRate;
	private boolean speakerRateChanged = false;
	private float speakerRateValue;
	private EditText email;
	private boolean correctEmail = false;
	private TextView speakerRatingText;

	private LayoutInflater inflater;
	private View ratingView;

	@Override
	public void onActivityCreated(Bundle arg0) {
		super.onActivityCreated(arg0);

		if (inflater == null) {
			inflater = LayoutInflater.from(getActivity());
		}
		if (ratingView == null) {
			ratingView = inflater.inflate(R.layout.rating_dialog_layout, null);
		}

		email = (EditText) ratingView.findViewById(R.id.emailAddress);
		email.addTextChangedListener(mTextWatcher);
		topicRate = (RatingBar) ratingView.findViewById(R.id.topicRate);
		topicRate.setOnRatingBarChangeListener(this);
		speakerRate = (RatingBar) ratingView.findViewById(R.id.speakerRate);
		speakerRate.setOnRatingBarChangeListener(this);
		speakerRatingText = (TextView) ratingView
				.findViewById(R.id.textSpeakerRating);

		Bundle args = getArguments();
		if (getArg(args, ScheduleDetailsActivity.SPEAKERS_COUNT) > 1f) {
			speakerRatingText.setText(getString(R.string.speakersRate));
		}
		topicRate.setRating(getArg(args, ScheduleDetailsActivity.TOPIC_GRADE));
		speakerRate.setRating(getArg(args,
				ScheduleDetailsActivity.SPEAKER_GRADE));

		setEnableSendButton(false);
	}

	private float getArg(Bundle args, String key) {
		float value = 0;
		if (args != null && args.containsKey(key)) {
			value = args.getFloat(key);
		}
		return value;
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
			hideKeyboardIfNecessary();
		} else if (ratingBar == speakerRate) {
			speakerRateChanged = true;
			speakerRateValue = rating;
			hideKeyboardIfNecessary();
		}

		if (allFiledsFilled()) {
			setEnableSendButton(true);
		}
	}

	private boolean allFiledsFilled() {
		return topicRateChanged && speakerRateChanged && correctEmail;
	}

	private final TextWatcher mTextWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			if (emailValidation()) {
				correctEmail = true;
			} else {
				correctEmail = false;
			}

			if (allFiledsFilled()) {
				setEnableSendButton(true);
			} else {
				setEnableSendButton(false);
			}
		}
	};

	private void hideKeyboardIfNecessary() {
		InputMethodManager imm = (InputMethodManager) getActivity()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(email.getWindowToken(), 0);
	}

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		if (inflater == null) {
			inflater = LayoutInflater.from(getActivity());
		}
		if (ratingView == null) {
			ratingView = inflater.inflate(R.layout.rating_dialog_layout, null);
		}

		return new AlertDialog.Builder(getContext())
				.setView(ratingView)
				.setTitle(R.string.ratePresentation)
				.setPositiveButton(R.string.send, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						mOnRatingListener.onSendRatingButtonClick(
								topicRateValue, speakerRateValue, email
										.getText().toString());
					}
				})
				.setNeutralButton(R.string.cancel, null)
				.create();
	}

	public static void show(FragmentActivity activity, String tag, Bundle args) {
		RateDialog dialog = new RateDialog();
		dialog.setArguments(args);
		dialog.show(activity.getSupportFragmentManager(), tag);
	}

	private void setEnableSendButton(boolean value) {
		Button sendButton = ((AlertDialog) getDialog()).getButton(DialogInterface.BUTTON_POSITIVE);
		if (sendButton != null) {
			sendButton.setTextColor(value == true ? Color.BLACK : Color.GRAY);
			sendButton.setEnabled(value);

		}
	}
}
