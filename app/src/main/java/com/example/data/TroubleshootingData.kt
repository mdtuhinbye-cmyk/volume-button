package com.example.data

data class TroubleshootingGuide(
    val id: String,
    val category: GuideCategory,
    val titleEn: String,
    val titleBn: String,
    val shortDescEn: String,
    val shortDescBn: String,
    val warningEn: String? = null,
    val warningBn: String? = null,
    val stepsEn: List<String>,
    val stepsBn: List<String>,
    val technicianNoteEn: String? = null,
    val technicianNoteBn: String? = null
)

enum class GuideCategory {
    SIM,
    NETWORK,
    MOBILE_DATA,
    CALLS_SMS,
    REPAIR_SHOP
}

data class FaqItem(
    val questionEn: String,
    val questionBn: String,
    val answerEn: String,
    val answerBn: String,
    val category: String
)

data class ChecklistItem(
    val id: String,
    val titleEn: String,
    val titleBn: String,
    val descriptionEn: String,
    val descriptionBn: String,
    val isHardwareCheck: Boolean = false
)

object TroubleshootingRepository {

    val guides: List<TroubleshootingGuide> = listOf(
        TroubleshootingGuide(
            id = "sim_not_detected",
            category = GuideCategory.SIM,
            titleEn = "SIM Card Not Detected / No SIM",
            titleBn = "সিম কার্ড ডিটেক্ট হচ্ছে না / নো সিম",
            shortDescEn = "Phone displays 'No SIM Card inserted' even when a SIM is present.",
            shortDescBn = "সিম কার্ড লাগানো থাকা সত্ত্বেও 'No SIM' বার্তা প্রদর্শিত হচ্ছে।",
            warningEn = "Safe Warning: An app cannot physically fix worn-out metallic SIM contacts or a cracked SIM chip.",
            warningBn = "সতর্কতা: কোনো অ্যাপ নষ্ট সিম চিপ বা ক্ষয়ে যাওয়া ধাতব পিন ঠিক করতে পারে না।",
            stepsEn = listOf(
                "Power off your device completely before ejecting the SIM tray.",
                "Eject the SIM tray using a standard ejection pin. Avoid using sewing needles that can bend the internal connector pins.",
                "Inspect the SIM's gold contacts for dust, oxidation, or grease. Gently clean with a soft dry microfiber cloth or soft pencil eraser.",
                "Verify the SIM is properly aligned in the tray (matching the diagonal notch).",
                "Reinsert tray firmly and power on the phone.",
                "Cross-Check: Insert another working SIM into this phone, or test this SIM in another device to identify if the issue is with the SIM or the phone slot.",
                "If the SIM fails in all devices, request a SIM Replacement (SIM Swap) from your operator's customer care."
            ),
            stepsBn = listOf(
                "সিম ট্রে খোলার আগে ফোনটি সম্পূর্ণভাবে বন্ধ (Power Off) করুন।",
                "স্ট্যান্ডার্ড সিম ইজেকশন পিন দিয়ে সাবধানে ট্রে বের করুন। সুই বা সেফটিপিন ব্যবহার করবেন না, এতে অভ্যন্তরীণ কানেক্টর পিন ভেঙে যেতে পারে।",
                "সিমের সোনালী চিপে কোনো ধুলাবালি বা ময়লা থাকলে নরম শুকনো কাপড় বা ইরেজার দিয়ে আলতো করে পরিষ্কার করুন।",
                "সিম ট্রেতে সিমটি সঠিক কোণে (কাটা খাঁজ মিলিয়ে) বসানো হয়েছে কিনা নিশ্চিত করুন।",
                "ট্রেটি সাবধানে পুনরায় প্রবেশ করান এবং ফোন চালু করুন।",
                "ক্রস-চেক: এই ফোনে অন্য একটি সচল সিম ঢুকিয়ে দেখুন, অথবা এই সিমটি অন্য ফোনে লাগিয়ে দেখুন সমস্যাটি সিমের নাকি ফোনের সিম স্লটের।",
                "যদি সিমটি অন্য ফোনেও কাজ না করে, তবে কাস্টমার কেয়ারে গিয়ে সিম রিপ্লেসমেন্ট (সিম সোয়াপ) করুন।"
            ),
            technicianNoteEn = "Shop Tip: Measure SIM socket pin continuity using a multimeter on diode mode. Check VCC (1.8V/3.0V), RST, CLK, and GND lines.",
            technicianNoteBn = "টেকনিশিয়ান টিপস: মাল্টিমিটার ডায়োড মোডে রেখে সিম সকেটের পিনগুলোর (VCC, CLK, RST, GND) রিডিং চেক করুন।"
        ),
        TroubleshootingGuide(
            id = "sim_unavailable",
            category = GuideCategory.SIM,
            titleEn = "SIM Showing Unavailable or Locked",
            titleBn = "সিম অনুপলব্ধ বা লক দেখাচ্ছে",
            shortDescEn = "SIM recognized by hardware but service displays 'Unavailable' or 'SIM locked'.",
            shortDescBn = "সিম পাওয়া গেলেও 'Unavailable' বা 'SIM Locked' দেখাচ্ছে।",
            stepsEn = listOf(
                "Check if PIN/PUK lock is requested. If PUK code is prompted, do NOT guess random numbers (10 wrong attempts permanently disables the SIM).",
                "Look up the original SIM card pack for the PUK code, or call operator customer service from another phone.",
                "Go to Settings -> Network & Internet -> SIMs and check if the SIM is toggled ON.",
                "Verify your SIM registration status with biometric NID records if required by local regulations.",
                "Check for carrier network bans or unpaid postpaid bills."
            ),
            stepsBn = listOf(
                "ফোনে পিন (PIN) বা পিইউকে (PUK) কোড চাচ্ছে কিনা দেখুন। আন্দাজে ভুল PUK কোড দিবেন না (১০ বার ভুল দিলে সিম স্থায়ীভাবে নষ্ট হবে)।",
                "আসল সিম প্যাকেটের পেছনে থাকা PUK কোড ব্যবহার করুন অথবা অন্য ফোন থেকে অপারেটরের হেল্পলাইনে কল করুন।",
                "ফোন সেটিংস -> Network & Internet -> SIMs-এ গিয়ে সিমটি চালু (Toggle ON) আছে কিনা নিশ্চিত করুন।",
                "বায়োমেট্রিক সিম নিবন্ধনে কোনো ত্রুটি বা জাতীয় পরিচয়পত্র জনিত সমস্যা আছে কিনা অপারেটর থেকে যাচাই করুন।",
                "পোস্টপেইড বা বকেয়া বিলের কারণে সংযোগ স্থগিত করা হয়েছে কিনা পরীক্ষা করুন।"
            )
        ),
        TroubleshootingGuide(
            id = "dual_sim_issues",
            category = GuideCategory.SIM,
            titleEn = "Dual-SIM Configuration & Detection",
            titleBn = "ডুয়াল সিম কনফিগারেশন সমস্যা",
            shortDescEn = "Only SIM 1 or SIM 2 is active, or mobile data toggles incorrectly.",
            shortDescBn = "শুধুমাত্র একটি সিম কাজ করছে অথবা ডাটা ব্যবহারের সিম বদলানো যাচ্ছে না।",
            stepsEn = listOf(
                "Open Settings -> SIM Card Manager (or Mobile Network -> SIMs).",
                "Ensure both SIM 1 and SIM 2 slots are toggled to ENABLED.",
                "Set preferred SIM for Calls, SMS, and Mobile Data explicitly rather than leaving it on 'Always Ask' if experiencing conflicts.",
                "Note: Many modern phones support Dual-SIM Dual Standby (DSDS) where only one SIM can use 4G/5G data at a time.",
                "Swap physical slots (put SIM 1 in Slot 2 and vice versa) to isolate whether one slot has a mechanical contact issue."
            ),
            stepsBn = listOf(
                "ফোন সেটিংস থেকে SIM Card Manager বা Mobile Network -> SIMs অপশনে প্রবেশ করুন।",
                "সিম ১ এবং সিম ২ উভয় স্লটই অন (Enabled) করা আছে কিনা নিশ্চিত করুন।",
                "কল, এসএমএস এবং মোবাইল ডাটার জন্য নির্দিষ্ট সিম নির্বাচন করুন।",
                "মনে রাখবেন: অধিকাংশ ফোনে Dual-SIM Dual Standby (DSDS) প্রযুক্তির কারণে এক সাথে কেবল একটি সিমেই উচ্চগতির ডাটা কাজ করে।",
                "স্লট অদলবদল করে দেখুন (সিম ১ কে সিম ২ এ দিয়ে দেখুন) কোনো নির্দিষ্ট স্লটের মেকানিক্যাল সংযোগে সমস্যা আছে কিনা।"
            )
        ),
        TroubleshootingGuide(
            id = "no_service_emergency",
            category = GuideCategory.NETWORK,
            titleEn = "No Service / Emergency Calls Only",
            titleBn = "নো সার্ভিস / শুধু জরুরি কল (Emergency Calls Only)",
            shortDescEn = "Signal bars show an 'X' or 'Emergency Calls Only' appears on the status bar.",
            shortDescBn = "সিগন্যালে ক্রস চিহ্ন বা স্ট্যাটাস বারে 'Emergency Calls Only' দেখাচ্ছে।",
            warningEn = "Software tools cannot bypass carrier barring or boost weak physical radio waves from remote towers.",
            warningBn = "সফটওয়্যার দিয়ে কোনো অপারেটরের বিধিনিষেধ এড়ানো বা টাওয়ারের রেডিও তরঙ্গ বৃদ্ধি করা সম্ভব নয়।",
            stepsEn = listOf(
                "Toggle Airplane Mode: Turn ON Airplane Mode, wait 15 seconds, then turn it OFF. This forces the baseband modem to re-register with the nearest cell tower.",
                "Restart the device to clear stuck radio interface processes.",
                "Check Network Mode: Go to Settings -> Mobile Network -> Preferred Network Type. Choose '5G/4G/3G Auto' or try '4G/3G Auto' if 5G coverage is patchy.",
                "Manual Operator Search: Go to Settings -> Mobile Network -> Network Operators. Turn OFF 'Automatically select network' and select your carrier manually from the list.",
                "Dial *#06# on Phone Dialer. Verify that valid 15-digit IMEI numbers appear. If it displays 'Null/Null' or 'Unknown', the baseband firmware or RF hardware has failed."
            ),
            stepsBn = listOf(
                "এয়ারপ্লেন মোড টগল করুন: এয়ারপ্লেন মোড চালু করুন, ১৫ সেকেন্ড অপেক্ষা করে বন্ধ করুন। এতে ফোনের মোডেম নিকটবর্তী টাওয়ারের সাথে পুনরায় যুক্ত হয়।",
                "ফোনটি রিস্টার্ট (Restart) করুন যাতে আটকে থাকা রেডিও প্রসেসগুলো নতুন করে চালু হতে পারে।",
                "নেটওয়ার্ক মোড পরীক্ষা করুন: সেটিংস -> মোবাইল নেটওয়ার্ক -> Preferred Network Type-এ গিয়ে '5G/4G/3G Auto' অথবা '4G/3G Auto' নির্বাচন করুন।",
                "ম্যানুয়াল অপারেটর সার্চ: মোবাইল নেটওয়ার্ক সেটিংস থেকে 'Automatically select network' বন্ধ করে ম্যানুয়ালি আপনার সিমের অপারেটর সিলেক্ট করুন।",
                "ডায়ালপ্যাডে *#06# ডায়াল করুন। ১৫ সংখ্যার বৈধ IMEI দেখা যাচ্ছে কিনা পরীক্ষা করুন। যদি 'Null/Null' দেখায়, তবে ফোনের বেসব্যান্ড বা হার্ডওয়্যার ক্ষতিগ্রস্ত হয়েছে।"
            ),
            technicianNoteEn = "Shop Tip: Check baseband version in Settings -> About. If 'Baseband Unknown', reflash firmware or inspect baseband PMIC and RF transceiver.",
            technicianNoteBn = "টেকনিশিয়ান টিপস: সেটিংস -> About এ গিয়ে Baseband Version চেক করুন। 'Unknown' থাকলে ফার্মওয়্যার ফ্ল্যাশ বা RF Transceiver আইসি চেক করুন।"
        ),
        TroubleshootingGuide(
            id = "weak_signal",
            category = GuideCategory.NETWORK,
            titleEn = "Weak or Fluctuating Signal",
            titleBn = "দুর্বল বা ওঠানামা করা সিগন্যাল",
            shortDescEn = "Signal bars constantly drop to 1 bar or switch between 4G and 2G.",
            shortDescBn = "সিগন্যাল বারবার কমে ১ বারে নেমে যায় অথবা ৪জি ও ২জি এর মধ্যে ওঠানামা করে।",
            stepsEn = listOf(
                "Check your physical surroundings: Basements, thick concrete structures, and metal roofing severely attenuate cellular radio frequencies.",
                "Remove metallic or magnetic phone cases. Heavy armored cases with metal kickstands often block the internal antenna bands.",
                "Switch Preferred Network to 'LTE/4G only' or '3G/2G auto' temporarily if the phone is constantly hunting between weak 4G and 3G.",
                "Avoid holding the top or bottom antenna seam areas tightly if your phone has metal frame antenna cutouts.",
                "Check if friends/family using the same operator in the same room experience the same issue (local tower congestion)."
            ),
            stepsBn = listOf(
                "আপনার অবস্থান পরীক্ষা করুন: বেজমেন্ট, ঘন কংক্রিট বা টিনের চালের নিচে সিগন্যাল উল্লেখযোগ্যভাবে কমে যায়।",
                "মেটালিক বা চুম্বকযুক্ত ব্যাককভার খুলে ফেলুন। ধাতব কেসিং ফোনের অ্যান্টেনার কার্যক্ষমতা কমিয়ে দেয়।",
                "দুর্বল নেটওয়ার্ক এলাকায় ৪জি ও ৩জি এর ওঠানামা কমাতে সাময়িকভাবে প্রেফার্ড নেটওয়ার্ক মোড পরিবর্তন করে দেখুন।",
                "মেটাল ফ্রেমের অ্যান্টেনা ব্যান্ডে হাত চেপে না ধরে ফোনটি ধরুন।",
                "একই কক্ষে একই অপারেটর ব্যবহারকারী অন্য কারও সিগন্যাল চেক করুন টাওয়ার রক্ষণাবেক্ষণ চলছে কিনা জানতে।"
            )
        ),
        TroubleshootingGuide(
            id = "mobile_data_not_working",
            category = GuideCategory.MOBILE_DATA,
            titleEn = "Mobile Data Not Working / No Internet",
            titleBn = "মোবাইল ডাটা কাজ করছে না / ইন্টারনেট নেই",
            shortDescEn = "Mobile data icon shows 'E', '! ' or no data icon appears despite active data plan.",
            shortDescBn = "ডাটা প্যাক থাকা সত্ত্বেও 'E', '!' চিহ্ন দেখাচ্ছে বা কোনো ডাটা আইকন আসছে না।",
            stepsEn = listOf(
                "Confirm mobile data is enabled in quick settings, and verify the correct SIM is designated for data.",
                "Check account balance and data pack validity by dialing your operator code (GP: *121*1*4#, Robi/Airtel: *3#, Banglalink: *5000*500#, Teletalk: *152#).",
                "Check Data Limit: Go to Settings -> Network -> Data Usage and verify 'Data Warning & Limit' has not paused your connection.",
                "Verify APN (Access Point Name): Go to Settings -> Mobile Network -> Access Point Names. For GP: Name='GP', APN='gpinternet'. For Robi: APN='internet'. For Banglalink: APN='blweb'. For Teletalk: APN='teletalk'. Tap 3-dots -> 'Reset to default'.",
                "Turn Airplane mode ON for 10 seconds and turn it OFF."
            ),
            stepsBn = listOf(
                "কুইক সেটিংসে মোবাইল ডাটা অন আছে কিনা এবং সঠিক সিমে ডাটা সিলেক্ট করা আছে কিনা নিশ্চিত করুন।",
                "ডাটা ব্যালেন্স ও মেয়াদ ডায়াল কোডের মাধ্যমে চেক করুন (জিপি: *১২১*১*৪#, রবি/এয়ারটেল: *৩#, বাংলালিংক: *৫০০০*৫০০#, টেলিটক: *১৫২#)।",
                "ডাটা লিমিট পরীক্ষা করুন: সেটিংস -> Data Usage-এ গিয়ে ডেটা লিমিট শেষ হয়ে ডাটা বন্ধ হয়েছে কিনা দেখুন।",
                "এপিএন (APN) যাচাই করুন: সেটিংস -> মোবাইল নেটওয়ার্ক -> Access Point Names-এ গিয়ে ৩-ডটে ক্লিক করে 'Reset to default' দিন। (GP: gpinternet, Robi: internet, BL: blweb, Teletalk: teletalk)।",
                "১০ সেকেন্ডের জন্য এয়ারপ্লেন মোড অন করে আবার অফ করুন।"
            )
        ),
        TroubleshootingGuide(
            id = "connectivity_4g_5g",
            category = GuideCategory.MOBILE_DATA,
            titleEn = "4G / 5G Connectivity Problems",
            titleBn = "৪জি / ৫জি কানেক্টিভিটি সমস্যা",
            shortDescEn = "Device supports 4G/5G but is stuck on 2G (EDGE) or 3G.",
            shortDescBn = "ফোন ৪জি/৫জি সাপোর্টেড হলেও ২জি (EDGE) বা ৩জিতে আটকে থাকছে।",
            stepsEn = listOf(
                "Verify that your SIM card is a 4G-enabled USIM. Older 2G/3G SIM cards cannot connect to 4G LTE/5G bands without upgrading (free SIM swap at customer center).",
                "Open Settings -> Mobile Network -> Preferred Network Type and ensure '5G (recommended)' or 'LTE/4G Auto' is selected.",
                "Check Power Saving Mode: Many devices automatically disable 5G and throttle network bands when 'Battery Saver' is enabled.",
                "Confirm 5G coverage: 5G rollout in Bangladesh is currently limited to specific commercial areas and zones.",
                "Ensure VoLTE (Voice over LTE) is toggled ON in mobile network settings for high-speed simultaneous call and data performance."
            ),
            stepsBn = listOf(
                "আপনার সিমটি ৪জি সাপোর্টেড (USIM) কিনা নিশ্চিত করুন। পুরনো ২জি/৩জি সিম দিয়ে ৪জি বা ৫জি পাওয়া যায় না (কাস্টমার কেয়ার থেকে ৪জি সিম সোয়াপ করুন)।",
                "সেটিংস -> মোবাইল নেটওয়ার্ক -> Preferred Network Type-এ গিয়ে '5G (recommended)' বা 'LTE/4G Auto' নির্বাচন করুন।",
                "ব্যাটারি সেভার মোড পরীক্ষা করুন: অনেক ফোনে 'Battery Saver' চালু থাকলে ৫জি স্বয়ংক্রিয়ভাবে বন্ধ হয়ে যায়।",
                "৫জি কভারেজ এলাকা নিশ্চিত করুন: বাংলাদেশে ৫জি নেটওয়ার্ক বর্তমানে নির্দিষ্ট কিছু এলাকায় পরীক্ষামূলকভাবে চালু রয়েছে।",
                "কল ও ডাটা একসাথে দ্রুত চালাতে মোবাইল নেটওয়ার্ক সেটিংসে VoLTE চালু রাখুন।"
            )
        ),
        TroubleshootingGuide(
            id = "calls_and_sms",
            category = GuideCategory.CALLS_SMS,
            titleEn = "Calls Failing & SMS Not Sending",
            titleBn = "কল ও এসএমএস ব্যর্থ হওয়া",
            shortDescEn = "Calls drop immediately or SMS messages fail with 'Message not sent'.",
            shortDescBn = "কল দিলে সাথে সাথে কেটে যায় অথবা এসএমএস পাঠানো ব্যর্থ হয়।",
            stepsEn = listOf(
                "Check SMS Center (SMSC) Number: In Messages app settings, verify the SMS service center number for your carrier (GP: +8801700000600, Robi: +8801801000004, BL: +8801900000600, Teletalk: +8801550150080).",
                "Check Call Barring and Fixed Dialing Numbers (FDN) in the Phone app settings.",
                "Verify Call Forwarding is not unintentionally rerouting incoming calls to an expired number.",
                "Disable 2G security toggles or temporary blocking features if your carrier does not support VoLTE in your region.",
                "Restart the device to refresh call session handles."
            ),
            stepsBn = listOf(
                "এসএমএস সেন্টার (SMSC) নম্বর চেক করুন: মেসেজ অ্যাপের সেটিংসে অপারেটরের সঠিক সার্ভিস সেন্টার নম্বর দেওয়া আছে কিনা দেখুন (GP: +8801700000600, Robi: +8801801000004, BL: +8801900000600, Teletalk: +8801550150080)।",
                "ফোন ডায়ালার সেটিংসে Call Barring বা FDN (Fixed Dialing) চালু আছে কিনা দেখুন।",
                "কল ফরওয়ার্ডিং চালু হয়ে কোনো বন্ধ নম্বরে কল ডাইভার্ট হচ্ছে কিনা যাচাই করুন (*#61# ডায়াল করে চেক করতে পারেন)।",
                "ক্লিয়ার কলের জন্য VoLTE সেটিংস অন/অফ করে পরীক্ষা করুন।",
                "ফোনটি একবার রিস্টার্ট করুন।"
            )
        )
    )

    val faqs: List<FaqItem> = listOf(
        FaqItem(
            questionEn = "Can any software or app increase my physical signal strength?",
            questionBn = "কোনো অ্যাপ বা সফটওয়্যার কি আসলেই সিগন্যাল বাড়াতে পারে?",
            answerEn = "No. Physical signal strength (RSSI/RSRP) is governed entirely by your distance from the operator's cell tower, physical obstructions, and the phone's internal RF hardware. Apps can only help you refresh network registration, fix incorrect APN/network mode settings, and guide you to better diagnostic steps.",
            answerBn = "না। ফিজিক্যাল সিগন্যাল সম্পূর্ণভাবে আপনার এলাকার টাওয়ারের দূরত্ব, দেওয়াল বা ভবনের বাধা এবং ফোনের রেডিও হার্ডওয়্যারের ওপর নির্ভর করে। কোনো অ্যাপ সিগন্যাল বুস্ট করতে পারে না; তবে আটকে থাকা মোডেম রিফ্রেশ, ভুল এপিএন ও নেটওয়ার্ক সেটিংস ঠিক করে সমস্যার সমাধান করতে পারে।",
            category = "Signal"
        ),
        FaqItem(
            questionEn = "What does 'Emergency Calls Only' mean?",
            questionBn = "'Emergency Calls Only' দেখানোর কারণ কী?",
            answerEn = "It means your phone's antenna detects a cell tower, but your specific SIM card is not allowed to register on that network. This happens when your SIM is unactivated, damaged, barred by the operator, or when your operator has no coverage in that area (other carriers' towers will only route emergency 999 calls).",
            answerBn = "এর অর্থ হলো ফোনটির অ্যান্টেনা কোনো না কোনো টাওয়ারের সিগন্যাল পাচ্ছে, কিন্তু আপনার নির্দিষ্ট সিমটি সেই নেটওয়ার্কে নিবন্ধিত হতে পারছে না। সিমটি নষ্ট থাকলে, বকেয়া বা ভেরিফিকেশন সমস্যায় ব্লক হলে, অথবা আপনার অপারেটরের নিজস্ব কভারেজ না থাকলে এমন দেখায়।",
            category = "Network"
        ),
        FaqItem(
            questionEn = "What should I do if my SIM is blocked with a PUK code?",
            questionBn = "সিম যদি PUK কোড দিয়ে ব্লক হয়ে যায় তাহলে করণীয় কী?",
            answerEn = "Never enter guessed codes! Entering 10 incorrect PUK attempts permanently destroys the SIM card. Contact your mobile operator's helpline or visit their service center with your NID card to retrieve the master PUK or get an instant SIM swap.",
            answerBn = "কখনোই আন্দাজে ভুল কোড টাইপ করবেন না! ১০ বার ভুল PUK দিলে সিম কার্ডটি স্থায়ীভাবে অচল হয়ে যাবে। অন্য ফোন থেকে অপারেটরের হেল্পলাইনে কল করুন অথবা আপনার জাতীয় পরিচয়পত্র নিয়ে কাস্টমার কেয়ারে যোগাযোগ করে PUK উদ্ধার বা নতুন সিম তুলে নিন।",
            category = "SIM"
        ),
        FaqItem(
            questionEn = "What are the standard APN settings for Bangladeshi operators?",
            questionBn = "বাংলাদেশের অপারেটরদের সঠিক APN সেটিংস কী কী?",
            answerEn = "Grameenphone: Name='GP', APN='gpinternet'\nRobi / Airtel: Name='Robi', APN='internet'\nBanglalink: Name='BL', APN='blweb'\nTeletalk: Name='Teletalk', APN='teletalk'\nLeave Username & Password blank, then Save and Restart.",
            answerBn = "গ্রামীণফোন: Name='GP', APN='gpinternet'\nরবি / এয়ারটেল: Name='Robi', APN='internet'\nবাংলালিংক: Name='BL', APN='blweb'\nটেলিটক: Name='Teletalk', APN='teletalk'\nইউজারনেম ও পাসওয়ার্ড খালি রেখে সেভ করে ফোন রিস্টার্ট দিন।",
            category = "APN"
        )
    )

    val repairShopChecklist: List<ChecklistItem> = listOf(
        ChecklistItem(
            id = "chk_imei",
            titleEn = "Verify IMEI Integrity (*#06#)",
            titleBn = "আইএমইআই (IMEI) অখণ্ডতা যাচাই (*#06#)",
            descriptionEn = "Dial *#06#. Confirm 15 digits appear for each active SIM slot. If blank or null, CPU/baseband cannot read modem NVRAM.",
            descriptionBn = "*#06# ডায়াল করে নিশ্চিত হন প্রতিটি স্লটের জন্য ১৫ ডিজিটের সঠিক IMEI আসছে। নাল (Null) বা খালি আসলে বেসব্যান্ড আইসি বা মোডেম ক্ষতিগ্রস্ত।"
        ),
        ChecklistItem(
            id = "chk_baseband",
            titleEn = "Check Baseband Version in Settings",
            titleBn = "সেটিংসে বেসব্যান্ড ভার্সন পরীক্ষা",
            descriptionEn = "Settings -> About Phone -> Android Version -> Baseband Version. If marked 'Unknown', baseband IC or power rails have failed.",
            descriptionBn = "সেটিংস -> About Phone -> Baseband Version দেখুন। 'Unknown' থাকলে বেসব্যান্ড আইসি, পাওয়ার সরবরাহ বা ফার্মওয়্যার নষ্ট।"
        ),
        ChecklistItem(
            id = "chk_sim_pins",
            titleEn = "Physical SIM Connector Inspection",
            titleBn = "সিম সকেটের মেকানিক্যাল পিন পরিদর্শন",
            descriptionEn = "Inspect SIM slot springs under magnifying lamp. Check for flattened, oxidized, or broken pins. Clean with isopropyl alcohol.",
            descriptionBn = "ম্যাগনিফায়ার দিয়ে সিম সকেটের পিনগুলো সোজা আছে কিনা ও মরিচা আছে কিনা পরীক্ষা করুন। আইসোপ্রোপাইল অ্যালকোহল দিয়ে পরিষ্কার করুন।",
            isHardwareCheck = true
        ),
        ChecklistItem(
            id = "chk_coaxial_cable",
            titleEn = "Sub-board RF Antenna Coaxial Cable",
            titleBn = "সাব-বোর্ডের কোঅ্যাক্সিয়াল অ্যান্টেনা ক্যাবল",
            descriptionEn = "Ensure tiny coaxial antenna wire between charging sub-board and main motherboard is firmly connected on both end connectors.",
            descriptionBn = "চার্জিং সাব-বোর্ড থেকে মাদারবোর্ডে আসা ক্ষুদ্র আরএফ অ্যান্টেনা তারটি উভয় মাথায় শক্তভাবে কানেক্টেড আছে কিনা নিশ্চিত করুন।",
            isHardwareCheck = true
        ),
        ChecklistItem(
            id = "chk_rf_pa",
            titleEn = "RF PA (Power Amplifier) & Transceiver",
            titleBn = "আরএফ পাওয়ার অ্যাম্প্লিফায়ার (PA) ও ট্রান্সিভার",
            descriptionEn = "If phone shows full 2G signal but 4G fails completely, PA or band filter hardware failure is likely.",
            descriptionBn = "ফোন ২জি সিগন্যাল পেলেও ৪জি বা ডাটাতে গেলে নো সার্ভিস হলে RF PA বা ব্যান্ড ফিল্টারের হার্ডওয়্যার সমস্যা হওয়ার সম্ভাবনা বেশি।",
            isHardwareCheck = true
        ),
        ChecklistItem(
            id = "chk_sim_cross_test",
            titleEn = "Cross-Device SIM Verification",
            titleBn = "অন্য ফোনে সিম ক্রস-টেস্ট",
            descriptionEn = "Test customer's SIM in a verified technician phone. If it also fails, issue is strictly SIM replacement, not phone hardware.",
            descriptionBn = "গ্রাহকের সিমটি টেকনিশিয়ানের সচল ফোনে টেস্ট করুন। সেখানেও কাজ না করলে সমস্যা ফোনের নয়, কাস্টমার কেয়ার থেকে সিম বদলাতে হবে।"
        )
    )

    val bdCustomerScripts: List<Pair<String, String>> = listOf(
        "কাস্টমারকে সিম নষ্ট হওয়ার কথা বোঝানোর নিয়ম" to
            "গ্রাহককে শান্তভাবে বলুন: 'আপনার সিমের সোনালী চিপের ইন্টারনাল মেমোরি নষ্ট হয়ে গেছে। এটি কোনো সফটওয়্যার বা রিস্টার্ট দিয়ে ঠিক হবে না। আপনার জাতীয় পরিচয়পত্র নিয়ে যেকোনো কাস্টমার কেয়ার থেকে ৫০-১০০ টাকার মধ্যে নতুন সিম তুলে নিলে (সিম সোয়াপ) আপনার একই নম্বর চালু থাকবে এবং সব সমস্যার সমাধান হয়ে যাবে। ফোনে কোনো হাত দেওয়ার প্রয়োজন নেই।'",

        "নো সার্ভিস বা আইসি সমস্যার ক্ষেত্রে গ্রাহককে বোঝানোর নিয়ম" to
            "গ্রাহককে জানান: 'আমরা ডায়াল করে দেখেছি ফোনের আইএমইআই এবং অ্যান্টেনা তার ভালো আছে। আপনার সমস্যাটি মূলত নেটওয়ার্ক আইসি বা রেডিও সিগন্যাল ফিল্টারের। এটি খোলার আগে আমরা সফটওয়্যার রিস্টোর করে দেখবো। নিশ্চিত না হয়ে আমরা মাদারবোর্ডে অপ্রয়োজনীয় কাজ করব না।'"
    )
}
