package com.example.dandan.mobileapp;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import java.util.ArrayList;
import java.util.List;

import android.widget.LinearLayout;
import android.widget.Toast;


public class AppsManager {
    public static final String TAG = "FirisoftAppsManager";
    private Context _context;
    private float _dpDensity = 0;
    private final ArrayList<String> _selectedApps = new ArrayList();
    private final ArrayList<String> _allApps = new ArrayList();


    public AppsManager(Context context) {
        this._context = context;
        _dpDensity = this._context.getResources().getDisplayMetrics().density;
    }

    public ArrayList<String> getSelectedApps() {
        return this._selectedApps;
    }

    public ArrayList<String> getAllApps() {
        return this._allApps;
    }

    public void addAllApps(LinearLayout linearLayout) {

        PackageManager packageManager = _context.getPackageManager();
        List<ApplicationInfo> applications = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);

        String packageName = "";
        String packName = "";
        Drawable appIcon;
        CharSequence appLabel;
        String appLabelStr;

        int r1 = 100;

        ArrayList<String> allApps = new ArrayList();

        for (ApplicationInfo applicationInfo : applications) {
            try {
                Log.d("APP_INFO", "App: " + applicationInfo.name + " Package: " + applicationInfo.packageName);

                packName = applicationInfo.packageName;
                packageName = packName.toLowerCase();

                if ((packageName.contains(".whatsapp") ||
                        packageName.contains(".facebook") ||
                        packageName.contains(".messages") ||
                        packageName.contains(".messaging") ||
                        packageName.contains(".messenger") ||
                        packageName.contains(".instagram") ||
                        packageName.contains(".mms")) &&
                        (!packageName.contains("installer") &&
                        !packageName.contains("manager") &&
                        !packageName.contains("service") &&
                        !packageName.contains("system"))){
                        //packageName.contains(".sms") ||
                        //packageName.contains(".manager") ||
                        //packageName.contains(".system") ||
                        //packageName.contains(".service")) {

                    try {
                        appIcon = packageManager.getApplicationIcon(packName);
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();

                        Resources res = _context.getResources();
                        appIcon = ResourcesCompat.getDrawable(res, R.drawable.startbutton_res, null);
                    }

                    r1 = r1 + 4;

                    try{
                        appLabel = packageManager.getApplicationLabel(applicationInfo);
                        appLabelStr = appLabel.toString();
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        appLabelStr = packName;
                    }

                    /* dandan instead   appLabel = _context.getApplicationLabel(applicationInfo);
                    try{
                        int stringId = applicationInfo.labelRes;
                        if (stringId == 0) {
                            appLabel = applicationInfo.nonLocalizedLabel.toString();
                        } else {
                            appLabel = _context.getString(stringId);
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    */

                    // dandan via the list _selectedApps.add(packageName);

                    _allApps.add(packageName); //dandan
                    addApp(linearLayout, r1, appLabelStr, appIcon, packName);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void addApp(LinearLayout linearLayout, int i, String str, Drawable drawable, String str2) {
        int i2 = i;
        final RelativeLayout relativeLayout = new RelativeLayout(this._context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
        layoutParams.setMargins(dpToPx(0), dpToPx(10), dpToPx(0), dpToPx(0));
        relativeLayout.setLayoutParams(layoutParams);
        relativeLayout.setBackground(this._context.getResources().getDrawable(R.drawable.big_card));
        int i3 = i2 + 1;
        relativeLayout.setId(i2);
        RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(-2, -2);
        View checkBox = new CheckBox(this._context);
        layoutParams2.setMargins(dpToPx(5), dpToPx(15), dpToPx(0), dpToPx(10));
        layoutParams2.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        checkBox.setLayoutParams(layoutParams2);
        final String str3 = str2;
        ((CheckBox) checkBox).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (z) {
                    AppsManager.this._selectedApps.add(str3);
                } else {
                    AppsManager.this._selectedApps.remove(str3);
                }
            }
        });
        i2 = i3 + 1;
        checkBox.setId(i3);
        relativeLayout.addView(checkBox);
        RelativeLayout.LayoutParams layoutParams3 = new RelativeLayout.LayoutParams(-2, -2);
        ImageView imageView = new ImageView(this._context);
        layoutParams3.addRule(1, i3);
        layoutParams3.setMargins(dpToPx(15), dpToPx(10), dpToPx(0), dpToPx(10));
        layoutParams3.height = dpToPx(40);
        layoutParams3.width = dpToPx(40);
        imageView.setLayoutParams(layoutParams3);
        imageView.setImageDrawable(drawable);
        i3 = i2 + 1;
        imageView.setId(i2);
        relativeLayout.addView(imageView);
        layoutParams3 = new RelativeLayout.LayoutParams(-2, -2);
        TextView textView = new TextView(this._context);
        textView.setText(str);
        layoutParams3.addRule(1, i2);
        layoutParams3.setMargins(dpToPx(15), dpToPx(20), dpToPx(0), dpToPx(10));
        textView.setLayoutParams(layoutParams3);
        textView.setId(i3);
        relativeLayout.addView(textView);
        final LinearLayout linearLayout2 = linearLayout;
        ((MainActivity) this._context).runOnUiThread(new Runnable() {
            public void run() {
                linearLayout2.addView(relativeLayout);
            }
        });
    }


    private int dpToPx(int i) {
        return (int) (((float) i) * this._dpDensity);
    }

}

