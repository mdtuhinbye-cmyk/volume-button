package com.example.ads

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

class InterstitialAdManager(private val context: Context) {

    private var interstitialAd: InterstitialAd? = null
    private var isLoading = false

    fun loadAd(adUnitId: String = AdMobConfig.getActiveInterstitialAdUnitId()) {
        if (isLoading || interstitialAd != null) return
        isLoading = true

        val adRequest = AdRequest.Builder().build()
        try {
            InterstitialAd.load(
                context,
                adUnitId,
                adRequest,
                object : InterstitialAdLoadCallback() {
                    override fun onAdLoaded(ad: InterstitialAd) {
                        interstitialAd = ad
                        isLoading = false
                        Log.d("InterstitialAdManager", "Interstitial ad loaded successfully")
                    }

                    override fun onAdFailedToLoad(error: LoadAdError) {
                        interstitialAd = null
                        isLoading = false
                        Log.d("InterstitialAdManager", "Interstitial ad failed to load: ${error.message} (Code: ${error.code})")
                    }
                }
            )
        } catch (e: Exception) {
            isLoading = false
            interstitialAd = null
            Log.e("InterstitialAdManager", "Interstitial load failed exception", e)
        }
    }

    /**
     * Safely show interstitial ad at natural transition points.
     * @param activity current Activity
     * @param onDismissed callback invoked when ad finishes or if ad was unavailable
     */
    fun showAd(activity: Activity, onDismissed: () -> Unit) {
        val ad = interstitialAd
        if (ad != null) {
            ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    interstitialAd = null
                    loadAd() // Preload next
                    onDismissed()
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    interstitialAd = null
                    loadAd()
                    onDismissed()
                }
            }
            try {
                ad.show(activity)
            } catch (_: Exception) {
                interstitialAd = null
                onDismissed()
            }
        } else {
            // If ad not ready, proceed without blocking the user
            loadAd()
            onDismissed()
        }
    }

    fun isAdReady(): Boolean = interstitialAd != null
}
