package welly.training.localize.helper;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by camlh on 4/8/2019.
 */

public class LocaleAwareCompatActivity extends AppCompatActivity {

    public LocaleHelperActivityDelegateImpl localeDelegate = new LocaleHelperActivityDelegateImpl();

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(localeDelegate.attachBaseContext(newBase));
    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        this.localeDelegate.onCreate(this);
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        this.localeDelegate.onResumed(this);
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        this.localeDelegate.onPaused();
//    }
}
