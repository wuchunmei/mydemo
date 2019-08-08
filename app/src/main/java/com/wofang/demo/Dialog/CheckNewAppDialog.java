package com.wofang.demo.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wofang.demo.R;


public class CheckNewAppDialog extends Dialog {

	private Context context;

	private TextView mTextTitle;
	private TextView mTextWarning;
	private LinearLayout mLayoutButtons;
	private Button mBtnConfirm;
	private Button mBtnCancel;
	private View mVerticalLine;

	private OnCheckNewAppDialogResult mDialogResult;

	public CheckNewAppDialog(Context context) {
		super(context);
		this.context = context;
	}

	public CheckNewAppDialog(Context context, int theme) {
		super(context, theme);
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		View view = LayoutInflater.from(context).inflate(R.layout.dialog_check_new_app, null);
		setContentView(view);

		setupLayout(view);
	}

	private void setupLayout(View view) {
		mTextTitle = (TextView) view.findViewById(R.id.textTitle);

		mTextWarning = (TextView) view.findViewById(R.id.textWarning);

		mLayoutButtons = (LinearLayout) view.findViewById(R.id.layoutButtons);

		mVerticalLine = (View) view.findViewById(R.id.verticalLine);

		mBtnConfirm = (Button) view.findViewById(R.id.btnConfirm);
		mBtnConfirm.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mDialogResult != null) {
					mDialogResult.finish(true);
				}
				dismiss();


			}
		});

		mBtnCancel = (Button) view.findViewById(R.id.btnCancel);
		mBtnCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
	}

	public void setDialogResult(OnCheckNewAppDialogResult dialogResult) {
		mDialogResult = dialogResult;
	}

	public CheckNewAppDialog setTitle(String title) {
		mTextTitle.setText(title);
		return this;
	}

	public CheckNewAppDialog setTitleVisible(boolean bVisible) {
		if (bVisible) {
			mTextTitle.setVisibility(View.VISIBLE);
		} else {
			mTextTitle.setVisibility(View.GONE);
		}
		return this;
	}

	public CheckNewAppDialog setWarning(String warning) {
		mTextWarning.setText(warning);
		return this;
	}

	public CheckNewAppDialog setWarning(String warning, boolean bAlignLeft) {
		mTextWarning.setText(warning);
		if (bAlignLeft) {
			mTextWarning.setGravity(Gravity.LEFT);
		}
		return this;
	}

	/**
	 * 设置提示文字的行间距
	 * 
	 * @param spacing 增加的间距，单位px
	 * @return
	 */
	public CheckNewAppDialog setWarningLineSpacing(float spacing) {
		mTextWarning.setLineSpacing(spacing, 1);
		return this;
	}

	public CheckNewAppDialog setButtonsVisible(boolean bVisible) {
		if (bVisible) {
			mLayoutButtons.setVisibility(View.VISIBLE);
		} else {
			mLayoutButtons.setVisibility(View.GONE);
		}
		return this;
	}

	public CheckNewAppDialog setCancelButtonVisible(boolean bVisible) {
		if (bVisible) {
			mBtnCancel.setVisibility(View.VISIBLE);
			mVerticalLine.setVisibility(View.VISIBLE);
		} else {
			mBtnCancel.setVisibility(View.GONE);
			mVerticalLine.setVisibility(View.GONE);
		}
		return this;
	}

	public CheckNewAppDialog setConfirmText(String text) {
		mBtnConfirm.setText(text);
		return this;
	}

	public CheckNewAppDialog setCancelText(String text) {
		mBtnCancel.setText(text);
		return this;
	}

	/**
	 * must be called before dialog.show()
	 */
	@Override
	public void setCancelable(boolean cancellable) {
		super.setCancelable(cancellable);
	}

	/**
	 * must be called before dialog.show()
	 */
	@Override
	public void setCanceledOnTouchOutside(boolean cancellable) {
		super.setCanceledOnTouchOutside(cancellable);
	}

	public interface OnCheckNewAppDialogResult {
		void finish(boolean bIsConfirmed);
	}


}
