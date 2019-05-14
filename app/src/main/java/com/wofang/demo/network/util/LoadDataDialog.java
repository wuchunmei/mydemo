package com.wofang.demo.network.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

import com.wofang.demo.mydemo.R;


/**
 * Created by wuchunmei on 9/15/17.
 */

public class LoadDataDialog extends ProgressDialog {

    private final String message;
    private final Activity mParentActivity;

    public LoadDataDialog(Context context, String message) {
        super(context, R.style.Loading_Dialog);
        this.message = message;
        mParentActivity = (Activity) context;
        setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_dialog_view);

        LayoutParams params = getWindow().getAttributes();
        params.width = LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.CENTER;

        getWindow().setAttributes(params);

        WindowManager.LayoutParams lp =(mParentActivity.getWindow().getAttributes());
        lp.alpha = 0.5f;
        (mParentActivity).getWindow().setAttributes(lp);

        TextView messageText = (TextView) this.findViewById(R.id.message);
        messageText.setText(message);
    }

    public Activity getParentActivity() {
        return mParentActivity;
    }

}
