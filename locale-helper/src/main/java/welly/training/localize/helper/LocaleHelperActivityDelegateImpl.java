package welly.training.localize.helper;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import java.util.Locale;

/**
 * Created by camlh on 4/8/2019.
 */

public class LocaleHelperActivityDelegateImpl implements LocaleHelperActivityDelegate {

    private Locale locale = Locale.getDefault();

    @Override
    public void setLocale(Activity activity, Locale newLocale) {
        LocaleHelper.getInstance().setLocale(activity, newLocale);
        this.locale = newLocale;
        activity.recreate();
    }

    @Override
    public Context attachBaseContext(Context newBase) {
        return LocaleHelper.getInstance().onAttach(newBase);
    }

//    @Override
//    public void onPaused() {
//        this.locale = Locale.getDefault();
//    }
//
//    @Override
//    public void onResumed(Activity activity) {
//        if (this.locale == Locale.getDefault()) {
//            return;
//        }
//        activity.recreate();
//    }
//
//    @Override
//    public void onCreate(Activity activity) {
//        activity.getWindow().getDecorView().setLayoutDirection(LocaleHelper.getInstance().isRTL(Locale.getDefault()) ? View.LAYOUT_DIRECTION_RTL : View.LAYOUT_DIRECTION_LTR);
//    }
}
