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
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Hardware
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SimCard
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ads.AdBannerView
import com.example.data.AppLanguage
import com.example.data.ChecklistItem
import com.example.data.TroubleshootingRepository
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.SkyBluePrimary
import com.example.ui.theme.StatusAmber
import com.example.ui.theme.StatusGreen
import com.example.ui.theme.StatusRed

@Composable
fun RepairShopScreen(
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = if (language == AppLanguage.EN) {
        listOf("Technician Checklist", "Fault Triaging", "Bangla Customer Scripts")
    } else {
        listOf("চেকলিস্ট", "ত্রুটি মূল্যায়ন", "কাস্টমার স্ক্রিপ্ট")
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("repair_shop_screen")
    ) {
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier.fillMaxWidth()
        ) {
            tabs.forEachIndexed { index, tabTitle ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            text = tabTitle,
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 13.sp,
                            maxLines = 1
                        )
                    },
                    modifier = Modifier.testTag("repair_tab_$index")
                )
            }
        }

        when (selectedTab) {
            0 -> TechnicianChecklistTab(language = language)
            1 -> FaultTriagingTab(language = language)
            2 -> CustomerScriptsTab(language = language)
        }
    }
}

@Composable
private fun TechnicianChecklistTab(language: AppLanguage) {
    val checklist = remember { TroubleshootingRepository.repairShopChecklist }
    val checkedState = remember { mutableStateMapOf<String, Boolean>() }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(10.dp))

            // Professional Disclaimer
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("tech_disclaimer_card"),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = NavyPrimary.copy(alpha = 0.1f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Build,
                        contentDescription = null,
                        tint = NavyPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = if (language == AppLanguage.EN)
                            "Professional Rule: Never declare an unverified diagnosis as a confirmed hardware fault. Complete the 6 non-destructive checks below before opening any phone."
                        else
                            "পেশাদার নীতিমালা: নিশ্চিত প্রমাণ ছাড়া কখনো অনুমান করে গ্রাহককে মাদারবোর্ড/আইসি নষ্ট বলবেন না। ফোন খোলার পূর্বে নিচের ৬টি অহিংস ধাপ সম্পন্ন করুন।"
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            val completedCount = checkedState.values.count { it }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (language == AppLanguage.EN) "Intake Diagnostic Checklist" else "সার্ভিসিং ইনটেক চেকলিস্ট",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "$completedCount / ${checklist.size} " + (if (language == AppLanguage.EN) "Checked" else "সম্পন্ন"),
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = if (completedCount == checklist.size) StatusGreen else MaterialTheme.colorScheme.primary
                )
            }
        }

        items(checklist, key = { it.id }) { item ->
            val isChecked = checkedState[item.id] == true

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("checklist_card_${item.id}")
                    .clickable { checkedState[item.id] = !isChecked },
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isChecked) StatusGreen.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Checkbox(
                        checked = isChecked,
                        onCheckedChange = { checkedState[item.id] = it },
                        modifier = Modifier.testTag("checkbox_${item.id}"),
                        colors = CheckboxDefaults.colors(
                            checkedColor = StatusGreen,
                            checkmarkColor = MaterialTheme.colorScheme.surface
                        )
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = if (language == AppLanguage.EN) item.titleEn else item.titleBn,
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, fontSize = 14.sp),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            if (item.isHardwareCheck) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(StatusAmber.copy(alpha = 0.2f))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "HW",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, fontSize = 9.sp),
                                        color = StatusAmber
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = if (language == AppLanguage.EN) item.descriptionEn else item.descriptionBn,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(10.dp))
            AdBannerView()
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun FaultTriagingTab(language: AppLanguage) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = if (language == AppLanguage.EN)
                    "Determining Fault Root Cause"
                else
                    "সমস্যার মূল কারণ নির্ধারণ নির্দেশিকা",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = if (language == AppLanguage.EN)
                    "Follow this triaging tree to decide whether the phone requires a SIM Swap, Carrier Helpline, Software Config, or Motherboard Hardware Inspection."
                else
                    "সিম রিপ্লেসমেন্ট, অপারেটর সাপোর্ট, সফটওয়্যার রিসেট নাকি মাদারবোর্ড মেরামত কোনটি দরকার তা নির্ধারণ করতে নিচের ধাপগুলো দেখুন।",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        item {
            TriageCategoryCard(
                categoryTitle = if (language == AppLanguage.EN) "1. SIM Replacement (SIM Swap)" else "১. কাস্টমার কেয়ার থেকে সিম বদল (SIM Swap)",
                symptoms = if (language == AppLanguage.EN) listOf(
                    "SIM fails in this device AND in a verified technician handset",
                    "Visible chip scratches, broken gold lines, or water corrosion on SIM",
                    "Device shows 'Card I/O Error' or 'SIM Perm Disabled'"
                ) else listOf(
                    "গ্রাহকের সিম এই ফোনেও কাজ করে না এবং টেকনিশিয়ানের সচল ফোনেও 'No SIM' দেখায়",
                    "সিমের সোনালী পাতে গভীর আঁচড়, ক্ষয় বা পানিতে চিপ নষ্ট",
                    "স্ক্রিনে 'Card I/O Error' বা স্থায়ী নিষ্ক্রিয় বার্তা"
                ),
                recommendation = if (language == AppLanguage.EN)
                    "Send customer to operator customer care with their NID. Do not open or charge for phone repair."
                else
                    "গ্রাহককে জাতীয় পরিচয়পত্র নিয়ে কাস্টমার কেয়ারে পাঠান। ফোনে কোনো কাজ করার বা চার্জ নেওয়ার প্রয়োজন নেই।",
                icon = Icons.Default.SimCard,
                iconColor = SkyBluePrimary
            )
        }

        item {
            TriageCategoryCard(
                categoryTitle = if (language == AppLanguage.EN) "2. Carrier / Account Barring" else "২. অপারেটর ব্যারিং বা বিলিং সমস্যা",
                symptoms = if (language == AppLanguage.EN) listOf(
                    "Signal bars are full, but outgoing calls fail with 'Call barred' or fast busy tone",
                    "Device says 'Emergency Calls Only' while other SIMs from same carrier work fine",
                    "PUK code screen is displayed"
                ) else listOf(
                    "সিগন্যাল ফুল কিন্তু কল দিলে সাথে সাথে কেটে যায় বা ব্যারিং মেসেজ দেয়",
                    "অন্য সিম কাজ করলেও এই নির্দিষ্ট সিমে 'Emergency Calls Only' দেখায়",
                    "PUK কোড চাচ্ছে কিন্তু গ্রাহকের কাছে প্যাকেটের কোড নেই"
                ),
                recommendation = if (language == AppLanguage.EN)
                    "Biometric re-verification or unpaid bill issue. Guide customer to call 121 (GP/Robi/BL/Teletalk)."
                else
                    "বায়োমেট্রিক পুনঃযাচাই বা বিল বকেয়ার সমস্যা। ১২১ নম্বরে কল দিয়ে কাস্টমার কেয়ার প্রতিনিধির সাথে কথা বলতে বলুন।",
                icon = Icons.Default.SupportAgent,
                iconColor = StatusAmber
            )
        }

        item {
            TriageCategoryCard(
                categoryTitle = if (language == AppLanguage.EN) "3. Software / APN / Mode Issue" else "৩. সফটওয়্যার, এপিএন বা মোড সেটিং",
                symptoms = if (language == AppLanguage.EN) listOf(
                    "Calls work normally but 4G Mobile Data fails to connect",
                    "Stuck in 2G (EDGE) because Preferred Network is set incorrectly",
                    "SIM is toggled OFF in device Settings -> SIM Manager"
                ) else listOf(
                    "কল ঠিকঠাক চলে কিন্তু মোবাইল ইন্টারনেট বা ৪জি চালু হচ্ছে না",
                    "ভুলবশত ২জি মোড সিলেক্ট থাকায় দ্রুত গতির নেটওয়ার্ক আসছে না",
                    "ফোন সেটিংসে সিম বন্ধ (Disabled) করা আছে"
                ),
                recommendation = if (language == AppLanguage.EN)
                    "Reset APN to defaults, set Network Mode to 4G/3G Auto, restart device."
                else
                    "এপিএন ডিফল্ট রিসেট দিন, প্রেফার্ড নেটওয়ার্ক মোড ৪জি অটো করুন এবং ফোন রিস্টার্ট দিন।",
                icon = Icons.Default.Build,
                iconColor = StatusGreen
            )
        }

        item {
            TriageCategoryCard(
                categoryTitle = if (language == AppLanguage.EN) "4. Hardware Inspection Required" else "৪. হার্ডওয়্যার মেরামত প্রয়োজন",
                symptoms = if (language == AppLanguage.EN) listOf(
                    "Dialing *#06# shows Null / Null IMEI or Unknown Baseband in Settings",
                    "Known good SIMs are never detected in slot",
                    "2G works near tower but 4G is permanently 'No Service' (RF PA failure)"
                ) else listOf(
                    "*#06# ডায়াল করলে IMEI নাল (Null) বা খালি আসে, অথবা বেসব্যান্ড আননোন",
                    "দোকানের সচল কোনো সিমই এই ফোনের সিম স্লটে পায় না",
                    "টাওয়ারের কাছে ২জি চলে কিন্তু ৪জিতে গেলে সাথে সাথে নো সার্ভিস (RF PA ফল্ট)"
                ),
                recommendation = if (language == AppLanguage.EN)
                    "Hardware inspection justified: Inspect SIM tray pins, RF coaxial wire, and RF transceiver IC."
                else
                    "হার্ডওয়্যার মেরামতের উপযোগী: সিম সকেট পিন, কোঅ্যাক্সিয়াল অ্যান্টেনা ক্যাবল ও আরএফ আইসি পরীক্ষা করুন।",
                icon = Icons.Default.Hardware,
                iconColor = StatusRed
            )
        }

        item {
            Spacer(modifier = Modifier.height(10.dp))
            AdBannerView()
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun TriageCategoryCard(
    categoryTitle: String,
    symptoms: List<String>,
    recommendation: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: androidx.compose.ui.graphics.Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(iconColor.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(20.dp))
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = categoryTitle,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 15.sp),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "লক্ষণসমূহ (Symptoms):",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(4.dp))

            symptoms.forEach { symptom ->
                Row(modifier = Modifier.padding(vertical = 2.dp)) {
                    Text(text = "• ", fontWeight = FontWeight.Bold, color = iconColor)
                    Text(text = symptom, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(iconColor.copy(alpha = 0.08f))
                    .padding(10.dp)
            ) {
                Text(
                    text = "সিদ্ধান্ত: $recommendation",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold, fontSize = 12.sp),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
private fun CustomerScriptsTab(language: AppLanguage) {
    val clipboardManager = LocalClipboardManager.current
    val scripts = remember { TroubleshootingRepository.bdCustomerScripts }
    var copiedIndex by remember { mutableStateOf<Int?>(null) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = if (language == AppLanguage.EN)
                    "Customer-Friendly Explanations in Bangla"
                else
                    "গ্রাহককে সহজ বাংলায় বোঝানোর স্ক্রিপ্ট",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = if (language == AppLanguage.EN)
                    "Technicians can use or adapt these polite, professional Bangla explanations to build trust with customers."
                else
                    "গ্রাহকের সাথে ভুল বোঝাবুঝি এড়াতে এবং বিশ্বাসযোগ্যতা তৈরি করতে নিচের সাবলীল ব্যাখ্যাগুলো ব্যবহার করুন।",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        items(scripts.indices.toList()) { index ->
            val (scriptTitle, scriptBody) = scripts[index]

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = scriptTitle,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, fontSize = 14.sp),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.weight(1f)
                        )

                        IconButton(
                            onClick = {
                                clipboardManager.setText(AnnotatedString(scriptBody))
                                copiedIndex = index
                            },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = if (copiedIndex == index) Icons.Default.Check else Icons.Default.ContentCopy,
                                contentDescription = "Copy Script",
                                tint = if (copiedIndex == index) StatusGreen else MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = scriptBody,
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp, lineHeight = 20.sp),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(10.dp))
            AdBannerView()
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
