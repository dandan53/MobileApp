package com.example.dandan.mobileapp;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
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


        /*
        r2 = this;
        super.onCreate(r3);
        r3 = 2131361819; // 0x7f0a001b float:1.8343401E38 double:1.0530326536E-314;
        r2.setContentView(r3);
        r3 = r2.getIntent();
        r0 = "EXIT";
        r1 = 0;
        r3 = r3.getBooleanExtra(r0, r1);
        if (r3 == 0) goto L_0x001a;
    L_0x0016:
        r2.finish();
        return;
    L_0x001a:
        r2.initControls();
        r3 = r2.getWindow();
        r0 = 4194304; // 0x400000 float:5.877472E-39 double:2.0722615E-317;
        r3.addFlags(r0);
        r0 = 524288; // 0x80000 float:7.34684E-40 double:2.590327E-318;
        r3.addFlags(r0);
        r0 = 2097152; // 0x200000 float:2.938736E-39 double:1.0361308E-317;
        r3.addFlags(r0);
        r2.startAlarmSound();
        r3 = r2.getIntent();
        r0 = "AlarmAppPackageName";
        r3 = r3.getStringExtra(r0);
        if (r3 == 0) goto L_0x0065;
    L_0x003f:
        r2._alarmedAppPackageName = r3;
        r3 = r2.getPackageManager();
        r0 = r2._alarmedAppPackageName;	 Catch:{ Exception -> 0x0065 }
        r1 = 128; // 0x80 float:1.794E-43 double:6.32E-322;	 Catch:{ Exception -> 0x0065 }
        r0 = r3.getApplicationInfo(r0, r1);	 Catch:{ Exception -> 0x0065 }
        r1 = r2._txtApp;	 Catch:{ Exception -> 0x0065 }
        r0 = r3.getApplicationLabel(r0);	 Catch:{ Exception -> 0x0065 }
        r0 = r0.toString();	 Catch:{ Exception -> 0x0065 }
        r1.setText(r0);	 Catch:{ Exception -> 0x0065 }
        r0 = r2._alarmedAppPackageName;	 Catch:{ Exception -> 0x0065 }
        r3 = r3.getApplicationIcon(r0);	 Catch:{ Exception -> 0x0065 }
        r0 = r2._imgAppIcon;	 Catch:{ Exception -> 0x0065 }
        r0.setImageDrawable(r3);	 Catch:{ Exception -> 0x0065 }
    L_0x0065:
        return;
        */
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

