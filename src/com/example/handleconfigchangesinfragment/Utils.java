package com.example.handleconfigchangesinfragment;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class Utils {

	/**
	 * Show the SoftKeyboard when the user tap on some View
	 * 
	 * @param context
	 * @param view
	 *            tapped view
	 */
	public void showSoftKeyboard(Context context, View view) {

		if (view.requestFocus()) {

			InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
		}
	}
}
