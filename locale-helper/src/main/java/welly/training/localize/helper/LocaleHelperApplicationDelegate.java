package welly.training.localize.helper;

import android.content.Context;

/**
 * Created by camlh on 4/8/2019.
 */

public class LocaleHelperApplicationDelegate {

    public Context attachBaseContext(Context base) {
        return LocaleHelper.getInstance().onAttach(base);
    }

    public void onConfigurationChanged(Context context) {
        LocaleHelper.getInstance().onAttach(context);
    }
}
