package com.gonanimationlib.interpolators;

/**
 * Created by leandro on 5/12/17.
 */

public class SpringInterpolator implements android.view.animation.Interpolator {

    private double mAmplitude = 0.2;
    private double mFrequency = 6;

    public SpringInterpolator() {}

    public SpringInterpolator(double amplitude, double frequency) {
        mAmplitude = amplitude;
        mFrequency = frequency;
    }

    @Override
    public float getInterpolation(float time) {
        return (float) ( 1 -     Math.cos(mFrequency * time) /
                                 Math.pow(Math.E, time / mAmplitude) );
    }
}
