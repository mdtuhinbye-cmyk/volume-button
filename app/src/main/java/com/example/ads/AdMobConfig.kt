package com.example.ads

/**
 * AdMob Configuration for Network Fix BD.
 *
 * EXACT USER AD UNIT IDs (Account: ca-app-pub-5439242993555137):
 * Unit 1: ca-app-pub-5439242993555137/4384375166
 * Unit 2: ca-app-pub-5439242993555137/5973692960
 * Unit 3: ca-app-pub-5439242993555137/9145002390
 *
 * NOTE: The fourth ID previously provided was identical to Unit 3; duplicates are omitted.
 *
 * ADMOB POLICY & FORMAT RULES:
 * 1. Do NOT guess ad formats: Unit formats (Banner vs Interstitial vs Native) are defined
 *    in your Google AdMob console. Test each unit with its verified format below.
 * 2. Keep AdMob App ID separate from Ad Unit IDs.
 * 3. Never invent an App ID. During development, the official Google Test App ID is used.
 * 4. Test Ads must be used during development to avoid account suspension for invalid traffic.
 */
object AdMobConfig {

    // Toggle for Development / Testing vs Production Live Ads
    // Set to true by default to ensure policy safety during testing.
    var isTestMode: Boolean = true

    // Official Google Test App ID & Ad Unit IDs
    const val TEST_APP_ID = "ca-app-pub-3940256099942544~3347511713"
    const val TEST_BANNER_AD_UNIT_ID = "ca-app-pub-3940256099942544/6300978111"
    const val TEST_INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712"

    // Exact Production Ad Unit IDs provided by the user
    const val PROD_AD_UNIT_1 = "ca-app-pub-5439242993555137/4384375166"
    const val PROD_AD_UNIT_2 = "ca-app-pub-5439242993555137/5973692960"
    const val PROD_AD_UNIT_3 = "ca-app-pub-5439242993555137/9145002390"

    // Production App ID slot (Separate from Ad Unit IDs — do not invent; fill from AdMob console)
    // Format: ca-app-pub-5439242993555137~XXXXXXXXXX
    const val PROD_APP_ID_PLACEHOLDER = "ca-app-pub-5439242993555137~YOUR_APP_ID_HERE"

    // Default Placements (can be reassigned if your AdMob console defines different formats)
    // By convention in AdMob dashboards:
    // Slot 1 is typically configured as Banner
    // Slot 2 is typically configured as Interstitial
    // Slot 3 is typically configured as Secondary Banner / Interstitial
    var bannerAdUnitId: String = PROD_AD_UNIT_1
    var interstitialAdUnitId: String = PROD_AD_UNIT_2
    var secondaryAdUnitId: String = PROD_AD_UNIT_3

    fun getActiveBannerAdUnitId(): String {
        return if (isTestMode) TEST_BANNER_AD_UNIT_ID else bannerAdUnitId
    }

    fun getActiveInterstitialAdUnitId(): String {
        return if (isTestMode) TEST_INTERSTITIAL_AD_UNIT_ID else interstitialAdUnitId
    }

    fun getAllProductionUnits(): List<Pair<String, String>> {
        return listOf(
            "Ad Unit 1 (Primary Banner Slot)" to PROD_AD_UNIT_1,
            "Ad Unit 2 (Interstitial Slot)" to PROD_AD_UNIT_2,
            "Ad Unit 3 (Secondary Slot)" to PROD_AD_UNIT_3
        )
    }
}
