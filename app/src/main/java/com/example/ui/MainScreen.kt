package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.NetworkCheck
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.SimCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.ads.InterstitialAdManager
import com.example.data.AppLanguage
import com.example.data.DeviceNetworkInspector
import com.example.data.ThemeMode
import com.example.data.UserPreferencesRepository
import com.example.ui.components.NavHeader
import com.example.ui.screens.AdMobGuideScreen
import com.example.ui.screens.DeviceNetworkInfoScreen
import com.example.ui.screens.FaqScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.NetworkTroubleshootScreen
import com.example.ui.screens.PrivacyPolicyScreen
import com.example.ui.screens.RepairGuideScreen
import com.example.ui.screens.RepairShopScreen
import com.example.ui.screens.SettingsShortcutsScreen
import com.example.ui.screens.SimTroubleshootScreen
import kotlinx.coroutines.launch

enum class ScreenRoute {
    HOME,
    SIM_TROUBLESHOOT,
    NETWORK_TROUBLESHOOT,
    DEVICE_INFO,
    SETTINGS_SHORTCUTS,
    REPAIR_SHOP,
    REPAIR_GUIDE,
    FAQS,
    PRIVACY_POLICY,
    ADMOB_GUIDE
}

@Composable
fun MainScreen(
    userPreferencesRepository: UserPreferencesRepository,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val currentLanguage by userPreferencesRepository.languageFlow.collectAsState(initial = AppLanguage.EN)
    val inspector = remember { DeviceNetworkInspector(context) }
    var deviceInfo by remember { mutableStateOf(inspector.inspect()) }
    val interstitialAdManager = remember {
        InterstitialAdManager(context).apply { loadAd() }
    }

    var currentScreen by remember { mutableStateOf(ScreenRoute.HOME) }
    val screenStack = remember { mutableListOf<ScreenRoute>() }

    fun navigateTo(route: ScreenRoute) {
        if (currentScreen != route) {
            screenStack.add(currentScreen)
            currentScreen = route
        }
    }

    fun navigateBack() {
        if (screenStack.isNotEmpty()) {
            currentScreen = screenStack.removeAt(screenStack.size - 1)
        } else {
            currentScreen = ScreenRoute.HOME
        }
    }

    // Handle Android system back gesture / back button
    BackHandler(enabled = currentScreen != ScreenRoute.HOME) {
        navigateBack()
    }

    val pageTitle = when (currentScreen) {
        ScreenRoute.HOME -> "Network Fix BD"
        ScreenRoute.SIM_TROUBLESHOOT -> if (currentLanguage == AppLanguage.EN) "SIM Troubleshooting" else "সিম সমস্যা সমাধান"
        ScreenRoute.NETWORK_TROUBLESHOOT -> if (currentLanguage == AppLanguage.EN) "Network & Signal" else "নেটওয়ার্ক ও সিগন্যাল"
        ScreenRoute.DEVICE_INFO -> if (currentLanguage == AppLanguage.EN) "Device Network Info" else "ডিভাইস ও নেটওয়ার্ক তথ্য"
        ScreenRoute.SETTINGS_SHORTCUTS -> if (currentLanguage == AppLanguage.EN) "Network Settings" else "নেটওয়ার্ক সেটিংস শর্টকাট"
        ScreenRoute.REPAIR_SHOP -> if (currentLanguage == AppLanguage.EN) "Repair Shop Mode" else "সার্ভিসিং ও টেকনিশিয়ান"
        ScreenRoute.REPAIR_GUIDE -> if (currentLanguage == AppLanguage.EN) "Step-by-Step Repair" else "ধাপে ধাপে মেরামত"
        ScreenRoute.FAQS -> if (currentLanguage == AppLanguage.EN) "Frequently Asked Questions" else "সাধারণ প্রশ্নোত্তর"
        ScreenRoute.PRIVACY_POLICY -> if (currentLanguage == AppLanguage.EN) "Privacy & Security" else "গোপনীয়তা নীতি"
        ScreenRoute.ADMOB_GUIDE -> if (currentLanguage == AppLanguage.EN) "AdMob Production Guide" else "অ্যাডমব সেটআপ গাইড"
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            NavHeader(
                title = pageTitle,
                currentLanguage = currentLanguage,
                isDarkTheme = isDarkTheme,
                canNavigateBack = currentScreen != ScreenRoute.HOME,
                onNavigateBack = { navigateBack() },
                onToggleLanguage = {
                    val nextLang = if (currentLanguage == AppLanguage.EN) AppLanguage.BN else AppLanguage.EN
                    scope.launch { userPreferencesRepository.setLanguage(nextLang) }
                },
                onToggleTheme = onToggleTheme
            )
        },
        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .navigationBarsPadding()
                    .testTag("main_bottom_nav"),
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ) {
                // Home
                NavigationBarItem(
                    selected = currentScreen == ScreenRoute.HOME,
                    onClick = {
                        screenStack.clear()
                        currentScreen = ScreenRoute.HOME
                    },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text(if (currentLanguage == AppLanguage.EN) "Home" else "হোম", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                    modifier = Modifier.testTag("nav_item_home")
                )

                // SIM
                NavigationBarItem(
                    selected = currentScreen == ScreenRoute.SIM_TROUBLESHOOT,
                    onClick = { navigateTo(ScreenRoute.SIM_TROUBLESHOOT) },
                    icon = { Icon(Icons.Default.SimCard, contentDescription = "SIM") },
                    label = { Text(if (currentLanguage == AppLanguage.EN) "SIM Fix" else "সিম", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                    modifier = Modifier.testTag("nav_item_sim")
                )

                // Network
                NavigationBarItem(
                    selected = currentScreen == ScreenRoute.NETWORK_TROUBLESHOOT,
                    onClick = { navigateTo(ScreenRoute.NETWORK_TROUBLESHOOT) },
                    icon = { Icon(Icons.Default.NetworkCheck, contentDescription = "Network") },
                    label = { Text(if (currentLanguage == AppLanguage.EN) "Network" else "নেটওয়ার্ক", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                    modifier = Modifier.testTag("nav_item_network")
                )

                // Repair Shop
                NavigationBarItem(
                    selected = currentScreen == ScreenRoute.REPAIR_SHOP,
                    onClick = { navigateTo(ScreenRoute.REPAIR_SHOP) },
                    icon = { Icon(Icons.Default.Build, contentDescription = "Shop Mode") },
                    label = { Text(if (currentLanguage == AppLanguage.EN) "Repair Shop" else "সার্ভিসিং", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                    modifier = Modifier.testTag("nav_item_repair_shop")
                )

                // Privacy
                NavigationBarItem(
                    selected = currentScreen == ScreenRoute.PRIVACY_POLICY,
                    onClick = { navigateTo(ScreenRoute.PRIVACY_POLICY) },
                    icon = { Icon(Icons.Default.Policy, contentDescription = "Privacy") },
                    label = { Text(if (currentLanguage == AppLanguage.EN) "Privacy" else "পলিসি", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                    modifier = Modifier.testTag("nav_item_privacy")
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                ScreenRoute.HOME -> HomeScreen(
                    deviceInfo = deviceInfo,
                    language = currentLanguage,
                    onRefreshInfo = { deviceInfo = inspector.inspect() },
                    onNavigateToRoute = { routeStr ->
                        when (routeStr) {
                            "sim_troubleshoot" -> navigateTo(ScreenRoute.SIM_TROUBLESHOOT)
                            "network_troubleshoot" -> navigateTo(ScreenRoute.NETWORK_TROUBLESHOOT)
                            "device_info" -> navigateTo(ScreenRoute.DEVICE_INFO)
                            "settings_shortcuts" -> navigateTo(ScreenRoute.SETTINGS_SHORTCUTS)
                            "repair_shop" -> navigateTo(ScreenRoute.REPAIR_SHOP)
                            "repair_guide" -> navigateTo(ScreenRoute.REPAIR_GUIDE)
                            "faqs" -> navigateTo(ScreenRoute.FAQS)
                            else -> navigateTo(ScreenRoute.HOME)
                        }
                    }
                )

                ScreenRoute.SIM_TROUBLESHOOT -> SimTroubleshootScreen(
                    language = currentLanguage,
                    onOpenSettingsShortcut = { navigateTo(ScreenRoute.SETTINGS_SHORTCUTS) }
                )

                ScreenRoute.NETWORK_TROUBLESHOOT -> NetworkTroubleshootScreen(
                    language = currentLanguage,
                    onOpenSettingsShortcut = { navigateTo(ScreenRoute.SETTINGS_SHORTCUTS) }
                )

                ScreenRoute.DEVICE_INFO -> DeviceNetworkInfoScreen(
                    info = deviceInfo,
                    language = currentLanguage,
                    onRefresh = { deviceInfo = inspector.inspect() }
                )

                ScreenRoute.SETTINGS_SHORTCUTS -> SettingsShortcutsScreen(
                    language = currentLanguage
                )

                ScreenRoute.REPAIR_SHOP -> RepairShopScreen(
                    language = currentLanguage
                )

                ScreenRoute.REPAIR_GUIDE -> RepairGuideScreen(
                    language = currentLanguage,
                    interstitialAdManager = interstitialAdManager,
                    onOpenSettingsShortcut = { navigateTo(ScreenRoute.SETTINGS_SHORTCUTS) }
                )

                ScreenRoute.FAQS -> FaqScreen(
                    language = currentLanguage
                )

                ScreenRoute.PRIVACY_POLICY -> PrivacyPolicyScreen(
                    language = currentLanguage,
                    onOpenAdMobGuide = { navigateTo(ScreenRoute.ADMOB_GUIDE) }
                )

                ScreenRoute.ADMOB_GUIDE -> AdMobGuideScreen(
                    language = currentLanguage
                )
            }
        }
    }
}
