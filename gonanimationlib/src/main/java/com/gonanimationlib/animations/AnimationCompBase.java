package com.gonanimationlib.animations;

import android.animation.Animator;
import android.view.View;
import android.view.animation.Animation;
import com.gonanimationlib.interfaces.Listener;

/**
 * Created by leandro on 9/4/18.
 */

public abstract class AnimationCompBase {

    // Listeners for animation base
    protected Listener onStart;
    protected Listener onEnd;
    protected Listener onRepeat;

    public abstract void startAnimation(View view);

    protected Animation.AnimationListener buildAnimationListener() {
        return new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                if (onStart != null) onStart.call();
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (onEnd != null) onEnd.call();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                if (onRepeat != null) onRepeat.call();
            }
        };
    }

    protected Animator.AnimatorListener buildAnimatorListener() {
        return new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                if (onStart != null) onStart.call();
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (onEnd != null) onEnd.call();
            }

            @Override
            public void onAnimationCancel(Animator animator) {}

            @Override
            public void onAnimationRepeat(Animator animator) {
                if (onRepeat != null) onRepeat.call();
            }
        };
    }

    protected void throwRuntimeException(String message) {
        throw new RuntimeException(message);
    }

    //region setters

    public AnimationCompBase onStart(Listener callback) {
        this.onStart = callback;
        return this;
    }

    public AnimationCompBase onEnd(Listener callback) {
        this.onEnd = callback;
        return this;
    }

    //endregion
}
