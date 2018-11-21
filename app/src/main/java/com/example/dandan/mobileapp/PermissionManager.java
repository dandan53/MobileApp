package com.example.dandan.mobileapp;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
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

        showMessageOKCancel(stringBuilder.toString(), new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                if (Build.VERSION.SDK_INT >= 23) {
                    PermissionManager.this._mainActivity.requestPermissions(new String[]{str}, PermissionManager.REQUEST_CODE_ASK_PERMISSIONS);
                }
            }
        });

        return false;
    }


    private void showMessageOKCancel(String str, DialogInterface.OnClickListener onClickListener) {
        new AlertDialog.Builder(this._mainActivity).setMessage(str).setPositiveButton(R.string.ok, onClickListener).setNegativeButton(R.string.cancel, null).create().show();
    }
}
