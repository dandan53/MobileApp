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

import java.util.ArrayList;
import java.util.List;

import android.widget.LinearLayout;
import android.widget.Toast;


public class AppsManager {
    public static final String TAG = "FirisoftAppsManager";
    private Context _context;
    private float _dpDensity = 0;
    private final ArrayList<String> _selectedApps = new ArrayList();

    public AppsManager(Context context) {
        this._context = context;
        _dpDensity = this._context.getResources().getDisplayMetrics().density;
    }

    public ArrayList<String> getSelectedApps() {
        return this._selectedApps;
    }

    public void addAllApps(LinearLayout linearLayout) {

        PackageManager packageManager = _context.getPackageManager();
        List<ApplicationInfo> packages = packageManager.getInstalledApplications(0);

        String packageName = "";
        String packName = "";
        Drawable appIcon;
        String appLabel;

        ArrayList<String> allApps = new ArrayList();

        for (ApplicationInfo applicationInfo : packages) {
            Log.d("APP_INFO", "App: " + applicationInfo.name + " Package: " + applicationInfo.packageName);

            packName = applicationInfo.packageName;

            packageName = packName.toLowerCase();

            if (packageName.contains("whatsapp") ||
                packageName.contains("facebook") ||
                packageName.contains("messages") ||
                packageName.contains("messaging") ||
                packageName.contains("messenger") ||
                packageName.contains("instagram") ||
                packageName.contains("mms") ||
                packageName.contains("sms") ||
                packageName.contains("installer") ||
                packageName.contains("manager") ||
                packageName.contains("system") ||
                packageName.contains("service")){

                try
                {
                    appIcon = packageManager.getApplicationIcon(packName);
                }
                catch (PackageManager.NameNotFoundException e)
                {
                    e.printStackTrace();

                    Resources res = _context.getResources();
                    appIcon = ResourcesCompat.getDrawable(res, R.drawable.startbutton_res, null);
                }

                int stringId = applicationInfo.labelRes;
                if (stringId == 0){
                    appLabel = applicationInfo.nonLocalizedLabel.toString();
                }
                else{
                    appLabel = _context.getString(stringId);
                }

                appLabel = packageName; //dandan

                addApp(linearLayout, 100, appLabel, appIcon, packName); //dandan
            }
        }
    }

//dandan
    private void addApp(LinearLayout linearLayout, int i, String str, Drawable drawable, String str2) {
        int i2 = i;
        final RelativeLayout relativeLayout = new RelativeLayout(this._context);
        LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
        //((RelativeLayout.LayoutParams)layoutParams).setMargins(dpToPx(0), dpToPx(10), dpToPx(0), dpToPx(0));
        //relativeLayout.setLayoutParams(layoutParams);
        relativeLayout.setBackground(this._context.getResources().getDrawable(R.drawable.ic_launcher_background)); //dandan image
        int i3 = i2 + 1;
        relativeLayout.setId(i2);
        LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(-2, -2);
        View checkBox = new CheckBox(this._context);
        //((RelativeLayout.LayoutParams)layoutParams2).setMargins(dpToPx(5), dpToPx(15), dpToPx(0), dpToPx(10));
        //((RelativeLayout.LayoutParams)layoutParams2).addRule(9);
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
        LayoutParams layoutParams3 = new RelativeLayout.LayoutParams(-2, -2);
        View imageView = new ImageView(this._context);
        //((RelativeLayout.LayoutParams)layoutParams3).addRule(1, i3);
        //((RelativeLayout.LayoutParams)layoutParams3).setMargins(dpToPx(15), dpToPx(10), dpToPx(0), dpToPx(10));
        layoutParams3.height = dpToPx(40);
        layoutParams3.width = dpToPx(40);
        imageView.setLayoutParams(layoutParams3);
        //dandan imageView.setImageDrawable(drawable);
        i3 = i2 + 1;
        imageView.setId(i2);
        relativeLayout.addView(imageView);
        layoutParams3 = new RelativeLayout.LayoutParams(-2, -2);
        View textView = new TextView(this._context);
        ((TextView)textView).setText(str);
        //((RelativeLayout.LayoutParams)layoutParams3).addRule(1, i2);
        //((RelativeLayout.LayoutParams)layoutParams3).setMargins(dpToPx(15), dpToPx(20), dpToPx(0), dpToPx(10));
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

