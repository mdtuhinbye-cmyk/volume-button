package com.example.ui.screens

import android.content.Intent
import android.net.Uri
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
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppLanguage
import com.example.ui.theme.StatusGreen

@Composable
fun PrivacyPolicyScreen(
    language: AppLanguage,
    privacyPolicyUrl: String = "https://networkfixbd.example.com/privacy",
    onOpenAdMobGuide: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("privacy_policy_screen")
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))

            // Trust & Security Header
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
                            .background(MaterialTheme.colorScheme.primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Text(
                            text = if (language == AppLanguage.EN) "Privacy & Safety First" else "গোপনীয়তা ও নিরাপত্তা নিশ্চয়তা",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = if (language == AppLanguage.EN) "100% On-Device Telemetry & Zero Data Collection" else "শতভাগ অন-ডিভাইস প্রসেসিং ও শূন্য ট্র্যাকিং",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }

        item {
            PrivacyCard(
                title = if (language == AppLanguage.EN) "1. No Personal Data Collection" else "১. কোনো ব্যক্তিগত তথ্য সংগ্রহ করা হয় না",
                content = if (language == AppLanguage.EN)
                    "Network Fix BD never accesses or uploads your phone number, contacts, call history, SMS messages, media, photos, or precise GPS location. Your privacy is paramount."
                else
                    "নেটওয়ার্ক ফিক্স বিডি আপনার ফোন নম্বর, কন্টাক্ট লিস্ট, কল লগ, এসএমএস বা সুনির্দিষ্ট জিপিএস লোকেশন কখনোই সংরক্ষণ বা কোনো সার্ভারে আপলোড করে না।"
            )
        }

        item {
            PrivacyCard(
                title = if (language == AppLanguage.EN) "2. Required & Optional Permissions" else "২. ব্যবহৃত অনুমতির উদ্দেশ্য",
                content = if (language == AppLanguage.EN)
                    "• INTERNET & ACCESS_NETWORK_STATE: Used exclusively to test connectivity status and load official Google AdMob advertisements.\n" +
                        "• READ_PHONE_STATE (Optional): Used solely on your device to read SIM state and carrier name. If denied, all guides and basic diagnostics remain fully accessible."
                else
                    "• INTERNET ও ACCESS_NETWORK_STATE: কেবল সক্রিয় ইন্টারনেট সংযোগ যাচাই ও গুগল অ্যাডমব প্রদর্শনের জন্য ব্যবহৃত হয়।\n" +
                        "• READ_PHONE_STATE (ঐচ্ছিক): শুধুমাত্র ডিভাইসে সিমের অবস্থা ও ক্যারিয়ার দেখতে ব্যবহৃত হয়। অনুমতি না দিলেও অ্যাপের সকল ফিচার ও নির্দেশিকা স্বাভাবিকভাবে ব্যবহার করা যাবে।"
            )
        }

        item {
            PrivacyCard(
                title = if (language == AppLanguage.EN) "3. Google AdMob Monetization Standards" else "৩. গুগল অ্যাডমব পলিসি সম্মতি",
                content = if (language == AppLanguage.EN)
                    "This app follows strict Google AdMob policies. Ads never imitate Android system error dialogs or network failure banners. Ads are loaded using the official Google Mobile Ads SDK with test units during development."
                else
                    "এই অ্যাপে গুগল অ্যাডমবের সর্বশেষ পলিসি অনুসরণ করা হয়েছে। কোনো বিজ্ঞাপন অ্যান্ড্রয়েড সিস্টেমের ভুল সতর্কতা বা নেটওয়ার্ক অ্যালার্ট হিসেবে প্রদর্শিত হয় না।"
            )
        }

        item {
            // Web Privacy Policy Link
            OutlinedButton(
                onClick = {
                    try {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(privacyPolicyUrl))
                        context.startActivity(intent)
                    } catch (_: Exception) {}
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("open_external_privacy_btn"),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(imageVector = Icons.Default.OpenInNew, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (language == AppLanguage.EN) "View Online Privacy Policy" else "অনলাইন প্রাইভেসি পলিসি দেখুন",
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // AdMob Developer Setup Guide Button
            Button(
                onClick = onOpenAdMobGuide,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("open_admob_developer_guide_btn"),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(imageVector = Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (language == AppLanguage.EN) "AdMob Production Setup Guide" else "অ্যাডমব প্রডাকশন সেটআপ গাইড",
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun PrivacyCard(title: String, content: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, fontSize = 14.sp),
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = content,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 13.sp, lineHeight = 20.sp),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
