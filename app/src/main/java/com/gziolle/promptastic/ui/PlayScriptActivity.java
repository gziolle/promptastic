/*
 * Copyright (C) 2019 Guilherme Ziolle
 */

package com.gziolle.promptastic.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;
import be.rijckaert.tim.animatedvector.FloatingMusicActionButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.gziolle.promptastic.R;
import com.gziolle.promptastic.util.Constants;

import java.util.Objects;

/*
 * Manages script playing, pausing and rewind.
 */

public class PlayScriptActivity extends AppCompatActivity {

    @BindView(R.id.sv_play_scroll_view)
    ScrollView mScrollView;

    @BindView(R.id.tv_play_text_container)
    TextView mPlayContainer;

    @BindView(R.id.tv_play_script_countdown)
    TextView mCountDownTextView;

    @BindView(R.id.fab_pause_script)
    FloatingMusicActionButton mPauseButton;

    @BindView(R.id.fab_area)
    LinearLayout mFabArea;

    ObjectAnimator mAnimator;
    CountDownTimer mCountDown;

    Handler mHandler;
    AlertDialog mAlertDialog;

    int mTextDuration;
    boolean mIsRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_script);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle != null){
            String text = bundle.getString(Constants.KEY_CONTENT);
            mPlayContainer.setText(text);
            setupScreen();
        } else{
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mIsRunning = true;
        setupPlay();
    }

    private void setupPlay(){
        Animation fadeOutAnimation = AnimationUtils.loadAnimation(this, android.R.anim.fade_out);
        fadeOutAnimation.setDuration(Constants.COUNTDOWN_TIME_INTERVAL);

        Animation fadeInAnimation = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        fadeInAnimation.setDuration(Constants.COUNTDOWN_TIME_INTERVAL);

        mCountDown = new CountDownTimer(Constants.COUNTDOWN_TOTAL_TIME, Constants.COUNTDOWN_TIME_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                long currentTime = (millisUntilFinished / 1000) + 1;
                mCountDownTextView.setText(String.valueOf(currentTime));
                mCountDownTextView.startAnimation(fadeOutAnimation);
            }

            @Override
            public void onFinish() {
                mCountDownTextView.setVisibility(View.GONE);
                mScrollView.startAnimation(fadeInAnimation);
                mScrollView.setVisibility(View.VISIBLE);
                mFabArea.setVisibility(View.VISIBLE);
                playScript();
            }
        };
        mCountDown.start();
    }

    private void playScript(){
        mHandler = new Handler();
        mHandler.postDelayed(() -> {
            mAnimator = ObjectAnimator.ofInt(mScrollView, "scrollY",0, mPlayContainer.getBottom());
            mAnimator.setDuration(mTextDuration);
            mAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {}

                @Override
                public void onAnimationEnd(Animator animator) {
                    if(mIsRunning){
                        finishPlaying();
                    }
                }

                @Override
                public void onAnimationCancel(Animator animator) { }

                @Override
                public void onAnimationRepeat(Animator animator) {}
            });
            mAnimator.start();
        }, Constants.COUNTDOWN_TOTAL_TIME);
    }

    private void finishPlaying(){
        if((mAlertDialog == null) || (!mAlertDialog.isShowing())){
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this)
                    .setMessage(getString(R.string.play_script_dialog_message))
                    .setPositiveButton(getString(R.string.play_script_dialog_ok), (dialog, which) -> {
                        mHandler.removeCallbacksAndMessages(null);
                        if(mAnimator != null){
                            mAnimator.cancel();
                        }
                        mScrollView.scrollTo(0, 0);
                        mScrollView.setVisibility(View.GONE);
                        mFabArea.setVisibility(View.GONE);
                        mCountDownTextView.setVisibility(View.VISIBLE);
                        setupPlay();
                    })
                    .setNegativeButton(getString(R.string.play_script_dialog_cancel), (dialog, which) -> {
                        if(mAnimator != null){
                            mAnimator.cancel();
                            mAnimator.removeAllListeners();
                        }
                        finish();
                    });
            mAlertDialog = dialogBuilder.create();
            mAlertDialog.show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mIsRunning = false;
        mCountDown.cancel();
    }

    @OnClick(R.id.fab_pause_script)
    public void pauseScript(){
        mPauseButton.playAnimation();
        if(mAnimator != null){
            if(!mAnimator.isPaused()){
                mAnimator.pause();
            } else{
                mAnimator.resume();
            }
        }
    }

    @OnClick(R.id.fab_stop_script)
    public void stopPlaying(){
        if(mAnimator != null && !mAnimator.isPaused()){
            mAnimator.pause();
        }
        finishPlaying();
    }

    private void setupScreen(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        String theme = prefs.getString("play_theme", Constants.PLAY_SCRIPT_THEME_LIGHT);

        if(Constants.PLAY_SCRIPT_THEME_LIGHT.equals(theme)){
            mScrollView.setBackgroundColor(ContextCompat.getColor(this,R.color.play_script_background_color_light));
            mPlayContainer.setTextColor(ContextCompat.getColor(this, R.color.play_script_text_color_light));
        }else if(Constants.PLAY_SCRIPT_THEME_DARK.equals(theme)){
            mScrollView.setBackgroundColor(ContextCompat.getColor(this,R.color.play_script_background_color_dark));
            mPlayContainer.setTextColor(ContextCompat.getColor(this, R.color.play_script_text_color_dark));
        }

        mTextDuration = Integer.parseInt(Objects.requireNonNull(prefs.getString("text_speed", Constants.PLAY_SCRIPT_DEFAULT_DURATION)));

        float textSize = Float.parseFloat(Objects.requireNonNull(prefs.getString("text_size", Constants.PLAY_SCRIPT_DEFAULT_TEXT_SIZE)));
        mPlayContainer.setTextSize(textSize);
    }
}
