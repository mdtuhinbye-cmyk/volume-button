package com.example.ui.screens

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SignalCellularAlt
import androidx.compose.material.icons.filled.SimCard
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ads.AdBannerView
import com.example.data.AppLanguage
import com.example.data.DeviceNetworkInfo
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.SkyBluePrimary
import com.example.ui.theme.StatusAmber
import com.example.ui.theme.StatusGreen
import com.example.ui.theme.StatusRed

@Composable
fun DeviceNetworkInfoScreen(
    info: DeviceNetworkInfo,
    language: AppLanguage,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showPermissionRationale by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { _ ->
        onRefresh()
    }

    if (showPermissionRationale) {
        AlertDialog(
            onDismissRequest = { showPermissionRationale = false },
            icon = {
                Icon(
                    imageVector = Icons.Default.Security,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(32.dp)
                )
            },
            title = {
                Text(
                    text = if (language == AppLanguage.EN) "Phone State Permission" else "ফোন স্টেট অনুমতি"
                )
            },
            text = {
                Text(
                    text = if (language == AppLanguage.EN)
                        "To read real-time cellular carrier names and SIM hardware state, Android requires the READ_PHONE_STATE permission.\n\n" +
                            "Privacy Guarantee: Network Fix BD NEVER accesses your phone number, contacts, call logs, SMS, or precise location. All diagnostics run 100% locally on your device."
                    else
                        "লাইভ সেলুলার ক্যারিয়ার নাম এবং সিম স্লট স্ট্যাটাস দেখতে অ্যান্ড্রয়েডের READ_PHONE_STATE অনুমতির প্রয়োজন।\n\n" +
                            "গোপনীয়তা নিশ্চয়তা: এই অ্যাপ কোনো ব্যক্তিগত ফোন নম্বর, কল হিস্ট্রি, এসএমএস বা অবস্থান সংগ্রহ করে না। সকল তথ্য আপনার ফোনেই সুরক্ষিত থাকে।"
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showPermissionRationale = false
                        permissionLauncher.launch(Manifest.permission.READ_PHONE_STATE)
                    },
                    modifier = Modifier.testTag("confirm_grant_permission_btn")
                ) {
                    Text(if (language == AppLanguage.EN) "Grant Permission" else "অনুমতি দিন")
                }
            },
            dismissButton = {
                TextButton(onClick = { showPermissionRationale = false }) {
                    Text(if (language == AppLanguage.EN) "Not Now" else "এখন নয়")
                }
            }
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("device_network_info_screen")
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))

            // Permission status banner
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("permission_status_card"),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (info.hasPhoneStatePermission)
                        StatusGreen.copy(alpha = 0.12f)
                    else
                        StatusAmber.copy(alpha = 0.12f)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (info.hasPhoneStatePermission) Icons.Default.Security else Icons.Default.Lock,
                        contentDescription = null,
                        tint = if (info.hasPhoneStatePermission) StatusGreen else StatusAmber,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (info.hasPhoneStatePermission)
                                (if (language == AppLanguage.EN) "Diagnostic Permission: Granted" else "ডায়াগনস্টিক অনুমতি: সক্রিয়")
                            else
                                (if (language == AppLanguage.EN) "Limited Access (Standard Mode)" else "সীমিত অ্যাক্সেস (স্ট্যান্ডার্ড মোড)"),
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = if (language == AppLanguage.EN) info.permissionNote else "ঐচ্ছিক ফোন স্টেট পারমিশন ছাড়া কিছু ক্যারিয়ার তথ্য লুকায়িত থাকতে পারে।",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    if (!info.hasPhoneStatePermission) {
                        FilledTonalButton(
                            onClick = { showPermissionRationale = true },
                            modifier = Modifier.testTag("request_permission_button")
                        ) {
                            Text(
                                text = if (language == AppLanguage.EN) "Enable" else "অনুমতি",
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }

        // Section 1: Device Hardware & OS
        item {
            InfoSectionHeader(
                icon = Icons.Default.PhoneAndroid,
                title = if (language == AppLanguage.EN) "Device & OS Specifications" else "ডিভাইস ও অপারেটিং সিস্টেম"
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    InfoRow(
                        label = if (language == AppLanguage.EN) "Manufacturer" else "প্রস্তুতকারক",
                        value = info.manufacturer
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))
                    InfoRow(
                        label = if (language == AppLanguage.EN) "Device Model" else "মডেল নম্বর",
                        value = info.model
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))
                    InfoRow(
                        label = if (language == AppLanguage.EN) "Android Version" else "অ্যান্ড্রয়েড ভার্সন",
                        value = "${info.androidVersion} (API ${info.sdkInt})"
                    )
                }
            }
        }

        // Section 2: SIM Card Diagnostics
        item {
            InfoSectionHeader(
                icon = Icons.Default.SimCard,
                title = if (language == AppLanguage.EN) "SIM Card & Hardware Slots" else "সিম কার্ড ও মডেম স্লট তথ্য"
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    InfoRow(
                        label = if (language == AppLanguage.EN) "SIM State" else "সিম অবস্থা",
                        value = info.simStateLabel,
                        valueColor = if (info.simStateCode == android.telephony.TelephonyManager.SIM_STATE_READY) StatusGreen else StatusAmber
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))
                    InfoRow(
                        label = if (language == AppLanguage.EN) "Hardware Modem Slots" else "হার্ডওয়্যার সিম স্লট",
                        value = "${info.simSlotCount} Slot(s) Available"
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))
                    InfoRow(
                        label = if (language == AppLanguage.EN) "Carrier (SIM Operator)" else "সিম অপারেটর",
                        value = info.carrierName ?: if (info.hasPhoneStatePermission) "Not Detected" else "Restricted (Needs Permission)"
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))
                    InfoRow(
                        label = if (language == AppLanguage.EN) "Registered Network Operator" else "নিবন্ধিত নেটওয়ার্ক",
                        value = info.networkOperatorName ?: if (info.hasPhoneStatePermission) "No Service" else "Restricted"
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))
                    InfoRow(
                        label = if (language == AppLanguage.EN) "Roaming Status" else "রোমিং অবস্থা",
                        value = if (info.isRoaming) "Active (Roaming)" else "Inactive (Home Network)"
                    )
                }
            }
        }

        // Section 3: Connection & Signal Telemetry
        item {
            InfoSectionHeader(
                icon = Icons.Default.SignalCellularAlt,
                title = if (language == AppLanguage.EN) "Live Network Telemetry" else "লাইভ নেটওয়ার্ক ও সংযোগ টেলিমেট্রি"
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    InfoRow(
                        label = if (language == AppLanguage.EN) "Active Connection Type" else "সক্রিয় সংযোগ টাইপ",
                        value = info.activeConnectionType,
                        valueColor = if (info.isConnected) MaterialTheme.colorScheme.primary else StatusRed
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))
                    InfoRow(
                        label = if (language == AppLanguage.EN) "Internet Connectivity" else "ইন্টারনেট অ্যাক্সেস",
                        value = if (info.isConnected) "Online (Validated)" else "Offline / Disconnected",
                        valueColor = if (info.isConnected) StatusGreen else StatusRed
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))
                    InfoRow(
                        label = if (language == AppLanguage.EN) "Data Metering" else "ডাটা মিটারিং",
                        value = if (info.isMetered) "Metered (Cellular / Pay-per-MB)" else "Unmetered (Flat Rate / Wi-Fi)"
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))
                    InfoRow(
                        label = if (language == AppLanguage.EN) "Airplane Mode" else "এয়ারপ্লেন মোড",
                        value = if (info.isAirplaneModeOn) "ENABLED (Radios Off)" else "DISABLED (Radios Active)",
                        valueColor = if (info.isAirplaneModeOn) StatusAmber else StatusGreen
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(6.dp))
            Button(
                onClick = onRefresh,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("refresh_device_info_btn"),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (language == AppLanguage.EN) "Re-Scan Device Telemetry" else "পুনরায় টেলিমেট্রি স্ক্যান করুন",
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            AdBannerView()
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun InfoSectionHeader(icon: ImageVector, title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
private fun InfoRow(
    label: String,
    value: String,
    valueColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onSurface
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
            color = valueColor
        )
    }
}
