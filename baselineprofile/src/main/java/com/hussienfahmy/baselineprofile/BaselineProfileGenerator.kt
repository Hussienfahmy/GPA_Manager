package com.hussienfahmy.baselineprofile

import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Until
import org.junit.Rule
import org.junit.Test

/**
 * Generates the baseline profile bundled into the release AAB (assets/dexopt/baseline.prof).
 * Run with the "generateBaselineProfile" Gradle task against a physical device or an
 * API 28+ emulator with Google Play image (rooted emulators don't support profile capture).
 *
 * The [markScreen] journey requires the tester to already be signed in on the target device
 * for the app's "benchmark" build type (sign in once manually, the session persists across
 * profile-generation runs since the app isn't reinstalled clean) — Firebase Auth can't be
 * driven headlessly here, so cold-start-only signed-out runs will only cover the onboarding
 * flow, not Semester Marks.
 */
class BaselineProfileGenerator {

    @get:Rule
    val baselineProfileRule = BaselineProfileRule()

    @Test
    fun startup() = baselineProfileRule.collect(
        packageName = TARGET_PACKAGE
    ) {
        pressHome()
        startActivityAndWait()
    }

    @Test
    fun markScreen() = baselineProfileRule.collect(
        packageName = TARGET_PACKAGE
    ) {
        pressHome()
        startActivityAndWait()

        // Onboarding shows for signed-out users; nothing further to profile without auth.
        val marksTab = device.wait(Until.findObject(By.text("Marks")), 5_000) ?: return@collect
        marksTab.click()
        device.waitForIdle()
    }

    private companion object {
        const val TARGET_PACKAGE = "com.hussienFahmy.myGpaManager"
    }
}
