package com.example.dandan.mobileapp;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.view.View;

public class PermissionManager {
    public static final int REQUEST_CODE_ASK_PERMISSIONS = 123;
    private final MainActivity _mainActivity;

    public PermissionManager(MainActivity mainActivity) {
        this._mainActivity = mainActivity;
    }

    public boolean checkAndAskPermission(final String str, String str2) {
        if (Build.VERSION.SDK_INT < 23 || this._mainActivity.checkSelfPermission(str) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (this._mainActivity.shouldShowRequestPermissionRationale(str)) {
            this._mainActivity.requestPermissions(new String[]{str}, REQUEST_CODE_ASK_PERMISSIONS);
            return false;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("For this app to work, you need to allow ");
        stringBuilder.append(str2);
        stringBuilder.append(" permissions in the next screen.");

        /* dandan showMessageOKCancel(stringBuilder.toString(), new View.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                if (Build.VERSION.SDK_INT >= 23) {
                    PermissionManager.this._mainActivity.requestPermissions(new String[]{str}, PermissionManager.REQUEST_CODE_ASK_PERMISSIONS);
                }
            }
        });*/
        return false;
    }

    private void showMessageOKCancel(String str, View.OnClickListener onClickListener) {
        //dandan new Builder(this._mainActivity).setMessage(str).setPositiveButton("OK", onClickListener).setNegativeButton("Cancel", null).create().show();
    }
}
