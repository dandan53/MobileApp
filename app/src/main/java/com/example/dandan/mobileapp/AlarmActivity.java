package com.example.dandan.mobileapp;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class AlarmActivity extends AppCompatActivity {
    private String _alarmedAppPackageName;
    private Button _btnDismiss;
    private Button _btnOpenApp;
    private ImageView _imgAppIcon;
    private MediaPlayer _mp;
    private TextView _txtApp;

    /* renamed from: com.firisoft.firisoft.nevertextanddrive.AlarmActivity$1 */
    class C03231 implements OnClickListener {
        C03231() {
        }

        public void onClick(View view) {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.MAIN");
            intent.addCategory("android.intent.category.HOME");
            intent.setFlags(Intent.FLAG_RECEIVER_FOREGROUND); //dandan 268435456
            startActivity(intent);
        }
    }

    /* renamed from: com.firisoft.firisoft.nevertextanddrive.AlarmActivity$2 */
    class C03242 implements OnClickListener {
        C03242() {
        }

        public void onClick(View view) {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.MAIN");
            intent.setClass(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
    }

    /* JADX: method processing error dandan */
    protected void onCreate(android.os.Bundle r3) {

        super.onCreate(r3);
        setContentView(R.layout.activity_alarm);

        Intent intent = getIntent();
        String r0 = "EXIT";
        boolean r1 = false;
        boolean bo = intent.getBooleanExtra(r0, r1);
        if (!bo) {
            finish();
            return;
        } else {
            initControls();
            Window window = getWindow();
            int f0 = 4194304; // 0x400000 float:5.877472E-39 double:2.0722615E-317;
            window.addFlags(f0);
            int f1 = 524288; // 0x80000 float:7.34684E-40 double:2.590327E-318;
            window.addFlags(f1);
            int f3 = 2097152; // 0x200000 float:2.938736E-39 double:1.0361308E-317;
            window.addFlags(f3);
            startAlarmSound();
            Intent intent1 = getIntent();
            String packName = "AlarmAppPackageName";
            String strExtra = intent1.getStringExtra(packName);
            if (strExtra == null) {
                return;
            } else {
                _alarmedAppPackageName = strExtra;
                PackageManager packageManager = getPackageManager();

                ApplicationInfo applicationInfo = null;

                try {
                    applicationInfo = packageManager.getApplicationInfo(this._alarmedAppPackageName, PackageManager.GET_META_DATA);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }

                if (applicationInfo != null) {
                    CharSequence appLabel = packageManager.getApplicationLabel(applicationInfo);
                    String appLabelStr = appLabel.toString();
                    _txtApp.setText(appLabelStr);

                    Drawable appIcon = null;

                    try {
                        appIcon = packageManager.getApplicationIcon(_alarmedAppPackageName);
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }

                    if (appIcon != null) {
                        _imgAppIcon.setImageDrawable(appIcon);
                    }
                }
            }
        }
    }

    protected void onDestroy() {
        super.onDestroy();
    }

    protected void onPause() {
        super.onPause();
        finish();
    }

    private void initControls() {
        this._mp = MediaPlayer.create(this, R.raw.carrbrake);
        this._txtApp = (TextView) findViewById(R.id.txtAlarmTitleAppName);
        this._btnDismiss = (Button) findViewById(R.id.dismissButton);
        this._btnOpenApp = (Button) findViewById(R.id.openAppButton);
        this._imgAppIcon = (ImageView) findViewById(R.id.imgAppIcon);
        this._btnDismiss.setOnClickListener(new C03231());
        this._btnOpenApp.setOnClickListener(new C03242());
    }

    private void startAlarmSound() {
        this._mp.start();
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i != 4) {
            return super.onKeyDown(i, keyEvent);
        }
        this._btnDismiss.callOnClick();
        return true;
    }
}

