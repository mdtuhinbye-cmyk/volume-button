package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AirplanemodeActive
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.NetworkCheck
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SimCard
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ads.AdBannerView
import com.example.data.AppLanguage
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.SkyBluePrimary
import com.example.ui.theme.StatusAmber
import kotlinx.coroutines.launch

data class SettingsShortcut(
    val id: String,
    val titleEn: String,
    val titleBn: String,
    val descEn: String,
    val descBn: String,
    val icon: ImageVector,
    val intentAction: String,
    val manualGuideEn: String,
    val manualGuideBn: String
)

@Composable
fun SettingsShortcutsScreen(
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var manualGuideDialog by remember { mutableStateOf<Pair<String, String>?>(null) }
    var fallbackMessage by remember { mutableStateOf<String?>(null) }

    val shortcuts = remember {
        listOf(
            SettingsShortcut(
                id = "mobile_network",
                titleEn = "Mobile Network & Roaming",
                titleBn = "মোবাইল নেটওয়ার্ক ও রোমিং সেটিংস",
                descEn = "Preferred network type (5G/4G/3G), network operators, VoLTE toggles",
                descBn = "প্রেফার্ড নেটওয়ার্ক মোড, অপারেটর সার্চ ও VoLTE অপশন",
                icon = Icons.Default.NetworkCheck,
                intentAction = Settings.ACTION_DATA_ROAMING_SETTINGS,
                manualGuideEn = "Manual Steps:\n1. Open device Settings.\n2. Tap 'Network & internet' or 'Connections'.\n3. Tap 'Mobile network' or 'SIM card & mobile data'.",
                manualGuideBn = "ম্যানুয়াল ধাপ:\n১. ফোন সেটিংস ওপেন করুন।\n২. 'Network & internet' বা 'Connections' অপশনে যান।\n৩. 'Mobile network' বা 'SIM card & mobile data' সিলেক্ট করুন।"
            ),
            SettingsShortcut(
                id = "sim_manager",
                titleEn = "SIM Card Management",
                titleBn = "সিম কার্ড ম্যানেজার সেটিংস",
                descEn = "Toggle SIM 1 & SIM 2 on/off, designate primary calling and data SIMs",
                descBn = "সিম ১ ও সিম ২ চালু বা বন্ধ, ডিফল্ট কল ও ডাটা সিম নির্বাচন",
                icon = Icons.Default.SimCard,
                intentAction = Settings.ACTION_WIRELESS_SETTINGS,
                manualGuideEn = "Manual Steps:\n1. Open device Settings.\n2. Tap 'Connections' or 'Network & internet'.\n3. Select 'SIM manager' or 'SIM card settings'.",
                manualGuideBn = "ম্যানুয়াল ধাপ:\n১. ফোন সেটিংস ওপেন করুন।\n২. 'Connections' অথবা 'Network & internet'-এ যান।\n৩. 'SIM manager' বা 'SIM card settings' নির্বাচন করুন।"
            ),
            SettingsShortcut(
                id = "apn_settings",
                titleEn = "Access Point Names (APN)",
                titleBn = "এক্সেস পয়েন্ট নেমস (APN) সেটিংস",
                descEn = "Reset APN to default, or enter operator configurations (GP, Robi, BL, Teletalk)",
                descBn = "এপিএন ডিফল্ট রিসেট অথবা অপারেটরের ইন্টারনেট সেটিংস প্রবেশ",
                icon = Icons.Default.Settings,
                intentAction = Settings.ACTION_APN_SETTINGS,
                manualGuideEn = "Manual Steps:\n1. Open Settings -> Mobile Network.\n2. Select your active SIM.\n3. Scroll down and tap 'Access Point Names' (APN).\n4. Tap 3 dots menu -> 'Reset to default'.",
                manualGuideBn = "ম্যানুয়াল ধাপ:\n১. সেটিংস -> মোবাইল নেটওয়ার্কে যান।\n২. আপনার সক্রিয় সিমটি সিলেক্ট করুন।\n৩. নিচে গিয়ে 'Access Point Names' (APN) এ ট্যাপ করুন।\n৪. উপরের ৩-ডট মেন্যু থেকে 'Reset to default' দিন।"
            ),
            SettingsShortcut(
                id = "wifi_settings",
                titleEn = "Wi-Fi & Wireless LAN",
                titleBn = "ওয়াই-ফাই (Wi-Fi) সেটিংস",
                descEn = "Connect to Wi-Fi, manage saved networks, Wi-Fi calling",
                descBn = "ওয়াই-ফাই সংযোগ, সেভ করা নেটওয়ার্ক ও ওয়াই-ফাই কলিং",
                icon = Icons.Default.Wifi,
                intentAction = Settings.ACTION_WIFI_SETTINGS,
                manualGuideEn = "Manual Steps:\n1. Open Settings.\n2. Tap 'Network & internet' -> 'Internet' or 'Wi-Fi'.",
                manualGuideBn = "ম্যানুয়াল ধাপ:\n১. সেটিংস ওপেন করুন।\n২. 'Network & internet' -> 'Internet' বা 'Wi-Fi' এ যান।"
            ),
            SettingsShortcut(
                id = "airplane_settings",
                titleEn = "Airplane Mode & Radios",
                titleBn = "এয়ারপ্লেন মোড ও ওয়্যারলেস সেটিংস",
                descEn = "Quickly access system wireless controls and airplane mode toggle",
                descBn = "সিস্টেম ওয়্যারলেস নিয়ন্ত্রণ এবং এয়ারপ্লেন মোড টগল",
                icon = Icons.Default.AirplanemodeActive,
                intentAction = Settings.ACTION_AIRPLANE_MODE_SETTINGS,
                manualGuideEn = "Manual Steps:\n1. Swipe down the Notification shade.\n2. Tap the Airplane mode quick tile.",
                manualGuideBn = "ম্যানুয়াল ধাপ:\n১. নোটিফিকেশন বার নিচে নামান।\n২. এয়ারপ্লেন মোড আইকনে ক্লিক করে অন/অফ করুন।"
            )
        )
    }

    if (manualGuideDialog != null) {
        AlertDialog(
            onDismissRequest = { manualGuideDialog = null },
            icon = {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(32.dp)
                )
            },
            title = {
                Text(text = manualGuideDialog?.first ?: "Manual Instructions")
            },
            text = {
                Text(text = manualGuideDialog?.second ?: "")
            },
            confirmButton = {
                Button(
                    onClick = { manualGuideDialog = null },
                    modifier = Modifier.testTag("dismiss_manual_guide_dialog")
                ) {
                    Text(if (language == AppLanguage.EN) "Understood" else "বুঝেছি")
                }
            }
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("settings_shortcuts_screen")
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))

            // Guidance Header
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("settings_shortcuts_header"),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = if (language == AppLanguage.EN)
                            "Android System Shortcuts: Tap below to open system settings directly. If your OEM skin blocks direct launch, manual step-by-step instructions are provided."
                        else
                            "সরাসরি অ্যান্ড্রয়েড সিস্টেম সেটিংস: নিচের বাটনে চাপ দিলে সরাসরি নির্দিষ্ট সেটিংসে চলে যাবে। কোনো ডিভাইসে আটকে গেলে সাথে সাথে ম্যানুয়াল নির্দেশিকা পাবেন।",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }

        items(shortcuts, key = { it.id }) { shortcut ->
            SettingsShortcutCard(
                shortcut = shortcut,
                language = language,
                onLaunch = {
                    launchSettingsSafely(
                        context = context,
                        action = shortcut.intentAction,
                        onFallback = {
                            val title = if (language == AppLanguage.EN) shortcut.titleEn else shortcut.titleBn
                            val manual = if (language == AppLanguage.EN) shortcut.manualGuideEn else shortcut.manualGuideBn
                            manualGuideDialog = Pair(title, manual)
                        }
                    )
                },
                onShowManual = {
                    val title = if (language == AppLanguage.EN) shortcut.titleEn else shortcut.titleBn
                    val manual = if (language == AppLanguage.EN) shortcut.manualGuideEn else shortcut.manualGuideBn
                    manualGuideDialog = Pair(title, manual)
                }
            )
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
            AdBannerView()
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun SettingsShortcutCard(
    shortcut: SettingsShortcut,
    language: AppLanguage,
    onLaunch: () -> Unit,
    onShowManual: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("settings_card_${shortcut.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(SkyBluePrimary.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = shortcut.icon,
                        contentDescription = null,
                        tint = SkyBluePrimary,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (language == AppLanguage.EN) shortcut.titleEn else shortcut.titleBn,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 15.sp),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = if (language == AppLanguage.EN) shortcut.descEn else shortcut.descBn,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onLaunch,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("launch_settings_btn_${shortcut.id}"),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.OpenInNew,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (language == AppLanguage.EN) "Open Settings" else "সেটিংস খুলুন",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                TextButton(
                    onClick = onShowManual,
                    modifier = Modifier.testTag("manual_guide_btn_${shortcut.id}")
                ) {
                    Text(
                        text = if (language == AppLanguage.EN) "Manual Steps" else "ম্যানুয়াল গাইড",
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}

private fun launchSettingsSafely(context: Context, action: String, onFallback: () -> Unit) {
    try {
        val intent = Intent(action).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        } else {
            // Try general settings before giving up
            val generalIntent = Intent(Settings.ACTION_SETTINGS).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(generalIntent)
        }
    } catch (_: Exception) {
        onFallback()
    }
}
