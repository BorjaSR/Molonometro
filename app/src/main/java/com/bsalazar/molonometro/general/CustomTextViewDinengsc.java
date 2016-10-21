package com.bsalazar.molonometro.general;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by asanz on 27/08/2015.
 */
public class CustomTextViewDinengsc extends TextView {


    public CustomTextViewDinengsc(Context context) {
        super(context);
        fuente();
    }

    public CustomTextViewDinengsc(Context context, AttributeSet attrs) {
        super(context, attrs);
        fuente();
    }

    public CustomTextViewDinengsc(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (!isInEditMode())
            fuente();
    }

    private void fuente() {
        Typeface myCustomFont = Typeface.createFromAsset(getContext().getAssets(), "fonts/emmasophia.ttf");
        this.setTypeface(myCustomFont);
    }

    @Override
    public boolean isInEditMode() {
        return false;
    }
}
