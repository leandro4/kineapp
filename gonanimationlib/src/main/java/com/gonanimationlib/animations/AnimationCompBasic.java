package com.gonanimationlib.animations;

import androidx.annotation.NonNull;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.gonanimationlib.interfaces.Listener;

public abstract class AnimationCompBasic extends AnimationCompBase {

    protected long duration = Animate.DURATION_SHORT;
    protected int delay = 0;
    protected int repeat = 0;
    protected Interpolator interpolator = new LinearInterpolator();
    protected boolean fillAfter = false;

    public AnimationCompBasic duration(long during) {
        if (during > 0)
            this.duration = during;
        return this;
    }

    public AnimationCompBasic startOffset(int delay) {
        if (delay > 0)
            this.delay = delay;
        return this;
    }

    public AnimationCompBasic fillAfter(boolean fillAfter) {
        this.fillAfter = fillAfter;
        return this;
    }

    public AnimationCompBasic repeat(int repeat, Listener callback) {
        if (repeat > 0) {
            this.repeat = repeat;
            this.onRepeat = callback;
        }
        return this;
    }

    public AnimationCompBasic interpolator(@NonNull Interpolator interpolator) {
        this.interpolator = interpolator;
        return this;
    }
}
