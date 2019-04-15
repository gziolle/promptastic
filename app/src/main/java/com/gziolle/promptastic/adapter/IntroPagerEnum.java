package com.gziolle.promptastic.adapter;

import com.gziolle.promptastic.R;

enum IntroPagerEnum {
    PAGE1(R.layout.intro_slide_1),
    PAGE2(R.layout.intro_slide_2),
    PAGE3(R.layout.intro_slide_3);

    private int mLayoutResId;

    IntroPagerEnum(int mLayoutResId) {
        this.mLayoutResId = mLayoutResId;
    }

    public int getLayoutResId() {
        return mLayoutResId;
    }
}
