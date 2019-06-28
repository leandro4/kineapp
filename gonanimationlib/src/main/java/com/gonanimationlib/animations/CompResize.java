package com.gonanimationlib.animations;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewGroup;

public final class CompResize extends AnimationCompBasic {

    private ATTR side;
    private int measure;

    @Override
    public void startAnimation(View view) {

        ValueAnimator anim = ValueAnimator.ofInt(side.getMeasuredValue(view), measure);
        anim.addUpdateListener(valueAnimator -> {
            int val = (Integer) valueAnimator.getAnimatedValue();
            side.setMeasure(val, view); });

        anim.setDuration(duration);
        anim.setInterpolator(interpolator);
        anim.setStartDelay(delay);
        anim.addListener(buildAnimatorListener());
        anim.start();
    }

    CompResize (int measure, ATTR side) {
        this.measure = measure;
        this.side = side;
    }

    public enum ATTR {
        WIDTH() {
            @Override
            public void setMeasure(int value, View view) {
                view.getLayoutParams().width = value;
            }

            @Override
            public int getMeasuredValue(View view) {
                return view.getMeasuredWidth();
            }
        }, HEIGHT() {
            @Override
            public void setMeasure(int value, View view) {
                view.getLayoutParams().height = value;
            }
            @Override
            public int getMeasuredValue(View view) {
                return view.getMeasuredHeight();
            }
        };

        ATTR() {}

        public abstract void setMeasure(int value, View view);
        public abstract int getMeasuredValue(View view);
    }
}
