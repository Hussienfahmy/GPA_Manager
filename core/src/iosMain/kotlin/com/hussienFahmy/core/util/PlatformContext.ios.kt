package com.hussienfahmy.core.util

// No iOS equivalent of Android's Context - empty placeholder, instantiated via an anonymous
// subclass (object : PlatformContext() {}) in KoinIos.kt's Koin single, so every :core class can
// take PlatformContext uniformly across platforms.
actual abstract class PlatformContext
