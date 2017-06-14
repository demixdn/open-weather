package com.github.demixdn.weather.ui.cities;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created on 14.06.2017
 * Project open-weather
 *
 * @author Aleks Sander
 */

public class RemoveDecoration extends RecyclerView.ItemDecoration {
    // we want to cache this and not allocate anything repeatedly in the onDraw method
    private Drawable background;
    private boolean initiated;

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

        if (!initiated) {
            init();
        }

        if (parent.getItemAnimator().isRunning()) {

            View lastViewComingDown = null;
            View firstViewComingUp = null;

            int left = 0;
            int right = parent.getWidth();

            int top = 0;
            int bottom = 0;

            int childCount = parent.getLayoutManager().getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = parent.getLayoutManager().getChildAt(i);
                if (child.getTranslationY() < 0) {
                    lastViewComingDown = child;
                } else if (child.getTranslationY() > 0 && firstViewComingUp == null) {
                    firstViewComingUp = child;
                }
            }

            if (lastViewComingDown != null && firstViewComingUp != null) {
                top = lastViewComingDown.getBottom() + (int) lastViewComingDown.getTranslationY();
                bottom = firstViewComingUp.getTop() + (int) firstViewComingUp.getTranslationY();
            } else if (lastViewComingDown != null) {
                top = lastViewComingDown.getBottom() + (int) lastViewComingDown.getTranslationY();
                bottom = lastViewComingDown.getBottom();
            } else if (firstViewComingUp != null) {
                top = firstViewComingUp.getTop();
                bottom = firstViewComingUp.getTop() + (int) firstViewComingUp.getTranslationY();
            }

            background.setBounds(left, top, right, bottom);
            background.draw(c);

        }
        super.onDraw(c, parent, state);
    }

    private void init() {
        background = new ColorDrawable(Color.RED);
        initiated = true;
    }
}
