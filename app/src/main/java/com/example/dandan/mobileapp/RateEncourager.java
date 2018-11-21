package com.example.dandan.mobileapp;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;

public class RateEncourager {
    private Activity _activity;
    private Context _context;

    //dandan - all comments

    /* renamed from: com.firisoft.firisoft.nevertextanddrive.RateEncourager$1 */
    /*class C03391 implements OnClickListener {
        C03391() {
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            RateEncourager.this.setDidUserRateValue("Never");
        }
    }*/

    /* renamed from: com.firisoft.firisoft.nevertextanddrive.RateEncourager$2 */
    /*class C03402 implements OnClickListener {
        C03402() {
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            RateEncourager.this.setDidUserRateValue("Later");
        }
    }*/

    /* renamed from: com.firisoft.firisoft.nevertextanddrive.RateEncourager$3 */
    /*class C03413 implements OnClickListener {
        C03413() {
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            RateEncourager.this.setDidUserRateValue("Rated");
            RateEncourager.this.showPlaystoreAppPage();
        }
    }*/

    public RateEncourager(Context context, Activity activity) {
        this._context = context;
        this._activity = activity;
    }

    public void EncourageToRateAfterKTimes(int i) {
        String didUserRateValue = getDidUserRateValue();
        if (!didUserRateValue.equals("Rated")) {
            if (!didUserRateValue.equals("Never")) {
                int rateAskingCounter = getRateAskingCounter() + 1;
                setRateAskingCounter(rateAskingCounter);
                if (rateAskingCounter >= i) {
                    setRateAskingCounter(0);
                    askToRate();
                }
            }
        }
    }

    private void askToRate() {
        //dandan new Builder(this._activity).setTitle("Rate this app").setMessage("Did you like this app? Please rate it with 5 stars in the Play store").setPositiveButton("Rate", new C03413()).setNeutralButton("Later", new C03402()).setNegativeButton("Never", new C03391()).setIcon(17301659).show();
    }

    private void showPlaystoreAppPage() {
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
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/299644693.run(Unknown Source)
*/
        /*
        r5 = this;
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r1 = "market://details?id=";
        r0.append(r1);
        r1 = r5._context;
        r1 = r1.getPackageName();
        r0.append(r1);
        r0 = r0.toString();
        r0 = android.net.Uri.parse(r0);
        r1 = new android.content.Intent;
        r2 = "android.intent.action.VIEW";
        r1.<init>(r2, r0);
        r0 = 1208483840; // 0x48080000 float:139264.0 double:5.97070349E-315;
        r1.addFlags(r0);
        r0 = r5._activity;	 Catch:{ ActivityNotFoundException -> 0x002d }
        r0.startActivity(r1);	 Catch:{ ActivityNotFoundException -> 0x002d }
        goto L_0x0054;
    L_0x002d:
        r0 = r5._activity;
        r1 = new android.content.Intent;
        r2 = "android.intent.action.VIEW";
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = "http://play.google.com/store/apps/details?id=";
        r3.append(r4);
        r4 = r5._context;
        r4 = r4.getPackageName();
        r3.append(r4);
        r3 = r3.toString();
        r3 = android.net.Uri.parse(r3);
        r1.<init>(r2, r3);
        r0.startActivity(r1);
    L_0x0054:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.firisoft.firisoft.nevertextanddrive.RateEncourager.showPlaystoreAppPage():void");
    }

    private void setRateAskingCounter(int i) {
        Context context = this._context;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this._context.getPackageName());
        stringBuilder.append("Rating");
        String stringBuilder2 = stringBuilder.toString();
        Context context2 = this._context;
        context.getSharedPreferences(stringBuilder2, 0).edit().putInt("RateAskingCounter", i).commit();
    }

    private int getRateAskingCounter() {
        Context context = this._context;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this._context.getPackageName());
        stringBuilder.append("Rating");
        String stringBuilder2 = stringBuilder.toString();
        Context context2 = this._context;
        return context.getSharedPreferences(stringBuilder2, 0).getInt("RateAskingCounter", 0);
    }

    private void setDidUserRateValue(String str) {
        Context context = this._context;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this._context.getPackageName());
        stringBuilder.append("Rating");
        String stringBuilder2 = stringBuilder.toString();
        Context context2 = this._context;
        context.getSharedPreferences(stringBuilder2, 0).edit().putString("DidUserRate", str).commit();
    }

    private String getDidUserRateValue() {
        Context context = this._context;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this._context.getPackageName());
        stringBuilder.append("Rating");
        String stringBuilder2 = stringBuilder.toString();
        Context context2 = this._context;
        return context.getSharedPreferences(stringBuilder2, 0).getString("DidUserRate", "Later");
    }
}
