package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.ads.AdMobConfig
import com.example.data.AppLanguage
import com.example.data.DeviceNetworkInspector
import com.example.data.GuideCategory
import com.example.data.TroubleshootingRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleUnitTest {

    @Test
    fun `read string from context matches Network Fix BD`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Network Fix BD", appName)
    }

    @Test
    fun `troubleshooting guides repository is populated with SIM and Network categories`() {
        val guides = TroubleshootingRepository.guides
        assertTrue("Guides must not be empty", guides.isNotEmpty())

        val simGuides = guides.filter { it.category == GuideCategory.SIM }
        assertTrue("SIM guides must be present", simGuides.isNotEmpty())

        val networkGuides = guides.filter { it.category == GuideCategory.NETWORK }
        assertTrue("Network guides must be present", networkGuides.isNotEmpty())

        // Ensure every guide has both English and Bangla instructions
        guides.forEach { guide ->
            assertNotNull(guide.titleEn)
            assertNotNull(guide.titleBn)
            assertTrue("Guide ${guide.id} must have English steps", guide.stepsEn.isNotEmpty())
            assertTrue("Guide ${guide.id} must have Bangla steps", guide.stepsBn.isNotEmpty())
        }
    }

    @Test
    fun `admob config provides test ad unit IDs in development mode`() {
        assertTrue(AdMobConfig.isTestMode)
        val bannerId = AdMobConfig.getActiveBannerAdUnitId()
        assertEquals(AdMobConfig.TEST_BANNER_AD_UNIT_ID, bannerId)
        val interstitialId = AdMobConfig.getActiveInterstitialAdUnitId()
        assertEquals(AdMobConfig.TEST_INTERSTITIAL_AD_UNIT_ID, interstitialId)

        // Verify exact user production ad units
        assertEquals("ca-app-pub-5439242993555137/4384375166", AdMobConfig.PROD_AD_UNIT_1)
        assertEquals("ca-app-pub-5439242993555137/5973692960", AdMobConfig.PROD_AD_UNIT_2)
        assertEquals("ca-app-pub-5439242993555137/9145002390", AdMobConfig.PROD_AD_UNIT_3)
    }

    @Test
    fun `device network inspector returns valid telemetry`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val inspector = DeviceNetworkInspector(context)
        val info = inspector.inspect()

        assertNotNull(info.manufacturer)
        assertNotNull(info.model)
        assertNotNull(info.androidVersion)
        assertNotNull(info.activeConnectionType)
        assertNotNull(info.simStateLabel)
    }
}
