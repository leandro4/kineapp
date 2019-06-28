package com.gonanimationlib.animations;

import android.view.MotionEvent;
import android.view.View;

import static android.view.MotionEvent.INVALID_POINTER_ID;

public final class CompDraggMultFingers extends AnimationCompBase {

    private int mActivePointerId = INVALID_POINTER_ID;
    private float mLastTouchX;
    private float mLastTouchY;

    @Override
    public void startAnimation(View view) {

        view.setOnTouchListener((v, ev) -> {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:

                    int pointerIndex = ev.getActionIndex();
                    float x = ev.getX(pointerIndex);
                    float y = ev.getY(pointerIndex);

                    mLastTouchX = x;
                    mLastTouchY = y;

                    mActivePointerId = ev.getPointerId(0);

                    view.setScaleX(1.2f);
                    view.setScaleY(1.2f);

                    break;

                case MotionEvent.ACTION_MOVE:
                    int pointerIndexMove = ev.findPointerIndex(mActivePointerId);

                    float xMove = ev.getX(pointerIndexMove);
                    float yMove = ev.getY(pointerIndexMove);

                    float dx = xMove - mLastTouchX;
                    float dy = yMove - mLastTouchY;

                    view.setX(v.getX() + dx);
                    view.setY(v.getY() + dy);

                    mLastTouchX = xMove;
                    mLastTouchY = yMove;

                    break;

                case MotionEvent.ACTION_UP:
                    mActivePointerId = INVALID_POINTER_ID;

                    view.setScaleX(1);
                    view.setScaleY(1);

                    break;

                case MotionEvent.ACTION_CANCEL:
                    mActivePointerId = INVALID_POINTER_ID;
                    break;

                case MotionEvent.ACTION_POINTER_UP:

                    final int pointerIndexUp = ev.getActionIndex();
                    final int pointerId = ev.getPointerId(pointerIndexUp);

                    if (pointerId == mActivePointerId) {
                        final int newPointerIndex = pointerIndexUp == 0 ? 1 : 0;
                        mLastTouchX = ev.getX(newPointerIndex);
                        mLastTouchY = ev.getY(newPointerIndex);
                        mActivePointerId = ev.getPointerId(newPointerIndex);
                    }
                    break;
            }

            v.performClick();

            return true;
        });
    }
}
