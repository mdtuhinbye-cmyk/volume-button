package com.example.ui.screens

import android.app.Activity
import androidx.compose.animation.AnimatedContent
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ads.AdBannerView
import com.example.ads.InterstitialAdManager
import com.example.data.AppLanguage
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.SkyBluePrimary
import com.example.ui.theme.StatusGreen

@Composable
fun RepairGuideScreen(
    language: AppLanguage,
    interstitialAdManager: InterstitialAdManager,
    onOpenSettingsShortcut: () -> Unit,
    modifier: Modifier = Modifier
) {
    val activity = LocalContext.current as? Activity
    var currentStep by remember { mutableIntStateOf(1) }
    val totalSteps = 4

    var selectedSymptom by remember { mutableStateOf<String?>(null) }
    var physicalCheckDone by remember { mutableStateOf(false) }
    var airplaneResetDone by remember { mutableStateOf(false) }
    var apnCheckDone by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("repair_guide_wizard_screen")
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))

            // Step Progress Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (language == AppLanguage.EN) "Guided Troubleshooting Wizard" else "ধাপে ধাপে ডায়াগনস্টিক উইজার্ড",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "Step $currentStep of $totalSteps",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    LinearProgressIndicator(
                        progress = { currentStep.toFloat() / totalSteps.toFloat() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                }
            }
        }

        item {
            AnimatedContent(targetState = currentStep, label = "wizard_step") { step ->
                when (step) {
                    1 -> Step1SymptomSelection(
                        language = language,
                        selected = selectedSymptom,
                        onSelect = { selectedSymptom = it }
                    )
                    2 -> Step2PhysicalChecks(
                        language = language,
                        airplaneDone = airplaneResetDone,
                        physicalDone = physicalCheckDone,
                        onToggleAirplane = { airplaneResetDone = !airplaneResetDone },
                        onTogglePhysical = { physicalCheckDone = !physicalCheckDone }
                    )
                    3 -> Step3SettingsApn(
                        language = language,
                        apnDone = apnCheckDone,
                        onToggleApn = { apnCheckDone = !apnCheckDone },
                        onOpenSettings = onOpenSettingsShortcut
                    )
                    4 -> Step4Result(
                        language = language,
                        symptom = selectedSymptom,
                        onRestart = {
                            currentStep = 1
                            selectedSymptom = null
                            physicalCheckDone = false
                            airplaneResetDone = false
                            apnCheckDone = false
                        }
                    )
                }
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (currentStep > 1) {
                    OutlinedButton(
                        onClick = { currentStep -= 1 },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("wizard_prev_btn"),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(if (language == AppLanguage.EN) "Previous" else "পূর্ববর্তী")
                    }
                }

                if (currentStep < totalSteps) {
                    Button(
                        onClick = {
                            if (currentStep == 1 && selectedSymptom == null) {
                                // Must select symptom
                            } else {
                                currentStep += 1
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("wizard_next_btn"),
                        shape = RoundedCornerShape(12.dp),
                        enabled = currentStep != 1 || selectedSymptom != null
                    ) {
                        Text(if (language == AppLanguage.EN) "Next Step" else "পরবর্তী ধাপ")
                    }
                } else {
                    // Final Step: Complete diagnosis & safe interstitial ad
                    Button(
                        onClick = {
                            if (activity != null) {
                                interstitialAdManager.showAd(activity) {
                                    currentStep = 1
                                }
                            } else {
                                currentStep = 1
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("wizard_finish_btn"),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(if (language == AppLanguage.EN) "Finish Diagnostic" else "ডায়াগনস্টিক সম্পন্ন")
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            AdBannerView()
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun Step1SymptomSelection(
    language: AppLanguage,
    selected: String?,
    onSelect: (String) -> Unit
) {
    val symptoms = listOf(
        "sim_absent" to (if (language == AppLanguage.EN) "SIM card is not detected at all" else "সিম কার্ড একেবারেই ডিটেক্ট হচ্ছে না"),
        "no_signal" to (if (language == AppLanguage.EN) "Shows 'No Service' or 'Emergency Calls Only'" else "স্ট্যাটাস বারে 'No Service' বা 'ইমার্জেন্সি কল' দেখাচ্ছে"),
        "data_down" to (if (language == AppLanguage.EN) "Mobile Data / 4G not working despite data pack" else "ডাটা প্যাক থাকা সত্ত্বেও মোবাইল ইন্টারনেট বা ৪জি কাজ করছে না"),
        "call_drops" to (if (language == AppLanguage.EN) "Calls dropping constantly or SMS not sending" else "কল কেটে যাচ্ছে বা এসএমএস পাঠানো যাচ্ছে না")
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = if (language == AppLanguage.EN) "Step 1: What problem are you experiencing?" else "ধাপ ১: আপনি ঠিক কী সমস্যার মুখোমুখি হচ্ছেন?",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, fontSize = 15.sp),
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(10.dp))

            symptoms.forEach { (key, label) ->
                val isSelected = selected == key
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isSelected) SkyBluePrimary.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                        .clickable { onSelect(key) }
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (isSelected) Icons.Default.RadioButtonChecked else Icons.Default.RadioButtonUnchecked,
                        contentDescription = null,
                        tint = if (isSelected) SkyBluePrimary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = label,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Composable
private fun Step2PhysicalChecks(
    language: AppLanguage,
    airplaneDone: Boolean,
    physicalDone: Boolean,
    onToggleAirplane: () -> Unit,
    onTogglePhysical: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = if (language == AppLanguage.EN) "Step 2: Basic Physical & Radio Checks" else "ধাপ ২: রেডিও ও প্রাথমিক মেকানিক্যাল পরীক্ষা",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, fontSize = 15.sp),
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(12.dp))

            WizardCheckRow(
                checked = airplaneDone,
                onToggle = onToggleAirplane,
                title = if (language == AppLanguage.EN) "Airplane Mode Reset" else "এয়ারপ্লেন মোড রিসেট",
                desc = if (language == AppLanguage.EN)
                    "Turn Airplane Mode ON for 15 seconds, then turn OFF to force baseband tower re-registration."
                else
                    "১৫ সেকেন্ডের জন্য এয়ারপ্লেন মোড অন করে আবার অফ করুন যাতে মডেম টাওয়ারের সাথে রিফ্রেশ হয়।"
            )

            Spacer(modifier = Modifier.height(10.dp))

            WizardCheckRow(
                checked = physicalDone,
                onToggle = onTogglePhysical,
                title = if (language == AppLanguage.EN) "Clean SIM Gold Contacts" else "সিমের সোনালী চিপ পরিষ্কার",
                desc = if (language == AppLanguage.EN)
                    "Eject SIM tray with device powered off. Clean SIM contacts gently with soft dry microfiber or eraser."
                else
                    "ফোন বন্ধ করে সিম ট্রে বের করুন এবং শুকনো নরম কাপড় দিয়ে সোনালী চিপ আলতো করে মুছুন।"
            )
        }
    }
}

@Composable
private fun Step3SettingsApn(
    language: AppLanguage,
    apnDone: Boolean,
    onToggleApn: () -> Unit,
    onOpenSettings: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = if (language == AppLanguage.EN) "Step 3: Network Mode & APN Configuration" else "ধাপ ৩: নেটওয়ার্ক মোড ও এপিএন কনফিগারেশন",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, fontSize = 15.sp),
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(12.dp))

            WizardCheckRow(
                checked = apnDone,
                onToggle = onToggleApn,
                title = if (language == AppLanguage.EN) "Reset APN to Default" else "এপিএন সেটিংস রিসেট",
                desc = if (language == AppLanguage.EN)
                    "In Mobile Network Settings -> Access Point Names, tap menu -> Reset to default."
                else
                    "মোবাইল নেটওয়ার্কের Access Point Names-এ গিয়ে ৩-ডট মেন্যু থেকে Reset to default দিন।"
            )

            Spacer(modifier = Modifier.height(14.dp))

            OutlinedButton(
                onClick = onOpenSettings,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(imageVector = Icons.Default.Settings, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(if (language == AppLanguage.EN) "Open Network Settings" else "নেটওয়ার্ক সেটিংস ওপেন করুন")
            }
        }
    }
}

@Composable
private fun Step4Result(
    language: AppLanguage,
    symptom: String?,
    onRestart: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = StatusGreen,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (language == AppLanguage.EN) "Step 4: Final Triage Outcome" else "ধাপ ৪: চূড়ান্ত সমাধান মূল্যায়ন",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, fontSize = 15.sp),
                    color = StatusGreen
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            val recommendation = when (symptom) {
                "sim_absent" -> if (language == AppLanguage.EN)
                    "If the SIM still does not appear after cleaning and cross-testing in another handset, the metallic chip is likely damaged or degraded. Visit your operator customer center with your NID for a quick SIM Swap (replacement). Do not attempt software tampering."
                else
                    "সিম পরিষ্কার ও অন্য ফোনে টেস্ট করার পরও যদি নো সিম দেখায়, তবে সিম চিপ ক্ষতিগ্রস্ত হয়েছে। আপনার জাতীয় পরিচয়পত্র নিয়ে কাস্টমার কেয়ার থেকে নতুন সিম তুলে নিন (সিম সোয়াপ)। এতে একই নম্বর চালু থাকবে।"

                "no_signal" -> if (language == AppLanguage.EN)
                    "Dial *#06# on phone dialer. If 15-digit IMEI numbers appear, contact carrier helpline (121) to confirm account/barring status. If IMEI shows Null or Unknown, visit a certified hardware technician for RF antenna/baseband diagnosis."
                else
                    "ডায়ালপ্যাডে *#06# চাপুন। সঠিক IMEI আসলে অপারেটরের হেল্পলাইনে (১২১) যোগাযোগ করে সিম সক্রিয় আছে কিনা নিশ্চিত হোন। আর IMEI নাল আসলে অনুমোদিত টেকনিশিয়ানকে দিয়ে অ্যান্টেনা বা মাদারবোর্ড চেক করান।"

                else -> if (language == AppLanguage.EN)
                    "Check data pack validity with your operator balance code. Ensure Preferred Network Type is set to 4G/3G Auto and VoLTE is enabled."
                else
                    "অপারেটরের ব্যালেন্স কোড ডায়াল করে ডাটার মেয়াদ চেক করুন। প্রেফার্ড নেটওয়ার্ক মোড ৪জি অটো এবং VoLTE চালু রাখুন।"
            }

            Text(
                text = recommendation,
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp, lineHeight = 20.sp),
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(14.dp))

            OutlinedButton(
                onClick = onRestart,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(imageVector = Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(if (language == AppLanguage.EN) "Start New Diagnostic" else "নতুন করে ডায়াগনস্টিক শুরু করুন")
            }
        }
    }
}

@Composable
private fun WizardCheckRow(
    checked: Boolean,
    onToggle: () -> Unit,
    title: String,
    desc: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(if (checked) StatusGreen.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .clickable { onToggle() }
            .padding(10.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = if (checked) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
            contentDescription = null,
            tint = if (checked) StatusGreen else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, fontSize = 13.sp),
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = desc,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
