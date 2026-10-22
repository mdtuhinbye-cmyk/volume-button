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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SimCard
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
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
import com.example.ui.theme.StatusRed

@Composable
fun SimTroubleshootScreen(
    language: AppLanguage,
    onOpenSettingsShortcut: () -> Unit,
    modifier: Modifier = Modifier
) {
    val simGuides = remember {
        TroubleshootingRepository.guides.filter { it.category == GuideCategory.SIM }
    }

    // Track expanded cards
    var expandedGuideId by remember { mutableStateOf<String?>(simGuides.firstOrNull()?.id) }
    // Track completed check steps
    val checkedSteps = remember { mutableStateMapOf<String, Boolean>() }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("sim_troubleshoot_screen")
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))

            // Safety Warning Header
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("sim_safety_notice_card"),
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
                        contentDescription = "Safety Notice",
                        tint = StatusAmber,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = if (language == AppLanguage.EN)
                            "Honest Notice: Software cannot physically repair cracked SIM chips or broken gold pins. Follow safe physical and configuration checks below."
                        else
                            "সতর্কবার্তা: সফটওয়্যার দিয়ে ভাঙা বা ত্রুটিপূর্ণ সিম চিপ ঠিক করা যায় না। নিচে উল্লেখিত নিরাপদ মেকানিক্যাল ও কনফিগারেশন ধাপগুলো অনুসরণ করুন।",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Quick button to open Android SIM settings
            OutlinedButton(
                onClick = onOpenSettingsShortcut,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("open_sim_settings_button"),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (language == AppLanguage.EN) "Open Device SIM Management Settings" else "ডিভাইস সিম ম্যানেজমেন্ট সেটিংস খুলুন",
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        items(simGuides, key = { it.id }) { guide ->
            val isExpanded = expandedGuideId == guide.id

            SimGuideExpandableCard(
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
            Spacer(modifier = Modifier.height(8.dp))
            AdBannerView()
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun SimGuideExpandableCard(
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
            .testTag("guide_card_${guide.id}")
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
                        .background(SkyBluePrimary.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.SimCard,
                        contentDescription = null,
                        tint = SkyBluePrimary,
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
                        text = if (language == AppLanguage.EN) "Step-by-Step Diagnostic Checks:" else "ধাপে ধাপে ডায়াগনস্টিক ধাপসমূহ:",
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
                                    .testTag("step_checkbox_${guide.id}_$index"),
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

                    // Technician Note if present
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
