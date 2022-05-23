package com.jarvis.design_system.shadow;

import android.content.Context;
import android.util.AttributeSet;

import com.loopeer.shadow.ShadowView;

import org.jetbrains.annotations.Nullable;

/**
 * Created by Jarvis on 7/5/2021.
 */
public class WellShadow extends ShadowView {
    public WellShadow(@Nullable Context context, @Nullable AttributeSet attributeSet, int defStyleInt) {
        super(context, attributeSet, defStyleInt);
    }

    public WellShadow(@Nullable Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public WellShadow(@Nullable Context context) {
        super(context);
    }
}
