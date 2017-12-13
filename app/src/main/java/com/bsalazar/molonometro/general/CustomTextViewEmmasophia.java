package com.bsalazar.molonometro.general;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by asanz on 27/08/2015.
 */
public class CustomTextViewEmmasophia extends TextView {


    public CustomTextViewEmmasophia(Context context) {
        super(context);
        fuente();
    }

    public CustomTextViewEmmasophia(Context context, AttributeSet attrs) {
        super(context, attrs);
        fuente();
    }

    public CustomTextViewEmmasophia(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (!isInEditMode())
            fuente();
    }

    private void fuente() {
        Typeface myCustomFont = Typeface.createFromAsset(getContext().getAssets(), "fonts/Reality Sunday light.ttf");
//        Typeface myCustomFont = Typeface.createFromAsset(getContext().getAssets(), "fonts/Vincentia.otf");
        this.setTypeface(myCustomFont);
    }

    @Override
    public boolean isInEditMode() {
        return false;
    }
}
