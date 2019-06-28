package com.gonanimationlib.animations;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.view.View;

public final class CompMove extends AnimationCompBasic {

    private float x;
    private float y;
    private boolean centerPosition = false;

    @Override
    public void startAnimation(View view) {

        if (centerPosition) {
            x -= view.getWidth()/2;
            y -= view.getHeight()/2;
        }

        final PropertyValuesHolder translationX = PropertyValuesHolder.ofFloat(View.X, x);
        final PropertyValuesHolder translationY = PropertyValuesHolder.ofFloat(View.Y, y);
        view.setLayerType(View.LAYER_TYPE_HARDWARE, null);

        AnimatorSet animationSet = new AnimatorSet();
        animationSet.playTogether(ObjectAnimator.ofPropertyValuesHolder(view, translationX, translationY));
        animationSet.setDuration(duration);
        animationSet.setInterpolator(interpolator);
        animationSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.setLayerType(View.LAYER_TYPE_NONE, null);
                if (onEnd != null) onEnd.call();
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                if (onRepeat != null) onRepeat.call();
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                if (onStart != null) onStart.call();
            }
        });
        animationSet.start();
    }

    CompMove(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public CompMove isCenter (boolean centerPosition) {
        this.centerPosition = centerPosition;
        return this;
    }
}
