package welly.training.localize.helper;

import android.app.Activity;
import android.content.Context;

import java.util.Locale;

/**
 * Created by camlh on 4/8/2019.
 */

public interface LocaleHelperActivityDelegate {

    void setLocale(Activity activity, Locale newLocale);

    Context attachBaseContext(Context newBase);

//    void onPaused();
//
//    void onResumed(Activity activity);
//
//    void onCreate(Activity activity);
}
