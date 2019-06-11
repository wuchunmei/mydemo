package com.wofang.demo.view;

import android.content.ClipboardManager;
import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasteEditText extends AppCompatEditText {
    private Context mContext;

    public PasteEditText(Context context) {
        super(context);
        this.mContext = context;
        this.setFilters(new InputFilter[]{filter_space, filter_speChat, new InputFilter.LengthFilter(11)});
        setFocusableInTouchMode(true);
        setFocusable(true);
        requestFocus();
    }

    public PasteEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        this.setFilters(new InputFilter[]{filter_space, filter_speChat, new InputFilter.LengthFilter(11)});
        setFocusableInTouchMode(true);
        setFocusable(true);
        requestFocus();
    }

    @Override
    public boolean onTextContextMenuItem(int id) {
        switch (id) {
            case android.R.id.paste:
                Log.d("wu", "粘贴");
               onTextPaste();
               break;
        }
        return super.onTextContextMenuItem(id);
    }

    private void onTextPaste() {
        ClipboardManager clipboardManager = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        //改变剪贴板中Content
        if (clipboardManager != null && clipboardManager.getText() != null) {
            String decResult = clipboardManager.getText().toString();
            if (!TextUtils.isEmpty(decResult)) {
                String mobile = decResult.replaceAll(" ", "");
                String phone = mobile.replaceAll("-", "");
                // 改变文本内容
                this.setText(phone);
                if(null != phone && phone.length() <= 11){
                    this.setSelection(phone.length());
                }
            }
        }
    }

        InputFilter filter_space = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (source.equals(" ") || source.equals("-"))
                    return "";
                else
                    return null;
            }
        };
        InputFilter filter_speChat = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence charSequence, int i, int i1, Spanned spanned, int i2, int i3) {
                String speChat = "[`~!@#_$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）—  +|{}【】‘；：”“’。，、？]";
                Pattern pattern = Pattern.compile(speChat);
                Matcher matcher = pattern.matcher(charSequence.toString());
                if (matcher.find()) return "";
                else return null;
            }
        };

    }
