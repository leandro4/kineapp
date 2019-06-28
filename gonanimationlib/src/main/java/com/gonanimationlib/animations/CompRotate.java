package com.gonanimationlib.animations;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

public final class CompRotate extends AnimationCompBasic {

    private int fromDegrees = 0;
    private int toDegrees;
    private float pivoteX = 0.5f;
    private float pivoteY = 0.5f;
    private boolean standFinalPos = true;

    @Override
    public void startAnimation(View view) {

        Animation rotate = new RotateAnimation(fromDegrees, toDegrees, Animation.RELATIVE_TO_SELF, pivoteX,
                                                                       Animation.RELATIVE_TO_SELF, pivoteY);
        rotate.setInterpolator(interpolator);
        rotate.setDuration(duration);
        rotate.setRepeatCount(repeat);
        rotate.setStartOffset(delay);
        rotate.setFillAfter(standFinalPos);
        rotate.setAnimationListener(buildAnimationListener());
        view.startAnimation(rotate);

    }

    CompRotate(int toDegrees) {
        this.toDegrees = toDegrees;
    }

    public CompRotate fromDegrees(int fromDegrees) {
        this.fromDegrees = fromDegrees;
        return this;
    }

    public CompRotate pivoteX(float pivoteX) {
        this.pivoteX = pivoteX;
        return this;
    }

    public CompRotate pivoteY(float pivoteY) {
        this.pivoteY = pivoteY;
        return this;
    }

    public CompRotate finalPosition(boolean standFinalPos) {
        this.standFinalPos = standFinalPos;
        return this;
    }

    public CompRotate infinitely() {
        this.repeat = Animation.INFINITE;
        return this;
    }
}
