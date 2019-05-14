package com.wofang.demo.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Create by wcm
 * on 2019/5/7 0007
 */
public interface BaseView {
    void onPresenterEvent(int code, @Nullable Bundle bundle);
}
