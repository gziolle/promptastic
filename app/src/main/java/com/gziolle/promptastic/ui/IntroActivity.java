package com.gziolle.promptastic.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.material.button.MaterialButton;
import com.gziolle.promptastic.R;
import com.gziolle.promptastic.adapter.IntroPagerAdapter;

public class IntroActivity extends AppCompatActivity {

    @BindView(R.id.intro_view_pager)
    ViewPager mIntroViewPager;

    @BindView(R.id.bt_intro_prev)
    MaterialButton mPrevButton;

    @BindView(R.id.bt_intro_got_it)
    MaterialButton mGotItButton;

    @BindView(R.id.bt_intro_next)
    MaterialButton mNextButton;

    @BindView(R.id.ll_intro_dots)
    LinearLayout mDotsLayout;

    private IntroPagerAdapter mPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        ButterKnife.bind(this);

        mPagerAdapter = new IntroPagerAdapter(this);
        mIntroViewPager.setAdapter(mPagerAdapter);

        mPrevButton.setOnClickListener(v -> mIntroViewPager.arrowScroll(View.FOCUS_LEFT));

        mNextButton.setOnClickListener(v -> mIntroViewPager.arrowScroll(View.FOCUS_RIGHT));

        mGotItButton.setOnClickListener(v -> {
            Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        mIntroViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                if(position == mPagerAdapter.getCount() - 1){
                    mNextButton.setVisibility(View.GONE);
                    mGotItButton.setVisibility(View.VISIBLE);
                } else{
                    mGotItButton.setVisibility(View.GONE);
                    mNextButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
