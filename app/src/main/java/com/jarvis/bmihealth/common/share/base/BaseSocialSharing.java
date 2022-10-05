package com.jarvis.bmihealth.common.share.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

/**
 * Created by thangnc on 30-09-2019
 */
public abstract class BaseSocialSharing {
    protected View shareLayout;
    protected Activity context;

    protected BaseSocialSharing(Activity activity, View shareLayout) {
        this.context = activity;
        this.shareLayout = shareLayout;
    }

    public abstract void share();

    protected void shareImageByPackage(Context context, String packageName, Uri uriForFile) {
        try {
            Intent intent = new Intent("android.intent.action.SEND");

            intent.putExtra(Intent.EXTRA_STREAM, uriForFile);
            intent.putExtra(Intent.EXTRA_TEXT, "Healthy");
            intent.setType("image/*");
            intent.setPackage(packageName);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
