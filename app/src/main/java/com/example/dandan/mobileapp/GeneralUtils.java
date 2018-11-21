package com.example.dandan.mobileapp;

import android.app.Activity;
import android.app.ProgressDialog;

public class GeneralUtils {
    private Activity _activity;
    private ProgressDialog _progress;

    public GeneralUtils(Activity activity) {
        this._activity = activity;
        _progress = new ProgressDialog(this._activity);
    }

    public void showLoadingProgress(String str, String str2) {
        this._progress.setTitle(str);
        // dandan str = this._progress;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Please wait while ");
        stringBuilder.append(str2);
        stringBuilder.append(" ...");
        //dandan str.setMessage(stringBuilder.toString());
        //this._progress.setCancelable(null);
        this._progress.setCancelable(false);
        this._progress.show();
    }

    public void dismissLoadingProgress() {
        this._progress.dismiss();
    }
}
