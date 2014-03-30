/*
 * Copyright 2013, Green Halo Labs LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this work except in compliance with the License.
 * You may obtain a copy of the License in the LICENSE file, or at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package com.greenhalolabs.emailautocompletetextview;

import java.util.List;

import pl.devcrowd.app.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.greenhalolabs.emailautocompletetextview.util.AccountUtil;

/**
 * <p>
 * An editable text view that provides auto completion for the user's e-mail.
 * The list of suggestions is displayed in a drop down menu from which the user
 * can choose an e-mail to replace the content of the edit box with.
 * </p>0
 * <p>0
 * An "X" button will also be present to the right of the text view if edit box
 * is not empty so that the view can easily be cleared.
 * </p>
 */
public class EmailAutoCompleteTextView extends AutoCompleteTextView implements
		View.OnTouchListener, View.OnFocusChangeListener {

	private static final String TAG = EmailAutoCompleteTextView.class.getName();

	private static final int DEFAULT_CLEAR_BUTTON = R.drawable.ic_clear;

	/* Private State */

	private Drawable mTappableDrawable;
	private OnTouchListener mOnTouchListener;
	private OnFocusChangeListener mOnFocusChangeListener;
	private OnClearClicked mOnClearClickListener;
	private int mClearButtonResId = DEFAULT_CLEAR_BUTTON;
	private boolean mClearButtonEnabled = true;

	/* Constructors */

	public EmailAutoCompleteTextView(Context context) {
		super(context);
		init(context, null, 0);
	}

	public EmailAutoCompleteTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs, 0);
	}

	public EmailAutoCompleteTextView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs, defStyle);
	}

	/* Public Methods */

	@Override
	public void setOnTouchListener(OnTouchListener l) {
		mOnTouchListener = l;
	}

	@Override
	public void setOnFocusChangeListener(OnFocusChangeListener l) {
		mOnFocusChangeListener = l;
	}

	/**
	 * Sets a listener for when the clear button is clicked.
	 * 
	 * @param listener
	 *            the listener.
	 */
	public void setOnClearClickListener(OnClearClicked listener) {
		mOnClearClickListener = listener;
	}

	/**
	 * Sets the visibility of the clear button. The clear button must also be
	 * enabled for it to be visible.
	 * 
	 * @see {@link #setClearButtonEnabled(boolean)}
	 * @param visible
	 *            true if the clear button should be visible, otherwise, false.
	 */
	public void setClearVisible(boolean visible) {
		if (mClearButtonEnabled) {
			final Drawable d = (visible ? mTappableDrawable : null);
			final Drawable[] drawables = getCompoundDrawables();
			if (drawables != null) {
				setCompoundDrawables(drawables[0], drawables[1], d,
						drawables[3]);
			} else {
				Log.w(TAG, "No clear button is available.");
			}
		}
	}

	/**
	 * @return true if the button is visible, otherwise, false.
	 */
	public boolean isButtonVisible() {
		final Drawable[] drawables = getCompoundDrawables();
		return (drawables != null && drawables[2] != null);
	}

	/**
	 * @return true if the clear button is enabled, otherwise, false.
	 */
	public boolean isClearButtonEnabled() {
		return mClearButtonEnabled;
	}

	/**
	 * Sets if the clear button should be enabled or not (default is true).
	 * 
	 * @param enabled
	 *            true if it should be enabled, false otherwise.
	 */
	public void setClearButtonEnabled(boolean enabled) {
		mClearButtonEnabled = enabled;
		initClearButton();
	}

	/**
	 * Sets the clear button drawable (the clear button must be enabled).
	 * 
	 * @see {@link #setClearButtonEnabled(boolean)}
	 * @param resId
	 *            the drawable resource ID.
	 */
	public void setClearButtonResId(int resId) {
		mClearButtonResId = resId;
		initClearButton();
	}

	/* Private Methods */

	private void initClearButton() {
		if (mClearButtonEnabled) {
			final Drawable[] drawables = getCompoundDrawables();
			mTappableDrawable = ((drawables != null && drawables[2] != null) ? drawables[2]
					: getContext().getResources()
							.getDrawable(mClearButtonResId));
			if (mTappableDrawable != null) {
				mTappableDrawable.setBounds(0, 0,
						mTappableDrawable.getIntrinsicWidth(),
						mTappableDrawable.getIntrinsicHeight());
				setOnClearClickListener(new DefaultClearButtonClickListener(
						this));
			}
		}
	}

	private void init(Context context, AttributeSet attrs, int defStyle) {

		if (attrs != null) {
			final TypedArray a = context.obtainStyledAttributes(attrs,
					R.styleable.EmailAutoCompleteTextView, defStyle, 0);

			mClearButtonEnabled = a
					.getBoolean(
							R.styleable.EmailAutoCompleteTextView_setClearButtonEnabled,
							true);
			mClearButtonResId = a.getResourceId(
					R.styleable.EmailAutoCompleteTextView_clearButtonDrawable,
					DEFAULT_CLEAR_BUTTON);

			a.recycle();
		}

		// Initialize clear button
		initClearButton();

		// Get e-mails
		final List<String> emailsList = AccountUtil.getAccountEmails(context);
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
				android.R.layout.simple_dropdown_item_1line, emailsList);
		setAdapter(adapter);
		setClearVisible(false);
		setSelectAllOnFocus(true);
		setInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
		super.setOnFocusChangeListener(this);
		super.setOnTouchListener(this);
		addTextChangedListener(new DefaultTextChangedListener(this));
	}

	/* Implemented Methods */

	@Override
	public void onFocusChange(View v, boolean hasFocus) {

		final EmailAutoCompleteTextView editText = (EmailAutoCompleteTextView) v;
		if (mClearButtonEnabled) {
			editText.setClearVisible((hasFocus && !TextUtils.isEmpty(editText
					.getText().toString())));
		}

		if (mOnFocusChangeListener != null) {
			mOnFocusChangeListener.onFocusChange(v, hasFocus);
		}
	}

	@Override
	public boolean onTouch(View view, MotionEvent event) {

		if (isButtonVisible()) {
			final boolean tappedButton = event.getX() > (getWidth()
					- getPaddingRight() - mTappableDrawable.getIntrinsicWidth());
			if (tappedButton) {
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (mClearButtonEnabled && mOnClearClickListener != null) {
						mOnClearClickListener.onClick();
					}
				}
				return true;
			}
		}

		// Propagate
		return (mOnTouchListener != null && mOnTouchListener.onTouch(view,
				event));
	}

	/* Inner Classes */

	public static final class DefaultClearButtonClickListener implements
			OnClearClicked {

		private final EmailAutoCompleteTextView editTextPlus;

		public DefaultClearButtonClickListener(
				EmailAutoCompleteTextView editTextPlus) {
			this.editTextPlus = editTextPlus;
		}

		@Override
		public void onClick() {
			editTextPlus.setText("");
		}
	}

	public static final class DefaultTextChangedListener implements TextWatcher {

		private final EmailAutoCompleteTextView editTextPlus;

		public DefaultTextChangedListener(EmailAutoCompleteTextView editTextPlus) {
			this.editTextPlus = editTextPlus;
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			if (editTextPlus.isClearButtonEnabled()) {
				editTextPlus.setClearVisible(editTextPlus.hasFocus()
						&& !TextUtils
								.isEmpty(editTextPlus.getText().toString()));
			}
		}

		@Override
		public void afterTextChanged(Editable s) {
		}
	}
}
