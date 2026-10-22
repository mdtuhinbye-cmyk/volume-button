package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AirplanemodeActive
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.NetworkCheck
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ads.AdBannerView
import com.example.data.AppLanguage
import com.example.data.GuideCategory
import com.example.data.TroubleshootingGuide
import com.example.data.TroubleshootingRepository
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.SkyBluePrimary
import com.example.ui.theme.StatusAmber
import com.example.ui.theme.StatusGreen

@Composable
fun NetworkTroubleshootScreen(
    language: AppLanguage,
    onOpenSettingsShortcut: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf<GuideCategory?>(null) }

    val networkGuides = remember(selectedCategory) {
        val all = TroubleshootingRepository.guides.filter { it.category != GuideCategory.SIM }
        if (selectedCategory == null) all else all.filter { it.category == selectedCategory }
    }

    var expandedGuideId by remember { mutableStateOf<String?>(networkGuides.firstOrNull()?.id) }
    val checkedSteps = remember { mutableStateMapOf<String, Boolean>() }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("network_troubleshoot_screen")
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))

            // Safety Warning Banner
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("network_safety_notice_card"),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = StatusAmber.copy(alpha = 0.12f)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Notice",
                        tint = StatusAmber,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = if (language == AppLanguage.EN)
                            "Reliable Diagnostic: We do not promise artificial signal boosts. Cellular radio reception relies on tower distance, terrain, and proper antenna/modem registration."
                        else
                            "নির্ভুল তথ্য: আমরা কৃত্রিমভাবে সিগন্যাল বৃদ্ধির কোনো ভুয়া প্রতিশ্রুতি দিই না। ফোনের অ্যান্টেনা ও টাওয়ারের রেডিও সংযোগের সঠিক ডায়াগনস্টিক ধাপ নিচে দেওয়া হলো।",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Category Filter Chips
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    FilterChip(
                        selected = selectedCategory == null,
                        onClick = { selectedCategory = null },
                        label = { Text(if (language == AppLanguage.EN) "All Issues" else "সকল সমস্যা") },
                        modifier = Modifier.testTag("filter_all_network")
                    )
                }
                item {
                    FilterChip(
                        selected = selectedCategory == GuideCategory.NETWORK,
                        onClick = { selectedCategory = GuideCategory.NETWORK },
                        label = { Text(if (language == AppLanguage.EN) "Signal & Service" else "সিগন্যাল ও সার্ভিস") },
                        modifier = Modifier.testTag("filter_signal")
                    )
                }
                item {
                    FilterChip(
                        selected = selectedCategory == GuideCategory.MOBILE_DATA,
                        onClick = { selectedCategory = GuideCategory.MOBILE_DATA },
                        label = { Text(if (language == AppLanguage.EN) "Mobile Data & 5G" else "মোবাইল ডাটা ও ৫জি") },
                        modifier = Modifier.testTag("filter_data")
                    )
                }
                item {
                    FilterChip(
                        selected = selectedCategory == GuideCategory.CALLS_SMS,
                        onClick = { selectedCategory = GuideCategory.CALLS_SMS },
                        label = { Text(if (language == AppLanguage.EN) "Calls & SMS" else "কল ও মেসেজ") },
                        modifier = Modifier.testTag("filter_calls")
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Airplane Mode Quick Reset Tip
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("airplane_mode_tip_card"),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AirplanemodeActive,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = if (language == AppLanguage.EN)
                            "Quick Action: Toggle Airplane Mode ON for 15s to force tower re-registration."
                        else
                            "সহজ ট্রিক: ১৫ সেকেন্ডের জন্য এয়ারপ্লেন মোড অন করে অফ করলে টাওয়ারের সাথে মোডেম পুনরায় রিফ্রেশ হয়।",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // List of guides
        items(networkGuides, key = { it.id }) { guide ->
            val isExpanded = expandedGuideId == guide.id

            NetworkGuideExpandableCard(
                guide = guide,
                isExpanded = isExpanded,
                language = language,
                checkedSteps = checkedSteps,
                onToggleExpand = {
                    expandedGuideId = if (isExpanded) null else guide.id
                },
                onToggleStep = { stepKey ->
                    checkedSteps[stepKey] = !(checkedSteps[stepKey] ?: false)
                }
            )
        }

        item {
            // Shortcut to Android Settings
            OutlinedButton(
                onClick = onOpenSettingsShortcut,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("open_cellular_settings_button"),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (language == AppLanguage.EN) "Open Mobile Network Settings" else "মোবাইল নেটওয়ার্ক সেটিংস খুলুন",
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            AdBannerView()
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun NetworkGuideExpandableCard(
    guide: TroubleshootingGuide,
    isExpanded: Boolean,
    language: AppLanguage,
    checkedSteps: Map<String, Boolean>,
    onToggleExpand: () -> Unit,
    onToggleStep: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("network_guide_card_${guide.id}")
            .clickable { onToggleExpand() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isExpanded) 3.dp else 1.dp)
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
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(NavyPrimary.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.NetworkCheck,
                        contentDescription = null,
                        tint = NavyPrimary,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (language == AppLanguage.EN) guide.titleEn else guide.titleBn,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 15.sp),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = if (language == AppLanguage.EN) guide.shortDescEn else guide.shortDescBn,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (isExpanded) "Collapse" else "Expand",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
                    val steps = if (language == AppLanguage.EN) guide.stepsEn else guide.stepsBn

                    Text(
                        text = if (language == AppLanguage.EN) "Recommended Diagnostic Steps:" else "প্রয়োজনীয় সমাধান ধাপসমূহ:",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    steps.forEachIndexed { index, stepText ->
                        val stepKey = "${guide.id}_step_$index"
                        val isDone = checkedSteps[stepKey] == true

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isDone) StatusGreen.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                                .clickable { onToggleStep(stepKey) }
                                .padding(horizontal = 8.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Checkbox(
                                checked = isDone,
                                onCheckedChange = { onToggleStep(stepKey) },
                                modifier = Modifier
                                    .size(28.dp)
                                    .testTag("network_step_${guide.id}_$index"),
                                colors = CheckboxDefaults.colors(
                                    checkedColor = StatusGreen,
                                    checkmarkColor = MaterialTheme.colorScheme.surface
                                )
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "${index + 1}. $stepText",
                                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
                                color = if (isDone) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f) else MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }

                    val techNote = if (language == AppLanguage.EN) guide.technicianNoteEn else guide.technicianNoteBn
                    if (techNote != null) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(NavyPrimary.copy(alpha = 0.1f))
                                .padding(10.dp)
                        ) {
                            Text(
                                text = techNote,
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold, fontSize = 12.sp),
                                color = NavyPrimary
                            )
                        }
                    }
                }
            }
        }
    }
}
