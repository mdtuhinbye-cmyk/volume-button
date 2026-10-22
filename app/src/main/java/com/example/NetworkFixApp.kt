package com.example

import android.app.Application
import android.util.Log
import com.google.android.gms.ads.MobileAds

class NetworkFixApp : Application() {
    override fun onCreate() {
        super.onCreate()
        try {
            // Initialize Google Mobile Ads SDK on a background thread
            MobileAds.initialize(this) { initializationStatus ->
                Log.d("NetworkFixApp", "AdMob initialized: $initializationStatus")
            }
        } catch (e: Exception) {
            Log.e("NetworkFixApp", "AdMob initialization failed", e)
        }
    }
}
