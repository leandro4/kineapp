package com.gonanimationlib.animations;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import static android.view.MotionEvent.INVALID_POINTER_ID;

public final class CompSretch extends AnimationCompBase {

    private int firstPointerId = INVALID_POINTER_ID;
    private int secondPointerId = INVALID_POINTER_ID;

    private double distance = 0;

    @Override
    public void startAnimation(View view) {
        view.setOnTouchListener((v, ev) -> {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:

                    firstPointerId = ev.getPointerId(0);
                    Log.d("STRETCH", "Primer dedo: " + firstPointerId);

                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    if (ev.getPointerCount() == 2) {
                        secondPointerId = ev.getPointerId(1);

                        Log.d("STRETCH", "Segundo dedo: " + secondPointerId);

                        distance = Math.sqrt(Math.pow(ev.getX(ev.findPointerIndex(firstPointerId)) - ev.getX(ev.findPointerIndex(secondPointerId)), 2)
                                + Math.pow(ev.getY(ev.findPointerIndex(firstPointerId)) - ev.getY(ev.findPointerIndex(secondPointerId)), 2));
                    }
                    break;
                case MotionEvent.ACTION_MOVE:

                    if (ev.getPointerCount() != 2) return false;

                    if (secondPointerId == INVALID_POINTER_ID)
                        secondPointerId = ev.getPointerId(1);

                    final int pointerIndexUp = ev.getActionIndex();
                    final int pointerId = ev.getPointerId(pointerIndexUp);

                    if (pointerId != firstPointerId && pointerId != secondPointerId) return false;

                    int firstFinger = ev.findPointerIndex(firstPointerId);
//                    float xFirst = ev.getX(firstFinger);
//                    float yFirst = ev.getY(firstFinger);

                    int secondFinger = ev.findPointerIndex(secondPointerId);
//                    float xSecond = ev.getX(secondFinger);
//                    float ySecond = ev.getY(secondFinger);

                    float x = ev.getX(firstFinger) - ev.getX(secondFinger);
                    float y = ev.getY(firstFinger) - ev.getY(secondFinger);

                    double newDistance = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));

                    if (newDistance > distance) {
                        view.setScaleX(view.getScaleX() + 0.1f);
                        view.setScaleY(view.getScaleY() + 0.1f);
                    } else {
                        view.setScaleX(view.getScaleX() - 0.1f);
                        view.setScaleY(view.getScaleY() - 0.1f);
                    }

                    distance = newDistance;

                    break;

                case MotionEvent.ACTION_UP:
                    firstPointerId = INVALID_POINTER_ID;
                    secondPointerId = INVALID_POINTER_ID;
                    break;
                case MotionEvent.ACTION_CANCEL:
                    firstPointerId = INVALID_POINTER_ID;
                    secondPointerId = INVALID_POINTER_ID;
                    break;
                case MotionEvent.ACTION_POINTER_UP:

                    view.setScaleX(1);
                    view.setScaleY(1);

//                    final int pointerIndexUp = ev.getActionIndex();
//                    final int pointerId = ev.getPointerId(pointerIndexUp);
//
//                    if (pointerId == mActivePointerId) {
//                        final int newPointerIndex = pointerIndexUp == 0 ? 1 : 0;
//                        mLastTouchX = ev.getX(newPointerIndex);
//                        mLastTouchY = ev.getY(newPointerIndex);
//                        mActivePointerId = ev.getPointerId(newPointerIndex);
//                    }
                    break;
            }

            v.performClick();

            return true;
        });
    }
}
