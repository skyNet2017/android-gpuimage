package jp.co.cyberagent.android.gpuimage.sample.utils;

import android.view.View;
import androidx.core.view.ViewCompat;

public class ViewExt {

    public interface OnViewActionListener {
        void onAction(View view);
    }

    public static void doOnLayout(View view, final OnViewActionListener action) {
        if (ViewCompat.isLaidOut(view) && !view.isLayoutRequested()) {
            action.onAction(view);
        } else {
            doOnNextLayout(view, action);
        }
    }

    public static void doOnNextLayout(View view, final OnViewActionListener action) {
        view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(
                    View view,
                    int left,
                    int top,
                    int right,
                    int bottom,
                    int oldLeft,
                    int oldTop,
                    int oldRight,
                    int oldBottom
            ) {
                view.removeOnLayoutChangeListener(this);
                action.onAction(view);
            }
        });
    }
}