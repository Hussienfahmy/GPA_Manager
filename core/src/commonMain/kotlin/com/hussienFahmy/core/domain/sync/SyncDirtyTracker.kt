package com.hussienfahmy.core.domain.sync

import kotlin.concurrent.atomics.AtomicBoolean
import kotlin.concurrent.atomics.ExperimentalAtomicApi

/**
 * Tracks whether each Firebase-synced collection has been modified since its last push.
 * Mutation use cases call the relevant markXChanged(), and sync call sites consume the flag to
 * decide whether (and what) to push.
 */
@OptIn(ExperimentalAtomicApi::class)
class SyncDirtyTracker {
    private val subjectsChanged = AtomicBoolean(false)
    private val settingsChanged = AtomicBoolean(false)
    private val semestersChanged = AtomicBoolean(false)

    fun markSubjectsChanged() {
        subjectsChanged.store(true)
    }

    fun markSettingsChanged() {
        settingsChanged.store(true)
    }

    fun markSemestersChanged() {
        semestersChanged.store(true)
    }

    /** True if any collection changed, without consuming the flags. */
    fun hasAnyChanges(): Boolean =
        subjectsChanged.load() || settingsChanged.load() || semestersChanged.load()

    /** Returns true if subjects changed since the last call, and resets that flag. */
    fun consumeSubjectsChanged(): Boolean = subjectsChanged.exchange(false)

    /** Returns true if settings changed since the last call, and resets that flag. */
    fun consumeSettingsChanged(): Boolean = settingsChanged.exchange(false)

    /** Returns true if semesters changed since the last call, and resets that flag. */
    fun consumeSemestersChanged(): Boolean = semestersChanged.exchange(false)
}
