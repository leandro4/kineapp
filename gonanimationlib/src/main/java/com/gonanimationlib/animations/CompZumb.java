package com.gonanimationlib.animations;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;

public final class CompZumb extends AnimationCompBasic {

    private float scale = 1.2f;
    private float pivoteX = 0.5f;
    private float pivoteY = 0.5f;
    private int degrees = 6;
    private int cycles = 3;

    private final float pivoteDefault = 0.5f;
    private final float scaleDefault = 1;

    @Override
    public void startAnimation(View view) {

        long time = duration / 4;

        Animation stretch = new ScaleAnimation(scaleDefault, scale, scaleDefault, scale, Animation.RELATIVE_TO_SELF, pivoteDefault,
                Animation.RELATIVE_TO_SELF, pivoteDefault);
        stretch.setDuration(time);
        stretch.setInterpolator(new AnticipateInterpolator(4));


        Animation rotateInitMid = new RotateAnimation(0, degrees, Animation.RELATIVE_TO_SELF, pivoteX,
                Animation.RELATIVE_TO_SELF, pivoteY);
        rotateInitMid.setInterpolator(new CycleInterpolator(cycles));
        rotateInitMid.setDuration(time * 2);
        rotateInitMid.setStartOffset(time);


        Animation back = new ScaleAnimation(scale, scaleDefault, scale, scaleDefault, Animation.RELATIVE_TO_SELF, pivoteDefault,
                Animation.RELATIVE_TO_SELF, pivoteDefault);
        back.setDuration(time);
        back.setStartOffset(time * 3);
        back.setInterpolator(new LinearInterpolator());


        AnimationSet set = new AnimationSet(false);
        set.addAnimation(stretch);
        set.addAnimation(rotateInitMid);
        set.addAnimation(back);

        set.setAnimationListener(buildAnimationListener());
        view.startAnimation(set);
    }

    // region parameters

    public CompZumb stretch(float stretchScale) {
        scale = stretchScale;
        return this;
    }

    public CompZumb pivoteX(float pivoteX) {
        this.pivoteX = pivoteX;
        return this;
    }

    public CompZumb pivoteY(float pivoteY) {
        this.pivoteY = pivoteY;
        return this;
    }

    public CompZumb degrees(int degrees) {
        this.degrees = degrees;
        return this;
    }

    public CompZumb cycles(int cycles) {
        this.cycles = cycles;
        return this;
    }

    // endregion
}
