package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.example.data.ThemeMode
import com.example.data.UserPreferencesRepository
import com.example.ui.MainScreen
import com.example.ui.theme.NetworkFixTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val userPreferencesRepository = UserPreferencesRepository(applicationContext)

        setContent {
            val scope = rememberCoroutineScope()
            val themeMode by userPreferencesRepository.themeFlow.collectAsState(initial = ThemeMode.SYSTEM)
            val isSystemDark = isSystemInDarkTheme()

            val isDark = when (themeMode) {
                ThemeMode.LIGHT -> false
                ThemeMode.DARK -> true
                ThemeMode.SYSTEM -> isSystemDark
            }

            NetworkFixTheme(darkTheme = isDark) {
                MainScreen(
                    userPreferencesRepository = userPreferencesRepository,
                    isDarkTheme = isDark,
                    onToggleTheme = {
                        val nextTheme = if (isDark) ThemeMode.LIGHT else ThemeMode.DARK
                        scope.launch {
                            userPreferencesRepository.setThemeMode(nextTheme)
                        }
                    }
                )
            }
        }
    }
}
