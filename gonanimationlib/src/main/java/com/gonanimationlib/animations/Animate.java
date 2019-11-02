package com.gonanimationlib.animations;

import android.content.Context;
import androidx.core.content.ContextCompat;

/**
 * Created by leandro on 6/4/18.
 */

public final class Animate {

    public static CompAlpha ALPHA (float alpha) {
        return new CompAlpha(alpha);
    }

    public static CompColor COLOR (int color) {
        return new CompColor(color);
    }

    public static CompZumb ZUMB () {
        return new CompZumb();
    }

    public static CompThrob THROB () {
        return new CompThrob();
    }

    public static CompSlide SLIDE (CompSlide.FROM from) {
        return new CompSlide(from);
    }
    public static CompSlide SLIDE (CompSlide.TO to) {
        return new CompSlide(to);
    }

    public static CompMove MOVE (float x, float y) {
        return new CompMove(x, y);
    }

    public static CompRotate ROTATE (int degrees) {
        return new CompRotate(degrees);
    }

    public static CompResize RESIZE_WIDTH (int width) {
        return new CompResize(width, CompResize.ATTR.WIDTH);
    }

    public static CompResize RESIZE_HEIGHT (int height) {
        return new CompResize(height, CompResize.ATTR.HEIGHT);
    }

    public static CompZoom ZOOM (CompZoom.KIND kind) {
        return new CompZoom(kind);
    }

    public static CompDraggOneFinger DRAGG() {
        return new CompDraggOneFinger();
    }

    public static CompSretch STRETCH() {
        return new CompSretch();
    }

    // durations
    public static final long DURATION_V_SHORT = 100;
    public static final long DURATION_SHORT = 200;
    public static final long DURATION_MEDIUM = 350;
    public static final long DURATION_LARGE = 500;
    public static final long DURATION_V_LARGE = 700;

    public static final long DURATION_STATIC = 2500;
    public static final long DURATION_STATIC_SHORT = 1500;

    public static class Util {

        public static int DpToPx (Context context, int dp) {
            return (int) (dp * context.getResources().getDisplayMetrics().density);
        }

        public static int GetColor(Context context, int color) {
            try {
                return ContextCompat.getColor(context, color);
            } catch (Exception ex) {
                return -1;
            }
        }
    }
}
