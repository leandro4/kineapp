package com.gonanimationlib.animations;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;

public final class CompColor extends AnimationCompBasic {

    private int colorFrom = Color.WHITE;
    private int colorTo;
    private boolean useActualColor = true;

    @Override
    public void startAnimation(View view) {

        if (useActualColor && view.getBackground() instanceof ColorDrawable)
            colorFrom = ((ColorDrawable) view.getBackground()).getColor();

        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setDuration(duration);
        colorAnimation.setStartDelay(delay);
        colorAnimation.addUpdateListener(valueAnim -> view.setBackgroundColor((int) valueAnim.getAnimatedValue()));
        colorAnimation.addListener(buildAnimatorListener());
        colorAnimation.start();

    }

    public CompColor setColorFrom(int colorFrom) {
        useActualColor = false;
        this.colorFrom = colorFrom;
        return this;
    }

    CompColor(int colorTo) {
        this.colorTo = colorTo;
    }
}
