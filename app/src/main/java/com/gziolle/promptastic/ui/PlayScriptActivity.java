package com.gziolle.promptastic.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import be.rijckaert.tim.animatedvector.FloatingMusicActionButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
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
            mPlayContainer.setTextSize(Constants.PLAY_SCRIPT_DEFAULT_TEXT_SIZE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
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
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            mAnimator = ObjectAnimator.ofInt(mScrollView, "scrollY",0, mPlayContainer.getBottom());
            mAnimator.setDuration(Constants.PLAY_SCRIPT_VERY_SLOW_DURATION);
            mAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {}

                @Override
                public void onAnimationEnd(Animator animator) {
                    finishPlaying();
                }

                @Override
                public void onAnimationCancel(Animator animator) {}

                @Override
                public void onAnimationRepeat(Animator animator) {}
            });
            mAnimator.start();
        }, Constants.COUNTDOWN_TOTAL_TIME);
    }

    private void finishPlaying(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this)
                .setMessage(getString(R.string.play_script_dialog_message))
                .setPositiveButton(getString(R.string.play_script_dialog_ok), (dialog, which) -> {
                    mScrollView.scrollTo(0, 0);
                    mScrollView.setVisibility(View.GONE);
                    mFabArea.setVisibility(View.GONE);
                    mCountDownTextView.setVisibility(View.VISIBLE);
                    setupPlay();
                })
                .setNegativeButton(getString(R.string.play_script_dialog_cancel), (dialog, which) -> finish());
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
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
        mAnimator.pause();
        finishPlaying();
    }
}
