package com.example.dandan.mobileapp;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

public class DrivingListener {
    private boolean _alreadyDetectedStartBlockingSpeed;
    private boolean _alreadyDetectedStopBlockingSpeed;
    private Context _context;
    private LocationManager _lm;
    private double _speedToStartBlockingSms;
    private double _speedToStopBlockingSms;
    private long _startTimeOfDetectedBlockingSpeed;
    private long _startTimeOfDetectedStopBlockingSpeed;
    LocationListener locationListener = new C03271();

    /* renamed from: com.firisoft.firisoft.nevertextanddrive.DrivingListener$1 */
    class C03271 implements LocationListener {
        public void onProviderDisabled(String str) {
        }

        public void onProviderEnabled(String str) {
        }

        public void onStatusChanged(String str, int i, Bundle bundle) {
        }

        C03271() {
        }

        public void onLocationChanged(Location location) {
            DrivingListener.this.new_loc(location);
        }
    }

    public DrivingListener(Context context) {
        this._context = context;
    }

    void unregisterRequestingUpdates() {
        if (this._lm != null) {
            if (ContextCompat.checkSelfPermission(this._context, "android.permission.ACCESS_FINE_LOCATION") == 0 || ContextCompat.checkSelfPermission(this._context, "android.permission.ACCESS_COARSE_LOCATION") == 0) {
                //dandan this._lm.removeUpdates(this.locationListener);
            }
        }
    }

    public void request_updates() {
        if (!this._lm.isProviderEnabled("gps")) {
            Toast.makeText(this._context, "Can't listening to driving, GPS is Disabled. Please enable it.", Toast.LENGTH_SHORT).show();
        } else if (ContextCompat.checkSelfPermission(this._context, "android.permission.ACCESS_FINE_LOCATION") == 0 || ContextCompat.checkSelfPermission(this._context, "android.permission.ACCESS_COARSE_LOCATION") == 0) {
           //dandan this._lm.requestLocationUpdates("gps", 0, 0.0f, this.locationListener);
            Toast.makeText(this._context, "Listening to driving.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this._context, "Can't listening to driving. No permission was granted.", Toast.LENGTH_SHORT).show();
        }
    }

    public void StartGpsListener(double d, double d2) {
        this._speedToStartBlockingSms = d;
        this._speedToStopBlockingSms = d2;
        //dandan d = this._context;
        Context context = this._context;
        //dandan this._lm = (LocationManager) d.getSystemService("location");
        request_updates();
    }

    public void new_loc(Location location) {
        if (((double) location.getAccuracy()) > 0.0d && location.getAccuracy() < 5.0f && ((double) location.getSpeed()) * 3.6d >= this._speedToStartBlockingSms) {
            this._alreadyDetectedStopBlockingSpeed = false;
            if (!this._alreadyDetectedStartBlockingSpeed) {
                this._alreadyDetectedStartBlockingSpeed = true;
                this._startTimeOfDetectedBlockingSpeed = System.currentTimeMillis();
            } else if (System.currentTimeMillis() - this._startTimeOfDetectedBlockingSpeed > 3000) {
                //dandan ((MainActivity) this._context).startClosingAppTimer();
                this._alreadyDetectedStartBlockingSpeed = false;
            }
        }
        if (((double) location.getSpeed()) * 3.6d <= this._speedToStopBlockingSms) {
            this._alreadyDetectedStartBlockingSpeed = false;
            //dandan if (this._alreadyDetectedStopBlockingSpeed == null) {
            if (!this._alreadyDetectedStopBlockingSpeed) {
                this._alreadyDetectedStopBlockingSpeed = true;
                this._startTimeOfDetectedStopBlockingSpeed = System.currentTimeMillis();
            } else if (System.currentTimeMillis() - this._startTimeOfDetectedStopBlockingSpeed > 3000) {
                //dandan ((MainActivity) this._context).stopClosingAppTimer();
                this._alreadyDetectedStopBlockingSpeed = false;
            }
        }
    }
}
