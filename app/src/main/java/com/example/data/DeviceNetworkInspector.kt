package com.example.data

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import androidx.core.content.ContextCompat

data class DeviceNetworkInfo(
    val manufacturer: String,
    val model: String,
    val androidVersion: String,
    val sdkInt: Int,
    val isAirplaneModeOn: Boolean,
    val activeConnectionType: String,
    val isConnected: Boolean,
    val isMetered: Boolean,
    val simStateLabel: String,
    val simStateCode: Int,
    val simSlotCount: Int,
    val carrierName: String?,
    val networkOperatorName: String?,
    val isRoaming: Boolean,
    val hasPhoneStatePermission: Boolean,
    val permissionNote: String
)

class DeviceNetworkInspector(private val context: Context) {

    fun inspect(): DeviceNetworkInfo {
        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as? TelephonyManager
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager

        val hasPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_PHONE_STATE
        ) == PackageManager.PERMISSION_GRANTED

        // Device details
        val manufacturer = Build.MANUFACTURER.replaceFirstChar { it.uppercase() }
        val model = Build.MODEL
        val androidVersion = "Android ${Build.VERSION.RELEASE}"
        val sdkInt = Build.VERSION.SDK_INT

        // Airplane Mode
        val isAirplaneModeOn = try {
            Settings.Global.getInt(context.contentResolver, Settings.Global.AIRPLANE_MODE_ON, 0) != 0
        } catch (_: Exception) {
            false
        }

        // Active Connection via ConnectivityManager
        var activeConnectionType = "No Active Connection"
        var isConnected = false
        var isMetered = false

        if (connectivityManager != null) {
            val activeNetwork = connectivityManager.activeNetwork
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
            if (capabilities != null) {
                isConnected = capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                isMetered = !capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED)

                activeConnectionType = when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> "Wi-Fi (Wireless LAN)"
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        resolveCellularGen(telephonyManager, hasPermission)
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> "Ethernet"
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> "Bluetooth Tethering"
                    else -> "Connected (Other Network)"
                }
            }
        }

        // SIM State and Carrier
        val simStateCode = telephonyManager?.simState ?: TelephonyManager.SIM_STATE_UNKNOWN
        val simStateLabel = resolveSimState(simStateCode)

        val simSlotCount = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            telephonyManager?.activeModemCount ?: 1
        } else {
            telephonyManager?.phoneCount ?: 1
        }

        var carrierName: String? = null
        var networkOperatorName: String? = null
        var isRoaming = false

        if (telephonyManager != null) {
            carrierName = telephonyManager.simOperatorName?.takeIf { it.isNotBlank() }
            networkOperatorName = telephonyManager.networkOperatorName?.takeIf { it.isNotBlank() }
            isRoaming = try { telephonyManager.isNetworkRoaming } catch (_: Exception) { false }
        }

        val permissionNote = if (hasPermission) {
            "Permission granted: Complete SIM & telephony parameters available."
        } else {
            "Optional Phone State permission not granted. Some carrier/SIM details may be restricted by Android."
        }

        return DeviceNetworkInfo(
            manufacturer = manufacturer,
            model = model,
            androidVersion = androidVersion,
            sdkInt = sdkInt,
            isAirplaneModeOn = isAirplaneModeOn,
            activeConnectionType = activeConnectionType,
            isConnected = isConnected,
            isMetered = isMetered,
            simStateLabel = simStateLabel,
            simStateCode = simStateCode,
            simSlotCount = simSlotCount,
            carrierName = carrierName,
            networkOperatorName = networkOperatorName,
            isRoaming = isRoaming,
            hasPhoneStatePermission = hasPermission,
            permissionNote = permissionNote
        )
    }

    private fun resolveSimState(state: Int): String {
        return when (state) {
            TelephonyManager.SIM_STATE_READY -> "SIM Ready & Active"
            TelephonyManager.SIM_STATE_ABSENT -> "No SIM Inserted / Absent"
            TelephonyManager.SIM_STATE_PIN_REQUIRED -> "Locked (PIN Required)"
            TelephonyManager.SIM_STATE_PUK_REQUIRED -> "Locked (PUK Required)"
            TelephonyManager.SIM_STATE_NETWORK_LOCKED -> "Network Locked (Carrier Lock)"
            TelephonyManager.SIM_STATE_NOT_READY -> "SIM Not Ready / Initializing"
            TelephonyManager.SIM_STATE_PERM_DISABLED -> "Permanently Disabled"
            TelephonyManager.SIM_STATE_CARD_IO_ERROR -> "SIM Hardware I/O Error"
            TelephonyManager.SIM_STATE_CARD_RESTRICTED -> "SIM Restricted"
            else -> "Unknown / Undetected"
        }
    }

    @Suppress("DEPRECATION")
    private fun resolveCellularGen(telephonyManager: TelephonyManager?, hasPermission: Boolean): String {
        if (telephonyManager == null) return "Cellular (Mobile Data)"
        return try {
            // General modern display
            "Cellular Mobile Data (LTE / 4G / 5G / 3G)"
        } catch (_: Exception) {
            "Cellular Mobile Data"
        }
    }
}
