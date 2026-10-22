package com.example.ui.screens

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ads.AdMobConfig
import com.example.ads.InterstitialAdManager
import com.example.data.AppLanguage
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.SkyBluePrimary
import com.example.ui.theme.StatusAmber
import com.example.ui.theme.StatusGreen
import com.example.ui.theme.StatusRed
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

@Composable
fun AdMobGuideScreen(
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val activity = context as? Activity

    var isTestModeActive by remember { mutableStateOf(AdMobConfig.isTestMode) }
    var verificationStatus by remember { mutableStateOf<String?>(null) }
    var isVerifying by remember { mutableStateOf(false) }
    var showProductionWarningDialog by remember { mutableStateOf(false) }

    if (showProductionWarningDialog) {
        AlertDialog(
            onDismissRequest = { showProductionWarningDialog = false },
            icon = {
                Icon(Icons.Default.Warning, contentDescription = null, tint = StatusAmber, modifier = Modifier.size(32.dp))
            },
            title = {
                Text(if (language == AppLanguage.EN) "Enable Production Ads?" else "প্রডাকশন অ্যাড চালু করবেন?")
            },
            text = {
                Text(
                    if (language == AppLanguage.EN)
                        "CRITICAL WARNING:\nNever click your own live advertisements. Google AdMob strictly forbids publisher self-clicks or artificial impressions. Your account may be permanently terminated if live ads are clicked on test devices."
                    else
                        "জরুরি সতর্কতা:\nনিজের অ্যাপের বিজ্ঞাপনে কখনো নিজে ক্লিক করবেন না। গুগল অ্যাডমব এটি কঠোরভাবে নিষিদ্ধ করে। টেস্ট ডিভাইসে লাইভ বিজ্ঞাপনে ক্লিক করলে অ্যাকাউন্ট স্থায়ীভাবে বাতিল হতে পারে।"
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        AdMobConfig.isTestMode = false
                        isTestModeActive = false
                        showProductionWarningDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = StatusRed)
                ) {
                    Text(if (language == AppLanguage.EN) "Switch to Production" else "প্রডাকশনে যান")
                }
            },
            dismissButton = {
                TextButton(onClick = { showProductionWarningDialog = false }) {
                    Text(if (language == AppLanguage.EN) "Cancel (Keep Test Ads)" else "বাতিল (টেস্ট মোডে রাখুন)")
                }
            }
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("admob_guide_screen")
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))

            // Header Banner
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(NavyPrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.MonetizationOn,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(26.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Text(
                            text = if (language == AppLanguage.EN) "AdMob Units & Monetization" else "অ্যাডমব ইউনিট ও মনিটাইজেশন কনফিগ",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = if (language == AppLanguage.EN) "Account: ca-app-pub-5439242993555137" else "অ্যাকাউন্ট: ca-app-pub-5439242993555137",
                            style = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace),
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }

        // Test Mode Switch Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isTestModeActive) StatusGreen.copy(alpha = 0.12f) else StatusAmber.copy(alpha = 0.15f)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (isTestModeActive) Icons.Default.Shield else Icons.Default.Warning,
                                contentDescription = null,
                                tint = if (isTestModeActive) StatusGreen else StatusAmber,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isTestModeActive)
                                    (if (language == AppLanguage.EN) "Test Ads Mode (Active)" else "টেস্ট অ্যাড মোড (সক্রিয়)")
                                else
                                    (if (language == AppLanguage.EN) "Production Live Mode" else "প্রডাকশন লাইভ মোড"),
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (isTestModeActive)
                                (if (language == AppLanguage.EN) "Safe for development & Google Play review." else "ডেভেলপমেন্ট ও পরীক্ষার জন্য নিরাপদ।")
                            else
                                (if (language == AppLanguage.EN) "Serving real ads from your AdMob account." else "আপনার অ্যাকাউন্ট থেকে আসল বিজ্ঞাপন পরিবেশন হচ্ছে।"),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Switch(
                        checked = !isTestModeActive,
                        onCheckedChange = { enableProduction ->
                            if (enableProduction) {
                                showProductionWarningDialog = true
                            } else {
                                AdMobConfig.isTestMode = true
                                isTestModeActive = true
                            }
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = StatusRed,
                            checkedTrackColor = StatusAmber
                        ),
                        modifier = Modifier.testTag("toggle_admob_test_mode")
                    )
                }
            }
        }

        // Your 3 Configured Ad Unit IDs
        item {
            Text(
                text = if (language == AppLanguage.EN) "Your Configured Ad Units" else "আপনার কনফিগার করা ৩টি অ্যাড ইউনিট",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
        }

        item {
            AdUnitCard(
                unitLabel = "Ad Unit 1",
                adUnitId = AdMobConfig.PROD_AD_UNIT_1,
                roleEn = "Assigned to Bottom Banner Placement",
                roleBn = "নিচের ব্যানার বিজ্ঞাপনের জন্য নির্ধারিত",
                onCopy = { clipboardManager.setText(AnnotatedString(AdMobConfig.PROD_AD_UNIT_1)) },
                onVerifyBanner = {
                    isVerifying = true
                    verifyBannerUnit(context, AdMobConfig.PROD_AD_UNIT_1) { result ->
                        verificationStatus = "Unit 1 (Banner check): $result"
                        isVerifying = false
                    }
                },
                onVerifyInterstitial = {
                    isVerifying = true
                    verifyInterstitialUnit(context, AdMobConfig.PROD_AD_UNIT_1) { result ->
                        verificationStatus = "Unit 1 (Interstitial check): $result"
                        isVerifying = false
                    }
                }
            )
        }

        item {
            AdUnitCard(
                unitLabel = "Ad Unit 2",
                adUnitId = AdMobConfig.PROD_AD_UNIT_2,
                roleEn = "Assigned to Repair Wizard Interstitial Placement",
                roleBn = "মেরামত উইজার্ড সমাপ্তির ইন্টারস্টিশিয়ালের জন্য নির্ধারিত",
                onCopy = { clipboardManager.setText(AnnotatedString(AdMobConfig.PROD_AD_UNIT_2)) },
                onVerifyBanner = {
                    isVerifying = true
                    verifyBannerUnit(context, AdMobConfig.PROD_AD_UNIT_2) { result ->
                        verificationStatus = "Unit 2 (Banner check): $result"
                        isVerifying = false
                    }
                },
                onVerifyInterstitial = {
                    isVerifying = true
                    verifyInterstitialUnit(context, AdMobConfig.PROD_AD_UNIT_2) { result ->
                        verificationStatus = "Unit 2 (Interstitial check): $result"
                        isVerifying = false
                    }
                }
            )
        }

        item {
            AdUnitCard(
                unitLabel = "Ad Unit 3",
                adUnitId = AdMobConfig.PROD_AD_UNIT_3,
                roleEn = "Secondary / Auxiliary Slot (Note: Unit 4 was duplicate)",
                roleBn = "অতিরিক্ত স্লট (নোট: ৪ নম্বর আইডিটি ৩ নম্বরের ডুপ্লিকেট ছিল)",
                onCopy = { clipboardManager.setText(AnnotatedString(AdMobConfig.PROD_AD_UNIT_3)) },
                onVerifyBanner = {
                    isVerifying = true
                    verifyBannerUnit(context, AdMobConfig.PROD_AD_UNIT_3) { result ->
                        verificationStatus = "Unit 3 (Banner check): $result"
                        isVerifying = false
                    }
                },
                onVerifyInterstitial = {
                    isVerifying = true
                    verifyInterstitialUnit(context, AdMobConfig.PROD_AD_UNIT_3) { result ->
                        verificationStatus = "Unit 3 (Interstitial check): $result"
                        isVerifying = false
                    }
                }
            )
        }

        // Live Verification Results Card if tested
        if (verificationStatus != null) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Info, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = verificationStatus ?: "",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // App ID vs Ad Unit ID Explanation
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (language == AppLanguage.EN) "App ID vs Ad Unit ID Requirement" else "অ্যাডমব অ্যাপ আইডি এবং অ্যাড ইউনিট আইডির পার্থক্য",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (language == AppLanguage.EN)
                            "• Ad Unit IDs (format: ca-app-pub-5439242993555137/XXXXXXXXXX with a slash '/') identify individual ad slots (Banner, Interstitial).\n\n" +
                                "• AdMob App ID (format: ca-app-pub-5439242993555137~XXXXXXXXXX with a tilde '~') identifies your application in Google Play services.\n\n" +
                                "In AndroidManifest.xml, when ready for release, replace the sample App ID with your real App ID from your AdMob App Settings tab:\n" +
                                "<meta-data android:name=\"com.google.android.gms.ads.APPLICATION_ID\" android:value=\"ca-app-pub-5439242993555137~YOUR_APP_ID\" />"
                        else
                            "• অ্যাড ইউনিট আইডিতে স্ল্যাশ '/' থাকে (যেমন: ca-app-pub-5439242993555137/XXXXXXXXXX), যা ব্যানারে বা ইন্টারস্টিশিয়ালে ব্যবহৃত হয়।\n\n" +
                                "• অ্যাডমব অ্যাপ আইডিতে টিল্ড '~' থাকে (যেমন: ca-app-pub-5439242993555137~XXXXXXXXXX), যা AndroidManifest.xml ফাইলে নির্দিষ্ট থাকে।\n\n" +
                                "কখনোই অ্যাপ আইডি বানিয়ে লিখবেন না; অ্যাডমব কনসোলের App Settings থেকে কপি করে বসাবেন।",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 13.sp, lineHeight = 20.sp),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        // Independent Network Diagnostics Notice
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = StatusGreen, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (language == AppLanguage.EN) "Independent Operation Guarantee" else "বিজ্ঞাপনহীন স্বাধীন কার্যকারিতা নিশ্চয়তা",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = StatusGreen
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = if (language == AppLanguage.EN)
                            "All network diagnostics, SIM tools, repair shop checklists, and settings shortcuts work 100% independently of advertising. If an ad fails to load or the device is offline, the app continues to function seamlessly without interruption."
                        else
                            "অ্যাপের সকল নেটওয়ার্ক ডায়াগনস্টিক, সিম সমাধান, টেকনিশিয়ান চেকলিস্ট এবং সেটিংস শর্টকাট বিজ্ঞাপনের ওপর নির্ভরশীল নয়। কোনো বিজ্ঞাপন লোড না হলেও বা অফলাইনে থাকলেও অ্যাপ নিরবচ্ছিন্নভাবে চলবে।",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 13.sp, lineHeight = 19.sp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun AdUnitCard(
    unitLabel: String,
    adUnitId: String,
    roleEn: String,
    roleBn: String,
    onCopy: () -> Unit,
    onVerifyBanner: () -> Unit,
    onVerifyInterstitial: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = unitLabel,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                )

                IconButton(onClick = onCopy, modifier = Modifier.size(28.dp)) {
                    Icon(imageVector = Icons.Default.ContentCopy, contentDescription = "Copy ID", modifier = Modifier.size(16.dp))
                }
            }

            Text(
                text = adUnitId,
                style = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace, fontWeight = FontWeight.SemiBold, fontSize = 12.sp),
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = roleEn,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(
                    onClick = onVerifyBanner,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Test as Banner", fontSize = 11.sp)
                }

                OutlinedButton(
                    onClick = onVerifyInterstitial,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Test as Interstitial", fontSize = 11.sp)
                }
            }
        }
    }
}

private fun verifyBannerUnit(context: Context, unitId: String, onResult: (String) -> Unit) {
    val adView = AdView(context).apply {
        setAdSize(AdSize.BANNER)
        adUnitId = unitId
        adListener = object : AdListener() {
            override fun onAdLoaded() {
                onResult("Success! This Ad Unit is valid and recognized as Banner.")
            }
            override fun onAdFailedToLoad(error: LoadAdError) {
                onResult("Ad Load Result: Code ${error.code} (${error.message}).")
            }
        }
    }
    adView.loadAd(AdRequest.Builder().build())
}

private fun verifyInterstitialUnit(context: Context, unitId: String, onResult: (String) -> Unit) {
    InterstitialAd.load(
        context,
        unitId,
        AdRequest.Builder().build(),
        object : InterstitialAdLoadCallback() {
            override fun onAdLoaded(ad: InterstitialAd) {
                onResult("Success! This Ad Unit is valid and recognized as Interstitial.")
            }
            override fun onAdFailedToLoad(error: LoadAdError) {
                onResult("Interstitial Result: Code ${error.code} (${error.message}).")
            }
        }
    )
}
