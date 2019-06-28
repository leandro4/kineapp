package com.gonanimationlib.animations;

import android.animation.ObjectAnimator;
import android.view.View;

public final class CompAlpha extends AnimationCompBasic {

    private float alpha;

    CompAlpha(float alpha) {
        if (alpha < 0 && alpha > 1)
            throwRuntimeException("'Alpha' value must be between 0 - 1");
        this.alpha = alpha;
    }

    @Override
    public void startAnimation(View view) {
        ObjectAnimator anim = ObjectAnimator.ofFloat(view, View.ALPHA, alpha);
        anim.setDuration(duration);
        anim.setStartDelay(delay);
        anim.setInterpolator(interpolator);
        anim.addListener(buildAnimatorListener());
        anim.start();
    }
}
