package cn.wl.base;

import android.os.Bundle;

import cn.wl.android.lib.ui.BaseActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected Object getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViewCreated(Bundle savedInstanceState) {
    }
}
