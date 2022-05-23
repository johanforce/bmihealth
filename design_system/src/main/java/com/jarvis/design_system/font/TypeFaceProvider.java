package com.jarvis.design_system.font;

import android.content.Context;
import android.graphics.Typeface;

import java.util.Hashtable;

/**
 * Created by Jarvis Nguyen on 20/01/2022.
 */

public class TypeFaceProvider {

    private static Hashtable<String, Typeface> sTypeFaces = new Hashtable<String, Typeface>(
            4);

    public static Typeface getTypeFace(Context context, String fileName) {
        Typeface tempTypeface = sTypeFaces.get(fileName);

        if (tempTypeface == null) {
            tempTypeface = Typeface.createFromAsset(context.getAssets(), fileName);
            sTypeFaces.put(fileName, tempTypeface);
        }
        return tempTypeface;
    }
}
