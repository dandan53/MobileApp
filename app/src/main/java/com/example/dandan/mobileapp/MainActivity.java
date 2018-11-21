package com.example.dandan.mobileapp;

import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.app.ListActivity;
import android.app.PendingIntent;
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
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
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

import android.content.DialogInterface.OnClickListener;
import android.app.ActivityManager.RunningTaskInfo;


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


    /* renamed from: com.firisoft.firisoft.nevertextanddrive.MainActivity$4 */
    class C03314 implements OnClickListener {
        public void onClick(DialogInterface dialogInterface, int i) {
        }

        C03314() {
        }
    }

    /* renamed from: com.firisoft.firisoft.nevertextanddrive.MainActivity$5 */
    class C03335 implements OnClickListener {

        /* renamed from: com.firisoft.firisoft.nevertextanddrive.MainActivity$5$1 */
        class C03321 implements OnClickListener {
            C03321() {
            }

            public void onClick(DialogInterface dialogInterface, int i) {
                MainActivity.this.finish();
            }
        }

        C03335() {
        }

        public void onClick(android.content.DialogInterface r2, int r3) {

            startActivity(new Intent(android.provider.Settings.ACTION_USAGE_ACCESS_SETTINGS));

            return;

            //dandan
            /* new AlertDialog.Builder(MainActivity.this).setTitle("Sorry, your device is not supported")
                    .setMessage("Seems like your phone manufacturer disabled the 'apps with usage access' setting.")
                    .setPositiveButton(R.string.ok, new C03335()).setNegativeButton(R.string.cancel, new C03314()).show(); //dandan .setIcon(17301543).show();
            */

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
                    imageButton.setBackgroundResource(R.drawable.startbutton_clicked_res);
                    //dandan TEMP MainActivity.this.startService(new Intent(MainActivity.this.getApplicationContext(), RunService.class));

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

    private ArrayList<String> allApps = new ArrayList();


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
            MainActivity.this._appsManager.addAllApps1(allApps);

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


    /* JADX WARNING: inconsistent code. dandan */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean isUsageAccessSettingsGranted() {
        AppOpsManager appOps = (AppOpsManager) getApplicationContext()
                .getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow("android:get_usage_stats",
                android.os.Process.myUid(), getApplicationContext().getPackageName());
        boolean granted = mode == AppOpsManager.MODE_ALLOWED;

        return granted;
    }

    private void showSettingsAppsAccessUsageMenu() {
        new AlertDialog.Builder(this).setTitle("Allow messages apps access").setMessage("Please allow this app to determine apps usage").setPositiveButton(R.string.ok, new C03335()).setNegativeButton(R.string.cancel, new C03314()).show(); //dandan .setIcon(17301543).show();
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
            return ((RunningTaskInfo) activityManager.getRunningTasks(1).get(0)).topActivity.getPackageName();
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

        //dandan
        // https://stackoverflow.com/questions/47531742/startforeground-fail-after-upgrade-to-android-8-1
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

    /* JADX: method processing error dandan */
    private void showAlarmScreen(String str) {

        try{
            Intent intent = new Intent("android.intent.action.MAIN");
            intent.putExtra("com.example.dandan.mobileapp", AlarmActivity.class);
            intent.setClass(this, AlarmActivity.class);
            startActivity(intent);

            PendingIntent r1 = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            r1.send(this, 0, intent);
        }
        catch (android.app.PendingIntent.CanceledException e){
            e.printStackTrace();
        }
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
