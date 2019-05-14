package com.wofang.demo.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.wofang.demo.mydemo.R;
import com.wofang.demo.utils.CommonUtil;


public class MyCustomDialog extends Dialog {

    Context context;

    public MyCustomDialog(Context context) {
        super(context);
        this.context = context;
    }

    public MyCustomDialog(Context context, int theme) {
        super(context, theme);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.my_custom_dialog);
    }
    /**
     * 有确定取消弹出对话框
     * @param context
     * @param title
     * @param content
     * @param btn_ok
     * @param btn_cancel
     * @param onOkCancelListener
     * @return
     */
    public static Dialog CustomDialogOkCancel(Context context, String title, String content, String btn_ok, String btn_cancel,
                                              final OnOkCancelListener onOkCancelListener) {
        final MyCustomDialog myDialog = new MyCustomDialog(context, R.style.dialogCommonStyle);
        myDialog.show();
        myDialog.findViewById(R.id.llBtn).setVisibility(View.VISIBLE);
        myDialog.findViewById(R.id.llTitle).setVisibility(View.VISIBLE);
        myDialog.findViewById(R.id.tvTitle).setVisibility(View.VISIBLE);

        myDialog.findViewById(R.id.vBtnLine).setVisibility(View.VISIBLE);

        ((TextView) myDialog.findViewById(R.id.tvTitle)).setText(title);

        myDialog.findViewById(R.id.btn_ok).setVisibility(View.VISIBLE);
        myDialog.findViewById(R.id.v_title_line).setVisibility(View.GONE);

        myDialog.findViewById(R.id.btn_cancel).setVisibility(View.VISIBLE);

        if (CommonUtil.isNull(btn_ok) || CommonUtil.isNull(btn_cancel)) {
            ((Button) myDialog.findViewById(R.id.btn_ok)).setText("确定");
            ((Button) myDialog.findViewById(R.id.btn_cancel)).setText("取消");

        } else {
            ((Button) myDialog.findViewById(R.id.btn_ok)).setText(btn_ok);
            ((Button) myDialog.findViewById(R.id.btn_cancel)).setText(btn_cancel);
        }

        if (!CommonUtil.isNull(content)) {
            myDialog.findViewById(R.id.ll_update).setVisibility(View.VISIBLE);
            myDialog.findViewById(R.id.tv_update_content).setVisibility(View.VISIBLE);
            ((TextView) myDialog.findViewById(R.id.tv_update_content)).setText(content);
        } else {
            ((TextView) myDialog.findViewById(R.id.tvTitle)).setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
            ((TextView) myDialog.findViewById(R.id.tvTitle)).setTextSize(12);
        }

        ((Button) myDialog.findViewById(R.id.btn_ok)).setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                myDialog.dismiss();
                onOkCancelListener.onOk();

            }

        });
        myDialog.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myDialog.dismiss();
                onOkCancelListener.onCancel();
            }
        });

        myDialog.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                onOkCancelListener.onDismiss();

            }
        });

        return myDialog;
    }

    /**
     * 密码框
     *
     * @param context
     * @param title
     * @param getPasswordListener
     * @return
     */
    public static Dialog CustomDialogPassword(Context context, String title, String btn_ok, String btn_cancel,
                                              final GetPasswordListener getPasswordListener) {
        final MyCustomDialog myDialog = new MyCustomDialog(context, R.style.dialogCommonStyle);
        try {
            myDialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
        myDialog.findViewById(R.id.llBtn).setVisibility(View.VISIBLE);
        myDialog.findViewById(R.id.llTitle).setVisibility(View.VISIBLE);
        myDialog.findViewById(R.id.btn_ok).setVisibility(View.VISIBLE);
        myDialog.findViewById(R.id.vBtnLine).setVisibility(View.VISIBLE);
        myDialog.findViewById(R.id.v_ok_cancel_line).setVisibility(View.GONE);
        myDialog.findViewById(R.id.btn_cancel).setVisibility(View.VISIBLE);
        myDialog.findViewById(R.id.llPhoneLine).setVisibility(View.GONE);
        myDialog.findViewById(R.id.imgClear).setVisibility(View.VISIBLE);
        myDialog.findViewById(R.id.llPassword).setVisibility(View.VISIBLE);
        EditText etPassword = (EditText) myDialog.findViewById(R.id.et_password);
        etPassword.setVisibility(View.VISIBLE);

        etPassword.setHint("请输入中文、英文或数字");

        if (!title.toString().trim().equals("")) {
            etPassword.setText(title);
            etPassword.setSelection(title.toString().trim().length());

        }
        //手动设置maxLength为12
        InputFilter[] filters = {new InputFilter.LengthFilter(12)};
        etPassword.setFilters(filters);

        etPassword.setHintTextColor(context.getResources().getColor(R.color.common_33000000));
        ImageView imgClear = (ImageView) myDialog.findViewById(R.id.imgClear);
        imgClear.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                ((EditText) myDialog.findViewById(R.id.et_password)).setText("");

            }
        });

        if (btn_ok.equals("") || btn_cancel.equals("")) {
            ((Button) myDialog.findViewById(R.id.btn_ok)).setText("确定");

            ((Button) myDialog.findViewById(R.id.btn_cancel)).setText("取消");

        } else {
            ((Button) myDialog.findViewById(R.id.btn_ok)).setText(btn_ok);
            ((Button) myDialog.findViewById(R.id.btn_cancel)).setText(btn_cancel);
            ((Button) myDialog.findViewById(R.id.btn_ok)).setTextColor(context.getResources().getColor(R.color.common_ff0000ff));

            ((Button) myDialog.findViewById(R.id.btn_cancel)).setTextColor(context.getResources().getColor(R.color.common_ff6600));
        }


        // ((EditText)myDialog.findViewById(R.id.et_password)).getText().toString().trim();
        ((Button) myDialog.findViewById(R.id.btn_ok)).setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                myDialog.dismiss();

                getPasswordListener.onCancel();
            }
        });
        myDialog.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getPasswordListener.onOk(((EditText) myDialog.findViewById(R.id.et_password)).getText().toString().trim());

            }
        });

        myDialog.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                getPasswordListener.onDismiss();

            }
        });

        return myDialog;
    }

    /**
     * 只有关闭的弹出框
     *
     * @param context
     * @param title
     * @param content
     * @param onCloseListener
     * @return
     */
    public static Dialog CustomDialogClose(Context context, String title, String content, int type, final OnCloseListener onCloseListener) {
        final MyCustomDialog myDialog = new MyCustomDialog(context, R.style.dialogCommonStyle);
        myDialog.show();

        myDialog.findViewById(R.id.llBtn).setVisibility(View.GONE);
        myDialog.findViewById(R.id.llTitle).setVisibility(View.GONE);

        myDialog.findViewById(R.id.ll_close).setVisibility(View.VISIBLE);

        ((TextView) myDialog.findViewById(R.id.tvDescription_title)).setText(title);

        TextView tv_content = (TextView) myDialog.findViewById(R.id.tv_content);

        tv_content.setText(content);
        if (type == 1) {
            tv_content.setGravity(Gravity.LEFT);

        } else {
            tv_content.setGravity(Gravity.CENTER);
        }

        ((ScrollView) myDialog.findViewById(R.id.sv_description)).setVisibility(View.VISIBLE);

        ((ImageButton) myDialog.findViewById(R.id.btnClose)).setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                myDialog.dismiss();
                onCloseListener.onClose();
            }
        });
        myDialog.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                onCloseListener.onDismiss();

            }
        });
        return myDialog;
    }

    /**
     * 只有确定弹出对话框
     *
     * @param context
     * @param title
     * @param content
     * @param onOkListener
     * @return
     */
    public static Dialog CustomDialogOk(Context context, String title, String content, String btn_ok, final OnOkListener onOkListener) {
        final MyCustomDialog myDialog = new MyCustomDialog(context, R.style.dialogCommonStyle);
        myDialog.show();
        myDialog.findViewById(R.id.llTitle).setVisibility(View.VISIBLE);
        myDialog.findViewById(R.id.llBtn).setVisibility(View.VISIBLE);

        myDialog.findViewById(R.id.vBtnLine).setVisibility(View.GONE);
        myDialog.findViewById(R.id.tvTitle).setVisibility(View.VISIBLE);
        ((TextView) myDialog.findViewById(R.id.tvTitle)).setText(title);
        myDialog.findViewById(R.id.btn_ok).setVisibility(View.VISIBLE);
        myDialog.findViewById(R.id.v_title_line).setVisibility(View.GONE);
        if (CommonUtil.isNull(btn_ok)) {
            ((Button) myDialog.findViewById(R.id.btn_ok)).setText("确定");

        } else {
            ((Button) myDialog.findViewById(R.id.btn_ok)).setText(btn_ok);

        }

        if (!CommonUtil.isNull(content)) {
            myDialog.findViewById(R.id.ll_update).setVisibility(View.VISIBLE);
            myDialog.findViewById(R.id.tv_update_content).setVisibility(View.VISIBLE);
            ((TextView) myDialog.findViewById(R.id.tv_update_content)).setText(content);
        } else {
            ((TextView) myDialog.findViewById(R.id.tvTitle)).setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
            ((TextView) myDialog.findViewById(R.id.tvTitle)).setTextSize(12);


        }

        ((Button) myDialog.findViewById(R.id.btn_ok)).setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                myDialog.dismiss();
                onOkListener.onOk();
            }
        });
        myDialog.findViewById(R.id.btn_cancel).setVisibility(View.GONE);
        myDialog.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                onOkListener.onDismiss();

            }
        });
        return myDialog;
    }

    /**
     * 有确定取消弹出对话框 带手机号加验证码
     *
     * @param context
     * @param title
     * @param content
     * @return
     */
    public static Dialog CustomDialogOkCancelVerifyPhone(final Context context, String title, String content, String btn_ok,
                                                         String btn_cancel, final OnOkCancelVerifyListener onOkCancelVerifyListener) {
        final MyCustomDialog myDialog = new MyCustomDialog(context, R.style.dialogCommonStyle);
        myDialog.show();
        myDialog.findViewById(R.id.llTitle).setVisibility(View.VISIBLE);

        myDialog.findViewById(R.id.llBtn).setVisibility(View.VISIBLE);
        myDialog.findViewById(R.id.tvTitle).setVisibility(View.VISIBLE);
        myDialog.findViewById(R.id.vVerify).setVisibility(View.VISIBLE);
        myDialog.findViewById(R.id.vBtnLine).setVisibility(View.VISIBLE);

        ((TextView) myDialog.findViewById(R.id.tvTitle)).setGravity(Gravity.CENTER);
        myDialog.findViewById(R.id.llPhone).setVisibility(View.VISIBLE);
        myDialog.findViewById(R.id.llVerify).setVisibility(View.VISIBLE);

        ((TextView) myDialog.findViewById(R.id.tvTitle)).setText(title);

        myDialog.findViewById(R.id.btn_ok).setVisibility(View.VISIBLE);

        myDialog.findViewById(R.id.v_title_line).setVisibility(View.VISIBLE);

        myDialog.findViewById(R.id.btn_cancel).setVisibility(View.VISIBLE);

        EditText etPhone = (EditText) myDialog.findViewById(R.id.etPhone);
        etPhone.setText(content);
        etPhone.setEnabled(false);
        TextView tvTime = (TextView) myDialog.findViewById(R.id.tvTime);
        EditText etVerify = (EditText) myDialog.findViewById(R.id.etVerify);

        myDialog.findViewById(R.id.btn_cancel).setBackgroundColor(context.getResources().getColor(R.color.app_green));
        ((TextView) myDialog.findViewById(R.id.btn_cancel)).setTextColor(context.getResources().getColor(R.color.common_ffffffff));

        if (btn_ok.equals("") || btn_cancel.equals("")) {
            ((Button) myDialog.findViewById(R.id.btn_ok)).setText("确定");
            ((Button) myDialog.findViewById(R.id.btn_cancel)).setText("取消");

        } else {
            ((Button) myDialog.findViewById(R.id.btn_ok)).setText(btn_ok);
            ((Button) myDialog.findViewById(R.id.btn_cancel)).setText(btn_cancel);
        }
        ((Button) myDialog.findViewById(R.id.btn_ok)).setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                myDialog.dismiss();
                onOkCancelVerifyListener.onOk();

            }

        });

        myDialog.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText etPhone = (EditText) myDialog.findViewById(R.id.etPhone);
                EditText etVerify = (EditText) myDialog.findViewById(R.id.etVerify);

                if (CommonUtil.isNull(etPhone.getText().toString().trim())) {
                    Toast.makeText(context, "请输入手机号", Toast.LENGTH_SHORT).show();

                } else if (CommonUtil.isNull(etVerify.getText().toString().trim())) {
                    Toast.makeText(context, "请输入验证码", Toast.LENGTH_SHORT).show();

                } else {

                    onOkCancelVerifyListener.onCancel(etPhone.getText().toString().trim(), etVerify.getText().toString().trim());

                }

            }
        });

        myDialog.findViewById(R.id.tvTime).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText etPhone = (EditText) myDialog.findViewById(R.id.etPhone);

                TextView tvTime = (TextView) myDialog.findViewById(R.id.tvTime);
                EditText etVerify = (EditText) myDialog.findViewById(R.id.etVerify);
                if (!CommonUtil.isNull(etPhone.getText().toString().trim())) {

                    onOkCancelVerifyListener.onVerify(etPhone.getText().toString().trim(), tvTime, etVerify);

                } else {
                    Toast.makeText(context, "请输入手机号", Toast.LENGTH_SHORT).show();
                }

            }
        });

        myDialog.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                onOkCancelVerifyListener.onDismiss();

            }
        });
        return myDialog;
    }

    /**
     * 有同意和拒绝弹出对话框
     *
     * @return
     */

    public static Dialog CustomDialogAgreeAndRefuse(Context context, String title, String content, String refuse, String agree,
                                                    final OnAgreeAndRefuseListener onAgreeAndRefuseListener) {
        final MyCustomDialog myDialog = new MyCustomDialog(context, R.style.dialogCommonStyle);
        myDialog.show();

        myDialog.findViewById(R.id.llAgreeAndRefuse).setVisibility(View.VISIBLE);
        ((TextView) myDialog.findViewById(R.id.tvTitle)).setText(title);

        if (agree.equals("") || refuse.equals("")) {
            ((Button) myDialog.findViewById(R.id.btnAgree)).setText("同意");
            ((Button) myDialog.findViewById(R.id.btnRefuse)).setText("拒绝");

        } else {
            ((Button) myDialog.findViewById(R.id.btnAgree)).setText(agree);
            ((Button) myDialog.findViewById(R.id.btnRefuse)).setText(refuse);
        }
        ((Button) myDialog.findViewById(R.id.btnAgree)).setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                myDialog.dismiss();
                onAgreeAndRefuseListener.onAgree();

            }

        });
        myDialog.findViewById(R.id.btnRefuse).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myDialog.dismiss();
                onAgreeAndRefuseListener.onRefuse();
            }
        });
        myDialog.findViewById(R.id.tvIsAgree).setSelected(true);
        myDialog.findViewById(R.id.tvIsAgree).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myDialog.findViewById(R.id.tvIsAgree).isSelected()) {
                    onAgreeAndRefuseListener.onIsAgree(0);
                    ((TextView) myDialog.findViewById(R.id.tvIsAgree)).setSelected(false);

                } else {
                    onAgreeAndRefuseListener.onIsAgree(1);
                    ((TextView) myDialog.findViewById(R.id.tvIsAgree)).setSelected(true);


                }


            }
        });

        myDialog.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                onAgreeAndRefuseListener.onDismiss();

            }
        });

        return myDialog;
    }



    /**
     * 普通对话框
     *
     * @author Administrator
     */
    public interface OnOkCancelListener {
        void onOk();

        void onCancel();

        void onDismiss();

    }

    public interface OnAgreeAndRefuseListener {
        void onAgree();

        void onRefuse();

        void onIsAgree(int type);

        void onDismiss();


    }

    /**
     * 普通对话框
     *
     * @author Administrator
     */
    public interface OnOkCancelVerifyListener {
        void onOk();

        void onCancel(String phone, String verify);

        void onVerify(String phone, TextView tvTiew, EditText etVerify);// 传递TextView

        void onDismiss();


    }

    /**
     * ok对话框
     */
    public interface OnOkListener {
        void onOk();

        void onDismiss();
    }

    /**
     * close对话框
     */
    public interface OnCloseListener {
        void onClose();

        void onDismiss();
    }

    /**
     * 密码对话框
     *
     * @author Administrator
     */
    public interface GetPasswordListener {
        void onOk(String password);

        void onCancel();

        void onDismiss();

    }

}
