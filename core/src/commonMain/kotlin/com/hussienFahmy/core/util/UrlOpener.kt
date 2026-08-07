package com.hussienfahmy.core.util

expect class UrlOpener(context: PlatformContext) {
    /** Opens [url] in the platform's default browser/handler. */
    fun open(url: String)
}
