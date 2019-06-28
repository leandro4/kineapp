package com.gonanimationlib.animations;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;

public final class CompThrob extends AnimationCompBasic {

    private float scale = 1.2f;

    private final float pivoteDefault = 0.5f;
    private final float scaleDefault = 1;

    @Override
    public void startAnimation(View view) {

        long time = duration / 2;

        Animation stretch = new ScaleAnimation(scaleDefault, scale, scaleDefault, scale, Animation.RELATIVE_TO_SELF, pivoteDefault,
                Animation.RELATIVE_TO_SELF, pivoteDefault);
        stretch.setDuration(time);
        stretch.setInterpolator(new AnticipateInterpolator(4));

        Animation back = new ScaleAnimation(scale, scaleDefault, scale, scaleDefault, Animation.RELATIVE_TO_SELF, pivoteDefault,
                Animation.RELATIVE_TO_SELF, pivoteDefault);
        back.setDuration(time);
        back.setStartOffset(time);
        back.setInterpolator(new LinearInterpolator());

        AnimationSet set = new AnimationSet(false);
        set.addAnimation(stretch);
        set.addAnimation(back);

        set.setAnimationListener(buildAnimationListener());
        view.startAnimation(set);
    }

    public CompThrob stretch(float stretchScale) {
        scale = stretchScale;
        return this;
    }
}
