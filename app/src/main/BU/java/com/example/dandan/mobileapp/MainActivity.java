package com.example.dandan.mobileapp;

import android.app.ActivityManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private int APP_SCAN_INTERVAL = 500;
    private Timer _appCloseTimer;
    private AppsManager _appsManager;
    private boolean _closingTimerOn;
    private ImageButton _decreaseSpeedButton;
    private TextView _driveSpeedTxt;
    private int _driveSpeedVal = 20;
    private DrivingListener _drivingListener;
    private GeneralUtils _generalUtils;
    private ImageButton _increaseSpeedButton;
    private boolean _isServiceActive = false;
    private RateEncourager _rateEncourager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Log.i("MobileApp", "## Main Started ##");

        this._appsManager = new AppsManager(this);
        this._generalUtils = new GeneralUtils(this);
        this._rateEncourager = new RateEncourager(getApplicationContext(), this);

        addAllApps();

        this._drivingListener = new DrivingListener(this);

        final ImageButton imageButton = (ImageButton) findViewById(R.id.buttonApply);
        imageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (MainActivity.this._isServiceActive) {
                    MainActivity.this._drivingListener.unregisterRequestingUpdates();
                    MainActivity.this.stopClosingAppTimer();
                    MainActivity.this._isServiceActive = false;
                    imageButton.setBackgroundResource(R.drawable.startbutton_res);
                    MainActivity.this.stopService(new Intent(MainActivity.this.getApplicationContext(), RunService.class));
                } else if (MainActivity.this.checkPermissions().booleanValue()) {
                    if (!MainActivity.this.isUsageAccessSettingsGranted()) {
                        MainActivity.this.showSettingsAppsAccessUsageMenu();
                        return;
                    }
                    if (MainActivity.this._driveSpeedVal < 0) {
                        MainActivity.this.startClosingAppTimer();
                    } else {
                        MainActivity.this._drivingListener.StartGpsListener(2.0d, (double) MainActivity.this._driveSpeedVal);
                    }
                    MainActivity.this._isServiceActive = true;
                    //dandandan imageButton.setBackgroundResource(R.dr);
                    MainActivity.this.startService(new Intent(MainActivity.this.getApplicationContext(), RunService.class));

                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.MAIN");
                    intent.addCategory("android.intent.category.HOME");
                    startActivity(intent);
                }
            }
        });

        this._driveSpeedTxt = (TextView) findViewById(R.id.minSpeedTxt);
        this._decreaseSpeedButton = (ImageButton) findViewById(R.id.minSpeedMinusButton);
        this._decreaseSpeedButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (MainActivity.this._driveSpeedVal > -1) {
                    if (MainActivity.this._isServiceActive) {
                        imageButton.callOnClick();
                    }
                    MainActivity.this._driveSpeedVal = MainActivity.this._driveSpeedVal - 5;
                    view = MainActivity.this._driveSpeedTxt;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(MainActivity.this._driveSpeedVal);
                    stringBuilder.append("");
                    ((TextView)view).setText(stringBuilder.toString());
                }
            }
        });
        this._increaseSpeedButton = (ImageButton) findViewById(R.id.minSpeedPlusButton);
        this._increaseSpeedButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (MainActivity.this._driveSpeedVal != 95) {
                    if (MainActivity.this._isServiceActive) {
                        imageButton.callOnClick();
                    }
                    MainActivity.this._driveSpeedVal = MainActivity.this._driveSpeedVal + 5;
                    view = MainActivity.this._driveSpeedTxt;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(MainActivity.this._driveSpeedVal);
                    stringBuilder.append("");
                    ((TextView)view).setText(stringBuilder.toString());
                }
            }
        });

    }


    /* renamed from: com.firisoft.firisoft.nevertextanddrive.MainActivity$7 */
    class AddAllAppsRun implements Runnable {

        /* renamed from: com.firisoft.firisoft.nevertextanddrive.MainActivity$7$1 */
        class AddAllAppsRunInner implements Runnable {
            AddAllAppsRunInner() {
            }

            public void run() {
                MainActivity.this._generalUtils.dismissLoadingProgress();
            }
        }

        AddAllAppsRun() {
        }

        public void run() {
            MainActivity.this._appsManager.addAllApps((LinearLayout) MainActivity.this.findViewById(R.id.appLayout));
            MainActivity.this.runOnUiThread(new AddAllAppsRunInner());
        }
    }

    private void addAllApps() {
        this._generalUtils.showLoadingProgress("Loading", "loading apps list");
        new Thread(new AddAllAppsRun()).start();
    }

    public void stopClosingAppTimer() {
        if (this._closingTimerOn) {
            this._closingTimerOn = false;
            Log.i("NeverTextAndDrive", "Not driving - text apps are enabled");
            Toast.makeText(this, "Not driving - text apps are enabled", Toast.LENGTH_SHORT).show();
            if (this._appCloseTimer != null) {
                this._appCloseTimer.cancel();
            }
        }
    }

    private Boolean checkPermissions() {
        boolean z;
        PermissionManager permissionManager = new PermissionManager(this);
        if (permissionManager.checkAndAskPermission("android.permission.ACCESS_FINE_LOCATION", "GPS Fine Location")) {
            if (permissionManager.checkAndAskPermission("android.permission.ACCESS_COARSE_LOCATION", "GPS Course Location")) {
                z = false;
                return Boolean.valueOf(Boolean.valueOf(z).booleanValue() ^ true);
            }
        }
        z = true;
        return Boolean.valueOf(Boolean.valueOf(z).booleanValue() ^ true);
    }


    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean isUsageAccessSettingsGranted() {
        /*
        r5 = this;
        r0 = android.os.Build.VERSION.SDK_INT;
        r1 = 1;
        r2 = 21;
        if (r0 >= r2) goto L_0x0008;
    L_0x0007:
        return r1;
    L_0x0008:
        r0 = "appops";
        r0 = r5.getSystemService(r0);
        r0 = (android.app.AppOpsManager) r0;
        r2 = "android:get_usage_stats";
        r3 = android.os.Process.myUid();
        r4 = r5.getPackageName();
        r0 = r0.checkOpNoThrow(r2, r3, r4);
        r2 = 3;
        r3 = 0;
        if (r0 != r2) goto L_0x002d;
    L_0x0022:
        r0 = "android.permission.PACKAGE_USAGE_STATS";
        r0 = r5.checkCallingOrSelfPermission(r0);
        if (r0 != 0) goto L_0x002b;
    L_0x002a:
        goto L_0x002f;
    L_0x002b:
        r1 = 0;
        goto L_0x002f;
    L_0x002d:
        if (r0 != 0) goto L_0x002b;
    L_0x002f:
        return r1;
        */
        //dandan throw new UnsupportedOperationException("Method not decompiled: com.firisoft.firisoft.nevertextanddrive.MainActivity.isUsageAccessSettingsGranted():boolean");
        return false;
    }

    private void showSettingsAppsAccessUsageMenu() {
        //dandan new AlertDialog.Builder(this).setTitle("Allow messages apps access").setMessage("Please allow this app to determine apps usage").setPositiveButton(17039379, new C03335()).setNegativeButton(17039369, new C03314()).setIcon(17301543).show();
    }

    public void startClosingAppTimer() {
        if (!this._closingTimerOn) {
            this._closingTimerOn = true;
            Log.i("NeverTextAndDrive", "Driving - All text apps are disabled");
            Toast.makeText(this, "Driving - All text apps are disabled", Toast.LENGTH_SHORT).show();
            this._appCloseTimer = new Timer();
            this._appCloseTimer.scheduleAtFixedRate(new startClosingAppTimerTimerTask(), 0, (long) this.APP_SCAN_INTERVAL);
        }
    }

    private String getProcessName() {
        String str = "";
        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT < 21) {
            //dandan return ((RunningTaskInfo) activityManager.getRunningTasks(1).get(0)).topActivity.getPackageName();
            Toast.makeText(this, "Build.VERSION.SDK_INT < 21", Toast.LENGTH_SHORT).show(); //dandan
            return "Build.VERSION.SDK_INT < 21";
        }

        UsageStatsManager usageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
        long currentTimeMillis = System.currentTimeMillis();
        List<UsageStats> queryUsageStats = usageStatsManager.queryUsageStats(0, currentTimeMillis - 10000, currentTimeMillis);
        if (queryUsageStats == null) {
            return str;
        }
        SortedMap treeMap = new TreeMap();
        for (UsageStats usageStats : queryUsageStats) {
            treeMap.put(Long.valueOf(usageStats.getLastTimeUsed()), usageStats);
        }
        if (treeMap == null || treeMap.isEmpty()) {
            return str;
        }
        return ((UsageStats) treeMap.get(treeMap.lastKey())).getPackageName();
    }

    public boolean isAppOnForeground(String str) {
        return getProcessName().equals(str);
    }



    /* renamed from: com.firisoft.firisoft.nevertextanddrive.MainActivity$6 */
    class startClosingAppTimerTimerTask extends TimerTask {
        startClosingAppTimerTimerTask() {
        }

        public void run() {
            Iterator it = MainActivity.this._appsManager.getSelectedApps().iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                if (MainActivity.this.isAppOnForeground(str)) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Closing app while driving: ");
                    stringBuilder.append(str);
                    Log.i("NeverTextAndDrive", stringBuilder.toString());
                    MainActivity.this.showAlarmScreen(str);
                }
            }
        }
    }

    private void showAlarmScreen(String str) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.searchTryCatchDominators(ProcessTryCatchRegions.java:75)
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.process(ProcessTryCatchRegions.java:45)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.postProcessRegions(RegionMakerVisitor.java:63)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:58)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/299644693.run(Unknown Source)
*/
        /*
        r2 = this;
        r0 = new android.content.Intent;
        r1 = "android.intent.action.MAIN";
        r0.<init>(r1);
        r1 = "AlarmAppPackageName";
        r0.putExtra(r1, r3);
        r3 = com.firisoft.firisoft.nevertextanddrive.AlarmActivity.class;
        r0.setClass(r2, r3);
        r2.startActivity(r0);
        r3 = 0;
        r1 = 134217728; // 0x8000000 float:3.85186E-34 double:6.63123685E-316;
        r1 = android.app.PendingIntent.getActivity(r2, r3, r0, r1);
        r1.send(r2, r3, r0);	 Catch:{ Exception -> 0x001e }
    L_0x001e:
        return;
        */
        //dandan throw new UnsupportedOperationException("Method not decompiled: com.firisoft.firisoft.nevertextanddrive.MainActivity.showAlarmScreen(java.lang.String):void");
        Toast.makeText(this, "showAlarmScreen", Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
