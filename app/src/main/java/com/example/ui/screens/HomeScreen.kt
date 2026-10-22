package com.example.ui.screens

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
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.NetworkCell
import androidx.compose.material.icons.filled.NetworkCheck
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SignalCellularConnectedNoInternet0Bar
import androidx.compose.material.icons.filled.SimCard
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
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
import com.example.data.TroubleshootingRepository
import com.example.ui.components.DiagnosticCard
import com.example.ui.components.StatusBanner
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.SkyBluePrimary
import com.example.ui.theme.StatusAmber
import com.example.ui.theme.StatusGreen
import com.example.ui.theme.StatusRed

data class DashboardItem(
    val id: String,
    val titleEn: String,
    val titleBn: String,
    val subtitleEn: String,
    val subtitleBn: String,
    val icon: ImageVector,
    val tag: String,
    val route: String
)

@Composable
fun HomeScreen(
    deviceInfo: DeviceNetworkInfo,
    language: AppLanguage,
    onRefreshInfo: () -> Unit,
    onNavigateToRoute: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }

    val dashboardItems = remember {
        listOf(
            DashboardItem(
                id = "1",
                titleEn = "1. SIM Card Problems",
                titleBn = "১. সিম কার্ড সমস্যা",
                subtitleEn = "Not detected, unavailable, disabled in settings, PUK",
                subtitleBn = "সিম পাওয়া যাচ্ছে না, অনুপলব্ধ, সেটিংসে বন্ধ, PUK কোড",
                icon = Icons.Default.SimCard,
                tag = "SIM",
                route = "sim_troubleshoot"
            ),
            DashboardItem(
                id = "2",
                titleEn = "2. No Network / No Signal",
                titleBn = "২. কোন নেটওয়ার্ক / সিগন্যাল নেই",
                subtitleEn = "Emergency calls only, no service, baseband checks",
                subtitleBn = "ইমার্জেন্সি কল অনলি, নো সার্ভিস, বেসব্যান্ড পরীক্ষা",
                icon = Icons.Default.SignalCellularConnectedNoInternet0Bar,
                tag = "SIGNAL",
                route = "network_troubleshoot"
            ),
            DashboardItem(
                id = "3",
                titleEn = "3. Weak Signal Troubleshooting",
                titleBn = "৩. দুর্বল সিগন্যাল সমাধান",
                subtitleEn = "Fluctuating bars, building attenuation, case interference",
                subtitleBn = "সিগন্যাল ওঠানামা, কভার ও অ্যান্টেনা প্রতিবন্ধকতা",
                icon = Icons.Default.NetworkCheck,
                tag = "SIGNAL",
                route = "network_troubleshoot"
            ),
            DashboardItem(
                id = "4",
                titleEn = "4. Mobile Data Not Working",
                titleBn = "৪. মোবাইল ডাটা কাজ করছে না",
                subtitleEn = "APN reset, data limits, balance checks for BD operators",
                subtitleBn = "এপিএন রিসেট, ডাটা লিমিট, অপারেটর ব্যালেন্স ও প্যাক",
                icon = Icons.Default.Speed,
                tag = "DATA",
                route = "network_troubleshoot"
            ),
            DashboardItem(
                id = "5",
                titleEn = "5. 4G / 5G Connectivity",
                titleBn = "৫. ৪জি / ৫জি কানেক্টিভিটি",
                subtitleEn = "Stuck in 2G/3G, USIM requirements, network mode selection",
                subtitleBn = "২জি/৩জি তে আটকে থাকা, ৪জি সিমের প্রয়োজনীয়তা",
                icon = Icons.Default.NetworkCell,
                tag = "DATA",
                route = "network_troubleshoot"
            ),
            DashboardItem(
                id = "6",
                titleEn = "6. Calls and SMS Problems",
                titleBn = "৬. কল ও এসএমএস সমস্যা",
                subtitleEn = "Call drops, SMS center numbers (GP, Robi, BL, Teletalk)",
                subtitleBn = "কল কেটে যাওয়া, এসএমএস সেন্টার নম্বর সেটিংস",
                icon = Icons.Default.Call,
                tag = "VOICE",
                route = "network_troubleshoot"
            ),
            DashboardItem(
                id = "7",
                titleEn = "7. Network Settings Help",
                titleBn = "৭. নেটওয়ার্ক সেটিংস সহায়তা",
                subtitleEn = "Direct system settings shortcuts & manual fallbacks",
                subtitleBn = "সিস্টেম সেটিংস শর্টকাট ও ডিভাইস ভিত্তিক ম্যানুয়াল গাইড",
                icon = Icons.Default.Settings,
                tag = "SYSTEM",
                route = "settings_shortcuts"
            ),
            DashboardItem(
                id = "8",
                titleEn = "8. Device Network Information",
                titleBn = "৮. ডিভাইস ও নেটওয়ার্ক তথ্য",
                subtitleEn = "Live SIM state, operator name, modem slots & Android specs",
                subtitleBn = "সিম অবস্থা, অপারেটর নাম, মোডেম ও অ্যান্ড্রয়েড তথ্য",
                icon = Icons.Default.Info,
                tag = "HARDWARE",
                route = "device_info"
            ),
            DashboardItem(
                id = "9",
                titleEn = "9. Step-by-Step Repair Guide",
                titleBn = "৯. ধাপে ধাপে মেরামত নির্দেশিকা",
                subtitleEn = "Guided interactive repair workflows with safe actions",
                subtitleBn = "নিরাপদ ধাপে ধাপে ডায়াগনস্টিক ও সমাধানের নির্দেশিকা",
                icon = Icons.Default.Build,
                tag = "REPAIR",
                route = "repair_guide"
            ),
            DashboardItem(
                id = "10",
                titleEn = "10. Frequently Asked Questions",
                titleBn = "১০. সাধারণ প্রশ্নোত্তর (FAQ)",
                subtitleEn = "Real facts on signal boosting, PUK rules, and BD APNs",
                subtitleBn = "সিগন্যাল বৃদ্ধির সত্যতা, PUK নিয়ম ও অপারেটর এপিএন",
                icon = Icons.Default.HelpOutline,
                tag = "INFO",
                route = "faqs"
            )
        )
    }

    val filteredItems = remember(searchQuery, language) {
        if (searchQuery.isBlank()) {
            dashboardItems
        } else {
            val q = searchQuery.trim().lowercase()
            dashboardItems.filter {
                it.titleEn.lowercase().contains(q) ||
                    it.titleBn.contains(q) ||
                    it.subtitleEn.lowercase().contains(q) ||
                    it.subtitleBn.contains(q) ||
                    it.tag.lowercase().contains(q)
            }
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("home_screen_content")
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("search_problem_input"),
                placeholder = {
                    Text(
                        text = if (language == AppLanguage.EN)
                            "Search network or SIM problem..."
                        else
                            "নেটওয়ার্ক বা সিম সমস্যা খুঁজুন...",
                        fontSize = 14.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear")
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.surfaceVariant,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Live Network Status
            StatusBanner(
                info = deviceInfo,
                language = language,
                onRefresh = onRefreshInfo,
                onOpenDetails = { onNavigateToRoute("device_info") }
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Technician / Repair Shop Mode Special Card
            RepairShopBanner(
                language = language,
                onClick = { onNavigateToRoute("repair_shop") }
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = if (language == AppLanguage.EN) "Troubleshooting Modules" else "সমস্যা সমাধান মডিউলসমূহ",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp
                ),
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        // List of 10 feature cards
        items(filteredItems, key = { it.id }) { item ->
            val iconTint = when (item.tag) {
                "SIM" -> SkyBluePrimary
                "SIGNAL" -> NavyPrimary
                "DATA" -> StatusGreen
                "VOICE" -> StatusAmber
                "SYSTEM" -> MaterialTheme.colorScheme.primary
                "HARDWARE" -> StatusRed
                "REPAIR" -> NavyPrimary
                else -> SkyBluePrimary
            }

            DiagnosticCard(
                title = if (language == AppLanguage.EN) item.titleEn else item.titleBn,
                subtitle = if (language == AppLanguage.EN) item.subtitleEn else item.subtitleBn,
                icon = item.icon,
                iconTint = iconTint,
                badgeText = item.tag,
                testTag = "feature_card_${item.id}",
                onClick = { onNavigateToRoute(item.route) }
            )
        }

        // Banner Ad slot at bottom
        item {
            Spacer(modifier = Modifier.height(8.dp))
            AdBannerView()
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun RepairShopBanner(
    language: AppLanguage,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("repair_shop_mode_banner")
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(NavyPrimary.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Build,
                    contentDescription = null,
                    tint = NavyPrimary,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (language == AppLanguage.EN) "Repair Shop Mode" else "মোবাইল সার্ভিসিং ও টেকনিশিয়ান কর্নার",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 15.sp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(StatusAmber.copy(alpha = 0.2f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "PRO",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, fontSize = 9.sp),
                            color = StatusAmber
                        )
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = if (language == AppLanguage.EN)
                        "Hardware checklists, baseband checks & customer Bangla scripts"
                    else
                        "হার্ডওয়্যার চেকপ্রক্রিয়া, বেসব্যান্ড ডায়াগনস্টিক ও গ্রাহক স্ক্রিপ্ট",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                )
            }
        }
    }
}
